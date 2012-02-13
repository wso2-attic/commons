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

#ifndef WSF_WSDL_DATA_TEMPLATE_H
#define WSF_WSDL_DATA_TEMPLATE_H


/**
* *THE* template structure used to validate data in the wsdl mode module
*/
typedef struct wsf_wsdl_data_template
{
    axis2_char_t* data_type;
    axis2_char_t* type_ns;
    axis2_char_t* target_ns;
    axis2_bool_t is_simple;
    axis2_bool_t is_nullable;
    int min_occurs;
    int max_occurs;				/* -1 for unbounded */
    void* data;					/* this could be either a axutil_hash pointer or a axis2_char_t pointer. */
    int index;
    axutil_hash_t* attributes; 
} wsf_wsdl_data_template_t;


/**
* Creates the template structure.
* 
* @param env		: environment structure 
*
* @returns the template structure */

WSF_WSDL_EXTERN wsf_wsdl_data_template_t* WSF_WSDL_CALL
wsdl_data_template_create(const axutil_env_t* env);


/**
* Adds a simple template element to the complex template element as a child
*
* @param env			 : environment structure										[IN]	
* @param parent_element	 : added element will be a child element of this element.		[MODIFY]
* @param name			 : name of the element added									[IN]
* @param data_type		 : data type													[IN]
* @param type_ns		 : type name space												[IN]
* @param target_ns		 : target name space											[IN]
* @param is_nullable	 : AXIS2_TRUE or AXIS2_FALSE									[IN]
* @param min_occurs		 : integer value greater than or equal to 0						[IN]
* @param max_occurs		 : -1 if "unbounded", and integer >= 0 otherwise				[IN]
* @param index			 : order of the child element that should be appeared			[IN] */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_template_add_simple_element(const axutil_env_t* env, wsf_wsdl_data_template_t* parent_element,
                                      axis2_char_t* name, axis2_char_t* data_type, 
                                      axis2_char_t* type_ns, axis2_char_t* target_ns,
                                      axis2_bool_t is_nullable,
                                      int min_occurs, int max_occurs, int index);


/**
* Adds a complex template element to the complex template element as a child
*
* @param env			 : environment structure										[IN]	
* @param parent_element	 : added element will be a child element of this element.		[MODIFY]
* @param name			 : name of the element added									[IN]
* @param data_type		 : data type													[IN]
* @param type_ns		 : type name space												[IN]
* @param target_ns		 : target name space											[IN]
* @param is_nullable	 : AXIS2_TRUE or AXIS2_FALSE									[IN]
* @param min_occurs		 : integer value greater than or equal to 0						[IN]
* @param max_occurs		 : -1 if "unbounded", and integer >= 0 otherwise				[IN]
* @param data			 : *THE* complex element to be added							[IN]
* @param index			 : order of the child element that should be appeared			[IN] */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_template_add_complex_element(const axutil_env_t* env, wsf_wsdl_data_template_t* parent_element,
                                       axis2_char_t* name,  
                                       axis2_char_t* type_ns, axis2_char_t* target_ns,
                                       axis2_bool_t is_nullable,
                                       int min_occurs, int max_occurs, void* data, int index);


/**
* Adds a complex template element to the complex template element as a child
* Above two functions are abstractions of the following function. Call the following iff you know what you are doing 
*
* @param env			 : environment structure										[IN]	
* @param parent_element	 : added element will be a child element of this element.		[MODIFY]
* @param name			 : name of the element added									[IN]
* @param data_type		 : data type													[IN]
* @param is_simple		 : if AXIS2_TRUE, data will be ignored. considered otherwise	[IN]
* @param type_ns		 : type name space												[IN]
* @param target_ns		 : target name space											[IN]
* @param is_nullable	 : AXIS2_TRUE or AXIS2_FALSE									[IN]
* @param min_occurs		 : integer value greater than or equal to 0						[IN]
* @param max_occurs		 : -1 if "unbounded", and integer >= 0 otherwise				[IN]
* @param data			 : *THE* complex element to be added, see is_simple flag    	[IN]
* @param index			 : order of the child element that should be appeared			[IN] */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_template_add_element(const axutil_env_t* env, wsf_wsdl_data_template_t* parent_element,
                               axis2_char_t* name, axis2_char_t* data_type, 
                               axis2_char_t* type_ns, axis2_char_t* target_ns,
                               axis2_bool_t is_simple, axis2_bool_t is_nullable,
                               int min_occurs, int max_occurs,
                               void* data, /* depends on the is_simple flag */
                               int index);		


/**
* Free function for the template structure
* 
* @param env			 : environment structure										[IN]	
* @param data			 : template to free												[MODIFY] */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_template_free(const axutil_env_t* env, wsf_wsdl_data_template_t* data);

#endif /* WSF_WSDL_DATA_H */

