/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal;

import java.util.Hashtable;

/**
 * Configuration information for validation of a specific
 * file.
 */
public class WSDLValidationConfiguration 
{
  protected Hashtable properties = new Hashtable();
  
  /**
   * Set an attribute on the validator. An attribute is
   * defined by a name and a value pair. An attribute may
   * be defined for any validator, built in or an extension.
   * Extension validators can probe the attributes set on
   * the WSDL validator to customize the way in which they
   * validate. A null value will unset an attribute.
   * 
   * @param name The attribute identifier.
   * @param value The attribute itself.
   */
  public void setProperty(String name, Object value)
  {
	if(value == null)
	{
	  properties.remove(name);
	}
	else
	{
  	  properties.put(name, value);
	}
  }
  
  /**
   * Get the value set for a given property.
   * 
   * @return
   * 		The value for the specified property or null if the property has not been specified.
   */
  public Object getProperty(String name)
  {
	if(name != null)
	  return properties.get(name);
	return null;
  }

}
