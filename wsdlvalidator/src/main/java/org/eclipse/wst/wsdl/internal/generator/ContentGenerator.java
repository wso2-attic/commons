/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.internal.generator;


import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;


public interface ContentGenerator
{
  public static String ADDRESS_LOCATION = "http://www.example.org/";

  public String[] getRequiredNamespaces();

  public String getPreferredNamespacePrefix(String namespace);

  // generates the 'address' extensiblity element for a port
  public void generatePortContent(Port port);

  public void generateBindingContent(Binding binding, PortType portType);

  public void generateBindingOperationContent(BindingOperation bindingOperation, Operation operation);

  public void generateBindingInputContent(BindingInput bindingInput, Input input);

  public void generateBindingOutputContent(BindingOutput bindingOutput, Output output);

  public void generateBindingFaultContent(BindingFault bindingFault, Fault fault);

  public String getProtocol();
  
  public void setAddressLocation(String addressLocation);
}
