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
package org.eclipse.wst.wsi.internal.core.analyzer.config.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;

/**
 * The implementation for a reference to a description artifact.  
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class WSDLReferenceImpl implements WSDLReference
{
  /**
   * WSDL element.
   */
  protected WSDLElement wsdlElement = null;

  /**
   * Service location.
   */
  protected String serviceLocation = null;

  /**
   * WSDL document location.
   */
  protected String wsdlLocation = null;

  /**
   * Default constructor.
   */
  public WSDLReferenceImpl()
  {
  }

  /**
   * Constructor with all settings.
   * @param wsdlElement      a WSDL element.
   * @param wsdlLocation     a WSDL document location.
   * @param serviceLocation  a service location.
   */
  public WSDLReferenceImpl(
    WSDLElement wsdlElement,
    String wsdlLocation,
    String serviceLocation)
  {
    this.wsdlElement = wsdlElement;
    this.wsdlLocation = wsdlLocation;
    this.serviceLocation = serviceLocation;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference#getWSDLElement()
   */
  public WSDLElement getWSDLElement()
  {
    return this.wsdlElement;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference#setWSDLElement(WSDLElement)
   */
  public void setWSDLElement(WSDLElement wsdlElement)
  {
    this.wsdlElement = wsdlElement;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference#getWSDLLocation()
   */
  public String getWSDLLocation()
  {
    return this.wsdlLocation;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference#setWSDLLocation()
   */
  public void setWSDLLocation(String wsdlLocation)
  {
    this.wsdlLocation = wsdlLocation;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference#getServiceLocation()
   */
  public String getServiceLocation()
  {
    return this.serviceLocation;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference#setServiceLocation()
   */
  public void setServiceLocation(String serviceLocation)
  {
    this.serviceLocation = serviceLocation;
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println("  WSDL Reference:");
    pw.print(this.wsdlElement.toString());
    pw.println("    wsdlURI .................. " + this.wsdlLocation);
    if (this.serviceLocation != null)
      pw.println("  serviceLocation .......... " + this.serviceLocation);

    return sw.toString();
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Element
    pw.println("      <" + nsName + ELEM_NAME + ">");
    pw.print(getWSDLElement().toXMLString(nsName));
    pw.print("        <" + nsName + WSIConstants.ELEM_WSDL_URI + ">");
    pw.print(getWSDLLocation());
    pw.println("</" + nsName + WSIConstants.ELEM_WSDL_URI + ">");

    if (this.serviceLocation != null)
    {
      pw.print("        <" + nsName + WSIConstants.ELEM_SERVICE_LOCATION + ">");
      pw.print(XMLUtils.xmlEscapedString(getServiceLocation()));
      pw.println("</" + nsName + WSIConstants.ELEM_SERVICE_LOCATION + ">");
    }

    pw.println("      </" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }

}
