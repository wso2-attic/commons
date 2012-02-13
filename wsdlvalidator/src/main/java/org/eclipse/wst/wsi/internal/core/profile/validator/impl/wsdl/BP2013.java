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

import javax.wsdl.Binding;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl.WSDLValidatorImpl.BindingMatch;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP2013.
 * The binding (in soapbind:body elements) only refers to part elements that have been defined using the "type" attribute.
 *
 * @version 1.0.1 27.06.2003
 * @author Vitali Fedosenko
 */
public class BP2013 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2013(WSDLValidatorImpl impl)
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
    // Get binding from entry context

    // Oleg's & Graham's version:
    Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

    // to use original version require a binding array
    //Binding[] binding = {(Binding) entryContext.getEntry().getEntryDetail()};

    // get list of candidate matches
    BindingMatch[] bindingMatch =
      this.validator.getBindingMatches(
        binding,
        WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC,
        WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT);
    if (bindingMatch.length == 0)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
      try
      {
        if (!this.validator.checkPartAttributes(bindingMatch, "useInput", "useType")
          || !this.validator.checkPartAttributes(bindingMatch, "useOutput", "useType"))
        {
          // this should never happen
          throw new AssertionFailException("diagnostic: internal processing error!");
        }
      }
      catch (AssertionFailException e)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail =
          this.validator.createFailureDetail(e.getMessage(), entryContext, binding);
      }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}