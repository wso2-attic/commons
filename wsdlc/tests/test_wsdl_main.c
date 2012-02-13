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

#include "../include/wsf_wsdl_mode.h"

/* Please change the following directory according to your configurations */
#define WSFC_HOME_FOLDER "C:/dev/wsf_php/wso2-wsf-php-bin-1.2.1-win32/wsf_c"
//#define WSFC_HOME_FOLDER "C:/dev/wsfc/wso2-wsf-c-bin-1.2.0-win32"
#define WSDL_FOLDER	"C:/"
#define XSLT_FOLDER "C:/"

axis2_bool_t
request(const axutil_env_t* env, 
		axis2_char_t* wsdl_location, axis2_char_t* wsdl, 
		axis2_char_t* operation, wsf_wsdl_data_t* params, 
		axis2_char_t* type_map, 
		axis2_char_t* xslt_location, 
		wsf_wsdl_data_t** response)
{
	axis2_svc_client_t* svc_client = NULL;
	char* home_folder = WSFC_HOME_FOLDER;
	axis2_char_t* sample_wsdl = (axis2_char_t*)AXIS2_MALLOC(env->allocator, 256);
	sample_wsdl = strcpy(sample_wsdl, wsdl_location);
	sample_wsdl = strcat(sample_wsdl, "/");
	sample_wsdl = strcat(sample_wsdl, wsdl);

	svc_client = axis2_svc_client_create(env, home_folder);

	return wsf_wsdl_request(env, sample_wsdl, operation, params, type_map, xslt_location, svc_client, NULL, NULL, NULL, response);
}

void 
test_1(const axutil_env_t* env, axis2_char_t* wsdl_location, axis2_char_t* xslt_location)
{
	wsf_wsdl_data_t* params = NULL, *sub_params = NULL, *response = NULL;
	axis2_char_t* wsdl = "sample_wsdl_20.wsdl";
	axis2_char_t* operation = "GetPrice";
	axis2_char_t* type_map = "C:/type_map.xml"; 

	/* start : creating input parameters */
	params = wsdl_data_create_object(env);
	sub_params = wsdl_data_create_object(env);

	wsdl_data_add_simple_element(env, sub_params, "ProductType", "string", "Oil", "http://www.w3.org/2001/XMLSchema", "http://www.example.org/sample/");
	wsdl_data_add_simple_element(env, sub_params, "ItemNo", "int", "123", "http://www.w3.org/2001/XMLSchema", "http://www.example.org/sample/");

	wsdl_data_add_object(env, params, "GetPriceRequest", "whatever", sub_params, "http://www.example.org/sample/", NULL);
	/* end : creating input parameters */
	
	if (request(env, wsdl_location, wsdl, operation, params, type_map, xslt_location, &response) == AXIS2_TRUE)
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Test 1 succeeded!");
	else
		AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test] Test 1 failed!");
}

void 
test_2(const axutil_env_t* env, axis2_char_t* wsdl_location, axis2_char_t* xslt_location)
{
	wsf_wsdl_data_t* params = NULL, *sub_params = NULL, *response = NULL;
	axis2_char_t* wsdl = "sample_wsdl_11.wsdl";
	axis2_char_t* operation = "QueryPurchaseOrder";
	axis2_char_t* type_map = "C:/type_map.xml"; 

	/*<params wrapper-element="orderInfo" wrapper-element-ns="http://www.wso2.org/php">
            <param token="#in" minOccurs="1" maxOccurs="1" name="productName" type="string" type-namespace="http://www.w3.org/2001/XMLSchema" simple="yes"/>
            <param token="#in" minOccurs="1" maxOccurs="1" name="quantity" type="float" type-namespace="http://www.w3.org/2001/XMLSchema" simple="yes"/>
            <param token="#in" minOccurs="1" maxOccurs="1" name="date" type="dateTime" type-namespace="http://www.w3.org/2001/XMLSchema" simple="yes"/>
            <param token="#in" minOccurs="1" maxOccurs="1" name="orderNo" type="int" type-namespace="http://www.w3.org/2001/XMLSchema" simple="yes"/>
    </params>*/

	/* start : creating input parameters */
	params = wsdl_data_create_object(env);
	sub_params = wsdl_data_create_object(env);

	wsdl_data_add_simple_element(env, sub_params, "productName", "string", "Oil", "http://www.w3.org/2001/XMLSchema", "http://www.wso2.org/php");
	wsdl_data_add_simple_element(env, sub_params, "quantity", "float", "123", "http://www.w3.org/2001/XMLSchema", "http://www.wso2.org/php");
	wsdl_data_add_simple_element(env, sub_params, "date", "dateTime", "2008-02-14", "http://www.w3.org/2001/XMLSchema", "http://www.wso2.org/php");
	wsdl_data_add_simple_element(env, sub_params, "orderNo", "int", "123321", "http://www.w3.org/2001/XMLSchema", "http://www.wso2.org/php");
	
	wsdl_data_add_object(env, params, "orderInfo", "whatever", sub_params, "http://www.wso2.org/php", NULL);
	/* end : creating input parameters */
	
	if (request(env, wsdl_location, wsdl, operation, params, type_map, xslt_location, &response) == AXIS2_TRUE)
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Test 2 succeeded!");
	else
		AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test] Test 2 failed!");
}

void 
test_3(const axutil_env_t* env, axis2_char_t* wsdl_location, axis2_char_t* xslt_location)
{
}

void 
test_4(const axutil_env_t* env, axis2_char_t* wsdl_location, axis2_char_t* xslt_location)
{
}

void 
test_5(const axutil_env_t* env, axis2_char_t* wsdl_location, axis2_char_t* xslt_location)
{
}

void
test_wsdl_main_request(const axutil_env_t* env)
{
	axis2_char_t* wsdl_location = WSDL_FOLDER;
	axis2_char_t* xslt_location = XSLT_FOLDER;
	
	test_1(env, wsdl_location, xslt_location);
	test_2(env, wsdl_location, xslt_location);
	test_3(env, wsdl_location, xslt_location);
	test_4(env, wsdl_location, xslt_location);
	test_5(env, wsdl_location, xslt_location);
}










