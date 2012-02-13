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
package org.eclipse.wst.wsi.internal.core.xml.dom;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.xml.sax.Locator;

/**
 * This class provides line and column information for a node within an XML document.
 *  
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ElementLocation
{
  /**
   * Line number.
   */
  public static final String KEY_NAME = ElementLocation.class.getName();

  /**
   * Line number.
   */
  protected int lineNumber = 0;

  /**
   * Column number.
   */
  protected int columnNumber = 0;

  /**
   * Element location.
   * @param lineNumber    a line number.
   * @param columnNumber  a column number.
   */
  public ElementLocation(int lineNumber, int columnNumber)
  {
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
  }

  /**
   * Element location.
   * @param locator  a Locator object.
   */
  public ElementLocation(Locator locator)
  {
    this.lineNumber = locator.getLineNumber();
    this.columnNumber = locator.getColumnNumber();
  }

  /**
   * Get the line number.
   * @return an int representing the line number value.
   */
  public int getLineNumber()
  {
    return this.lineNumber;
  }

  /**
   * Get the column number.
   * @return an int representing the column number value.
   */
  public int getColumnNumber()
  {
    return this.columnNumber;
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    return "Element Location:\n"
      + "  "
      + WSIConstants.ATTR_LINE_NUMBER
      + "="
      + this.lineNumber
      + "\n";
    //+ "  " + WSIConstants.ATTR_COLUMN_NUMBER + "=" + this.columnNumber + "\n";
  }
}
