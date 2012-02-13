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

package org.eclipse.wst.wsdl.validation.internal.wsdl11.soap;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11ValidationInfo;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator;

import com.ibm.wsdl.BindingFaultImpl;
import com.ibm.wsdl.BindingImpl;
import com.ibm.wsdl.BindingInputImpl;
import com.ibm.wsdl.BindingOperationImpl;
import com.ibm.wsdl.BindingOutputImpl;
import com.ibm.wsdl.PortImpl;
import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;
import com.ibm.wsdl.extensions.soap.SOAPBindingImpl;
import com.ibm.wsdl.extensions.soap.SOAPBodyImpl;
import com.ibm.wsdl.extensions.soap.SOAPFaultImpl;
import com.ibm.wsdl.extensions.soap.SOAPHeaderFaultImpl;
import com.ibm.wsdl.extensions.soap.SOAPHeaderImpl;
import com.ibm.wsdl.extensions.soap.SOAPOperationImpl;

/**
 * The SOAP validator plugs into the WSDL validator to provide
 * validation for all elements in a WSDL document within the SOAP namespace.
 *
 */
public class SOAPValidator implements IWSDL11Validator
{
  private static String SOAP_RESOURCE_BUNDLE_NAME = "validatewsdlsoap";
	  
  private final String _ERROR_INVALID_PORT_ELEMENT = "_ERROR_INVALID_PORT_ELEMENT";
  private final String _ERROR_INVALID_BINDING_ELEMENT = "_ERROR_INVALID_BINDING_ELEMENT";
  private final String _ERROR_INVALID_BINDING_OPERATION_ELEMENT = "_ERROR_INVALID_BINDING_OPERATION_ELEMENT";
  private final String _ERROR_INVALID_HEADER_BODY_ELEMENT = "_ERROR_INVALID_HEADER_BODY_ELEMENT";
  private final String _ERROR_INVALID_FAULT_ELEMENT = "_ERROR_INVALID_FAULT_ELEMENT";
  private final String _ERROR_INVALID_SOAP_ELEMENT_FOR_LOCATION = "_ERROR_INVALID_SOAP_ELEMENT_FOR_LOCATION";
  private final String _ERROR_NO_LOCATION_FOR_ADDRESS = "_ERROR_NO_LOCATION_FOR_ADDRESS";
  private final String _ERROR_NO_SOAPBINDING_FOR_ADDRESS = "_ERROR_NO_SOAPBINDING_FOR_ADDRESS";
  private final String _ERROR_INVALID_BINDING_STYLE = "_ERROR_INVALID_BINDING_STYLE";
  private final String _ERROR_INVALID_BINDING_URI = "_ERROR_INVALID_BINDING_URI";
  private final String _ERROR_INVALID_OPERATION_STYLE = "_ERROR_INVALID_OPERATION_STYLE";
  private final String _ERROR_NO_SOAPBINDING_FOR_OPERATION = "_ERROR_NO_SOAPBINDING_FOR_OPERATION";
  private final String _ERROR_INVALID_BODY_ENCODING_STYLE = "_ERROR_INVALID_BODY_ENCODING_STYLE";
  //private final String _ERROR_INVALID_BODY_NAMESPACE_FOR_ENCODED = "_ERROR_INVALID_BODY_NAMESPACE_FOR_ENCODED";
  private final String _ERROR_INVALID_BODY_USE = "_ERROR_INVALID_BODY_USE";
  private final String _ERROR_INVALID_BODY_PART_NOT_TYPE = "_ERROR_INVALID_BODY_PART_NOT_TYPE";
  private final String _ERROR_INVALID_BODY_PART_UNDEFINED = "_ERROR_INVALID_BODY_PART_UNDEFINED";
  private final String _ERROR_NO_SOAPBINDING_FOR_BODY = "_ERROR_NO_SOAPBINDING_FOR_BODY";
  private final String _ERROR_HEADER_MESSAGE_UNDEFINED = "_ERROR_HEADER_MESSAGE_UNDEFINED";
  private final String _ERROR_HEADER_PART_UNDEFINED = "_ERROR_HEADER_PART_UNDEFINED";
  private final String _ERROR_HEADER_USE_UNDEFINED = "_ERROR_HEADER_USE_UNDEFINED";
  private final String _ERROR_HEADER_ENCODINGSTYLE_UNDEFINED = "_ERROR_HEADER_ENCODINGSTYLE_UNDEFINED";
  private final String _ERROR_HEADER_NAMESPACE_UNDEFINED = "_ERROR_HEADER_NAMESPACE_UNDEFINED";
  private final String _ERROR_NO_SOAPBINDING_FOR_HEADER = "_ERROR_NO_SOAPBINDING_FOR_HEADER";
  private final String _ERROR_HEADERFAULT_MESSAGE_UNDEFINED = "_ERROR_HEADERFAULT_MESSAGE_UNDEFINED";
  private final String _ERROR_HEADERFAULT_PART_UNDEFINED = "_ERROR_HEADERFAULT_PART_UNDEFINED";
  private final String _ERROR_HEADERFAULT_USE_UNDEFINED = "_ERROR_HEADERFAULT_USE_UNDEFINED";
  private final String _ERROR_HEADERFAULT_ENCODINGSTYLE_UNDEFINED = "_ERROR_HEADERFAULT_ENCODINGSTYLE_UNDEFINED";
  private final String _ERROR_HEADERFAULT_NAMESPACE_UNDEFINED = "_ERROR_HEADERFAULT_NAMESPACE_UNDEFINED";
  private final String _ERROR_INVALID_FAULT_NAME = "_ERROR_INVALID_FAULT_NAME";
  //private final String _ERROR_INVALID_FAULT_ENCODING_STYLE = "_ERROR_INVALID_FAULT_ENCODING_STYLE";
  //private final String _ERROR_INVALID_FAULT_NAMESPACE_FOR_ENCODED = "_ERROR_INVALID_FAULT_NAMESPACE_FOR_ENCODED";
  
  private final String ENCODED = "encoded";
  private final String LITERAL = "literal";
  private final String RPC = "rpc";
  private final String DOCUMENT = "document";
  
  private final String QUOTE = "'";
  private final String EMPTY_STRING = "";

  protected MessageGenerator messagegenerator;
 
  /**
   * Default constructor.
   */
  public SOAPValidator()
  {
	ResourceBundle rb = ResourceBundle.getBundle(SOAP_RESOURCE_BUNDLE_NAME, Locale.getDefault());
	messagegenerator = new MessageGenerator(rb);
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.wsdl11.IWSDL11Validator#validate(java.lang.Object, java.util.List, org.eclipse.wsdl.validate.wsdl11.IWSDL11ValidationInfo)
   */
  public void validate(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    // Port SOAP definition
    // make sure every port has only one address element defined
    // if it is an address element, validate it
    if (parents.get(0).getClass() == PortImpl.class)
    {
      if (element.getClass() == SOAPAddressImpl.class)
      {
        validateAddress(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(_ERROR_INVALID_PORT_ELEMENT, QUOTE + e.getElementType().getLocalPart() + QUOTE), element);
      }
    }

    // Binding SOAP definition
    // A SOAP Binding must have a style or rpc or document or no style defined - defaults to document
    // Must have a transport uri defined - check if the uri is empty
    else if (parents.get(0).getClass() == BindingImpl.class)
    {
      if (element.getClass() == SOAPBindingImpl.class)
      {
        validateBinding(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(_ERROR_INVALID_BINDING_ELEMENT, QUOTE + e.getElementType().getLocalPart() + QUOTE), element);
      }
    }
    // Binding Operation SOAP definition
    // A SOAP Operation may have a style defined in which case it must be document or rpc
    // and may have a soapAction uri defined
    else if (parents.get(0).getClass() == BindingOperationImpl.class)
    {
      if (element.getClass() == SOAPOperationImpl.class)
      {
        validateOperation(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(
              _ERROR_INVALID_BINDING_OPERATION_ELEMENT,
            QUOTE + e.getElementType().getLocalPart() + QUOTE), element);
      }

    }
    else if (
      parents.get(0).getClass() == BindingInputImpl.class || parents.get(0).getClass() == BindingOutputImpl.class)
    {
      // validate the SOAP body
      if (element.getClass() == SOAPBodyImpl.class)
      {
        validateBody(element, parents, valInfo);
      }
      // valiate the SOAP header
      else if (element.getClass() == SOAPHeaderImpl.class)
      {
        validateHeader(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(_ERROR_INVALID_HEADER_BODY_ELEMENT, QUOTE + e.getElementType().getLocalPart() + QUOTE), element);
      }
    }
    else if (parents.get(0).getClass() == BindingFaultImpl.class)
    {
      if (element.getClass() == SOAPFaultImpl.class)
      {
        validateFault(element, parents, valInfo);
      }
      else
      {
        ExtensibilityElement e = (ExtensibilityElement)element;
        valInfo.addError(
          messagegenerator.getString(_ERROR_INVALID_FAULT_ELEMENT, QUOTE + e.getElementType().getLocalPart() + QUOTE), element);
      }
    }
    // in this case there has been a SOAP element defined that is not defined for this point in the SOAP namespace
    else
    {
      ExtensibilityElement e = (ExtensibilityElement)element;
      valInfo.addError(
        messagegenerator.getString(
          _ERROR_INVALID_SOAP_ELEMENT_FOR_LOCATION,
          QUOTE + e.getElementType().getLocalPart() + QUOTE), element);
    }

  }

  /**
   * Ensure that the SOAP address has a value specified for it's uri and that the binding has a SOAP
   * Binding defined.
   * 
   * @param element The SOAP address element.
   * @param parents A list of parents of the SOAP address element.
   * @param valInfo The validation info for this validation.
   */
  protected void validateAddress(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    SOAPAddressImpl sa = (SOAPAddressImpl)element;

    String uri = sa.getLocationURI();
    if (uri == null || uri.equalsIgnoreCase(EMPTY_STRING))
    {
      valInfo.addError(messagegenerator.getString(_ERROR_NO_LOCATION_FOR_ADDRESS), sa);
    }

    Port port = (Port)parents.get(0);

    Binding binding = port.getBinding();

    if (!hasSoapBinding(binding))
    {
      valInfo.addError(
        messagegenerator.getString(
          _ERROR_NO_SOAPBINDING_FOR_ADDRESS,
          QUOTE + binding.getQName().getLocalPart() + QUOTE,
          QUOTE + port.getName() + QUOTE), sa);
    }
  }

  /**
   * Ensure the SOAP Binding defined is valid. A SOAP Binding must have a style of rpc or document
   * or no style defined (defaults to document.)  A valid (non empty) URI must also be specified.
   * 
   * @param element The SOAP binding element.
   * @param parents A list of parents of the SOAP binding element.
   * @param valInfo The validation info for this validation.
   */
  protected void validateBinding(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    SOAPBindingImpl sb = (SOAPBindingImpl)element;

    String style = sb.getStyle();
    String uri = sb.getTransportURI();

    if (style != null && !style.equalsIgnoreCase(RPC) && !style.equalsIgnoreCase(DOCUMENT))
    {
      valInfo.addError(
        messagegenerator.getString(_ERROR_INVALID_BINDING_STYLE, QUOTE + sb.getStyle() + QUOTE), element);
    }
    if (uri.equalsIgnoreCase(EMPTY_STRING))
    {
      valInfo.addError(messagegenerator.getString(_ERROR_INVALID_BINDING_URI), element);
    }
  }

  /**
   * An operation may have a style defined. If it is defined it must be rpc or document. It may also have a
   * uri defined which must be non empty.  It may have a soapAction defined as well.
   * 
   * @param element The SOAP operation element.
   * @param parents A list of parents of the SOAP operation element.
   * @param valInfo The validation info for this validation.
   */
  protected void validateOperation(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    SOAPOperation so = (SOAPOperation)element;

    String soapStyle = so.getStyle();

    if (soapStyle != null && !soapStyle.equalsIgnoreCase(RPC) && !soapStyle.equalsIgnoreCase(DOCUMENT))
    {
      valInfo.addError(messagegenerator.getString(_ERROR_INVALID_OPERATION_STYLE), element);
    }

    Binding binding = (Binding)parents.get(1);
    if (!hasSoapBinding(binding))
    {
      valInfo.addError(
        messagegenerator.getString(_ERROR_NO_SOAPBINDING_FOR_OPERATION, QUOTE + binding.getQName().getLocalPart() + QUOTE), so);
    }
  }

  /**
   * Validate the SOAP body. If encoded a body must have an encodingStyle. Also,
   * if specified, all of the parts listed must be defined parts and in the encoding use case, the parts
   * must have types defined.
   * 
   * @param element The SOAP body element.
   * @param parents A list of parents of the SOAP body element.
   * @param valInfo The validation info for this validation.
   */
  protected void validateBody(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    SOAPBodyImpl sb = (SOAPBodyImpl)element;

    String use = sb.getUse();

    // if the use = encoded then there must be encodingStyles.
    if (use != null && use.equalsIgnoreCase(ENCODED))
    {
      List encodingStyles = sb.getEncodingStyles();
      if (encodingStyles == null || encodingStyles.size() == 0)
      {
        valInfo.addError(messagegenerator.getString(_ERROR_INVALID_BODY_ENCODING_STYLE), sb);
      }
    }
    else if (use != null && !use.equalsIgnoreCase(LITERAL))
    {
      valInfo.addError(messagegenerator.getString(_ERROR_INVALID_BODY_USE, QUOTE + use + QUOTE), sb);
    }

    //Check that the parts are valid
    // parts must be defined in the message specified for the operation
    List parts = sb.getParts();

    if (parts != null)
    {
      Iterator partsIterator = parts.iterator();
      while (partsIterator.hasNext())
      {
        String part = (String)partsIterator.next();
        BindingOperation bo = (BindingOperation)parents.get(1);
        Operation o = bo.getOperation();

        if (o != null && !o.isUndefined())
        {
          // get the message from the input or output if it exists
          Message mess = null;
          if (parents.get(0).getClass() == BindingInputImpl.class)
          {
            Input input = o.getInput();

            if (input != null)
            {
              mess = input.getMessage();
            }
          }
          else if (parents.get(0).getClass() == BindingOutputImpl.class)
          {
            Output output = o.getOutput();

            if (output != null)
            {
              mess = output.getMessage();
            }
          }

          if (mess != null && !mess.isUndefined())
          {
            Part p = mess.getPart(part);

            if (p != null)
            {
              // if the use is encoded the parts must all have a type defined
              if (use != null && use.equalsIgnoreCase(ENCODED))
              {
                if (p.getTypeName() == null)
                {
                  // part error - part needs to be type and isn't	
                  valInfo.addError(
                    messagegenerator.getString(_ERROR_INVALID_BODY_PART_NOT_TYPE, QUOTE + part + QUOTE), sb);
                }
              }
            }
            else
            {
              //part error - part isn't defined	
              valInfo.addError(
                messagegenerator.getString(_ERROR_INVALID_BODY_PART_UNDEFINED, QUOTE + part + QUOTE), sb);
            }
          }
          else
          {
            //part error - input isn't defined
            valInfo.addError(
              messagegenerator.getString(_ERROR_INVALID_BODY_PART_UNDEFINED, QUOTE + part + QUOTE), sb);
          }
        }
        else
        {
          // parts error - operation isn't defined
          valInfo.addError(
            messagegenerator.getString(_ERROR_INVALID_BODY_PART_UNDEFINED, QUOTE + part + QUOTE), sb);
        }
      }
    }

    Binding binding = (Binding)parents.get(2);
    if (!hasSoapBinding(binding))
    {
      valInfo.addError(
        messagegenerator.getString(_ERROR_NO_SOAPBINDING_FOR_BODY, QUOTE + binding.getQName().getLocalPart() + QUOTE), sb);
    }
  }

  /**
   * A SOAP header must have a message, part and use defined. If the use is encoded, must
   * also have a non-empty encodingStyle and namespace defined.
   * A SOAP header may have headerfaults defined as well.
   * 
   * @param element The SOAP header element.
   * @param parents A list of parents of the SOAP header element.
   * @param valInfo The validation info for this validation.
   */
  protected void validateHeader(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    SOAPHeaderImpl soapHeader = (SOAPHeaderImpl)element;

    QName messageQName = soapHeader.getMessage();
    Message message = ((Definition)parents.get(parents.size() - 1)).getMessage(messageQName);
    if (message == null)
    {
      // message undefined
      valInfo.addError(
        messagegenerator.getString(_ERROR_HEADER_MESSAGE_UNDEFINED, QUOTE + messageQName.getLocalPart() + QUOTE), soapHeader);
    }
    else
    {
      String partname = soapHeader.getPart();
      Part part = message.getPart(partname);
      if (part == null)
      {
        // part undefined
        valInfo.addError(
          messagegenerator.getString(
            _ERROR_HEADER_PART_UNDEFINED,
            QUOTE + partname + QUOTE,
            QUOTE + messageQName.getLocalPart() + QUOTE), soapHeader);
      }
    }

    String use = soapHeader.getUse();
    if (use != null && !use.equalsIgnoreCase(LITERAL) && !use.equalsIgnoreCase(ENCODED))
    {
      // use undefined
      valInfo.addError(
        messagegenerator.getString(_ERROR_HEADER_USE_UNDEFINED, QUOTE + use + QUOTE), soapHeader);
    }

    if (use.equalsIgnoreCase(ENCODED))
    {
      List encodingStyles = soapHeader.getEncodingStyles();
      if (encodingStyles == null || encodingStyles.isEmpty())
      {
        // no encodingStyle defined
        valInfo.addError(messagegenerator.getString(_ERROR_HEADER_ENCODINGSTYLE_UNDEFINED), soapHeader);
      }

      String namespace = soapHeader.getNamespaceURI();
      if (namespace == null || namespace.equalsIgnoreCase(EMPTY_STRING))
      {
        // no namespace defined
        valInfo.addError(messagegenerator.getString(_ERROR_HEADER_NAMESPACE_UNDEFINED), soapHeader);
      }
    }

    List headerFaults = soapHeader.getSOAPHeaderFaults();
    if (headerFaults != null)
    {
      Iterator iheaderFaults = headerFaults.iterator();
      while (iheaderFaults.hasNext())
      {
        validateHeaderFault(iheaderFaults.next(), parents, valInfo);
      }
    }

    Binding binding = (Binding)parents.get(2);
    if (!hasSoapBinding(binding))
    {
      valInfo.addError(
        messagegenerator.getString(_ERROR_NO_SOAPBINDING_FOR_HEADER, QUOTE + binding.getQName().getLocalPart() + QUOTE), soapHeader);
    }
  }
  
  /**
   * A SOAP headerfault must have a message, part and use defined. If the use is encoded, must
   * also have a non-empty encodingStyle and namespace defined.
   * 
   * @param element The SOAP header fault element.
   * @param parents A list of parents of the SOAP header fault element.
   * @param valInfo The validation info for this validation.
   */
  protected void validateHeaderFault(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    SOAPHeaderFaultImpl soapHeaderFault = (SOAPHeaderFaultImpl)element;

    QName messageQName = soapHeaderFault.getMessage();
    Message message = ((Definition)parents.get(parents.size() - 1)).getMessage(messageQName);
    if (message == null)
    {
      // message undefined
      valInfo.addError(
        messagegenerator.getString(_ERROR_HEADERFAULT_MESSAGE_UNDEFINED, QUOTE + messageQName.getLocalPart() + QUOTE), soapHeaderFault);
    }
    else
    {
      String partname = soapHeaderFault.getPart();
      Part part = message.getPart(partname);
      if (part == null)
      {
        // part undefined
        valInfo.addError(
          messagegenerator.getString(
            _ERROR_HEADERFAULT_PART_UNDEFINED,
            QUOTE + partname + QUOTE,
            QUOTE + messageQName.getLocalPart() + QUOTE), soapHeaderFault);
      }
    }

    String use = soapHeaderFault.getUse();
    if (use != null && !use.equalsIgnoreCase(LITERAL) && !use.equalsIgnoreCase(ENCODED))
    {
      // use undefined
      valInfo.addError(
        messagegenerator.getString(_ERROR_HEADERFAULT_USE_UNDEFINED, QUOTE + use + QUOTE), soapHeaderFault);
    }

    if (use.equalsIgnoreCase(ENCODED))
    {
      List encodingStyles = soapHeaderFault.getEncodingStyles();
      if (encodingStyles == null || encodingStyles.isEmpty())
      {
        // no encodingStyle defined
        valInfo.addError(
          messagegenerator.getString(_ERROR_HEADERFAULT_ENCODINGSTYLE_UNDEFINED), soapHeaderFault);
      }

      String namespace = soapHeaderFault.getNamespaceURI();
      if (namespace == null || namespace.equalsIgnoreCase(EMPTY_STRING))
      {
        // no namespace defined
        valInfo.addError(
          messagegenerator.getString(_ERROR_HEADERFAULT_NAMESPACE_UNDEFINED), soapHeaderFault);
      }
    }
  }

  /**
  * Validate the SOAP fault. A SOAP fault must have a name defined that corresponds with the name
  * specified in the portType.  If encoded a fault must have an encodingStyle and a namespaceURI. 
  * 
  * @param element The SOAP fault element.
   * @param parents A list of parents of the SOAP fault element.
   * @param validationInfo The validation info for this validation.
  */
  protected void validateFault(Object element, List parents, IWSDL11ValidationInfo valInfo)
  {
    SOAPFaultImpl fault = (SOAPFaultImpl)element;

    String name = fault.getName();

    String parentName = ((BindingFault)parents.get(0)).getName();

    if (!name.equals(parentName))
    {
      valInfo.addError(
        messagegenerator.getString(_ERROR_INVALID_FAULT_NAME, QUOTE + name + QUOTE, QUOTE + parentName + QUOTE), fault);
    }

  }

  /**
   * Method hasSoapBinding. - helper Method
   * Given a binding returns true if it has a SOAP binding defined.
   * 
   * @param binding - the SOAP binding to check
   * @return true if a binding has a SOAP binding defined, false otherwise
   */
  protected boolean hasSoapBinding(Binding binding)
  {
    if (binding != null)
    {
      List extelems = binding.getExtensibilityElements();
      if (extelems != null)
      {
        Iterator iextelems = extelems.iterator();
        while (iextelems.hasNext())
        {
          if (iextelems.next().getClass() == SOAPBindingImpl.class)
          {
            return true;
          }
        }
      }
    }
    return false;
  }
}
