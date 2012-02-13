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


import java.util.HashMap;

import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactoryRegistry;


public class ExtensibilityElementFactoryRegistryImpl implements ExtensibilityElementFactoryRegistry
{
  protected HashMap map = new HashMap();

  public void put(String namespace, ExtensibilityElementFactoryDescriptor descriptor)
  {
    map.put(namespace, descriptor);
  }

  public ExtensibilityElementFactory getExtensibilityElementFactory(String namespace)
  {
    ExtensibilityElementFactory result = null;
    ExtensibilityElementFactoryDescriptor descriptor = (ExtensibilityElementFactoryDescriptor)map.get(namespace);
    if (descriptor != null)
    {
      result = descriptor.getExtensiblityElementFactory();
    }
    return result;
  }

  public void registerFactory(String namespace, ExtensibilityElementFactory factory)
  {
    ExtensibilityElementFactoryDescriptor descriptor = new ExtensibilityElementFactoryDescriptor(null, namespace, null);
    descriptor.setExtensiblityElementFactory(factory);
    map.put(namespace, descriptor);
  }
}
