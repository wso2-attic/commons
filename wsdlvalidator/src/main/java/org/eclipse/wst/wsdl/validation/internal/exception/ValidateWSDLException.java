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

package org.eclipse.wst.wsdl.validation.internal.exception;

/**
 * Custom exception for WSDL validation.
 */
public class ValidateWSDLException extends Exception
{
	/**
   * Required serial version uid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * 
   * @param message The exception message
   */
  public ValidateWSDLException(String message)
	{
	  super(message);
	}
}
