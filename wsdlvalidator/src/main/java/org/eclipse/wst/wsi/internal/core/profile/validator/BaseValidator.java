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
package org.eclipse.wst.wsi.internal.core.profile.validator;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AnalyzerConfig;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.w3c.dom.Document;

/**
 * The base interface for the profile validator.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface BaseValidator
{
  /**
   * Set all test assertions for an artifact to missingInput.
   * @throws WSIException if there is any problems while processing.
   */
  public void setAllMissingInput() throws WSIException;

  /**
   * Cleanup after processing all of the test assertions for an artifact.
   * @throws WSIException if there is any problems during cleanup.
   */
  public void cleanup() throws WSIException;

  /**
     * Parse XML document and validate with a schema document.
     * @param urlString XML document location.
     * @param baseURI a base url to assist in locating the XML document.
     * @param schema the related XML schema.
     * @return XML document.
     * @throws WSIException if there are any problems while parsing or 
     *         validating the XML document.
     */
  public Document parseXMLDocumentURL(
    String urlString,
    String baseURI,
    String schema)
    throws WSIException;

  /**
   * Initiailize validation test procedure.
   * @param analyzerContext the analyzerContext.
   * @param assertions      the assertions
   * @param reportArtifact  the report artifact.
   * @param analyzerConfig  the analyzerConfig
   * @param reporter        the reporter which is used to add errors to the
   *                        conformance report
   * @throws WSIException   if message validator could not be initialized.
   */
  public void init(
    AnalyzerContext analyzerContext,
    ProfileAssertions assertions,
    ReportArtifact reportArtifact,
    AnalyzerConfig analyzerConfig,
    Reporter reporter)
    throws WSIException;

  /**
   * Determines if this validator should be run (depending on the analyzer
   * config)
   * @return boolean  true if validator should be run, false if not
   */
  public boolean runTests();

  /**
   * Run the validator
   * @throws WSIException if an unexpected error occurs during validation
   */
  public void validateArtifact() throws WSIException;
  
  /**
   * Get the artifact type that this validator applies to.
   * @return the artifact type (a String)
   */
  public String getArtifactType();

  /**
   * Get the collection of entry types that this validator applies to.
   * @return an array of entry types (Strings)
   */
  public String[] getEntryTypes();
}
