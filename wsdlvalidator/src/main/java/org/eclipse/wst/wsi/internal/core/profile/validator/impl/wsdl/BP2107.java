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

import java.util.Iterator;
import java.util.List;

import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * BP2107.
 * <context>For a candidate wsdl:types element containing an xsd:schema element</context>
 * <assertionDescription>The xsd:schema element contains a targetNamespace attribute with a valid and non-null value unless the xsd:schema element has xsd:import and/or xsd:annotation as its only child element(s).</assertionDescription>
 */
public class BP2107 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2107(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private boolean schemaFound = false;
  private ErrorList errors = new ErrorList();
  //private String context;

  /* 
   * Validates the test assertion.
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_FAILED;

    Types t = (Types) entryContext.getEntry().getEntryDetail();
    List exts = t.getExtensibilityElements();
    if (exts != null)
    {
      //context = entryContext.getWSDLDocument().getDefinitions().getDocumentBaseURI();
      Iterator it = exts.iterator();
      while (it.hasNext())
      {
        ExtensibilityElement el = (ExtensibilityElement) it.next();
        if (el instanceof Schema)
          searchForSchema(((Schema) el).getElement());
      }
    }

    //			  context = entryContext.getWSDLDocument().getDefinitions().getDocumentBaseURI();
    //			  processWSDL(entryContext.getWSDLDocument().getFilename());

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
    }

    else if (!schemaFound)
      result = AssertionResult.RESULT_NOT_APPLICABLE;

    else
      result = AssertionResult.RESULT_PASSED;

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /*
   * Check node schema or load schema from inmport if it exists and process it. 
   * @param n - node
  */
  private void searchForSchema(Node n)
  {
    while (n != null)
    {
      // searches for xsd:import element
      if (Node.ELEMENT_NODE == n.getNodeType())
      {
        // if xsd:schema element is found -> process schema
        if (XMLUtils.equals(n, ELEM_XSD_SCHEMA))
        {
          schemaFound = true;
          processSchema(n, null);
        }

        else
        {
          // if xsd:import element is found -> load schema and process schema
          //if (XMLUtils.equals(n, ELEM_XSD_IMPORT))
          //  loadSchema(n);
          //else
          // else iterate element recursively
          searchForSchema(n.getFirstChild());
        }
      }

      n = n.getNextSibling();
    }
  }

  /*
   * Load schema and process it.
   * @param importNode - xsd:import element
  */
  //private void loadSchema(Node importNode)
  //{
  //  Element im = (Element) importNode;
  //  Attr schemaLocation = XMLUtils.getAttribute(im, ATTR_XSD_SCHEMALOCATION);
  //  // try to parse imported XSD
  //  if (schemaLocation != null && schemaLocation.getValue() != null)
  //    try
  //    {
  //      // if any error or root element is not XSD schema -> error
  //      Document schema =
  //        validator.parseXMLDocumentURL(schemaLocation.getValue(), context);
  //      if (XMLUtils.equals(schema.getDocumentElement(), ELEM_XSD_SCHEMA))
  //      {
  //        Attr a = XMLUtils.getAttribute(im, ATTR_XSD_NAMESPACE);
  //        String namespace = (a != null) ? a.getValue() : "";
  //        processSchema(schema.getDocumentElement(), namespace);
  //      }
  //    }
  //    catch (Throwable t)
  //    {
  //      // nothing. it's not a schema
  //    }
  //}

  /*
   * Create falure report if it's not correspons assertion description.
   * @param schema - xsd:schema
   * @param namespace - namespace of schema
  */
  private void processSchema(Node schema, String namespace)
  {
    Attr a =
      XMLUtils.getAttribute((Element) schema, ATTR_XSD_TARGETNAMESPACE);
    String targetNamespace = (a != null) ? a.getValue() : null;

    Node n = schema.getFirstChild();
    //	   !! we suppose that xsd:import element is occured only within xsd:schema element
    boolean containsOnlyImportAndAnnotation = true;
    while (n != null)
    {
      if (n.getNodeType() == Node.ELEMENT_NODE)
      {
        containsOnlyImportAndAnnotation
          &= (XMLUtils.equals(n, ELEM_XSD_IMPORT)
            || XMLUtils.equals(n, ELEM_XSD_ANNOTATION));
      }

      //if (Node.ELEMENT_NODE == n.getNodeType() && XMLUtils.equals(n, ELEM_XSD_IMPORT))
      //	loadSchema(n);

      n = n.getNextSibling();
    }

    // If the target namespace is not set and there are elements in addition to import and annotation, then error
    if ((targetNamespace == null || targetNamespace.length() == 0)
      && (!containsOnlyImportAndAnnotation))
    {
      errors.add(targetNamespace, XMLUtils.serialize((Element) schema));
    }

    if (namespace != null && !namespace.equals(targetNamespace))
    {
      errors.add(namespace, XMLUtils.serialize((Element) schema));
    }
  }
}