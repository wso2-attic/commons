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

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.analyzer.Analyzer;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;

/**
 * Report context.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class ReportContext
{
  /**
   * Report title.
   */
  protected String reportTitle = WSIConstants.DEFAULT_REPORT_TITLE;

  /**
   * Profile definition.
   */
  protected ProfileAssertions profileAssertions;

  /**
   * Analyzer.
   */
  protected Analyzer analyzer;

  /**
   * Create report context.
   * @param reportTitle report title.
   * @param profileAssertions profile test assertions.
   * @param analyzer an Analyzer object.
  */
  public ReportContext(
    String reportTitle,
    ProfileAssertions profileAssertions,
    Analyzer analyzer)
  {
    this.reportTitle = reportTitle;
    this.profileAssertions = profileAssertions;
    this.analyzer = analyzer;
  }

  /**
   * Get report title.
   * @return report title.
   * @see #setReportTitle
   */
  public String getReportTitle()
  {
    return this.reportTitle;
  }

  /**
   * Set report title.
   * @param reportTitle report title.
   * @see #getReportTitle
   */
  public void setReportTitle(String reportTitle)
  {
    this.reportTitle = reportTitle;
  }

  /**
   * Get profile definition.
   * @return profile definition.
   * @see #setProfileAssertions
   */
  public ProfileAssertions getProfileAssertions()
  {
    return this.profileAssertions;
  }

  /**
   * Set profile definition.
   * @param profileAssertions profile definition.
   * @see #getProfileAssertions
   */
  public void setProfileAssertions(ProfileAssertions profileAssertions)
  {
    this.profileAssertions = profileAssertions;
  }

  /**
   * Get analyzer.
   * @return analyzer.
   * @see #setAnalyzer
   */
  public Analyzer getAnalyzer()
  {
    return this.analyzer;
  }

  /**
   * Set analyzer.
   * @param analyzer an Analyzer object.
   * @see #getAnalyzer
   * 
   */
  public void setAnalyzer(Analyzer analyzer)
  {
    this.analyzer = analyzer;
  }
}
