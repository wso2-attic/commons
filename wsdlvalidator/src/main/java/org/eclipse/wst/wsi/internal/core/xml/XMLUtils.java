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
package org.eclipse.wst.wsi.internal.core.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.util.NullUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.ibm.wsdl.util.StringUtils;

/**
 * Set of XML related utilities.
 * 
 * @version 1.0.1
 * @author Peter Brittenham
 */
public final class XMLUtils
{
	/**
	 * Some sax features that need to be set.
	 */
	public static final String FEATURE_NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
	public static final String FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";
	public static final String FEATURE_VALIDATION_SCHEMA = "http://apache.org/xml/features/validation/schema";
	public static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	public static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	public static final String W3C_SOAP_12_SCHEMA = "http://schemas.xmlsoap.org/soap/envelope/";
	public static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	public static final String PROPERTY_EXTERNAL_SCHEMA_LOCATION = "http://apache.org/xml/properties/schema/external-schemaLocation";

	public static final String SOAP_ELEM_ENVELOPE = "Envelope";
	public static final String SOAP_ELEM_HEADER = "Header";
	public static final String SOAP_ELEM_BODY = "Body";
	public static final String SOAP_ELEM_FAULT = "Fault";
	public static final String SOAP_ELEM_FAULT_CODE = "faultcode";
	public static final String SOAP_ELEM_FAULT_STRING = "faultstring";
	public static final String SOAP_ELEM_FAULT_DETAIL = "detail";
	public static final String SOAP_ELEM_FAULT_ACTOR = "faultactor";
	public static final String SOAP_ATTR_MUST_UNDERSTAND = "mustUnderstand";
	public static final String SOAP_ATTR_ACTOR = "actor";

	/**
	 * Get XMLReader.
	 * 
	 * @return the XMLReader value
	 * @throws WSIException if there are problems getting the XMLReader
	 *           implementation.
	 */
	public static XMLReader getXMLReader() throws WSIException {
		XMLReader xmlReader = null;

		try
		{
			xmlReader = (new org.apache.xerces.jaxp.SAXParserFactoryImpl())
					.newSAXParser().getXMLReader();

			// Set namespace aware
			xmlReader.setFeature(FEATURE_NAMESPACE_PREFIXES, true);
			xmlReader.setFeature(FEATURE_NAMESPACES, true);
		}

		catch (Exception e)
		{
			throw new WSIException("Could not get XMLReader implementation.", e);
		}

		return xmlReader;
	}

	/**
	 * Parse text string as an XML document and return the document element.
	 * 
	 * @param text XML document text.
	 * @param validate true if the document will be validate, false otherwise
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(String text, boolean validate)
			throws WSIException {
		return parseXML(new StringReader(text), validate);
	}

	/**
	 * Parse text string as an XML document and return the document element.
	 * 
	 * @param text XML document text.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(String text) throws WSIException {
		return parseXML(new StringReader(text));
	}

	/**
	 * Parse the XML document and return the document element.
	 * 
	 * @param uri the location of the XML document.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 * @throws IOException if an I/O exception of some sort has occurred.
	 */
	public static Document parseXMLDocument(String uri) 
	throws WSIException, IOException 
	{
		return parseXMLDocument(uri, null);
	}

	/**
	 * Parse the XML document and return the document element.
	 * 
	 * @param uri the location of the XML document.
	 * @param schema a String identifying related schema document.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 * @throws IOException if an I/O exception of some sort has occurred.
	 */
	public static Document parseXMLDocument(String uri, String schema)
	throws WSIException, IOException 
	{
	      URL url = StringUtils.getURL(null, uri);
	      InputStream inputStream = (InputStream)url.getContent();
	      InputSource inputSource = new InputSource(inputStream);
	      inputSource.setSystemId(url.toString());
	      
	      if (schema == null)
	      	return parseXML(inputSource, false);
	      else
            return parseXML(inputSource, schema);
	}

	/**
	 * Parses an XML document from a reader and returns the document object.
	 * 
	 * @param url a String locating the XML document.
	 * @param schema a String identifying related schema document.
	 * @param baseURI a base url to assist in locating the XML document.
	 * @return Document.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXMLDocumentURL(String url, String schema,
			String baseURI) throws WSIException {
		try
		{
		  parseXMLDocumentURL(createURL(url, baseURI), schema);
		}

		catch (Exception e)
		{
			throw new WSIException(e.getMessage(), e);
		}

		return parseXMLDocumentURL(url, schema);
	}

	/**
	 * Parses an XML document from a reader and returns the document object.
	 * 
	 * @param url a URL object identifying the XML document.
	 * @param schema a String identifying related schema document.
	 * @return Document.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXMLDocumentURL(URL url, String schema)
			throws WSIException {
		try
		{
	      InputStream inputStream = (InputStream)url.getContent();
	      InputSource inputSource = new InputSource(inputStream);
	      inputSource.setSystemId(url.toString());

			Document doc = null;

			if (schema == null)
				doc = parseXML(inputSource);
			else
				doc = parseXML(inputSource, schema);

			inputStream.close();
			return doc;
		}

		catch (WSIException e)
		{
			throw e;
		}

		catch (Throwable t)
		{
			throw new WSIException(t.getMessage());
		}
	}

	/**
	 * Parses an XML document from a reader and returns the document object.
	 * 
	 * @param urlString a String locating the XML document.
	 * @param baseURI a base url to assist in locating the XML document.
	 * @return Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXMLDocumentURL(String urlString, String baseURI)
			throws WSIException {
		Document document = null;

		try
		{
			URL url = createURL(urlString, baseURI);

			document = parseXMLDocumentURL(url);
		}

		catch (WSIException we)
		{
			throw we;
		}

		catch (Exception e)
		{
			throw new WSIException(e.getMessage(), e);
		}

		return document;
	}

	/**
	 * Parses an XML document from a reader and returns the document object.
	 * 
	 * @param url a URL object identifying the XML document.
	 * @return Document.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXMLDocumentURL(URL url) throws WSIException {
		Document document = null;

		try
		{
			Reader reader = new InputStreamReader(url.openStream());
			InputSource source = new InputSource(reader);
			source.setSystemId(url.toString());
			document = parseXML(source);

			reader.close();
		}

		catch (Exception e)
		{
			throw new WSIException(e.getMessage(), e);
		}

		return document;
	}

	private static DocumentBuilder builder = null;

	/**
	 * Parse an XML document from a reader and return the document object.
	 * 
	 * @param reader a Reader object.
	 * @param validate true if the document will be validate, false otherwise
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(InputSource source, boolean validate)
			throws WSIException {
		Document doc = null;
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
		try
		{
    	  Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());   

    	  if (builder == null)
    	  {
  		    // Get the document factory
		    DocumentBuilderFactory factory = new org.eclipse.wst.wsi.internal.core.xml.jaxp.DocumentBuilderFactoryImpl();

		    // Set namespace aware, but for now do not validate
		    factory.setNamespaceAware(true);
		    factory.setIgnoringElementContentWhitespace(true);

		    // ADD: This should be set to true when we have access to the schema
		    // document
		    factory.setValidating(false);

		    builder = factory.newDocumentBuilder();
    	  }
		  // Parse the document
		  doc = builder.parse(source);
		  // workaround for compatibility Xerces 2.2.1 with Xerces 2.6.2,
		  // Xerces 2.6.2 supported XML 1.1 but WSI-tool and Xerces 2.2.1
		  // supported only XML 1.0
		  if (doc instanceof org.apache.xerces.dom.DocumentImpl)
		  {
			if (((org.apache.xerces.dom.DocumentImpl) doc).getXmlVersion().equals(
						"1.1"))
			{
				throw new WSIException("Fatal Error: XML version &quot;1.1&quot; "
						+ "is not supported, only XML 1.0 is supported.");
			}
		  }
		}
		catch (Exception e)
		{
			throw new WSIException("Could not parse XML document.", e);
		}
	    finally
	    { 
	      Thread.currentThread().setContextClassLoader(currentLoader);
	    }    

		// Return document
		return doc;
	}

	/**
	 * Parse an XML document from a reader and return the document object.
	 * 
	 * @param reader a Reader object.
	 * @param validate true if the document will be validate, false otherwise
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(Reader reader, boolean validate)
			throws WSIException {
		Document doc = null;
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
		try
		{
		  Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());   
		
		  // Create input source
		  InputSource inputSource = new InputSource(reader);

		  if (builder == null)
		  {
		    // Get the document factory
		    DocumentBuilderFactory factory = new org.eclipse.wst.wsi.internal.core.xml.jaxp.DocumentBuilderFactoryImpl();

		    // Set namespace aware, but for now do not validate
		    factory.setNamespaceAware(true);
		    factory.setIgnoringElementContentWhitespace(true);

		    // ADD: This should be set to true when we have access to the schema
		    // document
		    factory.setValidating(false);

		    builder = factory.newDocumentBuilder();
		  }
		  // Parse the document
		  doc = builder.parse(inputSource);
	   	  // workaround for compatibility Xerces 2.2.1 with Xerces 2.6.2,
		  // Xerces 2.6.2 supported XML 1.1 but WSI-tool and Xerces 2.2.1
		  // supported only XML 1.0
		  if (doc instanceof org.apache.xerces.dom.DocumentImpl)
		  {
			if (((org.apache.xerces.dom.DocumentImpl) doc).getXmlVersion().equals(
						"1.1"))
			{
			  throw new WSIException("Fatal Error: XML version &quot;1.1&quot; "
							+ "is not supported, only XML 1.0 is supported.");
			}
	      }
		}
		catch (Exception e)
		{
			throw new WSIException("Could not parse XML document.", e);
		}
	    finally
	    { 
	      Thread.currentThread().setContextClassLoader(currentLoader);
	    }    

		// Return document
		return doc;
	}

	/**
	 * Parse an XML document from a input source and return the document object.
	 * 
	 * @param source a InputSource object.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(Reader reader) throws WSIException {
		return parseXML(reader, false);
	}

	/**
	 * Parse an XML document from a input source and return the document object.
	 * 
	 * @param source a InputSource object.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(InputSource source) throws WSIException {
		return parseXML(source, false);
	}

	/**
	 * Parse text string as an XML document and return the document element.
	 * 
	 * @param text XML document text.
	 * @param schema a String identifying related schema document.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(String text, String schema)
			throws WSIException {
		return parseXML(new StringReader(text), schema);
	}

	/**
	 * Parse text string as an XML document and return the document element.
	 * 
	 * @param text XML document text.
	 * @param schemas a collection of related schema documents.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(String text, Collection schemas)
			throws WSIException {
		return parseXML(new StringReader(text), schemas);
	}


	/**
	 * Parse the XML document and return the document element.
	 * 
	 * @param text XML document text.
	 * @param schemaString a StringReader object.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 * @throws IOException if an I/O exception of some sort has occurred.
	 */
	public static Document parseXML(String text, StringReader schemaString)
			throws WSIException, IOException {
		return parseXML(new StringReader(text), schemaString);
	}

	/**
	 * Parse the XML document and return the document element.
	 * 
	 * @param reader a Reader object.
	 * @param schema a String identifying related schema document.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(Reader reader, String schema)
			throws WSIException {
		Document doc = null;
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
		try
		{
    	  Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());   
		  // Create input source
		  InputSource inputSource = new InputSource(reader);

		  // Get the document factory
		  DocumentBuilderFactory factory = new org.eclipse.wst.wsi.internal.core.xml.jaxp.DocumentBuilderFactoryImpl();

		  // Set namespace aware, but for now do not validate
		  factory.setNamespaceAware(true);
		  factory.setIgnoringElementContentWhitespace(true);

		  try
		  {
		    factory.setValidating(false);
			factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
 		  }
   		  catch (IllegalArgumentException e)
		  {
			String errMessage = "Error: JAXP DocumentBuilderFactory attribute not recognized: "
					+ JAXP_SCHEMA_LANGUAGE
					+ "\n"
					+ "Check to see if parser conforms to JAXP 1.2 spec.";
			throw new WSIException(errMessage, e);
		  }
		  factory.setAttribute(JAXP_SCHEMA_SOURCE, new InputSource(schema));

		  // Parse the document
		  DocumentBuilder builder = factory.newDocumentBuilder();
		  builder.setErrorHandler(new ErrHandler());
		  doc = builder.parse(inputSource);

		  // workaround for compatibility Xerces 2.2.1 with Xerces 2.6.2,
		  // Xerces 2.6.2 supported XML 1.1 but WSI-tool and Xerces 2.2.1
		  // supported only XML 1.0
		  if (doc instanceof org.apache.xerces.dom.DocumentImpl)
		  {
			if (((org.apache.xerces.dom.DocumentImpl) doc).getXmlVersion().equals("1.1"))
			{
		 	  throw new WSIException("Fatal Error: XML version &quot;1.1&quot; "
							+ "is not supported, only XML 1.0 is supported.");
			}
		  }
	    }
		catch (Exception e)
		{
			throw new WSIException("Could not parse XML document.", e);
		}
	    finally
	    { 
	      Thread.currentThread().setContextClassLoader(currentLoader);
	    }    
		// Return document
    	return doc;
	}

	/**
	 * Parse the XML document and return the document element.
	 * 
	 * @param reader a Reader object.
	 * @param schema a String identifying related schema document.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(InputSource source, String schema)
			throws WSIException {
		Document doc = null;
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
		try
		{
    	  Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());   
 		  // Get the document factory
		  DocumentBuilderFactory factory = new org.eclipse.wst.wsi.internal.core.xml.jaxp.DocumentBuilderFactoryImpl();

		  // Set namespace aware, but for now do not validate
		  factory.setNamespaceAware(true);
		  factory.setIgnoringElementContentWhitespace(true);

		  factory.setValidating(false);
		  try
		  {
			factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		  }

		  catch (IllegalArgumentException e)
		  {
			String errMessage = "Error: JAXP DocumentBuilderFactory attribute not recognized: "
					+ JAXP_SCHEMA_LANGUAGE
					+ "\n"
					+ "Check to see if parser conforms to JAXP 1.2 spec.";
			throw new WSIException(errMessage, e);
		  }
		  factory.setAttribute(JAXP_SCHEMA_SOURCE, new InputSource(schema));

          // Parse the document
		  DocumentBuilder builder = factory.newDocumentBuilder();
		  builder.setErrorHandler(new ErrHandler());
		  doc = builder.parse(source);

		  // workaround for compatibility Xerces 2.2.1 with Xerces 2.6.2,
		  // Xerces 2.6.2 supported XML 1.1 but WSI-tool and Xerces 2.2.1
		  // supported only XML 1.0
		  if (doc instanceof org.apache.xerces.dom.DocumentImpl)
		  {
			if (((org.apache.xerces.dom.DocumentImpl) doc).getXmlVersion().equals(
						"1.1"))
			{
				throw new WSIException("Fatal Error: XML version &quot;1.1&quot; "
						+ "is not supported, only XML 1.0 is supported.");
			}
		  }
		}
		catch (Exception e)
		{
			throw new WSIException("Could not parse XML document.", e);
		}
	    finally
	    { 
	      Thread.currentThread().setContextClassLoader(currentLoader);
	    }    

		// Return document
		return doc;

	}

	/**
	 * Parse the XML document and return the document element.
	 * 
	 * @param filename a Reader object.
	 * @param schemaString a StringReader object.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(Reader filename, StringReader schemaString)
			throws WSIException {

		Document doc = null;
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
		try
		{
    	  Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());   

		  // Create input source
		  InputSource inputSource = new InputSource(filename);

		  // Get the document factory
		  DocumentBuilderFactory factory = new org.eclipse.wst.wsi.internal.core.xml.jaxp.DocumentBuilderFactoryImpl();

		  // Set namespace aware, but for now do not validate
		  factory.setNamespaceAware(true);
		  factory.setIgnoringElementContentWhitespace(true);

		  factory.setValidating(false);
		  try
		  {
			factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		  } catch (IllegalArgumentException e)
		  {
			String errMessage = "Error: JAXP DocumentBuilderFactory attribute not recognized: "
					+ JAXP_SCHEMA_LANGUAGE
					+ "\n"
					+ "Check to see if parser conforms to JAXP 1.2 spec.";
			throw new WSIException(errMessage, e);
		  }
		  factory.setAttribute(JAXP_SCHEMA_SOURCE, new InputSource(schemaString));

		  // Parse the document
		  DocumentBuilder builder = factory.newDocumentBuilder();
		  builder.setErrorHandler(new ErrHandler());
		  doc = builder.parse(inputSource);
		}
		catch (Exception e)
		{
			throw new WSIException("Could not parse XML document.", e);
		}
	    finally
	    { 
	      Thread.currentThread().setContextClassLoader(currentLoader);
	    }    

		// Return document
		return doc;

	}

	/**
	 * Parse the XML document and return the document element.
	 * 
	 * @param filename a Reader object
	 * @param schemaStrings a collection of related schema documents.
	 * @return a Document object.
	 * @throws WSIException if there is a problem parsing the XML document.
	 */
	public static Document parseXML(Reader filename, Collection schemaStrings)
			throws WSIException {

		Document doc = null;
		ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
		try
		{
    	  Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());   

		  // Create input source
		  InputSource inputSource = new InputSource(filename);

		  // Get the document factory
		  DocumentBuilderFactory factory = new org.eclipse.wst.wsi.internal.core.xml.jaxp.DocumentBuilderFactoryImpl();

		  // Set namespace aware, but for now do not validate
		  factory.setNamespaceAware(true);
		  factory.setIgnoringElementContentWhitespace(true);

		  factory.setValidating(false);
		  try
		  {
			factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		  } catch (IllegalArgumentException e)
		  {
		    String errMessage = "Error: JAXP DocumentBuilderFactory attribute not recognized: "
					+ JAXP_SCHEMA_LANGUAGE
					+ "\n"
					+ "Check to see if parser conforms to JAXP 1.2 spec.";
			throw new WSIException(errMessage, e);
		  }
		  // convert schema strings to array of InputSources
		  Iterator i = schemaStrings.iterator();
		  Vector readers = new Vector();
		  while (i.hasNext())
		  {
			String nextSchema = (String) i.next();
			readers.add(new InputSource(new StringReader(nextSchema)));
		  }
		  InputSource[] inputSources = (InputSource[]) readers
				.toArray(new InputSource[]{});
		  // pass an array of schema strings (each of which contains a schema)
		  factory.setAttribute(JAXP_SCHEMA_SOURCE, inputSources);

		  // Parse the document
		  DocumentBuilder builder = factory.newDocumentBuilder();
		  builder.setErrorHandler(new ErrHandler());
		  doc = builder.parse(inputSource);
		}
		catch (Exception e)
		{
			throw new WSIException("Could not parse XML document.", e);
		}
	    finally
	    { 
	      Thread.currentThread().setContextClassLoader(currentLoader);
	    }    

		// Return document
		return doc;

	}

	/**
	 * Get attribute value with the given name defined for the specified element.
	 * 
	 * @param element an Element object.
	 * @param attrName a name of an attribute
	 * @return the attribute value.
	 */
	public static String getAttributeValue(Element element, String attrName) {
		String attrValue = null;
		Attr attr = null;

		// Get the attribute using its name
		if ((attr = element.getAttributeNode(attrName)) != null)
		{
			attrValue = attr.getValue().trim();
		}

		// Return attribute value
		return attrValue;
	}

	/**
	 * Get attribute value.
	 * 
	 * @param element an Element object.
	 * @param attrName a name of an attribute
	 * @param defaultValue a default value for the specified attribute.
	 * @return the attribute value if found. Otherwise the specified default
	 *         value.
	 */
	public static String getAttributeValue(Element element, String attrName,
			String defaultValue) {
		String returnValue = defaultValue;
		String attrValue = null;

		if ((attrValue = getAttributeValue(element, attrName)) != null)
			returnValue = attrValue;

		return returnValue;
	}

	/**
	 * Get attribute value.
	 * 
	 * @param element an Element object.
	 * @param namespace a namespace.
	 * @param attrName a name of an attribute
	 * @return the attribute value.
	 */
	public static String getAttributeValueNS(Element element, String namespace,
			String attrName) {
		String attrValue = null;
		Attr attr = null;

		// Get the attribute using its name
		if ((attr = element.getAttributeNodeNS(namespace, attrName)) != null)
		{
			attrValue = attr.getValue().trim();
		}

		// Return attribute value
		return attrValue;
	}

	/**
	 * Get the first child element from the input elment.
	 * 
	 * @param element an Element object.
	 * @return the firstchild element.
	 */
	public static Element getFirstChild(Element element) {
		// Return the first child element
		return findNextSibling(element.getFirstChild());
	}

	/**
	 * Get the next sibling element.
	 * 
	 * @param element - an Element object.
	 * @return the next sibling element.
	 */
	public static Element getNextSibling(Element element) {
		// Return next sibling element
		return findNextSibling(element.getNextSibling());
	}

	/**
	 * Find the next sibling element.
	 * 
	 * @param startNode XML start node.
	 * @return the next sibling element.
	 */
	protected static Element findNextSibling(Node startNode) {
		Node node = null;
		Element returnElement = null;

		// Find the next sibling element
		for (node = startNode; node != null && returnElement == null; node = node
				.getNextSibling())
		{
			// If this node is an element node, then return it
			if (node.getNodeType() == Node.ELEMENT_NODE)
			{
				returnElement = (Element) node;
			}
		}

		// Return next sibling element
		return (Element) returnElement;
	}

	/**
	 * Find the previous sibling element.
	 * 
	 * @param startNode XML start node.
	 * @return the previous sibling element.
	 */
	public static Element findPreviousSibling(Node startNode) {
		if (startNode == null)
			return null;

		while (startNode != null)
		{
			startNode = startNode.getPreviousSibling();
			if (startNode == null)
				return null;
			if (startNode.getNodeType() == Node.ELEMENT_NODE)
				return (Element) startNode;
		}

		return null;
	}

	/**
	 * Get the text that is associated with this element.
	 * 
	 * @param element an Element object.
	 * @return the text that is associated with this element.
	 */
	public static String getText(Element element) {
		String text = null;

		// Get first child element
		Node node = element.getFirstChild();

		// NodeList nodeList = element.getChildNodes();

		// int length = nodeList.getLength();

		// Process while there are nodes and the text hasn't been found
		while ((node != null) && (text == null))
		{
			// If a text node or cdata section is found, then get text
			if ((node.getNodeType() == Node.TEXT_NODE)
					|| (node.getNodeType() == Node.CDATA_SECTION_NODE))
			{
				text = ((CharacterData) node).getData();
			}

			// Get next sibling
			node = node.getNextSibling();
		}

		if (text != null)
			text = text.trim();

		return text;
	}

	/**
	 * Determine if an element is represented by the QName.
	 * 
	 * @param qname a QName object.
	 * @param element an Element object.
	 * @return true if an element is represented by the QName.
	 */
	public static boolean equals(QName qname, Element element) {
		boolean equals = false;

		// If both the namespace URI and local name are the same, then they are
		// equal
		if ((qname.getNamespaceURI().equals(element.getNamespaceURI()))
				&& (qname.getLocalPart().equals(element.getLocalName())))
		{
			equals = true;
		}

		return equals;
	}

	/**
	 * XML encode a text string.
	 * 
	 * @param text - a String.
	 * @return an XML encoded text string.
	 */
	public static String xmlEscapedString(String text) 
	{
	  if (text == null) return text;
	  else
	  {
	    StringBuffer sb = new StringBuffer(text.length()*2);
	    int size = text.length();
	    for (int i=0; i<size; i++)
	    {
	      char c = text.charAt(i);
	      switch (c)
		  {
             case '<': 
               sb.append("&lt;");
               break;
             case '>': 
             	sb.append("&gt;");
                break;
             case '&': 
             	sb.append("&amp;");
                break;
             case '"': 
             	sb.append("&quot;");
                break;
             case '\'': 
             	sb.append("&apos;");
                break;
             case '\r': 
             	sb.append("&#xd;");
                break;
             default:
             	sb.append(c);
		  }
		}
	    return  sb.toString();
	  }
	}

	/**
	 * XML encode a text string.
	 * 
	 * @param text - a String.
	 * @return an XML encoded text string.
	 */
	public static String xmlRemoveEscapedString(String text) 
	{
	  if (text == null) return text;
	  else
	  {
        StringBuffer sb = new StringBuffer(text);
      
	    int i = sb.indexOf("&#xd;");
	    while(i != -1)
	    {
	      sb.replace(i, i+5, "\r");
	      i = sb.indexOf("&#xd;");
	    }
	    
	    i = sb.indexOf("&lt;");
	    while(i != -1)
	    {
	      sb.replace(i, i+4, "<");
	      i = sb.indexOf("&lt;");
	    }

	    i = sb.indexOf("&gt;");
	    while(i != -1)
	    {
	      sb.replace(i, i+4, ">");
	      i = sb.indexOf("&gt;");
	    }

	    i = sb.indexOf("&quot;");
	    while(i != -1)
	    {
	      sb.replace(i, i+6, "\"");
	      i = sb.indexOf("&quot;");
	    }

	    i = sb.indexOf("&apos;");
	    while(i != -1)
	    {
	      sb.replace(i, i+6, "\'");
	      i = sb.indexOf("&apos;");
	    }

	    i = sb.indexOf("&amp;");
	    while(i != -1)
	    {
	      sb.replace(i, i+5, "&");
	      i = sb.indexOf("&amp;");
	    }
 	    return  sb.toString();
	  }
	}
	/**
	 * Get the specified element from a parent element.
	 * 
	 * @param elementName the element tag to serach for.
	 * @param parentElement the parent element.
	 * @return an element given the name and the parent element.
	 */
	public static Element getElement(String elementName, Element parentElement) {
		Element returnElement = null;
		NodeList nl;

		// Get the list of elements
		if ((nl = parentElement.getElementsByTagName(elementName)) != null)
		{
			// Return first element found
			returnElement = (Element) nl.item(0);
		}

		// Return element
		return returnElement;
	}

	/**
	 * Determine if this element matches specified local name in the specified
	 * namespace.
	 * 
	 * @param element an Element object.
	 * @param namespaceURI a namespace.
	 * @param localName a local name.
	 * @return true if this element matches specified local name in the specified
	 *         namespace.
	 */
	public static boolean isElement(Element element, String namespaceURI,
			String localName) {
		boolean isElement = false;

		if (element != null)
		{
			// Check is
			if (element.getNamespaceURI().equals(namespaceURI)
					&& element.getLocalName().equals(localName))
			{
				isElement = true;
			}
		}

		return isElement;
	}

	/**
	 * Determine if this element matches specified local name in the specified
	 * namespace.
	 * 
	 * @param element an Element object.
	 * @param namespaceURIs a list of valid namespaces.
	 * @param localName a local name.
	 * @return true if this element matches specified local name in the specified
	 *         namespace.
	 */
	public static boolean isElement(Element element, List namespaceURIs,
			String localName) {
		boolean isElement = false;

		if (element != null)
		{
			// Check is
			if (namespaceURIs.contains(element.getNamespaceURI())
					&& element.getLocalName().equals(localName))
			{
				isElement = true;
			}
		}

		return isElement;
	}

	/**
	 * Get element text as a boolean.
	 * 
	 * @param element an Element object.
	 * @param defaultValue a boolean to be used as a default value.
	 * @return element text as a boolean value.
	 */
	public static boolean getBooleanValue(Element element, boolean defaultValue) {
		boolean returnValue = defaultValue;
		String booleanValue = null;

		// Get value as a string
		if ((booleanValue = XMLUtils.getText(element)) != null)
		{
			returnValue = Boolean.valueOf(booleanValue).booleanValue();
		}

		// Return boolean
		return returnValue;
	}

	/**
	 * Get attribute value as a boolean.
	 * 
	 * @param element an Element object.
	 * @param attrName a name of an attribute.
	 * @param defaultValue a boolean to be used as a default value.
	 * @return attribute value as a boolean.
	 */
	public static boolean getBooleanValue(Element element, String attrName,
			boolean defaultValue) {
		boolean returnValue = defaultValue;
		String booleanValue = null;

		// Get value as a string
		if ((booleanValue = XMLUtils.getAttributeValue(element, attrName)) != null)
		{
			returnValue = Boolean.valueOf(booleanValue).booleanValue();
		}

		// Return boolean
		return returnValue;
	}

	/**
	 * Create QName.
	 * 
	 * @param qnameString a qualified name.
	 * @return a QName object.
	 */
	public static QName createQName(String qnameString) {
		QName qname = null;

		// Locate local part
		int index = qnameString.lastIndexOf(":");

		// Create new QName
		if (index != -1)
		{
			qname = new QName(qnameString.substring(0, index), qnameString
					.substring(index + 1));
		}

		else
		{
			qname = new QName(qnameString);
		}

		return qname;
	}

	/**
	 * Error Handler
	 */
	private static class ErrHandler implements ErrorHandler
	{
		/**
		 * Warning
		 */
		public void warning(SAXParseException spe) throws SAXException {
			String message = "Warning: " + spe.getMessage();
			throw new SAXException(message);
		}

		/**
		 * Error
		 */
		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + spe.getMessage();
			throw new SAXException(message);
		}

		/**
		 * Fatal Error
		 */
		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + spe.getMessage();
			throw new SAXException(message);
		}
	}

	/**
	 * Determine if the string is a NMTOKEN data type.
	 * 
	 * @param text a string value.
	 * @return true if the string is a NMTOKEN data type.
	 */
	public static boolean isNmtoken(String text) {
		boolean nmtoken = true;

		// ADD: Need to find a utility function that does this or write it from
		// scratch

		return nmtoken;
	}

	/**
	 * The method searches the first direct descendant element with the given
	 * qname.
	 * 
	 * @param parent parent DOM element.
	 * @param elementName QName of the element to be searched.
	 * @return DOM element if the required element found, and null otherwise.
	 */
	static public Element findChildElement(Element parent, QName elementName) {
		if (parent == null)
			throw new IllegalArgumentException("Parent element can not be NULL");
		if (elementName == null)
			throw new IllegalArgumentException("Element name can not be NULL");
		if (elementName.getLocalPart() == null)
			throw new IllegalArgumentException(
					"Local part of the element name can not be NULL");

		Node n = parent.getFirstChild();
		String local = elementName.getLocalPart();
		String ns = elementName.getNamespaceURI();
		while (n != null)
		{
			if (Node.ELEMENT_NODE == n.getNodeType()
					&& local.equals(n.getLocalName())
					&& NullUtil.equals(ns, n.getNamespaceURI()))
				return (Element) n;
			n = n.getNextSibling();
		}

		return null;
	}

	/**
	 * The method searches the first sibling element with the given qname.
	 * 
	 * @param active DOM element.
	 * @param elementName QName of the element to be searched.
	 * @return DOM element if the required element found, and null otherwise.
	 */
	static public Element findElement(Element active, QName elementName) {
		if (active == null)
			throw new IllegalArgumentException("Active element can not be NULL");
		if (elementName == null)
			throw new IllegalArgumentException("Element name can not be NULL");
		if (elementName.getLocalPart() == null)
			throw new IllegalArgumentException(
					"Local part of the element name can not be NULL");

		Node n = active.getNextSibling();
		String local = elementName.getLocalPart();
		String ns = elementName.getNamespaceURI();
		while (n != null)
		{
			if (Node.ELEMENT_NODE == n.getNodeType()
					&& local.equals(n.getLocalName())
					&& NullUtil.equals(ns, n.getNamespaceURI()))
				return (Element) n;
			n = n.getNextSibling();
		}

		return null;
	}

	/**
	 * The method returns attribute node by the given qname.
	 * 
	 * @param el owner element.
	 * @param attributeName QName of the attribute node to be searched.
	 * @return attribute node by the given qname.
	 */
	static public Attr getAttribute(Element el, QName attributeName) {
		if (el == null)
			throw new IllegalArgumentException("Element can not be NULL");
		if (attributeName == null)
			throw new IllegalArgumentException("Attribute name can not be NULL");
		String nsURI = attributeName.getNamespaceURI();
		String localPart = attributeName.getLocalPart();
		if (localPart == null)
			throw new IllegalArgumentException(
					"Local part of the attribute name can not be NULL");

		Attr a = el.getAttributeNodeNS(nsURI,
				localPart);
		if (a == null)
			// try to get with null namespace
			a = el.getAttributeNodeNS(null, localPart);
		return a;
	}

	/**
	 * The method compares node's name to the given qname.
	 * 
	 * @param n a node.
	 * @param name a QName object.
	 * @return true if the node's name is the same as the given qname.
	 */
	static public boolean equals(Node n, QName name) {
		if (n == null || name == null)
			return false;
		return (NullUtil.equals(name.getLocalPart(), n.getLocalName()) && NullUtil
				.equals(name.getNamespaceURI(), n.getNamespaceURI()));
	}

	/**
	 * The method searches namespace URI for the given prefix. The searching
	 * mechanism is implemented according to the "XML Namespaces resolution"
	 * algorithm ('http://www.w3.org/TR/2003/WD-DOM-Level-3-
	 * Core-20030226/namespaces-algorithms.html').
	 * 
	 * @param n a node.
	 * @param prefix a prefix.
	 * @return the namespace URI for the given prefix.
	 */
	static public String findNamespaceURI(Node n, String prefix) {
		if (prefix == null)
			return null;

		while (n != null)
		{
			if (prefix.equals(n.getPrefix()))
				return n.getNamespaceURI();

			if (Node.ELEMENT_NODE == n.getNodeType())
			{
				NamedNodeMap m = n.getAttributes();
				if (m != null)
					for (int i = 0; i < m.getLength(); i++)
					{
						Node a = m.item(i);
						if (WSITag.NS_URI_XMLNS.equals(a.getNamespaceURI())
								&& prefix.equals(a.getLocalName()))
							return a.getNodeValue();
					}
			}

			n = n.getParentNode();
		}

		return null;
	}

	/**
	 * Serializes element.
	 * 
	 * @param n a DOM element.
	 * @return the serialized element.
	 */
	public static String serialize(Element n) {
		String value = null;
		try
		{
			StringWriter writer = new StringWriter();
			XMLSerializer s = new XMLSerializer(writer, new OutputFormat("xml",
					"UTF-8", true));
			s.serialize(n);
			value = writer.toString();
			writer.close();
		} catch (Throwable t)
		{
			// nothing
			value = "EXCEPTION : " + t.getMessage();
		}

		return value;
	}

	/**
	 * The method return list of child elements.
	 * 
	 * @param parent an org.w3c.dom.Element object.
	 * @return list of child elements.
	 */
	static public Vector getChildElements(Element parent) {
		if (parent == null)
			throw new IllegalArgumentException("Element can not be NULL");

		Vector vect = new Vector();
		Element elem = getFirstChild(parent);
		while (elem != null)
		{
			vect.add(elem);
			elem = getNextSibling(elem);
		}
		return vect;
	}

	/**
	 * Serializes document.
	 * 
	 * @param doc an org.w3c.dom.Document object.
	 * @param writer a java.io.Writer object.
	 * @throws Exception if unable to serialize the document.
	 */
	public static void serializeDoc(Document doc, Writer writer)
			throws java.lang.Exception {
		XMLSerializer s = new XMLSerializer(writer, new OutputFormat("xml",
				"UTF-8", true));
		s.serialize(doc);
	}

	/**
	 * Serealizes element.
	 * 
	 * @param elem an org.w3c.dom.Element object.
	 * @param writer a java.io.Writer object.
	 * @throws Exception if unable to serialize the DOM element.
	 */
	public static void serializeElement(Element elem, Writer writer)
			throws java.lang.Exception {
		XMLSerializer s = new XMLSerializer(writer, new OutputFormat("xml",
				"UTF-8", true));
		s.serialize(elem);
	}

	/**
	 * Remove all elements from list without namespace.
	 * 
	 * @param vect a list of elements.
	 */
	public static void removeAllElementsWithoutNS(Vector vect) {
		for (int i = 0; i < vect.size();)
		{
			if (((Element) vect.get(i)).getNamespaceURI() == null
					|| ((Element) vect.get(i)).getNamespaceURI().equals(""))
				vect.remove(i);
			else
				i++;
		}
	}

	/**
	 * Create URL using base URI.
	 * 
	 * @param url a URL string.
	 * @param baseURI a base url string to assist in creating a URL.
	 * @return newly created URL.
	 * @throws MalformedURLException if a malformed URL has occurred.
	 */
	public static URL createURL(String url, String baseURI)
			throws MalformedURLException {
		URL returnURL = null;
		URI uri = null;
		try
		{
            returnURL = new URL(url);
			uri = new URI(url);
			uri = uri.normalize();
			returnURL =  new URL(uri.toString());
		}

		catch (Exception mue)
		{
			int i = baseURI.lastIndexOf('/');
			int j = baseURI.lastIndexOf('\\');
			if (j > i)
				i = j;
			try
			{
			  uri = new URI(baseURI.substring(0, i + 1) + url);
			  uri = uri.normalize();
			  returnURL = uri.toURL();
			}
			catch (Exception e)
			{
			  return new URL(baseURI.substring(0, i + 1) + url);
			}
		}
		return returnURL;
	}

	/**
	 * Create URL using base URI.
	 * 
	 * @param url a URL string.
	 * @param baseURI a base url string to assist in creating a URL.
	 * @return newly created URL string.
	 * @throws MalformedURLException if a malformed URL has occurred.
	 */
	public static String createURLString(String url, String baseURI)
			throws MalformedURLException {
		return createURL(url, baseURI).toExternalForm();
	}

	/**
	 * This method is used only for unit testing.
	 * 
	 * @param args arguments for main.
	 */
	public static void main(String[] args) {
		try
		{
		  System.out.println("Filename: " + args[0] + ", schema: " + args[1]);
		  parseXMLDocument(args[0], args[1]);
		  System.out.println("Done.");
		}

		catch (Exception e)
		{
			System.err.println(e.toString());
		}

		System.exit(0);
	}
}
