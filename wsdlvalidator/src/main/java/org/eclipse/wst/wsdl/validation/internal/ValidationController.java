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

package org.eclipse.wst.wsdl.validation.internal;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.xerces.impl.XMLErrorReporter;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.StandardParserConfiguration;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.eclipse.wst.wsdl.validation.internal.exception.ValidateWSDLException;
import org.eclipse.wst.wsdl.validation.internal.logging.ILogger;
import org.eclipse.wst.wsdl.validation.internal.logging.LoggerFactory;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.xml.AbstractXMLConformanceFactory;
import org.eclipse.wst.wsdl.validation.internal.xml.DefaultXMLValidator;
import org.eclipse.wst.wsdl.validation.internal.xml.IXMLValidator;
import org.eclipse.wst.wsdl.validation.internal.xml.LineNumberDOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * This is the main entrypoint to the WSDL Validator. The controller is
 * responsible for calling the reader, the XML conformance check, the WSDL
 * validation and the WS-I validation if selected. The controller contains any
 * errors and warnings generated as well.
 */
public class ValidationController
{
  protected final String _ERROR_PROBLEM_WSDL_VALIDATOR = "_ERROR_PROBLEM_WSDL_VALIDATOR";
  protected final String _ERROR_NO_WSDL_VALIDATOR = "_ERROR_NO_WSDL_VALIDATOR";
  protected final String _ERROR_PROBLEM_EXT_VALIDATOR = "_ERROR_PROBLEM_EXT_VALIDATOR";
  protected final String _ERROR_DOCUMENT_LOCATION = "_ERROR_DOCUMENT_LOCATION";

  protected ValidatorRegistry ver;
  protected ResourceBundle resourcebundle;
  protected MessageGenerator messagegenerator;
  protected URIResolver uriResolver;

  //protected String wsdlNamespace = null;

  /**
   * The ResourceBundle needs to be set so it can be passed to the reader.
   * 
   * @param rb
   *            The resource bundle for this validator.
   */
  public ValidationController(ResourceBundle rb, URIResolver uriResolver)
  {
    resourcebundle = rb;
    messagegenerator = new MessageGenerator(resourcebundle);
    this.uriResolver = uriResolver;

    ver = ValidatorRegistry.getInstance();
  }
  
  /**
   * Validate the WSDL file with the stream. This method will run the check of the 
   * WSDL document. The validation is broken up into three stages: XML conformance,
   * WSDL semantic, and post validation.
   * 
 * @param uri 
 * 			The URI of the WSDL document to be validated.
 * @param inputStream 
 * 			The contents of the WSDL document to be validated.
 * @param configuration
 * 			The WSDL validation configuration to be used while validating this URI.
 * @return A validation report with the validation info for the file.
 */
  public IValidationReport validate(String uri, InputStream inputStream, WSDLValidationConfiguration configuration)
  {
    
    InputStream xmlValidateStream = null;
    InputStream wsdlValidateStream = null;
    if (inputStream != null)
    { //copy the inputStream so we can use it more than once
      String contents = createStringForInputStream(inputStream);
      xmlValidateStream = new ByteArrayInputStream(contents.getBytes());
      wsdlValidateStream = new ByteArrayInputStream(contents.getBytes());
    }
    
    ControllerValidationInfo valInfo = new ValidationInfoImpl(uri, messagegenerator);
    ((ValidationInfoImpl)valInfo).setURIResolver(uriResolver);
    ((ValidationInfoImpl)valInfo).setConfiguration(configuration);

    if (validateXML(valInfo, xmlValidateStream))
    {
      Document wsdldoc = getDocument(uri, wsdlValidateStream, valInfo);

      if(!valInfo.hasErrors())
      {
        String wsdlns = getWSDLNamespace(wsdldoc);
        if(wsdlns != null)
        {
          if (validateWSDL(wsdldoc, valInfo, wsdlns))
          {
            validateExtensionValidators(wsdldoc, valInfo, wsdlns);
          }
        }
      }
    }
    return valInfo;
  }

  /**
   * Validate the file for XML conformance.
	 * @param valInfo information about the validation
   * @param inputStream the inputStream to validate
   * @return True if the file is conformant, false otherwise.
   */
  protected boolean validateXML(ControllerValidationInfo valInfo, InputStream inputStream)
  {
    IXMLValidator xmlValidator = AbstractXMLConformanceFactory.getInstance().getXMLValidator();
    xmlValidator.setURIResolver(uriResolver);
    xmlValidator.setFile(valInfo.getFileURI());
    if (xmlValidator instanceof DefaultXMLValidator)
    {
        ((DefaultXMLValidator)xmlValidator).setInputStream(inputStream);
        XMLGrammarPool grammarPool = (XMLGrammarPool)valInfo.getAttribute(Constants.XML_CACHE_ATTRIBUTE);
        if(grammarPool != null)
          ((DefaultXMLValidator)xmlValidator).setGrammarPool(grammarPool);
    }
    //xmlValidator.setValidationInfo(valInfo);
    xmlValidator.run();
    // if there are no xml conformance problems go on to check the wsdl stuff
    if (xmlValidator.hasErrors())
    {
      // temp handling of XML errors until validator is updated.
      List errors = xmlValidator.getErrors();
      Iterator errorsIter = errors.iterator();
      while (errorsIter.hasNext())
      {
        IValidationMessage valMes = (IValidationMessage)errorsIter.next();
        
        if (valMes instanceof ValidationMessageImpl && valInfo instanceof ValidationInfoImpl)
        {   String errorKey = ((ValidationMessageImpl)valMes).getErrorKey();
            Object[] messageArgs = ((ValidationMessageImpl)valMes).getMessageArguments();
            ((ValidationInfoImpl)valInfo).addError(valMes.getMessage(), valMes.getLine(), valMes.getColumn(), valMes.getURI(), errorKey, messageArgs);
        }
        else
        {
            valInfo.addError(valMes.getMessage(), valMes.getLine(), valMes.getColumn(), valMes.getURI());
        }
      }
      return false;
    }
    //wsdlNamespace = xmlValidator.getWSDLNamespace();
    return true;
  }
  
  /**
   * Validate the file for XML conformance.
	 * @param valInfo information about the validation
   * @return True if the file is conformant, false otherwise.
   */
  protected boolean validateXML(ControllerValidationInfo valInfo)
  { return validateXML(valInfo, null);
  }

  /**
   * Validate the WSDL file. Set the errors and warning appropriately.
   * 
   * @param wsdldoc A W3C document representation of the WSDL document.
   * @param valInfo The current validation information.
   * @param wsdlNamespace The WSDL namespace to validate.
   * @return True if the file is valid, false otherwise.
   */
  protected boolean validateWSDL(Document wsdldoc, ControllerValidationInfo valInfo, String wsdlNamespace)
  {
    WSDLValidatorDelegate[] wsdlVals = ver.queryValidatorRegistry(wsdlNamespace, ValidatorRegistry.WSDL_VALIDATOR);
    if (wsdlVals != null)
    {
      for (int i = 0; i < wsdlVals.length; i++)
      {
        WSDLValidatorDelegate wsdlvaldel = wsdlVals[i];
        IWSDLValidator wsdlVal = wsdlvaldel.getValidator();

        // If the wsdl validator isn't null, validate.
        if (wsdlVal != null)
        {
          try
          {
            wsdlVal.validate(wsdldoc, valInfo);
          }
          catch (ValidateWSDLException e)
          {
            valInfo.addError(messagegenerator.getString(_ERROR_PROBLEM_WSDL_VALIDATOR, wsdlNamespace), 1, 1, valInfo.getFileURI());
          }
        }
        // If the validator is null and the namespace isn't create an error.
        // If the namespace is null the file is empty (and the XML validator
        // has let it go)
        // so it is valid but does not need to be validated.
        else
        {
          valInfo.addError(
            messagegenerator.getString(_ERROR_NO_WSDL_VALIDATOR, wsdlNamespace),
            1,
            1,
            valInfo.getFileURI());
        }
      }
    }
    // No validators registered.
    else
    {
      valInfo.addError(messagegenerator.getString(_ERROR_NO_WSDL_VALIDATOR, wsdlNamespace), 1, 1, valInfo.getFileURI());
    }
    valInfo.completeWSDLValidation();

    return valInfo.isWSDLValid();
  }

  /**
   * Validate the WSDL file with the extension validator. Set the errors and warning appropriately.
   * 
   * @param wsdldoc A W3C document representation of the WSDL document.
   * @param valInfo The current validation information.
   * @param wsdlNamespace The WSDL namespace to validate.
   * @return True if the file is valid, false otherwise.
   */
  protected void validateExtensionValidators(Document wsdldoc, ControllerValidationInfo valInfo, String wsdlNamespace)
  {
    WSDLValidatorDelegate[] extVals = ver.queryValidatorRegistry(wsdlNamespace, ValidatorRegistry.EXT_VALIDATOR);
    if(extVals != null)
    {
      int numvals = extVals.length;
      for(int i = 0; i < numvals; i++)
      {
        WSDLValidatorDelegate extvaldel = extVals[i];
        IWSDLValidator extval = extvaldel.getValidator();
        if(extval != null)
        {
          try
          {
          extval.validate(wsdldoc, valInfo);
          }
          catch(Throwable  t)
          {
            valInfo.addWarning(messagegenerator.getString(_ERROR_PROBLEM_EXT_VALIDATOR,  extvaldel.getValidatorName(), wsdlNamespace), 1, 1, valInfo.getFileURI());
            LoggerFactory.getInstance().getLogger().log(messagegenerator.getString(_ERROR_PROBLEM_EXT_VALIDATOR,  extvaldel.getValidatorName(), wsdlNamespace), ILogger.SEV_ERROR, t);
          }
        }
      }
    }
  }

  /**
   * Set the ResourceBundle for this ValidatorManager.
   * 
   * @param rb
   *            The resource bundle to set.
   * @see #getResourceBundle
   */
  public void setResourceBundle(ResourceBundle rb)
  {
    resourcebundle = rb;
  }

  /**
   * Get the ResourceBundle for this ValidationController.
   * 
   * @return The resource bundle set for this ValidationController.
   * @see #setResourceBundle
   */
  public ResourceBundle getResourceBundle()
  {
    return resourcebundle;
  }
  
  /**
   * Get a DOM document representation of the WSDL document.
   * 
   * @param uri 
   * 		The uri of the file to read
   * @param inputStream
   * 		An optional InputStream to read the document from.
   * @param valinfo
   * 		A validation info object used for reporting messages.
   * @return The DOM model of the WSDL document or null if the document can't be read.
   */
  private Document getDocument(String uri, InputStream inputStream, ControllerValidationInfo valinfo)
  {
    try
    {
      // Catch a premature EOF error to allow empty WSDL files to be considered valid.
      StandardParserConfiguration configuration = new StandardParserConfiguration()
      {
        protected XMLErrorReporter createErrorReporter()
        {
          return new XMLErrorReporter()
          {
            public void reportError(String domain, String key, Object[] arguments, short severity) throws XNIException
            {
              boolean reportError = true;
              if (key.equals("PrematureEOF"))
              {         
                reportError = false;
              }

              if (reportError)
              {
                super.reportError(domain, key, arguments, severity);
              }
            }
          };
        }
      };
      
      InputSource inputSource = null;
      if (inputStream != null)
      { //then we want to create a DOM from the inputstream
        inputSource = new InputSource(inputStream);
      }
      else
      { inputSource = new InputSource(uri);
      }
      
      DOMParser builder = new LineNumberDOMParser(configuration);
      builder.parse(inputSource);
      Document doc = builder.getDocument();

      return doc;
    }
    catch (Throwable t)
    {
      // In this case the validator will fail with an unexplained error message
      // about a null WSDL namespace. This error should be addressed as well.
     valinfo.addError(messagegenerator.getString(_ERROR_DOCUMENT_LOCATION, uri), 0, 0, uri);
      
    }
    return null;
  }
  
  private String getWSDLNamespace(Document doc)
  {
    String wsdlns = null;
    if(doc != null)
    {
      Element rootdoc = doc.getDocumentElement();
      if(rootdoc != null)
      {
        wsdlns = rootdoc.getNamespaceURI();
      }
    }
    return wsdlns;
  }
  
  
  
  private final String createStringForInputStream(InputStream inputStream)
  {
    // Here we are reading the file and storing to a stringbuffer.
    StringBuffer fileString = new StringBuffer();
    try
    {
      InputStreamReader inputReader = new InputStreamReader(inputStream);
      BufferedReader reader = new BufferedReader(inputReader);
      char[] chars = new char[1024];
      int numberRead = reader.read(chars);
      while (numberRead != -1)
      {
        fileString.append(chars, 0, numberRead);
        numberRead = reader.read(chars);
      }
    }
    catch (Exception e)
    {
      //TODO: log error message
      //e.printStackTrace();
    }
    return fileString.toString();
  }
  
}
