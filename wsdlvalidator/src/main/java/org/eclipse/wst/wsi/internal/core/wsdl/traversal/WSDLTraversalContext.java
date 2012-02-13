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
package org.eclipse.wst.wsi.internal.core.wsdl.traversal;

import java.util.Map;
import java.util.TreeMap;

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
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPFault;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPHeaderFault;
import javax.wsdl.extensions.soap.SOAPOperation;

import org.w3c.dom.Element;

/**
 * The class represents runtime context for the traversal process.
 * Context stores artifacts being processed.
 * 
 * @author Andrey Kulik
 */
public class WSDLTraversalContext
{
  /**
   * Flags which indicate whether corresponding WSDL element should be processed
   * or not.
   */
  private boolean processPart = true;
  private boolean processService = true;
  private boolean processTypes = true;
  private boolean processOperation = true;
  private boolean processInput = true;
  private boolean processOutput = true;
  private boolean processFault = true;
  private boolean processBinding = true;
  private boolean processBindingOperation = true;
  private boolean processBindingInput = true;
  private boolean processBindingOutput = true;
  private boolean processBindingFault = true;
  private boolean processImport = true;
  private boolean processElement = true;
  private boolean processMessage = true;
  private boolean processPort = true;
  private boolean processPortType = true;
  private boolean processDefinition = true;
  private boolean processExtensibilityElement = true;
  private boolean processSOAPBinding = true;
  private boolean processSOAPHeader = true;
  private boolean processSOAPHeaderFault = true;
  private boolean processSOAPFault = true;
  private boolean processSOAPOperation = true;

  /**
   * Last processed WSDL elements. 
   */
  private Part activePart = null;
  private Service activeService = null;
  private Types activeTypes = null;
  private Operation activeOperation = null;
  private Input activeInput = null;
  private Output activeOutput = null;
  private Fault activeFault = null;
  private Binding activeBinding = null;
  private BindingOperation activeBindingOperation = null;
  private BindingInput activeBindingInput = null;
  private BindingOutput activeBindingOutput = null;
  private BindingFault activeBindingFault = null;
  private Import activeImport = null;
  private Element activeElement = null;
  private Message activeMessage = null;
  private Port activePort = null;
  private PortType activePortType = null;
  private Definition activeDefinition = null;
  private ExtensibilityElement activeExtensibilityElement = null;
  private SOAPBinding activeSOAPBinding = null;
  private SOAPBody activeSOAPBody = null;
  private SOAPHeader activeSOAPHeader = null;
  private SOAPHeaderFault activeSOAPHeaderFault = null;
  private SOAPFault activeSOAPFault = null;
  private SOAPOperation activeSOAPOperation = null;

  /**
   * Parameters map
   */
  Map params = new TreeMap();

  /**
   * Active traversal.
   */
  final private WSDLTraversal traversal;

  /**
   * The default constructor.
   * @param traversal
   */
  WSDLTraversalContext(WSDLTraversal traversal)
  {
    this.traversal = traversal;
  }

  /**
   * Gets the active traversal.
   * @return tha active traversal.
   */
  public WSDLTraversal getTraversal()
  {
    return traversal;
  }

  /**
   * Gets parameter from context by the given key. 
   * @param key  a key value.
   * @return a parameter corresponding to the given key.
   */
  public Object getParameter(Object key)
  {
    return params.get(key);
  }

  /**
   * Adds parameter into context with the given key.
   * @param key    the key.
   * @param value  the value.
   */
  public void addParameter(Object key, Object value)
  {
    params.put(key, value);
  }

  /**
   * Removes the parameter from context by the given key.
   * @param key  a key value.
   */
  public void removeParameter(Object key)
  {
    params.remove(key);
  }

  /**
   * Cancels processing of the corresponding WSDL part. 
   */
  public void cancelPartProcessing()
  {
    processPart = false;
  }
  /**
   * Cancels processing of the corresponding WSDL service. 
   */
  public void cancelServiceProcessing()
  {
    processService = false;
  }
  /**
   * Cancels processing of the corresponding WSDL types. 
   */
  public void cancelTypesProcessing()
  {
    processTypes = false;
  }
  /**
   * Cancels processing of the corresponding WSDL operation. 
   */
  public void cancelOperationProcessing()
  {
    processOperation = false;
  }
  /**
   * Cancels processing of the corresponding WSDL input. 
   */
  public void cancelInputProcessing()
  {
    processInput = false;
  }
  /**
   * Cancels processing of the corresponding WSDL output. 
   */
  public void cancelOutputProcessing()
  {
    processOutput = false;
  }
  /**
   * Cancels processing of the corresponding WSDL fault. 
   */
  public void cancelFaultProcessing()
  {
    processFault = false;
  }
  /**
   * Cancels processing of the corresponding WSDL binding. 
   */
  public void cancelBindingProcessing()
  {
    processBinding = false;
  }
  /**
   * Cancels processing of the corresponding WSDL binding operation. 
   */
  public void cancelBindingOperationProcessing()
  {
    processBindingOperation = false;
  }
  /**
   * Cancels processing of the corresponding WSDL binding input. 
   */
  public void cancelBindingInputProcessing()
  {
    processBindingInput = false;
  }
  /**
   * Cancels processing of the corresponding WSDL binding output. 
   */
  public void cancelBindingOutputProcessing()
  {
    processBindingOutput = false;
  }
  /**
   * Cancels processing of the corresponding WSDL binding fault. 
   */
  public void cancelBindingFaultProcessing()
  {
    processBindingFault = false;
  }
  /**
   * Cancels processing of the corresponding WSDL import. 
   */
  public void cancelImportProcessing()
  {
    processImport = false;
  }
  /**
   * Cancels processing of the corresponding WSDL element. 
   */
  public void cancelElementProcessing()
  {
    processElement = false;
  }
  /**
   * Cancels processing of the corresponding WSDL message. 
   */
  public void cancelMessageProcessing()
  {
    processMessage = false;
  }
  /**
   * Cancels processing of the corresponding WSDL port. 
   */
  public void cancelPortProcessing()
  {
    processPort = false;
  }
  /**
   * Cancels processing of the corresponding WSDL port type. 
   */
  public void cancelPortTypeProcessing()
  {
    processPortType = false;
  }
  /**
   * Cancels processing of the corresponding WSDL definition. 
   */
  public void cancelDefinitionProcessing()
  {
    processDefinition = false;
  }
  /**
   * Cancels processing of the corresponding WSDL extensibility element. 
   */
  public void cancelExtensibilityElementProcessing()
  {
    processExtensibilityElement = false;
  }
  /**
   * Cancels processing of the corresponding WSDL SOAP binding. 
   */
  public void cancelSOAPBindingProcessing()
  {
    processSOAPBinding = false;
  }
  /**
   * Cancels processing of the corresponding WSDL SOAP header. 
   */
  public void cancelSOAPHeaderProcessing()
  {
    processSOAPHeader = false;
  }
  /**
   * Cancels processing of the corresponding WSDL SOAP header fault. 
   */
  public void cancelSOAPHeaderFaultProcessing()
  {
    processSOAPHeaderFault = false;
  }
  /**
   * Cancels processing of the corresponding WSDL SOAP fault. 
   */
  public void cancelSOAPFaultProcessing()
  {
    processSOAPFault = false;
  }
  /**
   * Cancels processing of the corresponding WSDL operation. 
   */
  public void cancelSOAPOperationProcessing()
  {
    processSOAPOperation = false;
  }

  /**
   * Cancels WSDL elements processing. 
   */
  public void cancelProcessing()
  {
    cancelPartProcessing();
    cancelServiceProcessing();
    cancelTypesProcessing();
    cancelOperationProcessing();
    cancelInputProcessing();
    cancelOutputProcessing();
    cancelFaultProcessing();
    cancelBindingProcessing();
    cancelBindingOperationProcessing();
    cancelBindingInputProcessing();
    cancelBindingOutputProcessing();
    cancelBindingFaultProcessing();
    cancelImportProcessing();
    cancelElementProcessing();
    cancelMessageProcessing();
    cancelPortProcessing();
    cancelPortTypeProcessing();
    cancelDefinitionProcessing();
    cancelExtensibilityElementProcessing();
    cancelSOAPBindingProcessing();
    cancelSOAPHeaderProcessing();
    cancelSOAPHeaderFaultProcessing();
    cancelSOAPFaultProcessing();
    cancelSOAPOperationProcessing();
  }

  /**
   * Resumes processing of the corresponding WSDL part. 
   */
  public void resumePartProcessing()
  {
    processPart = true;
  }
  /**
   * Resumes processing of the corresponding WSDL service. 
   */
  public void resumeServiceProcessing()
  {
    processService = true;
  }
  /**
   * Resumes processing of the corresponding WSDL types. 
   */
  public void resumeTypesProcessing()
  {
    processTypes = true;
  }
  /**
   * Resumes processing of the corresponding WSDL operation. 
   */
  public void resumeOperationProcessing()
  {
    processOperation = true;
  }
  /**
   * Resumes processing of the corresponding WSDL input. 
   */
  public void resumeInputProcessing()
  {
    processInput = true;
  }
  /**
   * Resumes processing of the corresponding WSDL output. 
   */
  public void resumeOutputProcessing()
  {
    processOutput = true;
  }
  /**
   * Resumes processing of the corresponding WSDL fault. 
   */
  public void resumeFaultProcessing()
  {
    processFault = true;
  }
  /**
   * Resumes processing of the corresponding WSDL binding. 
   */
  public void resumeBindingProcessing()
  {
    processBinding = true;
  }
  /**
   * Resumes processing of the corresponding WSDL binding operation. 
   */
  public void resumeBindingOperationProcessing()
  {
    processBindingOperation = true;
  }
  /**
   * Resumes processing of the corresponding WSDL binding input. 
   */
  public void resumeBindingInputProcessing()
  {
    processBindingInput = true;
  }
  /**
   * Resumes processing of the corresponding WSDL binding output. 
   */
  public void resumeBindingOutputProcessing()
  {
    processBindingOutput = true;
  }
  /**
   * Resumes processing of the corresponding WSDL binding fault. 
   */
  public void resumeBindingFaultProcessing()
  {
    processBindingFault = true;
  }
  /**
   * Resumes processing of the corresponding WSDL import. 
   */
  public void resumeImportProcessing()
  {
    processImport = true;
  }
  /**
   * Resumes processing of the corresponding WSDL element. 
   */
  public void resumeElementProcessing()
  {
    processElement = true;
  }
  /**
   * Resumes processing of the corresponding WSDL message. 
   */
  public void resumeMessageProcessing()
  {
    processMessage = true;
  }
  /**
   * Resumes processing of the corresponding WSDL port. 
   */
  public void resumePortProcessing()
  {
    processPort = true;
  }
  /**
   * Resumes processing of the corresponding WSDL port type. 
   */
  public void resumePortTypeProcessing()
  {
    processPortType = true;
  }
  /**
   * Resumes processing of the corresponding WSDL definition. 
   */
  public void resumeDefinitionProcessing()
  {
    processDefinition = true;
  }
  /**
   * Resumes processing of the corresponding WSDL extensibility element. 
   */
  public void resumeExtensibilityElementProcessing()
  {
    processExtensibilityElement = true;
  }
  /**
   * Resumes processing of the corresponding WSDL SOAP binding. 
   */
  public void resumeSOAPBindingProcessing()
  {
    processSOAPBinding = true;
  }
  /**
   * Resumes processing of the corresponding WSDL SOAP header. 
   */
  public void resumeSOAPHeaderProcessing()
  {
    processSOAPHeader = true;
  }
  /**
   * Resumes processing of the corresponding WSDL SOAP header fault. 
   */
  public void resumeSOAPHeaderFaultProcessing()
  {
    processSOAPHeaderFault = true;
  }
  /**
   * Resumes processing of the corresponding WSDL SOAP fault. 
   */
  public void resumeSOAPFaultProcessing()
  {
    processSOAPFault = true;
  }
  /**
   * Resumes processing of the corresponding WSDL SOAP operation. 
   */
  public void resumeSOAPOperationProcessing()
  {
    processSOAPOperation = true;
  }
  /**
   * Indicates whether the corresponding WSDL part should be processed.
   * @return true if the corresponding WSDL part should be processed.
   */
  public boolean processPart()
  {
    return processPart;
  }
  /**
   * Indicates whether the corresponding WSDL service should be processed.
   * @return true if the corresponding WSDL service should be processed.
   */
  public boolean processService()
  {
    return processService;
  }
  /**
   * Indicates whether the corresponding WSDL types should be processed.
   * @return true if the corresponding WSDL types should be processed.
   */
  public boolean processTypes()
  {
    return processTypes;
  }
  /**
   * Indicates whether the corresponding WSDL operation should be processed.
   * @return true if the corresponding WSDL operation should be processed.
   */
  public boolean processOperation()
  {
    return processOperation;
  }
  /**
   * Indicates whether the corresponding WSDL input should be processed.
   * @return true if the corresponding WSDL input should be processed.
   */
  public boolean processInput()
  {
    return processInput;
  }
  /**
   * Indicates whether the corresponding WSDL output should be processed.
   * @return true if the corresponding WSDL output should be processed.
   */
  public boolean processOutput()
  {
    return processOutput;
  }
  /**
   * Indicates whether the corresponding WSDL fault should be processed.
   * @return true if the corresponding WSDL fault should be processed.
   */
  public boolean processFault()
  {
    return processFault;
  }
  /**
   * Indicates whether the corresponding WSDL binding should be processed.
   * @return true if the corresponding WSDL binding should be processed.
   */
  public boolean processBinding()
  {
    return processBinding;
  }
  /**
   * Indicates whether the corresponding WSDL binding operation should be processed.
   * @return true if the corresponding WSDL binding operation should be processed.
   */
  public boolean processBindingOperation()
  {
    return processBindingOperation;
  }
  /**
   * Indicates whether the corresponding WSDL binding input should be processed.
   * @return true if the corresponding WSDL binding input should be processed.
   */
  public boolean processBindingInput()
  {
    return processBindingInput;
  }
  /**
   * Indicates whether the corresponding WSDL binding output should be processed.
   * @return true if the corresponding WSDL binding output should be processed.
   */
  public boolean processBindingOutput()
  {
    return processBindingOutput;
  }
  /**
   * Indicates whether the corresponding WSDL binding fault should be processed.
   * @return true if the corresponding WSDL binding fault should be processed.
   */
  public boolean processBindingFault()
  {
    return processBindingFault;
  }
  /**
   * Indicates whether the corresponding WSDL import should be processed.
   * @return true if the corresponding WSDL import should be processed.
   */
  public boolean processImport()
  {
    return processImport;
  }
  /**
   * Indicates whether the corresponding WSDL element should be processed.
   * @return true if the corresponding WSDL element should be processed.
   */
  public boolean processElement()
  {
    return processElement;
  }
  /**
   * Indicates whether the corresponding WSDL message should be processed.
   * @return true if the corresponding WSDL message should be processed.
   */
  public boolean processMessage()
  {
    return processMessage;
  }
  /**
   * Indicates whether the corresponding WSDL port should be processed.
   * @return true if the corresponding WSDL port should be processed.
   */
  public boolean processPort()
  {
    return processPort;
  }
  /**
   * Indicates whether the corresponding WSDL port type should be processed.
   * @return true if the corresponding WSDL port type should be processed.
   */
  public boolean processPortType()
  {
    return processPortType;
  }
  /**
   * Indicates whether the corresponding WSDL definition should be processed.
   * @return true if the corresponding WSDL definition should be processed.
   */
  public boolean processDefinition()
  {
    return processDefinition;
  }
  /**
   * Indicates whether the corresponding WSDL extensibility element should be processed.
   * @return true if the corresponding WSDL extensibility element should be processed.
   */
  public boolean processExtensibilityElement()
  {
    return processExtensibilityElement;
  }
  /**
   * Indicates whether the corresponding WSDL SOAP binding should be processed.
   * @return true if the corresponding WSDL SOAP binding should be processed.
   */
  public boolean processSOAPBinding()
  {
    return processSOAPBinding;
  }
  /**
   * Indicates whether the corresponding WSDL SOAP header should be processed.
   * @return true if the corresponding WSDL SOAP header should be processed.n
   */
  public boolean processSOAPHeader()
  {
    return processSOAPHeader;
  }
  /**
   * Indicates whether the corresponding WSDL SOAP header fault should be processed.
   * @return true if the corresponding WSDL SOA header fault should be processed.
   */
  public boolean processSOAPHeaderFault()
  {
    return processSOAPHeaderFault;
  }
  /**
   * Indicates whether the corresponding WSDL SOAP fault should be processed.
   * @return true if the corresponding WSDL SOAP fault should be processed.
   */
  public boolean processSOAPFault()
  {
    return processSOAPFault;
  }
  /**
   * Indicates whether the corresponding WSDL operation should be processed.
   * @return true if the corresponding WSDL operation should be processed.
   */
  public boolean processSOAPOperation()
  {
    return processSOAPOperation;
  }
  /**
   * Sets WSDL part to be processed.
   * @param value  the WSDL part to be processed.
   */
  void setPart(Part value)
  {
    activePart = value;
  }
  /**
   * Gets last processed WSDL part.
   * @return the last processed WSDL part.
   */
  public Part getPart()
  {
    return activePart;
  }
  /**
   * Sets WSDL service to be processed.
   * @param value  the WSDL service to be processed.
   */
  void setService(Service value)
  {
    activeService = value;
  }
  /**
   * Gets last processed WSDL service.
   * @return the last processed WSDL service.
   */
  public Service getService()
  {
    return activeService;
  }
  /**
   * Sets WSDL types to be processed.
   * @param value  the WSDL types to be processed.
   */
  void setTypes(Types value)
  {
    activeTypes = value;
  }
  /**
   * Gets last processed WSDL types.
   * @return the last processed WSDL types.
   */
  public Types getTypes()
  {
    return activeTypes;
  }
  /**
   * Sets WSDL operation to be processed.
   * @param value  the WSDL operation to be processed.
   */
  void setOperation(Operation value)
  {
    activeOperation = value;
  }
  /**
   * Gets last processed WSDL operation.
   * @return the last processed WSDL operation.
   */
  public Operation getOperation()
  {
    return activeOperation;
  }
  /**
   * Sets WSDL input to be processed
   * @param the WSDL input to be processed.
   */
  void setInput(Input value)
  {
    activeInput = value;
  }
  /**
   * Gets last processed WSDL input.
   * @return last processed WSDL input.
   */
  public Input getInput()
  {
    return activeInput;
  }
  /**
   * Sets WSDL output to be processed.
   * @param value  the WSDL output to be processed.
   */
  void setOutput(Output value)
  {
    activeOutput = value;
  }
  /**
   * Gets last processed WSDL output.
   * @return the last processed WSDL output.
   */
  public Output getOutput()
  {
    return activeOutput;
  }
  /**
   * Sets WSDL fault to be processed.
   * @param value  the WSDL fault to be processed.
   */
  void setFault(Fault value)
  {
    activeFault = value;
  }
  /**
   * Gets last processed WSDL fault.
   * @return the last processed WSDL fault.
   */
  public Fault getFault()
  {
    return activeFault;
  }
  /**
   * Sets WSDL binding to be processed.
   * @param value  the WSDL binding to be processed.
   */
  void setBinding(Binding value)
  {
    activeBinding = value;
  }
  /**
   * Gets last processed WSDL binding.
   * @return the last processed WSDL binding.
   */
  public Binding getBinding()
  {
    return activeBinding;
  }
  /**
   * Sets WSDL binding operation to be processed.
   * @param value  the SDL binding operation to be processed.
   */
  void setBindingOperation(BindingOperation value)
  {
    activeBindingOperation = value;
  }
  /**
   * Gets last processed WSDL binding operation.
   * @return the last processed WSDL binding operation.
   */
  public BindingOperation getBindingOperation()
  {
    return activeBindingOperation;
  }
  /**
   * Sets WSDL binding input to be processed.
   * @param value  the WSDL binding input to be processed.
   */
  void setBindingInput(BindingInput value)
  {
    activeBindingInput = value;
  }
  /**
   * Gets last processed WSDL binding input.
   * @return the last processed WSDL binding input.
   */
  public BindingInput getBindingInput()
  {
    return activeBindingInput;
  }
  /**
   * Sets WSDL binding output to be processed.
   * @param value  the WSDL binding output to be processed.
   */
  void setBindingOutput(BindingOutput value)
  {
    activeBindingOutput = value;
  }
  /**
   * Gets last processed WSDL binding output.
   * @return the last processed WSDL binding output.
   */
  public BindingOutput getBindingOutput()
  {
    return activeBindingOutput;
  }
  /**
   * Sets WSDL binding fault to be processed.
   * @param value  the WSDL binding fault to be processed.
   */
  void setBindingFault(BindingFault value)
  {
    activeBindingFault = value;
  }
  /**
   * Gets last processed WSDL binding fault.
   * @return the last processed WSDL binding fault.
   */
  public BindingFault getBindingFault()
  {
    return activeBindingFault;
  }
  /**
   * Sets WSDL import to be processed.
   * @param value  the WSDL import to be processed.
   */
  void setImport(Import value)
  {
    activeImport = value;
  }
  /**
   * Gets last processed WSDL import.
   * @return the last processed WSDL import.
   */
  public Import getImport()
  {
    return activeImport;
  }
  /**
   * Sets WSDL element to be processed.
   * @param value  the WSDL element to be processed.
   */
  void setElement(Element value)
  {
    activeElement = value;
  }
  /**
   * Gets last processed WSDL element.
   * @return the last processed WSDL element.
   */
  public Element getElement()
  {
    return activeElement;
  }
  /**
   * Sets WSDL message to be processed.
   * @param value  the WSDL message to be processed.
   */
  void setMessage(Message value)
  {
    activeMessage = value;
  }
  /**
   * Gets last processed WSDL message.
   * @return the last processed WSDL message.
   */
  public Message getMessage()
  {
    return activeMessage;
  }
  /**
   * Sets WSDL port to be processed.
   * @param value  the WSDL port to be processed.
   */
  void setPort(Port value)
  {
    activePort = value;
  }
  /**
   * Gets last processed WSDL port.
   * @return the last processed WSDL port.
   */
  public Port getPort()
  {
    return activePort;
  }
  /**
   * Sets WSDL port type to be processed.
   * @param value  the WSDL port type to be processed.
   */
  void setPortType(PortType value)
  {
    activePortType = value;
  }
  /**
   * Gets last processed WSDL port type.
   * @return the last processed WSDL port type.
   */
  public PortType getPortType()
  {
    return activePortType;
  }
  /**
   * Sets WSDL definition to be processed.
   * @param value  the WSDL definition to be processed.
   */
  void setDefinition(Definition value)
  {
    activeDefinition = value;
  }
  /**
   * Gets last processed WSDL definition.
   * @return the last processed WSDL definition.
   */
  public Definition getDefinition()
  {
    return activeDefinition;
  }
  /**
   * Sets WSDL extensibility element to be processed.
   * @param value  the WSDL extensibility element to be processed.
   */
  void setExtensibilityElement(ExtensibilityElement value)
  {
    activeExtensibilityElement = value;
  }
  /**
   * Gets last processed WSDL extensibility element.
   * @return the last processed WSDL extensibility element.
   */
  public ExtensibilityElement getExtensibilityElement()
  {
    return activeExtensibilityElement;
  }
  /**
   * Sets WSDL element to be processed.
   * @param value  the WSDL element to be processed.
   */
  void setSOAPBinding(SOAPBinding value)
  {
    activeSOAPBinding = value;
  }
  /**
   * Gets last processed WSDL SOAP binding.
   * @return the last processed WSDL SOAP binding.
   */
  public SOAPBinding getSOAPBinding()
  {
    return activeSOAPBinding;
  }
  /**
   * Sets WSDL element to be processed.
   * @param value  WSDL element to be processed.
   */
  void setSOAPBody(SOAPBody value)
  {
    activeSOAPBody = value;
  }
  /**
   * Gets last processed WSDL SOAP body.
   * @return the last processed WSDL SOAP body.
   */
  public SOAPBody getSOAPBody()
  {
    return activeSOAPBody;
  }
  /**
   * Sets WSDL element to be processed.
   * @param value  the WSDL element to be processed.
   */
  void setSOAPHeader(SOAPHeader value)
  {
    activeSOAPHeader = value;
  }
  /**
   * Gets last processed WSDL SOAP header.
   * @return the last processed WSDL SOAP header.
   */
  public SOAPHeader getSOAPHeader()
  {
    return activeSOAPHeader;
  }
  /**
   * Sets WSDL element to be processed.
   * @param value  the WSDL element to be processed.
   */
  void setSOAPHeaderFault(SOAPHeaderFault value)
  {
    activeSOAPHeaderFault = value;
  }
  /**
   * Gets last processed WSDL SOAP header fault.
   * @return the last processed WSDL SOAP header fault.
   */
  public SOAPHeaderFault getSOAPHeaderFault()
  {
    return activeSOAPHeaderFault;
  }
  /**
   * Sets WSDL element to be processed.
   * @param value  the WSDL element to be processed.
   */
  void setSOAPFault(SOAPFault value)
  {
    activeSOAPFault = value;
  }
  /**
   * Gets last processed WSDL SOAP fault.
   * @return the last processed WSDL SOAP fault.
   */
  public SOAPFault getSOAPFault()
  {
    return activeSOAPFault;
  }
  /**
   * Sets WSDL element to be processed.
   * @param value  theWSDL element to be processed
   */
  void setSOAPOperation(SOAPOperation value)
  {
    activeSOAPOperation = value;
  }
  /**
   * Gets last processed WSDL SOAP operation.
   * @return the last processed WSDL SOAP operation.
   */
  public SOAPOperation getSOAPOperation()
  {
    return activeSOAPOperation;
  }
}
