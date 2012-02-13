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

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLFactory;


public class PortGenerator extends BaseGenerator
{
  private Service service;
  private String addressLocation = ContentGenerator.ADDRESS_LOCATION;

  /**
   * Constructs a port generator given the target service.
   * @param service the target Service, must not be null.
   */
  public PortGenerator(Service service)
  {
    this.service = service;
    definition = service.getEnclosingDefinition();
  }
  
  public void setAddressLocation(String location) {
    addressLocation = location;
  }

  /**
   * Generates a service port given it's name and binding name.
   * The port is then added to the service passed in the constructor.
   * @return the Port instance
   */
  public Port generatePort()
  {
    String portName = getName();
    String bindingName = getRefName();

    Binding binding = getBinding(bindingName);

    Port port = WSDLFactory.eINSTANCE.createPort();
    port.setName(portName);
    port.setEnclosingDefinition(definition);
    port.setBinding(binding);

    service.addPort(port);

    ContentGenerator contentGenerator = getContentGenerator();

    if (contentGenerator != null)
    {
      //wtp bug 221024 - namespace and prefixes are not added when add new port
      addRequiredNamespaces(service.getEnclosingDefinition());
      contentGenerator.setAddressLocation(addressLocation);
      contentGenerator.generatePortContent(port);
    }

    return port;
  }

  /*
   * methods addRequiredNamespaces() and computeUniquePrefix() are used to add necessary
   * namespaces 
   * 
   * TODO:
   * Does this belong here?  This is copied from wsdl.ui.  Can we sync up in some way?
   */
  protected void addRequiredNamespaces(Definition definition)
  {
    if (contentGenerator != null)
    {
      String[] namespaceNames = contentGenerator.getRequiredNamespaces();
      String[] preferredPrefixes = new String [namespaceNames.length];
      for (int index = 0; index < namespaceNames.length; index++)
      {
        preferredPrefixes[index] = contentGenerator.getPreferredNamespacePrefix(namespaceNames[index]);
      }

      Map map = definition.getNamespaces();

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
          definition.addNamespace(prefix, namespace);
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

  /**
   * Locates a binding given its prefixed name (someNSPrefix:someBindingName).
   * 
   * @param bindingName the qname of the desired binding.
   * @return a Binding with the given name, or null if one is not found.
   */
  private Binding getBinding(String bindingName)
  {
    List allBindings = getAllBindings();
    Iterator bindingIterator = allBindings.iterator();

    while (bindingIterator.hasNext())
    {
      Binding binding = (Binding)bindingIterator.next();

      List prefixedNames = getPrefixedNames(binding);

      if (prefixedNames.contains(bindingName))
      {
        return binding;
      }
    }

    return null;
  }

  /**
   * Locates all the bindings available in this definition and all its
   * imported definitions.
   * 
   * @return a List with all available bindings.
   */
  public List getAllBindings()
  {
    List allBindings = new ArrayList();
    List allDefinitions = getAllDefinitions();

    Iterator defsIterator = allDefinitions.iterator();

    while (defsIterator.hasNext())
    {
      Definition definition = (Definition)defsIterator.next();
      Map bindings = definition.getBindings();

      // ISSUE A possible problem here if somehow a binding is duplicated in one of the imports?
      // In that case we should iterate over and check them one by one.

      allBindings.addAll(bindings.values());
    }

    return allBindings;
  }

  /**
   * Attempts to locate and add to the list all the definitions imported by the
   * definition passed in. Recursively tries to locate all definitions.
   * 
   * @param definition the start Definition. Must not be null.
   * @param list used to collect all the imported definitions. Must not be null.
   */
  private void addDefinition(Definition definition, List list)
  {
    list.add(definition);
    List imports = definition.getEImports();
    Iterator importsIterator = imports.iterator();

    while (importsIterator.hasNext())
    {
      Import theImport = (Import)importsIterator.next();

      String importLocationURI = theImport.getLocationURI();
      if (importLocationURI != null &&
      // ISSUE This assumption seems a bit weak.
        importLocationURI.endsWith("wsdl"))
      {
        Definition importedDefinition = theImport.getEDefinition();

        if (importedDefinition != null && !list.contains(importedDefinition))
        {
          // Recursively try to locate all definitions.

          addDefinition(importedDefinition, list);
        }
      }
    }
  }

  /**
   * Attempts to locate all definitions imported by the current definition and
   * all their imports in turn. 
   * 
   * @return a List with all definitions imported by the current definition.
   */
  private List getAllDefinitions()
  {
    List list = new ArrayList();
    addDefinition(definition, list);
    return list;
  }

  private List getPrefixedNames(Binding binding)
  {
    List prefixedNames = new ArrayList();
    String currentBindingName = binding.getQName().getLocalPart();
    String currentNamespace = binding.getQName().getNamespaceURI();

    Map namespaceMap = definition.getNamespaces();
    Iterator keys = namespaceMap.keySet().iterator();
    while (keys.hasNext())
    {
      Object key = keys.next();
      Object value = namespaceMap.get(key);

      if (currentNamespace.equals(value))
      {
        // Found a match.  Add to our list
        prefixedNames.add(key + ":" + currentBindingName); //$NON-NLS-1$
      }
    }

    return prefixedNames;
  }

  public Service getService()
  {
    return service;
  }
}
