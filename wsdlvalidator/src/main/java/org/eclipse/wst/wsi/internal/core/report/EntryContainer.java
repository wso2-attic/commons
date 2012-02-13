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
package org.eclipse.wst.wsi.internal.core.report;

/**
 * This class represents a container which has one or more entries.
 * For example, a WSDL document contains one or more elements.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public interface EntryContainer extends EntryResult
{
  /**
   * Get container id.
   * @return container id.
   * @see #setId
   */
  public String getId();

  /**
   * Set container id.
   * @param id container id.
   * @see #getId
   */
  public void setId(String id);
}
