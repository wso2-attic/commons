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

package org.eclipse.wst.wsdl.validation.internal.resolver;


/**
 * A delegate to hold information about an extension URI resolver.
 */
public class URIResolverDelegate
{
  private String classname;
  private ClassLoader classloader;
  private IExtensibleURIResolver resolver = null;
  
  
  /**
   * Constructor.
   * 
   * @param classname The class name of the URI resolver.
   * @param classloader The class loader to use to load the URI resolver.
   */
  public URIResolverDelegate(String classname, ClassLoader classloader)
  {
    this.classname = classname;
    this.classloader = classloader;
  }
  
  /**
   * Get the URI resolver described by this delegate.
   * 
   * @return The URI resolver described by this delegate.
   */
  public IExtensibleURIResolver getURIResolver()
  {
    if(resolver == null)
    {
      try
      {
        resolver = (IExtensibleURIResolver)classloader.loadClass(classname).newInstance();
      }
      catch(Exception e)
      {
        try
        {
          resolver = (IExtensibleURIResolver)getClass().getClassLoader().loadClass(classname).newInstance();
        }
        catch(Exception e2)
        {
        }
      }
    }
    return resolver;
  }
}
