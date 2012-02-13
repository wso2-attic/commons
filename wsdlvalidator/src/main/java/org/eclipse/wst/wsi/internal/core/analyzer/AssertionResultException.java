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
package org.eclipse.wst.wsi.internal.core.analyzer;

/**
 * Exception for Assertion Result.
 * 
 * @author gturrell
 */
public class AssertionResultException extends Exception
{

  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3256720684700152632L;
	private String detailMessage;
  /**
   * Constructor for AssertionresultException.
   */
  public AssertionResultException()
  {
    super();
    detailMessage = null;
  }

  /**
   * Constructor for AssertionResultException.
   * @param arg0 the detail message.
   */
  public AssertionResultException(String arg0)
  {
    super(arg0);
    detailMessage = null;
  }

  /**
   * Constructor for AssertionResultException.
   * @param arg0 the detail message.
   * @param arg1  further detailed message.
   */
  public AssertionResultException(String arg0, String arg1)
  {
    super(arg0);
    detailMessage = arg1;
  }

  /**
   * Returns the detailMessage.
   * @return the detailMessage.
   */
  public String getDetailMessage()
  {
    return detailMessage;
  }

}
