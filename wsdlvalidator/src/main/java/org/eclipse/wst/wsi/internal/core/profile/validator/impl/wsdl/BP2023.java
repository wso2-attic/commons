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
import javax.wsdl.extensions.soap.SOAPFault;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP2023.
 *   <context>For a candidate wsdl:binding element, with the "use" attribute present on the soapbind:fault element</context>
 *   <assertionDescription>The value of the attribute is "literal".</assertionDescription>
 */
public class BP2023 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2023(WSDLValidatorImpl impl)
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

    // Get list of SOAP faults for the binding
    SOAPFault[] soapFaults = this.validator.getSoapFaults(binding);

    if (soapFaults == null || soapFaults.length == 0)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    else
    {
      // check all soap:faults. 
      // MUST have a name attribute, and if a use attribute is specified, it MUST be value "literal"
      for (int i = 0; i < soapFaults.length; i++)
      {
        SOAPFault soapFault = soapFaults[i];
        if ((soapFault.getUse() != null)
          && (!soapFault
            .getUse()
            .equals(WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT)))
        {
          result = AssertionResult.RESULT_FAILED;

          failureDetail =
            this.validator.createFailureDetail(soapFault.toString(), entryContext);
          break;
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}