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

import java.util.Hashtable;
import java.util.Map;

/**
 * The default implementation of the XML Catalog.
 * 
 * @author Lawrence Mandel (lmandel@ca.ibm.com)
 */
public class XMLCatalogImpl extends XMLCatalog
{
  protected Map catalog = new Hashtable();

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.xmlconformance.XMLCatalog#addEntryToCatalog(java.lang.String, java.lang.String)
   */
  public void addEntryToCatalog(String publicId, String systemId)
  {
    catalog.put(publicId, systemId);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wsdl.validate.xmlconformance.XMLCatalog#resolveEntityLocation(java.lang.String, java.lang.String)
   */
  public String resolveEntityLocation(String publicId, String systemId)
  {
  	// if there's no system id use the public id
    if (systemId == null || systemId.equals(""))
    {
      systemId = publicId;
    }
    return (String)catalog.get(systemId);
  }
}
