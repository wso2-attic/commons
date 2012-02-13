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

/**
 * A WSDLValidatorDelegate holds a reference to a WSDL validator.
 * A delegate is used to lazy load extension validators.
 */
public abstract class WSDLValidatorDelegate 
{
  private IWSDLValidator validator = null;
  
  /**
   * Get the validator specified in this delegate.
   * 
   * @return The WSDL validator specified by this delegate.
   */
  public IWSDLValidator getValidator()
  {
    if (validator == null)
    {
      validator = loadValidator();
    }
    return validator;
  }
  
  /**
   * Load the validator specified in this delegate.
   * 
   * @return The WSDL validator specified by this delegate.
   */
  protected abstract IWSDLValidator loadValidator();
  
  /**
   * Return the name of the validator.
   * 
   * @return The validator name.
   */
  public abstract String getValidatorName();
}
