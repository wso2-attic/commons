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
package org.eclipse.wst.wsi.internal.core.report;

import java.io.Writer;

import org.eclipse.wst.wsi.internal.core.document.DocumentWriter;

/**
 * Defines the interface used to write the report documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface ReportWriter extends DocumentWriter
{
  /**
   * Write the entire contents of the document using the location
   * that was previously set using the setLocation method.
   * @param report a report.
   * @throws IllegalStateException if this operation has been invoked 
   *         at an illegal or inappropriate time
   */
  public void write(Report report) throws IllegalStateException;

  /**
   * Write the entire contents of the document.
   * @param report a report.
   * @param writer a Writer object.
   */
  public void write(Report report, Writer writer);
}
