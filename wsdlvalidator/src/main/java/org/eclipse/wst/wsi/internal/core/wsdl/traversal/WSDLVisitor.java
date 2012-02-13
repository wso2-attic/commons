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
 * The interface defines callback methods during traverse WSDL.
 * WSDLVisitor could cancel artifact processing by using traversal context
 * <code>cancelXXXProcessing</code> methods.
 * 
 * @author Kulik
 */
public interface WSDLVisitor
{
  /**
   * Method visits WSDL part object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Part obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL service object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Service obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL types object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Types obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL operation object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Operation obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL input object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Input obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL output object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Output obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL fault object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Fault obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL binding object.
     * @param obj WSDL element to be visited.
     * @param parent WSDL element.
     * @param ctx current traversal context.
   */
  public void visit(Binding obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL binding operation object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(
    BindingOperation obj,
    Object parent,
    WSDLTraversalContext ctx);
  /**
   * Method visits WSDL binding input object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(BindingInput obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL binding output object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(BindingOutput obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL binding fault object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(BindingFault obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL import object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Import obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL element object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Element obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL message object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Message obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL port object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Port obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL port type object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(PortType obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL definition object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(Definition obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL extensibility element object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(
    ExtensibilityElement obj,
    Object parent,
    WSDLTraversalContext ctx);
  /**
   * Method visits WSDL SOAP binding object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(SOAPBinding obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL SOAP body object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(SOAPBody obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL SOAP header object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(SOAPHeader obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL SOAP header fault object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(
    SOAPHeaderFault obj,
    Object parent,
    WSDLTraversalContext ctx);
  /**
   * Method visits WSDL SOAP fault object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element.
   * @param ctx current traversal context.
   */
  public void visit(SOAPFault obj, Object parent, WSDLTraversalContext ctx);
  /**
   * Method visits WSDL SOAP operation object.
   * @param obj WSDL element to be visited.
   * @param parent WSDL element..
   * @param ctx current traversal context.
   */
  public void visit(SOAPOperation obj, Object parent, WSDLTraversalContext ctx);
}
