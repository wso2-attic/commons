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

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;

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
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLUtils;
import org.eclipse.wst.wsi.internal.core.xml.XMLTags;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * BP1211.
 * 
 * Context: 
 * For a candidate message in the message log file, that is referred by a binding style RPC-literal
 * 
 * Description:
 * Part accessor elements in the message do not have an xsi:nil attribute with a value of "1" or "true".
 */
public class BP1211 extends AssertionProcess implements XMLTags
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1211(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(TestAssertion, EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      if (validator.isOneWayResponse(entryContext))
        throw new AssertionNotApplicableException();

      // Parse message
      Document doc = entryContext.getMessageEntryDocument();
      Document docRequest = entryContext.getRequestDocument();

      Element soapOperation = null;
      // If there is a Fault entry or no body entries,
      // the assertion is not applicable
      if (validator.isFault(doc)
        || (soapOperation = validator.getSoapBodyChild(doc)) == null)
        throw new AssertionNotApplicableException();

      // Get SOAPAction
      String headers = entryContext.getRequest().getHTTPHeaders();
      String action = null;
      if (headers != null)
        action = (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("SOAPAction".toUpperCase());

      // Get the binding that is being processed                
      Binding binding = validator.analyzerContext.getCandidateInfo().getBindings()[0];

      //Create the types registry
      TypesRegistry registry =
        new TypesRegistry(
          this.validator.getWSDLDocument().getDefinitions(),
          validator);

      // Find an operation match        
      OperationSignature.OperationMatch match =
        OperationSignature.matchOperation(
          docRequest,
          action,
          binding,
          registry);
      if (match == null)
        throw new AssertionNotApplicableException();

      // Get the binding operation based on the match
      BindingOperation bindingOperation = match.getOperation();

      // If this is not rpc-literal, then return notApplicable result
      if (!WSDLUtils
        .isRpcLiteral(match.getOperationStyle(), bindingOperation))
        throw new AssertionNotApplicableException();

      // Going through all the accessors
      Element accessor = XMLUtils.getFirstChild(soapOperation);
      while (accessor != null)
      {
        Attr attr = XMLUtils.getAttribute(accessor, ATTR_XSI_NIL);
        // If there is xsi:nil attribute and its value is "1" or "true"
        // the assertion failed
        if (attr != null
          && (attr.getValue().equals("1") || attr.getValue().equals("true")))
        {
          throw new AssertionFailException("The accessor name is "
            + accessor.getNodeName());
        }
        // Getting the next accessor
        accessor = XMLUtils.getNextSibling(accessor);
      }
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }
    catch (Exception e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

}