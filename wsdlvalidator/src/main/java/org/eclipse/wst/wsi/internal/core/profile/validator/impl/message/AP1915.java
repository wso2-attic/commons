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
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.MIMEConstants;
import org.eclipse.wst.wsi.internal.core.util.MIMEUtils;
import org.eclipse.wst.wsi.internal.core.util.Utils;

/**
 * AP1915
 *
 * <context>For a candidate root-part of a multipart/related message</context>
 * <assertionDescription>The entity body of the root-part of a
 * multipart/related message is serialized using either UTF-8 or UTF-16
 * character encoding.</assertionDescription>
 */
public class AP1915 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1915(BaseMessageValidator impl)
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
    // get MIME parts
    if (!entryContext.getMessageEntry().isMimeContent())
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      MimeParts mimeParts = entryContext.getMessageEntry().getMimeParts();    	
      MimePart part = mimeParts.getRootPart();
      if (part == null)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        String xmlEncoding = null;
        String charset = MIMEUtils.getMimeHeaderSubAttribute(
          part.getHeaders(),
          MIMEConstants.HEADER_CONTENT_TYPE, 
          "charset");
        try
        {
          // The HTTP Content-Type header's charset value is either UTF-8 or UTF-16. 
          if((charset != null) && (charset.equalsIgnoreCase("utf-8") ||
              charset.equalsIgnoreCase("utf-16")))
          {
            // Looking at the messageContent element of the logged message, either 
            // (1) it has a BOM attribute which maps the charset value in the Content-Type header, or 
            int bom = 0;
            if ((bom = entryContext.getMessageEntry().getBOM()) != 0)
            {
              if ((bom == WSIConstants.BOM_UTF8
                && !charset.equalsIgnoreCase("utf-8"))
                || ((bom == WSIConstants.BOM_UTF16
                  && !charset.equalsIgnoreCase("utf-16")))
                || ((bom == WSIConstants.BOM_UTF16_BIG_ENDIAN
                  && !charset.equalsIgnoreCase("utf-16"))))
              {
                throw new AssertionFailException("The BOM (" + bom + 
                    ") and charset value (" + charset + ")do not match.");
              }
            }
            // (2) it has an XML declaration which matches the charset value in the Content-Type header, or 
            else if (((xmlEncoding = 
              Utils.getXMLEncoding(part.getContent())) != null) &&
              !xmlEncoding.equals(""))  
            {
              if(!xmlEncoding.equalsIgnoreCase(charset))
              {
                throw new AssertionFailException("The XML declaration encoding (" + 
                    xmlEncoding + ") and charset value (" + charset + 
                    ") do not match.");
              }
            
            }
            // (3) there is no BOM attribute and no XML declaration, and the charset value is UTF-8.
            else if(!charset.equalsIgnoreCase("utf-8"))
            {
              throw new AssertionFailException("The no BOM attribute and no XML "+
                  "declaration, and the charset value is (" + charset + ")");
            }
          }
          // header do not found or incorrect charset value
          else 
          {
            throw new AssertionFailException("Either the Content-Type header is not "+
                "present in the Root Part or a charset value is invalid.");
          }
        }
        catch (AssertionFailException e) 
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail = validator.createFailureDetail(e.getMessage(), entryContext);
        }
      }
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}