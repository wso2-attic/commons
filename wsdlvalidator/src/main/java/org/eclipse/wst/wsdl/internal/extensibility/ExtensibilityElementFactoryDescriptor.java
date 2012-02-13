/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.internal.extensibility;


import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;
import org.osgi.framework.Bundle;


public class ExtensibilityElementFactoryDescriptor
{
  private final static String CLASS_LOADING_ERROR = "CLASS_LOADING_ERROR";

  protected Bundle bundle;

  protected String namespace;

  protected String className;

  protected Object factory;

  public ExtensibilityElementFactoryDescriptor(String className, String namespace, Bundle bundle)
  {
    this.bundle = bundle;
    this.className = className;
    this.namespace = namespace;
  }

  public ExtensibilityElementFactory getExtensiblityElementFactory()
  {
    if (factory == null)
    {
      try
      {
        Class theClass = bundle.loadClass(className);
        factory = (ExtensibilityElementFactory)theClass.newInstance();
      }
      catch (Exception e)
      {
        factory = CLASS_LOADING_ERROR;
        e.printStackTrace();
      }
    }
    return factory != CLASS_LOADING_ERROR ? (ExtensibilityElementFactory)factory : null;
  }

  public void setExtensiblityElementFactory(ExtensibilityElementFactory factory)
  {
    this.factory = factory;
  }
}
