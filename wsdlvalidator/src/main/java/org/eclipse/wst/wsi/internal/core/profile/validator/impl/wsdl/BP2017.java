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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPOperation;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP2017.
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>The "style" attribute of each operation in the contained soap:binding has the same value of "document" or "rpc", for all operations of the wsdl:binding.</assertionDescription>
 */
public class BP2017 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2017(WSDLValidatorImpl impl)
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
    //String firstStyle = null; // GT: is this needed??
    Vector failureDetailList = new Vector();

    // Get the binding from the entry context
    Binding binding = (Binding) entryContext.getEntry().getEntryDetail();
    if (binding == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      return validator.createAssertionResult(testAssertion, result, failureDetailList);
    }
    SOAPBinding soapB = WSDLValidatorImpl.getSoapBinding(binding);
    if (soapB == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      return validator.createAssertionResult(testAssertion, result, failureDetailList);
    }
    String styleB =
      (soapB.getStyle() == null
        ? WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC
        : soapB.getStyle());

    // Try to get the SOAP operations
    HashMap soapOperationList = validator.getSoapOperations(binding);

    if (soapOperationList.isEmpty())
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // check that they all have the same style element value
      String style = null;
      String checkStyle;
      SOAPOperation soapOperation;
      Iterator iterator = soapOperationList.keySet().iterator();
      while (iterator.hasNext())
      {
        soapOperation = (SOAPOperation) iterator.next();
        checkStyle = soapOperation.getStyle();
        if (checkStyle == null)
          checkStyle = styleB;
        if (checkStyle != null
          && !checkStyle.equals(WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC)
          && !checkStyle.equals(WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC))
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetailList.add(
            this.validator.createFailureDetail(
              "Operation Name: "
                + soapOperationList.get(soapOperation)
                + "\nSOAP Operation: "
                + soapOperation.toString(),
              entryContext,
              soapOperation));
          // REMOVE: Need to process all operations
          //break;
        }

        if (style == null)
        {
          style = checkStyle;
        }
        else if (!style.equals(checkStyle) || !style.equals(styleB))
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetailList.add(
            this.validator.createFailureDetail(
              "Operation Name: "
                + soapOperationList.get(soapOperation)
                + "\nSOAP Operation: "
                + soapOperation.toString(),
              entryContext,
              soapOperation));
          // REMOVE: Need to process all operations
          //break;
        }
      }

      if (style == null)
      {
        // no style attribute was found in the SOAP operation(s) of the binding.
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
    }
    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailList);
  }
}