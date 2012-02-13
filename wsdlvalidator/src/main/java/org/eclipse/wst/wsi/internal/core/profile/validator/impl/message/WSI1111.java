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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.message;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * WSI1111.
 * Any conformance claims MUST NOT use soap:mustUnderstand='1'.
 */
public class WSI1111 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public WSI1111(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(TestAssertion, EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    Document doc;
    if (entryContext.getMessageEntry() != null
      && !this.validator.isOneWayResponse(entryContext)
      && (doc = entryContext.getMessageEntryDocument()) != null)
    {
      Element root = doc.getDocumentElement();
      // find "Claim" element
      NodeList claimList =
        root.getElementsByTagNameNS(WSIConstants.NS_URI_CLAIM, "Claim");
      if (claimList != null && claimList.getLength() != 0)
      {
        // if contains "Claim" element find all "Header" elements
        NodeList headerList =
          root.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "Header");
        if (headerList != null && headerList.getLength() != 0)
        {
          for (int iHeader = 0; iHeader < headerList.getLength(); iHeader++)
          {
            // find all "Header" child elements
            Node child = headerList.item(iHeader).getFirstChild();
            while (child != null)
            {
              if (child.getNodeType() == Node.ELEMENT_NODE)
              {
                // if contains "mustUnderstand" -> fail
                String attrVal =
                  ((Element) child).getAttributeNS(
                    WSIConstants.NS_URI_SOAP,
                    "mustUnderstand");
                if (attrVal != null && !"".equals(attrVal))
                {
                  result = AssertionResult.RESULT_FAILED;
                  failureDetail =
                    this.validator.createFailureDetail(
                      "\nSOAP message:\n"
                        + entryContext.getMessageEntry().getMessage(),
                      entryContext);
                  iHeader = headerList.getLength();
                  break;
                }
              }
              child = child.getNextSibling();
            }
          }
        }
      }
      else
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
    }
    else
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}