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

#ifndef WSF_WSDL_DATA_UTIL_H
#define WSF_WSDL_DATA_UTIL_H


/** 
* function responsible for validating a wsf_wsdl_data_t structre against a wsf_wsdl_data_template_t structure
* validation depends on the criteria specified by validation_criteria see, VALIDATION_CRITERIA_* defines */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsdl_data_util_validate_data(
    const axutil_env_t* env, 
    axiom_node_t* type_map, 
    wsf_wsdl_data_template_t* templ, 
    wsf_wsdl_data_t* data, 
    int validation_criteria);
/**
* Creates a payload according to the data structure provided. Please note the data pointer points to a dummy structure.
* It has a data member (a Hash) with one element. Key corresponds to the Wrapper Element. 
* @param data: Contains all the data under a defined parent node. */

WSF_WSDL_EXTERN axiom_node_t* WSF_WSDL_CALL
wsdl_data_util_create_payload(
    const axutil_env_t* env, 
    wsf_wsdl_data_t* data, 
    int binding_style);

/**
* converts a properly formatted axiom to a template */

void
axiom_to_template(
    const axutil_env_t* env, 
    axiom_node_t* node, 
    wsf_wsdl_data_template_t* templ);


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
wsdl_data_util_axiom_to_template(
    const axutil_env_t* env, 
    axiom_node_t* node, 
    wsf_wsdl_data_template_t** templ);


/*
* converts an axiom to wsf_wsdl_data_t
* Initial call, contains body as the root node. */

void
axiom_to_data(
    const axutil_env_t* env, 
    axiom_node_t* node, 
    wsf_wsdl_data_t* data);

/**
* converts an properly formatted axiom to a wsf_wsdl_data_t structure
* @param node: node's root node is the soapenv:Body. While creating the data, the root node is ignored.  */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_util_axiom_to_data(
    const axutil_env_t* env, 
    axiom_node_t* node, 
    wsf_wsdl_data_t** data);

/** 
* custom link list add element function to support sorting */

void
linked_list_add_sorted(
    axutil_linked_list_t* link_list, 
    wsf_wsdl_data_pair_t* pair, 
    const axutil_env_t* env);

/**
* converts a wsf_wsdl_data_t structure to an axiom node */

void 
wsdl_data_to_axiom_node(const axutil_env_t* env, 
                        axis2_char_t* local_name, 
                        wsf_wsdl_data_t* data, 
                        axiom_node_t* node, 
                        int level, 
                        int sub_level);

/**
* Recursive function used in validating wsf_wsdl_data_t structure against wsf_wsdl_data_template_t structure */

axis2_bool_t
validate_complex(
    const axutil_env_t* env, 
    axiom_node_t* type_map, 
    wsf_wsdl_data_template_t* templ, 
    wsf_wsdl_data_t* data, 
    int validation_criteria);
/**
* validating simple element, used by validate_complex function */

axis2_bool_t
validate_simple(
    const axutil_env_t* env, 
    axiom_node_t* type_map, 
    wsf_wsdl_data_template_t* templ, 
    wsf_wsdl_data_t* data, 
    int validation_criteria);


/**
* handling data null scenario. If there is no required element found against the template, 
* checks the is_nullable flag and returns true if it allows it. */

axis2_bool_t
validate_data_null_scenario(
    wsf_wsdl_data_template_t* templ);


/**
* recursive serialization of wsf_wsdl_data_t structure */

axis2_char_t*
serialize_data(
    const axutil_env_t* env, 
    wsf_wsdl_data_t* data,
    int indentation);

/**
* recursive serialization of wsf_wsdl_data_template_t structure */

axis2_char_t*
serialize_template(
    const axutil_env_t* env, 
    wsf_wsdl_data_template_t* templ,
    int indentation);

/**
* serialization of wsf_wsdl_data_t structure */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_util_serialize_data(
    const axutil_env_t* env, 
    wsf_wsdl_data_t* data,
    axis2_char_t** buffer);


/**
* serialization of wsf_wsdl_data_t structure */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_util_serialize_template(
    const axutil_env_t* env, 
    wsf_wsdl_data_template_t* templ, 
    axis2_char_t** buffer);


#endif /* WSF_WSDL_DATA_UTIL_H */

