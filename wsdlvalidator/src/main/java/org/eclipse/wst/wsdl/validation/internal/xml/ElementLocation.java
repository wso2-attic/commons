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

package org.eclipse.wst.wsdl.validation.internal.xml;

/**
 * Holds the location information for an element in the model.
 */
public class ElementLocation
{  
  public static final String ID = "location";
  protected int line;
  protected int column;
  /**
   * Constructor.
   * 
   * @param line - the line location of the element
   * @param column - the column location of the element
   */
  public ElementLocation(int line, int column)
  {
    this.line = line;
    this.column = column;
  }
  
  /**
   * Return the line number for this element.
   * 
   * @return the line number for this element
   */
  public int getLineNumber()
  {
  	return line;
  }
  
  /**
   * Return the column number for this element.
   * 
   * @return the column number for this element
   */
  public int getColumnNumber()
  {
  	return column;
  }
}
