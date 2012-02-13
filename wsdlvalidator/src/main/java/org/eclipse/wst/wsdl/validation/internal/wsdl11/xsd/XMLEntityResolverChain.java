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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 * Handle a chain of entity resolvers.
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
    
    // TODO: This fix should be removed once this problem is fixed in Xerces.
    // Xerces currently (version 2.7) has a problem when the honour all schema locations
    // property is set and the same schema is imported by two different files and one
    // of the imports uses \ and another uses / in part of the location.
    if(xmlResourceIdentifier.getLiteralSystemId() != null)
    	xmlResourceIdentifier.setLiteralSystemId(xmlResourceIdentifier.getLiteralSystemId().replace('\\','/'));
    
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
