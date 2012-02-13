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
#include <axutil_utils_defines.h>
#include <stdlib.h>
#include <axiom.h>
#include <axiom_util.h>
#include <axutil_hash.h>
#include <axutil_array_list.h>
#include <axutil_string.h>
#include <axutil_linked_list.h>

#include "../include/wsf_wsdl_defines.h"
#include "../include/wsf_wsdl_data.h"
#include "../include/wsf_wsdl_data_template.h"
#include "../include/wsf_wsdl_data_util.h"



wsf_wsdl_data_t*
create_params_to_serialize(const axutil_env_t* env);

wsf_wsdl_data_template_t* 
create_template_to_serialize(const axutil_env_t* env);

void
test_serialization(const axutil_env_t* env)
{
	wsf_wsdl_data_t* data = NULL;
	wsf_wsdl_data_template_t* templ = NULL;
	axis2_bool_t success;
	axis2_char_t* buffer;
	
	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Starting test_serialization...");

	data = create_params_to_serialize(env);

	if (data)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Data is not NULL, again a good sign!");
	}

	wsdl_data_util_serialize_data(env, data, &buffer);

	AXIS2_LOG_DEBUG_MSG(env->log, buffer);

	AXIS2_FREE(env->allocator, buffer);

	templ = create_template_to_serialize(env);

	if (templ)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Template is not NULL, again a good sign!");
	}

	wsdl_data_util_serialize_template(env, templ, &buffer);

	AXIS2_LOG_DEBUG_MSG(env->log, buffer);

	if (buffer)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] test serialization is successful, check the listing.. ");
	}
	else 
	{
		AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test] test serialization failed");
	}

	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Ending test_serialization...");

	AXIS2_FREE(env->allocator, buffer);
}

wsf_wsdl_data_t*
create_params_to_serialize(const axutil_env_t* env)
{
	wsf_wsdl_data_t* book = NULL, *author = NULL, *link = NULL, *link1 = NULL, *link2 = NULL, *father = NULL;

	book = wsdl_data_create_object(env);
	wsdl_data_add_simple_element(env, book, "title", "string", "Understanding WSDL", "ns1", NULL);
	
	father = wsdl_data_create_object(env);
	wsdl_data_add_simple_element(env, father, "name", "string", "Buddhika Sr.", "ns2", NULL);
	wsdl_data_add_simple_element(env, father, "url", "string", "http://buddhika.blogspot.com", "ns2", NULL);

	author = wsdl_data_create_object(env);
	wsdl_data_add_simple_element(env, author, "name", "string", "Buddhika", "ns2", NULL);
	wsdl_data_add_simple_element(env, author, "url", "string", "http://buddhika.blogspot.com", "ns2", NULL);
	wsdl_data_add_complex_element(env, author, "father", "father's ns", father, "ns1", NULL, CHILDREN_TYPE_ATTRIBUTES);

	wsdl_data_add_complex_element(env, book, "author", "data author's ns", author, "ns1", NULL, CHILDREN_TYPE_ATTRIBUTES);

	link1 = wsdl_data_create_object(env);
	wsdl_data_add_simple_element(env, link1, "type", "string", "weblink", "ns2", NULL);
	wsdl_data_add_simple_element(env, link1, "href", "string", "http://www.google.com", "ns2", NULL);
	
	link2 = wsdl_data_create_object(env);
	wsdl_data_add_simple_element(env, link2, "type", "string", "weblink", "ns2", NULL);
	wsdl_data_add_simple_element(env, link2, "href", "string", "http://www.yahoo.com", "ns2", NULL);

	link = wsdl_data_create_array(env);
	wsdl_data_add_complex_element(env, link, NULL, "link's link1", link1, "ns2", NULL, CHILDREN_TYPE_ATTRIBUTES);
	wsdl_data_add_complex_element(env, link, NULL, "link's link2", link2, "ns2", NULL, CHILDREN_TYPE_ATTRIBUTES);

	wsdl_data_add_complex_element(env, book, "link", "book's link", link, "ns1", NULL, CHILDREN_TYPE_ARRAY_ELEMENTS);

	wsdl_data_add_simple_element(env, book, "generator", "string", "Wordpress Book Plugin", "ns1", NULL);

	return book;
}

wsf_wsdl_data_template_t* 
create_template_to_serialize(const axutil_env_t* env)
{
	wsf_wsdl_data_template_t* book = NULL, *author = NULL, *link = NULL, *father = NULL;

	book = wsdl_data_template_create(env);

	wsdl_data_template_add_simple_element(env, book, "title", "string", "ns1", "whatever", AXIS2_FALSE, 1, 1, 0);
	

	father = wsdl_data_template_create(env);
	wsdl_data_template_add_simple_element(env, father, "name", "string", "ns2", "template name ns", AXIS2_FALSE, 0, 1, 0);
	wsdl_data_template_add_simple_element(env, father, "url", "string", "ns2", "template url ns", AXIS2_FALSE, 0, 1, 1);

	author = wsdl_data_template_create(env);
	wsdl_data_template_add_simple_element(env, author, "name", "string", "ns2", "template name ns", AXIS2_FALSE, 0, 1, 0);
	wsdl_data_template_add_simple_element(env, author, "url", "string", "ns2", "template url ns", AXIS2_FALSE, 0, 1, 1);
	wsdl_data_template_add_complex_element(env, author, "father", "ns1", "template father ns", AXIS2_FALSE, 1, 1, father, 2);

	wsdl_data_template_add_complex_element(env, book, "author", "ns1", "template author ns", AXIS2_FALSE, 1, 1, author, 1);

	link = wsdl_data_template_create(env);
	wsdl_data_template_add_simple_element(env, link, "type", "string", "ns2", "link's type", AXIS2_FALSE, 0, 1, 0);
	wsdl_data_template_add_simple_element(env, link, "href", "string", "ns2", "link's href", AXIS2_FALSE, 0, 1, 1);
	wsdl_data_template_add_simple_element(env, link, "email", "string", "ns2", "link's email", AXIS2_FALSE, 0, 1, 2);

	wsdl_data_template_add_complex_element(env, book, "link", "ns1", "link", AXIS2_FALSE, 0, -1, link, 2);

	wsdl_data_template_add_simple_element(env, book, "generator", "string", "ns1", "whatever", AXIS2_FALSE, 0, 1, 3);

	wsdl_data_template_add_simple_element(env, book, "terminator", "string", "ns1", "whatever", AXIS2_TRUE, 0, 1, 4);

	return book;
}