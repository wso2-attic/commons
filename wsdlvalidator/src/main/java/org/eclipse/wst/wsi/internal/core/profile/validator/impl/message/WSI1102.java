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

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.HttpHeadersValidator;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * BPWSI4.
 * The request message should be invalid (HTTP request malformed, 
 * XML not well formed, ...).
 */
public class WSI1102 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public WSI1102(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    String httpHeader = entryContext.getMessageEntry().getHTTPHeaders();

    String requestMessage = null;
    //String responseMessage = null;
    String contentType = null;
    result = null;
    //

    try
    {
      // check HTTP status code
      String httpStatus =
        Utils.getHTTPStatusCode(
          entryContext.getMessageEntry().getHTTPHeaders());
      if ("400".equals(httpStatus))
      {

        requestMessage = entryContext.getRequest().getMessage();

        String requestHTTPHeaders =
          entryContext.getRequest().getHTTPHeaders();

        //check request HTTP Headers

        //HttpHeadersValidator validator = new HttpHeadersValidator();

        if (!HttpHeadersValidator
          .validateHttpRequestHeaders(requestHTTPHeaders))
        {
          result = AssertionResult.RESULT_PASSED;
        }

        //check request xml message

        contentType = (String) HTTPUtils.getHttpHeaderTokens(httpHeader, ":")
         .get(HTTPConstants.HEADER_CONTENT_TYPE.toUpperCase());
        if (contentType.indexOf(WSIConstants.CONTENT_TYPE_TEXT_XML) != -1)
        {
          try
          {
            if (requestMessage != null)
            {
              DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
              DocumentBuilder builder = factory.newDocumentBuilder();
              //Document doc = builder.parse(new InputSource(new StringReader(requestMessage)));
              builder.parse(
                new InputSource(new StringReader(requestMessage)));
            }

          }
          catch (SAXException e)
          {
            result = AssertionResult.RESULT_PASSED;
          }
          catch (Exception e)
          {
            new WSIException(e.getMessage(), e);
          }
        }

        if (result == null)
        {
          result = AssertionResult.RESULT_WARNING;
          failureDetail =
            this.validator.createFailureDetail(
              "\nRequest message:\nHeaders:\n"
                + entryContext.getRequest().getHTTPHeaders()
                + "Message:\n"
                + entryContext.getRequest().getMessage()
                + "\n\nResponse message:\nHeaders:\n"
                + entryContext.getResponse().getHTTPHeaders()
                + "Message:\n"
                + entryContext.getResponse().getMessage(),
              entryContext);
        }
      }
      else
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
    }
    catch (WSIException wsie)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}