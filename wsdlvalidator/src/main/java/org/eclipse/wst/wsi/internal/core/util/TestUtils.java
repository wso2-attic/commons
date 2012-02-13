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
package org.eclipse.wst.wsi.internal.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.wst.wsi.internal.core.ToolInfo;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Element;

/**
 * Test Utility class.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public final class TestUtils
{
  /**
   * Format a style sheet declaration from the addStyleSheet element.
   * @param element an Element object.
   * @param addStyleSheet an addStyleSheet element.
   * @param defaultHref default href.
   */
  public static final void parseAddStyleSheet(
    Element element,
    AddStyleSheet addStyleSheet,
    String defaultHref)
  {
    addStyleSheet.setHref(
      XMLUtils.getAttributeValue(element, WSIConstants.ATTR_HREF, defaultHref));
    addStyleSheet.setType(
      XMLUtils.getAttributeValue(
        element,
        WSIConstants.ATTR_TYPE,
        WSIConstants.DEFAULT_XSL_TYPE));
    addStyleSheet.setTitle(
      XMLUtils.getAttributeValue(element, WSIConstants.ATTR_TITLE));
    addStyleSheet.setMedia(
      XMLUtils.getAttributeValue(element, WSIConstants.ATTR_MEDIA));
    addStyleSheet.setCharset(
      XMLUtils.getAttributeValue(element, WSIConstants.ATTR_CHARSET));
    addStyleSheet.setAlternate(
      XMLUtils.getAttributeValue(element, WSIConstants.ATTR_ALTERNATE));
  }

  /**
   * Display tool name and copyright notice.
   * @param toolInfo a ToolInfo object.
   */
  public static void printToolInfo(ToolInfo toolInfo)
  {
    System.out.println(
      "Conformance "
        + toolInfo.getName()
        + " Tool"
        + ", Version: "
        + toolInfo.getVersion()
        + ", Release Date: "
        + toolInfo.getReleaseDate());
    // System.out.println(WSIConstants.COPYRIGHT);
    // System.out.println(" ");
  }

  /** 
   * Get XML comment used in log and report file.
   * @return XML comment used in log and report file.
   */
  public static String getXMLComment()
  {
    String comment = null;
    String readLine = null;

    try
    {
      // Add required comments (copyright, etc.)
      BufferedReader bufferedReader =
        new BufferedReader(
          new InputStreamReader(
            Utils.getInputStream(WSIConstants.COMMENTS_FILE)));

      // Initialize string to empty string
      comment = "";

      while (bufferedReader.ready())
      {
        if ((readLine = bufferedReader.readLine()) != null)
          comment += readLine + WSIConstants.LINE_SEPARATOR;
      }
    }

    catch (Exception e)
    {
      // Ignore exception and just return null
    }

    return comment;
  }

  /**
   * Get schema location for XML schema.
   * @return schema location for XML schema.
   */
  public static String getXMLSchemaLocation()
  {
    return getSchemaLocation(
      WSIProperties.PROP_XML_SCHEMA,
      WSIProperties.DEF_XML_SCHEMA);
  }

  /**
   * Get SOAP schema location.
   * @return SOAP schema location.
   */
  public static String getSOAPSchemaLocation()
  {
    return getSchemaLocation(
      WSIProperties.PROP_SOAP_SCHEMA,
      WSIProperties.DEF_SOAP_SCHEMA);
  }

  /**
   * Get WSDL schema location.
   * @return WSDL schema location.
   */
  public static String getWSDLSchemaLocation()
  {
    return getSchemaLocation(
      WSIProperties.PROP_WSDL_SCHEMA,
      WSIProperties.DEF_WSDL_SCHEMA);
  }

  /**
  * Get WSDL SOAP schema location.
  * @return WSDL SOAP schema location.
  */
  public static String getWSDLSOAPSchemaLocation()
  {
    return getSchemaLocation(
      WSIProperties.PROP_WSDL_SOAP_SCHEMA,
      WSIProperties.DEF_WSDL_SOAP_SCHEMA);
  }
  /**
   * Get WSDL schema location.
   * @return WSDL schema location.
   */
  private static String getSchemaLocation(
    String propertyName,
    String defaultValue)
  {
    //String schemaLocation = null;

    // Get the wsi.home system property
    //String wsiHome = System.getProperty(WSIProperties.PROP_WSI_HOME);
    //String fileLocation = WSIProperties.getProperty(propertyName, defaultValue);
    //
    // If the file location is specified in the wsi.properties file then build location
    //if (fileLocation != null)
    //{
    // If wsi.home is set, then use it
    //  if (wsiHome != null)
    //  {
    //     schemaLocation = wsiHome;
    //   }
    //   if (schemaLocation == null)
    //   {
    //    schemaLocation = fileLocation;
    //  }
    //  else
    //  {
    //   if (!schemaLocation.endsWith("/"))
    //    {
    //     schemaLocation += "/";
    //   }
    //
    //    schemaLocation += fileLocation;
    //  }
    // }
    return defaultValue;
  }
}
