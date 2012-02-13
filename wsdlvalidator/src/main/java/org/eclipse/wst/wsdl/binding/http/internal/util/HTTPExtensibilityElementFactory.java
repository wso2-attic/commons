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
package org.eclipse.wst.wsdl.binding.http.internal.util;


import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.binding.http.HTTPFactory;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;


public class HTTPExtensibilityElementFactory implements ExtensibilityElementFactory
{
  public ExtensibilityElement createExtensibilityElement(String namespace, String localName)
  {
    if (HTTPConstants.HTTP_NAMESPACE_URI.equals(namespace))
    {
      if (HTTPConstants.ADDRESS_ELEMENT_TAG.equals(localName))
      {
        return HTTPFactory.eINSTANCE.createHTTPAddress();
      }
      else if (HTTPConstants.BINDING_ELEMENT_TAG.equals(localName))
      {
        return HTTPFactory.eINSTANCE.createHTTPBinding();
      }
      else if (HTTPConstants.OPERATION_ELEMENT_TAG.equals(localName))
      {
        return HTTPFactory.eINSTANCE.createHTTPOperation();
      }
      else if (HTTPConstants.URL_ENCODED_ELEMENT_TAG.equals(localName))
      {
        return HTTPFactory.eINSTANCE.createHTTPUrlEncoded();
      }
      else if (HTTPConstants.URL_REPLACEMENT_ELEMENT_TAG.equals(localName))
      {
        return HTTPFactory.eINSTANCE.createHTTPUrlReplacement();
      }
      else
      {
        //System.out.println("Unhandled localName: " + localName);
        return WSDLFactory.eINSTANCE.createUnknownExtensibilityElement();
      }
    }
    else
    {
      //System.out.println("Unhandled namespace: " + namespace);
      return WSDLFactory.eINSTANCE.createUnknownExtensibilityElement();
    }
  }
}
