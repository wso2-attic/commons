/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.xml;

import java.util.List;

import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;

/**
 * An interface for an XML validator.
 */
public interface IXMLValidator
{
  /**
   * Set the file to be validated.
   * 
   * @param uri - the uri of the file to be validated
   */
  public void setFile(String uri);
  
  /**
   * Validate the file.
   */
  public void run();

  /**
   * Returns true if there were validation errors, false otherwise.
   * 
   * @return true if there were validation errors, false otherwise
   */
  public boolean hasErrors();
  
  /**
   * Returns the list of errors.
   * 
   * @return the list of errors
   */
  public List getErrors();
  
  /**
   * Set the URI resolver to use.
   * 
   * @param uriResolver The URI resolver to use.
   */
  public void setURIResolver(URIResolver uriResolver);
}
