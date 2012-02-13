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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
 * The class implements plain traverse over WSDL artifacts.
 * WSDLTraversal is context-driven traversal. 
 * 
 * @author Andrey Kulik
 */
public class WSDLTraversal
{

  /**
   * Current WSDL visitor (callback).
   */
  private WSDLVisitor visitor;

  /**
   * Flags which specify whether traversal will travers from one WSDL artifact
   * to another.
   */
  private boolean traverseBinding2BindingOperation = true;
  private boolean traverseBinding2Element = true;
  private boolean traverseBinding2ExtensibilityElement = true;
  private boolean traverseBinding2PortType = true; // reference
  private boolean traverseBindingFault2Element = true;
  private boolean traverseBindingFault2ExtensibilityElement = true;
  private boolean traverseBindingInput2Element = true;
  private boolean traverseBindingInput2ExtensibilityElement = true;
  private boolean traverseBindingOperation2BindingFault = true;
  private boolean traverseBindingOperation2BindingInput = true;
  private boolean traverseBindingOperation2BindingOutput = true;
  private boolean traverseBindingOperation2Element = true;
  private boolean traverseBindingOperation2ExtensibilityElement = true;
  private boolean traverseBindingOperation2Operation = true; // reference  
  private boolean traverseBindingOutput2Element = true;
  private boolean traverseBindingOutput2ExtensibilityElement = true;
  private boolean traverseDefinition2Binding = true;
  private boolean traverseDefinition2Element = true;
  private boolean traverseDefinition2ExtensibilityElement = true;
  private boolean traverseDefinition2Import = true;
  private boolean traverseDefinition2Message = true;
  private boolean traverseDefinition2PortType = true;
  private boolean traverseDefinition2Service = true;
  private boolean traverseDefinition2Types = true;
  private boolean traverseFault2Element = true;
  private boolean traverseFault2Message = true; // reference 
  private boolean traverseImport2Definition = true;
  private boolean traverseImport2Element = true;
  private boolean traverseInput2Element = true;
  private boolean traverseInput2Message = true; // reference 
  private boolean traverseMessage2Element = true;
  private boolean traverseMessage2Part = true;
  private boolean traverseOperation2Element = true;
  private boolean traverseOperation2Fault = true;
  private boolean traverseOperation2Input = true;
  private boolean traverseOperation2Output = true;
  private boolean traverseOutput2Element = true;
  private boolean traverseOutput2Message = true; // reference 
  private boolean traversePart2Element = true;
  private boolean traversePort2Binding = true; // reference 
  private boolean traversePort2Element = true;
  private boolean traversePort2ExtensibilityElement = true;
  private boolean traversePortType2Element = true;
  private boolean traversePortType2Operation = true;
  private boolean traverseService2Element = true;
  private boolean traverseService2ExtensibilityElement = true;
  private boolean traverseService2Port = true;
  private boolean traverseTypes2Element = true;
  private boolean traverseTypes2ExtensibilityElement = true;
  //private boolean traverseSOAPBody2Part = true;
  private boolean traverseSOAPHeader2SOAPHeaderFault = true;
  private boolean traverseBinding2SOAPBinding = true;
  private boolean traverseBindingOperation2SOAPOperation = true;
  private boolean traverseBindingInput2SOAPHeader = true;
  private boolean traverseBindingInput2SOAPBody = true;
  private boolean traverseBindingOutput2SOAPHeader = true;
  private boolean traverseBindingOutput2SOAPBody = true;
  private boolean traverseBindingFault2SOAPFault = true;

  /**
   * Flags which specify whether traversal will visit corresponding WSDL
   * artifact.
   */
  private boolean visitBinding = false;
  private boolean visitBindingFault = false;
  private boolean visitBindingInput = false;
  private boolean visitBindingOperation = false;
  private boolean visitBindingOutput = false;
  private boolean visitDefinition = false;
  private boolean visitElement = false;
  private boolean visitExtensibilityElement = false;
  private boolean visitFault = false;
  private boolean visitImport = false;
  private boolean visitInput = false;
  private boolean visitMessage = false;
  private boolean visitOperation = false;
  private boolean visitOutput = false;
  private boolean visitPart = false;
  private boolean visitPort = false;
  private boolean visitPortType = false;
  private boolean visitService = false;
  private boolean visitTypes = false;
  private boolean visitSOAPBinding = false;
  private boolean visitSOAPBody = false;
  private boolean visitSOAPFault = false;
  private boolean visitSOAPHeader = false;
  private boolean visitSOAPHeaderFault = false;
  private boolean visitSOAPOperation = false;

  List alreadyTraversedDefinitions;
  /**
   * Default constructor.
   * @see java.lang.Object#Object()
   */
  public WSDLTraversal()
  {
	alreadyTraversedDefinitions = new ArrayList();
  }

  /**
   * The method specifies that traversal will ignore indirect references between
   * WSDL artifacts. For example:
   * <ol>
   *   <li>binding to port type</li>
   *   <li>binding operation to operation</li>
   *   <li>fault to message</li>
   *   <li>input to message</li>
   *   <li>output to message</li>
   *   <li>port to binding</li>
   * </ol>
   */
  public void ignoreReferences()
  {
    ignoreBinding2PortType();
    ignoreBindingOperation2Operation();
    ignoreFault2Message();
    ignoreInput2Message();
    ignoreOutput2Message();
    ignorePort2Binding();
    //ignoreSOAPBody2Part();
  }

  /**
   * The method sets visitor.
   * @param visitor  a WSDL visitor.
   */
  public void setVisitor(WSDLVisitor visitor)
  {
    this.visitor = visitor;
  }

  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return the traversal.
   */
  public WSDLTraversal ignoreBinding2SOAPBinding()
  {
    traverseBinding2SOAPBinding = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOperation2SOAPOperation()
  {
    traverseBindingOperation2SOAPOperation = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingInput2SOAPHeader()
  {
    traverseBindingInput2SOAPHeader = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingInput2SOAPBody()
  {
    traverseBindingInput2SOAPBody = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOutput2SOAPHeader()
  {
    traverseBindingOutput2SOAPHeader = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOutput2SOAPBody()
  {
    traverseBindingOutput2SOAPBody = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingFault2SOAPFault()
  {
    traverseBindingFault2SOAPFault = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreSOAPHeader2SOAPHeaderFault()
  {
    traverseSOAPHeader2SOAPHeaderFault = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBinding2BindingOperation()
  {
    traverseBinding2BindingOperation = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBinding2Element()
  {
    traverseBinding2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBinding2ExtensibilityElement()
  {
    traverseBinding2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBinding2PortType()
  {
    traverseBinding2PortType = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingFault2Element()
  {
    traverseBindingFault2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingFault2ExtensibilityElement()
  {
    traverseBindingFault2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingInput2Element()
  {
    traverseBindingInput2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingInput2ExtensibilityElement()
  {
    traverseBindingInput2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOperation2BindingFault()
  {
    traverseBindingOperation2BindingFault = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOperation2BindingInput()
  {
    traverseBindingOperation2BindingInput = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOperation2BindingOutput()
  {
    traverseBindingOperation2BindingOutput = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOperation2Element()
  {
    traverseBindingOperation2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOperation2ExtensibilityElement()
  {
    traverseBindingOperation2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOperation2Operation()
  {
    traverseBindingOperation2Operation = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOutput2Element()
  {
    traverseBindingOutput2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOutput2ExtensibilityElement()
  {
    traverseBindingOutput2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition2Binding()
  {
    traverseDefinition2Binding = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition2Element()
  {
    traverseDefinition2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition2ExtensibilityElement()
  {
    traverseDefinition2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition2Import()
  {
    traverseDefinition2Import = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition2Message()
  {
    traverseDefinition2Message = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition2PortType()
  {
    traverseDefinition2PortType = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition2Service()
  {
    traverseDefinition2Service = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition2Types()
  {
    traverseDefinition2Types = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreFault2Element()
  {
    traverseFault2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreFault2Message()
  {
    traverseFault2Message = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreImport2Definition()
  {
    traverseImport2Definition = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreImport2Element()
  {
    traverseImport2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreInput2Element()
  {
    traverseInput2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreInput2Message()
  {
    traverseInput2Message = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreMessage2Element()
  {
    traverseMessage2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreMessage2Part()
  {
    traverseMessage2Part = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreOperation2Element()
  {
    traverseOperation2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreOperation2Fault()
  {
    traverseOperation2Fault = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreOperation2Input()
  {
    traverseOperation2Input = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreOperation2Output()
  {
    traverseOperation2Output = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreOutput2Element()
  {
    traverseOutput2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreOutput2Message()
  {
    traverseOutput2Message = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePart2Element()
  {
    traversePart2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePort2Binding()
  {
    traversePort2Binding = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePort2Element()
  {
    traversePort2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePort2ExtensibilityElement()
  {
    traversePort2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePortType2Element()
  {
    traversePortType2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePortType2Operation()
  {
    traversePortType2Operation = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreService2Element()
  {
    traverseService2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreService2ExtensibilityElement()
  {
    traverseService2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreService2Port()
  {
    traverseService2Port = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreTypes2Element()
  {
    traverseTypes2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore reference between WSDL elements...
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreTypes2ExtensibilityElement()
  {
    traverseTypes2ExtensibilityElement = false;
    return this;
  }

  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitSOAPBinding(boolean value)
  {
    visitSOAPBinding = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitSOAPBody(boolean value)
  {
    visitSOAPBody = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitSOAPFault(boolean value)
  {
    visitSOAPFault = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitSOAPHeader(boolean value)
  {
    visitSOAPHeader = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitSOAPHeaderFault(boolean value)
  {
    visitSOAPHeaderFault = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitSOAPOperation(boolean value)
  {
    visitSOAPOperation = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitBinding(boolean value)
  {
    visitBinding = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitBindingFault(boolean value)
  {
    visitBindingFault = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitBindingInput(boolean value)
  {
    visitBindingInput = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitBindingOperation(boolean value)
  {
    visitBindingOperation = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitBindingOutput(boolean value)
  {
    visitBindingOutput = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitDefinition(boolean value)
  {
    visitDefinition = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitElement(boolean value)
  {
    visitElement = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitExtensibilityElement(boolean value)
  {
    visitExtensibilityElement = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitFault(boolean value)
  {
    visitFault = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitImport(boolean value)
  {
    visitImport = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitInput(boolean value)
  {
    visitInput = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitMessage(boolean value)
  {
    visitMessage = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitOperation(boolean value)
  {
    visitOperation = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitOutput(boolean value)
  {
    visitOutput = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitPart(boolean value)
  {
    visitPart = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitPort(boolean value)
  {
    visitPort = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitPortType(boolean value)
  {
    visitPortType = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitService(boolean value)
  {
    visitService = value;
    return this;
  }
  /**
   * Instructs traversal to visit or not the corresponding WSDL element.
   * @param value true if the WSDL element should be visited, otherwise - false.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal visitTypes(boolean value)
  {
    visitTypes = value;
    return this;
  }

  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBinding()
  {
    traverseDefinition2Binding = false;
    traversePort2Binding = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingFault()
  {
    traverseBindingOperation2BindingFault = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingInput()
  {
    traverseBindingOperation2BindingInput = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOperation()
  {
    traverseBinding2BindingOperation = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreBindingOutput()
  {
    traverseBindingOperation2BindingOutput = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreDefinition()
  {
    traverseImport2Definition = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreElement()
  {
    traverseBinding2Element = false;
    traverseBindingFault2Element = false;
    traverseBindingInput2Element = false;
    traverseBindingOperation2Element = false;
    traverseBindingOutput2Element = false;
    traverseDefinition2Element = false;
    traversePort2Element = false;
    traverseService2Element = false;
    traverseFault2Element = false;
    traverseOutput2Element = false;
    traverseImport2Element = false;
    traverseInput2Element = false;
    traverseOperation2Element = false;
    traverseMessage2Element = false;
    traversePart2Element = false;
    traversePortType2Element = false;
    traverseTypes2Element = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreExtensibilityElement()
  {
    traverseService2ExtensibilityElement = false;
    traversePort2ExtensibilityElement = false;
    traverseDefinition2ExtensibilityElement = false;
    traverseBindingOutput2ExtensibilityElement = false;
    traverseBinding2ExtensibilityElement = false;
    traverseBindingFault2ExtensibilityElement = false;
    traverseBindingInput2ExtensibilityElement = false;
    traverseBindingOperation2ExtensibilityElement = false;
    traverseTypes2ExtensibilityElement = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreSOAPBinding()
  {
    traverseBinding2SOAPBinding = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreSOAPOperation()
  {
    traverseBindingOperation2SOAPOperation = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreSOAPBody()
  {
    traverseBindingInput2SOAPBody = false;
    traverseBindingOutput2SOAPBody = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreSOAPHeader()
  {
    traverseBindingInput2SOAPHeader = false;
    traverseBindingOutput2SOAPHeader = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreSOAPHeaderFault()
  {
    traverseSOAPHeader2SOAPHeaderFault = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreSOAPFault()
  {
    traverseBindingFault2SOAPFault = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreFault()
  {
    traverseOperation2Fault = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreImport()
  {
    traverseImport2Definition = false; // fix
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreInput()
  {
    traverseOperation2Input = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreMessage()
  {
    traverseFault2Message = false;
    traverseDefinition2Message = false;
    traverseInput2Message = false;
    traverseOutput2Message = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreOperation()
  {
    traverseBindingOperation2Operation = false;
    traversePortType2Operation = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreOutput()
  {
    traverseOperation2Output = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePart()
  {
    traverseMessage2Part = false;
    //traverseSOAPBody2Part = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePort()
  {
    traverseService2Port = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignorePortType()
  {
    traverseBinding2PortType = false;
    traverseDefinition2PortType = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreService()
  {
    traverseDefinition2Service = false;
    return this;
  }
  /**
   * Instructs traversal to ignore all references to the corresponding WSDL
   * element.
   * @return a WSDLTraversal object.
   */
  public WSDLTraversal ignoreTypes()
  {
    traverseDefinition2Types = false;
    return this;
  }

  /**
   * The method adjusts traveseXXX variables according to the visitXXX variables
   */
  private void adjust()
  {
    adjustPart();
    adjustService();
    adjustTypes();
    adjustOperation();
    adjustInput();
    adjustOutput();
    adjustFault();
    adjustBinding();
    adjustBindingOperation();
    adjustBindingInput();
    adjustBindingOutput();
    adjustBindingFault();
    adjustImport();
    adjustElement();
    adjustMessage();
    adjustPort();
    adjustPortType();
    adjustDefinition();
    adjustExtensibilityElement();
    adjustSOAPBinding();
    adjustSOAPBody();
    adjustSOAPHeader();
    adjustSOAPHeaderFault();
    adjustSOAPFault();
    adjustSOAPOperation();
  }

  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustExtensibilityElement()
  {
    boolean value = visitExtensibilityElement;
    if (!value)
      ignoreExtensibilityElement();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustPart()
  {
    boolean value = visitPart || (traversePart2Element && adjustElement());
    if (!value)
      ignorePart();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustSOAPHeader()
  {
    boolean value =
      visitSOAPHeader
        || (traverseSOAPHeader2SOAPHeaderFault && adjustSOAPHeaderFault());
    if (!value)
      ignoreSOAPHeader();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustBindingOutput()
  {
    boolean value =
      visitBindingOutput
        || (traverseBindingOutput2Element && adjustElement())
        || (traverseBindingOutput2ExtensibilityElement
          && adjustExtensibilityElement())
        || (traverseBindingOutput2SOAPBody
          && adjustSOAPBody()
          || (traverseBindingOutput2SOAPHeader && adjustSOAPHeader()));
    if (!value)
      ignoreBindingOutput();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustElement()
  {
    boolean value = visitElement;
    if (!value)
      ignoreElement();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustTypes()
  {
    boolean value =
      visitTypes
        || (traverseTypes2Element && adjustElement())
        || (traverseTypes2ExtensibilityElement && adjustExtensibilityElement());
    if (!value)
      ignoreTypes();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustSOAPBinding()
  {
    boolean value = visitSOAPBinding;
    if (!value)
      ignoreSOAPBinding();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustPort()
  {
    boolean value =
      visitPort
        || (traversePort2Element && adjustElement())
        || (traversePort2ExtensibilityElement && adjustExtensibilityElement())
        || (traversePort2Binding && adjustBinding());
    if (!value)
      ignorePort();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustOperation()
  {
    boolean value =
      visitOperation
        || (traverseOperation2Element && adjustElement())
        || (traverseOperation2Input && adjustInput())
        || (traverseOperation2Output && adjustOutput())
        || (traverseOperation2Fault && adjustFault());
    if (!value)
      ignoreOperation();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustBindingOperation()
  {
    boolean value =
      visitBindingOperation
        || (traverseBindingOperation2Element && adjustElement())
        || (traverseBindingOperation2BindingFault && adjustBindingFault())
        || (traverseBindingOperation2BindingOutput && adjustBindingOutput())
        || (traverseBindingOperation2Operation && adjustOperation())
        || (traverseBindingOperation2BindingInput && adjustBindingInput())
        || (traverseBindingOperation2ExtensibilityElement
          && adjustExtensibilityElement())
        || (traverseBindingOperation2SOAPOperation && adjustSOAPOperation());
    if (!value)
      ignoreBindingOperation();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustInput()
  {
    boolean value =
      visitInput
        || (traverseInput2Element && adjustElement())
        || (traverseInput2Message && adjustMessage());
    if (!value)
      ignoreInput();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustBinding()
  {
    boolean value =
      visitBinding
        || (traverseBinding2Element && adjustElement())
        || (traverseBinding2BindingOperation && adjustBindingOperation())
        || (traverseBinding2ExtensibilityElement && adjustExtensibilityElement())
        || (traverseBinding2PortType && adjustPortType())
        || (traverseBinding2SOAPBinding && adjustSOAPBinding());
    if (!value)
      ignoreBinding();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustSOAPBody()
  {
    boolean value = visitSOAPBody;
    /* || 
    		(traverseSOAPBody2Part && adjustPart());*/
    if (!value)
      ignoreSOAPBody();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustSOAPHeaderFault()
  {
    boolean value = visitSOAPHeaderFault;
    if (!value)
      ignoreSOAPHeaderFault();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustBindingInput()
  {
    boolean value =
      visitBindingInput
        || (traverseBindingInput2Element && adjustElement())
        || (traverseBindingInput2ExtensibilityElement
          && adjustExtensibilityElement())
        || (traverseBindingInput2SOAPBody
          && adjustSOAPBody()
          || (traverseBindingInput2SOAPHeader && adjustSOAPHeader()));
    if (!value)
      ignoreBindingInput();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustOutput()
  {
    boolean value =
      visitOutput
        || (traverseOutput2Element && adjustElement())
        || (traverseOutput2Message && adjustMessage());
    if (!value)
      ignoreOutput();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustBindingFault()
  {
    boolean value =
      visitBindingFault
        || (traverseBindingFault2Element && adjustElement())
        || (traverseBindingFault2ExtensibilityElement
          && adjustExtensibilityElement())
        || (traverseBindingFault2SOAPFault && adjustSOAPFault());
    if (!value)
      ignoreBindingFault();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustSOAPOperation()
  {
    boolean value = visitSOAPOperation;
    if (!value)
      ignoreSOAPOperation();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustDefinition()
  {
    boolean value =
      visitDefinition
        || (traverseDefinition2Element && adjustElement())
        || (traverseDefinition2Import && adjustImport())
        || (traverseDefinition2Types && adjustTypes())
        || (traverseDefinition2Message && adjustMessage())
        || (traverseDefinition2ExtensibilityElement
          && adjustExtensibilityElement())
        || (traverseDefinition2PortType && adjustPortType())
        || (traverseDefinition2Binding && adjustBinding())
        || (traverseDefinition2Service && adjustService());
    if (!value)
      ignoreDefinition();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustSOAPFault()
  {
    boolean value = visitSOAPFault;
    if (!value)
      ignoreSOAPFault();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustImport()
  {
    boolean value = visitImport || (traverseImport2Definition
    /* && adjustDefinition()*/
      )
    || // avoid cycling problem. Thus, you should call ignoreImport manually  
   (traverseImport2Element && adjustElement());
    if (!value)
      ignoreImport();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustFault()
  {
    boolean value =
      visitFault
        || (traverseFault2Element && adjustElement())
        || (traverseFault2Message && adjustMessage());
    if (!value)
      ignoreFault();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustPortType()
  {
    boolean value =
      visitPortType
        || (traversePortType2Element && adjustElement())
        || (traversePortType2Operation && adjustOperation());
    if (!value)
      ignorePortType();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustService()
  {
    boolean value =
      visitService
        || (traverseService2Element && adjustElement())
        || (traverseService2ExtensibilityElement && adjustExtensibilityElement())
        || (traverseService2Port && adjustPort());
    if (!value)
      ignoreService();
    return value;
  }
  /**
   * The method adjusts traversal ignore flags according to the
   * corresponding visit flags.
   * @return boolean 
   */
  private boolean adjustMessage()
  {
    boolean value =
      visitMessage
        || (traverseMessage2Element && adjustElement())
        || (traverseMessage2Part && adjustPart());
    if (!value)
      ignoreMessage();
    return value;
  }

  /**
   * The method traverses given WSDL extensibility element according to the 
   * settings in the traversal context.
   * 
   * @param objExtensibilityElement  a WSDL extensibility element artifact.
   * @param parent                   parent of the WSDL extensibility element artifact.
   * @param ctx                      the traversal contex.
   */
  private void traverse(
    ExtensibilityElement objExtensibilityElement,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeExtensibilityElementProcessing();
    ctx.setExtensibilityElement(objExtensibilityElement);
    if (visitExtensibilityElement)
      visitor.visit(objExtensibilityElement, parent, ctx);
  }

  /**
   * The method traverses given WSDL part artifact according to the settings in
   * the traversal context.
   * 
   * @param objPart  the WSDL part artifact.
   * @param parent   parent of the WSDL part artifact.
   * @param ctx      the traversal contex.
   */
  private void traverse(Part objPart, Object parent, WSDLTraversalContext ctx)
  {
    ctx.resumePartProcessing();
    ctx.setPart(objPart);
    if (visitPart)
    {
      visitor.visit(objPart, parent, ctx);
      if (!ctx.processPart())
        return;
    }
    if (objPart == null)
      return;
    if (traversePart2Element)
      traverse(objPart.getDocumentationElement(), objPart, ctx);
  }

  /**
   * The method traverses given WSDL SOAP header artifact according to the 
   * settings in the traversal context.
   * 
   * @param objSOAPHeader  a WSDL SOAP header artifact.
   * @param parent         parent of the WSDL SOAP header artifact.
   * @param ctx            the traversal contex.
   */
  private void traverse(
    SOAPHeader objSOAPHeader,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeSOAPHeaderProcessing();
    ctx.setSOAPHeader(objSOAPHeader);
    if (visitSOAPHeader)
    {
      visitor.visit(objSOAPHeader, parent, ctx);
      if (!ctx.processSOAPHeader())
        return;
    }
    if (objSOAPHeader == null)
      return;
    if (traverseSOAPHeader2SOAPHeaderFault
      && objSOAPHeader.getSOAPHeaderFaults() != null)
    {
      Iterator it = objSOAPHeader.getSOAPHeaderFaults().iterator();
      while (it.hasNext())
      {
        traverse((SOAPHeaderFault) it.next(), objSOAPHeader, ctx);
        if (!ctx.processSOAPHeader())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL element artifact according to the settings in
   * the traversal context.
   * 
   * @param objElement  a WSDL element artifact.
   * @param parent   parent of the WSDL element artifact.
   * @param ctx      the traversal contex.
   */
  private void traverse(
    Element objElement,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.setElement(objElement);
    if (visitElement)
      visitor.visit(objElement, parent, ctx);
  }

  /**
   * The method traverses given WSDL types artifact according to the settings in
   * the traversal context.
   * 
   * @param objTypes  a WSDL types artifact.
   * @param parent    parent of the WSDL types artifact.
   * @param ctx       the traversal contex.
   */
  private void traverse(
    Types objTypes,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeTypesProcessing();
    ctx.setTypes(objTypes);
    if (visitTypes)
    {
      visitor.visit(objTypes, parent, ctx);
      if (!ctx.processTypes())
        return;
    }
    if (objTypes == null)
      return;
    if (traverseTypes2Element)
    {
      traverse(objTypes.getDocumentationElement(), objTypes, ctx);
      if (!ctx.processTypes())
        return;
    }
    if (traverseTypes2ExtensibilityElement
      && objTypes.getExtensibilityElements() != null)
    {
      Iterator it = objTypes.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objTypes, ctx);
        if (!ctx.processTypes())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL SOAP binding artifact according to the 
   * settings in the traversal context.
   * 
   * @param objSOAPBinding  a WSDL SOAP binding artifact.
   * @param parent          parent of the WSDL SOAP binding artifact.
   * @param ctx             the traversal contex.
   */
  private void traverse(
    SOAPBinding objSOAPBinding,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.setSOAPBinding(objSOAPBinding);
    if (visitSOAPBinding)
      visitor.visit(objSOAPBinding, parent, ctx);
  }

  /**
   * The method traverses given WSDL definition artifact according to the 
   * settings in the traversal context.
   * 
   * @param objDefinition  a WSDL definition artifact.
   * @param parent         parent of the WSDL definition artifact.
   * @param ctx            the traversal contex.
   */
  private void traverse(
    Definition objDefinition,
    Object parent,
    WSDLTraversalContext ctx)
  {
	if ((objDefinition == null) || (this.alreadyTraversedDefinitions.contains(objDefinition)))
	{
	  return;	
	}
	this.alreadyTraversedDefinitions.add(objDefinition);
    ctx.resumeDefinitionProcessing();
    ctx.setDefinition(objDefinition);

    if (visitDefinition)
    {
      visitor.visit(objDefinition, parent, ctx);
      if (!ctx.processDefinition())
        return;
    }
    if (traverseDefinition2Import && objDefinition.getImports() != null)
    {
      Iterator it = objDefinition.getImports().values().iterator();
      while (it.hasNext())
      {
        Vector v = (Vector) it.next();
        if (v != null)
        {
          Iterator it2 = v.iterator();
          while (it2.hasNext())
          {
            traverse((Import) it2.next(), objDefinition, ctx);
            if (!ctx.processDefinition())
              return;
          }
        }
      }
    }
    if (traverseDefinition2Element)
    {
      traverse(objDefinition.getDocumentationElement(), objDefinition, ctx);
      if (!ctx.processDefinition())
        return;
    }
    if (traverseDefinition2Types)
    {
      traverse(objDefinition.getTypes(), objDefinition, ctx);
      if (!ctx.processDefinition())
        return;
    }
    if (traverseDefinition2Message && objDefinition.getMessages() != null)
    {
      Iterator it = objDefinition.getMessages().values().iterator();
      while (it.hasNext())
      {
        traverse((Message) it.next(), objDefinition, ctx);
        if (!ctx.processDefinition())
          return;
      }
    }
    if (traverseDefinition2PortType && objDefinition.getPortTypes() != null)
    {
      Iterator it = objDefinition.getPortTypes().values().iterator();
      while (it.hasNext())
      {
        traverse((PortType) it.next(), objDefinition, ctx);
        if (!ctx.processDefinition())
          return;
      }
    }
    if (traverseDefinition2Binding && objDefinition.getBindings() != null)
    {
      Iterator it = objDefinition.getBindings().values().iterator();
      while (it.hasNext())
      {
        traverse((Binding) it.next(), objDefinition, ctx);
        if (!ctx.processDefinition())
          return;
      }
    }
    if (traverseDefinition2Service && objDefinition.getServices() != null)
    {
      Iterator it = objDefinition.getServices().values().iterator();
      while (it.hasNext())
      {
        traverse((Service) it.next(), objDefinition, ctx);
        if (!ctx.processDefinition())
          return;
      }
    }
    if (traverseDefinition2ExtensibilityElement
      && objDefinition.getExtensibilityElements() != null)
    {
      Iterator it = objDefinition.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objDefinition, ctx);
        if (!ctx.processDefinition())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL port artifact according to the settings in
   * the traversal context.
   * 
   * @param objPort  a WSDL port artifact.
   * @param parent   parent of the WSDL port artifact.
   * @param ctx      the traversal contex.
   */
  private void traverse(Port objPort, Object parent, WSDLTraversalContext ctx)
  {
    ctx.resumePortProcessing();
    ctx.setPort(objPort);
    if (visitPort)
    {
      visitor.visit(objPort, parent, ctx);
      if (!ctx.processPort())
        return;
    }
    if (objPort == null)
      return;
    if (traversePort2Element)
    {
      traverse(objPort.getDocumentationElement(), objPort, ctx);
      if (!ctx.processPort())
        return;
    }
    if (traversePort2Binding)
    {
      traverse(objPort.getBinding(), objPort, ctx);
      if (!ctx.processPort())
        return;
    }
    if (traversePort2ExtensibilityElement
      && objPort.getExtensibilityElements() != null)
    {
      Iterator it = objPort.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objPort, ctx);
        if (!ctx.processPort())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL operation artifact according to the settings in
   * the traversal context.
   * 
   * @param objOperation  a WSDL operation artifact.
   * @param parent        parent of the WSDL operation artifactt.
   * @param ctx           the traversal contex.
   */
  private void traverse(
    Operation objOperation,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeOperationProcessing();
    ctx.setOperation(objOperation);
    if (visitOperation)
    {
      visitor.visit(objOperation, parent, ctx);
      if (!ctx.processOperation())
        return;
    }
    if (objOperation == null)
      return;
    if (traverseOperation2Element)
    {
      traverse(objOperation.getDocumentationElement(), objOperation, ctx);
      if (!ctx.processOperation())
        return;
    }
    if (traverseOperation2Input)
    {
      traverse(objOperation.getInput(), objOperation, ctx);
      if (!ctx.processOperation())
        return;
    }
    if (traverseOperation2Output)
    {
      traverse(objOperation.getOutput(), objOperation, ctx);
      if (!ctx.processOperation())
        return;
    }
    if (traverseOperation2Fault && objOperation.getFaults() != null)
    {
      Iterator it = objOperation.getFaults().values().iterator();
      while (it.hasNext())
      {
        traverse((Fault) it.next(), objOperation, ctx);
        if (!ctx.processOperation())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL input artifact according to the settings in
   * the traversal context.
   * 
   * @param objInput  a WSDL input artifact.
   * @param parent    parent of the WSDL input artifact.
   * @param ctx       the traversal contex.
   */
  private void traverse(
    Input objInput,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeInputProcessing();
    ctx.setInput(objInput);
    if (visitInput)
    {
      visitor.visit(objInput, parent, ctx);
      if (!ctx.processInput())
        return;
    }
    if (objInput == null)
      return;
    if (traverseInput2Element)
    {
      traverse(objInput.getDocumentationElement(), objInput, ctx);
      if (!ctx.processInput())
        return;
    }
    if (traverseInput2Message)
      traverse(objInput.getMessage(), objInput, ctx);
  }

  /**
   * The method traverses given WSDL SOAP body artifact according to the 
   * settings in the traversal context.
   * 
   * @param objSOAPBody  a WSDL SOAP body artifact.
   * @param parent       parent of the WSDL SOAP body artifact.
   * @param ctx          the traversal contex.
   */
  private void traverse(
    SOAPBody objSOAPBody,
    Object parent,
    WSDLTraversalContext ctx)
  {
    //  	ctx.resumeSOAPBodyProcessing();
    ctx.setSOAPBody(objSOAPBody);
    if (visitSOAPBody)
      //	{
      visitor.visit(objSOAPBody, parent, ctx);
    /*	  if (!ctx.processSOAPBody())
    		return;
    	}
    	if (objSOAPBody == null)
    		return;
    	if (traverseSOAPBody2Part && objSOAPBody.getParts() != null) {
    	  if (ctx)
    	  Iterator it = objSOAPBody.getParts().iterator();
    	  while (it.hasNext()) {
    	  	
    		traverse((Part)it.next(), objSOAPBody, ctx);
    		if (!ctx.processSOAPBody())
    		  return;
    	  }
    	}
    */
  }

  /**
   * The method traverses given WSDL SOAP header fault artifact according to 
   * the settings in the traversal context.
   * 
   * @param objSOAPHeaderFault  a WSDL SOAP header fault artifact.
   * @param parent              parent of the WSDL SOAP header fault artifact.
   * @param ctx                 the traversal contex.
   */
  private void traverse(
    SOAPHeaderFault objSOAPHeaderFault,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.setSOAPHeaderFault(objSOAPHeaderFault);
    if (visitSOAPHeaderFault)
      visitor.visit(objSOAPHeaderFault, parent, ctx);
  }

  /**
   * The method traverses given WSDL output artifact according to the settings in
   * the traversal context.
   * 
   * @param objOutput  a WSDL output artifact.
   * @param parent   parent of the WSDL output artifact.
   * @param ctx      the traversal contex.
   * @param parent
   * @param ctx
   */
  private void traverse(
    Output objOutput,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeOutputProcessing();
    ctx.setOutput(objOutput);
    if (visitOutput)
    {
      visitor.visit(objOutput, parent, ctx);
      if (!ctx.processOutput())
        return;
    }
    if (objOutput == null)
      return;
    if (traverseOutput2Element)
    {
      traverse(objOutput.getDocumentationElement(), objOutput, ctx);
      if (!ctx.processOutput())
        return;
    }
    if (traverseOutput2Message)
      traverse(objOutput.getMessage(), objOutput, ctx);
  }

  /**
   * The method traverses given WSDL binding fault artifact according to the 
   * settings in the traversal context.
   * 
   * @param objBindingFault  a WSDL binding fault artifact.
   * @param parent           parent of the WSDL binding fault artifact.
   * @param ctx              the traversal contex.
   */
  private void traverse(
    BindingFault objBindingFault,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeBindingFaultProcessing();
    ctx.setBindingFault(objBindingFault);
    if (visitBindingFault)
    {
      visitor.visit(objBindingFault, parent, ctx);
      if (!ctx.processBindingFault())
        return;
    }
    if (objBindingFault == null)
      return;
    if (traverseBindingFault2Element)
    {
      traverse(objBindingFault.getDocumentationElement(), objBindingFault, ctx);
      if (!ctx.processBindingFault())
        return;
    }
    if (traverseBindingFault2SOAPFault
      && objBindingFault.getExtensibilityElements() != null)
    {
      Iterator it = objBindingFault.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        if (o instanceof SOAPFault)
          traverse((SOAPFault) o, objBindingFault, ctx);
        if (!ctx.processBindingFault())
          return;
      }
    }
    if (traverseBindingFault2ExtensibilityElement
      && objBindingFault.getExtensibilityElements() != null)
    {
      Iterator it = objBindingFault.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objBindingFault, ctx);
        if (!ctx.processBindingFault())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL SOAP operation artifact according to 
   * the settings in the traversal context.
   * 
   * @param objSOAPOperation  a WSDL SOAP operation artifact.
   * @param parent            parent of the WSDL SOAP operation artifact.
   * @param ctx               the traversal contex.
   */
  private void traverse(
    SOAPOperation objSOAPOperation,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.setSOAPOperation(objSOAPOperation);
    if (visitSOAPOperation)
      visitor.visit(objSOAPOperation, parent, ctx);
  }

  /**
   * The method traverses given WSDL binding input artifact according to 
   * the settings in the traversal context.
   * 
   * @param objBindingInput  a WSDL binding input artifact.
   * @param parent           parent of the WSDL binding input artifact.
   * @param ctx              the traversal contex.
   */
  private void traverse(
    BindingInput objBindingInput,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeBindingInputProcessing();
    ctx.setBindingInput(objBindingInput);
    if (visitBindingInput)
    {
      visitor.visit(objBindingInput, parent, ctx);
      if (!ctx.processBindingInput())
        return;
    }
    if (objBindingInput == null)
      return;
    if (traverseBindingInput2Element)
    {
      traverse(objBindingInput.getDocumentationElement(), objBindingInput, ctx);
      if (!ctx.processBindingInput())
        return;
    }
    if ((traverseBindingInput2SOAPBody || traverseBindingInput2SOAPHeader)
      && objBindingInput.getExtensibilityElements() != null)
    {
      Iterator it = objBindingInput.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        if (traverseBindingInput2SOAPBody && (o instanceof SOAPBody))
          traverse((SOAPBody) o, objBindingInput, ctx);
        else if (traverseBindingInput2SOAPHeader && (o instanceof SOAPHeader))
          traverse((SOAPHeader) o, objBindingInput, ctx);
        if (!ctx.processBindingInput())
          return;
      }
    }
    if (traverseBindingInput2ExtensibilityElement
      && objBindingInput.getExtensibilityElements() != null)
    {
      Iterator it = objBindingInput.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objBindingInput, ctx);
        if (!ctx.processBindingInput())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL binding operation artifact according to 
   * the settings in the traversal context.
   * 
   * @param objBindingOperation  a WSDL binding operation artifact.
   * @param parent               parent of the WSDL binding operation artifact.
   * @param ctx                  the traversal contex.
   */
  private void traverse(
    BindingOperation objBindingOperation,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeBindingOperationProcessing();
    ctx.setBindingOperation(objBindingOperation);
    if (visitBindingOperation)
    {
      visitor.visit(objBindingOperation, parent, ctx);
      if (!ctx.processBindingOperation())
        return;
    }
    if (objBindingOperation == null)
      return;
    if (traverseBindingOperation2Element)
    {
      traverse(
        objBindingOperation.getDocumentationElement(),
        objBindingOperation,
        ctx);
      if (!ctx.processBindingOperation())
        return;
    }
    if (traverseBindingOperation2SOAPOperation
      && objBindingOperation.getExtensibilityElements() != null)
    {
      Iterator it = objBindingOperation.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        if (o instanceof SOAPOperation)
          traverse((SOAPOperation) o, objBindingOperation, ctx);
        if (!ctx.processBindingOperation())
          return;
      }
    }
    if (traverseBindingOperation2ExtensibilityElement
      && objBindingOperation.getExtensibilityElements() != null)
    {
      Iterator it = objBindingOperation.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objBindingOperation, ctx);
        if (!ctx.processBindingOperation())
          return;
      }
    }
    if (traverseBindingOperation2Operation)
    {
      traverse(objBindingOperation.getOperation(), objBindingOperation, ctx);
      if (!ctx.processBindingOperation())
        return;
    }
    if (traverseBindingOperation2BindingInput)
    {
      traverse(objBindingOperation.getBindingInput(), objBindingOperation, ctx);
      if (!ctx.processBindingOperation())
        return;
    }
    if (traverseBindingOperation2BindingOutput)
    {
      traverse(
        objBindingOperation.getBindingOutput(),
        objBindingOperation,
        ctx);
      if (!ctx.processBindingOperation())
        return;
    }
    if (traverseBindingOperation2BindingFault
      && objBindingOperation.getBindingFaults() != null)
    {
      Iterator it = objBindingOperation.getBindingFaults().values().iterator();
      while (it.hasNext())
      {
        traverse((BindingFault) it.next(), objBindingOperation, ctx);
        if (!ctx.processBindingOperation())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL SOAP fault artifact according to the 
   * settings in the traversal context.
   * 
   * @param objSOAPFault  a WSDL SOAP fault artifact.
   * @param parent        parent of the WSDL SOAP fault artifact.
   * @param ctx           the traversal contex.
   */
  private void traverse(
    SOAPFault objSOAPFault,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.setSOAPFault(objSOAPFault);
    if (visitSOAPFault)
      visitor.visit(objSOAPFault, parent, ctx);
  }

  /**
   * The method traverses given WSDL binding artifact according to the settings in
   * the traversal context.
   * 
   * @param objBinding  a WSDL binding artifact.
   * @param parent      parent of the WSDL binding artifact.
   * @param ctx         the traversal contex.
   */
  private void traverse(
    Binding objBinding,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeBindingProcessing();
    ctx.setBinding(objBinding);
    if (visitBinding)
    {
      visitor.visit(objBinding, parent, ctx);
      if (!ctx.processBinding())
        return;
    }
    if (objBinding == null)
      return;
    if (traverseBinding2Element)
    {
      traverse(objBinding.getDocumentationElement(), objBinding, ctx);
      if (!ctx.processBinding())
        return;
    }
    if (traverseBinding2SOAPBinding
      && objBinding.getExtensibilityElements() != null)
    {
      Iterator it = objBinding.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        if (o instanceof SOAPBinding)
          traverse((SOAPBinding) o, objBinding, ctx);
        if (!ctx.processBinding())
          return;
      }
    }
    if (traverseBinding2PortType)
    {
      traverse(objBinding.getPortType(), objBinding, ctx);
      if (!ctx.processBinding())
        return;
    }
    if (traverseBinding2BindingOperation
      && objBinding.getBindingOperations() != null)
    {
      Iterator it = objBinding.getBindingOperations().iterator();
      while (it.hasNext())
      {
        traverse((BindingOperation) it.next(), objBinding, ctx);
        if (!ctx.processBinding())
          return;
      }
    }
    if (traverseBinding2ExtensibilityElement
      && objBinding.getExtensibilityElements() != null)
    {
      Iterator it = objBinding.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objBinding, ctx);
        if (!ctx.processBinding())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL fault artifact according to the settings in
   * the traversal context.
   * 
   * @param objFault  a WSDL fault artifact.
   * @param parent    parent of the WSDL fault artifact.
   * @param ctx       the traversal contex.
   */
  private void traverse(
    Fault objFault,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeFaultProcessing();
    ctx.setFault(objFault);
    if (visitFault)
    {
      visitor.visit(objFault, parent, ctx);
      if (!ctx.processFault())
        return;
    }
    if (objFault == null)
      return;
    if (traverseFault2Element)
    {
      traverse(objFault.getDocumentationElement(), objFault, ctx);
      if (!ctx.processFault())
        return;
    }
    if (traverseFault2Message)
      traverse(objFault.getMessage(), objFault, ctx);
  }

  /**
   * The method traverses given WSDL service artifact according to the settings in
   * the traversal context.
   * 
   * @param objService  a WSDL service artifact.
   * @param parent      parent of the WSDL service artifact.
   * @param ctx         the traversal contex.
   */
  private void traverse(
    Service objService,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeServiceProcessing();
    ctx.setService(objService);
    if (visitService)
    {
      visitor.visit(objService, parent, ctx);
      if (!ctx.processService())
        return;
    }
    if (objService == null)
      return;
    if (traverseService2Element)
    {
      traverse(objService.getDocumentationElement(), objService, ctx);
      if (!ctx.processService())
        return;
    }
    if (traverseService2ExtensibilityElement
      && objService.getExtensibilityElements() != null)
    {
      Iterator it = objService.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objService, ctx);
        if (!ctx.processService())
          return;
      }
    }
    if (traverseService2Port && objService.getPorts() != null)
    {
      Iterator it = objService.getPorts().values().iterator();
      while (it.hasNext())
      {
        traverse((Port) it.next(), objService, ctx);
        if (!ctx.processService())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL import artifact according to the settings in
   * the traversal context.
   * 
   * @param objImport  a WSDL import artifact.
   * @param parent     parent of the WSDL import artifact.
   * @param ctx        the traversal contex.
   */
  private void traverse(
    Import objImport,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeImportProcessing();
    ctx.setImport(objImport);
    if (visitImport)
    {
      visitor.visit(objImport, parent, ctx);
      if (!ctx.processImport())
        return;
    }
    if (objImport == null)
      return;
    if (traverseImport2Element)
    {
      traverse(objImport.getDocumentationElement(), objImport, ctx);
      if (!ctx.processImport())
        return;
    }
    if (traverseImport2Definition)
    {
      try
      {
        Definition definition = objImport.getDefinition();
        if ((definition != null) && (!alreadyTraversedDefinitions.contains(definition)))
        {
          alreadyTraversedDefinitions.add(definition);
          traverse(objImport.getDefinition(), objImport, ctx);
        }
      }
      catch (Exception e){}
    }
  }

  /**
   * The method traverses given WSDL binding output artifact according to the 
   * settings in the traversal context.
   * 
   * @param objBindingOutput  a WSDL binding output artifact.
   * @param parent            parent of the WSDL binding output artifact.
   * @param ctx               the traversal contex.
   */
  private void traverse(
    BindingOutput objBindingOutput,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeBindingOutputProcessing();
    ctx.setBindingOutput(objBindingOutput);
    if (visitBindingOutput)
    {
      visitor.visit(objBindingOutput, parent, ctx);
      if (!ctx.processBindingOutput())
        return;
    }
    if (objBindingOutput == null)
      return;
    if (traverseBindingOutput2Element)
    {
      traverse(
        objBindingOutput.getDocumentationElement(),
        objBindingOutput,
        ctx);
      if (!ctx.processBindingOutput())
        return;
    }
    if ((traverseBindingOutput2SOAPBody || traverseBindingOutput2SOAPHeader)
      && objBindingOutput.getExtensibilityElements() != null)
    {
      Iterator it = objBindingOutput.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        if (traverseBindingOutput2SOAPBody && (o instanceof SOAPBody))
          traverse((SOAPBody) o, objBindingOutput, ctx);
        else if (traverseBindingOutput2SOAPHeader && (o instanceof SOAPHeader))
          traverse((SOAPHeader) o, objBindingOutput, ctx);
        if (!ctx.processBindingOutput())
          return;
      }
    }
    if (traverseBindingOutput2ExtensibilityElement
      && objBindingOutput.getExtensibilityElements() != null)
    {
      Iterator it = objBindingOutput.getExtensibilityElements().iterator();
      while (it.hasNext())
      {
        traverse((ExtensibilityElement) it.next(), objBindingOutput, ctx);
        if (!ctx.processBindingOutput())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL port type artifact according to the settings in
   * the traversal context.
   * 
   * @param objPortType  a WSDL port type artifact.
   * @param parent       parent of the WSDL port type.
   * @param ctx          the traversal contex.
   */
  private void traverse(
    PortType objPortType,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumePortTypeProcessing();
    ctx.setPortType(objPortType);
    if (visitPortType)
    {
      visitor.visit(objPortType, parent, ctx);
      if (!ctx.processPortType())
        return;
    }
    if (objPortType == null)
      return;
    if (traversePortType2Element)
    {
      traverse(objPortType.getDocumentationElement(), objPortType, ctx);
      if (!ctx.processPortType())
        return;
    }
    if (traversePortType2Operation && objPortType.getOperations() != null)
    {
      Iterator it = objPortType.getOperations().iterator();
      while (it.hasNext())
      {
        traverse((Operation) it.next(), objPortType, ctx);
        if (!ctx.processPortType())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL message artifact according to the settings in
   * the traversal context.
   * 
   * @param objMessage  a WSDL message.
   * @param parent      parent of the WSDL message.
   * @param ctx         the traversal contex.
   */
  private void traverse(
    Message objMessage,
    Object parent,
    WSDLTraversalContext ctx)
  {
    ctx.resumeMessageProcessing();
    ctx.setMessage(objMessage);
    if (visitMessage)
    {
      visitor.visit(objMessage, parent, ctx);
      if (!ctx.processMessage())
        return;
    }
    if (objMessage == null)
      return;
    if (traverseMessage2Element)
    {
      traverse(objMessage.getDocumentationElement(), objMessage, ctx);
      if (!ctx.processMessage())
        return;
    }
    if (traverseMessage2Part && objMessage.getParts() != null)
    {
      Iterator it = objMessage.getParts().values().iterator();
      while (it.hasNext())
      {
        traverse((Part) it.next(), objMessage, ctx);
        if (!ctx.processMessage())
          return;
      }
    }
  }

  /**
   * The method traverses given WSDL part artifact according to the settings.
   * @param a  a WSDL part artifact.
   */
  public void traverse(Part a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL service artifact according to the settings.
   * @param a  a WSDL service artifact.
   */
  public void traverse(Service a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL types artifact according to the settings.
   * @param a WSDL types artifact.
   */
  public void traverse(Types a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL operation artifact according to the settings.
   * @param a  a WSDL operation artifact.
   */
  public void traverse(Operation a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL input artifact according to the settings.
   * @param a  a WSDL input artifact.
   */
  public void traverse(Input a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL output artifact according to the settings.
   * @param a  a WSDL output artifact.
   */
  public void traverse(Output a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL fault artifact according to the settings.
   * @param a  a WSDL fault artifact.
   */
  public void traverse(Fault a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL binding artifact according to the settings.
   * @param a  a WSDL binding artifact.
   */
  public void traverse(Binding a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL binding operation artifact according to the 
   * settings.
   * @param a  a WSDL binding operation artifact.
   */
  public void traverse(BindingOperation a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL binding input artifact according to 
   * the settings.
   * @param a  a WSDL binding input artifact.
   */
  public void traverse(BindingInput a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL binding output artifact according to 
   * the settings.
   * @param a  a WSDL binding output artifact.
   */
  public void traverse(BindingOutput a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL binding fault artifact according to 
   * the settings.
   * @param a   a WSDL binding fault artifact.
   */
  public void traverse(BindingFault a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL import artifact according to the settings.
   * @param a  a WSDL import artifact.
   */
  public void traverse(Import a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL element artifact according to the settings.
   * @param a  a WSDL element artifact.
   */
  public void traverse(Element a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL message artifact according to the settings.
   * @param a  a WSDL message artifact.
   */
  public void traverse(Message a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL port artifact according to the settings.
   * @param a  a WSDL port artifact.
   */
  public void traverse(Port a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL port type artifact according to the settings.
   * @param a  a WSDL port type artifact.
   */
  public void traverse(PortType a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL definition artifact according to the settings.
   * @param a  a WSDL definition artifact.
   */
  public void traverse(Definition a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL extensibility elment artifact according 
   * to the settings.
   * @param a  a WSDL extensibility element artifact.
   */
  public void traverse(ExtensibilityElement a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL SOAP binding artifact according to 
   * the settings.
   * @param a  a WSDL SOAP binding artifact.
   */
  public void traverse(SOAPBinding a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL SOAP body artifact according to the settings.
   * @param a  a WSDL SOAP body artifact.
   */
  public void traverse(SOAPBody a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL SOAP header artifact according to the settings.
   * @param a  a WSDL SOAP header artifact.
   */
  public void traverse(SOAPHeader a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL SOAP header fault artifact according to 
   * the settings.
   * @param a  a WSDL SOAP header fault artifact.
   */
  public void traverse(SOAPHeaderFault a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL SOAP fault artifact according to the settings.
   * @param a  a WSDL SOAP fault artifact.
   */
  public void traverse(SOAPFault a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }
  /**
   * The method traverses given WSDL SOAP operation artifact according to 
   * the settings.
   * @param a  a WSDL SOAP operation artifact.
   */
  public void traverse(SOAPOperation a)
  {
    adjust();
    traverse(a, null, new WSDLTraversalContext(this));
  }

  /**
   * The method traverses given WSDL part artifact according to the settings.
   * @param a       a WSDL part artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(Part a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL service artifact according to the settings.
   * @param a       a WSDL service artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(Service a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL types artifact according to the settings.
   * @param a       a WSDL types artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(Types a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL operation artifact according to the settings.
   * @param a       a WSDL operation artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(Operation a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL input artifact according to the settings.
   * @param a       a WSDL input artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(Input a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL output artifact according to the settings.
   * @param a       a WSDL output artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(Output a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL fault artifact according to the settings.
   * @param a       a WSDL fault artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(Fault a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL binding artifact according to the settings.
   * @param a       a WSDL binding artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(Binding a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL binding operation artifact according 
   * to the settings.
   * @param a       a WSDL binding operation artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(BindingOperation a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL binding input artifact according 
   * to the settings.
   * @param a       a WSDL binding input artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(BindingInput a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL binding output artifact according to 
   * the settings.
   * @param a       a WSDL binding output artifact.
   * @param params  a Map object representing settings.  
   */
  public void traverse(BindingOutput a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL binding fault artifact according to 
   * the settings.
   * @param a       a WSDL binding fault artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(BindingFault a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL import artifact according to the settings.
   * @param a       a WSDL import artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(Import a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL element artifact according to the settings.
   * @param a       a WSDL element artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(Element a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL message artifact according to the settings.
   * @param a       a WSDL message.
   * @param params  a Map object representing settings.   
   */
  public void traverse(Message a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL port artifact according to the settings.
   * @param a       a WSDL port.
   * @param params  a Map object representing settings.   
   */
  public void traverse(Port a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL port type artifact according to the settings.
   * @param a       a WSDL port type artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(PortType a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL definition artifact according to the settings.
   * @param a       a WSDL definition artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(Definition a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL extensibility element artifact according to the settings.
   * @param a       a WSDL extensibility element artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(ExtensibilityElement a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL SOAP binding artifact according to the settings.
   * @param a       a WSDL SOAP binding artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(SOAPBinding a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL SOAP body artifact according to the settings.
   * @param a       a WSDL SOAP body artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(SOAPBody a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL SOAP header artifact according to the settings.
   * @param a       a WSDL SOAP header artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(SOAPHeader a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL hearder fault artifact according to the settings.
   * @param a       a WSDL SOAP header fault artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(SOAPHeaderFault a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL SOAP fault artifact according to the settings.
   * @param a       a WSDL SOAP fault artifact.
   * @param params  a Map object representing settings.   
   */
  public void traverse(SOAPFault a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
  /**
   * The method traverses given WSDL SOAP operation artifact according to the settings.
   * @param a       a WSDL SOAP operation artifact.
   * @param params  a Map object representing settings.
   */
  public void traverse(SOAPOperation a, Map params)
  {
    adjust();
    WSDLTraversalContext ctx = new WSDLTraversalContext(this);
    if (params != null)
      ctx.params.putAll(params);
    traverse(a, null, ctx);
  }
}
