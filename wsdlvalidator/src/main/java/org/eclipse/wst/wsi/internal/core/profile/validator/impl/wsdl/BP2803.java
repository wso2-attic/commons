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

import java.net.URI;

import javax.wsdl.Import;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;


/**
 * BP2803
 * <context>For a candidate wsdl:import element</context>
 * <assertionDescription>The "namespace" attribute is specified for the wsdl:import element, and the attribute's value is not a relative URI.</assertionDescription>
 */
public class BP2803 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2803(WSDLValidatorImpl impl)
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

    // Getting wsdl:import
    Import im = (Import) entryContext.getEntry().getEntryDetail();

    try
    {
      // If there is no the "namespace" attribute in the import,
      // the assertion is not applicable
      if (im.getNamespaceURI() == null)
      {
        throw new AssertionNotApplicableException();
      }

      // If a namespace URI is relative or invalid, the assertion failed
      URI uri = new URI(im.getNamespaceURI());
      if (!uri.isAbsolute())
      {
        throw new AssertionFailException(
          im.getNamespaceURI() + " is relative URI.");
      }
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (Exception e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        e.getMessage(), entryContext);
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}