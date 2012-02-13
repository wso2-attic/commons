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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.wst.wsi.internal.core.util.XMLInfo;

/**
 * This class contains Tool environment information.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class ToolEnvironment
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_ENVIRONMENT;

  /**
   * QName.
   */
  //public static final QName QNAME = new QName(WSIConstants.NS_URI_WSI_COMMON, ELEM_NAME);

  /**
   * Tool environment information.
   */
  protected String runtimeName = System.getProperty("java.runtime.name");
  protected String runtimeVersion = System.getProperty("java.runtime.version");

  protected String osName = System.getProperty("os.name");
  protected String osVersion = System.getProperty("os.version");

  protected String xmlParserName = "?";
  protected String xmlParserVersion = "?";

  /**
   * Additional tool environment information.
   */
  protected XMLInfo xmlInfo = null;

  /**
   * Create object that represents tool runtime environment.
   */
  public ToolEnvironment()
  {
    try
    {

      // FUTURE: made compatible with later versions of Xerces where getVersion
      // is no longer statically available.
      // xmlParserVersion = new org.apache.xerces.impl.Version().getVersion();
      xmlParserVersion = org.apache.xerces.impl.Version.getVersion();

      xmlParserName = "Apache Xerces";
    }

    catch (Exception e)
    {
      // ADD: How else can we get the name version number
    }

    catch (Error err)
    {
      // ADD: How else can we get the name version number
    }
  }

  /**
   * Create object that represents tool runtime environment.
   * @param xmlInfo additional tool environment information.
   */
  public ToolEnvironment(XMLInfo xmlInfo)
  {
    this.xmlInfo = xmlInfo;
  }

  /**
   * Returns the osName.
   * @return the osName.
   * @see #setOSName
   */
  public String getOSName()
  {
    return osName;
  }

  /**
   * Returns the osVersion.
   * @return the osVersion.
   * @see #setOSVersion
   */
  public String getOSVersion()
  {
    return osVersion;
  }

  /**
   * Returns the parserName.
   * @return the parserName.
   * @see #setXMLParserName
   */
  public String getXMLParserName()
  {
    return xmlParserName;
  }

  /**
   * Returns the parserVersion.
   * @return the parserVersion.
   * @see #setXMLParserVersion
   */
  public String getXMLParserVersion()
  {
    return xmlParserVersion;
  }

  /**
   * Returns the runtimeName.
   * @return the runtimeName.
   * @see #setRuntimeName
   */
  public String getRuntimeName()
  {
    return runtimeName;
  }

  /**
   * Returns the runtimeVersion.
   * @return the runtimeVersion.
   * @see #setRuntimeVersion
   */
  public String getRuntimeVersion()
  {
    return runtimeVersion;
  }

  /**
   * Sets the osName.
   * @param osName the osName to set.
   * @see #getOSName
   */
  public void setOSName(String osName)
  {
    this.osName = osName;
  }

  /**
   * Sets the osVersion.
   * @param osVersion the osVersion to set.
   * @see #getOSVersion
   */
  public void setOSVersion(String osVersion)
  {
    this.osVersion = osVersion;
  }

  /**
   * Sets the parserName.
   * @param xmlParserName the parserName to set.
   * @see #getXMLParserName
   */
  public void setXMLParserName(String xmlParserName)
  {
    this.xmlParserName = xmlParserName;
  }

  /**
   * Sets the parserVersion.
   * @param xmlParserVersion the parserVersion to set.
   * @see #getXMLParserVersion
   */
  public void setXMLParserVersion(String xmlParserVersion)
  {
    this.xmlParserVersion = xmlParserVersion;
  }

  /**
   * Sets the runtimeName.
   * @param runtimeName the runtimeName to set.
   * @see #getRuntimeName
   */
  public void setRuntimeName(String runtimeName)
  {
    this.runtimeName = runtimeName;
  }

  /**
   * Sets the runtimeVersion.
   * @param runtimeVersion the runtimeVersion to set.
   * @see #getRuntimeVersion
   */
  public void setRuntimeVersion(String runtimeVersion)
  {
    this.runtimeVersion = runtimeVersion;
  }

  /**
   * Sets the additional environment information.
   * @param xmlInfo additional tool environment information.
   */
  public void setAdditionalToolEnvironment(XMLInfo xmlInfo)
  {
    this.xmlInfo = xmlInfo;
  }

  /**
   * Return XML string representation of this object. 
   * @param namespaceName  the namespace prefix.
   * @return the XML string representation of this object. 
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if (!nsName.equals(""))
      nsName += ":";

    // Start element
    pw.println("    <" + nsName + ELEM_NAME + ">");

    pw.print("      <" + nsName + WSIConstants.ELEM_RUNTIME + " ");
    pw.print(WSIConstants.ATTR_NAME + "=\"" + runtimeName + "\" ");
    pw.println(WSIConstants.ATTR_VERSION + "=\"" + runtimeVersion + "\"/>");
    pw.print("      <" + nsName + WSIConstants.ELEM_OPERATING_SYSTEM + " ");
    pw.print(WSIConstants.ATTR_NAME + "=\"" + osName + "\" ");
    pw.println(WSIConstants.ATTR_VERSION + "=\"" + osVersion + "\"/>");
    pw.print("      <" + nsName + WSIConstants.ELEM_XML_PARSER + " ");
    pw.print(WSIConstants.ATTR_NAME + "=\"" + xmlParserName + "\" ");
    pw.println(WSIConstants.ATTR_VERSION + "=\"" + xmlParserVersion + "\"/>");

    // If addtional info, then get it
    if (xmlInfo != null)
      pw.print(xmlInfo.toXMLString(nsName));

    // End element
    pw.println("    </" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }
}
