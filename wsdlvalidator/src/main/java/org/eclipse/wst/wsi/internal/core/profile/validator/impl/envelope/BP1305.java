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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.envelope;

import java.net.HttpURLConnection;
import com.ibm.icu.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
   * BP1305.
   * It is in an HTTP 500 message with "Server" Error code.
   */
public class BP1305 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1305(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    Document doc;
    // Check if this is one way response
    // or message is mepty or invalid
    if (this.validator.isOneWayResponse(entryContext)
      || (doc = entryContext.getMessageEntryDocument()) == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // look for <soap:Fault> element:
      NodeList faultList =
        doc.getElementsByTagNameNS(
          WSIConstants.NS_URI_SOAP,
          XMLUtils.SOAP_ELEM_FAULT);
      if ((faultList == null) || (faultList.getLength() == 0))
      {
        // Response does not contain a soap:fault
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        // we have a soap:Fault. Check that it is contained in a HTTP 500 message
        // GT : base HTTP Header parsing on BP1001 - this should be factored to a 
        // general-purpose HTTP message parsing method or methods (request & response)
        String httpHeader = entryContext.getMessageEntry().getHTTPHeaders();
        //Response-Line   = HTTP-Version ResponseCode ResponseMsg SP CRLF
        Vector responseLine = new Vector();
        String startLine = null;
        //String httpVersion = null;
        //String responseCode = null;
        String statusCode = null;
        StringTokenizer httpMessageTokenizer =
          new StringTokenizer(httpHeader, "\n\r\f");

        if (httpMessageTokenizer.hasMoreTokens())
          startLine = httpMessageTokenizer.nextToken();

        if (startLine.startsWith("HTTP"))
        {
          StringTokenizer startLineTokenizer =
            new StringTokenizer(startLine, "\u0020");

          while (startLineTokenizer.hasMoreTokens())
          {
            responseLine.add(startLineTokenizer.nextToken());
          }

          //httpVersion = (String) responseLine.get(0);

          // PB: Check for 500 status code which should be the second token in the string
          statusCode = (String) responseLine.get(1);

          //responseCode = (String) responseLine.get(1);
          //statusCode = (String) responseLine.get(2);
        }
        try
        {
          // PB: Just check for 500 status code
          if (!statusCode
            .equals(String.valueOf(HttpURLConnection.HTTP_INTERNAL_ERROR)))
          {
            //if (!responseCode.equalsIgnoreCase("OK") ||
            //	(!statusCode.equalsIgnoreCase("ServerError"))) {     			
            // Assertion failed
            throw new AssertionFailException(httpHeader);
          }
        }
        catch (AssertionFailException e)
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail = this.validator.createFailureDetail(e.getMessage(), entryContext);
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}