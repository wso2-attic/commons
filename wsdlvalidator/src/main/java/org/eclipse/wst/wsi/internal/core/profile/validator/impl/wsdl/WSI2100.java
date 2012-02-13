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
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;

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
 * WSI2100.
   * <context>For a candidate wsdl:definitions, if it contains a wsdl:port, wsdl:binding, wsdl:portType, wsdl:operation, or wsdl:message, which in turn contains a conformance annotation</context>
   * <assertionDescription>The conformance annotation of the wsdl:port, wsdl:binding, wsdl:portType, wsdl:operation, or wsdl:message, validates to the schema defined in the Basic Profile, and is direct child of the documentation element for the WSDL element. The schema definition uses "http://ws-i.org/schemas/conformanceClaim/" as the targetNamespace. </assertionDescription>
 */
public class WSI2100 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public WSI2100(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private boolean containsClaims = false;
  private ErrorList errors = new ErrorList();

  /* Create falure report if port contains illegal claim.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Port, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Port port, Object parent, WSDLTraversalContext ctx)
  {
    if (port != null && !containsLegalClaim(port.getDocumentationElement()))
      errors.add(WSDL_PORT, port.getName());
  }

  /* Create falure report if binding contains illegal claim.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Binding, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Binding binding, Object parent, WSDLTraversalContext ctx)
  {
    if (binding != null
      && !containsLegalClaim(binding.getDocumentationElement()))
      errors.add(WSDL_BINDING, binding.getQName());
  }

  /* Create falure report if port type contains illegal claim.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.PortType, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(PortType type, Object parent, WSDLTraversalContext ctx)
  {
    if (type != null && !containsLegalClaim(type.getDocumentationElement()))
      errors.add(WSDL_PORTTYPE, type.getQName());
  }

  /* Create falure report if operation contains illegal claim.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Operation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    Operation operation,
    Object parent,
    WSDLTraversalContext ctx)
  {
    if (operation != null
      && !containsLegalClaim(operation.getDocumentationElement()))
      errors.add(WSDL_OPERATION, operation.getName());
  }

  /* Create falure report if message contains illegal claim.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Message, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Message message, Object parent, WSDLTraversalContext ctx)
  {
    if (message != null
      && !containsLegalClaim(message.getDocumentationElement()))
      errors.add(WSDL_MESSAGE, message.getQName());
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
    traversal.visitOperation(true);
    traversal.visitBinding(true);
    traversal.visitMessage(true);
    traversal.visitPort(true);
    traversal.visitPortType(true);
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
      result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /**
   * Check the documentation element whether contains conformance claims.
   * Returns true if documentation element contains conformance claim with
   * illegal schema definition URI. 
   * legal is 'http://ws-i.org/schemas/conformanceClaim/'
   * @param el
   * @return boolean
   */
  private boolean containsLegalClaim(Element el)
  {
    if (el == null)
      return true;
    // find claim
    el = XMLUtils.findChildElement(el, WSI_CLAIM);

    if (el != null)
      containsClaims = true;

    while (el != null)
    {
      if (XMLUtils.getAttribute(el, ATTR_CLAIM_CONFORMSTO) == null
        || XMLUtils.getAttribute(el, ATTR_SOAP_MUSTUNDERSTAND) != null)
        return false;
      el = XMLUtils.findElement(el, WSI_CLAIM);
    }
    return true;
  }
}