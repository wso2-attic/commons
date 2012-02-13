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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOutput;
import javax.wsdl.Fault;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;

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
 * BP2114.
 * <context>For a candidate wsdl:binding element</context>
 * 
 * 
 * <assertionDescription>Every wsdl:part from each wsdl:message in the associated 
 * wsdl:portType is referenced either by the soapbind:body, soapbind:header, 
 * soapbind:fault, or soapbind:headerfault.</assertionDescription>
 */
public class BP2114 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2114(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();
  // map Message QName -> set(Part name)
  public Map messages = new HashMap();

  /* 
   * Add to Map parts which used by message
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Message, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Message m, Object parent, WSDLTraversalContext ctx)
  {
    if (m != null
      && !messages.containsKey(m)
      && m.getOrderedParts(null) != null)
    {
      Set s = new HashSet();
      Iterator it = m.getOrderedParts(null).iterator();
      while (it.hasNext())
        s.add(((Part) it.next()).getName());

      //messages.put(m.getQName(), s);
      if (s.size() > 0)
      {
        messages.put(m.getQName(), s);
      }
    }
  }

  /* 
   * Put in context style of soap binding
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
    if (style != null)
      ctx.addParameter("style", style);
  }

  /* 
   * Remove parts of soap body from Map.  
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPBody, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPBody body, Object parent, WSDLTraversalContext ctx)
  {
    // String style = (String)ctx.getParameter("style");

    // find corresponding message
    Operation op = ctx.getBindingOperation().getOperation();
    // if some links are broken, cancel processing
    if (op == null)
      return;
    Message m = null;
    if (parent instanceof BindingInput)
    {
      if (op.getInput() == null)
        return;
      m = op.getInput().getMessage();
    }
    else if (parent instanceof BindingOutput)
    {
      if (op.getOutput() == null)
        return;
      m = op.getOutput().getMessage();
    }
    if (m == null)
      return;

    // get parts
    List parts = WSDLUtil.getParts(op, m, body, null);
    if (parts == null)
      return;

    // iterate all parts and check
    Iterator it = parts.iterator();
    while (it.hasNext())
      removePart(m.getQName(), ((Part) it.next()).getName());
  }

  /* 
   * Remove parts of soap header from Map.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPHeader, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPHeader header,
    Object parent,
    WSDLTraversalContext ctx)
  {
    // we suppose that it possible to have legal message and part with null names
    // ATTENTION
    // should we add message which is referenced by header or headerfault to messages ?
    removePart(header.getMessage(), header.getPart());
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPHeaderFault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPHeaderFault fault,
    Object parent,
    WSDLTraversalContext ctx)
  {
    // we suppose that it possible to have legal message and patr with null names
    QName messageName = fault.getMessage();
    if (messageName == null)
      messageName = ctx.getSOAPHeader().getMessage();
    removePart(messageName, fault.getPart());
  }

  /* 
   * Remove parts of soap fault from Map.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPFault, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPFault fault, Object parent, WSDLTraversalContext ctx)
  {
    String faultName = fault.getName();
    if (faultName == null)
      faultName = ctx.getBindingFault().getName();
    Operation op = ctx.getBindingOperation().getOperation();
    if (op == null)
      return;

    // we suppose that SOAPFault.getName() corresponds to the abstract operation's fault name           
    Fault f = op.getFault(faultName);

    // fault message should have 1 part
    if (f == null
      || f.getMessage() == null
      || f.getMessage().getParts().size() != 1)
      return;

    Part p = (Part) f.getMessage().getOrderedParts(null).get(0);
    removePart(f.getMessage().getQName(), p.getName());
  }

  /*
   * Remove part from Map. 
  * @param messageName - message
  * @param partName - part
  */
  // refatoring
  private void removePart(QName messageName, String partName)
  {
    Set parts = (Set) messages.get(messageName);
    if (parts != null)
    {
      parts.remove(partName);
      if (parts.size() == 0)
        messages.remove(messageName);
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
    result = AssertionResult.RESULT_WARNING;

    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.visitMessage(true);
    traversal.visitSOAPBinding(true);
    traversal.visitSOAPBody(true);
    traversal.visitSOAPHeader(true);
    traversal.visitSOAPHeaderFault(true);
    traversal.visitSOAPFault(true);
    traversal.visitSOAPOperation(true);

    Binding b = (Binding) entryContext.getEntry().getEntryDetail();
    traversal.traverse(b);

    if (messages.size() > 0)
      errors.add(b.getQName());

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_WARNING;
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
    }

    else
      result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}