/*
 * Copyright 2005-2008 WSO2, Inc. http://wso2.com
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

#ifndef WSF_WSDL_UTIL_H
#define WSF_WSDL_UTIL_H

/** 
* loads a wsdl and apply xslt transformation to generate sig, returns its axis2_char_t* buffer and 
* length. 
* @param env, environment structure
* @param source_wsdl_file, full path to the wsdl file
* @param sig_stream, pointer to the sig stream buffer, caller 
* @param sig_stream_len, length of the stream
* @param xslt_location, xslt location
* @param is_version1_wsdl, returns the wsdl version
* @param is_service_call, specify the call type: service or client
* @returns AXIS2_SUCCESS on success, else AXIS2_FAILURE 
*/
WSF_WSDL_EXTERN axis2_status_t WSF_WSDL_CALL 
wsf_wsdl_util_apply_xslt(
    const axutil_env_t* env, 
    axis2_char_t* source_wsdl_file, 
    axis2_char_t* xslt_location,
    axis2_bool_t is_service_call,
    axis2_bool_t* is_version1_wsdl,
    axiom_node_t** wsdl_axiom,
    axiom_node_t** sig_axiom);

axis2_status_t
wsf_wsdl_xslt_apply(
    const axutil_env_t* env,
    const axis2_char_t* source_xml_file,
    axis2_char_t* target_xml_file,
    axis2_char_t* xslt_file);

axis2_status_t
wsf_wsdl_xslt_create_long_file_names(
    const axutil_env_t* env,
    axis2_char_t* xslt_file_location,
    axis2_bool_t is_service_call,
    axis2_char_t** wsdl_1to2_xslt_file,
    axis2_char_t** wsdl_2tosig_xslt_file,
    axis2_char_t** intermediate_wsdl_file,
    axis2_char_t** resultant_sig_file);

//////////////////
/// parser /////////////////////////////////////////////////////////////////////////////////////////
////////////////

axis2_status_t
wsf_wsdl_util_get_all_policies_from_wsdl(
    const axutil_env_t* env, 
    axiom_node_t* wsdl_axiom,
    axis2_bool_t is_version1_wsdl,
    axiom_node_t* binding_node,
    const axis2_char_t* operation_name,
    axutil_hash_t** wsdl_policy_hash);

axis2_char_t* 
wsf_axiom_get_attribute_val_of_node_by_qname(
    const axutil_env_t *env,
    axiom_node_t *node,
    axutil_qname_t *qname);

axis2_char_t* 
wsf_axiom_get_attribute_value_of_node_by_name(
    const axutil_env_t *env,
    axiom_node_t *node,
    axis2_char_t *attribute_name,
    axis2_char_t *ns);

axis2_status_t
wsf_wsdl_util_get_policy(
    const axutil_env_t* env,
    axiom_node_t* wsdl_axiom,
    axis2_char_t* policy_uri,
    axiom_node_t** policy_node);

/** 
* get all policies included in operation node.
* @param env, environment structure
* @param wsdl_axiom, source wsdl axiom.
* @param operation_node, operation node from wsdl
* @param wsdl_policy_hash, target hash to contain policies. Allocation should be done by caller.
* @returns AXIS2_SUCCESS on success, else AXIS2_FAILURE 
*/
axis2_status_t
wsf_wsdl_fill_policy_hash_from_operation_node_policies(
    const axutil_env_t* env,
    axiom_node_t* wsdl_axiom,
    axiom_node_t* operation_node, 
    axutil_hash_t* wsdl_policy_hash);

axis2_status_t
wsf_wsdl_get_policy_using_policy_attribute(
    const axutil_env_t* env, 
    axiom_node_t* binding_node,
    axiom_node_t* wsdl_axiom,
    axiom_node_t** binding_policy);

axis2_status_t
get_policy_using_policy_reference_attribute(
    const axutil_env_t* env, 
    axiom_node_t* binding_node, 
    axiom_node_t* wsdl_axiom,
    axiom_node_t** binding_policy);

/* caller should clean operation_name */
axis2_status_t 
wsf_wsdl_parser_extract_operation_name( 
    const axutil_env_t* env, 
    axiom_node_t* operation_node, 
    axis2_bool_t is_version1_wsdl, 
    axis2_char_t** operation_name);

/** 
* get all policies to a hash, key -> operation name; value -> hash of policies
* @param env, environment structure
* @param wsdl_axiom, wsdl axiom
* @param binding_node, binding node,
* @param is_version1_wsdl, AXIS2_TRUE for version 1 wsdls, AXIS2_FALSE otherwise
* @param wsdl_hash_of_policy_hash, a hash of policy hashes. [key, value]=[operation_name, hash]
* @returns AXIS2_SUCCESS on success, else AXIS2_FAILURE 
*/
axis2_status_t
wsf_wsdl_util_get_all_policies(
    const axutil_env_t* env, 
    axiom_node_t* wsdl_axiom,
    axiom_node_t* binding_node,
    axis2_bool_t is_version1_wsdl,
    axutil_hash_t** wsdl_hash_of_policy_hash);

/** 
* gets the corresponding binding node when a port node is given
* @param env, environment structure
* @param port_node, port node given
* @param is_version1_wsdl, flag to specify wsdl version
* @param wsdl_axiom, the wsdl node
* @param binding_node, binding node corresponding to the port node
* @returns AXIS2_SUCCESS on success, else AXIS2_FAILURE 
*/
axis2_status_t
wsf_wsdl_parser_get_binding_node_from_port_node(
    const axutil_env_t* env,
    axiom_node_t* port_node,
    axis2_bool_t is_version1_wsdl,
    axiom_node_t* wsdl_axiom,
    axiom_node_t** binding_node);

/** 
* get all binding nodes corresponding to service.
* @param env, environment structure
* @param service_node, service node
* @param wsdl_axiom, wsdl axiom
* @param is_version1_wsdl, flag to specify wsdl version
* @param ports, a hash with, key: port name, value: binding node. caller should create the hash.
* @returns AXIS2_SUCCESS on success, else AXIS2_FAILURE 
*/
axis2_status_t
wsf_wsdl_parser_get_binding_nodes_under_service(
    axutil_env_t* env,
    axiom_node_t* service_node,
    axiom_node_t* wsdl_axiom,
    axis2_bool_t is_version1_wsdl,
    axutil_hash_t** ports);

/** 
* get all binding nodes corresponding to services and ports. 
* @param env, environment structure
* @param wsdl_axiom, wsdl axiom
* @param is_version1_wsdl, flag to specify wsdl version
* @param services, hash containing (key: service name, value: hash -> caller should free this) 
*                  hashes (key: port name, value : binding nodes) of binding nodes.                   
*                  caller should create the parent hash.
* @returns AXIS2_SUCCESS on success, else AXIS2_FAILURE 
*/
axis2_status_t
wsf_wsdl_parser_get_binding_nodes(
    axutil_env_t* env,
    axiom_node_t* wsdl_axiom,
    axis2_bool_t is_version1_wsdl,
    axutil_hash_t** services);

axis2_status_t
wsf_wsdl_util_get_binding_node(
    const axutil_env_t* env,
    axiom_node_t* wsdl_axiom, 
    axis2_bool_t is_version1_wsdl, 
    axis2_char_t* service_name, 
    axis2_char_t* port_name,
    axiom_node_t** binding_node);

axis2_bool_t
wsdl_util_get_operations(
    const axutil_env_t* env, 
    axiom_node_t* sig_axiom, 
    axutil_hash_t** operations);

/**
* loads the wsdl and create sig_axiom and wsdl_axiom */
axis2_status_t
wsf_wsdl_parser_load_wsdl(
    const axutil_env_t* env, 
    axis2_char_t* wsdl_file_name, 
    axis2_char_t* xslt_location,
    axiom_node_t** wsdl_axiom,
    axiom_node_t** sig_axiom,
    axis2_bool_t* is_version1_wsdl,
    axis2_bool_t is_service_call);

/**
* retrieves the endpoint address */
axis2_bool_t
wsdl_util_get_endpoint_address(
    axiom_node_t* sig_axiom, 
    const axutil_env_t* env, 
    axis2_char_t** endpoint_address);

/**
* finds and returns the operation axiom */
axis2_bool_t
wsdl_util_find_operation(
    const axutil_env_t* env,
    const axis2_char_t* operation_name, 
    axis2_char_t* endpoint_address,
    axis2_bool_t is_multiple,
    axiom_node_t* sig_axiom,
    axiom_node_t** operation_axiom,
    int* soap_version);

/** 
* extracting binding details from the operation_axiom */

axis2_bool_t
wsdl_util_get_binding_details(
    const axutil_env_t* env,
    axiom_node_t* operation_axiom,
    axis2_char_t** wsa_action, axis2_char_t** soap_action);
/**
* retrieving the params axiom from operation axiom */

axis2_bool_t
wsdl_util_get_params_node(
    const axutil_env_t* env, 
    axiom_node_t* operation_axiom, 
    axiom_node_t** params);

/** 
* retrieving the returns axiom from operation (sig) axiom */

axis2_bool_t
wsdl_util_get_returns_node(
    const axutil_env_t* env, 
    axiom_node_t* operation_axiom, 
    axiom_node_t** params);

/** 
* retrieving params or returns axioms from operation (sig) axiom */

axis2_bool_t
wsdl_util_get_params_or_returns_node(
    const axutil_env_t* env, 
    axiom_node_t* operation_axiom, 
    axis2_char_t* node_in_need, 
    axiom_node_t** params);
/**
* identifies the binding style of the operation axiom */

axis2_bool_t
wsdl_util_identify_binding_style(
    const axutil_env_t* env, 
    axiom_node_t* operation_axiom, 
    int* binding_style);

/** 
* retrieves the wrapper element and its namespace from the params node */
axis2_bool_t
wsdl_util_get_wrapper_element(
    const axutil_env_t* env, 
    axiom_node_t* params_node, 
    axis2_char_t** wrapper_element, 
    axis2_char_t** wrapper_element_ns);

axis2_status_t
wsf_wsdl_parser_determine_wsdl_version(
    const axutil_env_t* env,
    axiom_node_t* wsdl_node,
    axis2_bool_t* is_version1_wsdl);


////////////
/// util ///////////////////////////////////////////////////////////////////////////////////////////
////////////

void
wsdl_util_manage_client_options(
    const axutil_env_t* env, 
    axis2_svc_client_t* svc_client,
    axutil_hash_t* svc_client_user_options, 
    axis2_options_t* client_options, 
    const axis2_char_t* operation_name,
    axiom_node_t* wsdl_axiom,
    axis2_bool_t is_version1_wsdl,
    axiom_node_t* sig_axiom, 
    axis2_char_t* service_name,
    axis2_char_t* port_name,
    axiom_node_t** operation_axiom,
    int* soap_version);

/** 
* creates the request envelope for the payload provided */
axis2_char_t* 
wsdl_util_create_request_envelope(
    const axutil_env_t *env,
    axiom_node_t *payload,
    int soap_version);

/**
* creates the soap envelope from the xml buffer */
axiom_soap_envelope_t *
wsdl_util_create_soap_envelope_from_buffer(
    const axutil_env_t * env, 
    axis2_char_t* buffer,
    axis2_char_t * soap_version_uri);

/**
* send and receive using the service client. payload is provided through the buffer. */
axiom_soap_envelope_t *
wsdl_util_send_receive_soap_envelope_with_op_client(
    const axutil_env_t* env,
    axis2_svc_client_t* svc_client,
    axis2_options_t* options,
    axis2_char_t* buffer);

/**
* Configures security in the svc_client. Policies from script_client_options override policies 
* from WSDL, if any. 
*
* @param env pointer to environment struct
* @param script_client_options pointer to a hash of client options from scripting language
*                 WSF_WSDL_HK_POLICY_STRING given priority over WSF_WSDL_HK_POLICY_HASH if both set.
* @param wsdl_policy_hash pointer to a hash of policy axioms from wsdl
* @param svc_client pointer to the service client struct
* @return AXIS2_SUCCESS on success, else AXIS2_FAILURE
*/
axis2_status_t
wsf_wsdl_util_configure_security_for_svc_client(
    const axutil_env_t* env, 
    axutil_hash_t* script_client_options_hash, 
    axutil_hash_t* wsdl_policy_hash,
    axis2_svc_client_t* svc_client);

axis2_status_t
wsf_wsdl_util_insert_policy_to_svc_client(
    const axutil_env_t* env, 
    neethi_policy_t* merged_input_neethi_policy,
    axis2_svc_client_t* svc_client);

axis2_status_t
wsf_wsdl_util_add_security_to_svc_client_configuration(
    const axutil_env_t* env,
    axutil_hash_t* security_token_hash,
    axis2_svc_client_t* svc_client);

axis2_status_t
wsf_wsdl_util_set_security_token_options_to_rampart_ctx(
    const axutil_env_t* env,
    axutil_hash_t* security_token_hash,
    rampart_context_t* rampart_ctx);

axis2_status_t
wsf_wsdl_util_create_merged_neethi_policy(
    const axutil_env_t* env,
    axiom_node_t* input_policy_node_axiom,
    axiom_node_t* binding_policy_node_axiom,
    neethi_policy_t** merged_input_neethi_policy);

axis2_status_t
wsf_wsdl_util_policy_from_xml_string(
    const axutil_env_t* env, 
    axis2_char_t* policy_xml_str, 
    axiom_node_t** input_policy_node_axiom);

axis2_char_t*
wsf_wsdl_util_get_rampart_token_value(
    axis2_char_t* token_ref);

axis2_status_t
wsf_wsdl_util_policy_from_options_hash(
    const axutil_env_t* env, 
    axutil_hash_t* policy_hash, 
    axiom_node_t** input_policy_node_axiom);

axis2_status_t
wsf_wsdl_util_policy_from_wsdl_policy_hash(
    const axutil_env_t* env, 
    axutil_hash_t* wsdl_policy_hash, 
    axiom_node_t** input_policy_node_axiom,
    axiom_node_t** binding_policy_node_axiom);

axiom_node_t *
wsf_wsdl_util_deserialize_buffer(
    const axutil_env_t* env,  
    axis2_char_t* buffer);

axiom_node_t *
wsf_wsdl_util_deserialize_file(
    const axutil_env_t* env,  
    axis2_char_t* file_name);

axis2_status_t
wsf_wsdl_util_handle_service_security(
    const axutil_env_t* env,
    axis2_svc_t* service,
    axis2_conf_ctx_t* worker_conf_ctx,
    axutil_hash_t* script_service_user_options,
    axis2_char_t* service_name,
    axis2_char_t* port_name,
    axiom_node_t* wsdl_axiom,
    axis2_bool_t is_version1_wsdl);

axis2_status_t
wsf_wsdl_util_configure_security_for_service(
    const axutil_env_t* env, 
    axutil_hash_t* script_client_options_hash, 
    axutil_hash_t* policies_hash,
    axis2_svc_t* svc,
    axis2_conf_ctx_t* worker_conf_ctx);

axis2_status_t
wsf_wsdl_util_insert_policy_to_svc(
    const axutil_env_t* env, 
    neethi_policy_t* merged_input_neethi_policy,
    axis2_svc_t* svc,
    axis2_char_t* op_name);

axis2_status_t
wsf_wsdl_util_add_security_to_svc_configuration(
    const axutil_env_t* env,
    axutil_hash_t* security_token_hash,
    axis2_svc_t* svc);

axis2_status_t
wsf_wsdl_util_engage_module(
    axis2_conf_t * conf,
    axis2_char_t * module_name,
    const axutil_env_t * env,
    axis2_svc_t * svc);

#endif /* WSF_WSDL_UTIL_H */


