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
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPBinding;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.Utils;


/**
 * BP2019.
 * <context>For a candidate wsdl:binding element, if the contained soap:binding element has a "style" attribute equal to "document" and soap:operations have "use" attribute equal to "literal":</context>
 * <assertionDescription>The "namespace" attribute is not specified in any contained soapbind:body, soapbind:header, soapbind::headerfault, soapbind:fault elements </assertionDescription>
 * @version 1.0.1 27.06.2003
 * @author Vitali Fedosenko
 */
public class BP2019 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2019(WSDLValidatorImpl impl)
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
    boolean docLitFound = false;
    String errantElements = new String("");
    // Get the binding from the entry context
    Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

    // Try to get the SOAP binding
    SOAPBinding soapBinding = WSDLValidatorImpl.getSoapBinding(binding);

    if (soapBinding != null)
    {
      String style =
        (soapBinding.getStyle() == null)
          ? WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC
          : soapBinding.getStyle();
      if (style.equals(WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC))
      {
        Vector soapElements = Utils.arrayToVector(this.validator.getSoapBodies(binding));
        soapElements.addAll(Utils.arrayToVector(this.validator.getSoapFaults(binding)));
        soapElements.addAll(Utils.arrayToVector(this.validator.getSoapHeaders(binding)));
        soapElements.addAll(
          Utils.arrayToVector(this.validator.getSoapHeaderFaults(binding)));

        Iterator i = soapElements.iterator();
        while (i.hasNext())
        {
          ExtensibilityElement soapElement =
            (ExtensibilityElement) (i.next());
          try
          {
            if (this.validator.isLiteral(soapElement))
            {
              docLitFound = true;
              if (this.validator.namespaceFoundInSoapLiteral(soapElement))
              {
                errantElements += "\n--- " + (soapElement.toString());
              }
            }
          }
          catch (Exception e)
          {
          } // continue with clenched teeth
        }
      }
    }

    if (!docLitFound)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // If a namespace was not found then test assertion passed
      if (errantElements.length() != 0)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail =
          this.validator.createFailureDetail(
            "Failing elements:" + errantElements,
            entryContext);
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}