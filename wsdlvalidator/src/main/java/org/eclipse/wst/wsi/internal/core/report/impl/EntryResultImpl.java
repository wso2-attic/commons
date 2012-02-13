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

import java.util.TreeMap;

import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.report.EntryResult;

/**
 * Result for a specific test assertion.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public abstract class EntryResultImpl implements EntryResult
{
  /**
   * Assertion result list.
   */
  protected TreeMap assertionResultList = new TreeMap();

  /* (non-Javadoc)
   * @see org.wsi.test.report.EntryResult#addAssertionResult(org.wsi.test.report.AssertionResult)
   */
  public void addAssertionResult(AssertionResult assertionResult)
  {
    this.assertionResultList.put(
      assertionResult.getAssertion().getId(),
      assertionResult);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.EntryResult#getAssertionResultList()
   */
  public TreeMap getAssertionResultList()
  {
    return assertionResultList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.report.EntryResult#getAssertionResult(String)
   */
  public AssertionResult getAssertionResult(String assertionId)
  {
    return (AssertionResult) assertionResultList.get(assertionId);
  }
}
