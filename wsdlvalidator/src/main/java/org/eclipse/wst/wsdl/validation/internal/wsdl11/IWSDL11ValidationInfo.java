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

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

import java.util.Hashtable;

import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.wsdl.validation.internal.resolver.URIResolver;

/**
 * An interface for WSDL 1.1 validation information. Uses an existing
 * validation info object and provides methods to set and retrieve
 * schemas and convenience methods for setting errors with objects.
 */
public interface IWSDL11ValidationInfo
{
  /**
   * Returns the URI of the file being validated.
   * 
   * @return The URI of the file being validated.
   */
  public String getFileURI();
  
  /**
   * Add a schema to the list of schemas available for this WSDL document.
   * 
   * @param xsModel The schema to add to the list.
   */
  public void addSchema(XSModel xsModel);
  
  /**
   * Get an array of all the schemas available for this WSDL document.
   * 
   * @return An array of all the schemas available for this WSDL document.
   */
  public XSModel[] getSchemas();
  
  /**
   * Clear all the stored schemas.
   */
  public void clearSchemas();
  
  /**
   * Set the element locations hashtable.
   * 
   * @param elementLocations The hashtable to set with the element locations.
   */
  public void setElementLocations(Hashtable elementLocations);
  
  /**
   * Convenience method for extensibly validators to add error messages.
   * 
   * @param message The error to add.
   * @param element The object to add the error for.
   */
  public void addError(String message, Object element);
  
  /**
   * Convenience method for extensibly validators to add error messages.
   * 
   * @param message The error to add.
   * @param element The object to add the error for.
   * @param errorKey The error key for this message
   * @param messageArguments The strings used to create the message.
   */
  public void addError(String message, Object element, String errorKey, Object[] messageArguments);
  
  /**
   * Add an error message at the given line and column.
   * 
   * @param message The error to add.
   * @param line The line location of the error.
   * @param column The column location of the error.
   * @param uri The uri of the file containing the error.
   */
  public void addError(String message, int line, int column, String uri);
  
  /**
   * Convenience method for extensibly validators to add warning messages.
   * 
   * @param message The warning to add.
   * @param element The object to add the warning for.
   */
  public void addWarning(String message, Object element);
  
  /**
   * Add a warning message at the given line and column.
   * 
   * @param message The warning to add.
   * @param line The line location of the warning.
   * @param column The column location of the warning.
   * @param uri The uri of the file containing the warning.
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
   * Get the schema cache if one is specified.
   * 
   * @return The schema cache if one is specified, null otherwise.
   */
  public XMLGrammarPool getSchemaCache();
  
  /**
   * Get the XML cache if one is specified.
   * 
   * @return The XML cache if one is specified, null otherwise.
   */
  public XMLGrammarPool getXMLCache();
}

