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
package org.eclipse.wst.wsi.internal.core.wsdl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

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
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.dom.ElementImpl;
import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.ibm.wsdl.Constants;
import com.ibm.wsdl.extensions.schema.SchemaConstants;
import com.ibm.wsdl.util.StringUtils;
import com.ibm.wsdl.util.xml.DOMUtils;
import com.ibm.wsdl.util.xml.QNameUtils;
import com.ibm.wsdl.util.xml.XPathUtils;

/**
 * This class is a specialization of com.ibm.wsdl.xml.WSDLReaderImpl in WSDL4J.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class WSDLReaderImpl extends com.ibm.wsdl.xml.WSDLReaderImpl
{
  // WSDL element list.
  protected WSDLElementList wsdlElementList = new WSDLElementList();

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseDefinitions(java.lang.String, org.w3c.dom.Element, java.util.Map)
   */
  protected Definition parseDefinitions(
    String documentBaseURI,
    Element defEl,
    Map importedDefs)
    throws WSDLException
  {
	  Definition def = null;
	ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();   
	try
	{
   	  Thread.currentThread().setContextClassLoader(WSDLReaderImpl.class.getClassLoader());   
	
      def =
      super.parseDefinitions(documentBaseURI, defEl, importedDefs);

    // Try to add element to list
    addElementToList(defEl, def);


	}
catch (Exception e)
	{
	}
    finally
    { 
      Thread.currentThread().setContextClassLoader(currentLoader);
    }  
    return def;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseImport(org.w3c.dom.Element, javax.wsdl.Definition, java.util.Map)
   */
  protected Import parseImport(Element element, Definition definition, Map map)
    throws WSDLException
  {
    /*boolean wasEx = false;
    if (def.getDocumentBaseURI()!=null){
    	File f = new File(def.getDocumentBaseURI());
    	if (f.getParent()!=null && importEl.getAttribute("location")!=null){		
    	   f = new File(f.getParent()+"/"+importEl.getAttribute("location"));
    	   
    	}
    } else wasEx = false;
      
    Import importDef = super.parseImport(importEl, def, importedDefs);
    
    // Try to add element to list
    addElementToList(importEl, importDef);
    
    return importDef;*/

    /*Import import1 = definition.createImport();
    String s = DOMUtils.getAttribute(element, "namespace");
    String s1 = DOMUtils.getAttribute(element, "location");
    if (s != null)
    	import1.setNamespaceURI(s);
    if (s1 != null) {
    	if (importDocuments) {
    		String s2 = definition.getDocumentBaseURI();
    		try {
    
    			URL url1 = s2 == null ? null : StringUtils.getURL(null, s2);
    			URL url = StringUtils.getURL(url1, s1);
    			InputStream inputstream =
    				StringUtils.getContentAsInputStream(url);
    			inputstream.close();
    		} catch (IOException ex) {
    			//import1.setLocationURI(null);
    			addElementToList(element, import1);
    			return import1;
    		} catch (Throwable ex) {
    			throw new WSDLException(
    				"OTHER_ERROR",
    				"Unable to resolve imported document at '" + s1 + "'.",
    				ex);
    		}
    
    	}
    }
    Import importDef = super.parseImport(element, definition, map);
    addElementToList(element, importDef);*/
    Import import1 = definition.createImport();
    String s = DOMUtils.getAttribute(element, "namespace");
    String s1 = DOMUtils.getAttribute(element, "location");
    if (s != null)
      import1.setNamespaceURI(s);
    // ADD: check "location" attribute for empty 
    // string to prevent self-defenition assigning
    if ((s1 != null) && (s1.length() > 0))
    {
      import1.setLocationURI(s1);
      if (importDocuments)
        try
        {
          String s2 = definition.getDocumentBaseURI();
          Definition definition1 = null;
          InputStream inputstream = null;
          InputSource inputsource = null;
          URL url = null;
          if (loc != null)
          {
            inputsource = loc.getImportInputSource(s2, s1);
            String s3 = loc.getLatestImportURI();
            definition1 = (Definition) map.get(s3);
          }
          else
          {
            URL url1 = s2 == null ? null : StringUtils.getURL(null, s2);
            url = StringUtils.getURL(url1, s1);
            definition1 = (Definition) map.get(url.toString());
            if (definition1 == null)
            {
              try
              {

                inputstream = url.openStream();
              }
              catch (IOException ex)
              {
                //import1.setLocationURI(null);
                addElementToList(element, import1);
                return import1;
              }
              if (inputstream != null)
                inputsource = new InputSource(inputstream);
            }
          }
          if (definition1 == null)
          {
            if (inputsource == null)
              throw new WSDLException(
                "OTHER_ERROR",
                "Unable to locate imported document at '"
                  + s1
                  + "'"
                  + (s2 != null ? ", relative to '" + s2 + "'." : "."));
            Document document = null;
            try
            {
              document = getDocument(inputsource, s1);
            }
            catch (WSDLException ex)
            {
              addElementToList(element, import1);
              return import1;
            }
            if (inputstream != null)
              inputstream.close();
            Element element2 = document.getDocumentElement();
            if (QNameUtils.matches(Constants.Q_ELEM_DEFINITIONS, element2))
            {
              //if (verbose)
              //  System.out.println(
              //    "Retrieving document at '"
              //      + s1
              //      + "'"
              //      + (s2 != null ? ", relative to '" + s2 + "'." : "."));
              String s4 =
                loc == null
                  ? url == null
                  ? s1
                  : url.toString() : loc.getLatestImportURI();
              definition1 = readWSDL(s4, element2, map);
            }
            else
            {
              QName qname = QNameUtils.newQName(element2);
              if (SchemaConstants.XSD_QNAME_LIST.contains(qname))
              {
                WSDLFactory wsdlfactory =
                  factoryImplName == null
                    ? WSDLFactory.newInstance()
                    : WSDLFactory.newInstance(factoryImplName);
                definition1 = wsdlfactory.newDefinition();
                if (extReg != null)
                  definition1.setExtensionRegistry(extReg);
                String s5 =
                  loc == null
                    ? url == null
                    ? s1
                    : url.toString() : loc.getLatestImportURI();
                definition1.setDocumentBaseURI(s5);
                /* Don't add types element since it doesn't exist.  Adding it causes problems
                 * since it will add a types element for processing that does not exist.
                Types types = definition1.createTypes();
                UnknownExtensibilityElement unknownextensibilityelement =
                	new UnknownExtensibilityElement();
                unknownextensibilityelement.setElement(element2);
                types.addExtensibilityElement(
                	unknownextensibilityelement);
                definition1.setTypes(types);
                */
              }
            }
          }
          if (definition1 != null)
            import1.setDefinition(definition1);
        }
        catch (WSDLException wsdlexception)
        {
          wsdlexception.setLocation(XPathUtils.getXPathExprFromNode(element));
          throw wsdlexception;
        }
        catch (Throwable throwable)
        {
          throw new WSDLException(
            "OTHER_ERROR",
            "Unable to resolve imported document at '" + s1 + "'.",
            throwable);
        }
    }
    for (Element element1 = DOMUtils.getFirstChildElement(element);
      element1 != null;
      element1 = DOMUtils.getNextSiblingElement(element1))
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, element1))
        import1.setDocumentationElement(element1);
      else
        DOMUtils.throwWSDLException(element1);

    return import1;

  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseTypes(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Types parseTypes(Element typesEl, Definition def)
    throws WSDLException
  {
    Types types = super.parseTypes(typesEl, def);

    // Try to add element to list
    addElementToList(typesEl, types);

    return types;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseBinding(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Binding parseBinding(Element bindingEl, Definition def)
    throws WSDLException
  {
    Binding binding = super.parseBinding(bindingEl, def);

    // Try to add element to list
    addElementToList(bindingEl, binding);

    return binding;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseBindingOperation(org.w3c.dom.Element, javax.wsdl.PortType, javax.wsdl.Definition)
   */
  protected BindingOperation parseBindingOperation(
    Element bindingOperationEl,
    PortType portType,
    Definition def)
    throws WSDLException
  {
    BindingOperation bindingOperation = null;

    // The follow try-catch was added to detect when a duplicate operation name was detected
    try
    {
      bindingOperation =
        super.parseBindingOperation(bindingOperationEl, portType, def);
    }

    catch (IllegalArgumentException iae)
    {
      if (iae.getMessage().startsWith("Duplicate"))
      {
        bindingOperation =
          parseBindingOperationWithDuplicateNames(
            bindingOperationEl,
            portType,
            def);
      }

      else
      {
        throw iae;
      }
    }

    // Try to add element to list
    if (bindingOperation != null)
    {
      addElementToList(bindingOperationEl, bindingOperation);
    }

    return bindingOperation;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseBindingInput(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected BindingInput parseBindingInput(
    Element bindingInputEl,
    Definition def)
    throws WSDLException
  {
    BindingInput bindingInput = super.parseBindingInput(bindingInputEl, def);

    // Try to add element to list
    addElementToList(bindingInputEl, bindingInput);

    return bindingInput;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseBindingOutput(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected BindingOutput parseBindingOutput(
    Element bindingOutputEl,
    Definition def)
    throws WSDLException
  {
    BindingOutput bindingOutput =
      super.parseBindingOutput(bindingOutputEl, def);

    // Try to add element to list
    addElementToList(bindingOutputEl, bindingOutput);

    return bindingOutput;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseBindingFault(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected BindingFault parseBindingFault(
    Element bindingFaultEl,
    Definition def)
    throws WSDLException
  {
    BindingFault bindingFault = super.parseBindingFault(bindingFaultEl, def);

    // Try to add element to list
    addElementToList(bindingFaultEl, bindingFault);

    return bindingFault;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseMessage(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Message parseMessage(Element msgEl, Definition def)
    throws WSDLException
  {
    Message msg = super.parseMessage(msgEl, def);

    // Try to add element to list
    addElementToList(msgEl, msg);

    return msg;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parsePart(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Part parsePart(Element partEl, Definition def) throws WSDLException
  {
    //Part part = super.parsePart(partEl, def);

    Part part = def.createPart();
    String name = DOMUtils.getAttribute(partEl, Constants.ATTR_NAME);

    // WS-I: The try-catch was added for WSI2416
    QName elementName;

    try
    {
      elementName =
        DOMUtils.getQualifiedAttributeValue(
          partEl,
          Constants.ATTR_ELEMENT,
          Constants.ELEM_MESSAGE,
          false,
          def);
    }

    catch (WSDLException we)
    {
      String prefixedValue =
        DOMUtils.getAttribute(partEl, Constants.ATTR_ELEMENT);
      int index = prefixedValue.indexOf(':');
      String localPart = prefixedValue.substring(index + 1);

      elementName = new QName(localPart);
    }

    QName typeName =
      DOMUtils.getQualifiedAttributeValue(
        partEl,
        Constants.ATTR_TYPE,
        Constants.ELEM_MESSAGE,
        false,
        def);

    if (name != null)
    {
      part.setName(name);
    }

    if (elementName != null)
    {
      part.setElementName(elementName);
    }

    if (typeName != null)
    {
      part.setTypeName(typeName);
    }

    Element tempEl = DOMUtils.getFirstChildElement(partEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        part.setDocumentationElement(tempEl);
      }
      else
      {
        DOMUtils.throwWSDLException(tempEl);
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    parseExtensibilityAttributes(partEl, Part.class, part, def);
//    Map extensionAttributes = part.getExtensionAttributes();
//    extensionAttributes.putAll(getPartAttributes(partEl, def));

    // Need to do something here to locate part definition.

    // Try to add element to list
    addElementToList(partEl, part);

    return part;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parsePortType(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected PortType parsePortType(Element portTypeEl, Definition def)
    throws WSDLException
  {
    PortType portType = super.parsePortType(portTypeEl, def);

    // Try to add element to list
    addElementToList(portTypeEl, portType);

    return portType;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseOperation(org.w3c.dom.Element, javax.wsdl.PortType, javax.wsdl.Definition)
   */
  protected Operation parseOperation(
    Element opEl,
    PortType portType,
    Definition def)
    throws WSDLException
  {
    Operation op = super.parseOperation(opEl, portType, def);

    // Try to add element to list
    addElementToList(opEl, op);

    return op;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseService(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Service parseService(Element serviceEl, Definition def)
    throws WSDLException
  {
    Service service = super.parseService(serviceEl, def);

    // Try to add element to list
    addElementToList(serviceEl, service);

    return service;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parsePort(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Port parsePort(Element portEl, Definition def) throws WSDLException
  {
    Port port = super.parsePort(portEl, def);

    // Try to add element to list
    addElementToList(portEl, port);

    return port;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseExtensibilityElement(java.lang.Class, org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected ExtensibilityElement parseExtensibilityElement(
    Class parentType,
    Element el,
    Definition def)
    throws WSDLException
  {
    ExtensibilityElement extElement =
      super.parseExtensibilityElement(parentType, el, def);

    // Try to add element to list
    addElementToList(el, extElement);

    return extElement;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseInput(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Input parseInput(Element inputEl, Definition def)
    throws WSDLException
  {
    Input input = super.parseInput(inputEl, def);

    // Try to add element to list
    addElementToList(inputEl, input);

    return input;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseOutput(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Output parseOutput(Element outputEl, Definition def)
    throws WSDLException
  {
    Output output = super.parseOutput(outputEl, def);

    // Try to add element to list
    addElementToList(outputEl, output);

    return output;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#parseFault(org.w3c.dom.Element, javax.wsdl.Definition)
   */
  protected Fault parseFault(Element faultEl, Definition def)
    throws WSDLException
  {
    Fault fault = super.parseFault(faultEl, def);

    // Try to add element to list
    addElementToList(faultEl, fault);

    return fault;
  }

  /* (non-Javadoc)
   * @see com.ibm.wsdl.xml.WSDLReaderImpl#getDocument(org.xml.sax.InputSource, java.lang.String)
   */
  protected Document getDocument(InputSource inputSource, String desc)
    throws WSDLException
  {
    ClassLoader currentLoader = Thread.currentThread().getContextClassLoader();
    try
	{
      Thread.currentThread().setContextClassLoader(WSDLReaderImpl.class.getClassLoader());   	
	
  	  //DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilderFactory factory =
        new org.eclipse.wst.wsi.internal.core.xml.jaxp.DocumentBuilderFactoryImpl();

      factory.setNamespaceAware(true);
      factory.setValidating(false);

      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(inputSource);

      return doc;
    }
    catch (Throwable t)
    {
      throw new WSDLException(
        WSDLException.PARSER_ERROR,
        "Problem parsing '" + desc + "'.",
        t);
    }
    finally
    { 
      Thread.currentThread().setContextClassLoader(currentLoader);
    }    
  }

  /**
   * Get element list.
   * @return the element list.
   */
  public WSDLElementList getElementList()
  {
    return this.wsdlElementList;
  }

  /** 
   * Add WSDL element to element list.
   */
  private void addElementToList(Element element, Object wsdlElement)
  {
    ElementLocation elementLocation = null;

    try
    {
      // See if the element object is an instanceof org.apache.xerces.dom.ElementImpl
      ElementImpl elementImpl = (ElementImpl) element;

      // If it is, then get the element location information
      elementLocation =
        (ElementLocation) elementImpl.getUserData();

      // Add it to the list
      this.wsdlElementList.addElement(wsdlElement, elementLocation);
    }

    catch (ClassCastException cce)
    {
      // ADD: Should we add the element with a null or zero location?
    }
  }

  /**
   * This method is used when a WSDL document contains duplicate operation names.
   * It is the same as the original parseBindingOperation method, except that it will
   * just find the first operation that matches instead of throwing an exception.
   */
  private BindingOperation parseBindingOperationWithDuplicateNames(
    Element bindingOperationEl,
    PortType portType,
    Definition def)
    throws WSDLException
  {
    BindingOperation bindingOperation = def.createBindingOperation();
    String name =
      DOMUtils.getAttribute(bindingOperationEl, Constants.ATTR_NAME);

    if (name != null)
    {
      bindingOperation.setName(name);
    }

    Element tempEl = DOMUtils.getFirstChildElement(bindingOperationEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        bindingOperation.setDocumentationElement(tempEl);
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_INPUT, tempEl))
      {
        bindingOperation.setBindingInput(parseBindingInput(tempEl, def));
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_OUTPUT, tempEl))
      {
        bindingOperation.setBindingOutput(parseBindingOutput(tempEl, def));
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_FAULT, tempEl))
      {
        bindingOperation.addBindingFault(parseBindingFault(tempEl, def));
      }
      else
      {
        bindingOperation.addExtensibilityElement(
          parseExtensibilityElement(BindingOperation.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    if (portType != null)
    {
      BindingInput bindingInput = bindingOperation.getBindingInput();
      BindingOutput bindingOutput = bindingOperation.getBindingOutput();
      String inputName = (bindingInput != null ? bindingInput.getName() : null);
      String outputName =
        (bindingOutput != null ? bindingOutput.getName() : null);

      //Operation op = portType.getOperation(name, inputName, outputName);
      // Get all operations, and then find the first one that matches
      Operation op = null, checkOperation;
      Iterator iterator = portType.getOperations().iterator();
      while (iterator.hasNext() && op == null)
      {
        // Get the next operation
        checkOperation = (Operation) iterator.next();

        // Get the operation name, input name, and output name
        String checkName = checkOperation.getName();
        String checkInputName =
          (checkOperation.getInput() == null
            ? null
            : checkOperation.getInput().getName());
        String checkOutputName =
          (checkOperation.getOutput() == null
            ? null
            : checkOperation.getOutput().getName());

        // If the names match, then that operation
        if ((checkName != null && checkName.equals(name))
          && ((checkInputName != null && checkInputName.equals(inputName))
            || (checkInputName == null && inputName == null))
          && ((checkOutputName != null && checkOutputName.equals(outputName))
            || (checkOutputName == null && outputName == null)))
        {
          op = checkOperation;
        }
      }

      if (op == null)
      {
        op = def.createOperation();
        op.setName(name);
        portType.addOperation(op);
      }

      bindingOperation.setOperation(op);
    }

    return bindingOperation;
  }
  
 protected ExtensibilityElement parseSchema(Class ccc, Element elem, Definition def) throws WSDLException
{
   ExtensibilityElement extElem = null;
   extElem = super.parseSchema(ccc, elem, def);

   // Try to add element to list
   addElementToList(elem, extElem);
  return extElem;
 }
}
