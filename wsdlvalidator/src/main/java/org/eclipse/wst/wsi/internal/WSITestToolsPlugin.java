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
package org.eclipse.wst.wsi.internal;

import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.wst.wsi.internal.core.profile.validator.BaseValidator;

/**
 * The WS-I test tools plugin.
 * 
 * @author lauzond
 */

public class WSITestToolsPlugin extends Plugin
{
  private static Plugin instance;
  protected final String PLUGIN_PROPERTIES = "wsivalidate";
  protected static final String PLUGIN_ID = "org.eclipse.wst.wsi";
  protected static final String VALIDATOR_EXT_ID = PLUGIN_ID + ".wsivalidator";
  protected static final String TAD_VERSION_EXT_ID = PLUGIN_ID + ".tads";
  protected static final String REPORT_ARTIFACT_TYPES_EXT_ID = PLUGIN_ID + ".reportArtifactTypes";
  protected static final String ATT_CLASS = "class";
  protected static final String ATT_TAD_NAME = "name";
  protected static final String ATT_TAD_VERSION = "version";
  protected static final String ATT_ARTIFACT_TYPE = "artifactType";

  /* Holds validators read from the platform registry.  Lazy initialized in
   * computeValidators().  */
  private BaseValidator validators[];

  /* Holds arrays containing information about Test Assertion document (TAD)
   * versions read from the platform registry.  Each array has two elements:
   *   0: the TAD name
   *   1: the TAD version string
   * Lazy initialized in computeTADVersions().  */
  private String tadVersions[][];

  /* Holds artifact type names for reporting read from the platform registry.
   * Lazy initialized in getAllReportArtifactTypes().  */
  private String reportArtifactTypes[];

  /**
   * Constructor for wsiTestToolsPlugin.
   */
  public WSITestToolsPlugin()
  {
    super();
    instance = this;

    // set the current directory
   WSITestToolsProperties.setEclipseContext(true);
   //resourcebundle = ResourceBundle.getBundle(PLUGIN_PROPERTIES);
  }

  /**
   * Method getInstance.
   * @return AbstractUIPlugin
   */
  public static Plugin getInstance()
  {
    if (instance == null) {
        instance = new WSITestToolsPlugin();
    }
    return instance;
  }

  public void setBaseValidators(BaseValidator validators[]) {
      this.validators = validators;
  }

   /**
   * Method getPlugin.
   * @return WSIToolsUtilPlugin
   */
  public static WSITestToolsPlugin getPlugin()
  {
    return (WSITestToolsPlugin) instance;
  }

  /* Computes the list of validators by scanning the platform registry. */
  private BaseValidator[] computeValidators() {
      IExtensionRegistry registry = Platform.getExtensionRegistry();
      IExtensionPoint extensionPoint = registry.getExtensionPoint(
              VALIDATOR_EXT_ID);
      IExtension[] extensions;
      if (extensionPoint != null) {
          extensions = extensionPoint.getExtensions();
      } else {
          extensions = new IExtension[0];
      }
      ArrayList results = new ArrayList();
      for (int i = 0; i < extensions.length; i++) {
          /* Only one validator per extension point */
          IConfigurationElement validatorElement = extensions[i].
                  getConfigurationElements()[0];
          BaseValidator validator = null;
          try {
              validator = (BaseValidator) validatorElement.
                      createExecutableExtension(ATT_CLASS);
          } catch (Throwable e) {
              e.printStackTrace();
          }
          results.add(validator);
      }
      return (BaseValidator[]) results.toArray(new BaseValidator[0]);
  }

  /**
   * Find all validators found in the platform registry extension points
   * org.eclipse.wst.wsi.validator.
   * @return an array containing these validators
   */
  public BaseValidator[] getBaseValidators() 
  {
	if (validators == null)
	  validators = computeValidators();
    return validators;  
  }

  /**
   * Scan all validators found in the platform registry for supported artifact
   * types.
   * @return an array of artifact type names (Strings)
   */
  public String[] getArtifactTypes() {
      if (validators == null)
         getBaseValidators();
      String artifactTypes[] = new String[validators.length];
      
      for (int i = 0; i < validators.length; i++)
          artifactTypes[i] = validators[i].getArtifactType();
      return artifactTypes;
  }
  
  /* Computes the list of supported TAD versions by scanning the platform 
   * registry.  See comment for tadVersions inst var for a description of the
   * return type. */
  private String[][] computeTADVersions() {
      IExtensionRegistry registry = Platform.getExtensionRegistry();
      IExtensionPoint extensionPoint = registry.getExtensionPoint(
              TAD_VERSION_EXT_ID);
      IExtension[] extensions;
      if (extensionPoint != null) {
          extensions = extensionPoint.getExtensions();
      } else {
          extensions = new IExtension[0];
      }
      ArrayList results = new ArrayList();
      for (int i = 0; i < extensions.length; i++) {

          IConfigurationElement versionElements[] = extensions[i].
                  getConfigurationElements();
          for (int j = 0; j < versionElements.length; j++) {
              String nameVersion[] = new String[2];
              nameVersion[0] = versionElements[j].getAttribute(ATT_TAD_NAME);
              nameVersion[1] = versionElements[j].getAttribute(ATT_TAD_VERSION);
              results.add(nameVersion);
          }
      }
      return (String[][]) results.toArray(new String[0][0]);
  }

  /**
   * Find all TAD versions found in the platform registry extension points
   * org.eclipse.wst.wsi.tad_versions.
   * 
   * @return an array where each member is a two element arrays that describes
   * one TAD version:
   *   element 0: the TAD name
   *   element 1: the TAD version string
   */
  public String[][] getAllTADVersions() 
  {
	if (tadVersions == null)
	  tadVersions = computeTADVersions();
    return tadVersions;  
  }
  
  /* Computes the list of artifact types by scanning the platform registry. */
  private String[] computeReportArtifactTypes() 
  {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint extensionPoint = registry.getExtensionPoint(REPORT_ARTIFACT_TYPES_EXT_ID);
    IExtension[] extensions;
    if (extensionPoint != null) {
      extensions = extensionPoint.getExtensions();
    } else {
      extensions = new IExtension[0];
    }
    ArrayList results = new ArrayList();
    for (int i = 0; i < extensions.length; i++) 
    {
      IConfigurationElement reportArtifactElements[] = extensions[i].getConfigurationElements();
      for (int j = 0; j < reportArtifactElements.length; j++) 
      {
        String reportArtifact = reportArtifactElements[j].getAttribute(ATT_ARTIFACT_TYPE);
        results.add(reportArtifact);
      }
    }
    return (String[]) results.toArray(new String[0]);
  }
  
  /**
   * Find all report artifact types tags found in the platform registry
   * extension points org.eclipse.wst.wsi.reportArtifactTypes.
   * @return an array containing these Strings
   */
  public String[] getAllReportArtifactTypes() 
  {
    if (reportArtifactTypes == null)
        reportArtifactTypes = computeReportArtifactTypes();
    return reportArtifactTypes;
  }
}
