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

package org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.eclipse.wst.wsdl.validation.internal.util.LazyURLInputStream;

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
    String namespace = resource.getNamespace();
    String url = null;
    if(systemId != null)
    {
      url = systemId;
    }
    else if(publicId != null)
    {
      url = publicId;
    }
    else if(namespace != null)
    {
      url = namespace;
    }
    if(url != null)
    {
      InputStream is = new LazyURLInputStream(url);
      return new XMLInputSource(publicId, resource.getExpandedSystemId(), resource.getExpandedSystemId(), is, null);
    }
    return null;
  }

}
