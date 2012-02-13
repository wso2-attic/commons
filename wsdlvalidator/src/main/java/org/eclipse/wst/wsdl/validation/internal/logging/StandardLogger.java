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

import java.io.PrintStream;

/**
 * A logger that will print log info to standard system outputs.
 */
public class StandardLogger implements ILogger
{ 
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int)
   */
  public void log(String message, int severity) 
  {  
	PrintStream outputStream = System.out;
	if(severity == ILogger.SEV_ERROR)
	{
	  outputStream = System.err;
	}
    outputStream.println(message);
  }

/* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.validation.internal.logging.ILogger#log(java.lang.String, int, java.lang.Throwable)
   */
  public void log(String error, int severity, Throwable throwable)
  {
	log(error, severity);
	log(throwable.toString(), severity);
  }
}
