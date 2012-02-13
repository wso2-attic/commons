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
package org.eclipse.wst.wsi.internal.core.monitor.config;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.document.WSIDocument;
import org.eclipse.wst.wsi.internal.core.util.MessageList;

/**
 * This is the interface for the monitor configuration file.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface MonitorConfig extends WSIDocument
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_CONFIG;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_MONITOR_CONFIG, ELEM_NAME);

  /**
   * Initialize monitor config.
   * @param messageList a lMessageList object.
   */
  public void init(MessageList messageList);

  /**
   * Get comment.
   * @return comment.
   * @see #setComment
   */
  public Comment getComment();

  /**
   * Set comment.
   * @param comment a Comment object.
   * @see #getComment
   */
  public void setComment(Comment comment);

  /**
   * Get log file location.
   * @return log file location.
   * @see #setLogLocation
   */
  public String getLogLocation();

  /**
   * Set log file location.
   * @param logURI log file location.
   * @see #getLogLocation
   */
  public void setLogLocation(String logURI);

  /**
   * Get replace log.
   * @return replace log.
   * @see #setReplaceLog
   */
  public boolean getReplaceLog();

  /**
   * Set replace log.
   * @param replaceLog a replace log.
   * @see #getReplaceLog
   */
  public void setReplaceLog(boolean replaceLog);

  /**
   * Get add style sheet.
   * @return add style sheet.
   * @see #setAddStyleSheet
   */
  public AddStyleSheet getAddStyleSheet();

  /**
   * Set add style sheet.
   * @param addStyleSheet add style sheet.
   * @see #getAddStyleSheet
   */
  public void setAddStyleSheet(AddStyleSheet addStyleSheet);

  /**
   * Get log duration.
   * @return log duration.
   * @see #setLogDuration
   */
  public int getLogDuration();

  /**
   * Set log duration.
   * @param logDuration log duration.
   * @see #getLogDuration
   */
  public void setLogDuration(int logDuration);

  /**
   * Get timeout.
   * @return timeout.
   * @see #setTimeout
   */
  public int getTimeout();

  /**
   * Set timeout.
   * @param timeout timeout.
   * @see #getTimeout
   */
  public void setTimeout(int timeout);

  /**
   * Get man-in-the-middle information.
   * @return man-in-the-middle information.
   * @see #setManInTheMiddle
   */
  public ManInTheMiddle getManInTheMiddle();

  /**
   * Set man-in-the-middle information.
   * @param manInTheMiddle man-in-the-middle information.
   * @see #getManInTheMiddle
   */
  public void setManInTheMiddle(ManInTheMiddle manInTheMiddle);

  /**
   * Get verbose option.
   * @return verbose option.
   * @see #setVerboseOption
   */
  public boolean getVerboseOption();

  /**
   * Set verbose option.
   * @param verbose verbose option.
   * @see #getVerboseOption
   */
  public void setVerboseOption(boolean verbose);

  /**
   * Parse the command line arguments.
   * @param args the command line arguments.
   * @throws WSIException if problems occur while parsing the 
   *         command line arguments,
   */
  public void parseArgs(String[] args) throws WSIException;
}
