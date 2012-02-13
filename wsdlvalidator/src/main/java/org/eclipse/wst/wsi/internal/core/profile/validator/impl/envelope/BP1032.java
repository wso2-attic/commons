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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;


/**
 * BP1032
 *
 * <context>For a candidate envelope</context>
 * <assertionDescription>The soap:Envelope, soap:Header, and soap:Body elements do not have any attributes in the namespace "http://schemas.xmlsoap.org/soap/envelope/"</assertionDescription>
 */
public class BP1032 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1032(BaseMessageValidator impl)
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
        throw new AssertionNotApplicableException();

      // Getting a root element
      Element element = doc.getDocumentElement();

      // Assuming that root element should be soap:Envelope
      // If not, the assertion is not applicable
      if (!element.getLocalName().equals(XMLUtils.SOAP_ELEM_ENVELOPE))
        throw new AssertionNotApplicableException();

      // looking for any attribute of soap:Envelope in the namespace
      // "http://schemas.xmlsoap.org/soap/envelope/"
      // If at least one attribute is found, the assertion failed
      Attr attr = getAttribute(element.getAttributes());
      if (attr != null)
        throw new AssertionFailException("soap:Envelope attribute name is "
          + attr.getName());

      // Getting the first child of soap:Envelope
      element = XMLUtils.getFirstChild(element);

      // If the child is soap:Header
      if (element != null
        && element.getLocalName().equals(XMLUtils.SOAP_ELEM_HEADER))
      {
        // looking for any attribute of soap:Header in the namespace
        // "http://schemas.xmlsoap.org/soap/envelope/"
        // If at least one attribute is found, the assertion failed
        attr = getAttribute(element.getAttributes());
        if (attr != null)
          throw new AssertionFailException("soap:Header attribute name is "
            + attr.getName());

        // Moving to the next soap:Envelope child
        element = XMLUtils.getNextSibling(element);
      }

      // If the child is soap:Body
      if (element != null
        && element.getLocalName().equals(XMLUtils.SOAP_ELEM_BODY))
      {
        // looking for any attribute of soap:Body in the namespace
        // "http://schemas.xmlsoap.org/soap/envelope/"
        // If at least one attribute is found, the assertion failed
        attr = getAttribute(element.getAttributes());
        if (attr != null)
          throw new AssertionFailException("soap:Body attribute name is "
            + attr.getName());
      }

    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Looks for an attribute that is in the namespace
   * http://schemas.xmlsoap.org/soap/envelope/
   * @param attrs a collection of attributes
   * @return an attribute in the specified namespace or null
   */
  private Attr getAttribute(NamedNodeMap attrs)
  {
    Attr ret = null;
    if (attrs != null)
    {
      for(int i = 0; i < attrs.getLength(); i++)
      {
        Attr attr = (Attr) attrs.item(i);
        if (attr.getNamespaceURI() != null
          && attr.getNamespaceURI().equals(WSIConstants.NS_URI_SOAP))
        {
          ret = attr;
          break;
        }
      }
    }

    return ret;
  }
}