/*
* Copyright 2005,2008 WSO2, Inc. http://wso2.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

#include <axis2_addr.h>
#include <axutil_error_default.h>
#include <axutil_log_default.h>
#include <axutil_uuid_gen.h>
#include <axiom_util.h>
#include <axiom.h>
#include <axiom_soap_envelope.h>
#include <rampart_context.h>
#include <neethi_options.h>
#include <axis2_policy_include.h>
#include <neethi_engine.h>

#include "wsf_wsdl_mode.h"

/** 
* initializes the service and create the wsdl_info_t structure */
WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_initialize_for_service(
    const axutil_env_t* env,
    axis2_char_t* wsdl_file_name,
    axis2_char_t* script_binding_home,
    axis2_svc_t* service,
    axis2_conf_ctx_t* worker_conf_ctx,
    axutil_hash_t* script_service_user_options,
    axis2_char_t* service_name,
    axis2_char_t* port_name,
    wsf_wsdl_info_t** wsdl_info)
{
    axiom_node_t* type_map = NULL;	
    axiom_node_t* sig_axiom = NULL;	
    axiom_node_t* wsdl_axiom = NULL;	
    axutil_hash_t* operations = NULL;
    axis2_bool_t success = AXIS2_FAILURE;
    wsf_wsdl_info_t* info = NULL;
    axis2_bool_t is_version1_wsdl = AXIS2_FALSE;
    axis2_char_t* xslt_location = NULL;
    axis2_char_t* type_map_file = NULL;

    xslt_location = axutil_strcat(env, script_binding_home, WSF_WSDL_XSLT_LOCATION_POSTFIX, NULL);
    type_map_file = axutil_strcat(env, script_binding_home, WSF_WSDL_TYPE_MAP_POSTFIX, NULL);

    if (wsf_wsdl_parser_load_wsdl(env, 
                                  wsdl_file_name, 
                                  xslt_location, 
                                  &wsdl_axiom, 
                                  &sig_axiom, 
                                  &is_version1_wsdl, 
                                  AXIS2_TRUE))
    {

        wsf_wsdl_util_handle_service_security(env,
                                              service,
                                              worker_conf_ctx,
                                              script_service_user_options,
                                              service_name,
                                              port_name,
                                              wsdl_axiom,
                                              is_version1_wsdl);
                                          

        wsdl_util_create_type_map(env, type_map_file, &type_map);
        wsdl_util_get_operations(env, sig_axiom, &operations);

        if (wsdl_info)
        {
            if (*wsdl_info)
            {       
                AXIS2_FREE(env->allocator, *wsdl_info);
            }

            info = (wsf_wsdl_info_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_info_t));

            info->type_map = type_map;
            info->operations = operations;

            *wsdl_info = info;

            success = AXIS2_SUCCESS;
        }
    }   

    return success;
}


/**
* validates an axiom against a given template. this is a high level exposed function using wsdl_data_util_validate_data. 
* validation depends on VALIDATION_CRITERIA_* defines */
WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_validate_axiom(
    const axutil_env_t* env, 
    axiom_node_t* type_map,
    wsf_wsdl_data_template_t* templ,
    axiom_node_t* node,
    unsigned short criteria,
    wsf_wsdl_data_t** data)
{
    wsdl_data_util_axiom_to_data(env, node, data);
    return wsdl_data_util_validate_data(env, type_map, templ, *data, criteria);
}


/**
* High level exposed function to validate data against the template, it is expected not to expose wsdl_data_util_validate_data.*/

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_validate_data(
    const axutil_env_t* env, 
    axiom_node_t* type_map,
    wsf_wsdl_data_template_t* templ,
    wsf_wsdl_data_t* data,
    unsigned short criteria)
{
    return wsdl_data_util_validate_data(env, type_map, templ, data, criteria);
}


/**
* client request with the knowledge of the wsdl_info_t structure */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_client_request(
    const axutil_env_t* env,
    axis2_svc_client_t* svc_client,
    const axis2_char_t* operation_name,	
    wsf_wsdl_data_t* user_parameters,
    wsf_wsdl_info_t* info,
    wsf_wsdl_data_t** response)
{
    /* TODO: implement this */
    env = NULL;
    svc_client = NULL;
    operation_name = NULL;
    user_parameters = NULL;
    info = NULL;
    *response = NULL;
    return AXIS2_FAILURE;
}


/**
* intialize the client and generate the wsdl_info_t structure */
//
//WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL 
//wsf_wsdl_request(
//                 const axutil_env_t* env,
//                 axis2_char_t* wsdl_file_name,
//                 const axis2_char_t* operation_name,	
//                 wsf_wsdl_data_t* parameters,
//                 axis2_char_t* script_binding_home,
//                 axis2_svc_client_t* svc_client,
//                 axutil_hash_t* svc_client_user_options,
//                 axis2_char_t* service_name,
//                 axis2_char_t* port_name,
//                 wsf_wsdl_data_t** response)

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_initialize_for_client(
    const axutil_env_t* env,
    const axis2_char_t* wsdl_file_name,
    axis2_char_t* script_binding_home,
    axis2_svc_client_t* svc_client,
    axutil_hash_t* svc_client_user_options,
    wsf_wsdl_info_t** wsdl_info)
{
    /* TODO: implement this */
    env = NULL;
    wsdl_file_name = NULL;
    script_binding_home = NULL;
    svc_client = NULL;
    svc_client_user_options = NULL;
    wsdl_info = NULL;
    return AXIS2_FAILURE;
}


/** 
* creating a response payload node from a given wsf_wsdl_data_t structure */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_create_response_node(
    const axutil_env_t* env,
    wsf_wsdl_data_t* data,
    axiom_node_t** response_node)
{
    axiom_node_t* node = NULL;
    node = wsdl_data_util_create_payload(env, data, -1);

    *response_node = node;

    return node? AXIS2_SUCCESS : AXIS2_FAILURE;
}


/**
* following function is deprecated.
* executing the request operation with parsing wsdl, this function is devided into above 2 functions,  wsf_wsdl_mode_initialize_for_client 
* and wsf_wsdl_mode_client_request */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL 
wsf_wsdl_request(
    const axutil_env_t* env,
    axis2_char_t* wsdl_file_name,
    const axis2_char_t* operation_name,	
    wsf_wsdl_data_t* parameters,
    axis2_char_t* script_binding_home,
    axis2_svc_client_t* svc_client,
    axutil_hash_t* svc_client_user_options,
    axis2_char_t* service_name,
    axis2_char_t* port_name,
    wsf_wsdl_data_t** response)
{
    int soap_version = 2; 
    int has_fault = AXIS2_FALSE;
    int binding_style = WSDL_BINDING_STYLE_DOC_LIT_W;
    axis2_char_t* payload = NULL;	
    axiom_node_t* payload_node = NULL;
    axis2_char_t* request_buffer = NULL;	
    axiom_node_t* type_map = NULL;	
    axis2_char_t *res_text = NULL;	
    axiom_node_t* sig_axiom = NULL;	
    axiom_node_t *fault_node = NULL;	
    axiom_node_t* wsdl_axiom = NULL;	
    axiom_node_t* params_node = NULL;	
    axiom_node_t* returns_node = NULL;	
    axiom_soap_body_t *soap_body = NULL;	
    axiom_node_t *body_base_node = NULL;	
    /*axis2_char_t *wrapper_element = NULL;*/	
    axiom_node_t* operation_axiom = NULL;	
    axiom_soap_fault_t *soap_fault = NULL;	
    axis2_options_t *client_options = NULL;	
    /*axis2_char_t *wrapper_element_ns = NULL;*/	
    /*axiom_node_t *axiom_soap_base_node = NULL;	*/
    axiom_soap_envelope_t *response_envelope = NULL;	
    wsf_wsdl_data_template_t* input_template = NULL;	
    wsf_wsdl_data_template_t* output_template = NULL;	
    axis2_bool_t is_version1_wsdl = AXIS2_FALSE;
    axis2_char_t* xslt_location = NULL;
    axis2_char_t* type_map_file = NULL;

    AXIS2_LOG_TRACE_MSG(env->log, "Starting execution of wsf_wsdl_request..."); 

    xslt_location = axutil_strcat(env, script_binding_home, WSF_WSDL_XSLT_LOCATION_POSTFIX, NULL);
    type_map_file = axutil_strcat(env, script_binding_home, WSF_WSDL_TYPE_MAP_POSTFIX, NULL);

    client_options = (axis2_options_t *)axis2_svc_client_get_options(svc_client, env);

    axis2_options_set_xml_parser_reset(client_options, env, AXIS2_FALSE);

    if (!wsf_wsdl_parser_load_wsdl(env, wsdl_file_name, xslt_location, &wsdl_axiom, &sig_axiom, &is_version1_wsdl, AXIS2_FALSE))
        return AXIS2_FALSE;

    wsdl_util_create_type_map(env, type_map_file, &type_map);

    wsdl_util_manage_client_options(env, 
        svc_client, 
        svc_client_user_options, 
        client_options, 
        operation_name, 
        wsdl_axiom, 
        is_version1_wsdl, 
        sig_axiom, 
        service_name,
        port_name,
        &operation_axiom, 
        &soap_version);

    wsdl_util_identify_binding_style(env, operation_axiom, &binding_style);

    if (!operation_axiom)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Operation axiom is NULL");

        if (wsdl_axiom) 
            axiom_node_free_tree(wsdl_axiom, env);
        if (sig_axiom)
            axiom_node_free_tree(sig_axiom, env);
        if (type_map)
            axiom_node_free_tree(type_map, env);
        return AXIS2_FALSE;
    }

    wsdl_util_get_params_node(env, operation_axiom, &params_node);
    wsdl_util_get_returns_node(env, operation_axiom, &returns_node);

    wsdl_data_util_axiom_to_template(env, params_node, &input_template);
    wsdl_data_util_axiom_to_template(env, returns_node, &output_template);

#ifdef WSDL_DEBUG_MODE
    {
        axis2_char_t* buffer = NULL;
        wsdl_data_util_serialize_data(env, parameters, &buffer);
        AXIS2_LOG_DEBUG_MSG(env->log, buffer);
        AXIS2_FREE(env->allocator, buffer);
        wsdl_data_util_serialize_template(env, input_template, &buffer);
        AXIS2_LOG_DEBUG_MSG(env->log, buffer);
        AXIS2_FREE(env->allocator, buffer);
        buffer = NULL;
    }
#endif

    if (wsdl_data_util_validate_data(env, type_map, input_template, parameters, VALIDATION_CRITERIA_REQUEST_MODE_TYPE) == AXIS2_TRUE)
    {
        payload_node = wsdl_data_util_create_payload(env, parameters, binding_style);
        payload = payload_node? axiom_node_to_string(payload_node, env) : NULL;
        // axiom_node_free_tree(payload_node, env);
    }
    else
    {
        AXIS2_LOG_ERROR_MSG(env->log, "User data is not valid or not in proper format");

        if (input_template)
            wsdl_data_template_free(env, input_template);
        if (output_template)
            wsdl_data_template_free(env, output_template);

        if (wsdl_axiom) 
            axiom_node_free_tree(wsdl_axiom, env);
        if (sig_axiom)
            axiom_node_free_tree(sig_axiom, env);
        if (type_map)
            axiom_node_free_tree(type_map, env);
        return AXIS2_FALSE;
    }

    if (!payload_node)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Request payload is not found");

        if (input_template)
            wsdl_data_template_free(env, input_template);
        if (output_template)
            wsdl_data_template_free(env, output_template);

        if (wsdl_axiom) 
            axiom_node_free_tree(wsdl_axiom, env);
        if (sig_axiom)
            axiom_node_free_tree(sig_axiom, env);
        if (type_map)
            axiom_node_free_tree(type_map, env);
        return AXIS2_FALSE; 
    }

    request_buffer = (axis2_char_t*)wsdl_util_create_request_envelope(env, payload_node, soap_version);

    if (!request_buffer)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Error in creating request payload dom");

        if (input_template)
            wsdl_data_template_free(env, input_template);
        if (output_template)
            wsdl_data_template_free(env, output_template);

        if (wsdl_axiom) 
            axiom_node_free_tree(wsdl_axiom, env);
        if (sig_axiom)
            axiom_node_free_tree(sig_axiom, env);
        if (type_map)
            axiom_node_free_tree(type_map, env);

        return AXIS2_FALSE;
    }

    response_envelope = (axiom_soap_envelope_t*)wsdl_util_send_receive_soap_envelope_with_op_client(env, 
        svc_client, 
        client_options, 
        request_buffer);
    if (response_envelope) 
    {
        has_fault = AXIS2_TRUE;
        soap_body = axiom_soap_envelope_get_body (response_envelope, env);

        if (soap_body)
        {
            soap_fault = axiom_soap_body_get_fault (soap_body, env);
        }

        if (soap_fault) 
        {
            soap_version = axis2_options_get_soap_version(client_options, env);
            fault_node = axiom_soap_fault_get_base_node(soap_fault, env);
            if (fault_node)
            {
                res_text = axiom_node_to_string(fault_node, env);
                AXIS2_LOG_DEBUG (env->log, AXIS2_LOG_SI, "Fault payload is %s", res_text);

                if (input_template)
                    wsdl_data_template_free(env, input_template);
                if (output_template)
                    wsdl_data_template_free(env, output_template);
                if (wsdl_axiom) 
                    axiom_node_free_tree(wsdl_axiom, env);
                if (sig_axiom)
                    axiom_node_free_tree(sig_axiom, env);
                if (type_map)
                    axiom_node_free_tree(type_map, env);

                return AXIS2_FALSE;
            }    
        }

        if (soap_body)
        {
            body_base_node = axiom_soap_body_get_base_node(soap_body, env);
        }

        if (body_base_node && !soap_fault)
        {
            axis2_char_t *response_buffer = NULL;	
            wsf_wsdl_data_t *response_data = NULL;

            response_buffer = axiom_node_to_string(body_base_node, env);
            AXIS2_LOG_DEBUG (env->log, AXIS2_LOG_SI,
                "Response buffer is %s", response_buffer);

            AXIS2_FREE(env->allocator, response_buffer);

            wsdl_data_util_axiom_to_data(env, body_base_node, &response_data);

#ifdef WSDL_DEBUG_MODE
            {
                axis2_char_t* buffer = NULL;
                wsdl_data_util_serialize_data(env, response_data, &buffer);
                AXIS2_LOG_DEBUG_MSG(env->log, buffer);
                AXIS2_FREE(env->allocator, buffer);
                wsdl_data_util_serialize_template(env, output_template, &buffer);
                AXIS2_LOG_DEBUG_MSG(env->log, buffer);
                AXIS2_FREE(env->allocator, buffer);
                buffer = NULL;
            }
#endif

            if (wsdl_data_util_validate_data(env, type_map, output_template, response_data, VALIDATION_CRITERIA_RESPONSE_MODE) == AXIS2_TRUE)
            {
                AXIS2_LOG_DEBUG_MSG(env->log, "Valid response!!!");
                *response = response_data;

                if (input_template)
                    wsdl_data_template_free(env, input_template);
                if (output_template)
                    wsdl_data_template_free(env, output_template);
                if (wsdl_axiom) 
                    axiom_node_free_tree(wsdl_axiom, env);
                if (sig_axiom)
                    axiom_node_free_tree(sig_axiom, env);
                if (type_map)
                    axiom_node_free_tree(type_map, env);

                return AXIS2_TRUE;
            }
            else
            {
                AXIS2_LOG_ERROR_MSG(env->log, "Response data is not valid or not in proper format");

                if (input_template)
                    wsdl_data_template_free(env, input_template);
                if (output_template)
                    wsdl_data_template_free(env, output_template);
                if (wsdl_axiom) 
                    axiom_node_free_tree(wsdl_axiom, env);
                if (sig_axiom)
                    axiom_node_free_tree(sig_axiom, env);
                if (type_map)
                    axiom_node_free_tree(type_map, env);

                return AXIS2_FALSE;
            }
        }
    }
    else  /* response_envelope == NULL */
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Response envelope not found");
    }

    if (input_template)
        wsdl_data_template_free(env, input_template);
    if (output_template)
        wsdl_data_template_free(env, output_template);

    if (wsdl_axiom) 
        axiom_node_free_tree(wsdl_axiom, env);
    if (sig_axiom)
        axiom_node_free_tree(sig_axiom, env);
    if (type_map)
        axiom_node_free_tree(type_map, env);

    return AXIS2_FALSE;
}


