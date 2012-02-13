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
package org.eclipse.wst.wsi.internal.core.report.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TreeMap;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.report.ReportArtifact;
import org.eclipse.wst.wsi.internal.core.util.ArtifactType;

/**
 * This class contains a report artifact.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ReportArtifactImpl implements ReportArtifact
{
  /**
   * Artifact type.
   */
  protected ArtifactType artifactType = null;

  /**
   * Entries.
   */
  protected TreeMap entryList = new TreeMap();

  /* (non-Javadoc)
   * @see org.wsi.test.report.ReportArtifact#getType()
   */
  public ArtifactType getType()
  {
    return this.artifactType;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ReportArtifact#setType(ArtifactType)
   */
  public void setType(ArtifactType artifactType)
  {
    this.artifactType = artifactType;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ReportArtifact#addEntry(Entry)
   */
  public void addEntry(Entry entry)
  {
    // Add entry
    if (entry.getEntryType() != null)
      this.entryList.put(entry.getEntryType().getTypeName(), entry);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ReportArtifact#getEntryList()
   */
  public TreeMap getEntryList()
  {
    return this.entryList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ReportArtifact#getStartXMLString(String)
   */
  public String getStartXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Create element 
    pw.print("  <" + nsName + ELEM_NAME + " ");
    pw.print(
      WSIConstants.ATTR_TYPE + "=\"" + this.artifactType.getTypeName() + "\">");

    return sw.toString();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ReportArtifact#getEndXMLString(String)
   */
  public String getEndXMLString(String namespaceName)
  {
    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    return "  </" + nsName + ELEM_NAME + ">";
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    // Return string
    return getStartXMLString("") + getEndXMLString("");
  }

}
