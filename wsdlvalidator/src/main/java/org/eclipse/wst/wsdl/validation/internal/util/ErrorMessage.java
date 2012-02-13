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

package org.eclipse.wst.wsdl.validation.internal.util;

/**
 * A class to hold validation messages. In this case an error message is a generic
 * term for any kind of validation message.
 */
public class ErrorMessage
{
  private int errorLine, errorColumn, severity;
  private String errorString, uri;
  private int startOffset = -1;
  private int endOffset = -1;

  /**
   * Constructor.
   */
  public ErrorMessage()
  {
  }

  /**
   * Sets the message.
   * 
   * @param error The message to set.
   * @see #getErrorMessage
   */
  public void setErrorMessage(String error)
  {
    errorString = error;
  }

  /**
   * Sets the severity of the message.
   * 
   * @param severity The severity of the message.
   * @see #getSeverity
   */
  public void setSeverity(int severity)
  {
    this.severity = severity;
  }

  /**
   * Sets the severity of the message using string names for the severity.
   * 
   * @param severity The string representation of the severity.
   * @see #getSeverity
   */
  public void setSeverity(String severity)
  {
    if (severity.equals("warning"))
    {
      this.severity = 0;
    }
    else if (severity.equals("error"))
    {
      this.severity = 1;
    }
    else if (severity.equals("fatal"))
    {
      this.severity = 2;
    }
  }

  /**
   * Sets the line where the error is located.
   * 
   * @param line The line where the error is located.
   * @see #getErrorLine
   */
  public void setErrorLine(int line)
  {
    errorLine = line;
  }

  /**
   * Sets the column where the error is located.
   * 
   * @param column The column where the error is located.
   * @see #getErrorColumn
   */
  public void setErrorColumn(int column)
  {
    errorColumn = column;
  }

  /**
   * Returns the error message.
   * 
   * @return The error message.
   * @see #setErrorMessage
   */
  public String getErrorMessage()
  {
    return errorString;
  }

  /**
   * Returns the severity of the error.
   * 
   * @return The severity of the error.
   * @see #setSeverity
   */
  public int getSeverity()
  {
    return severity;
  }

  /**
   * Returns the line location of the error.
   * 
   * @return The line location of the error.
   * @see #setErrorLine
   */
  public int getErrorLine()
  {
    return errorLine;
  }

  /**
   * Returns the column location of the error.
   * 
   * @return The column location of the error.
   * @see #setErrorColumn
   */
  public int getErrorColumn()
  {
    return errorColumn;
  }

  /**
   * Sets the start offset.
   * 
   * @param start The start offset.
   * @see #getErrorStartOffset
   */
  public void setErrorStartOffset(int start)
  {
    startOffset = start;
  }

  /**
   * Returns the error start offset.
   * 
   * @return The error start offset.
   * @see #setErrorStartOffset
   */
  public int getErrorStartOffset()
  {
    return startOffset;
  }

  /**
   * Sets the end offset.
   * 
   * @param end The end offset.
   * @see #getErrorEndOffset
   */
  public void setErrorEndOffset(int end)
  {
    endOffset = end;
  }

  /**
   * Returns the error end offset.
   * 
   * @return The error end offset.
   * @see #setErrorEndOffset
   */
  public int getErrorEndOffset()
  {
    return endOffset;
  }

  /**
   * Adds a new line ot the error message.
   * 
   * @param newLine The new line to add to the error message.
   */
  public void addNewErrorMessageLine(String newLine)
  {
    errorString += "\n" + newLine;
  }
  
  /**
   * Set the URI of the error message.
   * 
   * @param uri The URI to set.
   */
 
  public void setURI(String uri)
  {
    this.uri = uri;
  }
  
  /**
   * Get the URI of the error message.
   * 
   * @return The URI of the error message.
   */
  public String getURI()
  {
    return uri;
  }
}
