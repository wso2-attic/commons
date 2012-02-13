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
 * WSI1110.
 * Any conformance claims MUST adhere to the WS-I conformance 
 * claim schema defined in the Basic Profile.
 */
public class WSI1110 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public WSI1110(BaseMessageValidator impl)
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
      if (claimList == null || claimList.getLength() == 0)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        for (int i = 0; i < claimList.getLength(); i++)
        {
          Element elem = (Element) claimList.item(i);
          // check "conformsTo" attribute
          Node node = elem.getAttributeNode("conformsTo");
          if (node == null)
          {
            result = AssertionResult.RESULT_FAILED;
            failureDetail =
              this.validator.createFailureDetail(
                "\nSOAP message:\n"
                  + entryContext.getMessageEntry().getMessage(),
                entryContext);
            break;
          }
          /* This is checked in WSI1111
          // check "mustUnderstand" attribute
          node = elem.getAttributeNodeNS(WSIConstants.NS_URI_SOAP, "mustUnderstand");
          if (node != null)
          {
            result = AssertionResult.RESULT_FAILED;
            failureDetail = createFailureDetail("\nSOAP message:\n" + entryContext.getMessageEntry().getMessage(), entryContext);
            break;
          }
          */
        }
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