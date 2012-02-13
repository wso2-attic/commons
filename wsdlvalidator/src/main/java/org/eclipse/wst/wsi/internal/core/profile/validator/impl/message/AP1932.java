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
 * AP1932
 *
 * <context>For a candidate message containing a Content-Type HTTP header
 * field-value with a media type of "multipart/related"</context>
 * <assertionDescription>In a message, the Content-Type HTTP header field-value
 * has a type parameter with a value of "text/xml" when it has a media type of
 * "multipart/related".</assertionDescription>
 */
public class AP1932 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1932(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // check for multipart/related content type
    String content_type = HTTPUtils.getHttpHeaderAttribute(
        entryContext.getMessageEntry().getHTTPHeaders(), 
        HTTPConstants.HEADER_CONTENT_TYPE);
    
    if((content_type == null) || 
       (!content_type.equalsIgnoreCase(WSIConstants.CONTENT_TYPE_MULTIPART)))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    } 
    else  
    {
      String subType = HTTPUtils.getHttpHeaderSubAttribute(
          entryContext.getMessageEntry().getHTTPHeaders(), 
          HTTPConstants.HEADER_CONTENT_TYPE, "type");
      if((subType == null)
        || !subType.equalsIgnoreCase(WSIConstants.CONTENT_TYPE_TEXT_XML))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = validator.createFailureDetail(
            "The \"type\" parameter value is \"" + subType + "\"", entryContext);
      }
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}