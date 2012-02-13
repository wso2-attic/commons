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
import javax.wsdl.extensions.soap.SOAPBinding;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP2402.
 * <context>For a candidate wsdl:binding element</context>
 * <assertionDescription>The wsdl:binding element has a soapbind:binding child element.</assertionDescription>
 */
public class BP2402 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2402(WSDLValidatorImpl impl)
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
    result = AssertionResult.RESULT_FAILED;

    // Get the port from the entry context
    //Port port = (Port) entryContext.getEntry().getEntryDetail();
    Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

    // Try to get the SOAP binding
    SOAPBinding soapBinding = WSDLValidatorImpl.getSoapBinding(binding);

    // Test the assertion 
    // Check that there is at least one SOAP Binding
    if (soapBinding != null)
    {
      result = AssertionResult.RESULT_PASSED;
      failureDetail = null;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}