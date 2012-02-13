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

/**
 * An interface for an XML catalog.
 */
public interface IXMLCatalog
{

  /**
   * Add a public id and a location to the catalog.
   * 
   * @param publicId -
   *          the public id of the entry
   * @param systemId -
   *          the system id of the entry
   */
  public abstract void addEntryToCatalog(String publicId, String systemId);

  /**
   * Resolve the location of an entity given public and system ids.
   * 
   * @param publicId -
   *          the public id of the entity to be resolved
   * @param systemId -
   *          the system id of the entity to be resolved
   * @return the location of the entity
   */
  public abstract String resolveEntityLocation(String publicId, String systemId);
}
