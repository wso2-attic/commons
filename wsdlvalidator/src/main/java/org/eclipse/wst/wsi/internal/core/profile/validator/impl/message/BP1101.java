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

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.Utils;


/**
 * BP1101.
 * A response message that does not contain a SOAP message SHOULD be sent 
 * using either a "200 OK" or "202 Accepted" HTTP status code.
 */
public class BP1101 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1101(BaseMessageValidator impl)
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

    // response does not contains SOAP message
    if (entryContext.getMessageEntry().getMessage() == null
      || "".equals(entryContext.getMessageEntry().getMessage()))
    {
      // check HTTP status code 
      String httpStatus =
        Utils.getHTTPStatusCode(
          entryContext.getMessageEntry().getHTTPHeaders());
      if ("200".equals(httpStatus));
      else if ("202".equals(httpStatus));

      // If 4xx, then result is notApplicable
      else if (httpStatus.startsWith("4"))
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        result = AssertionResult.RESULT_WARNING;
        failureDetail =
          this.validator.createFailureDetail(
            "\nResponse message:\nHeaders:\n"
              + entryContext.getMessageEntry().getHTTPHeaders()
              + "Message:\n"
              + entryContext.getMessageEntry().getMessage(),
            entryContext);
      }
    }
    else
    {
      // response contains SOAP message
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}