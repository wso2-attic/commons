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
package org.eclipse.wst.wsi.internal.analyzer;

/**
 * AnalyzerException
 * 
 * Exception to be thrown if an analyzer fails
 */
public class WSIAnalyzerException extends Exception
{
  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3835158350219194677L;
	/**
   * Throwable.
   */
  protected Throwable throwable = null;

  /**
   * Constructor for AnalyzerException.
   */
  public WSIAnalyzerException()
  {
    super();
  }

  /**
   * Constructor for AnalyzerException.
   * @param s detail message.
   */
  public WSIAnalyzerException(String s)
  {
    super(s);
  }

  /**
   * Create an exception with a message and related exception.
   * 
   * @param msg        the exception message
   * @param throwable  throwable that is related to this exception
   */
  public WSIAnalyzerException(String msg, Throwable throwable)
  {
    // Exception
    super(msg);

    // Save input reference
    this.throwable = throwable;
  }

  /**
   * Returns the String representation of this object's values.
   *
   * @return Returns the detail message of this throwable object.
   */
  public String getMessage()
  {
    return super.getMessage();
  }

  /**
   * Returns the exception that caused this exception to be created.
   * 
   * @return Returns the encapsulated throwable object.
   */
  public Throwable getTargetException()
  {
    // Return throwable
    return throwable;
  }

}
