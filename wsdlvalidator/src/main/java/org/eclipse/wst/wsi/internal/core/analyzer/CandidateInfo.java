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
package org.eclipse.wst.wsi.internal.core.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.xml.namespace.QName;

import org.apache.xerces.util.URI;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.config.WSDLElement;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLUtils;

/**
 * Provide a normalized set of data relating to the service under test.
 * For example, with endpoint correlation and wsdlElement of type "message" 
 * there could be multiple matches for most element types including WSDL Service.
 * (though the objective is to ensure filtering to a single service if at all possible). 
 * 
 * @author gturrell
 */

// ADD:could be better as a singleton?

public class CandidateInfo
{

  // Normalized fixed correlation data derived from the analyzer config and the wsdl,
  // representing what we know about the service from these two sources.  
  private Definition[] definitions = null;

  private Import[] imports = null;
  private Types[] types = null;

  private Message[] messages = null;
  private Operation[] operations = null;
  private PortType[] portTypes = null;
  private Binding[] bindings = null;
  private Port[] ports = null;

  private URI[] endPoints = null; // list obtainable from a port

  private WSDLDocument wsdlDocument; // reference probably not desirable here

  /**
   * Constructor for CandidateInfo.
   * Extract normalised entry data from wsdl, according to config wsdlElement & serviceLocation
   * @param serviceReference a ServiceReference object.
   * @param wsdlDocument a WSDL document.
   * @throws WSIException if problems occur creating CandidateInfo object.
   */
  public CandidateInfo(
    ServiceReference serviceReference,
    WSDLDocument wsdlDocument)
    throws WSIException
  {

    // ADD: check for null document?

    WSDLElement wsdlElement = serviceReference.getWSDLElement();

    this.wsdlDocument = wsdlDocument;

    /*
     * Generalised fields independent of wsdlElement:
     */
    // ADD: check whether these need to be either expanded or filtered down.
    // Assume WSDL4J pulls int the full tree at the root document for now		
    //this.imports = wsdlDocument.getImports();

    // ... or if only down to first level....
    this.imports =
      (Import[]) getAllImports(
        wsdlDocument.getDefinitions()).toArray(new Import[] {
    });

    /* Definitions. 
     * Note that the the first array element is for the root doc
     * which contains all WSDL elements in scope via <import> declarations,
     * as well as the root document itself. Therefore the second definitions 
     * array element and above are redundant, but *may* prove useful to the assertion
     * code.
     */

    this.definitions = new Definition[imports.length + 1];
    // allow room for root doc		

    this.definitions[0] = wsdlDocument.getDefinitions(); // root document    

    // Allocate array for types elements
    Types[] tempTypes = new Types[definitions.length];

    int typesCount = 0;

    if (definitions[0].getTypes() != null)
    {
      tempTypes[0] = this.definitions[0].getTypes(); // root document	
      typesCount++;
    }

    // Definitions from other (imported) wsdls correlating to the candidate
    // Only one level down for now
    for (int i = 0; i < imports.length; i++)
    {
      if (((definitions[i + 1] = imports[i].getDefinition()) != null)
        && (definitions[i + 1].getTypes() != null))
      {
        tempTypes[typesCount] = definitions[i + 1].getTypes();
        typesCount++;
      }
    }

    if (typesCount > 0)
    {
      this.types = new Types[typesCount];

      for (int i = 0; i < typesCount; i++)
        this.types[i] = tempTypes[i];
    }
    /* 
     * Populate element hierachy:
     * Port
     * Binding
     * PortType
     * operation(s)
     * message(s)
     */

    if (wsdlElement.isPort())
    {

      Port port = null;

      // Use parentElementName to qualify the port within a service.		
      QName serviceName = wsdlElement.getParentElementQName();
      Service[] s = wsdlDocument.getServices();
      String portName = wsdlElement.getName();
      for (int i = 0; i < s.length && port == null; i++)
      {
        if (s[i].getQName().equals(serviceName))
        {
          port = s[i].getPort(portName);
        }
      }

      if (port == null)
      {
        throw new WSIException(
          "WSDL Port \'"
            + portName
            + "\' for Service \'"
            + serviceName
            + "\' not found in service description");
      }
      else
      {
        this.ports = new Port[] { port };
        // ADD: do serviceLocation check for soapbind:address?
        descendents(port);
      }

      // ADD: the following could be instantiated here instead to refine context info
      // definitions 
      // imports 
      // types

    }

    else if (wsdlElement.isBinding())
    {
      if (wsdlElement.getQName() != null
        && wsdlElement.getQName().getLocalPart() != null
        && wsdlElement.getQName().getLocalPart().length() > 0)
      {
        Binding binding =
          wsdlDocument.getDefinitions().getBinding(wsdlElement.getQName());

        if (binding == null)
        {
          throw new WSIException(
            "WSDL Binding named \'"
              + wsdlElement.getQName()
              + "\' not found in service description");
        }
        else
        {
          this.bindings = new Binding[] { binding };

          // the rest ... below binding:	
          // portTypes from binding
          // operations from portTypes
          // messages from operations
          descendents(binding);

          // above binding:
          // ports
          // definitions, imports, types (future?)				
          // ancestors(bindings);
        }
      }
    }

    else if (wsdlElement.isPortType())
    {

      PortType portType =
        wsdlDocument.getDefinitions().getPortType(wsdlElement.getQName());
      this.portTypes = new PortType[] { portType };

      if (portType == null)
      {
        throw new WSIException(
          "WSDL PortType named \'"
            + wsdlElement.getQName()
            + "\' not found in service description");
      }
      else
      {
        this.portTypes = new PortType[] { portType };
        // the rest ... below portType:	
        descendents(portType);

        // above portType:
        // ports
        // definitions, imports, types (future) ?
        //ancestors(portTypes);		
      }
    }
    else if (wsdlElement.isOperation())
    {

      Operation operation = null;
      String configOpName = wsdlElement.getName();

      // Use parentElementName to qualify the operation within a portType.
      QName portTypeName = wsdlElement.getParentElementQName();
      PortType[] p = wsdlDocument.getPortTypes();
      for (int i = 0; i < p.length && operation == null; i++)
      {
        if (p[i].getQName().equals(portTypeName))
        {
          // wsdl4j available method call below implies that only 
          // name+inputname+outputname uniquely defines operation! 
          // Since we do not have <input> & <output> name information
          // available in the config, use this instead for now: - 
          // Get the first operation we find:
          Iterator opIt = p[i].getOperations().iterator();
          Operation op = null;
          while (opIt.hasNext() && operation == null)
          {
            op = (Operation) opIt.next();
            if (configOpName.equals(op.getName()))
            {
              operation = op;
            }
          }
        }
      }

      if (operation == null)
      {
        throw new WSIException(
          "No WSDL Operation named \'"
            + wsdlElement.getQName()
            + "\' found in service description");
      }
      else
      {
        this.operations = new Operation[] { operation };

        descendents(operation);
        //ancestors(operations);
      }
    }

    else if (wsdlElement.isMessage())
    {

      Message message =
        wsdlDocument.getDefinitions().getMessage(wsdlElement.getQName());
      if (message == null)
      {
        throw new WSIException(
          "No WSDL Message named \'"
            + wsdlElement.getQName()
            + "\' found in service description");
      }
      else
      {
        this.messages = new Message[] { message };

        //ancestors(messages); 
      }
    }

    else
    {
      throw new WSIException(
        "Unrecognised <WSDLElement type> in config: " + wsdlElement.getType());
    }

    // get info about the effective service location (s)
    //this.endPoints = deriveEndpoints(analyzerConfig, this.ports, this.definitions);
    this.endPoints =
      deriveEndpoints(serviceReference, this.ports, this.definitions);
  }

  /** 
     * ancestor code is not used at present.
     * 
     */
  /*   
     protected void ancestors(Port[] ports) {
     	// no ancestors of Port required (for now)
     } 	    
     protected void ancestors(Binding[] bindings) {	    
    		// Ports from Bindings  1-2-1
     	
     	// *** cheat for now - get ports from all services in the wsdl doc
     	// ADD: find correct mapping based on the supplied bindings
     	Service[] service = wsdlDocument.getServices();
     	HashSet set = new HashSet();
  		for (int i=0; i>service.length; i++) {
  			set.add(service[i].getPorts());  
  		}
  			
  		// assign the parent group and process the grandparents if any								
  		ancestors(this.ports = (Port[])set.toArray(this.ports = new Port[0]));
     }
     protected void ancestors(PortType[] portTypes) {	    
    		// Bindings from PortTypes  1-2-1
     	
     	// add have to start with all bindings in doc and search for those
     	// with the portType  
     	HashSet set = new HashSet();
  		for (int i=0; i>portTypes.length; i++) {
  		// set.add(portTypes[i].get());  
  		}
  			
  		// assign the parent group and process the grandparents if any								
  		ancestors(this.bindings = (Binding[])set.toArray(this.bindings = new Binding[0]));
     }
     protected void ancestors(Operation[] operations) {	    
    		// PortTypes from Operations  1-2-1
     	  
     	HashSet set = new HashSet();
  		for (int i=0; i>operations.length; i++) {
  			// set.add(operations[i].get());  
  		}
  			
  		// assign the parent group and process the grandparents if any								
  		ancestors(this.portTypes = (PortType[])set.toArray(this.portTypes = new PortType[0]));
     }
     protected void ancestors(Message[] messages) {	    
    		// Operations from Messages  1-2-1
     	// ADD fix it!  
     	HashSet set = new HashSet();
  		for (int i=0; i>messages.length; i++) {
  			// set.add(messages[i].get());  
  		}
  			
  		// assign the parent group 	
  		this.portTypes = (PortType[])set.toArray(this.portTypes = new PortType[0]);
     }
  */

  /**
   * Descendant method for completing candidate service context creation.
   * @param port  a Port object
   * @throws WSIException if port is null.
   */
  protected void descendents(Port port) throws WSIException
  {
    // Binding from Port  1-2-1

    if (port == null)
    {
      throw new WSIException("Internal error: expected a Port value");
    }
    else
    {
      this.bindings = new Binding[] { port.getBinding()};
      if (this.bindings[0] != null)
      {
        /* Assign the child group and process the grandchildren if any.
        * Null argument value passed into the following method would 
        * suggest a WSDL definition inconsistency
        * which will be picked up during Description Artifact TA testing.
        */
        descendents(this.bindings[0]);
      }
    }
  }

  /**
   * Descendant method for completing candidate service context creation.
   * @param binding  a Binding object
   * @throws WSIException if binding is null.
   */
  protected void descendents(Binding binding) throws WSIException
  {
    // portType from Binding 1-2-1

    if (binding == null)
    {
      throw new WSIException("Internal error: expected a Binding value");
    }
    else
    {
      this.portTypes = new PortType[] { binding.getPortType()};
      if (this.portTypes[0] != null)
      {
        /* Assign the child group and process the grandchildren if any.
        	* Null argument value passed into the following method would 
        	* suggest a WSDL definition inconsistency
        	* which will be picked up during Description Artifact TA testing.
        	*/
        descendents(this.portTypes[0]);

        // Get any messages that are referenced from headers and headerfaults

        // Get reference to definition
        Definition definition = definitions[0];

        // If there are messages already, then get them as a collection
        HashSet messageSet = new HashSet();
        if (messages != null)
        {
          for (int i = 0; i < messages.length; i++)
          {
            messageSet.add(messages[i]);
          }
        }

        // Get the messages that are referenced only by a binding 
        HashSet bindingMessages = WSDLUtils.findMessages(definition, binding);

        // Add these messages to the complete message list  
        messageSet.addAll(bindingMessages);

        // Create array from message set
        this.messages =
          (Message[]) messageSet.toArray(this.messages = new Message[0]);
      }
    }
  }

  /**
   * Descendant method for completing candidate service context creation.
   * @param portType  a PortType object
   * @throws WSIException if portType is null.
   */
  protected void descendents(PortType portType) throws WSIException
  {
    // Operations from PortType 1-2-n

    if (portType == null)
    {
      throw new WSIException("Internal error: expected a PortType value");
    }
    else
    {
      this.operations =
        (Operation[]) (portType
          .getOperations()
          .toArray(this.operations = new Operation[0]));
      if (this.operations.length > 0)
      {
        descendents(this.operations);
      }
    }
  }

  /**
     * Descendant method for completing candidate service context creation.
     * @param operation  a Operation object
     * @throws WSIException if operation is null.
     */
  protected void descendents(Operation operation) throws WSIException
  {
    // Messages from Operation

    if (operation == null)
    {
      throw new WSIException("Internal error: expected an Operation value");
    }
    else
    {
      descendents(new Operation[] { operation });
    }
  }

  /**
   * Descendant method for completing candidate service context creation.
   * @param operations  an array of operations.
   * @throws WSIException if operations is null.
   */
  protected void descendents(Operation[] operations) throws WSIException
  {
    // Messages from Operations 1-2-n

    if (operations == null)
    {
      throw new WSIException("Internal error: expected an Operation[] value");
    }

    HashSet set = new HashSet();
    for (int i = 0; i < operations.length; i++)
    {
      if (operations[i].getInput() != null)
        set.add(operations[i].getInput().getMessage()); //1-2-1
      if (operations[i].getOutput() != null)
        set.add(operations[i].getOutput().getMessage()); //1-2-1

      // get messages associated with faults for this operation 
      // 1-2-n
      Iterator it = operations[i].getFaults().values().iterator();
      while (it.hasNext())
      {
        set.add(((Fault) it.next()).getMessage());
      }
    }
    this.messages = (Message[]) set.toArray(this.messages = new Message[0]);
    // no descendents of messages so stop.
  }

  /**
   * Provide a recursive non-repeating list of imports for the specified
   * WSDL document definition.
   */
  /*   private HashSet getAllImports(Definition rootDef) throws WSIException {
     	
     	HashSet importSet = new HashSet();
     	
     	Collection importList =  rootDef.getImports().values();
     	Iterator i = importList.iterator();
     	
     	while (i.hasNext()) {
     		Import nextImport = (Import)(i.next());
     		if (nextImport != null) {
     			// its a wsdl document
     			importSet.addAll(getAllImports(nextImport.getDefinition()));
     		}   			
     	}
     	
     	return (importSet);
     }
   */
  private HashSet getAllImports(Definition rootDef) throws WSIException
  {

    HashSet importSet = new HashSet();
    importSet =  getAllImports(new ArrayList(), rootDef);
    return (importSet);
  }
  
  private HashSet getAllImports(List alreadyProcessedDefinitions, Definition rootDef) 
  {
	HashSet importSet = new HashSet();
	if ((rootDef != null) && (!alreadyProcessedDefinitions.contains(rootDef)))
	{
      alreadyProcessedDefinitions.add(rootDef);	
	  Map importMap = rootDef.getImports();
      Iterator i = importMap.values().iterator();

      while (i.hasNext())
      {
        List nextImportList = (List) (i.next());
        Iterator listIt = nextImportList.iterator();
        while (listIt.hasNext())
        {
          Import nextImport = (Import) listIt.next();
          if (nextImport != null)
          {
            importSet.add(nextImport);
            Definition def = nextImport.getDefinition();
       	    HashSet nestedImports = getAllImports(alreadyProcessedDefinitions, def);
            for (Iterator j = nestedImports.iterator(); j.hasNext();)
              importSet.add(j.next());
          }
        }
      }
    }
    return importSet;
  }

  /**
   * get all service location endpoint values
   * relevant to the service under test.
   * 
   * If the service location is specified in the config we use
   * just that.
   * Otherwise, if the port is specified, we get all endpoints
   * associated with that port.
   * If we have neither the service location value or the port,
   * then all endpoints in the definition are used.
   */
  private URI[] deriveEndpoints(
  //AnalyzerConfig analyzerConfig, 
  ServiceReference serviceReference, Port[] ports, Definition[] definitions)
  {

    URI[] endp = null;
    //Port port = null;	
    try
    {
      String serviceLocation = null;
      // try service location...
      //if ((serviceLocation = analyzerConfig.getServiceLocation()) != null) {      
      if ((serviceLocation = serviceReference.getServiceLocation()) != null)
      {
        endp = new URI[] { new URI(serviceLocation)};
      }
      //else if (analyzerConfig.getWSDLElement().isPort()) {
      else if (serviceReference.getWSDLElement().isPort())
      {
        if (ports.length != 1)
        {
          throw new WSIException("Internal error - expected 1-element Port array");
        }
        //else {
        //	port = ports[0]; // if Port was given in config, there is just one
        //}
        //QName soapAddress = new QName(WSIConstants.NS_URI_WSDL_SOAP, "address");
        Iterator i = ports[0].getExtensibilityElements().iterator();
        while (i.hasNext() && serviceLocation == null)
        {
          ExtensibilityElement extElem = (ExtensibilityElement) i.next();
          if (extElem instanceof SOAPAddress)
          {
            //if (extEl.getElementType().equals(soapAddress) {
            // this element is our SOAPAddress - get the location
            serviceLocation = ((SOAPAddress) extElem).getLocationURI();
            endp = new URI[] { new URI(serviceLocation)};
          }
        }
      }
      else
      { // no port info from config, so supply all in document
        // QName soapAddress = new QName(WSIConstants.NS_URI_WSDL_SOAP, "address");
        HashSet endpointSet = new HashSet();
        Iterator i = definitions[0].getExtensibilityElements().iterator();
        while (i.hasNext())
        {
          ExtensibilityElement extElem = (ExtensibilityElement) i.next();
          if (extElem instanceof SOAPAddress)
          {
            //if (extEl.getElementType().equals(soapAddress) {
            // this element is our SOAPAddress - get the location
            endpointSet.add(((SOAPAddress) extElem).getLocationURI());
          }
        }
        // Convert the derived List to a URI array
        endp = (URI[]) endpointSet.toArray(endp = new URI[0]);
      }
    }
    catch (Exception e)
    {
    }
    return endp;
  }

  /**
   * Returns the binding.
   * @return Binding
   */
  public Binding[] getBindings()
  {
    return bindings;
  }

  /**
   * Returns the endPoints.
   * @return URI[]
   */
  public URI[] getEndPoints()
  {
    // get list of matching endpoint(s) associated with service.
    return endPoints;
  }

  /**
  * Returns the endPoints matching the specified host and port.
  * @param hostAndPort  host and port location.
  * @return URI[] if matched, null otherwise.
  */
  public URI[] getEndPoints(String hostAndPort)
  {
    // get list of matching endpoints associated with service,
    // having the specified host and port configuration
    String port;

    Vector matchedEndpoints = new Vector();
    for (int i = 0; i < endPoints.length; i++)
    {
      // PB: If the endpoint does not contain a port number, then default it to "80"
      port =
        (endPoints[i].getPort() == -1)
          ? "80"
          : String.valueOf(endPoints[i].getPort());
      if (hostAndPort.equals(endPoints[i].getHost() + ":" + port))
      {
        matchedEndpoints.add(endPoints[i]);
      }
    }
    return (URI[]) matchedEndpoints.toArray(new URI[0]);
  }

  /**
   * Returns the operation.
   * @return Operation
   */
  public Operation[] getOperations()
  {
    return operations;
  }

  /**
   * Returns the portType.
   * @return PortType
   */
  public PortType[] getPortType()
  {
    return portTypes;
  }

  /**
   * Returns true if the hostAndPort matches at least one context endpoint.
   * @param hostAndPortString a host and port location.
   * @return true if the hostAndPort matches at least one context endpoint.
   * @throws WSIException if given hostandPort String does not convert to URI.
   */
  public boolean hasHostAndPort(String hostAndPortString) throws WSIException
  {
    URI hostAndPort;
    try
    {
      hostAndPort = new URI(hostAndPortString);
    }
    catch (Exception e)
    {
      throw new WSIException(
        "Could not convert string to URI: " + hostAndPortString);
    }
    String host = hostAndPort.getHost();
    int port = hostAndPort.getPort();
    for (int i = 0; i < this.endPoints.length; i++)
    {
      if (this.endPoints[i].getHost().equals(host)
        && this.endPoints[i].getPort() == port)
      {
        return true;
      }
    }
    return false; // for now
  }

  /**
   * Returns the definitions.
   * @return Definition[]
   */
  public Definition[] getDefinitions()
  {
    return definitions;
  }

  /**
   * Returns the imports.
   * @return Import[]
   */
  public Import[] getImports()
  {
    return imports;
  }

  /**
   * Returns the messages.
   * @return Message[]
   */
  public Message[] getMessages()
  {
    return messages;
  }

  /**
   * Returns the ports.
   * @return Port[]
   */
  public Port[] getPorts()
  {
    return ports;
  }

  /**
   * Returns the portTypes.
   * @return PortType[]
   */
  public PortType[] getPortTypes()
  {
    return portTypes;
  }

  /**
   * Returns the types.
   * @return Types[]
   */
  public Types[] getTypes()
  {
    return types;
  }
  /**
   * Returns the wsdlDocument.
   * @return WSDLDocument
   */
  public WSDLDocument getWsdlDocument()
  {
    return wsdlDocument;
  }

  /**
   * Return the definition element that contains the types element.
   * @param types a Types object.
   * @return the definition element that contains the types element.
   */
  public Definition getDefinition(Types types)
  {
    Definition definition = null;
    Types checkTypes;

    for (int i = 0; i < definitions.length && definition == null; i++)
    {
      if (((checkTypes = definitions[i].getTypes()) != null)
        && (checkTypes.equals(types)))
      {
        definition = definitions[i];
      }
    }

    return definition;
  }

  /**
   * Return the definition element that contains the binding element.
   * @param binding a Binding object.
   * @return the definition element that contains the binding element.
   */
  public Definition getDefinition(Binding binding)
  {
    Definition definition = null;

    for (int i = 0; i < definitions.length && definition == null; i++)
    {
      if (definitions[i].getBinding(binding.getQName()) != null)
        definition = definitions[i];
    }

    return definition;
  }

  /**
   * Return the definition element that contains the portType element.
   * @param portType a PortType object.
   * @return the definition element that contains the portType element.
   */
  public Definition getDefinition(PortType portType)
  {
    Definition definition = null;

    for (int i = 0; i < definitions.length && definition == null; i++)
    {
      if (definitions[i].getPortType(portType.getQName()) != null)
        definition = definitions[i];
    }

    return definition;
  }

  /**
   * Return the definition element that contains the message.
   * @param message a Message object.
   * @return the definition element that contains the message.
   */
  public Definition getDefinition(Message message)
  {
    Definition definition = null;

    for (int i = 0; i < definitions.length && definition == null; i++)
    {
      if (definitions[i].getMessage(message.getQName()) != null)
        definition = definitions[i];
    }

    return definition;
  }

}
