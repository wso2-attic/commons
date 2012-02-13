/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core;

/**
 * This class represents runtime exceptions that occur after processing the configuration files.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class WSIRuntimeException extends WSIException
{

  /**
   * Static needed for serializable class
   */
  private static final long serialVersionUID = 4309320981515513171L;

  /**
   * Constructor for WSIRuntimeException.
   */
  public WSIRuntimeException()
  {
    super();
  }

  /**
   * Constructor for WSIRuntimeException.
   * @param msg  the detail message.
   */
  public WSIRuntimeException(String msg)
  {
    super(msg);
  }

  /**
   * Constructor for WSIRuntimeException.
   * @param msg        the detail message.
   * @param throwable  the initial exception thrown.
  
   */
  public WSIRuntimeException(String msg, Throwable throwable)
  {
    super(msg, throwable);
  }

}
