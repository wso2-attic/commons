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

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Operation;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.OperationSignature;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.w3c.dom.Document;


/**
 * BP1010.
 * It MUST NOT contain a SOAP Envelope: the HTTP entity-body must be empty.
 */
public class BP1010 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1010(BaseMessageValidator impl)
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
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {

        BindingOperation bindingOperation = match.getOperation();
        //find operation in port type
        Operation operation = bindingOperation.getOperation();
        if (operation == null)
        {
          result = AssertionResult.RESULT_FAILED;
        }
        else
        {
          if (operation.getOutput() == null)
          {
            String message = entryContext.getMessageEntry().getMessage();
            if (message.length() == 0)
            {
              result = AssertionResult.RESULT_PASSED;
            }
            else
            {
              result = AssertionResult.RESULT_FAILED;
            }
          }
          else
          {
            result = AssertionResult.RESULT_NOT_APPLICABLE;
          }
        }
      }

      if (result == AssertionResult.RESULT_FAILED)
      {
        failureDetail = this.validator.createFailureDetail(null, entryContext);
      }
    }
    catch (Exception e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}