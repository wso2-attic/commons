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

import java.util.Vector;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.analyzer.config.AssertionResultsOption;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;

/**
 * Test assertion result.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface AssertionResult extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_ASSERTION_RESULT;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_REPORT, ELEM_NAME);

  /**
   * Result values.
   */
  public static final String RESULT_PASSED = "passed";
  public static final String RESULT_FAILED = "failed";
  public static final String RESULT_PREREQ_FAILED = "prereqFailed";
  public static final String RESULT_MISSING_INPUT = "missingInput";
  public static final String RESULT_WARNING = "warning";
  public static final String RESULT_NOT_APPLICABLE = "notApplicable";

  /**
   * Get result.
   * @return result.
   * @see #setResult
   */
  public String getResult();

  /**
   * Set result.
   * @param result the result value.
   * @see #getResult
   */
  public void setResult(String result);

  /**
   * Get assertion.
   * @return profile test assertion.
   * @see #setAssertion
   */
  public TestAssertion getAssertion();

  /**
   * Set assertion.
   * @param profileAssertion profile test assertion.
   * @see #getAssertion
   */
  public void setAssertion(TestAssertion profileAssertion);

  /**
   * Get entry.
   * @return entry.
   * @see #setEntry
   */
  public Entry getEntry();

  /**
   * Set entry.
   * @param entry an entry.
   * @see #getEntry
   */
  public void setEntry(Entry entry);

  /**
   * Get failure detail messages.
   * @return failure detail messages.
   * @see #setFailureDetailList
   */
  public Vector getFailureDetailList();

  /**
   * Set failure detail messages.
   * @param failureDetailList failure detail messages.
   * @see #getFailureDetailList
   */
  public void setFailureDetailList(Vector failureDetailList);

  /**
   * Get warning messages.
   * @return warning messages.
   * @see #setWarningMessages
   */
  //public Vector getWarningMessages();

  /**
   * Set warning messages.
   * @param warningMessages warning messages.
   * @see #getWarningMessages
   */
  //public void setWarningMessages(Vector warningMessages);

  /**
   * Get assertion results option.
   * @return ssertion results option.
   * @see #setAssertionResultsOption
   */
  public AssertionResultsOption getAssertionResultsOption();

  /**
   * Set assertion results option.
   * @param assertionResultsOption assertion results option.
   * @see #getAssertionResultsOption
   */
  public void setAssertionResultsOption(AssertionResultsOption assertionResultsOption);

  /**
   * Get prereq failed list.
   * @return prereq failed list.
   * @see #setPrereqFailedList
   */
  public PrereqFailedList getPrereqFailedList();

  /**
   * Set prereq failed list.
   * @param prereqFailedList prereq failed list.
   * @see #getPrereqFailedList
   */
  public void setPrereqFailedList(PrereqFailedList prereqFailedList);
}
