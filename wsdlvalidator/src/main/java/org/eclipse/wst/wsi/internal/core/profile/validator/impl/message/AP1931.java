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

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * AP1931
 *
 * <context>For a candidate root-part of a multipart/related message</context>
 * <assertionDescription>The entity body of the root-part of multipart/related
 * message is a soap:Envelope.</assertionDescription>
 */
public class AP1931 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1931(BaseMessageValidator impl)
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
    // check for the root part
    if(entryContext.getMessageEntry().isMimeContent() &&
       (entryContext.getMessageEntry().getMimeParts().getRootPart() != null))
    {
      try
      {
      // parse root part message
      Document doc = XMLUtils.parseXML(
          entryContext.getMessageEntry().getMimeParts().getRootPart().getContent());
      
      // get entity body
      Element el = doc.getDocumentElement();
      
      // if the element is not soap:Envelope, then fail  
      if(!el.getLocalName().equals(WSITag.ELEM_SOAP_ENVELOPE.getLocalPart()) ||
         !el.getNamespaceURI().equals(WSITag.ELEM_SOAP_ENVELOPE.getNamespaceURI()))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = validator.createFailureDetail(
            "the entity body of the root part is {" +
            el.getNamespaceURI() + "}" + el.getLocalName(), entryContext);
      }
      } 
      catch (Exception e)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = validator.createFailureDetail(
            "the entity body of the root part does not contain " +
            "\"soap:Envelope\" element", entryContext);
      }
    } 
    else 
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}