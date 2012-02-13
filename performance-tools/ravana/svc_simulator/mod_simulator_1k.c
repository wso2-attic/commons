/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <httpd.h>
#include <http_config.h>
#include <http_log.h>
#include <http_protocol.h>
#include <ap_config.h>
#include <apr_strings.h>

#define SVC_SIMULATOR_HTTP_HEADER_CONTENT_TYPE "Content-Type"
#define SVC_SIMULATOR_HTTP_HEADER_ACCEPT_TEXT_XML "text/xml"
#define SVC_SIMULATOR_HTTP_HEADER_ACCEPT_TEXT_PLAIN "text/plain"
#define SVC_SIMULATOR_HTTP_HEADER_SERVER_SVC_SIMULATOR "Service Simulator Version 1.0"
#define SVC_SIMULATOR_ESC_NULL '\0'
#define SVC_SIMULATOR_FILE_READ_SIZE 2048

/* Configuration structure populated by apache2.conf */
typedef struct svc_simulator_1k_config_rec
{
    char *filepath;
} svc_simulator_1k_config_rec_t;

typedef struct svc_simulator_1k_worker
{
    char *filepath;
    char *bodystring;
    int bodystring_len;
} svc_simulator_1k_worker_t;

svc_simulator_1k_worker_t *simulator_worker = NULL;

/******************************Function Headers********************************/
static void *
svc_simulator_1k_create_svr(
    apr_pool_t * p,
    server_rec * s);

static const char *
svc_simulator_1k_set_filepath(
    cmd_parms * cmd,
    void *dummy,
    const char *arg);

static int
svc_simulator_1k_handler(
    request_rec * req);

svc_simulator_1k_worker_t *
svc_simulator_1k_worker_create(
    apr_pool_t * p,
    char * filepath);

static int
svc_simulator_1k_process_request(
    request_rec * request);

static char *
svc_simulator_1k_get_reply(
    apr_pool_t * pool);

static void
svc_simulator_1k_module_init(
    apr_pool_t * p,
    server_rec * svr_rec);

static void
svc_simulator_1k_register_hooks(
    apr_pool_t * p);

/***************************End of Function Headers****************************/

static const command_rec svc_simulator_1k_cmds[] = { AP_INIT_TAKE1("SvcSimulatorInputFile_1k", svc_simulator_1k_set_filepath, NULL,
    RSRC_CONF, "Service Simulator file path"), { NULL } };

/* Dispatch list for API hooks */
module AP_MODULE_DECLARE_DATA svc_simulator_1k_module = { STANDARD20_MODULE_STUFF, NULL, /* create per-dir    config structures */
NULL, /* merge  per-dir    config structures */
svc_simulator_1k_create_svr, /* create per-server config structures */
NULL, /* merge  per-server config structures */
svc_simulator_1k_cmds, /* table of config file commands       */
svc_simulator_1k_register_hooks /* register hooks                      */
};

static void *
svc_simulator_1k_create_svr(
    apr_pool_t * p,
    server_rec * s)
{
    svc_simulator_1k_config_rec_t *conf = apr_palloc(p, sizeof(*conf));
    conf->filepath = NULL;
    return conf;
}

static const char *
svc_simulator_1k_set_filepath(
    cmd_parms * cmd,
    void *dummy,
    const char *arg)
{
    apr_status_t status;
    svc_simulator_1k_config_rec_t *conf = NULL;
    const char *err = ap_check_cmd_context(cmd, GLOBAL_ONLY);
    if(err != NULL)
    {
        return err;
    }
    conf = (svc_simulator_1k_config_rec_t *)ap_get_module_config(cmd->server->module_config, &svc_simulator_1k_module);
    conf->filepath = apr_pstrdup(cmd->pool, arg);
    return NULL;
}

/* The sample content handler */
static int
svc_simulator_1k_handler(
    request_rec * req)
{
    int rv = 0;

    /*ap_log_rerror(APLOG_MARK, APLOG_EMERG, 0, req, "[svc_simulator_1k] damcame1");*/
    if(strcmp(req->handler, "svc_simulator_1k_module"))
    {
        return DECLINED;
    }

    /* Set up the read policy from the client. */
    if((rv = ap_setup_client_block(req, REQUEST_CHUNKED_DECHUNK)) != OK)
    {
        return rv;
    }
    ap_should_client_block(req);

    rv = svc_simulator_1k_process_request(req);
    if(DECLINED == rv)
    {
        return HTTP_INTERNAL_SERVER_ERROR;
    }

    return rv;
}

static int
svc_simulator_1k_post_config(
    apr_pool_t *pconf,
    apr_pool_t *plog,
    apr_pool_t *ptemp,
    server_rec *svr_rec)
{
    apr_status_t status = APR_SUCCESS;
    void *data = NULL;
    const char *userdata_key = "svc_simulator_1k_init";
    svc_simulator_1k_config_rec_t *conf = (svc_simulator_1k_config_rec_t *)ap_get_module_config(
            svr_rec->module_config, &svc_simulator_1k_module);

    /* svc_simulator_1k_post_config() will be called twice. Don't bother
     * going through all of the initialization on the first call
     * because it will just be thrown away.*/

    ap_add_version_component(pconf, SVC_SIMULATOR_HTTP_HEADER_SERVER_SVC_SIMULATOR);

    apr_pool_userdata_get(&data, userdata_key, svr_rec->process->pool);
    if(!data)
    {
        apr_pool_userdata_set((const void *)1, userdata_key, apr_pool_cleanup_null,
            svr_rec->process->pool);
        return OK;
    }

    return OK;
}

static void
svc_simulator_1k_module_init(
    apr_pool_t * p,
    server_rec * svr_rec)
{
    apr_pool_t *pool;
    apr_status_t status;
    svc_simulator_1k_config_rec_t *conf = (svc_simulator_1k_config_rec_t*)ap_get_module_config(
            svr_rec->module_config, &svc_simulator_1k_module);

    status = apr_pool_create(&pool, p);
    if(status)
    {
        ap_log_error(APLOG_MARK, APLOG_EMERG, status, svr_rec,
            "[Axis2] Error allocating mod_svc_simulator_1k memory pool");
        exit(APEXIT_CHILDFATAL);
    }

    simulator_worker = svc_simulator_1k_worker_create(pool, conf->filepath);
    if(!simulator_worker)
    {
        ap_log_error(APLOG_MARK, APLOG_EMERG, APR_EGENERAL, svr_rec,
            "[svc_simulator_1k] Error creating mod_svc_simulator_1k worker;filepath:%s", conf->filepath);
        exit(APEXIT_CHILDFATAL);
    }
}

static void
svc_simulator_1k_register_hooks(
    apr_pool_t * p)
{
    ap_hook_post_config(svc_simulator_1k_post_config, NULL, NULL, APR_HOOK_MIDDLE);
    ap_hook_handler(svc_simulator_1k_handler, NULL, NULL, APR_HOOK_MIDDLE);
    ap_hook_child_init(svc_simulator_1k_module_init, NULL, NULL, APR_HOOK_MIDDLE);
}

svc_simulator_1k_worker_t *
svc_simulator_1k_worker_create(
    apr_pool_t * pool,
    char * filepath)
{
    svc_simulator_1k_worker_t *worker = NULL;
    worker = (svc_simulator_1k_worker_t *) apr_palloc(pool, sizeof(svc_simulator_1k_worker_t));

    if(!worker)
    {
        return NULL;
    }

    worker->filepath = filepath;

    if(!worker->filepath)
    {
        return NULL;
    }
    
    worker->bodystring = svc_simulator_1k_get_reply(pool);
    worker->bodystring_len = strlen(worker->bodystring);

    return worker;
}


static int
svc_simulator_1k_process_request(
    request_rec * request)
{
    char *http_version = NULL;
    int content_length = -1;
    unsigned int body_string_len = 0;
    int send_status = DECLINED;
    char *content_type = NULL;
    /*char request_url[128];*/

    /*sprintf(request_url, "http://%s:%d%s", request->hostname, request->parsed_uri.port, request->unparsed_uri);
    if(!request_url)
    {
        ap_log_rerror(APLOG_MARK, APLOG_EMERG, 0, request, "[svc_simulator_1k] request url:%s", request_url);
    }*/
    content_length = (int)request->remaining;
    /* We are sure that the difference lies within the int range */
    http_version = request->protocol;

    content_type = (char *)apr_table_get(request->headers_in,
        SVC_SIMULATOR_HTTP_HEADER_CONTENT_TYPE);
    if(!content_type)
    {
        content_type = SVC_SIMULATOR_HTTP_HEADER_ACCEPT_TEXT_PLAIN;
    }
    request->content_type = content_type;

    if(request->read_chunked == 1 && 0 == content_length)
    {
        content_length = -1;
        request->chunked = 1;
    }
    if(!http_version)
    {
        return DECLINED;
    }

    /*request->content_type = SVC_SIMULATOR_HTTP_HEADER_ACCEPT_TEXT_XML;*/


    send_status = OK;

    ap_rwrite(simulator_worker->bodystring, simulator_worker->bodystring_len, request);

    return send_status;
}

static char *
svc_simulator_1k_get_reply(
    apr_pool_t * pool)
{
    char *bodystring = NULL;
    /*char *path = NULL;
    char **url_tok = NULL;
    unsigned int len = 0;

    path = simulator_worker->filepath;

    if(path)
    {
        FILE *reply_file = NULL;
        char *content = NULL;
        int c;
        int size = SVC_SIMULATOR_FILE_READ_SIZE;
        char *tmp;
        int i = 0;

        content = (char *) apr_palloc(pool, size);
        reply_file = fopen(path, "r");
        if(reply_file)
        {
            c = fgetc(reply_file);
            while(c != EOF)
            {
                if(i >= size)
                {
                    size = size * 3;
                    tmp = (char *) apr_palloc(pool, size);
                    memcpy(tmp, content, i);
                    free(content);
                    content = tmp;
                }
                content[i++] = (char)c;
                c = fgetc(reply_file);
            }
            content[i] = SVC_SIMULATOR_ESC_NULL;
            bodystring = (char *)content;
			fclose(reply_file);
        }
    }
    else
    {
        bodystring = strdup("Unable to retrieve reply");
    }*/

    /*bodystring = "";*/
    bodystring = "<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"></soapenv:Header><soapenv:Body><m:buyStocks xmlns:m=\"http://po.services.core.carbon.wso2.org\"><m:order><m:symbol>IBM</m:symbol><m:buyerID>asankha</m:buyerID><m:price>140.34</m:price><m:volume>200000</m:volume></m:order><m:order><m:symbol>OCUANJ</m:symbol><m:buyerID>BRNSTD</m:buyerID><m:price>609515.87</m:price><m:volume>934549</m:volume></m:order><m:order><m:symbol>JAJCWP</m:symbol><m:buyerID>XHPOJY</m:buyerID><m:price>301801.66</m:price><m:volume>803810</m:volume></m:order><m:order><m:symbol>YWJOWP</m:symbol><m:buyerID>PZLUNM</m:buyerID><m:price>537220.87</m:price><m:volume>130034</m:volume></m:order><m:order><m:symbol>CBAMRH</m:symbol><m:buyerID>QSKGFY</m:buyerID><m:price>306485.91</m:price><m:volume>350005</m:volume></m:order><m:order><m:symbol>RDFPUS</m:symbol><m:buyerID>NVIBXN</m:buyerID><m:price>465499.69</m:price><m:volume>070510</m:volume></m:order><m:order><m:symbol>QGLQOJ</m:symbol><m:buyerID>JVASPB</m:buyerID><m:price>094155.60</m:price><m:volume>041415</m:volume></m:order></m:buyStocks></soapenv:Body></soapenv:Envelope>";
    return (char *) apr_pstrdup(pool, bodystring);
}

