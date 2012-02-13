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
import javax.wsdl.BindingFault;
import javax.wsdl.extensions.soap.SOAPFault;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP2032.
 *   <context>For a candidate wsdl:binding element</context>
 *   <assertionDescription>the name attribute that is specified on the soapbind:fault element matches the value specified on the parent element wsdl:fault.</assertionDescription>
 */
public class BP2032 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2032(WSDLValidatorImpl impl)
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
    // Get the binding from the entry context
    Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

    // Get list of Binding faults for the binding
    BindingFault[] bindingFaults = this.validator.getAllBindingFaults(binding);

    if (bindingFaults == null || bindingFaults.length == 0)
    {
      result = AssertionResult.RESULT_PASSED;
    }

    else
    {
      // for each binding fault, compare the child soap:fault name with the binding fault name 

      for (int i = 0; i < bindingFaults.length; i++)
      {
        BindingFault bindingFault = bindingFaults[i];
        SOAPFault soapFault = this.validator.getSoapFault(bindingFault);
        if (soapFault == null
          || soapFault.getName() == null
          || bindingFault.getName() == null
          || !bindingFault.getName().equals(soapFault.getName()))
        {
          result = AssertionResult.RESULT_FAILED;

          failureDetail = this.validator.createFailureDetail(
            soapFault == null ? null : soapFault.toString(), entryContext);
          break;
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}