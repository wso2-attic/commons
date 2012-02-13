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

import com.ibm.icu.util.StringTokenizer;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP1004. 
 * The request message is a POST message, without any use of framework extension.
 */
public class BP1004 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1004(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    String httpHeader = entryContext.getMessageEntry().getHTTPHeaders();
    StringTokenizer httpMessageTokenizer =
      new StringTokenizer(httpHeader, "\n\r\f");

    // Check if this is a POST message
    if (!httpHeader.startsWith(MessageValidator.HTTP_POST))
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(httpHeader, entryContext);
    }

    String line = null;
    while (httpMessageTokenizer.hasMoreTokens()
      && (result.equals(AssertionResult.RESULT_PASSED)))
    {
      line = httpMessageTokenizer.nextToken();
      if ((line != null)
        && (line.startsWith("M-POST") || line.startsWith("Ext:")))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = this.validator.createFailureDetail(httpHeader, entryContext);
      }
    } //End While

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}