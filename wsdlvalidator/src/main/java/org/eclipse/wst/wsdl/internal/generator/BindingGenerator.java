/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.internal.generator;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.internal.generator.extension.ContentGeneratorExtensionFactoryRegistry;


/*
 * Class used to generate the Binding and it's content.  We look in the registry for
 * appropriate ContentGenerator classes based on the existing Binding Content's namespace.
 * Alternatively, users can pass in a namespace through the constructor to specify which
 * namespace to use when searching the registry.
 * 
 * The ContentGenerator may also be set manually by calling
 * setContentGenerator(ContentGenerator).
 */
public class BindingGenerator extends BaseGenerator
{
  private WSDLFactory factory = WSDLFactory.eINSTANCE;

  private Binding binding;

  /*
   * When the constructor is used, automatically attempt to retrieve a proper
   * ContentGenerator based on the Binding given.  The ContentGenerator may
   * be replaced by calling setContentGenerator(ContentGenerator).
   */
  public BindingGenerator(Definition definition, Binding binding)
  {
    this.definition = definition;
    this.binding = binding;
    contentGenerator = getContentGenerator(binding);
  }
  
  public void setDefinition(Definition definition) {
    this.definition = definition;
  }
  
  public void setBinding(Binding binding) {
    this.binding = binding;
  }

  /*
   * When the constructor is used, automatically attempt to retrieve a proper
   * ContentGenerator based on the namespace given.  The ContentGenerator may
   * be replaced by calling setContentGenerator(ContentGenerator).
   */
  public BindingGenerator(Definition definition, Binding binding, String namespace)
  {
    this.definition = definition;
    this.binding = binding;
    contentGenerator = getContentGenerator(namespace);
  }

  public static ContentGenerator getContentGenerator(Binding binding)
  {
    if (binding == null)
    {
      return null;
    }
    /******************************************************
     * Find the regeistered Content Generator for the Binding
     ******************************************************/
    // Get BindingContent namespace
    String namespace = null;
    List eeList = binding.getEExtensibilityElements();
    if (eeList.size() > 0)
    {
      ExtensibilityElement ee = (ExtensibilityElement)eeList.get(0);
      // TODO: QName qName = ee.getElementType(); go get the namespace instead?
      namespace = ee.getElement().getNamespaceURI();
      return getContentGenerator(namespace);
    }

    return null;
  }

  public static ContentGenerator getContentGenerator(String namespace)
  {
    ContentGenerator contentGen = null;
    if (namespace != null)
    {
      ContentGeneratorExtensionFactoryRegistry factoryRegistry = ContentGeneratorExtensionFactoryRegistry.getInstance();
      contentGen = factoryRegistry.getGeneratorClassFromNamespace(namespace);
    }

    return contentGen;
  }

  private Binding createEmptyBinding(String localName)
  {
    Binding newBinding = WSDLFactory.eINSTANCE.createBinding();
    newBinding.setQName(new QName(definition.getTargetNamespace(), localName));
    newBinding.setEnclosingDefinition(definition);
    definition.addBinding(newBinding);

    return newBinding;
  }

  /*
   * TODO: Scenario:
   * 1)overwrite == false
   * 2)BindingOperation with 1 input
   * 3)Corresponding Operation (with same name) with 1 input and 1 output
   * If we generate with overwrite == false, nothing is done.  Thus,  an
   * output is not generated on the BindingOperation.  This is because we
   * search for existing Elements only at the level of BindingOperations.
   * For example, if there is a corresponding BindingOperation with the same
   * name as our Operation, leave it alone.... but since there is already
   * a BindingOperation with the same name, we don't create a new BindingOperation.
   * 
   * The correct implementation is reduce this granularity to the MessageReference
   * level.  The code is almost there except for how we generate the Binding element
   * content.  Look at BindingGenrator.generateBindingOperation() and
   * SOAPContentGenerator.java and it's content generation method
   * for a good place to start.
   * 
   * For wtp RC1, We shall only look at the Operation level (as we do in the previous
   * version).
   */
  public Binding generateBinding()
  {
    try
    {
      // If Binding is null (No Binding was passed into the constructor), we create an empty Binding first.
      if (binding == null)
      {
        binding = createEmptyBinding(getName());
      }
      if (getName() != null && !binding.getQName().getLocalPart().equals(getName()))
      {
        binding.setQName(new QName(binding.getQName().getNamespaceURI(), getName()));
      }
      if (getRefName() != null)
      {
        PortType portType = getPortType();
        binding.setEPortType(portType);
        if (portType == null)
        {
          //The model doesn't reconile with it's Element properly when we're setting a null for it's PortType
          binding.getElement().setAttribute("type", "");
        }
      }

      List bindingOperations = binding.getEBindingOperations();
      PortType portType = binding.getEPortType();

      if (!getOverwrite())
      {
        // Don't Overwrite
        if (portType == null)
        {
          return binding;
        }
        else
        {
          List operations = portType.getOperations();

          /*******************************************************************************
           * Determine which Operations require new a new corresponding BindingOperations
           *******************************************************************************/
          List newBindingOpsNeeded = new ArrayList();
          for (int index = 0; index < operations.size(); index++)
          {
            Operation operation = (Operation)operations.get(index);

            boolean foundMatch = false;
            Iterator bindingOperationsIt = bindingOperations.iterator();
            while (bindingOperationsIt.hasNext())
            {
              BindingOperation bindingOperation = (BindingOperation)bindingOperationsIt.next();

              if (namesEqual(bindingOperation.getName(), operation.getName()))
              {
                foundMatch = true;
                break;
              }
            }

            if (!foundMatch)
            {
              newBindingOpsNeeded.add(operation);
            }
          }
          // newBindingOpsNeeded is the List of Operations needing new corresponding
          // BindingOperation's
          List newBindingOps = createNewBindingOperations(newBindingOpsNeeded);

          // don't add required namespace if nothing is really being added
          if (!newBindingOps.isEmpty()) {
            addRequiredNamespaces(binding);            
          }
          
          // Generate the contents of the new BindingOperation's
          Iterator newBindingOpsIt = newBindingOps.iterator();
          while (newBindingOpsIt.hasNext())
          {
            BindingOperation newBindingOp = (BindingOperation)newBindingOpsIt.next();
            generateBindingOperation(newBindingOp);
            generateBindingOperationContent(newBindingOp);
          }
        }

        generateBindingContent(binding);
      }
      else
      {
        // Overwrite
        if (portType == null)
        {
          // We need to blow away everything under the Binding.  No PortType associated with this Binding
          bindingOperations.clear();
          return binding;
        }
        else
        {
          addRequiredNamespaces(binding);
          List operations = portType.getOperations();

          /******************************************************
           * Compare the Operations
           ******************************************************/
          // Remove any BindingOperations which are no longer used
          for (int index = 0; index < bindingOperations.size(); index++)
          {
            BindingOperation bindingOperation = (BindingOperation)bindingOperations.get(index);

            boolean foundMatch = false;
            Iterator operationsIt = operations.iterator();
            while (operationsIt.hasNext())
            {
              Operation operation = (Operation)operationsIt.next();

              if (namesEqual(bindingOperation.getName(), operation.getName()))
              {
                foundMatch = true;
                break;
              }
            }

            if (!foundMatch)
            {
              // We need to remove this BindingFault from the bindingFaults List
              bindingOperations.remove(index);
              index--;
            }
          }

          // Remove any Operations which already exists in bindingOperations.  What we
          // have left are the Operations which needs newly created BindingOperations
          List bindingOperationsNeeded = new ArrayList();
          for (int index = 0; index < operations.size(); index++)
          {
            Operation operation = (Operation)operations.get(index);

            boolean foundMatch = false;
            Iterator bindingOperationsIt = bindingOperations.iterator();
            while (bindingOperationsIt.hasNext())
            {
              BindingOperation bindingOperation = (BindingOperation)bindingOperationsIt.next();

              if (namesEqual(bindingOperation.getName(), operation.getName()))
              {
                foundMatch = true;
                break;
              }
            }

            if (!foundMatch)
            {
              // We need to remove this BindingFault from the bindingFaults List
              bindingOperationsNeeded.add(operation); // Store the actual Operation
            }
          }

          // Create required BindingOperations
          createNewBindingOperations(bindingOperationsNeeded);

          /******************************************************
           * Process the contents of the Operations
           ******************************************************/
          Iterator bindingOperationsIt = binding.getEBindingOperations().iterator();
          while (bindingOperationsIt.hasNext())
          {
            generateBindingOperation((BindingOperation)bindingOperationsIt.next());
          }

          generateBindingContent(binding);

          return binding;
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  private List createNewBindingOperations(List operations)
  {
    List newBindingOps = new ArrayList();

    Iterator neededBindingOperationsIt = operations.iterator();
    while (neededBindingOperationsIt.hasNext())
    {
      Operation operation = (Operation)neededBindingOperationsIt.next();
      BindingOperation newBindingOperation = factory.createBindingOperation();
      newBindingOperation.setEOperation(operation);
      newBindingOperation.setName(operation.getName());
      binding.addBindingOperation(newBindingOperation);

      newBindingOps.add(newBindingOperation);
    }

    return newBindingOps;
  }

  private void generateBindingOperation(BindingOperation bindingOperation)
  {
    BindingInput bindingInput = bindingOperation.getEBindingInput();
    BindingOutput bindingOutput = bindingOperation.getEBindingOutput();
    List bindingFaults = bindingOperation.getEBindingFaults();

    Operation operation = bindingOperation.getEOperation();
    Input input = operation.getEInput();
    Output output = operation.getEOutput();
    List faults = operation.getEFaults();

    /******************************************************
     * Compare the Operation names
     ******************************************************/
    if (!namesEqual(bindingOperation.getName(), operation.getName()))
    {
      bindingOperation.setName(operation.getName());
    }

    /******************************************************
     * Compare the Output
     ******************************************************/
    if (output == null)
    {
      bindingOperation.setBindingOutput(null);
    }
    else
    {
      // Create BindingOutput if necessary
      if (bindingOutput == null)
      {
        BindingOutput newBindingOutput = factory.createBindingOutput();
        newBindingOutput.setEOutput(output);
        newBindingOutput.setName(output.getName());
        bindingOperation.setBindingOutput(newBindingOutput);
      }
      else
      {
        // Compare the Output names
        if (!namesEqual(bindingOutput.getName(), output.getName()))
        {
          bindingOutput.setName(output.getName());
        }
      }
    }
    generateBindingOutputContent(bindingOperation.getEBindingOutput());

    /******************************************************
     * Compare the Input
     ******************************************************/
    if (input == null)
    {
      bindingOperation.setBindingInput(null);
    }
    else
    {
      // Create BindingInput if necessary
      if (bindingInput == null)
      {
        BindingInput newBindingInput = factory.createBindingInput();
        newBindingInput.setEInput(input);
        newBindingInput.setName(input.getName());
        bindingOperation.setBindingInput(newBindingInput);
      }
      else
      {
        // Compare the Input names
        if (!namesEqual(bindingInput.getName(), input.getName()))
        {
          bindingInput.setName(input.getName());
        }
      }
    }
    generateBindingInputContent(bindingOperation.getEBindingInput());

    /******************************************************
     * Compare the Faults
     ******************************************************/
    // Remove any BindingFaults which are no longer used
    for (int index = 0; index < bindingFaults.size(); index++)
    {
      BindingFault bindingFault = (BindingFault)bindingFaults.get(index);

      boolean foundMatch = false;
      Iterator faultsIt = faults.iterator();
      while (faultsIt.hasNext())
      {
        Fault fault = (Fault)faultsIt.next();
        if (namesEqual(bindingFault.getName(), fault.getName()))
        {
          foundMatch = true;
          break;
        }
      }

      if (!foundMatch)
      {
        // We need to remove this BindingFault from the bindingFaults List
        bindingFaults.remove(index);
        index--;
      }
    }

    // Remove any Faults which already exists in bindingFaults.  What we
    // have left are the Faults which needs newly created BindingFaults
    List bindingFaultsNeeded = new ArrayList();
    for (int index = 0; index < faults.size(); index++)
    {
      Fault fault = (Fault)faults.get(index);

      boolean foundMatch = false;
      Iterator bindingFaultsIt = bindingFaults.iterator();
      while (bindingFaultsIt.hasNext())
      {
        BindingFault bindingFault = (BindingFault)bindingFaultsIt.next();
        if (namesEqual(bindingFault.getName(), fault.getName()))
        {
          foundMatch = true;
          break;
        }
      }

      if (!foundMatch)
      {
        // We need to remove this BindingFault from the bindingFaults List
        bindingFaultsNeeded.add(fault);
      }
    }

    // bindingFaultsNeeded contains the needed BindingFault's we need to create
    Iterator neededBindingFaultsIt = bindingFaultsNeeded.iterator();
    while (neededBindingFaultsIt.hasNext())
    {
      Fault fault = (Fault)neededBindingFaultsIt.next();
      BindingFault newBindingFault = factory.createBindingFault();
      newBindingFault.setEFault(fault);
      newBindingFault.setName(fault.getName());
      bindingOperation.addBindingFault(newBindingFault);
    }

    // Create the contents for each BindingFault
    Iterator faultContentIt = bindingOperation.getEBindingFaults().iterator();
    while (faultContentIt.hasNext())
    {
      BindingFault bindingFault = (BindingFault)faultContentIt.next();
      generateBindingFaultContent(bindingFault);
    }

    generateBindingOperationContent(bindingOperation);
  }

  private boolean namesEqual(String name1, String name2)
  {
    boolean match = false;

    if (name1 != null ^ name2 != null)
    {
      // one is null
      match = false;
    }
    else if (name1 != null && name2 != null)
    {
      // neither is null
      match = name1.equals(name2);
    }
    else
    {
      // both are null
      match = true;
    }

    return match;
  }

  protected void generateBindingContent(Binding binding)
  {
    if (contentGenerator != null)
    {
      if (getOverwrite() || binding.getEExtensibilityElements().size() == 0)
      {
        contentGenerator.generateBindingContent(binding, (PortType)binding.getEPortType());
      }
    }
    else
    {
      removeExtensibilityElements(binding);
    }
  }

  protected void generateBindingOperationContent(BindingOperation bindingOperation)
  {
    if (bindingOperation != null && contentGenerator != null)
    {
      contentGenerator.generateBindingOperationContent(bindingOperation, bindingOperation.getEOperation());
    }
    else
    {
      removeExtensibilityElements(bindingOperation);
    }
  }

  protected void generateBindingInputContent(BindingInput bindingInput)
  {
    if (bindingInput != null && contentGenerator != null)
    {
      contentGenerator.generateBindingInputContent(bindingInput, bindingInput.getEInput());
    }
    else
    {
      removeExtensibilityElements(bindingInput);
    }
  }

  protected void generateBindingOutputContent(BindingOutput bindingOutput)
  {
    if (bindingOutput != null && contentGenerator != null)
    {
      contentGenerator.generateBindingOutputContent(bindingOutput, bindingOutput.getEOutput());
    }
    else
    {
      removeExtensibilityElements(bindingOutput);
    }
  }

  protected void generateBindingFaultContent(BindingFault bindingFault)
  {
    if (bindingFault != null && contentGenerator != null)
    {
      contentGenerator.generateBindingFaultContent(bindingFault, bindingFault.getEFault());
    }
    else
    {
      removeExtensibilityElements(bindingFault);
    }
  }

  private void removeExtensibilityElements(ExtensibleElement ee)
  {
    if (ee != null)
    {
      ee.getEExtensibilityElements().clear();
    }
  }

  /*
   * Generate Port Content for all Ports with a reference to the Binding
   * which was passed in through the constructor (or a reference to the
   * newly created Binding).
   */
  public void generatePortContent()
  {
    if (binding != null && contentGenerator != null)
    {
      List portsToUpdate = new ArrayList();

      Iterator servicesIt = binding.getEnclosingDefinition().getEServices().iterator();
      while (servicesIt.hasNext())
      {
        Service service = (Service)servicesIt.next();
        Iterator portsIt = service.getEPorts().iterator();
        while (portsIt.hasNext())
        {
          Port port = (Port)portsIt.next();
          if (binding.equals(port.getEBinding()))
          {
            // Found a match
            portsToUpdate.add(port);
          }
        }
      }

      Iterator portsToUpdateIt = portsToUpdate.iterator();
      while (portsToUpdateIt.hasNext())
      {
        contentGenerator.generatePortContent((Port)portsToUpdateIt.next());
      }
    }
  }

  /*
   * methods addRequiredNamespaces() and computeUniquePrefix() are used to add necessary
   * namespaces 
   * 
   * TODO:
   * Does this belong here?  This is copied from wsdl.ui.  Can we sync up in some way?
   */
  protected void addRequiredNamespaces(Binding binding)
  {
    if (contentGenerator != null)
    {
      String[] namespaceNames = contentGenerator.getRequiredNamespaces();
      String[] preferredPrefixes = new String [namespaceNames.length];
      for (int index = 0; index < namespaceNames.length; index++)
      {
        preferredPrefixes[index] = contentGenerator.getPreferredNamespacePrefix(namespaceNames[index]);
      }

      Map map = binding.getEnclosingDefinition().getNamespaces();

      for (int i = 0; i < namespaceNames.length; i++)
      {
        String namespace = namespaceNames[i];
        if (!map.containsValue(namespace))
        {
          String prefix = (i < preferredPrefixes.length) ? preferredPrefixes[i] : "p0";
          if (map.containsKey(prefix))
          {
            prefix = computeUniquePrefix("p", map);
          }
          binding.getEnclosingDefinition().addNamespace(prefix, namespace);
        }
      }
    }
  }

  private String computeUniquePrefix(String base, Map table)
  {
    int i = 0;
    String prefix = base;
    while (true)
    {
      if (!table.containsKey(prefix))
      {
        break;
      }
      else
      {
        prefix = base + i;
        i++;
      }
    }
    return prefix;
  }

  private PortType getPortType()
  {
    if (getRefName().equals(""))
    {
      // Means we should set the PortType to Unspecified
      return null;
    }

    if (getRefName() != null)
    {
      Iterator portTypeIt = definition.getEPortTypes().iterator();

      while (portTypeIt.hasNext())
      {
        PortType pt = (PortType)portTypeIt.next();
        List prefixedNames = getPrefixedNames(pt);
        if (prefixedNames.contains(getRefName()))
        {
          return pt;
        }
      }
    }

    return binding.getEPortType();
  }

  private List getPrefixedNames(PortType portType)
  {
    List prefixedNames = new ArrayList();
    String currentPortTypeName = portType.getQName().getLocalPart();
    String currentNamespace = portType.getQName().getNamespaceURI();

    Map namespaceMap = definition.getNamespaces();
    Iterator keys = namespaceMap.keySet().iterator();
    while (keys.hasNext())
    {
      Object key = keys.next();
      Object value = namespaceMap.get(key);

      if (currentNamespace.equals(value))
      {
        // Found a match.  Add to our list
        prefixedNames.add(key + ":" + currentPortTypeName);
      }
    }

    return prefixedNames;

  }
}
