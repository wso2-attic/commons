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

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
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
import org.w3c.dom.NodeList;


/**
 * BP1308.
 * The children elements of soap:Body do not have a soap:encodingStyle attribute.
 */
public class BP1308 extends AssertionProcess implements WSITag
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1308(BaseMessageValidator impl)
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

      // look for soap:Body elements:
      NodeList soapBodyList = doc.getElementsByTagNameNS(
        ELEM_SOAP_BODY.getNamespaceURI(), ELEM_SOAP_BODY.getLocalPart());

      // The message does not contain any soap:Body, the assertion is not applicable
      if ((soapBodyList == null) || (soapBodyList.getLength() == 0))
        throw new AssertionNotApplicableException();

      // check that no soap:Body child elements contain a soap:encodingStyle attribute
      // Getting the first soap:Body child element
      Element child =
        XMLUtils.getFirstChild((Element) soapBodyList.item(0));
      while (child != null)
      {
        if (child.getAttributeNodeNS(
          ELEM_SOAP_BODY.getNamespaceURI(), "encodingStyle") != null)
        {
          throw new AssertionFailException("The child element name is \"" +
            child.getNodeName() + "\".");
        }
        // Getting the next soap:Body child element
        child = XMLUtils.getNextSibling(child);
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

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}