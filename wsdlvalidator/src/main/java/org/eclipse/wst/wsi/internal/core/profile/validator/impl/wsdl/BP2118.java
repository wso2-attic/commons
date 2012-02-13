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
import javax.wsdl.Operation;

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
 * BP2118.
   * <context>For a candidate wsdl:binding element</context>
   * <assertionDescription>The list (or set) of wsdl:operation elements for the contained wsdl:binding is the same as the list of wsdl:operations for the referred wsdl:portType.</assertionDescription>
 */
public class BP2118 extends AssertionProcessVisitor implements WSITag
{

  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2118(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Operation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Operation op, Object parent, WSDLTraversalContext ctx)
  {
    // assert parent instanceof BindingOperation
    if (op == null)
    {
      errors.add(ctx.getBinding().getQName());
      //				suppresses subsequent processing, because error list contains only failed binding
      ctx.cancelBindingProcessing();
      ctx.cancelBindingOperationProcessing();
    }
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

    Binding b = (Binding) entryContext.getEntry().getEntryDetail();
    // if operations count in port type and binding is differ -> error
    if (b.getPortType() == null
      || b.getPortType().getOperations().size()
        != b.getBindingOperations().size())
      errors.add(b.getQName());
    else
    {
      WSDLTraversal traversal = new WSDLTraversal();
      //VisitorAdaptor.adapt(this);
      traversal.setVisitor(this);
      traversal.visitOperation(true);
      traversal.traverse(b);
    }

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