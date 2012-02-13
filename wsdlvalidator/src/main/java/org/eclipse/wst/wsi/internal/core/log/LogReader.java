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

import java.io.Reader;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.DocumentReader;

/**
 * Defines the interface used to read the Log file.
 * 
 * @version 1.0.1
 * @author Neil Delima (nddelima@ca.ibm.com)
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface LogReader extends DocumentReader
{
  /**
   * Start reading log entries from a specified URI location using the specified callback.
   * 
   * @param logURI the location of the log file.
   * @param logEntryHandler the function that will be called to process each log entry.
   * @throws WSIException if problem reading log.
   */
  public void readLog(String logURI, MessageEntryHandler logEntryHandler)
    throws WSIException;

  /**
   * Start reading log entries from a reader using the specified callback.
   * 
   * @param reader the source for the log file.
   * @param logEntryHandler the function that will be called to process each log entry.
   * @throws WSIException if problem reading log.
   */
  public void readLog(Reader reader, MessageEntryHandler logEntryHandler)
    throws WSIException;

  // ADD: Do we need to add functions that will read the entire log file?
}
