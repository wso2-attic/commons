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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.ToolInfo;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.document.DocumentFactory;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.profile.validator.ProfileValidatorFactory;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.util.MessageList;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;

/**
 * The Analyzer will process all Profile conformance functions.
 * This class should be sub-classed for each profile.
 *
 * @version 1.0.1
 * @author Jim Clune
 * @author Peter Brittenham
 */
public abstract class Analyzer
{
  /**
   * Message list.
   */
  protected MessageList messageList = null;

  protected final static String RESOURCE_BUNDLE_NAME =
    "org.eclipse.wst.wsi.internal.core.analyzer.Analyzer";

  public final static String USAGE_MESSAGE =
    "Usage: Analyzer -config <configFilename>";

  protected Reporter reporter = null;
  protected AnalyzerContext analyzerContext = null;
  protected CandidateInfo candidateInfo = null;

  /**
   * Analyzer tool information.
   */
  protected ToolInfo toolInfo = null;

  /**
   * Profile validator factory.
   */
  protected ProfileValidatorFactory factory = null;

  /**
   * Profile validator factory.
   */
  protected DocumentFactory documentFactory = null;

  /**
   * Analyzer config file list.
   */
  protected List analyzerConfigList = new Vector();
  protected int analyzerConfigIndex = 0;

  /**
   * Profile assertions.
   */
  protected ProfileAssertions profileAssertions = null;

  /**
   * Log file.
   */
  protected Log log = null;

  /**
   * Create an instance of the analyzer tool.
   * @param args      command line arguments.
   * @param toolInfo  analyzer tool information.
   * @throws WSIException if problems creating the instance of the analyzer tool.
   */
  public Analyzer(String[] args, ToolInfo toolInfo) throws WSIException
  {
    init(toolInfo);

    // Get new config object   
    this.analyzerConfigList.add(
      analyzerConfigIndex,
      documentFactory.newAnalyzerConfig());
    getAnalyzerConfig().init(messageList);

    // Parse command line arguments
    getAnalyzerConfig().parseArgs(args, true);

    // Display copyright and options that were specified
    TestUtils.printToolInfo(toolInfo);
    System.out.println(getAnalyzerConfig().toString());

    // Display message
    printMessage(
      "progress01",
      null,
      "Please wait while the specified artifacts are analyzed...");
  }

  /**
   * Create an instance of the analyzer tool.
   * @param args      command line arguments.
   * @param toolInfo  analyzer tool information.
   * @param validate  flag for config options validation. 
   * @throws WSIException if problems creating the instance of the analyzer tool.
   */
  public Analyzer(String[] args, ToolInfo toolInfo, boolean validate) throws WSIException
  {
    init(toolInfo);

    // Get new config object   
    this.analyzerConfigList.add(
        analyzerConfigIndex,
        documentFactory.newAnalyzerConfig());
    getAnalyzerConfig().init(messageList);

    // Parse command line arguments
    getAnalyzerConfig().parseArgs(args, validate);

    // Display copyright and options that were specified
    TestUtils.printToolInfo(toolInfo);
    System.out.println(getAnalyzerConfig().toString());

    // Display message
    printMessage(
        "progress01",
        null,
    "Please wait while the specified artifacts are analyzed...");
  }

  /**
   * Create an instance of the analyzer tool.
   * @param analyzerConfigList  analyzer config file list.
   * @param toolInfo            analyzer tool information.
   * @throws WSIException if problems creating the instance of the analyzer tool.
   */
  public Analyzer(List analyzerConfigList, ToolInfo toolInfo)
    throws WSIException
  {
    init(toolInfo);

    this.analyzerConfigList = analyzerConfigList;
  }

  /**
   * Common initialization.
   * @param toolInfo  analyzer tool information.
   * @throws WSIException if problems occur during common initialization.
   */
  protected void init(ToolInfo info) throws WSIException
  {
    this.toolInfo = info;

    // Create message list
    messageList = new MessageList(RESOURCE_BUNDLE_NAME);

    // Create profile validator factory
    factory = ProfileValidatorFactory.newInstance();

    // Create document factory
    documentFactory = DocumentFactory.newInstance();
  }

  /**
   * Process all conformance validation functions.
   * @return status code.
   * @throws WSIException if problems occur during validation.
   */
  public int validateAll() throws WSIException
  {
    int statusCode = 0;
    int tempStatusCode;

    for (int index = 0; index < analyzerConfigList.size(); index++)
    {
      // DEBUG:
      //System.out.println("index: " + index);

      // Set current analyzer config index 
      setAnalyzerConfig(index);

      if ((tempStatusCode = validateConformance()) > statusCode)
        statusCode = tempStatusCode;
    }

    return statusCode;
  }

  /**
   * Process all conformance validation functions.
   * @return status code.
   * @throws WSIException if problems occur during conformance validation.
   */
  public abstract int validateConformance() throws WSIException;

  /**
   * Get tool information.
   * @return the tool information.
   */
  public ToolInfo getToolInfo()
  
  {
    return this.toolInfo;
  }

  /**
   * Set current analyzer configuration.
   * @param index  urrent analyzer configuration.
   * @see #getAnalyzerConfig
   */
  protected void setAnalyzerConfig(int index)
  {
    this.analyzerConfigIndex = index;
  }

  protected int getAnalyzerConfigIndex()
  {
    return this.analyzerConfigIndex;
  }

  /**
   * Get analyzer configuration information.
   * @return analyzer configuration information.
   * @see #setAnalyzerConfig
   */
  public AnalyzerConfig getAnalyzerConfig()
  {
    return (AnalyzerConfig) this.analyzerConfigList.get(analyzerConfigIndex);
  }

  /**
   * Print a message from the resource bundle.
   * @param key             a key.
   * @param messageData     message data.
   * @param defaultMessage  a default message.
   */
  public void printMessage(
    String key,
    String messageData,
    String defaultMessage)
  {
    messageList.printMessage(key, messageData, defaultMessage);
  }

  /**
   * Print message.
   * @param key             a key.
   * @param messageData     message data.
   * @param defaultMessage  a default message.
   */
  public static void staticPrintMessage(
    String key,
    String messageData,
    String defaultMessage)
  {
    MessageList.printMessage(
      RESOURCE_BUNDLE_NAME,
      key,
      messageData,
      defaultMessage);
  }

  /**
   * Return XML string representation of this object.
   * @param namespaceName  a namespace prefix.
   * @return XML string representation of this object.
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    // Start     
    pw.print(toolInfo.getStartXMLString(namespaceName));

    // Config
    pw.print(
      getAnalyzerConfig().toXMLString(
        WSIConstants.NS_NAME_WSI_ANALYZER_CONFIG));

    // End
    pw.println(toolInfo.getEndXMLString(namespaceName));

    return sw.toString();
  }

  /**
   * Print a message.
   * @param message a message.
   */
  public void printMessage(String message)
  {
    if (getAnalyzerConfig().getVerboseOption())
    {
      System.err.println(message);
    }
  }
}
