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
package org.eclipse.wst.wsdl.binding.mime.internal.util;


import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.binding.mime.MIMEFactory;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;


public class MIMEExtensibilityElementFactory implements ExtensibilityElementFactory
{
  public ExtensibilityElement createExtensibilityElement(String namespace, String localName)
  {
    if (MIMEConstants.MIME_NAMESPACE_URI.equals(namespace))
    {
      if (MIMEConstants.CONTENT_ELEMENT_TAG.equals(localName))
      {
        return MIMEFactory.eINSTANCE.createMIMEContent();
      }
      else if (MIMEConstants.MIME_XML_ELEMENT_TAG.equals(localName))
      {
        return MIMEFactory.eINSTANCE.createMIMEMimeXml();
      }
      else if (MIMEConstants.MULTIPART_RELATED_ELEMENT_TAG.equals(localName))
      {
        return MIMEFactory.eINSTANCE.createMIMEMultipartRelated();
      }
      /* TODO - fix the rose model
       else if ("part".equals(localName))
       {
       return MIMEFactory.eINSTANCE.createMIMEPart();
       }
       */
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
