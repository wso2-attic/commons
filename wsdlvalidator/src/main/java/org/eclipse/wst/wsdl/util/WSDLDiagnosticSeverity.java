/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.util;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;


/**
 * WSDL diagnostic severity levels type safe enumeration. This class is similar
 * to XSDDiagnosticSeverity.
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this
 * API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public final class WSDLDiagnosticSeverity extends AbstractEnumerator
{
  /**
   * This is used to indicate that the problem is really bad and that further
   * processing is likely not possible.
   * 
   * @see #FATAL_LITERAL
   */
  public static final int FATAL = 0;

  /**
   * This is used to indicate that the problem is bad and that the schema is
   * likely not suitable for use.
   * 
   * @see #ERROR_LITERAL
   */
  public static final int ERROR = 1;

  /**
   * This is used to indicate that the problem is not too bad and that the
   * definition is likely suitable for use.
   * 
   * @see #WARNING_LITERAL
   */
  public static final int WARNING = 2;

  /**
   * This is used to indicate that the problem is only potential and that the
   * definition is very likely suitable for use.
   * 
   * @see #INFORMATION_LITERAL
   */
  public static final int INFORMATION = 3;

  /**
   * The '<em><b>Fatal</b></em>' literal object.
   * 
   * @see #FATAL
   */
  public static final WSDLDiagnosticSeverity FATAL_LITERAL = new WSDLDiagnosticSeverity(FATAL, "fatal", "fatal");

  /**
   * The '<em><b>Error</b></em>' literal object.
   * 
   * @see #ERROR
   */
  public static final WSDLDiagnosticSeverity ERROR_LITERAL = new WSDLDiagnosticSeverity(ERROR, "error", "error");

  /**
   * The '<em><b>Warning</b></em>' literal object.
   * 
   * @see #WARNING
   */
  public static final WSDLDiagnosticSeverity WARNING_LITERAL = new WSDLDiagnosticSeverity(WARNING, "warning", "warning");

  /**
   * The '<em><b>Information</b></em>' literal object.
   * 
   * @see #INFORMATION
   */
  public static final WSDLDiagnosticSeverity INFORMATION_LITERAL = new WSDLDiagnosticSeverity(INFORMATION, "information", "information");

  /**
   * An array of all the '<em><b>Diagnostic Severity</b></em>' enumerators.
   */
  private static final WSDLDiagnosticSeverity[] VALUES_ARRAY = new WSDLDiagnosticSeverity []{
    FATAL_LITERAL,
    ERROR_LITERAL,
    WARNING_LITERAL,
    INFORMATION_LITERAL, };

  /**
   * A public read-only list of all the '<em><b>Diagnostic Severity</b></em>'
   * enumerators.
   */
  public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Diagnostic Severity</b></em>' literal with the
   * specified literal value.
   */
  public static WSDLDiagnosticSeverity get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      WSDLDiagnosticSeverity result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Diagnostic Severity</b></em>' literal with the
   * specified name.
   */
  public static WSDLDiagnosticSeverity getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      WSDLDiagnosticSeverity result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Diagnostic Severity</b></em>' literal with the
   * specified integer value.
   */
  public static WSDLDiagnosticSeverity get(int value)
  {
    switch (value)
    {
      case FATAL:
      return FATAL_LITERAL;
      case ERROR:
      return ERROR_LITERAL;
      case WARNING:
      return WARNING_LITERAL;
      case INFORMATION:
      return INFORMATION_LITERAL;
    }
    return null;
  }

  /**
   * Only this class can construct instances.
   */
  private WSDLDiagnosticSeverity(int value, String name, String literal)
  {
    super(value, name, literal);
  }
}
