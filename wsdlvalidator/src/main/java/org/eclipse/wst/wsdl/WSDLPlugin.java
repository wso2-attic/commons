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
package org.eclipse.wst.wsdl;


import javax.wsdl.factory.WSDLFactory;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.wst.wsdl.internal.extensibility.ExtensibilityElementFactoryRegistryImpl;
import org.eclipse.wst.wsdl.internal.extensibility.ExtensibilityElementFactoryRegistryReader;
import org.eclipse.wst.wsdl.internal.impl.wsdl4j.WSDLFactoryImpl;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactoryRegistry;


/**
 * The <b>Plugin</b> for the model.
 * The WSDL model needs to be able to run within an Eclipse workbench,
 * within a headless Eclipse workspace, or just stand-alone as part 
 * of some other application.
 * To support this, all access is directed to the static methods,
 * which can redirect the service as appopriate to the runtime.
 * During stand-alone invocation no plugin initialization takes place.
 * In this case you will need the resources jar on the class path.
 * @see #getBaseURL
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 * </p>
 * @since 1.0
 */
public final class WSDLPlugin extends EMFPlugin
{
  /**
   * The singleton instance of the plugin.
   */
  public static final WSDLPlugin INSTANCE = new WSDLPlugin();

  /**
   * The one instance of this class.
   */
  static WSDLPluginImplementation plugin;

  private ExtensibilityElementFactoryRegistryImpl extensibilityElementFactoryRegistry;

  /**
   * Creates the singleton instance.
   */
  private WSDLPlugin()
  {
    super(new ResourceLocator []{});
  }

  /*
   * Javadoc copied from base class.
   */
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * Returns the singleton instance of the Eclipse plugin.
   * @return the singleton instance.
   */
  public static WSDLPluginImplementation getPlugin()
  {
    return plugin;
  }

  private ExtensibilityElementFactoryRegistryImpl internalGetExtensibilityElementFactoryRegistry()
  {
    if (extensibilityElementFactoryRegistry == null)
    {
      extensibilityElementFactoryRegistry = new ExtensibilityElementFactoryRegistryImpl();
      if (plugin != null)
      {
        new ExtensibilityElementFactoryRegistryReader(extensibilityElementFactoryRegistry).readRegistry();
      }
    }
    return extensibilityElementFactoryRegistry;
  }

  public ExtensibilityElementFactory getExtensibilityElementFactory(String namespace)
  {
    return internalGetExtensibilityElementFactoryRegistry().getExtensibilityElementFactory(namespace);
  }

  public ExtensibilityElementFactoryRegistry getExtensibilityElementFactoryRegistry()
  {
    return internalGetExtensibilityElementFactoryRegistry();
  }

  public WSDLFactory createWSDL4JFactory()
  {
    return new WSDLFactoryImpl();
  }
}
