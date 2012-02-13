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

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.WSIDocument;

/**
 * Conformance test report.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface Report extends WSIDocument, BuildReport
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_REPORT;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_REPORT, ELEM_NAME);

  /**
   * Get report context.
   * @return report context.
   * @see #setReportContext
   */
  public ReportContext getReportContext();

  /**
   * Set report context.
   * @param reportContext report context.
   * @see #getReportContext
   */
  public void setReportContext(ReportContext reportContext);

  /**
   * Get current artifact.
   * @return current artifact.
   */
  public ReportArtifact getCurrentArtifact();

  /**
   * Get current entry.
   * @return current entry.
   */
  public Entry getCurrentEntry();

  /**
   * Method getEntries.
   * @return entry list.
   */
  public List getEntries();

  /**
   * Method getArtifacts.
   * @return artifacts.
   */
  public List getArtifacts();

  /**
   * Get start element string.
   * @param namespaceName namespace prefix
   * @return start element string.
   */
  public String getStartXMLString(String namespaceName);

  /**
   * Get end element string.
   * @param namespaceName namespace prefix
   * @return end element string. 
   */
  public String getEndXMLString(String namespaceName);

  /**
   * Get error XML string.
   * @param namespaceName namespace prefix.
   * @param errorDetail an error detail.
   * @return error XML string. 
   */
  public String getErrorXMLString(String namespaceName, String errorDetail);

  /**
   * Get summary result. 
   * @return summary result.
   */
  public String getSummaryResult();

}
