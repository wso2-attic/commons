/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.internal.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class WSDLUtil extends WSDLConstants
{
  protected static WSDLUtil instance;

  //  protected NodeAssociationManager nodeAssociationManager = new NodeAssociationManager();

  protected HashMap elementNameToTypeMap = new HashMap();

  public static WSDLUtil getInstance()
  {
    if (instance == null)
    {
      instance = new WSDLUtil();
    }
    return instance;
  }

  public WSDLUtil()
  {
    elementNameToTypeMap.put(BINDING_ELEMENT_TAG, new Integer(BINDING));
    elementNameToTypeMap.put(DEFINITION_ELEMENT_TAG, new Integer(DEFINITION));
    elementNameToTypeMap.put(DOCUMENTATION_ELEMENT_TAG, new Integer(DOCUMENTATION));
    elementNameToTypeMap.put(FAULT_ELEMENT_TAG, new Integer(FAULT));
    elementNameToTypeMap.put(IMPORT_ELEMENT_TAG, new Integer(IMPORT));
    elementNameToTypeMap.put(INPUT_ELEMENT_TAG, new Integer(INPUT));
    elementNameToTypeMap.put(MESSAGE_ELEMENT_TAG, new Integer(MESSAGE));
    elementNameToTypeMap.put(OPERATION_ELEMENT_TAG, new Integer(OPERATION));
    elementNameToTypeMap.put(OUTPUT_ELEMENT_TAG, new Integer(OUTPUT));
    elementNameToTypeMap.put(PART_ELEMENT_TAG, new Integer(PART));
    elementNameToTypeMap.put(PORT_ELEMENT_TAG, new Integer(PORT));
    elementNameToTypeMap.put(PORT_TYPE_ELEMENT_TAG, new Integer(PORT_TYPE));
    elementNameToTypeMap.put(SERVICE_ELEMENT_TAG, new Integer(SERVICE));
    elementNameToTypeMap.put(TYPES_ELEMENT_TAG, new Integer(TYPES));
  }

  public int getWSDLType(Element element)
  {
    int result = -1;

    if (WSDLConstants.isWSDLNamespace(element.getNamespaceURI()))
    {
      Integer integer = (Integer) elementNameToTypeMap.get(element.getLocalName());
      if (integer != null)
      {
        result = integer.intValue();
      }
    }
    return result;
  }

  protected List getParentElementChain(Element element)
  {
    List list = new ArrayList();
    while (element != null)
    {
      list.add(0, element);
      Node node = element.getParentNode();
      element = (node != null && node.getNodeType() == Node.ELEMENT_NODE) ? (Element)node : null;
    }
    return list;
  }

  /*
   public Object findModelObjectForElement(Definition definition, Element targetElement)
   {   
   Object o = nodeAssociationManager.getModelObjectForNode(definition, targetElement);
   return o;
   }
   
   
   public Element getElementForObject(Object o)
   {      
   Node node = nodeAssociationManager.getNode(o);  
   return (node != null && node.getNodeType() == Node.ELEMENT_NODE) ? (Element)node : null;                  
   }
   
   public Node getNodeForObject(Object o)
   {      
   return nodeAssociationManager.getNode(o);                      
   }
   
   public ITypeSystemProvider getTypeSystemProvider(Definition definition)
   {      
   return WSDLToDOMElementUtil.getTypeSystemProvider(definition);                    
   }
   
   
   public void setTypeSystemProvider(Definition definition, ITypeSystemProvider typeSystemProvider)
   {      
   WSDLToDOMElementUtil.setTypeSystemProvider(definition, typeSystemProvider);                      
   }   
   */

  public static QName createQName(Definition definition, String prefixedName)
  {
    QName qname = null;
    if (prefixedName != null)
    {
      int index = prefixedName.indexOf(":");
      String prefix = (index == -1) ? null : prefixedName.substring(0, index);
      if (prefix != null)
      {
        String namespace = definition.getNamespace(prefix);
        if (namespace != null)
        {
          String localPart = prefixedName.substring(index + 1);
          qname = new QName(namespace, localPart);
        }
      }
    }
    return qname;
  }

  public List getExtensibilityElementNodes(ExtensibleElement extensibleElement)
  {
    List childList = new ArrayList();
    for (Iterator i = extensibleElement.getEExtensibilityElements().iterator(); i.hasNext();)
    {
      ExtensibilityElement extensibilityElement = (ExtensibilityElement)i.next();
      if (extensibilityElement != null)
      {
        Element element = extensibilityElement.getElement();
        if (element != null)
        {
          childList.add(element);
        }
      }
    }
    return childList;
    //return extensibleElement.getEExtensibilityElements();
  }
}
