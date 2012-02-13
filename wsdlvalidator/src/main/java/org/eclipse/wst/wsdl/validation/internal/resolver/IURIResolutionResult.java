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
package org.eclipse.wst.wsdl.validation.internal.resolver;

/**
 * The representation of a URI resolution result. This interface is not meant to
 * be implemented by clients.
 *
 */
public interface IURIResolutionResult 
{
  /**
   * Get the logical location of the resolution result.
   * 
   * @return The logical location of the resolution result.
   */
  public String getLogicalLocation();
  
  /**
   * Set the logical location of the resolution result.
   * 
   * @param logicalLocation The logical location of the resolution result.
   */
  public void setLogicalLocation(String logicalLocation);
  
  /**
   * Get the physical location of the resolution result.
   * 
   * @return The physical location of the resolution result.
   */
  public String getPhysicalLocation();
  
  /**
   * Set the physical location of the resolution result.
   * 
   * @param physicalLocation The physical location of the resolution result.
   */
  public void setPhysicalLocation(String physicalLocation);
}
