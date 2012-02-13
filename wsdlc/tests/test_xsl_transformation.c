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
#include <axiom_xml_reader.h>
#include <axiom_util.h>
#include <axis2_addr.h>
#include <axutil_hash.h>
#include <axis2_options.h>
#include <axis2_svc_client.h>
#include <axis2_http_transport.h>
#include <axiom_soap_envelope.h>
#include <rampart_context.h>
#include <neethi_options.h>
#include <axis2_policy_include.h>
#include <neethi_engine.h>

#include "../include/wsf_wsdl_defines.h"
#include "../include/wsf_wsdl_data.h"
#include "../include/wsf_wsdl_data_template.h"
#include "../include/wsf_wsdl_util.h"


axis2_bool_t
test_apply_xsl_transformation(const axutil_env_t* env)
{
	const axis2_char_t* file_name = "C:\\test.wsdl";
    axis2_bool_t version1_wsdl = AXIS2_TRUE;
    axiom_node_t* wsdl_axiom, *sig_axiom;
	
	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Starting test_xsl_transformation...");

    wsf_wsdl_util_apply_xslt(env, 
                             file_name, "C:/", AXIS2_TRUE, &version1_wsdl, &wsdl_axiom, &sig_axiom);

	AXIS2_LOG_DEBUG(env->log, 
                    AXIS2_LOG_SI, 
                    "[wsf_wsdl_test]sig_stream: \n %s", 
                    axiom_node_to_string(sig_axiom, env));

	AXIS2_LOG_DEBUG_MSG(env->log, "[wsf_wsdl_test] Ending test_xsl_transformation...");

	return AXIS2_TRUE; 
}

