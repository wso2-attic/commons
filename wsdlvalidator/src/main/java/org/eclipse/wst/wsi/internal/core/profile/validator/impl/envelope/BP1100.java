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
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * BP1100.
 * A message that does not contain a SOAP fault SHOULD use a "200 OK" 
 * HTTP status code.
 */
public class BP1100 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1100(BaseMessageValidator impl)
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
    if (this.validator.isOneWayResponse(entryContext))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // look for <soap:Fault> element:
      Document doc = entryContext.getMessageEntryDocument();
      if (doc == null)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        Element root = doc.getDocumentElement();
        NodeList faultList =
          root.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "Fault");

        // If response does not contain a soap:Fault, then check it
        if ((faultList == null) || (faultList.getLength() == 0))
        {
          // Response does not contain a soap:Fault
          // check HTTP status code
          String httpStatus =
            Utils.getHTTPStatusCode(
              entryContext.getMessageEntry().getHTTPHeaders());
          if (!"200".equals(httpStatus))
          {
            result = AssertionResult.RESULT_WARNING;
            //failureDetailMessage = entryContext.getMessageEntry().getMessage();
            failureDetail =
              this.validator.createFailureDetail(
                "\nResponse message:\nHeaders:\n"
                  + entryContext.getMessageEntry().getHTTPHeaders()
                  + "\nMessage:\n"
                  + entryContext.getMessageEntry().getMessage(),
                entryContext);
          }
        }

        // If it is a soap:Fault, then the result must be notApplicable
        else
        {
          result = AssertionResult.RESULT_NOT_APPLICABLE;
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}