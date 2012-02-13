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

#ifndef WSF_WSDL_DATA_H
#define WSF_WSDL_DATA_H

/**
* *THE* data structure used to feed data to the wsdl mode module
*/
typedef struct wsf_wsdl_data
{
    unsigned short children_type;
    axis2_char_t* data_type;
    axis2_char_t* type_ns;
    axis2_char_t* target_ns;
    int index;
    void* data;
    axutil_hash_t* attributes;
} wsf_wsdl_data_t;


/**
* Structure used in linklist sorting 
*/
typedef struct wsf_wsdl_data_pair
{
    wsf_wsdl_data_t* data;		
    axis2_char_t* name;
} wsf_wsdl_data_pair_t;


/**
* Structure used in iterating wsf_wsdl_data structure 
*/
typedef struct wsf_wsdl_data_iterator
{
    wsf_wsdl_data_t* element;	/* parent wsdl_data_t structure */
    wsf_wsdl_data_t* this;		/* current child */
    axis2_char_t* name;			/* name for the child, null for array. */
    int type;		/* SIMPLE, ARRAY, COMPLEX */
    void* index;				/* to keep track of index on Array or Hash */
} wsf_wsdl_data_iterator_t;


/**
* Creates the wsdl data structure corresponding to an object. 
* To feed an object(complex) to the wsdl mode module, first use this function
* to create the structure and use "add" functions to add elements.
*
* @param env		: environment structure 
*
* @returns created structure. */

WSF_WSDL_EXTERN wsf_wsdl_data_t* WSF_WSDL_CALL
wsdl_data_create_object(const axutil_env_t* env);


/**
* Creates the wsdl data structure corresponding to an array. 
* To feed an array to the wsdl mode module, first use this function
* to create the structure and use "add" functions to add elements.
* 
* @param env		: environment structure 
*
* @returns created structure. */

WSF_WSDL_EXTERN wsf_wsdl_data_t* WSF_WSDL_CALL
wsdl_data_create_array(const axutil_env_t* env);


/**
* Adds a simple element to an array or to an object created by "create" functions
*
* @param env			 : environment structure											
* @param parent_element	 : added element will be a child element of this element.		
* @param name			 : name of the element added									
* @param data_type		 : data type													
* @param data			 : "the simple element" added, provide a axis2_char_t* buffer	
* @param type_ns		 : type name space												
* @param target_ns		 : target name space											 */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_add_simple_element(const axutil_env_t* env, wsf_wsdl_data_t* parent_element,
                             axis2_char_t* name, axis2_char_t* data_type, axis2_char_t* data,
                             axis2_char_t* type_ns, axis2_char_t* target_ns);


/**
* Adds a complex element(i.e. an array or an object) to an array or to an object created by "create" functions
* Consider this as a low level function. Following two functions are simplified to provide the same functionality.
* 
* @param env			 : environment structure											
* @param parent_element	 : added element will be a child element of this element.		
* @param name			 : name of the element added									
* @param data_type		 : data type													
* @param data			 : "the complex element" added, provide an array or an object	
* @param type_ns		 : type name space												
* @param target_ns		 : target name space											
* @param children_type	 : CHILDREN_TYPE_ATTRIBUTES (object) or 
*												CHILDREN_TYPE_ARRAY_ELEMENTS (array)     */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_add_complex_element(const axutil_env_t* env, wsf_wsdl_data_t* parent_element, 
                              axis2_char_t* name, axis2_char_t* data_type, wsf_wsdl_data_t* data,
                              axis2_char_t* type_ns, axis2_char_t* target_ns, unsigned short children_type);


/**
* Adds an array to an array or to an object created by "create" functions
* 
* @param env			 : environment structure											
* @param parent_element	 : added element will be a child element of this element.		
* @param name			 : name of the element added									
* @param data_type		 : data type													
* @param data			 : THE array added												
* @param type_ns		 : type name space												
* @param target_ns		 : target name space											 */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_add_array(const axutil_env_t* env, wsf_wsdl_data_t* parent_element, 
                     axis2_char_t* name, wsf_wsdl_data_t* data,
                     axis2_char_t* type_ns, axis2_char_t* target_ns);


/**
* Adds an object to an array or to an object created by "create" functions
* 
* @param env			 : environment structure											
* @param parent_element	 : added element will be a child element of this element.		
* @param name			 : name of the element added									
* @param data_type		 : data type													
* @param data			 : THE object added												
* @param type_ns		 : type name space												
* @param target_ns		 : target name space											 */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_add_object(const axutil_env_t* env, wsf_wsdl_data_t* parent_element, 
                     axis2_char_t* name, axis2_char_t* data_type, wsf_wsdl_data_t* data,
                     axis2_char_t* type_ns, axis2_char_t* target_ns);


/**
* The free function fo wsf_wsdl_data_t structure
* 
* @param env			 : environment structure											
* @param data			 : element to be free											 */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_free(const axutil_env_t* env, wsf_wsdl_data_t* data);


/*\________________________________________________________________________________________________________________/*\ 
  Following functions are to iterate through the wsf_wsdl_data_t structure. (to hide the complexities of the struct). 
\*------------------------------------------------------------------------------------------------------------------*/


/**
* Creates the iterator
*
* @param env			 : environment structure											
* @param element	     : attached data element										
*
* @returns the iterator created. */

WSF_WSDL_EXTERN wsf_wsdl_data_iterator_t* WSF_WSDL_CALL
wsdl_data_iterator_create(const axutil_env_t* env, wsf_wsdl_data_t* element);


/** 
* Free the iterator
*
* @param env			 : environment structure											
* @param ite			 : iterator to be free											 */

WSF_WSDL_EXTERN void WSF_WSDL_CALL
wsdl_data_iterator_free(const axutil_env_t* env, wsf_wsdl_data_iterator_t* ite);


/** 
* Reset to the first child element of the wsf_wsdl_data_t structure.
*
* @param env			 : environment structure											
* @param ite			 : iterator to be modified										
*
* @returns AXIS2_FALSE or AXIS2_TRUE depending on the availability of the first element */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsdl_data_iterator_first(const axutil_env_t* env, wsf_wsdl_data_iterator_t** ite);


/**
* Move the iterator to the next child element of the wsf_wsdl_data_t structure.
*
* @param env			 : environment structure											
* @param data			 : iterator to be modified										
*
* @returns AXIS2_FALSE or AXIS2_TRUE depending on the availability of the next element */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsdl_data_iterator_next(const axutil_env_t* env, wsf_wsdl_data_iterator_t** ite);




#endif /* WSF_WSDL_DATA_H */

