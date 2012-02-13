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
package org.eclipse.wst.wsi.internal.validate.wsdl;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;

import org.eclipse.wst.wsdl.validation.internal.IValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.IWSDLValidator;
import org.eclipse.wst.wsdl.validation.internal.ValidationInfoImpl;
import org.eclipse.wst.wsdl.validation.internal.exception.ValidateWSDLException;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDL11ValidationInfoImpl;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDLDocument;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.WSDLReaderImpl;
import org.eclipse.wst.wsi.internal.WSIPreferences;
import org.eclipse.wst.wsi.internal.WSITestToolsEclipseProperties;
import org.eclipse.wst.wsi.internal.WSITestToolsProperties;
import org.eclipse.wst.wsi.internal.analyzer.WSDLAnalyzer;
import org.eclipse.wst.wsi.internal.analyzer.WSIAnalyzerException;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.report.AssertionError;
import org.w3c.dom.Document;

/**
 * The WSI validator plugs into the WSDL validator to provide
 * validation for a WSDL document with respect to the WS-I Basic Profile (currently 1.0).
 * This class acts as an bridge between the WS-I tools and the WSDL Validator.
 * 
 * @author Lawrence Mandel (lmandel@ca.ibm.com)
 * @author Wajiha Rehman
 */
public class WSDLValidator implements IWSDLValidator
{
  protected final String _EXC_UNABLE_TO_VALIDATE_WSI = "_EXC_UNABLE_TO_VALIDATE_WSI";

  // indications for types of errors
  protected final int ERROR = 2;
  protected final int WARNING = 1;
  
  protected ResourceBundle resourcebundle = null;
  protected boolean wsiValid = false;

  /**
   * Constructor.
   */
  public WSDLValidator()
  {
	resourcebundle = ResourceBundle.getBundle("wsivalidate", Locale.getDefault());
  }

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.IWSDLValidator#validate(org.w3c.dom.Document, org.eclipse.wsdl.validate.ValidationInfo)
   */
  public void validate(Document domModel, IValidationInfo valInfo) throws ValidateWSDLException
  {
	boolean withAttachments = false;
	boolean withBasic = false;
	Object attValue = valInfo.getAttribute("http://ws-i.org/profiles/BasicWithAttachments/ComplianceLevel");
	if(attValue == null)
	{
	  attValue = valInfo.getAttribute("http://ws-i.org/profiles/Basic/ComplianceLevel");
   	  if(attValue != null)
	  {
	    withBasic = true;
	  }
	}
	else
	{
	  withAttachments = true;
	}
	String wsiLevel = WSITestToolsProperties.WARN_NON_WSI;
	WSIPreferences wsiPreference = null;
	String uri = valInfo.getFileURI();
	if(attValue != null)
	{
	  String value = (String)attValue;
	  if(value.equals("IGNORE"))
	  {
	  	wsiLevel = WSITestToolsProperties.IGNORE_NON_WSI;
	  }
	  else if(value.equals("REQUIRE"))
	  {
	   	wsiLevel = WSITestToolsProperties.STOP_NON_WSI;
	  }
	}
	else 
	{
	  if(WSITestToolsProperties.getEclipseContext())
	  {
		wsiPreference = WSITestToolsEclipseProperties.checkWSIPreferences(uri);
		wsiLevel = wsiPreference.getComplianceLevel();
	  }
	  else
	  {
		wsiPreference = WSITestToolsProperties.checkWSIPreferences(uri);
		wsiLevel = wsiPreference.getComplianceLevel();
	  }
	}
	// If we are ignoring WS-I then don't run the tests.
	if(wsiLevel.equals(WSITestToolsProperties.IGNORE_NON_WSI))
	{
	  return;
	}
	    
  	wsiValid = true;
  	Definition definition = null;
	boolean hasAnalyzerConfig = false;
  	try
  	{
      //WSDLFactory factory = WSDLFactory.newInstance();
 	  ValidationInfoImpl vali = new ValidationInfoImpl(valInfo.getFileURI(), new MessageGenerator(resourcebundle));
  	  vali.setURIResolver((URIResolver)valInfo.getURIResolver());
      WSDLReaderImpl reader = new WSDLReaderImpl(new WSDL11ValidationInfoImpl(vali));
      WSDLDocument[] docs = reader.readWSDL(uri, domModel);
      int numdocs = docs.length;
      for(int i = 0; i < numdocs; i++)
      {
        WSDLDocument tempDoc = docs[i];
		if(tempDoc.getDefinition().getDocumentBaseURI().equals(valInfo.getFileURI()))
        {
          definition = tempDoc.getDefinition();  
          break;
        }
      }
  	}
  	catch(WSDLException e)
  	{
  	  throw new ValidateWSDLException(MessageFormat.format(resourcebundle.getString(_EXC_UNABLE_TO_VALIDATE_WSI), new Object[] { uri }));
  	}
	    
    if (definition != null && valInfo != null)
    {
      String namespace = definition.getTargetNamespace();

      // get all the collections we may need to validate
      Collection services = definition.getServices().values();
      Collection bindings = definition.getBindings().values();
      Collection portTypes = definition.getPortTypes().values();
      Collection messages = definition.getMessages().values();

      WSDLAnalyzer wsdlAnalyzer;
	  if(withAttachments)
	  {
	  	WSIPreferences preferences = new WSIPreferences();
   	    preferences.setComplianceLevel(wsiLevel);
	    preferences.setTADFile(WSITestToolsProperties.AP_ASSERTION_FILE);
	    wsdlAnalyzer = new WSDLAnalyzer(uri, preferences);
	  }
	  else if(withBasic)
	  {
	  	WSIPreferences preferences = new WSIPreferences();
		preferences.setComplianceLevel(wsiLevel);	
	    preferences.setTADFile(WSITestToolsProperties.SSBP_ASSERTION_FILE);
	    wsdlAnalyzer = new WSDLAnalyzer(uri, preferences);
	  }
	  else if(wsiPreference != null)
	  {
		wsdlAnalyzer = new WSDLAnalyzer(uri, wsiPreference);
	  }
	  else {
		// default preference setting
	  	wsdlAnalyzer = new WSDLAnalyzer(uri);
	  }

	  // The WS-I conformance tools require that each service be analyzed separately.
	  // Get all the services and analyze them.
	  if (services != null && !services.isEmpty())
	  {
	    Iterator serviceIterator = services.iterator();

	    while (serviceIterator.hasNext())
        {
          Service service = (Service) serviceIterator.next();

          String servicename = service.getQName().getLocalPart();

          Collection ports = service.getPorts().values();
          if (ports != null && !ports.isEmpty())
          {
            // The WS-I tools must be called once for each port within each service.
            Iterator portIterator = ports.iterator();
            while (portIterator.hasNext())
            {
              Port port = (Port) portIterator.next();

              try
              {
                wsdlAnalyzer.addConfigurationToTest(servicename, namespace, port.getName(), WSDLAnalyzer.PORT);
				hasAnalyzerConfig = true;
              }
              catch (WSIAnalyzerException e)
              {
                // TODO: Add code to log error 
                //System.out.println("exception " + e);
              }

            }
          }
        }
      }
      // validate at the binding level - check for every binding
      else if (bindings != null && !bindings.isEmpty())
      {
        Iterator bindingIterator = bindings.iterator();

        while (bindingIterator.hasNext())
        {
          Binding binding = (Binding) bindingIterator.next();

          String bindingname = binding.getQName().getLocalPart();
          try
          {
            wsdlAnalyzer.addConfigurationToTest(null, namespace, bindingname, WSDLAnalyzer.BINDING);
			hasAnalyzerConfig = true;
          }
          catch (WSIAnalyzerException e)
          {
            // TODO: Add code to log error 
            //System.out.println("exception " + e);
          }
        }
      }
      // validate at the portType level - check for every portType
      else if (portTypes != null && !portTypes.isEmpty())
      {
        Iterator portTypeIterator = portTypes.iterator();
        while (portTypeIterator.hasNext())
        {
          PortType portType = (PortType) portTypeIterator.next();
          String portTypename = portType.getQName().getLocalPart();
          try
          {
            wsdlAnalyzer.addConfigurationToTest(null, namespace, portTypename, WSDLAnalyzer.PORTTYPE);
			hasAnalyzerConfig = true;
          }
          catch (WSIAnalyzerException e)
          {
            // TODO: Add code to log error 
            //System.out.println("exception " + e);
          }
        }
      }
      // validate at the message level - check for every message
      else if (messages != null && !messages.isEmpty())
      {
        Iterator messageIterator = messages.iterator();

        while (messageIterator.hasNext())
        {
          Message message = (Message) messageIterator.next();
          String messagename = message.getQName().getLocalPart();
          try
          {
            wsdlAnalyzer.addConfigurationToTest(null, namespace, messagename, WSDLAnalyzer.MESSAGE);
			hasAnalyzerConfig = true;
          }
          catch (WSIAnalyzerException e)
          {
            // TODO: Add code to log error 
            //System.out.println("exception " + e);
          }
        }
      }
      try
      {
        // only run the analyzer if there is something to check
		if(hasAnalyzerConfig)
		{
		  // run the conformance check and add errors and warnings as needed
		  wsdlAnalyzer.validateConformance();
			  
		  // If the level is suggest all errors should be reported as warnings.
		  if(wsdlAnalyzer.getWSIPreferences().getComplianceLevel().equals(WSITestToolsProperties.WARN_NON_WSI))
		  {
		    addWarnings(wsdlAnalyzer.getAssertionErrors(), valInfo);
		  }
		  else
		  {
		    addErrors(wsdlAnalyzer.getAssertionErrors(), valInfo);
		  }
		  addWarnings(wsdlAnalyzer.getAssertionWarnings(), valInfo);
		}
      }
	  catch (WSIAnalyzerException e)
	  {
		// TODO: Add code to log error 
	    valInfo.addWarning(WSIConstants.WSI_PREFIX + "A problem occured while running the WS-I WSDL conformance check: " + e, 1, 0, valInfo.getFileURI());
	  }
    }
  }

  /**
   * Add error messages to the validationcontroller.
   * 
   * @param errors The list of errors to add.
   * @param valInfo The object that contains the validation information.
   */
  protected void addErrors(List errors, IValidationInfo valInfo)
  {
    reportProblems(errors, valInfo, ERROR);
  }

  /**
   * Add warning messages to the validationcontroller.
   * 
   * @param warnings The list of warnings to add.
   * @param valInfo The object that contains the validation information.
   */
  protected void addWarnings(List warnings, IValidationInfo valInfo)
  {
    reportProblems(warnings, valInfo, WARNING);
  }

  /**
   * Report the problems specified for the given type to the validationcontroller.
   * 
   * @param problems The problems to report.
   * @param valInfo The object to report the problems to.
   * @param type The type of messages to add.
   */
  protected void reportProblems(List problems, IValidationInfo valInfo, int type)
  {
    // if there were no problems just return
    if (problems == null)
      return;

    
    Iterator problemsIterator = problems.iterator();
    while (problemsIterator.hasNext())
    {
      AssertionError assertionerror = (AssertionError) problemsIterator.next();
      if (type == ERROR)
      {
		wsiValid = false;
        valInfo.addError(
        		WSIConstants.WSI_PREFIX + "(" + assertionerror.getAssertionID()+ ") " + assertionerror.getErrorMessage(),
          assertionerror.getLine(),
          assertionerror.getColumn(),
          valInfo.getFileURI());

      }
      else if (type == WARNING)
      {
        valInfo.addWarning(
        		WSIConstants.WSI_PREFIX + "(" + assertionerror.getAssertionID()+ ") " + assertionerror.getErrorMessage(),
          assertionerror.getLine(),
          assertionerror.getColumn(),
          valInfo.getFileURI());
      }
    }
  }

  /**
   * @see org.eclipse.wsdl.validate.controller.IWSDLValidator#isValid()
   */
  public boolean isValid()
  {
    return wsiValid;
  }

  public void setResourceBundle(ResourceBundle rb) {
	// Not used.
	
  }
  
  

}
