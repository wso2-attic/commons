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
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * BP1307.
 * The elements of the message that are namespaced 
 * "http://schemas.xmlsoap.org/soap/envelope/" do not have a soap:encodingStyle attribute.
 */
public class BP1307 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1307(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    Document doc;
    // Check if this is one way response
    // or message is mepty or invalid
    if (this.validator.isOneWayResponse(entryContext)
      || (doc = entryContext.getMessageEntryDocument()) == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // look for <soap:xxx> elements:
      NodeList soapList =
        doc.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "*");
      if ((soapList == null) || (soapList.getLength() == 0))
      {
        // Response does not contain any soap envelope elements
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        // check that no <soap:xxx> elements contains a soap:encodingStyle attribute
        // For each <soap:xxx>
        try
        {
          for (int n = 0; n < soapList.getLength(); n++)
          {
            Element nextElem = (Element) soapList.item(n);
            if (nextElem
              .getAttributeNodeNS(WSIConstants.NS_URI_SOAP, "encodingStyle")
              != null)
            {
              // Assertion failed
              throw new AssertionFailException(
                entryContext.getMessageEntry().getMessage());
            }
          }
        }
        catch (AssertionFailException e)
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail = this.validator.createFailureDetail(e.getMessage(), entryContext);
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}