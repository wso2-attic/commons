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

#include <string.h>
#include <stdio.h>

#ifdef _WIN32
#define WSF_WSDL_CALL __stdcall
#else
#define WSF_WSDL_CALL
#endif

#include <axis2_addr.h>
#include <axutil_error_default.h>
#include <axutil_log_default.h>
#include <axutil_uuid_gen.h>
#include <axiom_util.h>
#include <axiom.h>
#include <axiom_soap_envelope.h>
#include <rampart_context.h>
#include <neethi_options.h>
#include <axis2_policy_include.h>
#include <neethi_engine.h>
#include <axutil_env.h>
#include <axis2_svc_client.h>
#include <axiom_soap_body.h>

#include "../include/wsf_wsdl_defines.h"
#include "../include/wsf_wsdl_data.h"
#include "../include/wsf_wsdl_data_template.h"
#include "../include/wsf_wsdl_data_util.h"

#define LOG_PATH	"C:/"

axutil_env_t *
wsf_env_create(const axis2_char_t * path_tolog);

void
test_validate_data_and_payload_creation(const axutil_env_t* env);

void
test_validate_response(const axutil_env_t* env);

void
test_wsdl_main_request(const axutil_env_t* env);

void
test_wsdl_type_map(const axutil_env_t* env);

void
test_wsdl_data_iterator(const axutil_env_t* env);

void
test_serialization(const axutil_env_t* env);

int
main(int argc, char **argv) 
{
	axutil_env_t* env = NULL;
	
	env = wsf_env_create(LOG_PATH);

	// test_wsdl_main_request(env);

	test_validate_data_and_payload_creation(env);	

	test_validate_response(env);

	test_apply_xsl_transformation(env);

	test_wsdl_type_map(env);

	test_wsdl_data_iterator(env);

	test_serialization(env);

	

	return 0;
}

static void WSF_WSDL_CALL
wsf_free_wrapper_cli (
    axutil_allocator_t * allocator,
    void *ptr) 
{   
    if (ptr)
        free (ptr);
}   
    
/* }}} end efree wrapper */
    
/* {{{ malloc wrapper */ 
static void *WSF_WSDL_CALL
wsf_malloc_wrapper_cli ( 
    axutil_allocator_t * allocator,
    size_t size)
{   
    return malloc (size);
}
    
/* }}} */
/* {{{ realloc wrapper */
static void *WSF_WSDL_CALL
wsf_realloc_warpper_cli (
    axutil_allocator_t * allocator,
    void *ptr,
    size_t size) 
{   
    return realloc (ptr, size);
} 

axutil_env_t *
wsf_env_create (const axis2_char_t * path_tolog)
{
    axutil_allocator_t *allocator = NULL;
    axutil_error_t *error = NULL;
    axutil_log_t *log = NULL;
    axis2_char_t log_path[250];
    axutil_env_t *env = NULL;
    axutil_thread_pool_t *thread_pool = NULL;
    const axis2_char_t *LOG_NAME = "wsf_wsdl_test.log";
    allocator = malloc (sizeof (axutil_allocator_t));

    allocator->free_fn = wsf_free_wrapper_cli;
    allocator->malloc_fn = wsf_malloc_wrapper_cli;
    allocator->realloc = wsf_realloc_warpper_cli;

    error = axutil_error_create (allocator);
    if (path_tolog && (
            (0 == strcmp (path_tolog, "")) ||
            (0 == strcmp (path_tolog, ".")) ||
            (0 == strcmp (path_tolog, "./")))) {
        sprintf (log_path, "%s", LOG_NAME);
    } else {
        sprintf (log_path, "%s/%s", path_tolog, LOG_NAME);
    }

    thread_pool = axutil_thread_pool_init (allocator);
    log = axutil_log_create (allocator, NULL, log_path);
    env =
        axutil_env_create_with_error_log_thread_pool (allocator, error, log,
        thread_pool);

	return env;
}


