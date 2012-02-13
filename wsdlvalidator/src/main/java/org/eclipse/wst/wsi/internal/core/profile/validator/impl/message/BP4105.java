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
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;


/**
 * BP4105
 * <context>For a candidate message in the message log file containing a Content-encoding HTTP header field with a value other than "gzip", "compress" or "deflate"</context>
 * <assertionDescription>The contained Content-encoding HTTP header field has a value other than "gzip", "compress" or "deflate".</assertionDescription>
 */
public class BP4105 extends AssertionProcess {

  // All the registered content-coding value tokens (RFC2616) except "identity"
  private static final String GZIP_VALUE = "gzip";
  private static final String COMPRESS_VALUE = "compress";
  private static final String DEFLATE_VALUE = "deflate";

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP4105(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    // Getting the Content-Encoding HTTP header value
    String contentEncoding = (String) HTTPUtils.getHttpHeaderTokens(
      entryContext.getMessageEntry().getHTTPHeaders(),
      ":").get(HTTPConstants.HEADER_CONTENT_ENCODING.toUpperCase());

    if (contentEncoding != null)
    {
      // Lowering value's case because RFC2616 says
      // that all content-coding values are case-insensitive
      contentEncoding = contentEncoding.toLowerCase();

      // Multiple encodings can be applied to an entity,
      // so breaking the value into tokens
      StringTokenizer st = new StringTokenizer(contentEncoding, ",");
      while (st.hasMoreTokens())
      {
        String token = st.nextToken().trim();
        // If a token does not equal to any registered one
        // then the assertion passed
        if ( !token.equals(GZIP_VALUE)
          && !token.equals(COMPRESS_VALUE)
          && !token.equals(DEFLATE_VALUE)) {

          failureDetail = validator.createFailureDetail(
            testAssertion.getDetailDescription(), entryContext);

          break;
        }
      }
    }

    // If there is no unregistered value of Content-Encoding,
    // then the assertion is not applicable 
    if (failureDetail == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}