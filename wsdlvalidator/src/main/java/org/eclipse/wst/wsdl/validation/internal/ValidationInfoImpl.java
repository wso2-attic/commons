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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;

/**
 * An implementation of the validation info interface.
 */
public class ValidationInfoImpl implements IValidationReport, ControllerValidationInfo
{
  private boolean WRAPPER_ERROR_SUPPORT_ENABLED = true;
  private final String _WARN_NO_VALDIATOR = "_WARN_NO_VALDIATOR";
  private final String _REF_FILE_ERROR_MESSAGE = "_REF_FILE_ERROR_MESSAGE";
  private String validating_file_uri = null;
  private URL validating_file_url = null;
  private boolean wsdlValid = false;
  private List messages = new Vector();
  //private List schemas = new Vector();
  private List nsNoVals = new Vector();
  private MessageGenerator messagegenerator = null;
  private boolean haserrors = false;
  private HashMap nestedMessages = new HashMap();
  private URIResolver uriResolver = null;
  private WSDLValidationConfiguration configuration;

  /**
   * Constructor.
   * 
   * @param uri
   *            The URI of the file for the validation.
   */
  public ValidationInfoImpl(String uri, MessageGenerator messagegenerator)
  {
    this.validating_file_uri = uri;
    if(uri != null)
    {
      uri = uri.replaceAll("%20"," ");
      this.validating_file_uri = uri;
      try
      {
        this.validating_file_url = new URL(uri);
      } catch (MalformedURLException e)
      {
      }
    }
    this.messagegenerator = messagegenerator;
  }
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationReport#getFileURI()
   */
  public String getFileURI()
  {
    return validating_file_uri;
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationReport#isWSDLValid()
   */
  public boolean isWSDLValid()
  {
    return wsdlValid;
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationInfo#addError(java.lang.String,
   *      int, int)
   */
  public void addError(String message, int line, int column)
  {
    addError(message, line, column, validating_file_uri);
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationInfo#addError(java.lang.String,
   *      int, int)
   */
  public void addError(String message, int line, int column, String uri)
  {
    if(addMessage(message, line, column, uri, ValidationMessageImpl.SEV_ERROR))
    {
      haserrors = true;
    }
  }

  public void addError(String message, int line, int column, String uri, String errorKey, Object[] messageArguments)
  {
    if(addMessage(message, line, column, uri, ValidationMessageImpl.SEV_ERROR, errorKey, messageArguments))
    {
      haserrors = true;
    }
  }
  
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationInfo#addWarning(java.lang.String,
   *      int, int)
   */
  public void addWarning(String message, int line, int column)
  {
    addWarning(message, line, column, validating_file_uri);
  }
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationInfo#addWarning(java.lang.String,
   *      int, int)
   */
  public void addWarning(String message, int line, int column, String uri)
  {
    addMessage(message, line, column, uri, ValidationMessageImpl.SEV_WARNING);
  }
  
  /**
   * Add a message to the list. A message may not be added to the list in 
   * certain circumstances such as when the URI is invalid or the message 
   * is null.
   * 
   * @param message The message to add.
   * @param line The line location of the message.
   * @param column The column location of the message.
   * @param uri The URI of the file that contains the message.
   * @param severity The severity of the message.
   * @return True if the message was able to be added, false otherwise.
   */
  private boolean addMessage(String message, int line, int column, String uri, int severity)
  {
      return this.addMessage(message, line, column, uri, severity, null, null);
  }
  
  /**
   * Add a message to the list. A message may not be added to the list in 
   * certain circumstances such as when the URI is invalid or the message 
   * is null.
   * 
   * @param message The message to add.
   * @param line The line location of the message.
   * @param column The column location of the message.
   * @param uri The URI of the file that contains the message.
   * @param severity The severity of the message.
   * @param errorKey The Xerces Error Key
   * @param messageArguments The Xerces arguments used to create the error message
   * @return True if the message was able to be added, false otherwise.
   */
  private boolean addMessage(String message, int line, int column, String uri, int severity, String errorKey, Object[] messageArguments)
  {
    boolean successfullyAdded = false;
    // If the message is null there is nothing to report.
    if(message == null)
    {
      return successfullyAdded;
    }
    String errorURI = uri;
    URL errorURL = null;
    if (errorURI != null)
    {
      try
      {
        errorURI = errorURI.replaceAll("%20", " ");
        errorURL = new URL(errorURI);
      } catch (MalformedURLException e)
      {
      }
      //errorURI = normalizeURI(errorURI);
    }
//    else
//    {
//      errorURI = validating_file_uri;
//      errorURL = validating_file_url;
//    }
    //boolean doDialog = true;
    if (errorURL != null)
    {
      successfullyAdded = true;
      // Add to the appropriate list if nested error support is off or
      // this message is for the current file.
      if (!WRAPPER_ERROR_SUPPORT_ENABLED || validating_file_url.sameFile(errorURL))
      {

        ValidationMessageImpl valmes = new ValidationMessageImpl(message, line,
            column, severity, uri, errorKey, messageArguments);
        messages.add(valmes);
      }
      // If nested error support is enabled create a nested error.
      else if (WRAPPER_ERROR_SUPPORT_ENABLED)
      {
        String nesteduri = errorURL.toExternalForm();
        ValidationMessageImpl nestedmess = new ValidationMessageImpl(message, line,
            column, severity, nesteduri);

        ValidationMessageImpl container = (ValidationMessageImpl) nestedMessages.get(nesteduri);
        if(container == null)
        {
          // Initially set the nested error to a warning. This will automatically be changed
          // to an error if a nested message has a severity of error.
          container = new ValidationMessageImpl(messagegenerator.getString(_REF_FILE_ERROR_MESSAGE, nesteduri), 1, 0, IValidationMessage.SEV_WARNING, nesteduri);
          nestedMessages.put(nesteduri, container);
          messages.add(container);
        }
        container.addNestedMessage(nestedmess);
      }
    }

    return successfullyAdded;
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationReport#getValidationMessages()
   */
  public IValidationMessage[] getValidationMessages()
  {
    return (IValidationMessage[])messages.toArray(new IValidationMessage[messages.size()]);
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationInfo#addNamespaceWithNoValidator(java.lang.String)
   */
  public void addNamespaceWithNoValidator(String namespace)
  {
    // If the list doesn't already contain this namespace, add it to the list
    // and create a warning message.
    if (!nsNoVals.contains(namespace))
    {
      nsNoVals.add(namespace);
      addWarning(messagegenerator.getString(_WARN_NO_VALDIATOR, namespace), 1, 0);

    }

  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.ControllerValidationInfo#completeWSDLValidation()
   */
  public void completeWSDLValidation()
  {
    if (haserrors)
    {
      wsdlValid = false;
    }
    else
    {
      wsdlValid = true;
    }
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.IValidationReport#hasErrors()
   */
  public boolean hasErrors()
  {
    return haserrors;
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.ValidationReport#getNestedMessages()
   */
  public HashMap getNestedMessages()
  {
    return nestedMessages;
  }
  
  public void setURIResolver(URIResolver uriResolver)
  {
  	this.uriResolver = uriResolver;
  }
  
  public URIResolver getURIResolver()
  {
  	return uriResolver;
  }

	/* (non-Javadoc)
	 * @see org.eclipse.wsdl.validate.ValidationInfo#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) 
	{
		if(configuration == null)
		{
			return null;
		}
		return configuration.getProperty(name);
	}
	
	public void setConfiguration(WSDLValidationConfiguration configuration)
	{
		this.configuration = configuration;
	}
	public boolean isValid() {
		return !hasErrors();
	}
}
