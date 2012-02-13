/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.ui.text;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.wst.wsdl.validation.internal.ClassloaderWSDLValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidationConfiguration;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidator;
import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;
import org.eclipse.wst.wsdl.validation.internal.logging.LoggerFactory;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolverDelegate;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.ClassloaderWSDL11ValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalog;
import org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalogEntityHolder;

import com.ibm.wsdl.util.StringUtils;

/**
 * A commande line tool to run WSDL Validation on a single or multiple files.
 * 
 * Options
 * -schemaDir directory       : a directory of schemas to load into the catalog
 * -schema namespace location : a schema to load into the registry
 * -wsdl11v classname namespace resourcebundle : register a WSDL 1.1 extension validator
 *                              to load for the given namespace with the given resourcebundle
 * -wsiv validatorClass namespace propertiesfile : register a WS-I validator
 * -uriresolver URIResolverClass : register an extension URI resolver
 */
public class WSDLValidate
{
  private final String FILE_PREFIX = "file:///";
  private static final String VALIDATOR_PROPERTIES =
    org.eclipse.wst.wsdl.validation.internal.Constants.WSDL_VALIDATOR_PROPERTIES_FILE;
  
  protected static final String PARAM_WSDL11VAL = "-wsdl11v";
  protected static final String PARAM_EXTVAL = "-extv";
  protected static final String PARAM_SCHEMADIR = "-schemaDir";
  protected static final String PARAM_SCHEMA = "-schema";
  protected static final String PARAM_URIRESOLVER = "-uriresolver";
  protected static final String PARAM_LOGGER = "-logger";
  protected static final String PARAM_PROPERTY = "-D";
  protected static final String PARAM_VERBOSE = "-verbose";
  protected static final String PARAM_VERBOSE_SHORT = "-v";
  
  private static final String STRING_EMPTY = "";
  private static final String STRING_SPACE = " ";
  private static final String STRING_DASH = "-";
  
  String workingdir = System.getProperty("user.dir");
  
  //protected ResourceBundle resourceBundle;

  protected WSDLValidator wsdlValidator = null;
  protected WSDLValidationConfiguration configuration = null;
  protected List wsdlFiles = new ArrayList();
  protected boolean verbose = false;
  protected ResourceBundle validatorRB = null;

  /**
   * Constuctor.
   */
  protected WSDLValidate()
  {
  	wsdlValidator = new WSDLValidator();
  	configuration = new WSDLValidationConfiguration();
  	
    try
    {
      validatorRB = ResourceBundle.getBundle(VALIDATOR_PROPERTIES);
    }
    catch (MissingResourceException e)
    {
      LoggerFactory.getInstance().getLogger().log("Validation failed: Unable to load the WSDL validator properties file.", ILogger.SEV_ERROR, e);
    }
  }
  
  /**
   * Validate the files specified.
   */
  protected void validate()
  {
	ILogger logger = LoggerFactory.getInstance().getLogger();
	
	int numInvalid = 0;

	Iterator filesIter = wsdlFiles.iterator();
    while (filesIter.hasNext())
	{
	  String wsdlFile = (String)filesIter.next();
	  IValidationReport valReport = validateFile(wsdlFile);

	  if(valReport.hasErrors())
	  {
	    numInvalid++;
	    logger.log(MessageFormat.format(WSDLValidateTextUIMessages._UI_FILE_INVALID, new Object[]{wsdlFile}), ILogger.SEV_INFO);
	    //logger.log(WSDLValidateTextUIMessages._UI_INFORMATION_DELIMITER, ILogger.SEV_ERROR);
	    logger.log(getMessages(valReport.getValidationMessages()), ILogger.SEV_INFO);
	  }
	  else if(verbose)
	  {
	    logger.log(MessageFormat.format(WSDLValidateTextUIMessages._UI_FILE_VALID, new Object[]{wsdlFile}), ILogger.SEV_VERBOSE);
	  }
	}
    
    // Log the summary.
    logger.log(MessageFormat.format(WSDLValidateTextUIMessages._UI_VALIDATION_SUMMARY, new Object[]{new Integer(wsdlFiles.size()), new Integer(numInvalid)}), ILogger.SEV_INFO);
  }

  /**
   * Run WSDL validation on a given file.
   * 
   * @param filename 
   * 		The name of the file to validate.
   * @throws Exception
   */
  protected IValidationReport validateFile(String filename)
  {	
    // Resolve the location of the file.
    String filelocation = null;
    try
    {
      URL fileURL = StringUtils.getURL(new URL(FILE_PREFIX + workingdir + "/"), filename);
      filelocation = fileURL.toExternalForm();
    }
    catch (MalformedURLException e)
    {
      // Do nothing. The WSDL validator will handle the error.
      //LoggerFactory.getInstance().getLogger().log(MessageFormat.format(WSDLValidateTextUIMessages._ERROR_UNABLE_TO_READ_FILE, new Object[]{filename}), ILogger.SEV_ERROR);
    }
    
    // Run validation on the file.
    IValidationReport valReport = wsdlValidator.validate(filelocation, null, configuration);
    return valReport;
  }

  /**
   * Returns a String with formatted output for a list of messages.
   * 
   * @param messages 
   * 		The messages to get.
   * @return 
   * 		A string with a formatted list of the messages.
   */
  protected String getMessages(IValidationMessage[] messages)
  {
    StringBuffer outputBuffer = new StringBuffer();
    if (messages != null)
    {
      // create a list of messages that looks like
      // ERROR 1:1 Error message content
      int numMessages = messages.length;
      String marker = null;
      for (int i = 0; i < numMessages; i++)
      {
        IValidationMessage message = messages[i];
        int severity = message.getSeverity();
        if (severity == IValidationMessage.SEV_ERROR)
        {
          marker = WSDLValidateTextUIMessages._UI_ERROR_MARKER;
        }
        else if (severity == IValidationMessage.SEV_WARNING)
        {
          marker = WSDLValidateTextUIMessages._UI_WARNING_MARKER;
        }
        else
        {
          marker = STRING_EMPTY;
        }
        outputBuffer
          .append(marker)
          .append(STRING_SPACE)
          .append(message.getLine())
          .append(":")
          .append(message.getColumn())
          .append(STRING_SPACE)
          .append(message.getMessage());
        if(i != numMessages -1)
        {
          outputBuffer.append("\n");
        }
      }
    }
    return outputBuffer.toString();
  }

  /**
   * The main entry point into the command line tool. 
   * Checks the command line arguments, registers the default validators and runs validation on the
   * list of files.
   * 
   * @param args - the arguments to the validator
   */
  public static void main(String[] args)
  {
    // No arguments specified. Print usage.
	if (args.length < 1)
    {
	  System.err.println(WSDLValidateTextUIMessages._ERROR_WRONG_ARGUMENTS);
	  System.exit(0);
	}
	
	WSDLValidate wsdlValidate = new WSDLValidate();
	wsdlValidate.parseArguments(args);
	wsdlValidate.validate();
  }
  
  /**
   * Parse the arguments specified for this WSDLValidate task and
   * configure validation.
   * 
   * @param args
   * 		The arguments specified for this task.
   */
  protected void parseArguments(String[] args)
  {
	int numargs = args.length;

	for (int i = 0; i < numargs; i++)
	{
	  String param = args[i];

	  // Registering an extension validator or WSDL 1.1 extension validator.
	  if (param.equals(WSDLValidate.PARAM_WSDL11VAL) 
			  || param.equals(WSDLValidate.PARAM_EXTVAL))
	  {
	    String namespace = args[++i];
	    if (!namespace.startsWith(WSDLValidate.STRING_DASH))
	    {
	      String validatorClass = args[++i];

	      if (!validatorClass.startsWith(WSDLValidate.STRING_DASH))
	      {
	        if(param.equalsIgnoreCase(WSDLValidate.PARAM_WSDL11VAL))
	        {  
	          WSDL11ValidatorDelegate delegate = new ClassloaderWSDL11ValidatorDelegate(validatorClass);
	          wsdlValidator.registerWSDL11Validator(namespace, delegate);
	        }
	        else if(param.equalsIgnoreCase(WSDLValidate.PARAM_EXTVAL))
	        {
	          ClassloaderWSDLValidatorDelegate delegate = new ClassloaderWSDLValidatorDelegate(validatorClass);
	          wsdlValidator.registerWSDLExtensionValidator(namespace, delegate);
	        }
	      }
	      else
	      {
	        i--;
	      }
	    }
	    else
	    {
	      i--;
	    }  
	  }
	  // Register a directory with schemas.
	  else if (param.equalsIgnoreCase(WSDLValidate.PARAM_SCHEMADIR))
	  {
	    String xsdDir = args[++i];
	    XMLCatalog.addSchemaDir(xsdDir);
	  }
	  // Register a schema.
	  // TODO: Replace this with an XML catalog.
	  else if (param.equalsIgnoreCase(WSDLValidate.PARAM_SCHEMA))
	  {
	    String publicid = args[++i];
	    String systemid = args[++i];
	    XMLCatalog.addEntity(new XMLCatalogEntityHolder(publicid, systemid));
	  }
	  // Register a URI resolver. 
	  // TODO: Determine if this is appropriate for the text UI.
	  else if(param.equalsIgnoreCase(PARAM_URIRESOLVER))
	  {
	    String resolverClass = args[++i];
	    wsdlValidator.addURIResolver(new URIResolverDelegate(resolverClass, null).getURIResolver());
	  }
	  // Configure the logger.
	  else if(param.equals(PARAM_LOGGER))
	  {
	    String loggerClassString = args[++i];
	    try
	    {
	      Class loggerClass = WSDLValidate.class.getClassLoader().loadClass(loggerClassString);
	      ILogger logger = (ILogger)loggerClass.newInstance();
	      LoggerFactory.getInstance().setLogger(logger);
	    }
	    catch(Exception e)
	    {
	      System.err.println(MessageFormat.format(WSDLValidateTextUIMessages._ERROR_LOADING_LOGGER, new Object[]{loggerClassString}));
	    }
	  }
	  // Set properties.
	  else if(param.startsWith(PARAM_PROPERTY))
	  {
	    int separator = param.indexOf('=');
	    String name = param.substring(2, separator);
	    String value = param.substring(separator+1);
	    configuration.setProperty(name, value);
	  }
	  // Set verbose;
	  else if(param.equals(PARAM_VERBOSE) || param.equals( PARAM_VERBOSE_SHORT))
	  {
		verbose = true;
	  }
	  // a file to validate
	  else
	  {
	    if(!param.startsWith(WSDLValidate.STRING_DASH))
	    {  
	      wsdlFiles.add(param);
	    }
	  }
    }
  }
}
