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

#ifndef WSF_WSDL_TYPE_MAP_H
#define WSF_WSDL_TYPE_MAP_H

/* Type map file should follow the structure below.
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
    axiom_node_t** type_map);

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
    axis2_char_t* type2);

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
    axis2_char_t** type2);

#endif


