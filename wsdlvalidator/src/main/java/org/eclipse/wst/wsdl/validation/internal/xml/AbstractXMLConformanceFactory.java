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
 * Factory for generating objects needed for XML Conformance validation.
 */
public abstract class AbstractXMLConformanceFactory
{
  protected static AbstractXMLConformanceFactory instance;
  
  /**
   * Returns the instance of this factory.
   * 
   * @return the instance of this factory
   */
  public static AbstractXMLConformanceFactory getInstance()
  {
    if (instance == null)
    {
      instance = new DefaultXMLConformanceFactory();
    }
    return instance;
  }
  
  /**
   * Set the implementation for this factory to use.
   * 
   * @param factory - the implementation this factory will use
   */
  public static void setImplementation(AbstractXMLConformanceFactory factory)
  {
    instance = factory;
  }
  
  /**
   * Return an XML validator.
   * 
   * @return an XML validator
   */
  abstract public IXMLValidator getXMLValidator();
}
