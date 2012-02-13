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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * Handle a chain of entity resolvers.
 * 
 * @author Lawrence Mandel (lmandel@ca.ibm.com)
 */
public class XMLEntityResolverChain implements XMLEntityResolver
{
  private List entityResolvers = new Vector();
  
  /**
   * @see org.apache.xerces.xni.parser.XMLEntityResolver#resolveEntity(org.apache.xerces.xni.XMLResourceIdentifier)
   */
  public XMLInputSource resolveEntity(XMLResourceIdentifier xmlResourceIdentifier) throws XNIException, IOException
  {
    XMLInputSource is = null;
    Iterator entityResolverIter = entityResolvers.iterator();
    while (entityResolverIter.hasNext())
    {
      XMLEntityResolver entityResolver = (XMLEntityResolver)entityResolverIter.next();
      try
      {
        is = entityResolver.resolveEntity(xmlResourceIdentifier);
      }
      catch (XNIException e)
      {
      }
      catch (IOException e)
      {
      }
      if (is != null)
      {
        break;
      }
    }
    // Throw and IOException if the file isn't found
    if (is == null)
    {
      throw new IOException("Unable to locate file");
    }
    return is;
  }

  /**
   * Add an entity resolver to this chain.
   * 
   * @param entityResolver The resolver to add to the chain.
   */
  public void addEntityResolver(XMLEntityResolver entityResolver)
  {
    entityResolvers.add(entityResolver);
  }
}
