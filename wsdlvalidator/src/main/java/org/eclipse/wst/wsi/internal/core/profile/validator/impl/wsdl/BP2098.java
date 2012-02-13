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

import javax.wsdl.Import;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;


/**
 * BP2098.
 * <context>For a candidate wsdl:import element</context>
 * <assertionDescription>The "location" attribute is specified for the wsdl:import element, and has a non-empty value.</assertionDescription>
 */
public class BP2098 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2098(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_FAILED;

    Import im = (Import) entryContext.getEntry().getEntryDetail();
    // suppose that if location is invalid, the definition equals to null
    if (im.getLocationURI() == null || im.getLocationURI() == "")
      errors.add(im.getNamespaceURI(), im.getLocationURI());

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
    }
    else
      result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}