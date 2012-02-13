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
package org.eclipse.wst.wsdl.validation.internal.eclipse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.eclipse.wst.wsdl.validation.internal.Constants;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidationConfiguration;
import org.eclipse.wst.xml.core.internal.validation.core.AbstractNestedValidator;
import org.eclipse.wst.xml.core.internal.validation.core.NestedValidatorContext;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationInfo;
import org.eclipse.wst.xml.core.internal.validation.core.ValidationReport;

/**
 * A WSDL validator to contribute to the WTP validation framework.
 */
public class Validator extends AbstractNestedValidator 
{
  protected HashMap xsdGrammarPools = new HashMap();
  protected HashMap xmlGrammarPools = new HashMap();

  /** 
   * Create and configure the two grammar pools for this WSDL validation context.
   * 
   * @see org.eclipse.wst.xml.core.internal.validation.core.AbstractNestedValidator#setupValidation(org.eclipse.wst.xml.core.internal.validation.core.NestedValidatorContext)
   */
  protected void setupValidation(NestedValidatorContext context) 
  {
	super.setupValidation(context);
    
	XMLGrammarPool xsdGrammarPool = new InlineSchemaModelGrammarPoolImpl();
	XMLGrammarPool xmlGrammarPool = new XMLGrammarPoolImpl();
	
	xsdGrammarPools.put(context, xsdGrammarPool);
	xmlGrammarPools.put(context, xmlGrammarPool);
  }

  /**
   * Remove two grammar pools for this WSDL validation context.
   * 
   * @see org.eclipse.wst.xml.core.internal.validation.core.AbstractNestedValidator#teardownValidation(org.eclipse.wst.xml.core.internal.validation.core.NestedValidatorContext)
   */
  protected void teardownValidation(NestedValidatorContext context) 
  {
    XMLGrammarPool xsdGrammarPool = (XMLGrammarPool)xsdGrammarPools.remove(context);
    if(xsdGrammarPool != null)
      xsdGrammarPool.clear();
    XMLGrammarPool xmlGrammarPool = (XMLGrammarPool)xmlGrammarPools.remove(context);
    if(xmlGrammarPool != null)
      xmlGrammarPool.clear();
    
    super.teardownValidation(context);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.xml.core.internal.validation.core.AbstractNestedValidator#validate(java.lang.String, java.io.InputStream, org.eclipse.wst.xml.core.internal.validation.core.NestedValidatorContext)
   */
  public ValidationReport validate(String uri, InputStream inputstream, NestedValidatorContext context) 
  {
	XMLGrammarPool xsdGrammarPool = (XMLGrammarPool)xsdGrammarPools.get(context);
	XMLGrammarPool xmlGrammarPool = (XMLGrammarPool)xmlGrammarPools.get(context);
	
	WSDLValidator validator = WSDLValidator.getInstance();
	
	WSDLValidationConfiguration configuration = new WSDLValidationConfiguration();
	configuration.setProperty(Constants.XMLSCHEMA_CACHE_ATTRIBUTE, xsdGrammarPool);
    configuration.setProperty(Constants.XML_CACHE_ATTRIBUTE, xmlGrammarPool);

	IValidationReport valreport = null;
	if (inputstream != null)
	{
	  valreport = validator.validate(uri, inputstream, configuration);
	}
	else
	{
	  valreport = validator.validate(uri, null, configuration);
	}
			        
	return convertReportToXMLReport(valreport);
  }
  
  /**
   * Convert a WSDL IValidationReport to an XML Validation Report.
   * 
   * @param report 
   * 		The WSDL IValidationReport to convert.
   * @return 
   * 		An XML Validation Report representing the information contained in the IValidationReport.
   */
  protected ValidationReport convertReportToXMLReport(IValidationReport report)
  {
	ValidationInfo convertedReport = new ValidationInfo(report.getFileURI());
	IValidationMessage[] messages = report.getValidationMessages();
	int numMessages = messages.length;
	for(int i = 0; i < numMessages; i++)
	{
	  convertMessage(messages[i], convertedReport);
	}

	return convertedReport;
  }
  
  /**
   * Convert a WSDL IValidationMessage by reporting it in the ValidationInfo object.
   * 
   * @param message
   * 		The IValidationMessage to convert.
   * @param convertedReport
   * 		The ValidationInfo object represting the converted report.
   */
  protected void convertMessage(IValidationMessage message, ValidationInfo convertedReport)
  {
	List nestedMessages = message.getNestedMessages();
	if(nestedMessages != null && nestedMessages.size() > 0)
	{
	  Iterator messageIter = nestedMessages.iterator();
	  while(messageIter.hasNext())
	  {
		convertMessage((IValidationMessage)messageIter.next(), convertedReport);
	  }
	}
	else
	{
	  if(message.getSeverity() == IValidationMessage.SEV_WARNING)
	    convertedReport.addWarning(message.getMessage(), message.getLine(), message.getColumn(), message.getURI());
	  else
		convertedReport.addError(message.getMessage(), message.getLine(), message.getColumn(), message.getURI());
	}
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.wst.xml.core.internal.validation.core.AbstractNestedValidator#getValidatorID()
   */
  protected String getValidatorID()
  {
    // Because this class is used as a delegate, return the id of the validator
    // which delegates to this class.
 
    return WSDLDelegatingValidator.class.getName(); //$NON-NLS-1$
  }
}
