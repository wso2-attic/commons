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


wsf_wsdl_data_template_t* 
create_template(const axutil_env_t* env);

wsf_wsdl_data_t*
create_params(const axutil_env_t* env);

axis2_bool_t
validate_data(const axutil_env_t* env, axiom_node_t* type_map, wsf_wsdl_data_template_t* data_template, wsf_wsdl_data_t* data, int validation_criteria);

axis2_char_t*
create_payload(const axutil_env_t* env, wsf_wsdl_data_t* data);

void
test_validate_data_and_payload_creation(const axutil_env_t* env)
{
	wsf_wsdl_data_t* data = NULL;
	wsf_wsdl_data_template_t* data_template = NULL;
	axis2_bool_t success;
	axis2_char_t* payload;
	
	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Starting test_wsdl_data...");

	data = create_params(env);

	if (data)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test]data is not NULL, again a good sign!");
	}

	data_template = create_template(env);

	if (data_template)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test]wow!, even template is not NULL!");
	}

	success = validate_data(env, NULL, data_template, data, VALIDATION_CRITERIA_REQUEST_MODE_TYPE);
	
	if (success)	
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test]validation result : SUCCESS!!!");
	}
	
	payload = create_payload(env, data);
	AXIS2_LOG_DEBUG(env->log, AXIS2_LOG_SI, "[wsf_wsdl_test]payload : %s", payload);

	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Ending test_wsdl_data...");
}

void 
create_axiom_from_string(const axutil_env_t* env, axis2_char_t* buffer, axiom_node_t** node)
{
	axiom_xml_reader_t* xml_reader = NULL;
    axiom_document_t* document = NULL;
    axiom_stax_builder_t* om_builder = NULL;
    
	xml_reader = axiom_xml_reader_create_for_memory(env, buffer, strlen(buffer), NULL, AXIS2_XML_PARSER_TYPE_BUFFER); /* encoding ?? */

	if (!xml_reader)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test]Failed to create wsdl_xml_reader\n");
        return;
    }

    om_builder = axiom_stax_builder_create(env, xml_reader);
    if (!om_builder)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test]Failed to create wsdl stax builder\n");
        axiom_xml_reader_free(xml_reader, env);
        return;
    }
    
    document = axiom_stax_builder_get_document(om_builder, env);
    if (!document)
    {
        AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test]Failed to get retrieve wsdl document\n");
        axiom_stax_builder_free(om_builder, env);
        return;
    }        
   
    *node = axiom_document_build_all(document, env);

	return;
}


void
test_validate_response(const axutil_env_t* env)
{
	axiom_node_t* node = NULL, *sig_node = NULL;
	wsf_wsdl_data_t* data = NULL;
	wsf_wsdl_data_template_t* templ = NULL;
	axis2_bool_t success = AXIS2_FALSE;

	axis2_char_t* response_buffer = "<Body><Product><ID>387488</ID><Price>112.20</Price><Qty>5</Qty><Details><Category>Auto</Category><Make>Honda</Make></Details></Product></Body>";
	axis2_char_t* sig_buffer =	"<params wrapper-element=\"Product\" wrapper-element-ns=\"http://tempuri.org/\"> \
									<param token=\"#in\" targetNamespace=\"http://schemas.datacontract.org/2004/07/System\" minOccurs=\"1\" maxOccurs=\"1\" name=\"ID\" type=\"int\" type-namespace=\"http://www.w3.org/2001/XMLSchema\" simple=\"yes\"/> \
									<param token=\"#in\" targetNamespace=\"http://schemas.datacontract.org/2004/07/System\" minOccurs=\"1\" maxOccurs=\"1\" name=\"Price\" type=\"float\" type-namespace=\"http://www.w3.org/2001/XMLSchema\" simple=\"yes\"/> \
									<param token=\"#in\" targetNamespace=\"http://schemas.datacontract.org/2004/07/System\" minOccurs=\"1\" maxOccurs=\"1\" name=\"Qty\" type=\"int\" type-namespace=\"http://www.w3.org/2001/XMLSchema\" simple=\"yes\"/> \
									<param token=\"#in\" targetNamespace=\"http://tempuri.org/\" minOccurs=\"0\" maxOccurs=\"1\" name=\"Details\" type=\"\" type-namespace=\"http://schemas.datacontract.org/2004/07/System\" simple=\"no\"> \
									  <param token=\"#in\" targetNamespace=\"http://schemas.datacontract.org/2004/07/System\" minOccurs=\"1\" maxOccurs=\"1\" name=\"Category\" type=\"string\" type-namespace=\"http://www.w3.org/2001/XMLSchema\" simple=\"yes\"/> \
									  <param token=\"#in\" targetNamespace=\"http://schemas.datacontract.org/2004/07/System\" minOccurs=\"1\" maxOccurs=\"1\" name=\"Make\" type=\"string\" type-namespace=\"http://www.w3.org/2001/XMLSchema\" simple=\"yes\"/> \
									</param> \
								  </params>";


	create_axiom_from_string(env, response_buffer, &node);

	create_axiom_from_string(env, sig_buffer, &sig_node);

	wsdl_data_util_axiom_to_template(env, sig_node, &templ);

	/* node is enclosed with "body" node */
	wsdl_data_util_axiom_to_data(env, node, &data);

	success = wsdl_data_util_validate_data(env, NULL, templ, data, VALIDATION_CRITERIA_RESPONSE_MODE);

	if (success)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Validation Test Case Successful !");
	}
	else
	{
		AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test] Validation Test Case Failed !");
	}

	return;
}

wsf_wsdl_data_template_t* 
create_template(const axutil_env_t* env)
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
validate_data(const axutil_env_t* env, axiom_node_t* type_map, wsf_wsdl_data_template_t* data_template, wsf_wsdl_data_t* data, int validation_criteria)
{
	return wsdl_data_util_validate_data(env, type_map, data_template, data, validation_criteria);
}

axis2_char_t*
create_payload(const axutil_env_t* env, wsf_wsdl_data_t* data)
{
	axiom_node_t* node = wsdl_data_util_create_payload(env, data, -1);
	return axiom_node_to_string(node, env);
}