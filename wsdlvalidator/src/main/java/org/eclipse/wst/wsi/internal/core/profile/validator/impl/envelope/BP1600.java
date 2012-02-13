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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.envelope;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;


/**
 * BP1600
 *
 * <context>For a candidate envelope</context>
 * <assertionDescription>The envelope conforms to the structure specified in SOAP 1.1 Section 4.</assertionDescription>
 */
public class BP1600 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1600(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      // Getting a message document
      Document doc = entryContext.getMessageEntryDocument();

      // If the message is empty or invalid, the assertion failed
      if (doc == null)
      {
      	if (this.validator.isOneWayResponse(entryContext))
      		throw new AssertionNotApplicableException();
        else 
        	throw new AssertionFailException("The log message is empty or invalid.");
      }
      // SOAP 1.1 specifications, Section 4.
      // http://www.w3.org/TR/2000/NOTE-SOAP-20000508/#_Toc478383494

      // The namespace identifier for the elements and attributes defined
      // in this section is "http://schemas.xmlsoap.org/soap/envelope/".

      //  o The Envelope is the top element of the XML document representing the message.

      // Rule 1. Envelope
      //  o The element name is "Envelope".
      //  o The element MUST be present in a SOAP message
      //  o The element MAY contain namespace declarations as well as additional attributes.
      //    If present, such additional attributes MUST be namespace-qualified. Similarly,
      //    the element MAY contain additional sub elements. If present these elements
      //    MUST be namespace-qualified and MUST follow the SOAP Body element.

      // Getting the top element
      Element element = doc.getDocumentElement();

      // Assuming that the top element should be soap:Envelope
      // If not, the assertion failed
      if (!element.getLocalName().equals(XMLUtils.SOAP_ELEM_ENVELOPE)
        || !isSOAPNamespace(element.getNamespaceURI()))
        throw new AssertionFailException("The top element is not soap:Envelope");

      // Collecting all the namespace declarations
      Collection envelopeNamespaces = collectNamespaces(element.getAttributes());
      envelopeNamespaces.add(WSIConstants.NS_URI_XMLNS);

      // Getting an attribute that is not in any of the namespaces
      Attr notValidAttr = getNotValidAttr(
        element.getAttributes(),
        envelopeNamespaces);

      // If found one, the assertion failed
      if (notValidAttr != null)
        throw new AssertionFailException("The attribute "
          + notValidAttr.getNodeName() + " is not namespace-qualified");


      // Rule 2. Header 
      //  o The element name is "Header".
      //  o The element MAY be present in a SOAP message. If present, the element
      //    MUST be the first immediate child element of a SOAP Envelope element.
      //  o The element MAY contain a set of header entries each being an immediate
      //    child element of the SOAP Header element. All immediate child elements
      //    of the SOAP Header element MUST be namespace-qualified.

      // Getting the first sub element of the envelope
      element = XMLUtils.getFirstChild(element);
      Collection headerNamespaces = collectNamespaces(element.getAttributes());

      // If the child is soap:Header
      if (element != null
        && element.getLocalName().equals(XMLUtils.SOAP_ELEM_HEADER)
        && isSOAPNamespace(element.getNamespaceURI()))
      {

        // Going through all the Header entries
        Element headerEntry = XMLUtils.getFirstChild(element);
        while (headerEntry != null)
        {
          // Collecting all the namespaces for the current entry
          Collection headerEntryNamespaces = collectNamespaces(
            headerEntry.getAttributes());

          // If the entry is not in the namespaces, the assertion failed
          if (!envelopeNamespaces.contains(headerEntry.getNamespaceURI())
            && !headerNamespaces.contains(headerEntry.getNamespaceURI())
            && !headerEntryNamespaces.contains(headerEntry.getNamespaceURI()))
            throw new AssertionFailException("The header entry "
              + headerEntry.getNodeName() + " is not namespace-qualified");

          // Getting the next Header entry
          headerEntry = XMLUtils.getNextSibling(headerEntry);
        }

        // Getting the next sub element of the envelope
        element = XMLUtils.getNextSibling(element);
      }


      // Rule 3. Body
      //  o The element name is "Body".
      //  o The element MUST be present in a SOAP message and MUST be an immediate
      //    child element of a SOAP Envelope element. It MUST directly follow the
      //    SOAP Header element if present. Otherwise it MUST be the first immediate
      //    child element of the SOAP Envelope element.
      //  o The element MAY contain a set of body entries each being an immediate
      //    child element of the SOAP Body element. Immediate child elements of the
      //    SOAP Body element MAY be namespace-qualified. SOAP defines the SOAP Fault
      //    element, which is used to indicate error messages.

      // if the SOAP Body element is not presented, the assertion failed
      if (element == null
        || !element.getLocalName().equals(XMLUtils.SOAP_ELEM_BODY)
        || !isSOAPNamespace(element.getNamespaceURI()))
        throw new AssertionFailException("The soap:Body element is not presented "
          + "or follows an additional sub element of soap:Envelope");

      // Processing all other sub elements of the envelope
      element = XMLUtils.getNextSibling(element);
      while (element != null)
      {
        // Checking for the SOAP Header element
        if (element.getLocalName().equals(XMLUtils.SOAP_ELEM_HEADER)
          && isSOAPNamespace(element.getNamespaceURI()))
            throw new AssertionFailException(
              "The soap:Header element cannot follow the soap:Body element");

        // Collecting all the namespaces for the current element
        Collection elementNamespaces = collectNamespaces(
          element.getAttributes());

        // If the element is not in the namespaces, the assertion failed
        if (!envelopeNamespaces.contains(element.getNamespaceURI())
          && !elementNamespaces.contains(element.getNamespaceURI()))
          throw new AssertionFailException("The sub envelope element "
            + element.getNodeName() + " is not namespace-qualified");

        // Getting the next sub element of the envelope
        element = XMLUtils.getNextSibling(element);
      }
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }

    catch (AssertionNotApplicableException anae)
	{
	  result = AssertionResult.RESULT_NOT_APPLICABLE;
	}
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Collects all the namespace declarations from attributes collection
   * @param attrs an attributes collection
   * @return a collection of namespaces
   */
  private Collection collectNamespaces(NamedNodeMap attrs)
  {
    Collection ns = new HashSet();
    if (attrs != null)
    {
      for(int i = 0; i < attrs.getLength(); i++)
      {
        Attr attr = (Attr) attrs.item(i);
        if (attr.getNamespaceURI() != null
          && attr.getNamespaceURI().equals(WSIConstants.NS_URI_XMLNS))
          ns.add(attr.getValue());
      }
    }

    return ns;
  }

  /**
   * Retrieves an attribute that is not in namespaces
   * @param attrs an attributes collection
   * @param ns a namespaces collection
   * @return an attribtue that is not in any of the namespaces specified
   */
  private Attr getNotValidAttr(NamedNodeMap attrs, Collection ns)
  {
    Attr ret = null;
    if (attrs != null)
    {
      for(int i = 0; i < attrs.getLength(); i++)
      {
        Attr attr = (Attr) attrs.item(i);
        if (attr.getNamespaceURI() == null
          || !ns.contains(attr.getNamespaceURI()))
        {
          ret = attr;
          break;
        }
      }
    }
    return ret;
  }

  /**
   * Qualifies whether a namespace is the SOAP namespace
   * "http://schemas.xmlsoap.org/soap/envelope/"
   * @param ns a namespace to be qualified
   * @return true if the namespace is qualified, false otherwise
   */
  private boolean isSOAPNamespace(String ns)
  {
    if (ns == null)
      return false;

    return ns.equals(WSIConstants.NS_URI_SOAP);
  }
}