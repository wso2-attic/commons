/*******************************************************************************
 * Copyright (c) 2002-2003 IBM Corporation, Parasoft and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   Parasoft - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.analyzer.config.impl;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultType;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultsOption;

/**
 * This class contains the assertion results option definition.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class AssertionResultsOptionImpl implements AssertionResultsOption
{
  /**
   * Assertion result type.
   */
  protected AssertionResultType resultType = AssertionResultType.ALL;

  /**
   * Show message entry indicator.
   */
  protected boolean showMessageEntry = true;

  /**
   * Show assertion description indicator.
   */
  protected boolean showAssertionDescription = false;

  /**
   * Show failure message indicator.
   */
  protected boolean showFailureMessage = true;

  /**
   * Show failure detail indicator.
   */
  protected boolean showFailureDetail = true;

  /**
   * Get assertion result type.
   * @see #setAssertionResultType
   */
  public AssertionResultType getAssertionResultType()
  {
    return resultType;
  }

  /**
   * Set assertion result type.
   * @see #getAssertionResultType
   */
  public void setAssertionResultType(AssertionResultType resultType)
  {
    this.resultType = resultType;
  }

  /**
   * Get show message entry indicator.
   * @see #setShowMessageEntry
   */
  public boolean getShowMessageEntry()
  {
    return this.showMessageEntry;
  }

  /**
   * Set show message entry indictor.
   * @see #getShowMessageEntry
   */
  public void setShowMessageEntry(boolean showMessageEntry)
  {
    this.showMessageEntry = showMessageEntry;
  }

  /**
   * Get show assertion description indicator.
   * @see #setShowAssertionDescription
   */
  public boolean getShowAssertionDescription()
  {
    return this.showAssertionDescription;
  }

  /**
   * Set show assertion description indictor.
   * @see #getShowAssertionDescription
   */
  public void setShowAssertionDescription(boolean showAssertionDescription)
  {
    this.showAssertionDescription = showAssertionDescription;
  }

  /**
   * Get show failure message indicator.
   * @see #setShowFailureMessage
   */
  public boolean getShowFailureMessage()
  {
    return this.showFailureMessage;
  }

  /**
   * Set show failure mesage indicator.
   * @see #getShowFailureMessage
   */
  public void setShowFailureMessage(boolean showFailureMessage)
  {
    this.showFailureMessage = showFailureMessage;
  }

  /**
   * Get show failure detail indicator.
   * @see #setShowFailureDetail
   */
  public boolean getShowFailureDetail()
  {
    return this.showFailureDetail;
  }

  /**
   * Set show failure detail indicator.
   * @see #getShowFailureDetail
   */
  public void setShowFailureDetail(boolean showFailureDetail)
  {
    this.showFailureDetail = showFailureDetail;
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println("  Assertion Results:");
    pw.println("    type ..................... " + this.resultType);
    pw.println("    messageEntry ............. " + this.showMessageEntry);
    pw.println(
      "    assertionDescription ..... " + this.showAssertionDescription);
    pw.println("    failureMessage ........... " + this.showFailureMessage);
    pw.println("    failureDetail ............ " + this.showFailureDetail);

    return sw.toString();
  }

  /**
   * Get representation of this object as an XML string.
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // Config options
    pw.print("      <" + nsName + WSIConstants.ELEM_ASSERTION_RESULTS + " ");
    pw.print(WSIConstants.ATTR_TYPE + "=\"" + getAssertionResultType() + "\" ");
    pw.print(
      WSIConstants.ATTR_MESSAGE_ENTRY + "=\"" + getShowMessageEntry() + "\" ");
    pw.print(
      WSIConstants.ATTR_ASSERTION_DESCRIPTION
        + "=\""
        + getShowAssertionDescription()
        + "\" ");
    pw.print(
      WSIConstants.ATTR_FAILURE_MESSAGE
        + "=\""
        + getShowFailureMessage()
        + "\" ");
    pw.println(
      WSIConstants.ATTR_FAILURE_DETAIL
        + "=\""
        + getShowFailureDetail()
        + "\"/>");
    // REMOVE:
    //pw.println(WSIConstants.ATTR_WARNING_MESSAGE + "=\"" + getShowWarningMessage() + "\"/>");

    return sw.toString();
  }
}
