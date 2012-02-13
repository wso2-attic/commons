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
typedef struct svc_simulator_10k_config_rec
{
    char *filepath;
} svc_simulator_10k_config_rec_t;

typedef struct svc_simulator_10k_worker
{
    char *filepath;
    char *bodystring;
    int bodystring_len;
} svc_simulator_10k_worker_t;

svc_simulator_10k_worker_t *simulator_worker = NULL;

/******************************Function Headers********************************/
static void *
svc_simulator_10k_create_svr(
    apr_pool_t * p,
    server_rec * s);

static const char *
svc_simulator_10k_set_filepath(
    cmd_parms * cmd,
    void *dummy,
    const char *arg);

static int
svc_simulator_10k_handler(
    request_rec * req);

svc_simulator_10k_worker_t *
svc_simulator_10k_worker_create(
    apr_pool_t * p,
    char * filepath);

static int
svc_simulator_10k_process_request(
    request_rec * request);

static char *
svc_simulator_10k_get_reply(
    apr_pool_t * pool);

static void
svc_simulator_10k_module_init(
    apr_pool_t * p,
    server_rec * svr_rec);

static void
svc_simulator_10k_register_hooks(
    apr_pool_t * p);

/***************************End of Function Headers****************************/

static const command_rec svc_simulator_10k_cmds[] = { AP_INIT_TAKE1("SvcSimulatorInputFile_10k", svc_simulator_10k_set_filepath, NULL,
    RSRC_CONF, "Service Simulator file path"), { NULL } };

/* Dispatch list for API hooks */
module AP_MODULE_DECLARE_DATA svc_simulator_10k_module = { STANDARD20_MODULE_STUFF, NULL, /* create per-dir    config structures */
NULL, /* merge  per-dir    config structures */
svc_simulator_10k_create_svr, /* create per-server config structures */
NULL, /* merge  per-server config structures */
svc_simulator_10k_cmds, /* table of config file commands       */
svc_simulator_10k_register_hooks /* register hooks                      */
};

static void *
svc_simulator_10k_create_svr(
    apr_pool_t * p,
    server_rec * s)
{
    svc_simulator_10k_config_rec_t *conf = apr_palloc(p, sizeof(*conf));
    conf->filepath = NULL;
    return conf;
}

static const char *
svc_simulator_10k_set_filepath(
    cmd_parms * cmd,
    void *dummy,
    const char *arg)
{
    apr_status_t status;
    svc_simulator_10k_config_rec_t *conf = NULL;
    const char *err = ap_check_cmd_context(cmd, GLOBAL_ONLY);
    if(err != NULL)
    {
        return err;
    }
    conf = (svc_simulator_10k_config_rec_t *)ap_get_module_config(cmd->server->module_config, &svc_simulator_10k_module);
    conf->filepath = apr_pstrdup(cmd->pool, arg);
    return NULL;
}

/* The sample content handler */
static int
svc_simulator_10k_handler(
    request_rec * req)
{
    int rv = 0;

    /*ap_log_rerror(APLOG_MARK, APLOG_EMERG, 0, req, "[svc_simulator_10k] damcame1");*/
    if(strcmp(req->handler, "svc_simulator_10k_module"))
    {
        return DECLINED;
    }

    /* Set up the read policy from the client. */
    if((rv = ap_setup_client_block(req, REQUEST_CHUNKED_DECHUNK)) != OK)
    {
        return rv;
    }
    ap_should_client_block(req);

    rv = svc_simulator_10k_process_request(req);
    if(DECLINED == rv)
    {
        return HTTP_INTERNAL_SERVER_ERROR;
    }

    return rv;
}

static int
svc_simulator_10k_post_config(
    apr_pool_t *pconf,
    apr_pool_t *plog,
    apr_pool_t *ptemp,
    server_rec *svr_rec)
{
    apr_status_t status = APR_SUCCESS;
    void *data = NULL;
    const char *userdata_key = "svc_simulator_10k_init";
    svc_simulator_10k_config_rec_t *conf = (svc_simulator_10k_config_rec_t *)ap_get_module_config(
            svr_rec->module_config, &svc_simulator_10k_module);

    /* svc_simulator_10k_post_config() will be called twice. Don't bother
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
svc_simulator_10k_module_init(
    apr_pool_t * p,
    server_rec * svr_rec)
{
    apr_pool_t *pool;
    apr_status_t status;
    svc_simulator_10k_config_rec_t *conf = (svc_simulator_10k_config_rec_t*)ap_get_module_config(
            svr_rec->module_config, &svc_simulator_10k_module);

    status = apr_pool_create(&pool, p);
    if(status)
    {
        ap_log_error(APLOG_MARK, APLOG_EMERG, status, svr_rec,
            "[Axis2] Error allocating mod_svc_simulator_10k memory pool");
        exit(APEXIT_CHILDFATAL);
    }

    simulator_worker = svc_simulator_10k_worker_create(pool, conf->filepath);
    if(!simulator_worker)
    {
        ap_log_error(APLOG_MARK, APLOG_EMERG, APR_EGENERAL, svr_rec,
            "[svc_simulator_10k] Error creating mod_svc_simulator_10k worker;filepath:%s", conf->filepath);
        exit(APEXIT_CHILDFATAL);
    }
}

static void
svc_simulator_10k_register_hooks(
    apr_pool_t * p)
{
    ap_hook_post_config(svc_simulator_10k_post_config, NULL, NULL, APR_HOOK_MIDDLE);
    ap_hook_handler(svc_simulator_10k_handler, NULL, NULL, APR_HOOK_MIDDLE);
    ap_hook_child_init(svc_simulator_10k_module_init, NULL, NULL, APR_HOOK_MIDDLE);
}

svc_simulator_10k_worker_t *
svc_simulator_10k_worker_create(
    apr_pool_t * pool,
    char * filepath)
{
    svc_simulator_10k_worker_t *worker = NULL;
    worker = (svc_simulator_10k_worker_t *) apr_palloc(pool, sizeof(svc_simulator_10k_worker_t));

    if(!worker)
    {
        return NULL;
    }

    worker->filepath = filepath;

    if(!worker->filepath)
    {
        return NULL;
    }
    
    worker->bodystring = svc_simulator_10k_get_reply(pool);
    worker->bodystring_len = strlen(worker->bodystring);

    return worker;
}


static int
svc_simulator_10k_process_request(
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
        ap_log_rerror(APLOG_MARK, APLOG_EMERG, 0, request, "[svc_simulator_10k] request url:%s", request_url);
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

    /*ap_log_rerror(APLOG_MARK, APLOG_EMERG, 0, request, "[svc_simulator_10k] bodystring:%s", simulator_worker->bodystring);*/
    ap_rwrite(simulator_worker->bodystring, simulator_worker->bodystring_len, request);

    return send_status;
}

static char *
svc_simulator_10k_get_reply(
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

    bodystring="<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"></soapenv:Header><soapenv:Body><m:buyStocks xmlns:m=\"http://po.services.core.carbon.wso2.org\"><m:order><m:symbol>IBM</m:symbol><m:buyerID>asankha</m:buyerID><m:price>140.34</m:price><m:volume>200000</m:volume></m:order><m:order><m:symbol>TGTBAA</m:symbol><m:buyerID>XDXKTE</m:buyerID><m:price>919411.08</m:price><m:volume>077522</m:volume></m:order><m:order><m:symbol>UVLKOR</m:symbol><m:buyerID>IYSLPA</m:buyerID><m:price>006867.14</m:price><m:volume>967602</m:volume></m:order><m:order><m:symbol>GTBHDW</m:symbol><m:buyerID>FRGRIS</m:buyerID><m:price>453076.68</m:price><m:volume>688500</m:volume></m:order><m:order><m:symbol>PXPQUK</m:symbol><m:buyerID>PPIKPM</m:buyerID><m:price>106803.06</m:price><m:volume>425930</m:volume></m:order><m:order><m:symbol>AEZHUM</m:symbol><m:buyerID>IBRVOT</m:buyerID><m:price>702090.46</m:price><m:volume>801480</m:volume></m:order><m:order><m:symbol>FBZDIE</m:symbol><m:buyerID>KWSDHG</m:buyerID><m:price>628780.33</m:price><m:volume>660965</m:volume></m:order><m:order><m:symbol>LKQIXV</m:symbol><m:buyerID>ZGJFDW</m:buyerID><m:price>092180.02</m:price><m:volume>666160</m:volume></m:order><m:order><m:symbol>ISQJWX</m:symbol><m:buyerID>GQKAIF</m:buyerID><m:price>478032.43</m:price><m:volume>710755</m:volume></m:order><m:order><m:symbol>DYOXOW</m:symbol><m:buyerID>ODBBEI</m:buyerID><m:price>231541.03</m:price><m:volume>855340</m:volume></m:order><m:order><m:symbol>AXSFNF</m:symbol><m:buyerID>CJWBJA</m:buyerID><m:price>040260.78</m:price><m:volume>725749</m:volume></m:order><m:order><m:symbol>KEZULN</m:symbol><m:buyerID>CYAALV</m:buyerID><m:price>373454.37</m:price><m:volume>759802</m:volume></m:order><m:order><m:symbol>PZEVQV</m:symbol><m:buyerID>VYEHSW</m:buyerID><m:price>088964.88</m:price><m:volume>004801</m:volume></m:order><m:order><m:symbol>NHIHYZ</m:symbol><m:buyerID>JGAUER</m:buyerID><m:price>520707.16</m:price><m:volume>615930</m:volume></m:order><m:order><m:symbol>CHHCTJ</m:symbol><m:buyerID>WJEOYQ</m:buyerID><m:price>581465.30</m:price><m:volume>407996</m:volume></m:order><m:order><m:symbol>PXIRBQ</m:symbol><m:buyerID>QTUWYG</m:buyerID><m:price>006747.73</m:price><m:volume>799885</m:volume></m:order><m:order><m:symbol>BLIXWM</m:symbol><m:buyerID>TRSLSL</m:buyerID><m:price>378975.45</m:price><m:volume>575364</m:volume></m:order><m:order><m:symbol>XGSAQN</m:symbol><m:buyerID>IWKFJJ</m:buyerID><m:price>000509.78</m:price><m:volume>383966</m:volume></m:order><m:order><m:symbol>KBSJDP</m:symbol><m:buyerID>ATCNFR</m:buyerID><m:price>134189.90</m:price><m:volume>847800</m:volume></m:order><m:order><m:symbol>KJBXAY</m:symbol><m:buyerID>YKSEQY</m:buyerID><m:price>093292.96</m:price><m:volume>843561</m:volume></m:order><m:order><m:symbol>SJAULV</m:symbol><m:buyerID>MKZCLJ</m:buyerID><m:price>298709.22</m:price><m:volume>760334</m:volume></m:order><m:order><m:symbol>VFOLTZ</m:symbol><m:buyerID>PGIOGQ</m:buyerID><m:price>014305.25</m:price><m:volume>321470</m:volume></m:order><m:order><m:symbol>UZQIMS</m:symbol><m:buyerID>TIPHRD</m:buyerID><m:price>963051.71</m:price><m:volume>840866</m:volume></m:order><m:order><m:symbol>PECPUA</m:symbol><m:buyerID>PXLVWE</m:buyerID><m:price>606177.64</m:price><m:volume>329181</m:volume></m:order><m:order><m:symbol>USZACM</m:symbol><m:buyerID>KYBKRM</m:buyerID><m:price>768250.47</m:price><m:volume>300028</m:volume></m:order><m:order><m:symbol>YWSOPO</m:symbol><m:buyerID>ZIOANQ</m:buyerID><m:price>061901.12</m:price><m:volume>032004</m:volume></m:order><m:order><m:symbol>GTJCNP</m:symbol><m:buyerID>WRFFOF</m:buyerID><m:price>760119.81</m:price><m:volume>733448</m:volume></m:order><m:order><m:symbol>TEFGMZ</m:symbol><m:buyerID>UWQGFB</m:buyerID><m:price>446944.90</m:price><m:volume>065656</m:volume></m:order><m:order><m:symbol>KRXWNN</m:symbol><m:buyerID>LFCLBX</m:buyerID><m:price>841476.73</m:price><m:volume>166097</m:volume></m:order><m:order><m:symbol>DNUFBA</m:symbol><m:buyerID>JENRQM</m:buyerID><m:price>450349.86</m:price><m:volume>147163</m:volume></m:order><m:order><m:symbol>CAIJUP</m:symbol><m:buyerID>UKFDXU</m:buyerID><m:price>428779.38</m:price><m:volume>619281</m:volume></m:order><m:order><m:symbol>HNQUIW</m:symbol><m:buyerID>XCIHVO</m:buyerID><m:price>555335.25</m:price><m:volume>100788</m:volume></m:order><m:order><m:symbol>ZFDVKW</m:symbol><m:buyerID>XUCEME</m:buyerID><m:price>059954.96</m:price><m:volume>606056</m:volume></m:order><m:order><m:symbol>BCIGKX</m:symbol><m:buyerID>AIFTWY</m:buyerID><m:price>973096.10</m:price><m:volume>020098</m:volume></m:order><m:order><m:symbol>YOTRHE</m:symbol><m:buyerID>MIMPKT</m:buyerID><m:price>096826.39</m:price><m:volume>264010</m:volume></m:order><m:order><m:symbol>JCQPVN</m:symbol><m:buyerID>WHSYWZ</m:buyerID><m:price>640071.52</m:price><m:volume>200834</m:volume></m:order><m:order><m:symbol>PQFFWT</m:symbol><m:buyerID>FPVQSA</m:buyerID><m:price>370356.18</m:price><m:volume>073080</m:volume></m:order><m:order><m:symbol>TBCNQG</m:symbol><m:buyerID>POLCMM</m:buyerID><m:price>104169.06</m:price><m:volume>482009</m:volume></m:order><m:order><m:symbol>YTQMJY</m:symbol><m:buyerID>VESGSY</m:buyerID><m:price>890380.03</m:price><m:volume>304870</m:volume></m:order><m:order><m:symbol>PRQZPO</m:symbol><m:buyerID>UVVLTX</m:buyerID><m:price>080906.00</m:price><m:volume>977130</m:volume></m:order><m:order><m:symbol>PUGFGZ</m:symbol><m:buyerID>IKMUPA</m:buyerID><m:price>476967.28</m:price><m:volume>014793</m:volume></m:order><m:order><m:symbol>BAVCMR</m:symbol><m:buyerID>WVPRFM</m:buyerID><m:price>617407.06</m:price><m:volume>364486</m:volume></m:order><m:order><m:symbol>DZOWAO</m:symbol><m:buyerID>CRKQKJ</m:buyerID><m:price>633705.76</m:price><m:volume>236066</m:volume></m:order><m:order><m:symbol>KZBCYP</m:symbol><m:buyerID>GYTYTJ</m:buyerID><m:price>608701.73</m:price><m:volume>999410</m:volume></m:order><m:order><m:symbol>SXOKJM</m:symbol><m:buyerID>ACYHWT</m:buyerID><m:price>288037.42</m:price><m:volume>530078</m:volume></m:order><m:order><m:symbol>OWDMHT</m:symbol><m:buyerID>QVTXKT</m:buyerID><m:price>893027.50</m:price><m:volume>447061</m:volume></m:order><m:order><m:symbol>XVFCAF</m:symbol><m:buyerID>WZDTQW</m:buyerID><m:price>601173.45</m:price><m:volume>027814</m:volume></m:order><m:order><m:symbol>QVUEUF</m:symbol><m:buyerID>DKSMDK</m:buyerID><m:price>486330.14</m:price><m:volume>981207</m:volume></m:order><m:order><m:symbol>XBHKRC</m:symbol><m:buyerID>XFWQTI</m:buyerID><m:price>170938.94</m:price><m:volume>383985</m:volume></m:order><m:order><m:symbol>VCCOQR</m:symbol><m:buyerID>KDUUJY</m:buyerID><m:price>883487.30</m:price><m:volume>631641</m:volume></m:order><m:order><m:symbol>RENUUX</m:symbol><m:buyerID>BBXKGO</m:buyerID><m:price>204641.85</m:price><m:volume>903405</m:volume></m:order><m:order><m:symbol>UJJQMZ</m:symbol><m:buyerID>SWJETK</m:buyerID><m:price>233806.66</m:price><m:volume>494590</m:volume></m:order><m:order><m:symbol>YRZVKH</m:symbol><m:buyerID>BQPCBV</m:buyerID><m:price>883156.29</m:price><m:volume>448120</m:volume></m:order><m:order><m:symbol>RRYRLA</m:symbol><m:buyerID>FAMRBL</m:buyerID><m:price>987276.01</m:price><m:volume>258622</m:volume></m:order><m:order><m:symbol>DSAXVB</m:symbol><m:buyerID>TKHIOU</m:buyerID><m:price>017668.39</m:price><m:volume>280136</m:volume></m:order><m:order><m:symbol>MQWFTL</m:symbol><m:buyerID>SAHQGE</m:buyerID><m:price>950377.64</m:price><m:volume>859760</m:volume></m:order><m:order><m:symbol>FKNZWD</m:symbol><m:buyerID>OJINUJ</m:buyerID><m:price>058970.27</m:price><m:volume>808055</m:volume></m:order><m:order><m:symbol>UCSLOI</m:symbol><m:buyerID>ZEXEQD</m:buyerID><m:price>303554.87</m:price><m:volume>299738</m:volume></m:order><m:order><m:symbol>NENUXC</m:symbol><m:buyerID>THBRYW</m:buyerID><m:price>496332.39</m:price><m:volume>876000</m:volume></m:order><m:order><m:symbol>GNMVFT</m:symbol><m:buyerID>VRAJKA</m:buyerID><m:price>867900.04</m:price><m:volume>153644</m:volume></m:order><m:order><m:symbol>OFQNFK</m:symbol><m:buyerID>BNOMPC</m:buyerID><m:price>086052.60</m:price><m:volume>404771</m:volume></m:order><m:order><m:symbol>COPBTY</m:symbol><m:buyerID>DXTDHL</m:buyerID><m:price>915730.43</m:price><m:volume>010330</m:volume></m:order><m:order><m:symbol>VFKEZJ</m:symbol><m:buyerID>MEOHTY</m:buyerID><m:price>121661.14</m:price><m:volume>344911</m:volume></m:order><m:order><m:symbol>BQZVNO</m:symbol><m:buyerID>NOOLSK</m:buyerID><m:price>578227.80</m:price><m:volume>216145</m:volume></m:order><m:order><m:symbol>RLKLQU</m:symbol><m:buyerID>SNWYSE</m:buyerID><m:price>041091.60</m:price><m:volume>539896</m:volume></m:order><m:order><m:symbol>OZKWMO</m:symbol><m:buyerID>HLZVSS</m:buyerID><m:price>622564.16</m:price><m:volume>591690</m:volume></m:order><m:order><m:symbol>KEWEXF</m:symbol><m:buyerID>ZVUWVV</m:buyerID><m:price>611028.79</m:price><m:volume>634026</m:volume></m:order><m:order><m:symbol>WCHTPW</m:symbol><m:buyerID>GZYAZR</m:buyerID><m:price>739195.12</m:price><m:volume>716093</m:volume></m:order><m:order><m:symbol>KYCZTV</m:symbol><m:buyerID>MUMPOJ</m:buyerID><m:price>848464.01</m:price><m:volume>092927</m:volume></m:order><m:order><m:symbol>YIPWYN</m:symbol><m:buyerID>GTCFKX</m:buyerID><m:price>087592.80</m:price><m:volume>261282</m:volume></m:order><m:order><m:symbol>PYCXLB</m:symbol><m:buyerID>YCCWCO</m:buyerID><m:price>160805.74</m:price><m:volume>772286</m:volume></m:order><m:order><m:symbol>FKYFHE</m:symbol><m:buyerID>TVTAJS</m:buyerID><m:price>306710.36</m:price><m:volume>775027</m:volume></m:order><m:order><m:symbol>XLYSHQ</m:symbol><m:buyerID>IRPZKG</m:buyerID><m:price>408154.10</m:price><m:volume>098802</m:volume></m:order><m:order><m:symbol>DCAORN</m:symbol><m:buyerID>AXLAPL</m:buyerID><m:price>464100.01</m:price><m:volume>740362</m:volume></m:order><m:order><m:symbol>OEUTSO</m:symbol><m:buyerID>FPODPW</m:buyerID><m:price>068223.72</m:price><m:volume>947409</m:volume></m:order><m:order><m:symbol>KZQUVB</m:symbol><m:buyerID>KZJLVW</m:buyerID><m:price>566215.70</m:price><m:volume>720605</m:volume></m:order><m:order><m:symbol>NNSSNX</m:symbol><m:buyerID>YZOTXJ</m:buyerID><m:price>933079.71</m:price><m:volume>485398</m:volume></m:order><m:order><m:symbol>RFOJSQ</m:symbol><m:buyerID>NSGTDA</m:buyerID><m:price>757636.80</m:price><m:volume>232737</m:volume></m:order></m:buyStocks></soapenv:Body></soapenv:Envelope>";

    return (char *) apr_pstrdup(pool, bodystring);
}
