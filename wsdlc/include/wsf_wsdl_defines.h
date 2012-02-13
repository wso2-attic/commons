/*
 * Copyright 2005-2008 WSO2, Inc. http://wso2.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"
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

#ifndef WS_WSDL_DEFINES_H
#define WS_WSDL_DEFINES_H

#define WS_WSDL_WSCLIENT        "wsclient"
#define WS_WSDL_ENDPOINT        "endpoint"
#define WS_WSDL_CLASSMAP        "classmap"
#define WS_WSDL_XSLT_LOCATION   "xslt_location"
#define WS_WSDL_ARG_COUNT       "arg_count"
#define WS_WSDL_ARG_ARRAY       "arg_array"

#define WS_WSDL_ENDPOINT_URI    "endpoint_uri"
#define WS_WSDL_BINDING_DETAILS "binding_details"
#define WS_WSDL_WSA             "wsa"
#define WS_WSDL_SOAP            "soap"
#define WS_WSDL_SOAP_VERSION    "soap_version"
#define WS_WSDL_REQ_PAYLOAD     "request_payload"
#define WS_WSDL_POLICY_NODE     "policy_node"
#define WS_WSDL_RES_SIG_NODEL   "response_sig_model"
#define WS_WSDL_ARGS            "args"

#define WS_WSDL_OP_POLICY       "operation_policy"
#define WS_WSDL_IN_POLICY       "input_policy"
#define WS_WSDL_OUT_POLICY      "output_policy"

#define WS_WSDL_ENV_UTF         "UTF-8"
#define WS_WSDL_ENV_1_0         "1.0"

#define WS_WSDL_DOM				"wsdl_dom"

#define WSF_WSDL_MODULE_ADDRESSING	"addressing"

#define XSLT_WSDL1_WSDL2		"wsdl11to20.xsl10.xsl"
#define XSLT_WSDL2_SIG			"wsdl2sig.xslt"

//#define WSF_WSDL				"wsdl"
//#define WSF_ENDPOINT			"endpoint"
//#define WSF_INVOKE_FUNCTION		"invoke_function"
//#define WSF_ARG_COUNT			"arg_count"
//#define WSF_ARG_ARRAY			"arg_array"
#define WSF_WSDL_SIGNATURE			"signature"
#define WSF_WSDL_METHOD				"method"
#define WSF_WSDL_INFERENCE			"inference"
#define WSF_WSDL_RPC					"rpc"
//#define WSF_ENDPOINT_URI		"endpoint_uri"
//#define WSF_BINDING_DETAILS		"binding_details"
//#define WSF_REQUEST_PAYLOAD		"request_payload"
//#define WSF_POLICY_NODE			"policy_node"
//
//#define WSF_DEFINITION			"definitions"
//#define WSF_DESCRIPTION			"description"
//#define WSF_OPERATIONS			"operations"
#define WSF_WSDL_OPERATION			"operation"
//#define WSF_ADDRESS				"address"
//#define WSF_NAME				"name"
//#define WSF_TYPE				"type"
#define WSF_WSDL_MINOCCURS			"minOccurs"
#define WSF_WSDL_MAXOCCURS			"maxOccurs"
#define WSF_WSDL_NILLABLE			"nillable"
//#define WSF_ID					"Id"
//#define WSF_SERVICE				"service"
//#define WSF_BINDING				"binding"
//#define WSF_WSDL_POLICY_REFERENCE	"PolicyReference"
#define WSF_WSDL_POLICY				"Policy"
//#define WSF_REF					"ref"
//#define WSF_URI					"URI"
//#define WSF_PARAMS				"params"
//#define WSF_PARAM				"param"
//#define WSF_WRAPPER_ELEMENT		"wrapper-element"
//#define WSF_WRAPPER_ELEMENT_NS	"wrapper-element-ns"
//#define WSF_TARGETNAMESPACE		"targetNamespace"
//#define WSF_BINDINDG_DETAILS	"binding-details"
//#define WSF_WSAWAACTION			"wsawaction"
//#define WSF_SOAPACTION			"soapaction"
//#define WSF_WSA					"wsa"
//#define WSF_SOAP				"soap"
//#define WSF_SOAP11				"SOAP11"
//#define WSF_SOAP12				"SOAP12"
#define WSF_WSDL_SOAP_1_1			"1.1"
#define WSF_WSDL_SOAP_1_2			"1.2"
//#define WSF_SOAP_VERSION		"soap_version"
//#define WSF_RETURNS				"returns"
//#define WSF_RESPONSE_SIG_MODEL	"response_sig_model"
//#define WSF_ENVELOPE			"envelope"
//#define WSF_BODY				"body"
//#define WSF_TYPE_NAMESPACE		"type-namespace"
//#define WSF_CLASSMAP			"classmap"
//#define WSF_NS					"ns"
//#define WSF_TYPE_NS				"type_ns"
//#define WSF_TNS					"tns"
//#define WSF_PORT				"port"
//#define WSF_LOCATION			"location"
//#define WSF_XSLT_LOCATION		"xslt_location"
//#define WSF_TYPES				"types"
//#define WSF_WSDL_DOM			"wsdl_dom"
#define WSF_WSDL_SIMPLE			"simple"
//#define WSF_WSDL_COMPLEX_TYPE	"complexType"
#define WSF_WSDL_REST           "rest"
//
#define WSF_WSDL2_NAMESPACE		"http://www.w3.org/ns/wsdl"
#define WSF_WSDL_NAMESPACE		"http://schemas.xmlsoap.org/wsdl/"
#define WSF_WSDL_POLICY_REFERENCE_NAMESPACE_URI	"http://schemas.xmlsoap.org/ws/2004/09/policy"
#define WSF_WSDL_POLICY_NAMESPACE_URI "http://www.w3.org/ns/ws-policy"
#define WSF_WSDL_POLICY_ID_NAMESPACE_URI \
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"

//#define WS_ACTIONS				"actions"
//#define WS_OPERATIONS			"operations"
//#define WS_OP_MEP				"opMEP"
//#define WS_OP_PARAMS			"opParams"
//#define WS_WSDL					"wsdl"
//#define WS_CLASSMAP				"classmap"
//#define WS_CLASSES				"classes"
//#define WS_CONS_ARGS			"args"

//#define WS_IN_ONLY				"IN_ONLY"
//#define WS_IN_OUT				"IN_OUT"
//
//#define WS_TO					"to"			
//
//#define WS_ACTION				"action"        
//#define WS_FROM					"from"
//#define WS_REPLY_TO				"replyTo"
//#define WS_FAULT_TO				"faultTo"
//
//#define WS_SOAP_ACTION			"SOAPAction"
//
//#define WS_DEFAULT_ATTACHEMENT_CONTENT_TYPE		"defaultAttachmentContentType"
//#define WS_ATTACHMENTS 			"attachments"
//#define WS_CID2CONTENT_TYPE 	"cid2contentType"
//#define WS_TIMEOUT              "timeout"
//
//#define WS_WILL_CONTINUE_SEQUENCE 	"willContinueSequence"
//#define WS_LAST_MESSAGE 		    "lastMessage"
//#define WS_RELIABLE			        "reliable"
//#define WS_SEQUENCE_EXPIRY_TIME 	"sequenceExpiryTime"
//#define WS_SEQUENCE_KEY      		"sequenceKey"
//
///** protocol */
//
#define WSF_WSDL_HTTP_METHOD 			"HTTPMethod"
//
///** SSL certificate */
#define WSF_WSDL_SERVER_CERT 			"CACert"
#define WSF_WSDL_CLIENT_CERT 			"clientCert"
#define WSF_WSDL_PASSPHRASE  			"passphrase"
//
///** wsdl mode */
//#define WS_BINDING_STYLE 		"bindingStyle"
//#define WS_WSDL          		"wsdl"
//#define WS_CACHE_WSDL           "cache_wsdl"
//
///** soap fault */
//#define WS_FAULT_REASON 		"Reason"
//#define WS_FAULT_ROLE   		"Role"
//#define WS_FAULT_TEXT   		"text"
//#define WS_FAULT_CODE   		"Code"
//#define WS_FAULT_CODE_NS        "codens"
//#define WS_FAULT_SUBCODE 		"Subcode"
//#define WS_FAULT_DETAIL 		"Detail"
//
///** header options */
//#define WS_INPUT_HEADERS        "inputHeaders"
//#define WS_OUTPUT_HEADERS       "outputHeaders"
//#define WS_HEADER_PREFIX        "prefix"
//#define WS_HEADER_NS    		"ns"
//#define WS_HEADER_LOCALNAME 	"name"
//#define WS_HEADER_DATA     		"data"
//#define WS_HEADER_ROLE     		"role"
//#define WS_HEADER_ACTOR         "actor"
//#define WS_HEADER_PREFIX        "prefix"
//#define WS_HEADER_MUST_UNDERSTAND 	"mustUnderstand"

#define WSF_WSDL_PROXY_HOST           "proxyHost"
#define WSF_WSDL_PROXY_PORT           "proxyPort"
///** end options */
//
//#define WS_MSG_PAYLOAD_STR 		"str"
//
///** soap header attributes */
//
//#define WS_SOAP_ROLE_NONE			        1
//#define WS_SOAP_ROLE_NEXT			        2
//#define WS_SOAP_ROLE_ULTIMATE_RECEIVER		3
//
//#define WS_SOAP_ROLE_NONE_URI				"http://www.w3.org/2003/05/soap-envelope/role/none"
//#define WS_SOAP_ROLE_NEXT_URI				"http://www.w3.org/2003/05/soap-envelope/role/next"
//#define WS_SOAP_ROLE_ULTIMATE_RECEIVER_URI "http://www.w3.org/2003/05/soap-envelope/role/next"
//
//#define WS_SOAP_1_1_NAMESPACE_URI	"http://schemas.xmlsoap.org/soap/envelope/"
//#define WS_SOAP_1_2_NAMESPACE_URI	"http://www.w3.org/2003/05/soap-envelope"
//
///** sandesha2 */
//#define WS_SANDESHA2_CLIENT_ACKS_TO			"Sandesha2AcksTo"
//#define WS_SANDESHA2_CLIENT_LAST_MESSAGE	"Sandesha2LastMessage"
//#define WS_SANDESHA2_CLIENT_OFFERED_SEQ_ID	"Sandesha2OfferedSequenceId"
//#define WS_SANDESHA2_CLIENT_DEBUG_MODE		"Sandesha2DebugMode"
//#define WS_SANDESHA2_CLIENT_SEQ_KEY			"Sandesha2SequenceKey"
//#define WS_SANDESHA2_CLIENT_MESSAGE_NUMBER	"Sandesha2MessageNumber"
//#define WS_SANDESHA2_CLIENT_RM_SPEC_VERSION "Sandesha2RMSpecVersion"
//#define WS_SANDESHA2_CLIENT_DUMMY_MESSAGE	"Sandesha2DummyMessage"
//
//#define WS_RM_VERSION_1_0_STR	"Spec_2005_02"
//#define WS_RM_VERSION_1_0		1
//#define WS_RM_VERSION_1_1_STR	"Spec_2006_08"
//#define WS_RM_VERSION_1_1		2
//
//#define WS_RM_RESPONSE_TIMEOUT			"responseTimeout"
//#define WS_RM_DEFAULT_RESPONSE_TIMEOUT	"5"

#define WSF_WSDL_DEFAULT_STRING  "dummy"
#define WSF_WSDL_UNKNOWN_TYPE "type-unknown"
//
//
///************ engaged module names *****************/
//#define WS_MODULE_ADDRESSING	"addressing"
//#define WS_MODULE_SECURITY		"rampart"
//#define WS_MODULE_RM			"sandesha2"
//
///** http status codes */
//#define WS_HTTP_OK						200
//#define WS_HTTP_INTERNAL_SERVER_ERROR	500
//#define WS_HTTP_ACCEPTED				202
///*************************************************/
//
//#define WS_SOAP_DOCUMENT	1
//#define WS_SOAP_RPC			2
//
//#define WS_SOAP_ENCODED		1
//#define WS_SOAP_LITERAL		2

#ifdef AXIS2_LOG_PROJECT_PREFIX
	#undef AXIS2_LOG_PROJECT_PREFIX
#endif

#define AXIS2_LOG_PROJECT_PREFIX "[wsdlc]"

/* for *_MSG macro safety define "" */
#ifndef AXIS2_LOG_PROJECT_PREFIX
    #define AXIS2_LOG_PROJECT_PREFIX ""
#endif 

#ifndef AXIS2_LOG_USER_MSG
	#define AXIS2_LOG_USER_MSG(log, msg) AXIS2_LOG_USER (log, AXIS2_LOG_SI, "%s %s", AXIS2_LOG_PROJECT_PREFIX, msg)
#endif 
#ifndef AXIS2_LOG_DEBUG_MSG
	#define AXIS2_LOG_DEBUG_MSG(log, msg) AXIS2_LOG_DEBUG (log, AXIS2_LOG_SI, "%s %s", AXIS2_LOG_PROJECT_PREFIX, msg)
#endif
#ifndef AXIS2_LOG_INFO_MSG
	#define AXIS2_LOG_INFO_MSG(log, msg) AXIS2_LOG_INFO (log, AXIS2_LOG_SI, "%s %s", AXIS2_LOG_PROJECT_PREFIX, msg)
#endif
#ifndef AXIS2_LOG_WARNING_MSG
	#define AXIS2_LOG_WARNING_MSG(log, msg) AXIS2_LOG_WARNING (log, AXIS2_LOG_SI, "%s %s", AXIS2_LOG_PROJECT_PREFIX, msg)
#endif
#ifndef AXIS2_LOG_ERROR_MSG
	#define AXIS2_LOG_ERROR_MSG(log, msg) AXIS2_LOG_ERROR (log, AXIS2_LOG_SI, "%s %s", AXIS2_LOG_PROJECT_PREFIX, msg)
#endif
#ifndef AXIS2_LOG_CRITICAL_MSG
	#define AXIS2_LOG_CRITICAL_MSG(log, msg) AXIS2_LOG_CRITICAL (log, AXIS2_LOG_SI, "%s %s", AXIS2_LOG_PROJECT_PREFIX, msg)
#endif
#ifndef AXIS2_LOG_TRACE_MSG
	#define AXIS2_LOG_TRACE_MSG(log, msg) AXIS2_LOG_TRACE (log, AXIS2_LOG_SI, "%s %s", AXIS2_LOG_PROJECT_PREFIX, msg)
#endif

#define WSDL_TYPEMAP_FORWARD "forward"
#define WSDL_TYPEMAP_REVERSE "reverse"

#define VALIDATION_CRITERIA_REQUEST_MODE_TYPE_WITH_NS	 0
#define VALIDATION_CRITERIA_REQUEST_MODE_TYPE			 1
#define VALIDATION_CRITERIA_RESPONSE_MODE				 2
#define VALIDATION_CRITERIA_RESPONSE_MODE_WITH_NS		 3

#define WSF_ANYTYPE		"anyType"
#define WSF_UNBOUNDED	"unbounded"
#define WSF_YES			"yes"
#define WSF_NO			"no"

#define CHILDREN_TYPE_NONE              0
#define CHILDREN_TYPE_ATTRIBUTES        1
#define CHILDREN_TYPE_ARRAY_ELEMENTS    2

//#define WSF_WSDL_SERVER_CERT	"SERVER_CERT"
#define WSF_WSDL_KEY_FILE		"KEY_FILE"
#define WSF_WSDL_SSL_PASSPHRASE	"SSL_PASSPHRASE"

#define WSDL_BINDING_STYLE_DOC_LIT_B		0
#define WSDL_BINDING_STYLE_DOC_LIT_W		1
#define WSDL_BINDING_STYLE_RPC_ENC			2

#define WSDL_DEBUG_MODE		1

#define WSDL_SCHEMA_NULL_STRING		"NULL"
#define WSDL_NOT_APPLICABLE_STRING	"N/A"


/*\*****************************************************************************************************\*/
/*\*****************************************************************************************************\*/
/*\										After code review												\*/
/*\*****************************************************************************************************\*/
/*\*****************************************************************************************************\*/

/* HK stands for HASHKEY */

/* user script client/service options hash keys*/
#define WSF_WSDL_HK_POLICY_STRING	"policy_xml"
#define WSF_WSDL_HK_POLICY_HASH		"policy_hash"
#define WSF_WSDL_HK_SECURITY_TOKEN	"security_token"

/* policy options hash keys */
#define WSF_WSDL_HK_TIMESTAMP		"time_stamp"
#define WSF_WSDL_HK_USERNAME_TOKEN  "username_token"
#define WSF_WSDL_HK_ENCRYPTION		"encryption"
#define WSF_WSDL_HK_ALGORITHM		"algorithm"
#define WSF_WSDL_HK_SIGNING			"signing"
#define WSF_WSDL_HK_TOKEN_REFERENCE	"token_reference"
#define WSF_WSDL_HK_ENCRYPT_SIGNATURE "encrypt_signature"
#define WSF_WSDL_HK_PROTECTION_ORDER  "protection_order"
	
#define WSF_WSDL_PROTECTION_ORDER_ENCRYPT_BEFORE "encrypt_before"
#define WSF_WSDL_PROTECTION_ORDER_SIGN_BEFORE	 "sign_before"

#define WSF_WSDL_ISSUER_SERIAL		"issuer_serial"
#define WSF_WSDL_KEYIDENTIFIER		"key_identifier"
#define WSF_WSDL_EMBEDDEDTOKEN		"embedded_token"
#define WSF_WSDL_THUMBPRINT			"thumb_print"

#define WSF_WSDL_RP_REQUIRE_ISSUER_SERIAL_REFERENCE  "require_issuer_serial"
#define WSF_WSDL_RP_REQUIRE_KEY_IDENTIFIER_REFERENCE "require_key_identifier_reference"
#define WSF_WSDL_RP_REQUIRE_EMBEDDED_TOKEN_REFERENCE "require_embedded_token_reference"
#define WSF_WSDL_RP_REQUIRE_THUMBPRINT_REFERENCE	 "require_thumbprint_reference"

/* security token hash keys */
#define WSF_WSDL_HK_PASSWORD_CALL_BACK	"password_callback"
#define WSF_WSDL_HK_TTL					"ttl"
#define WSF_WSDL_HK_PASSWORD_TYPE	    "password_type"
#define WSF_WSDL_HK_PASSWORD			"password"
#define WSF_WSDL_HK_USER				"user"
#define WSF_WSDL_HK_RECEIVER_CERTIFICATE "receiver_certificate"
#define WSF_WSDL_HK_CERTIFICATE			"certificate"
#define WSF_WSDL_HK_PRIVATE_KEY			"private_key"

#define WSF_WSDL_RAMPART_CONFIGURATION "RampartConfiguration"
#define WSF_WSDL_MODULE_SECURITY	   "rampart"

#define WSF_WSDL_ENCODING_UTF_8			"utf-8"

#define WSF_WSDL_OP_POLICY		"operation_policy"
#define WSF_WSDL_IN_POLICY		"input_policy"

#define WSF_WSDL_DEFINITION		"definitions"
#define WSF_WSDL_DESCRIPTION	"description"
#define WSF_WSDL_OPERATIONS		"operations"
#define WSF_WSDL_OPERATION		"operation"
#define WSF_WSDL_ADDRESS		"address"
#define WSF_WSDL_NAME			"name"
#define WSF_WSDL_TYPE			"type"
#define WSF_WSDL_MAX_OCCURS		"maxOccurs"
#define WSF_WSDL_MIN_OCCURS		"minOccurs"
#define WSF_WSDL_TYPE_REP		"type_rep"
#define WSF_WSDL_ID				"Id"
#define WSF_WSDL_SERVICE		"service"
#define WSF_WSDL_CLIENT			"client"
#define WSF_WSDL_BINDING		"binding"
#define WSF_WSDL_POLICY_REFERENCE "PolicyReference"
#define WSF_WSDL_POLICY			"Policy"
#define WSF_WSDL_REF			"ref"
#define WSF_WSDL_URI			"URI"
#define WSF_WSDL_PARAMS			"params"
#define WSF_WSDL_PARAM			"param"
#define WSF_WSDL_WRAPPER_ELEMENT	"wrapper-element"
#define WSF_WSDL_WRAPPER_ELEMENT_NS	"wrapper-element-ns"
#define WSF_WSDL_TARGETNAMESPACE	"targetNamespace"
#define WSF_WSDL_BINDINDG_DETAILS	"binding-details"
#define WSF_WSDL_WSAWAACTION	"wsawaction"
#define WSF_WSDL_SOAPACTION		"soapaction"
#define WSF_WSDL_HTTPMETHOD		"httpmethod"
#define WSF_WSDL_USE_SOAP		"usesoap"
#define WSF_WSDL_WSA			"wsa"
#define WSF_WSDL_SOAP			"soap"
#define WSF_WSDL_SOAP11			"SOAP11"
#define WSF_WSDL_SOAP12			"SOAP12"
#define WSF_WSDL_SOAP_VERSION	"soap_version"
#define WSF_WSDL_RETURNS		"returns"
#define WSF_WSDL_RESPONSE_SIG_MODEL "response_sig_model"
#define WSF_WSDL_ENVELOPE		"envelope"
#define WSF_WSDL_BODY			"body"
#define WSF_WSDL_TYPE_NAMESPACE "type-namespace"
#define WSF_WSDL_CLASSMAP		"classmap"
#define WSF_WSDL_NS				"ns"
#define WSF_WSDL_TYPE_NS		"type_ns"
#define WSF_WSDL_TNS			"tns"
#define WSF_WSDL_PORT			"port"
#define WSF_WSDL_LOCATION		"location"
#define WSF_WSDL_XSLT_LOCATION	"xslt_location"
#define WSF_WSDL_TYPES			"types"
#define WSF_WSDL_SCHEMA			"schema"
#define WSF_WSDL_ELEMENT		"element"
#define WSF_WSDL_LIST			"list"
#define WSF_WSDL_UNION			"union"
#define WSF_WSDL_WSDL_DOM		"wsdl_dom"
#define WSF_WSDL_WSDL_SIMPLE	"simple"
#define WSF_WSDL_WSDL_COMPLEX_TYPE	"complexType"
#define WSF_WSDL_WSDL_PORT_TYPE "portType"
#define WSF_WSDL_WSDL_MESSAGE	"message"
#define WSF_WSDL_WSDL_INPUT		"input"
#define WSF_WSDL_WSDL_OUTPUT	"output"
#define WSF_WSDL_WSDL_PART		"part"
#define WSF_WSDL_ENDPOINT		"endpoint"

#define WSF_WSDL_V1_WSDL_NAMESPACE "http://www.w3.org/ns/wsdl"
#define WSF_WSDL_V2_WSDL_NAMESPACE "http://schemas.xmlsoap.org/wsdl/"
#define WSF_WSDL_POLICY_REFERENCE_NAMESPACE_URI "http://schemas.xmlsoap.org/ws/2004/09/policy"
#define WSF_WSDL_POLICY_NAMESPACE_URI "http://www.w3.org/ns/ws-policy"
#define WSF_WSDL_POLICY_ID_NAMESPACE_URI "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
                                          

// constants involves in sig model data 

#define WSF_WSDL_SIG_META_DATA	"__sig_meta_data" // doesn't support elements with this name in schemas

#define WSF_WSDL_SIG_CHILDS		"childs" // doesn't support elements with this name in schemas
#define WSF_WSDL_HAS_SIG_CHILDS "has_childs"

#define WSF_WSDL_CLASSMAP_NAME	"classmap_name"
#define WSF_WSDL_XSD_NILLABLE	"nillable"
#define WSF_WSDL_CONTENT_MODEL	"contentModel"
#define WSF_WSDL_INNER_CONTENT	"inner-content"
#define WSF_WSDL_INHERITED_CONTENT "inherited-content"
#define WSF_WSDL_EXTENSION		"extension"
#define WSF_WSDL_ATTRIBUTE		"attribute"

#define WSF_WSDL_STARTING_NS_PREFIX "ns1"
#define WSF_WSDL_SOAP_BODY			"Body"

#define WSF_WSDL_WSDL_SEQUENCE	"sequence"
#define WSF_WSDL_WSDL_CHOICE	"choice"
#define WSF_WSDL_WSDL_ALL		"all"

#define WSF_WSDL_SIMPLE_CONTENT_VALUE	"value"
#define WSF_WSDL_SIMPLE_CONTENT			"simpleContent"

#define WSF_WSDL_XSD_ANYTYPE	"anyType"
#define WSF_WSDL_VALUE			"value"

// log level definition

#define WSF_WSDL_LOG_CRITICAL	0
#define WSF_WSDL_LOG_ERROR		1
#define WSF_WSDL_LOG_WARNING	2
#define WSF_WSDL_LOG_INFO		3
#define WSF_WSDL_LOG_DEBUG		4
#define WSF_WSDL_LOG_USER		5
#define WSF_WSDL_LOG_TRACE		6

#ifdef WIN32
#define WSF_WSDL_CALL	__stdcall
#else
#define WSF_WSDL_CALL
#endif

#define WSF_WSDL_EXTERN AXIS2_EXTERN

#define WSF_WSDL_XSLT_WSDL_2_TO_SIG "wsdl2sig.xslt"
#define WSF_WSDL_XSLT_WSDL_1_TO_2   "wsdl11to20.xsl10.xsl"
#define WSF_WSDL_XSLT_WSDL_INTERMEDIATE "intermediate.wsdl"
#define WSF_WSDL_XSLT_SIG_FINAL "final.sig"

#define WSF_WSDL_XSLT_LOCATION_POSTFIX "/wsdlc/xslt/"
#define WSF_WSDL_TYPE_MAP_POSTFIX "/wsdlc/conf/type_map.xml"
#endif /* WS_WSDL_DEFINES_H */ 

