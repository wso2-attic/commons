/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.report;

import org.eclipse.wst.wsi.internal.core.report.Report;
import org.eclipse.wst.wsi.internal.core.report.impl.ReportImpl;

/**
 * Null report.
 *
 * @author Jeffrey Liu (jeffliu@ca.ibm.com)
 */
public class NullReportImpl extends ReportImpl implements Report
{
  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getStartXMLString(java.lang.String)
   */
  public String getStartXMLString(String namespaceName)
  {
    return "";
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getEndXMLString(java.lang.String)
   */
  public String getEndXMLString(String namespaceName)
  {
    return "";
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getErrorXMLString(java.lang.String, java.lang.String)
   */
  public String getErrorXMLString(String namespaceName, String errorDetail)
  {
    return "";
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(java.lang.String)
   */
 public String toXMLString(String namespaceName)
  {
    return "";
  }
}