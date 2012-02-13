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
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP1002.
 * If it is a request, the arg #2 of POST is &lt;HTTP/1.1&gt; or &lt;HTTP/1.0&gt;. If absent, first line 
 * of the body is: HTTP-Version = HTTP/1.1. (or HTTP/1.0). If it is a response, it starts with 
 * &lt;HTTP/1.1&gt; or &lt;HTTP/1.0&gt; or higher;
 */
public class BP1002 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1002(BaseMessageValidator impl)
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

    // If this is a request message, then check POST for 1.1 or 1.0
    if (entryContext
      .getMessageEntry()
      .getType()
      .equalsIgnoreCase(MessageEntry.TYPE_REQUEST))
    {
      // Get each entry in the post header
      StringTokenizer postMessage = new StringTokenizer(httpHeader, " \r\n");

      // If there is no third token, then fail
      if (postMessage.countTokens() < 3)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = this.validator.createFailureDetail(httpHeader, entryContext);
      }
      else
      {
        // Get the 3rd token (there must be a better way to do this?)
        String httpVersion = "";
        String messageType = "";
        for (int i = 0; i < 3; i++)
        {
          // Message type
          if (i == 0)
            messageType = postMessage.nextToken();
          else if (i == 2)
            httpVersion = postMessage.nextToken();
          else
            postMessage.nextToken();
        }

        if (!messageType.equals("POST"))
        {
          result = AssertionResult.RESULT_NOT_APPLICABLE;
        }

        // If the third token is not HTTP/1.1 ot HTTP/1.0, then fail
        else if (
          (!httpVersion.equalsIgnoreCase(MessageValidator.HTTP_VERSION_1_1))
            && (!httpVersion.equalsIgnoreCase(MessageValidator.HTTP_VERSION_1_0)))
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail = this.validator.createFailureDetail(httpHeader, entryContext);
        }
      }
    }

    // Else it is a response message
    else
    {
      // Response-Line (1st) = HTTP-Version SP Response-code SP Response-text CRLF
      StringTokenizer httpMessageTokenizer =
        new StringTokenizer(httpHeader, "\n\r\f");

      String startLine = null;
      try
      {
        if (httpMessageTokenizer.hasMoreTokens())
        {
          startLine = httpMessageTokenizer.nextToken();
        }
        else
        {
          // HTTP version info expected but not found
          // (state the fact in the exception message?)
          throw new AssertionFailException(httpHeader);
        }

        // check that protocol version indicates HTTP/1.0 or HTTP/1.1
        if (!startLine.startsWith(MessageValidator.HTTP_VERSION_1_1)
          && !startLine.startsWith(MessageValidator.HTTP_VERSION_1_0))
        {
          throw new AssertionFailException(httpHeader);
        }
      }
      catch (AssertionFailException e)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = this.validator.createFailureDetail(e.getMessage(), entryContext);
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}