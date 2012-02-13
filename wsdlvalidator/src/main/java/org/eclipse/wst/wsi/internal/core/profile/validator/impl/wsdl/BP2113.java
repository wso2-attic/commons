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

import java.util.HashMap;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;

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
 * BP2113.
   * <context>For a candidate wsdl:binding element</context>
   * <assertionDescription>The soapbind:header, soapbind:headerfault and soapbind:fault elements only refer to wsdl:part element(s) that have been defined using the "element" attribute.</assertionDescription>
 */
public class BP2113 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2113(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  /* 
   * Verify soap header uses part is define using "element" attribute   
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPHeader, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPHeader header,
    Object parent,
    WSDLTraversalContext ctx)
  {
    Definition d = (Definition) ctx.getParameter("definition");
    Message m = d.getMessage(header.getMessage());
    if (m != null)
      checkPart(m.getPart(header.getPart()), ctx.getBinding());
  }

  /* 
   * Verify soap headerfault uses part is define using "element" attribute
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPHeaderFault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPHeaderFault fault,
    Object parent,
    WSDLTraversalContext ctx)
  {
    Definition d = (Definition) ctx.getParameter("definition");
    Message m = d.getMessage(fault.getMessage());
    // if message or part is not found - NOT_APPLICABLE ????
    if (m != null)
      checkPart(m.getPart(fault.getPart()), ctx.getBinding());
  }

  /* 
   * Verify soap fault uses part is define using "element" attribute
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPFault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPFault fault, Object parent, WSDLTraversalContext ctx)
  {
    String faultName = fault.getName();
    if (faultName == null)
      faultName = ctx.getBindingFault().getName();
    Operation op = ctx.getBindingOperation().getOperation();
    if (op == null /* || faultName == null*/
      ) // may be it's possible to have legal fault with null name
      return;
    // we suppose that SOAPFault.getName() corresponds to the abstract operation's fault name			
    Fault f = op.getFault(faultName);
    if (f == null)
      return;
    Message m = f.getMessage();
    // message should have only one part
    if (m == null || m.getParts() == null || m.getParts().size() != 1)
      return;
    checkPart((Part) m.getOrderedParts(null).get(0), ctx.getBinding());
  }

  /*
   * Verify part is define using "element" attribute.
   * @param p - part
   * @param b - binding
  */
  // refactoring
  private void checkPart(Part p, Binding b)
  {
    if (p != null && p.getElementName() == null)
      errors.add(b.getQName(), p.getName());
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

    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);;
    traversal.setVisitor(this);
    traversal.visitSOAPHeader(true);
    traversal.visitSOAPHeaderFault(true);
    traversal.visitSOAPFault(true);

    Map m = new HashMap();
    Definition def = entryContext.getWSDLDocument().getDefinitions();
    WSDLUtil.expandDefinition(def);
    m.put("definition", def);
    traversal.traverse((Binding) entryContext.getEntry().getEntryDetail(), m);

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