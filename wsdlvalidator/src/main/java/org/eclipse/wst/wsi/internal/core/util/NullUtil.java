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
package org.eclipse.wst.wsi.internal.core.util;

/**
 * The utility class to automate null checking.
 * 
 * @author Kulik
 */
public final class NullUtil
{
  /**
   * The method checks objects on existence and compare with <code>equals</code> method.
   * @param o1 first object.
   * @param o2 second object.
   * @return true if first object is equal to the second object.
   */
  public static boolean equals(Object o1, Object o2)
  {
    return ((o1 == null && o2 == null) || (o1 != null && o1.equals(o2)));
  }

  /**
   * The method checks object on existence and returns its string representation with <code>toString()</code> method.
   * @param o source object.
   * @return string representation of object.
   */
  public static String toString(Object o)
  {
    return (o == null) ? null : o.toString();
  }
}
