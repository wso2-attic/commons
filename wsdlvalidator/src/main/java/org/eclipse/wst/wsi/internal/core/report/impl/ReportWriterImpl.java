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
package org.eclipse.wst.wsi.internal.core.report.impl;

import java.io.PrintWriter;
import java.io.Writer;

import org.eclipse.wst.wsi.internal.core.document.impl.DocumentWriterImpl;
import org.eclipse.wst.wsi.internal.core.report.Report;
import org.eclipse.wst.wsi.internal.core.report.ReportWriter;

/**
 * Defines the interface used to write the Conformance XML documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class ReportWriterImpl
  extends DocumentWriterImpl
  implements ReportWriter
{
  /**
   * Report writer.
   */
  public ReportWriterImpl()
  {
  }

  /**
   * Write the entire contents of the document using the writer
   * that was previously set using the setWriter method.
   */
  public void write(Report report) throws IllegalStateException
  {
    // If writer was not set previously, then throw exception
    if (writer == null)
    {
      throw new IllegalStateException("Report writer must be set before writing report.");
    }

    // Write report
    write(report, this.writer);
  }

  /**
   * Write the entire contents of the document.
   */
  public void write(Report report, Writer writer)
  {
    // Create print writer
    new PrintWriter(writer);

    // ADD: Write out complete report
  }
}
