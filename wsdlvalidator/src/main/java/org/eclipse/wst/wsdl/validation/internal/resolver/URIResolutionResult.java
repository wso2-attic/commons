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
 * This class holds the URI resolution results.
 *
 */
public class URIResolutionResult implements IURIResolutionResult
{
  private String logicalLocation = null;
  private String physicalLocation = null;
  
  /**
   * Constructor.
   */
  public URIResolutionResult()
  {
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult#getLogicalLocation()
   */
  public String getLogicalLocation()
  {
	return logicalLocation;
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult#setLogicalLocation(java.lang.String)
   */
  public void setLogicalLocation(String logicalLocation)
  {
	this.logicalLocation = logicalLocation;
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult#getPhysicalLocation()
   */
  public String getPhysicalLocation()
  {
	return physicalLocation;
  }
  
  /**
   * @see org.eclipse.wst.wsdl.validation.internal.resolver.IURIResolutionResult#setPhysicalLocation(java.lang.String)
   */
  public void setPhysicalLocation(String physicalLocation)
  {
	this.physicalLocation = physicalLocation;
  }

}
