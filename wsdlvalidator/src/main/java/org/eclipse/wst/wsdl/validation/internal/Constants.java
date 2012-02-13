/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal;

/**
 * Set constants that the validator makes use of.
 */
public interface Constants
{
  /**
   * The name of the validator properties file.
   */
  public final static String WSDL_VALIDATOR_PROPERTIES_FILE = "validatewsdl";

  /**
   * The SOAP 1.1 namespace.
   */
  public final static String NS_SOAP11 = "http://schemas.xmlsoap.org/wsdl/soap/";

  /**
   * The HTTP namespace.
   */
  public final static String NS_HTTP = "http://schemas.xmlsoap.org/wsdl/http/";

  /**
   * The MIME namespace.
   */
  public final static String NS_MIME = "http://schemas.xmlsoap.org/wsdl/mime/";
  
  /**
   * The WSDL schema cache attribute allows an XMLGrammarPool object to be set for use in 
   * inline schema validation.
   */
  public final static String XMLSCHEMA_CACHE_ATTRIBUTE = "http://www.eclipse.org/webtools/wsdl/schemacache";
  
  /**
   * The WSDL schema cache attribute allows an XMLGrammarPool object to be set for use in 
   * XML conformance validation.
   */
  public final static String XML_CACHE_ATTRIBUTE = "http://www.eclipse.org/webtools/wsdl/xmlcache";
}
