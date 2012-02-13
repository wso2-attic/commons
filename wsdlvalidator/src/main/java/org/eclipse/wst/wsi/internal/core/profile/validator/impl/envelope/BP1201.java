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

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * BP1201.
 * The namespace of the soap:Envelope in the message has value: http://schemas.xmlsoap.org/soap/envelope/. 
 */
public class BP1201 extends AssertionProcess implements WSITag
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1201(BaseMessageValidator impl)
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
    // Check if this is one way response or message is mepty or invalid
    if (this.validator.isOneWayResponse(entryContext) ||
      (doc = entryContext.getMessageEntryDocument()) == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      Element root = doc.getDocumentElement();
      String ns = root.getNamespaceURI();
      String local = root.getLocalName();
      if (!ELEM_SOAP_ENVELOPE.getNamespaceURI().equals(ns))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = validator.createFailureDetail(
          "Root element has namespace: " + ns, entryContext);
      }

      else if (!ELEM_SOAP_ENVELOPE.getLocalPart().equals(local))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = validator.createFailureDetail(
          "Root element has local name: " + local, entryContext);
      }
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}