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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl;

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

import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLVisitor;
import org.w3c.dom.Element;


/**
 * Process assertion using WSDLVisitor interface.
 */
public abstract class AssertionProcessVisitor
  extends AssertionProcess
  implements WSDLVisitor
{
  /**
   * @param BaseValidatorImpl
   */
  public AssertionProcessVisitor(BaseValidatorImpl impl)
  {
    super(impl);
  }
  
  public void visit(Part obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Service obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Types obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Operation obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Input obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Output obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Fault obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Binding obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(
    BindingOperation obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  public void visit(
    BindingInput obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  public void visit(
    BindingOutput obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  public void visit(
    BindingFault obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  public void visit(Import obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Element obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Message obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Port obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(PortType obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(Definition obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(
    ExtensibilityElement obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  public void visit(SOAPBinding obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(SOAPBody obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(SOAPHeader obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(
    SOAPHeaderFault obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
  public void visit(SOAPFault obj, Object parent, WSDLTraversalContext ctx)
  {
  }
  public void visit(
    SOAPOperation obj,
    Object parent,
    WSDLTraversalContext ctx)
  {
  }
}