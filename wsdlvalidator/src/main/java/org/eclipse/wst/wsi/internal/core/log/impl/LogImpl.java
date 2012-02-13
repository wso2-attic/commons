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
package org.eclipse.wst.wsi.internal.core.log.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;
import org.eclipse.wst.wsi.internal.core.util.Utils;

/**
 * This class represents the message log file.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class LogImpl implements Log
{
  /**
   * Log URI.
   */
  protected String logURI = null;

  /**
   * Start time stamp.
   */
  protected String startTimestamp = null;

  /**
   * Log entries.
   */
  protected Vector logEntryList = new Vector();

  /**
   * Last log entry.
   */
  protected MessageEntry lastLogEntry = null;

  /**
   * Log title.
   */
  // ADD: Should we provide a way to set the log title?
  protected String logTitle = "Message Log File";

  /**
   * Style sheet string.
   */
  protected String styleSheetString = null;

  /**
   * @see org.eclipse.wst.wsi.internal.core.log.Log#addLogEntry(LogEntry)
   */
  public void addLogEntry(MessageEntry logEntry) throws WSIException
  {
    // Save last log entry
    lastLogEntry = logEntry;

    // Add log entry
    logEntryList.add(logEntry);
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.log.Log#getEntryCount()
   */
  public int getEntryCount()
  {
    return logEntryList.size();
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.log.Log#getLogEntry(int)
   */
  public MessageEntry getLogEntry(int index)
  {
    return (MessageEntry) logEntryList.elementAt(index);
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.log.Log#getLogEntryList()
   */
  public Vector getLogEntryList()
  {
    return logEntryList;
  }

  /**
   * Get last log entry.
   */
  public MessageEntry getLastLogEntry()
  {
    return lastLogEntry;
  }

  /**
   * Create log entry object.
   */
  public MessageEntry createLogEntry()
  {
    return new MessageEntryImpl();
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.WSIDocument#getLocation()
   */
  public String getLocation()
  {
    return this.logURI;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.WSIDocument#setLocation(String)
   */
  public void setLocation(String documentURI)
  {
    this.logURI = documentURI;
  }

  /**
   * Set style sheet string.
   */
  public void setStyleSheetString(String styleSheetString)
  {
    this.styleSheetString = styleSheetString;
  }

  /**
   * Get start element string. 
   */
  public String getStartXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Create report element 
    pw.println(WSIConstants.XML_DECL);
    if (this.styleSheetString != null)
      pw.println(this.styleSheetString);

    // Add XML comment
    String comment;
    if ((comment = TestUtils.getXMLComment()) != null)
      pw.print(comment);

    // Log
    pw.print("<" + nsName + ELEM_NAME);

    // REMOVED: No longer required by the monitor spec
    //pw.print(" " + WSIConstants.ATTR_NAME + "=\"" + logTitle + "\"");
    pw.println(
      " " + WSIConstants.ATTR_TIMESTAMP + "=\"" + Utils.getTimestamp() + "\"");
    pw.println("    xmlns=\"" + WSIConstants.NS_URI_WSI_LOG + "\"");
    pw.println(
      "    xmlns:"
        + WSIConstants.NS_NAME_WSI_MONITOR_CONFIG
        + "=\""
        + WSIConstants.NS_URI_WSI_MONITOR_CONFIG
        + "\"");
    //pw.println("    xmlns:" + WSIConstants.NS_NAME_WSI_COMMON + "=\"" + 
    //      WSIConstants.NS_URI_WSI_COMMON + "\"");
    pw.println(
      "    xmlns:"
        + WSIConstants.NS_NAME_XSI
        + "=\""
        + WSIConstants.NS_URI_XSI
        + "\">");

    // Add  monitor tool info
    //pw.println(monitor.toXMLString(nsName));

    // Return XML string
    return sw.toString();
  }

  /**
   * Get end element string. 
   */
  public String getEndXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // End log element 
    pw.println("</" + nsName + ELEM_NAME + ">");

    // Return XML string
    return sw.toString();
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    // ADD:
    return null;
  }

}
