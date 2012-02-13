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
  * BP2012.
  * Example:
  * 
  * <message name="getConfigurationOptionsRequest">
  * 	<part name="refresh" element="cfg:refresh" /> 
  * </message>
  * <portType name="ConfiguratorPortType">
  * 	<operation name="getConfigurationOptions">
  * 		<input message="tns:getConfigurationOptionsRequest" /> 
  * 		<output ..... /> 
  * 		<fault ..... /> 
  * 	</operation>
  * </portType>
  * <binding name="ConfiguratorBinding" type="tns:ConfiguratorPortType">
  * 	<soap:binding transport="http://schemas.xmlsoap.org/soap/http" style="document" /> 
  * 	<operation name="getConfigurationOptions">
  *		<soap:operation soapAction="http://www.zzz.com/zzz.wsdl/getConfigurationOptions" /> 
  * 		<input>
  * 			<soap:body use="literal" namespace="http://www.zzz.com/zzz.wsdl" /> 
  * 		</input>
  * 		<output>.....</output>
  *		<fault>......</fault>
  * 	</operation>
  * </binding>
  * 
  * Above, the test for this assertion might be that the element attribute is specified in the "refresh" 
  * part of the getConfigurationOptionsRequest message. 
  * 
  * Context : for <binding> "ConfiguratorBinding", <soap:binding> style=document; <soap:body> use=literal
  * for the <input> of binding operation "getConfigurationOptions". 
  * (<input> is an example. Could be <output> or <fault> equally).
  * Action: check the part(s) of the message specified in the <input> element (in example) of the <portType>,
  * namely tns:getConfigurationOptionsRequest, that the element attribute is specified. In this case we have
  * <part name="refresh" element="cfg:refresh" />, so the test passes.
  * All such parts associated with the assertion candidate binding(s) must be similarly checked for this assertion
  * to pass.
  */
public class BP2012 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2012(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* 
   * Validates the test assertion.
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
        WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC,
        WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT);
    if (bindingMatch.length == 0)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
      try
      {
        if (!this.validator.checkPartAttributes(bindingMatch, "useInput", "useElement")
          || !this.validator.checkPartAttributes(bindingMatch, "useOutput", "useElement"))
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