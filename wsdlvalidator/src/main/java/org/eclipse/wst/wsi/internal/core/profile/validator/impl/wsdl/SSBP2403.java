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
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.UnknownExtensibilityElement;

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
import org.eclipse.wst.wsi.internal.core.xml.XMLTraversal;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * SSBP2403.
 * <context>For a candidate wsdl:binding element</context>
 * <assertionDescription>Descendant elements and attributes in the wsdl:binding are not from the namespaces for the WSDL MIME, HTTP GET/POST or DIME binding extensions.</assertionDescription>
 */
public class SSBP2403 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public SSBP2403(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }


  private ErrorList errors = new ErrorList();

  /**
   * Returns true if the the namespace specifies any WSDL MIME,
   * HTTP GET/POST or DIME binding extensions.
   * @param namespace a namespace.
   * @return true if the the namespace specifies any WSDL MIME,
   *         HTTP GET/POST or DIME binding extensions.
   */
  private boolean isNonConformantBindingExtension(String namespace)
  {
    boolean result = false;
    if (namespace != null)
    {
      if (namespace.equals(WSIConstants.NS_NAME_WSDL_MIME)
        || namespace.equals(WSIConstants.NS_NAME_WSDL_HTTP)
        || namespace.equals(WSIConstants.NS_NAME_WSDL_DIME))
        result = true;
    }
    return result;
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.ExtensibilityElement, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    ExtensibilityElement exel,
    Object parent,
    WSDLTraversalContext ctx)
  {
    if (isNonConformantBindingExtension(exel
      .getElementType()
      .getNamespaceURI()))
      errors.add(exel.getElementType());
    else
    {
    	// extensibility element is ok, check the descendants
      if (exel instanceof UnknownExtensibilityElement)
      {
        Element el = ((UnknownExtensibilityElement) exel).getElement();

        // traverse all of the descendants and check for non compliant binding extensions
        XMLTraversalCheckingExtensions traversal =
          new XMLTraversalCheckingExtensions();
        traversal.visit(el);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_FAILED;

    WSDLTraversal traversal = new WSDLTraversal();
    traversal.setVisitor(this);
    traversal.visitElement(true);
    traversal.visitExtensibilityElement(true);

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


  /**
   * A specialized XMLTraversal class to check if the wsdl:binding element has any 
   * descendant elements/attributes from the namespaces for the WSDL MIME, 
   * HTTP GET/POST or DIME binding extensions.
   */
  private class XMLTraversalCheckingExtensions extends XMLTraversal
  {
    boolean nonConformantExtensionFound = false;

    /* (non-Javadoc)
     * @see org.wsi.xml.XMLTraversal#action(org.w3c.dom.Node)
     */
    public boolean action(Node n)
    {
      return !nonConformantExtensionFound;
    }

    /* (non-Javadoc)
     * @see org.wsi.xml.XMLVisitor#visit(org.w3c.dom.Attr)
     */
    public void visit(Attr node)
    {
      if (action(node))
      {
        if (isNonConformantBindingExtension(node.getNamespaceURI()))
        {
          errors.add(node.getName());
          nonConformantExtensionFound = true;
        }
      }
    }

    /* (non-Javadoc)
     * @see org.wsi.xml.XMLVisitor#visit(org.w3c.dom.Element)
     */
    public void visit(Element node)
    {
      if (action(node))
      {
        if (isNonConformantBindingExtension(node.getNamespaceURI()))
        {
          errors.add(node.getNodeName());
          nonConformantExtensionFound = true;
        }
        else
        {
          // element is compliant, check attributes and descendants
          visit(node.getAttributes());
          for (Node n = node.getFirstChild();
            n != null;
            n = n.getNextSibling())
          {
            doVisit(n);
          }
        }
      }
    }
  }
}