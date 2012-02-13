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

import java.net.URL;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.w3c.dom.Document;

/**
 * This is an abstract class that takes advantage of cached XML documents.
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */

public abstract class XMLDocumentCacheUser
{
  /**
   * Get document from cache.
   * @see #setDocument
   *
   * @param key  a String. 
   * @return a Document object corresponding to the key.
   */
  protected Document getDocument(String key)
  {
    return (Document) XMLDocumentCache.instance().get(key);
  }

  /**
   * Put document with corresponding key in cache.
   * @see #getDocument
   *
   * @param key       a String used as a key to identify specified document.
   * @param document  a document.
   */
  protected void setDocument(String key, Object document)
  {
	  XMLDocumentCache.instance().put(key, document);
  }

  /**
   * Get document list.
   * @return a XMLDocumentCache object representing the document list value.
   */
  public XMLDocumentCache getDocumentList()
  {
    return XMLDocumentCache.instance();
  }

  /**
   * Parse XML document.
   * @param urlString  a String locating the XML document.
   * @param baseURI    a base url to assist in locating the XML document.
   * @return a Document object.
   * @throws WSIException if document cannot be parsed.
   */
  public Document parseXMLDocumentURL(String urlString, String baseURI)
    throws WSIException
  {
    return parseXMLDocumentURL(urlString, baseURI, null);
  }

  /**
   * Parse XML document and validate with a schema document.
   * @param urlString  a String locating the XML document.
   * @param baseURI    a base url to assist in locating the XML document.
   * @param schema     a String identifying related schema document.
   * @return a Document object.
   * @throws WSIException if document cannot be parsed.
   */
  public Document parseXMLDocumentURL(
    String urlString,
    String baseURI,
    String schema)
    throws WSIException
  {
    Document document = null;

    try
    {
      // Create URL reference to document
      URL url = XMLUtils.createURL(urlString, baseURI);
               
      // TODO: Add schema to the xml document cache, so that you can detect when it has already 
      //  been parsed using a specific schema definition 

      // If the document is not in the cache, then read and parse it
   	  if ((document = getDocument(urlString)) == null)
      {
        document = XMLUtils.parseXMLDocumentURL(url, schema);
        // Add document to cache
        setDocument(urlString, document);
      }
    }

    catch (WSIException e)
    {
      throw e;
    }

    catch (Exception e)
    {
      throw new WSIException(e.getMessage(), e);
    }

    return document;
  }
}
