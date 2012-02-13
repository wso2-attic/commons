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
 * Holds the location information for an element in a document.
 */
public class LocationHolder
{
  private int line;
  private int column;
  private String uri;
  
  /**
   * Constructor.
   * 
   * @param line The line number.
   * @param column The column number.
   * @param uri The URI of the document.
   */
  public LocationHolder(int line, int column, String uri)
  {
    this.line = line;
    this.column = column;
    this.uri = uri;
  }
  
  /**
   * Get the line number.
   * 
   * @return The line number.
   */
  public int getLine()
  {
    return line;
  }
  
  /**
   * Get the column number.
   * 
   * @return The column number.
   */
  public int getColumn()
  {
    return column;
  }
  
  /**
   * Get the file URI.
   * 
   * @return The file URI.
   */
  public String getURI()
  {
    return uri;
  }
}
