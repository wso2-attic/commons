/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.xml.schema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.ibm.wsdl.util.xml.DOM2Writer;

/**
 * This class is used to validate an XML Schema.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class XMLSchemaValidator extends XMLSchemaProcessor
{
  private static final String XMLNS_PREFIX = "xmlns:";

  /**
   * Constructor for XMLSchemaValidator.
   * @param context       document context. 
   * @param documentList  cache of previously parsed documents.
   */
  public XMLSchemaValidator(String context)
  {
    super(context);
  }

  protected void processSchema(Element element)
  {
    NamedNodeMap attrList;

    HashMap elementMap = new HashMap();
    HashMap nsMap = new HashMap();

    try
    {
      // Get schema content as a string
      String schema = DOM2Writer.nodeToString(element);

      // Get list of element namespaces
      if ((attrList = element.getAttributes()) != null)
      {
        addNamespaces(attrList, elementMap, null);
      }

      // If elementMap does not contain entry for it's own namespace, then add it
      if (elementMap.get(XMLNS_PREFIX + element.getPrefix()) == null)
      {
        elementMap.put(
          XMLNS_PREFIX + element.getPrefix(),
          element.getNamespaceURI());
      }

      // Get first parent node and then process all of them until you hit the top of the document
      Node parentNode = element.getParentNode();
      while (parentNode != null)
      {
        // Only process Element nodes
        if (parentNode.getNodeType() == Node.ELEMENT_NODE)
        {
          // Get the list of attributes for the parent node
          attrList = parentNode.getAttributes();

          // If there are attributes, look for the xmlns: attributes
          if (attrList != null)
          {
            addNamespaces(attrList, nsMap, elementMap);
          }
        }

        parentNode = parentNode.getParentNode();
      }

      // Build namespace list
      if (nsMap.size() > 0)
      {
        String attr;
        String namespaceList = "";
        Iterator iterator = nsMap.keySet().iterator();
        while (iterator.hasNext())
        {
          attr = (String) iterator.next();
          namespaceList += " " + attr + "=\"" + nsMap.get(attr) + "\"";
        }

        // Add namespace settings, since the DOM2Writer will miss any that are used as attribute values
        int index = schema.indexOf(">");
        if (index > 0)
        {
          String start = schema.substring(0, index);
          String end = schema.substring(index);

          schema = start + namespaceList + end;
        }
      }

      // Schema validate the XML schema document
      XMLUtils.parseXML(schema, TestUtils.getXMLSchemaLocation());
    }

    catch (WSIException we)
    {
      Throwable t = we.getTargetException();
      if (t != null)
      {
        returnList.add(t.getMessage());
      }
    }
  }

  private void addNamespaces(NamedNodeMap attrList, Map nsMap, Map elementMap)
  {
    Node attr;
    String nodeName;

    for (int i = 0; i < attrList.getLength(); i++)
    {
      attr = attrList.item(i);

      // Get the attribute node name
      nodeName = attr.getNodeName();

      // If it starts with xmlns:, then determine if it should be added to the list of namespaces 
      if (nodeName.startsWith(XMLNS_PREFIX)
        && ((elementMap == null
          || (elementMap != null && elementMap.get(nodeName) == null))))
      {
        if (nsMap.get(nodeName) == null)
          nsMap.put(nodeName, attr.getNodeValue());
      }
    }
  }

}
