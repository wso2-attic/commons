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
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;


/**
 * BP2417. 
 *   <context>For a candidate wsdl:definitions</context>
 *   <assertionDescription>Every QName in the WSDL document and its imports and that 
 *      is referring to a schema component, uses the namespace defined in the targetNamespace 
 *      attribute on the xs:schema element, or a namespace defined in the namespace attribute 
 *      on an xs:import element within the xs:schema element.</assertionDescription>
 */
public class BP2417 extends AssertionProcessVisitor
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2417(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errorList = new ErrorList();
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
        List namespaceList = (List) ctx.getParameter(NS_LIST_KEY);

        if ((namespaceList != null) && !namespaceList.contains(namespace))
        {
          errorList.add(qname);
        }
      }
    }
  }

  /**
   * @see org.eclipse.wst.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Message, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Message message, Object parent, WSDLTraversalContext ctx)
  {
    if (message != null)
    {
      Map parts = message.getParts();

      for (Iterator iter = parts.values().iterator(); iter.hasNext();)
      {
        Part part = (Part) iter.next();

        checkNamespace(part.getElementName(), ctx);
        checkNamespace(part.getTypeName(), ctx);
      }
    }
  }

  /**
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
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

    // Get a list of all schema targetNamespaces
    List tnsList = null;
    try
    {
      if ((tnsList = this.validator.getSchemaNamespaceList(definition)) == null)
        tnsList = new Vector();
    }

    catch (WSIException e)
    {
      // This exception would indicate that the schema definition had a problem
      tnsList = new Vector();
    }

    // Always add XML schema namespace to cover built-in types
    tnsList.add(WSIConstants.NS_URI_XSD);

    // Traverse definition to check schema namespaces
    Map params = new HashMap();
    params.put(NS_LIST_KEY, tnsList);

    WSDLTraversal traversal = new WSDLTraversal();
    traversal.setVisitor(this);
    traversal.visitMessage(true);

    traversal.ignoreImport();
    traversal.ignoreReferences();
    traversal.traverse(definition, params);

    // If there were no errors, then the assertion passed
    if (errorList.isEmpty())
    {
      result = AssertionResult.RESULT_PASSED;
    }

    // Otherwise it failed / create the failure detail using the QNames that were in error
    else
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errorList.toString(), entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}