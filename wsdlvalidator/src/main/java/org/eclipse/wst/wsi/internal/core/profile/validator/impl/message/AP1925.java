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

import java.util.List;

import javax.wsdl.BindingOperation;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;

/**
 * AP1925
 *
 * <context>For a candidate message</context>
 * <assertionDescription>If the WSDL description lists at least one non-root
 * MIME part, then the corresponding message has a Content-Type HTTP header
 * field-value with a media-type of "multipart/related.</assertionDescription>
 */
public class AP1925 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1925(BaseMessageValidator impl)
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
    try
    {
      BindingOperation bindingOperation = validator.getOperationMatch(
          entryContext.getEntry().getEntryType(),
          entryContext.getMessageEntryDocument());
  
      // If there is no matched operation, the assertion is not applicable
      if (bindingOperation == null)
        throw new AssertionNotApplicableException();
  
      // Finding operation extensibility elems
      // in the binding depending on message type
      List extElems = null;
      if (entryContext.getMessageEntry().getType().equals(
        MessageEntry.TYPE_REQUEST)
        && bindingOperation.getBindingInput() != null)
      {
        extElems = bindingOperation
          .getBindingInput().getExtensibilityElements();
      }
      else if (entryContext.getMessageEntry().getType().equals(
        MessageEntry.TYPE_RESPONSE)
        && bindingOperation.getBindingOutput() != null)
      {
        extElems = bindingOperation
          .getBindingOutput().getExtensibilityElements();
      }
      // check list on first element
      if((extElems == null) || (extElems.size() == 0) || 
         !(extElems.get(0) instanceof MIMEMultipartRelated))
        throw new AssertionNotApplicableException();
      
      // get list mime parts from definition
      MIMEMultipartRelated mime = (MIMEMultipartRelated) extElems.get(0);
      List parts = mime.getMIMEParts();
      // if parts count is more than one, than assertion is notApplicable
      if(parts.size() <= 1)
        throw new AssertionNotApplicableException();
      String contentType = HTTPUtils.getHttpHeaderAttribute(
          entryContext.getMessageEntry().getHTTPHeaders(),
          HTTPConstants.HEADER_CONTENT_TYPE); 
      if((contentType == null) || 
         !contentType.equalsIgnoreCase(WSIConstants.CONTENT_TYPE_MULTIPART))
      {
        throw new AssertionFailException("Incorrect Content-Type value \"" +
            contentType + "\" in HTTP header");
      }
    }
    catch (AssertionNotApplicableException e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = 
        validator.createFailureDetail(e.getMessage(), entryContext);
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Returns the part value from MIMEContent element
   * @param part MIMEPart element
   * @return the part value from MIMEContent element
   */
  //private String getMIMEContentPart(MIMEPart part) {
  //  List list = part.getExtensibilityElements();
  //  if(list.size() == 0)
  //    return null;
  //  return ((MIMEContent) list.get(0)).getPart();
  //}
}