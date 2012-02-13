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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.uddi;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.uddi4j.datatype.binding.BindingTemplate;


/**
 * BP3002 - The uddi:bindingTemplate element contains a uddi:accessPoint element,
 * with a non-empty value.
 */
public class BP3002 extends AssertionProcess
{
  private final UDDIValidatorImpl validator;

  /**
   * @param UDDIValidatorImpl
   */
  public BP3002(UDDIValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * Validates the test assertion.
  * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    String serviceLocation = null;

    result = AssertionResult.RESULT_PASSED;

    // Get the bindingTemplate from the entryContext
    BindingTemplate bindingTemplate =
      (BindingTemplate) entryContext.getEntry().getEntryDetail();

    // If the bindingTemplate does not contain an accessPoint, then fail
    if ((bindingTemplate.getAccessPoint() == null)
      || ((serviceLocation = bindingTemplate.getAccessPoint().getText())
        == null))
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetailMessage =
        "The bindingTempate key is: ["
          + bindingTemplate.getBindingKey()
          + "].";
    }

    // Else save the service location in the assertion result
    else
    {
      // Save service location in analyzer context
      entryContext
        .getAnalyzerContext()
        .getServiceReference()
        .setServiceLocation(
        serviceLocation);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailMessage);
  }
}