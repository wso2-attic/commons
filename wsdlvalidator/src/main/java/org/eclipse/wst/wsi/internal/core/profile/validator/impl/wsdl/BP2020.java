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
import javax.wsdl.extensions.soap.SOAPBody;

import org.apache.xerces.util.URI;
import org.apache.xerces.util.URI.MalformedURIException;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
* BP2020.
*    <context>For a candidate wsdl:binding element, if the style attribute of the contained soapbind:binding is "rpc" 
*       and the use attribute is "literal".</context>
*    <assertionDescription>The namespace attribute is specified on all soapbind:body elements and the value of the 
* namespace attribute is an absolute URI. </assertionDescription>
* @version 1.0.1 27.06.2003
* @author Vitali Fedosenko
**/
public class BP2020 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2020(WSDLValidatorImpl impl)
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
    result = AssertionResult.RESULT_FAILED;
    boolean rpcLitFound = false;

    // Get the binding from the entry context
    Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

    // Try to get the SOAP binding
    SOAPBinding soapBinding = WSDLValidatorImpl.getSoapBinding(binding);

    // Test the assertion 
    // Check style to make sure that it is rpc - the subject of the assertion
    String style = WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC;
    if (soapBinding != null)
      style =
        (soapBinding.getStyle() == null
          ? WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC
          : soapBinding.getStyle());

    if ((soapBinding != null)
      && (style.equals(WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC)))
    {
      // Get the list of SOAP body elements
      SOAPBody[] soapBodies = this.validator.getSoapBodies(binding);

      // Check for Soap operation namespace
      boolean assertionFailure = false;
      String namespaceURI = null;
      for (int i = 0; i < soapBodies.length && !(assertionFailure); i++)
      {
        SOAPBody soapBody = soapBodies[i];
        // Added check for null use value, since the default value is literal
        if (soapBody.getUse() == null
          || soapBody.getUse().equals(WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT))
        {
          rpcLitFound = true;
          // If there is no namespace, then flag as a failure
          if ((namespaceURI = soapBody.getNamespaceURI()) == null)
          {
            assertionFailure = true;
            failureDetail =
              this.validator.createFailureDetail(
                "--- SOAP BODY:\n"
                  + soapBody.toString()
                  + "\n--- BINDING QNAME:\n"
                  + binding.getQName(),
                entryContext);
          }

          // Else, verify that the namespace is an absolute URI
          else
          {
            try
            {
              //URL url = new URL(namespaceURI);
              URI uri = new URI(namespaceURI);
              if (uri.getScheme() == null)
              {
                // no scheme implies not an absolute URI
                assertionFailure = true;
                failureDetail =
                  this.validator.createFailureDetail(
                    "--- SOAP BODY:\n"
                      + soapBody.toString()
                      + "\n--- BINDING QNAME:\n"
                      + binding.getQName(),
                    entryContext);
              }
            }
            catch (MalformedURIException mue)
            {
              assertionFailure = true;
              failureDetail =
                this.validator.createFailureDetail(
                  "--- SOAP BODY:\n"
                    + soapBody.toString()
                    + "\n--- BINDING QNAME:\n"
                    + binding.getQName(),
                  entryContext);
            }

          }
        }
      }
      if (!rpcLitFound)
      {
        // style is rpc but no document use elements
        result = AssertionResult.RESULT_NOT_APPLICABLE;
        failureDetail = null;
      }
      else if (!assertionFailure)
      { // If no assertion failure, then set result to passed
        result = AssertionResult.RESULT_PASSED;
        failureDetail = null;
      }
    }
    else
    {
      // style is not rpc
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      failureDetail = null;
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}