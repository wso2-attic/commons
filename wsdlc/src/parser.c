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
#include "wsf_wsdl_data_util.h"
#include "wsf_wsdl_info.h"
#include "wsf_wsdl_util.h"

axis2_status_t
wsf_wsdl_util_get_all_policies_from_wsdl(
    const axutil_env_t* env, 
    axiom_node_t* wsdl_axiom,
    axis2_bool_t is_version1_wsdl,
    axiom_node_t* binding_node,
    const axis2_char_t* operation_name,
    axutil_hash_t** wsdl_policy_hash)
{
    axiom_element_t* binding_element = NULL;
    axutil_qname_t *qname = NULL;		
    axis2_status_t status = AXIS2_FAILURE;

    AXIS2_PARAM_CHECK(env->error, binding_node, AXIS2_FAILURE);

    binding_element = (axiom_element_t*)axiom_node_get_data_element(binding_node, env); 

    if (binding_element)
    {
        axiom_children_qname_iterator_t* itr = NULL;
        axis2_bool_t operation_found = AXIS2_FALSE;	
        axiom_node_t* operation_node = NULL;
        axiom_node_t* policy = NULL;

        status = wsf_wsdl_get_policy_using_policy_attribute(env,
            binding_node,
            wsdl_axiom,
            &policy);

        if (!status)
        {
            status = get_policy_using_policy_reference_attribute(env,
                binding_node,
                wsdl_axiom,
                &policy);
        }

        if (is_version1_wsdl)
        {
            qname = axutil_qname_create(env, WSF_WSDL_OPERATION, WSF_WSDL_NAMESPACE, NULL);
        }
        else
        {
            qname = axutil_qname_create(env, WSF_WSDL_OPERATION, WSF_WSDL2_NAMESPACE, NULL);
        }

        itr = axiom_element_get_children_with_qname(binding_element, 
            env,
            qname,
            binding_node);

        axutil_qname_free(qname, env);

        while (axiom_children_qname_iterator_has_next(itr, env))
        {
            axis2_char_t* op_name = NULL;
            operation_found = AXIS2_FALSE;

            operation_node = (axiom_node_t*)axiom_children_qname_iterator_next(itr, env);

            if (wsf_wsdl_parser_extract_operation_name(env, operation_node, is_version1_wsdl, &op_name))
            {
                operation_found = (axutil_strcmp(operation_name, op_name) == 0);

                AXIS2_FREE(env->allocator, op_name);

                if (operation_found)
                {
                    *wsdl_policy_hash = axutil_hash_make(env);

                    if (policy)
                    {
                        axutil_hash_set(*wsdl_policy_hash, 
                            axutil_strdup(env, WS_WSDL_OP_POLICY), 
                            AXIS2_HASH_KEY_STRING, 
                            policy);
                    }

                    status = wsf_wsdl_fill_policy_hash_from_operation_node_policies(env,
                        wsdl_axiom,
                        operation_node,
                        *wsdl_policy_hash);
                    break;
                }
            }
        }
    }

        return status;
}

axis2_char_t* 
wsf_axiom_get_attribute_val_of_node_by_qname(
    const axutil_env_t *env,
    axiom_node_t *node,
    axutil_qname_t *qname)
{
    /*QName might NOT contain the prefix*/
    axiom_element_t *ele = NULL;
    axutil_hash_t *attr_list = NULL;
    axutil_hash_index_t *hi = NULL;
    axis2_char_t *local_name = NULL;
    axis2_char_t *ns_uri = NULL;
    axis2_char_t *found_val = NULL;

    ele = axiom_node_get_data_element (node, env);

    /*Get attribute list of the element*/
    attr_list = axiom_element_extract_attributes(ele, env, node);
    if (!attr_list)
    {
        return NULL;
    }
    /*Get localname of the qname*/
    local_name =  axutil_qname_get_localpart(qname, env);
    /*Get nsuri of the qname*/
    ns_uri = axutil_qname_get_uri(qname, env);
    if (!ns_uri)
    {
        ns_uri = "";
    }
    /*Traverse thru all the attributes. If both localname and the nsuri matches return the val*/
    for (hi = axutil_hash_first(attr_list, env); hi; hi = axutil_hash_next(env, hi))
    {
        void *attr = NULL;
        axiom_attribute_t *om_attr = NULL;
        axutil_hash_this(hi, NULL, NULL, &attr);
        if (attr)
        {
            axis2_char_t *this_attr_name = NULL;
            axis2_char_t *this_attr_ns_uri = NULL;
            axiom_namespace_t *attr_ns = NULL;

            om_attr = (axiom_attribute_t*)attr;
            this_attr_name = axiom_attribute_get_localname(om_attr, env);
            attr_ns = axiom_attribute_get_namespace(om_attr, env);
            if (attr_ns)
            {
                this_attr_ns_uri = axiom_namespace_get_uri(attr_ns, env);
            } 
            else
            {
                this_attr_ns_uri = "";
            }
            if (0 == axutil_strcmp(local_name, this_attr_name) && 0 == axutil_strcmp(ns_uri, this_attr_ns_uri))
            {
                /*Got it !!!*/
                found_val = axiom_attribute_get_value(om_attr, env);
				if (env)
                {
					AXIS2_FREE(env->allocator, hi);
                }
                break;
            }
        }
    }

    for (hi = axutil_hash_first(attr_list, env); hi; hi = axutil_hash_next(env, hi))
    {
        void *val = NULL;
        axutil_hash_this(hi, NULL, NULL, &val);
        if (val)
        {
            axiom_attribute_free((axiom_attribute_t *)val, env);
            val = NULL;
        }
    }
    axutil_hash_free(attr_list, env);
    attr_list = NULL;

    return found_val;
}

axis2_char_t* 
wsf_axiom_get_attribute_value_of_node_by_name(
    const axutil_env_t *env,
    axiom_node_t *node,
    axis2_char_t *attribute_name,
    axis2_char_t *ns)
{
    axis2_char_t *attribute_value = NULL;
    axutil_qname_t *qname = NULL;

    qname = axutil_qname_create(env, attribute_name, ns , NULL);
    attribute_value = wsf_axiom_get_attribute_val_of_node_by_qname(env, node, qname);
    axutil_qname_free(qname, env);
    qname = NULL;
    return attribute_value;
}


axis2_status_t
wsf_wsdl_util_get_policy(const axutil_env_t* env,
						 axiom_node_t* wsdl_axiom,
						 axis2_char_t* policy_uri,
						 axiom_node_t** policy_node)
{
	axutil_qname_t *qname = NULL;				
	axiom_element_t* root_element = NULL;	
	axiom_node_t* policy_node_temp = NULL;			
	axiom_element_t* policy_element_temp = NULL;	
	axis2_char_t* policy_uri_hash_position = NULL;
	axis2_bool_t policy_found = AXIS2_FALSE;

	if (!policy_uri)
	{
		return AXIS2_FAILURE;
	}

	policy_uri_hash_position = axutil_strchr(policy_uri, '#');

	if (!policy_uri_hash_position)
	{
		/* TODO: support policy in another WSDL issue */
		AXIS2_LOG_DEBUG_MSG(env->log, 
							"Policy is not in the same WSDL, yet to support this feature!");
		return AXIS2_FAILURE;
	}
	else if (policy_uri_hash_position - policy_uri) /* # is not at the beginnig */
	{
		AXIS2_LOG_DEBUG_MSG(env->log, 
							"# charactor should be at the begining of policy URI, but it is NOT");
		return AXIS2_FAILURE;
	}

	policy_uri = axutil_strdup(env, policy_uri);
	policy_uri = axutil_string_substring_starting_at(policy_uri, 1);

	root_element = (axiom_element_t*)axiom_node_get_data_element(wsdl_axiom, env);
	
	if (root_element)
	{
		axiom_children_qname_iterator_t* itr = NULL;

		qname = axutil_qname_create(env, WSF_WSDL_POLICY, WSF_WSDL_POLICY_REFERENCE_NAMESPACE_URI, NULL);
		itr = axiom_element_get_children_with_qname(root_element, 
													env,
													qname,
													wsdl_axiom);

		axutil_qname_free(qname, env);

		while (axiom_children_qname_iterator_has_next(itr, env))
		{
			policy_node_temp = (axiom_node_t*)axiom_children_qname_iterator_next(itr, env);
            policy_element_temp = 
				               (axiom_element_t*)axiom_node_get_data_element(policy_node_temp, env);

			if (policy_element_temp)
			{
				if (strcmp(policy_uri,  
				   wsf_axiom_get_attribute_value_of_node_by_name(env,
                                                                 policy_node_temp, 
                                                                 WSF_WSDL_ID,
                                                                 WSF_WSDL_POLICY_ID_NAMESPACE_URI))
				   == 0)
				{
					policy_found = AXIS2_TRUE;
					break;
				}
			}
		}
	}

	AXIS2_FREE(env->allocator, policy_uri);

	if (policy_found)
	{
		*policy_node = policy_node_temp;
		return AXIS2_SUCCESS;
	}
	else
	{
		*policy_node = NULL;
		return AXIS2_FAILURE;
	}
}

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
    axutil_hash_t* wsdl_policy_hash)
{
    axiom_node_t* io_node = NULL;
    axiom_element_t* io_element = NULL;
    axiom_children_iterator_t* input_output_itr = NULL;
    axiom_node_t* policy_ref_node = NULL;
    axiom_element_t* policy_ref_element = NULL;
  	axutil_qname_t *qname = NULL;		
    axis2_status_t status = AXIS2_FAILURE;
    axiom_element_t* operation_element = NULL;
    
    operation_element = (axiom_element_t*)axiom_node_get_data_element(operation_node, env);

    input_output_itr = axiom_element_get_children(operation_element, 
											      env,
											      operation_node);

    while (axiom_children_iterator_has_next(input_output_itr, env))
    {
	    io_node = (axiom_node_t*)axiom_children_iterator_next(input_output_itr, env);
	    io_element = (axiom_element_t*)axiom_node_get_data_element(io_node, env);

	    qname = axutil_qname_create(env, 
                                    WSF_WSDL_POLICY_REFERENCE, 
                                    WSF_WSDL_POLICY_REFERENCE_NAMESPACE_URI, 
                                    NULL);

	    policy_ref_element = axiom_element_get_first_child_with_qname(io_element, 
																      env,
																      qname, 
																      io_node,
																      &policy_ref_node);
	    axutil_qname_free(qname, env);
    	
	    if (policy_ref_element)
	    {
		    axis2_char_t* policy_uri = NULL;
		    axiom_node_t* msg_policy = NULL;

		    policy_uri = axiom_element_get_attribute_value_by_name(policy_ref_element, 
															       env, 
															       WSF_WSDL_URI);

		    if (policy_uri)
		    {
			    if (wsf_wsdl_util_get_policy(env, wsdl_axiom, policy_uri, &msg_policy))
			    {
				    axutil_string_t* local_name = NULL;
				    const axis2_char_t* local_name_buffer = NULL;
				    axis2_char_t* key = NULL;
    				
				    local_name = axiom_element_get_localname_str(io_element, env);

				    local_name_buffer = axutil_string_get_buffer(local_name, env);

				    key = axutil_strcat(env, local_name_buffer, "_policy", NULL);
    							
				    axutil_hash_set(wsdl_policy_hash, 
								    key, 
								    AXIS2_HASH_KEY_STRING, 
								    msg_policy);
                    status = AXIS2_SUCCESS;
			    }
		    }
	    }
    }
    return status;
}


axis2_status_t
wsf_wsdl_get_policy_using_policy_attribute(
    const axutil_env_t* env, 
    axiom_node_t* binding_node,
    axiom_node_t* wsdl_axiom,
    axiom_node_t** binding_policy)
{
    axis2_status_t status = AXIS2_FAILURE;
   	axutil_qname_t *qname = NULL;		
    axiom_node_t* policy_reference_node = NULL;
    axiom_element_t* policy_reference_element = NULL;
    axiom_element_t* binding_element = NULL;
    
	binding_element = (axiom_element_t*)axiom_node_get_data_element(binding_node, env); 

	qname = axutil_qname_create(env, 
                                WSF_WSDL_POLICY_REFERENCE, 
                                WSF_WSDL_POLICY_REFERENCE_NAMESPACE_URI, 
                                NULL);
	
	policy_reference_element = 
		(axiom_element_t*)axiom_element_get_first_child_with_qname(binding_element, 
																   env,
																   qname,
																   binding_node,
																   &policy_reference_node);
	
	axutil_qname_free(qname, env);

	if (policy_reference_element)
	{
		axis2_char_t* policy_uri = NULL;
		
		policy_uri = 
		 axiom_element_get_attribute_value_by_name(policy_reference_element, env, WSF_WSDL_URI);

		if (wsf_wsdl_util_get_policy(env, wsdl_axiom, policy_uri, binding_policy))
		{
            status = AXIS2_SUCCESS;
		}
	}

    return status;
}


axis2_status_t
get_policy_using_policy_reference_attribute(
    const axutil_env_t* env, 
    axiom_node_t* binding_node, 
    axiom_node_t* wsdl_axiom,
    axiom_node_t** binding_policy)
{
    axis2_status_t status = AXIS2_FAILURE;
   	axutil_qname_t *qname = NULL;		
    axiom_element_t* binding_element = NULL;
	axiom_node_t* policy_node = NULL;
	axiom_element_t* policy_element = NULL;

  	binding_element = (axiom_element_t*)axiom_node_get_data_element(binding_node, env); 
    qname = axutil_qname_create(env, WSF_WSDL_POLICY, WSF_WSDL_POLICY_REFERENCE_NAMESPACE_URI, NULL);
    policy_element = (axiom_element_t*)axiom_element_get_first_child_with_qname(binding_element, 
														                        env,
														                        qname,
														                        binding_node,
														                        &policy_node);		
    axutil_qname_free(qname, env);

    if (policy_element)
    {
        axis2_char_t* policy_uri = NULL;
    	
        policy_uri = axiom_element_get_attribute_value_by_name(policy_element, 
													           env, 
													           WSF_WSDL_URI);

        if (wsf_wsdl_util_get_policy(env, wsdl_axiom, policy_uri, binding_policy))
        {
            status = AXIS2_SUCCESS;
        }
    }

    return status;
}


/* caller should clean operation_name */
axis2_status_t
wsf_wsdl_parser_extract_operation_name(
    const axutil_env_t* env,
    axiom_node_t* operation_node,
    axis2_bool_t is_version1_wsdl,
    axis2_char_t** operation_name)
{
    axis2_status_t status = AXIS2_FAILURE;
    axiom_element_t* operation_element = NULL;
    
    operation_element = (axiom_element_t*)axiom_node_get_data_element(operation_node, env);

    if (!is_version1_wsdl)
	{
		axis2_char_t* ref = NULL;
		ref = axiom_element_get_attribute_value_by_name(operation_element, 
			                                            env, 
													    WSF_WSDL_REF);
		ref = axutil_strchr(ref, ':');

		if (ref)
		{
			ref = axutil_strdup(env, ref);
			ref = axutil_string_substring_starting_at(ref, 1);

			*operation_name = ref;
            status = AXIS2_SUCCESS;
		}
	}
	else 
	{
		axis2_char_t* name = NULL;
		name = axiom_element_get_attribute_value_by_name(operation_element, 
														 env, 
													     WSF_WSDL_NAME);
		if (name)
		{
			*operation_name = axutil_strdup(env, name);
            status = AXIS2_SUCCESS;
		}
	}

    return status;
}



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
wsf_wsdl_util_get_all_policies(const axutil_env_t* env, 
							   axiom_node_t* wsdl_axiom,
							   axiom_node_t* binding_node,
  							   axis2_bool_t is_version1_wsdl,
							   axutil_hash_t** wsdl_hash_of_policy_hash)
{
	axiom_element_t* binding_element = NULL;
	axutil_qname_t *qname = NULL;		
    axis2_status_t status = AXIS2_FAILURE;

	AXIS2_PARAM_CHECK(env->error, binding_node, AXIS2_FAILURE);
	
	binding_element = (axiom_element_t*)axiom_node_get_data_element(binding_node, env); 

	if (binding_element)
	{
        axiom_children_qname_iterator_t* itr = NULL;
        axiom_node_t* operation_node = NULL;       
        axiom_node_t* policy = NULL;
       
        status = wsf_wsdl_get_policy_using_policy_attribute(env,
                                                            binding_node,
                                                            wsdl_axiom,
                                                            &policy);
        
        if (!status)
        {
            get_policy_using_policy_reference_attribute(env,
                                                        binding_node,
                                                        wsdl_axiom,
                                                        &policy); /*policy will be NULL on FAILURE*/
        }

		if (is_version1_wsdl)
        {
		    qname = axutil_qname_create(env, WSF_WSDL_OPERATION, WSF_WSDL_NAMESPACE, NULL);
        }
        else
        {
            qname = axutil_qname_create(env, WSF_WSDL_OPERATION, WSF_WSDL2_NAMESPACE, NULL);
        }

		itr = axiom_element_get_children_with_qname(binding_element, 
													env,
													qname,
													binding_node);

		axutil_qname_free(qname, env);

        status = AXIS2_FAILURE;

        /* iterate through all the operations */
		while (axiom_children_qname_iterator_has_next(itr, env))
		{
            axis2_char_t* op_name = NULL;

			operation_node = (axiom_node_t*)axiom_children_qname_iterator_next(itr, env);

            /* get the operation name from the operation node */
            if (wsf_wsdl_parser_extract_operation_name(env, operation_node, is_version1_wsdl, &op_name))
            {
                axutil_hash_t* policy_hash = axutil_hash_make(env);
                
                if (policy)
                {
                    axutil_hash_set(policy_hash, 
                                    axutil_strdup(env, WS_WSDL_OP_POLICY), 
                                    AXIS2_HASH_KEY_STRING, 
                                    policy);
                }

                wsf_wsdl_fill_policy_hash_from_operation_node_policies(env,
                                                                       wsdl_axiom,
                                                                       operation_node,
                                                                       policy_hash);
                if (axutil_hash_count(policy_hash) > 0)
                {
                    axutil_hash_set(*wsdl_hash_of_policy_hash, 
                                    op_name, 
                                    AXIS2_HASH_KEY_STRING, 
                                    policy_hash);                   
                    
                    status = AXIS2_SUCCESS;
                }
                else
                {
                    axutil_hash_free(policy_hash, env);
                }
            }
 		}
	}

	return status;
}


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
    axiom_node_t** binding_node)
{
    axis2_char_t* binding_name = NULL;
    axiom_element_t* port_element = NULL;
    axiom_element_t* wsdl_element = NULL;
    axutil_qname_t* qname = NULL; 
    axis2_status_t status = AXIS2_FAILURE;

    port_element = (axiom_element_t*)axiom_node_get_data_element(port_node, env);
    wsdl_element = (axiom_element_t*)axiom_node_get_data_element(wsdl_axiom, env);
    *binding_node = NULL;

    binding_name = axiom_element_get_attribute_value_by_name(port_element, env, WSF_WSDL_BINDING);

    if (binding_name)
    {
        axiom_children_qname_iterator_t* itr = NULL;
        axis2_bool_t binding_found = AXIS2_FALSE;
        axiom_node_t* binding_node_temp = NULL;
        axiom_element_t* binding_element_temp = NULL;

        if (axutil_strchr(binding_name, ':'))
        {
            /* getting our copy of a string from the axiom */
            binding_name = axutil_strdup(env, axutil_strchr(binding_name, ':'));
            binding_name = axutil_string_substring_starting_at(binding_name, 1);
        }
        else 
        {
            /* getting a copy of a string from the axiom */
            binding_name = axutil_strdup(env, binding_name); 
        }

        if (is_version1_wsdl)
        {
            qname = axutil_qname_create(env, WSF_WSDL_BINDING, WSF_WSDL_NAMESPACE, NULL);
        }
        else 
        {
            qname = axutil_qname_create(env, WSF_WSDL_BINDING, WSF_WSDL2_NAMESPACE, NULL);
        }

        itr = axiom_element_get_children_with_qname(wsdl_element, env, qname, wsdl_axiom);

        axutil_qname_free(qname, env);

        while (axiom_children_qname_iterator_has_next(itr, env))
        {
            binding_node_temp = 
                (axiom_node_t*)axiom_children_qname_iterator_next(itr, env);
            binding_element_temp = 
                (axiom_element_t*)axiom_node_get_data_element(binding_node_temp, env);

            if (strcmp(binding_name,  
                       axiom_element_get_attribute_value_by_name(binding_element_temp, 
                                                                 env, 
                                                                 WSF_WSDL_NAME)) == 0)
            {
                binding_found = AXIS2_TRUE;
                break;
            }
        }

        AXIS2_FREE(env->allocator, binding_name);

        if (binding_found)
        {
            *binding_node = binding_node_temp;
            status = AXIS2_SUCCESS;
        }
    }

    return status;
}	


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
wsf_wsdl_parser_get_binding_nodes_under_service(axutil_env_t* env,
                                                axiom_node_t* service_node,
                                                axiom_node_t* wsdl_axiom,
                                                axis2_bool_t is_version1_wsdl,
                                                axutil_hash_t** ports)
{
    axiom_children_qname_iterator_t* port_itr = NULL;
    axis2_bool_t port_found = AXIS2_FALSE;
    axiom_node_t* port_node = NULL;
    axiom_element_t* port_element = NULL;
    axutil_qname_t* qname = NULL;
    axiom_element_t* service_element = NULL;
    axis2_status_t status = AXIS2_FAILURE;

    service_element = (axiom_element_t*)axiom_node_get_data_element(service_node, env);

    if (is_version1_wsdl)
    {
        qname = axutil_qname_create(env, WSF_WSDL_PORT, WSF_WSDL_NAMESPACE, NULL);
    }
    else
    {
        qname = axutil_qname_create(env, WSF_WSDL_ENDPOINT, WSF_WSDL2_NAMESPACE, NULL);
    }

    port_itr = axiom_element_get_children_with_qname(service_element, 
                                                     env,
                                                     qname,
                                                     service_node);
    axutil_qname_free(qname, env);

    while (axiom_children_qname_iterator_has_next(port_itr, env))
    {
        axis2_char_t* port_name = NULL;
        axiom_node_t* binding_node = NULL;

        port_node = (axiom_node_t*)axiom_children_qname_iterator_next(port_itr, env);
        port_element = (axiom_element_t*)axiom_node_get_data_element(port_node, env);

        port_name = axiom_element_get_attribute_value_by_name(port_element, env, WSF_WSDL_NAME);

        if (port_name)
        {
            axis2_bool_t binding_nodes_found = AXIS2_FAILURE;
            port_found = AXIS2_TRUE;

            binding_nodes_found = wsf_wsdl_parser_get_binding_node_from_port_node(env,
                                                                                port_node,
                                                                                is_version1_wsdl,
                                                                                wsdl_axiom,
                                                                                &binding_node);
            if (binding_nodes_found)
            {
                axutil_hash_set(*ports, port_name, AXIS2_HASH_KEY_STRING, binding_node);
                status = AXIS2_SUCCESS;
            }
        }
    }

    return status;
}


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
    axutil_hash_t** services)
{
    axutil_qname_t *qname = NULL;				
    axiom_element_t* wsdl_element = NULL;	
    axiom_node_t* service_node = NULL;			
    axiom_element_t* service_element = NULL;	

    wsdl_element = (axiom_element_t*)axiom_node_get_data_element(wsdl_axiom, env);

    if (wsdl_element)
    {
        axiom_children_qname_iterator_t* service_itr = NULL;
        axis2_bool_t service_found = AXIS2_FALSE;

        if (is_version1_wsdl)
        {
            qname = axutil_qname_create(env, WSF_WSDL_SERVICE, WSF_WSDL_NAMESPACE, NULL);
        }
        else
        {
            qname = axutil_qname_create(env, WSF_WSDL_SERVICE, WSF_WSDL2_NAMESPACE, NULL);
        }

        service_itr = axiom_element_get_children_with_qname(wsdl_element, 
                                                            env,
                                                            qname,
                                                            wsdl_axiom);
        axutil_qname_free(qname, env);

        while (axiom_children_qname_iterator_has_next(service_itr, env))
        {
            axis2_char_t* service_name = NULL;

            service_node = (axiom_node_t*)axiom_children_qname_iterator_next(service_itr, env);
            service_element = (axiom_element_t*)axiom_node_get_data_element(service_node, env);

            service_name = 
                axiom_element_get_attribute_value_by_name(service_element, env, WSF_WSDL_NAME);

            if (service_name)
            {
                axutil_hash_t* ports = NULL;
                axis2_status_t binding_nodes_for_service_status = AXIS2_FAILURE;
                service_found = AXIS2_TRUE;
                
                ports = axutil_hash_make(env);
                binding_nodes_for_service_status = 
                    wsf_wsdl_parser_get_binding_nodes_under_service(env, 
                                                                    service_node,
                                                                    wsdl_axiom, 
                                                                    is_version1_wsdl,
                                                                    &ports);
                if (binding_nodes_for_service_status)
                {
                    axutil_hash_set(*services, service_name, AXIS2_HASH_KEY_STRING, ports);
                }                
            }
        }     
    }

    return AXIS2_SUCCESS;
}


axis2_status_t
wsf_wsdl_util_get_binding_node(
    const axutil_env_t* env,
    axiom_node_t* wsdl_axiom, 
    axis2_bool_t is_version1_wsdl, 
    axis2_char_t* service_name, 
    axis2_char_t* port_name,
    axiom_node_t** binding_node)
{   
    axutil_qname_t *qname = NULL;				
    axiom_element_t* root_element = NULL;	
    axiom_node_t* service_node = NULL;			
    axiom_element_t* service_element = NULL;	
    axis2_char_t* tmp = NULL;

    *binding_node = NULL;
    root_element = (axiom_element_t*)axiom_node_get_data_element(wsdl_axiom, env);
    tmp = axiom_node_to_string(wsdl_axiom, env);

    if (root_element)
    {
        axiom_children_qname_iterator_t* service_itr = NULL;
        axis2_bool_t service_found = AXIS2_FALSE;

        if (is_version1_wsdl)
        {
            qname = axutil_qname_create(env, WSF_WSDL_SERVICE, WSF_WSDL_NAMESPACE, NULL);
        }
        else
        {
            qname = axutil_qname_create(env, WSF_WSDL_SERVICE, WSF_WSDL2_NAMESPACE, NULL);
        }

        service_itr = axiom_element_get_children_with_qname(root_element, 
            env,
            qname,
            wsdl_axiom);
        axutil_qname_free(qname, env);

        while (axiom_children_qname_iterator_has_next(service_itr, env))
        {
            service_node = (axiom_node_t*)axiom_children_qname_iterator_next(service_itr, env);
            service_element = (axiom_element_t*)axiom_node_get_data_element(service_node, env);

            if (!service_name)
            {
                /* if service_name is null, get the first service node */
                service_found = AXIS2_TRUE;
                break;
            }

            if (strcmp(service_name,  
                axiom_element_get_attribute_value_by_name(service_element, env, WSF_WSDL_NAME))	== 0)
            {
                service_found = AXIS2_TRUE;
                break;
            }
        }     

        if (service_found)
        {
            axiom_children_qname_iterator_t* port_itr = NULL;
            axis2_bool_t port_found = AXIS2_FALSE;
            axiom_node_t* port_node = NULL;
            axiom_element_t* port_element = NULL;

            if (is_version1_wsdl)
            {
                qname = axutil_qname_create(env, WSF_WSDL_PORT, WSF_WSDL_NAMESPACE, NULL);
            }
            else
            {
                qname = axutil_qname_create(env, WSF_WSDL_ENDPOINT, WSF_WSDL2_NAMESPACE, NULL);
            }

            port_itr = axiom_element_get_children_with_qname(service_element, 
                env,
                qname,
                service_node);
            axutil_qname_free(qname, env);

            while (axiom_children_qname_iterator_has_next(port_itr, env))
            {
                port_node = (axiom_node_t*)axiom_children_qname_iterator_next(port_itr, env);
                port_element = (axiom_element_t*)axiom_node_get_data_element(port_node, env);

                if (!port_name)
                {
                    port_found = AXIS2_TRUE;
                    break;
                }

                if (strcmp(port_name,  
                    axiom_element_get_attribute_value_by_name(port_element, env, WSF_WSDL_NAME))
                    == 0)
                {
                    port_found = AXIS2_TRUE;
                    break;
                }
            }

            if (port_found)
            {
                return wsf_wsdl_parser_get_binding_node_from_port_node(env,
                    port_node,
                    is_version1_wsdl,
                    wsdl_axiom,
                    binding_node);
            }
        }
    }

    return AXIS2_FAILURE;
}
    

axis2_bool_t
wsdl_util_get_operations(
    const axutil_env_t* env, 
    axiom_node_t* sig_axiom, 
    axutil_hash_t** operations)
{
	axis2_bool_t success = AXIS2_FAILURE;
    axutil_qname_t *qname = NULL;			
    axiom_element_t* services_element = NULL;	
    axiom_node_t* service_node = NULL;		
    axiom_element_t* service_element = NULL;
	axutil_hash_t* operations_hash = NULL;

	if (!(*operations))
	{
		axutil_hash_free((*operations), env);
	}

	services_element = (axiom_element_t*)axiom_node_get_data_element(sig_axiom, env);

    if (services_element)
    {
        axiom_children_qname_iterator_t* itr = NULL;	
        qname = axutil_qname_create(env, WSF_WSDL_SERVICE, NULL, NULL);	
        itr = axiom_element_get_children_with_qname(services_element, env, qname,
                                                               sig_axiom);
    
        axutil_qname_free(qname, env);

		operations_hash = (axutil_hash_t*)axutil_hash_make(env);
        
		/* iterate all the service elements under services element */
        while (AXIS2_TRUE == axiom_children_qname_iterator_has_next(itr, env))
        {
            axis2_char_t* address = NULL;
            service_node = (axiom_node_t*)axiom_children_qname_iterator_next(itr, env);
            service_element = (axiom_element_t*)axiom_node_get_data_element(service_node, env);

			/* address is no longer considered, unlike client situation */
            address = axiom_element_get_attribute_value_by_name(service_element, env, WSF_WSDL_ADDRESS);          
            {
                axiom_node_t* operations_node = NULL;			
                axiom_element_t* operations_element = NULL;		
                axiom_node_t* operation_node = NULL;
                axiom_element_t* operation_element = NULL;
                axutil_qname_t* operations_qname = NULL;	
                axutil_qname_t* operation_qname = NULL;		
                axiom_children_qname_iterator_t* operation_itr = NULL;	

				operations_qname = axutil_qname_create(env, WSF_WSDL_OPERATIONS, NULL, NULL);
                operations_element = axiom_element_get_first_child_with_qname(service_element, env, operations_qname,
                                                               service_node, &operations_node);
                axutil_qname_free(operations_qname, env);
            
                operation_qname = axutil_qname_create(env, WSF_WSDL_OPERATION, NULL, NULL);
                operation_itr = axiom_element_get_children_with_qname(operations_element, env, operation_qname,
                                                                      operations_node);
                axutil_qname_free(operation_qname, env);

                /* iterate all the operation elements under operations element */
				while (AXIS2_TRUE == axiom_children_qname_iterator_has_next(operation_itr, env))
                {
                    axis2_char_t* operation = NULL;
					axiom_node_t* params_node = NULL;
					axiom_node_t* returns_node = NULL;
					wsf_wsdl_data_template_t* input_template = NULL;
					wsf_wsdl_data_template_t* output_template = NULL;
					wsf_wsdl_operation_info_t* operation_info = NULL;

                    operation_node = (axiom_node_t*)axiom_children_qname_iterator_next(operation_itr, env);
                    operation_element = (axiom_element_t*)axiom_node_get_data_element(operation_node, env);

                    operation = axiom_element_get_attribute_value_by_name(operation_element, env, WSF_WSDL_NAME);

					wsdl_util_get_params_node(env, operation_node, &params_node);
					wsdl_util_get_returns_node(env, operation_node, &returns_node);

					wsdl_data_util_axiom_to_template(env, params_node, &input_template);
					wsdl_data_util_axiom_to_template(env, returns_node, &output_template);

					operation_info = (wsf_wsdl_operation_info_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_operation_info_t));

					operation_info->safe = AXIS2_FALSE;							/* TODO: parse */
					operation_info->pattern = NULL;								/* TODO: parse */
					operation_info->soap_action = NULL;							/* TODO: parse */
					operation_info->request_template = input_template;
					operation_info->response_template = output_template;

					axutil_hash_set(operations_hash, operation, AXIS2_HASH_KEY_STRING, operation_info);

					success = AXIS2_SUCCESS;
                }		
            }
        }
    }

	if (success)
	{
		*operations = operations_hash;
	}
	else
	{
		if (operations_hash)
		{
			axutil_hash_free(operations_hash, env);
		}
		*operations = NULL;
	}
    
	return success;
}



/**
* loads the wsdl and create sig_axiom wsdl_axiom */

axis2_status_t
wsf_wsdl_parser_load_wsdl(
    const axutil_env_t* env, 
    axis2_char_t* wsdl_file_name, 
    axis2_char_t* xslt_location,
    axiom_node_t** wsdl_axiom,
    axiom_node_t** sig_axiom,
    axis2_bool_t* is_version1_wsdl,
    axis2_bool_t is_service_call)
{
    return wsf_wsdl_util_apply_xslt(env, 
                                    wsdl_file_name, 
                                    xslt_location, 
                                    is_service_call, 
                                    is_version1_wsdl, 
                                    wsdl_axiom, 
                                    sig_axiom);
}


/**
* retrieves the endpoint address */

axis2_bool_t
wsdl_util_get_endpoint_address(axiom_node_t* sig_axiom, 
                               const axutil_env_t* env, 
                               axis2_char_t** endpoint_address)
{
    axutil_qname_t *qname = NULL;				
    axiom_element_t* services_element = NULL;	
    axiom_node_t* service_node = NULL;			
    axiom_element_t* service_element = NULL;	

    services_element = (axiom_element_t*)axiom_node_get_data_element(sig_axiom, env);
    
    if (services_element)
    {
        qname = axutil_qname_create(env, WSF_WSDL_SERVICE, NULL, NULL);
        service_element = axiom_element_get_first_child_with_qname(services_element, env, qname,
                                                                   sig_axiom, &service_node);
        
        axutil_qname_free(qname, env);

        *endpoint_address = axiom_element_get_attribute_value_by_name(service_element, env, WSF_WSDL_ADDRESS);
    
        if (*endpoint_address) 
        {
            return AXIS2_SUCCESS;      
        }
    }

    endpoint_address = NULL;
    
    return AXIS2_FAILURE;    
}


/**
* finds and returns the operation axiom */
axis2_bool_t
wsdl_util_find_operation(const axutil_env_t* env,
							 const axis2_char_t* operation_name, axis2_char_t* endpoint_address,
                             axis2_bool_t is_multiple,
                             axiom_node_t* sig_axiom,
                             axiom_node_t** operation_axiom,
							 int* soap_version)
{
    if (is_multiple == AXIS2_TRUE)
    {
        axutil_qname_t *qname = NULL;			
        axiom_element_t* services_element = NULL;	
        axiom_node_t* service_node = NULL;		
        axiom_element_t* service_element = NULL;	

        services_element = (axiom_element_t*)axiom_node_get_data_element(sig_axiom, env);
    
        if (services_element)
        {
            axiom_children_qname_iterator_t* itr = NULL;	
            qname = axutil_qname_create(env, WSF_WSDL_SERVICE, NULL, NULL);	
            itr = axiom_element_get_children_with_qname(services_element, env, qname,
                                                                   sig_axiom);
        
            axutil_qname_free(qname, env);
            
            while (AXIS2_TRUE == axiom_children_qname_iterator_has_next(itr, env))
            {
                axis2_char_t* address = NULL;
                service_node = (axiom_node_t*)axiom_children_qname_iterator_next(itr, env);
                service_element = (axiom_element_t*)axiom_node_get_data_element(service_node, env);

                address = axiom_element_get_attribute_value_by_name(service_element, 
                                                                    env, 
                                                                    WSF_WSDL_ADDRESS);
                
                if (address && (axutil_strcmp(address, endpoint_address) == 0))
                {
                    axiom_node_t* operations_node = NULL;			
                    axiom_element_t* operations_element = NULL;		
                    axiom_node_t* operation_node = NULL;
                    axiom_element_t* operation_element = NULL;
                    axutil_qname_t* operations_qname = NULL;	
                    axutil_qname_t* operation_qname = NULL;		
                    axiom_children_qname_iterator_t* operation_itr = NULL;	

                    axis2_char_t* soap_version_string = NULL;
                    
                    soap_version_string = axiom_element_get_attribute_value_by_name(service_element, 
                                                                                    env, 
                                                                                    WSF_WSDL_TYPE);
                    
                    if (soap_version_string)
                    {
                        if (axutil_strcmp(soap_version_string, WSF_WSDL_SOAP11) == 0)
                        {
                            *soap_version = 1;
                        }
                        else if (axutil_strcmp(soap_version_string, WSF_WSDL_SOAP12) == 0)
                        {
                            *soap_version = 2; 
                        }       
                        else
                        {       
                            *soap_version = 2;
                        }
                    }

                    operations_qname = axutil_qname_create(env, WSF_WSDL_OPERATIONS, NULL, NULL);
                    operations_element = axiom_element_get_first_child_with_qname(service_element, 
                                                                                  env, 
                                                                                  operations_qname,
                                                                                  service_node, 
                                                                                  &operations_node);
                    axutil_qname_free(operations_qname, env);
                
                    operation_qname = axutil_qname_create(env, WSF_WSDL_OPERATION, NULL, NULL);
                    operation_itr = axiom_element_get_children_with_qname(operations_element, 
                                                                          env, 
                                                                          operation_qname,
                                                                          operations_node);
                    axutil_qname_free(operation_qname, env);

                    while (AXIS2_TRUE == axiom_children_qname_iterator_has_next(operation_itr, env))
                    {
                        axis2_char_t* operation = NULL;
                        operation_node = 
                              (axiom_node_t*)axiom_children_qname_iterator_next(operation_itr, env);
                        operation_element = 
                              (axiom_element_t*)axiom_node_get_data_element(operation_node, env);

                        operation = axiom_element_get_attribute_value_by_name(operation_element, 
                                                                              env, 
                                                                              WSF_WSDL_NAME);
                
                        if (operation && (axutil_strcmp(operation, operation_name) == 0))
                        {
                            *operation_axiom = operation_node;
                            return AXIS2_SUCCESS;
                        }
                    }		
                }
            }
        }
    }
    else /* is_multiple == AXIS2_FALSE */
    {
       /*TODO: implement this */ 
    }
    operation_axiom = NULL;
    
    return AXIS2_FAILURE;
}


/** 
* extracting binding details from the operation_axiom */

axis2_bool_t
wsdl_util_get_binding_details(const axutil_env_t* env,
								  axiom_node_t* operation_axiom,
                                  axis2_char_t** wsa_action, axis2_char_t** soap_action)
{
    axutil_qname_t *qname = NULL;				
    axiom_element_t* operation_element = NULL;
    axiom_node_t* binding_node = NULL;
    axiom_element_t* binding_element = NULL;

    operation_element = (axiom_element_t*)axiom_node_get_data_element(operation_axiom, env);
    
    if (operation_element)
    {
        qname = axutil_qname_create(env, WSF_WSDL_BINDINDG_DETAILS, NULL, NULL);
        binding_element = axiom_element_get_first_child_with_qname(operation_element, env, qname,
                                                                   operation_axiom, &binding_node);
        axutil_qname_free(qname, env);
            
        if (binding_element)
        {
            *wsa_action = axiom_element_get_attribute_value_by_name(binding_element, env, WSF_WSDL_WSAWAACTION);
            *soap_action = axiom_element_get_attribute_value_by_name(binding_element, env, WSF_WSDL_SOAPACTION);
                
            return AXIS2_SUCCESS;
        }
    }
    
    *wsa_action = NULL;
    *soap_action = NULL;
    
    return AXIS2_FAILURE;
}


/**
* retrieving the params axiom from operation axiom */

axis2_bool_t
wsdl_util_get_params_node(const axutil_env_t* env, axiom_node_t* operation_axiom, axiom_node_t** params)
{
	return wsdl_util_get_params_or_returns_node(env, operation_axiom, WSF_WSDL_PARAMS, params);
}


/** 
* retrieving the returns axiom from operation (sig) axiom */

axis2_bool_t
wsdl_util_get_returns_node(const axutil_env_t* env, axiom_node_t* operation_axiom, axiom_node_t** params)
{
	return wsdl_util_get_params_or_returns_node(env, operation_axiom, WSF_WSDL_RETURNS, params);
}


/** 
* retrieving params or returns axioms from operation (sig) axiom */

axis2_bool_t
wsdl_util_get_params_or_returns_node(const axutil_env_t* env, axiom_node_t* operation_axiom, axis2_char_t* node_in_need, axiom_node_t** params)
{
	axiom_element_t* operation_element = NULL;
	axiom_element_t* signature_element = NULL;
	axiom_element_t* params_element = NULL;
	axiom_node_t* params_node = NULL;
	axiom_node_t* signature_node = NULL;
	axutil_qname_t *qname = NULL; 

	operation_element = (axiom_element_t*)axiom_node_get_data_element(operation_axiom, env);

	if (operation_element)
    {
        qname = axutil_qname_create(env, WSF_WSDL_SIGNATURE, NULL, NULL);
        signature_element = axiom_element_get_first_child_with_qname(operation_element, env, qname,
                                                                     operation_axiom, &signature_node);
        axutil_qname_free(qname, env);
            
        if (signature_element)
        {
            qname = axutil_qname_create(env, node_in_need, NULL, NULL);
			params_element = axiom_element_get_first_child_with_qname(signature_element, env, qname,
																	  signature_node, &params_node);
			axutil_qname_free(qname, env);

			*params = params_node;
                
            return AXIS2_SUCCESS;
        }
    }

	return AXIS2_FAILURE;
}


/**
* identifies the binding style of the operation axiom */

axis2_bool_t
wsdl_util_identify_binding_style(const axutil_env_t* env, axiom_node_t* operation_axiom, int* binding_style)
{
	axiom_element_t* operation_element = NULL;
	axiom_element_t* signature_element = NULL;
	axiom_node_t* signature_node = NULL;
	axutil_qname_t *qname = NULL; 
	axis2_char_t* method = NULL;

	operation_element = (axiom_element_t*)axiom_node_get_data_element(operation_axiom, env);

	if (operation_element)
    {
        qname = axutil_qname_create(env, WSF_WSDL_SIGNATURE, NULL, NULL);
        signature_element = axiom_element_get_first_child_with_qname(operation_element, 
                                                                     env, 
                                                                     qname,
                                                                     operation_axiom, 
                                                                     &signature_node);
        axutil_qname_free(qname, env);
            
        if (signature_element)
        {
			method = axiom_element_get_attribute_value_by_name(signature_element,
                                                               env, 
                                                               WSF_WSDL_METHOD);
			if (method)
			{
                if (axutil_strcmp(method, WSF_WSDL_INFERENCE) == 0)
				{
					*binding_style = WSDL_BINDING_STYLE_DOC_LIT_W;
				}
				else if (axutil_strcmp(method, WSF_WSDL_RPC) == 0)
				{
					*binding_style = WSDL_BINDING_STYLE_RPC_ENC;
				}
				else
				{
					*binding_style = -1;
				}
				return AXIS2_SUCCESS;
			}
        }
    }

	return AXIS2_FAILURE;
}


/** 
* retrieves the wrapper element and its namespacde from the params node */
 
axis2_bool_t
wsdl_util_get_wrapper_element(const axutil_env_t* env, axiom_node_t* params_node, axis2_char_t** wrapper_element, axis2_char_t** wrapper_element_ns)
{
	axiom_element_t* params_element = NULL;
	axis2_char_t* wrap_element = NULL;
	axis2_char_t* wrap_element_ns = NULL;
	
	if (!params_node)
	{
		AXIS2_LOG_ERROR_MSG(env->log, "params_node is null");
		return AXIS2_FAILURE;
	}

	params_element = (axiom_element_t*)axiom_node_get_data_element(params_node, env);

	if (params_element)
	{
		wrap_element = axiom_element_get_attribute_value_by_name(params_element, 
                                                                 env, 
                                                                 WSF_WSDL_WRAPPER_ELEMENT);
		if (wrap_element)
		{
			*wrapper_element = wrap_element;
			AXIS2_LOG_DEBUG_MSG(env->log, "wrap_element found");
		}
		
		wrap_element_ns = axiom_element_get_attribute_value_by_name(params_element, 
                                                                    env, 
                                                                    WSF_WSDL_WRAPPER_ELEMENT_NS);
		if (wrap_element_ns)
		{
			*wrapper_element_ns = wrap_element_ns;
			AXIS2_LOG_DEBUG_MSG(env->log, "wrap_element_ns found");
		}

		return AXIS2_SUCCESS;
	}

	AXIS2_LOG_ERROR_MSG(env->log, "failed while fetching wrapper element");

	return AXIS2_FAILURE;
}

axis2_status_t
wsf_wsdl_parser_determine_wsdl_version(
    const axutil_env_t* env,
    axiom_node_t* wsdl_node,
    axis2_bool_t* is_version1_wsdl)
{
    axis2_status_t status = AXIS2_FAILURE;
    axiom_element_t* wsdl_element = NULL;
    axis2_char_t* local_name = NULL;

    if (wsdl_node)
    {
        wsdl_element = (axiom_element_t*)axiom_node_get_data_element(wsdl_node, env);
        local_name = axiom_element_get_localname(wsdl_element, env);

        if (local_name)
        {
            if (strcmp(local_name, WSF_WSDL_DEFINITION) == 0)
            {
                *is_version1_wsdl = AXIS2_TRUE;
                status = AXIS2_SUCCESS;
            }
            else if (strcmp(local_name, WSF_WSDL_DESCRIPTION) == 0)
            {
                *is_version1_wsdl = AXIS2_FALSE;
                status = AXIS2_SUCCESS;
            }
        }
    }

    return status;
}