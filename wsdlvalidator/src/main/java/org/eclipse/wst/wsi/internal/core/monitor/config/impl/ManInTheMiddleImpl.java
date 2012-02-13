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
package org.eclipse.wst.wsi.internal.core.monitor.config.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.monitor.config.Comment;
import org.eclipse.wst.wsi.internal.core.monitor.config.ManInTheMiddle;
import org.eclipse.wst.wsi.internal.core.monitor.config.Redirect;

/**
 * The implementation for the monitor config ManInTheMiddle element.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class ManInTheMiddleImpl implements ManInTheMiddle
{
  /**
   * Comment.
   */
  protected Comment comment;

  /**
   * Redirect list.
   */
  protected Vector redirectList = new Vector();

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.ManInTheMiddle#getComment()
   */
  public Comment getComment()
  {
    return this.comment;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.ManInTheMiddle#setComment(Comment)
   */
  public void setComment(Comment comment)
  {
    this.comment = comment;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.ManInTheMiddle#getRedirectList()
   */
  public Vector getRedirectList()
  {
    return redirectList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.ManInTheMiddle#addRedirect(Redirect)
   */
  public void addRedirect(Redirect redirect)
  {
    redirectList.add(redirect);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.ManInTheMiddle#setRedirectList(Vector)
   */
  public void setRedirectList(Vector redirectList)
  {
    this.redirectList = redirectList;
  }

  /* (non-Javadoc)
   * Get string representation of this object.
   */
  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println("  man-in-the-middle comment ... " + this.comment);

    // Redirect list
    int count = 1;
    Redirect redirect;
    Iterator iterator = redirectList.iterator();
    while (iterator.hasNext())
    {
      // Get next redirect element
      redirect = (Redirect) iterator.next();
      pw.println("  redirect [" + count++ +"]");
      pw.print(redirect.toString());
    }

    return sw.toString();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Start element
    pw.println("      <" + nsName + ELEM_NAME + ">");

    // Comment
    if (this.comment != null)
    {
      pw.println(this.comment.toXMLString(nsName));
    }

    // Redirect
    Redirect redirect;
    Iterator iterator = redirectList.iterator();
    while (iterator.hasNext())
    {
      // Get next redirect element
      redirect = (Redirect) iterator.next();
      pw.print(redirect.toXMLString(nsName));
    }

    // End element
    pw.println("      </" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }

}
