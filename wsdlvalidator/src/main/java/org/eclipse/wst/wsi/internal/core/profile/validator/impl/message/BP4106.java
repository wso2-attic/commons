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
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;


/**
 * BP4106
 * <context>For a candidate response message in the message log file that contains a transfer-encoding HTTP header field.</context>
 * <assertionDescription>The contained transfer-encoding HTTP header field has a value of "chunked"</assertionDescription>
 */
public class BP4106 extends AssertionProcess {

  private static final String CHUNKED_VALUE = "chunked";

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP4106(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    // Getting a Transfer-Encoding HTTP header value
    String transferEncoding = (String) HTTPUtils.getHttpHeaderTokens(
      entryContext.getMessageEntry().getHTTPHeaders(),
      ":").get(HTTPConstants.HEADER_TRANSFER_ENCODING.toUpperCase());

    if (transferEncoding != null)
    {
      // Lowering value's case because RFC2616 says
      // that all transfer-coding values are case-insensitive
      transferEncoding = transferEncoding.toLowerCase();

      // If the value is other than "chunked", then the assertion passed
      if (!transferEncoding.equals(CHUNKED_VALUE)) {
        failureDetail = validator.createFailureDetail(
          testAssertion.getDetailDescription(), entryContext);
      }
    }

    // If there is no Tranfer-Encoding or its value is "chunked",
    // then the assertion is not applicable 
    if (failureDetail == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}