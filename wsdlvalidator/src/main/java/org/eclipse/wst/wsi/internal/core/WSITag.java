/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.xml.XMLTags;

/**
 * WS-I constants.  
 *  
 * @author Kulik
 */
public interface WSITag extends XMLTags
{
  /**
   * Qualified names of WSDL elements.
   */
  public final static QName WSDL_IMPORT =
    new QName(WSIConstants.NS_URI_WSDL, "import");
  public final static QName WSDL_TYPES =
    new QName(WSIConstants.NS_URI_WSDL, "types");
  public final static QName WSDL_MESSAGE =
    new QName(WSIConstants.NS_URI_WSDL, "message");
  public final static QName WSDL_DOCUMENTATION =
    new QName(WSIConstants.NS_URI_WSDL, "documentation");
  public final static QName WSDL_DEFINITIONS =
    new QName(WSIConstants.NS_URI_WSDL, "definitions");
  public final static QName WSDL_BINDING =
    new QName(WSIConstants.NS_URI_WSDL, "binding");
  public final static QName WSDL_OPERATION =
    new QName(WSIConstants.NS_URI_WSDL, "operation");
  public final static QName WSDL_PART =
    new QName(WSIConstants.NS_URI_WSDL, "part");
  public final static QName WSDL_INPUT =
    new QName(WSIConstants.NS_URI_WSDL, "input");
  public final static QName WSDL_OUTPUT =
    new QName(WSIConstants.NS_URI_WSDL, "output");
  public final static QName WSDL_FAULT =
    new QName(WSIConstants.NS_URI_WSDL, "fault");
  public final static QName WSDL_SERVICE =
    new QName(WSIConstants.NS_URI_WSDL, "service");
  public final static QName WSDL_PORT =
    new QName(WSIConstants.NS_URI_WSDL, "port");
  public final static QName WSDL_PORTTYPE =
    new QName(WSIConstants.NS_URI_WSDL, "portType");

  public static final QName WSDL_SOAP_BINDING =
    new QName(WSIConstants.NS_URI_WSDL_SOAP, "binding");
  public static final QName WSDL_SOAP_BODY =
    new QName(WSIConstants.NS_URI_WSDL_SOAP, "body");
  public static final QName WSDL_SOAP_HEADER =
    new QName(WSIConstants.NS_URI_WSDL_SOAP, "header");
  public static final QName WSDL_SOAP_HEADERFAULT =
    new QName(WSIConstants.NS_URI_WSDL_SOAP, "headerfault");
  public static final QName WSDL_SOAP_FAULT =
    new QName(WSIConstants.NS_URI_WSDL_SOAP, "fault");

  public static final QName WSDL_MIME_CONTENT =
    new QName(WSIConstants.NS_NAME_WSDL_MIME, "content");
  public static final QName WSDL_MIME_XML =
    new QName(WSIConstants.NS_NAME_WSDL_MIME, "mimeXml");
  public static final QName WSDL_MIME_PART =
    new QName(WSIConstants.NS_NAME_WSDL_MIME, "part");
  public static final QName WSDL_MIME_MULTIPART =
    new QName(WSIConstants.NS_NAME_WSDL_MIME, "multipartRelated");

  public final static QName ATTR_WSDL_ARRAYTYPE =
    new QName(WSIConstants.NS_URI_WSDL, "arrayType");
  public final static QName ATTR_WSDL_NAMESPACE =
    new QName(WSIConstants.NS_URI_WSDL, "namespace");
  public final static QName ATTR_WSDL_LOCATION =
    new QName(WSIConstants.NS_URI_WSDL, "location");

  /**
   * Namespaces.
   */
  public final static String NS_URI_CLAIM =
    "http://ws-i.org/schemas/conformanceClaim/";
  public final static String NS_URI_SOAP =
    "http://schemas.xmlsoap.org/soap/envelope/";
  public final static String NS_URI_SOAPENC =
    "http://schemas.xmlsoap.org/soap/encoding/";
  public final static String NS_URI_BASIC_PROFILE10 =
    "http://wsi.org/profiles/basic1.0/";

  public final static QName SOAPENC_ARRAY = new QName(NS_URI_SOAPENC, "Array");

  /**
   * Qualified names of WS-I claim elements.
   */
  public final static QName WSI_CLAIM = new QName(NS_URI_CLAIM, "Claim");
  public final static QName ATTR_CLAIM_CONFORMSTO =
    new QName(NS_URI_CLAIM, "conformsTo");
  public final static QName ATTR_SOAP_MUSTUNDERSTAND =
    new QName(NS_URI_SOAP, "mustUnderstand");

  /**
   * Qualified names of SOAP elements.
   */
  public final static QName ELEM_SOAP_ENVELOPE = new QName(NS_URI_SOAP, "Envelope");
  public final static QName ELEM_SOAP_BODY = new QName(NS_URI_SOAP, "Body");
  public final static QName ELEM_SOAP_FAULT = new QName(NS_URI_SOAP, "Fault");
  public final static QName ELEM_SOAP_HEADER = new QName(NS_URI_SOAP, "Header");

}