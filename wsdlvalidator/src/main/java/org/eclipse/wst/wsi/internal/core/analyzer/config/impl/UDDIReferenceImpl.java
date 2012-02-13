/*******************************************************************************
 *
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
import org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;

/**
 * The iimplementation for a reference to a discovery artifact.  
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class UDDIReferenceImpl implements UDDIReference
{
  /**
   * UDDI key type.
   */
  protected String keyType = null;

  /**
   * UDDI key.
   */
  protected String key = null;

  /**
   * UDDI inquiry URL.
   */
  protected String inquiryURL = null;

  /**
   * WSDL element.
   */
  protected WSDLElement wsdlElement = null;

  /**
   * Service location.
   */
  protected String serviceLocation = null;

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference#getKeyType()
   */
  public String getKeyType()
  {
    return this.keyType;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference#setKeyType(String)
   */
  public void setKeyType(String keyType)
  {
    if (keyType.equals(BINDING_KEY) || keyType.equals(TMODEL_KEY))
      this.keyType = keyType;
    else
      throw new IllegalArgumentException(
        "Invalid UDDI key type: [" + keyType + "].");
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference#getKey()
   */
  public String getKey()
  {
    return this.key;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference#setKey(String)
   */
  public void setKey(String key)
  {
    this.key = key;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference#getInquiryURL()
   */
  public String getInquiryURL()
  {
    return this.inquiryURL;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference#setInquiryURL(String)
   */
  public void setInquiryURL(String inquiryURL)
  {
    this.inquiryURL = inquiryURL;
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

    pw.println("  UDDI Reference: ");
    pw.println("    UDDI Key: ");
    pw.println("      type ................... " + this.keyType);
    pw.println("      key .................... " + this.key);
    pw.println("    inquiryURL ............... " + this.inquiryURL);
    if (this.serviceLocation != null)
      pw.println("    serviceLocation .......... " + this.serviceLocation);
    if (this.wsdlElement != null)
      pw.print(this.wsdlElement.toString());

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
    pw.print("        <" + nsName + WSIConstants.ELEM_UDDI_KEY + " ");
    pw.print(WSIConstants.ATTR_TYPE + "=\"" + getKeyType() + "\">");
    pw.print(getKey());
    pw.println("</" + nsName + WSIConstants.ELEM_UDDI_KEY + ">");
    pw.print("        <" + nsName + WSIConstants.ELEM_INQUIRY_URL + ">");
    pw.print(getInquiryURL());
    pw.println("</" + nsName + WSIConstants.ELEM_INQUIRY_URL + ">");

    if (this.wsdlElement != null)
      pw.print(getWSDLElement().toXMLString(nsName));

    if (this.serviceLocation != null)
    {
      pw.print("        <" + nsName + WSIConstants.ELEM_SERVICE_LOCATION + ">");
      pw.print(getServiceLocation());
      pw.println(
        "        </" + nsName + WSIConstants.ELEM_SERVICE_LOCATION + ">");
    }

    pw.println("      </" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }

}
