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

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.OperationSignature;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * BP1005.
 */
public class BP1005 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1005(BaseMessageValidator impl)
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
      if (this.validator.isOneWayResponse(entryContext))
        throw new AssertionNotApplicableException();

      // Parse message
      Document doc = entryContext.getMessageEntryDocument();

      // If this is a fault mesage, then it is not applicable
      if (this.validator.isFault(doc))
      {
        throw new AssertionNotApplicableException();
      }

      // Check if there is a soap body element
      if (!this.validator.containsSoapBodyWithChild(doc))
      {
        throw new AssertionNotApplicableException();
      }

      // Parse request message
      Document docRequest = entryContext.getRequestDocument();

      // get SOAPAction
      String headers = entryContext.getRequest().getHTTPHeaders();
      String action = null;
      if (headers != null)
        action = (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("SOAPAction".toUpperCase());

      Binding binding = validator.analyzerContext.getCandidateInfo().getBindings()[0];
      TypesRegistry registry =
        new TypesRegistry(
          this.validator.getWSDLDocument().getDefinitions(),
          validator);
      OperationSignature.OperationMatch match =
        OperationSignature.matchOperation(
          docRequest,
          action,
          binding,
          registry);
      if (match == null)
        throw new AssertionNotApplicableException();

      BindingOperation bindingOperation = match.getOperation();

      // Is rpc style?
      if (!WSIConstants
        .ATTRVAL_SOAP_BIND_STYLE_RPC
        .equals(match.getOperationStyle()))
        throw new AssertionNotApplicableException();

      List extElements = null;

      if (bindingOperation.getBindingOutput() != null)
        extElements =
          bindingOperation.getBindingOutput().getExtensibilityElements();

      // Is message RPC-literal?
      if (!validator.isLiteral(extElements))
        throw new AssertionNotApplicableException();

      //Get message

      // Gets body
      NodeList soapBodyList =
        doc.getElementsByTagNameNS(
          WSIConstants.NS_URI_SOAP,
          XMLUtils.SOAP_ELEM_BODY);
      if (soapBodyList.getLength() == 0 || soapBodyList.getLength() > 1)
      {
        // There is not a body or more than one bodies in the envlope.
        throw new AssertionFailException();
      }
      Element soapBodyElem = (Element) soapBodyList.item(0);

      // Find wrapper element in the body
      Element wrapperElement = XMLUtils.getFirstChild(soapBodyElem);
      if (wrapperElement == null)
      {
        throw new AssertionFailException();
      }

      if (wrapperElement
        .getLocalName()
        .equals(bindingOperation.getName() + "Response"))
      {
        result = AssertionResult.RESULT_PASSED;
      }
      else
      {
        result = AssertionResult.RESULT_FAILED;
      }

    }
    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_FAILED;
    }
    catch (AssertionNotApplicableException e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (Exception e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}