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
package org.eclipse.wst.wsi.internal.core.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * This class creates a HashMap to cache XML documents.
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class XMLDocumentCache extends HashMap
{
	static XMLDocumentCache instance;

  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3834028043709657401L;

	/**
   * Constructor for XMLDocumentCache.
   * @param initialCapacity  the initial capacity.
   * @param loadFactor       the load factor. 
   */
  public XMLDocumentCache(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
    instance = this;
  }

  /**
   * Constructor for XMLDocumentCache.
   * @param initialCapacity  the initial capacity.
   */
  public XMLDocumentCache(int initialCapacity)
  {
    super(initialCapacity);
    instance = this;
  }

  /**
   * Constructor for XMLDocumentCache.
   */
  public XMLDocumentCache()
  {
    super();
    instance = this;
  }

  /**
   * Constructor for XMLDocumentCache.
   * @param t  the map whose mappings are to be placed in this map.
   */
  public XMLDocumentCache(Map t)
  {
    super(t);
    instance = this;
  }

  public static XMLDocumentCache instance()
  {
	  if (instance == null) 
		  instance = new XMLDocumentCache();
	  
	  return instance;
  }
}
