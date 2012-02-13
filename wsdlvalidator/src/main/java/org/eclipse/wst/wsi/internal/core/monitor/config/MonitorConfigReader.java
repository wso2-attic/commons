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

import java.io.Reader;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.DocumentReader;
import org.eclipse.wst.wsi.internal.core.util.MessageList;

/**
 * Defines the interface used to read the monitor config documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface MonitorConfigReader extends DocumentReader
{
  /**
   * Initialize monitor config.
   * @param messageList a MessageList object.
   */
  public void init(MessageList messageList);

  /**
   * Read the monitor config file.
   * @param monitorConfigURI the monitor config file location.
   * @return a MonitorConfig object
   * @throws WSIException if problems occur while reading monitor config file.
   */
  public MonitorConfig readMonitorConfig(String monitorConfigURI)
    throws WSIException;

  /**
   * Read the monitor config file.
   * @param reader a Reader object.
   * @return a MonitorConfig object
   * @throws WSIException if problems occur while reading monitor config file.
   */
  public MonitorConfig readMonitorConfig(Reader reader) throws WSIException;
}
