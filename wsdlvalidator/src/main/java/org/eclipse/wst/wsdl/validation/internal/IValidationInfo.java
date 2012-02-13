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

import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;


/**
 * An interface representing the current validation information.
 * This interface is not meant to be implemented by clients.
 */
public interface IValidationInfo extends IValidationReport
{
  
  /**
   * Add an error message for this file. (Uses the URI from this validation info object.)
   * 
   * @param message The message for the error.
   * @param line The line location of the error in the file.
   * @param column The column location of the error in the file.
   * @deprecated
   */
  public void addError(String message, int line, int column);
  
  /**
   * Add an error message.
   * 
   * @param message The message for the error.
   * @param line The line location of the error in the file.
   * @param column The column location of the error in the file.
   * @param uri The URI of the file that contains the error.
   */
  public void addError(String message, int line, int column, String uri);
  
  /**
   * Add a warning message for this file. (Uses the URI from this validation info object.)
   * 
   * @param message The message for the warning.
   * @param line The line location of the warning in the file.
   * @param column The column location of the warning in the file.
   * @deprecated
   */
  public void addWarning(String message, int line, int column);
  
  /**
   * Add a warning message.
   * 
   * @param message The message for the warning.
   * @param line The line location of the warning in the file.
   * @param column The column location of the warning in the file.
   * @param uri The URI of the file that contains the error.
   */
  public void addWarning(String message, int line, int column, String uri);
  
  /**
   * Get the URI resolver in use for this validation. The URI resolver
   * returned is actually a URI resolver handler that will 
   * iterate through all of the registered URI resolvers.
   * 
   * @return The URI resolver handler.
   */
  public URIResolver getURIResolver();
  
  /**
   * Get the attribute with the given name. If the attribute
   * has not been registered, return null.
   * 
   * @param name The name of the attribute being requested.
   * @return The attribute value if the attribute has been registered, null otherwise.
   */
  public Object getAttribute(String name);
}
