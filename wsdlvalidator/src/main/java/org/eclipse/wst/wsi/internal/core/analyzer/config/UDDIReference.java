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
 * The interface for a reference to a discovery artifact.  
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface UDDIReference extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_UDDI_REFERENCE;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_ANALYZER_CONFIG, ELEM_NAME);

  /**
   * UDDI key type: bindingKey.
   */
  public static final String BINDING_KEY = "bindingKey";

  /**
   * UDDI key type: tModelKey.
   */
  public static final String TMODEL_KEY = "tModelKey";

  /**
   * Get UDDI  key type.
   * @return UDDI  key type.
   * @see #setKeyType
   */
  public String getKeyType();

  /**
   * Set UDDI key type.
   * @param keyType UDDI key type.
   * @see #getKeyType
   */
  public void setKeyType(String keyType);

  /**
   * Get UDDI key.
   * @return UDDI key.
   * @see #setKey
   */
  public String getKey();

  /**
   * Set UDDI key.
   * @param key UDDI key.
   * @see #getKey
   */
  public void setKey(String key);

  /**
   * Get UDDI inquiry URL.
   * @return UDDI inquiry URL.
   * @see #setInquiryURL
   */
  public String getInquiryURL();

  /**
   * Set UDDI inquiryURL.
   * @param inquiryURL UDDI inquiryURL.
   * @see #getInquiryURL
   */
  public void setInquiryURL(String inquiryURL);

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
