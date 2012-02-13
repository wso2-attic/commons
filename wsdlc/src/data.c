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

#include <axutil_hash.h>
#include <axutil_array_list.h>
#include <axutil_string.h>

#include "wsf_wsdl_defines.h"
#include "wsf_wsdl_data.h"

/** 
* creates the wsf_wsdl_data_t structure as a complex object  */

WSF_WSDL_EXTERN wsf_wsdl_data_t* WSF_WSDL_CALL 
wsdl_data_create_object(const axutil_env_t* env)
{
    wsf_wsdl_data_t* data = NULL;
    data = (wsf_wsdl_data_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_data_t));
    data->data_type = NULL;
    data->data = NULL;
    data->children_type = CHILDREN_TYPE_ATTRIBUTES; 
    data->type_ns = NULL;
    data->target_ns = NULL;
    data->index = -1;
    return data;
}


/** 
* creates the wsf_wsdl_data_t structure as an array  */

WSF_WSDL_EXTERN wsf_wsdl_data_t* WSF_WSDL_CALL
wsdl_data_create_array(const axutil_env_t* env)
{
    wsf_wsdl_data_t* data = NULL;
    data = (wsf_wsdl_data_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_data_t));
    data->data_type = NULL;
    data->data = NULL;
    data->type_ns = NULL;
    data->target_ns = NULL;
    data->children_type = CHILDREN_TYPE_ARRAY_ELEMENTS;
    data->index = -1;
    return data;
}


/** 
* retrieves the element by name, expecting only an complex object and arrays, simple elements are ignored  */

void  
wsdl_data_get_element(const axutil_env_t* env, wsf_wsdl_data_t* parent_element,
                      axis2_char_t* name, void** element)
{
    /* params should be valid to proceed */
    if ((!parent_element) || (!name) || (!element))
    {
        AXIS2_LOG_ERROR_MSG(env->log, "get_element has NULL parent_element or name");
        *element = NULL;
        return;
    }
    
    /* only interested in complex objects, so ignore simple and array elements */
    if ((parent_element->children_type == CHILDREN_TYPE_NONE) || (parent_element->children_type == CHILDREN_TYPE_ARRAY_ELEMENTS))
    {
        AXIS2_LOG_ERROR_MSG(env->log, "to get a child the children type should be object");
        *element = NULL;
        return;
    }
    else if (parent_element->children_type == CHILDREN_TYPE_ATTRIBUTES) /* parent_element->data should contain a pointer to a hash table */   
    {
        *element = axutil_hash_get((axutil_hash_t*)parent_element->data, name, AXIS2_HASH_KEY_STRING);
    }
}


/**
* adds a simple element to a complex object or to an array as a child*/

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_add_simple_element(const axutil_env_t* env, wsf_wsdl_data_t* parent_element,
                             axis2_char_t* name, axis2_char_t* data_type, axis2_char_t* data,
                             axis2_char_t* type_ns, axis2_char_t* target_ns)
{
    if (!parent_element)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "add_simple_element has NULL parent_element");
        return;
    }
    
    /* depending on the type of the parent element, taking different approaches to add the simple element */
    if (parent_element->children_type == CHILDREN_TYPE_NONE)
    {
        /* could not add a simple element to a simple element, that does not make sense.. */
        AXIS2_LOG_ERROR_MSG(env->log, "to add a child the children type should be either array or object");
        return;
    }
    else if (parent_element->children_type == CHILDREN_TYPE_ATTRIBUTES) /* parent_element->data should contain a pointer to a hash table */   
    {
        wsf_wsdl_data_t* attribute = NULL;
        
        if (!name)
        {
            AXIS2_LOG_ERROR_MSG(env->log, "to add a child as an attribute the name should NOT be NULL");
            return;
        }

        if (!parent_element->data)
        {
            parent_element->data = axutil_hash_make(env);
        }
        
        attribute = wsdl_data_create_object(env);

        attribute->data_type = data_type? axutil_strdup(env, data_type) : NULL;
        attribute->data = data? axutil_strdup(env, data) : NULL;
        attribute->type_ns = type_ns? axutil_strdup(env, type_ns) : NULL;
        attribute->target_ns = type_ns? axutil_strdup(env, target_ns) : NULL;
        attribute->children_type = CHILDREN_TYPE_NONE;

        axutil_hash_set((axutil_hash_t*)parent_element->data, name, AXIS2_HASH_KEY_STRING, attribute);        
    } 
    else if (parent_element->children_type == CHILDREN_TYPE_ARRAY_ELEMENTS)
    { 
        wsf_wsdl_data_t* attribute = NULL;     
        
        if (!parent_element->data)
        {
            parent_element->data = axutil_array_list_create(env, -1);
        }
        
        attribute = wsdl_data_create_object(env);

        attribute->data_type = data_type? axutil_strdup(env, data_type) : NULL;
        attribute->data = data? axutil_strdup(env, data) : NULL;
        attribute->children_type = CHILDREN_TYPE_NONE;
        attribute->type_ns = type_ns? axutil_strdup(env, type_ns) : NULL;
        attribute->target_ns = target_ns? axutil_strdup(env, target_ns) : NULL;

        axutil_array_list_add((axutil_array_list_t*)(parent_element->data), env, attribute);
    }
}


/**
* adds (a complex object or an array) to an array or to a complex object, as a child. 
* next two functions are the abstractions of this */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_add_complex_element(const axutil_env_t* env, wsf_wsdl_data_t* parent_element, 
                              axis2_char_t* name, axis2_char_t* data_type, wsf_wsdl_data_t* data,
                              axis2_char_t* type_ns, axis2_char_t* target_ns, unsigned short children_type)
{
    if (!parent_element)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "add_complex_element has NULL parent_element");
        return;
    }

    /* depending on the type of the parent element, taking different approaches to add the simple element */
    if (parent_element->children_type == CHILDREN_TYPE_NONE)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "to add a child, the children type should be either array or object");
        return;
    }
    else if (parent_element->children_type == CHILDREN_TYPE_ATTRIBUTES) /* parent_element->data should contain a pointer to a hash table */
    {
        wsf_wsdl_data_t* attribute = NULL;
        
        if (!parent_element->data)
        {
            parent_element->data = axutil_hash_make(env);
        }
        
        attribute = data;

        attribute->data_type = data_type? axutil_strdup(env, data_type) : NULL;
        attribute->children_type = children_type;
        attribute->type_ns = type_ns? axutil_strdup(env, type_ns) : NULL;
        attribute->target_ns = target_ns? axutil_strdup(env, target_ns) : NULL;

        axutil_hash_set((axutil_hash_t*)parent_element->data, name, AXIS2_HASH_KEY_STRING, attribute);
    }
    else if (parent_element->children_type == CHILDREN_TYPE_ARRAY_ELEMENTS)
    {
        wsf_wsdl_data_t* attribute = NULL;  
        
        if (!parent_element->data)
        {
            parent_element->data = axutil_array_list_create(env, -1);
        }
        
        attribute = data;

        attribute->data_type = data_type? axutil_strdup(env, data_type) : NULL;
        attribute->children_type = children_type;
        attribute->type_ns = type_ns? axutil_strdup(env, type_ns) : NULL;
        attribute->target_ns = target_ns? axutil_strdup(env, target_ns) : NULL;
        axutil_array_list_add((axutil_array_list_t*)parent_element->data, env, attribute);
    }
}


/**
* adds a array to a complex object or to an array as a child element. */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_add_array(const axutil_env_t* env, wsf_wsdl_data_t* parent_element, 
                     axis2_char_t* name, wsf_wsdl_data_t* data,
                     axis2_char_t* type_ns, axis2_char_t* target_ns)
{
    wsdl_data_add_complex_element(env, parent_element, name, NULL, data, type_ns, target_ns, CHILDREN_TYPE_ARRAY_ELEMENTS);
}


/**
* adds a complex object to a complex object or to an array as a child element. */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_add_object(const axutil_env_t* env, wsf_wsdl_data_t* parent_element, 
                     axis2_char_t* name, axis2_char_t* data_type, wsf_wsdl_data_t* data,
                     axis2_char_t* type_ns, axis2_char_t* target_ns)
{
    wsdl_data_add_complex_element(env, parent_element, name, data_type, data, type_ns, target_ns, CHILDREN_TYPE_ATTRIBUTES);
}


/**
* free the wsf_wsdl_data_t structure */

WSF_WSDL_EXTERN void WSF_WSDL_CALL
wsdl_data_free(const axutil_env_t* env, wsf_wsdl_data_t* data)
{
    if (data)
    {
        if (data->data_type)
        {
            AXIS2_FREE(env->allocator, data->data_type);
        }

        if (data->target_ns)
        {
            AXIS2_FREE(env->allocator, data->target_ns);
        }

        if (data->type_ns)
        {
            AXIS2_FREE(env->allocator, data->type_ns);
        }
       
        if (data->children_type == CHILDREN_TYPE_NONE)    
        {
            AXIS2_FREE(env->allocator, data->data);
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
                axutil_hash_this(hi, NULL, NULL, &val);
                attribute = (wsf_wsdl_data_t *)val;
                if (attribute)
                {
                    wsdl_data_free(env, attribute);
                }
            }
            
            axutil_hash_free((axutil_hash_t*)(data->data), env);
            data->data = NULL;
        }
        else if (data->children_type == CHILDREN_TYPE_ARRAY_ELEMENTS)
        {
            axutil_array_list_t* al = NULL;
            
            al = data->data;
            while (axutil_array_list_is_empty(al, env) == AXIS2_FALSE)
            {
                wsf_wsdl_data_t* attribute = NULL;
                attribute = (wsf_wsdl_data_t*) axutil_array_list_get(al, env, 0);
                axutil_array_list_remove(al, env, 0);

                if (attribute)
                {
                    wsdl_data_free(env, attribute);
                }
            }
            axutil_array_list_free(al, env);
        }

        AXIS2_FREE(env->allocator, data); 
    }
}


/**
* creates the iterator */ 

WSF_WSDL_EXTERN wsf_wsdl_data_iterator_t* WSF_WSDL_CALL
wsdl_data_iterator_create(const axutil_env_t* env, wsf_wsdl_data_t* element)
{
    wsf_wsdl_data_iterator_t* iterator = NULL;
    iterator = (wsf_wsdl_data_iterator_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_data_iterator_t));

    iterator->element = element;
    iterator->index = NULL;
    iterator->name = NULL;
    iterator->type = -1;
    iterator->this = NULL;
    
    return iterator;
}


/** 
* free the iterator */

WSF_WSDL_EXTERN void WSF_WSDL_CALL
wsdl_data_iterator_free(const axutil_env_t* env, wsf_wsdl_data_iterator_t* iterator)
{
    if (iterator)
    {
        if (iterator->index)
            AXIS2_FREE(env->allocator, iterator->index);
        
        if (iterator->name)
            AXIS2_FREE(env->allocator, iterator->name);
        
        AXIS2_FREE(env->allocator, iterator);
    }
    
    return;
}


/** 
* reset the iterator to point to the first child element */ 

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsdl_data_iterator_first(const axutil_env_t* env, wsf_wsdl_data_iterator_t** ite)
{
    axis2_bool_t success = AXIS2_FALSE;

    if ((!ite) || (!(*ite)))
        return success;

    switch ((*ite)->element->children_type)
    {
    case CHILDREN_TYPE_ATTRIBUTES:
        {
            axutil_hash_t* hash = NULL;
            axutil_hash_index_t* index = NULL;
            axis2_char_t* key = NULL;
            void* value = NULL;
            axis2_ssize_t key_len;
            
            hash = (axutil_hash_t*)((*ite)->element->data);
            index = axutil_hash_first(hash, env);

            axutil_hash_this(index, &key, &key_len, &value);

            (*ite)->index = index;
            (*ite)->type = (*ite)->element->children_type;
            (*ite)->name = (axis2_char_t*)axutil_strdup(env, key);
            (*ite)->this = (wsf_wsdl_data_t*)value;

            success = AXIS2_TRUE;
        }
        break;
    case CHILDREN_TYPE_ARRAY_ELEMENTS:
        {
            axutil_array_list_t* array_list = NULL;
            int array_list_size = -1;
            array_list = (axutil_array_list_t*)((*ite)->element->data);
            
            array_list_size = axutil_array_list_size(array_list, env);

            if (array_list_size >= 0)
            {
                (*ite)->this = (wsf_wsdl_data_t*)axutil_array_list_get(array_list, env, 0);

                if ((*ite)->name)
                {
                    AXIS2_FREE(env->allocator, (*ite)->name);
                }
                
                (*ite)->name = NULL;
                (*ite)->type = (*ite)->element->children_type;

                if ((*ite)->index)
                {
                    AXIS2_FREE(env->allocator, (*ite)->index);
                    (*ite)->index = (int*)AXIS2_MALLOC(env->allocator, sizeof(int));
                }

                *(int*)((*ite)->index) = 0;

                success = AXIS2_TRUE;
            }
            else
            {
                (*ite)->this = NULL;
                success = AXIS2_FALSE;
            }
        }
        break;
    case CHILDREN_TYPE_NONE:
    default:
        if ((*ite)->name)
            AXIS2_FREE(env->allocator, (*ite)->name);
        if ((*ite)->index)
            AXIS2_FREE(env->allocator, (*ite)->index);		

        (*ite)->name = NULL;
        (*ite)->this = NULL;
        (*ite)->type = (*ite)->element->children_type;
        (*ite)->index = NULL;
        
        success = AXIS2_FALSE;
        break;
    }

    return success;
}


/**
* increment the iterator to point to the next element */

WSF_WSDL_EXTERN axis2_bool_t WSF_WSDL_CALL
wsdl_data_iterator_next(const axutil_env_t* env, wsf_wsdl_data_iterator_t** ite)
{
    axis2_bool_t success = AXIS2_FALSE;

    if ((!ite) || (!(*ite)))
        return success;

    switch ((*ite)->element->children_type)
    {
    case CHILDREN_TYPE_ATTRIBUTES:
        {
            axutil_hash_t* hash = NULL;
            axutil_hash_index_t* index = NULL;
            axis2_char_t* key = NULL;
            void* value = NULL;
            axis2_ssize_t key_len;
            hash = (axutil_hash_t*)((*ite)->element->data);

            index = (axutil_hash_index_t*)((*ite)->index);

            index = axutil_hash_next(env, index);

            if (index)
            {
                (*ite)->index = index;
                (*ite)->type = (*ite)->element->children_type;
                
                axutil_hash_this(index, &key, &key_len, &value);

                (*ite)->name = (axis2_char_t*)axutil_strdup(env, key);
                (*ite)->this = (wsf_wsdl_data_t*)value;
                success = AXIS2_TRUE;
            }
            else
            {
                (*ite)->index = NULL;
                (*ite)->type = (*ite)->element->children_type;

                if ((*ite)->name)
                {
                    AXIS2_FREE(env->allocator, (*ite)->name);
                }
                
                (*ite)->name = NULL;
                (*ite)->this = NULL;
                success = AXIS2_FALSE;
            }
        }
        break;
    case CHILDREN_TYPE_ARRAY_ELEMENTS:
        {
            axutil_array_list_t* array_list = NULL;
            int array_list_size = -1;
            int index = -1;
            array_list = (axutil_array_list_t*)((*ite)->element->data);
            
            array_list_size = axutil_array_list_size(array_list, env);
            
            index = *(int*)((*ite)->index);

            if ((index >= 0) && (index < array_list_size))
            {
                (*ite)->this = (wsf_wsdl_data_t*)axutil_array_list_get(array_list, env, index + 1);

                if ((*ite)->name)
                {
                    AXIS2_FREE(env->allocator, (*ite)->name);
                }
                
                (*ite)->name = NULL;
                (*ite)->type = (*ite)->element->children_type;

                if ((*ite)->index)
                    AXIS2_FREE(env->allocator, (*ite)->index);

                (*ite)->index = (int*)AXIS2_MALLOC(env->allocator, sizeof(int));
                *(int*)((*ite)->index) = index + 1;
                success = AXIS2_TRUE;
            }
            else
            {
                (*ite)->this = NULL;

                if ((*ite)->name)
                {
                    AXIS2_FREE(env->allocator, (*ite)->name);
                }
                
                (*ite)->name = NULL;
                (*ite)->type = (*ite)->element->children_type;
                
                if ((*ite)->index)
                    AXIS2_FREE(env->allocator, (*ite)->index);

                (*ite)->index = NULL;

                success = AXIS2_FALSE;
            }
        }
        break;
    case CHILDREN_TYPE_NONE:
    default:
        if ((*ite)->name)
            AXIS2_FREE(env->allocator, (*ite)->name);
        if ((*ite)->index)
            AXIS2_FREE(env->allocator, (*ite)->index);		

        (*ite)->name = NULL;
        (*ite)->this = NULL;
        (*ite)->type = (*ite)->element->children_type;
        (*ite)->index = NULL;
        
        success = AXIS2_FALSE;
        break;
    }

    return success;
}
