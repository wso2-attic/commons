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

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;

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
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * BP4200. 
 * <context>For a candidate wsdl:definitions that contains a wsdl extension element or attribute from a namespace other than "http://schemas.xmlsoap.org/wsdl/soap/".</context>
 * <assertionDescription>Contained WSDL extension elements that do not belong to the "http://schemas.xmlsoap.org/wsdl/soap/" namespaces may require out of band negotiation.</assertionDescription>
*/
public class BP4200 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;
  private ErrorList errorList = new ErrorList();

  /**
   * @param WSDLValidatorImpl
   */
  public BP4200(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.ExtensibilityElement, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(ExtensibilityElement obj, Object parent,
    WSDLTraversalContext ctx)
  {
    // If a child element of wsdl:types is from a namespace other than
    // "http://www.w3.org/2001/XMLSchema"
    if (parent instanceof Types)
    {
      //if (!obj.getElementType().getNamespaceURI().equals(
      //  WSIConstants.NS_URI_XSD))
      //{
      //  Add element name to error list
      //  errorList.add(obj.getElementType());
      //}
    }
    // or any other ext element coming from a namespace other than
    // "http://schemas.xmlsoap.org/wsdl/soap/"
    else if (!obj.getElementType().getNamespaceURI().equals(WSIConstants.NS_URI_WSDL_SOAP)
    	  && !obj.getElementType().getNamespaceURI().equals(WSIConstants.NS_URI_WSDL))
    {
      // Add element name to error list
      errorList.add(obj.getElementType()); 
    }
  }

   /**
  *  (non-Javadoc)
  *  @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(TestAssertion testAssertion,
    EntryContext entryContext) throws WSIException
  {
    // Get the definition from the entry context
    Definition definition =
      (Definition) entryContext.getEntry().getEntryDetail();

    WSDLTraversal traversal = new WSDLTraversal();
    traversal.setVisitor(this);

    traversal.visitExtensibilityElement(true);
    traversal.visitElement(true);

    traversal.ignoreImport();
    traversal.ignoreReferences();
    traversal.traverse(definition);

    try
	{
      // getting WSDL document.
      Document doc = validator.parseXMLDocumentURL(definition.getDocumentBaseURI(), null);
      if (doc != null)
      {   
        checkElement(doc.getDocumentElement());

        if (errorList.isEmpty())
        {
          result = AssertionResult.RESULT_NOT_APPLICABLE;
        }
        else
        {
          failureDetail = validator.createFailureDetail(
          testAssertion.getDetailDescription() + "\n\n" + errorList.toString(),
          entryContext);
        }
      }
	}   
    catch (Throwable t)
    {
	}
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Checking on wsdl elements recursively whether they contain extension attributes.
   * @param elem Element.
   */
  private void checkElement(Element elem)
  {
    while (elem != null)
    {
      String elemNS = elem.getNamespaceURI();
      NamedNodeMap attrs = elem.getAttributes();
      if (attrs != null)
      {
        for (int i = 0; i < attrs.getLength(); i++)
        {
          Attr attr = (Attr) attrs.item(i);
          String attrNS = attr.getNamespaceURI();
          if (attrNS != null
            && !attrNS.equals(WSIConstants.NS_URI_XMLNS)
            && !attrNS.equals(WSIConstants.NS_URI_WSDL)
			&& !attrNS.equals(WSIConstants.NS_URI_WSDL_SOAP))
          {
            errorList.add("Extensibility attribute " + attr.getName()
              + "for the " + elem.getNodeName() + " element is found.");
          }
        }
      }

      if (!elemNS.equals(WSIConstants.NS_URI_XSD))
      {
        checkElement(XMLUtils.getFirstChild(elem));
      }
      elem = XMLUtils.getNextSibling(elem);
    }
  }
}