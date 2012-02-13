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

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * The class accumulates all error strings into string buffer.
 * 
 * @author Kulik
 */
public final class ErrorList
{
  private Set errors = new HashSet();
  private MessageFormat format = new MessageFormat("{0}:{1}");
  private FieldPosition pos = new FieldPosition(0);

  /**
   * Default constructor.
   * @see java.lang.Object#Object()
   */
  public ErrorList()
  {
  }

  /**
   * Construct error list using contents of a list.
   * @param list a List object.
   * @see java.lang.Object#Object()
   */
  public ErrorList(List list)
  {
    add(list);
  }

  /**
   * Constructor with the specified message format.
   * @param f a message format.
   */
  public ErrorList(MessageFormat f)
  {
    if (f != null)
      format = f;
  }

  /**
   * Add list. 
   * @param list list of errors.
   */
  public void add(List list)
  {
    Iterator iterator = list.iterator();
    while (iterator.hasNext())
    {
      add((String) iterator.next());
    }
  }

  /**
   * Adds error description into the list. 
   * @param s1 a string.
   * @param s2 a string.
   */
  public void add(String s1, String s2)
  {
    errors.add(new Pair(s1, s2));
  }

  /**
   * Adds error description into the list.
   * @param q a QName object.
   * @param s a string.
   */
  public void add(QName q, String s)
  {
    errors.add(new Pair(NullUtil.toString(q), s));
  }

  /**
   * Adds error description into the list.
   * @param q1 a QName object.
   * @param q2 a string.
   */
  public void add(QName q1, QName q2)
  {
    errors.add(new Pair(NullUtil.toString(q1), NullUtil.toString(q2)));
  }

  /**
   * Adds error description into the list...
   * @param q a QName object.
   */
  public void add(QName q)
  {
    errors.add(new Pair(NullUtil.toString(q), null));
  }

  /**
   * Adds error description into the list.
   * @param s a string.
   */
  public void add(String s)
  {
    errors.add(new Pair(s, null));
  }

  /**
   * Returns the error list string representation. 
   * @return the error list string representation. 
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuffer buf = new StringBuffer();

    Iterator it = errors.iterator();
    while (it.hasNext())
    {
      Pair p = (Pair) it.next();
      if (p.getFirst() != null && p.getSecond() != null)
        format.format(new Object[] { p.getFirst(), p.getSecond()}, buf, pos);
      else if (p.getFirst() != null)
        buf.append(p.getFirst());
      else
        buf.append(p.getSecond());
      if (it.hasNext())
        buf.append(",\n");
    }

    return buf.toString();
  }

  /**
   * Indicates whether error list is empty or not.
   * @return true if error list is empty.
   */
  public boolean isEmpty()
  {
    return errors.size() == 0;
  }
}
