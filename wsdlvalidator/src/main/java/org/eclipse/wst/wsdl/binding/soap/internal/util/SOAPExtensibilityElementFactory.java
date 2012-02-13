/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.util;


import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;


public class SOAPExtensibilityElementFactory implements ExtensibilityElementFactory
{
  public ExtensibilityElement createExtensibilityElement(String namespace, String localName)
  {
    if (SOAPConstants.SOAP_NAMESPACE_URI.equals(namespace))
    {
      if (SOAPConstants.ADDRESS_ELEMENT_TAG.equals(localName))
      {
        return SOAPFactory.eINSTANCE.createSOAPAddress();
      }
      else if (SOAPConstants.BINDING_ELEMENT_TAG.equals(localName))
      {
        return SOAPFactory.eINSTANCE.createSOAPBinding();
      }
      else if (SOAPConstants.BODY_ELEMENT_TAG.equals(localName))
      {
        return SOAPFactory.eINSTANCE.createSOAPBody();
      }
      else if (SOAPConstants.FAULT_ELEMENT_TAG.equals(localName))
      {
        return SOAPFactory.eINSTANCE.createSOAPFault();
      }
      else if (SOAPConstants.OPERATION_ELEMENT_TAG.equals(localName))
      {
        return SOAPFactory.eINSTANCE.createSOAPOperation();
      }
      else if (SOAPConstants.HEADER_ELEMENT_TAG.equals(localName))
      {
        return SOAPFactory.eINSTANCE.createSOAPHeader();
      }
      else if (SOAPConstants.HEADER_FAULT_ELEMENT_TAG.equals(localName))
      {
        return SOAPFactory.eINSTANCE.createSOAPHeaderFault();
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
