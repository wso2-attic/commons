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

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.wst.wsi.internal.core.util.ErrorList;
import org.eclipse.wst.wsi.internal.core.util.StringTokenizer;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * BP2011.
 * The imported XML schema is a well-formed XML 1.0 document and if it contains an 
 * XML declaration, it is version 1.0. 
 *
 * @version 1.0.1 27.06.2003
 * @author Vitali Fedosenko
**/
public class BP2011 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2011(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  private ErrorList errors = new ErrorList();

  private final char[] OMMITED_XML_DECLARATION_DELIMITERS =
    new char[] { 0x20, 0x9, 0xD, 0xA, '\'', '\"' };
  private final char[] XML_DECLARATION_DELIMITERS = new char[] { '=' };
  private final String VERSION_TOKEN = "version";
  private final String VERSION = "1.0";

  /* Validates the test assertion.
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_NOT_APPLICABLE;

    //Definition def = (Definition) entryContext.getEntry().getEntryDetail();
    Types types = (Types) entryContext.getEntry().getEntryDetail();

    List exts = null;
    //if (def.getTypes()!=null)
    if (types != null)
    {
      exts = types.getExtensibilityElements();
    }
    if (exts != null)
    {
      Definition definition = null;
      if ((definition =
        validator.analyzerContext.getCandidateInfo().getDefinition(types))
        == null)
      {
        throw new WSIException("Could not find types definition in any WSDL document.");
      }

      Iterator it = exts.iterator();
      while (it.hasNext())
      {
        ExtensibilityElement el = (ExtensibilityElement) it.next();
        if (el instanceof Schema)
          searchForSchema(((Schema) el).getElement(),
              definition.getDocumentBaseURI(), new ArrayList());
      }
    }

    if (!errors.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errors.toString(), entryContext);
    } /*else
         			result = AssertionResult.RESULT_PASSED;*/

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /* Search xsd schema or xsd import from node. If node is xsd import it's loading schema.
   * @param n - UnknownExtencibilityElement
  */
  private void searchForSchema(Node n, String context, List processedSchemas)
  {
    if ((n!= null) && (!processedSchemas.contains(n)))
    {
      while (n != null)
      {
        // searches for xsd:import element
        if (Node.ELEMENT_NODE == n.getNodeType())
        {
          // if xsd:schema element is found -> process schema
          if (XMLUtils.equals(n, ELEM_XSD_SCHEMA))
            processSchema(n, context, processedSchemas);
          else
            // if xsd:import element is found -> load schema and process schema
            // FIXED: if xsd:import is found and parent element is xsd:schema
            if (XMLUtils.equals(n, ELEM_XSD_IMPORT)
              && XMLUtils.equals(n.getParentNode(), ELEM_XSD_SCHEMA))
              loadSchema(n, context, processedSchemas);
            else
              // else iterate element recursively
              searchForSchema(n.getFirstChild(), context, processedSchemas);
        }
        n = n.getNextSibling();
      }
    }
  }

  /*
   * It loads xsd schema and then check the version 1.0 and looking for xsd:schema element for next process.
   * @param importNode xsd schema
  */
  private void loadSchema(Node importNode, String context, List processedSchemas)
  {
    Element im = (Element) importNode;
    Attr schemaLocation = XMLUtils.getAttribute(im, ATTR_XSD_SCHEMALOCATION);
    // try to parse imported XSD
    if (schemaLocation != null && schemaLocation.getValue() != null)
    {
      try
      {
        // if any error or root element is not XSD schema -> error
        String decl =
          readXMLDeclarationStatement(schemaLocation.getValue(), context);
        if (!validVersion(decl))
        {
          Attr a = XMLUtils.getAttribute(im, ATTR_XSD_NAMESPACE);
          errors.add((a != null) ? a.getValue() : "");
        }
        Document schema =
          validator.parseXMLDocumentURL(schemaLocation.getValue(), context);
        if (XMLUtils.equals(schema.getDocumentElement(), ELEM_XSD_SCHEMA))
        {
          processSchema(schema.getDocumentElement(),
            XMLUtils.createURLString(schemaLocation.getValue(), context), processedSchemas);
        }
        result = AssertionResult.RESULT_PASSED;
      }
      catch (Throwable t)
      {
      }
    }
    // else if there is only the namespace attribute, the import relates to inline schema
    else if (XMLUtils.getAttribute(im, ATTR_XSD_NAMESPACE) != null)
    {
      result = AssertionResult.RESULT_PASSED;
    }
  }

  /**
  * Reads an XML declaration statement.
  * @param location
  * @return String
  */
  private String readXMLDeclarationStatement(String location, String baseURI)
  {
    String result = null;
    try
    {
      new URL(location);
    }
    catch (Throwable t)
    {
      // nothing
      int i = baseURI.lastIndexOf('/');
      int j = baseURI.lastIndexOf('\\');
      if (j > i)
        i = j;
      location = baseURI.substring(0, i + 1) + location;
    }

    if (location != null)
    {
      URL url = null;
      Reader reader = null;

      try
      {
        try
        {
          url = new URL(location);
        }
        catch (MalformedURLException e)
        {
          // we should try to access location as file
        }

        if (url != null)
        {
          reader = new InputStreamReader(url.openStream());
        }
        else
        {
          reader = new InputStreamReader(new FileInputStream(location));
        }

        int charCode;
        boolean end = false;
        if (reader.ready())
        {
          charCode = reader.read();

          while (reader.ready() && !(charCode == '<'))
          {
            charCode = reader.read();
          }

          StringBuffer buf = new StringBuffer();
          if (charCode == '<')
          {
            buf.append((char) charCode);
            while (reader.ready() && !end)
            {
              charCode = reader.read();
              buf.append((char) charCode);

              end = charCode == '>';
            }
          }
          else
          {
            // NOTE: This result does not get propogated back!
            result = AssertionResult.RESULT_FAILED;
            failureDetailMessage =
              "Cannot read the XML declaration statement.";
          }

          result = buf.toString();
        }
      }
      catch (Exception e)
      {
      }
      finally
      {
        if (reader != null)
        {
          try
          {
            reader.close();
          }
          catch (Throwable e)
          {
          }
        }
      }
    }

    return result;
  }

  /*
   * @param xmlDecl - xml declaration
   * @return if xml declaration contains version="1.0" it retirns true. 
  */
  private boolean validVersion(String xmlDecl)
  {
    //boolean result = false;
    boolean result = true;
    if (xmlDecl != null)
    {
      StringTokenizer st =
        new StringTokenizer(
          OMMITED_XML_DECLARATION_DELIMITERS,
          XML_DECLARATION_DELIMITERS);
      Enumeration tokens = st.parse(xmlDecl);
      boolean found = false;
      while (tokens.hasMoreElements() && !found)
      {
        String token = (String) tokens.nextElement();

        if (token.equalsIgnoreCase(VERSION_TOKEN))
        {
          found = true;

          tokens.nextElement();
          String ver = (String) tokens.nextElement();

          result = VERSION.equals(ver);
        }
      }
    }

    return result;
  }

  /*
   * It's loking for xsd import and load it if find.
   * @param schema xsd schema
  */
  private void processSchema(Node schema, String context, List processedSchemas)
  {
	if ((schema != null) && (!processedSchemas.contains(schema)))
	{
	  processedSchemas.add(schema);
      Node n = schema.getFirstChild();
      while (n != null)
      {
        if (Node.ELEMENT_NODE == n.getNodeType()
          && XMLUtils.equals(n, ELEM_XSD_IMPORT))
          loadSchema(n, context, processedSchemas);

        n = n.getNextSibling();
      }
	}
  }
}