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
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.Reporter;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;

/**
 * Interface definition for envelope validation test procedure.
 *
 * @version 1.0
 */
public interface EnvelopeValidator extends LogValidator
{

  /**
   * Initiailize validation test procedure.
   * @param analyzerContext the analyzerContext.
   * @param artifact        an profile artifact.
   * @param reportArtifact  the report artifact.
   * @param wsdlDocument the Web service definition
   * @param reporter the reporter which is used to add errors to the conformance report
   * @throws WSIException if message validator could not be initialized.
   * @deprecated -- use init(AnalyzerContext analyzerContext,
   *                         ProfileAssertions assertions,
   *                         ReportArtifact reportArtifact,
   *                         AnalyzerConfig analyzerConfig,
   *                         Reporter reporter)
   */
  public void init(
    AnalyzerContext analyzerContext,
    ProfileArtifact artifact,
    ReportArtifact reportArtifact,
    WSDLDocument wsdlDocument,
    Reporter reporter)
    throws WSIException;
  
  /** Envelope artifact type */
  public static final String TYPE_ENVELOPE = "envelope";

  /** Request envelope entry type. */
  public static final String TYPE_ENVELOPE_REQUEST = "requestEnvelope";

  /** Response envelope entry type. */
  public static final String TYPE_ENVELOPE_RESPONSE = "responseEnvelope";

  /** Any envelope entry type. */
  public static final String TYPE_ENVELOPE_ANY = "anyEnvelope";
}