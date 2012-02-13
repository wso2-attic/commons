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

#include <axiom_util.h>

#include "wsf_wsdl_defines.h"
#include "wsf_wsdl_type_map.h"

/* 
<type-map>
    <forward>
        <!-- <script-type>xsd type</script-type> -->
        <string>string</string>
        <int>int</int>
        <float>float</float>
    </forward>

    <reverse>
        <!-- <xsd-type>best matched script type</xsd-type> -->
        <string>string</string>
        <int>long</int>
        <float>float</float>
    </reverse>
</type-map>
*/


/** 
* Creates the type map. Invoker should clear the memory allocated (axiom_node_t) by this function. */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL 
wsdl_util_create_type_map(
    const axutil_env_t* env, 
    axis2_char_t* type_map_xml_file, 
    axiom_node_t** type_map)
{
    axiom_xml_reader_t* typemap_xml_reader = NULL;
    axiom_document_t* typemap_document = NULL;
    axiom_stax_builder_t* typemap_om_builder = NULL;

    typemap_xml_reader = axiom_xml_reader_create_for_file(env, type_map_xml_file, NULL); /* encoding ?? */
    if (!typemap_xml_reader)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Failed to create xml reader for typemap");
        return AXIS2_FAILURE;
    }

    typemap_om_builder = axiom_stax_builder_create(env, typemap_xml_reader);
    if (!typemap_om_builder)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Failed to create om builder for typemap");
        axiom_xml_reader_free(typemap_xml_reader, env);
        return AXIS2_FAILURE;
    }
    
    typemap_document = axiom_stax_builder_get_document(typemap_om_builder, env);
    if (!typemap_document)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "Failed to create document for typemap");
        axiom_stax_builder_free(typemap_om_builder, env);
        return AXIS2_FAILURE;
    }        
   
    *type_map = axiom_document_build_all(typemap_document, env);
     
    axiom_stax_builder_free_self(typemap_om_builder, env); 
    
    return AXIS2_SUCCESS;
}


/** 
* Following function will validate the type according to the type_map 
*
* @param type1 : type from the scripting language 
* @param type2 : type from the template */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL 
wsdl_util_forward_type_from_type_map(
    const axutil_env_t* env, 
    axiom_node_t* type_map, 
    axis2_char_t* type1, 
    axis2_char_t* type2)
{
    axiom_element_t* type_map_element = NULL, *forward_element = NULL;
    axiom_node_t* forward_node = NULL;
    axutil_qname_t *qname = NULL; 
    axis2_bool_t type_found = AXIS2_FALSE;
    
    type_map_element = (axiom_element_t*)axiom_node_get_data_element(type_map, env);

    if (type_map_element)
    {
        qname = axutil_qname_create(env, WSDL_TYPEMAP_FORWARD, NULL, NULL);
        forward_element = axiom_element_get_first_child_with_qname(type_map_element, env, qname,
                                                                   type_map, &forward_node);
        
        axutil_qname_free(qname, env);

        if (forward_element)
        {
            axiom_children_qname_iterator_t* parse_iter = NULL;

            qname = axutil_qname_create(env, type1, NULL, NULL);
            parse_iter = axiom_element_get_children_with_qname(forward_element, env, qname, forward_node);
                
            while (axiom_children_qname_iterator_has_next(parse_iter, env) == AXIS2_TRUE)
            {
                axiom_node_t* type_node = NULL;
                axiom_element_t* type_element = NULL;
                axis2_char_t* text = NULL;
    
                type_node = (axiom_node_t*)axiom_children_qname_iterator_next(parse_iter, env);
                type_element = (axiom_element_t*)axiom_node_get_data_element(type_node, env);

                text = axiom_element_get_text(type_element, env, type_node);
                
                if (text)
                {
                    if (!axutil_strcmp(text, type2))
                    {
                        type_found = AXIS2_TRUE;
                        break;
                    }
                }
                else
                {
                    AXIS2_LOG_ERROR_MSG(env->log, "Badly formatted type map");
                }
            }

            axutil_qname_free(qname, env);

            if (!type_found)
            {
                AXIS2_LOG_DEBUG_MSG(env->log, "Requested type is not found in the type map");
            }
        }
    }

    return type_found;
}


/** 
* Following function will get the reverse type according to the type_map
*
* @param type1 : type from the xsd 
* @param type2 : type from the scripting language, caller has to clean memory */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL 
wsdl_util_reverse_type_from_type_map(
    const axutil_env_t* env, 
    axiom_node_t* type_map, 
    axis2_char_t* type1, 
    axis2_char_t** type2)
{
    axiom_element_t* type_map_element = NULL, *reverse_element = NULL;
    axiom_node_t* reverse_node = NULL; 
    axutil_qname_t *qname = NULL; 

    type_map_element = (axiom_element_t*)axiom_node_get_data_element(type_map, env);

    if (type_map_element)
    {
        qname = axutil_qname_create(env, WSDL_TYPEMAP_REVERSE, NULL, NULL);
        reverse_element = axiom_element_get_first_child_with_qname(type_map_element, env, qname,
                                                                   type_map, &reverse_node);
        
        axutil_qname_free(qname, env);

        if (reverse_element)
        {
            axiom_node_t* type_node = NULL;
            axiom_element_t* type_element = NULL;

            qname = axutil_qname_create(env,  type1, NULL, NULL);
            type_element = axiom_element_get_first_child_with_qname(reverse_element, env, qname,
                                                                    reverse_node, &type_node);
            
            axutil_qname_free(qname, env);

            if (type_element)
            {
                axis2_char_t* text = NULL;
                text = axiom_element_get_text(type_element, env, type_node);
                if (text)
                {
                    *type2 = (axis2_char_t*)axutil_strdup(env, text);
                    return AXIS2_SUCCESS;
                }
                else
                {
                    AXIS2_LOG_ERROR_MSG(env->log, "Badly formatted type map");
                    return AXIS2_FAILURE;
                }
            }
            else
            {
                AXIS2_LOG_DEBUG_MSG(env->log, "Requested type is not found in the type map");
                return AXIS2_FAILURE;
            }
        }
    }

    return AXIS2_FAILURE;
}


