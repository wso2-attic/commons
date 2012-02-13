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

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP2904
 * <context>For a candidate wsdl:binding with at least one mime:content element</context>
 * <assertionDescription>The part attribute of each mime:content element in a wsdl:binding
 * does not reference a sub-component of a wsdl:part of the wsdl:message that is present in the
 * respective wsdl:input or wsdl:output of the corresponding wsdl:operation of the corresponding
 * wsdl:portType.</assertionDescription>
 */
public class AP2904 extends AP2903
{
  /**
   * @param WSDLValidatorImpl
   */
  public AP2904(WSDLValidatorImpl impl)
  {
    super(impl);
  }

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // Return assertion result
    return super.validate(testAssertion, entryContext);
  }
}