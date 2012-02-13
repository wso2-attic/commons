/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolver;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;

/**
 * WS-I test tool properties specific for Eclipse.
 */
public class WSITestToolsEclipseProperties extends WSITestToolsProperties
{
 /**
  * @see org.eclipse.wst.wsi.internal.WSITestToolsProperties#checkWSIPreferences(java.lang.String)
  */
  public static WSIPreferences checkWSIPreferences(String fileuri)
  {
	// Cache the WS-I tads.
	cacheTADFiles();
	
    WSIPreferences preferences = new WSIPreferences();
    // Remove file: and any slashes from the fileuri. 
    // Eclipse's resolution mechanism needs to start with the drive.
    String uriStr = trimURI(fileuri);

    PersistentWSIContext APcontext = WSPlugin.getInstance().getWSIAPContext();
    PersistentWSIContext SSBPcontext = WSPlugin.getInstance().getWSISSBPContext();
    
    IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(new Path(uriStr));
    if (files != null && files.length == 1)
    {
      //check project level compliance
      IProject project = files[0].getProject();
      
      if (APcontext.projectStopNonWSICompliances(project))
      {
        preferences.setTADFile(AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      } 
      else if (APcontext.projectWarnNonWSICompliances(project))
      {
        preferences.setTADFile(AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
      }
      else if (SSBPcontext.projectStopNonWSICompliances(project))
      {
        preferences.setTADFile(SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      }
      else if (SSBPcontext.projectWarnNonWSICompliances(project))
      {
        preferences.setTADFile(SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
      }
      else
      {
        preferences.setTADFile(DEFAULT_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.IGNORE_NON_WSI);
      }
    }
    else
    {
      // If we can't obtain the project preference use the global preference.
      String APlevel = APcontext.getPersistentWSICompliance();
      String SSBPlevel = SSBPcontext.getPersistentWSICompliance();
      if(APlevel.equals(PersistentWSIContext.STOP_NON_WSI))
      {
        preferences.setTADFile(AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
      }
      else if(APlevel.equals(PersistentWSIContext.WARN_NON_WSI))
      {
        preferences.setTADFile(AP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
     }
     if(SSBPlevel.equals(PersistentWSIContext.STOP_NON_WSI))
     {
       preferences.setTADFile(SSBP_ASSERTION_FILE);
       preferences.setComplianceLevel(PersistentWSIContext.STOP_NON_WSI);
     }
     else if(SSBPlevel.equals(PersistentWSIContext.WARN_NON_WSI))
     {
      preferences.setTADFile(SSBP_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.WARN_NON_WSI);
     }
     else
     {
        preferences.setTADFile(DEFAULT_ASSERTION_FILE);
        preferences.setComplianceLevel(PersistentWSIContext.IGNORE_NON_WSI);
      }
    }
    return preferences;
  }
  
  protected static void cacheTADFiles()
  {
	  String resultAP = cacheFile(AP_ASSERTION_FILE);
	  if(resultAP != null)
	  {
		  AP_ASSERTION_FILE = resultAP;
	  }
	  
	  String resultSSBP = cacheFile(SSBP_ASSERTION_FILE);
	  if(resultSSBP != null)
	  {
		  SSBP_ASSERTION_FILE = resultSSBP;
	  }
  }
  
  protected static String cacheFile(String uri)
  {
	  URIResolver resolver = getURIResolver();
	  String resolvedUri = resolver.resolve("", null, uri);
	  return resolver.resolvePhysicalLocation("", null, resolvedUri);
  }
  
  /**
   * Get the URI resolver to use for WS-I validaiton.
   * 
   * @return
   * 		The URI resolver to use for WS-I validation.
   */
  public static URIResolver getURIResolver()
  {
	  return URIResolverPlugin.createResolver();
  }

}
