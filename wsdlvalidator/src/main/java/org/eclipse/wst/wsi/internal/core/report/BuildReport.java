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

import org.eclipse.wst.wsi.internal.core.WSIException;

/**
 * This interface is used to build the Profile report.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface BuildReport
{
  /**
   * Prereq type - entry.
   */
  public static final String PREREQ_TYPE_ENTRY = "entry";

  /**
   * Prereq type - entry container.
   */
  public static final String PREREQ_TYPE_ENTRY_CONTAINER = "entryContainer";

  /**
   * Start current artifact.
   * @param reportArtifact current report artifact.
   * @throws WSIException the base WS-I exception.
   */
  public void setCurrentArtifact(ReportArtifact reportArtifact)
    throws WSIException;

  /**
   * Add artifact refererence.
   * @param artifactReference artifact reference.
   * @throws WSIException the base WS-I exception.
   */
  public void addArtifactReference(ArtifactReference artifactReference)
    throws WSIException;

  /**
   * End current artifact.
   * @throws WSIException the base WS-I exception.
   */
  public void endCurrentArtifact() throws WSIException;

  /**
   * Start current entry.
   * @param entry the current entry.
   * @throws WSIException the base WS-I exception.
   */
  public void setCurrentEntry(Entry entry) throws WSIException;

  /**
   * Start current entry.
   * @param entry the current entry.
   * @throws WSIException the base WS-I exception.
   */
  public void setCurrentEnvelopeEntry(Entry entry) throws WSIException;
  /**
   * End current entry.
   * @throws WSIException the base WS-I exception.
   */
  public void endCurrentEntry() throws WSIException;

  /**
   * Add test assertion result.
   * @param assertionResult test assertion result.
   * @throws WSIException the base WS-I exception.
   */
  public void addAssertionResult(AssertionResult assertionResult)
    throws WSIException;

  /**
   * Get the assertion result for a specific test assertion.
   * @param assertionId the test assertion ID.
   * @return  the assertion result for the specified test assertion ID.
   *          If an assertion result was not found, then null is returned.
   */
  public AssertionResult getAssertionResult(String assertionId);

  /**
   * Create assertion result.
   * @return newly created assertion result.
   */
  public AssertionResult createAssertionResult();

  /**
   * Create entry.
   * @return newly created entry.
   */
  public Entry createEntry();

  /**
   * Create entry container.
   * @return newly created entry container.
   */
  public EntryContainer createEntryContainer();

  /**
   * Create artifact.
   * @return newly created artifact.
   */
  public ReportArtifact createArtifact();

  /**
   * Create failure detail.
   * @return newly created failure detail.
   */
  public FailureDetail createFailureDetail();

  /**
   * Set type of prereq processing - either entry or document.
   * @param prereqType type of prereq processing - either entry or document.
   */
  public void setPrereqType(String prereqType);
}
