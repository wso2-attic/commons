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
#include <axutil_string.h>
#include <axiom.h>

#include "wsf_wsdl_defines.h"
#include "wsf_wsdl_data_template.h"


/** 
* creates a template structure */

WSF_WSDL_EXTERN wsf_wsdl_data_template_t* WSF_WSDL_CALL
wsdl_data_template_create(const axutil_env_t* env)
{
    wsf_wsdl_data_template_t* data = NULL;
    data = (wsf_wsdl_data_template_t*)AXIS2_MALLOC(env->allocator, sizeof(wsf_wsdl_data_template_t));   
    data->data_type = NULL;
    data->target_ns = NULL;
    data->type_ns = NULL;
    data->is_nullable = AXIS2_FALSE;
    data->is_simple = AXIS2_FALSE;
    data->min_occurs = 1;
    data->max_occurs = 1;
    data->data = NULL;
    return data;
}


/**
* adds a child template to a parent template
* Caller should clean allocated memory of parameters except for template data. 
* All strings are duplicated inside. */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_template_add_element(const axutil_env_t* env, wsf_wsdl_data_template_t* parent_element,
                               axis2_char_t* name, axis2_char_t* data_type, 
                               axis2_char_t* type_ns, axis2_char_t* target_ns,
                               axis2_bool_t is_simple, axis2_bool_t is_nullable,
                               int min_occurs, int max_occurs,
                               void* data, /* depending on is_simple flag, data will be either pointer to a template or to a string */
                               int index)		
{
    wsf_wsdl_data_template_t* element = NULL;

    if (!parent_element)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "template_add_element has a NULL parent_element");
        return;
    }

    if (!name)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "template_add_element has a NULL name");
        return;
    }

    if (!parent_element->data)	/* hash not created, create it */
    {
        parent_element->data = axutil_hash_make(env);
    }

    if (is_simple == AXIS2_TRUE)
    {
        element = wsdl_data_template_create(env);
        element->data = data? axutil_strdup(env, data) : NULL;
    }
    else
    {
        element = data;
    }

    element->data_type = data_type? axutil_strdup(env, data_type): NULL;
    element->type_ns = type_ns? axutil_strdup(env, type_ns): NULL;
    element->target_ns = target_ns? axutil_strdup(env, target_ns): NULL;
    element->is_nullable = is_nullable;
    element->is_simple = is_simple;
    element->max_occurs = max_occurs;
    element->min_occurs = min_occurs;
    element->index = index;
    
    axutil_hash_set((axutil_hash_t*)parent_element->data, name, AXIS2_HASH_KEY_STRING, element);  

    return;
}


/**
* free the template structure */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_template_free(const axutil_env_t* env, wsf_wsdl_data_template_t* data)
{
    if (data)
    {
        if (data->data_type)
        {
            AXIS2_FREE(env->allocator, data->data_type);
        }

        if (data->type_ns)
        {
            AXIS2_FREE(env->allocator, data->type_ns);
        }

        if (data->target_ns)
        {
            AXIS2_FREE(env->allocator, data->target_ns);
        }
       
        if (data->is_simple == AXIS2_TRUE)
        {
            AXIS2_FREE(env->allocator, data->data);
        }
        else
        {
            axutil_hash_t* ht = NULL;
            axutil_hash_index_t *hi;
            void *val;

            ht = data->data;
            for (hi = axutil_hash_first(ht, env); hi; hi = axutil_hash_next(env, hi)) 
            {
                wsf_wsdl_data_template_t* element = NULL;
                axutil_hash_this(hi, NULL, NULL, &val);
                element = (wsf_wsdl_data_template_t *)val;
                if (element)
                {
                    wsdl_data_template_free(env, element);
                }
            }
            
            axutil_hash_free((axutil_hash_t*)(data->data), env);
            data->data = NULL;
        }
    }
    return;
}


/** 
* add a simple template element under a parent element */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_template_add_simple_element(const axutil_env_t* env, wsf_wsdl_data_template_t* parent_element,
                                      axis2_char_t* name, axis2_char_t* data_type, 
                                      axis2_char_t* type_ns, axis2_char_t* target_ns,
                                      axis2_bool_t is_nullable,
                                      int min_occurs, int max_occurs, int index)
{
    wsdl_data_template_add_element(env, 
                                   parent_element, name, 
                                   data_type, type_ns, target_ns, 
                                   AXIS2_TRUE, is_nullable, min_occurs, max_occurs, NULL, index);
}


/**
* add a complex template element under a parent element */

WSF_WSDL_EXTERN void WSF_WSDL_CALL 
wsdl_data_template_add_complex_element(const axutil_env_t* env, wsf_wsdl_data_template_t* parent_element,
                                       axis2_char_t* name,  
                                       axis2_char_t* type_ns, axis2_char_t* target_ns,
                                       axis2_bool_t is_nullable,
                                       int min_occurs, int max_occurs, void* data, int index)
{
    wsdl_data_template_add_element(env, 
                                   parent_element, name, 
                                   NULL, type_ns, target_ns, 
                                   AXIS2_FALSE, is_nullable, min_occurs, max_occurs, data, index);
}
