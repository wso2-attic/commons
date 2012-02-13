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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * BP2102.
 * <context>For a candidate wsdl:types element, with XML schema import elements</context>
 * <assertionDescription>Each XML schema import statement (xsd:import) is only used to directly reference an XML schema definition, which has "schema" from XML namespace "http://www.w3.org/2001/XMLSchema" as root element.  It does not contain a reference to another document embedding the XML schema definition (e.g. WSDL).</assertionDescription>
 */
public class BP2102 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2102(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private boolean importFound = false;

  /* Validates the test assertion.
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_PASSED;

    // parse WSDL as XML
    try
    {
      Types t = (Types) entryContext.getEntry().getEntryDetail();
      List exts = t.getExtensibilityElements();

      Definition definition = null;
      if ((definition = validator.analyzerContext.getCandidateInfo().getDefinition(t))
        == null)
      {
        throw new WSIException("Could not find types definition in any WSDL document.");
      }

      if (exts != null)
      {
        Iterator it = exts.iterator();
        while (it.hasNext())
        {
          ExtensibilityElement el = (ExtensibilityElement) it.next();
          if (el instanceof Schema
            && el.getElementType().equals(ELEM_XSD_SCHEMA))
            testNode(((Schema) el).getElement(),
              definition.getDocumentBaseURI(), new ArrayList());

          if (result.equals(AssertionResult.RESULT_FAILED))
          {
            failureDetail =
              this.validator.createFailureDetail(failureDetailMessage, entryContext);
          }
        }
      }

      if (!importFound)
        result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    catch (Throwable tt)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
      failureDetail =
        this.validator.createFailureDetail(
          "WSDL document can not be processed",
          entryContext);
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /*
   * Check node is xsd import and it is 
   * only used to directly reference an XML schema definition, which has "schema" 
   * from XML namespace "http://www.w3.org/2001/XMLSchema" as root element.  
   * And it does not contain a reference to another document embedding the XML schema 
   * definition (e.g. WSDL).
  * @param n - Unknown extensibility element
  */
  private void testNode(Node n, String context, List processedSchemas)
  {
	if ((n != null) && (!processedSchemas.contains(n)))
	{
      if (XMLUtils.equals(n.getParentNode(), ELEM_XSD_SCHEMA))
         processedSchemas.add(n);    	

      while (n != null)
      {
        // searches for xsd:import element
        if (Node.ELEMENT_NODE == n.getNodeType())
        {
          if (XMLUtils.equals(n, ELEM_XSD_IMPORT))
          {
            importFound = true;

            Element im = (Element) n;
            // Getting the schemaLocation and the namespace attributes
            Attr schemaLocation =
              XMLUtils.getAttribute(im, ATTR_XSD_SCHEMALOCATION);
            Attr namespace = XMLUtils.getAttribute(im, ATTR_XSD_NAMESPACE);
            // If there is only the namespace attribute of import element
            if (schemaLocation == null && namespace != null)
            {
              // Getting all the inline schemas of the wsdl definition
              Map schemasMap = validator.wsdlDocument.getSchemas();
              // If an inline schema imported is defined
              if (schemasMap.keySet().contains(namespace.getValue()))
              {
                // If an inline schema imported is defined
                // (that means the schema is valid),
                // continue with the next element
                n = n.getNextSibling();
                continue;
              }

              // no schemaLocation so try the namespace 
              schemaLocation = namespace;
            }

            // try to parse imported XSD
            if (schemaLocation != null && schemaLocation.getValue() != null)
            {
              try
              {
                // if any error or root element is not XSD schema -> error
                // !! ATTENTION
                // root XSD SCHEMA SCHEMA is not valid                            
                //Document schema = XMLUtils.parseXMLDocumentURL(schemaLocation.getValue(), XSD_SCHEMALOCATION, context);
                Document schema =
                  validator.parseXMLDocumentURL(schemaLocation.getValue(), context);

                // If the import is valid, then check its contents
                if (XMLUtils
                  .equals(schema.getDocumentElement(), ELEM_XSD_SCHEMA))
                {
                  // Check content of imported document
                  testNode(schema.getDocumentElement().getFirstChild(),
                    XMLUtils.createURLString(schemaLocation.getValue(), context), processedSchemas);
                }

                else
                {
                  throw new Exception();
                }
              }
              catch (Throwable t)
              {
                result = AssertionResult.RESULT_FAILED;
                failureDetailMessage = schemaLocation.getValue();
                break;
              }
            }
            else
            {
              //result = AssertionResult.RESULT_FAILED;
              result = AssertionResult.RESULT_NOT_APPLICABLE;
              failureDetailMessage =
                "schemaLocation == null and namespace == null";
              break;
            }
          }
          testNode(n.getFirstChild(), context, processedSchemas);
        }
        n = n.getNextSibling();
      }
    }
  }
}