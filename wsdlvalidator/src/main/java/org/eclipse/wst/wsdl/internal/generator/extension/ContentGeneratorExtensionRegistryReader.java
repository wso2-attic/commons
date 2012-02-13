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


import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.osgi.framework.Bundle;


public class ContentGeneratorExtensionRegistryReader
{
  protected static final String PLUGIN_ID = WSDLPlugin.getPlugin().getSymbolicName();

  protected static final String EXTENSION_POINT_ID = "contentGenerators";

  protected static final String ELEMENT_NAME = "contentGenerator";

  protected static final String ATT_NAMESPACE = "namespace";

  protected static final String ATT_NAME = "name";

  protected static final String ATT_CLASS = "class";

  protected ContentGeneratorExtensionFactoryRegistry contentGeneratorExtensionFactoryRegistry;

  public ContentGeneratorExtensionRegistryReader(ContentGeneratorExtensionFactoryRegistry generatorExtensionFactoryRegistry)
  {
    this.contentGeneratorExtensionFactoryRegistry = generatorExtensionFactoryRegistry;
  }

  /**
   * read from plugin registry and parse it.
   */
  public void readRegistry()
  {
    IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
    IExtensionPoint point = pluginRegistry.getExtensionPoint(PLUGIN_ID, EXTENSION_POINT_ID);
    if (point != null)
    {
      IConfigurationElement[] elements = point.getConfigurationElements();
      for (int i = 0; i < elements.length; i++)
      {
        readElement(elements[i]);
      }
    }
  }

  public String resolve(URL platformURL, String relativePath) throws Exception
  {
    URL resolvedURL = FileLocator.resolve(platformURL);
    return resolvedURL.toString() + relativePath;
  }

  protected void readElement(IConfigurationElement element)
  {
    if (element.getName().equals(ELEMENT_NAME))
    {
      try
      {
        IConfigurationElement childElement = (IConfigurationElement)element;
        String name = childElement.getAttribute(ATT_NAME);
        String namespace = childElement.getAttribute(ATT_NAMESPACE);

        if (namespace != null)
        {
          Bundle pluginBundle = Platform.getBundle(element.getDeclaringExtension().getContributor().getName());
          String className = childElement.getAttribute(ATT_CLASS);
          ContentGeneratorExtensionDescriptor descriptor = new ContentGeneratorExtensionDescriptor(pluginBundle, className, namespace, name);
          contentGeneratorExtensionFactoryRegistry.put(namespace, name, descriptor);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}
