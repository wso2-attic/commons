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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.wsdl.Binding;
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
 * BP2120.
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>Each operation referenced by the binding results in a unique wire signature.</assertionDescription>
 */
public class BP2120 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2120(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();
  private Set wares = new HashSet();

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
    if (style != null)
      ctx.addParameter("style", style);
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPBody, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(SOAPBody body, Object parent, WSDLTraversalContext ctx)
  {
    List signature = new LinkedList();

    String style = (String) ctx.getParameter("style");

    // find corresponding message
    Operation op = ctx.getBindingOperation().getOperation();

    // if some links are broken, cancel processing
    if (op == null
      || op.getInput() == null
      || op.getInput().getMessage() == null)
      return;
    Message m = op.getInput().getMessage();

    List parts = WSDLUtil.getParts(op, m, body, style);
    if (parts == null)
      return;
    // !! ATTENTION
    // may be required to add types instead of part into signature ?

    // create signature
    signature.addAll(parts);

    // suppose that wire signature for
    // - rpc style = operation name + parts' qname
    // - document style = parts' qname
    if (WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC.equals(style))
      signature.add(0, op.getName());

    if (sameSignature(wares, signature))
      errors.add(op.getName());
    else
      wares.add(signature);
  }

  /**
   * Check if the signature is in the set match.
   */
  private boolean sameSignature(Set signatureSet, List signature)
  {
    boolean same = false;

    // Get iterator for set
    Iterator set = signatureSet.iterator();
    while (set.hasNext() && !same)
    {
      // Get next signature from the set
      List nextSignature = (List) set.next();

      // Only continue if the signatures are the same size
      if (nextSignature.size() == signature.size())
      {
        Iterator iterator1 = nextSignature.iterator();
        Iterator iterator2 = signature.iterator();

        // Assume same until a difference is found
        same = true;

        // Process each list until a difference is found
        while (iterator1.hasNext() && iterator2.hasNext() && same)
        {
          // Get the next elements
          Object element1 = iterator1.next();
          Object element2 = iterator2.next();

          // If the element is a string, then if equal check parts
          if ((element1 instanceof String)
            && (element2 instanceof String)
            && (((String) element1).equals((String) element2)))
          {
            same = true;
          }

          // If the elements are parts, then check if they are equal
          else if ((element1 instanceof Part) && (element2 instanceof Part))
          {
            same = sameParts((Part) element1, (Part) element2);
          }

          else
          {
            same = false;
          }
        }
      }
    }

    return same;
  }

  /**
   * Check if two parts are the same.
   */
  private boolean sameParts(Part part1, Part part2)
  {
    boolean same = false;

    // If the part has an element then see if they are the same
    if (part1.getElementName() != null
      && part2.getElementName() != null
      && part1.getElementName().equals(part2.getElementName()))
    {
      same = true;
    }

    // If the part has an type then see if they are the same
    else if (
      part1.getTypeName() != null
        && part2.getTypeName() != null
        && part1.getTypeName().equals(part2.getTypeName()))
    {
      same = true;
    }

    else
    {
      same = false;
    }

    return same;
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
    traversal.ignoreBindingOutput();
    traversal.traverse((Binding) entryContext.getEntry().getEntryDetail());

    // !! ATTENTION
    // Analyze soapbind:body:namespace and service targetNamespace            
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