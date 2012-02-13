/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.xml;

import java.io.IOException;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.wst.wsdl.validation.internal.util.LazyURLInputStream;

/**
 * A resolver to resolver entities from the XML catalog.
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
    if(publicId == null)
    {
      publicId = resourceIdentifier.getNamespace();
    }
    String systemId = resourceIdentifier.getLiteralSystemId();
    String location = XMLCatalog.getInstance().resolveEntityLocation(publicId, systemId);
    if (location != null)
    {
      LazyURLInputStream is = new LazyURLInputStream(location);
      XMLInputSource inputSource = new XMLInputSource(publicId, systemId, systemId, is, null);
      return inputSource;
    }
    // otherwise return null to tell the parser to locate the systemId as a URI
    return null;
  }
}
