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
import java.util.List;

import javax.wsdl.Binding;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * AP2908
 *
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>The mime:part element in a DESCRIPTION does not have a name attribute.</assertionDescription>
 */
public class AP2908 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP2908(WSDLValidatorImpl impl)
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
      Binding wsdlBinding = (Binding) entryContext.getEntry().getEntryDetail();

      // Since WSDL4J 1.4 ignores any attributes of mime:part, use Xerces 2.6.2 instead
      Document doc = entryContext.getWSDLDocument().getDocument();
      if (doc == null)
      {
        doc = validator.parseXMLDocumentURL(validator.wsdlDocument.getLocation(), null);
      }

      // Finding the wsdl:binding element being processed
      Element binding = getBindingElement(
        doc.getDocumentElement(), wsdlBinding.getQName().getLocalPart());

      List ops = getChildElements(binding, WSDL_OPERATION);

      // A variable that indicates a binding contains at least one
      // mime:multipartRelated element
      boolean multipartsFound = false;

      // Going through the operation elements
      for (int i = 0; i < ops.size(); i++)
      {
        Element bindingOperation = (Element) ops.get(i);

        // Getting wsdl:input and wsdl:output elements of an operation
        Element bindingInput = getChildElement(bindingOperation, WSDL_INPUT);
        Element bindingOutput = getChildElement(bindingOperation, WSDL_OUTPUT);

        // Collecting all the mime:multipartRelated elements from wsdl:input and wsdl:output
        List inputMultiparts = getMimeMultipartElements(bindingInput);
        List outputMultiparts = getMimeMultipartElements(bindingOutput);

        // If the wsdl:input contains mime:multipartRelated elements
        if (!inputMultiparts.isEmpty())
        {
          multipartsFound = true;

          // If there is a mime:part element containing a name attribute,
          // the assertion failed
          if (containsInvalidMimePart(inputMultiparts))
          {
            throw new AssertionFailException("The invalid "
                + "mime:part element is in the wsdl:input of the \""
                + bindingOperation.getAttribute(WSIConstants.ATTR_NAME)
                + "\" binding operation.");
          }
        }

        // If the wsdl:output contains mime:multipartRelated elements
        if (!outputMultiparts.isEmpty())
        {
          multipartsFound = true;

          // If there is a mime:part element containing a name attribute,
          // the assertion failed
          if (containsInvalidMimePart(outputMultiparts))
          {
            throw new AssertionFailException("The invalid "
                + "mime:part element is in the wsdl:output of the \""
                + bindingOperation.getAttribute(WSIConstants.ATTR_NAME)
                + "\" binding operation.");
          }
        }
      }
      // If the binding contains no one mime:multipartRelated element,
      // the assertion is not applicable
      if (!multipartsFound)
        result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }
    catch (Exception ioe)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Validates mime:part elements for each of mime:multipartRelated element.
   * @param multiparts a list of mime:multipartRelated elements.
   * @return true, if any mime:part contains a name attribute, false otherwise.
   */
  private boolean containsInvalidMimePart(List multiparts) {
    // Going through a list of mime:multipartRelated elements
    for (int i = 0; i < multiparts.size(); i++)
    {
      // Getting a list of mime:part elements
      List mimeParts =
        getChildElements((Element) multiparts.get(i), WSDL_MIME_PART);
      // Going through all the mime:part elements
      for (int j = 0; j < mimeParts.size(); j++)
      {
        Element mimePart = (Element) mimeParts.get(j);
        // If the mime:part element contains a name attribute,
        // return true
        if (mimePart.getAttributeNode(WSIConstants.ATTR_NAME) != null)
        {
          return true;
        }
      }
    }
    // No one invalid mime:part element is found, return false
    return false;
  }

  /**
   * Collects all mime:multipartRelated elements.
   * @param parent an element which the child elements are gathered from.
   * @return the list of mime:multipartRelated elements found.
   */
  private List getMimeMultipartElements(Element parent)
  {
    List mimeMultipartElements = new ArrayList();
    // If the parent is not null
    if (parent != null)
    {
      // Getting the first parent's child
      Element child = XMLUtils.getFirstChild(parent);
      while (child != null)
      {
        // If the child is a mime:multipartRelated element
        if (child.getNamespaceURI().equals(WSDL_MIME_MULTIPART.getNamespaceURI())
          && child.getLocalName().equals(WSDL_MIME_MULTIPART.getLocalPart()))
        {
          // Adding the element to the list being returned
          mimeMultipartElements.add(child);

          // Getting mime:partS from the element
          List mimeParts = getChildElements(child, WSDL_MIME_PART);
          // Going through all the mime:part elements
          for (int i = 0; i < mimeParts.size(); i++)
          {
            // Collecting all the mime:multipartRelated elements of this mime:part
            List elems = getMimeMultipartElements((Element) mimeParts.get(i));
            // Adding the elements to the list being returned
            mimeMultipartElements.addAll(elems);
          }
        }
        // Getting the next child
        child = XMLUtils.getNextSibling(child);
      }
    }

    return mimeMultipartElements;
  }

  /**
   * Looks for an element's child element.
   * @param parent a parent element.
   * @param childName a qualified element name being found.
   * @return an element or null if it is not found.
   */
  private Element getChildElement(Element parent, QName childName)
  {
    // Getting the first parent's child
    Element child = XMLUtils.getFirstChild(parent);
    while (child != null)
    {
      // If the child has the required qualified name
      if (child.getNamespaceURI().equals(childName.getNamespaceURI())
        && child.getLocalName().equals(childName.getLocalPart()))
      {
        // return the child
        return child;
      }
      // Getting the next child
      child = XMLUtils.getNextSibling(child);
    }
    return null;
  }

  /**
   * Collects element's child elements.
   * @param parent a parent element.
   * @param childName a qualified element name being found.
   * @return a list of elements found.
   */
  private List getChildElements(Element parent, QName childName)
  {
    List children = new ArrayList();
    if (parent != null)
    {
      // Getting the first parent's child
      Element child = XMLUtils.getFirstChild(parent);
      while (child != null)
      {
        // If the child has the required qualified name
        if (child.getNamespaceURI().equals(childName.getNamespaceURI())
          && child.getLocalName().equals(childName.getLocalPart()))
        {
          // Adding the child to the list
          children.add(child);
        }
        // Getting the next binding's child
        child = XMLUtils.getNextSibling(child);
      }
    }
    return children;
  }

  /**
   * Looks for wsdl:binding element.
   * @param definitions a wsdl:definitions element.
   * @param bindingName a name of wsdl:binding element.
   * @return a wsdl:binding element or null if it is not found.
   */
  private Element getBindingElement(Element definitions, String bindingName)
  {
    // Getting the first definitions' child
    Element child = XMLUtils.getFirstChild(definitions);
    while (child != null)
    {
      // If definitions' child is wsdl:binding element
      // and is the same that is being processed by WSDLValidator
      if (child.getNamespaceURI().equals(WSDL_BINDING.getNamespaceURI())
        && child.getLocalName().equals(WSDL_BINDING.getLocalPart())
        && child.getAttribute(WSIConstants.ATTR_NAME).equals(bindingName))
      {
        // return the wsdl:binding element
        return child;
      }
      // Getting the next definitions' child
      child = XMLUtils.getNextSibling(child);
    }
    // return null, is there is no such wsdl:binding
    return null;
  }
}