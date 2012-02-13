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
 * The class represents pair of values.
 * 
 * @author Kulik
 */
public final class Pair
{
  final private Object first;
  final private Object second;

  /**
   * Constructor.
   * @param first object
   * @param second object.
   */
  public Pair(Object first, Object second)
  {
    this.first = first;
    this.second = second;
  }

  /**
   * Gets first object.
   * @return Object
   */
  public Object getFirst()
  {
    return first;
  }

  /**
   * Gets second object.
   * @return Object
   */
  public Object getSecond()
  {
    return second;
  }

  /**
   * @see java.lang.Object#equals(Object)
   */
  public boolean equals(Object o)
  {
    if (o == null || !(o instanceof Pair))
      return false;
    Pair p = (Pair) o;
    return (
      NullUtil.equals(p.getFirst(), first)
        && NullUtil.equals(p.getSecond(), second));
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    int code = 0;
    if (first != null)
      code += first.hashCode();
    if (second != null)
      code += second.hashCode();
    return code;
  }
}
