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
typedef struct svc_simulator_20k_config_rec
{
    char *filepath;
} svc_simulator_20k_config_rec_t;

typedef struct svc_simulator_20k_worker
{
    char *filepath;
    char *bodystring;
    int bodystring_len;
} svc_simulator_20k_worker_t;

svc_simulator_20k_worker_t *simulator_worker = NULL;

/******************************Function Headers********************************/
static void *
svc_simulator_20k_create_svr(
    apr_pool_t * p,
    server_rec * s);

static const char *
svc_simulator_20k_set_filepath(
    cmd_parms * cmd,
    void *dummy,
    const char *arg);

static int
svc_simulator_20k_handler(
    request_rec * req);

svc_simulator_20k_worker_t *
svc_simulator_20k_worker_create(
    apr_pool_t * p,
    char * filepath);

static int
svc_simulator_20k_process_request(
    request_rec * request);

static char *
svc_simulator_20k_get_reply(
    apr_pool_t * pool);

static void
svc_simulator_20k_module_init(
    apr_pool_t * p,
    server_rec * svr_rec);

static void
svc_simulator_20k_register_hooks(
    apr_pool_t * p);

/***************************End of Function Headers****************************/

static const command_rec svc_simulator_20k_cmds[] = { AP_INIT_TAKE1("SvcSimulatorInputFile_20k", svc_simulator_20k_set_filepath, NULL,
    RSRC_CONF, "Service Simulator file path"), { NULL } };

/* Dispatch list for API hooks */
module AP_MODULE_DECLARE_DATA svc_simulator_20k_module = { STANDARD20_MODULE_STUFF, NULL, /* create per-dir    config structures */
NULL, /* merge  per-dir    config structures */
svc_simulator_20k_create_svr, /* create per-server config structures */
NULL, /* merge  per-server config structures */
svc_simulator_20k_cmds, /* table of config file commands       */
svc_simulator_20k_register_hooks /* register hooks                      */
};

static void *
svc_simulator_20k_create_svr(
    apr_pool_t * p,
    server_rec * s)
{
    svc_simulator_20k_config_rec_t *conf = apr_palloc(p, sizeof(*conf));
    conf->filepath = NULL;
    return conf;
}

static const char *
svc_simulator_20k_set_filepath(
    cmd_parms * cmd,
    void *dummy,
    const char *arg)
{
    apr_status_t status;
    svc_simulator_20k_config_rec_t *conf = NULL;
    const char *err = ap_check_cmd_context(cmd, GLOBAL_ONLY);
    if(err != NULL)
    {
        return err;
    }
    conf = (svc_simulator_20k_config_rec_t *)ap_get_module_config(cmd->server->module_config, &svc_simulator_20k_module);
    conf->filepath = apr_pstrdup(cmd->pool, arg);
    return NULL;
}

/* The sample content handler */
static int
svc_simulator_20k_handler(
    request_rec * req)
{
    int rv = 0;

    /*ap_log_rerror(APLOG_MARK, APLOG_EMERG, 0, req, "[svc_simulator_20k] damcame1");*/
    if(strcmp(req->handler, "svc_simulator_20k_module"))
    {
        return DECLINED;
    }

    /* Set up the read policy from the client. */
    if((rv = ap_setup_client_block(req, REQUEST_CHUNKED_DECHUNK)) != OK)
    {
        return rv;
    }
    ap_should_client_block(req);

    rv = svc_simulator_20k_process_request(req);
    if(DECLINED == rv)
    {
        return HTTP_INTERNAL_SERVER_ERROR;
    }

    return rv;
}

static int
svc_simulator_20k_post_config(
    apr_pool_t *pconf,
    apr_pool_t *plog,
    apr_pool_t *ptemp,
    server_rec *svr_rec)
{
    apr_status_t status = APR_SUCCESS;
    void *data = NULL;
    const char *userdata_key = "svc_simulator_20k_init";
    svc_simulator_20k_config_rec_t *conf = (svc_simulator_20k_config_rec_t *)ap_get_module_config(
            svr_rec->module_config, &svc_simulator_20k_module);

    /* svc_simulator_20k_post_config() will be called twice. Don't bother
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
svc_simulator_20k_module_init(
    apr_pool_t * p,
    server_rec * svr_rec)
{
    apr_pool_t *pool;
    apr_status_t status;
    svc_simulator_20k_config_rec_t *conf = (svc_simulator_20k_config_rec_t*)ap_get_module_config(
            svr_rec->module_config, &svc_simulator_20k_module);

    status = apr_pool_create(&pool, p);
    if(status)
    {
        ap_log_error(APLOG_MARK, APLOG_EMERG, status, svr_rec,
            "[Axis2] Error allocating mod_svc_simulator_20k memory pool");
        exit(APEXIT_CHILDFATAL);
    }

    simulator_worker = svc_simulator_20k_worker_create(pool, conf->filepath);
    if(!simulator_worker)
    {
        ap_log_error(APLOG_MARK, APLOG_EMERG, APR_EGENERAL, svr_rec,
            "[svc_simulator_20k] Error creating mod_svc_simulator_20k worker;filepath:%s", conf->filepath);
        exit(APEXIT_CHILDFATAL);
    }
}

static void
svc_simulator_20k_register_hooks(
    apr_pool_t * p)
{
    ap_hook_post_config(svc_simulator_20k_post_config, NULL, NULL, APR_HOOK_MIDDLE);
    ap_hook_handler(svc_simulator_20k_handler, NULL, NULL, APR_HOOK_MIDDLE);
    ap_hook_child_init(svc_simulator_20k_module_init, NULL, NULL, APR_HOOK_MIDDLE);
}

svc_simulator_20k_worker_t *
svc_simulator_20k_worker_create(
    apr_pool_t * pool,
    char * filepath)
{
    svc_simulator_20k_worker_t *worker = NULL;
    worker = (svc_simulator_20k_worker_t *) apr_palloc(pool, sizeof(svc_simulator_20k_worker_t));

    if(!worker)
    {
        return NULL;
    }

    worker->filepath = filepath;

    if(!worker->filepath)
    {
        return NULL;
    }
    
    worker->bodystring = svc_simulator_20k_get_reply(pool);
    worker->bodystring_len = strlen(worker->bodystring);

    return worker;
}


static int
svc_simulator_20k_process_request(
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
        ap_log_rerror(APLOG_MARK, APLOG_EMERG, 0, request, "[svc_simulator_20k] request url:%s", request_url);
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

    /*ap_log_rerror(APLOG_MARK, APLOG_EMERG, 0, request, "[svc_simulator_20k] bodystring:%s", simulator_worker->bodystring);*/
    ap_rwrite(simulator_worker->bodystring, simulator_worker->bodystring_len, request);

    return send_status;
}

static char *
svc_simulator_20k_get_reply(
    apr_pool_t * pool)
{
    char *bodystring = NULL;

    bodystring = "<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"></soapenv:Header><soapenv:Body><m:buyStocks xmlns:m=\"http://po.services.core.carbon.wso2.org\"><m:order><m:symbol>IBM</m:symbol><m:buyerID>asankha</m:buyerID><m:price>140.34</m:price><m:volume>200000</m:volume></m:order><m:order><m:symbol>KFFPEB</m:symbol><m:buyerID>AADDUZ</m:buyerID><m:price>606889.46</m:price><m:volume>524064</m:volume></m:order><m:order><m:symbol>IMELZP</m:symbol><m:buyerID>JWEWCZ</m:buyerID><m:price>303037.12</m:price><m:volume>734028</m:volume></m:order><m:order><m:symbol>VHZORN</m:symbol><m:buyerID>ELVFPV</m:buyerID><m:price>769558.32</m:price><m:volume>656879</m:volume></m:order><m:order><m:symbol>EUYLLE</m:symbol><m:buyerID>HRUJLT</m:buyerID><m:price>700441.73</m:price><m:volume>916401</m:volume></m:order><m:order><m:symbol>VMARTP</m:symbol><m:buyerID>GDUFQG</m:buyerID><m:price>696102.83</m:price><m:volume>030968</m:volume></m:order><m:order><m:symbol>ZLMXFT</m:symbol><m:buyerID>DJJNCZ</m:buyerID><m:price>998401.06</m:price><m:volume>118110</m:volume></m:order><m:order><m:symbol>YPLYRG</m:symbol><m:buyerID>NUALJP</m:buyerID><m:price>087636.48</m:price><m:volume>282865</m:volume></m:order><m:order><m:symbol>CQDLCM</m:symbol><m:buyerID>RBZQAY</m:buyerID><m:price>064256.10</m:price><m:volume>401909</m:volume></m:order><m:order><m:symbol>NIBAEZ</m:symbol><m:buyerID>KTDSUK</m:buyerID><m:price>773888.00</m:price><m:volume>588468</m:volume></m:order><m:order><m:symbol>XFKGAU</m:symbol><m:buyerID>UYLQXW</m:buyerID><m:price>965859.38</m:price><m:volume>223943</m:volume></m:order><m:order><m:symbol>YXODML</m:symbol><m:buyerID>RZHMYN</m:buyerID><m:price>086882.74</m:price><m:volume>330219</m:volume></m:order><m:order><m:symbol>ZFHDZT</m:symbol><m:buyerID>GYHTGZ</m:buyerID><m:price>657592.13</m:price><m:volume>265778</m:volume></m:order><m:order><m:symbol>UMMTWV</m:symbol><m:buyerID>FHDFYB</m:buyerID><m:price>335551.57</m:price><m:volume>721425</m:volume></m:order><m:order><m:symbol>QJUYXS</m:symbol><m:buyerID>XVMXPB</m:buyerID><m:price>853494.23</m:price><m:volume>865905</m:volume></m:order><m:order><m:symbol>DCLMPZ</m:symbol><m:buyerID>WMEUZP</m:buyerID><m:price>243101.09</m:price><m:volume>663064</m:volume></m:order><m:order><m:symbol>OFKTIJ</m:symbol><m:buyerID>OBMRLZ</m:buyerID><m:price>984568.30</m:price><m:volume>192264</m:volume></m:order><m:order><m:symbol>KNBJZG</m:symbol><m:buyerID>FFZSWK</m:buyerID><m:price>440032.61</m:price><m:volume>203089</m:volume></m:order><m:order><m:symbol>RQNKWJ</m:symbol><m:buyerID>MWLMYQ</m:buyerID><m:price>979533.46</m:price><m:volume>761068</m:volume></m:order><m:order><m:symbol>VRATAR</m:symbol><m:buyerID>BGQWZQ</m:buyerID><m:price>242380.05</m:price><m:volume>795927</m:volume></m:order><m:order><m:symbol>OZHEZO</m:symbol><m:buyerID>SAISUI</m:buyerID><m:price>311666.56</m:price><m:volume>577231</m:volume></m:order><m:order><m:symbol>OEWKMF</m:symbol><m:buyerID>SGTTJW</m:buyerID><m:price>250810.81</m:price><m:volume>399129</m:volume></m:order><m:order><m:symbol>PTNYXY</m:symbol><m:buyerID>DRQEKX</m:buyerID><m:price>314325.14</m:price><m:volume>207186</m:volume></m:order><m:order><m:symbol>QRSCJM</m:symbol><m:buyerID>KPXTIO</m:buyerID><m:price>104646.66</m:price><m:volume>308808</m:volume></m:order><m:order><m:symbol>SNOQPI</m:symbol><m:buyerID>RENWVO</m:buyerID><m:price>807529.73</m:price><m:volume>140403</m:volume></m:order><m:order><m:symbol>EUBUZO</m:symbol><m:buyerID>DVYKTN</m:buyerID><m:price>708300.29</m:price><m:volume>028803</m:volume></m:order><m:order><m:symbol>WXLPOX</m:symbol><m:buyerID>MERTLH</m:buyerID><m:price>830350.58</m:price><m:volume>288273</m:volume></m:order><m:order><m:symbol>EBRSUW</m:symbol><m:buyerID>PKMJGI</m:buyerID><m:price>709071.04</m:price><m:volume>586960</m:volume></m:order><m:order><m:symbol>RXDGLI</m:symbol><m:buyerID>OFVARX</m:buyerID><m:price>599537.20</m:price><m:volume>419160</m:volume></m:order><m:order><m:symbol>LGNXTJ</m:symbol><m:buyerID>KTMCWE</m:buyerID><m:price>779591.89</m:price><m:volume>629050</m:volume></m:order><m:order><m:symbol>YNOWPL</m:symbol><m:buyerID>JLMZBQ</m:buyerID><m:price>660754.45</m:price><m:volume>087502</m:volume></m:order><m:order><m:symbol>SVAMHE</m:symbol><m:buyerID>EXMBAX</m:buyerID><m:price>477922.50</m:price><m:volume>577556</m:volume></m:order><m:order><m:symbol>JMOXRX</m:symbol><m:buyerID>ZGNRJI</m:buyerID><m:price>966729.65</m:price><m:volume>666613</m:volume></m:order><m:order><m:symbol>KARRDG</m:symbol><m:buyerID>UMZZMH</m:buyerID><m:price>776104.51</m:price><m:volume>339083</m:volume></m:order><m:order><m:symbol>LUEIFS</m:symbol><m:buyerID>COTZQV</m:buyerID><m:price>109480.23</m:price><m:volume>475023</m:volume></m:order><m:order><m:symbol>ANRQOW</m:symbol><m:buyerID>YNTHEJ</m:buyerID><m:price>799126.51</m:price><m:volume>054899</m:volume></m:order><m:order><m:symbol>JRFJCJ</m:symbol><m:buyerID>RKFLWG</m:buyerID><m:price>193632.28</m:price><m:volume>632894</m:volume></m:order><m:order><m:symbol>TSCTSP</m:symbol><m:buyerID>RLLIOA</m:buyerID><m:price>222366.56</m:price><m:volume>423518</m:volume></m:order><m:order><m:symbol>HKXAQU</m:symbol><m:buyerID>MFKVIG</m:buyerID><m:price>802055.55</m:price><m:volume>515890</m:volume></m:order><m:order><m:symbol>GHSPAZ</m:symbol><m:buyerID>STVGUL</m:buyerID><m:price>861047.43</m:price><m:volume>870693</m:volume></m:order><m:order><m:symbol>KWJBGL</m:symbol><m:buyerID>OBSQNC</m:buyerID><m:price>764410.47</m:price><m:volume>016370</m:volume></m:order><m:order><m:symbol>KKANCR</m:symbol><m:buyerID>SCVETW</m:buyerID><m:price>204449.98</m:price><m:volume>065544</m:volume></m:order><m:order><m:symbol>WCNOSK</m:symbol><m:buyerID>HMDCJB</m:buyerID><m:price>805150.06</m:price><m:volume>606883</m:volume></m:order><m:order><m:symbol>QCPVAY</m:symbol><m:buyerID>IIWKHH</m:buyerID><m:price>360103.90</m:price><m:volume>207505</m:volume></m:order><m:order><m:symbol>MFCYAC</m:symbol><m:buyerID>VSYRBK</m:buyerID><m:price>598221.68</m:price><m:volume>053519</m:volume></m:order><m:order><m:symbol>RORIBU</m:symbol><m:buyerID>VTEEFE</m:buyerID><m:price>933266.33</m:price><m:volume>666960</m:volume></m:order><m:order><m:symbol>BLLJMY</m:symbol><m:buyerID>XGPGUI</m:buyerID><m:price>680017.19</m:price><m:volume>747906</m:volume></m:order><m:order><m:symbol>VIYFOG</m:symbol><m:buyerID>BFPAHA</m:buyerID><m:price>525334.79</m:price><m:volume>093062</m:volume></m:order><m:order><m:symbol>UNMRLU</m:symbol><m:buyerID>WXHJYB</m:buyerID><m:price>030870.07</m:price><m:volume>041676</m:volume></m:order><m:order><m:symbol>CEVBIZ</m:symbol><m:buyerID>ADAIPS</m:buyerID><m:price>332572.56</m:price><m:volume>255270</m:volume></m:order><m:order><m:symbol>BCIAQT</m:symbol><m:buyerID>NXSBWD</m:buyerID><m:price>475107.74</m:price><m:volume>730545</m:volume></m:order><m:order><m:symbol>FXXMOX</m:symbol><m:buyerID>IGNVXP</m:buyerID><m:price>217224.19</m:price><m:volume>551771</m:volume></m:order><m:order><m:symbol>YVCCCS</m:symbol><m:buyerID>XVGTTB</m:buyerID><m:price>332400.47</m:price><m:volume>934048</m:volume></m:order><m:order><m:symbol>UPVTHW</m:symbol><m:buyerID>GTSWCJ</m:buyerID><m:price>058641.89</m:price><m:volume>373306</m:volume></m:order><m:order><m:symbol>PJYOLK</m:symbol><m:buyerID>WNDOST</m:buyerID><m:price>672123.65</m:price><m:volume>200430</m:volume></m:order><m:order><m:symbol>XHRCYP</m:symbol><m:buyerID>YEUDHY</m:buyerID><m:price>135712.81</m:price><m:volume>917409</m:volume></m:order><m:order><m:symbol>NXJPLG</m:symbol><m:buyerID>EUJSBM</m:buyerID><m:price>849087.80</m:price><m:volume>918408</m:volume></m:order><m:order><m:symbol>NLGZZG</m:symbol><m:buyerID>PPXFHV</m:buyerID><m:price>007569.03</m:price><m:volume>039396</m:volume></m:order><m:order><m:symbol>TOAMJY</m:symbol><m:buyerID>HLTQSU</m:buyerID><m:price>230832.55</m:price><m:volume>404294</m:volume></m:order><m:order><m:symbol>MTZWXD</m:symbol><m:buyerID>WPMCVJ</m:buyerID><m:price>290714.24</m:price><m:volume>746034</m:volume></m:order><m:order><m:symbol>VUZACA</m:symbol><m:buyerID>KAQXQD</m:buyerID><m:price>708253.52</m:price><m:volume>880802</m:volume></m:order><m:order><m:symbol>XHPJNC</m:symbol><m:buyerID>PTOZVR</m:buyerID><m:price>660809.00</m:price><m:volume>482300</m:volume></m:order><m:order><m:symbol>YDGZJU</m:symbol><m:buyerID>SCHSZT</m:buyerID><m:price>096512.68</m:price><m:volume>588778</m:volume></m:order><m:order><m:symbol>KABWEA</m:symbol><m:buyerID>IRMBGD</m:buyerID><m:price>002320.56</m:price><m:volume>281708</m:volume></m:order><m:order><m:symbol>IPWJRU</m:symbol><m:buyerID>WIBDGA</m:buyerID><m:price>260594.43</m:price><m:volume>642655</m:volume></m:order><m:order><m:symbol>SRDEPK</m:symbol><m:buyerID>WJDAZY</m:buyerID><m:price>555851.05</m:price><m:volume>502705</m:volume></m:order><m:order><m:symbol>HYGNHI</m:symbol><m:buyerID>HMVYAT</m:buyerID><m:price>910059.84</m:price><m:volume>544515</m:volume></m:order><m:order><m:symbol>WDSGRC</m:symbol><m:buyerID>ZGXQIE</m:buyerID><m:price>530639.47</m:price><m:volume>212706</m:volume></m:order><m:order><m:symbol>LKYPVS</m:symbol><m:buyerID>LILSTS</m:buyerID><m:price>219300.42</m:price><m:volume>313320</m:volume></m:order><m:order><m:symbol>KNDNUI</m:symbol><m:buyerID>NZTAIZ</m:buyerID><m:price>768197.70</m:price><m:volume>000676</m:volume></m:order><m:order><m:symbol>QVJCJT</m:symbol><m:buyerID>VXOCFF</m:buyerID><m:price>016931.10</m:price><m:volume>080804</m:volume></m:order><m:order><m:symbol>VSWJSX</m:symbol><m:buyerID>YXOLXN</m:buyerID><m:price>554045.01</m:price><m:volume>573044</m:volume></m:order><m:order><m:symbol>KBBWAV</m:symbol><m:buyerID>SKEVYL</m:buyerID><m:price>506355.79</m:price><m:volume>010082</m:volume></m:order><m:order><m:symbol>NFEWPI</m:symbol><m:buyerID>TRNUKK</m:buyerID><m:price>965334.99</m:price><m:volume>476102</m:volume></m:order><m:order><m:symbol>ASNJIW</m:symbol><m:buyerID>OTZWCH</m:buyerID><m:price>649612.74</m:price><m:volume>306669</m:volume></m:order><m:order><m:symbol>GWIIAO</m:symbol><m:buyerID>NYGMHB</m:buyerID><m:price>737959.70</m:price><m:volume>831840</m:volume></m:order><m:order><m:symbol>XRCPUR</m:symbol><m:buyerID>GUUWVI</m:buyerID><m:price>532032.91</m:price><m:volume>801524</m:volume></m:order><m:order><m:symbol>ATZHQV</m:symbol><m:buyerID>CSPTNX</m:buyerID><m:price>843960.98</m:price><m:volume>009738</m:volume></m:order><m:order><m:symbol>VACOON</m:symbol><m:buyerID>XYVPWY</m:buyerID><m:price>207990.00</m:price><m:volume>576010</m:volume></m:order><m:order><m:symbol>TDURIL</m:symbol><m:buyerID>OLZXAI</m:buyerID><m:price>332748.33</m:price><m:volume>121267</m:volume></m:order><m:order><m:symbol>OUXPXG</m:symbol><m:buyerID>MZKQLT</m:buyerID><m:price>315508.05</m:price><m:volume>609154</m:volume></m:order><m:order><m:symbol>KZXNAT</m:symbol><m:buyerID>MFMPIP</m:buyerID><m:price>698799.80</m:price><m:volume>332152</m:volume></m:order><m:order><m:symbol>GYEPMR</m:symbol><m:buyerID>MNKYVK</m:buyerID><m:price>800723.89</m:price><m:volume>698089</m:volume></m:order><m:order><m:symbol>XGDAKH</m:symbol><m:buyerID>DYPNJH</m:buyerID><m:price>080650.08</m:price><m:volume>870224</m:volume></m:order><m:order><m:symbol>FPCBNA</m:symbol><m:buyerID>DBVWPM</m:buyerID><m:price>195510.51</m:price><m:volume>139050</m:volume></m:order><m:order><m:symbol>VIFJOM</m:symbol><m:buyerID>SDWNBL</m:buyerID><m:price>224815.30</m:price><m:volume>496256</m:volume></m:order><m:order><m:symbol>OXCCWJ</m:symbol><m:buyerID>RFFDJD</m:buyerID><m:price>916408.83</m:price><m:volume>508300</m:volume></m:order><m:order><m:symbol>MJARZR</m:symbol><m:buyerID>WOBISX</m:buyerID><m:price>416627.41</m:price><m:volume>941195</m:volume></m:order><m:order><m:symbol>SUDKIL</m:symbol><m:buyerID>NEXWCN</m:buyerID><m:price>204502.50</m:price><m:volume>706351</m:volume></m:order><m:order><m:symbol>TKLLRM</m:symbol><m:buyerID>PTAJCE</m:buyerID><m:price>322527.07</m:price><m:volume>366615</m:volume></m:order><m:order><m:symbol>EWIMET</m:symbol><m:buyerID>UPRMWI</m:buyerID><m:price>831411.79</m:price><m:volume>708581</m:volume></m:order><m:order><m:symbol>QRHGES</m:symbol><m:buyerID>VKFUPB</m:buyerID><m:price>536260.35</m:price><m:volume>463650</m:volume></m:order><m:order><m:symbol>MPICTR</m:symbol><m:buyerID>UBRUQF</m:buyerID><m:price>562535.86</m:price><m:volume>930581</m:volume></m:order><m:order><m:symbol>EBUJGF</m:symbol><m:buyerID>HVUYQD</m:buyerID><m:price>315442.09</m:price><m:volume>776162</m:volume></m:order><m:order><m:symbol>LCTDUQ</m:symbol><m:buyerID>MFMYAQ</m:buyerID><m:price>436009.15</m:price><m:volume>860724</m:volume></m:order><m:order><m:symbol>QFZSCD</m:symbol><m:buyerID>WTWWCB</m:buyerID><m:price>625440.90</m:price><m:volume>677536</m:volume></m:order><m:order><m:symbol>KHIOOX</m:symbol><m:buyerID>VAPEXO</m:buyerID><m:price>889010.03</m:price><m:volume>000185</m:volume></m:order><m:order><m:symbol>SKQTJI</m:symbol><m:buyerID>ZTZVNW</m:buyerID><m:price>944306.79</m:price><m:volume>657037</m:volume></m:order><m:order><m:symbol>OSZGLZ</m:symbol><m:buyerID>OKURPN</m:buyerID><m:price>941097.06</m:price><m:volume>659851</m:volume></m:order><m:order><m:symbol>PWHJIK</m:symbol><m:buyerID>RFWJXO</m:buyerID><m:price>007106.03</m:price><m:volume>227384</m:volume></m:order><m:order><m:symbol>XLVDQL</m:symbol><m:buyerID>JTSIMN</m:buyerID><m:price>047636.83</m:price><m:volume>837225</m:volume></m:order><m:order><m:symbol>BTEXPX</m:symbol><m:buyerID>GKFPSY</m:buyerID><m:price>679919.72</m:price><m:volume>814576</m:volume></m:order><m:order><m:symbol>UVTYVW</m:symbol><m:buyerID>FKWPNH</m:buyerID><m:price>920428.08</m:price><m:volume>005749</m:volume></m:order><m:order><m:symbol>LLSCUF</m:symbol><m:buyerID>SPMRLZ</m:buyerID><m:price>804807.02</m:price><m:volume>553008</m:volume></m:order><m:order><m:symbol>PGEQUN</m:symbol><m:buyerID>ANDWON</m:buyerID><m:price>136450.40</m:price><m:volume>022152</m:volume></m:order><m:order><m:symbol>CQJVIG</m:symbol><m:buyerID>YFCOGP</m:buyerID><m:price>843210.47</m:price><m:volume>700704</m:volume></m:order><m:order><m:symbol>DXOJQN</m:symbol><m:buyerID>MEXUZZ</m:buyerID><m:price>757020.23</m:price><m:volume>743673</m:volume></m:order><m:order><m:symbol>DEQDJC</m:symbol><m:buyerID>VQIERV</m:buyerID><m:price>888019.80</m:price><m:volume>000922</m:volume></m:order><m:order><m:symbol>SKORQM</m:symbol><m:buyerID>XIBHNQ</m:buyerID><m:price>024149.96</m:price><m:volume>807386</m:volume></m:order><m:order><m:symbol>DDYFPW</m:symbol><m:buyerID>QCXIWP</m:buyerID><m:price>545492.10</m:price><m:volume>054905</m:volume></m:order><m:order><m:symbol>HIKEFR</m:symbol><m:buyerID>YFGQLI</m:buyerID><m:price>097008.69</m:price><m:volume>202934</m:volume></m:order><m:order><m:symbol>SZUJGS</m:symbol><m:buyerID>LRGKIY</m:buyerID><m:price>302227.19</m:price><m:volume>690202</m:volume></m:order><m:order><m:symbol>MZSPTS</m:symbol><m:buyerID>WNXNKU</m:buyerID><m:price>514012.69</m:price><m:volume>055814</m:volume></m:order><m:order><m:symbol>MCFJGP</m:symbol><m:buyerID>GRJPLL</m:buyerID><m:price>191913.66</m:price><m:volume>701114</m:volume></m:order><m:order><m:symbol>CKPZJX</m:symbol><m:buyerID>YFAMMM</m:buyerID><m:price>707897.37</m:price><m:volume>818823</m:volume></m:order><m:order><m:symbol>WEUXMF</m:symbol><m:buyerID>WSKBGZ</m:buyerID><m:price>004147.16</m:price><m:volume>277640</m:volume></m:order><m:order><m:symbol>BPGYLU</m:symbol><m:buyerID>SQNDYO</m:buyerID><m:price>040102.09</m:price><m:volume>543754</m:volume></m:order><m:order><m:symbol>PXGHLR</m:symbol><m:buyerID>SFTIMI</m:buyerID><m:price>951510.76</m:price><m:volume>950533</m:volume></m:order><m:order><m:symbol>RYJLLR</m:symbol><m:buyerID>AAJRSX</m:buyerID><m:price>755701.01</m:price><m:volume>824911</m:volume></m:order><m:order><m:symbol>LQPYMN</m:symbol><m:buyerID>PQZXBE</m:buyerID><m:price>584823.70</m:price><m:volume>306111</m:volume></m:order><m:order><m:symbol>DQJEDK</m:symbol><m:buyerID>CSBASL</m:buyerID><m:price>286875.50</m:price><m:volume>293538</m:volume></m:order><m:order><m:symbol>IXHGQM</m:symbol><m:buyerID>YWPHHF</m:buyerID><m:price>155109.13</m:price><m:volume>900492</m:volume></m:order><m:order><m:symbol>VXKLYJ</m:symbol><m:buyerID>FOWFAR</m:buyerID><m:price>470308.80</m:price><m:volume>161599</m:volume></m:order><m:order><m:symbol>OEITKR</m:symbol><m:buyerID>GVCLHY</m:buyerID><m:price>346014.37</m:price><m:volume>658660</m:volume></m:order><m:order><m:symbol>EXXXDJ</m:symbol><m:buyerID>RPVAFK</m:buyerID><m:price>854605.57</m:price><m:volume>736576</m:volume></m:order><m:order><m:symbol>DNRCEL</m:symbol><m:buyerID>GLGZFX</m:buyerID><m:price>907295.96</m:price><m:volume>007327</m:volume></m:order><m:order><m:symbol>RJTIDL</m:symbol><m:buyerID>MRMPQI</m:buyerID><m:price>468854.91</m:price><m:volume>110708</m:volume></m:order><m:order><m:symbol>NFSZVE</m:symbol><m:buyerID>MKZJRZ</m:buyerID><m:price>656408.57</m:price><m:volume>373080</m:volume></m:order><m:order><m:symbol>CSUHKE</m:symbol><m:buyerID>JQTBSY</m:buyerID><m:price>362658.01</m:price><m:volume>337963</m:volume></m:order><m:order><m:symbol>TLMQFX</m:symbol><m:buyerID>NEKPIL</m:buyerID><m:price>738736.81</m:price><m:volume>391686</m:volume></m:order><m:order><m:symbol>CIZDQK</m:symbol><m:buyerID>NJELSP</m:buyerID><m:price>252971.05</m:price><m:volume>889367</m:volume></m:order><m:order><m:symbol>OJZMIB</m:symbol><m:buyerID>WRTIIM</m:buyerID><m:price>437572.21</m:price><m:volume>200098</m:volume></m:order><m:order><m:symbol>GRYQRW</m:symbol><m:buyerID>KEIMKE</m:buyerID><m:price>916580.04</m:price><m:volume>551245</m:volume></m:order><m:order><m:symbol>BBPGIV</m:symbol><m:buyerID>KUQBZT</m:buyerID><m:price>609501.93</m:price><m:volume>400290</m:volume></m:order><m:order><m:symbol>QFTZUS</m:symbol><m:buyerID>OKHUQJ</m:buyerID><m:price>571258.85</m:price><m:volume>981840</m:volume></m:order><m:order><m:symbol>DJBEKK</m:symbol><m:buyerID>WVCELN</m:buyerID><m:price>805723.96</m:price><m:volume>426410</m:volume></m:order><m:order><m:symbol>THNZBH</m:symbol><m:buyerID>SRQIXU</m:buyerID><m:price>400575.73</m:price><m:volume>704110</m:volume></m:order><m:order><m:symbol>MJZKTD</m:symbol><m:buyerID>EWIJLA</m:buyerID><m:price>880775.37</m:price><m:volume>057812</m:volume></m:order><m:order><m:symbol>GDYAYZ</m:symbol><m:buyerID>RGUYRC</m:buyerID><m:price>274835.02</m:price><m:volume>000128</m:volume></m:order><m:order><m:symbol>DLJZGV</m:symbol><m:buyerID>GOEJLS</m:buyerID><m:price>622090.48</m:price><m:volume>840465</m:volume></m:order><m:order><m:symbol>YJPLXU</m:symbol><m:buyerID>YBLUJG</m:buyerID><m:price>243019.74</m:price><m:volume>113066</m:volume></m:order><m:order><m:symbol>LIFALM</m:symbol><m:buyerID>FLKLZD</m:buyerID><m:price>395421.10</m:price><m:volume>879578</m:volume></m:order><m:order><m:symbol>NVVZCJ</m:symbol><m:buyerID>WKIGBZ</m:buyerID><m:price>204398.69</m:price><m:volume>414717</m:volume></m:order><m:order><m:symbol>ACSMXG</m:symbol><m:buyerID>YFVWDN</m:buyerID><m:price>160115.83</m:price><m:volume>410274</m:volume></m:order><m:order><m:symbol>SLTNCN</m:symbol><m:buyerID>FZQRZS</m:buyerID><m:price>839926.48</m:price><m:volume>618077</m:volume></m:order><m:order><m:symbol>SEKQEP</m:symbol><m:buyerID>DTXVUO</m:buyerID><m:price>457204.15</m:price><m:volume>559747</m:volume></m:order><m:order><m:symbol>JKWNBG</m:symbol><m:buyerID>YHBTXB</m:buyerID><m:price>003080.84</m:price><m:volume>640292</m:volume></m:order><m:order><m:symbol>OVGGJX</m:symbol><m:buyerID>RBYTHA</m:buyerID><m:price>812481.49</m:price><m:volume>885290</m:volume></m:order><m:order><m:symbol>RVILQR</m:symbol><m:buyerID>XTMIBI</m:buyerID><m:price>330008.52</m:price><m:volume>540617</m:volume></m:order><m:order><m:symbol>PWFNXT</m:symbol><m:buyerID>WYKYAG</m:buyerID><m:price>612925.04</m:price><m:volume>768754</m:volume></m:order></m:buyStocks></soapenv:Body></soapenv:Envelope>";

    return (char *) apr_pstrdup(pool, bodystring);
}
