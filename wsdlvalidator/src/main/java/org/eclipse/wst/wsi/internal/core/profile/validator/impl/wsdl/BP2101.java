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
import javax.wsdl.Import;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;


/**
 * BP2101.
   * <context>For a candidate wsdl:definition, if it contains wsdl:import statements</context>
   * <assertionDescription>Each wsdl:import statement is only used to import another WSDL description.</assertionDescription>
 */
public class BP2101 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2101(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  boolean importFound = false;

  /* Create falure report if import contains reference to non WSDL description. 
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Import, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Import im, Object parent, WSDLTraversalContext ctx)
  {
    importFound = true;
    try
    {
    // by the way : WSDL4J throws Exception if imported WSDL is not resolved
    // but documentation says that im.getDefinition() will be equal to null
    if (im.getDefinition() == null)
      errors.add(
        im.getNamespaceURI()
          + ":"
          + im.getLocationURI()
          + "\nImport element does not reference a WSDL definition.");
    }
    catch (Exception e)
    {
      errors.add(
        im.getNamespaceURI()
          + ":"
          + im.getLocationURI()
          + "\nImport element does not reference a WSDL definition.");
    }
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_FAILED;

    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.visitImport(true);
    traversal.ignoreReferences();
    traversal.traverse((Definition) entryContext.getEntry().getEntryDetail());

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
    }

    else if (!importFound)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    else
      result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}