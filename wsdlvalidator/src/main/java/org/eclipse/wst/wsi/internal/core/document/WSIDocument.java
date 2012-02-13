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
package org.eclipse.wst.wsi.internal.core.document;

/**
 * Defines the interface used for all Conformance XML documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface WSIDocument extends DocumentElement
{
  /**
   * Get the location of the document.
   * @return the location of the document.
   * @see #setLocation
   */
  public String getLocation();

  /**
   * Set the location of the document.
   * @param documentURI the location of the document.
   * @see #getLocation
   */
  public void setLocation(String documentURI);
}
