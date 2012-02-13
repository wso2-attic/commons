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
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Element;


/**
 * BP2123. 
 * <context>For a candidate wsdl:definitions, if it contains a wsdl:port, wsdl:binding, wsdl:portType, wsdl:operation, or wsdl:message</context>
 * <assertionDescription>Contained WSDL extension elements do not use the wsdl:required attribute value of "true".</assertionDescription>
 */
public class BP2123 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2123(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private boolean claimFound = false;

  private static final String PORT_KEY = "port";
  private static final String BINDING_KEY = "binding";
  private static final String PORT_TYPE_KEY = "port type";
  private static final String OPERATION_KEY = "operation";
  private static final String MESSAGE_KEY = "message";

  /* (non-Javadoc)
  * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Port, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
  */
  public void visit(Port port, Object parent, WSDLTraversalContext ctx)
  {
    if (port != null)
    {
      if (containsLegalClaim(port.getDocumentationElement()))
      {
        claimFound = true;
        ctx.addParameter(PORT_KEY, Boolean.TRUE);
      }
      else
      {
        ctx.addParameter(PORT_KEY, Boolean.FALSE);
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
      Boolean port = (Boolean) ctx.getParameter(PORT_KEY);

      if (port.booleanValue())
      {
        ctx.addParameter(BINDING_KEY, Boolean.TRUE);
      }
      else
      {
        claimFound = containsClaim(binding.getDocumentationElement());
        ctx.addParameter(
          BINDING_KEY,
          new Boolean(containsLegalClaim(binding.getDocumentationElement())));
      }
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.PortType, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(PortType type, Object parent, WSDLTraversalContext ctx)
  {
    if (type != null)
    {
      Boolean binding = (Boolean) ctx.getParameter(BINDING_KEY);

      if (binding.booleanValue())
      {
        ctx.addParameter(PORT_TYPE_KEY, Boolean.TRUE);
      }
      else
      {
        claimFound = containsClaim(type.getDocumentationElement());
        ctx.addParameter(
          PORT_TYPE_KEY,
          new Boolean(containsLegalClaim(type.getDocumentationElement())));
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
      Boolean type = (Boolean) ctx.getParameter(PORT_TYPE_KEY);

      if (type.booleanValue())
      {
        ctx.addParameter(OPERATION_KEY, Boolean.TRUE);
      }
      else
      {
        claimFound = containsClaim(operation.getDocumentationElement());
        ctx.addParameter(
          OPERATION_KEY,
          new Boolean(
            containsLegalClaim(operation.getDocumentationElement())));
      }
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Message, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Message message, Object parent, WSDLTraversalContext ctx)
  {
    if (message != null)
    {
      Boolean operation = (Boolean) ctx.getParameter(OPERATION_KEY);

      if (operation.booleanValue())
      {
        ctx.addParameter(MESSAGE_KEY, Boolean.TRUE);
      }
      else
      {
        claimFound = containsClaim(message.getDocumentationElement());
        ctx.addParameter(
          MESSAGE_KEY,
          new Boolean(containsLegalClaim(message.getDocumentationElement())));
      }
    }
  }

  /*
  public void visit(BindingOperation operation, Object parent, WSDLTraversalContext ctx) {
  	if (operation != null) {
  		if (containsLegalClaim(operation.getDocumentationElement())) {
  			System.out.println("binding operation found");
  		} else {
  			
  		}
  	}
  }
  */

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.ExtensibilityElement, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */

  public void visit(
    ExtensibilityElement el,
    Object parent,
    WSDLTraversalContext ctx)
  {
    boolean required =
      el != null
        && el.getRequired() != null
        && el.getRequired().booleanValue();

    if (isParentConforms(parent, ctx))
    {
      if (required)
      {
        result = AssertionResult.RESULT_WARNING;
        failureDetailMessage = el.toString();
      }
    }
  }

  /**
   * Indicates whether the parent element conforms to the basic profile.
   * @param parent
   * @param ctx
   * @return boolean
   */
  private boolean isParentConforms(Object parent, WSDLTraversalContext ctx)
  {
    boolean result = false;

    if (parent instanceof Port)
    {
      result = ((Boolean) ctx.getParameter(PORT_KEY)).booleanValue();
    }
    else if (
      parent instanceof Binding
        || parent instanceof BindingOperation
        || parent instanceof BindingInput
        || parent instanceof BindingOutput
        || parent instanceof BindingFault)
    {
      result = ((Boolean) ctx.getParameter(BINDING_KEY)).booleanValue();
    }
    else if (parent instanceof PortType)
    {
      result = ((Boolean) ctx.getParameter(PORT_TYPE_KEY)).booleanValue();
    }
    else if (
      parent instanceof Operation
        || parent instanceof Input
        || parent instanceof Output
        || parent instanceof Fault)
    {
      result = ((Boolean) ctx.getParameter(OPERATION_KEY)).booleanValue();
    }
    else if (parent instanceof Message || parent instanceof Part)
    {
      result = ((Boolean) ctx.getParameter(MESSAGE_KEY)).booleanValue();
    }

    return result;
  }

  /**
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(TestAssertion, EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_PASSED;

    // Expect wsdl:defintions
    Definition def = (Definition) entryContext.getEntry().getEntryDetail();
    //((Service)def.getServices().values().toArray()[0]).getPorts()
    // Traverse WSDL
    WSDLTraversal traversal = new WSDLTraversal();
    // VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.visitOperation(true);
    traversal.visitBinding(true);
    traversal.visitMessage(true);
    traversal.visitPort(true);
    traversal.visitPortType(true);
    traversal.visitExtensibilityElement(true);

    traversal.ignoreImport();
    traversal.ignoreDefinition2Binding();
    traversal.ignoreDefinition2Element();
    traversal.ignoreDefinition2ExtensibilityElement();
    traversal.ignoreDefinition2Message();
    traversal.ignoreDefinition2PortType();
    traversal.ignoreDefinition2Types();

    traversal.traverse(def);

    if (!claimFound)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    else if (result.equals(AssertionResult.RESULT_WARNING))
    {
      failureDetail = this.validator.createFailureDetail(failureDetailMessage, entryContext);
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /**
   * Check the documentation element whether contains conformance claims.
   * Returns true if documentation element contains conformance claim with
   * legal schema definition URI. 
   * legal is 'http://ws-i.org/schemas/conformanceClaim/'
   * @param el
   * @return boolean
   */
  private boolean containsClaim(Element el)
  {
    if (el == null)
      return false;
    // find claim
    el = XMLUtils.findChildElement(el, WSI_CLAIM);
    if (el != null)
    {
      return true;
    }
    return false;
  }

  /**
   * Check the documentation element whether contains conformance claims.
   * Returns true if documentation element contains conformance claim with
   * legal schema definition URI. 
   * legal is 'http://ws-i.org/schemas/conformanceClaim/'
   * @param el
   * @return boolean
   */
  private boolean containsLegalClaim(Element el)
  {
    if (el == null)
      return false;
    // find claim
    el = XMLUtils.findChildElement(el, WSI_CLAIM);
    while (el != null)
    {
      String value = el.getAttribute(ATTR_CLAIM_CONFORMSTO.getLocalPart());
      if (value != null
        && value.equalsIgnoreCase(WSIConstants.ATTRVAL_UDDI_CLAIM_KEYVALUE))
      {
        return true;
      }

      el = XMLUtils.findElement(el, WSI_CLAIM);
    }
    return false;
  }
}