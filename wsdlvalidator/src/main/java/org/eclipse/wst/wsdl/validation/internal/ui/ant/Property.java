/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.ui.ant;

import org.apache.tools.ant.Task;

/**
 * This class represents a property set on the WSDL validator.
 */
public class Property extends Task 
{
  private String name = null;
  private String value = null;
  
  /**
   * Set the name of the property.
   * 
   * @param name
   * 		The name of the property.
   */
  public void setName(String name)
  {
    this.name = name;
  }
	  
  /**
   * Get the name of the property.
   * 
   * @return
   * 		The name of the property.
   */
  public String getName()
  {
    return name;
  }
	  
  /**
   * Set the value of the property.
   * 
   * @param value
   * 		The value of the property.
   */
  public void setValue(String value)
  {
    this.value = value;
  }
	  
  /**
   * Get the value of the property.
   * 
   * @return
   * 		The value of the property.
   */
  public String getValue()
  {
    return value;
  }
}
