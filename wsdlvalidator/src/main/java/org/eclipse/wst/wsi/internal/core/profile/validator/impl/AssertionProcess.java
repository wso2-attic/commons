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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.report.FailureDetail;


/**
 * Process assertion.
 */
public abstract class AssertionProcess
{
  protected String result;
  protected String failureDetailMessage;
  protected FailureDetail failureDetail;
  private final BaseValidatorImpl base_validator;
  
  /**
   * @param BaseValidatorImpl
   */
  public AssertionProcess(BaseValidatorImpl impl)
  {
    this.base_validator = impl;
  }

  /**
   * Create assertion result.
   */
  void reset()
  {
    result = AssertionResult.RESULT_PASSED;
    failureDetailMessage = null;
    failureDetail = null;
  }

  /**
   * Validate assertion.
   */
  public abstract AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext targetContext)
    throws WSIException;

  /**
   * Verbose output.
   */
  public void debug(String message)
  {
    if ((base_validator != null) && base_validator.verboseOption)
    {
      System.err.println(message);
    }
  }
}