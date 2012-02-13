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

import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOutput;
import javax.wsdl.Operation;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
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
 * BP2119.
   * <context>For a candidate wsdl:binding, which is of type document-literal</context>
   * <assertionDescription>If it does not specify the parts attribute on a soapbind:body element, the corresponding abstract wsdl:message defines zero or one wsdl:part.</assertionDescription>
 */
public class BP2119 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2119(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private boolean docLiteralFound = false;
  private ErrorList errors = new ErrorList();

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPBinding, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPBinding binding,
    Object parent,
    WSDLTraversalContext ctx)
  {
    String style =
      (binding.getStyle() == null)
        ? WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC
        : binding.getStyle();
    ctx.addParameter("style", style);
  }
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPOperation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPOperation operation,
    Object parent,
    WSDLTraversalContext ctx)
  {
    String style = operation.getStyle();
    if (style == null)
      style = (String) ctx.getParameter("style");
    // assert style != null
    if (!WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC.equals(style))
      ctx.cancelBindingOperationProcessing();
  }
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPBody, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPBody body, Object parent, WSDLTraversalContext ctx)
  {
    String use =
      (body.getUse() == null)
        ? WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT
        : body.getUse();

    // assert style == "document"
    if (WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT.equals(use))
    {
      docLiteralFound = true;
      if (body.getParts() == null)
      {
        Operation op = ctx.getBindingOperation().getOperation();
        if (op == null)
          return;

        Map parts = null;
        if (parent instanceof BindingInput)
        {
          if (op.getInput() == null || op.getInput().getMessage() == null)
            return;
          parts = op.getInput().getMessage().getParts();
        }
        else if (parent instanceof BindingOutput)
        {
          if (op.getOutput() == null || op.getOutput().getMessage() == null)
            return;
          parts = op.getOutput().getMessage().getParts();
        }

        if (parts != null && parts.size() > 1)
        {
          errors.add(ctx.getBinding().getQName());
          //						suppresses subsequent processing, because error list contains only failed binding
          ctx.cancelBindingProcessing();
          ctx.cancelBindingOperationProcessing();
          ctx.cancelBindingInputProcessing();
          ctx.cancelBindingOutputProcessing();
        }
      }
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

    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.visitSOAPBinding(true);
    traversal.visitSOAPBody(true);
    traversal.visitSOAPOperation(true);
    traversal.ignoreImport();
    traversal.traverse((Binding) entryContext.getEntry().getEntryDetail());

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
    }

    else if (!docLiteralFound)
      result = AssertionResult.RESULT_NOT_APPLICABLE;

    else
      result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}