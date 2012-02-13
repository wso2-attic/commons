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
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.Utils;


/**
 * BP1104.
 * The request message should not contain a content-type of "text/xml".
 */
public class BP1104 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1104(BaseMessageValidator impl)
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
    // check request for the "text/xml" content type
    MessageEntry request = entryContext.getRequest();
    String headers = request.getHTTPHeaders();
    String contentType = (String) HTTPUtils.getHttpHeaderTokens(headers, ":")
      .get(HTTPConstants.HEADER_CONTENT_TYPE.toUpperCase());

    // Get HTTP status code
    String httpStatus =
      Utils.getHTTPStatusCode(
        entryContext.getMessageEntry().getHTTPHeaders());

    // If the request message has a content type of text/xml,
    // then the assertion is not applicable
    if (contentType != null
      && contentType.indexOf(WSIConstants.CONTENT_TYPE_TEXT_XML) != -1)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // check HTTP status code
      if (!"415".equals(httpStatus))
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
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}