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
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;

/**
 * BP4103
 *
 * <context>For a candidate message in the message log file</context>
 * <assertionDescription>The message contains an HTTP Authentication header field</assertionDescription>
 */
public class BP4103 extends AssertionProcess {

  private static final String HTTP_AUTH_SCHEME_BASIC = "Basic";
  private static final String HTTP_AUTH_SCHEME_DIGEST = "Digest";

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP4103(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // Getting message headers
    String headers = entryContext.getMessageEntry().getHTTPHeaders();
    // If this is a request message
    if (entryContext.getMessageEntry().getType().equals(MessageEntry.TYPE_REQUEST))
    {
      // If the request headers contain authentication scheme "Basic" or "Digest"
      // or there are no HTTP Authentication headers, the assertion is not applicable
      if (!containsInvalidAuth(headers, HTTPConstants.HEADER_AUTHORIZATION)
        && !containsInvalidAuth(headers, HTTPConstants.HEADER_PROXY_AUTHORIZATION))
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
    }
    // else this is a response
    else
    {
      // If the response headers contain authentication scheme "Basic" or "Digest"
      // or there are no HTTP Authentication headers, the assertion is not applicable
      if (!containsInvalidAuth(headers, HTTPConstants.HEADER_WWW_AUTHENTICATE)
        && !containsInvalidAuth(headers, HTTPConstants.HEADER_PROXY_AUTHENTICATE))
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
    }

    // Assertion result has not been changed, HTTP Authentication headers
    // does not contain authentication scheme "Basic" or "Digest",
    // the assertion passed
    if (result.equals(AssertionResult.RESULT_PASSED))
    {
      failureDetail = validator.createFailureDetail(
        testAssertion.getDetailDescription(), entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Checks whether HTTP headers contain HTTP Authentication headers that uses
   * authentication scheme other than "Basic" or "Digest".
   * @param headers HTTP headers.
   * @param header a header name being retrieved.
   * @return true if the HTTP Authentication header that uses authentication
   * scheme other than "Basic" or "Digest" is found, false otherwise.
   */
  private boolean containsInvalidAuth(String headers, String header)
  {
    // Getting a header value
    String headerValue = null;
    try
    {
      headerValue = (String) HTTPUtils.getHttpHeaderTokens(headers,":")
        .get(header.toUpperCase());
    }
    catch (Exception e) {}

    // If the header is presented
    if (headerValue != null)
    {
      // Retrieving authentication scheme
      int idxSP = headerValue.indexOf(" ");
      if (idxSP > -1)
      {
        headerValue = headerValue.substring(0, idxSP);
      }
      // If a scheme is neither "Basic" nor "Digest", return true
      if (!headerValue.equalsIgnoreCase(HTTP_AUTH_SCHEME_BASIC)
        && !headerValue.equalsIgnoreCase(HTTP_AUTH_SCHEME_DIGEST))
      {
        return true;
      }
    }
    return false;
  }
}