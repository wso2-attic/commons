%{
#include "wsf_wsdl_mode.h"

#define WS_USE_SOAP         "use_soap"
#define WS_TO               "to"
#define WS_RESPONSE_XOP     "response_xop"
#define WS_HTTP_METHOD      "http_method"
#define WS_USE_MTOM         "use_mtom"
#define WS_USE_WSA          "use_wsa"
#define WS_POLICY           "policy"
#define WS_SECURITY_TOKEN   "security_token"
#define WS_PROXY_HOST       "proxy_host"
#define WS_PROXY_PORT       "proxy_port"
#define WS_WSDL             "wsdl"

#define WS_SOAP_11          "1.1"
#define WS_SOAP_12          "1.2"
#define WS_TRUE             "true"
#define WS_FALSE            "false"

#define WS_TIMESTAMP        "time_stamp"
#define WS_USERNAME_TOKEN   "username_token"
#define WS_ENCRYPTION        "encryption"
#define WS_ALGORITHM        "algorithm"
#define WS_SIGNING            "signing"
#define WS_TOKEN_REFERENCE    "token_reference"
#define WS_ENCRYPT_SIGNATURE "encrypt_signature"
#define WS_PROTECTION_ORDER  "protection_order"


%}

%inline %{

/* 
axis2_status_t
wsf_handle_client_security_for_ruby(const axutil_env_t* env,
                                    axis2_svc_client_t *svc_client,
                                    axiom_node_t *incoming_policy_node,
                                    VALUE security_token)
{
   char* prv_key = NULL;
   VALUE private_key = Qnil;
   char* certificate = NULL;
   VALUE cert = Qnil;
   char* receiver_certificate = NULL;
   VALUE rec_cert = Qnil;
   char* username = NULL;
   VALUE usr = Qnil;
   char* password = NULL;
   VALUE pwd = Qnil;
   char* password_type = NULL;
   VALUE pwd_type = Qnil;
   int ttl = -1;
   VALUE time_to_live = Qnil;
   char* callback_function_name = NULL;
   VALUE callback_fn = Qnil;
   
   private_key = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("private_key"));
   if (!NIL_P(private_key) && (TYPE(private_key) == T_STRING))
   {
        prv_key = RSTRING(private_key)->ptr;
   }

   cert = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("certificate"));
   if (!NIL_P(cert) && (TYPE(cert) == T_STRING))
   {
        certificate = RSTRING(cert)->ptr;
   }

   rec_cert = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("receiver_certificate"));
   if (!NIL_P(rec_cert) && (TYPE(rec_cert) == T_STRING))
   {
        receiver_certificate = RSTRING(rec_cert)->ptr;
   }

   usr = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("user"));
   if (!NIL_P(usr) && (TYPE(usr) == T_STRING))
   {
        username = RSTRING(usr)->ptr;
   }

   pwd = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("password"));
   if (!NIL_P(pwd) && (TYPE(pwd) == T_STRING))
   {
        password = RSTRING(pwd)->ptr;
   }

   pwd_type = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("password_type"));
   if (!NIL_P(pwd_type) && (TYPE(pwd_type) == T_STRING))
   {
        password_type = RSTRING(pwd_type)->ptr;
   }

   time_to_live = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("ttl"));
   if (!NIL_P(time_to_live) && (TYPE(time_to_live) == T_FIXNUM))
   {
        ttl = FIX2INT(time_to_live);
   }

          handle_client_security(env, 
                                 svc_client,
                                 incoming_policy_node,
                                 prv_key,
                                 certificate,
                                 receiver_certificate,
                                 username,
                                 password,
                                 password_type,
                                 ttl);
   return AXIS2_SUCCESS;
}*/

/* Commenting out, this has moved to C layer, delete this after changing WSF/Ruby to reflect the new function */

axis2_status_t
wsf_set_security_token_data_to_rampart_context(const axutil_env_t * env,
                                                rampart_context_t *rampart_context,
                                                VALUE security_token)
{
   char* prv_key = NULL;
   VALUE private_key = Qnil;
   char* certificate = NULL;
   VALUE cert = Qnil;
   char* receiver_certificate = NULL;
   VALUE rec_cert = Qnil;
   char* username = NULL;
   VALUE usr = Qnil;
   char* password = NULL;
   VALUE pwd = Qnil;
   char* password_type = NULL;
   VALUE pwd_type = Qnil;
   int ttl = -1;
   VALUE time_to_live = Qnil;
   char* callback_function_name = NULL;
   VALUE callback_fn = Qnil;
   
   private_key = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("private_key"));
   if (!NIL_P(private_key) && (TYPE(private_key) == T_STRING))
   {
        prv_key = RSTRING(private_key)->ptr;
 
        AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf-ruby] checkcheck ");
        AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, prv_key);

        if ((prv_key != NULL) && (rampart_context_set_prv_key(rampart_context, env, (void *)prv_key) == AXIS2_SUCCESS))
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting pvt key  ");
   
        if ((prv_key != NULL) && (rampart_context_set_prv_key_type(rampart_context, env, AXIS2_KEY_TYPE_PEM) == AXIS2_SUCCESS))
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting pvt key format ");
   }

   cert = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("certificate"));
   if (!NIL_P(cert) && (TYPE(cert) == T_STRING))
   {
        certificate = RSTRING(cert)->ptr;

        if ((certificate != NULL) && (rampart_context_set_certificate(rampart_context, env, (void *)certificate) == AXIS2_SUCCESS))
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting pub key  ");

        if ((certificate != NULL) && (rampart_context_set_certificate_type(rampart_context, env, AXIS2_KEY_TYPE_PEM) == AXIS2_SUCCESS))
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting pub key type ");
   }

   rec_cert = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("receiver_certificate"));
   if (!NIL_P(rec_cert) && (TYPE(rec_cert) == T_STRING))
   {
        receiver_certificate = RSTRING(rec_cert)->ptr;

        if ((receiver_certificate != NULL) && (rampart_context_set_receiver_certificate(rampart_context, env, receiver_certificate) == AXIS2_SUCCESS))
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting receiver pub key");

        if ((receiver_certificate != NULL) && (rampart_context_set_receiver_certificate_type(rampart_context, env, AXIS2_KEY_TYPE_PEM) == AXIS2_SUCCESS)) 
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting receiver pub key format");
   }

   usr = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("user"));
   if (!NIL_P(usr) && (TYPE(usr) == T_STRING))
   {
        username = RSTRING(usr)->ptr;

        if ((username != NULL) && (rampart_context_set_user(rampart_context, env, username) == AXIS2_SUCCESS)) 
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting username ");
   }

   pwd = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("password"));
   if (!NIL_P(pwd) && (TYPE(pwd) == T_STRING))
   {
        password = RSTRING(pwd)->ptr;

        if ((password != NULL) && (rampart_context_set_password(rampart_context, env, password) == AXIS2_SUCCESS)) 
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting password ");
   }

   pwd_type = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("password_type"));
   if (!NIL_P(pwd_type) && (TYPE(pwd_type) == T_STRING))
   {
        password_type = RSTRING(pwd_type)->ptr;

        if ((password_type != NULL) && (rampart_context_set_password_type(rampart_context, env, password_type) == AXIS2_SUCCESS))
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting password type ");
   }

   time_to_live = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("ttl"));
   if (!NIL_P(time_to_live) && (TYPE(time_to_live) == T_FIXNUM))
   {
        ttl = FIX2INT(time_to_live);

        if (rampart_context_set_ttl(rampart_context, env, ttl) == AXIS2_SUCCESS) 
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy) setting ttl");
   }
   
   /*callback_fn =  rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("password_callback"));
   if (!NIL_P(callback_fn) && (TYPE(callback_fn) == T_STRING))
   {
        callback_function_name = RSTRING(callback_fn)->ptr;
          
        AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_ruby] password_callback function name received; ");
        AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, callback_function_name);
       
        //if ((password_type != NULL) && (rampart_context_set_password_type(rampart_context, env, password_type) == AXIS2_SUCCESS))
        //        AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_sec_policy] setting password type ");
   }*/

   /*#option = sec_token.option("password_callback")
    #if not option.nil?
    #   if (WSFC::ruby_rampart_context_set_pwcb_function(rampart_context, @env, nil, option) == WSFC::AXIS2_SUCCESS) then
    #      WSFC::axis2_log_debug(@env, "[wsf_sec_policy] setting callback function")
    #   end
    #endn */

   return AXIS2_SUCCESS;

}


    axis2_status_t 
    wsf_handle_client_security_for_ruby  (axutil_env_t *env,
                                          axis2_svc_client_t *svc_client,
                                          axiom_node_t *incoming_policy_node,
                                          VALUE sec_token)
    {

        axiom_element_t *root_ele = NULL;
        neethi_policy_t *neethi_policy = NULL;
        axis2_svc_t *svc = NULL;
        axis2_desc_t *desc = NULL;
        axis2_policy_include_t *policy_include = NULL;
        axis2_svc_ctx_t *svc_ctx = NULL;
        axis2_conf_ctx_t *conf_ctx = NULL;
        axis2_conf_t *conf = NULL;
        rampart_context_t *rampart_ctx = NULL;
        axutil_param_t *security_param = NULL;
        
        if (incoming_policy_node) {
            if (axiom_node_get_node_type (incoming_policy_node, env) == AXIOM_ELEMENT) {
                root_ele = (axiom_element_t *) axiom_node_get_data_element (incoming_policy_node, env);

                if (root_ele) {
                    neethi_policy = (neethi_policy_t *) neethi_engine_get_policy (env, incoming_policy_node, root_ele);
                    
                    if (!neethi_policy) {
                        return AXIS2_FAILURE;
                    }
                    svc = axis2_svc_client_get_svc (svc_client, env);
                    if (!svc) {
                        return AXIS2_FAILURE;
                    }
                    desc = axis2_svc_get_base (svc, env);
                    if (!desc) {
                        return AXIS2_FAILURE;
                    }
                    policy_include = axis2_desc_get_policy_include (desc, env);
                    if (!policy_include) {
                        return AXIS2_FAILURE;
                    }
                    axis2_policy_include_add_policy_element (policy_include, env,
                                                             AXIS2_SERVICE_POLICY, neethi_policy);

                    svc_ctx = axis2_svc_client_get_svc_ctx (svc_client, env);
                    conf_ctx = axis2_svc_ctx_get_conf_ctx (svc_ctx, env);
                    conf = axis2_conf_ctx_get_conf (conf_ctx, env);

                    if (!conf) {
                        return AXIS2_FAILURE;
                    }
                    rampart_ctx = rampart_context_create(env);
                    wsf_set_security_token_data_to_rampart_context(env, rampart_ctx, sec_token);
                    security_param = axutil_param_create(env, (axis2_char_t *) WSF_RAMPART_CONFIGURATION, rampart_ctx);

                    if (!security_param) {
                        return AXIS2_FAILURE;
                    }

                    axis2_conf_add_param(conf, env, security_param);
                    return AXIS2_SUCCESS;
                    
                }
            }
        }
        return AXIS2_FAILURE;
        
    }

    
    VALUE
    wsf_wsdl_request_function_mem_free_return(axutil_env_t* env,
                                              wsf_wsdl_data_t* response,
                                              wsf_wsdl_data_t* user_parameters,
                                              axis2_char_t* wsdl_file_name,
                                              axis2_char_t* operation_name,
                                              axis2_char_t* wsf_ruby_home,
                                              axutil_hash_t* svc_client_options)
    {
        if (user_parameters)
            wsdl_data_free(env, user_parameters);
        if (response)
            wsdl_data_free(env, response);
        /*if (operation_name)
            AXIS2_FREE(env->allocator, operation_name);
        if (wsdl_file_name)
            AXIS2_FREE(env->allocator, wsdl_file_name);
        if (type_map_file)
            AXIS2_FREE(env->allocator, type_map_file);
        if (xslt_location)
            AXIS2_FREE(env->allocator, xslt_location);
        if (svc_client_options)
            axutil_hash_free(svc_client_options, env);*/

        return Qnil;
    }
    
    wsf_wsdl_data_t*
    wsf_wsdl_convert_params_to_wsdl_data(axutil_env_t* env, VALUE params);

    
    static void
    wsf_evaluate_security_token_and_insert_to_hash(axutil_env_t* env,
                                                   VALUE security_token,
                                                   axutil_hash_t* hash)
    {
        char* prv_key = NULL;
        VALUE private_key = Qnil;
        char* certificate = NULL;
        VALUE cert = Qnil;
        char* receiver_certificate = NULL;
        VALUE rec_cert = Qnil;
        char* username = NULL;
        VALUE usr = Qnil;
        char* password = NULL;
        VALUE pwd = Qnil;
        char* password_type = NULL;
        VALUE pwd_type = Qnil;
        int ttl = -1;
        VALUE time_to_live = Qnil;
        char* callback_function_name = NULL;
        VALUE callback_fn = Qnil;

        if (!hash)
            return;

        private_key = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("private_key"));
        if (!NIL_P(private_key) && (TYPE(private_key) == T_STRING))
        {
            prv_key = RSTRING(private_key)->ptr;

            if (prv_key)
            {
                axutil_hash_set(hash, 
                                WSF_WSDL_HK_PRIVATE_KEY, 
                                AXIS2_HASH_KEY_STRING, 
                                axutil_strdup(env, prv_key));
            }
        }

        cert = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("certificate"));
        if (!NIL_P(cert) && (TYPE(cert) == T_STRING))
        {
            certificate = RSTRING(cert)->ptr;

            if (certificate)
            {
                axutil_hash_set(hash, 
                                WSF_WSDL_HK_CERTIFICATE, 
                                AXIS2_HASH_KEY_STRING, 
                                axutil_strdup(env, certificate));
            }
        }

        rec_cert = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("receiver_certificate"));
        if (!NIL_P(rec_cert) && (TYPE(rec_cert) == T_STRING))
        {
            receiver_certificate = RSTRING(rec_cert)->ptr;

            if (receiver_certificate)
            {
                axutil_hash_set(hash, 
                                WSF_WSDL_HK_RECEIVER_CERTIFICATE, 
                                AXIS2_HASH_KEY_STRING, 
                                axutil_strdup(env, receiver_certificate));
            }
        }

        usr = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("user"));
        if (!NIL_P(usr) && (TYPE(usr) == T_STRING))
        {
            username = RSTRING(usr)->ptr;

            if (username)
            {
                axutil_hash_set(hash, 
                                WSF_WSDL_HK_USER, 
                                AXIS2_HASH_KEY_STRING, 
                                axutil_strdup(env, username));
            }
        }

        pwd = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("password"));
        if (!NIL_P(pwd) && (TYPE(pwd) == T_STRING))
        {
            password = RSTRING(pwd)->ptr;

            if (password)
            {
                axutil_hash_set(hash, 
                                WSF_WSDL_HK_PASSWORD, 
                                AXIS2_HASH_KEY_STRING, 
                                axutil_strdup(env, password));
            }
        }

        pwd_type = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("password_type"));
        if (!NIL_P(pwd_type) && (TYPE(pwd_type) == T_STRING))
        {
            password_type = RSTRING(pwd_type)->ptr;

            if (password_type)
            {
                axutil_hash_set(hash, 
                                WSF_WSDL_HK_PASSWORD_TYPE, 
                                AXIS2_HASH_KEY_STRING, 
                                axutil_strdup(env, password_type));
            }
        }

        callback_fn = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("password_callback"));
        if (!NIL_P(callback_fn) && (TYPE(callback_fn) == T_STRING))
        {
            callback_function_name = RSTRING(callback_fn)->ptr;
                              
            if (callback_function_name)
            {
                axutil_hash_set(hash, 
                                WSF_WSDL_HK_PASSWORD_CALL_BACK, 
                                AXIS2_HASH_KEY_STRING, 
                                axutil_strdup(env, callback_function_name));
            }
        }

        time_to_live = rb_funcall(security_token, rb_intern("option"), 1, rb_str_new2("ttl"));
        if (!NIL_P(time_to_live) && (TYPE(time_to_live) == T_FIXNUM))
        {
            ttl = FIX2INT(time_to_live);

            if (ttl >= 0)
            {
                axutil_hash_set(hash, 
                                WSF_WSDL_HK_TTL, 
                                AXIS2_HASH_KEY_STRING, 
                                (void*)ttl);
            }
        }
    }

    static void
    wsf_evaluate_policy_options_and_insert_to_hash(axutil_env_t* env,
                                                   VALUE policy,
                                                   axutil_hash_t* hash)
    {
        VALUE time_stamp;
        VALUE username_token;
        VALUE encryption;
        VALUE algorithm;
        VALUE sign;
        VALUE token_reference;
        VALUE encrypt_signature;
        VALUE protection_order;
        
        char* ts = NULL;
         char* ut = NULL;
        char* encrypt = NULL;
        char* algo_suite = NULL;
        char* sg = NULL;
        char* tkn = NULL;
        char* token_ref = NULL;
        char* encrypt_sign = NULL;
        char* order = NULL;

        if (policy != Qnil) 
        {
              time_stamp = rb_funcall(policy, rb_intern("option"), 1, rb_str_new2(WS_TIMESTAMP));
        
            if (!NIL_P(time_stamp) && (TYPE(time_stamp) == T_STRING))
            {
                ts = RSTRING(time_stamp)->ptr;
                
                if (ts) 
                {
                    axutil_hash_set(hash, 
                            WSF_WSDL_HK_TIMESTAMP, 
                            AXIS2_HASH_KEY_STRING, 
                            axutil_strdup(env, ts)); 
                }
            }
            
            username_token = rb_funcall(policy, rb_intern("option"), 1, rb_str_new2(WS_USERNAME_TOKEN));
            
            if (!NIL_P(username_token) && (TYPE(username_token) == T_STRING))
            {   
                ut = RSTRING(username_token)->ptr;
                if (ut)
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_HK_USERNAME_TOKEN, 
                                    AXIS2_HASH_KEY_STRING, 
                                    axutil_strdup(env, ut)); 
                }
            }

            encryption = rb_funcall(policy, rb_intern("option"), 1, rb_str_new2(WS_ENCRYPTION));
            
            if (!NIL_P(encryption) && (TYPE(encryption) == T_STRING))
            { 
                encrypt = RSTRING(encryption)->ptr;

                if (encrypt)
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_HK_ENCRYPTION, 
                                    AXIS2_HASH_KEY_STRING, 
                                    axutil_strdup(env, encrypt)); 
                }
            }

            algorithm = rb_funcall(policy, rb_intern("option"), 1, rb_str_new2(WS_ALGORITHM));

            if (!NIL_P(algorithm) && (TYPE(algorithm) == T_STRING))
            {
                algo_suite = RSTRING(algorithm)->ptr;
                
                if (algo_suite)
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_HK_ALGORITHM, 
                                    AXIS2_HASH_KEY_STRING, 
                                    axutil_strdup(env, algo_suite)); 
                }
            }
                     
            sign = rb_funcall(policy, rb_intern("option"), 1, rb_str_new2(WS_SIGNING));
               
            if (!NIL_P(sign) && (TYPE(sign) == T_STRING))
            {
                 sg = RSTRING(sign)->ptr;

                if (sg)
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_HK_SIGNING, 
                                    AXIS2_HASH_KEY_STRING, 
                                    axutil_strdup(env, sg)); 
                }
            }

            token_reference = rb_funcall(policy, rb_intern("option"), 1, rb_str_new2(WS_TOKEN_REFERENCE));

            if (!NIL_P(token_reference) && (TYPE(token_reference) == T_STRING))
            {
                tkn = RSTRING(token_reference)->ptr;
                
                if (tkn)
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_HK_TOKEN_REFERENCE, 
                                    AXIS2_HASH_KEY_STRING, 
                                    axutil_strdup(env, tkn)); 
                }
            }
           
            encrypt_signature = rb_funcall(policy, rb_intern("option"), 1, rb_str_new2(WS_ENCRYPT_SIGNATURE)); 
            
            if (!NIL_P(encrypt_signature) && (TYPE(encrypt_signature) == T_STRING))
            {
                encrypt_sign = RSTRING(encrypt_signature)->ptr;
                
                if (encrypt_sign)
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_HK_ENCRYPT_SIGNATURE, 
                                    AXIS2_HASH_KEY_STRING, 
                                    axutil_strdup(env, encrypt_sign)); 
                }
            }

            protection_order = rb_funcall(policy, rb_intern("option"), 1, rb_str_new2(WS_PROTECTION_ORDER));    

            if (!NIL_P(protection_order) && (TYPE(protection_order) == T_STRING))
            {
                order = RSTRING(protection_order)->ptr;

                if (order)
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_HK_PROTECTION_ORDER, 
                                    AXIS2_HASH_KEY_STRING, 
                                    axutil_strdup(env, order)); 
                }
            }
        }       
    }

    
   
    static void
    wsf_evaluate_policy_and_insert_to_hash(axutil_env_t* env,
                                           VALUE policy,
                                           axutil_hash_t* hash)
    {
        VALUE policy_string;

        policy_string = rb_funcall(policy, rb_intern("get_policy"), 0);
   
        if (!NIL_P(policy_string))
           {
            axis2_char_t* policy_buffer = NULL;
            axis2_char_t* policy_str = NULL;
            policy_str = RSTRING(policy_string)->ptr;

            policy_buffer = axutil_strdup(env, policy_str);
            
            axutil_hash_set(hash, 
                            WSF_WSDL_HK_POLICY_STRING, 
                            AXIS2_HASH_KEY_STRING, 
                            policy_buffer); 
           }
        else
        {    
            axutil_hash_t* policy_hash = NULL;
            policy_hash = axutil_hash_make(env);

            wsf_evaluate_policy_options_and_insert_to_hash(env, policy, policy_hash);

            axutil_hash_set(hash, 
                            WSF_WSDL_HK_POLICY_HASH, 
                            AXIS2_HASH_KEY_STRING, 
                            policy_hash); 
        }
    }

    static void
    wsf_evaluate_client_options_and_insert_to_hash(axutil_env_t* env, 
                                                   axis2_char_t* key,
                                                   VALUE value,
                                                   axutil_hash_t* hash)
    {
        if (!strcmp(key, WS_USE_SOAP))
        {
            VALUE val;
            axis2_char_t* value_str = NULL;

            val = rb_funcall(value, rb_intern("to_s"), 0, 0);
            if (!NIL_P(val) && (TYPE(val) == T_STRING))
            {
                value_str = axutil_strdup(env, StringValuePtr(val));

                if ((!strcmp(value_str, WS_TRUE))
                    || (!strcmp(value_str, WS_SOAP_12)))
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_USE_SOAP, 
                                    AXIS2_HASH_KEY_STRING, 
                                    WSF_WSDL_SOAP_1_2);
                }
                else if (!strcmp(value_str, WS_SOAP_11))
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_USE_SOAP, 
                                    AXIS2_HASH_KEY_STRING, 
                                    WSF_WSDL_SOAP_1_1);    
                }
                else if (!strcmp(value_str, WS_FALSE))
                {
                    axutil_hash_set(hash, 
                                    WSF_WSDL_USE_SOAP, 
                                    AXIS2_HASH_KEY_STRING, 
                                    WSF_WSDL_REST); 
                }
            }
        }
        else if (!strcmp(key, WS_HTTP_METHOD))
        {
        }
        else if (!strcmp(key, WS_RESPONSE_XOP))
        {
        }
        else if (!strcmp(key, WS_TO))
        {
            VALUE val;
            axis2_char_t* value_str = NULL;

            val = rb_funcall(value, rb_intern("to_s"), 0, 0);
            if (!NIL_P(val) && (TYPE(val) == T_STRING))
            {
                value_str = axutil_strdup(env, StringValuePtr(val));
                axutil_hash_set(hash, 
                                  WSF_WSDL_ENDPOINT, 
                                  AXIS2_HASH_KEY_STRING, 
                                  value_str);
            } 
        }
        else if (!strcmp(key, WS_USE_WSA))
        {
            VALUE val;
            axis2_char_t* value_str = NULL;

            val = rb_funcall(value, rb_intern("to_s"), 0, 0);
            if (!NIL_P(val) && (TYPE(val) == T_STRING))
            {
                value_str = axutil_strdup(env, StringValuePtr(val));
                axutil_hash_set(hash, 
                                  WS_USE_WSA, 
                                  AXIS2_HASH_KEY_STRING, 
                                  value_str);
              
            }
        }
        else if (!strcmp(key, WS_POLICY))
        {
            wsf_evaluate_policy_and_insert_to_hash(env, value, hash);
        }
        else if (!strcmp(key, WS_SECURITY_TOKEN))
        {
            axutil_hash_t* security_token_hash = NULL;
            security_token_hash = axutil_hash_make(env);
            wsf_evaluate_security_token_and_insert_to_hash(env, value, security_token_hash);
            
            axutil_hash_set(hash, 
                            WSF_WSDL_HK_SECURITY_TOKEN, 
                            AXIS2_HASH_KEY_STRING, 
                            security_token_hash); 
        }
        else if (!strcmp(key, WS_PROXY_HOST))
        {
        }
        else if (!strcmp(key, WS_PROXY_PORT))
        {
        }
        else if (!strcmp(key, WS_WSDL))
        {
        }
    }

    static VALUE
    hash_options_callback (VALUE key_val_array, void** callback_data)
    {
        axutil_env_t* env = NULL;
        axutil_hash_t* hash = NULL;
        VALUE key;
        VALUE value;

        env = (axutil_env_t*)(callback_data[0]);
        hash = (axutil_hash_t*)(callback_data[1]);

        key = rb_ary_entry(key_val_array, 0);               
        value = rb_ary_entry(key_val_array, 1);             

        /*if (TYPE(key) == T_SYMBOL)
        {
            key = rb_sym2str(key);                            
        }*/
        
        if (!NIL_P(key) && !NIL_P(value))
        {
            axis2_char_t* key_str = NULL;

            key_str = axutil_strdup(env, StringValuePtr(key));

            wsf_evaluate_client_options_and_insert_to_hash(env, key_str, value, hash);
        }

        return Qnil;
    }

    static VALUE
    hash_callback (VALUE key_val_array, void** callback_data)
    {
        axutil_env_t* env = NULL;
        wsf_wsdl_data_t* data = NULL;
        VALUE key;
        VALUE value;

        env = (axutil_env_t*)(callback_data[0]);
        data = (wsf_wsdl_data_t*)(callback_data[1]);

        key = rb_ary_entry(key_val_array, 0);               
        value = rb_ary_entry(key_val_array, 1);             

        /*if (TYPE(key) == T_SYMBOL)
        {
            key = rb_sym2str(key);                            
        }*/
        
        if (!NIL_P(key) && !NIL_P(value))
        {
            axis2_char_t* key_str = NULL;
            axis2_char_t* value_str = NULL;

            key_str = axutil_strdup(env, StringValuePtr(key));

            if (TYPE(value) == T_HASH)
            {
                wsf_wsdl_data_t* sub_data = NULL;
                sub_data = wsf_wsdl_convert_params_to_wsdl_data(env, value);
                wsdl_data_add_object(env, data, key_str, "whatever", sub_data, "http://www.example.org/sample/", NULL);
            }
            else
            {
                axis2_char_t* type_str = NULL;
                VALUE type_val;
                VALUE type_val_str;
                VALUE val;
                val = rb_funcall(value, rb_intern("to_s"), 0, 0);
                type_val = rb_funcall(value, rb_intern("class"), 0, 0);
                type_val_str = rb_funcall(type_val, rb_intern("to_s"), 0, 0);

                if (!NIL_P(val) && (TYPE(val) == T_STRING))
                {
                    value_str = axutil_strdup(env, StringValuePtr(val));
                    type_str = axutil_strdup(env, StringValuePtr(type_val_str));
                    wsdl_data_add_simple_element(env, data, key_str, type_str, value_str, "http://www.w3.org/2001/XMLSchema", "http://www.example.org/sample/");
                    /*AXIS2_FREE(env->allocator, value_str);*/
                }
            }
            
            /*AXIS2_FREE(env->allocator, key_str);*/
        }
        else
        {
            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] key and value should not be NULL");
        }

        return Qnil;
    }

    wsf_wsdl_data_t*
    wsf_wsdl_convert_params_to_wsdl_data(axutil_env_t* env, VALUE params)
    {
        wsf_wsdl_data_t* data = NULL;
        void** callback_data = AXIS2_MALLOC(env->allocator, sizeof(void*) * 2);
        callback_data[0] = env;

        data = wsdl_data_create_object(env);
        callback_data[1] = data;
        
        if (!NIL_P(params))
        {
            if (TYPE(params) == T_HASH)
            {
                rb_iterate (rb_each, params, hash_callback, (VALUE)callback_data);
            }
            else /*if (TYPE(params) == T_OBJECT)*/
            {
                /* TODO: implement this */
            }
        }

        AXIS2_FREE(env->allocator, callback_data);

        return data;
    }

    VALUE
    wsf_wsdl_convert_wsdl_data_to_hash(axutil_env_t* env, wsf_wsdl_data_t* response);

    VALUE
    wsf_wsdl_convert_response_to_ruby(axutil_env_t* env, wsf_wsdl_data_t* response)
    {
        VALUE hash = wsf_wsdl_convert_wsdl_data_to_hash(env, response);
        return hash;
    }

    VALUE
    wsf_wsdl_convert_wsdl_data_to_hash(axutil_env_t* env, wsf_wsdl_data_t* response)
    {
        VALUE element = Qnil;
        wsf_wsdl_data_iterator_t* iterator = NULL;

        if(response == NULL) {
          return Qnil;
        }
        
        if (response->children_type == CHILDREN_TYPE_NONE)
        {
            axis2_char_t* data = (axis2_char_t*)(response->data);
            
            if (strcmp(data, "String") == 0)
            {
                element = rb_str_new2(data);
            }
            else if (strcmp(data, "Fixnum") == 0)
            {
                int fixnum = atoi(data);
                element = rb_fix_new(fixnum);
            }
            else if (strcmp(data, "Float") == 0)
            {
                int floatnum = atof(data);
                element = rb_float_new(floatnum);
            }
            else
            {
                element = rb_str_new2(data);
            }
            
            return element;
        }

        iterator = wsdl_data_iterator_create(env, response);

        if (!wsdl_data_iterator_first(env, &iterator))
            return Qnil;

        if (iterator->type == CHILDREN_TYPE_ARRAY_ELEMENTS)
        {
            element = rb_ary_new();
            do
            {
                VALUE sub_element;
                wsf_wsdl_data_t* data = iterator->this;
                
                sub_element = wsf_wsdl_convert_wsdl_data_to_hash(env, data);

                rb_ary_push(element, sub_element);

            } while (wsdl_data_iterator_next(env, &iterator));
        }
        else if (iterator->type == CHILDREN_TYPE_ATTRIBUTES)
        {
            element = rb_hash_new();
            do
            {
                VALUE sub_element;
                VALUE key;
                wsf_wsdl_data_t* data = iterator->this;

                sub_element = wsf_wsdl_convert_wsdl_data_to_hash(env, data);
                key = rb_tainted_str_new2(iterator->name);

                rb_hash_aset(element, key, sub_element);

            } while (wsdl_data_iterator_next(env, &iterator));
        }

        wsdl_data_iterator_free(env, iterator);

        return element;
    }

    VALUE
    wsf_wsdl_request_function(axutil_env_t* env, axis2_svc_client_t* svc_client, 
                              VALUE options,                /* RUBY client options hash*/
                              VALUE wsdl,                    /* wsdl file */
                              VALUE operation, VALUE arg,    /* operation name and arg */
                              VALUE service, 
                              VALUE port,
                              VALUE ruby_home)                /* ruby_home */
    {
        wsf_wsdl_data_t* response = NULL;            /* cleaned */
        wsf_wsdl_data_t* user_parameters = NULL;    /* TODO: NOT cleaned */
        axis2_bool_t wsdl_request_success;        
        axis2_char_t* wsdl_file_name = NULL;            /* cleaned */                
        axis2_char_t* operation_name = NULL;            /* cleaned */            
        axis2_char_t* ruby_home_string = NULL;                /* cleaned */ 
        axutil_hash_t* svc_client_options = NULL;        /* TODO: NOT cleaned */
        axis2_char_t* service_name = NULL;
        axis2_char_t* port_name = NULL;

        AXIS2_LOG_INFO(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] calling wsf_wsdl_request_function....");
        
        /* wsdl_file_name */
        if (!NIL_P(wsdl) && (TYPE(wsdl) == T_STRING))
        {
            axis2_char_t* wsdl_file_name_ruby;

            wsdl_file_name_ruby = RSTRING(wsdl)->ptr;

            if (!wsdl_file_name_ruby)
            {
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] wsdl could not be null");
                return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
            }

            wsdl_file_name = AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * strlen(wsdl_file_name_ruby) + 1);
            wsdl_file_name = strcpy(wsdl_file_name, wsdl_file_name_ruby);
        }
        else
        {
            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] wsdl should be a STRING and could not be null");
            return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
        }

        /* operation_name */
        if (!NIL_P(operation) && (TYPE(operation) == T_STRING))
        {
            axis2_char_t* operation_name_ruby;
            
            operation_name_ruby = RSTRING(operation)->ptr;

            if (!operation_name_ruby)
            {
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] operation name could not be null");
                return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
            }

            operation_name = AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * strlen(operation_name_ruby) + 1);
            operation_name = strcpy(operation_name, operation_name_ruby);
        }
        else
        {
            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] operation name should be a STRING and could not be null");
            return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
        }

        /* user_parameters */
        if (!NIL_P(arg))
        {
            user_parameters = wsf_wsdl_convert_params_to_wsdl_data(env, arg);
        }
        else
        {
            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] params should not be NULL");
            return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
        }

        /* type_map_file */
        if (!NIL_P(ruby_home) && (TYPE(ruby_home) == T_STRING))
        {
            axis2_char_t* ruby_home_string_temp;        
            
            ruby_home_string_temp = RSTRING(ruby_home)->ptr;

            if (!ruby_home_string_temp)
            {
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] type_map could not be null");
                return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
            }

            ruby_home_string = AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * strlen(ruby_home_string_temp) + 1);
            ruby_home_string = strcpy(ruby_home_string, ruby_home_string_temp);
        }
        else
        {
            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] type_map should be a STRING and could not be null");
            return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
        }

        /* service name */
        if (!NIL_P(service) && (TYPE(service) == T_STRING))
        {
            axis2_char_t* service_name_ruby;
            
            service_name_ruby = RSTRING(service)->ptr;

            if (!service_name_ruby)
            {
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] service name is null");
                service_name = NULL;
            }
            else
            {
                service_name = AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * strlen(service_name_ruby) + 1);
                service_name = strcpy(service_name, service_name_ruby);
            }
        }
        else
        {
            AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] service name is null");
            service_name = NULL;
        }

        /* port name */
        if (!NIL_P(port) && (TYPE(port) == T_STRING))
        {
            axis2_char_t* port_name_ruby;
            
            port_name_ruby = RSTRING(port)->ptr;

            if (!port_name_ruby)
            {
                AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] port name is null");
                port_name = NULL;
            }
            else
            {
                port_name = AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * strlen(port_name_ruby) + 1);
                port_name = strcpy(port_name, port_name_ruby);
            }
        }
        else
        {
            AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] port name is null");
            port_name = NULL;
        }

        svc_client_options = NULL;

        if (!NIL_P(options) && (TYPE(options) == T_HASH))
        {
            void** callback_data = AXIS2_MALLOC(env->allocator, sizeof(void*) * 2);
            callback_data[0] = env;

            svc_client_options = axutil_hash_make(env);
            callback_data[1] = svc_client_options;
            
            rb_iterate (rb_each, options, hash_options_callback, (VALUE)callback_data);

            AXIS2_FREE(env->allocator, callback_data);
        }

        /* svc_client_options */
                            

        wsdl_request_success = wsf_wsdl_request(env, 
                                                wsdl_file_name, 
                                                operation_name, 
                                                user_parameters,
                                                ruby_home_string,
                                                svc_client,
                                                svc_client_options,
                                                service_name,
                                                port_name,
                                                &response);

        if (!wsdl_request_success)
        {
            AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] wsf_wsdl_request returned fault");
            return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
        }

        /* response */
        if (response)
        {
            VALUE response_ruby;
            response_ruby = wsf_wsdl_convert_response_to_ruby(env, response);
            return response_ruby;
        }
        else
        {
            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "[wsf_ruby] [wsdl_mode] wsf_wsdl_request response is NULL");
            return wsf_wsdl_request_function_mem_free_return(env, response, user_parameters, wsdl_file_name, operation_name, ruby_home_string, svc_client_options);
        }
        
        return Qnil;
    }
%}
