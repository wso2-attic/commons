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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * SSBP9704
 *
 * <context>For a candidate envelope</context>
 * <assertionDescription>The ENVELOPE does not contain the namespace declaration xmlns:xml="http://www.w3.org/XML/1998/namespace".</assertionDescription>
 */
public class SSBP9704 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public SSBP9704(BaseMessageValidator impl)
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
      // If the message is empty or invalid, the assertion is not applicable
      if (doc == null)
      {
        throw new AssertionNotApplicableException();
      }

      // Getting the root element
      Element elem = doc.getDocumentElement();
      // If it is not the soap:Envelope, the assertion is not applicable
      if (!elem.getNamespaceURI().equals(WSIConstants.NS_URI_SOAP)
        || !elem.getLocalName().equals(XMLUtils.SOAP_ELEM_ENVELOPE))
      {
        throw new AssertionNotApplicableException();
      }

      // If the envelope contains the xmlns:xml namespace declaration,
      // the assertion failed
      String incorrectElementName = getIncorrectElementName(elem);
      if (incorrectElementName != null)
      {
        throw new AssertionFailException("The name of an element containing "
          + "such namespace declaration is \"" + incorrectElementName + "\".");
      }
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_WARNING;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Looks for any element that contains the attribute xmlns:xml="http://www.w3.org/XML/1998/namespace". 
   * @param elem the root element.
   * @return the name of an element found or null.
   */
  private String getIncorrectElementName(Element elem)
  {
    // Getting a value of the attribute xmlns:xml
    String attrValue = elem.getAttributeNS(
      WSIConstants.NS_URI_XMLNS, "xml");
    // If it equals to "http://www.w3.org/XML/1998/namespace",
    // return an element name;
    if (WSIConstants.NS_URI_XML.equals(attrValue))
    {
      return elem.getNodeName();
    }
    // Going through element's children
    Element child = XMLUtils.getFirstChild(elem);
    while (child != null)
    {
      // If any of them has xmlns:xml attribute, return a value
      String name = getIncorrectElementName(child);
      if (name != null)
      {
        return name;
      }
      // Getting the next element's child
      child = XMLUtils.getNextSibling(child);
    }
    // No xmlns:xml attributes found, return null
    return null;
  }
}