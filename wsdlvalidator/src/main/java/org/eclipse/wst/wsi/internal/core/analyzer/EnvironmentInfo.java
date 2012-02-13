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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.util.XMLInfo;

/**
 * This class maintains the Environment information.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class EnvironmentInfo implements XMLInfo
{
  /**
   * Log timestamp.
   */
  protected String logTimestamp = null;

  /**
   * Monitor config comment.
   */
  protected String monitorConfigComment = null;

  /**
   * Create object.
   * @param logTimestamp the log timestamp.
   * @param monitorConfigComment the Monitor config comment.
   */
  public EnvironmentInfo(String logTimestamp, String monitorConfigComment)
  {
    this.logTimestamp = logTimestamp;
    this.monitorConfigComment = monitorConfigComment;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.util.XMLInfo#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if (!nsName.equals(""))
      nsName += ":";

    // Start element
    pw.print("      <" + nsName + WSIConstants.ELEM_LOG + " ");
    pw.println(
      WSIConstants.ATTR_TIMESTAMP + "=\"" + this.logTimestamp + "\"/>");

    pw.print(
      "        <"
        + WSIConstants.NS_NAME_WSI_LOG
        + WSIConstants.ELEM_COMMENT
        + ">");
    pw.print(this.monitorConfigComment);
    pw.println(
      "        </"
        + WSIConstants.NS_NAME_WSI_LOG
        + WSIConstants.ELEM_COMMENT
        + ">");

    // End element
    pw.println("    </" + nsName + WSIConstants.ELEM_LOG + ">");

    return sw.toString();
  }

}
