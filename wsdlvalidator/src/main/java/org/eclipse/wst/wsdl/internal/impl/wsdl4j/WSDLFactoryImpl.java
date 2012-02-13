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
package org.eclipse.wst.wsdl.internal.impl.wsdl4j;


import javax.wsdl.Definition;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;


public final class WSDLFactoryImpl extends WSDLFactory
{

  //public static WSDLFactory newInstance() throws WSDLException
  //{
  //  return new WSDLFactoryImpl();
  //}

  public WSDLFactoryImpl()
  {
  }

  public Definition newDefinition()
  {
    return org.eclipse.wst.wsdl.internal.impl.WSDLFactoryImpl.eINSTANCE.createDefinition();
  }

  public ExtensionRegistry newPopulatedExtensionRegistry()
  {
    return null;
  }

  public WSDLReader newWSDLReader()
  {
    WSDLReader reader = new WSDLReaderImpl();
    reader.setFactoryImplName(getClass().getName());
    return reader;
  }

  public WSDLWriter newWSDLWriter()
  {
    return new WSDLWriterImpl();
  }
}
