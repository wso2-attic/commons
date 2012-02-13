/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import javax.wsdl.Definition;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.xml.namespace.QName;

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
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * BP2103.
   * <context>For each a candidate wsdl:definitions</context>
   * <assertionDescription>For the referenced definitions as well as all imported 
   * descriptions, The XML schema import statement is only used within an xsd:schema 
   * element.</assertionDescription>
 */
public class BP2103 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2103(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  /* Check all unknown extensibility elements it is not xsd import.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.ExtensibilityElement, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    ExtensibilityElement el,
    Object parent,
    WSDLTraversalContext ctx)
  {
    if (el instanceof Schema)
      searchForImport(((Schema) el).getElement());
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

    //processWSDL(entryContext.getWSDLDocument().getFilename());
    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.ignoreImport();
    traversal.visitExtensibilityElement(true);

    traversal.ignoreReferences();
    traversal.traverse((Definition) entryContext.getEntry().getEntryDetail());

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
    }
    else
      result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /*
   * Create falure report if node is xsd import. 
   * @param n - node
   */
  private void searchForImport(Node n)
  {
    while (n != null)
    {
      // searches for xsd:import element
      if (Node.ELEMENT_NODE == n.getNodeType())
      {
        if (XMLUtils.equals(n, ELEM_XSD_IMPORT))
        {
          Attr a =
            XMLUtils.getAttribute((Element) n, ATTR_XSD_SCHEMALOCATION);
          String schemaLocation = (a != null) ? a.getValue() : "";
          a = XMLUtils.getAttribute((Element) n, ATTR_XSD_NAMESPACE);
          String namespace = (a != null) ? a.getValue() : "";
          errors.add(new QName(namespace, schemaLocation));
        }

        else
        {
          // if xsd:schema element is found -> process schema
          if (!XMLUtils.equals(n, ELEM_XSD_SCHEMA))
            searchForImport(n.getFirstChild());
        }
      }

      n = n.getNextSibling();
    }
  }
}