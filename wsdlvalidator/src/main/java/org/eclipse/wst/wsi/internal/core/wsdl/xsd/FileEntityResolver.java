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
import java.io.InputStream;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * Entity resolve to resolve file entities.
 */
public class FileEntityResolver implements XMLEntityResolver
{

  /**
   * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
   */
  public XMLInputSource resolveEntity(XMLResourceIdentifier resource) throws XNIException, IOException
  {
    String publicId = resource.getPublicId();
    String systemId = resource.getExpandedSystemId();
    String url = systemId;
    if(url == null)
    {
      url = publicId;
    }
    if(url == null)
    {
      url = resource.getNamespace();
    }
    if(url != null)
    {
      InputStream is = new LazyURLInputStream(url);
      return new XMLInputSource(publicId, systemId, systemId, is, null);
    }
    return null;
  }

}
