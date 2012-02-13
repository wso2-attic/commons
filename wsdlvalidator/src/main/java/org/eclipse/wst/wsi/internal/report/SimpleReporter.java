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

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.report.ArtifactReference;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.report.Report;
import org.eclipse.wst.wsi.internal.core.report.ReportWriter;
import org.eclipse.wst.wsi.internal.core.report.impl.DefaultReporter;

/**
 * This reporter does not write artifacts and assertions
 */
public class SimpleReporter extends DefaultReporter
{
  public SimpleReporter(Report report, ReportWriter reportWriter)
  {
    super(report, reportWriter);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#addArtifactReference(org.wsi.test.report.ArtifactReference)
   */
  public void addArtifactReference(ArtifactReference artifactReference)
    throws WSIException
  {
    report.addArtifactReference(artifactReference);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#addAssertionResult(org.wsi.test.report.AssertionResult)
   */
  public void addAssertionResult(AssertionResult assertionResult)
    throws WSIException
  {
    report.addAssertionResult(assertionResult);
  }
}