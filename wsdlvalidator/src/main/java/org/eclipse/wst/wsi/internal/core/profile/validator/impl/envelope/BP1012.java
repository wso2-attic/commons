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

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * BP1012.
 */
public class BP1012 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1012(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      // look for <soap:Envelope> elements:
      // (Note: since this is a Soap message we expect exactly one soap envelope, but we do not assume it).
      Document requestDoc = entryContext.getRequestDocument();

      // message is empty or invalid, the assertion is not applicable
      if (requestDoc == null)
        throw new AssertionNotApplicableException();

      Element envElem = requestDoc.getDocumentElement();
      //NodeList soapEnvList = requestDoc.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "Envelope");

      // If the document element tag name is not Envelope
      // or the namespace is http://schemas.xmlsoap.org/soap/envelope/
      if (!envElem.getLocalName().equals(XMLUtils.SOAP_ELEM_ENVELOPE)
        || envElem.getNamespaceURI().equals(WSIConstants.NS_URI_SOAP))
        // Request contains proper soap Envelope element
        throw new AssertionNotApplicableException();

      // we expect a Soap Fault as a response	
      Document responseDoc =
        XMLUtils.parseXML(entryContext.getResponse().getMessage());
      NodeList soapFaultList =
        responseDoc.getElementsByTagNameNS(
          WSIConstants.NS_URI_SOAP,
          "Fault");
      // ADD extra check here that the <soap:Fault> is a child of <soap:Envelope> ??		      				
      if ((soapFaultList == null) || (soapFaultList.getLength() == 0))
      {
        // No Soap fault found.
        // Assertion failed (ADD highlight the child here ?)
        throw new AssertionFailException(
          "--- REQUEST:\n"
            + entryContext.getRequest().getMessage()
            + "\n--- RESPONSE:\n"
            + entryContext.getResponse().getMessage());
      }

    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(e.getMessage(), entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}