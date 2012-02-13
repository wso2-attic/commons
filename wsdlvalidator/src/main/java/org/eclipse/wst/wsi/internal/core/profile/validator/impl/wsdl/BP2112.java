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
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
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
import org.eclipse.wst.wsi.internal.core.util.WSDLUtil;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;


/**
 * BP2112.
   * <context>For a candidate wsdl:binding, with a style "rpc" attribute and containing at least a soapbind:body element</context>
   * <assertionDescription>No wsdl:part referred by such a soapbind:body element is defined using the "element" attribute.</assertionDescription>
 */
public class BP2112 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2112(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private boolean rpcLiteralFound = false;
  private ErrorList errors = new ErrorList();

  /* 
   * Put in context soap binding's style
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
    ctx.addParameter("bindingStyle", style);
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.BindingOperation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    BindingOperation op,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.removeParameter("operationStyle");
  }

  /* 
   * Verify style of soap operation is "rpc" if not then it's canceled process. 
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPOperation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPOperation operation,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.addParameter("operationStyle", operation.getStyle());
  }

  /* 
   * If use of soap body is "literal" and if at least one part is define using "element" attribute it creates falure report.    
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPBody, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPBody body, Object parent, WSDLTraversalContext ctx)
  {
    String opStyle = (String) ctx.getParameter("operationStyle");
    String bStyle = (String) ctx.getParameter("bindingStyle");

    if ((opStyle == null
        && !WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC.equals(bStyle))
      || (opStyle != null 
      	&& !WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC.equals(opStyle)))
    {
      ctx.cancelBindingOperationProcessing();
      return;
    }
    // assert use == "literal"
    if (WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT.equals(body.getUse()))
    {
      rpcLiteralFound = true;

      // find corresponding message
      Operation op = ctx.getBindingOperation().getOperation();
      // if some links are broken, cancel processing
      if (op == null
        || (parent instanceof BindingInput && op.getInput() == null)
        || (parent instanceof BindingOutput && op.getOutput() == null))
        return;

      Message m =
        (parent instanceof BindingInput)
          ? op.getInput().getMessage()
          : op.getOutput().getMessage();
      // REMOVE: current message should equals to message given as entry
      //if (m == null || message != m)
      if (m == null)
        return;

      List parts =
        WSDLUtil.getParts(
          op,
          m,
          body,
          WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC);
      if (parts == null)
        return;

      /* check whether parts list contains a parts which uses
       * element attribute. If so, part name will be added into
       * errors set.
       */
      Iterator it = parts.iterator();
      while (it.hasNext())
      {
        Part part = (Part) it.next();
        if (part.getElementName() != null)
          errors.add(part.getName());
      }
    }
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
    result = AssertionResult.RESULT_FAILED;
    // save message 
    //message = (Message)entryContext.getEntry().getEntryDetail();

    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);;
    traversal.setVisitor(this);
    traversal.visitSOAPBinding(true);
    traversal.visitBindingOperation(true);
    traversal.visitSOAPOperation(true);
    traversal.visitSOAPBody(true);

    traversal.ignoreReferences();
    traversal.ignoreImport();

    traversal.traverse((Binding) entryContext.getEntry().getEntryDetail());

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
    }

    else if (!rpcLiteralFound)
      result = AssertionResult.RESULT_NOT_APPLICABLE;

    else
      result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}