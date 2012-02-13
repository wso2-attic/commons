/*******************************************************************************
 *
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

import org.eclipse.wst.wsi.internal.core.WSIException;

/**
 * Test assertion result type.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public final class AssertionResultType
{
  /**
   * This type.
   */
  private String resultType = _ALL;

  /**
   * String version of result types.
   */
  private static final String _ALL = "all";
  private static final String _NOT_INFO = "notInfo";
  private static final String _ONLY_FAILED = "onlyFailed";
  private static final String _NOT_PASSED = "notPassed";

  /**
   * Result types.
   */
  public static final AssertionResultType ALL = new AssertionResultType(_ALL);
  public static final AssertionResultType NOT_INFO = new AssertionResultType(_NOT_INFO);
  public static final AssertionResultType ONLY_FAILED =
    new AssertionResultType(_ONLY_FAILED);
  public static final AssertionResultType NOT_PASSED =
    new AssertionResultType(_NOT_PASSED);

  /**
   * Do not allow this object to be created with null constructor.
   */
  private AssertionResultType()
  {
  }

  /**
   * Create result type.
   * @param resultType a result type.
   */
  private AssertionResultType(String resultType)
  {
    this.resultType = resultType;
  }

  /**
   * Is result type all.
   * @return true if the result type is all.
   */
  public boolean isAll()
  {
    return resultType.equals(_ALL);
  }

  /**
   * Is result type failed only.
   * @return true if the result type is failed only.
   */
  public boolean isFailedOnly()
  {
    return resultType.equals(_ONLY_FAILED);
  }

  /**
   * Is result type not passed.
   * @return true if the result type is not passed.
   */
  public boolean isNotPassed()
  {
    return resultType.equals(_NOT_PASSED);
  }

  /**
   * Is result type not info.
   * @return true if the result type is not info.
   */
  public boolean isNotInfo()
  {
    return resultType.equals(_NOT_INFO);
  }

  /**
   * Get result type.
   * @return result type.
   */
  public String getType()
  {
    return resultType;
  }

  /**
   * Create new assertion result type.
   * @param resultType an assertion result type.
   * @return new assertion result type.
   * @throws WSIException if invalid assertion result type was specified.
   */
  public static AssertionResultType newInstance(String resultType)
    throws WSIException
  {
    AssertionResultType assertionResultType = null;

    if (resultType == null)
    {
      assertionResultType = NOT_INFO;
    }

    else if (resultType.equals(_ALL))
    {
      assertionResultType = ALL;
    }

    else if (resultType.equals(_ONLY_FAILED))
    {
      assertionResultType = ONLY_FAILED;
    }

    else if (resultType.equals(_NOT_PASSED))
    {
      assertionResultType = NOT_PASSED;
    }
    else if (resultType.equals(_NOT_INFO))
    {
      assertionResultType = NOT_INFO;
    }

    else
    {
      throw new WSIException(
        "An invalid assertion result type was specified: " + resultType + ".");
    }

    return assertionResultType;
  }

  /**
   * Get string representation of this object.
   * @return string representation of this object.
   */
  public String toString()
  {
    return resultType;
  }
}
