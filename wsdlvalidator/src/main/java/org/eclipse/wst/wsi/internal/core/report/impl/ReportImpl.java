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
import java.io.StringWriter;
import java.util.List;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.report.ArtifactReference;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.report.EntryContainer;
import org.eclipse.wst.wsi.internal.core.report.FailureDetail;
import org.eclipse.wst.wsi.internal.core.report.Report;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.ReportContext;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;

/**
 * WS-I conformance test report.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class ReportImpl implements Report
{
  /**
   * Report filename.
   */
  protected String filename = null;

  /**
   * Report context.
   */
  protected ReportContext reportContext = null;

  /**
   * Log file.
   */
  protected Log log = null;

  /**
   * Summary all passed flag.
   */
  protected boolean allPassed = true;

  /**
   * Test coverage.
   */
  //protected TestCoverage testCoverage;

  /**
   * Artifact list.
   */
  protected Vector artifactList = new Vector();

  /**
   * Entry list.
   */
  protected Vector entryList = new Vector();

  /**
   * Current artifact.
   */
  protected ReportArtifact currentArtifact = null;

  /**
   * Current entry.
   */
  protected Entry currentEntry = null;

  /**
   * Prereq type.
   */
  protected String prereqType;

  /**
   * Create a new conformance report.
   */
  public ReportImpl()
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.WSIDocument#getLocation()
   */
  public String getLocation()
  {
    // Get report URI
    //return reportURI;
    return this.filename;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.WSIDocument#setLocation(java.lang.String)
   */
  public void setLocation(String reportURI)
  {
    this.filename = reportURI;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getReportContext()
   */
  public ReportContext getReportContext()
  {
    return this.reportContext;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#setReportContext(org.wsi.test.report.ReportContext)
   */
  public void setReportContext(ReportContext reportContext)
  {
    // Save input references
    this.reportContext = reportContext;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getCurrentArtifact()
   */
  public ReportArtifact getCurrentArtifact()
  {
    // Return artifact
    return this.currentArtifact;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#setCurrentArtifact(org.wsi.test.report.ReportArtifact)
   */
  public void setCurrentArtifact(ReportArtifact artifact) throws WSIException
  {
    // Save reference to artifact
    artifactList.add(artifact);

    // Set as current artifact
    this.currentArtifact = artifact;

    // Set default prereq processing
    this.prereqType = PREREQ_TYPE_ENTRY;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#endCurrentArtifact()
   */
  public void endCurrentArtifact() throws WSIException
  {
    // Remove current artifact
    this.currentArtifact = null;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#addArtifactReference(org.wsi.test.report.ArtifactReference)
   */
  public void addArtifactReference(ArtifactReference artifactReference)
    throws WSIException
  {
    // ADD: How should this be saved?  Should it be added to the current artifact?
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getCurrentEntry()
   */
  public Entry getCurrentEntry()
  {
    // Return entry
    return this.currentEntry;
  }
  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getCurrentEntry()
   */
  public void setCurrentEnvelopeEntry(Entry entry) throws WSIException
  {
    
  }
  
  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#setCurrentEntry(org.wsi.test.report.Entry)
   */
  public void setCurrentEntry(Entry entry) throws WSIException
  {
    // Save reference to entry
    entryList.add(entry);

    // Set as current entry
    this.currentEntry = entry;
  }

  /**
   * Method getEntries.
   * @return entry list.
   */
  public List getEntries()
  {
    return entryList;
  }

  /**
   * Method getArtifacts.
   * @return artifacts.
   */
  public List getArtifacts()
  {
    return artifactList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#endCurrentEntry()
   */
  public void endCurrentEntry() throws WSIException
  {
    // Remove current entry
    this.currentEntry = null;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#addAssertionResult(org.wsi.test.report.AssertionResult)
   */
  public void addAssertionResult(AssertionResult assertionResult)
  {
    // ADD: Need to keep just a summary 

    // Add result to current entry
    if (currentEntry != null)
    {
      currentEntry.addAssertionResult(assertionResult);
    }

    // If the result was not passed, then set summary flag
    if (assertionResult.getResult().equals(AssertionResult.RESULT_FAILED))
    {
      allPassed = false;
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#getAssertionResult(java.lang.String)
   */
  public AssertionResult getAssertionResult(String assertionId)
  {
    AssertionResult result = null;

    // Determine where to get the assertion result from 
    if (this.prereqType.equals(PREREQ_TYPE_ENTRY))
      result = currentEntry.getAssertionResult(assertionId);
    else
      result = currentEntry.getEntryContainer().getAssertionResult(assertionId);

    // the assertion result is not found at the current entry, going through all the entries
    if (result == null)
    {
      for (int i = 0; i < entryList.size(); i ++)
      {
        Entry entry = (Entry) entryList.get(i);
        if ((result = entry.getAssertionResult(assertionId)) != null)
          break;
      }
    }
    return result;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createAssertionResult()
   */
  public AssertionResult createAssertionResult()
  {
    AssertionResult assertionResult = new AssertionResultImpl();
    assertionResult.setAssertionResultsOption(
      reportContext
        .getAnalyzer()
        .getAnalyzerConfig()
        .getAssertionResultsOption());
    return assertionResult;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createEntry()
   */
  public Entry createEntry()
  {
    Entry entry = new EntryImpl();
    //entry.setAssertionResultsOption(reportContext.getAnalyzer().getAnalyzerConfig().getAssertionResultsOption());
    return entry;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createEntryContainer()
   */
  public EntryContainer createEntryContainer()
  {
    EntryContainer entryContainer = new EntryContainerImpl();
    return entryContainer;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createArtifact()
   */
  public ReportArtifact createArtifact()
  {
    ReportArtifact reportArtifact = new ReportArtifactImpl();
    return reportArtifact;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#createFailureDetail()
   */
  public FailureDetail createFailureDetail()
  {
    FailureDetail failureDetail = new FailureDetailImpl();
    return failureDetail;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getSummaryResult()
   */
  public String getSummaryResult()
  {
    return (
      allPassed
        ? AssertionResult.RESULT_PASSED
        : AssertionResult.RESULT_FAILED);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.BuildReport#setPrereqType(java.lang.String)
   */
  public void setPrereqType(String prereqType)
  {
    this.prereqType = prereqType;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    // ADD: 
    return "Report:  ";
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getStartXMLString(java.lang.String)
   */
  public String getStartXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Create report element 
    pw.println(WSIConstants.XML_DECL);
    pw.println(
      reportContext
        .getAnalyzer()
        .getAnalyzerConfig()
        .getAddStyleSheet()
        .getStyleSheetString());

    // Add XML comment
    String comment;
    if ((comment = TestUtils.getXMLComment()) != null)
      pw.print(comment);

    // report
    pw.print(
      "<"
        + nsName
        + ELEM_NAME
        + " "
        + WSIConstants.ATTR_NAME
        + "=\""
        + reportContext.getReportTitle()
        + "\"");
    pw.println(
      "    "
        + WSIConstants.ATTR_TIMESTAMP
        + "=\""
        + Utils.getTimestamp()
        + "\"");
    pw.println("    xmlns=\"" + WSIConstants.NS_URI_WSI_REPORT + "\"");
    pw.println(
      "    xmlns:"
        + WSIConstants.NS_NAME_WSI_REPORT
        + "=\""
        + WSIConstants.NS_URI_WSI_REPORT
        + "\"");
    pw.println(
      "    xmlns:"
        + WSIConstants.NS_NAME_WSI_LOG
        + "=\""
        + WSIConstants.NS_URI_WSI_LOG
        + "\"");
    //pw.println("    xmlns:" + WSIConstants.NS_NAME_WSI_COMMON + "=\"" + WSIConstants.NS_URI_WSI_COMMON + "\"");
    pw.println(
      "    xmlns:"
        + WSIConstants.NS_NAME_WSI_ANALYZER_CONFIG
        + "=\""
        + WSIConstants.NS_URI_WSI_ANALYZER_CONFIG
        + "\"");
    pw.println(
      "    xmlns:"
        + WSIConstants.NS_NAME_WSI_MONITOR_CONFIG
        + "=\""
        + WSIConstants.NS_URI_WSI_MONITOR_CONFIG
        + "\"");
    pw.println(
      "    xmlns:"
        + WSIConstants.NS_NAME_WSI_ASSERTIONS
        + "=\""
        + WSIConstants.NS_URI_WSI_ASSERTIONS
        + "\"");
    pw.println(
      "    xmlns:"
        + WSIConstants.NS_NAME_XSI
        + "=\""
        + WSIConstants.NS_URI_XSI
        + "\">");

    // Add  analyzer tool info
    pw.print(reportContext.getAnalyzer().toXMLString(nsName));

    // Return XML string
    return sw.toString();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getEndXMLString(java.lang.String)
   */
  public String getEndXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Build summary
    pw.print("  <" + nsName + WSIConstants.ELEM_SUMMARY + " ");
    pw.println(WSIConstants.ATTR_RESULT + "=\"" + getSummaryResult() + "\">");

    // ADD:  Detail summary
    /*
    pw.print("    <" + nsName + WSIConstants.ELEM_ARTIFACT_SUMMARY + " ");
    pw.println(WSIConstants.ATTR_RESULT + "=\"xxxx\">");
    
    pw.println("    </" + nsName + WSIConstants.ELEM_ARTIFACT_SUMMARY + ">");
    */

    // End element 
    pw.println("  </" + nsName + WSIConstants.ELEM_SUMMARY + ">");

    // End report element 
    pw.println("</" + nsName + ELEM_NAME + ">");

    // Return XML string
    return sw.toString();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.Report#getErrorXMLString(java.lang.String, java.lang.String)
   */
  public String getErrorXMLString(String namespaceName, String errorDetail)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Build analyzer failure
    pw.println("  <" + nsName + WSIConstants.ELEM_ANALYZER_FAILURE + ">");

    // Add error detail
    pw.print("    <" + nsName + WSIConstants.ELEM_FAILURE_DETAIL + ">");
    pw.print(XMLUtils.xmlEscapedString(errorDetail));
    pw.println("    </" + nsName + WSIConstants.ELEM_FAILURE_DETAIL + ">");

    // End report element 
    pw.println("</" + nsName + WSIConstants.ELEM_ANALYZER_FAILURE + ">");

    // End report element 
    pw.println("</" + nsName + ELEM_NAME + ">");

    // Return XML string
    return sw.toString();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(java.lang.String)
   */
 public String toXMLString(String namespaceName)
  {
    return getStartXMLString(namespaceName) + getEndXMLString(namespaceName);
  }
}
