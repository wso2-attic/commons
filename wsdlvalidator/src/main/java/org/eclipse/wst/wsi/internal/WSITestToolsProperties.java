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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.document.DocumentFactory;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertionsReader;

/**
 * WS-I test tools property.
 */
public class WSITestToolsProperties
{
  protected static String installURL = "";
  protected static String tadfile = "";

  public final static String schemaDir = "common/schemas/";
  public static String SSBP_ASSERTION_FILE = "http://www.ws-i.org/Testing/Tools/2005/01/SSBP10_BP11_TAD_1-0.xml";
  public static String AP_ASSERTION_FILE = "http://www.ws-i.org/Testing/Tools/2004/12/AP10_BP11_SSBP10_TAD.xml";
  public final static String DEFAULT_ASSERTION_FILE = AP_ASSERTION_FILE;
  
  public static final String STOP_NON_WSI = "0";
  public static final String WARN_NON_WSI = "1";
  public static final String IGNORE_NON_WSI = "2";
  
  protected static boolean eclipseContext = false;
  protected static Map uriToAssertionsMap = new HashMap();
  protected static DocumentFactory documentFactory = null;

  /**
   *  Constructor.
   */
  protected WSITestToolsProperties()
  {
    super();
  }
  
  public static void setEclipseContext(boolean eclipseActive)
  {
    eclipseContext = eclipseActive;
  }
  
  public static boolean getEclipseContext()
  {
    return eclipseContext;
  }
  
  /**
   * Checks the WS-I preferences for the given file and return them in a
   * WSIPreferences object.
   * 
   * @param fileuri The file URI to check the WS-I preferences for.
   * @return A WSIPreferences object containing the preference for this file URI.
   */
  public static WSIPreferences checkWSIPreferences(String fileuri)
  {
  	return new WSIPreferences();
  }
  
  /**
   * Returns the profile assertions located at the given URI.
   * @param assertionsURI  the location of the TAD.
   * @return the profile assertions located at the given URI.
 * @throws WSIException 
   */
  public static ProfileAssertions getProfileAssertions(String assertionsURI) throws WSIException
  {
	 ProfileAssertions result = null;
	 if (assertionsURI != null)
	 {
	   try
	   {
	     if (uriToAssertionsMap.containsKey(assertionsURI))
		   result = (ProfileAssertions)uriToAssertionsMap.get(assertionsURI);
	     else
	     {
	       // Read profile assertions
	       if (documentFactory == null)
	    	   documentFactory = DocumentFactory.newInstance();
	       ProfileAssertionsReader profileAssertionsReader = documentFactory.newProfileAssertionsReader();
	       result = profileAssertionsReader.readProfileAssertions(assertionsURI);
           if (result != null)
           {
        	 uriToAssertionsMap.put(assertionsURI, result);
           }
	     }
	   }
	   catch (Exception e)
	   {
		 result = null;
	   }
	 }
	 return result;
  }
  
  /**
   * Remove file: and any slashes from the fileuri. 
   * Eclipse's resolution mechanism needs to start with the drive.
   */
  protected static String trimURI(String fileuri)
  {
  	String uriStr = fileuri;
  	
    if(fileuri.startsWith("file:"))
    {
      uriStr = fileuri.substring(5);
    }
    while(uriStr.startsWith("/") || uriStr.startsWith("\\"))
    {
      uriStr = uriStr.substring(1);
    }
    return uriStr;
  }

}
