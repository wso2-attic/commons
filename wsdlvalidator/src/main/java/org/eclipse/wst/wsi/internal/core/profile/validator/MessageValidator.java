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
 * Interface definition for message validation test procedure.
 *
 * @version 1.0.1
 * @author Peter Brittenham
 * @author Graham Turrell (gturrell@uk.ibm.com)
 */
public interface MessageValidator extends LogValidator
{
  /**
   * HTTP 1.0 version indicator.
   */
  public static final String HTTP_VERSION_1_0 = "HTTP/1.0";

  /**
   * HTTP 1.1 version indicator.
   */
  public static final String HTTP_VERSION_1_1 = "HTTP/1.1";

  /**
   * HTTP POST message indicator.
   */
  public static final String HTTP_POST = "POST";

  /** Message artifact type */
  public static final String TYPE_MESSAGE = "message";

  /** Request message entry type. */
  public static final String TYPE_MESSAGE_REQUEST = "requestMessage";

  /** Response message entry type. */
  public static final String TYPE_MESSAGE_RESPONSE = "responseMessage";

  /** Any message entry type. */
  public static final String TYPE_MESSAGE_ANY = "anyMessage";

  /** Mime part message entry type. */
  public static final String TYPE_MIME_PART = "part";

  /** Mime root part message entry type. */
  public static final String TYPE_MIME_ROOT_PART = "root-part";

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
}
