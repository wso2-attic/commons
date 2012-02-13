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
 * A container to hold the entity information until an XML catalog is
 * to be initialized.
 */
public class XMLCatalogEntityHolder
{
  private String publicId;
  private String systemId;
  
  /**
   * Constructor.
   * 
   * @param publicId The public id of the entity.
   * @param systemId The system id of the entity.
   */
  public XMLCatalogEntityHolder(String publicId, String systemId)
  {
    this.publicId = publicId;
    this.systemId = systemId;
  }
  
  /**
   * Returns the public id of the entity.
   * @return The public id of the entity.
   */
  public String getPublicId()
  {
    return publicId;
  }
  
  /**
   * Returns the system id of the entity.
   * @return The system id of the entity.
   */
  public String getSystemId()
  {
    return systemId;
  }
}
