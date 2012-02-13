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

import org.eclipse.wst.wsi.internal.core.monitor.config.Comment;

/**
 * The implementation for monitor config Comment element. 
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class CommentImpl implements Comment
{
  /**
   * Comment text.
   */
  protected String text = null;

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Comment#getText()
   */
  public String getText()
  {
    return this.text;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Comment#setText(String)
   */
  public void setText(String text)
  {
    this.text = text;
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

    // Config options
    //pw.print("    <" + nsName + ELEM_NAME + " xmlns=\"" + WSIConstants.NS_URI_WSI_MONITOR_CONFIG + "\">");    
    pw.print("    <" + nsName + ELEM_NAME + ">");
    pw.print(this.text);

    // End element
    pw.println("</" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }

  public String toString()
  {
    return text;
  }

}
