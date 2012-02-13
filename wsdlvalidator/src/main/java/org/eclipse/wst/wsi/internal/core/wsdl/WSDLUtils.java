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
package org.eclipse.wst.wsi.internal.core.wsdl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.PortType;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;

/**
 * Set of XML related utilities.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public final class WSDLUtils
{

  /**
   * Method isRpcLiteral.
   * @param bindingStyle a binding style.
   * @param operation a WSDL binding operation artifact.
   * @return true if binding is rpc literal.
   */
  public static boolean isRpcLiteral(
    String bindingStyle,
    BindingOperation operation)
  {
    return checkStyleAndUse(
      WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC,
      WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT,
      bindingStyle,
      operation);
  }

  /**
   * Method isDocLiteral.
   * @param bindingStyle  the binding style.
   * @param operation a WSDL binding operation.
   * @return true if binding is document literal. 
   */
  public static boolean isDocLiteral(
    String bindingStyle,
    BindingOperation operation)
  {
    return checkStyleAndUse(
      WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC,
      WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT,
      bindingStyle,
      operation);
  }

  /**
   * Method checkStyleAndUse.
   */
  private static boolean checkStyleAndUse(
    String checkStyle,
    String checkUse,
    String bindingStyle,
    BindingOperation bindingOperation)
  {
    boolean styleFound = false;
    boolean styleAndUseFound = false;

    // Find the soapbind:operation element
    SOAPOperation soapOperation = getSoapOperation(bindingOperation);

    // If there are no ext elements, then check against binding style
    if ((soapOperation == null) || (soapOperation.getStyle() == null))
    {
      if (checkStyle.equals(bindingStyle))
        styleFound = true;
    }

    else
    {
      if (checkStyle.equals(soapOperation.getStyle()))
        styleFound = true;
    }

    // If style found then check use
    if (styleFound)
    {
      // Find the soapbind:body element
      SOAPBody soapBody = getInputSoapBody(bindingOperation);

      // If there are no soapbind:body, then check against default use value
      if ((soapBody == null) || (soapBody.getUse() == null))
      {
        if (checkUse.equals(WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
          styleAndUseFound = true;
      }

      else
      {
        if (checkUse.equals(soapBody.getUse()))
          styleAndUseFound = true;
      }
    }

    return styleAndUseFound;
  }

  /**
   * Get soapbind:binding.
   * @param binding  a Binding object.
   * @return soapbind:binding.
   */
  public static SOAPBinding getSoapBinding(Binding binding)
  {
    SOAPBinding soapBinding = null;

    List extElements = null;

    // Find the soapbind:operation element
    if ((extElements = binding.getExtensibilityElements()) != null)
    {
      for (Iterator iterator = extElements.iterator();
        iterator.hasNext() && (soapBinding == null);
        )
      {
        Object obj = iterator.next();

        if (obj instanceof SOAPBinding)
        {
          soapBinding = (SOAPBinding) obj;
        }
      }
    }

    return soapBinding;
  }

  /**
   * Get soapbind:operation.
   * @param bindingOperation  a BindingOperation object.
   * @return soapbind:operation.
   */
  public static SOAPOperation getSoapOperation(BindingOperation bindingOperation)
  {
    SOAPOperation soapOperation = null;

    List extElements = null;

    // Find the soapbind:operation element
    if ((extElements = bindingOperation.getExtensibilityElements()) != null)
    {
      for (Iterator iterator = extElements.iterator();
        iterator.hasNext() && (soapOperation == null);
        )
      {
        Object obj = iterator.next();

        if (obj instanceof SOAPOperation)
        {
          soapOperation = (SOAPOperation) obj;
        }
      }
    }

    return soapOperation;
  }

  /**
   * Get soapbind:body from input element.
   * @param bindingOperation  the BindingOperation object.
   * @return soapbind:body from input element.
   */
  public static SOAPBody getInputSoapBody(BindingOperation bindingOperation)
  {
    SOAPBody soapBody = null;

    List extElements = null;

    // Find the soapbind:body element
    if ((extElements =
      bindingOperation.getBindingInput().getExtensibilityElements())
      != null)
    {
      for (Iterator iterator = extElements.iterator();
        iterator.hasNext() && (soapBody == null);
        )
      {
        Object obj = iterator.next();

        if (obj instanceof SOAPBody)
        {
          soapBody = (SOAPBody) obj;
        }
      }
    }

    return soapBody;
  }

  /**
   * Find messages referenced by a header or headerfault.
   * @param definition  a Definition object.
   * @param binding  a Binding object.
   * @return messages referenced by a header or headerfault.
   */
  public static HashSet findMessages(Definition definition, Binding binding)
  {
    BindingOperation bindingOperation;

    HashSet tempMessages, bindingMessages = new HashSet();

    HashSet portTypeMessages = findMessages(binding.getPortType());

    // Process each operation
    Iterator iterator = binding.getBindingOperations().iterator();
    while (iterator.hasNext())
    {
      bindingOperation = (BindingOperation) iterator.next();

      // Process the input and then the output
      if (bindingOperation.getBindingInput() != null)
      {
        tempMessages =
          findMessages(
            bindingOperation
              .getBindingInput()
              .getExtensibilityElements()
              .iterator(),
            portTypeMessages,
            definition);

        // Add messages to binding message set
        bindingMessages.addAll(tempMessages);
      }

      // Process output
      if (bindingOperation.getBindingOutput() != null)
      {
        tempMessages =
          findMessages(
            bindingOperation
              .getBindingOutput()
              .getExtensibilityElements()
              .iterator(),
            portTypeMessages,
            definition);

        // Add messages to binding message set
        bindingMessages.addAll(tempMessages);
      }
    }

    return bindingMessages;
  }

  /**
   * Find the messages that are referenced by a header or headerfault.
   * @param portType a PortType object.
   * @return he messages that are referenced by a header or headerfault.
   */
  public static HashSet findMessages(PortType portType)
  {
    HashSet messageSet = new HashSet();
    Operation operation;

    Iterator iterator = portType.getOperations().iterator();
    while (iterator.hasNext())
    {
      // Get next operation to process
      operation = (Operation) iterator.next();

      // Get input and output message
      if (operation.getInput() != null)
        messageSet.add(operation.getInput().getMessage());
      if (operation.getOutput() != null)
        messageSet.add(operation.getOutput().getMessage());

      // Process any faults
      Iterator faults = operation.getFaults().values().iterator();
      while (faults.hasNext())
      {
        messageSet.add(((Fault) faults.next()).getMessage());
      }
    }

    return messageSet;
  }

  /**
   * Find the messages that are referenced by a header or headerfault.
   * @param extElementList a list of external elements.
   * @param messageSet a set of messages.
   * @param definition a Definition object.
   * @return the messages that are referenced by a header or headerfault.
   */
  protected static HashSet findMessages(
    Iterator extElementList,
    HashSet messageSet,
    Definition definition)
  {
    HashSet returnSet = new HashSet();
    ExtensibilityElement extElement;
    Message saveMessage = null;

    while (extElementList.hasNext())
    {
      // Get ext. element
      extElement = (ExtensibilityElement) extElementList.next();

      QName messageQName;
      Message message;

      // If this is a soap:header element, then check for message reference
      if (extElement instanceof SOAPHeader)
      {
        SOAPHeader soapHeader = (SOAPHeader) extElement;
        if ((messageQName = soapHeader.getMessage()) != null)
        {
          // If message not found, then create a dummy message element
          if ((message = definition.getMessage(messageQName)) == null)
          {
            message = definition.createMessage();
            message.setQName(messageQName);
            message.setUndefined(true);
          }

          if (!messageSet.contains(message))
          {
            returnSet.add(message);
            saveMessage = message;
          }
        }

        // Process any header faults within this header
        Iterator headerFaultList = soapHeader.getSOAPHeaderFaults().iterator();
        while (headerFaultList.hasNext())
        {
          // Get soap header fault
          SOAPHeaderFault soapHeaderFault =
            (SOAPHeaderFault) headerFaultList.next();
          if ((messageQName = soapHeaderFault.getMessage()) != null)
          {
            // If message not found, then create a dummy message element
            if ((message = definition.getMessage(messageQName)) == null)
            {
              message = definition.createMessage();
              message.setQName(messageQName);
              message.setUndefined(true);
            }

            // If message not in message set and return set, then add it  
            if (!messageSet.contains(message)
              && ((saveMessage == null)
                || (saveMessage != null
                  && !saveMessage.getQName().equals(message.getQName()))))
              returnSet.add(message);
          }
        }
      }
    }

    return returnSet;
  }
  
  public static boolean isSOAP12WSDL(WSDLDocument wsdlDocument)
  {
	boolean result = false;
	if (wsdlDocument != null)
	{
	  Binding[] bindings = wsdlDocument.getBindings();
	  List extensibilityElementList = new ArrayList();
	  if (bindings != null)
	  {
		for (int i = 0; i < bindings.length; i++)
			extensibilityElementList.addAll(bindings[i].getExtensibilityElements());
		Iterator iterator = extensibilityElementList.iterator();
		while (iterator.hasNext()) 
		{
		  ExtensibilityElement e = (ExtensibilityElement) iterator.next();
			if (WSIConstants.NS_URI_WSDL_SOAP12.equals(e.getElementType().getNamespaceURI()))
			{
			  result = true;
			  break;
			}
		}
	  }
	}
    return result;
  }
}
