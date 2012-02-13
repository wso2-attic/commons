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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingOperation;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Output;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.mime.MIMEContent;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.mime.MIMEPart;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Element;

/**
 * AP2941
 *
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>A wsdl:binding in a description binds every wsdl:part
 * of a wsdl:message in the wsdl:portType to which it refers to one of
 * soapbind:body, soapbind:header, soapbind:fault , soapbind:headerfault,
 * or mime:content.</assertionDescription>
 */
public class AP2941 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP2941(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      // Getting a wsdl:binding
      Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

      // Getting its wsdl:operation elements
      List ops = binding.getBindingOperations();

      // Going through the operation elements
      for (int i = 0; i < ops.size(); i++)
      {
        BindingOperation bindingOperation = (BindingOperation) ops.get(i);
        Input portTypeInput = bindingOperation.getOperation().getInput();
        Output portTypeOutput = bindingOperation.getOperation().getOutput();
        // If the corresponding wsdl:input exists in wsdl:portType
        // and includes the message attribute
        if (portTypeInput != null && portTypeInput.getMessage() != null)
        {
          // Getting the list of all the parts bound by wsdl:input's child elements
          List inputParts = getBindingParts(
            bindingOperation.getBindingInput().getExtensibilityElements(),
            portTypeInput.getMessage());
          // If not true that all the wsdl:partS are bound,
          // the assertion failed
          if (!inputParts
            .containsAll(portTypeInput.getMessage().getParts().keySet()))
          {
            throw new AssertionFailException("The wsdl:input of the \""
              + bindingOperation.getName() + "\" binding operation does not "
              + "bind all the corresponding wsdl:partS.");
          }
        }

        // If the corresponding wsdl:output exists in wsdl:portType
        // and includes the message attribute
        if (portTypeOutput != null && portTypeOutput.getMessage() != null)
        {
          // Getting the list of all the parts bound by wsdl:output's child elements
          List outputParts = getBindingParts(
            bindingOperation.getBindingOutput().getExtensibilityElements(),
            portTypeOutput.getMessage());
          // If not true that all the wsdl:partS are bound,
          // the assertion failed
          if (!outputParts
            .containsAll(portTypeOutput.getMessage().getParts().keySet()))
          {
            throw new AssertionFailException("The wsdl:output of the \""
              + bindingOperation.getName() + "\" binding operation does not "
              + "bind all the corresponding wsdl:partS.");
          }
        }

        // IF there are wsdl:faultS in the wsdl:portType operation
        if (!bindingOperation.getOperation().getFaults().isEmpty())
        {
          // Collecting all the soap:fault names
          List faultNames = new ArrayList();
          Collection faults = bindingOperation.getBindingFaults().values();
          // Going through all the wsdl:faultS
          Iterator it = faults.iterator();
          while (it.hasNext())
          {
            // Getting wsdl:fault's extensibility elements
            List extElems = ((BindingFault) it.next()).getExtensibilityElements();
            for (int j = 0; j < extElems.size(); j++)
            {
              if (((ExtensibilityElement)extElems.get(j))
                .getElementType().equals(WSDL_SOAP_FAULT))
              {
                faultNames.add(((SOAPFault)extElems.get(j)).getName());
              }
            }
          }
          // Going through all the soap:headerfaultS
          faultNames.addAll(findAllHeaderFaults(bindingOperation));
          // If not true that all the wsdl:faultS are bound,
          // the assertion failed
          if (!faultNames.containsAll(
            bindingOperation.getOperation().getFaults().keySet()))
          {
              throw new AssertionFailException("The binding operation \""
                  + bindingOperation.getName() + "\" does not "
                  + "bind all the corresponding wsdl:faultS.");
          }
        }
      }
    }
    catch (AssertionFailException afe)
    {
      // The assertion is "recommended", using the "warning" result
      result = AssertionResult.RESULT_WARNING;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Collects all the parts bound by extensibility elements.
   * @param extElems a lit of extensibility elements.
   * @param message the wsdl:message element corresponging
   * to the extensibility elements.
   * @return a list of wsdl:part names bound.
   */
  private List getBindingParts(List extElems, Message message)
  {
    List parts = new ArrayList();
    if (extElems != null)
    {
      // Going through the extensibility elements
      for (int i = 0; i < extElems.size(); i++)
      {
        ExtensibilityElement extElem = (ExtensibilityElement) extElems.get(i);
        // If that is a soap:body
        if (extElem.getElementType().equals(WSDL_SOAP_BODY))
        {
          // Adding all the parts bound to the list
          List pts = ((SOAPBody) extElem).getParts();
          if (pts != null)
          {
            parts.addAll(pts);
          }
          // else the parts attribute is omitted,
          // all parts defined by the message are assumed to be included
          // in the SOAP Body portion.
          else
          {
            parts.addAll(message.getParts().keySet());
          }
        }
        // else if that is a soap:header
        else if (extElem.getElementType().equals(WSDL_SOAP_HEADER))
        {
          List headerFaults = null;
          if (extElem instanceof SOAPHeader)
          {
            SOAPHeader header = (SOAPHeader) extElem;
            // If a header references the corresponding message,
            // adding part name to the list 
            if (message.getQName().equals(header.getMessage()))
              parts.add(header.getPart());

            headerFaults = header.getSOAPHeaderFaults();
          }
          // WSDL4J 1.4 does not recognize soap:header elements that are enclosed
          // in mime:multipartRelated, so using a workaround
          else
          {
            Element header =
              ((UnknownExtensibilityElement) extElem).getElement();
            // If a header references the corresponding message,
            // adding part name to the list
            if (referencesMessage(header, message.getQName()))
              parts.add(header.getAttribute("part"));
            // Collecting soap:headerfault elements for the header
            headerFaults = getHeaderFaults(header);
          }
          // Going through the soap:headerfaultS
          for (int j = 0; j < headerFaults.size(); j++)
          {
            if (headerFaults.get(j) instanceof SOAPHeaderFault)
            {
              SOAPHeaderFault shf = (SOAPHeaderFault) headerFaults.get(j);
              // If a soap:headerfault references the corresponding
              // message, adding part name to the list
              if (message.equals(shf.getMessage()))
                parts.add(shf.getPart());
            }
            // the same workaround...
            else
            {
              Element shf = (Element) headerFaults.get(j);
              // If a soap:headerfault references the corresponding
              // message, adding part name to the list
              if (referencesMessage(shf, message.getQName()))
                parts.add(shf.getAttribute("part"));
            }
          }
        }
        // else if that is a mime:content
        else if (extElem.getElementType().equals(WSDL_MIME_CONTENT))
        {
          // adding part name to the list
          parts.add(((MIMEContent) extElem).getPart());
        }
        // else if that is a mime:multipartRelated
        else if (extElem.getElementType().equals(WSDL_MIME_MULTIPART))
        {
          // Getting the mime:part elements of the mime:multipartRelated
          List mimeParts = ((MIMEMultipartRelated) extElem).getMIMEParts();
          // Going through all the mime:part elements
          for (int j = 0; j < mimeParts.size(); j++)
          {
            // Collecting all the values of part attributes
            // of mime:part's extensibility elements
            parts.addAll(getBindingParts(
              ((MIMEPart) mimeParts.get(j)).getExtensibilityElements(),
              message));
          }
        }
      }
    }
    return parts;
  }

  /**
   * Validates whether an element contains a message attribute that references
   * a message that have the qualified name specified.
   * @param elem an element to be validated.
   * @param messageName the qualified name of a message.
   * @return true if an element is valid, false otherwise.
   */
  private boolean referencesMessage(Element elem, QName messageName)
  {
    // Getting the element's message attribute
    String message = elem.getAttribute("message");
    // finding the colon delimiter
    int colonPos = message.indexOf(":");
    String ns = null;
    // Getting a local part
    String lp = colonPos > -1 ? message.substring(colonPos + 1) : message;
    // If the delimiter is found
    if (colonPos > -1)
    {
      // Retrieving a namespace URI
      ns = validator.wsdlDocument.getDefinitions()
        .getNamespace(message.substring(0, colonPos));
    }
    // If the local part and the namespace URI are the same as a message have
    if (messageName.getLocalPart().equals(lp)
      && messageName.getNamespaceURI().equals(ns))
    {
      // element is valid, return true
      return true;
    }
    // element is not valid, return false
    return false;
  }

  private List findAllHeaderFaults(BindingOperation bindingOp)
  {
    List headerFaults = new ArrayList();
    if (bindingOp == null)
      return headerFaults;
    List ioElements = bindingOp.getBindingInput().getExtensibilityElements();
    ioElements.addAll(bindingOp.getBindingOutput().getExtensibilityElements());
    for (int i = 0; i < ioElements.size(); i++)
    {
      ExtensibilityElement extElem = (ExtensibilityElement) ioElements.get(i);
      if (extElem.getElementType().equals(WSDL_SOAP_HEADER)) {
        List shfList = ((SOAPHeader) extElem).getSOAPHeaderFaults();
        for (int j = 0; j < shfList.size(); j++)
          headerFaults.add(((SOAPHeaderFault) shfList.get(j)).getPart());
      }
      else if (!extElem.getElementType().equals(WSDL_SOAP_BODY)) {
        List elList = getHeaderFaults((
                  (UnknownExtensibilityElement) extElem).getElement());
        for (int j = 0; j < elList.size(); j++)
          headerFaults.add(((Element)elList.get(j)).getAttribute("part"));
      }
    }
    return headerFaults;
  }
  /**
   * Collects all the element's child elements of the soap:headerfault type.
   * @param element an element that can have soap:headerfault elements.
   * @return the list of soap:headerfault elements found.
   */
  private List getHeaderFaults(Element element)
  {
    List headerFaults = new ArrayList();
    if (element != null)
    {
      // Getting the first header's child
      Element child = XMLUtils.getFirstChild(element);
      while (child != null)
      {
        // If the child is soap:headerfault
        if (child.getNamespaceURI().equals(WSDL_SOAP_HEADERFAULT.getNamespaceURI())
          && child.getLocalName().equals(WSDL_SOAP_HEADERFAULT.getLocalPart()))
        {
          // Adding the child to the list
          headerFaults.add(child);
        }
        // Getting the next header's child
        child = XMLUtils.getNextSibling(child);
      }
    }
    return headerFaults;
  }
}