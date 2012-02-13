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
package org.eclipse.wst.wsi.internal.core.xml.jaxp;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.impl.Constants;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.eclipse.wst.wsi.internal.core.xml.dom.DOMParser;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Using this class, we can obtain a Document from XML. This class is a 
 * specialization of javax.xml.parsers.DocumentBuilder.
 * 
 * An instance of this class can be obtained from the 
 * DocumentBuilderFactory.newDocumentBuilder method. Once an instance 
 * of this class is obtained, XML can be parsed from a variety of input 
 * sources. These input sources are InputStreams, Files, URLs, and SAX 
 * InputSources.

This class will javax.xml.parsers.DocumentBuilderFactory...
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class DocumentBuilderImpl extends DocumentBuilder
{
  protected DOMParser domParser = null;

  protected EntityResolver entityResolver = null;
  protected ErrorHandler errorHandler = new DefaultHandler();

  protected static final String NAMESPACES_FEATURE =
    Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE;
  protected static final String VALIDATION_FEATURE =
    Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE;

  /**
   * Constructor for DocumentBuilderImpl2.
   *
   * @param dbFactory  a DocumentBuilderFactory object.
   * @throws SAXException if any parse errors occur. 
   */
  public DocumentBuilderImpl(DocumentBuilderFactory dbFactory, Hashtable attributes)
    throws SAXException
  {
    // Create parser
    domParser = new DOMParser();

    // Set namespace aware
    domParser.setFeature(NAMESPACES_FEATURE, dbFactory.isNamespaceAware());

    // Set validating
    domParser.setFeature(VALIDATION_FEATURE, dbFactory.isValidating());
    if (dbFactory.isValidating()) domParser.setFeature(XMLUtils.FEATURE_VALIDATION_SCHEMA, true);

    // Do not defer node expansion
    domParser.setFeature(
      Constants.XERCES_FEATURE_PREFIX + Constants.DEFER_NODE_EXPANSION_FEATURE,
      false);

    // Set other features from the document factory builder
    domParser.setFeature(
      Constants.XERCES_FEATURE_PREFIX + Constants.INCLUDE_IGNORABLE_WHITESPACE,
      !dbFactory.isIgnoringElementContentWhitespace());
    domParser.setFeature(
      Constants.XERCES_FEATURE_PREFIX
        + Constants.CREATE_ENTITY_REF_NODES_FEATURE,
      !dbFactory.isExpandEntityReferences());
    domParser.setFeature(
      Constants.XERCES_FEATURE_PREFIX + Constants.INCLUDE_COMMENTS_FEATURE,
      !dbFactory.isIgnoringComments());
    domParser.setFeature(
      Constants.XERCES_FEATURE_PREFIX + Constants.CREATE_CDATA_NODES_FEATURE,
      !dbFactory.isCoalescing());
   
    // set features and properties specified at factory level
    if (attributes != null)
    {
      for (Enumeration i=attributes.keys(); i.hasMoreElements();)
      {
        String attribute = (String)i.nextElement();
        if (attribute.equals(XMLUtils.JAXP_SCHEMA_SOURCE))
        {
          // for multiple schema validation may be array of InputSource 
          if(attributes.get(attribute) instanceof InputSource[])
            domParser.setProperty(attribute, (InputSource[])attributes.get(attribute));
          else
            domParser.setProperty(attribute, (InputSource)attributes.get(attribute));
        }
        else
        {
          domParser.setProperty(attribute, attributes.get(attribute));
        }
      }
    }
  }

  /**
   * Parse the content of the given input source as an XML document and return a new DOM.
   * @param is  InputStream containing the content to be parsed. 
   * @throws SAXException if any parse errors occur. 
   * @throws IOException if any IO errors occur. 
   */
  public Document parse(InputSource is) throws SAXException, IOException
  {
	ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
	try
	{
	  Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());   
 	
      // Set entity resolver
      if (this.entityResolver != null)
        domParser.setEntityResolver(this.entityResolver);

      // Set error handler    
      if (this.errorHandler != null)
        domParser.setErrorHandler(this.errorHandler);

      // Parse input source
      domParser.parse(is);

      // Return the document that was created
      return domParser.getDocument();
	}
    finally
    { 
      Thread.currentThread().setContextClassLoader(currentLoader);
    }    
  }

  /**
   * Indicates whether or not this parser is configured to understand namespaces.
   * @return true if this parser is configured to understand namespaces; false otherwise.
   */
  public boolean isNamespaceAware()
  {
    boolean namespaceAware = false;

    try
    {
      namespaceAware = domParser.getFeature(NAMESPACES_FEATURE);
    }

    catch (SAXException se)
    {
      throw new RuntimeException(se.toString());
    }

    return namespaceAware;
  }

  /**
   * Indicates whether or not this parser is configured to validate XML documents.
   * @return true if this parser is configured to validate XML documents; false otherwise.
   */
  public boolean isValidating()
  {
    boolean validating = false;

    try
    {
      validating = domParser.getFeature(VALIDATION_FEATURE);
    }

    catch (SAXException se)
    {
      throw new RuntimeException(se.toString());
    }

    return validating;
  }

  /**
   * Specify the EntityResolver to be used to resolve entities present 
   * in the XML document to be parsed. Setting this to null will result 
   * in the underlying implementation using it's own default 
   * implementation and behavior.
   * 
   * @param er  the EntityResolver to be used to resolve entities 
   *            present in the XML document to be parsed.
   */
  public void setEntityResolver(EntityResolver entityResolver)
  {
    this.entityResolver = entityResolver;
  }

  /**
   * Specify the ErrorHandler to be used to report errors present in 
   * the XML document to be parsed. Setting this to null will result 
   * in the underlying implementation using it's own default 
   * implementation and behavior. 
   * 
   * param eh  the ErrorHandler to be used to report errors present in 
   *           the XML document to be parsed.
   */
  public void setErrorHandler(ErrorHandler errorHandler)
  {
    this.errorHandler = errorHandler;
  }

  /**
   * Obtain a new instance of a DOM Document object to build a DOM 
   * tree with. An alternative way to create a DOM Document object 
   * is to use the getDOMImplementation method to get a DOM Level 2 
   * DOMImplementation object and then use DOM Level 2 methods on 
   * that object to create a DOM Document object.
   * 
   * @return a new instance of a DOM Document object.
   */
  public Document newDocument()
  {
    return new DocumentImpl();
  }

  /**
   * Obtain an instance of a DOMImplementation object. 
   * @return a new instance of a DOMImplementation.
   */
  public DOMImplementation getDOMImplementation()
  {
    return new DOMImplementationImpl();
  }

}
