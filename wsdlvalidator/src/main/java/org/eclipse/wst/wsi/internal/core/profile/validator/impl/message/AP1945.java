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
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;

/**
 * AP1945
 * <context>For a candidate message</context>
 * <assertionDescription>The Content-Type HTTP header field-value in a message 
 * is either "multipart/related" or "text/xml".</assertionDescription>
 */
public class AP1945 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public AP1945(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    String headerName = HTTPConstants.HEADER_CONTENT_TYPE;

    // Getting a header
    String headerValue = HTTPUtils.getHttpHeaderAttribute(
        entryContext.getMessageEntry().getHTTPHeaders(),
        headerName);
    // If headerValue is null, the assertion is not applicable
    if (headerValue == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    } 
    // If the header is "multipart/related" or "text/xml",
    // then the assertion passed
    else if (headerValue.equalsIgnoreCase(WSIConstants.CONTENT_TYPE_MULTIPART)
        || headerValue.equalsIgnoreCase(WSIConstants.CONTENT_TYPE_TEXT_XML))
    {
    }
    // else the assertion failed
    else {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
          headerValue, entryContext);
    }
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}