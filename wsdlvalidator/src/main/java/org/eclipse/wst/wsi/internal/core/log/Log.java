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
package org.eclipse.wst.wsi.internal.core.log;

import java.util.Vector;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.WSIDocument;

/**
 * The interface for the message log file.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface Log extends WSIDocument
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_LOG;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_LOG, ELEM_NAME);

  /**
   * Add message to log file.  The message will contain both 
   * transport and content information.
   * @param logEntry message containing both transport and content information.
   * @throws WSIException if problems occur adding the message to log file.
   */
  public void addLogEntry(MessageEntry logEntry) throws WSIException;

  /**
   * Get the number of log entries.
   * @return the number of log entries. 
   */
  public int getEntryCount();

  /**
   * Get a specific log file entry. 
   * @param index index into log entries.
   * @return log file entry at specified index.
   */
  public MessageEntry getLogEntry(int index);

  /**
   * Get log entry list.
   * @return list of log entries.
   */
  public Vector getLogEntryList();

  /**
   * Get last log entry.
   * @return ast log entry.
   */
  public MessageEntry getLastLogEntry();

  /**
   * Create log entry object.
   * @return newly created log entry.
   */
  public MessageEntry createLogEntry();

  /**
   * Set style sheet string.
   * @param styleSheetString a style sheet string.
   */
  public void setStyleSheetString(String styleSheetString);

  /**
   * Get start element string.
   * @param namespaceName a namespace prefix.
   * @return start element string.
   */
  public String getStartXMLString(String namespaceName);

  /**
   * Get end element string.
   * @param namespaceName a namespace prefix.
   * @return end element string. 
   */
  public String getEndXMLString(String namespaceName);
}
