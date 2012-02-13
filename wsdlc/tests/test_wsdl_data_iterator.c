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
create_data(const axutil_env_t* env);

void
test_wsdl_data_iterator(const axutil_env_t* env)
{
	wsf_wsdl_data_t* data = NULL;
	wsf_wsdl_data_iterator_t* iterator = NULL;
	
	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Starting test_wsdl_data_iterator...");
	
	data = create_data(env);
	iterator = wsdl_data_iterator_create(env, data);

	/* reset the iterator */
	wsdl_data_iterator_first(env, &iterator); 

	do
	{
		wsf_wsdl_data_t* current_data = NULL;
		if (iterator)
		{
			switch (iterator->type)
			{
			case CHILDREN_TYPE_ATTRIBUTES:
				iterator->name;
				current_data = iterator->this;
				AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] found an object attribute !");
				break;
			case CHILDREN_TYPE_ARRAY_ELEMENTS:
				current_data = iterator->this;
				AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] found an array element !");
				break;
			default:
				break;
			}
		}
	} while (wsdl_data_iterator_next(env, &iterator));

	wsdl_data_iterator_free(env, iterator);

	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Ending test_wsdl_data_iterator...");

	return;
}

wsf_wsdl_data_t*
create_data(const axutil_env_t* env)
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
