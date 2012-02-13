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
import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP1902
 *
 * <context>For a candidate message</context>
 * <assertionDescription>In a description, if the wsdl:input or wsdl:output
 * element in the wsdl:binding specifies WSDL MIME binding, then the message
 * can contain SOAP attachments.</assertionDescription>
 */
public class AP1902 extends AssertionProcess implements WSITag
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1902(BaseMessageValidator impl)
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
      // Getting an operation matched for a message
      BindingOperation bindingOperation = validator.getOperationMatch(
        entryContext.getEntry().getEntryType(),
        entryContext.getMessageEntryDocument());
      // If no one operation matches, the assertion is not applicable
      if (bindingOperation == null)
        throw new AssertionNotApplicableException();
      // Getting the corresponding extensibility elements
      List extElems;
      String type;
      if (MessageEntry.TYPE_REQUEST
        .equals(entryContext.getEntry().getEntryType()))
      {
        type = "input";
        extElems = bindingOperation.getBindingInput() == null ? null
          : bindingOperation.getBindingInput().getExtensibilityElements();
      }
      else
      {
        type = "output";
        extElems = bindingOperation.getBindingOutput() == null ? null
          : bindingOperation.getBindingOutput().getExtensibilityElements();
      }
      // If the MIME binding is not used, but the message has
      // at least one non-root MIME part, the assertion failed
      if (!usesMimeBinding(extElems)
        && entryContext.getMessageEntry().getMimeParts().count() > 1)
      {
        throw new AssertionFailException("The wsdl:" + type + " of the \""
          + bindingOperation.getName() + "\" binding operation.");
      }
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Checks whether the first extensibility element is mime:multipartRelated.
   * @param extElems a list of extensibility elements.
   * @return true if the element is mime:multipartRelated, false otherwise
   */
  private boolean usesMimeBinding(List extElems) {
    // If the first extensibility element is mime:multipartRelated,
    // return true
    if (extElems != null && extElems.size() > 0
      && ((ExtensibilityElement)extElems.get(0))
      .getElementType().equals(WSDL_MIME_MULTIPART))
    {
      return true;
    }
    // otherwise return false
    return false;
  }
}