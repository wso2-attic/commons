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

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;
import org.eclipse.wst.wsi.internal.core.report.ArtifactReference;

/**
 * An artifact reference.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ArtifactReferenceImpl implements ArtifactReference
{
  /**
   * Timestamp.
   */
  protected String timestamp = null;

  /**
   * Optional document element.
   */
  protected DocumentElement documentElement = null;

  /**
   * Optional document element namespace name.
   */
  protected String elementNamespaceName = null;

  /* (non-Javadoc)
   * @see org.wsi.test.report.ArtifactReference#getTimestamp()
   */
  public String getTimestamp()
  {
    return this.timestamp;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ArtifactReference#setTimestamp(String)
   */
  public void setTimestamp(String timestamp)
  {
    this.timestamp = timestamp;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ArtifactReference#getDocumentElement()
   */
  public DocumentElement getDocumentElement()
  {
    return this.documentElement;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.ArtifactReference#setDocumentElement(DocumentElement, String)
   */
  public void setDocumentElement(
    DocumentElement documentElement,
    String namespaceName)
  {
    this.documentElement = documentElement;
    this.elementNamespaceName = namespaceName;
  }

  /* (non-Javadoc)
   * Get string representation of this object.
   */
  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println("  Artifact Reference: ");
    pw.println("    timestamp ........... " + this.timestamp);

    if (this.documentElement != null)
      pw.println(
        "    documentElement ..... "
          + this.documentElement.toXMLString(this.elementNamespaceName));

    return sw.toString();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Create element 
    pw.print("    <" + nsName + ELEM_NAME + " ");
    pw.println(WSIConstants.ATTR_TIMESTAMP + "=\"" + getTimestamp() + "\">");

    // If there is a document element, then add it
    if (this.documentElement != null)
    {
      pw.print(documentElement.toXMLString(this.elementNamespaceName));
    }

    // End element
    pw.println("    </" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }

}
