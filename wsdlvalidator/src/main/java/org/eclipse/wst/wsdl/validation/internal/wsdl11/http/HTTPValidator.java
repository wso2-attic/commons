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

package org.eclipse.wst.wsdl.validation.internal.wsdl11.http;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.Port;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPOperation;

import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator;

import com.ibm.wsdl.BindingImpl;
import com.ibm.wsdl.BindingInputImpl;
import com.ibm.wsdl.BindingOperationImpl;
import com.ibm.wsdl.PortImpl;
import com.ibm.wsdl.extensions.http.HTTPAddressImpl;
import com.ibm.wsdl.extensions.http.HTTPBindingImpl;
import com.ibm.wsdl.extensions.http.HTTPOperationImpl;
import com.ibm.wsdl.extensions.http.HTTPUrlEncodedImpl;
import com.ibm.wsdl.extensions.http.HTTPUrlReplacementImpl;

/**
 * The HTTP validator is an extension WSDL validator that validates all elements in the HTTP
 * namespace.
 */
public class HTTPValidator implements IWSDL11Validator
{
  private static String HTTP_RESOURCE_BUNDLE_NAME = "validatewsdlhttp";
	
  private final String _ERROR_INVALID_PORT_ELEMENT = "_ERROR_INVALID_PORT_ELEMENT";
  private final String _ERROR_INVALID_BINDING_ELEMENT = "_ERROR_INVALID_BINDING_ELEMENT";
  private final String _ERROR_INVALID_BINDING_OPERATION_ELEMENT = "_ERROR_INVALID_BINDING_OPERATION_ELEMENT";
  private final String _ERROR_INVALID_BINDING_INPUT_ELEMENT = "_ERROR_INVALID_BINDING_INPUT_ELEMENT";
  private final String _ERROR_INVALID_HTTP_ELEMENT_FOR_LOCATION = "_ERROR_INVALID_HTTP_ELEMENT_FOR_LOCATION";
  private final String _ERROR_NO_LOCATION_FOR_ADDRESS = "_ERROR_NO_LOCATION_FOR_ADDRESS";
  private final String _ERROR_NO_HTTPBINDING_FOR_ADDRESS = "_ERROR_NO_HTTPBINDING_FOR_ADDRESS";
  private final String _ERROR_INVALID_BINDING_VERB = "_ERROR_INVALID_BINDING_VERB";
  private final String _ERROR_INVALID_LOCATION_URI = "_ERROR_INVALID_LOCATION_URI";
  private final String _ERROR_NO_HTTPBINDING_FOR_OPERATION = "_ERROR_NO_HTTPBINDING_FOR_OPERATION";
  private final String _ERROR_NOT_ONLY_ELEMENT_DEFINED = "_ERROR_NOT_ONLY_ELEMENT_DEFINED";
  private final String _ERROR_NO_HTTPOPERATION_FOR_URL = "_ERROR_NO_HTTPOPERATION_FOR_URL";

  private final String GET = "GET";
  private final String POST = "POST";

  private final String QUOTE = "'";
  private final String EMPTY_STRING = "";

  private MessageGenerator messagegenerator;

  /**
   * Default constructor.
   */
  public HTTPValidator()
  {
	ResourceBundle rb = ResourceBundle.getBundle(HTTP_RESOURCE_BUNDLE_NAME, Locale.getDefault());
	messagegenerator = new MessageGenerator(rb);
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator#validate(java.lang.Object, java.util.List, org.eclipse.wsdl.validate.wsdl11.IWSDL11ValidationInfo)
   */
  public void validate(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    // Port HTTP definition
    // make sure every port has only one address element defined
    // if it is an address element, validate it
    if (parents.get(0).getClass() == PortImpl.class)
    {
      if (element.getClass() == HTTPAddressImpl.class)
      {
        validateAddress(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(_ERROR_INVALID_PORT_ELEMENT, QUOTE + e.getElementType().getLocalPart() + QUOTE),
          element);
      }
    }

    // Binding HTTP definition
    // A HTTP Binding must have a verb of GET or POST
    else if (parents.get(0).getClass() == BindingImpl.class)
    {
      if (element.getClass() == HTTPBindingImpl.class)
      {
        validateBinding(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(_ERROR_INVALID_BINDING_ELEMENT, QUOTE + e.getElementType().getLocalPart() + QUOTE),
          element);
      }
    }
    // Binding Operation HTTP definition
    // A HTTP Operation has a location uri defined
    else if (parents.get(0).getClass() == BindingOperationImpl.class)
    {
      if (element.getClass() == HTTPOperationImpl.class)
      {
        validateOperation(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(
            _ERROR_INVALID_BINDING_OPERATION_ELEMENT,
            QUOTE + e.getElementType().getLocalPart() + QUOTE),
          element);
      }

    }
    else if (parents.get(0).getClass() == BindingInputImpl.class)
    {
      // validate the HTTP urlEncoded and urlReplacement
      if (element.getClass() == HTTPUrlEncodedImpl.class || element.getClass() == HTTPUrlReplacementImpl.class)
      {
        validateHttpUrl(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(_ERROR_INVALID_BINDING_INPUT_ELEMENT, QUOTE + e.getElementType().getLocalPart() + QUOTE),
          element);
      }
    }

    // in this case there has been a HTTP element defined that is not defined for this point in the HTTP namespace
    else
    {
      ExtensibilityElement e = (ExtensibilityElement)element;
      valInfo.addError(
        messagegenerator.getString(
          _ERROR_INVALID_HTTP_ELEMENT_FOR_LOCATION,
          QUOTE + e.getElementType().getLocalPart() + QUOTE),
        element);
    }

  }

  /**
   * Ensure that the HTTP address has a value specified for it's uri and that there is a HTTP Binding defined
   * for the Binding specified in the port.
   * 
   * @param element The HTTP address element.
   * @param parents The list of parents of the HTTP address element.
   * @param validatorcontroller The validator controller in charge of validation.
   */
  protected void validateAddress(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    HTTPAddressImpl ha = (HTTPAddressImpl)element;

    String uri = ha.getLocationURI();
    if (uri == null || uri.equalsIgnoreCase(EMPTY_STRING))
    {
      valInfo.addError(messagegenerator.getString(_ERROR_NO_LOCATION_FOR_ADDRESS), ha);
    }

    Port port = (Port)parents.get(0);

    Binding binding = port.getBinding();

    if (!hasHttpBinding(binding))
    {
      valInfo.addError(
        messagegenerator.getString(
          _ERROR_NO_HTTPBINDING_FOR_ADDRESS,
          QUOTE + binding.getQName().getLocalPart() + QUOTE,
          QUOTE + port.getName() + QUOTE),
        ha);
    }
  }

  /**
   * Ensure the HTTP Binding defined is valid. A HTTP Binding must have a verb of GET or POST.
   * 
   * @param element The HTTP binding element.
   * @param parents The list of parents of the HTTP binding element.
   * @param validatorcontroller The validator controller in charge of validation.
   */
  protected void validateBinding(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    HTTPBindingImpl hb = (HTTPBindingImpl)element;

    String verb = hb.getVerb();

    if (verb != null && !verb.equals(GET) && !verb.equals(POST))
    {
      valInfo.addError(messagegenerator.getString(_ERROR_INVALID_BINDING_VERB, QUOTE + verb + QUOTE), element);
    }
  }

  /**
   * An operation must have a location defined. A HTTP Binding must be specified to use an operation.
   * 
   * @param element The HTTP operation element.
   * @param parents The list of parents of the HTTP operation element.
   * @param validatorcontroller The validator controller in charge of validation.
   */
  protected void validateOperation(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    HTTPOperation ho = (HTTPOperation)element;

    String locationURI = ho.getLocationURI();

    if (locationURI != null && locationURI.equalsIgnoreCase(EMPTY_STRING))
    {
      valInfo.addError(messagegenerator.getString(_ERROR_INVALID_LOCATION_URI), element);
    }

    Binding binding = (Binding)parents.get(1);
    if (!hasHttpBinding(binding))
    {
      valInfo.addError(
        messagegenerator.getString(_ERROR_NO_HTTPBINDING_FOR_OPERATION, QUOTE + binding.getQName().getLocalPart() + QUOTE),
        ho);
    }
  }

  /**
   * Validate the HTTP urlReplacement or urlEncoded. Ensure the HTTP operation has been defined. 
   * Ensure that either element is the only element specified.
   * 
   * @param element The HTTP binding operation element.
   * @param parents The list of parents of the HTTP binding operation element.
   * @param validatorcontroller The validator controller in charge of validation.
   */
  protected void validateHttpUrl(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    BindingOperation operation = (BindingOperation)parents.get(1);

    String elementName;
    if (element.getClass() == HTTPUrlEncodedImpl.class)
    {
      elementName = "urlEncoded";
    }
    else if (element.getClass() == HTTPUrlReplacementImpl.class)
    {
      elementName = "urlReplacement";
    }
    else
    {
      elementName = EMPTY_STRING;
    }

    BindingInput input = (BindingInput)parents.get(0);
    if (input.getExtensibilityElements().size() != 1)
    {
      valInfo.addError(messagegenerator.getString(_ERROR_NOT_ONLY_ELEMENT_DEFINED, elementName), element);
    }

    if (!hasHttpOperation(operation))
    {
      valInfo.addError(
        messagegenerator.getString(_ERROR_NO_HTTPOPERATION_FOR_URL, QUOTE + operation.getName() + QUOTE, elementName),
        element);
    }
  }

  /**
   * Given a BindingOperation tells whether it has a HTTP operation defined for it.
   * 
   * @param binding The HTTP binding operation element.
   * @return True if there is an HTTP operation defined, false otherwise.
   */
  protected boolean hasHttpOperation(BindingOperation operation)
  {
    if (operation != null)
    {
      List extelems = operation.getExtensibilityElements();
      if (extelems != null)
      {
        Iterator iextelems = extelems.iterator();
        while (iextelems.hasNext())
        {
          if (iextelems.next().getClass() == HTTPOperationImpl.class)
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Given a binding returns true if it has a HTTP binding defined.
   * 
   * @param binding The binding to check.
   * @return True if there is an HTTP binding defined.
   */
  protected boolean hasHttpBinding(Binding binding)
  {
    if (binding != null)
    {
      List extelems = binding.getExtensibilityElements();
      if (extelems != null)
      {
        Iterator iextelems = extelems.iterator();
        while (iextelems.hasNext())
        {
          if (iextelems.next().getClass() == HTTPBindingImpl.class)
          {
            return true;
          }
        }
      }
    }
    return false;
  }
}
