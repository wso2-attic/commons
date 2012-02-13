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

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.uddi4j.transport.TransportFactory;

/**
 * The WSI properties can be defined in one of three locations.  
 *
 *   1. The wsi.properties file which can be located anywhere in the classpath.
 *   2. The system property settings.
 *   3. The thread local property settings.
 * 
 * The properties are set based on this ordering.  For example, the properties 
 * from the wsi.properties file will be replaced by properties from the system 
 * property settings, which will be replaced by the thread local property settings.
 *
 * The properties that are used to define the implementation classes can be
 * specified as system properties or as properties that are set in a thread 
 * local variable.  System properties should be used if only one implementation
 * is needed per JVM.  If two or more implementations are needed per JVM, then
 * the properties should be set using the thread local variable.
 *
 * @version 1.0.1
 * @author: Peter Brittenham
 */
public final class WSIProperties
{
  /**
   * Properties object.
   */
  protected static Properties wsiProperties = new Properties();

  /**
   * Default ProfileValidatorFactory class name.
   */
  public static final String DEF_VALIDATOR_FACTORY =
    "org.eclipse.wst.wsi.internal.core.profile.validator.impl.ProfileValidatorFactoryImpl";

  /**
   * Property that contains ProfileValidatorFactory class name.
   */
  public static final String PROP_VALIDATOR_FACTORY =
    "wsi.profile.validator.factory";

  /**
   * Default document factory class name.
   */
  public static final String DEF_DOCUMENT_FACTORY =
    "org.eclipse.wst.wsi.internal.core.document.impl.DocumentFactoryImpl";

  /**
   * Property that contains document factory class name.
   */
  public static final String PROP_DOCUMENT_FACTORY = "wsi.document.factory";

  /**
   * Default JAXP XML parser document factory builder.
   */
  public static final String DEF_JAXP_DOCUMENT_FACTORY =
    "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl";

  /**
   * Property that contains JAXP XML parser document factory builder.
   */
  public static final String PROP_JAXP_DOCUMENT_FACTORY =
    "javax.xml.parsers.DocumentBuilderFactory";

  /**
   * Default WSDL schema location.
   */
  public static final String DEF_WSDL_SCHEMA = "http://schemas.xmlsoap.org/wsdl/";

  /**
   * Property that contains WSDL schema location.
   */
  public static final String PROP_WSDL_SCHEMA = "wsi.analyzer.wsdl.schema";

  /**
   * Default WSDL SOAP schema location.
   */
  public static final String DEF_WSDL_SOAP_SCHEMA =
    "http://schemas.xmlsoap.org/wsdl/soap/";

  /**
   * Property that contains WSDL SOAP schema location.
   */
  public static final String PROP_WSDL_SOAP_SCHEMA =
    "wsi.analyzer.wsdlsoap.schema";

  /**
   * Default WSDL MIME schema location.
   */
  public static final String DEF_WSDL_MIME_SCHEMA =
    "http://schemas.xmlsoap.org/wsdl/mime/";

  /**
   * Property that contains WSDL SOAP schema location.
   */
  public static final String PROP_WSDL_MIME_SCHEMA =
    "wsi.analyzer.wsdlmime.schema";

  /**
   * Default SOAP schema location.
   */
  public static final String DEF_SOAP_SCHEMA =
    "http://schemas.xmlsoap.org/soap/envelope/";

  /**
   * Property that contains SOAP schema location.
   */
  public static final String PROP_SOAP_SCHEMA = "wsi.analyzer.soap.schema";

  /**
   * Default SOAP schema location.
   */
  public static final String DEF_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema.xsd";

  /**
   * Property that contains SOAP schema location.
   */
  public static final String PROP_XML_SCHEMA = "wsi.analyzer.xmlschema.schema";

  /**
   * Property which contains WSI_HOME value.
   */
  public static final String PROP_WSI_HOME = "wsi.home";

  /**
   * Property file name.
   */
  protected static final String PROP_FILENAME = "org.eclipse.wst.wsi.internal.core.util.wsi";

  /**
  * Thread local variable.
  */
  private static ThreadLocal threadLocal = new ThreadLocal();

  // Load profiles file
  static {
    loadPropertiesFile();
  }

  /**
   * Load properties from wsi.properties file.
   */
  private static void loadPropertiesFile()
  {
    try
    {
      // Try to load properties from wsi.properties
      //InputStream is = Utils.getInputStream(PROP_FILENAME);

      // Load properties from file
      //wsiPropertiesFile.load(is);

      ResourceBundle resourceBundle = ResourceBundle.getBundle(PROP_FILENAME);

      String nextKey;
      Enumeration enumeration = resourceBundle.getKeys();
      while (enumeration.hasMoreElements())
      {
        nextKey = (String) enumeration.nextElement();
        wsiProperties.put(nextKey, resourceBundle.getString(nextKey));
      }
    }

    catch (MissingResourceException mre)
    {
      // DEBUG:
      System.err.println(
        "WARNING:  Could not read "
          + PROP_FILENAME
          + ".  "
          + "Verify that it is in the CLASSPATH.");

      // Set up default values
      wsiProperties.put(PROP_VALIDATOR_FACTORY, DEF_VALIDATOR_FACTORY);
      wsiProperties.put(PROP_DOCUMENT_FACTORY, DEF_DOCUMENT_FACTORY);
      wsiProperties.put(PROP_JAXP_DOCUMENT_FACTORY, DEF_JAXP_DOCUMENT_FACTORY);
      wsiProperties.put(
        TransportFactory.PROPERTY_NAME,
        "org.uddi4j.transport.ApacheAxisTransport");
    }
  }

  /** 
   * Do not allow this object to be instantiated.
   */
  private WSIProperties()
  {
  }

  /** 
    * Get properties that were set for this thread only.
    * @return the properties that were set for this thread only.
    *
    * @see #setThreadLocalProperties
    */
  public static Properties getThreadLocalProperties()
  {
    // Return properties
    return (Properties) threadLocal.get();
  }

  /** 
   * Set properties for this thread only.
   * @param props  a Properties object.
   *
   * @see #getThreadLocalProperties
   */
  public static void setThreadLocalProperties(Properties props)
  {
    // Save the properties as thread local variables
    threadLocal.set(props);
  }

  /** 
   * Get the property value given the property name.
   * @param propertyName  a property name.
   * @return the corresponding property value. If not found return null.
   */
  public static String getProperty(String propertyName)
  {
    String propertyValue = null;
    Properties threadLocalProps = null;

    // 1. Look in thread local properties first
    if ((threadLocalProps = getThreadLocalProperties()) != null)
    {
      propertyValue = threadLocalProps.getProperty(propertyName);
    }

    // 2. Next look in system properties 
    if (propertyValue == null)
    {
      propertyValue = System.getProperty(propertyName);
    }

    // 3. Last look in the properties file
    if (propertyValue == null)
    {
      propertyValue = wsiProperties.getProperty(propertyName);
    }

    // Return property value
    return propertyValue;
  }

  /** 
   * Get the property value given the property name.
   * @param propertyName  a property name.
   * @param defaultValue  a default value for the property.
   * @return the corresponding property value. If not found return the default value.
   */
  public static String getProperty(String propertyName, String defaultValue)
  {
    String propertyValue = null;

    // If the propertyValue was not found, then return default value
    if ((propertyValue = getProperty(propertyName)) == null)
    {
      propertyValue = defaultValue;
    }

    // Return property value
    return propertyValue;
  }

  /**
   * This method is used to unit test this class.
   * @param args  the arguments for main.
   */
  public static void main(String[] args)
  {
    try
    {
      // Set property for this thread
      Properties props = new Properties();
      props.setProperty(PROP_VALIDATOR_FACTORY, "main");
      WSIProperties.setThreadLocalProperties(props);

      // Start four threads to verify that the multithreaded use of tread local vars works
      for (int i = 0; i < 5; i++)
      {
        // Create new thread
         (new Thread(new ThreadTest("test" + i))).start();

        try
        {
          // Sleep
          Thread.sleep(200);

          // Display properties, which should be main
          System.out.println(
            "PROP_VALIDATOR_FACTORY:  "
              + WSIProperties.getProperty(PROP_VALIDATOR_FACTORY));
        }

        catch (Exception e)
        {
        }
      }
    }

    catch (Exception e)
    {
      // e.printStackTrace();
    }
  }

  /**
   * Inner class used for unit test.
   */
  static private class ThreadTest extends Thread
  {
    String name;

    ThreadTest(String name)
    {
      this.name = name;
    }

    public void run()
    {
      Properties props = new Properties();
      props.setProperty(PROP_VALIDATOR_FACTORY, name);
      WSIProperties.setThreadLocalProperties(props);
      System.out.println(
        "PROP_VALIDATOR_FACTORY:  "
          + WSIProperties.getProperty(PROP_VALIDATOR_FACTORY));
    }
  }
}
