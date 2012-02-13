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
 * The interface for a reference to a description artifact.  
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface WSDLReference extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_WSDL_REFERENCE;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_ANALYZER_CONFIG, ELEM_NAME);

  /**
   * Get WSDL element.
   * @return WSDL element.
   * @see #setWSDLElement
   */
  public WSDLElement getWSDLElement();

  /**
   * Set WSDL element.
   * @param wsdlElement WSDL element.
   * @see #getWSDLElement
   */
  public void setWSDLElement(WSDLElement wsdlElement);

  /**
   * Get WSDL location.
   * @return WSDL location.
   * @see #setWSDLLocation
   */
  public String getWSDLLocation();

  /**
   * Set WSDL location.
   * @param wsdlLocation WSDL location.
   * @see #getWSDLLocation
   */
  public void setWSDLLocation(String wsdlLocation);

  /**
   * Get service location.
   * @return service location.
   * @see #setServiceLocation
   */
  public String getServiceLocation();

  /**
   * Set service location.
   * @param serviceLocation service location.
   * @see #getServiceLocation
   */
  public void setServiceLocation(String serviceLocation);
}
