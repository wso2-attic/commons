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
#include <stdlib.h>
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
#include "wsf_wsdl_type_map.h"
#include "wsf_wsdl_util.h"

void 
wsdl_data_to_axiom_node(const axutil_env_t* env, axis2_char_t* local_name, wsf_wsdl_data_t* data, axiom_node_t* node, int level, int sub_level);

void
wsdl_data_get_element(const axutil_env_t* env, wsf_wsdl_data_t* parent_element, axis2_char_t* name, void** element);

struct axutil_linked_list
{
    int size;
    entry_t *first;
    entry_t *last;
    int mod_count;
};


/** 
* function responsible for validating a wsf_wsdl_data_t structre against a wsf_wsdl_data_template_t structure
* validation depends on the criteria specified by validation_criteria see, VALIDATION_CRITERIA_* defines */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsdl_data_util_validate_data(const axutil_env_t* env, axiom_node_t* type_map, wsf_wsdl_data_template_t* templ, wsf_wsdl_data_t* data, int validation_criteria)
{
    #ifdef WSDL_DEBUG_MODE
    {
        axis2_char_t* buffer = NULL;
        wsdl_data_util_serialize_data(env, data, &buffer);
        AXIS2_LOG_DEBUG_MSG(env->log, buffer);
        AXIS2_FREE(env->allocator, buffer);
        wsdl_data_util_serialize_template(env, templ, &buffer);
        AXIS2_LOG_DEBUG_MSG(env->log, buffer);
        AXIS2_FREE(env->allocator, buffer);
        buffer = NULL;
    }
    #endif
    
    return validate_complex(env, type_map, templ, data, validation_criteria);
}


/**
* Creates a payload according to the data structure provided. Please note the data pointer points to a dummy structure.
* It has a data member (a Hash) with one element. Key corresponds to the Wrapper Element. 
* @param data: Contains all the data under a defined parent node. */

WSF_WSDL_EXTERN axiom_node_t* WSF_WSDL_CALL
wsdl_data_util_create_payload(const axutil_env_t* env, wsf_wsdl_data_t* data, int binding_style)
{
    /* TODO: create payload according to binding_styles, RPC/Enc Doc/Lit(Wrapped) Doc/Lit(Bare) etc.. */
    axiom_node_t* root_node = NULL;
    axutil_hash_t* hash = NULL;
    unsigned int count = 0;
    axutil_hash_index_t* hi = NULL;
    void* val = NULL;
    axis2_char_t* key = NULL;

    binding_style = 0;
    
    /* only complex object is supported */
    if (data->children_type != CHILDREN_TYPE_ATTRIBUTES)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Data parameter is invalid, Payload creation failed");
        return NULL;
    }

    root_node = axiom_node_create(env);

    hash = (axutil_hash_t*)data->data;

    count = axutil_hash_count(hash);

    /* provided complex object should have only one child */
    if (count != 1) 
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Data parameter is not properly formatted. Remember: Wrapper elements should come under one defined node");
        return NULL;
    }

    for (hi = axutil_hash_first(hash, env); hi; hi = axutil_hash_next(env, hi)) 
    {
        axis2_ssize_t size;
        wsf_wsdl_data_t* sub_data = NULL;
        axutil_hash_this(hi, &key, &size, &val);
        sub_data = (wsf_wsdl_data_t *)val;
        if (sub_data)
        {
            wsdl_data_to_axiom_node(env, key, sub_data, root_node, 1, 1);
        }
        else
        {
            AXIS2_LOG_ERROR_MSG(env->log, "Data parameter is not properly formatted.");
            return NULL;
        }
    }

    return root_node;
}


/**
* converts a properly formatted axiom to a template */

void
axiom_to_template(const axutil_env_t* env, axiom_node_t* node, 
                  wsf_wsdl_data_template_t* templ)
{
    axiom_element_t* element = NULL;
    axutil_qname_t *qname = NULL;
    
    element = (axiom_element_t*)axiom_node_get_data_element(node, env);

    if (element)
    {
        int index = 0;
        axiom_element_t* first_child_element = NULL;
        axiom_node_t* first_child_node = NULL;
        axiom_children_qname_iterator_t* itr = NULL;
        axis2_char_t *data_type = NULL, *type_ns = NULL, *target_ns = NULL, 
                     *is_simple = NULL, *is_nullable = NULL, *min_occurs = NULL, *max_occurs = NULL, *name = NULL;
        int min_occur = 0, max_occur = 1;
        axis2_bool_t is_null = AXIS2_FALSE;
        
        qname = axutil_qname_create(env, WSF_WSDL_PARAM, NULL, NULL);

        itr = axiom_element_get_children_with_qname(element, env, qname, node);

        while (axiom_children_qname_iterator_has_next(itr, env) == AXIS2_TRUE)
        {
            axiom_node_t* sub_node = NULL;
            axiom_element_t* sub_element = NULL;
        
            sub_node = (axiom_node_t*)axiom_children_qname_iterator_next(itr, env);
            sub_element = (axiom_element_t*)axiom_node_get_data_element(sub_node, env);

            name = axiom_element_get_attribute_value_by_name(sub_element, env, WSF_WSDL_NAME);
            data_type = axiom_element_get_attribute_value_by_name(sub_element, env, WSF_WSDL_TYPE);
            type_ns = axiom_element_get_attribute_value_by_name(sub_element, env, WSF_WSDL_TYPE_NAMESPACE);
            target_ns = axiom_element_get_attribute_value_by_name(sub_element, env, WSF_WSDL_TARGETNAMESPACE);
            is_nullable = axiom_element_get_attribute_value_by_name(sub_element, env, WSF_WSDL_NILLABLE);
            is_simple = axiom_element_get_attribute_value_by_name(sub_element, env, WSF_WSDL_SIMPLE);
            min_occurs = axiom_element_get_attribute_value_by_name(sub_element, env, WSF_WSDL_MINOCCURS);
            max_occurs = axiom_element_get_attribute_value_by_name(sub_element, env, WSF_WSDL_MAXOCCURS);

            if (max_occurs)
            {
                if (axutil_strcmp(max_occurs, WSF_UNBOUNDED) == 0)
                {
                    max_occur = -1;
                }
                else
                {
                    max_occur = atoi(max_occurs);
                }
            }

            if (axutil_strcmp(is_nullable, WSF_YES) == 0)
            {
                is_null = AXIS2_TRUE;
            }

            if (min_occurs)
            {
                min_occur = atoi(min_occurs);
            }

            first_child_element = axiom_element_get_first_child_with_qname(sub_element, env, qname, sub_node, &first_child_node);

            if (!first_child_element)
            {
                wsdl_data_template_add_simple_element(env, templ, name, data_type, type_ns, target_ns, is_null, min_occur, max_occur, index++);
            }
            else
            {
                wsf_wsdl_data_template_t* sub_tree = NULL;

                sub_tree = wsdl_data_template_create(env);

                axiom_to_template(env, sub_node, sub_tree);
            
                wsdl_data_template_add_complex_element(env, templ, name, type_ns, target_ns, is_null, min_occur, max_occur, sub_tree, index++);
            }

            name = NULL;
            data_type = NULL;
            type_ns = NULL;
            target_ns = NULL;
            is_nullable = NULL;
            is_simple = NULL;
            min_occurs = NULL;
            max_occurs = NULL;
        }

        axutil_qname_free(qname, env);
    }
}


/**
* Following function will create a template structure from a given axiom. The axiom's root node should be either
* "params" or "returns". 
*
* @param node  : This node's root can be either "params" or "returns" 
* @param templ : Returns the template structure. 
* 
* *********** NOTE: template is under a defined wsf_wsdl_data_template_t node ******
* ***********       also data should come under a defined wsf_wsdl_data_t node *****/

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_util_axiom_to_template(const axutil_env_t* env, axiom_node_t* node, 
                                 wsf_wsdl_data_template_t** templ)
{
    wsf_wsdl_data_template_t* template_to_return = NULL, *sub_template_to_return = NULL;
    axis2_char_t* wrapper_element = NULL, *wrapper_element_ns = NULL; 

    wsdl_util_get_wrapper_element(env, node, &wrapper_element, &wrapper_element_ns);

    if (!wrapper_element)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "wrapper element is NULL, this is not supported yet");
        return;
    }

    template_to_return = wsdl_data_template_create(env);

    sub_template_to_return = wsdl_data_template_create(env);

    axiom_to_template(env, node, sub_template_to_return);

    wsdl_data_template_add_complex_element(env, template_to_return, wrapper_element, wrapper_element_ns, NULL, AXIS2_FALSE, 1, 1, sub_template_to_return, 0);

    *templ = template_to_return;

    return;
}


/*
* converts an axiom to wsf_wsdl_data_t
* Initial call, contains body as the root node. */

void
axiom_to_data(const axutil_env_t* env, axiom_node_t* node, 
              wsf_wsdl_data_t* data)
{
    axiom_element_t* element = NULL;
    axiom_children_iterator_t *children_iter = NULL, *first_parse_iter = NULL;
    axutil_hash_t* hash = NULL;

    element = (axiom_element_t*)axiom_node_get_data_element(node, env);

    first_parse_iter = axiom_element_get_children(element, env, node);

    hash = axutil_hash_make(env);

    /* Fill the hash with array element names */
    if (first_parse_iter)
    {
        while (axiom_children_iterator_has_next(first_parse_iter, env))
        {
            axiom_node_t *om_node = NULL;
            axiom_element_t* om_element = NULL;

            om_node = axiom_children_iterator_next(first_parse_iter, env);
            if (om_node)
            {
                axiom_children_qname_iterator_t * parse_iter = NULL;
                axutil_qname_t* qname = NULL;
                axis2_char_t* local_name = NULL;
                int item_count = 0;

                om_element = (axiom_element_t*)axiom_node_get_data_element(om_node, env);
                local_name = (axis2_char_t*)axiom_element_get_localname(om_element, env);

                qname = axutil_qname_create(env, local_name, NULL, NULL);
                parse_iter = axiom_element_get_children_with_qname(element, env, qname, node);
                
                while (axiom_children_qname_iterator_has_next(parse_iter, env) == AXIS2_TRUE)
                {
                    axiom_node_t* sub_node = NULL;
                    axiom_element_t* sub_element = NULL;

                    if (item_count > 0)
                    {
                        axutil_hash_set(hash, local_name, AXIS2_HASH_KEY_STRING, WSF_WSDL_DEFAULT_STRING);
                        break;
                    }

                    sub_node = (axiom_node_t*)axiom_children_qname_iterator_next(parse_iter, env);
                    sub_element = (axiom_element_t*)axiom_node_get_data_element(sub_node, env);
                    item_count++;
                }

                axutil_qname_free(qname, env);
            }
        }

        axiom_children_iterator_reset(first_parse_iter, env);
    }

    children_iter = axiom_element_get_children(element, env, node);

    if (children_iter)
    {
        while (axiom_children_iterator_has_next(children_iter, env))
        {
            axiom_node_t *om_node = NULL;
            axiom_element_t* om_element = NULL;
            axis2_bool_t is_array_element = AXIS2_FALSE;

            om_node = axiom_children_iterator_next(children_iter, env);
            if (om_node)
            {
                axiom_namespace_t* ns = NULL;
                axis2_char_t* text = NULL, *local_name = NULL, *name_space = NULL;
                axiom_types_t node_type;
                
                node_type = axiom_node_get_node_type(om_node, env); 

                om_element = (axiom_element_t*)axiom_node_get_data_element(om_node, env);

                local_name = (axis2_char_t*)axiom_element_get_localname(om_element, env);
                ns = axiom_element_get_namespace(om_element, env, om_node);
                name_space = ns? axiom_namespace_to_string(ns, env) : NULL;
                
                if (axutil_hash_get(hash, local_name, AXIS2_HASH_KEY_STRING))
                {
                    is_array_element = AXIS2_TRUE;
                }

                if (node_type == AXIOM_ELEMENT)
                {
                    axiom_node_t* child_node = NULL;
                    axiom_element_t* child_element = NULL;

                    child_element = (axiom_element_t*)axiom_element_get_first_element(om_element, env, om_node, &child_node);

                    if (child_element)
                    {
                        wsf_wsdl_data_t* child_data = NULL;
                        child_data = wsdl_data_create_object(env);
                        axiom_to_data(env, om_node, child_data);

                        if (is_array_element == AXIS2_TRUE)
                        {
                            void* children_array = NULL;
                            wsdl_data_get_element(env, data, local_name, &children_array);

                            if (children_array)
                            {
                                wsdl_data_add_object(env, (wsf_wsdl_data_t*)children_array, local_name, WSF_WSDL_UNKNOWN_TYPE, child_data, name_space, NULL);
                            }
                            else
                            {
                                children_array = wsdl_data_create_array(env);
                                wsdl_data_add_object(env, (wsf_wsdl_data_t*)children_array, local_name, WSF_WSDL_UNKNOWN_TYPE, child_data, name_space, NULL);
                                wsdl_data_add_array(env, data, local_name, (wsf_wsdl_data_t*)children_array, name_space, NULL);
                            }
                        }
                        else
                        {
                            wsdl_data_add_object(env, data, local_name, WSF_WSDL_UNKNOWN_TYPE, child_data, name_space, NULL);
                        }
                    }
                    else
                    {
                        text = axiom_element_get_text(om_element, env, om_node);
                        
                        if (is_array_element == AXIS2_TRUE)
                        {
                            void* children_array = NULL;
                            wsdl_data_get_element(env, data, local_name, &children_array);

                            if (children_array)
                            {
                                wsdl_data_add_simple_element(env, (wsf_wsdl_data_t*)children_array, local_name, WSF_WSDL_UNKNOWN_TYPE, text, name_space, NULL);
                            }
                            else
                            {
                                children_array = wsdl_data_create_array(env);
                                wsdl_data_add_simple_element(env, (wsf_wsdl_data_t*)children_array, local_name, WSF_WSDL_UNKNOWN_TYPE, text, name_space, NULL);
                                wsdl_data_add_array(env, data, local_name, (wsf_wsdl_data_t*)children_array, name_space, NULL);
                            }
                        }
                        else
                        {
                            wsdl_data_add_simple_element(env, data, local_name, WSF_WSDL_UNKNOWN_TYPE, text, name_space, NULL);
                        }
                    }					
                }
                else if (node_type == AXIOM_TEXT)
                {
                    text = axiom_element_get_text(om_element, env, om_node);
                    
                    if (is_array_element == AXIS2_TRUE)
                    {
                        void* children_array = NULL;
                        wsdl_data_get_element(env, data, local_name, &children_array);

                        if (children_array)
                        {
                            wsdl_data_add_simple_element(env, (wsf_wsdl_data_t*)children_array, local_name, WSF_WSDL_UNKNOWN_TYPE, text, name_space, NULL);
                        }
                        else
                        {
                            children_array = wsdl_data_create_array(env);
                            wsdl_data_add_simple_element(env, (wsf_wsdl_data_t*)children_array, local_name, WSF_WSDL_UNKNOWN_TYPE, text, name_space, NULL);
                            wsdl_data_add_array(env, data, local_name, (wsf_wsdl_data_t*)children_array, name_space, NULL);
                        }
                    }
                    else
                    {
                        wsdl_data_add_simple_element(env, data, local_name, WSF_WSDL_UNKNOWN_TYPE, text, name_space, NULL);
                    }
                }
            } 
        }
    }
    
    axutil_hash_free(hash, env);
}


/**
* converts an properly formatted axiom to a wsf_wsdl_data_t structure
* @param node: node's root node is the soapenv:Body. While creating the data, the root node is ignored.  */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_util_axiom_to_data(const axutil_env_t* env, axiom_node_t* node, 
                             wsf_wsdl_data_t** data)
{
    wsf_wsdl_data_t* data_to_return = NULL;

    data_to_return = wsdl_data_create_object(env);

    axiom_to_data(env, node, data_to_return);

    *data = data_to_return;

    return;
}


/** 
* custom link list add element function to support sorting */

void
linked_list_add_sorted(axutil_linked_list_t* link_list, wsf_wsdl_data_pair_t* pair, const axutil_env_t* env)
{
    int size = 0;

    size = axutil_linked_list_size(link_list, env);

    if (size == 0)
    {
        axutil_linked_list_add(link_list, env, pair);
    }
    else
    {
        entry_t* current_entry = NULL;
        axis2_bool_t success = AXIS2_FALSE;

        current_entry = link_list->first;

        while (current_entry)
        {
            if (pair->data->index < ((wsf_wsdl_data_pair_t*)(current_entry->data))->data->index)
            {
                entry_t* new_entry = NULL;

                new_entry = AXIS2_MALLOC(env->allocator, sizeof(entry_t));

                new_entry->data = pair;

                new_entry->next = current_entry;
                new_entry->previous = current_entry->previous;
                if (current_entry->previous)
                {
                    current_entry->previous->next = new_entry;
                }
                else
                {
                    link_list->first = new_entry;
                }

                current_entry->previous = new_entry;

                success = AXIS2_TRUE;
                link_list->mod_count++;
                link_list->size++;
            
                break;
            }

            current_entry = current_entry->next;
        }

        if (success == AXIS2_FALSE)
        {
            axutil_linked_list_add_last(link_list, env, pair);
        }
    }
}


/**
* converts a wsf_wsdl_data_t structure to an axiom node */

void 
wsdl_data_to_axiom_node(const axutil_env_t* env, axis2_char_t* local_name, wsf_wsdl_data_t* data, axiom_node_t* node, int level, int sub_level)
{
    if (data)
    { 
        axutil_linked_list_t* link_list = NULL;
        axiom_node_t* current_node = NULL;
        axiom_element_t* current_element = NULL;
        axiom_namespace_t* type_namespace = NULL, *target_namespace = NULL;
        axis2_char_t number[16];

        link_list = axutil_linked_list_create(env);

        if ((data->children_type == CHILDREN_TYPE_NONE) || (data->children_type == CHILDREN_TYPE_ATTRIBUTES))
        {
            if (data->target_ns)
            {
                sprintf(number, "nst%ds%d", level, sub_level);
                target_namespace = axiom_namespace_create(env, (const axis2_char_t*)data->target_ns, number);
            }
            else
            {
                sprintf(number, "nst%ds%d", level, sub_level);
                target_namespace = axiom_namespace_create(env, WSDL_SCHEMA_NULL_STRING, number);
            }

            if (data->type_ns)
            {
                sprintf(number, "ns%ds%d", level, sub_level);
                type_namespace = axiom_namespace_create(env, data->type_ns, number);
            }
            else
            {
                sprintf(number, "ns%ds%d", level, sub_level);
                type_namespace = axiom_namespace_create(env, WSDL_SCHEMA_NULL_STRING, number);
            }

            current_element = axiom_element_create(env, node, local_name, NULL, &current_node);
            axiom_element_set_namespace(current_element, env, type_namespace, current_node);
            
            /*TODO: allow target namespace */
            /*axiom_element_declare_namespace(current_element, env, current_node, target_namespace);*/
        }
           
        if (data->children_type == CHILDREN_TYPE_NONE)    
        {
            wsf_wsdl_data_pair_t* key_attribute_pair = NULL;
            key_attribute_pair = (wsf_wsdl_data_pair_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_data_pair_t)); 
            key_attribute_pair->data = data;
            key_attribute_pair->name = local_name;

            linked_list_add_sorted(link_list, key_attribute_pair, env);
        }
        else if (data->children_type == CHILDREN_TYPE_ATTRIBUTES)
        {
            axutil_hash_t* ht = NULL;
            axutil_hash_index_t *hi;
            void *val;
            
            ht = data->data;
            for (hi = axutil_hash_first(ht, env); hi; hi = axutil_hash_next(env, hi)) 
            {
                wsf_wsdl_data_t* attribute = NULL;
                axis2_char_t* key = NULL;
                axis2_ssize_t size;
                wsf_wsdl_data_pair_t* key_attribute_pair = NULL;

                axutil_hash_this(hi, &key, &size, &val);
                attribute = (wsf_wsdl_data_t *)val;
                if (attribute)
                {
                    key_attribute_pair = (wsf_wsdl_data_pair_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_data_pair_t)); 
                    key_attribute_pair->data = attribute;
                    key_attribute_pair->name = key;
                    linked_list_add_sorted(link_list, key_attribute_pair, env);
                }
            }
        }
        else if (data->children_type == CHILDREN_TYPE_ARRAY_ELEMENTS)
        {
            axutil_array_list_t* al = NULL;
            int al_size = -1;
            int i = 0;
            wsf_wsdl_data_pair_t* key_attribute_pair = NULL;
            
            al = (axutil_array_list_t*)data->data;
            al_size = axutil_array_list_size(al, env);

            for (i = 0; i < al_size; i++)
            {
                wsf_wsdl_data_t* attribute = NULL;
                attribute = (wsf_wsdl_data_t*)axutil_array_list_get(al, env, i);

                if (attribute)
                {
                    key_attribute_pair = (wsf_wsdl_data_pair_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_data_pair_t)); 
                    key_attribute_pair->data = attribute;
                    key_attribute_pair->name = local_name;
                    linked_list_add_sorted(link_list, key_attribute_pair, env);
                }
            }
        }

        while (axutil_linked_list_size(link_list, env) > 0)
        {
            wsf_wsdl_data_pair_t* pair = NULL;
            pair = (wsf_wsdl_data_pair_t*)axutil_linked_list_remove_first(link_list, env);

            if (pair)
            {
                if (pair->data == data)
                {
                    axiom_element_set_text(current_element, env, (axis2_char_t*)(data->data), current_node);
                }
                else
                {
                    wsdl_data_to_axiom_node(env, pair->name, pair->data, current_node, level + 1, ++sub_level);
                }
            }

            AXIS2_FREE(env->allocator, pair);
        }

        axutil_linked_list_free(link_list, env);
    }
}


/**
* Recursive function used in validating wsf_wsdl_data_t structure against wsf_wsdl_data_template_t structure */

axis2_bool_t
validate_complex(const axutil_env_t* env, axiom_node_t* type_map, wsf_wsdl_data_template_t* templ, wsf_wsdl_data_t* data, int validation_criteria)
{
    axis2_bool_t success = AXIS2_FAILURE;

    if (templ->is_simple == AXIS2_FALSE)
    {
        axutil_hash_t* hash = NULL;
        axutil_hash_index_t *hi;
    
        if (!templ->data)
        {
            return AXIS2_FALSE;
        }

        hash = (axutil_hash_t*)templ->data;

        for (hi = axutil_hash_first(hash, env); hi; hi = axutil_hash_next(env, hi)) 
        {
            wsf_wsdl_data_template_t* templ_sub = NULL;
            wsf_wsdl_data_t* data_sub = NULL;
            axis2_char_t* key = NULL;
            void* val = NULL;
            axis2_ssize_t size;
            axutil_hash_this(hi, &key, &size, &val);

            templ_sub = (wsf_wsdl_data_template_t *)val;
            
            if (templ_sub)
            {
                if (data->children_type == CHILDREN_TYPE_ATTRIBUTES)
                {
                    axutil_hash_t* children = (axutil_hash_t*)data->data;
                    
                    data_sub = (wsf_wsdl_data_t*)axutil_hash_get(children, key, size);

                    if (!data_sub)
                    {
                        /* check for nullable, min occur conditions. */
                        success = validate_data_null_scenario(templ_sub);
                    }
                    else /* child not NULL */
                    {
                        if (data_sub->children_type == CHILDREN_TYPE_ARRAY_ELEMENTS)
                        {
                            int i;
                            int list_size; 

                            axutil_array_list_t* array_list = (axutil_array_list_t*)data_sub->data;
                            list_size = axutil_array_list_size(array_list, env);

                            if ((templ_sub->max_occurs > 0) && (list_size > templ_sub->max_occurs))
                            {
                                return AXIS2_FALSE;
                            }

                            for (i = 0; i < list_size; i++)
                            {
                                wsf_wsdl_data_t* element = NULL;
                                element = axutil_array_list_get(array_list, env, i);
                                
                                success = validate_complex(env, type_map, templ_sub, element, validation_criteria);

                                if (success == AXIS2_FALSE)
                                {
                                    return AXIS2_FALSE;
                                }
                            }
                        }
                        else
                        {
                            success = validate_complex(env, type_map, templ_sub, data_sub, validation_criteria);
                        }
                    }
                }
                else if (data->children_type == CHILDREN_TYPE_ARRAY_ELEMENTS)
                {
                    int i;
                    axutil_array_list_t* array_list = (axutil_array_list_t*)data->data;

                    for (i = 0; i < axutil_array_list_size(array_list, env); i++)
                    {
                        wsf_wsdl_data_t* element = NULL;
                        element = axutil_array_list_get(array_list, env, i);
                        
                        success = validate_complex(env, type_map, templ_sub, element, validation_criteria);

                        if (success == AXIS2_FALSE)
                        {
                            return AXIS2_FALSE;
                        }
                    }
                }
                else /* children_type = CHILDREN_TYPE_NONE */
                {
                    return AXIS2_FALSE;
                }
            }
            else /* templ->sub == NULL */
            {
                return AXIS2_FALSE;
            }

            if (success == AXIS2_FALSE)
            {
                return AXIS2_FALSE;
            }
        } /* for */
    }
    else /* is_simple == true */
    {
        success = validate_simple(env, type_map, templ, data, validation_criteria);
    }
    
    if (success == AXIS2_TRUE)
    {
        data->index = templ->index;
    }

    return success;
}


/**
* validating simple element, used by validate_complex function */

axis2_bool_t
validate_simple(const axutil_env_t* env, axiom_node_t* type_map, wsf_wsdl_data_template_t* templ, wsf_wsdl_data_t* data, int validation_criteria)
{
    /* it is known that templ->is_simple == AXIS2_TRUE, if this function is called */
    axis2_char_t* type = NULL;

    if (!data)
    {
        return validate_data_null_scenario(templ);
    }
    
    if (data->children_type != CHILDREN_TYPE_NONE)
    {
        return AXIS2_FALSE;
    }

    if ((validation_criteria == VALIDATION_CRITERIA_REQUEST_MODE_TYPE) || 
        (validation_criteria == VALIDATION_CRITERIA_REQUEST_MODE_TYPE_WITH_NS))
    {
        if (axutil_strcmp(templ->data_type, WSF_ANYTYPE) != 0)
        {
            if (type_map)
            {
                axis2_bool_t type_matched = AXIS2_FALSE;
                type_matched = wsdl_util_forward_type_from_type_map(env, type_map, data->data_type, templ->data_type);
                
                if (type_matched)
                {
                    AXIS2_FREE(env->allocator, data->data_type);
                    data->data_type = axutil_strdup(env, templ->data_type);
                }
                else
                {
                    return AXIS2_FALSE;
                }
            }
            else
            {
                if (axutil_strcmp(templ->data_type, data->data_type) != 0)
                {
                    return AXIS2_FALSE;
                }
            }
        }

        if (validation_criteria == VALIDATION_CRITERIA_REQUEST_MODE_TYPE_WITH_NS)
        {
            if (axutil_strcmp(templ->type_ns, data->type_ns) != 0)
            {
                return AXIS2_FALSE;
            }
        }
    }
    else if ((validation_criteria == VALIDATION_CRITERIA_RESPONSE_MODE) ||
             (validation_criteria == VALIDATION_CRITERIA_RESPONSE_MODE_WITH_NS))
    {
        if (validation_criteria == VALIDATION_CRITERIA_RESPONSE_MODE_WITH_NS)
        {
            if (axutil_strcmp(templ->type_ns, data->type_ns) != 0)
            {
                return AXIS2_FALSE;
            }
        }

        if (type_map)
        {
            if (wsdl_util_reverse_type_from_type_map(env, type_map, templ->data_type, &type))
            {
                if (data->data_type)
                    AXIS2_FREE(env->allocator, data->data_type);
                data->data_type = type;
            }
            else
            {
                AXIS2_LOG_ERROR_MSG(env->log, "Type is not found in the type map!");
            }
        }
        else
        {
            if (data->data_type)
                AXIS2_FREE(env->allocator, data->data_type);
            data->data_type = (axis2_char_t*)axutil_strdup(env, templ->data_type);
        }
    }

    return AXIS2_TRUE;
}


/**
* handling data null scenario. If there is no required element found against the template, checks the is_nullable flag 
* and returns true if it allows it. */

axis2_bool_t
validate_data_null_scenario(wsf_wsdl_data_template_t* templ)
{
    /* For a given template element, there is no data element. 
    It is time to check min_occurs and nullable allows it */
    if (templ->is_nullable)
    {
        /* TODO: Do we have to fill a "nil" element? */
        return AXIS2_TRUE;
    }

    if (templ->min_occurs == 0)
    {
        return AXIS2_TRUE;
    }

    return AXIS2_FALSE;
}


/**
* recursive serialization of wsf_wsdl_data_t structure */

axis2_char_t*
serialize_data(const axutil_env_t* env, wsf_wsdl_data_t* data,
               int indentation)
{
    int i = 0;
    if (data)
    {
        axis2_char_t* buffer = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * 512);
        axis2_char_t* indent = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * 64);
        axis2_char_t* result = NULL;

        memset(indent, 0, 64);

        for (i = 0; ((i <= indentation) && (i < 64)); i++)
        {
            indent[i] = ' ';
        }

        if (data->children_type == CHILDREN_TYPE_NONE)
        {
            sprintf(buffer, "children_type: simple, type_ns: %s, target_ns: %s, data_type: %s, data: %s\n", 
                (data->type_ns)? (axis2_char_t*)(data->type_ns): WSDL_NOT_APPLICABLE_STRING, 
                (data->target_ns)? (axis2_char_t*)(data->target_ns): WSDL_NOT_APPLICABLE_STRING,
                (data->data_type)? (axis2_char_t*)(data->data_type): WSDL_NOT_APPLICABLE_STRING,
                (data->data)? (axis2_char_t*)(data->data): WSDL_NOT_APPLICABLE_STRING);
            
            result = (axis2_char_t*)axutil_strcat(env, indent, buffer, NULL);
        }
        else if (data->children_type == CHILDREN_TYPE_ATTRIBUTES)
        {
            axutil_hash_t* hash = NULL;
            axutil_hash_index_t *hi;
            axis2_char_t* sub_element_key;
            axis2_char_t* sub_elements;
            
            sprintf(buffer, "children_type: hash, type_ns: %s, target_ns: %s, data_type: %s\n", 
                (data->type_ns)? (axis2_char_t*)(data->type_ns): WSDL_NOT_APPLICABLE_STRING, 
                (data->target_ns)? (axis2_char_t*)(data->target_ns): WSDL_NOT_APPLICABLE_STRING,
                (data->data_type)? (axis2_char_t*)(data->data_type): WSDL_NOT_APPLICABLE_STRING);
        
            result = (axis2_char_t*)axutil_strcat(env, (axis2_char_t*)indent, (axis2_char_t*)buffer, NULL);

            hash = (axutil_hash_t*)(data->data);

            for (hi = axutil_hash_first(hash, env); hi; hi = axutil_hash_next(env, hi)) 
            {
                wsf_wsdl_data_t* data_sub = NULL;
                axis2_char_t* key = NULL;
                void* val = NULL;
                axis2_ssize_t size;
                axis2_char_t* cumulative_result = NULL;

                axutil_hash_this(hi, &key, &size, &val);
                
                data_sub = (wsf_wsdl_data_t *)val;

                sub_element_key = (axis2_char_t*)axutil_strcat(env, indent, key, "\n", NULL);
                
                sub_elements = serialize_data(env, (wsf_wsdl_data_t*)data_sub, indentation + 2);

                cumulative_result = (axis2_char_t*)axutil_strcat(env, result, sub_element_key, sub_elements, NULL);

                AXIS2_FREE(env->allocator, sub_element_key);
                AXIS2_FREE(env->allocator, sub_elements);
                AXIS2_FREE(env->allocator, result);

                result = cumulative_result;
            }
        }
        else if (data->children_type == CHILDREN_TYPE_ARRAY_ELEMENTS)
        {
            axutil_array_list_t* array_list = NULL;
            axis2_char_t* sub_elements = NULL;

            sprintf(buffer, "children_type: array, type_ns: %s, target_ns: %s, data_type: %s\n", 
                (data->type_ns)? (axis2_char_t*)(data->type_ns): WSDL_NOT_APPLICABLE_STRING, 
                (data->target_ns)? (axis2_char_t*)(data->target_ns): WSDL_NOT_APPLICABLE_STRING,
                (data->data_type)? (axis2_char_t*)(data->data_type): WSDL_NOT_APPLICABLE_STRING);
            
            result = (axis2_char_t*)axutil_strcat(env, indent, buffer, NULL);

            array_list = (axutil_array_list_t*)data->data;

            for (i = 0; i < axutil_array_list_size(array_list, env); i++)
            {
                wsf_wsdl_data_t* element = NULL;
                axis2_char_t* cumulative_result = NULL;

                element = axutil_array_list_get(array_list, env, i);

                sub_elements = serialize_data(env, (wsf_wsdl_data_t*)element, indentation + 2);

                cumulative_result = (axis2_char_t*)axutil_strcat(env, result, sub_elements, NULL);
                
                AXIS2_FREE(env->allocator, sub_elements);
                AXIS2_FREE(env->allocator, result);

                result = cumulative_result;
            }
        }
        
        AXIS2_FREE(env->allocator, indent);
        AXIS2_FREE(env->allocator, buffer);

        if (indentation == 0)
        {
            axis2_char_t* with_initial_newline = NULL;
            with_initial_newline = (axis2_char_t*)axutil_strcat(env, "\n", result, NULL);
            AXIS2_FREE(env->allocator, result);

            result = with_initial_newline;
        }

        return result;
    }

    return NULL;
}


/**
* recursive serialization of wsf_wsdl_data_template_t structure */

axis2_char_t*
serialize_template(const axutil_env_t* env, wsf_wsdl_data_template_t* templ,
                   int indentation)
{
    int i = 0;
    if (templ)
    {
        axis2_char_t* buffer = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * 512);
        axis2_char_t* indent = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) * 64);
        axis2_char_t* result = NULL;

        memset(indent, 0, 64);

        for (i = 0; ((i <= indentation) && (i < 64)); i++)
        {
            indent[i] = ' ';
        }

        if (templ->is_simple == AXIS2_TRUE)
        {
            sprintf(buffer, "type_ns: %s, target_ns: %s, data_type: %s, is_simple: %d, is_nullable: %d, min: %d, max: %d\n", 
                (templ->type_ns)? (axis2_char_t*)(templ->type_ns): WSDL_NOT_APPLICABLE_STRING, 
                (templ->target_ns)? (axis2_char_t*)(templ->target_ns): WSDL_NOT_APPLICABLE_STRING,
                (templ->data_type)? (axis2_char_t*)(templ->data_type): WSDL_NOT_APPLICABLE_STRING,
                templ->is_simple, templ->is_nullable, templ->min_occurs, templ->max_occurs);
            
            result = (axis2_char_t*)axutil_strcat(env, indent, buffer, NULL);
        }
        else 
        {
            axutil_hash_t* hash = NULL;
            axutil_hash_index_t *hi;
            axis2_char_t* sub_element_key;
            axis2_char_t* sub_elements;
            
            sprintf(buffer, "type_ns: %s, target_ns: %s, data_type: %s, is_simple: %d, is_nullable: %d, min: %d, max: %d\n", 
                (templ->type_ns)? (axis2_char_t*)(templ->type_ns): WSDL_NOT_APPLICABLE_STRING, 
                (templ->target_ns)? (axis2_char_t*)(templ->target_ns): WSDL_NOT_APPLICABLE_STRING,
                (templ->data_type)? (axis2_char_t*)(templ->data_type): WSDL_NOT_APPLICABLE_STRING,
                templ->is_simple, templ->is_nullable, templ->min_occurs, templ->max_occurs);
        
            result = (axis2_char_t*)axutil_strcat(env, (axis2_char_t*)indent, (axis2_char_t*)buffer, NULL);

            hash = (axutil_hash_t*)(templ->data);

            for (hi = axutil_hash_first(hash, env); hi; hi = axutil_hash_next(env, hi)) 
            {
                wsf_wsdl_data_template_t* templ_sub = NULL;
                axis2_char_t* key = NULL;
                void* val = NULL;
                axis2_ssize_t size;
                axis2_char_t* cumulative_result = NULL;

                axutil_hash_this(hi, &key, &size, &val);
                
                templ_sub = (wsf_wsdl_data_template_t *)val;

                sub_element_key = (axis2_char_t*)axutil_strcat(env, indent, key, "\n", NULL);
                
                sub_elements = serialize_template(env, (wsf_wsdl_data_template_t*)templ_sub, indentation + 2);

                cumulative_result = (axis2_char_t*)axutil_strcat(env, result, sub_element_key, sub_elements, NULL);

                AXIS2_FREE(env->allocator, sub_element_key);
                AXIS2_FREE(env->allocator, sub_elements);
                AXIS2_FREE(env->allocator, result);

                result = cumulative_result;
            }
        }
        
        AXIS2_FREE(env->allocator, indent);
        AXIS2_FREE(env->allocator, buffer);

        if (indentation == 0)
        {
            axis2_char_t* with_initial_newline = NULL;
            with_initial_newline = (axis2_char_t*)axutil_strcat(env, "\n", result, NULL);
            AXIS2_FREE(env->allocator, result);

            result = with_initial_newline;
        }

        return result;
    }

    return NULL;
}


/**
* serialization of wsf_wsdl_data_t structure */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_util_serialize_data(const axutil_env_t* env, wsf_wsdl_data_t* data,
                              axis2_char_t** buffer)
{
    *buffer = serialize_data(env, data, 0);
}


/**
* serialization of wsf_wsdl_data_t structure */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_util_serialize_template(const axutil_env_t* env, wsf_wsdl_data_template_t* templ, 
                                  axis2_char_t** buffer)
{
    *buffer = serialize_template(env, templ, 0);
}
