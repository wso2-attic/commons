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
package org.eclipse.wst.wsi.internal.core.analyzer.config;

import org.eclipse.wst.wsi.internal.core.document.DocumentElement;

/**
 * This interface contains a definition of the assertion results that 
 * should be included in the report.  It also contains information 
 * that indicates which messages should be included in the report.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface AssertionResultsOption extends DocumentElement
{
  /**
   * Get assertion result type.
   * @return assertion result type.
   * @see #setAssertionResultType
   */
  public AssertionResultType getAssertionResultType();

  /**
   * Set assertion result type.
   * @param resultType assertion result type.
   * @see #getAssertionResultType
   */
  public void setAssertionResultType(AssertionResultType resultType);

  /**
   * Get show message entry indicator.
   * @return show message entry indicator.
   * @see #setShowMessageEntry
   */
  public boolean getShowMessageEntry();

  /**
   * Set show message entry indictor.
   * @param showMessageEntry show message entry indictor.
   * @see #getShowMessageEntry
   */
  public void setShowMessageEntry(boolean showMessageEntry);

  /**
   * Get show assertion description indicator.
   * @return show assertion description indicator.
   * @see #setShowAssertionDescription
   */
  public boolean getShowAssertionDescription();

  /**
   * Set show assertion description indictor.
   * @param showAssertionDescription show assertion description indictor.
   * @see #getShowAssertionDescription
   */
  public void setShowAssertionDescription(boolean showAssertionDescription);

  /**
   * Get show failure message indicator.
   * @return show failure message indicator.
   * @see #setShowFailureMessage
   */
  public boolean getShowFailureMessage();

  /**
   * Set show failure message indicator.
   * @param showFailureMessage show failure message indicator.
   * @see #getShowFailureMessage
   */
  public void setShowFailureMessage(boolean showFailureMessage);

  /**
   * Get show error detail indicator.
   * @return show error detail indicator.
   * @see #setShowFailureDetail
   */
  public boolean getShowFailureDetail();

  /**
   * Set show error detail indicator.
   * @param showfailureDetail show error detail indicator.
   * @see #getShowFailureDetail
   */
  public void setShowFailureDetail(boolean showfailureDetail);

  /**
   * Get string representation of this object.
   * @return string representation of this object.
   */
  public String toString();
}
