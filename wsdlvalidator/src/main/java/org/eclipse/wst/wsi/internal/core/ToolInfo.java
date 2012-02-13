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

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.eclipse.wst.wsi.internal.core.util.XMLInfo;

/**
 * Information that describes a conformance tool.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class ToolInfo
{
  /**
   * Tool information property names.
   */
  public static final String PROP_TOOL_VERSION = "wsi.tool.version";
  public static final String PROP_TOOL_RELEASE_DATE = "wsi.tool.release.date";
  public static final String PROP_TOOL_IMPLEMENTER = "wsi.tool.implementer";
  public static final String PROP_TOOL_LOCATION = "wsi.tool.location";

  /**
   * Default tool information property values.
   */
  private static final String DEF_TOOL_VERSION = "1.0.1";
  private static final String DEF_TOOL_RELEASE_DATE = "2004-01-22";
  private static final String DEF_TOOL_IMPLEMENTER = "Eclipse.org Organization";
  private static final String DEF_TOOL_LOCATION = "http://www.eclipse.org/wsvt";

  /**
   * Tool information.
   */
  protected String name;
  protected String version;
  protected String releaseDate;
  protected String implementer;
  protected String location;

  /**
   * Tool environment.
   */
  protected ToolEnvironment toolEnvironment = null;

  /**
   * Tool info properties filename.
   */
  public static final String PROP_FILENAME = "org.wsi.test.toolinfo";

  /**
   * Create tool information.
   */
  public ToolInfo()
  {
    this.toolEnvironment = new ToolEnvironment();
  }

  /**
   * Create tool information from information in the toolinfo.properties file.
   * @param name  the tool name.
   */
  public ToolInfo(String name)
  {
    this.name = name;

    getProperties();

    this.toolEnvironment = new ToolEnvironment();
  }

  /**
   * Create tool information.
   * @param name         the tool name.
   * @param version      the version of the tool.
   * @param releaseDate  the release date of the tool.
   * @param implementer  the implementer of the tool.
   * @param location     the implementer location.
   */
  public ToolInfo(
    String name,
    String version,
    String releaseDate,
    String implementer,
    String location)
  {
    this.name = name;
    this.version = version;
    this.releaseDate = releaseDate;
    this.implementer = implementer;
    this.location = location;

    this.toolEnvironment = new ToolEnvironment();
  }

  /**
   * Create tool information with tool unique environment information.
   * @param name         the tool name.
   * @param version      the version of the tool.
   * @param releaseDate  the release date of the tool.
   * @param implementer  the implementer of the tool.
   * @param location     the implementer location.
   * @param xmlInfo      additional tool environment information.
   */
  public ToolInfo(
    String name,
    String version,
    String releaseDate,
    String implementer,
    String location,
    XMLInfo xmlInfo)
  {
    this.name = name;
    this.version = version;
    this.releaseDate = releaseDate;
    this.implementer = implementer;
    this.location = location;

    this.toolEnvironment = new ToolEnvironment(xmlInfo);
  }

  /**
   * Get name.
   * @return name.
   * @see #setName
   */
  public String getName()
  {
    return name;
  }

  /**
   * Get version.
   * @return version.
   * @see #setVersion
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * Get release date.
   * @return release date.
   * @see #setReleaseDate
   */
  public String getReleaseDate()
  {
    return releaseDate;
  }

  /**
   * Get implementer.
   * @return implementer.
   * @see #setImplementer
   */
  public String getImplementer()
  {
    return implementer;
  }

  /**
   * Get implementer location.
   * @return implementer location.
   * @see #setLocation
   */
  public String getLocation()
  {
    return location;
  }

  /**
   * Returns the toolEnvironment.
   * @return ToolEnvironment
   * @see #setToolEnvironment
   */
  public ToolEnvironment getToolEnvironment()
  {
    return toolEnvironment;
  }

  /**
   * Sets the implementer.
   * @param implementer the implementer to set.
   * @see #getImplementer
   */
  public void setImplementer(String implementer)
  {
    this.implementer = implementer;
  }

  /**
   * Sets the location.
   * @param location the location to set.
   * @see #getLocation
   */
  public void setLocation(String location)
  {
    this.location = location;
  }

  /**
   * Sets the name.
   * @param name the name to set.
   * @see #getName
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Sets the releaseDate.
   * @param releaseDate the releaseDate to set.
   * @see #getReleaseDate
   */
  public void setReleaseDate(String releaseDate)
  {
    this.releaseDate = releaseDate;
  }

  /**
   * Sets the toolEnvironment.
   * @param toolEnvironment the toolEnvironment to set.
   * @see #getToolEnvironment
   */
  public void setToolEnvironment(ToolEnvironment toolEnvironment)
  {
    this.toolEnvironment = toolEnvironment;
  }

  /**
   * Sets the toolEnvironment.
   * @param xmlInfo additional tool environment information.
   */
  public void setAdditionalToolEnvironment(XMLInfo xmlInfo)
  {
    this.toolEnvironment.setAdditionalToolEnvironment(xmlInfo);
  }

  /**
   * Sets the version.
   * @param version the version to set.
   * @see #getVersion
   */
  public void setVersion(String version)
  {
    this.version = version;
  }

  /**
   * Return start XML string representation of this object.
   * @param namespaceName  the namespace prefix.
   * @return the start XML string representation of this object. 
   */
  public String getStartXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if (!nsName.equals(""))
      nsName += ":";

    pw.print("  <" + nsName + getName().toLowerCase() + " ");
    pw.print(WSIConstants.ATTR_VERSION + "=\"" + getVersion() + "\" ");
    pw.println(
      WSIConstants.ATTR_RELEASE_DATE + "=\"" + getReleaseDate() + "\">");

    pw.print("    <" + nsName + WSIConstants.ELEM_IMPLEMENTER + " ");
    pw.print(WSIConstants.ATTR_NAME + "=\"" + getImplementer() + "\" ");
    pw.println(WSIConstants.ATTR_LOCATION + "=\"" + getLocation() + "\"/>");

    // Environment
    pw.print(toolEnvironment.toXMLString(namespaceName));

    return sw.toString();
  }

  /**
   * Return the end XML string representation of this object.
   * @param namespaceName  the namespace prefix.
   * @return the end XML string representation of this object. 
   */
  public String getEndXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if (!nsName.equals(""))
      nsName += ":";

    pw.println("  </" + nsName + getName().toLowerCase() + ">");

    return sw.toString();
  }

  /**
   * Get values from the tools.properties file.
   */
  protected void getProperties()
  {
    //Properties props = new Properties();
    ResourceBundle resourceBundle;

    try
    {
      // Try to load properties from wsi.properties
      //InputStream is = Utils.getInputStream(PROP_FILENAME);

      // Load properties from file
      //props.load(is);

      // Get resource
      resourceBundle = ResourceBundle.getBundle(PROP_FILENAME);

      // Get property values
      this.version =
        getProperty(resourceBundle, PROP_TOOL_VERSION, DEF_TOOL_VERSION);
      this.releaseDate =
        getProperty(
          resourceBundle,
          PROP_TOOL_RELEASE_DATE,
          DEF_TOOL_RELEASE_DATE);
      this.implementer =
        getProperty(
          resourceBundle,
          PROP_TOOL_IMPLEMENTER,
          DEF_TOOL_IMPLEMENTER);
      this.location =
        getProperty(resourceBundle, PROP_TOOL_LOCATION, DEF_TOOL_LOCATION);
    }

    catch (MissingResourceException mre)
    {
      // DEBUG:
      //System.out.println("Exception loading properties:  " + e.toString());

      // Set default property values
      this.version = DEF_TOOL_VERSION;
      this.releaseDate = DEF_TOOL_RELEASE_DATE;
      this.implementer = DEF_TOOL_IMPLEMENTER;
      this.location = DEF_TOOL_LOCATION;
    }
  }

  /**
   * Get property.
   * @param resourceBundle  a resource bundle.
   * @param key             a key.
   * @param defaultValue    a default value.
   * @return property. If not found return default value.
   */
  protected String getProperty(
    ResourceBundle resourceBundle,
    String key,
    String defaultValue)
  {
    String value;

    if ((value = resourceBundle.getString(key)) == null)
      value = defaultValue;

    return value;
  }

  /**
   * Create the tools.properties file from the command line.
   * The input arguements are:
   * <ol>
   * <li>Filename for the properties file to create
   * <li>Version number
   * <li>
   * </ol>
   * @param args  the arguments for main.
   */
  public static void main(String[] args)
  {
    int statusCode = 0;
    String filename = PROP_FILENAME;
    String version = DEF_TOOL_VERSION;
    String implementer = DEF_TOOL_IMPLEMENTER;
    String location = DEF_TOOL_LOCATION;

    // Set the file name
    if (args.length >= 1)
      filename = args[0];

    if (args.length >= 2)
      version = args[1];

    if (args.length >= 3)
      implementer = args[2];

    if (args.length >= 4)
      location = args[3];

    try
    {
      // Create file writer
      FileWriter fileWriter = new FileWriter(filename);

      // Create print writer
      PrintWriter printWriter = new PrintWriter(fileWriter);

      // Write out contents of properties file
      printWriter.println(COMMENTS);
      printWriter.println(PROP_TOOL_VERSION + "=" + version);
      printWriter.println(PROP_TOOL_RELEASE_DATE + "=" + Utils.getDate());
      printWriter.println(PROP_TOOL_IMPLEMENTER + "=" + implementer);
      printWriter.println(PROP_TOOL_LOCATION + "=" + location);

      // Close file writer
      fileWriter.close();

      // Done
      System.out.println("File [" + filename + "] created.");
    }

    catch (java.io.IOException ioe)
    {
      statusCode = 1;
      System.out.println("EXCEPTION: " + ioe.toString());
      ioe.printStackTrace();
    }

    // Exit
    System.exit(statusCode);
  }

  private static final String COMMENTS = "";
}
