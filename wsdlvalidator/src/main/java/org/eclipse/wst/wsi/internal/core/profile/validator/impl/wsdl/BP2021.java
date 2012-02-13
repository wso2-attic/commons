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

import java.util.Iterator;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;


/**
*  BP2021.
*     <context>For a candidate wsdl:binding element</context>
*     <assertionDescription>The wsdl:input element and wsdl:output element of each operation uses the attribute name "part" with a Schema type of "NMTOKEN" and does not use "parts", for both soapbind:header elements and soapbind:headerfault elements. </assertionDescription>
*
* @version 1.0.1 27.06.2003
* @author Vitali Fedosenko
**/
public class BP2021 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2021(WSDLValidatorImpl impl)
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
    //Operation operation = null;

    // Get the binding from the entry context
    Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

    // Get SOAP headers for this binding
    SOAPHeader[] soapHeaders = this.validator.getSoapHeaders(binding);

    if (soapHeaders.length == 0)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // Check each header to see if it has part element with NMTOKEN type
      for (int header = 0;
        header < soapHeaders.length
          && result.equals(AssertionResult.RESULT_PASSED);
        header++)
      {
        if ((soapHeaders[header].getPart() == null)
          || (!XMLUtils.isNmtoken(soapHeaders[header].getPart())))
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail =
            this.validator.createFailureDetail(soapHeaders[header].toString(), entryContext);
        }
        // Else, check the headerfault if there is one
        else
        {
          List soapHeaderFaultList =
            soapHeaders[header].getSOAPHeaderFaults();
          Iterator iterator = soapHeaderFaultList.iterator();

          // Check each header fault to see if it has part element with NMTOKEN type
          while (iterator.hasNext()
            && result.equals(AssertionResult.RESULT_PASSED))
          {
            SOAPHeaderFault soapHeaderFault =
              (SOAPHeaderFault) iterator.next();
            if ((soapHeaderFault.getPart() == null)
              || (!XMLUtils.isNmtoken(soapHeaderFault.getPart())))
            {
              result = AssertionResult.RESULT_FAILED;
              failureDetail =
                this.validator.createFailureDetail(soapHeaderFault.toString(), entryContext);
            }
          }
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}