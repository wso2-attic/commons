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
import javax.wsdl.Import;
import javax.xml.namespace.QName;

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
import org.w3c.dom.Node;


/**
  * BP2105.
  * <context>For a candidate wsdl:definitions</context>
  * <assertionDescription>For the referenced definitions, as well as all imported descriptions, in the WSDL namespace under the wsdl:defintitions element, the wsdl:import element is either directly after the wsdl:documentation element or is the first child element if the wsdl:documentation element is not present.</assertionDescription>
  */
public class BP2105 extends AssertionProcessVisitor implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2105(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  private boolean importFound = false;

  /* 
   * Check wsld import element is either directly after the wsdl:documentation element 
   * or is the first child element if the wsdl:documentation element is not present.
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Import, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Import im, Object parent, WSDLTraversalContext ctx)
  {
    importFound = true;
    if (im.getDefinition() != null && im.getLocationURI() != null)
      processWSDL(im.getDefinition().getDocumentBaseURI());
  }

  /*
   * Check element is extensibility.
   * @param el - xml element
   * @return boolean
  */
  private boolean isExtensibilityElement(Element el)
  {
    boolean isEx = true;
    isEx = isEx && !XMLUtils.equals(el, WSDL_BINDING);
    isEx = isEx && !XMLUtils.equals(el, WSDL_DEFINITIONS);
    isEx = isEx && !XMLUtils.equals(el, WSDL_DOCUMENTATION);
    isEx = isEx && !XMLUtils.equals(el, WSDL_FAULT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_IMPORT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_INPUT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_MESSAGE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_OPERATION);
    isEx = isEx && !XMLUtils.equals(el, WSDL_OUTPUT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PART);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PORT);
    isEx = isEx && !XMLUtils.equals(el, WSDL_PORTTYPE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_SERVICE);
    isEx = isEx && !XMLUtils.equals(el, WSDL_TYPES);

    return isEx;
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

    // Get the definition element that will be analyzed       
    Definition definition =
      (Definition) entryContext.getEntry().getEntryDetail();

    // If it doesn't contain any imports, then the result is notApplicable
    if ((definition.getImports() == null)
      || (definition.getImports().size() == 0))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Else analyze the 
    else
    {
      processWSDL(definition.getDocumentBaseURI());

      WSDLTraversal traversal = new WSDLTraversal();
      //VisitorAdaptor.adapt(this);
      traversal.setVisitor(this);
      traversal.visitImport(true);

      traversal.ignoreReferences();
      traversal.ignoreImport();
      traversal.traverse(
        (Definition) entryContext.getEntry().getEntryDetail());

      if (!errors.isEmpty())
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
      }

      else if (!importFound)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }

      else
      {
        result = AssertionResult.RESULT_PASSED;
      }
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /*
   * the method loads WSDL and check structure
   * @param location
   */
  private void processWSDL(String location)
  {
    try
    {
      // parses WSDL
      Document doc = validator.parseXMLDocumentURL(location, null);
      checkWSDL(doc.getDocumentElement()); // get definition
    }
    catch (Throwable t)
    {
      // ADD: add exception handling
      //System.err.println("2105 - WSDL load failed");
      errors.add(t.toString());
    }
  }

  /*
   * Create falure report contains information of node.
   * @param root
   */
  private void createFailed(Node root)
  {
    Attr a = XMLUtils.getAttribute((Element) root, ATTR_WSDL_LOCATION);
    String schemaLocation = (a != null) ? a.getValue() : "";
    a = XMLUtils.getAttribute((Element) root, ATTR_WSDL_NAMESPACE);
    String namespace = (a != null) ? a.getValue() : "";
    errors.add(new QName(namespace, schemaLocation));
  }

  /*
   * Check wsdl correspons assertion description.
  * @param root - definition
   */
  private void checkWSDL(Node root)
  {
    if (root != null)
    {
      // Find the first import element
      Element im = XMLUtils.findChildElement((Element) root, WSDL_IMPORT);

      // Determine if a documentation element is present in the WSDL document
      boolean documentIsPresent =
        (XMLUtils.findChildElement((Element) root, WSDL_DOCUMENTATION)
          != null);

      // Verify that all import elements occur after a documentation element
      while (im != null)
      {
        // Find elements that are previous to the import element
        Element el = XMLUtils.findPreviousSibling(im);
        if (el != null)
          while (isExtensibilityElement(el) && el != null)
            if (el != null)
              el = XMLUtils.findPreviousSibling(el);

        // If the wsdl:document elemnt is present
        if (documentIsPresent && el != null)
        {
          if (!XMLUtils.equals(el, WSDL_DOCUMENTATION) && !XMLUtils.equals(el, WSDL_IMPORT))
            createFailed(root);
        }

        // Else check if it is another wsdl: element
        else if (el != null)
        {
          // If not the definitions element, then check for other wsdl: element
          if (!XMLUtils.equals(el, WSDL_DEFINITIONS) && !XMLUtils.equals(el, WSDL_IMPORT))
          {
            if (el.getNamespaceURI().equals(WSIConstants.NS_URI_WSDL))
              createFailed(root);
          }
        }

        // Get the next import element
        im = XMLUtils.findElement(im, WSDL_IMPORT);
      }
    }
  }
}