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


import org.eclipse.wst.wsdl.internal.generator.ContentGenerator;
import org.osgi.framework.Bundle;


/*
 * Class which acts as a container to hold information about the extension.
 */
public class ContentGeneratorExtensionDescriptor
{
  protected Bundle bundle;

  protected ContentGenerator contentGenerator;

  protected String namespace;

  protected String name;

  protected String className;

  public ContentGeneratorExtensionDescriptor(Bundle bundle, String classString, String namespace, String name)
  {
    this.bundle = bundle;
    this.namespace = namespace;
    this.name = name;
    this.className = classString;
  }

  public Object getContentGenerator()
  {
    try
    {
      Class theClass = bundle.loadClass(className);
      contentGenerator = (ContentGenerator)theClass.newInstance();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return contentGenerator;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public String getName()
  {
    return name;
  }
}
