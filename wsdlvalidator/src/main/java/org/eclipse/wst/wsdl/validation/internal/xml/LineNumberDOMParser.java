/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.xml;

import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.Element;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.ibm.wsdl.extensions.schema.SchemaConstants;

/**
 * A DOM parser that will register location information with the elements in the model.
 */
public class LineNumberDOMParser extends DOMParser
{
  XMLLocator locator = null;

  /**
   * Constructor.
   */
  public LineNumberDOMParser()
  {
    super();
    try
    {
      setFeature(DEFER_NODE_EXPANSION, false);
    }
    catch (SAXNotSupportedException e)
    {
      System.out.println(e);
    }
    catch (SAXNotRecognizedException e)
    {
      System.out.println(e);
    }
  }

  /**
   * Constuctor.
   * 
   * @param arg0 The parser configuration.
   */
  public LineNumberDOMParser(XMLParserConfiguration arg0)
  {
    super(arg0);
    try
    {
      setFeature(DEFER_NODE_EXPANSION, false);
    }
    catch (SAXNotSupportedException e)
    {
      System.out.println(e);
    }
    catch (SAXNotRecognizedException e)
    {
      System.out.println(e);
    }
  }

  /**
   * Constructor.
   * 
   * @param arg0 A symbol table for the parser.
   */
  public LineNumberDOMParser(SymbolTable arg0)
  {
    super(arg0);
  }

  /**
   * Constructor.
   * 
   * @param arg0 A symbol table for the parser.
   * @param arg1 A grammar pool for the parser.
   */
  public LineNumberDOMParser(SymbolTable arg0, XMLGrammarPool arg1)
  {
    super(arg0, arg1);
  }

  /**
   * @see org.apache.xerces.xni.XMLDocumentHandler#startElement(org.apache.xerces.xni.QName, org.apache.xerces.xni.XMLAttributes, org.apache.xerces.xni.Augmentations)
   */
  public void startElement(QName arg0, XMLAttributes arg1, Augmentations arg2) throws XNIException
  {
    Element element;
    // For elements from the Schema namespace we want to preserve them
    // the way they were entered. Revert the values to the non-normalized values.
    String ns = arg0.uri;
    if(ns != null && (ns.equals(SchemaConstants.NS_URI_XSD_2001) || ns.equals(SchemaConstants.NS_URI_XSD_1999) || ns.equals(SchemaConstants.NS_URI_XSD_2000)))
    {
      int numatts = arg1.getLength();
      for(int i = 0; i < numatts; i++)
      {
        String nonNormalizedValue = arg1.getNonNormalizedValue(i);
        arg1.setValue(i, nonNormalizedValue);
      }
    }
    super.startElement(arg0, arg1, arg2);
    try
    {
      element = (Element)getProperty(CURRENT_ELEMENT_NODE);
      ElementImpl elementImpl = (ElementImpl)element;
      // Setting the user data with an identifier such as ElementLocation.KEY_NAME
      // may be a long term good idea. The setUserData method with no id is used 
      // to support JVMs with alternate versions of Xerces.
      elementImpl.setUserData(new ElementLocation(locator.getLineNumber(), locator.getColumnNumber()));
    }
    // catch SAXNotRecognizedException and SAXNotSupportedException if can't get element
    catch (ClassCastException e)
    {
      //System.out.println(e);
    }
    catch (SAXNotRecognizedException e)
    {
      //System.out.println(e);
    }
    catch (SAXNotSupportedException e)
    {
      //System.out.println(e);
    }
  }

  /**
   * @see org.apache.xerces.xni.XMLDocumentHandler#startDocument(org.apache.xerces.xni.XMLLocator, java.lang.String, org.apache.xerces.xni.NamespaceContext, org.apache.xerces.xni.Augmentations)
   */
  public void startDocument(XMLLocator arg0, String arg1, NamespaceContext arg2, Augmentations arg3)
    throws XNIException
  {
    locator = arg0;
    super.startDocument(arg0, arg1, arg2, arg3);
  }
}
