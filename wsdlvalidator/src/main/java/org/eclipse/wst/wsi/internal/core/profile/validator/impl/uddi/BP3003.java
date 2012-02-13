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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.uddi;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.UDDIUtils;
import org.uddi4j.datatype.tmodel.TModel;


/**
 * BP3003 - The uddi:tModel is categorized using the uddi:types taxonomy,
 * as "wsdlSpec": the uddi:keyedReference element has an attribute keyValue
 * equal to "wsdlSpec", and keyName equal to "uddi-org:types" or "types".
 */
public class BP3003 extends AssertionProcess
{
  private final UDDIValidatorImpl validator;

  /**
   * @param UDDIValidatorImpl
   */
  public BP3003(UDDIValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * Validates the test assertion.
  * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_PASSED;

    // Get the tModel from the entryContext
    TModel tModel = (TModel) entryContext.getEntry().getEntryDetail();

    // If the tModel does not exist, then fail
    if (tModel == null)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetailMessage =
        "Could not locate a tModel with a categorization of 'wsdlSpec'.";
    }

    // If there is a tModel and it is not categorized as "wsdlSpec", then fail
    else if (!(UDDIUtils.isWsdlSpec(tModel)))
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetailMessage =
        "The tModel key is: ["
          + tModel.getTModelKey()
          + "]\n"
          + "The categoryBag is: ["
          + this.validator.categoryBagToString(tModel.getCategoryBag())
          + "]";
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetailMessage);
  }
}