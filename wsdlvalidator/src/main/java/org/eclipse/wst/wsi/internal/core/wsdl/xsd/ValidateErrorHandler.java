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
package org.eclipse.wst.wsi.internal.core.wsdl.xsd;

import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLParseException;

/**
 * An implementation of XMLErrorHandler that captures error from Xerces.
 * 
 * @author Lawrence Mandel (lmandel@ca.ibm.com)
 */
public class ValidateErrorHandler implements XMLErrorHandler
{
  ArrayList errorList = new ArrayList();

  /**
   * Get the error messages created by Xerces.
   * 
   * @return The errors list.
   */
  public List getErrorMessages()
  {
    return errorList;
  }

  /**
   * @see org.apache.xerces.xni.parser.XMLErrorHandler#error(java.lang.String, java.lang.String, org.apache.xerces.xni.parser.XMLParseException)
   */
  public void error(String arg0, String arg1, XMLParseException exception) throws XNIException
  {
    errorList.add("Error: " + exception.getMessage());
  }

  /**
   * @see org.apache.xerces.xni.parser.XMLErrorHandler#fatalError(java.lang.String, java.lang.String, org.apache.xerces.xni.parser.XMLParseException)
   */
  public void fatalError(String arg0, String arg1, XMLParseException exception) throws XNIException
  {
    errorList.add("Fatal error: " + exception.getMessage());
  }

  /**
   * @see org.apache.xerces.xni.parser.XMLErrorHandler#warning(java.lang.String, java.lang.String, org.apache.xerces.xni.parser.XMLParseException)
   */
  public void warning(String arg0, String arg1, XMLParseException exception) throws XNIException
  {
    errorList.add("Warning: " + exception.getMessage());
  }
}