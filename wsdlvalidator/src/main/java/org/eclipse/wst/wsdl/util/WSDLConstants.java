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
package org.eclipse.wst.wsdl.util;


import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * This class defines constants for WSDL element tags and relevant namespaces.
 * @since 1.0
 */
public class WSDLConstants
{
  /**
   * The element tag is <code>"binding"</code>.
   * @see #getElementTag(int).
   */
  public static final int BINDING = 0;

  /**
   * The element tag is <code>"definitions"</code>.
   * @see #getElementTag(int).
   */
  public static final int DEFINITION = 1;

  /**
   * The element tag is <code>"documentation"</code>.
   * @see #getElementTag(int).
   */
  public static final int DOCUMENTATION = 2;

  /**
   * The element tag is <code>"fault"</code>.
   * @see #getElementTag(int).
   */
  public static final int FAULT = 3;

  /**
   * The element tag is <code>"import"</code>.
   * @see #getElementTag(int).
   */
  public static final int IMPORT = 4;

  /**
   * The element tag is <code>"input"</code>.
   * @see #getElementTag(int).
   */
  public static final int INPUT = 5;

  /**
   * The element tag is <code>"message"</code>.
   * @see #getElementTag(int).
   */
  public static final int MESSAGE = 6;

  /**
   * The element tag is <code>"operation"</code>.
   * @see #getElementTag(int).
   */
  public static final int OPERATION = 7;

  /**
   * The element tag is <code>"output"</code>.
   * @see #getElementTag(int).
   */
  public static final int OUTPUT = 8;

  /**
   * The element tag is <code>"part"</code>.
   * @see #getElementTag(int).
   */
  public static final int PART = 9;

  /**
   * The element tag is <code>"port"</code>.
   * @see #getElementTag(int).
   */
  public static final int PORT = 10;

  /**
   * The element tag is <code>"portType"</code>.
   * @see #getElementTag(int).
   */
  public static final int PORT_TYPE = 11;

  /**
   * The element tag is <code>"service"</code>.
   * @see #getElementTag(int).
   */
  public static final int SERVICE = 12;

  /**
   * The element tag is <code>"types"</code>.
   * @see #getElementTag(int).
   */
  public static final int TYPES = 13;

  /**
   * The element tag is <code>"binding"</code>.
   * @see #nodeType(String localName).
   */
  public static final String BINDING_ELEMENT_TAG = "binding"; //$NON-NLS-1$

  /**
   * The element tag is <code>"definitions"</code>.
   * @see #nodeType(String localName).
   */
  public static final String DEFINITION_ELEMENT_TAG = "definitions"; //$NON-NLS-1$

  /**
   * The element tag is <code>"documentation"</code>.
   * @see #nodeType(String localName).
   */
  public static final String DOCUMENTATION_ELEMENT_TAG = "documentation"; //$NON-NLS-1$

  /**
   * The element tag is <code>"fault"</code>.
   * @see #nodeType(String localName).
   */
  public static final String FAULT_ELEMENT_TAG = "fault"; //$NON-NLS-1$

  /**
   * The element tag is <code>"import"</code>.
   * @see #nodeType(String localName).
   */
  public static final String IMPORT_ELEMENT_TAG = "import"; //$NON-NLS-1$

  /**
   * The element tag is <code>"input"</code>.
   * @see #nodeType(String localName).
   */
  public static final String INPUT_ELEMENT_TAG = "input"; //$NON-NLS-1$

  /**
   * The element tag is <code>"message"</code>.
   * @see #nodeType(String localName).
   */
  public static final String MESSAGE_ELEMENT_TAG = "message"; //$NON-NLS-1$

  /**
   * The element tag is <code>"operation"</code>.
   * @see #nodeType(String localName).
   */
  public static final String OPERATION_ELEMENT_TAG = "operation"; //$NON-NLS-1$

  /**
   * The element tag is <code>"output"</code>.
   * @see #nodeType(String localName).
   */
  public static final String OUTPUT_ELEMENT_TAG = "output"; //$NON-NLS-1$

  /**
   * The element tag is <code>"part"</code>.
   * @see #nodeType(String localName).
   */
  public static final String PART_ELEMENT_TAG = "part"; //$NON-NLS-1$

  /**
   * The element tag is <code>"port"</code>.
   * @see #nodeType(String localName).
   */
  public static final String PORT_ELEMENT_TAG = "port"; //$NON-NLS-1$

  /**
   * The element tag is <code>"portType"</code>.
   * @see #nodeType(String localName).
   */
  public static final String PORT_TYPE_ELEMENT_TAG = "portType"; //$NON-NLS-1$

  /**
   * The element tag is <code>"service"</code>.
   * @see #nodeType(String localName).
   */
  public static final String SERVICE_ELEMENT_TAG = "service"; //$NON-NLS-1$

  /**
   * The element tag is <code>"types"</code>.
   * @see #nodeType(String localName).
   */
  public static final String TYPES_ELEMENT_TAG = "types"; //$NON-NLS-1$

  // common

  /**
   * The attribute is <code>"name"</code>.
   * @see #getAttribute(Element,String).
   */
  public static final String NAME_ATTRIBUTE = "name"; //$NON-NLS-1$

  /**
   * The attribute is <code>"message"</code>.
   * @see #getAttribute(Element,String).
   */
  public static final String MESSAGE_ATTRIBUTE = "message"; //$NON-NLS-1$

  /**
   * The attribute is <code>"binding"</code>.
   * @see #getAttribute(Element,String).
   */
  public static final String BINDING_ATTRIBUTE = "binding"; //$NON-NLS-1$

  /**
   * The attribute is <code>"type"</code>.
   * @see #getAttribute(Element,String).
   */
  public static final String TYPE_ATTRIBUTE = "type"; //$NON-NLS-1$

  // definitions

  /**
   * The attribute is <code>"encoding"</code>.
   * @see #getAttribute(Element,String).
   */
  public final static String ENCODING_ATTRIBUTE = "encoding"; //$NON-NLS-1$

  /**
   * The attribute is <code>"targetNamespace"</code>.
   * @see #getAttribute(Element,String).
   */
  public final static String TARGETNAMESPACE_ATTRIBUTE = "targetNamespace"; //$NON-NLS-1$

  // binding

  /**
   * The attribute is <code>"resourceURI"</code>.
   * @see #getAttribute(Element,String).
   */
  public final static String RESOURCE_URI_ATTRIBUTE = "resourceURI"; //$NON-NLS-1$

  // part

  /**
   * The attribute is <code>"element"</code>.
   * @see #getAttribute(Element,String).
   */
  public final static String ELEMENT_ATTRIBUTE = "element"; //$NON-NLS-1$

  // import

  /**
   * The attribute is <code>"location"</code>.
   * @see #getAttribute(Element,String).
   */
  public final static String LOCATION_ATTRIBUTE = "location"; //$NON-NLS-1$

  // operation

  /**
   * The attribute is <code>"parameterOrder"</code>.
   * @see #getAttribute(Element,String).
   */
  public final static String PARAMETER_ORDER_ATTRIBUTE = "parameterOrder"; //$NON-NLS-1$
  
  /**
   * The attribute is <code>"namespace"</code>.
   * @see #getAttribute(Element,String).
   */
  public static final String NAMESPACE_ATTRIBUTE = "namespace"; //$NON-NLS-1$

  /**
   * The value <code>"http://schemas.xmlsoap.org/wsdl/"</code>.
   */
  public static final String WSDL_NAMESPACE_URI = "http://schemas.xmlsoap.org/wsdl/"; //$NON-NLS-1$

  /**
   * The value <code>"http://www.w3.org/2001/XMLSchema"</code>.
   */
  public static final String XSD_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$

  /**
   * The value <code>"http://www.w3.org/1999/XMLSchema"</code>.
   */
  public static final String SCHEMA_FOR_SCHEMA_URI_1999 = "http://www.w3.org/1999/XMLSchema"; //$NON-NLS-1$

  /**
   * The value <code>"http://www.w3.org/2000/10/XMLSchema"</code>.
   */
  public static final String SCHEMA_FOR_SCHEMA_URI_2000_10 = "http://www.w3.org/2000/10/XMLSchema"; //$NON-NLS-1$

  /**
   * The value <code>"http://www.w3.org/2001/XMLSchema"</code>.
   */
  public static final String SCHEMA_FOR_SCHEMA_URI_2001 = "http://www.w3.org/2001/XMLSchema"; //$NON-NLS-1$

  private static final String[] ELEMENT_TAGS = new String []{
    BINDING_ELEMENT_TAG,
    DEFINITION_ELEMENT_TAG,
    DOCUMENTATION_ELEMENT_TAG,
    FAULT_ELEMENT_TAG,
    IMPORT_ELEMENT_TAG,
    INPUT_ELEMENT_TAG,
    MESSAGE_ELEMENT_TAG,
    OPERATION_ELEMENT_TAG,
    OUTPUT_ELEMENT_TAG,
    PART_ELEMENT_TAG,
    PORT_ELEMENT_TAG,
    PORT_TYPE_ELEMENT_TAG,
    SERVICE_ELEMENT_TAG,
    TYPES_ELEMENT_TAG };

  /**
   * Returns a String name of element tag given the nodeType.
   * @param nodeType a constant defined in WSDLConstants.
   * @return element tag.
   */
  public static final String getElementTag(int nodeType)
  {
    return ELEMENT_TAGS[nodeType];
  }

  /**
   * Returns a node type given the String name of element tag.
   * @param localName the local name of element tag.
   * @return a node type defined in WSDLConstants.
   */
  public static final int nodeType(String localName)
  {
    for (int i = 0; i < ELEMENT_TAGS.length; ++i)
    {
      if (localName.equals(ELEMENT_TAGS[i]))
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns a node type defined in WSDLConstants given the DOM node.
   * @param node a DOM node.
   * @return a node type defined in WSDLConstants.
   */
  public static final int nodeType(Node node)
  {
    return isWSDLNamespace(node.getNamespaceURI()) ? nodeType(node.getLocalName()) : -1;
  }

  /**
   * Returns whether the given namespace is the WSDL namespace or not.
   * @param namespace a namespace.
   * @return whether the given namespace is the WSDL namespace or not.
   */
  public static boolean isWSDLNamespace(String namespace)
  {
    return WSDL_NAMESPACE_URI.equals(namespace);
  }

  /**
   * Returns true if namespace1 equals to namespace2.
   * @param namespace1 a namespace.
   * @param namespace2 a namespace.
   * @return true if namespace1 equals to namespace2.
   */
  public static boolean isMatchingNamespace(String namespace1, String namespace2)
  {
    return (namespace1 == null ? namespace2 == null : namespace1.equals(namespace2));
  }

  /**
   * Returns an attribute value given the attributeName in the element.
   * @param element a DOM element to search for the attribute from.
   * @param attributeName an attribute to find from the element.
   * @return an attribute value.
   */
  public static String getAttribute(Element element, String attributeName)
  {
    return element.hasAttribute(attributeName) ? element.getAttribute(attributeName) : null;
  }
}
