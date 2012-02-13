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

package org.eclipse.wst.wsdl.validation.internal;

import java.util.HashMap;

/**
 * An interface for a WSDL validation report.
 */
public interface IValidationReport
{
  /**
   * Returns the URI for the file the report refers to.
   * 
   * @return The URI for the file the report refers to.
   */
  public String getFileURI();
  
  /**
   * Returns whether the file is valid according to the WSDL specification.
   * 
   * @return True if the file is WSDL valid, false otherwise.
   */
  public boolean isWSDLValid();

  /**
   * Returns an array of validation messages.
   * 
   * @return An array of validation messages.
   */
  public IValidationMessage[] getValidationMessages();
  
  /**
   * Returns true if there are errors, false otherwise.
   * 
   * @return True if there are errors, false otherwise.
   */
  public boolean hasErrors();
  
  public HashMap getNestedMessages();

}
