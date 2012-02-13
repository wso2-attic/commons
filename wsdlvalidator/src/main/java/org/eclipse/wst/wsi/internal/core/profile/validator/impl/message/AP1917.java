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
import javax.wsdl.extensions.soap.SOAPBody;

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
 * AP1917
 *
 * <context>For a candidate message containing zero attachment parts</context>
 * <assertionDescription>A message containing zero attachment parts is sent
 * using a content-type of either "text/xml" as though a SOAP HTTP binding were
 * used or "multipart/related" when the WSDL description for the message
 * specifies the mime:multipartRelated element on the corresponding wsdl:input
 * or wsdl:output element in its wsdl:binding.</assertionDescription>
 */
public class AP1917 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1917(BaseMessageValidator impl)
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
      // not applicable if there are attachments
      if (entryContext.getMessageEntry().isMimeContent())
      {
        if (entryContext.getMessageEntry().getMimeParts().count() > 1)
           throw new AssertionNotApplicableException();
      }

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

      // check list
      if((extElems == null) || (extElems.size() == 0))
      {
        throw new AssertionNotApplicableException();
      }
      
      // determine type 
      boolean isMultiPart = false;
      
      // if MIMEMultipartRelated then only root-part allowed 
      if(extElems.get(0) instanceof MIMEMultipartRelated)
      {
        if(((MIMEMultipartRelated) extElems.get(0)).getMIMEParts().size() != 1) {
          throw new AssertionNotApplicableException();
        }
        isMultiPart = true;
      }
      // else if not soapbind:body root element
      else if(!(extElems.get(0) instanceof SOAPBody))
      {
        throw new AssertionNotApplicableException();
      }

      // get HTTP content type
      String contentType = HTTPUtils.getHttpHeaderAttribute(
          entryContext.getMessageEntry().getHTTPHeaders(),
          HTTPConstants.HEADER_CONTENT_TYPE);
 
      //allow "text/xml" or 
      //("multipart/related" and "mime:multipartRelated" WSDL bindings)
      if((contentType == null) || 
         (!contentType.equalsIgnoreCase(WSIConstants.CONTENT_TYPE_MULTIPART) &&
         !contentType.equalsIgnoreCase(WSIConstants.CONTENT_TYPE_TEXT_XML)) ||
         ((contentType.equalsIgnoreCase(WSIConstants.CONTENT_TYPE_MULTIPART) && 
             !isMultiPart)))
      {
        throw new AssertionFailException("The content-type header field  " +
            "value \"" + contentType + "\" is incorrect");
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
}