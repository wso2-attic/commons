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
 * SSBP5101
 * <context>For a candidate message in the log file.</context>
 * <assertionDescription>A message must have a "Content-Type" HTTP header field.  The "Content-Type" HTTP header field must have a field-value whose media type is "text/xml".</assertionDescription>
 * 
 * @author lauzond
 */
public class SSBP5101 extends AssertionProcess {

  protected final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public SSBP5101(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    // getting Content-Type HTTP header
    String contentType = (String) HTTPUtils.getHttpHeaderTokens(
      entryContext.getMessageEntry().getHTTPHeaders(),
      ":").get(HTTPConstants.HEADER_CONTENT_TYPE.toUpperCase());

    // if Content-Type header is not presented
    // or does not equal to text/xml, then the assertion is failed
    if (contentType == null
      || !contentType.startsWith(WSIConstants.CONTENT_TYPE_TEXT_XML))
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        testAssertion.getFailureMessage(),
        entryContext);
    }
    return this.validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}