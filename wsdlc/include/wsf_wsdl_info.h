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

#ifndef WSF_WSDL_INFO_H
#define WSF_WSDL_INFO_H

typedef struct wsf_wsdl_operation_info
{
	axis2_bool_t safe;
	axis2_char_t* pattern;
	axis2_char_t* soap_action;
	wsf_wsdl_data_template_t* request_template;
	wsf_wsdl_data_template_t* response_template;
} wsf_wsdl_operation_info_t;


#endif /* WSF_WSDL_INFO_H */


