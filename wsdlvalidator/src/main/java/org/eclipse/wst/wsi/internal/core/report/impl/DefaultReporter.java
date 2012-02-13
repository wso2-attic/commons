/*******************************************************************************
 * Copyright (c) 2002-2003 IBM Corporation, Parasoft and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   Parasoft - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.report.impl;

import java.io.StringReader;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultType;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.report.ArtifactReference;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.report.EntryContainer;
import org.eclipse.wst.wsi.internal.core.report.FailureDetail;
import org.eclipse.wst.wsi.internal.core.report.Report;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.ReportWriter;
import org.eclipse.wst.wsi.internal.core.report.Reporter;

/**
 * Base class for reporting errors from the analyzer.
 * Extend this class for specific types of reporting, such as reporting
 * in different formats, reporting to files, reporting to a GUI.
 *
 * @version 1.0.1
 * @author Jim Clune
 * @author Peter Brittenham
 */
public class DefaultReporter implements Reporter
{
  /**
   * Conformance report.
   */
  protected Report report;

  /**
   * Document writer.
   */
  protected ReportWriter reportWriter;

  /**
   * Analyzer config.
   */
  protected AnalyzerConfig analyzerConfig;

  /**
   * Assertoin result type.
   */
  protected AssertionResultType assertionResultType;

  /**
   * Create result reporter.
   * @param report a Report object.
   * @param reportWriter a ReportWriterObject.
   */
  public DefaultReporter(Report report, ReportWriter reportWriter)
  {
    this.report = report;
    this.reportWriter = reportWriter;

    // ADD: Verify that writer set in reportWriter

    // Get report context
    this.analyzerConfig =
      report.getReportContext().getAnalyzer().getAnalyzerConfig();
    this.assertionResultType =
      this.analyzerConfig.getAssertionResultsOption().getAssertionResultType();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#setCurrentArtifact(org.wsi.test.report.ReportArtifact)
   */
  public void setCurrentArtifact(ReportArtifact reportArtifact)
    throws WSIException
  {
    report.setCurrentArtifact(reportArtifact);
    reportWriter.write(new StringReader(reportArtifact.getStartXMLString("")));
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#endCurrentArtifact()
   */
  public void endCurrentArtifact() throws WSIException
  {
    reportWriter.write(
      new StringReader(report.getCurrentArtifact().getEndXMLString("")));
    report.endCurrentArtifact();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#addArtifactReference(org.wsi.test.report.ArtifactReference)
   */
  public void addArtifactReference(ArtifactReference artifactReference)
    throws WSIException
  {
    reportWriter.write(new StringReader(artifactReference.toXMLString("")));

    // Add artifact reference to report
    report.addArtifactReference(artifactReference);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#setCurrentEntry(org.wsi.test.report.Entry)
   */
  public void setCurrentEntry(Entry entry) throws WSIException
  {
    report.setCurrentEntry(entry);
    reportWriter.write(
      new StringReader(
        entry.getStartXMLString(
          "",
          this
            .report
            .getReportContext()
            .getAnalyzer()
            .getAnalyzerConfig()
            .getAssertionResultsOption()
            .getShowMessageEntry())));
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#setCurrentEntry(org.wsi.test.report.Entry)
   */
  public void setCurrentEnvelopeEntry(Entry entry) throws WSIException
  {
    report.setCurrentEntry(entry);
    reportWriter.write(
      new StringReader(
        entry.getStartXMLString(
          "",
          this
            .report
            .getReportContext()
            .getAnalyzer()
            .getAnalyzerConfig()
            .getAssertionResultsOption()
            .getShowMessageEntry(),
		  true)));
  }
/* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#endCurrentEntry()
   */
  public void endCurrentEntry() throws WSIException
  {
    reportWriter.write(
      new StringReader(report.getCurrentEntry().getEndXMLString("")));
    report.endCurrentEntry();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#addAssertionResult(org.wsi.test.report.AssertionResult)
   */
  public void addAssertionResult(AssertionResult assertionResult)
    throws WSIException
  {
    // Based on the config options, write out assertion result
    if ((assertionResultType.isAll())
      || ((assertionResultType.isFailedOnly())
        && (assertionResult.getResult().equals(AssertionResult.RESULT_FAILED)))
      || ((assertionResultType.isNotPassed())
        && (!assertionResult.getResult().equals(AssertionResult.RESULT_PASSED)))
	      || ((assertionResultType.isNotInfo())
	            && (!assertionResult.getAssertion().getType().equals(TestAssertion.TYPE_INFORMATIONAL))))
    {
      reportWriter.write(new StringReader(assertionResult.toXMLString("")));
    }

    // Add assertion to report
    report.addAssertionResult(assertionResult);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#getAssertionResult(java.lang.String)
   */
  public AssertionResult getAssertionResult(String assertionId)
  {
    // Get the assertion result from the current assertion target
    return report.getAssertionResult(assertionId);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.WriteReport#startReport()
   */
  public void startReport() throws WSIException
  {
    // Write out start of report
    reportWriter.write(new StringReader(report.getStartXMLString("")));
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.WriteReport#finishReport()
   */
  public void finishReport() throws WSIException
  {
    // End the report file
    reportWriter.write(new StringReader(report.getEndXMLString("")));
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.WriteReport#finishReportWithError(java.lang.String)
   */
  public void finishReportWithError(String errorDetail) throws WSIException
  {
    // Check if entry or artifact need to be closed
    if (this.report.getCurrentEntry() != null)
      endCurrentEntry();
    if (this.report.getCurrentArtifact() != null)
      endCurrentArtifact();

    // End the report file
    reportWriter.write(
      new StringReader(report.getErrorXMLString("", errorDetail)));
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createAssertionResult()
   */
  public AssertionResult createAssertionResult()
  {
    return report.createAssertionResult();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createArtifact()
   */
  public ReportArtifact createArtifact()
  {
    return report.createArtifact();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createEntry()
   */
  public Entry createEntry()
  {
    return report.createEntry();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createEntryContainer()
   */
  public EntryContainer createEntryContainer()
  {
    return report.createEntryContainer();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createFailureDetail()
   */
  public FailureDetail createFailureDetail()
  {
    return report.createFailureDetail();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.WriteReport#getReport()
   */
  public Report getReport()
  {
    return this.report;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#setPrereqType(java.lang.String)
   */
  public void setPrereqType(String prereqType)
  {
    this.report.setPrereqType(prereqType);
  }
}
