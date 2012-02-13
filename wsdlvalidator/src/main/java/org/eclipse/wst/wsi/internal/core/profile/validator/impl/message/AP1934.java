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

import java.util.Iterator;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.MIMEConstants;
import org.eclipse.wst.wsi.internal.core.util.MIMEUtils;

/**
 * AP1934
 *
 * <context>For a candidate part of a multipart/related message</context>
 * <assertionDescription>The Content-Transfer-Encoding field of a part in a
 * multipart/related message has a value of "7bit", "8bit", "binary",
 * "quoted-printable" or "base64".</assertionDescription>
 */
public class AP1934 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1934(BaseMessageValidator impl)
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
    if(!entryContext.getMessageEntry().isMimeContent())
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    } 
    else 
    {
      // get MIME parts
      MimeParts parts = entryContext.getMessageEntry().getMimeParts();
      if(parts.count() == 0) 
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
      	Iterator iparts = parts.getParts().iterator();
        // check each part for the Content-Transfer-Encoding field
        int i = 0;
        while (iparts.hasNext())
        {
          MimePart part = (MimePart)iparts.next();
          String type_value = MIMEUtils.getMimeHeaderAttribute(
              part.getHeaders(),
              MIMEConstants.HEADER_CONTENT_TRANSFER_ENCODING);
          if (type_value != null)
          {
            if((type_value == null) || !(type_value.equalsIgnoreCase("7bit") ||
                type_value.equalsIgnoreCase("8bit") || 
                type_value.equalsIgnoreCase("base64") ||
                type_value.equalsIgnoreCase("quoted-printable") ||
                type_value.equalsIgnoreCase("binary")))
            {
              result = AssertionResult.RESULT_FAILED;
              failureDetail = validator.createFailureDetail(
                  "(" + (i+1) +
                  "), part header field \"Content-Transfer-Encoding\" has incorrect value \""+
                  type_value+"\"", entryContext);
            }
          }
          i++;
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}