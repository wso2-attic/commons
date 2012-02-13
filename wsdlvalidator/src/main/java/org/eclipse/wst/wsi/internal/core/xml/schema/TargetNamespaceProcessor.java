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

import java.util.List;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class processes target namespaces. Note this class is a specialization
 * of org.wsi.xml.schema.XMLSchemaProcessor.
 * 
 * @author Peter Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class TargetNamespaceProcessor extends XMLSchemaProcessor
{

  /**
   * Constructor for TargetNamespaceProcess.
   * 
   * @param context
   *            document context
   * @param documentList
   *            cache of previously parsed documents
   */
  public TargetNamespaceProcessor(String context)
  {
    super(context, false);
  }

  protected void processSchema(Element element)
  {
    String tns = null;
    Attr attr = XMLUtils.getAttribute(element, ATTR_XSD_TARGETNAMESPACE);

    // If the attribute was found and value is not null, then add it to the list
    if ((attr != null) && ((tns = attr.getValue()) != null))
    {
      // Add the targetNamespace to the return list
      returnList.add(tns);
    }
  }

  /**
   * Search for all schema elements, load the xsd documents, and then process
   * them.
   * 
   * @param node
   *            node.
   * @return a list.
   * @throws WSIException
   *             if the XML schema file is not found or if it is not formatted
   *             correctly.
   */
  public List processAllSchema(Node node) throws WSIException
  {
    return processAllSchema(node, context);
  }

  /**
   * Search for all schema elements, load the xsd documents, and then process
   * them.
   * 
   * @param node
   *            node.
   * @return a list.
   * @throws WSIException
   *             if the XML schema file is not found or if it is not formatted
   *             correctly.
   */
  private List processAllSchema(Node node, String ctx) throws WSIException
  {
	ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
	try
	{
   	  Thread.currentThread().setContextClassLoader(XMLUtils.class.getClassLoader());   
	
      // if xsd:schema element is found -> process schema
      if (XMLUtils.equals(node, ELEM_XSD_SCHEMA))
      {
        processSchema((Element) node);

        Node n = node.getFirstChild();
        while (n != null)
        {
          if (Node.ELEMENT_NODE == n.getNodeType()&& XMLUtils.equals(n, ELEM_XSD_IMPORT))
          {
            Attr schemaLocation = XMLUtils.getAttribute((Element) n, ATTR_XSD_SCHEMALOCATION);

            // Try to parse imported XSD
            if (schemaLocation != null && schemaLocation.getValue() != null)
            {
         	  if (!schemaLocations.contains(schemaLocation.getValue()))
          	  {
                schemaLocations.add(schemaLocation.getValue());
                try
                {
                  // Read and pParse the XML schema document
                  Document document = parseXMLDocumentURL(schemaLocation.getValue(), ctx, TestUtils.getXMLSchemaLocation());
                  processAllSchema(document.getDocumentElement(), XMLUtils.createURLString(schemaLocation.getValue(), ctx));
                }
                catch (WSIException e)
                {
                  if (throwException) throw e;
                }
                catch (Throwable t)
               {
                 // NOTE: An exception will occur if the XML schema file is not
  			     // found or if it is not formatted correctly
                 if (throwException) throw new WSIException(t.getMessage(), t);
               }
             }
           }
         }
         n = n.getNextSibling();
       }
     }

     // Return list created by the class that extends this class
     return returnList;
    }
    finally
    { 
      Thread.currentThread().setContextClassLoader(currentLoader);
    }    
}
}
