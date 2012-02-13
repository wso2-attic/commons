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

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;


/**
	 * BP2108.
     * <context>For a candidate wsdl:types, with Array declarations either locally defined or from an imported description</context>
     * <assertionDescription>The type soapenc:Array does not appear in these declarations, and the wsdl:arrayType attribute is not used in the type declaration.</assertionDescription>
	 */
public class BP2108 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2108(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  /* 
   * Validates the test assertion.
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_FAILED;

    Types t = (Types) entryContext.getEntry().getEntryDetail();

    // Search the definitions in CandidateInfo to locate the definition element that contains the specified types element
    Definition definition = null;
    if ((definition = validator.analyzerContext.getCandidateInfo().getDefinition(t))
      == null)
    {
      // This should never happen, but if it does then throw an execption
      throw new WSIException("Could not locate types element definition.");
    }

    else
    {
      TypesRegistry registry =
        new TypesRegistry(
          t,
          definition.getDocumentBaseURI(),
          validator);

      Iterator it = registry.getArrayTypes().iterator();
      while (it.hasNext())
        errors.add((QName) it.next());

      if (!errors.isEmpty())
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
      }
      else
        result = AssertionResult.RESULT_PASSED;
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}