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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import javax.wsdl.Operation;
import javax.wsdl.OperationType;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP2208.
 * <context>For a candidate wsdl:operation in a wsdl:portType definition</context>
 * <assertionDescription>The wsdl:operation element is either a WSDL request/response or a one-way operation (no Notification or Sollicit-Response).</assertionDescription>
 */
public class BP2208 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2208(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* (non-Javadoc)
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    Operation operation =
      (Operation) entryContext.getEntry().getEntryDetail();
    OperationType opType = operation.getStyle();
    if ((opType == null)
      || (!opType.equals(OperationType.ONE_WAY)
        && !opType.equals(OperationType.REQUEST_RESPONSE)))
    {
      result = AssertionResult.RESULT_FAILED;
      if (opType == null)
        failureDetail =
          this.validator.createFailureDetail(
            "Could not determine the operation type for "
              + operation.getName()
              + ".",
            entryContext);
      else
        failureDetail = null; // none defined in TAD at present 
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}