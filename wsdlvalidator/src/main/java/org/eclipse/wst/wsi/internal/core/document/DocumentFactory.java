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
package org.eclipse.wst.wsi.internal.core.document;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfigReader;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.log.LogReader;
import org.eclipse.wst.wsi.internal.core.log.LogWriter;
import org.eclipse.wst.wsi.internal.core.monitor.config.MonitorConfig;
import org.eclipse.wst.wsi.internal.core.monitor.config.MonitorConfigReader;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertionsReader;
import org.eclipse.wst.wsi.internal.core.report.Report;
import org.eclipse.wst.wsi.internal.core.report.ReportWriter;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.util.WSIProperties;

/**
 * This abstract class defines the factory interface that can be used to 
 * read and write all of the Conformance XML documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public abstract class DocumentFactory
{
  /**
   * Get a new instance of a DocumentFactory.
   * @return a new instance of a DocumentFactory.
   * @throws WSIException if problems occur during creation.
   */
  public static DocumentFactory newInstance() throws WSIException
  {
    // Return instance of factory
    return newInstance(getFactoryClassName());
  }

  /**
   * Get a new instance of a DocumentFactory using the specified
   * factory class name.
   * @param factoryClassName factory class name.
   * @return a new instance of a DocumentFactory.
   * @throws WSIException if problems occur during creation.
   */
  public static DocumentFactory newInstance(String factoryClassName)
    throws WSIException
  {
    DocumentFactory documentFactory = null;

    // If a factory class name was specified, then create object
    if (factoryClassName != null)
    {
      try
      {
        // Get class object
        Class factoryClass = Class.forName(factoryClassName);

        // Create new factory
        documentFactory = (DocumentFactory) factoryClass.newInstance();
      }

      catch (Exception e)
      {
        throw new WSIException(
          "Could not instantiate document factory class: "
            + factoryClassName
            + ".",
          e);
      }
    }
    else
    {
      throw new WSIException("A WSIDocumentFactory implementation class was not found.");
    }

    // Return factory
    return documentFactory;
  }

  // DOCUMENT TYPES

  /**
   * Create log file.
   * @return log file.
   */
  public abstract Log newLog();

  /**
   * Create monitor config.
   * @return newly created monitor config.
   */
  public abstract MonitorConfig newMonitorConfig();

  /**
   * Create analyzer config.
   * @return newly created analyzer config.
   */
  public abstract AnalyzerConfig newAnalyzerConfig();

  /**
   * Create profile assertions.
   * @return newly created profile assertions.
   */
  public abstract ProfileAssertions newProfileAssertions();

  /**
   * Create report.
   * @return newly created report.
   */
  public abstract Report newReport();

  // READERS

  /**
   * Create profile assertions reader.
   * @return newly created profile assertions reader.
   */
  public abstract ProfileAssertionsReader newProfileAssertionsReader();

  /**
   * Create monitor config reader.
   * @return newly created monitor config reader.
   */
  public abstract MonitorConfigReader newMonitorConfigReader();

  /**
   * Create analyzer config reader.
   * @return newly created analyzer config reader.
   */
  public abstract AnalyzerConfigReader newAnalyzerConfigReader();

  /**
   * Create log reader.
   * @return newly created log reader.
   */
  public abstract LogReader newLogReader();

  // ADD: Add other readers

  // WRITERS

  /**
   * Create report writer.
   * @return newly created report writer.
   */
  public abstract ReportWriter newReportWriter();

  /**
   * Create log writer.
   * @return newly created log writer.
   */
  public abstract LogWriter newLogWriter();

  /**
   * Create a reporter.
   * @return newly created reporter.
   */
  public abstract Reporter newReporter(Report report, ReportWriter reportWriter);

  // ADD: Add other writers

  /**
   * Find the factory class name which can be specified as a Java property.
   */
  private static String getFactoryClassName()
  {
    // Get property value
    return WSIProperties.getProperty(
      WSIProperties.PROP_DOCUMENT_FACTORY,
      WSIProperties.DEF_DOCUMENT_FACTORY);
  }
}
