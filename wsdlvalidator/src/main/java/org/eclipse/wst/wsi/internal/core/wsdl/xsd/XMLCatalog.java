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

/**
 * XMLCatalog
 * This class can be used to register, obtain and delete an instance of an 
 * XML catalog. Method definitions are provided for the catalog to set
 * a location in the catalog and resolve an entity from the catalog.
 * 
 * @author Lawrence Mandel (lmandel@ca.ibm.com)
 */
public abstract class XMLCatalog
{
  private static XMLCatalog instance = null;
  private static Class xmlcatalogclass = null;

  /**
   * Return an instance of the XML catalog. If no instance is registered,
   * returns a default instance.
   * 
   * @return the instance of the XML catalog
   */
  public static XMLCatalog getInstance()
  {
    if (instance == null)
    {
      if (xmlcatalogclass != null)
      {
        try
        {
          instance = (XMLCatalog)xmlcatalogclass.newInstance();
        }
        catch (Exception e)
        {
          instance = new XMLCatalogImpl();
        }
      }
      else
      {
        instance = new XMLCatalogImpl();
      }
    }
    return instance;
  }

  /**
   * Set the class of the XML catalog to be used.
   * 
   * @param xmlcatalog - the class of the XML catalog to be used
   */
  public static void setXMLCatalog(Class xmlcatalog)
  {
    xmlcatalogclass = xmlcatalog;
  }

  /**
   * Resets the instance of the XML catalog to null. Allows switching XML catalogs.
   */
  public static void reset()
  {
    instance = null;
    xmlcatalogclass = null;
  }

  /**
   * Add a public id and a location to the catalog.
   * 
   * @param publicId - the public id of the entry
   * @param systemId - the system id of the entry
   */
  public abstract void addEntryToCatalog(String publicId, String systemId);

  /**
   * Resolve the location of an entity given public and system ids.
   * 
   * @param publicId - the public id of the entity to be resolved
   * @param systemId - the system id of the entity to be resolved
   * @return the location of the entity
   */
  public abstract String resolveEntityLocation(String publicId, String systemId);
}