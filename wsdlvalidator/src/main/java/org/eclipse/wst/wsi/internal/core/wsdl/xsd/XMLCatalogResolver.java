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
package org.eclipse.wst.wsi.internal.core.wsdl.xsd;

import java.io.IOException;
import java.io.StringReader;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * A resolver to resolve entities from the XML catalog.
 * 
 * @author Lawrence Mandel (lmandel@ca.ibm.com)
 */
public class XMLCatalogResolver implements XMLEntityResolver
{
  protected static XMLCatalogResolver xmlCatalog;

  /**
   * Constructor.
   */
  protected XMLCatalogResolver()
  {
  }

  /**
   * Get the instance of this resolver.
   * 
   * @return the instance of this resolver
   */
  public static XMLCatalogResolver getInstance()
  {
    if (xmlCatalog == null)
    {
      xmlCatalog = new XMLCatalogResolver();
    }
    return xmlCatalog;
  }

  /**
   * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
   */
  public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws XNIException, IOException
  {
    String publicId = resourceIdentifier.getPublicId();
    String systemId = resourceIdentifier.getExpandedSystemId();
    String location = XMLCatalog.getInstance().resolveEntityLocation(publicId, systemId);
    if (location == null)
    {
      XMLInputSource inputSource = new XMLInputSource(publicId, systemId, systemId, new StringReader(location), null);
      return inputSource;
    }
    // otherwise return null to tell the parser to locate the systemId as a URI
    return null;
  }
}
