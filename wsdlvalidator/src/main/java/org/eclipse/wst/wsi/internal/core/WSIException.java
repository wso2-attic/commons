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

import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;

/**
 * Primary WS-I exception.
 *
 * @version 1.0.1
 * @author Peter Brittenham
 */
public class WSIException extends Exception
{

  /**
   * Static needed for serializable class
   */
  private static final long serialVersionUID = 5446823222767299308L;

  /**
   * Throwable.
   */
  protected Throwable throwable = null;

  /**
   * Create an exception without a message.
   */
  public WSIException()
  {
    // Exception
    super();
  }

  /**
   * Create an exception with a message.
   * 
   * @param msg the exception message
   */
  public WSIException(String msg)
  {
    // Exception
    super(msg);
  }

  /**
   * Create an exception with a message and related exception.
   * 
   * @param msg        the exception message
   * @param throwable  throwable that is related to this exception
   */
  public WSIException(String msg, Throwable throwable)
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
   * Returns the String representation of this object's values.
   *
   * @return Returns the detail message of this throwable object.
   */
  public String toString()
  {
    //StringWriter sw;
    //PrintWriter pw;

    // Always get message from super class
    String msg = super.getMessage();

    // If this exception contains a reference to another exception,
    // then add it to the message
    if (throwable != null)
    {
      msg += "\n"
        + "------------------------------------------------------------------------------\n"
        + "  Nested exception is: \n\n  "
        + throwable.toString()
        + "\n";

      // If throwable is MissingResourceException, 
      // then add class name and key to output
      if (throwable instanceof MissingResourceException)
      {
        MissingResourceException mre = (MissingResourceException) throwable;

        // Get class name and key
        msg += " - "
          + mre.getKey()
          + "\n\t"
          + "[Class Name: "
          + mre.getClassName()
          + "]";
      }

      // If throwable is InvocationTargetException, 
      // then target of exception
      else if (throwable instanceof InvocationTargetException)
      {
        InvocationTargetException ite = (InvocationTargetException) throwable;

        // Get target
        msg += " - " + ite.getTargetException().toString();
      }

      // ADD: Print stack trace
      //sw = new StringWriter();
      //pw = new PrintWriter(sw);
      //throwable.printStackTrace(pw);

      // Add it to the message
      //msg += sw.toString();
    }

    // Return message
    return msg;
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
