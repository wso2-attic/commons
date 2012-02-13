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
package org.eclipse.wst.wsi.internal.core.analyzer.config;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;

/**
 * The base interface for WSDL definitions. 
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface WSDLElement extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_WSDL_ELEMENT;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_ANALYZER_CONFIG, ELEM_NAME);

  /**
   * Get WSDL element type.
   * @return WSDL element type.
   * @see #setType
   */
  public String getType();

  /**
   * Set WSDL element type.
   * @param type WSDL element type.
   * @see #getType
   */
  public void setType(String type);

  /**
   * Get WSDL element namespace.
   * @return WSDL element namespace.
   * @see #setNamespace
   */
  public String getNamespace();

  /**
   * Set WSDL element namespace.
   * @param namespace WSDL element namespace.
   * @see #getNamespace
   */
  public void setNamespace(String namespace);

  /**
   * Get WSDL element name.
   * @return WSDL element name.
   * @see #setName
   */
  public String getName();

  /**
   * Set WSDL element name.
   * @param name WSDL element name.
   * @see #getName
   */
  public void setName(String name);

  /**
   * Get WSDL element qualified name.
   * @return WSDL element qualified name.
   */
  public QName getQName();

  /**
   * Get WSDL parent element name.
   * @return WSDL parent element name.
   * @see #setParentElementName
   * 
   */
  public String getParentElementName();

  /**
   * Set WSDL parent element name.
   * @param parentElementName WSDL parent element name.
   * @see #getParentElementName
   */
  public void setParentElementName(String parentElementName);

  /**
   * Get WSDL parent element QName.
   * @return WSDL parent element QName.
   */
  public QName getParentElementQName();

  /**
   * Is port element.
   * @return true if the element is a port.
   */
  public boolean isPort();

  /**
   * Is binding element.
   * @return true if the lement is a binding.
   */
  public boolean isBinding();

  /**
   * Is portType element.
   * @return true if element is a port type.
   */
  public boolean isPortType();

  /**
   * Is operation element.
   * @return true if the element is an operation.
   */
  public boolean isOperation();

  /**
   * Is message element.
   * @return true if element is a message.
   */
  public boolean isMessage();
}
