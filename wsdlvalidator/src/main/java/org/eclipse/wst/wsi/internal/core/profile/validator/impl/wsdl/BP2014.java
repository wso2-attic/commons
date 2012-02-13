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
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
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
 * BP2014.
 * Context:
 * For a candidate wsdl:operation, if referred to by a candidate rpc-literal wsdl:binding element
 * If the parameterOrder attribute is present, it omits at most 1 part from output wsdl:message.
 **/
public class BP2014 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2014(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  /*
   * @param binding - binding
   * @return It returns style of soap binding of binding. If style omitted returns "document" style.   
   */
  private String getSOAPBindingStyle(Binding binding)
  {
    String style = null;
    List list = binding.getExtensibilityElements();
    for (int i = 0; i < list.size(); i++)
    {
      if (list.get(i) instanceof SOAPBinding)
      {
        style = ((SOAPBinding) list.get(i)).getStyle();
        break;
      }
    }
    if (style == null)
      style = WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC;
    return style;
  }

  /*
   * @param list - List of extencibility elements from binding output
   * @return first soap body in list.
   */
  private SOAPBody getSOAPBody(List list)
  {
    for (int i = 0; i < list.size(); i++)
      if (list.get(i) instanceof SOAPBody)
        return (SOAPBody) list.get(i);
    return null;
  }

  /*
   * @param list - List of extencibility elements of binding operation
   * @return First soap operation in list.
   */
  private SOAPOperation getSOAPOperation(List list)
  {
    for (int i = 0; i < list.size(); i++)
      if (list.get(i) instanceof SOAPOperation)
        return (SOAPOperation) list.get(i);
    return null;
  }

  /*          
   * @param oper - operation
   * @param binding - binding
   * @return if style of soap binding of binding is rpc and use of soap body of binding output of binding operation is literal then it returns true. 
  */
  private boolean checkRpcLiteral(BindingOperation oper, Binding binding)
  {
    List list = oper.getExtensibilityElements();
    SOAPOperation sop = getSOAPOperation(list);
    if (sop == null)
      return false;
    String style =
      (sop.getStyle() == null
        ? getSOAPBindingStyle(binding)
        : sop.getStyle());
    if (!style.equals(WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC))
      return false;
    list = oper.getBindingOutput().getExtensibilityElements();
    SOAPBody body = getSOAPBody(list);
    if (body == null)
      return false;
    String use = body.getUse();
    if (use == null)
      return false;

    return use.equals(WSIConstants.ATTRVAL_SOAP_BODY_USE_LIT);
  }

  /* It checks operation contains more than one part in parameter order 
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Operation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Operation obj, Object parent, WSDLTraversalContext ctx)
  {
    Binding[] bindings = validator.analyzerContext.getCandidateInfo().getBindings();
    if (obj.getParameterOrdering() == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      return;
    }

    if (bindings == null || obj == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      return;
    }

    if (obj.getOutput() == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      return;
    }

    if (obj.getOutput().getMessage() == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      return;
    }

    // For each binding
    for (int i = 0; i < bindings.length; i++)
    {
      List list = bindings[i].getBindingOperations();
      // For each binding operation
      for (int j = 0; j < list.size(); j++)
      {
        BindingOperation oper = (BindingOperation) list.get(j);

        // If the input operation name and binding operatino name are not null
        if (obj.getName() != null && oper.getName() != null)
        {
          // If the operation names are equal
          if (obj.getName().equals(oper.getName()))
          {
            // If the operation is RPC literal
            if (checkRpcLiteral(oper, bindings[i]))
            {
              // Get the list of parts in the parameterOrder attribute				
              List parts = obj.getParameterOrdering();

              Output output = null;
              Message message = null;
              Map messageParts = null;

              // Get the list of parts for the output message
              if ((output = obj.getOutput()) != null)
              {
                if ((message = output.getMessage()) != null)
                {
                  messageParts = message.getParts();
                }
              }

              // If there are parts in the parameterOrder attribute 
              // and there are output message parts, then process assertion
              if (parts != null && messageParts != null)
              {
                int partCount = 0;

                String partName;
                Iterator iterator = parts.iterator();
                while (iterator.hasNext())
                {
                  // Get part name from parameterOrder list
                  partName = (String) iterator.next();

                  // Check each output message part to see if there is a match
                  if (messageParts.containsKey(partName))
                    partCount++;
                }

                if ((partCount == messageParts.size())
                  || (partCount == messageParts.size() - 1))
                  result = AssertionResult.RESULT_PASSED;
                else
                {
                  errors.add(obj.getName());
                  ctx.cancelOperationProcessing();
                }
                return;
              }

              else
              {
                if (parts == null)
                  result = AssertionResult.RESULT_NOT_APPLICABLE;
                else
                  result = AssertionResult.RESULT_PASSED;
                return;
              }
            }

            else
            {
              result = AssertionResult.RESULT_NOT_APPLICABLE;
              return;
            }
          }
        }

        else
        {
          result = AssertionResult.RESULT_NOT_APPLICABLE;
          return;
        }
      }
    }
    result = AssertionResult.RESULT_NOT_APPLICABLE;

  }

  /**
   * Validates the test assertion.
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
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
    traversal.visitOperation(true);
    traversal.ignoreImport();
    traversal.traverse((Operation) entryContext.getEntry().getEntryDetail());

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail =
        this.validator.createFailureDetail(
          errors.toString(),
          entryContext,
          entryContext.getEntry().getEntryDetail());
    }
    //else
    //  result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}