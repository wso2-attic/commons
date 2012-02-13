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
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.Utils;


/**
 * BP1103.
 * The request message should not contain a POST method.
 */
public class BP1103 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1103(BaseMessageValidator impl)
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

    MessageEntry request = entryContext.getRequest();
    String requestHeaders = request.getHTTPHeaders();

    // Get HTTP status code
    String httpStatus =
      Utils.getHTTPStatusCode(
        entryContext.getMessageEntry().getHTTPHeaders());

    if (requestHeaders.startsWith("POST "))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else if (!"405".equals(httpStatus))
    {
      result = AssertionResult.RESULT_WARNING;
      failureDetail =
        this.validator.createFailureDetail(
          "\nRequest message:\nHeaders:\n"
            + entryContext.getRequest().getHTTPHeaders()
            + "\nMessage:\n"
            + entryContext.getRequest().getMessage()
            + "\n\nResponse message:\nHeaders:\n"
            + entryContext.getResponse().getHTTPHeaders()
            + "\nMessage:\n"
            + entryContext.getResponse().getMessage(),
          entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}