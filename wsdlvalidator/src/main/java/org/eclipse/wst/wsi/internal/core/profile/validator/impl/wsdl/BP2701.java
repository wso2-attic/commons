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

import javax.wsdl.Definition;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
  * BP2701.
  *   <context>For a candidate wsdl:definitions element within a WSDL document.</context>
  *   <assertionDescription>The wsdl:definitions namespace has value: http://schemas.xmlsoap.org/wsdl/.</assertionDescription>
  */
public class BP2701 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2701(WSDLValidatorImpl impl)
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

    Definition def = (Definition) entryContext.getEntry().getEntryDetail();

    /*
    if (def != null) {
    	QName qname = def.getQName();
    				
    	if (qname != null) {	
    		//if (def.getNamespace("").equals(WSIConstants.NS_URI_WSDL) {
    		if (qname.getNamespaceURI().equals(WSIConstants.NS_URI_WSDL)) {
    			result = AssertionResult.RESULT_PASSED;
    		}
    	}
    }
    */

    try
    {
      String schemaUsed = this.validator.getSchemaUsed(def);
      if (schemaUsed.equals(WSIConstants.NS_URI_WSDL))
      {
        result = AssertionResult.RESULT_PASSED;
      }

      else
      {
        result = AssertionResult.RESULT_FAILED;
      }
    }

    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_FAILED;
      //ADD: failure description?
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}