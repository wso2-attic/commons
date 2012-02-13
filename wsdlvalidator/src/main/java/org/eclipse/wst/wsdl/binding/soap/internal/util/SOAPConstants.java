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


import org.w3c.dom.Element;


public final class SOAPConstants
{
  public static final String ADDRESS_ELEMENT_TAG = "address"; //$NON-NLS-1$

  public static final String BINDING_ELEMENT_TAG = "binding"; //$NON-NLS-1$

  public static final String BODY_ELEMENT_TAG = "body"; //$NON-NLS-1$

  public static final String FAULT_ELEMENT_TAG = "fault"; //$NON-NLS-1$

  public static final String HEADER_ELEMENT_TAG = "header"; //$NON-NLS-1$

  public static final String HEADER_FAULT_ELEMENT_TAG = "headerfault"; //$NON-NLS-1$

  public static final String OPERATION_ELEMENT_TAG = "operation"; //$NON-NLS-1$

  public static final String SOAP_NAMESPACE_URI = "http://schemas.xmlsoap.org/wsdl/soap/"; //$NON-NLS-1$

  public static final String SOAP_ACTION_ATTRIBUTE = "soapAction"; //$NON-NLS-1$

  public static final String STYLE_ATTRIBUTE = "style"; //$NON-NLS-1$

  public static final String LOCATION_ATTRIBUTE = "location"; //$NON-NLS-1$

  public static final String TRANSPORT_ATTRIBUTE = "transport"; //$NON-NLS-1$

  public static final String USE_ATTRIBUTE = "use"; //$NON-NLS-1$

  public static final String NAMESPACE_ATTRIBUTE = "namespace"; //$NON-NLS-1$

  public static final String NAMESPACE_URI_ATTRIBUTE = "namespaceURI"; //$NON-NLS-1$

  public static final String NAME_ATTRIBUTE = "name"; //$NON-NLS-1$

  public static final String ENCODING_STYLE_ATTRIBUTE = "encodingStyle"; //$NON-NLS-1$

  public static final String MESSAGE_ATTRIBUTE = "message"; //$NON-NLS-1$

  public static final String PART_ATTRIBUTE = "part"; //$NON-NLS-1$

  public static final String PARTS_ATTRIBUTE = "parts"; //$NON-NLS-1$

  public static String getAttribute(Element element, String attributeName)
  {
    return element.hasAttribute(attributeName) ? element.getAttribute(attributeName) : null;
  }
}
