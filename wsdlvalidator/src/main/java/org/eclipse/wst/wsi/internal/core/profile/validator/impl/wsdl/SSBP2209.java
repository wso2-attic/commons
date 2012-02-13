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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;


/**
 * SSBP2209
 *
 * <context>For a candidate wsdl:binding element</context>
 * <assertionDescription>The wsdl:binding binds every wsdl:part of a wsdl:message in the wsdl:portType to which it refers to one of soapbind:body, soapbind:header, soapbind:fault or soapbind:headerfault.</assertionDescription>
 */
public class SSBP2209 extends BP2114
{
  /**
   * @param WSDLValidatorImpl
   */
  public SSBP2209(WSDLValidatorImpl impl)
  {
    super(impl);
  }
}