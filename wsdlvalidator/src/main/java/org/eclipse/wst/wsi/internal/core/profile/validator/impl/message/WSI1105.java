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
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.SetCookie2Validator;


/**
 * BPWSI4.
 * The Cookies should conform to RFC2965.
 */
public class WSI1105 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public WSI1105(BaseMessageValidator impl)
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

    SetCookie2Validator cookieVal = new SetCookie2Validator();
    String headers = entryContext.getMessageEntry().getHTTPHeaders();
    if (entryContext
      .getMessageEntry()
      .getType()
      .equalsIgnoreCase(MessageEntry.TYPE_RESPONSE))
    {
      // if response trom server
      String setCookie2 =
        (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("Set-Cookie2".toUpperCase());
      if (setCookie2 == null)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        if (cookieVal.isSetCookie2(setCookie2.trim()))
          result = AssertionResult.RESULT_PASSED;
        else
        {
          result = AssertionResult.RESULT_WARNING;
          failureDetail =
            this.validator.createFailureDetail(
              "\nMessage:\nHeaders:\n"
                + entryContext.getMessageEntry().getHTTPHeaders()
                + "\nSOAP message:\n"
                + entryContext.getMessageEntry().getMessage(),
              entryContext);
        }
      }
    }
    else
    {
      // if request to the server
      String cookie =
        (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("Cookie");
      if (cookie == null)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        if (cookieVal.isCookie(cookie.trim()))
          result = AssertionResult.RESULT_PASSED;
        else
        {
          result = AssertionResult.RESULT_WARNING;
          failureDetail =
            this.validator.createFailureDetail(
              "\nMessage:\nHeaders:\n"
                + entryContext.getMessageEntry().getHTTPHeaders()
                + "\nSOAP message:\n"
                + entryContext.getMessageEntry().getMessage(),
              entryContext);
        }
      }
    }
    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}