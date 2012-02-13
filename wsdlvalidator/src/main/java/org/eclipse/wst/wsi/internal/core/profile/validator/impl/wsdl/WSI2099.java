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

import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Input;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Service;
import javax.wsdl.Types;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Element;


/**
 * WSI2099.
   * <context>For a candidate wsdl:definitions, </context>
   * <assertionDescription>A WS-I conformance annotation does not appear outside a wsdl:port, wsdl:binding, wsdl:portType, wsdl:operation, or wsdl:message element.</assertionDescription>
 */
public class WSI2099 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public WSI2099(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();
  private boolean containsClaims = false;

  /**
   *  Check a WS-I conformance annotation does not appear outside a wsdl:port, wsdl:binding, wsdl:portType, wsdl:operation, or wsdl:message element if not it creates failure report.
   * @see org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLVisitor#visit(org.w3c.dom.Element, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Element el, Object parent, WSDLTraversalContext ctx)
  {
    // conformance claim should be within : port, binding, porttype, operation, message
    if (containsConformanceClaim(el))
    {
      containsClaims = true;

      if (parent instanceof BindingFault)
        errors.add(WSDL_FAULT, ((BindingFault) parent).getName());
      else if (parent instanceof BindingInput)
        errors.add(WSDL_INPUT, ((BindingInput) parent).getName());
      else if (parent instanceof BindingOperation)
        errors.add(WSDL_OPERATION, ((BindingOperation) parent).getName());
      else if (parent instanceof BindingOutput)
        errors.add(WSDL_OUTPUT, ((BindingOutput) parent).getName());
      else if (parent instanceof Definition)
        errors.add(WSDL_DEFINITIONS, ((Definition) parent).getQName());
      else if (parent instanceof Service)
        errors.add(WSDL_SERVICE, ((Service) parent).getQName());
      else if (parent instanceof Fault)
        errors.add(WSDL_FAULT, ((Fault) parent).getName());
      else if (parent instanceof Output)
        errors.add(WSDL_INPUT, ((Output) parent).getName());
      else if (parent instanceof Import)
        errors.add(WSDL_IMPORT, ((Import) parent).getNamespaceURI());
      else if (parent instanceof Input)
        errors.add(WSDL_INPUT, ((Input) parent).getName());
      else if (parent instanceof Part)
        errors.add(WSDL_PART, ((Part) parent).getName());
      else if (parent instanceof Types)
        errors.add(WSDL_TYPES);
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
    traversal.visitElement(true);
    /*
    traversal.
    	ignorePort2Element().
    	ignoreMessage2Element().
    	ignoreBinding2Element().
    	ignorePortType2Element().
    	ignoreOperation2Element();		
      */
    traversal.ignoreReferences();
    traversal.ignoreImport();
    traversal.traverse((Definition) entryContext.getEntry().getEntryDetail());

    // create detail message with the list of error elements
    if (!errors.isEmpty())
    {
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
      result = AssertionResult.RESULT_FAILED;
    }

    else if (!containsClaims)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    else
    {
      result = AssertionResult.RESULT_PASSED;
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /**
   * Check the documentation element whether contains conformance claims. 
   * Returns true if documentation element contains conformance claim. 
   * @param el
   * @return boolean
   */
  private boolean containsConformanceClaim(Element el)
  {
    if (el == null)
      return false;
    el = XMLUtils.findChildElement(el, WSI_CLAIM);
    return (el != null);
  }
}