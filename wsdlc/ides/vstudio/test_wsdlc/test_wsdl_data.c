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

#include "../include/wsf_wsdl_data.h"
#include "../include/wsf_wsdl_data_template.h"
#include "../include/wsf_wsdl_data_util.h"

wsf_wsdl_data_template_t* 
create_template(const axutil_env_t* env);

wsf_wsdl_data_t*
create_params(const axutil_env_t* env);

axis2_bool_t
validate_data(const axutil_env_t* env, wsf_wsdl_data_template_t* data_template, wsf_wsdl_data_t* data);

axis2_char_t*
create_payload(const axutil_env_t* env, wsf_wsdl_data_t* data);

void
test_validate_data_and_payload_creation(const axutil_env_t* env)
{
	wsf_wsdl_data_t* data = NULL;
	wsf_wsdl_data_template_t* data_template = NULL;
	axis2_bool_t success;
	axis2_char_t* payload;
	
	if (env)
		printf("env is not NULL, which is good!\n");

	data = create_params(env);

	if (data)
		printf("data is not NULL, again a good sign!\n");

	data_template = create_template(env);

	if (data_template)
		printf("wow!, even template is not NULL! \n");

	success = validate_data(env, data_template, data);
	
	if (success)	
		printf("validation result : SUCCESS!!! \n");
	
	payload = create_payload(env, data);
	printf("payload : %s", payload);
}

wsf_wsdl_data_template_t* 
create_template(const axutil_env_t* env)
{
	wsf_wsdl_data_template_t* book = NULL, *author = NULL, *link = NULL, *father = NULL;

	book = wsdl_data_template_create(env);

	wsdl_data_template_add_simple_element(env, book, "title", "string", "ns1", "whatever", AXIS2_FALSE, 1, 1);
	

	father = wsdl_data_template_create(env);
	wsdl_data_template_add_simple_element(env, father, "name", "string", "ns2", "template name ns", AXIS2_FALSE, 0, 1);
	wsdl_data_template_add_simple_element(env, father, "url", "string", "ns2", "template url ns", AXIS2_FALSE, 0, 1);

	author = wsdl_data_template_create(env);
	wsdl_data_template_add_simple_element(env, author, "name", "string", "ns2", "template name ns", AXIS2_FALSE, 0, 1);
	wsdl_data_template_add_simple_element(env, author, "url", "string", "ns2", "template url ns", AXIS2_FALSE, 0, 1);
	wsdl_data_template_add_complex_element(env, author, "father", "ns1", "template father ns", AXIS2_FALSE, 1, 1, father);

	wsdl_data_template_add_complex_element(env, book, "author", "ns1", "template author ns", AXIS2_FALSE, 1, 1, author);

	link = wsdl_data_template_create(env);
	wsdl_data_template_add_simple_element(env, link, "type", "string", "ns2", "link's type", AXIS2_FALSE, 0, 1);
	wsdl_data_template_add_simple_element(env, link, "href", "string", "ns2", "link's href", AXIS2_FALSE, 0, 1);
	wsdl_data_template_add_simple_element(env, link, "email", "string", "ns2", "link's email", AXIS2_FALSE, 0, 1);

	wsdl_data_template_add_complex_element(env, book, "link", "ns1", "link", AXIS2_FALSE, 0, -1, link);

	wsdl_data_template_add_simple_element(env, book, "generator", "string", "ns1", "whatever", AXIS2_FALSE, 0, 1);

	wsdl_data_template_add_simple_element(env, book, "terminator", "string", "ns1", "whatever", AXIS2_TRUE, 0, 1);

	return book;
}

wsf_wsdl_data_t*
create_params(const axutil_env_t* env)
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

axis2_bool_t
validate_data(const axutil_env_t* env, wsf_wsdl_data_template_t* data_template, wsf_wsdl_data_t* data)
{
	return wsdl_data_util_validate_data(env, data_template, data);
}

axis2_char_t*
create_payload(const axutil_env_t* env, wsf_wsdl_data_t* data)
{
	axiom_node_t* node = wsdl_data_util_create_payload(env, data);
	return axiom_node_to_string(node, env);
}