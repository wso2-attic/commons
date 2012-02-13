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

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.core.profile.validator.WSDLValidator;

/**
 * The implementation for WSDL definitions. 
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class WSDLElementImpl implements WSDLElement
{
  protected String type;
  protected String namespace;
  protected String name;
  protected String parentElementName;

  /**
   * @see #setType
   */
  public String getType()
  {
    return this.type;
  }

  /**
   * @see #getType
   */
  public void setType(String type)
  {
    this.type = type;
  }

  /**
   * @see #setNamespace
   */
  public String getNamespace()
  {
    return this.namespace;
  }

  /**
   * @see #getNamespace
   */
  public void setNamespace(String namespace)
  {
    this.namespace = namespace;
  }

  /**
   * @see #setName
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * @see #getName
   */
  public void setName(String name)
  {
    this.name = name;
  }

  public QName getQName()
  {
    return new QName(this.namespace, this.name);
  }

  /**
   * Get WSDL parent element name.
   * @see #setParentElementName
  
   */
  public String getParentElementName()
  {
    return this.parentElementName;
  }

  /**
   * Set WSDL parent element name.
   * @see #getParentElementName
   */
  public void setParentElementName(String parentElementName)
  {
    this.parentElementName = parentElementName;
  }

  /**
   * @see org.eclipse.wst.wsi.test.analyzer.config.WSDLElement#getParentElementQName()
   */
  public QName getParentElementQName()
  {
    return new QName(this.namespace, this.parentElementName);
  }

  /**
   * Is port element.
   */
  public boolean isPort()
  {
    return type.equals(WSDLValidator.TYPE_DESCRIPTION_PORT);
  }

  /**
   * Is binding element.
   */
  public boolean isBinding()
  {
    return type.equals(WSDLValidator.TYPE_DESCRIPTION_BINDING);
  }

  /**
   * Is portType element.
   */
  public boolean isPortType()
  {
    return type.equals(WSDLValidator.TYPE_DESCRIPTION_PORTTYPE);
  }

  /**
   * Is operation element.
   */
  public boolean isOperation()
  {
    return type.equals(WSDLValidator.TYPE_DESCRIPTION_OPERATION);
  }

  /**
   * Is message element.
   */
  public boolean isMessage()
  {
    return type.equals(WSDLValidator.TYPE_DESCRIPTION_MESSAGE);
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println("    WSDL Element: ");
    pw.println("      type ................... " + this.type);
    pw.println("      namespace .............. " + this.namespace);
    pw.println("      name ................... " + this.name);

    if (this.parentElementName != null)
      pw.println("      parentElementName ...... " + this.parentElementName);

    return sw.toString();
  }

  /**
   * Get representation of this object as an XML string.
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Config options
    pw.print("        <" + nsName + ELEM_NAME + " ");
    pw.print(WSIConstants.ATTR_TYPE + "=\"" + getType() + "\" ");
    pw.print(WSIConstants.ATTR_NAMESPACE + "=\"" + getNamespace() + "\" ");
    if (this.parentElementName != null)
      pw.print(
        WSIConstants.ATTR_PARENT_ELEMENT_NAME
          + "=\""
          + getParentElementName()
          + "\"");
    pw.print(">");
    pw.print(getName());
    pw.println("</" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }
}
