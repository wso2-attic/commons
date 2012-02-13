/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.xml;


/**
 * The default implementation of AbstractXMLConformanceFactory.
 */
public class DefaultXMLConformanceFactory extends AbstractXMLConformanceFactory
{

  /**
   * Constructor.
   */
  public DefaultXMLConformanceFactory()
  {
  }

  /**
   * @see org.eclipse.wst.wsdl.validation.internal.xml.AbstractXMLConformanceFactory#getXMLValidator()
   */
  public IXMLValidator getXMLValidator()
  {
    return new DefaultXMLValidator();
  }

}
