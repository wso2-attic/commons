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
package org.eclipse.wst.wsdl.binding.soap.internal.generator;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.internal.generator.ContentGenerator;


public class SOAPContentGenerator implements ContentGenerator
{
  public static final int OPTION_NOT_SET = -1;

  public static final int STYLE_DOCUMENT = 1;

  public static final int STYLE_RPC = 2;

  public static final int USE_LITERAL = 1;

  public static final int USE_ENCODED = 2;

  private int styleOption = OPTION_NOT_SET;

  private int useOption = OPTION_NOT_SET;

  protected String namespaceValue = "";

  protected String addressLocation = ContentGenerator.ADDRESS_LOCATION;

  protected final static String[] requiredNamespaces = { "http://schemas.xmlsoap.org/wsdl/soap/" };

  protected final static String[] preferredNamespacePrefixes = { "soap" };

  public String[] getRequiredNamespaces()
  {
    return requiredNamespaces;
  }

  public String getPreferredNamespacePrefix(String namespace)
  {
    if (namespace.equals("http://schemas.xmlsoap.org/wsdl/soap/"))
    {
      return "soap";
    }

    return "";
  }

  public void setStyle(int style)
  {
    styleOption = style;
  }

  public void setUse(int use)
  {
    useOption = use;
  }

  public void setAddressLocation(String addressLocation)
  {
    this.addressLocation = addressLocation;
  }

  public void generatePortContent(Port port)
  {
    // We blow away any existing ExtensibilityElements/content before we generate it
    // Is it worth being smarter?  Look for matching content first and create those which aren't found????
    List removeList = new ArrayList(port.getEExtensibilityElements());
    removeExtensebilityElements(port.getEExtensibilityElements(), removeList);

    SOAPAddress soapAddress = SOAPFactory.eINSTANCE.createSOAPAddress();
    soapAddress.setLocationURI(addressLocation);
    port.addExtensibilityElement(soapAddress);
  }

  public void generateBindingContent(Binding binding, PortType portType)
  {
    // We blow away any existing ExtensibilityElements/content before we generate it
    // Is it worth being smarter?  Look for matching content first and create those which aren't found????
    List removeList = new ArrayList(binding.getEExtensibilityElements());
    removeExtensebilityElements(binding.getEExtensibilityElements(), removeList);

    SOAPFactory soapFactory = SOAPFactory.eINSTANCE;
    SOAPBinding soapBinding = soapFactory.createSOAPBinding();
    soapBinding.setStyle((getStyleOption(binding) == STYLE_DOCUMENT) ? "document" : "rpc");
    soapBinding.setTransportURI("http://schemas.xmlsoap.org/soap/http");

    binding.addExtensibilityElement(soapBinding);
  }

  public void generateBindingOperationContent(BindingOperation bindingOperation, Operation operation)
  {
    // We blow away any existing ExtensibilityElements/content before we generate it
    // Is it worth being smarter?  Look for matching content first and create those which aren't found????
    List removeList = new ArrayList(bindingOperation.getEExtensibilityElements());
    removeExtensebilityElements(bindingOperation.getEExtensibilityElements(), removeList);

    SOAPOperation soapOperation = SOAPFactory.eINSTANCE.createSOAPOperation();

    String soapActionValue = getNamespace(bindingOperation);
    if (!soapActionValue.endsWith("/"))
    {
      soapActionValue += "/";
    }
    soapActionValue += operation.getName();

    soapOperation.setSoapActionURI(soapActionValue);

    bindingOperation.addExtensibilityElement(soapOperation);
  }

  public void generateBindingInputContent(BindingInput bindingInput, Input input)
  {
    // We blow away any existing ExtensibilityElements/content before we generate it
    // Is it worth being smarter?  Look for matching content first and create those which aren't found????
    List removeList = new ArrayList(bindingInput.getEExtensibilityElements());
    removeExtensebilityElements(bindingInput.getEExtensibilityElements(), removeList);

    SOAPFactory soapFactory = SOAPFactory.eINSTANCE;
    SOAPBody soapBody = soapFactory.createSOAPBody();
    soapBody.setUse((getUseOption(null) == USE_ENCODED) ? "encoded" : "literal");
    if (getUseOption(bindingInput) == USE_ENCODED && getStyleOption(bindingInput) == STYLE_RPC)
    {
      List encodingList = new BasicEList();
      encodingList.add("http://schemas.xmlsoap.org/soap/encoding/");
      soapBody.setEncodingStyles(encodingList);
      soapBody.setNamespaceURI(getNamespace(bindingInput));
    }
    else if (getUseOption(bindingInput) == USE_LITERAL && getStyleOption(bindingInput) == STYLE_RPC)
    {
      soapBody.setNamespaceURI(getNamespace(bindingInput));
    }

    bindingInput.addExtensibilityElement(soapBody);
  }

  public void generateBindingOutputContent(BindingOutput bindingOutput, Output output)
  {
    // We blow away any existing ExtensibilityElements/content before we generate it
    // Is it worth being smarter?  Look for matching content first and create those which aren't found????
    List removeList = new ArrayList(bindingOutput.getEExtensibilityElements());
    removeExtensebilityElements(bindingOutput.getEExtensibilityElements(), removeList);

    SOAPFactory soapFactory = SOAPFactory.eINSTANCE;
    SOAPBody soapBody = soapFactory.createSOAPBody();
    soapBody.setUse((getUseOption(bindingOutput) == USE_ENCODED) ? "encoded" : "literal");
    if (getUseOption(bindingOutput) == USE_ENCODED && getStyleOption(bindingOutput) == STYLE_RPC)
    {
      List encodingList = new BasicEList();
      encodingList.add("http://schemas.xmlsoap.org/soap/encoding/");
      soapBody.setEncodingStyles(encodingList);
      soapBody.setNamespaceURI(getNamespace(bindingOutput));
    }
    else if (getUseOption(bindingOutput) == USE_LITERAL && getStyleOption(bindingOutput) == STYLE_RPC)
    {
      soapBody.setNamespaceURI(getNamespace(bindingOutput));
    }

    bindingOutput.addExtensibilityElement(soapBody);
  }

  public void generateBindingFaultContent(BindingFault bindingFault, Fault fault)
  {
    // We blow away any existing ExtensibilityElements/content before we generate it
    // Is it worth being smarter?  Look for matching content first and create those which aren't found????
    List removeList = new ArrayList(bindingFault.getEExtensibilityElements());
    removeExtensebilityElements(bindingFault.getEExtensibilityElements(), removeList);

    SOAPFactory soapFactory = SOAPFactory.eINSTANCE;
    SOAPFault soapFault = soapFactory.createSOAPFault();
    soapFault.setUse((getUseOption(bindingFault) == USE_ENCODED) ? "encoded" : "literal");
    soapFault.setName(fault.getName());
    //	soapFault.setNamespaceURI(getNamespace(bindingFault));

    if (getUseOption(bindingFault) == USE_ENCODED && getStyleOption(bindingFault) == STYLE_RPC)
    {
      List encodingList = new BasicEList();
      encodingList.add("http://schemas.xmlsoap.org/soap/encoding/");
      soapFault.setEncodingStyles(encodingList);
    }

    bindingFault.addExtensibilityElement(soapFault);
  }

  private String getNamespace(WSDLElement wsdlElement)
  {
    if (namespaceValue.equals("") && wsdlElement != null)
    {
      namespaceValue = wsdlElement.getEnclosingDefinition().getTargetNamespace();
    }
    if (namespaceValue == null)
    {
      namespaceValue = "";
    }

    return namespaceValue;
  }

  private void removeExtensebilityElements(List originalList, List removeList)
  {
    Iterator removeIt = removeList.iterator();
    while (removeIt.hasNext())
    {
      originalList.remove(removeIt.next());
    }
  }

  private Binding getBindingObject(Object genericBindingObject)
  {
    Object parent = genericBindingObject;

    int index = 0;
    while (parent != null && index < 5)
    {
      parent = getGenericBindingObjectParent(parent);
      if (parent instanceof Binding)
      {
        break;
      }
      index++;
    }

    return (parent instanceof Binding) ? (Binding)parent : null;
  }

  private Object getGenericBindingObjectParent(Object genericBindingObject)
  {
    Object parent = null;

    if (genericBindingObject != null)
    {
      if (genericBindingObject instanceof BindingOperation)
      {
        parent = ((BindingOperation)genericBindingObject).getContainer();
      }
      else if (genericBindingObject instanceof BindingInput)
      {
        parent = ((BindingInput)genericBindingObject).getContainer();
      }
      else if (genericBindingObject instanceof BindingOutput)
      {
        parent = ((BindingOutput)genericBindingObject).getContainer();
      }
      else if (genericBindingObject instanceof BindingFault)
      {
        parent = ((BindingFault)genericBindingObject).getContainer();
      }
    }

    return parent;
  }

  private int getStyleOption(Object genericBindingObject)
  {
    if (styleOption == OPTION_NOT_SET && genericBindingObject != null)
    {
      // init() was never called, try to determine the 'style' based on what we have/know
      Binding binding = getBindingObject(genericBindingObject);

      // Try to determine the style from the Binding Object
      if (binding != null)
      {
        List list = binding.getEExtensibilityElements();
        Iterator valuesIt = getExtensibilityElementAttributeValue(list, "style").iterator();

        while (valuesIt.hasNext())
        {
          String style = (String)valuesIt.next();

          if (style.equals("document"))
          {
            styleOption = STYLE_DOCUMENT;
          }
          else if (style.equals("rpc"))
          {
            styleOption = STYLE_RPC;
          }

          if (styleOption != OPTION_NOT_SET)
          {
            break;
          }
        }
      }
    }

    if (styleOption == OPTION_NOT_SET)
    {
      styleOption = STYLE_DOCUMENT;
    }

    return styleOption;
  }

  private int getUseOption(Object genericBindingObject)
  {
    if (useOption == OPTION_NOT_SET)
    {
      // init() was never called, try to determine the 'use' based on what we have/know
      Iterator messageRefIt = getMessageReferenceBindingObjects(genericBindingObject).iterator();

      // Try to determine the use from the list of BindingInputs, BindingOutputs, and BindingFaults
      while (messageRefIt.hasNext())
      {
        Object messageRef = messageRefIt.next();
        List values = new ArrayList();

        if (messageRef instanceof BindingInput)
        {
          List list = ((BindingInput)messageRef).getEExtensibilityElements();
          values = getExtensibilityElementAttributeValue(list, "use");
        }
        else if (messageRef instanceof BindingOutput)
        {
          List list = ((BindingOutput)messageRef).getEExtensibilityElements();
          values = getExtensibilityElementAttributeValue(list, "use");
        }
        else if (messageRef instanceof BindingFault)
        {
          List list = ((BindingFault)messageRef).getEExtensibilityElements();
          values = getExtensibilityElementAttributeValue(list, "use");
        }

        Iterator valuesIt = values.iterator();
        while (valuesIt.hasNext())
        {
          String use = (String)valuesIt.next();

          if (use.equals("literal"))
          {
            useOption = USE_LITERAL;
          }
          else if (use.equals("encoded"))
          {
            useOption = USE_ENCODED;
          }
        }

        if (useOption != OPTION_NOT_SET)
        {
          break;
        }
      }
    }

    if (useOption == OPTION_NOT_SET)
    {
      useOption = USE_LITERAL;
    }

    return useOption;
  }

  private List getMessageReferenceBindingObjects(Object genericBindingObject)
  {
    List list = new ArrayList();
    Binding binding = getBindingObject(genericBindingObject);

    if (binding != null)
    {
      Iterator operationsIt = binding.getEBindingOperations().iterator();

      while (operationsIt.hasNext())
      {
        BindingOperation op = (BindingOperation)operationsIt.next();
        list.add(op.getEBindingInput());
        list.add(op.getEBindingOutput());
        list.addAll(op.getEBindingFaults());
      }
    }

    return list;
  }

  private List getExtensibilityElementAttributeValue(List eeList, String attributeKey)
  {
    List values = new ArrayList();
    Iterator eeElementsIt = eeList.iterator();

    while (eeElementsIt.hasNext())
    {
      ExtensibilityElement eeElement = (ExtensibilityElement)eeElementsIt.next();
      String attributeValue = eeElement.getElement().getAttribute(attributeKey);
      if (attributeValue != null && !attributeValue.equals(""))
      {
        values.add(attributeValue);
      }
    }

    return values;
  }

  public String getProtocol()
  {
    return "SOAP";
  }
}
