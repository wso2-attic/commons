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

import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP1001.
 * If it is a request, the arg #2 of POST is &lt;HTTP/1.1&gt;. If absent, first line of the 
 * body is: HTTP-Version = HTTP/1.1. If it is a response, it starts with &lt;HTTP/1.1&gt;.
 */
public class BP1001 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1001(BaseMessageValidator impl)
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

    // If this is a request message, then check POST header
    if (entryContext
      .getMessageEntry()
      .getType()
      .equalsIgnoreCase(MessageEntry.TYPE_REQUEST))
    {
      Vector requestLine;
      if (((requestLine = this.validator.getPostRequest(httpHeader)) == null)
        || (requestLine.size() == 0))
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        String method = (String) requestLine.get(0);
        //String requestURI = (String) requestLine.get(1);
        String httpVersion = (String) requestLine.get(2);

        //For each request message that is an HTTP POST
        if (method.equals(MessageValidator.HTTP_POST) && httpVersion != null)
        {
          if (httpVersion.equals(MessageValidator.HTTP_VERSION_1_1))
          {
            result = AssertionResult.RESULT_PASSED;
          }
          else
          {
            result = AssertionResult.RESULT_WARNING;
            failureDetail = this.validator.createFailureDetail(httpHeader, entryContext);
          }
        }
      }
    }

    // Otherwise it must be a response
    else
    {
      if (httpHeader.startsWith(MessageValidator.HTTP_VERSION_1_1))
      {
        result = AssertionResult.RESULT_PASSED;
      }
      else
      {
        result = AssertionResult.RESULT_WARNING;
        failureDetail = this.validator.createFailureDetail(httpHeader, entryContext);
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}