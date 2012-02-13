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

import java.util.TreeMap;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;
import org.eclipse.wst.wsi.internal.core.util.ArtifactType;

/**
 * Report artifact.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface ReportArtifact extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_ARTIFACT;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_REPORT, ELEM_NAME);

  /**
   * Get artifact type.
   * @return artifact type.
   * @see #setType
   */
  public ArtifactType getType();

  /**
   * Set artifact type.
   * @param artifactType artifact type.
   * @see #getType
   */
  public void setType(ArtifactType artifactType);

  /**
   * Add entry.
   * @param entry an entry.
   */
  public void addEntry(Entry entry);

  /**
   * Get entry list.
   * @return entry list.
   */
  public TreeMap getEntryList();

  /**
   * Get start element string.
   * @param namespaceName namespace prefix.
   * @return start element string.
   */
  public String getStartXMLString(String namespaceName);

  /**
   * Get end element string. 
   * @param namespaceName namespace prefix.
   * @return end element string.
   */
  public String getEndXMLString(String namespaceName);
}
