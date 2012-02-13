/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.validation.internal.wsdl11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.OperationType;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.ExtensionDeserializer;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.WSDLFactory;
import javax.xml.namespace.QName;

import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.ElementNSImpl;
import org.apache.xerces.xs.XSModel;
import org.eclipse.wst.wsdl.validation.internal.util.MessageGenerator;
import org.eclipse.wst.wsdl.validation.internal.wsdl11.xsd.InlineSchemaValidator;
import org.eclipse.wst.wsdl.validation.internal.xml.ElementLocation;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.wsdl.Constants;
import com.ibm.wsdl.util.StringUtils;
import com.ibm.wsdl.util.xml.DOMUtils;
import com.ibm.wsdl.util.xml.QNameUtils;
import com.ibm.wsdl.util.xml.XPathUtils;

/**
 * A WSDL document that knows how to parse itself.
 */
public class WSDLDocument
{
  private static final List STYLE_ONE_WAY = Arrays.asList(new String[] { Constants.ELEM_INPUT });
  private static final List STYLE_REQUEST_RESPONSE =
    Arrays.asList(new String[] { Constants.ELEM_INPUT, Constants.ELEM_OUTPUT });
  private static final List STYLE_SOLICIT_RESPONSE =
    Arrays.asList(new String[] { Constants.ELEM_OUTPUT, Constants.ELEM_INPUT });
  private static final List STYLE_NOTIFICATION = Arrays.asList(new String[] { Constants.ELEM_OUTPUT });
  
  private static final String _ERROR_MULTIPLE_TYPES_DEFINED = "_ERROR_MULTIPLE_TYPES_DEFINED";
  private static final String _UNABLE_TO_IMPORT_NO_LOCATION = "_UNABLE_TO_IMPORT_NO_LOCATION";

  protected ExtensionRegistry extReg = null;
  protected String factoryImplName = null;

  // store the element locations within the file - line and column numbers
  // the location info is stored as an int array of length 2 {linenumber, colnumber}
  protected Hashtable elementLocations = new Hashtable();
  
  // hold the reader errors
  protected List readerErrors = new ArrayList();
  protected List readerWarnings = new ArrayList();
  protected MessageGenerator messagegenerator;
  
  private Definition def = null;
  private Set importedDefs = new TreeSet();
  private Element typesEl = null;
  private List messages = new ArrayList();
  private List porttypes = new ArrayList();
  private List bindings = new ArrayList();
  private List services = new ArrayList();
  private List extelements = new ArrayList();
  private int depth;
  // Hold the schemas that are imported or declared inline in this wsdl document.
  private List schemas = new ArrayList();
  private IWSDL11ValidationInfo valinfo;
  
  /**
   * Constructor. Performs a preparse of the document and handles imports and types.
   * 
   * @param documentBaseURI The URI of this WSDL document.
   * @param defEl The definitions element.
   * @param depth The depth of this document in a document tree.
   * @param messagegenerator A messagegenerator used for retrieving strings.
   * @param valinfo A WSDL11ValidationInfo object for reporting messages.
   * @throws WSDLException
   */
  public WSDLDocument(String documentBaseURI, Element defEl, int depth, MessageGenerator messagegenerator, IWSDL11ValidationInfo valinfo) throws WSDLException
  {
    this.messagegenerator = messagegenerator;
    this.valinfo = valinfo;
    this.depth = depth;
    
    checkElementName(defEl, Constants.Q_ELEM_DEFINITIONS);
    
    WSDLFactory factory =
      (factoryImplName != null) ? WSDLFactory.newInstance(factoryImplName) : WSDLFactory.newInstance();
    def = factory.newDefinition();
    // Fix for WSDL4J adding default WSDL namespace when no default
    // namespace has been declared.
    // TODO: Remove this fix once fixed in WSDL4J.
    if(def.getNamespace("") != null)
    	def.getNamespaces().remove("");

    if (extReg != null)
    {
      def.setExtensionRegistry(extReg);
    }

    String name = DOMUtils.getAttribute(defEl, Constants.ATTR_NAME);
    String targetNamespace = DOMUtils.getAttribute(defEl, Constants.ATTR_TARGET_NAMESPACE);
    NamedNodeMap attrs = defEl.getAttributes();

    if (documentBaseURI != null)
    {
      def.setDocumentBaseURI(documentBaseURI);
    }

    if (name != null)
    {
      def.setQName(new QName(targetNamespace, name));
    }

    if (targetNamespace != null)
    {
      def.setTargetNamespace(targetNamespace);
    }

    int size = attrs.getLength();

    for (int i = 0; i < size; i++)
    {
      Attr attr = (Attr)attrs.item(i);
      String namespaceURI = attr.getNamespaceURI();
      String localPart = attr.getLocalName();
      String value = attr.getValue();

      if (namespaceURI != null && namespaceURI.equals(Constants.NS_URI_XMLNS))
      {
        if (localPart != null && !localPart.equals(Constants.ATTR_XMLNS))
        {
          def.addNamespace(localPart, value);
        }
        else
        {
          def.addNamespace(null, value);
        }
      }
    }

    // There are problems when the model is created and the order of elements is not
    // Import - Types - Message - PortType - Binding - Service so we need to read them
    // into the model in that order
    // Performance wise this should be modified as we have to make 5 extra loops through
    // all of the elements as a result

    // take care of Imports, Types, Documentation and extensibleelements together - saves a pass for Documentation 
    // later and Docs don't effect anything else - Imports and Types are essentially the same thing
    // no preconceived ideas about extensible elements
    Element tempEl = DOMUtils.getFirstChildElement(defEl);

    while (tempEl != null)
    {

      if (QNameUtils.matches(Constants.Q_ELEM_IMPORT, tempEl))
      {
        String namespaceURI = DOMUtils.getAttribute(tempEl, Constants.ATTR_NAMESPACE);
        String locationURI = DOMUtils.getAttribute(tempEl, Constants.ATTR_LOCATION);
        if(locationURI == null || locationURI.equals(""))
        {
          addReaderError(def, tempEl, messagegenerator.getString(_UNABLE_TO_IMPORT_NO_LOCATION));
        }
        else
        {
          ImportHolder ih = new ImportHolder(namespaceURI, locationURI, def.getDocumentBaseURI(), this, depth+1, tempEl, messagegenerator, valinfo);
          // Only add the import to the list if it is not an import for this document.
          if(!documentBaseURI.equals(ih.getLocation()))
          {
            importedDefs.add(ih);
          }
        }
        setLocation(tempEl, tempEl);
//        if (importedDefs == null)
//        {
//          importedDefs = new Hashtable();
//        }
//        if (documentBaseURI != null)
//        {
//          importedDefs.put(documentBaseURI, def);
//        }
//        def.addImport(parseImport(tempEl, def, importedDefs));
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        def.setDocumentationElement(tempEl);
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_TYPES, tempEl))
      {
        if(typesEl != null)
        {
          setLocation(tempEl, tempEl);
          addReaderError(def, tempEl, messagegenerator.getString(_ERROR_MULTIPLE_TYPES_DEFINED));
        }
        else
        {
          typesEl = tempEl;
          parseTypes();
        }
//        def.setTypes(parseTypes(tempEl, def));
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_MESSAGE, tempEl))
      {
        messages.add(tempEl);
        //def.addMessage(parseMessage(tempEl, def));
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_PORT_TYPE, tempEl))
      {
        porttypes.add(tempEl);
        //             PortType pt = parsePortType(tempEl, def);
        //             if(pt != null)
        //             {
        //          def.addPortType(pt);
        //             }
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_BINDING, tempEl))
      {
        bindings.add(tempEl);
        //def.addBinding(parseBinding(tempEl, def));
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_SERVICE, tempEl))
      {
        services.add(tempEl);
        //def.addService(parseService(tempEl, def));
      }
      else
      {
        extelements.add(tempEl);
        //        def.addExtensibilityElement(
        //          parseExtensibilityElement(Definition.class, tempEl, def));
      }
      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

  }
  
  /**
   * Get the definitions element for this document.
   * 
   * @return The definitions element for this document.
   */
  public Definition getDefinition()
  {
    return def;
  }
  
  /**
   * Get a set of the imports in this document.
   * 
   * @return A set of the imports in this document.
   */
  public Set getImports()
  {
    return importedDefs;
  }
  
  /**
   * 
   * @param def
   * @param doc
   * @param namespace
   * @param location
   */
//  public void setImport(Definition def, Element doc, String namespace, String location)
//  {
//    if(location == null || location.equals(""))
//    {
//      valinfo.addError()_UNABLE_TO_IMPORT_NO_LOCATION 
//    }
//    Import imp = def.createImport();
//    imp.setDefinition(def);
//    imp.setDocumentationElement(doc);
//    imp.setNamespaceURI(namespace);
//    imp.setLocationURI(location);
//    def.addImport(imp);
//  }
  
  /**
   * Parse the types in the WSDL document. Handles documentation, import and schema
   * elements.
   */
  public void parseTypes()
  {
    Types types = def.createTypes();

    Element tempEl = DOMUtils.getFirstChildElement(typesEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        types.setDocumentationElement(tempEl);
      }
      else if (tempEl.getLocalName().equalsIgnoreCase("import"))
        //else if (QNameUtils.matches(Constants.Q_ELEM_IMPORT, tempEl))
      {
        // this shouldn't really be used here but a little hack will make
        // life easier
        //parseImport(tempEl, def, (Map)new Hashtable());
        String namespaceURI = DOMUtils.getAttribute(tempEl, Constants.ATTR_NAMESPACE);
        String locationURI = DOMUtils.getAttribute(tempEl, "schemaLocation");
        importedDefs.add(new ImportHolder(namespaceURI, locationURI, def.getDocumentBaseURI(), this, depth+1, tempEl, messagegenerator, valinfo));
        try
        {
          types.addExtensibilityElement(parseExtensibilityElement(Types.class, tempEl, def));
        }
        catch(WSDLException e)
        {
          
        }
      }
      else
      {
        try
        {
          ExtensibilityElement extElem = parseExtensibilityElement(Types.class, tempEl, def);
          types.addExtensibilityElement(extElem);
        }
        catch(WSDLException e)
        {
          
        }
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }
    def.setTypes(types);
    
    valinfo.setElementLocations(elementLocations);
    List typesElems = types.getExtensibilityElements();
    if(typesElems != null)
    {
      Iterator typesElemsIter = typesElems.iterator();
      while(typesElemsIter.hasNext())
      {
        ExtensibilityElement typeElement = (ExtensibilityElement)typesElemsIter.next();
        
        InlineSchemaValidator xsdVal = new InlineSchemaValidator();
        xsdVal.setMessageGenerator(messagegenerator);
    
        List parents = new ArrayList();
        parents.add(def);
        parents.add(0,types);
        xsdVal.validate(typeElement, parents,valinfo);
        XSModel[] typesSchemas = valinfo.getSchemas();
        List typesSchemaList = new ArrayList();
        for(int i = 0; i < typesSchemas.length; i++)
        {  
          typesSchemaList.add(typesSchemas[i]);
        }
        schemas.addAll(typesSchemaList);
        valinfo.clearSchemas();
      }
    }
    valinfo.setElementLocations(null);
  }
  
  /**
   * Parse the messages in this document.
   */
  public void parseMessages()
  {
    for (int i = 0; i < messages.size(); i++)
    {
      try
      {
        def.addMessage(parseMessage((Element)messages.get(i), def));
      }
      catch(WSDLException e)
      {}
    }
  }
  
  /**
   * Parse the portTypes in this document.
   */
  public void parsePorttypes()
  {
    for (int i = 0; i < porttypes.size(); i++)
    {
      try
      {
        PortType pt = parsePortType((Element)porttypes.get(i), def);
        if (pt != null)
        {
          def.addPortType(pt);
        }
      }
      catch(WSDLException e)
      {}
    }
  }
  
  /**
   * Parse the bindings in this document.
   */
  public void parseBindings()
  {
    for (int i = 0; i < bindings.size(); i++)
    {
      try
      {
        def.addBinding(parseBinding((Element)bindings.get(i), def));
      }
      catch(WSDLException e)
      {}
    }
  }
  
  /**
   * Parse the services in this document.
   */
  public void parseServices()
  {
    for (int i = 0; i < services.size(); i++)
    {
      try
      {
        def.addService(parseService((Element)services.get(i), def));
      }
      catch(WSDLException e)
      {}
    }
  }
  
  /**
   * Parse the extensibility elements in this document.
   */
  public void parseExtensibilityElements()
  {
    for (int i = 0; i < extelements.size(); i++)
    {
      try
      {
        def.addExtensibilityElement(parseExtensibilityElement(Definition.class, (Element)extelements.get(i), def));
      }
      catch(WSDLException e)
      {}
    }
  }
  
  /**
   * Add the given list of schemas to the schemas for this document.
   * 
   * @param schemas The list of schemas to add to this document's schemas.
   */
  public void addSchemas(List schemas)
  {
    this.schemas.addAll(schemas);
  }
  
  /**
   * Get the schemas associated with this document.
   * 
   * @return The schemas associated with this document.
   */
  public List getSchemas()
  {
    return schemas;
  }
  
  /**
   * Parse the specified binding.
   * 
   * @param bindingEl The binding element.
   * @param def The definitions element.
   * @return A WSDL binding element.
   * @throws WSDLException
   */
  protected Binding parseBinding(Element bindingEl, Definition def) throws WSDLException
  {
    Binding binding = null;
    String name = DOMUtils.getAttribute(bindingEl, Constants.ATTR_NAME);
    QName portTypeName;
    try
    {
      portTypeName = DOMUtils.getQualifiedAttributeValue(bindingEl, Constants.ATTR_TYPE, Constants.ELEM_BINDING, false, def);
    }
    catch (Exception e)
    {
      //the call above fails if there is no qualified namespace for the message name
      portTypeName = new QName(null, DOMUtils.getAttribute(bindingEl, "type"));
    }

    PortType portType = null;

    if (name != null)
    {
      QName bindingName = new QName(def.getTargetNamespace(), name);

      binding = def.getBinding(bindingName);

      if (binding == null)
      {
        binding = def.createBinding();
        binding.setQName(bindingName);
      }
      // report an error if a binding with this name has already been defined
      else if (!binding.isUndefined())
      {
        //addReaderError(def,bindingEl, "_BINDING_NAME_ALREADY_DEFINED");       
        addReaderError(
          def,
          bindingEl,
          messagegenerator.getString("_BINDING_NAME_ALREADY_DEFINED", "'" + binding.getQName().getLocalPart() + "'"));
      }
    }
    else
    {
      binding = def.createBinding();
    }

    // Whether it was retrieved or created, the definition has been found.
    binding.setUndefined(false);

    if (portTypeName != null)
    {
      portType = def.getPortType(portTypeName);

      if (portType == null)
      {
        portType = def.createPortType();
        portType.setQName(portTypeName);
        def.addPortType(portType);
      }

      binding.setPortType(portType);
    }

    Element tempEl = DOMUtils.getFirstChildElement(bindingEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        binding.setDocumentationElement(tempEl);
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_OPERATION, tempEl))
      {
        binding.addBindingOperation(parseBindingOperation(tempEl, portType, def));
      }
      else
      {
        binding.addExtensibilityElement(parseExtensibilityElement(Binding.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(binding, bindingEl);

    return binding;
  }

  /**
   * Parse a specific binding operation.
   * 
   * @param bindingOperationEl The binding operation element.
   * @param portType The portType the binding references.
   * @param def The definitions element.
   * @return A WSDL binding operation element.
   * @throws WSDLException
   */
  protected BindingOperation parseBindingOperation(Element bindingOperationEl, PortType portType, Definition def)
    throws WSDLException
  {
    BindingOperation bindingOperation = def.createBindingOperation();
    String name = DOMUtils.getAttribute(bindingOperationEl, Constants.ATTR_NAME);

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
        bindingOperation.addExtensibilityElement(parseExtensibilityElement(BindingOperation.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    if (portType != null)
    {
      BindingInput bindingInput = bindingOperation.getBindingInput();
      BindingOutput bindingOutput = bindingOperation.getBindingOutput();
      //String inputName = (bindingInput != null ? bindingInput.getName() : null);
      //String outputName = (bindingOutput != null ? bindingOutput.getName() : null);

      // hack code to get at operations that are defined with the same name but different
      // inputs and outputs 
      Operation op = null;
      List operations = portType.getOperations();
      // get a list of all operations with matching names
      List matchingOperations = new Vector();
      Iterator iOperations = operations.iterator();
      while (iOperations.hasNext())
      {
        Operation oper = (Operation)iOperations.next();
        if (oper.getName().equalsIgnoreCase(bindingOperation.getName()))
        {
          matchingOperations.add(oper);
        }
      }

      if (matchingOperations != null)
      {
        // If there's only one matching operation this is what we're referring to.
        // Only matching if binding operation input name and output name are
        // both null or the same as the portType operation input and output names.
        // the portType operation name
        if (matchingOperations.size() == 1)
        {
          // only say the single operation is the one we're looking for if the names
          // of the binding input and output are not specified
          Operation tempOp = (Operation)matchingOperations.get(0);
          boolean inputOK = false;
          boolean outputOK = false;
          Input tempInput = tempOp.getInput();
          Output tempOutput = tempOp.getOutput();

          // order is important in these conditions. condition 2 must fail for 3 to work
          // check the input
          if (tempInput == null && bindingInput == null)
          {
            inputOK = true;
          }
          else if (bindingInput == null || bindingInput.getName() == null)
          {
            inputOK = true;
          }
          else if (tempInput != null && bindingInput.getName().equals(tempInput.getName()))
          {
            inputOK = true;
          }
          // check the output
          if (tempOutput == null && bindingOutput == null)
          {
            outputOK = true;
          }
          else if (bindingOutput == null || bindingOutput.getName() == null)
          {
            outputOK = true;
          }
          else if (tempOutput != null && bindingOutput.getName().equals(tempOutput.getName()))
          {
            outputOK = true;
          }
          if (inputOK && outputOK)
          {
            op = tempOp;
          }
          //          op = (Operation) matchingOperations.get(0);
        }
        // otherwise find the operation with the same name, inputname, outputname signature
        if (matchingOperations != null && op == null)
        {
          Iterator iMatchingOperations = matchingOperations.iterator();
          while (iMatchingOperations.hasNext())
          {

            boolean inputNamesEqual = false;
            boolean outputNamesEqual = false;
            Operation oper = (Operation)iMatchingOperations.next();
            //          if (oper.getName().equalsIgnoreCase(bindingOperation.getName()))
            //          {
            Input opInput = oper.getInput();
            if (opInput != null && bindingInput != null)
            {
              String opInputName = opInput.getName();
              String bindingInputName = bindingInput.getName();
              if (opInputName != null && opInputName.equalsIgnoreCase(bindingInputName))
              {
                inputNamesEqual = true;
              }
              else if (opInputName == null && bindingInputName == null)
              {
                inputNamesEqual = true;
              }
            }
            else if (opInput == null && bindingInput == null)
            {
              inputNamesEqual = true;
            }
            Output opOutput = oper.getOutput();
            if (opOutput != null && bindingOutput != null)
            {
              String opOutputName = opOutput.getName();
              String bindingOutputName = bindingOutput.getName();
              if (opOutputName != null && opOutputName.equalsIgnoreCase(bindingOutputName))
              {
                outputNamesEqual = true;
              }
              else if (opOutputName == null && bindingOutputName == null)
              {
                outputNamesEqual = true;
              }
            }
            else if (opOutput == null && bindingOutput == null)
            {
              outputNamesEqual = true;
            }
            if (inputNamesEqual && outputNamesEqual)
            {
              op = oper;
              break;
            }
            //}
          }
        }
      }
      //Operation op = portType.getOperation(name, inputName, outputName);

      if (op == null)
      {
        op = def.createOperation();
        if (name != null) {
          op.setName(name);
          portType.addOperation(op);
        }
      }

      bindingOperation.setOperation(op);
    }

    // add the location of this element to elementLocations 
    setLocation(bindingOperation, bindingOperationEl);

    return bindingOperation;
  }
  
  /**
   * Parse a specific binding input element.
   * 
   * @param bindingInputEl The binding input element.
   * @param def The definitions element.
   * @return A WSDL binding input element.
   * @throws WSDLException
   */
  protected BindingInput parseBindingInput(Element bindingInputEl, Definition def) throws WSDLException
  {
    BindingInput bindingInput = def.createBindingInput();
    String name = DOMUtils.getAttribute(bindingInputEl, Constants.ATTR_NAME);

    if (name != null)
    {
      bindingInput.setName(name);
    }

    Element tempEl = DOMUtils.getFirstChildElement(bindingInputEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        bindingInput.setDocumentationElement(tempEl);
      }
      else
      {
        bindingInput.addExtensibilityElement(parseExtensibilityElement(BindingInput.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(bindingInput, bindingInputEl);

    return bindingInput;
  }

  /**
   * Parse a specific binding output element.
   * 
   * @param bindingOutputEl The binding output element.
   * @param def The definitions element.
   * @return A WSDL binding output element.
   * @throws WSDLException
   */
  protected BindingOutput parseBindingOutput(Element bindingOutputEl, Definition def) throws WSDLException
  {
    BindingOutput bindingOutput = def.createBindingOutput();
    String name = DOMUtils.getAttribute(bindingOutputEl, Constants.ATTR_NAME);

    if (name != null)
    {
      bindingOutput.setName(name);
    }

    Element tempEl = DOMUtils.getFirstChildElement(bindingOutputEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        bindingOutput.setDocumentationElement(tempEl);
      }
      else
      {
        bindingOutput.addExtensibilityElement(parseExtensibilityElement(BindingOutput.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(bindingOutput, bindingOutputEl);

    return bindingOutput;
  }

  /**
   * Parse a specific binding fault element.
   * 
   * @param bindingFaultEl The binding fault element.
   * @param def The definitions element.
   * @return A WSDL binding fault element.
   * @throws WSDLException
   */
  protected BindingFault parseBindingFault(Element bindingFaultEl, Definition def) throws WSDLException
  {
    BindingFault bindingFault = def.createBindingFault();
    String name = DOMUtils.getAttribute(bindingFaultEl, Constants.ATTR_NAME);

    if (name != null)
    {
      bindingFault.setName(name);
    }

    Element tempEl = DOMUtils.getFirstChildElement(bindingFaultEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        bindingFault.setDocumentationElement(tempEl);
      }
      else
      {
        bindingFault.addExtensibilityElement(parseExtensibilityElement(BindingFault.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(bindingFault, bindingFaultEl);

    return bindingFault;
  }

  /**
   * Parse a specific message element.
   * 
   * @param msgEl The message element.
   * @param def The definitions element.
   * @return A WSDL message element.
   * @throws WSDLException
   */
  protected Message parseMessage(Element msgEl, Definition def) throws WSDLException
  {
    Message msg = null;
    String name = DOMUtils.getAttribute(msgEl, Constants.ATTR_NAME);

    if (name != null)
    {
      QName messageName = new QName(def.getTargetNamespace(), name);

      msg = def.getMessage(messageName);

      if (msg == null)
      {
        msg = def.createMessage();
        msg.setQName(messageName);
      }
      else if (!msg.isUndefined())
      {
        // produce an error message as a message with this name has already been defined
        addReaderError(
          def,
          msgEl,
          messagegenerator.getString("_MESSAGE_NAME_ALREADY_DEFINED", "'" + msg.getQName().getLocalPart() + "'"));
      }
    }
    else
    {
      msg = def.createMessage();
    }

    // Whether it was retrieved or created, the definition has been found.
    msg.setUndefined(false);

    Element tempEl = DOMUtils.getFirstChildElement(msgEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        msg.setDocumentationElement(tempEl);
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_PART, tempEl))
      {
        msg.addPart(parsePart(tempEl, def));
      }
      else
      {
        // message allows extensibility elements
        msg.addExtensibilityElement(parseExtensibilityElement(Message.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(msg, msgEl);

    return msg;
  }

  /**
   * Parse a specific part element.
   * 
   * @param partEl The part element.
   * @param def The definitions element.
   * @return A WSDL part element.
   * @throws WSDLException
   */
  protected Part parsePart(Element partEl, Definition def) throws WSDLException
  {
    Part part = def.createPart();
    String name = DOMUtils.getAttribute(partEl, Constants.ATTR_NAME);

    QName elementName;
    try
    {
      elementName = DOMUtils.getQualifiedAttributeValue(partEl, Constants.ATTR_ELEMENT, Constants.ELEM_MESSAGE, false, def);
    }
    catch (Exception e)
    {
      //the call above fails if there is no qualified namespace for the element name
      elementName = new QName(null, DOMUtils.getAttribute(partEl, Constants.ATTR_ELEMENT));
    }

    QName typeName;
    try
    {
      typeName = DOMUtils.getQualifiedAttributeValue(partEl, Constants.ATTR_TYPE,
        // Corrected - was ATTR_ELEMENT
  Constants.ELEM_MESSAGE, false, def);
    }
    catch (Exception e)
    {
      //the call above fails if there is no qualified namespace for the element attribute
      typeName = new QName(null, DOMUtils.getAttribute(partEl, Constants.ATTR_TYPE));
    }

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
        // XML Validation will catch this
        DOMUtils.throwWSDLException(tempEl);
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    Map extensionAttributes = part.getExtensionAttributes();

    extensionAttributes.putAll(getPartAttributes(partEl, def));

    // Need to do something here to locate part definition.

    // add the location of this element to elementLocations 
    setLocation(part, partEl);

    return part;
  }

  /**
   * Get a map of the part attributes.
   * 
   * @param el The part attributes element.
   * @param def The defintions element.
   * @return A map containing the part attributes.
   * @throws WSDLException
   */
  protected Map getPartAttributes(Element el, Definition def) throws WSDLException
  {
    Map attributes = new HashMap();
    NamedNodeMap nodeMap = el.getAttributes();
    int atts = nodeMap.getLength();

    for (int a = 0; a < atts; a++)
    {
      Attr attribute = (Attr)nodeMap.item(a);
      String lName = attribute.getLocalName();
      String nSpace = attribute.getNamespaceURI();
      String prefix = attribute.getPrefix();
      QName name = new QName(nSpace, lName);

      if (nSpace != null && !nSpace.equals(Constants.NS_URI_WSDL))
      {
        if (!nSpace.equals(Constants.NS_URI_XMLNS))
        {
          String strValue = attribute.getValue();
          QName qValue = null;

          try
          {
            qValue = DOMUtils.getQName(strValue, el, def);
          }
          catch (WSDLException e)
          {
            qValue = new QName(strValue);
          }

          attributes.put(name, qValue);

          String tempNSUri = def.getNamespace(prefix);

          while (tempNSUri != null && !tempNSUri.equals(nSpace))
          {
            prefix += "_";
            tempNSUri = def.getNamespace(prefix);
          }

          def.addNamespace(prefix, nSpace);
        }
      }
      else if (
        !lName.equals(Constants.ATTR_NAME)
          && !lName.equals(Constants.ATTR_ELEMENT)
          && !lName.equals(Constants.ATTR_TYPE))
      {
        WSDLException wsdlExc =
          new WSDLException(
            WSDLException.INVALID_WSDL,
            "Encountered illegal "
              + "part extension "
              + "attribute '"
              + name
              + "'. Extension "
              + "attributes must be in "
              + "a namespace other than "
              + "WSDL's.");

        wsdlExc.setLocation(XPathUtils.getXPathExprFromNode(el));
        //throw wsdlExc;
      }
    }

    // add the location of this element to elementLocations 
    setLocation(attributes, el);

    return attributes;
  }

  /**
   * Parse a specific portType element.
   * 
   * @param portTypeEl The portType element.
   * @param def The defintions element.
   * @return A WSDL portType element.
   * @throws WSDLException
   */
  protected PortType parsePortType(Element portTypeEl, Definition def) throws WSDLException
  {

    PortType portType = null;
    String name = DOMUtils.getAttribute(portTypeEl, Constants.ATTR_NAME);

    if (name != null)
    {
      QName portTypeName = new QName(def.getTargetNamespace(), name);

      portType = def.getPortType(portTypeName);

      if (portType == null)
      {
        portType = def.createPortType();
        portType.setQName(portTypeName);
      }
      else if (!portType.isUndefined())
      {
        // if the PortType has already been defined produce an error and return null
        addReaderError(
          def,
          portTypeEl,
          messagegenerator.getString("_PORTTYPE_NAME_ALREADY_DEFINED", "'" + portType.getQName().getLocalPart() + "'"));
        return null;
      }
    }
    else
    {
      portType = def.createPortType();
    }

    // Whether it was retrieved or created, the definition has been found.
    portType.setUndefined(false);

    Element tempEl = DOMUtils.getFirstChildElement(portTypeEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        portType.setDocumentationElement(tempEl);
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_OPERATION, tempEl))
      {
        // modified so duplicate operations will not be added to porttype 
        Operation op = parseOperation(tempEl, portType, def);
        if (op != null)
        {
          portType.addOperation(op);
        }
        //portType.addOperation(parseOperation(tempEl, portType, def));
      }
      else
      {
        // something else that shouldn't be here
        // NEED TO ADD TO ERROR LIST
        //DOMUtils.throwWSDLException(tempEl);
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(portType, portTypeEl);

    return portType;
  }

  /**
   * Parse a specific operation element.
   * 
   * @param opEl The operation element.
   * @param portType The portType element.
   * @param def The definitions element.
   * @return A WSDL operation element.
   * @throws WSDLException
   */
  protected Operation parseOperation(Element opEl, PortType portType, Definition def) throws WSDLException
  {
    Operation op = null;
    String name = DOMUtils.getAttribute(opEl, Constants.ATTR_NAME);
    String parameterOrderStr = DOMUtils.getAttribute(opEl, Constants.ATTR_PARAMETER_ORDER);
    Element tempEl = DOMUtils.getFirstChildElement(opEl);
    List messageOrder = new Vector();
    Element docEl = null;
    Input input = null;
    Output output = null;
    List faults = new Vector();
    List extElements = new ArrayList();

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        docEl = tempEl;
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_INPUT, tempEl))
      {
        input = parseInput(tempEl, def);
        messageOrder.add(Constants.ELEM_INPUT);
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_OUTPUT, tempEl))
      {
        output = parseOutput(tempEl, def);
        messageOrder.add(Constants.ELEM_OUTPUT);
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_FAULT, tempEl))
      {
        faults.add(parseFault(tempEl, def));
      }
      else
      {
        // operation allows extensibility elements
        extElements.add(parseExtensibilityElement(Operation.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    if (name != null)
    {
      String inputName = (input != null ? input.getName() : null);
      String outputName = (output != null ? output.getName() : null);

      boolean opDefined = false;

      try
      {

        //op = portType.getOperation(name, inputName, outputName);

        //Operation op = null;
        List operations = portType.getOperations();
        if (operations != null)
        {

          Iterator iOperations = operations.iterator();
          while (iOperations.hasNext())
          {
            boolean inputNamesEqual = false;
            boolean outputNamesEqual = false;
            Operation oper = (Operation)iOperations.next();
            if (oper.getName().equals(name))
            {
              Input opInput = oper.getInput();
              if (opInput != null && input != null)
              {
                String opInputName = opInput.getName();
                if (opInputName != null && inputName != null && opInputName.equals(inputName))
                {
                  inputNamesEqual = true;
                }
                else if (opInputName == null && inputName == null)
                {
                  inputNamesEqual = true;
                }
              }
              else if (opInput == null && input == null)
              {
                inputNamesEqual = true;
              }
              Output opOutput = oper.getOutput();
              if (opOutput != null && output != null)
              {
                String opOutputName = opOutput.getName();
                if (opOutputName != null && outputName != null && opOutputName.equals(outputName))
                {
                  outputNamesEqual = true;
                }
                else if (opOutputName == null && outputName == null)
                {
                  outputNamesEqual = true;
                }
              }
              else if (opOutput == null && output == null)
              {
                outputNamesEqual = true;
              }
              if (inputNamesEqual && outputNamesEqual)
              {
                op = oper;
                break;
              }
            }
          }
        }
      }
      catch (Exception e)
      {
        opDefined = true;
      }

      if (op != null /*&& !op.isUndefined()*/
        )
      {
        //op = null;
        opDefined = true;

      }

      if (op != null && !opDefined)
      {
        if (inputName == null)
        {
          Input tempIn = op.getInput();

          if (tempIn != null)
          {
            if (tempIn.getName() != null)
            {
              //op = null;
              opDefined = true;
            }
          }
        }
      }

      if (op != null && !opDefined)
      {
        if (outputName == null)
        {
          Output tempOut = op.getOutput();

          if (tempOut != null)
          {
            if (tempOut.getName() != null)
            {
              //op = null;
              opDefined = true;
            }
          }
        }
      }

      if (opDefined)
      {
        // instead of creating a new one with the same name we're going to return null.
        // According to the WSDL 1.2 working draft operation overloading is no longer allowed.
        // Going to use that here to save a lot of work
        setLocation(op, opEl);
        addReaderError(
          portType,
          op,
          messagegenerator.getString(
            "_DUPLICATE_OPERATION_FOR_PORTTYPE",
            "'" + op.getName() + "'",
            "'" + portType.getQName().getLocalPart() + "'"));
        return null;
      }
      if (op == null)
      {
        op = def.createOperation();
        op.setName(name);

      }
    }
    else
    {
      op = def.createOperation();
    }

    // Whether it was retrieved or created, the definition has been found.
    op.setUndefined(false);

    if (parameterOrderStr != null)
    {
      op.setParameterOrdering(StringUtils.parseNMTokens(parameterOrderStr));
    }

    if (docEl != null)
    {
      op.setDocumentationElement(docEl);
    }

    if (input != null)
    {
      op.setInput(input);
    }

    if (output != null)
    {
      op.setOutput(output);
    }

    if (faults.size() > 0)
    {
      Iterator faultIterator = faults.iterator();

      while (faultIterator.hasNext())
      {
        Fault f = (Fault)faultIterator.next();
        // if the fault isn't defined yet
        if (op.getFault(f.getName()) == null)
        {
          op.addFault(f);
        }
        else
        {
          addReaderError(
            op,
            f,
            messagegenerator.getString("_DUPLICATE_FAULT_NAME", "'" + f.getName() + "'", "'" + op.getName() + "'"));
          //faultErrors.add(new Object[]{f,"Duplicate Name",op});
        }
      }
    }
    
    if (extElements.size() > 0)
    {
      Iterator extElementsIterator = extElements.iterator();
      while (extElementsIterator.hasNext())
      {
          op.addExtensibilityElement((ExtensibilityElement)extElementsIterator.next());
      }
    }

    OperationType style = null;

    if (messageOrder.equals(STYLE_ONE_WAY))
    {
      style = OperationType.ONE_WAY;
    }
    else if (messageOrder.equals(STYLE_REQUEST_RESPONSE))
    {
      style = OperationType.REQUEST_RESPONSE;
    }
    else if (messageOrder.equals(STYLE_SOLICIT_RESPONSE))
    {
      style = OperationType.SOLICIT_RESPONSE;
    }
    else if (messageOrder.equals(STYLE_NOTIFICATION))
    {
      style = OperationType.NOTIFICATION;
    }

    if (style != null)
    {
      op.setStyle(style);
    }

    // add the location of this element to elementLocations 
    setLocation(op, opEl);

    // modified to remove duplicate operations
    //    if(opDefined)
    //    {
    //      addReaderError(portType,op,ValidateWSDLPlugin.getInstance().getString("_DUPLICATE_OPERATION_FOR_PORTTYPE","'"+op.getName()+"'","'"+portType.getQName().getLocalPart()+"'"));
    //      return null;
    //    }
    return op;
  }

  /**
   * Parse a specific service element.
   * 
   * @param serviceEl The service element.
   * @param def The defintions element.
   * @return A WSDL service element.
   * @throws WSDLException
   */
  protected Service parseService(Element serviceEl, Definition def) throws WSDLException
  {
    Service service = def.createService();
    String name = DOMUtils.getAttribute(serviceEl, Constants.ATTR_NAME);

    if (name != null)
    {
      service.setQName(new QName(def.getTargetNamespace(), name));
    }
    Service s;
    // a service with this name has already been defined
    if ((s = def.getService(service.getQName())) != null)
    {
      addReaderError(
        def,
        serviceEl,
        messagegenerator.getString("_SERVICE_NAME_ALREADY_DEFINED", "'" + s.getQName().getLocalPart() + "'"));
      return s;
    }
    Element tempEl = DOMUtils.getFirstChildElement(serviceEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        service.setDocumentationElement(tempEl);
      }
      else if (QNameUtils.matches(Constants.Q_ELEM_PORT, tempEl))
      {
        service.addPort(parsePort(tempEl, def));
      }
      else
      {
        service.addExtensibilityElement(parseExtensibilityElement(Service.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(service, serviceEl);

    return service;
  }
  
  /**
   * Parse a specific port element.
   * 
   * @param portEl The port element.
   * @param def The definitions element.
   * @return A WSDL port element.
   * @throws WSDLException
   */
  protected Port parsePort(Element portEl, Definition def) throws WSDLException
  {
    Port port = def.createPort();
    String name = DOMUtils.getAttribute(portEl, Constants.ATTR_NAME);
    QName bindingStr;
    try
    {
      bindingStr = DOMUtils.getQualifiedAttributeValue(portEl, Constants.ATTR_BINDING, Constants.ELEM_PORT, false, def);
    }
    catch (Exception e)
    {
      //the call above fails if there is no qualified namespace for the message name
      bindingStr = new QName(null, DOMUtils.getAttribute(portEl, "binding"));
    }

    if (name != null)
    {
      port.setName(name);
    }

    if (bindingStr != null)
    {
      Binding binding = def.getBinding(bindingStr);

      if (binding == null)
      {
        binding = def.createBinding();
        binding.setQName(bindingStr);
        def.addBinding(binding);
      }

      port.setBinding(binding);
    }

    Element tempEl = DOMUtils.getFirstChildElement(portEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        port.setDocumentationElement(tempEl);
      }
      else
      {
        port.addExtensibilityElement(parseExtensibilityElement(Port.class, tempEl, def));
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(port, portEl);

    return port;
  }

  /**
   * Parse a specific extensibility element.
   * 
   * @param parentType The parent type of the extensibility element.
   * @param el The extensibility element.
   * @param def The definitions element.
   * @return A WSDL extensibility element.
   * @throws WSDLException
   */
  protected ExtensibilityElement parseExtensibilityElement(Class parentType, Element el, Definition def)
    throws WSDLException
  {
    QName elementType = QNameUtils.newQName(el);

    try
    {
      ExtensionRegistry extReg = def.getExtensionRegistry();

      if (extReg == null)
      {
        throw new WSDLException(
          WSDLException.CONFIGURATION_ERROR,
          "No ExtensionRegistry set for this "
            + "Definition, so unable to deserialize "
            + "a '"
            + elementType
            + "' element in the "
            + "context of a '"
            + parentType.getName()
            + "'.");
      }

      ExtensionDeserializer extDS = extReg.queryDeserializer(parentType, elementType);

      // asign the ExtensibilityElement to a var so we can add it to the locations
      ExtensibilityElement extElem = extDS.unmarshall(parentType, elementType, el, def, extReg);
      // add the location of this element to elementLocations 
      // this might not work properly
      setLocation(extElem, el);

      // register all of the child Elements so we can find them later
      // used for inline schema validation
      registerChildElements(extElem);

      return extElem;
    }
    catch (WSDLException e)
    {
      if (e.getLocation() == null)
      {
        e.setLocation(XPathUtils.getXPathExprFromNode(el));
      }
      throw e;
    }
  }

  /**
   * Parse a specific input element.
   * 
   * @param inputEl The input element.
   * @param def The defintions element.
   * @return A WSDL input element.
   * @throws WSDLException
   */
  protected Input parseInput(Element inputEl, Definition def) throws WSDLException
  {

    Input input = def.createInput();
    String name = DOMUtils.getAttribute(inputEl, Constants.ATTR_NAME);
    QName messageName = null;
    try
    {
      messageName = DOMUtils.getQualifiedAttributeValue(inputEl, Constants.ATTR_MESSAGE, Constants.ELEM_INPUT, false, def);
    }
    catch (Exception e)
    {
      //the call above fails if there is no qualified namespace for the message name
      messageName = new QName(null, DOMUtils.getAttribute(inputEl, "message"));
    }

    if (name != null)
    {
      input.setName(name);
    }

    if (messageName != null)
    {
      Message message = def.getMessage(messageName);

      if (message == null)
      {
        message = def.createMessage();
        message.setQName(messageName);
        def.addMessage(message);
      }

      input.setMessage(message);
    }

    Element tempEl = DOMUtils.getFirstChildElement(inputEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        input.setDocumentationElement(tempEl);
      }
      else
      {
        // XML Validation will catch this
        DOMUtils.throwWSDLException(tempEl);
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(input, inputEl);

    return input;
  }

  /**
   * Parse a specific output element.
   * 
   * @param outputEl The output element.
   * @param def The defintions element.
   * @return A WSDL output element.
   * @throws WSDLException
   */
  protected Output parseOutput(Element outputEl, Definition def) throws WSDLException
  {
    Output output = def.createOutput();
    String name = DOMUtils.getAttribute(outputEl, Constants.ATTR_NAME);
    QName messageName = null;
    try
    {
      messageName = DOMUtils.getQualifiedAttributeValue(outputEl, Constants.ATTR_MESSAGE, Constants.ELEM_OUTPUT, false, def);
    }
    catch (Exception e)
    {
      //the call above fails if there is no qualified namespace for the message name
      messageName = new QName(null, DOMUtils.getAttribute(outputEl, "message"));
    }

    if (name != null)
    {
      output.setName(name);
    }

    if (messageName != null)
    {
      Message message = def.getMessage(messageName);

      if (message == null)
      {
        message = def.createMessage();
        message.setQName(messageName);
        def.addMessage(message);
      }

      output.setMessage(message);
    }

    Element tempEl = DOMUtils.getFirstChildElement(outputEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        output.setDocumentationElement(tempEl);
      }
      else
      {
        // XML Validation will catch this
        DOMUtils.throwWSDLException(tempEl);
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(output, outputEl);

    return output;
  }

  /**
   * Parse a specific fault element.
   * 
   * @param faultEl The fault element to parse.
   * @param def The definitions element.
   * @return A WSDL fault element.
   * @throws WSDLException
   */
  protected Fault parseFault(Element faultEl, Definition def) throws WSDLException
  {
    Fault fault = def.createFault();
    String name = DOMUtils.getAttribute(faultEl, Constants.ATTR_NAME);
    QName messageName = null;
    try
    {
      messageName = DOMUtils.getQualifiedAttributeValue(faultEl, Constants.ATTR_MESSAGE, Constants.ELEM_INPUT, false, def);
    }
    catch (Exception e)
    {
      //the call above fails if there is no qualified namespace for the message name
      messageName = new QName(null, DOMUtils.getAttribute(faultEl, "message"));
    }

    if (name != null)
    {
      fault.setName(name);
    }

    if (messageName != null)
    {
      Message message = def.getMessage(messageName);

      if (message == null)
      {
        message = def.createMessage();
        message.setQName(messageName);
        def.addMessage(message);
      }

      fault.setMessage(message);
    }

    Element tempEl = DOMUtils.getFirstChildElement(faultEl);

    while (tempEl != null)
    {
      if (QNameUtils.matches(Constants.Q_ELEM_DOCUMENTATION, tempEl))
      {
        fault.setDocumentationElement(tempEl);
      }
      else
      {
        // XML Validation will catch this
        DOMUtils.throwWSDLException(tempEl);
      }

      tempEl = DOMUtils.getNextSiblingElement(tempEl);
    }

    // add the location of this element to elementLocations 
    setLocation(fault, faultEl);

    return fault;
  }
  
  /**
   * Set the messagegenerator for the reader.
   * 
   * @param mg The message generator to set.
   */
  public void setMessageGenerator(MessageGenerator mg)
  {
    messagegenerator = mg;
  }

  /**
   * Add the refObject to the elementLocation hashtable with the location defined in element.
   * 
   * @param refObject The object to add.
   * @param element The element that contains the location information.
   */
  protected void setLocation(Object refObject, Element element)
  {
    try
    {
      ElementImpl elementImpl = (ElementImpl)element;
      ElementLocation elementLocation = (ElementLocation)elementImpl.getUserData();
      if (elementLocation != null)
      {

        elementLocations.put(
          refObject,
          new LocationHolder(elementLocation.getLineNumber(), elementLocation.getColumnNumber(), def.getDocumentBaseURI()));
      }
    }
    catch (ClassCastException e)
    {
    }
  }
  /**
   * Add a reader error to the list.
   * 
   * @param parentobject The parent object of the object with the error.
   * @param object The object with the error.
   * @param error The error message.
   */
  protected void addReaderError(Object parentobject, Object object, String error)
  {
    readerErrors.add(new ReaderError(parentobject, object, error));
  }
  
  /**
   * Add a reader warning to the list.
   * 
   * @param parentobject The parent object of the object with the error.
   * @param object The object with the error.
   * @param warning The warning message.
   */
  protected void addReaderWarning(Object parentobject, Object object, String warning)
  {
    readerWarnings.add(new ReaderError(parentobject, object, warning));
  }
  
  /**
   * Register all of the locations of the child elements of the extensibility
   * element given.
   * 
   * @param extElem The extensibility element whose child elements will be registered.
   */

  protected void registerChildElements(ExtensibilityElement extElem)
  {
    // only add those that are of type unknown. if they're known they 
    // will take care of themselves
    if (extElem instanceof UnknownExtensibilityElement)
    {
      Element elem = ((UnknownExtensibilityElement)extElem).getElement();
      registerChildElementsRecursively(elem);
    } else if (extElem instanceof Schema)
    {
        Element elem = ((Schema)extElem).getElement();
        registerChildElementsRecursively(elem);
    }
  }

  /**
   * Register the location of all of the child elements of elem.
   * 
   * @param elem The element whose child elements will be registered.
   */
  protected void registerChildElementsRecursively(Element elem)
  {
    if (elem instanceof ElementNSImpl)
    {
      setLocation(elem, elem);

      // call the method recursively for each child element
      NodeList childNodes = elem.getChildNodes();

      for (int i = 0; i < childNodes.getLength() || i < 5; i++)
      {
        Node n = childNodes.item(i);
        // we only want nodes that are Elements
        if (n instanceof Element)
        {
          Element child = (Element)n;
          registerChildElementsRecursively(child);
        }
      }
    }
  }
  /**
   * Check that an element name matches the expected name.
   * 
   * @param el The element with the name to check.
   * @param qname The name to check against.
   * @throws WSDLException
   */
  private static void checkElementName(Element el, QName qname) throws WSDLException
  {
    if (!QNameUtils.matches(qname, el))
    {
      WSDLException wsdlExc = new WSDLException(WSDLException.INVALID_WSDL, "Expected element '" + qname + "'.");

      wsdlExc.setLocation(XPathUtils.getXPathExprFromNode(el));

      throw wsdlExc;
    }
  }
  
  /**
   * Get the element locations hashtable.
   * 
   * @return The element locations hashtable.
   */
  public Hashtable getElementLocations()
  {
    return elementLocations;
  }
  
  /**
   * Get the reader errors.
   * 
   * @return The reader errors.
   */
  public List getReaderErrors()
  {
    return readerErrors;
  }
  
  /**
   * Get reader warnings.
   * 
   * @return The reader warnings.
   */
  public List getReaderWarnings()
  {
    return readerWarnings;
  }
}