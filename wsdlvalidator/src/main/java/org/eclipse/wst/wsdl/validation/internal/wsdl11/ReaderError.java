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

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

/**
 * Holds an error created when reading a WSDL document.
 */
public class ReaderError
{
  protected Object parentObject; // the object of the parent of the object with the error
  protected Object object; // the object the error is associated with
  protected String error; // the error associated with the object 

  /**
   * Constructor.
   * 
   * @param parentObject the parent object of the object with the error
   * @param object the object with the error
   * @param error the error
   */
  public ReaderError(Object parentObject, Object object, String error)
  {
    this.parentObject = parentObject;
    this.object = object;
    this.error = error;
  }

  /**
   * Returns the parent object of the object with the error.
   * 
   * @return the parent object of the object with the error
   */
  public Object getParentObject()
  {
    return parentObject;
  }

  /**
   * Returns the object with the error.
   * 
   * @return the object with the error
   */
  public Object getObject()
  {
    return object;
  }

  /**
   * Returns the error message.
   * 
   * @return the error message
   */
  public String getError()
  {
    return error;
  }
}
