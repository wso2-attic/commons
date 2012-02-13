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
 * Base Exception for Assertion Fail.
 * 
 * @author gturrell
 * */
public class AssertionFailException extends Exception
{

  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257007635743258933L;

	/**
   * Constructor for AssertionException.
   */
  public AssertionFailException()
  {
    super();
  }

  /**
   * Constructor for AssertionException.
   * @param arg0 the detail message.
   */
  public AssertionFailException(String arg0)
  {
    super(arg0);
  }

}
