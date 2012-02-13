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
package org.eclipse.wst.wsdl.internal.impl;


import javax.wsdl.OperationType;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Namespace;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WSDLFactoryImpl extends EFactoryImpl implements WSDLFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static WSDLFactory init()
  {
    try
    {
      WSDLFactory theWSDLFactory = (WSDLFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/wsdl/2003/WSDL"); //$NON-NLS-1$ 
      if (theWSDLFactory != null)
      {
        return theWSDLFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new WSDLFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WSDLFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case WSDLPackage.PORT_TYPE:
      return createPortType();
      case WSDLPackage.OPERATION:
      return createOperation();
      case WSDLPackage.MESSAGE:
      return createMessage();
      case WSDLPackage.PART:
      return createPart();
      case WSDLPackage.BINDING:
      return createBinding();
      case WSDLPackage.BINDING_OPERATION:
      return createBindingOperation();
      case WSDLPackage.SERVICE:
      return createService();
      case WSDLPackage.PORT:
      return createPort();
      case WSDLPackage.EXTENSIBILITY_ELEMENT:
      return createExtensibilityElement();
      case WSDLPackage.DEFINITION:
      return createDefinition();
      case WSDLPackage.IMPORT:
      return createImport();
      case WSDLPackage.INPUT:
      return createInput();
      case WSDLPackage.OUTPUT:
      return createOutput();
      case WSDLPackage.FAULT:
      return createFault();
      case WSDLPackage.BINDING_INPUT:
      return createBindingInput();
      case WSDLPackage.BINDING_OUTPUT:
      return createBindingOutput();
      case WSDLPackage.BINDING_FAULT:
      return createBindingFault();
      case WSDLPackage.NAMESPACE:
      return createNamespace();
      case WSDLPackage.TYPES:
      return createTypes();
      case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT:
      return createUnknownExtensibilityElement();
      case WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT:
      return createXSDSchemaExtensibilityElement();
      default:
      throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case WSDLPackage.QNAME:
      return createQNameFromString(eDataType, initialValue);
      case WSDLPackage.OPERATION_TYPE:
      return createOperationTypeFromString(eDataType, initialValue);
      case WSDLPackage.DOM_ELEMENT:
      return createDOMElementFromString(eDataType, initialValue);
      case WSDLPackage.WSDL_EXCEPTION:
      return createWSDLExceptionFromString(eDataType, initialValue);
      case WSDLPackage.DOM_DOCUMENT:
      return createDOMDocumentFromString(eDataType, initialValue);
      default:
      throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case WSDLPackage.QNAME:
      return convertQNameToString(eDataType, instanceValue);
      case WSDLPackage.OPERATION_TYPE:
      return convertOperationTypeToString(eDataType, instanceValue);
      case WSDLPackage.DOM_ELEMENT:
      return convertDOMElementToString(eDataType, instanceValue);
      case WSDLPackage.WSDL_EXCEPTION:
      return convertWSDLExceptionToString(eDataType, instanceValue);
      case WSDLPackage.DOM_DOCUMENT:
      return convertDOMDocumentToString(eDataType, instanceValue);
      default:
      throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PortType createPortType()
  {
    PortTypeImpl portType = new PortTypeImpl();
    return portType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operation createOperation()
  {
    OperationImpl operation = new OperationImpl();
    return operation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message createMessage()
  {
    MessageImpl message = new MessageImpl();
    return message;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Part createPart()
  {
    PartImpl part = new PartImpl();
    return part;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Binding createBinding()
  {
    BindingImpl binding = new BindingImpl();
    return binding;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BindingOperation createBindingOperation()
  {
    BindingOperationImpl bindingOperation = new BindingOperationImpl();
    return bindingOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Service createService()
  {
    ServiceImpl service = new ServiceImpl();
    return service;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Port createPort()
  {
    PortImpl port = new PortImpl();
    return port;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExtensibilityElement createExtensibilityElement()
  {
    ExtensibilityElementImpl extensibilityElement = new ExtensibilityElementImpl();
    return extensibilityElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition createDefinition()
  {
    DefinitionImpl definition = new DefinitionImpl();
    return definition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Import createImport()
  {
    ImportImpl import_ = new ImportImpl();
    return import_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Input createInput()
  {
    InputImpl input = new InputImpl();
    return input;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Output createOutput()
  {
    OutputImpl output = new OutputImpl();
    return output;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Fault createFault()
  {
    FaultImpl fault = new FaultImpl();
    return fault;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BindingInput createBindingInput()
  {
    BindingInputImpl bindingInput = new BindingInputImpl();
    return bindingInput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BindingOutput createBindingOutput()
  {
    BindingOutputImpl bindingOutput = new BindingOutputImpl();
    return bindingOutput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BindingFault createBindingFault()
  {
    BindingFaultImpl bindingFault = new BindingFaultImpl();
    return bindingFault;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Namespace createNamespace()
  {
    NamespaceImpl namespace = new NamespaceImpl();
    return namespace;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Types createTypes()
  {
    TypesImpl types = new TypesImpl();
    return types;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public UnknownExtensibilityElement createUnknownExtensibilityElement()
  {
    UnknownExtensibilityElementImpl unknownExtensibilityElement = new UnknownExtensibilityElementImpl();
    return unknownExtensibilityElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XSDSchemaExtensibilityElement createXSDSchemaExtensibilityElement()
  {
    XSDSchemaExtensibilityElementImpl xsdSchemaExtensibilityElement = new XSDSchemaExtensibilityElementImpl();
    return xsdSchemaExtensibilityElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QName createQNameFromString(EDataType eDataType, String initialValue)
  {
    return (QName)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertQNameToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public OperationType createOperationTypeFromString(EDataType eDataType, String initialValue)
  {
    if ("REQUEST_RESPONSE".equals(initialValue))
    {
      return OperationType.REQUEST_RESPONSE;
    }
    else if ("ONE_WAY".equals(initialValue))
    {
      return OperationType.ONE_WAY;
    }
    else if ("NOTIFICATION".equals(initialValue))
    {
      return OperationType.NOTIFICATION;
    }
    else if ("SOLICIT_RESPONSE".equals(initialValue))
    {
      return OperationType.SOLICIT_RESPONSE;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertOperationTypeToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Element createDOMElementFromString(EDataType eDataType, String initialValue)
  {
    return (Element)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDOMElementToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WSDLException createWSDLExceptionFromString(EDataType eDataType, String initialValue)
  {
    return (WSDLException)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertWSDLExceptionToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Document createDOMDocumentFromString(EDataType eDataType, String initialValue)
  {
    return (Document)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDOMDocumentToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WSDLPackage getWSDLPackage()
  {
    return (WSDLPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  public static WSDLPackage getPackage()
  {
    return WSDLPackage.eINSTANCE;
  }

  //
  //
  //
  public ExtensibilityElement createExtensibilityElement(String namespace, String localName)
  {
    ExtensibilityElementFactory factory = WSDLPlugin.INSTANCE.getExtensibilityElementFactory(namespace);
    if (factory != null)
      return factory.createExtensibilityElement(namespace, localName);
    else
    {
      ExtensibilityElement extensibilityElement = createUnknownExtensibilityElement();
      // Provide some information about the extensibility element.

      QName elementType = new QName(namespace, localName);
      extensibilityElement.setElementType(elementType);
      return extensibilityElement;
    }
  }

  /*
   public void setAlwaysUseUnknownExtensibilityElement(boolean value)
   {
   System.out.println("Deprecated: WSDLFactoryImpl.setUseExtensionFactories");
   }
   */
} //WSDLFactoryImpl
