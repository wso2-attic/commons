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

#ifndef WSF_WSDL_MODE_H
#define WSF_WSDL_MODE_H

#include <axiom.h>
#include <axiom_xml_reader.h>
#include <axiom_util.h>
#include <axis2_addr.h>
#include <axutil_hash.h>
#include <axis2_options.h>
#include <axis2_svc_client.h>
#include <axis2_http_transport.h>
#include <axiom_soap_envelope.h>
#include <rampart_context.h>
#include <neethi_options.h>
#include <axis2_policy_include.h>
#include <neethi_engine.h>

#include "wsf_wsdl_defines.h"
#include "wsf_wsdl_data.h"
#include "wsf_wsdl_data_template.h"
#include "wsf_wsdl_info.h"
#include "wsf_wsdl_data_util.h"
#include "wsf_wsdl_util.h"
#include "wsf_wsdl_type_map.h"

/** 
* Intermediate structures
*/

typedef struct wsf_wsdl_info
{
	axiom_node_t* type_map;		/* contains the script to schema type map */
	axutil_hash_t* operations;  /* contains the request/response templates per each operation */
/*    axis2_char_t* service_name; /* contains service name, only used in server side */
/*    axis2_char_t* port_name;    /* contains port name, only used in client side */
} wsf_wsdl_info_t;

/** 
* Initialize the wsdl mode module for client 
* Mainly responsible of parsing the wsdl and creating intermediate structures for future use.
* 
* @param env			   : environment structure
* @param wsdl_file_name    : location of the wsdl file
* @param svc_client        : service client
* @param svc_client_options: service client options
* @param wsdl_info		   : intermediate structure containing, templates and typemap
*
* @returns AXIS2_SUCCESS or AXIS2_FAILURE depending on the success of the initialization.*/

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_initialize_for_client(
    const axutil_env_t* env,
    const axis2_char_t* wsdl_file_name,
    axis2_char_t* script_binding_home,
    axis2_svc_client_t* svc_client,
    axutil_hash_t* svc_client_options,
    wsf_wsdl_info_t** wsdl_info);


/** 
* Initialize the wsdl mode module for client 
* Mainly responsible of parsing the wsdl and creating intermediate structures for future use.
* 
* @param env			   : environment structure
* @param wsdl_file_name    : location of the wsdl file
* @param type_map_file	   : location of the type map file
* @param xslt_location	   : where xslts reside
* @param wsdl_info		   : intermediate structure containing, templates and typemap
*
* @returns AXIS2_SUCCESS or AXIS2_FAILURE depending on the success of the initialization.*/

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
    wsf_wsdl_info_t** wsdl_info);


/**
* Validate a given axiom against the template.
* 
* @param env		 	   : environment structure
* @param type_map		   : type map object
* @param templ			   : the template
* @param node			   : axiom node
* @param criteria          : criteria for the validation. 
* @param data			   : data contains the wsdl_data_t version of the axiom node.
*
* @returns AXIS2_SUCCESS or AXIS2_FAILURE depending on the success of the validation.*/

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_validate_axiom(
    const axutil_env_t* env, 
    axiom_node_t* type_map,
    wsf_wsdl_data_template_t* templ,
    axiom_node_t* node,
    unsigned short criteria,
    wsf_wsdl_data_t** data);


/**
* Validate a given axiom against the template. data can be modified during validation, depending on the criteria.
* 
* @param env			   : environment structure
* @param type_map		   : type map object
* @param templ			   : the template
* @param data			   : wsf_wsdl_data_t structure, which may be modified depending on the criteria.
* @param criteria          : criteria for the validation. 
*
* @returns AXIS2_SUCCESS or AXIS2_FAILURE depending on the success of the validation.*/

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_validate_data(
    const axutil_env_t* env,
    axiom_node_t* type_map,
    wsf_wsdl_data_template_t* templ,
    wsf_wsdl_data_t* data,
    unsigned short criteria);


/**
* Perform client request. Uses intermediate structures created during initialization.
* 
* @param env			   : environment structure
* @param svc_client        : service client
* @param operation_name    : the operation used
* @param user_parameters   : input data from the user
* @param info			   : info structure created during initialization 
* @param response          : response received (if any)
*
* @returns AXIS2_SUCCESS or AXIS2_FAILURE depending on the success of the client request.*/

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_client_request(
    const axutil_env_t* env,
    axis2_svc_client_t* svc_client,
    const axis2_char_t* operation_name,	
    wsf_wsdl_data_t* user_parameters,
    wsf_wsdl_info_t* info,
    wsf_wsdl_data_t** response);


/**
* Creates the response axiom node from wsf_wsdl_data_t structure. 
* 
* @param env			   : environment structure
* @param data			   : data structure containing the payload
* @param response_node     : an axiom node, ready to send to client
*
* @returns AXIS2_SUCCESS or AXIS2_FAILURE depending on the success of the response creation.*/

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsf_wsdl_mode_create_response_node(
    const axutil_env_t* env,
    wsf_wsdl_data_t* data,
    axiom_node_t** response_node);




/* following function is deprecated */
WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL 
wsf_wsdl_request(
    const axutil_env_t* env,
    axis2_char_t* wsdl_file_name,
    const axis2_char_t* operation_name,		
    wsf_wsdl_data_t* user_parameters,
    axis2_char_t* script_binding_home,
    axis2_svc_client_t* svc_client,
    axutil_hash_t* svc_client_options,
    axis2_char_t* service_name,
    axis2_char_t* port_name,
    wsf_wsdl_data_t** response);

#endif /* WSF_WSDL_MODE_H */

