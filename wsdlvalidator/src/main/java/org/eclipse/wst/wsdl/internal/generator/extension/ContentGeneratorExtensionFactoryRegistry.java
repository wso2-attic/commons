/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.internal.generator.extension;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.wsdl.internal.generator.ContentGenerator;


public class ContentGeneratorExtensionFactoryRegistry
{
  protected Map map;

  protected static ContentGeneratorExtensionFactoryRegistry instance;

  public static ContentGeneratorExtensionFactoryRegistry getInstance()
  {
    if (instance == null)
    {
      instance = new ContentGeneratorExtensionFactoryRegistry();

      // TODO... don't read the registry until an 'get'occurs
      //
      ContentGeneratorExtensionRegistryReader reader = new ContentGeneratorExtensionRegistryReader(instance);
      reader.readRegistry();
    }

    return instance;
  }

  protected ContentGeneratorExtensionFactoryRegistry()
  {
    map = new HashMap();
  }

  public void put(String namespace, String name, ContentGeneratorExtensionDescriptor descriptor)
  {
    // Check if this namespace/name key is already in the Map
    List foundKey = findExistingKey(namespace, name);

    // Did we find an existing key with the same namespace/key pairing?
    if (foundKey != null)
    {
      // Simply add the descriptor to the list of descriptors associated
      // with this namespace/name key
      List descriptorList = (List)map.get(foundKey);
      descriptorList.add(descriptor);
    }
    else
    {
      // We need to create a new entry in the Map for this namespace/name key
      List newKey = new ArrayList(2);
      newKey.add(namespace);
      newKey.add(name);

      List newDescriptorList = new ArrayList();
      newDescriptorList.add(descriptor);

      map.put(newKey, newDescriptorList);
    }
  }

  /*
   * Returns the 'name' attribute for this extension.
   */
  public List getBindingExtensionNames()
  {
    List list = new ArrayList();

    Iterator keyIt = map.keySet().iterator();
    List key = null;
    while (keyIt.hasNext())
    {
      key = (List)keyIt.next();
      list.add(key.get(1));
    }

    return list;
  }

  /*
   * Return the class specified for this extension based on the namespace
   * attribute.
   */
  public ContentGenerator getGeneratorClassFromNamespace(String namespace)
  {
    ContentGenerator generatorClass = null;
    List key = findExistingKeyFromNamespace(namespace);

    if (key != null)
    {
      List descriptors = (List)map.get(key);

      // Grab the first descriptor available
      if (descriptors.size() > 0)
      {
        ContentGeneratorExtensionDescriptor extensionDescriptor = (ContentGeneratorExtensionDescriptor)descriptors.get(0);
        generatorClass = (ContentGenerator)extensionDescriptor.getContentGenerator();
      }
    }

    return generatorClass;
  }

  /*
   * Return the class specified for this extension based on the name
   * attribute.
   */
  public ContentGenerator getGeneratorClassFromName(String name)
  {
    ContentGenerator generatorClass = null;
    List key = findExistingKeyFromName(name);

    if (key != null)
    {
      List descriptors = (List)map.get(key);

      // Grab the first descriptor available
      if (descriptors.size() > 0)
      {
        ContentGeneratorExtensionDescriptor extensionDescriptor = (ContentGeneratorExtensionDescriptor)descriptors.get(0);
        generatorClass = (ContentGenerator)extensionDescriptor.getContentGenerator();
      }
    }

    return generatorClass;
  }

  // TODO: We need to common the following three methods up.......
  private List findExistingKeyFromNamespace(String namespace)
  {
    Iterator keyIt = map.keySet().iterator();
    List foundKey = null;
    while (keyIt.hasNext())
    {
      foundKey = (List)keyIt.next();
      if (namespace.equals(foundKey.get(0)))
      {
        break;
      }
      foundKey = null;
    }

    return foundKey;
  }

  private List findExistingKeyFromName(String name)
  {
    Iterator keyIt = map.keySet().iterator();
    List foundKey = null;
    while (keyIt.hasNext())
    {
      foundKey = (List)keyIt.next();
      if (name.equals(foundKey.get(1)))
      {
        break;
      }
      foundKey = null;
    }

    return foundKey;
  }

  private List findExistingKey(String namespace, String name)
  {
    Iterator keyIt = map.keySet().iterator();
    List foundKey = null;
    while (keyIt.hasNext())
    {
      foundKey = (List)keyIt.next();
      if (namespace.equals(foundKey.get(0)) && name.equals(foundKey.get(1)))
      {
        break;
      }
      foundKey = null;
    }

    return foundKey;
  }
}
