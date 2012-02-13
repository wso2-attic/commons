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
package org.eclipse.wst.wsi.internal.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.wsi.internal.WSITestToolsPlugin;

/**
 * This class ...
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ArtifactType
{
  /**
   * This type.
   */
  private String type;

  private static Map typeMap;
  private static List loggableArtifactTypes; 

  /**
   * ReportArtifact types.
   */
  /** @deprecated -- use EnvelopeValidator.TYPE_ENVELOPE **/ 
  public static final String TYPE_ENVELOPE = "envelope";
  /** @deprecated -- use MessageValidator.TYPE_MESSAGE **/ 
  public static final String TYPE_MESSAGE = "message";
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION **/ 
  public static final String TYPE_DESCRIPTION = "description";
  /** @deprecated -- use UDDIValidator.TYPE_DISCOVERY **/ 
  public static final String TYPE_DISCOVERY = "discovery";

  /**
   * ReportArtifact types.
   */
  /** @deprecated -- use getArtifactType(String typeName) to access ArtifactType **/ 
  public static final ArtifactType ARTIFACT_TYPE_ENVELOPE =
    new ArtifactType(TYPE_ENVELOPE);
  /** @deprecated -- use getArtifactType(String typeName) to access ArtifactType **/ 
  public static final ArtifactType ARTIFACT_TYPE_MESSAGE =
    new ArtifactType(TYPE_MESSAGE);
  /** @deprecated -- use getArtifactType(String typeName) to access ArtifactType **/
  public static final ArtifactType ARTIFACT_TYPE_DESCRIPTION =
    new ArtifactType(TYPE_DESCRIPTION);
  /** @deprecated -- use getArtifactType(String typeName) to access ArtifactType **/
  public static final ArtifactType ARTIFACT_TYPE_DISCOVERY =
    new ArtifactType(TYPE_DISCOVERY);
  
  /**
   * Create artifact type.
   */
  private ArtifactType(String type)
  {
    this.type = type;
  }

  /** Returns true if this artifact can be output to the report. */
  public boolean isLoggable() 
  {
    if (loggableArtifactTypes == null) 
    {
      String artifactArray[] = WSITestToolsPlugin.getPlugin().getAllReportArtifactTypes();
      loggableArtifactTypes = new ArrayList(artifactArray.length);
      for (int i = 0; i < artifactArray.length; i++)
         loggableArtifactTypes.add(artifactArray[i]);
    }
    return loggableArtifactTypes.contains(type);
  }

  /**
   * Is artifact type envelope.
   * @return true if artifact type envelope.
   * @deprecated -- an artifactType is no longer restricted to envelope, 
   *                message, description or discovery.
   */
  public boolean isEnvelope()
  {
    return type.equals(TYPE_ENVELOPE);
  }

  /**
   * Is artifact type messages.
   * @return true if artifact type messages.
   * @deprecated -- an artifactType is no longer restricted to envelope, 
   *                message, description or discovery.
   */
  public boolean isMessages()
  {
    return type.equals(TYPE_MESSAGE);
  }

  /**
   * Is artifact type description.
   * @return true if artifact type description.
   * @deprecated -- an artifactType is no longer restricted to envelope, 
   *                message, description or discovery.
   */
  public boolean isDescription()
  {
    return type.equals(TYPE_DESCRIPTION);
  }

  /**
   * Is artifact type discovery.
   * @return true if artifact type discovery.
   * @deprecated -- an artifactType is no longer restricted to envelope, 
   *                message, description or discovery.
   */
  public boolean isDiscovery()
  {
    return type.equals(TYPE_DISCOVERY);
  }

  /**
   * Get artifact type.
   * @return artifact type.
   */
  public String getTypeName()
  {
    return type;
  }

  /**
   * Instantiates a new artifact type and adds it to the registry map
   * @param name - the artifact name (matches artifact type attribute from TAD)
   */
  public static void registerArtifactType(String name) {
      if (typeMap == null)
          typeMap = new HashMap();
      typeMap.put(name, new ArtifactType(name));
  }
  
  /**
   * Create artifact type.
   * @param typeName artifact type name.
   * @return newly created artifact type.
   * @throws RuntimeException if artifact type name is invalid or inappropriate.
   */
  public static final ArtifactType getArtifactType(String typeName)
    throws RuntimeException
  {
      if (typeMap == null) {
          String types[] = WSITestToolsPlugin.getPlugin().getArtifactTypes();
          for (int i = 0; i < types.length; i++)
              registerArtifactType(types[i]);
      }
      if (typeMap == null) {
          return ArtifactType.newArtifactType(typeName);
      }
      
    if (typeMap.containsKey(typeName))
        return (ArtifactType) typeMap.get(typeName);
     else {
         throw new RuntimeException(
                 "Could not create new artifact type using invalid type name: "
                 + typeName + ".");
     }
  }
  
    /**
   * Create artifact type.
   * @param typeName artifact type name.
   * @return newly created artifact type.
   * @throws RuntimeException if artifact type name is invalid or inappropriate.
   * @deprecated -- use getArtifactType(String typeName)
   */
  public static final ArtifactType newArtifactType(String typeName)
    throws RuntimeException
  {
    ArtifactType artifactType = null;

    if (typeName.equals(TYPE_DESCRIPTION))
    {
      artifactType = ARTIFACT_TYPE_DESCRIPTION;
    }

    else if (typeName.equals(TYPE_MESSAGE))
    {
      artifactType = ARTIFACT_TYPE_MESSAGE;
    }

    else if (typeName.equals(TYPE_ENVELOPE))
    {
      artifactType = ARTIFACT_TYPE_ENVELOPE;
    }

    else if (typeName.equals(TYPE_DISCOVERY))
    {
      artifactType = ARTIFACT_TYPE_DISCOVERY;
    }

    else
    {
      throw new RuntimeException(
        "Could not create new artifact type using invalid type name: "
          + typeName
          + ".");
    }

    return artifactType;
  }
}