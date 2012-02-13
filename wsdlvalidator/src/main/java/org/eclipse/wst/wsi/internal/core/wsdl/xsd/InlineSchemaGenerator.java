/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.wsdl.xsd;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.wsdl.extensions.schema.SchemaConstants;

/**
 * Generate a String representation of a schema for an inline schema. Will add imports for unresolved
 * namespaces.
 * 
 * @author Lawrence Mandel, IBM
 */
public class InlineSchemaGenerator
{
  protected final String SOAP_ENCODING_URI = "http://schemas.xmlsoap.org/soap/encoding/";
  protected final String XMLNS = "xmlns";
  protected final String TARGETNAMESPACE = "targetNamespace";
  protected final String NAMESPACE = "namespace";
  protected final String IMPORT = "import";
  protected final String INCLUDE = "include";
  protected final String SCHEMA = "schema";
  protected final String SCHEMALOCATION = "schemaLocation";
  protected final String TYPE = "type";
  protected final String NAME = "name";
  protected final String[] ignoreNamespaces =
    { SchemaConstants.NS_URI_XSD_1999, SchemaConstants.NS_URI_XSD_2000, SchemaConstants.NS_URI_XSD_2001 };

  protected static InlineSchemaGenerator instance = null;

  /**
   * Constructor.
   */
  protected InlineSchemaGenerator()
  {
  }

  /**
   * Get the instance of the InlineSchemaGenerator.
   * 
   * @return The instance of the inline schema generator.
   */
  protected static InlineSchemaGenerator getInstance()
  {
    if (instance == null)
    {
      instance = new InlineSchemaGenerator();
    }
    return instance;
  }
  
  /**
   * Create a string representation of a schema from the element provided.
   * 
   * @param element The root element of the schema.
   * @param elements A list of the elements in the schema in order.
   * @param filelocation The URI of the file that contains the schema.
   * @return A string representation of a schema.
   */
  public static String createXSDString(Element element, List elements, String filelocation)
  {
    return InlineSchemaGenerator.createXSDString(element, elements, filelocation, new Hashtable());
  }
  
  /**
  	* Creates a String representing the schema model with the root element of
  	* extElem. Calls createXSDStringRecursively to take care of building the String
  	* after it obtains the Element from the UnknownExtensibilityElement.
  	* 
  	* @param element The root element of the schema.
  	* @param elements A list to contain the elements in the schema in order.
  	* @param filelocation The location of the file the schema is located in.
  	* @param parentNSs A hashtable of parent namespaces to used to resolve prefixes.
  	* @return A string representation of the schema with the root element 'element'.
  	*/
  public static String createXSDString(Element element, List elements, String filelocation, Hashtable parentNSs)
  {
    InlineSchemaGenerator schemaGenerator = InlineSchemaGenerator.getInstance();
    Hashtable nsResolver = schemaGenerator.getNSResolver(element);
    List reqns = schemaGenerator.getRequiredNamespaces(element);
    Hashtable reqNSDecl = schemaGenerator.resolveNamespaces(reqns, nsResolver, parentNSs);
    //Hashtable reqNSDecl = schemaGenerator.getRequiredNSDeclarations(reqns, nsResolver, parentNSs);
    List importNS = schemaGenerator.getImportNamespaces(element);
    reqns = schemaGenerator.removeImports(reqns, importNS);
    reqns = schemaGenerator.removeLocalNamespaces(reqns, element);
    return schemaGenerator.createXSDStringRecursively(element, elements, reqns, reqNSDecl, filelocation);
  }
  /**
   * Returns true if the SOAP encoding namespace is required but not imported.
   * 
   * @param element The root element of the schema.
   * @param filelocation The location of the file containing the schema.
   * @param parentNSs A hashtable of the parent namespaces.
   * @return True if the soap encoding namespace is required but not imported, false otherwise.
   */
  public static boolean soapEncodingRequiredNotImported(Element element, String filelocation, Hashtable parentNSs)
  {
    InlineSchemaGenerator schemaGenerator = InlineSchemaGenerator.getInstance();
    Hashtable nsResolver = schemaGenerator.getNSResolver(element);
    List reqns = null;

    reqns = schemaGenerator.getRequiredNamespaces(element);
    schemaGenerator.resolveNamespaces(reqns, nsResolver, parentNSs);
    //schemaGenerator.resolveUndeclaredNamespaces(reqns, parentNSs);
    List importNS = schemaGenerator.getImportNamespaces(element);
    reqns = schemaGenerator.removeImports(reqns, importNS);
    reqns = schemaGenerator.removeLocalNamespaces(reqns, element);

    return schemaGenerator.checkSOAPEncodingRequired(reqns);
  }

  /**
   * This recursive method creates the schema String from the root Element.
   * 
   * @param elem The root element of the schema.
   * @param elements A list to be created of the elements in the schema in order.
   * @param requiredNamespaces A list of required namespaces.
   * @param reqNSDecl A hashtable of required namespace declarations.
   * @param filelocation The uri of the file that contains this schema.
   * @return A string representation of this schema.
   */
  protected String createXSDStringRecursively(
    Element elem,
    List elements,
    List requiredNamespaces,
    Hashtable reqNSDecl,
    String filelocation)
  {
    if (elem == null)
      return ""; // just in case

    elements.add(elem);

    StringBuffer xsdString = new StringBuffer();
    String elementName = elem.getTagName();
    xsdString.append("<").append(elementName);

    boolean foundSchemaLocation = false; // flag if schemalocation is defined
    String namespace = ""; // the namespace if we're checking an import or include
    String namePrefix = ""; // the xmlns prefix used for the elements
    // Get all of the attributes for this element and append them to the xsdString
    NamedNodeMap atts = elem.getAttributes();
    for (int i = 0; i < atts.getLength(); i++)
    {
      Node n = atts.item(i);
      xsdString.append(" ").append(n.getNodeName()).append("=\"");
      String nodeName = n.getNodeName();
      if (nodeName.equalsIgnoreCase(SCHEMALOCATION) && filelocation != null)
      {
        foundSchemaLocation = true;
        String relativePath = n.getNodeValue();
        xsdString.append(relativePath).append("\"");   
      }
      else
      {
        String nodeValue = n.getNodeValue();
        if (nodeName.equalsIgnoreCase(NAMESPACE))
        {
          namespace = nodeValue;
        }
        // get the name prefix for this schema to use in generating import statements
        else if (nodeName.indexOf(XMLNS) != -1)
        {

          if (nodeValue.equalsIgnoreCase(elem.getNamespaceURI()))
          {
            namePrefix = nodeName;
            if (namePrefix.equalsIgnoreCase(XMLNS))
            {
              namePrefix = "";
            }
            else
            {
              namePrefix = namePrefix.substring(6) + ":";
            }
          }
        }
        // Replace old schema namespaces with the new schema namespace.
        if(nodeValue.equals(SchemaConstants.NS_URI_XSD_1999) || nodeValue.equals(SchemaConstants.NS_URI_XSD_2000))
         {
          nodeValue = SchemaConstants.NS_URI_XSD_2001;
        }
        xsdString.append(nodeValue).append("\"");
      }
    }
    if (elementName.equalsIgnoreCase("import") && !foundSchemaLocation)
    {
      xsdString.append(" ").append(SCHEMALOCATION).append("=\"").append(namespace).append("\"");
    }
    // add in any required NS declarations from parent elements
    if (reqNSDecl != null)
    {
      Enumeration keys = reqNSDecl.keys();
      while (keys.hasMoreElements())
      {
        String key = (String)keys.nextElement();
        String declNS = (String)reqNSDecl.get(key);
        if(declNS.equals(SchemaConstants.NS_URI_XSD_1999) || declNS.equals(SchemaConstants.NS_URI_XSD_2000))
         {
          declNS = SchemaConstants.NS_URI_XSD_2001;
        }
        xsdString.append(" ").append(key).append("=\"").append(declNS).append("\"");
      }

    }
    xsdString.append(">");
    if (requiredNamespaces != null)
    {
      Iterator iRequiredNamespaces = requiredNamespaces.iterator();
      while (iRequiredNamespaces.hasNext())
      {
        String ns = (String)iRequiredNamespaces.next();

        xsdString
          .append("<")
          .append(namePrefix)
          .append(IMPORT)
          .append(" ")
          .append(NAMESPACE)
          .append("=\"")
          .append(ns)
          .append("\" ")
          .append(SCHEMALOCATION)
          .append("=\"")
          .append(ns)
          .append("\"/>");
      }

    }
    xsdString.append("\n");

    // call the method recursively for each child element
    NodeList childNodes = elem.getChildNodes();

    for (int i = 0; i < childNodes.getLength() || i < 5; i++)
    {
      Node n = childNodes.item(i);
      // we only want nodes that are Elements
      if (n instanceof Element)
      {
        Element child = (Element)n;
        xsdString.append(createXSDStringRecursively(child, elements, null, null, filelocation));
      }
    }

    xsdString.append("</").append(elem.getTagName()).append(">");

    return xsdString.toString();

  }
  /**
   * Get a list of all the namespaces that are used for elements or types in the schema.
   * These are the required namespaces in order to ensure that all the elments are valid.
   * 
   * @param elem The element to check for required namespaces.
   * @return A list of required namespaces for the element and all its children.
   */
  protected List getRequiredNamespaces(Element elem)
  {
    List namespace = new Vector();

    // call the method recursively for each child element
    // register all the child types first
    NodeList childNodes = elem.getChildNodes();
    int numChildren = childNodes.getLength();
    // TODO: why is there a < 5 condition?
    for (int i = 0; i < numChildren /*|| i < 5*/; i++)
    {
      Node n = childNodes.item(i);
      // we only want nodes that are Elements
      if (n instanceof Element)
      {
        Element child = (Element)n;
        List childns = getRequiredNamespaces(child);
        for (int j = childns.size() - 1; j >= 0; j--)
        {
          String ns = (String)childns.get(j);
          
          if (!namespace.contains(ns))
          {
            namespace.add(ns);
          }
        }
      }
    }
    // Add the namespace of the current element
    String elemNS = elem.getPrefix();
    // if there is no namespace prefix set it to the empty prefix.
    if(elemNS == null)
    {
      elemNS = "";
    }
    if (!namespace.contains(elemNS.intern()))
     {
      namespace.add(elemNS.intern());
    }
    // now add all of the current element's namespaces
    // don't include import and schema elements
    String localname = elem.getLocalName();
    if (!localname.equals(IMPORT) && !localname.equals(INCLUDE) && !localname.equals(SCHEMA))
    {
      NamedNodeMap atts = elem.getAttributes();
      for (int i = 0; i < atts.getLength(); i++)
      {
        Node n = atts.item(i);

        String nodeName = n.getNodeName();
        // removed restriction that we're only looking at types
        //		if (nodeName.equalsIgnoreCase(TYPE))
        //		{
        // don't take namespace info from attributes defining namespaces.
        // that includes xmlns, targetNamespace
        // don't take namespace info from name attributes
        if (nodeName.indexOf(XMLNS) != -1 || nodeName.equals(TARGETNAMESPACE) || nodeName.equals(NAME))
        {
          continue;
        }
        String nodeValue = n.getNodeValue();
        
        
        int colonIndex = nodeValue.indexOf(":");
        // Don't take namespace info from attributes with the default namespace, that is attributes
        // that are not prefixed. (colonIndex == -1)
        // If the colonIndex is followed by a / then it is a URI and not
        // namespace qualified.
        if (colonIndex == -1 || nodeValue.charAt(colonIndex + 1) == '/')
        {
          continue;
        }
        // here we have found a colon delimiter so we need the namespace defined here
        else
        {
          nodeValue = nodeValue.substring(0, colonIndex);
        }
        if (!namespace.contains(nodeValue.intern()))
        {

          namespace.add(nodeValue.intern());
        }
        //		}
      }
    }

    return namespace;

  }

  /**
   * Get a list of all the namespaces that have an import statement.
   * 
   * @param elem The root element of the schema.
   * @return A list of all the namespaces that are imported.
   */
  protected List getImportNamespaces(Element elem)
  {
    List namespace = new Vector();

    // call the method recursively for each child element
    // register all the child types first
    NodeList childNodes = elem.getChildNodes();

    for (int i = 0; i < childNodes.getLength() || i < 5; i++)
    {
      Node n = childNodes.item(i);
      // we only want nodes that are Elements
      if (n instanceof Element)
      {
        Element child = (Element)n;
        List childns = getImportNamespaces(child);
        for (int j = childns.size() - 1; j >= 0; j--)
        {
          String ns = (String)childns.get(j);
          if (!namespace.contains(ns))
          {
            namespace.add(ns);
          }
        }
      }
    }
    // if this is an import element get the namespace and add it to the list
    if (elem.getLocalName().equalsIgnoreCase(IMPORT))
    {
      NamedNodeMap atts = elem.getAttributes();
      for (int i = 0; i < atts.getLength(); i++)
      {
        Node n = atts.item(i);

        String nodeName = n.getNodeName();
        if (nodeName.equalsIgnoreCase(NAMESPACE))
        {
          String nodeValue = n.getNodeValue();
          if (!namespace.contains(nodeValue.intern()))
          {

            namespace.add(nodeValue.intern());
          }
        }
      }
    }

    return namespace;

  }

  /**
   * Return a Hashtable with namespace prefixes as keys from the given element.
   * 
   * @param elem The root element of the schema.
   * @return A hashtable with namespace prefixes mapped to namespaces.
   */
  protected Hashtable getNSResolver(Element elem)
  {
    Hashtable nsResolver = new Hashtable();

    NamedNodeMap atts = elem.getAttributes();
    for (int i = 0; i < atts.getLength(); i++)
    {
      Node n = atts.item(i);

      String nodeName = n.getNodeName();
      if (nodeName.indexOf(XMLNS) != -1)
      {
        String nodeValue = n.getNodeValue();
        String namePrefix = nodeName;

        if (namePrefix.equalsIgnoreCase(XMLNS))
        {
          namePrefix = "";
        }
        else
        {
          namePrefix = namePrefix.substring(6);
        }
        nsResolver.put(namePrefix, nodeValue);

      }
    }
    return nsResolver;

  }

  /**
   * Resolve the namespaces in the given namespaces list with the two namespace
   * resolver hashtables provided. Return a list of all the namespace that need
   * to be declared.
   * First resolve against the local namespaces with nsResolver.
   * Next resolve against the parent namespace with parentNSResolver.
   * A side affect of this method is the namespaces list is left with only those 
   * namespaces that are resolved and the resolved entities are placed in the 
   * list instead of the original entries.
   * For ex. If you provide a list such as {xsd, intf} and only xsd can be resolved
   * you will end up with the list {http://www.w3.org/2001/XMLSchema}
   * 
   * @param namespaces The list of namespaces to resolve.
   * @param nsResolver The hashtable to be used as the local resolver.
   * @param parentNSResolver The hashtable to be used as the parent namespace resolver.
   * @return A Hashtable of namespaces that must be declared.
   */
  protected Hashtable resolveNamespaces(List namespaces, Hashtable nsResolver, Hashtable parentNSResolver)
  {
  	Hashtable reqNSDecl = new Hashtable();
    if (namespaces != null && !namespaces.isEmpty() && nsResolver != null)
    {
      for (int i = namespaces.size() - 1; i >= 0; i--)
      {
        String ns = (String)namespaces.get(i);
        // Remove the namespace from the list.
        namespaces.remove(i);
        // First try to resolve against the local namespace resolver.
        if (nsResolver.containsKey(ns))
        {
          Object resolvedNS = nsResolver.get(ns);
          // Only add the namespace if it's not already in the list.
          if(!namespaces.contains(resolvedNS))
          {
            namespaces.add(i, nsResolver.get(ns));
          }
        }
        // Next try to resolve against the parent namespace resolver.
        else
        {
        	if (ns.equals(""))
            {
              ns = XMLNS;
            }
            else
            {
              ns = XMLNS + ":" + ns;
            }
            if (parentNSResolver.containsKey(ns))
            {
              Object resolvedNS = parentNSResolver.get(ns);
              // Only add the namespace if it's not already in the list.
              if(!namespaces.contains(resolvedNS))
              {
                namespaces.add(i, resolvedNS);
              }
              // Still need to declare the namespace though.
              reqNSDecl.put(ns, resolvedNS);
            }
        }

      }
    }
    return reqNSDecl;
  }

  /**
   * Remove any namespace from the namespaces list if it is in the import list.
   * 
   * @param namespaces The namespaces list.
   * @param importedNamespaces A list of imported namespaces.
   * @return The list of namespaces without the imported namespaces.
   */
  protected List removeImports(List namespaces, List importedNamespaces)
  {
    if (namespaces != null && importedNamespaces != null && !importedNamespaces.isEmpty())
    {
      Iterator iImportedNS = importedNamespaces.iterator();
      while (iImportedNS.hasNext())
      {
        String iNS = (String)iImportedNS.next();

        namespaces.remove(iNS);
      }
    }
    return namespaces;
  }

  /**
   * Remove the local namespace for the schema and the namespaces listed in the ignoreNamespaces
   * list from the namespaces list provided.
   * 
   * @param namespaces The list of local namespaces.
   * @param elem The root element of the schema.
   * @return The list of namespaces with the local namespaces removed.
   */
  protected List removeLocalNamespaces(List namespaces, Element elem)
  {
    if (namespaces != null && elem != null)
    {
      String ns = elem.getAttribute(TARGETNAMESPACE);
      namespaces.remove(ns);

      for (int i = ignoreNamespaces.length - 1; i >= 0; i--)
      {
        // keep removing the namespace until it is not in the list
        if (namespaces.remove(ignoreNamespaces[i]))
        {
          i++;
        }
      }
    }
    return namespaces;
  }
  
  /**
   * Returns true if the SOAP encoding namespace is in the list of required namespaces,
   * false otherwise.
   * 
   * @param reqns The list of namespaces to check for the SOAP encoding namespace.
   * @return True if the SOAP encoding namespaces is in the list, false otherwise.
   */
  protected boolean checkSOAPEncodingRequired(List reqns)
  {
    if (reqns.contains(SOAP_ENCODING_URI))
    {
      return true;
    }
    return false;
  }
}
