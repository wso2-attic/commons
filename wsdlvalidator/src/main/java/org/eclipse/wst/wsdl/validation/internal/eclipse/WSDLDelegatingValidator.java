/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.eclipse;

import org.eclipse.wst.validation.internal.delegates.DelegatingValidator;

/**
 * This class provides a unique name (class name) which the validation framework
 * will use to identify the WSDL validator. The actual delegating validator
 * functionality is provided by the base class. The actual validation
 * functionality is provided by the delegates registered with this class as
 * their target.
 */
public class WSDLDelegatingValidator extends DelegatingValidator
{
  /**
   * Default constructor.
   */
  public WSDLDelegatingValidator()
  {
  }
}
