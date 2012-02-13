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

package org.eclipse.wst.wsdl.validation.internal.ui.ant;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.DTDLocation;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.XMLCatalog;
import org.eclipse.wst.wsdl.validation.internal.ClassloaderWSDLValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidationConfiguration;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidator;
import org.eclipse.wst.wsdl.validation.internal.logging.LoggerFactory;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolverDelegate;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.ClassloaderWSDL11ValidatorDelegate;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidatorDelegate;

/**
 * An Ant task to run WSDL validation on a file or a set of files.
 * 
 * Options
 * - file - the file to run validation on (optional if fileset is used)
 * - failonerror - fail the build process on the first file with an error
 * 
 * Supported Nested Elements
 * - xmlcatalog - an xml catalog
 * - dtd        - a dtd specification as would appear in an xmlcatalog
 * - schema     - a schema specification as would appear in an xmlcatalog
 * - entity     - an entity specification as would appear in an xmlcatalog
 * - fileset    - a set of files to validate
 * - wsdl11validator - an extension WSDL 1.1 validator
 * - uriresolver - an extension URI resolver
 * 
 */
public class WSDLValidate extends Task
{
  protected final String UI_PROPERTIES = "validatewsdlui";
  protected final String VALIDATOR_PROPERTIES = org.eclipse.wst.wsdl.validation.internal.Constants.WSDL_VALIDATOR_PROPERTIES_FILE;
  protected final String _ERROR_NO_FILE_SPECIFIED = "_ERROR_NO_FILE_SPECIFIED";
  protected final String _UI_INFORMATION_DELIMITER = "_UI_INFORMATION_DELIMITER";
  protected final String _UI_ACTION_VALIDATING_FILE = "_UI_ACTION_VALIDATING_FILE";
  protected final String _UI_ERROR_MARKER = "_UI_ERROR_MARKER";
  protected final String _UI_WARNING_MARKER = "_UI_WARNING_MARKER";
  protected final String _UI_VALID = "_UI_VALID";
  protected final String _UI_INVALID = "_UI_INVALID";
  protected final String _EXC_UNABLE_TO_VALIDATE_FILE = "_EXC_UNABLE_TO_VALIDATE_FILE";
  protected final String _EXC_WSDL_FAIL_ON_ERROR = "_EXC_WSDL_FAIL_ON_ERROR";
  
  protected final String FILE_PROTOCOL = "file:///";

  // Global Vars
  protected List filesets = new ArrayList();
  protected String file = null;
  protected Path classpath;
  protected String xsdDirectory = null;
  protected boolean failOnError = false;
  protected XMLCatalog globalXMLCatalog = new XMLCatalog();
  protected List wsdl11validators = new ArrayList();
  protected List extvalidators = new ArrayList();
  protected List extURIResolvers = new ArrayList();
  protected List properties = new ArrayList();

  /**
   * Constuctor.
   */
  public WSDLValidate()
  {
	LoggerFactory.getInstance().setLogger(new AntLogger(this));
  }

  /**
   * Tells the WSDL validate task to fail the build if an error is encountered.
   * 
   * @param failOnError Whether to fail on error or not.
   */
  public void setFailOnError(boolean failOnError)
  {
    this.failOnError = failOnError;
  }

  /**
   * Set the directory where the base schema files for the catalog are located.
   * 
   * @param dir - the directory of the base schema files
   */
  public void setSchemaDir(String dir)
  {
    xsdDirectory = dir;
  }

  /**
   * Set a file to run WSDL validation on.
   * 
   * @param file - a file to run validation on
   */
  public void setFile(String file)
  {
    this.file = file;
  }

  /**
   * Create a set of files to run WSDL validation on.
   * 
   * @return the FileSet of files to run WSDL validation on
   */
  public FileSet createFileset()
  {
    FileSet fileset = new FileSet();
    filesets.add(fileset);
    return fileset;
  }

  /**
   * Add an XML catalog to the validator.
   * 
   * @param catalog - the catalog to add to the validator
   */
  public void addConfiguredXMLCatalog(XMLCatalog catalog)
  {
    globalXMLCatalog.addConfiguredXMLCatalog(catalog);
  }
  /**
   * Add an extension validator.
   * 
   * @param extVal The extension validator to add.
   */
  public void addConfiguredExtensionValidator(ExtensionValidator extVal)
  {
    extvalidators.add(extVal);
  }
  
  /**
   * Add an extension WSDL 1.1 validator.
   * 
   * @param extVal The extension WSDL 1.1 validator to add.
   */
  public void addConfiguredWSDL11Validator(ExtensionValidator extVal)
  {
    wsdl11validators.add(extVal);
  }

  /**
   * Allow specification of an entity outside of an XMLCatalog.
   * 
   * @return a DTDLocation with the specified entity
   **/
  public DTDLocation createEntity()
  {
    DTDLocation dtdLoc = new DTDLocation();
    globalXMLCatalog.addEntity(dtdLoc);
    return dtdLoc;
  }

  /**
   * Allow specification of a DTD outside of an XMLCatalog.
   * 
   * @return a DTDLocation with the specified DTD
   **/
  public DTDLocation createDTD()
  {
    DTDLocation dtdLoc = new DTDLocation();
    globalXMLCatalog.addEntity(dtdLoc);
    return dtdLoc;
  }
  
  /**
   * Create a URIResolver extension.
   * 
   * @return A URIResolver.
   */
  public URIResolver createURIResolver()
  {
    URIResolver urires = new URIResolver();
    extURIResolvers.add(urires.getClassName());
    return urires;
  }
  
  /**
   * Add a property to the WSDL validator.
   * 
   * @param property 
   * 		The property to add.
   */
  public void addConfiguredProperty(Property property)
  {
    properties.add(property);
  }

  /**
   * Get a list of all the files to run WSDL validation on. Takes the file and fileset
   * and creates the list.
   * 
   * @return the list of files to be validated
   */
  protected List getFileList()
  {
    List files = new ArrayList();

    // if a specific file was specified add it to the list
    if (file != null)
    {
      try
      {
        URL url = new URL(file);
        files.add(url.toExternalForm());
      }
      catch(Exception e)
      {
        File theFile = new File(file);
        if(!theFile.isAbsolute())
        {
          theFile = new File(getProject().getBaseDir(), file);
        }
        String absFile = theFile.toString(); 
        if(!absFile.startsWith("file:"))
        {
          absFile = FILE_PROTOCOL + absFile;
        }
        absFile = absFile.replace('\\','/');
        files.add(absFile);
      }
    }

    // go through all filesets specified and add all the files to the list
    Iterator fsIter = filesets.iterator();
    while (fsIter.hasNext())
    {
      FileSet fileset = (FileSet)fsIter.next();
      DirectoryScanner ds = fileset.getDirectoryScanner(fileset.getProject());
      String basedir = ds.getBasedir().toString() + File.separator;

      String[] filelist = ds.getIncludedFiles();
      int numFiles = filelist.length;
      if (files != null && numFiles > 0)
      {
        for (int i = 0; i < numFiles; i++)
        {
          String absFile = FILE_PROTOCOL + basedir + filelist[i];
          absFile = absFile.replace('\\','/');
          files.add(absFile);
        }
      }
    }
    return files;
  }

  /* (non-Javadoc)
   * @see org.apache.tools.ant.Task#execute()
   */
  public void execute() throws BuildException
  {
    // the resource bundles for the ui and validator are needed
    MessageGenerator messGen = null;
    try
    {
      ResourceBundle uiRB = ResourceBundle.getBundle(UI_PROPERTIES);
      messGen = new MessageGenerator(uiRB);
    }
    catch (MissingResourceException e)
    {
      // if the resource bundles can't be opened we can't report error so throw an exception
      throw new BuildException("Unable to open resource bundle. " + e);
    }

    // Set the XML catalog.
    org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalog.setExtensionXMLCatalog(XMLCatalogImpl.class.getName(), getClass().getClassLoader());
    XMLCatalogImpl xmlCatalog = (XMLCatalogImpl)org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalog.getExtensionCatalogInstance();
    xmlCatalog.addXMLCatalog(globalXMLCatalog);
    
    WSDLValidator wsdlValidator = new WSDLValidator();
    
    WSDLValidationConfiguration configuration = new WSDLValidationConfiguration();
    // Set the properties.
    Iterator propertyIter = properties.iterator();
    while(propertyIter.hasNext())
    {
      Property property = (Property)propertyIter.next();
      configuration.setProperty(property.getName(), property.getValue());
    }
    	
    // Set the extension URIResolvers.
    Iterator resolversIter = extURIResolvers.iterator();
    while(resolversIter.hasNext())
    {
      String resolverClass = (String)resolversIter.next();
      wsdlValidator.addURIResolver(new URIResolverDelegate(resolverClass, getClass().getClassLoader()).getURIResolver());
    }
    
    // Get the list of files to validate.
    List files = getFileList();
    
    // Register the WSDL 1.1 extension validators.
    Iterator wsdl11extIter = wsdl11validators.iterator();
    while(wsdl11extIter.hasNext())
    {
      ExtensionValidator extVal = (ExtensionValidator)wsdl11extIter.next();
      WSDL11ValidatorDelegate delegate = new ClassloaderWSDL11ValidatorDelegate(extVal.getClassName());
      wsdlValidator.registerWSDL11Validator(extVal.getNamespace(), delegate);
    }
    
    // Register the extension validators.
    Iterator extIter = extvalidators.iterator();
    while(extIter.hasNext())
    {
      ExtensionValidator extVal = (ExtensionValidator)extIter.next();
      ClassloaderWSDLValidatorDelegate delegate = new ClassloaderWSDLValidatorDelegate(extVal.getClassName());
      wsdlValidator.registerWSDLExtensionValidator(extVal.getNamespace(), delegate);
    }

    // The user didn't specify any files to validate.
    if (files == null || files.isEmpty())
    {
      log(messGen.getString(_ERROR_NO_FILE_SPECIFIED), Project.MSG_ERR);
      return;
    }

    if (xsdDirectory != null)
    {
      org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalog.addSchemaDir(xsdDirectory);
    }

    // Validate all the files specified.
    Iterator iFiles = files.iterator();

    // Common strings needed in validation output.
    String infoDelim = messGen.getString(_UI_INFORMATION_DELIMITER);
    String valid = messGen.getString(_UI_VALID);
    String invalid = messGen.getString(_UI_INVALID);
    String errormarker = messGen.getString(_UI_ERROR_MARKER);
    String warningmarker = messGen.getString(_UI_WARNING_MARKER);

    StringBuffer result = null;
    boolean notvalid = true;
    while (iFiles.hasNext())
    {
      result = new StringBuffer();
      notvalid = false;
      String filename = (String)iFiles.next();
      try
      {
        result.append(infoDelim).append("\n");
        result.append(messGen.getString(_UI_ACTION_VALIDATING_FILE, filename)).append(" - ");

        IValidationReport valReport = wsdlValidator.validate(filename, null, configuration);

        IValidationMessage[] messages = valReport.getValidationMessages();

        if (!valReport.hasErrors())
        {
          result.append(valid);
        }
        else
        {
          result.append(invalid);
          notvalid = true;
        }
        result.append("\n").append(infoDelim).append("\n");

        result.append(reportMessages(messages, errormarker, warningmarker));

        if(notvalid)
        {
          log(result.toString(), Project.MSG_ERR);
        }
        else
        {
          log(result.toString());
        }
      }
      catch (Exception e)
      {
        log(messGen.getString(_EXC_UNABLE_TO_VALIDATE_FILE, filename, e), Project.MSG_ERR);
      }
      finally
      {
        if (notvalid && failOnError)
        {
          // To fail on error, throw a build exception.
          throw new BuildException(messGen.getString(_EXC_WSDL_FAIL_ON_ERROR));
        }
      }
    }

    org.eclipse.wst.wsdl.validation.internal.xml.XMLCatalog.reset();

  }

  /**
   * Return a string with formatted output for the messages.
   * 
   * @param messages The messages to report.
   * @param errormarker The marker to use for error messages.
   * @param warningmarker The marker to use for warning messages.
   * @return A string with the formatted output.
   */
  protected String reportMessages(IValidationMessage[] messages, String errormarker, String warningmarker)
  {
    StringBuffer returnBuffer = new StringBuffer();

    if (messages == null)
      return returnBuffer.toString();

    String prefix = null;
    int numMessages = messages.length;
    for(int i = 0; i < numMessages; i++)
    {
      IValidationMessage message = messages[i];

      if(message.getSeverity() == IValidationMessage.SEV_ERROR)
       {
        prefix = errormarker;
      }
      else if(message.getSeverity() == IValidationMessage.SEV_WARNING)
       {
        prefix = warningmarker;
      }
      else
       {
        prefix = "";
      }if(message.getSeverity() == IValidationMessage.SEV_ERROR)
       {
        prefix = errormarker;
      }
      else if(message.getSeverity() == IValidationMessage.SEV_WARNING)
       {
        prefix = warningmarker;
      }
      else
       {
        prefix = "";
      }if(message.getSeverity() == IValidationMessage.SEV_ERROR)
       {
        prefix = errormarker;
      }
      else if(message.getSeverity() == IValidationMessage.SEV_WARNING)
       {
        prefix = warningmarker;
      }
      else
       {
        prefix = "";
      }if(message.getSeverity() == IValidationMessage.SEV_ERROR)
       {
        prefix = errormarker;
      }
      else if(message.getSeverity() == IValidationMessage.SEV_WARNING)
       {
        prefix = warningmarker;
      }
      else
       {
        prefix = "";
      }
      returnBuffer
        .append(prefix)
        .append(" ")
        .append(message.getLine())
        .append(":")
        .append(message.getColumn())
        .append(":")
        .append(message.getMessage())
        .append("\n");
    }
    return returnBuffer.toString();
  }
}