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
 * The logger factory allows for simple use of whatever logging mechanism is in
 * place. The tool can specify a custom ILogger. The WSDL validator will request the
 * logger from this class.
 */
public class LoggerFactory
{
  private static LoggerFactory factory = null;
  private ILogger logger  = null;

  /**
   * Get the one and only instance of the logger factory.
   * 
   * @return The one and only instance of the logger.
   */
  public static LoggerFactory getInstance()
  {
	if(factory == null)
	{
	  factory = new LoggerFactory();
	}
	return factory;
  }
  
  /**
   * Set the logger implementation to be used.
   * 
   * @param logger
   *            The ILogger to use.
   */
  public void setLogger(ILogger logger)
  {
    this.logger = logger;
  }
  
  /**
   * Get the logger.
   * 
   * @return
   * 		The logger.
   */
  public ILogger getLogger()
  {
	if(logger == null)
	{
	  logger = new StandardLogger();
	}
	return logger;
  }
}
