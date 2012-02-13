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
package org.eclipse.wst.wsi.internal.core.xml;

import javax.xml.namespace.QName;

/**
 * This class contains information on XML tags.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public interface XMLTags
{
  /**
   * Namespaces.
   */
  public final static String NS_URI_XSI =
    "http://www.w3.org/2001/XMLSchema-instance";
  public final static String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";
  public final static String NS_URI_XSD = "http://www.w3.org/2001/XMLSchema";
  public final static String XSD_SCHEMALOCATION =
    "http://www.w3.org/2001/XMLSchema.xsd";

  /**
   * Qualified names of XSD elements and attributes.
   */
  public final static QName ELEM_XSD_ANNOTATION =
    new QName(NS_URI_XSD, "annotation");
  public final static QName ELEM_XSD_IMPORT = new QName(NS_URI_XSD, "import");  
  public final static QName ELEM_XSD_INCLUDE = new QName(NS_URI_XSD, "include");
  public final static QName ELEM_XSD_SCHEMA = new QName(NS_URI_XSD, "schema");
  public final static QName ELEM_XSD_ELEMENT = new QName(NS_URI_XSD, "element");
  public final static QName ELEM_XSD_ATTRIBUTE =
    new QName(NS_URI_XSD, "attribute");
  public final static QName ELEM_XSD_COMPLEXTYPE =
    new QName(NS_URI_XSD, "complexType");

  public final static QName ATTR_XSI_TYPE = new QName(NS_URI_XSI, "type");
  public final static QName ATTR_XSI_NIL = new QName(NS_URI_XSI, "nil");

  public final static QName ATTR_XSD_NAME = new QName(NS_URI_XSD, "name");
  public final static QName ATTR_XSD_TYPE = new QName(NS_URI_XSD, "type");
  public final static QName ATTR_XSD_BASE = new QName(NS_URI_XSD, "base");
  public final static QName ATTR_XSD_NAMESPACE =
    new QName(NS_URI_XSD, "namespace");
  public final static QName ATTR_XSD_SCHEMALOCATION =
    new QName(NS_URI_XSD, "schemaLocation");
  public final static QName ATTR_XSD_TARGETNAMESPACE =
    new QName(NS_URI_XSD, "targetNamespace");

}
