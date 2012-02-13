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
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
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
 * BP2117.
   * <context>For a candidate wsdl:binding element, referred to by an rpc-literal soap:binding</context>
   * <assertionDescription>The rpc-literal binding does not have a namespace attribute specified on a contained soapbind:header, soapbind:headerfault, and soapbind:fault element.</assertionDescription>
 */
public class BP2117 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2117(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private boolean rpcLiteralFound = false;
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

  /* 
   * If use of soap body is "literal" and if at least one part is define using "element" attribute it creates falure report.    
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPBody, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPBody body, Object parent, WSDLTraversalContext ctx)
  {
    // assert use == "literal"
    if (WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT.equals(body.getUse())
      && ((String) ctx.getParameter("style")).equals(
        WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC))
    {
      rpcLiteralFound = true;
    }
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
    if (!WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC.equals(style))
      ctx.cancelBindingOperationProcessing();
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPHeader, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPHeader header,
    Object parent,
    WSDLTraversalContext ctx)
  {
    check(header.getUse(), header.getNamespaceURI(), ctx);
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPHeaderFault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPHeaderFault fault,
    Object parent,
    WSDLTraversalContext ctx)
  {
    check(fault.getUse(), fault.getNamespaceURI(), ctx);
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPFault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPFault fault, Object parent, WSDLTraversalContext ctx)
  {
    check(fault.getUse(), fault.getNamespaceURI(), ctx);
  }

  // refactoring
  // check LITERAL use and != namespace
  // canceling further processing
  private void check(String use, String namespace, WSDLTraversalContext ctx)
  {
    if (WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT.equals(use)
      && namespace != null)
    {
      errors.add(ctx.getBinding().getQName());
      //              suppresses subsequent processing, because error list contains only failed binding
      ctx.cancelProcessing();
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
    traversal.visitSOAPHeader(true);
    traversal.visitSOAPHeaderFault(true);
    traversal.visitSOAPFault(true);
    traversal.visitSOAPOperation(true);

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