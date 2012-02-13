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

import java.util.TreeMap;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.WSITestToolsPlugin;
import org.eclipse.wst.wsi.internal.core.profile.validator.BaseValidator;

/**
 * This class ...
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class EntryType
{
  /**
   * ReportArtifact type.
   */
  private ArtifactType artifactType;

  /**
   * This type.
   */
  private String typeName;

  /**
   * Entry types accessible by type name.
   */
  protected static TreeMap entryTypeMap;

  /**
   * Entry type names accessible by artifact.
   */
  protected static final TreeMap entryTypeNameMap = new TreeMap();

  /**
   * Envelope entry types.
   */
  /** @deprecated -- use EnvelopeValidator.TYPE_ENVELOPE_REQUEST */
  public static final String TYPE_ENVELOPE_REQUEST = "requestEnvelope";
  /** @deprecated -- use EnvelopeValidator.TYPE_ENVELOPE_RESPONSE */
  public static final String TYPE_ENVELOPE_RESPONSE = "responseEnvelope";
  /** @deprecated -- use EnvelopeValidator.TYPE_ENVELOPE_ANYT */
  public static final String TYPE_ENVELOPE_ANY = "anyEnvelope";

  /**
   * Message entry types.
   */
  /** @deprecated -- use MessageValidator.TYPE_MESSAGE_REQUEST */
  public static final String TYPE_MESSAGE_REQUEST = "requestMessage";
  /** @deprecated -- use MessageValidator.TYPE_MESSAGE_RESPONSE */
  public static final String TYPE_MESSAGE_RESPONSE = "responseMessage";
  /** @deprecated -- use MessageValidator.TYPE_MESSAGE_ANY */
  public static final String TYPE_MESSAGE_ANY = "anyMessage";

  /**
   * MIME entry types.
   */
  /** @deprecated -- use MessageValidator.TYPE_MIME_PART */
  public static final String TYPE_MIME_PART = "part";
  /** @deprecated -- use MessageValidator.TYPE_MIME_ROOT_PART */
  public static final String TYPE_MIME_ROOT_PART = "root-part";

  /**
   * Description entry types.
   */
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION_DEFINITIONS */
  public static final String TYPE_DESCRIPTION_DEFINITIONS = "definitions";
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION_IMPORT */
  public static final String TYPE_DESCRIPTION_IMPORT = "import";
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION_TYPES */
  public static final String TYPE_DESCRIPTION_TYPES = "types";
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION_MESSAGE */
  public static final String TYPE_DESCRIPTION_MESSAGE = "message";
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION_OPERATION */
  public static final String TYPE_DESCRIPTION_OPERATION = "operation";
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION_PORTTYPE */
  public static final String TYPE_DESCRIPTION_PORTTYPE = "portType";
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION_BINDING */
  public static final String TYPE_DESCRIPTION_BINDING = "binding";
  /** @deprecated -- use WSDLValidator.TYPE_DESCRIPTION_PORT */
  public static final String TYPE_DESCRIPTION_PORT = "port";

  /**
   * Discovery entry types.
   */
  /** @deprecated -- use UDDIValidator.TYPE_DISCOVERY_BINDINGTEMPLATE */
  public static final String TYPE_DISCOVERY_BINDINGTEMPLATE = "bindingTemplate";
  /** @deprecated -- use UDDIValidator.TYPE_DISCOVERY_TMODEL */
  public static final String TYPE_DISCOVERY_TMODEL = "tModel";

  /**
   * Envelope entry types.
   */
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_REQUESTENV = getEntryType(TYPE_ENVELOPE_REQUEST);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_RESPONSEENV = getEntryType(TYPE_ENVELOPE_RESPONSE);

  /**
   * Message entry types.
   */
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_REQUEST = getEntryType(TYPE_MESSAGE_REQUEST);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_RESPONSE = getEntryType(TYPE_MESSAGE_RESPONSE);
  //  public static final EntryType ENTRY_TYPE_ANYENTRY = 
  //        new EntryType(ArtifactType.ARTIFACT_TYPE_MESSAGES, TYPE_MESSAGE_ANYENTRY);

  /**
   * Description entry types.
   */
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_DEFINITIONS = getEntryType(TYPE_DESCRIPTION_DEFINITIONS);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_IMPORT = getEntryType(TYPE_DESCRIPTION_IMPORT);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_TYPES = getEntryType(TYPE_DESCRIPTION_TYPES);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_MESSAGE = getEntryType(TYPE_DESCRIPTION_MESSAGE);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_OPERATION = getEntryType(TYPE_DESCRIPTION_OPERATION);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_PORTTYPE = getEntryType(TYPE_DESCRIPTION_PORTTYPE);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_BINDING = getEntryType(TYPE_DESCRIPTION_BINDING);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_PORT = getEntryType(TYPE_DESCRIPTION_PORT);

  /**
   * Discovery entry types.
   */
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_BINDINGTEMPLATE = getEntryType(TYPE_DISCOVERY_BINDINGTEMPLATE);
  /** @deprecated -- use getEntryType(String typeName) to access EntryType. */
  public static final EntryType ENTRY_TYPE_TMODEL = getEntryType(TYPE_DISCOVERY_TMODEL);

  /**
   * Create entry type.
   */
  private EntryType(ArtifactType artifactType, String typeName)
  {
    this.artifactType = artifactType;
    this.typeName = typeName;
  }

  /**
   * Is entry type equal to specified type.
   * @param typeName entry type name.
   * @return true if entry type equal to specified type.
   */
  public boolean isType(String typeName)
  {
    return this.typeName.equals(typeName);
  }

  /**
   * Is entry type equal to specified type.
   * @param entryType entry type.
   * @return true if entry type equal to specified type.
   */
  public boolean isType(EntryType entryType)
  {
    return typeName.equals(entryType.getTypeName());
  }

  /**
   * Get artifact type.
   * @return artifact type.
   */
  public ArtifactType getArtifactType()
  {
    return artifactType;
  }

  /**
   * Get entry type name.
   * @return entry type name.
   */
  public String getTypeName()
  {
    return typeName;
  }

  /**
   * Is valid entry type.
   * @param typeName entry type name.
   * @return true if entry type name is valid.
   */
  public static final boolean isValidEntryType(String typeName)
  {
    return (entryTypeMap.get(typeName) == null ? false : true);
  }

  /**
   * Create entry type.
   * @param typeName entry type name.
   * @return entry type.
   * @throws RuntimeException if entry type name is invalid or inappropriate.
   */
  public static final EntryType getEntryType(String typeName)
    throws RuntimeException
  {
    if (entryTypeMap == null) {
        BaseValidator validators[] = WSITestToolsPlugin.getPlugin()
                .getBaseValidators();
        for (int i = 0; i < validators.length; i++) {
            String entryTypes[] = validators[i].getEntryTypes();
            for (int j = 0; j < entryTypes.length; j++)
                registerEntryType(ArtifactType.getArtifactType(
                        validators[i].getArtifactType()), entryTypes[j]);
        }
    }
    // Get the entry type by type name
    EntryType entryType = (EntryType) entryTypeMap.get(typeName);

    if (entryType == null)
    {
      throw new RuntimeException(
        "Could not get entry type because type name is invalid: "
          + typeName
          + ".");
    }

    return entryType;
  }

  /**
   * Get list of entry type names for a specified artifact type name.
   * @param artifactTypeName artifact type name.
   * @return list of entry type names for a specified artifact type name.
   * @throws RuntimeException if entry type name is invalid or inappropriate.
   */
  public static final Vector getEntryTypeNameList(String artifactTypeName)
    throws RuntimeException
  {
    // Get list
    Vector entryTypeNameList = (Vector) entryTypeNameMap.get(artifactTypeName);

    // If the list was not found, then throw an exception
    if (entryTypeNameList == null)
    {
      throw new RuntimeException(
        "Could not get entry type name list because artifact type name is invalid: "
          + artifactTypeName
          + ".");
    }

    return entryTypeNameList;
  }

  /**
   * Get list of type names for a specified artifact type name.
   */
  public static final EntryType registerEntryType(
    ArtifactType artifactType,
    String typeName)
  {
    EntryType entryType = null;

    // Create entry type
    entryType = new EntryType(artifactType, typeName);

    if (entryTypeMap == null) {
        entryTypeMap = new TreeMap();
    }
    // Add to entry type map
    entryTypeMap.put(typeName, entryType);

    // Get the entry type name vector for the artifact type
    Vector entryTypeNameList =
      (Vector) entryTypeNameMap.get(artifactType.getTypeName());
    
    // Lazy initialize
    if (entryTypeNameList == null) {
        entryTypeNameList = new Vector();
        entryTypeNameMap.put(artifactType.getTypeName(), entryTypeNameList);
    }

    // Add the type name to the list
    entryTypeNameList.add(typeName);

    return entryType;
  }
}
