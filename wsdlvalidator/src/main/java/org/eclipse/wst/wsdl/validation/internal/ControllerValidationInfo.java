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


/**
 * An interface for the validation info that allows for starting and
 * completing WSDL and WS-I validation.
 */
public interface ControllerValidationInfo extends IValidationInfo
{
  
  /**
   * Perform the necessary steps to complete WSDL validation.
   */
  public void completeWSDLValidation();
  
//  /**
//   * Perform the necessary steps to complete WS-I WSDL validation.
//   */
//  public void completeWSIValidation();
  
}
