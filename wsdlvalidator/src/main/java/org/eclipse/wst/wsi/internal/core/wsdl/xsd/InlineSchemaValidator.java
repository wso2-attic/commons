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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.schema.Schema;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.ibm.wsdl.Constants;
import com.ibm.wsdl.extensions.schema.SchemaConstants;

/**
 * Plugin validator for the WSDL Validation framework. Validates inline schema found in a WSDL document.
 * 
 * @author Lawrence Mandel (lmandel@ca.ibm.com)
 */
public class InlineSchemaValidator
{
  List elements = null;


  public Map validate(Object element, List parents, String filename) throws Exception
  {
  	elements = new Vector();
  	Schema elem = (Schema) element;
    Definition wsdlDefinition = (Definition) parents.get(parents.size() - 1);
    // Add in the namespaces defined in the doc already that aren't defined locally in this schema.
    // There is no need to check for namespaces other then in the defintions and types elements as
    // inline schema can not have any other parents and must have there two parents.
    // First take care of the definitions element

    // create the inline schema string
	Element w3celement = elem.getElement();
	Hashtable parentnamespaces = getNamespaceDeclarationsFromParents(wsdlDefinition,w3celement);
	String targetNamespace = w3celement.getAttribute(Constants.ATTR_TARGET_NAMESPACE);
	// if the targetNamespace hasn't been defined for the schema use the 
	// targetNamespace of the definitions element
	/*if(targetNamespace == null || targetNamespace.equals(""))
	 {
	  targetNamespace = wsdlDefinition.getTargetNamespace();
	  w3celement.setAttribute(Constants.ATTR_TARGET_NAMESPACE,targetNamespace);
	}*/

	String xsd = InlineSchemaGenerator.createXSDString(w3celement, elements, filename, parentnamespaces);

    // If the namespace given is one of the old schema namespaces produce a warning.
    String namespace = w3celement.getNamespaceURI();
    if(namespace.equals(SchemaConstants.NS_URI_XSD_1999) || namespace.equals(SchemaConstants.NS_URI_XSD_2000))
    {
      throw new Exception("An old version of the schema namespace is specified.");
    }

    // now create and call the validator for the inline schema
    XSDValidator schemav = new XSDValidator();

    //String fileLocation = new URL(validatormanager.getFilename()).getPath();
    InlineXSDResolver inlineEntityResolver =
      getEntityResolver(wsdlDefinition, (Types) parents.get(0), filename, targetNamespace);
    //	add in the external XSD Catalog to resolve schemas offline
    XMLEntityResolverChain entityResolverChain = new XMLEntityResolverChain();
    entityResolverChain.addEntityResolver(inlineEntityResolver);
    entityResolverChain.addEntityResolver(new FileEntityResolver());
    entityResolverChain.addEntityResolver(XMLCatalogResolver.getInstance());
    schemav.validateInlineSchema(xsd, targetNamespace, filename, entityResolverChain, inlineEntityResolver);

    // check if the SOAP Encoding namespace is required but not imported
    if (InlineSchemaGenerator.soapEncodingRequiredNotImported(elem.getElement(), filename,parentnamespaces))
    {
      throw new Exception("The inline schema uses an element or type from "
        + "the SOAP encoding namespace but the namespace has not been imported. "
        + "The SOAP encoding namespace should be imported with an import "
        + "statement before it is used.");
    }

    // If the schema isn't valid
    if (!schemav.isValid())
    {
      // Gathering all the errors
      StringBuffer exception = new StringBuffer();
      Iterator i = schemav.getErrors().iterator();
      while (i.hasNext())
      {
        exception.append(i.next()).append('\n');
      }
      // throw an exception
      throw new Exception(exception.toString());
    }
    // if the schema is valid
    else
    {
      Map map = new HashMap(1);
      map.put(targetNamespace, schemav.getXSModel());
      return map;
    }
  }

  /**
   * Get an entity resolver that will resolve inline schemas.  Every inline schema is preregistered with
   * the resolver.
   * 
   * @param wsdlDefinition The WSDL definitions element.
   * @param types The types element.
   * @param referenceLocation The location of the file that contains this schema.
   * @param targetNamespace The targetNamespace of the schema.
   * @return An entity resolver that can resolve inline schemas.
   */
  protected InlineXSDResolver getEntityResolver(Definition wsdlDefinition, Types types, String referenceLocation, String targetNamespace)
  {
    InlineXSDResolver entityResolver = new InlineXSDResolver();
//    entityResolver.setReferenceLocation(referenceLocation);
    List schemas = types.getExtensibilityElements();
    if (schemas != null)
    {
      Iterator iSchemas = schemas.iterator();
      while (iSchemas.hasNext())
      {
    	Schema extElem = (Schema) iSchemas.next();
        String thisNamespace = extElem.getElement().getAttribute(Constants.ATTR_TARGET_NAMESPACE);
        if (thisNamespace != null && !thisNamespace.equalsIgnoreCase(targetNamespace))
        {

        	Element element = extElem.getElement();
			
//			create the inline schema string
			 //Element w3celement = elem.getElement();
			 Hashtable parentnamespaces = getNamespaceDeclarationsFromParents(wsdlDefinition,element);
			 String xsd = InlineSchemaGenerator.createXSDString(element, elements, referenceLocation, parentnamespaces);
        	//addNamespaceDeclarationsFromParents(wsdlDefinition,element);
          entityResolver.add(thisNamespace, xsd);
        }

      }
    }
    return entityResolver;
  }
  
  /**
   * Get the namespace declarations as in the form 
   * xmlns="somenamespace"
   * from the definitions and types elements and add them to the schema element so the schema 
   * validator will have access to them.
   * 
   * @param wsdlDefinition The WSDL definitions element.
   * @param element The types element.
   * @return A hashtable with the namespace elements from the elements provided.
   */
  protected Hashtable getNamespaceDeclarationsFromParents(Definition wsdlDefinition, Element element)
  {
  	Hashtable nss = new Hashtable();
    Iterator nameSpaces = wsdlDefinition.getNamespaces().keySet().iterator();

    String XMLNS = Constants.ATTR_XMLNS;
    
    while (nameSpaces.hasNext())
    {
      String nsprefix = XMLNS;
      String ns = (String) nameSpaces.next();
      if (!ns.equalsIgnoreCase(""))
      {
        nsprefix += ":";
      }
      if (!element.hasAttribute(nsprefix + ns))
      {
      	nss.put(nsprefix + ns, wsdlDefinition.getNamespace(ns));
//        element.setAttribute(nsprefix + ns, wsdlDefinition.getNamespace(ns));
      }

    }
    // Next handle the parent types element
    NamedNodeMap atts = element.getParentNode().getAttributes();
    int attslength = atts.getLength();
    for (int i = 0; i < attslength; i++)
    {
      Node tempnode = atts.item(i);
      String nodename = tempnode.getNodeName();
      // if this is a namespace attribute
      if (nodename.indexOf(XMLNS) != -1)
      {
		nss.put(nodename,  tempnode.getNodeValue());
        //element.setAttribute(nodename, tempnode.getNodeValue());
      }
    }
    return nss;
  }
  
  /**
   * Given a line number for the schema returns the element found on that line. 
   * Useful for obtaining elements from schema Strings.
   * 
   * @param line The line number for the schema.
   * @return The object located at the line or at line 0 if the line is invalid.
   */
   public Object getObjectAtLine(int line)
   {
   	if(line < 0 || line >= elements.size())
   	{
   	  line = 0;
   	}
	return elements.get(line);
   }
   
  /**
   * Replace any instance of the 2001 schema namespace in the given message with
   * the given namespace.
   * 
   * @param message The message to replace the namespace in.
   * @param namespace The namespace used for replacement.
   * @return The message with the 2001 schema namespace replaced by the given namespace.
   */
 // private String replaceNamespace(String message, String namespace)
 //  {
 //    String xsd2001 = Constants.NS_URI_XSD_2001;
 //    int start = message.indexOf(xsd2001);
 //    int end = start + xsd2001.length();
 //    if(start < 0)
 //    {
 //      return message;
 //    }
 //    String startString = message.substring(0,start);
 //    String endString = message.substring(end,message.length());
 //    return startString + namespace + endString;
 //  }
}
