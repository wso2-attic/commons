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
package org.eclipse.wst.wsi.internal.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.validator.BaseValidator;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLVisitor;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * The class extracts XSD schema information from the given WSDL.
 * 
 * @author Kulik
 */
public final class TypesRegistry implements WSITag, WSDLVisitor
{
  List schemaProcessedList = new ArrayList();
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Part, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Part obj, Object parent, WSDLTraversalContext ctx)
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Service, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Service obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Types, Object,
   * WSDLTraversalContext)
   */
  public void visit(Types obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Operation, Object,
   * WSDLTraversalContext)
   */
  public void visit(Operation obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Input, Object,
   * WSDLTraversalContext)
   */
  public void visit(Input obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Output, Object,
   * WSDLTraversalContext)
   */
  public void visit(Output obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Fault, Object,
   * WSDLTraversalContext)
   */
  public void visit(Fault obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Binding, Object,
   * WSDLTraversalContext)
   */
  public void visit(Binding obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(BindingOperation, Object,
   * WSDLTraversalContext)
   */
  public void visit(
    BindingOperation obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(BindingInput, Object,
   * WSDLTraversalContext)
   */
  public void visit(BindingInput obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(BindingOutput, Object,
   * WSDLTraversalContext)
   */
  public void visit(BindingOutput obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(BindingFault, Object,
   * WSDLTraversalContext)
   */
  public void visit(BindingFault obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Element, Object,
   * WSDLTraversalContext)
   */
  public void visit(Element obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Message, Object,
   * WSDLTraversalContext)
   */
  public void visit(Message obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Port, Object,
   * WSDLTraversalContext)
   */
  public void visit(Port obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(PortType, Object,
   * WSDLTraversalContext)
   */
  public void visit(PortType obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(Definition, Object,
   * WSDLTraversalContext)
   */
  public void visit(Definition obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(ExtensibilityElement,
   * Object, WSDLTraversalContext)
   */
  public void visit(
    ExtensibilityElement obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPBinding, Object,
   * WSDLTraversalContext)
   */
  public void visit(SOAPBinding obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPBody, Object,
   * WSDLTraversalContext)
   */
  public void visit(SOAPBody obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPHeader, Object,
   * WSDLTraversalContext)
   */
  public void visit(SOAPHeader obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPHeaderFault, Object,
   * WSDLTraversalContext)
   */
  public void visit(
    SOAPHeaderFault obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPFault, Object,
   * WSDLTraversalContext)
   */
  public void visit(SOAPFault obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(SOAPOperation, Object,
   * WSDLTraversalContext)
   */
  public void visit(SOAPOperation obj, Object parent, WSDLTraversalContext ctx)
  {
  }

  private Map element2Type = new HashMap();
  private Set extendsArray = new HashSet();
  private Set usesWsdlArrayType = new HashSet();

  protected BaseValidator baseValidator = null;
 
  /**
   * Constructor creates the types registry. by the given WSDL definition
   * object.
   * 
   * @param def a WSDL definition.
   * @param baseValidator a base validator.
   */
  public TypesRegistry(Definition def, BaseValidator baseValidator)
  {
    this.baseValidator = baseValidator;
    if (def == null)
      throw new IllegalArgumentException("Definition can not be null");

    WSDLTraversal traversal = new WSDLTraversal();
    //VisitorAdaptor.adapt(this);
    traversal.setVisitor(this);
    traversal.visitImport(true);
    
    processTypes(def.getTypes(), def.getDocumentBaseURI());
    traversal.traverse(def);
  }

  /**
   * Constructor creates the types registry by given WSDL types object and
   * location context URI.
   * @param types WSDL types object.
   * @param context location context URI.
   * @param baseValidator a base validator.
   */
  public TypesRegistry(
    Types types,
    String context,
    BaseValidator baseValidator)
  {
    this.baseValidator = baseValidator;
    processTypes(types, context);
  }

  /**
   * The method returns type's qname by given element's qname.
   * @param elementName a qualified element name.
   * @return type's qname by given element's qname.
   */
  public QName getType(QName elementName)
  {
    return (QName) element2Type.get(elementName);
  }

  /**
   * The method returns set of types which is array attribute.
   * @return set of types which is array attribute.
   */
  public Set getArrayTypes()
  {
    Set s = new HashSet();
    s.addAll((Collection) usesWsdlArrayType);
    s.addAll((Collection) extendsArray);
    return s;
  }

  /**
   * The method returns set of elements defined in types element.
   * @return set of elements defined in types element.
   */
  public Set getElementList()
  {
    return this.element2Type.keySet();
  }

  /**
   * The method returns true if the given type declares wsdl:arrayType
   * attribute within declaration.
   * @param type a type.
   * @return true if the given type declares wsdl:arrayType
   * attribute within declaration.
   */
  public boolean isUsesWSDLArrayType(QName type)
  {
    return usesWsdlArrayType.contains(type);
  }

  /**
   * The method returns true if given type extends soapenc:Array type.
   * @param type a type.
   * @return true if given type extends soapenc:Array type.
   */
  public boolean isExtendsArray(QName type)
  {
    return extendsArray.contains(type);
  }

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.Import, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(Import im, Object parent, WSDLTraversalContext ctx)
  {
    if (im.getDefinition() != null)
      processWSDL(im.getDefinition());
  }

  /**
   * Internal method processes WSDL definition.
   * @param def a WSDL definition.
   */
  private void processWSDL(Definition def)
  {
    if (def.getTypes() != null)
      processTypes(def.getTypes(), def.getDocumentBaseURI());
  }

  /**
   * Internal method processes WSDL types.
   * @param types WSDL types.
   * @param context a context.
   */
  private void processTypes(Types types, String context)
  {
    if (types == null)
      return;
    List exts = types.getExtensibilityElements();
    if (exts != null)
    {
      Iterator it = exts.iterator();
      while (it.hasNext())
      {
        ExtensibilityElement el = (ExtensibilityElement) it.next();
        if (el instanceof Schema)
          searchForSchema(
            ((Schema) el).getElement(),
            context);
      }
    }
  }

  /**
   * Internal method searches XSD schema declaration and XSD import
   * statements.
   * @param n
   * @param context
   */
  private void searchForSchema(Node n, String context)
  {
    while (n != null)
    {
      // searches for xsd:import element
      if (Node.ELEMENT_NODE == n.getNodeType())
      {
        // if xsd:schema element is found -> process schema
        if (XMLUtils.equals(n, ELEM_XSD_SCHEMA))
          processSchema(n, context, new ArrayList());
      }
      n = n.getNextSibling();
    }
  }

  /**
   * Internal method loads XSD schema by using schema location and
   * location context.
   * @param importNode
   * @param context
   */
  private void loadSchema(Node importNode, String context, List processedSchemas)
  {
    Element im = (Element) importNode;
    Attr schemaLocation = XMLUtils.getAttribute(im, ATTR_XSD_SCHEMALOCATION);
	
    // try to parse imported XSD
    if (schemaLocation != null && schemaLocation.getValue() != null)
      try
      {
    	String urlString = XMLUtils.createURLString(schemaLocation.getValue(), context);
    	if (!schemaProcessedList.contains(urlString))
    	{
          // if any error or root element is not XSD schema -> error
          Document schema =
            baseValidator.parseXMLDocumentURL(
              schemaLocation.getValue(),
              context,
              null);
          schemaProcessedList.add(urlString);
          if (XMLUtils.equals(schema.getDocumentElement(), ELEM_XSD_SCHEMA))
            processSchema(
              schema.getDocumentElement(),
              urlString, processedSchemas);
    	}
      }
      catch (Throwable t)
      {
        //t.printStackTrace();
        // nothing. it's not a schema
      }
  }

  /**
   * Internal method processes XSD schema and retrieves types declaration from
   * it.
   * @param schema
   * @param context
   */
  private void processSchema(Node schema, String context, List processedSchemas)
  {
	if ((schema != null) && (!processedSchemas.contains(schema)))
	{
	  processedSchemas.add(schema);
      Attr a = XMLUtils.getAttribute((Element) schema, ATTR_XSD_TARGETNAMESPACE);
      String targetNamespace = (a != null) ? a.getValue() : "";
      // iterate schema
      Node n = schema.getFirstChild();
      // !! we suppose that xsd:import element is occured only within xsd:schema element        
      while (n != null)
      {
        if (Node.ELEMENT_NODE == n.getNodeType())
        {
          if (XMLUtils.equals(n, ELEM_XSD_ELEMENT))
          {
            Element el = (Element) n;
            a = XMLUtils.getAttribute(el, ATTR_XSD_NAME);
            QName element =
              new QName(targetNamespace, (a != null) ? a.getValue() : "");

            a = XMLUtils.getAttribute(el, ATTR_XSD_TYPE);
            QName type = null;
            if (a != null)
            {
              String t = a.getValue();
              // if type contains ':', it means that it contains qname
              int i = t.indexOf(':');
              if (i != -1)
              {
                String prefix = t.substring(0, i);
                String nsURI = XMLUtils.findNamespaceURI(n, prefix);
                type = new QName(nsURI, t.substring(i + 1));
              }
              else
                type = new QName(targetNamespace, t);
            }
            else
            {
              // suppose that element directly contains type declaration
              type = element;
              checkType(n, type);
            }

            element2Type.put(element, type);
          }
          else if (XMLUtils.equals(n, ELEM_XSD_IMPORT))
            loadSchema(n, context, processedSchemas);
          else if (XMLUtils.equals(n, ELEM_XSD_INCLUDE))
            loadSchema(n, context, processedSchemas);
          else if (XMLUtils.equals(n, ELEM_XSD_COMPLEXTYPE))
          {
            Element el = (Element) n;
            a = XMLUtils.getAttribute(el, ATTR_XSD_NAME);
            QName type =
              new QName(targetNamespace, (a != null) ? a.getValue() : "");
            checkType(n, type);
          }

        }

        n = n.getNextSibling();
      }
	}
  }

  /**
   * Internal method checks whether specified type has WSDL array type
   * attribute in the XSD declaration.
   * @param n
   * @param name
   */
  private void checkType(Node n, QName name)
  {
    while (n != null)
    {
      if (Node.ELEMENT_NODE == n.getNodeType())
      {
        // check such sentence
        //              xsd:attribute ref="soapenc:arrayType" wsdl:arrayType="tns:MyArray2Type[]"/>
        if (XMLUtils.equals(n, ELEM_XSD_ATTRIBUTE))
        {
          Attr a = XMLUtils.getAttribute((Element) n, ATTR_WSDL_ARRAYTYPE);
          if (a != null)
            usesWsdlArrayType.add(name);
        }

        // retrieve base attribute may be from restriction or extension
        Attr a = XMLUtils.getAttribute((Element) n, ATTR_XSD_BASE);
        if (a != null)
        {
          String base = a.getValue();
          int i = base.indexOf(":");
          if (i != -1)
          {
            String prefix = base.substring(0, i);
            String local = base.substring(i + 1);
            String namespace = XMLUtils.findNamespaceURI(n, prefix);
            if (SOAPENC_ARRAY.equals(new QName(namespace, local)))
              extendsArray.add(name);
          }
        }
        checkType(n.getFirstChild(), name);
      }
      n = n.getNextSibling();
    }
  }
}
