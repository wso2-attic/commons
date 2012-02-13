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
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.eclipse.wst.wsi.internal.core.util.WSDLUtil;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;


/**
 * BP2121.
 * Context:
 * For  a wsdl:binding element which is referenced directly or associated
 * with a specified wsdl:port.
 * 
 * Assertion Description:
 * Each  operation referenced by a document-literal binding must have a
 * message definition where the wsdl:part references a global element
 * definition.
 */
public class BP2121 extends AssertionProcessVisitor implements WSITag
{

  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2121(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();
  private TypesRegistry registry = null;

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

  public void visit(SOAPBody body, Object parent, WSDLTraversalContext ctx)
  {
    // !! ATTENTION
    // we suppose that soapbody child elements is its parts.
    // assert style == "document"
    if (WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT.equals(body.getUse()))
    {
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
      if (m == null)
        return;

      // find parts which are involved in the given body
      List parts =
        WSDLUtil.getParts(
          op,
          m,
          body,
          WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC);
      if (parts == null)
        return;

      // check parts
      Iterator it = parts.iterator();
      while (it.hasNext())
      {
        Part p = (Part) it.next();
        // check whether p declared as global element
        if (p.getElementName() == null
          || registry.getType(p.getElementName()) == null)
        {
          errors.add(op.getName());
          //						suppresses subsequent processing, because error list contains only failed operation
          ctx.cancelBindingOperationProcessing();
        }
      }
    }
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_FAILED;

    registry =
      new TypesRegistry(
        entryContext.getWSDLDocument().getDefinitions(),
        validator);

    // collect all types from messages to checkedParts map
    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.visitSOAPBinding(true);
    traversal.visitSOAPBody(true);
    traversal.visitSOAPOperation(true);
    traversal.ignoreReferences();
    traversal.ignoreImport();
    traversal.traverse((Binding) entryContext.getEntry().getEntryDetail());

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