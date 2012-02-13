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

import java.util.List;

import javax.wsdl.BindingOperation;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.EntryType;

/**
 * AP1920
 *
 * <context>For an envelope in a response message
 * containing a soap:Fault element</context>
 * <assertionDescription>In a description, if and only if, the wsdl:output
 * element is described using WSDL MIME binding, then the envelope in the
 * response message can contain faults with attachments.</assertionDescription>
 */
public class AP1920 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1920(BaseMessageValidator impl)
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
      if (!entryContext.getMessageEntry().isMimeContent())
      {
        throw new AssertionNotApplicableException();
      }

      // check for a fault element within the message 
      if((entryContext.getMessageEntryDocument() == null) || 
          entryContext.getMessageEntryDocument().
          getElementsByTagNameNS(WSITag.ELEM_SOAP_FAULT.getNamespaceURI(), 
              WSITag.ELEM_SOAP_FAULT.getLocalPart()).getLength() != 1)
        throw new AssertionNotApplicableException();
      
      BindingOperation bindingOperation = validator.getOperationMatch(
          EntryType.getEntryType(MessageValidator.TYPE_MESSAGE_REQUEST),
          entryContext.getRequestDocument());

      // If there is no matched operation, the assertion is not applicable
      if (bindingOperation == null)
        throw new AssertionNotApplicableException();
      // If the envelope in a response message contains a fault with attachments
      // and the corresponding wsdl:output element in the description is not 
      // described using the WSDL MIME binding, the assertion is failed  
      if ((entryContext.getMessageEntry().getMimeParts().count() > 1) &&
          (getMultipartRelatedBindings(bindingOperation) == null))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = 
          validator.createFailureDetail("The message contains \"" + 
              entryContext.getMessageEntry().getMimeParts().count() + "\" part(s)", 
              entryContext);
      }
    }
    catch(AssertionNotApplicableException e)
    {
       result = AssertionResult.RESULT_NOT_APPLICABLE; 
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
  
  /**
   * Returns Multipart related bindings, if does not found, returns null.
   * @param op binding operation
   * @return Multipart related bindings, if does not found, returns null.
   */
  private MIMEMultipartRelated getMultipartRelatedBindings(
      BindingOperation op) 
  {
    // Finding operation extensibility elems
    // in the binding depending on message type
    List extElems = op.getBindingOutput().getExtensibilityElements();
    // check list on first element
    if((extElems == null) || (extElems.size() == 0) || 
       !(extElems.get(0) instanceof MIMEMultipartRelated))
      return null;
    return (MIMEMultipartRelated) extElems.get(0); 
  }
}