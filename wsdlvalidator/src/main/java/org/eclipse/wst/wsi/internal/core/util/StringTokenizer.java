/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 ********************************************************************************/
package org.eclipse.wst.wsi.internal.core.util;

/**
 * String tokenizer.
 * 
 * @author: Kulik
 */
final public class StringTokenizer
{
  private char[] spaceDel;
  private char[] del;
  private char[] allDel;

  /**
   * Enumeration of strings.
  * @author Kulik
   */
  private class StringTokenizerEnumeration implements java.util.Enumeration
  {
    private final char[] str;
    private final int size;
    private int index;

    /**
     * Constructor.
     * @param s
     */
    StringTokenizerEnumeration(char[] s)
    {
      str = s;
      size = str.length;
      index = 0;

      // skip space delimiters
      while (index < size && contain(str[index], spaceDel))
        index++;
    }

    /**
     * @see java.util.Enumeration#hasMoreElements()
     */
    public boolean hasMoreElements()
    {
      return (index < size);
    }

    /**
     * @see java.util.Enumeration#nextElement()
     */
    public Object nextElement()
    {
      if (index >= size)
        throw new java.util.NoSuchElementException(
          "StringTokenizer enumeration");

      // skip delimiters
      int begStr = index;
      if (index < size && contain(str[index], del))
        index++;

      // skip word
      if (begStr == index)
        while (index < size && !contain(str[index], allDel))
          index++;

      int endStr = index;

      // skip space delimiters
      while (index < size && contain(str[index], spaceDel))
        index++;

      return String.copyValueOf(str, begStr, endStr - begStr);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      int i = index;
      String s = (String) nextElement();
      index = i;
      return s;
    }
  }

  /**
   * StringTokenizer constructor.
     * @param spaceDelimiters the set of delimiters to be ignored
     * @param delimiters the set of delimiters to be remained.
   */
  public StringTokenizer(char[] spaceDelimiters, char[] delimiters)
  {
    super();

    if (spaceDelimiters != null)
    {
      spaceDel = new char[spaceDelimiters.length];
      System.arraycopy(spaceDelimiters, 0, spaceDel, 0, spaceDelimiters.length);
      sort(spaceDel);
    }
    else
      spaceDel = null;

    if (delimiters != null)
    {
      del = new char[delimiters.length];
      System.arraycopy(delimiters, 0, del, 0, delimiters.length);
      sort(del);
    }
    else
      del = null;

    if (del != null && spaceDel != null)
    {
      allDel = new char[del.length + spaceDel.length];
      System.arraycopy(del, 0, allDel, 0, del.length);
      System.arraycopy(spaceDel, 0, allDel, del.length, spaceDel.length);
    }
    else if (del != null)
      allDel = del;
    else
      allDel = spaceDel;

    sort(allDel);
  }
  /**
   * Parses string.
   * @return java.util.Enumeration
   * @param s java.lang.String
   */
  public java.util.Enumeration parse(String s)
  {
    if (s == null)
      throw new IllegalArgumentException("StringTokenizer : String cannot be NULL");

    return new StringTokenizerEnumeration(s.toCharArray());
  }

  /**
   * Binary search.
   * @return boolean
   * @param c char
   */
  private static boolean contain(char c, char[] a)
  {
    if (a == null)
      return false;

    int l = 0, r = a.length - 1, center;
    while (l < r)
    {
      center = (l + r) / 2;
      if (c > a[center])
        l = center + 1;
      else
        r = center;
    }

    return a[l] == c;
  }
  /**
   * Heap sort
   * @param c char[]
   */
  static private void sort(char[] c)
  {
    if (c != null)
    {
      int j, k;
      char ci;

      // push heap
      for (int i = 1; i < c.length; i++)
      {
        j = ((k = i) - 1) / 2;
        ci = c[i];
        while (k > 0 && c[j] < ci)
        {
          c[k] = c[j];
          j = ((k = j) - 1) / 2;
        }
        c[k] = ci;
      }

      // pop heap
      for (int i = c.length - 1; i > 0; i--)
      {
        j = 2;
        k = 0;
        ci = c[0];
        while (j <= i)
        {
          c[k] = (c[j - 1] > c[j]) ? c[--j] : c[j];
          j = ((k = j) + 1) * 2;
        }
        c[k] = c[i];
        c[i] = ci;
      }
    }
  }
}
