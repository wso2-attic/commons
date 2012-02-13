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

import java.net.URISyntaxException;
import java.util.Iterator;

import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Port;
import javax.wsdl.extensions.soap.SOAPAddress;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;


/**
* BP4201. 
* <context>For a candidate wsdl:import element, where the location attribute or the namespace attribute has a value that is a relative URI, or a for soap:address element where the location attribute is a relative URI.</context>
* <assertionDescription>The use of a relative URI as the value for a wsdl:import location or namespace attribute, or for a soap:address location attribute may require out of band coordination.</assertionDescription>
*/
public class BP4201 extends AssertionProcessVisitor
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP4201(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }
  private ErrorList errorList = new ErrorList();

  /** 
   * (non-Javadoc)
   * @see org.eclipse.wst.wsi.wsdl.traversal.WSDLVisitor#visit(Import, Object, WSDLTraversalContext)
   */
  public void visit(Import obj, Object parent, WSDLTraversalContext ctx)
  {
    if(obj != null)
    {
      if(isRelativeURI(obj.getNamespaceURI()) || 
          isRelativeURI(obj.getLocationURI()))
      {
        errorList.add(obj.getNamespaceURI(), obj.getLocationURI());
      }
    }
  }

  /** 
   * (non-Javadoc)
   * @see org.eclipse.wst.wsi.wsdl.traversal.WSDLVisitor#visit(Port, Object, WSDLTraversalContext)
   */
  public void visit(Port obj, Object parent, WSDLTraversalContext ctx)
  {
    if(obj != null)
    {
      Iterator it = obj.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
         Object e = (Object) it.next();
         // for each SOAPAddress elements check LocationURI 
         if(e instanceof SOAPAddress) {
           if(isRelativeURI(((SOAPAddress) e).getLocationURI()))
           {
             errorList.add(((SOAPAddress) e).getElementType() + 
                 ":" + ((SOAPAddress) e).getLocationURI());
           }
         }
      }
    }
  }

  /**
   *  (non-Javadoc)
   *  @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
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

    WSDLTraversal traversal = new WSDLTraversal();
    traversal.setVisitor(this);
    traversal.visitImport(true);
    traversal.visitPort(true);
    traversal.ignoreReferences();
    traversal.traverse(definition);

    if (errorList.isEmpty()) 
    {
       return validator.createAssertionResult(testAssertion, 
             AssertionResult.RESULT_NOT_APPLICABLE, (String) null);
    }
    
    failureDetail = this.validator.createFailureDetail(testAssertion.getFailureMessage() + 
        "\n\n" + errorList.toString(), entryContext);
    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
  /**
   * Returns true if URI is relative 
   * @param s - URI
   * @return true - if URI is relative, false - in other case  
   */
  private boolean isRelativeURI(String s)
  {
    if(s == null)
      return false;
    try
    {
      if(new java.net.URI(s).isAbsolute())
      {
        return false;
      }
    } catch (URISyntaxException e)
    {
      return false;
    }
    return true;
  }
}