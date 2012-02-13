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
import org.eclipse.wst.wsi.internal.core.analyzer.config.UDDIReference;
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.report.Reporter;

/**
 * The WSDL validator will verify that the WSDL and associated XML schema definitions
 * are in conformance with the profile.
 *
 * @version 1.0.1
 * @author Peter Brittenham
 */
public interface UDDIValidator extends BaseValidator
{
  /** Discovery artifact type */
  public static final String TYPE_DISCOVERY = "discovery";

  /** Binding template discovery entry type. */
  public static final String TYPE_DISCOVERY_BINDINGTEMPLATE = "bindingTemplate";

  /** tModel discovery entry type. */
  public static final String TYPE_DISCOVERY_TMODEL = "tModel";

  /**
   * Initiailize validation test procedure.
   * @param analyzerContext the analyzerContext.
   * @param artifact        an profile artifact.
   * @param reportArtifact  the report artifact.
   * @param uddiReference   a UDDI reference.
   * @param reporter        a Reporter object.
   * @throws WSIException if UDDI validator could not be initialized.
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
    UDDIReference uddiReference,
    Reporter reporter)
    throws WSIException;

  /**
   * Validate the UDDI based service description.
   * @return If the UDDI entries are valid, then the value returned 
   *         is the URL for the WSDL document.
   * @throws WSIException WSIException if an unexpected error occurred
   *         while processing the UDDIentries.
   * @deprecated -- use validateArtifact()
   */
  public String validate() throws WSIException;
}
