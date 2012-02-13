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

import java.io.Writer;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.DocumentWriter;

/**
 * Defines the interface used to write the Log file.
 * 
 * @version 1.0.1
 * @author Neil Delima (nddelima@ca.ibm.com)
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface LogWriter extends DocumentWriter
{
  /**
   * Write the entire contents of the document using the location
   * that was previously set using the setLocation method.
   * @param log the Log object.
   * @throws IllegalStateException if this operation has been invoked 
   *         at an illegal or inappropriate time
   * @throws WSIException if problems occur during the writing of 
   *         the log.
   */
  public void write(Log log) throws IllegalStateException, WSIException;

  /**
   * Write the entire contents of the document.
   * @param log the Log object.
   * @param writer a Writer object.
   * @throws WSIException if problems occur during the writing of 
   *         the log.
   */
  public void write(Log log, Writer writer) throws WSIException;
}
