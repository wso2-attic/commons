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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;


/**
* BP2416. 
*   <context>For a candidate wsdl:definitions</context>
*   <assertionDescription>Every QName in the WSDL document and its imports, is referencing an element in a namespace that has either been imported or defined in the WSDL document that contains the reference.</assertionDescription>
*/
public class BP2416 extends AssertionProcessVisitor
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2416(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private static final String NS_LIST_KEY = "namespaces";

  /**
   * Checks whether the namespace is defined or imported in the definition.
   * @param qname
   * @param ctx
   */
  private void checkNamespace(QName qname, WSDLTraversalContext ctx)
  {
    if (qname != null)
    {
      String namespace = qname.getNamespaceURI();

      if (namespace != null)
      {
        List namespaces = (List) ctx.getParameter(NS_LIST_KEY);

        if (!namespaces.contains(namespace))
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetailMessage = namespace;

          ctx.cancelProcessing();
        }
      } // ??? should we do something otherwise  
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Port, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Port port, Object parent, WSDLTraversalContext ctx)
  {
    if (port != null)
    {
      Binding binding = port.getBinding();
      if (binding != null)
      {
        checkNamespace(binding.getQName(), ctx);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Binding, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Binding binding, Object parent, WSDLTraversalContext ctx)
  {
    if (binding != null)
    {
      PortType pType = binding.getPortType();
      if (pType != null)
      {
        checkNamespace(pType.getQName(), ctx);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Operation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    Operation operation,
    Object parent,
    WSDLTraversalContext ctx)
  {
    if (operation != null)
    {
      Input input = operation.getInput();
      if (input != null && input.getMessage() != null)
      {
        checkNamespace(input.getMessage().getQName(), ctx);
      }

      Output output = operation.getOutput();
      if (output != null && output.getMessage() != null)
      {
        checkNamespace(output.getMessage().getQName(), ctx);
      }

      Map faults = operation.getFaults();
      for (Iterator iter = faults.values().iterator(); iter.hasNext();)
      {
        Fault fault = (Fault) iter.next();

        if (fault.getMessage() != null)
        {
          checkNamespace(fault.getMessage().getQName(), ctx);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Message, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  /* REMOVE: This is already done in WSI2417
  public void visit(Message message, Object parent, WSDLTraversalContext ctx) {
   if (message != null) {
     Map parts = message.getParts();
     for (Iterator iter = parts.values().iterator(); iter.hasNext();) {
       Part part = (Part) iter.next();
       
       checkNamespace(part.getElementName(), ctx);                
       checkNamespace(part.getTypeName(), ctx);
     }
   }
  }
  */

  /* (non-Javadoc)
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_PASSED;

    // Get the definition from the entry context
    Definition definition =
      (Definition) entryContext.getEntry().getEntryDetail();

    // get list of namespaces imported and defined in the definition
    List namespaces = this.validator.getWSDLTargetNamespaceList(definition);

    // REMOVE:
    //Map namespaces = definition.getNamespaces();

    // traverse definition to check namespaces
    Map params = new HashMap();
    params.put(NS_LIST_KEY, namespaces);

    WSDLTraversal traversal = new WSDLTraversal();
    // VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.visitOperation(true);
    traversal.visitBinding(true);
    traversal.visitMessage(true);
    traversal.visitPort(true);

    traversal.ignoreImport();
    traversal.ignoreReferences();
    traversal.traverse(definition, params);

    if (result.equals(AssertionResult.RESULT_FAILED))
    {
      this.validator.createFailureDetail(failureDetailMessage, entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}