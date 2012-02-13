/*******************************************************************************
 * Copyright (c) 2002-2003 IBM Corporation, Parasoft and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   Parasoft - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core;

/**
 * WS-I constants.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 * @author Jim Clune
 * @author Graham Turrell (gturrell@uk.ibm.com)
 */
public interface WSIConstants {

	/**
	 * XML declaration statement.
	 */
	public static final String XML_DECL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	/**
	 * XML namespace URI.
	 */
	public static final String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";
	public static final String NS_URI_XML =  "http://www.w3.org/XML/1998/namespace";
	/**
	 * XML schema namespace URI.
	 */
	public static final String NS_URI_XSD = "http://www.w3.org/2001/XMLSchema";

	/**
	 * XML schema location.
	 */
	public static final String XSD_SCHEMA_LOCATION = "http://www.w3.org/2001/XMLSchema.xsd";

	/**
	 * XML schema namespace name.
	 */
	public static final String NS_NAME_XSD = "xsd";

	/**
	 * XML schema instance namespace URI.
	 */
	public static final String NS_URI_XSI = "http://www.w3.org/2001/XMLSchema-instance";

	/**
	 * XML schema namespace name.
	 */
	public static final String NS_NAME_XSI = "xsi";


	/**
	 * WS-I message log namespace URI.
	 */
	public static final String NS_URI_WSI_LOG = "http://www.ws-i.org/testing/2004/07/log/";

	public static final String NS_NAME_WSI_LOG = "wsi-log";

	/**
	 * WS-I conformance report namespace URI.
	 */
	public static final String NS_URI_WSI_REPORT = "http://www.ws-i.org/testing/2004/07/report/";
	public static final String NS_NAME_WSI_REPORT = "wsi-report";

	/**
	 * WS-I profile test assertions namespace URI.
	 */
	public static final String NS_URI_WSI_ASSERTIONS_2003 = "http://www.ws-i.org/testing/2003/03/assertions/";
	public static final String NS_URI_WSI_ASSERTIONS = "http://www.ws-i.org/testing/2004/07/assertions/";

	public static final String NS_NAME_WSI_ASSERTIONS = "wsi-assertions";

	/**
	 * WS-I monitor configuration file namesapce URI.
	 */
	public static final String NS_URI_WSI_MONITOR_CONFIG_2003 = "http://www.ws-i.org/testing/2003/03/monitorConfig/";
	public static final String NS_URI_WSI_MONITOR_CONFIG = "http://www.ws-i.org/testing/2004/07/monitorConfig/";
	

	public static final String NS_NAME_WSI_MONITOR_CONFIG = "wsi-monConfig";

	/**
	 * WS-I analyzer configuration file namesapce URI.
	 */
	public static final String NS_URI_WSI_ANALYZER_CONFIG_2003 = "http://www.ws-i.org/testing/2003/03/analyzerConfig/";
	public static final String NS_URI_WSI_ANALYZER_CONFIG = "http://www.ws-i.org/testing/2004/07/analyzerConfig/";
	
	public static final String NS_NAME_WSI_ANALYZER_CONFIG = "wsi-analyzerConfig";

	/**
	 * WS-I common namesapce URI.
	 */
	public static final String NS_URI_WSI_COMMON_2003 = "http://www.ws-i.org/testing/2003/03/common/";
	public static final String NS_URI_WSI_COMMON = "http://www.ws-i.org/testing/2004/07/common/";
	public static final String NS_NAME_WSI_COMMON = "wsi-common";

	/**
	 * SOAP V1.1 namespace.
	 */
	public final static String NS_URI_SOAP = "http://schemas.xmlsoap.org/soap/envelope/";

	/**
	 * SOAP/1.1 encoding.
	 */
	public final static String NS_URI_SOAP_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";

	public static final String NS_URI_SOAP_NEXT_ACTOR = "http://schemas.xmlsoap.org/soap/actor/next" ;
	/**
	 * HTTP transport.
	 */
	public final static String NS_URI_SOAP_HTTP = "http://schemas.xmlsoap.org/soap/http";

	/**
	 * WSDL V1.1 namespace.
	 */
	public final static String NS_URI_WSDL = "http://schemas.xmlsoap.org/wsdl/";

	/**
	 * WSDL SOAP binding namespace.
	 */
	public final static String NS_URI_WSDL_SOAP = "http://schemas.xmlsoap.org/wsdl/soap/";
	public final static String NS_URI_WSDL_SOAP12 = "http://schemas.xmlsoap.org/wsdl/soap12/";
	public final static String NS_NAME_WSDL_SOAP = "soap";

	/**
	 * WSDL MIME binding namespace.
	 */
	public final static String NS_NAME_WSDL_MIME = "http://schemas.xmlsoap.org/wsdl/mime/";

	/**
	 * WSDL HTTP GET & POST binding namespace.
	 */
	public final static String NS_NAME_WSDL_HTTP = "http://schemas.xmlsoap.org/wsdl/http/";

	/**
	 * WSDL DIME binding namespace.
	 */
	public final static String NS_NAME_WSDL_DIME = "http://schemas.xmlsoap.org/ws/2002/04/dime/wsdl/";

	/**
	 * Conformance Claim namespace.
	 */
	public final static String NS_URI_CLAIM = "http://ws-i.org/schemas/conformanceClaim/";

	/**
	 * UDDI V2 namespace.
	 */
	public final static String NS_URI_UDDI_V2 = "urn:uddi-org:api_v2";

	/**
	 * The XML Schema for the type used to refer to attachments from the SOAP
	 * envelope.
	 */
	public final static String NS_URI_SWA_REF = "http://ws-i.org/profiles/basic/1.1/xsd";

	public final static String SCHEMA_TYPE_SWA_REF = "swaRef";

	/**
	 * Element names.
	 */
	public static final String ELEM_ADD_STYLE_SHEET = "addStyleSheet";

	public static final String ELEM_ADDITIONAL_ENTRY_TYPE_LIST = "additionalEntryTypeList";

	public static final String ELEM_ANALYZER_FAILURE = "analyzerFailure";

	public static final String ELEM_ARTIFACT = "artifact";

	public static final String ELEM_ARTIFACT_REFERENCE = "artifactReference";

	public static final String ELEM_ASSERTION_COVERAGE = "assertionCoverage";

	public static final String ELEM_ASSERTION_DESCRIPTION = "assertionDescription";

	public static final String ELEM_ASSERTION_RESULT = "assertionResult";

	public static final String ELEM_ASSERTION_RESULT_SUMMARY = "assertionResultSummary";

	public static final String ELEM_ASSERTION_RESULTS = "assertionResults";

	public static final String ELEM_CLEANUP_TIMEOUT_SECONDS = "cleanupTimeoutSeconds";

	public static final String ELEM_CONFIG = "configuration";

	public static final String ELEM_COMMENT = "comment";

	public static final String ELEM_CONTEXT = "context";

	public static final String ELEM_DETAIL = "detail";

	public static final String ELEM_DETAIL_DESCRIPTION = "detailDescription";

	public static final String ELEM_DESCRIPTION = "description";

	public static final String ELEM_ENVIRONMENT = "environment";

	public static final String ELEM_ENTRY = "entry";

	public static final String ELEM_FAILURE_DETAIL = "failureDetail";

	public static final String ELEM_FAILURE_DETAIL_DESCRIPTION = "failureDetailDescription";

	public static final String ELEM_FAILURE_MESSAGE = "failureMessage";

	public static final String ELEM_HTTP_HEADERS = "httpHeaders";

	public static final String ELEM_IMPLEMENTER = "implementer";

	public static final String ELEM_INQUIRY_URL = "inquiryURL";

	public static final String ELEM_LISTEN_PORT = "listenPort";

	public static final String ELEM_LOG = "log";

	public static final String ELEM_LOG_DURATION = "logDuration";

	public static final String ELEM_LOG_FILE = "logFile";

	public static final String ELEM_MAN_IN_THE_MIDDLE = "manInTheMiddle";

	public static final String ELEM_MAX_CONNECTIONS = "maxConnections";

	public static final String ELEM_MESSAGE_CONTENT = "messageContent";
	
	public static final String ELEM_MESSAGE_CONTENT_WITH_ATTACHMENTS = "messageContentWithAttachments";
	
	public static final String ELEM_MESSAGE_ENTRY = "messageEntry";

	public static final String ELEM_MESSAGE_INPUT = "messageInput";

	public static final String ELEM_MONITOR = "monitor";

	public static final String ELEM_OPERATING_SYSTEM = "operatingSystem";

	public static final String ELEM_PREREQ_FAILED_LIST = "prereqFailedList";

	public static final String ELEM_PREREQ_LIST = "prereqList";

	public static final String ELEM_PROFILE = "profile";

	public static final String ELEM_PROFILE_ASSERTIONS = "profileAssertions";

	public static final String ELEM_READ_TIMEOUT_SECONDS = "readTimeoutSeconds";

	public static final String ELEM_RECEIVER_HOST_AND_PORT = "receiverHostAndPort";

	public static final String ELEM_REDIRECT = "redirect";

	public static final String ELEM_REPORT = "report";

	public static final String ELEM_REPORT_FILE = "reportFile";

	public static final String ELEM_RUNTIME = "runtime";

	public static final String ELEM_SCHEME_AND_HOSTPORT = "schemeAndHostPort";

	public static final String ELEM_SENDER_HOST_AND_PORT = "senderHostAndPort";

	public static final String ELEM_SERVICE_LOCATION = "serviceLocation";

	public static final String ELEM_SUMMARY = "summary";

	public static final String ELEM_TEST_ASSERTION = "testAssertion";

	public static final String ELEM_TEST_ASSERTIONS_FILE = "testAssertionsFile";

	public static final String ELEM_TEST_ASSERTION_ID = "testAssertionID";

	public static final String ELEM_TEST_COVERAGE = "testCoverage";

	public static final String ELEM_UDDI_ENTRY = "uddiEntry";

	public static final String ELEM_UDDI_KEY = "uddiKey";

	public static final String ELEM_UDDI_REFERENCE = "uddiReference";

	public static final String ELEM_VERBOSE = "verbose";

	public static final String ELEM_WSDL_ELEMENT = "wsdlElement";

	public static final String ELEM_WSDL_INPUT = "wsdlInput";

	public static final String ELEM_WSDL_REFERENCE = "wsdlReference";

	public static final String ELEM_WSDL_URI = "wsdlURI";

	public static final String ELEM_XML_PARSER = "xmlParser";
	
	public static final String ELEM_MIME_PART = "mimePart";

	public static final String ELEM_MIME_HEADERS = "mimeHeaders";
	
	public static final String ELEM_MIME_CONTENT = "mimeContent";

	public static final String ELEM_BOUNDARY_STRING = "boundaryString";

	/**
	 * Attribute names.
	 */
	public static final String ATTR_ALTERNATE = "alternate";

	public static final String ATTR_ARRAY_TYPE = "arrayType";

	public static final String ATTR_ASSERTION_DESCRIPTION = "assertionDescription";

	public static final String ATTR_BOM = "BOM";

	public static final String ATTR_CHARSET = "charset";

	public static final String ATTR_CODE = "code";

	public static final String ATTR_COLUMN_NUMBER = "columnNumber";

	public static final String ATTR_CONVERSATION_ID = "conversationID";

	public static final String ATTR_CORRELATION_TYPE = "correlationType";

	public static final String ATTR_DATE = "date";

	public static final String ATTR_ENABLED = "enabled";

	public static final String ATTR_ENTRY_TYPE = "entryType";

	public static final String ATTR_FAILURE_MESSAGE = "failureMessage";

	public static final String ATTR_FAILURE_DETAIL = "failureDetail";

	public static final String ATTR_HREF = "href";

	public static final String ATTR_ID = "id";

	public static final String ATTR_ID_UC = "ID";

	public static final String ATTR_IMPLEMENTER = "implementer";

	public static final String ATTR_LINE_NUMBER = "lineNumber";

	public static final String ATTR_LOCATION = "location";

	public static final String ATTR_MEDIA = "media";

	public static final String ATTR_MESSAGE_ENTRY = "messageEntry";

	public static final String ATTR_MESSAGES = "messages";

	public static final String ATTR_NAME = "name";

	public static final String ATTR_NAMESPACE = "namespace";

	public static final String ATTR_PARENT_ELEMENT_NAME = "parentElementName";

	public static final String ATTR_REFERENCE_ID = "referenceID";

	public static final String ATTR_REFERENCE_TYPE = "referenceType";

	public static final String ATTR_RELEASE_DATE = "releaseDate";

	public static final String ATTR_REPLACE = "replace";

	public static final String ATTR_RESULT = "result";

	public static final String ATTR_SERVICE_NAME = "serviceName";

	public static final String ATTR_SPECIFICATION = "specification";

	public static final String ATTR_TIMESTAMP = "timestamp";

	public static final String ATTR_TITLE = "title";

	public static final String ATTR_TYPE = "type";

	public static final String ATTR_VERSION = "version";

	/**
	 * Default values.
	 */
	public static final String DEFAULT_REPORT_URI = "report.xml";

	// TEMP: Use the following report title for the draft release only.
	//public static final String DEFAULT_REPORT_TITLE = "WS-I Profile
	// Conformance Report";
	public static final String DEFAULT_REPORT_TITLE = "WS-I Basic Profile Conformance Report.";

	public static final String DEFAULT_TEST_ASSERTIONS_DOCUMENT_URI = "http://www.ws-i.org/Testing/Tools/2004/12/AP10_BP11_SSBP10_TAD.xml";

	public static final String DEFAULT_LOG_XSL = "../common/xsl/log.xsl";

	public static final String DEFAULT_REPORT_XSL = "../common/xsl/report.xsl";

	public static final String DEFAULT_XSL_TYPE = "text/xsl";

	public static final String DEFAULT_XML_ENCODING = "UTF-8";

	/**
	 * BOM constants.
	 *  
	 */
	public static int BOM_UTF16 = 0xFFFE;

	public static int BOM_UTF16_BIG_ENDIAN = 0xFEFF;

	public static int BOM_UTF8 = 0xEFBBBF;

	/**
	 * Validation constants.
	 *  
	 */
	public static final String ATTRVAL_SOAP_BODY_USE_LIT = "literal";

	public static final String ATTRVAL_SOAP_BODY_USE_ENC = "encoded";

	public static final String ATTRVAL_SOAP_BIND_STYLE_DOC = "document";

	public static final String ATTRVAL_SOAP_BIND_STYLE_RPC = "rpc";

	public static final String ATTRVAL_CORRELATION_TYPE_ENDPOINT = "endpoint";

	public static final String ATTRVAL_CORRELATION_TYPE_NAMESPACE = "namespace";

	public static final String ATTRVAL_CORRELATION_TYPE_OPERATION = "operation";

	/**
	 * WS-I constants - attributes for conformance claims (discovery).
	 *  
	 */

	public static final String ATTRVAL_UDDI_CLAIM_KEYVALUE = "http://ws-i.org/profiles/basic/1.0";

	public static final String ATTRVAL_UDDI_CLAIM_TMODELKEY = "uuid:65719168-72c6-3f29-8c20-62defb0961c0";

	/**
	 * WS-I names and versions of the profile test assertion document supported
	 * in the current version of the test tools.
     */
	/** @deprecated -- this has been replaced with a TAD registry mechanism. */
	public static final String BASIC_PROFILE_TAD_NAME = "Basic Profile Test Assertions";
    /** @deprecated -- this has been replaced with a TAD registry mechanism. */
	public static final String BASIC_PROFILE_1_1_TAD_NAME = "Basic Profile 1.1 Test Assertions";
    /** @deprecated -- this has been replaced with a TAD registry mechanism. */
	public static final String SIMPLE_SOAP_BINDINGS_PROFILE_TAD_NAME = "Simple Soap Binding Profile [1.0] (with Basic Profile [1.1]) Test Assertions";
    /** @deprecated -- this has been replaced with a TAD registry mechanism. */
	public static final String ATTACHMENTS_PROFILE_TAD_NAME = "Attachments Profile [1.0] (with Basic Profile [1.1] and Simple Soap Binding Profile [1.0]) Test Assertions";
    /** @deprecated -- this has been replaced with a TAD registry mechanism. */
	public static final String BASIC_PROFILE_TAD_VERSION = "1.1.0";
    /** @deprecated -- this has been replaced with a TAD registry mechanism. */
	public static final String BASIC_PROFILE_1_1_TAD_VERSION = "1.1.0";
    /** @deprecated -- this has been replaced with a TAD registry mechanism. */
	public static final String SIMPLE_SOAP_BINDINGS_PROFILE_TAD_VERSION = "1.0.0";
    /** @deprecated -- this has been replaced with a TAD registry mechanism. */
	public static final String ATTACHMENTS_PROFILE_TAD_VERSION = "1.0.0";

	/**
	 * System dependent line separator character.
	 */
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * System dependent path separator character.
	 */
	public static final String PATH_SEPARATOR = System
			.getProperty("path.separator");

	/**
	 * File which contains the copyright, license and feedback comments.
	 */
	public static final String COMMENTS_FILE = "comments.xml";
	public static final String CONTENT_TYPE_TEXT_XML = "text/xml";
	public static final String CONTENT_TYPE_MULTIPART = "multipart/related";

	public static final String FILE_PREFIX = "file:";
	public static final String HTTP_PREFIX = "http:";
	public static final String FILE_PROTOCOL = "file:///";
	public static final String WSI_PREFIX = "WS-I: ";
}