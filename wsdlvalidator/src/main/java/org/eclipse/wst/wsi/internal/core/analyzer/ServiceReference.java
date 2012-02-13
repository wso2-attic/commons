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
package org.eclipse.wst.wsi.internal.core.analyzer;

import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;

/**
 * This class contains all of the information that is needed to process description test assertions.  The infromation
 * in this object will be derived from analyzer configuration information, UDDI entries, or the WSDL port element. 
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ServiceReference
{
  protected WSDLElement wsdlElement;
  protected String wsdlLocation;
  protected String serviceLocation;

  /**
   * Create service reference without references.  All of them will be set later as test assertions are 
   * being processed.
   */
  public ServiceReference()
  {
  }

  /**
   * Create service reference from entries specified in the analyzer config file.  This information may include
   * the information extracted from the wsdlElement, wsdlURI and serviceLocation elements.
   * @param analyzerConfig an AnalyzerConfig object.
   */
  public ServiceReference(AnalyzerConfig analyzerConfig)
  {
    this.wsdlElement = analyzerConfig.getWSDLElement();
    this.wsdlLocation = analyzerConfig.getWSDLLocation();
    this.serviceLocation = analyzerConfig.getServiceLocation();

  }

  /**
   * Get the WSDL element where analysis should begin.
   * @return he WSDL element where analysis should begin.
   * @see #setWSDLElement
   */
  public WSDLElement getWSDLElement()
  {
    return this.wsdlElement;
  }

  /**
   * Set the WSDL element where analysis should begin.
   * @param wsdlElement the WSDL element where analysis should begin.
   * @see #getWSDLElement
   */
  public void setWSDLElement(WSDLElement wsdlElement)
  {
    this.wsdlElement = wsdlElement;
  }

  /**
   * Get WSDL document location.
   * @return WSDL document location.
   * @see #setWSDLLocation
   */
  public String getWSDLLocation()
  {
    return this.wsdlLocation;
  }

  /**
   * Set WSDL document location.
   * @param wsdlLocation WSDL document location.
   * @see #getWSDLLocation
   */
  public void setWSDLLocation(String wsdlLocation)
  {
    this.wsdlLocation = wsdlLocation;
  }

  /**
   * Get Web service location.
   * @return Web service location.
   * @see #setServiceLocation
   */
  public String getServiceLocation()
  {
    return this.serviceLocation;
  }

  /**
   * Set Web service location.
   * @param serviceLocation eb service location.
   * @see #getServiceLocation
   */
  public void setServiceLocation(String serviceLocation)
  {
    this.serviceLocation = serviceLocation;
  }
}
