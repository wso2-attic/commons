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
package org.eclipse.wst.wsdl.validation.internal.logging;

/**
 * The interface for a standard logger for the WSDL validator. 
 * Allows for logging errors and warnings.
 */
public interface ILogger
{
  /**
   * Severity error for logging.
   */
  public static int SEV_ERROR = 0;
  
  /**
   * Severity warning for logging.
   */
  public static int SEV_WARNING = 1;
  
  /**
   * Severity info for logging.
   */
  public static int SEV_INFO = 2;
  
  /**
   * Severity verbose for logging.
   */
  public static int SEV_VERBOSE = 3;
  
  /**
   * Log a message.
   * 
   * @param message 
   * 		The message to log.
   * @param severity
   * 		The severity of the message to log.
   */
  public void log(String message, int severity);
  
  /**
   * Log a message.
   * 
   * @param message 
   * 		The message to log.
   * @param severity
   * 		The severity of the message to log.
   * @param throwable 
   * 		The exception to log.
   */
  public void log(String message, int severity, Throwable throwable);
}
