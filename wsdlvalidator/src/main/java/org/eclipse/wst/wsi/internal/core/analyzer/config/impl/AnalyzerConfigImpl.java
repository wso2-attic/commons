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

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.Analyzer;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfigReader;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultType;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultsOption;
import org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLReference;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.common.impl.AddStyleSheetImpl;
import org.eclipse.wst.wsi.internal.core.util.MessageList;
import org.eclipse.wst.wsi.internal.core.util.Utils;

/**
 * This class is an implementation of the analyzer configuration file interface.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class AnalyzerConfigImpl implements AnalyzerConfig
{
  /**
   * Message list reference.
   */
  protected MessageList messageList = null;

  /**
   * Analyzer configuration file URI.
   */
  protected String configURI = null;

  /**
   * Optional description.
   */
  protected String description = null;

  /**
   * Verbose option.
   */
  protected boolean verbose = false;

  /**
   * Assertion results option.
   */
  protected AssertionResultsOption assertionResultsOption =
    new AssertionResultsOptionImpl();

  /**
   * Report location.
   */
  protected String reportURI = WSIConstants.DEFAULT_REPORT_URI;

  /**
   * Replace report indicator.
   */
  protected boolean replaceReport = false;

  /**
   * Add style sheet.
   */
  protected AddStyleSheet addStyleSheet;

  /**
   * Profile test assertions document location.
   */
  protected String testAssertionsDocumentURI =
    WSIConstants.DEFAULT_TEST_ASSERTIONS_DOCUMENT_URI;

  /**
   * Message log location.
   */
  protected String logURI = null;

  /**
   * Message correlation type.
   */
  protected String correlationType =
    WSIConstants.ATTRVAL_CORRELATION_TYPE_OPERATION;

  /**
   * WSDL reference.
   */
  protected WSDLReference wsdlReference = null;

  /**
   * UDDI reference.
   */
  protected UDDIReference uddiReference = null;

  /**
   * Default constructor.
   */
  public AnalyzerConfigImpl()
  {
  }

  /**
   * Constructor with all settings.
   * @param description                an optional description.
   * @param verbose                    the verbose option.
   * @param assertionResultsOption     an assertion results option.
   * @param reportURI                  report location.
   * @param replaceReport              a replace report indicator.
   * @param addStyleSheet              add style sheet.
   * @param testAssertionsDocumentURI  profile test assertions document location.
   * @param logURI                     message log location.
   * @param correlationType            message correlation type.
   * @param wsdlReference              a WSDL reference..
   * @param uddiReference              a UUDDI reference.
   */
  public AnalyzerConfigImpl(
    String description,
    boolean verbose,
    AssertionResultsOption assertionResultsOption,
    String reportURI,
    boolean replaceReport,
    AddStyleSheet addStyleSheet,
    String testAssertionsDocumentURI,
    String logURI,
    String correlationType,
    WSDLReference wsdlReference,
    UDDIReference uddiReference)
  {
    this.description = description;
    this.verbose = verbose;
    this.assertionResultsOption = assertionResultsOption;
    this.reportURI = reportURI;
    this.replaceReport = replaceReport;
    this.addStyleSheet = addStyleSheet;
    this.testAssertionsDocumentURI = testAssertionsDocumentURI;
    this.logURI = logURI;
    this.correlationType = correlationType;
    this.wsdlReference = wsdlReference;
    this.uddiReference = uddiReference;
  }

  /**
   * Initialize analyzer config.
   */
  public void init(MessageList messageList)
  {
    this.messageList = messageList;
  }

  /**
   * Get optional description.
   * @see #setDescription
   */
  public String getDescription()
  {
    return this.description;
  }

  /**
   * Set optional description.
   * @see #getDescription
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * @see #setVerboseOption
   */
  public boolean getVerboseOption()
  {
    return this.verbose;
  }

  /**
   * @see #getVerboseOption
   */
  public void setVerboseOption(boolean verbose)
  {
    this.verbose = verbose;
  }

  /**
   * Get assertion results option.
   * @see #setAssertionResultsOption
   */
  public AssertionResultsOption getAssertionResultsOption()
  {
    return this.assertionResultsOption;
  }

  /**
   * Set assertion results option.
   * @see #getAssertionResultsOption
   */
  public void setAssertionResultsOption(AssertionResultsOption assertionResultsOption)
  {
    this.assertionResultsOption = assertionResultsOption;
  }

  /**
   * @see #setReportLocation
   */
  public String getReportLocation()
  {
    return this.reportURI;
  }

  /**
   * @see #getReportLocation
   */
  public void setReportLocation(String reportURI)
  {
    this.reportURI = reportURI;
  }

  /**
   * Get replace report file option.
   * @see #setReplaceReport
   */
  public boolean getReplaceReport()
  {
    return this.replaceReport;
  }

  /**
   * Set report file location.
   * @see #getReplaceReport
   */
  public void setReplaceReport(boolean replaceReport)
  {
    this.replaceReport = replaceReport;
  }

  /**
   * Get add style sheet.
   * @see #setAddStyleSheet
   */
  public AddStyleSheet getAddStyleSheet()
  {
    return this.addStyleSheet;
  }

  /**
   * Set replace report file option.
   * @see #getAddStyleSheet
   */
  public void setAddStyleSheet(AddStyleSheet addStyleSheet)
  {
    this.addStyleSheet = addStyleSheet;
  }

  /**
   * @see #setTestAssertionDocumentLocation
   */
  public String getTestAssertionsDocumentLocation()
  {
    return this.testAssertionsDocumentURI;
  }

  /**
   * @see #getTestAssertionDocumentLocation
   */
  public void setTestAssertionsDocumentLocation(String testAssertionsDocumentURI)
  {
    this.testAssertionsDocumentURI = testAssertionsDocumentURI;
  }

  /**
   * @see #setLogLocation
   */
  public String getLogLocation()
  {
    return logURI;
  }

  /**
   * @see #getLogLocation
   */
  public void setLogLocation(String logURI)
  {
    this.logURI = logURI;
  }

  public boolean isLogSet()
  {
    return (logURI != null);
  }

  /**
   * Get correlation type.
   * @see #setCorrelationType
   */
  public String getCorrelationType()
  {
    return this.correlationType;
  }

  /**
   * Set correlation type.
   * @see #getCorrelationType
   * 
   */
  public void setCorrelationType(String correlationType)
  {
    this.correlationType = correlationType;
  }

  /**
   * Is WSDL reference set.
   */
  public boolean isWSDLReferenceSet()
  {
    return (this.wsdlReference == null ? false : true);
  }

  /**
   * Get WSDL reference.
   * @see #setWSDLReference
   */
  public WSDLReference getWSDLReference()
  {
    return this.wsdlReference;
  }

  /**
   * Set WSDL reference.
   * @see #getWSDLReference
   */
  public void setWSDLReference(WSDLReference wsdlReference)
  {
    this.wsdlReference = wsdlReference;
  }

  /**
   * Get WSDL element.
   * 
   */
  public WSDLElement getWSDLElement()
  {
    return (this.wsdlReference == null)
      ? null
      : this.wsdlReference.getWSDLElement();
  }

  /**
   * Get service location.
   */
  public String getServiceLocation()
  {
    return (this.wsdlReference == null)
      ? null
      : this.wsdlReference.getServiceLocation();
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig#getWSDLLocation()
   */
  public String getWSDLLocation()
  {
    return (this.wsdlReference == null)
      ? null
      : this.wsdlReference.getWSDLLocation();
  }

  /**
   * @see org.wsi.test.analyzer.config.AnalyzerConfig#isUddiReferenceSet)
   */
  public boolean isUDDIReferenceSet()
  {
    return (this.uddiReference == null ? false : true);
  }

  /**
   * Get UDDI reference.
   * @see #setUDDIReference
   */
  public UDDIReference getUDDIReference()
  {
    return this.uddiReference;
  }

  /**
   * Set UDDI reference.
   * @see #getUDDIReference
   */
  public void setUDDIReference(UDDIReference uddiReference)
  {
    this.uddiReference = uddiReference;
  }

  /**
   * @see #setLocation
   */
  public String getLocation()
  {
    return configURI;
  }

  /**
   * @see getLocation
   */
  public void setLocation(String configURI)
  {
    this.configURI = configURI;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.WSIDocument#toXMLString()
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Start element
    pw.println("    <" + nsName + ELEM_NAME + ">");

    // Verbose option
    pw.print("      <" + nsName + WSIConstants.ELEM_VERBOSE + ">");
    pw.print(getVerboseOption());
    pw.println("</" + nsName + WSIConstants.ELEM_VERBOSE + ">");

    // Get assertion results option
    pw.print(assertionResultsOption.toXMLString(nsName));

    // report file
    pw.print("        <" + nsName + WSIConstants.ELEM_REPORT_FILE + " ");
    pw.print(WSIConstants.ATTR_REPLACE + "=\"" + getReplaceReport() + "\" ");
    pw.println(
      WSIConstants.ATTR_LOCATION + "=\"" + getReportLocation() + "\">");
    pw.print(getAddStyleSheet().toXMLString(namespaceName));
    pw.println("        </" + nsName + WSIConstants.ELEM_REPORT_FILE + ">");

    // test assertion file
    pw.print(
      "        <" + nsName + WSIConstants.ELEM_TEST_ASSERTIONS_FILE + ">");
    pw.print(getTestAssertionsDocumentLocation());
    pw.println("</" + nsName + WSIConstants.ELEM_TEST_ASSERTIONS_FILE + ">");

    // log file
    pw.print("        <" + nsName + WSIConstants.ELEM_LOG_FILE);
    pw.print(
      " "
        + WSIConstants.ATTR_CORRELATION_TYPE
        + "=\""
        + this.correlationType
        + "\">");
    pw.print(getLogLocation());
    pw.println("</" + nsName + WSIConstants.ELEM_LOG_FILE + ">");

    // WSDL reference 
    if (this.wsdlReference != null)
    {
      pw.print(getWSDLReference().toXMLString(nsName));
    }

    // UDDI Reference
    if (this.uddiReference != null)
    {
      pw.print(getUDDIReference().toXMLString(nsName));
    }

    // End element
    pw.println("    </" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    // verbose option
    pw.println("  verbose .................... " + this.verbose);

    // assertionResults options
    pw.print(this.assertionResultsOption.toString());

    // reportFile options
    pw.println("  Report File:");
    pw.println("    replace .................. " + this.replaceReport);
    pw.println("    location ................. " + this.reportURI);
    if (this.addStyleSheet != null)
    {
      pw.println("    Style Sheet:");
      if (this.addStyleSheet.getHref() != null)
        pw.println(
          "      href ................... " + this.addStyleSheet.getHref());
      if (this.addStyleSheet.getType() != null)
        pw.println(
          "      type ................... " + this.addStyleSheet.getType());
      if (this.addStyleSheet.getTitle() != null)
        pw.println(
          "      title .................. " + this.addStyleSheet.getTitle());
      if (this.addStyleSheet.getMedia() != null)
        pw.println(
          "      media .................. " + this.addStyleSheet.getMedia());
      if (this.addStyleSheet.getCharset() != null)
        pw.println(
          "      charset ................ " + this.addStyleSheet.getCharset());
      if (this.addStyleSheet.getAlternate() != null)
        pw.println(
          "      alternate .............. "
            + this.addStyleSheet.getAlternate());
    }

    // testAssertionsFile parameter
    pw.println(
      "  testAssertionsFile ......... " + this.testAssertionsDocumentURI);

    // logFile options
    if (this.logURI != null)
    {
      pw.println("  Message Log File:");
      pw.println("    location ................. " + this.logURI);
      pw.println("    correlationType .......... " + this.correlationType);
    }

    // wsdlReference options   
    if (this.isWSDLReferenceSet())
    {
      pw.print(this.wsdlReference.toString());
    }

    // uddiReference options
    if (this.isUDDIReferenceSet())
    {
      pw.print(this.uddiReference.toString());
    }

    // description parameter
    if (this.description != null)
    {
      pw.println("  description ................ " + this.description);
    }

    return sw.toString();
  }

  /**
   * Set config values from another analyzer config object.
   */
  private void setConfig(AnalyzerConfig analyzerConfig)
  {
    this.configURI = analyzerConfig.getLocation();
    this.description = analyzerConfig.getDescription();
    this.verbose = analyzerConfig.getVerboseOption();
    this.assertionResultsOption = analyzerConfig.getAssertionResultsOption();
    this.reportURI = analyzerConfig.getReportLocation();
    this.replaceReport = analyzerConfig.getReplaceReport();
    this.logURI = analyzerConfig.getLogLocation();
    this.correlationType = analyzerConfig.getCorrelationType();
    this.testAssertionsDocumentURI =
      analyzerConfig.getTestAssertionsDocumentLocation();
    this.addStyleSheet = analyzerConfig.getAddStyleSheet();
    this.wsdlReference = analyzerConfig.getWSDLReference();
    this.uddiReference = analyzerConfig.getUDDIReference();
  }

  /**
   * Parse command line arguments.
   */
  public void parseArgs(String[] args, boolean validate) throws WSIException
  {
    WSDLReference wsdlReference = null;
    UDDIReference uddiReference = null;

    // Get new config reader
    AnalyzerConfigReader analyzerConfigReader = new AnalyzerConfigReaderImpl();
    analyzerConfigReader.init(this.messageList);

    // Analyzer config object which will be merged after all of the input parms are processed
    AnalyzerConfig analyzerConfigFromArgs = null;

    // Assertion results option
    AssertionResultsOption assertionResultsOption = null;

    // If no input arguments, then throw exception
    if (args.length == 0)
    {
      // ADD: 
      throw new IllegalArgumentException(
        getMessage("usage01", Analyzer.USAGE_MESSAGE));
    }

    // Parse the command line arguments to locate the config file option (if it was specified)
    for (int argCount = 0; argCount < args.length; argCount++)
    {
      // -config
      if ((args[argCount].equalsIgnoreCase("-config"))
        || (args[argCount].equals("-c")))
      {
        argCount++;
        analyzerConfigFromArgs =
          analyzerConfigReader.readAnalyzerConfig(
            getOptionValue(args, argCount));
      }
    }

    // If config file was not specified, then create analyzer config object
    if (analyzerConfigFromArgs == null)
    {
      analyzerConfigFromArgs = new AnalyzerConfigImpl();
      analyzerConfigFromArgs.init(this.messageList);
    }

    // Get assertion results option
    if ((assertionResultsOption =
      analyzerConfigFromArgs.getAssertionResultsOption())
      == null)
    {
      assertionResultsOption = new AssertionResultsOptionImpl();
      analyzerConfigFromArgs.setAssertionResultsOption(assertionResultsOption);
    }

    // Parse all of the command line arguments
    for (int argCount = 0; argCount < args.length; argCount++)
    {
      // -config
      if ((args[argCount].equalsIgnoreCase("-config"))
        || (args[argCount].equals("-c")))
      {
        // Skip this option since it was already processed
        argCount++;
      }

      // -verbose
      else if (
        (args[argCount].equalsIgnoreCase("-verbose"))
          || (args[argCount].equals("-v")))
      {
        argCount++;
        analyzerConfigFromArgs.setVerboseOption(
          Boolean.valueOf(getOptionValue(args, argCount)).booleanValue());
      }

      // -assertionResults
      else if (
        (args[argCount].equalsIgnoreCase("-assertionResults"))
          || (args[argCount].equals("-a")))
      {
        argCount++;
        assertionResultsOption.setAssertionResultType(
          AssertionResultType.newInstance(getOptionValue(args, argCount)));
      }

      // -messageEntry
      else if (
        (args[argCount].equalsIgnoreCase("-messageEntry"))
          || (args[argCount].equals("-M")))
      {
        argCount++;
        assertionResultsOption.setShowMessageEntry(
          Boolean.valueOf(getOptionValue(args, argCount)).booleanValue());
      }

      // -assertionDescription
      else if (
        (args[argCount].equalsIgnoreCase("-assertionDescription"))
          || (args[argCount].equals("-A")))
      {
        argCount++;
        assertionResultsOption.setShowAssertionDescription(
          Boolean.valueOf(args[argCount]).booleanValue());
      }

      // -failureMessage
      else if (
        (args[argCount].equalsIgnoreCase("-failureMessage"))
          || (args[argCount].equals("-F")))
      {
        argCount++;
        assertionResultsOption.setShowFailureMessage(
          Boolean.valueOf(getOptionValue(args, argCount)).booleanValue());
      }

      // -failureDetail
      else if (
        (args[argCount].equalsIgnoreCase("-failureDetail"))
          || (args[argCount].equals("-D")))
      {
        argCount++;
        assertionResultsOption.setShowFailureDetail(
          Boolean.valueOf(getOptionValue(args, argCount)).booleanValue());
      }

      // -logFile
      else if (
        (args[argCount].equalsIgnoreCase("-logFile"))
          || (args[argCount].equals("-l")))
      {
        argCount++;
        analyzerConfigFromArgs.setLogLocation(getOptionValue(args, argCount));
      }

      // -testAssertionFile
      else if (
        (args[argCount].equalsIgnoreCase("-testAssertionFile"))
          || (args[argCount].equals("-t")))
      {
        argCount++;
        analyzerConfigFromArgs.setTestAssertionsDocumentLocation(
          getOptionValue(args, argCount));
      }

      // -reportFile
      else if (
        (args[argCount].equalsIgnoreCase("-reportFile"))
          || (args[argCount].equals("-r")))
      {
        argCount++;
        analyzerConfigFromArgs.setReportLocation(
          getOptionValue(args, argCount));
      }

      // -replace
      else if (
        (args[argCount].equalsIgnoreCase("-replace"))
          || (args[argCount].equals("-R")))
      {
        argCount++;
        analyzerConfigFromArgs.setReplaceReport(
          Boolean.valueOf(getOptionValue(args, argCount)).booleanValue());
      }

      // -correlationType
      else if (
        (args[argCount].equalsIgnoreCase("-correlationType"))
          || (args[argCount].equals("-C")))
      {
        argCount++;
        analyzerConfigFromArgs.setCorrelationType(
          getOptionValue(args, argCount));
      }

      // -wsdlElement
      else if (
        (args[argCount].equalsIgnoreCase("-wsdlElement"))
          || (args[argCount].equals("-W")))
      {
        String optionName = args[argCount];
        argCount++;

        WSDLElement wsdlElement = new WSDLElementImpl();
        wsdlElement.setName(getOptionValue(args, argCount, optionName));
        argCount++;
        wsdlElement.setType(getOptionValue(args, argCount, optionName));
        argCount++;
        wsdlElement.setNamespace(getOptionValue(args, argCount, optionName));
        if (!args[argCount].startsWith("-"))
        {
          argCount++;
          wsdlElement.setParentElementName(
            getOptionValue(args, argCount, optionName));
        }

        if (wsdlReference != null)
        {
          wsdlReference.setWSDLElement(wsdlElement);
        }
        else if (uddiReference != null)
        {
          uddiReference.setWSDLElement(wsdlElement);
        }
        else
        {
          throw new IllegalArgumentException(
            getMessage(
              "config10",
              "The -wsdlElement option must appear after the -wsdlURI or -uddiKey options."));
        }

        analyzerConfigFromArgs.setWSDLReference(wsdlReference);
      }

      // -serviceLocation
      else if (
        (args[argCount].equalsIgnoreCase("-serviceLocation"))
          || (args[argCount].equals("-S")))
      {
        argCount++;

        if (wsdlReference != null)
        {
          wsdlReference.setServiceLocation(getOptionValue(args, argCount));
        }
        else if (uddiReference != null)
        {
          uddiReference.setServiceLocation(getOptionValue(args, argCount));
        }
        else
        {
          throw new IllegalArgumentException(
            getMessage(
              "config11",
              "The -serviceLocation option must appear after the -wsdlURI or -uddiKey options."));
        }
      }

      // -wsdlURI
      else if (
        (args[argCount].equalsIgnoreCase("-wsdlURI"))
          || (args[argCount].equals("-W")))
      {
        argCount++;
        if (wsdlReference == null)
          wsdlReference = new WSDLReferenceImpl();
        wsdlReference.setWSDLLocation(getOptionValue(args, argCount));
        analyzerConfigFromArgs.setWSDLReference(wsdlReference);
      }

      // -uddiKeyType
      else if (
        (args[argCount].equalsIgnoreCase("-uddiKeyType"))
          || (args[argCount].equals("-K")))
      {
        argCount++;
        if (uddiReference == null)
          uddiReference = new UDDIReferenceImpl();
        uddiReference.setKeyType(getOptionValue(args, argCount));
        analyzerConfigFromArgs.setUDDIReference(uddiReference);
      }

      // -uddiKey
      else if (
        (args[argCount].equalsIgnoreCase("-uddiKey"))
          || (args[argCount].equals("-k")))
      {
        argCount++;
        if (uddiReference == null)
          uddiReference = new UDDIReferenceImpl();
        uddiReference.setKey(getOptionValue(args, argCount));
        analyzerConfigFromArgs.setUDDIReference(uddiReference);
      }

      // -inquiryURL
      else if (
        (args[argCount].equalsIgnoreCase("-inquiryURL"))
          || (args[argCount].equals("-i")))
      {
        argCount++;
        if (uddiReference == null)
          uddiReference = new UDDIReferenceImpl();
        uddiReference.setInquiryURL(getOptionValue(args, argCount));
        analyzerConfigFromArgs.setUDDIReference(uddiReference);
      }

      // ADD: Need to add support for other options (-config, -binding, etc.)
      else
      {
        throw new IllegalArgumentException(
          getMessage(
            "config12",
            args[argCount],
            "The specified option is not supported:"));
      }
    }

    // If addStyleSheet was not specified, then create a comment version of it
    if (analyzerConfigFromArgs.getAddStyleSheet() == null)
    {
      AddStyleSheet addStyleSheet = new AddStyleSheetImpl();
      addStyleSheet.setHref(WSIConstants.DEFAULT_REPORT_XSL);
      addStyleSheet.setType(WSIConstants.DEFAULT_XSL_TYPE);
      addStyleSheet.setComment(true);
      analyzerConfigFromArgs.setAddStyleSheet(addStyleSheet);
    }

    // Merge config info into this object
    setConfig(analyzerConfigFromArgs);

    if (validate)
    {  
      // Verify that all required options were specified and 
      // that the specified ones were correct
      checkConfigOptions();
    }
  }

  /**
   * Verify config options.
   */
  private void checkConfigOptions() throws WSIException
  {
    String fileLocation;

    // If wsdlReference and uddiReference specified, then throw exception
    if ((this.isWSDLReferenceSet()) && (this.isUDDIReferenceSet()))
    {
      throw new IllegalArgumentException(
        getMessage(
          "config04",
          "Can not specify both the WSDL reference and UDDI reference options"));
    }

    // Check if test assertion file exists
    if ((fileLocation = this.getTestAssertionsDocumentLocation()) == null)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config16",
          "Test assertion document location must be specified."));
    }

    else if (!Utils.fileExists(fileLocation))
    {
      throw new IllegalArgumentException(
        getMessage(
          "config14",
          fileLocation,
          "Could not find test assertion document.  Specified file location:"));
    }

    // Check if log file exists
    if ((fileLocation = this.getLogLocation()) != null)
    {
      if (!Utils.fileExists(fileLocation))
        throw new IllegalArgumentException(
          messageList.getMessage(
            "config02",
            fileLocation,
            "Could not find log file:"));
    }

    // Check if WSDL file exists
    if ((this.getWSDLReference() != null)
      && (fileLocation = this.getWSDLReference().getWSDLLocation()) != null)
    {
      if (!Utils.fileExists(fileLocation))
        throw new IllegalArgumentException(
          messageList.getMessage(
            "config03",
            fileLocation,
            "Could not find WSDL file:"));
    }

    File file = null;
    try
    {
      // Get file object for report file
      file = new File(this.getReportLocation());
    }

    catch (Exception e)
    {
      throw new IllegalArgumentException(
        messageList.getMessage(
          "config17",
          "Could not get report file location."));
    }

    // If replace flag is false and file exists, then throw exception
    if (file.exists() && !this.getReplaceReport())
    {
      throw new IllegalArgumentException(
        messageList.getMessage(
          "config15",
          "Report file already exists and replace option was not specified."));
    }

    if (correlationType != null
      && !this.correlationType.equals(
        WSIConstants.ATTRVAL_CORRELATION_TYPE_ENDPOINT)
      && !this.correlationType.equals(
        WSIConstants.ATTRVAL_CORRELATION_TYPE_NAMESPACE)
      && !this.correlationType.equals(
        WSIConstants.ATTRVAL_CORRELATION_TYPE_OPERATION))
    {
      throw new IllegalArgumentException(
        messageList.getMessage(
          "config18",
          "Invalid correlation type specified."));
    }
  }

  /**
   * Get option value.
   */
  private String getOptionValue(String[] args, int index) throws WSIException
  {
    return getOptionValue(args, index, args[index - 1]);
  }

  /**
   * Get option value.
   */
  private String getOptionValue(String[] args, int index, String optionName)
    throws WSIException
  {
    // If index greater than array length, then throw exception
    if (index >= args.length)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config13",
          optionName,
          "Option did not contain a value.  Specified option:"));
    }

    // If the value is an option (i.e. starts with a "-"), then throw exception
    if (args[index].startsWith("-"))
    {
      throw new IllegalArgumentException(
        getMessage(
          "config13",
          optionName,
          "Option did not contain a value.  Specified option:"));
    }

    return (args[index]);
  }

  /**
   * Get message from resource bundle.
   */
  private String getMessage(String messageID, String defaultMessage)
  {
    return getMessage(messageID, null, defaultMessage);
  }

  /**
   * Get message from resource bundle.
   */
  private String getMessage(
    String messageID,
    String messageData,
    String defaultMessage)
  {
    String message = defaultMessage;
    if (messageList != null)
      message = messageList.getMessage(messageID, messageData, defaultMessage);
    else if (messageData != null)
      message += " " + messageData + ".";

    return message;
  }
}
