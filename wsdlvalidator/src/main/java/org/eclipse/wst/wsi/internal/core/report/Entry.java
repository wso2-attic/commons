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

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;
import org.eclipse.wst.wsi.internal.core.util.EntryType;

/**
 * Test assertion target.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface Entry extends EntryResult, DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_ENTRY;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_REPORT, ELEM_NAME);

  /**
   * Get assertion results option.
   */
  //public AssertionResultsOption getAssertionResultsOption();

  /**
   * Set assertion results option.
   */
  //public void setAssertionResultsOption(AssertionResultsOption assertionResultsOption);

  /**
   * Get entry type.
   * @return entry type.
   * @see #setEntryType
   */
  public EntryType getEntryType();

  /**
   * Set entry type.
   * @param entryType entry type.
   * @see #getEntryType
   */
  public void setEntryType(EntryType entryType);

  /**
   * Get artifact name.
   * @return artifact name.
   * @see #setArtifactName
   */
  public String getArtifactName();

  /**
   * Set artifact name.
   * @param artifactName artifact name.
   * @see #getArtifactName
   */
  public void setArtifactName(String artifactName);

  /**
   * Get reference ID.
   * @return reference ID.
   * @see #setReferenceID
   */
  public String getReferenceID();

  /**
   * Set reference ID.
   * @param referenceID referenceID.
   * @see #getReferenceID
   */
  public void setReferenceID(String referenceID);

  /**
   * Get parent element name.
   * @return parent element name.
   * @see #setParentElementName
   */
  public String getParentElementName();

  /**
   * Set parent element name.
   * @param parentElementName parent element name.
   * @see #getParentElementName
   */
  public void setParentElementName(String parentElementName);

  /**
   * Get entry detail.
   * @return entry detail.
   * @see #setEntryDetail
   */
  public Object getEntryDetail();

  /**
   * Set entry detail.
   * @param detail entry detail.
   * @see #getEntryDetail
   */
  public void setEntryDetail(Object detail);

  /**
   * Get entry container.
   * @return entry container.
   * @see #setEntryContainer
   */
  public EntryContainer getEntryContainer();

  /**
   * Set entry container.
   * @param entryContainer entry container.
   * @see #getEntryContainer
   */
  public void setEntryContainer(EntryContainer entryContainer);

  /**
   * Get start element string.
   * @param namespaceName namespace prefix.
   * @param showLogEntry show log entry option.
   * @return start XML element string.
   */
  public String getStartXMLString(String namespaceName, boolean showLogEntry);

  /**
   * Get start element string.
   * @param namespaceName namespace prefix.
   * @param showLogEntry show log entry option.
   * @return start XML element string.
   */
  public String getStartXMLString(String namespaceName, boolean showLogEntry, boolean envelopeArtifactType);
  /**
   * Get end element string.
   * @param namespaceName namespace prefix.
   * @return end XML element string.
   */
  public String getEndXMLString(String namespaceName);
}
