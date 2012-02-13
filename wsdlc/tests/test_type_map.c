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
#include "../include/wsf_wsdl_type_map.h"

void
test_wsdl_type_map(const axutil_env_t* env)
{
	axis2_bool_t success = AXIS2_FALSE;
	axiom_node_t* type_map = NULL;
	axis2_char_t* type = NULL;
	
	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Starting test_type_map...");

	success = wsdl_util_create_type_map(env, "C://type_map.xml", &type_map);
	
	if (success)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Successfully loaded the typemap");
	}
	else
	{
		AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test] Failed to load the typemap");
	}
	
	success = wsdl_util_forward_type_from_type_map(env, type_map, 
										       "string", type);

	if (success)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Successfully found the forward type from the typemap, type follows... START");
		AXIS2_LOG_DEBUG_MSG(env->log, type);
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] END");
	}
	else
	{
		AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test] Failed to find the forward type from the typemap");
	}

	success = wsdl_util_reverse_type_from_type_map(env, type_map, "string", &type);

	if (success)
	{
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Successfully found the reverse type from the typemap, type follows... START");
		AXIS2_LOG_DEBUG_MSG(env->log, type);
		AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] END");
	}
	else
	{
		AXIS2_LOG_ERROR_MSG(env->log, "[wsf_wsdl_test] Failed to find the reverse type from the typemap");
	}
	
	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Ending test_type_map...");
}