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
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP2406.
 * <context>For a candidate wsdl:binding element, if the use attribute is specified on the soapbind:body, soapbind:fault, soapbind:header, or soapbind:headerfault elements.</context>
 * <assertionDescription>The use attribute has a value of "literal".</assertionDescription>
 */
public class BP2406 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2406(WSDLValidatorImpl impl)
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

    // Get the binding from the entry context
    Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

    // Get SOAP body elements      
    SOAPBody[] soapBodies = this.validator.getSoapBodies(binding);

    // ADD: What happens if there are no soap body elements?  Right now the TA result is NA.      

    // Test the assertion 
    // Check for correct use value 
    for (int i = 0; i < soapBodies.length; i++)
    {
      SOAPBody soapBody = soapBodies[i];
      if (soapBody.getUse() != null
        && !soapBody.getUse().equals(WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail =
          this.validator.createFailureDetail(soapBody.toString(), entryContext);
        break;
      }
    }

    // Get SOAP fault elements      
    SOAPFault[] soapFaults = this.validator.getSoapFaults(binding);

    // Test the assertion 
    // Check for correct use value 
    for (int i = 0; i < soapFaults.length; i++)
    {
      SOAPFault soapFault = soapFaults[i];
      if (soapFault.getUse() != null
        && !soapFault.getUse().equals(WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetailMessage =
          (failureDetailMessage == null
            ? soapFault.toString()
            : failureDetailMessage + "\n\n" + soapFault.toString());
        if (failureDetail == null)
          failureDetail =
            this.validator.createFailureDetail(failureDetailMessage, entryContext);
        else
          failureDetail.setFailureMessage(failureDetailMessage);
        break;
      }
    }

    // Get SOAP header elements      
    SOAPHeader[] soapHeaders = this.validator.getSoapHeaders(binding);

    // Test the assertion 
    // Check for correct use value 
    for (int i = 0; i < soapHeaders.length; i++)
    {
      SOAPHeader soapHeader = soapHeaders[i];
      if (soapHeader.getUse() != null
        && !soapHeader.getUse().equals(WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetailMessage =
          (failureDetailMessage == null
            ? soapHeader.toString()
            : failureDetailMessage + "\n\n" + soapHeader.toString());
        if (failureDetail == null)
          failureDetail =
            this.validator.createFailureDetail(failureDetailMessage, entryContext);
        else
          failureDetail.setFailureMessage(failureDetailMessage);
        break;
      }
    }

    // Get SOAP header fault elements      
    SOAPHeaderFault[] soapHeaderFaults = this.validator.getSoapHeaderFaults(binding);

    // Test the assertion 
    // Check for correct use value 
    for (int i = 0; i < soapHeaderFaults.length; i++)
    {
      SOAPHeaderFault soapHeaderFault = soapHeaderFaults[i];
      if (soapHeaderFault.getUse() != null
        && !soapHeaderFault.getUse().equals(
          WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetailMessage =
          (failureDetailMessage == null
            ? soapHeaderFault.toString()
            : failureDetailMessage + "\n\n" + soapHeaderFault.toString());
        if (failureDetail == null)
          failureDetail =
            this.validator.createFailureDetail(failureDetailMessage, entryContext);
        else
          failureDetail.setFailureMessage(failureDetailMessage);
        break;
      }
    }

    // If the result is passed, but there was nothing to check then set to notApplicable
    if (result == AssertionResult.RESULT_PASSED
      && (soapBodies == null || soapBodies.length == 0)
      && (soapFaults == null || soapFaults.length == 0)
      && (soapHeaders == null || soapHeaders.length == 0)
      && (soapHeaderFaults == null || soapHeaderFaults.length == 0))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}