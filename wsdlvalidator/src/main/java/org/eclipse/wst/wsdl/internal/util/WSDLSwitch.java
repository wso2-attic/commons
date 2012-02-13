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
package org.eclipse.wst.wsdl.internal.util;


import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.extensions.AttributeExtensible;
import javax.wsdl.extensions.ElementExtensible;
import javax.wsdl.extensions.ExtensionRegistry;

import javax.wsdl.extensions.schema.Schema;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.*;


/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.WSDLPackage
 * @generated
 */
public class WSDLSwitch
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static WSDLPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WSDLSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = WSDLPackage.eINSTANCE;
    }
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  public Object doSwitch(EObject theEObject)
  {
    return doSwitch(theEObject.eClass(), theEObject);
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  protected Object doSwitch(EClass theEClass, EObject theEObject)
  {
    if (theEClass.eContainer() == modelPackage)
    {
      return doSwitch(theEClass.getClassifierID(), theEObject);
    }
    else
    {
      List eSuperTypes = theEClass.getESuperTypes();
      return eSuperTypes.isEmpty() ? defaultCase(theEObject) : doSwitch((EClass)eSuperTypes.get(0), theEObject);
    }
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  protected Object doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case WSDLPackage.WSDL_ELEMENT:
      {
        WSDLElement wsdlElement = (WSDLElement)theEObject;
        Object result = caseWSDLElement(wsdlElement);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.PORT_TYPE:
      {
        PortType portType = (PortType)theEObject;
        Object result = casePortType(portType);
        if (result == null)
          result = caseExtensibleElement(portType);
        if (result == null)
          result = caseIPortType(portType);
        if (result == null)
          result = caseWSDLElement(portType);
        if (result == null)
          result = caseIElementExtensible(portType);
        if (result == null)
          result = caseIAttributeExtensible(portType);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.OPERATION:
      {
        Operation operation = (Operation)theEObject;
        Object result = caseOperation(operation);
        if (result == null)
          result = caseExtensibleElement(operation);
        if (result == null)
          result = caseIOperation(operation);
        if (result == null)
          result = caseWSDLElement(operation);
        if (result == null)
          result = caseIElementExtensible(operation);
        if (result == null)
          result = caseIAttributeExtensible(operation);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.MESSAGE:
      {
        Message message = (Message)theEObject;
        Object result = caseMessage(message);
        if (result == null)
          result = caseExtensibleElement(message);
        if (result == null)
          result = caseIMessage(message);
        if (result == null)
          result = caseWSDLElement(message);
        if (result == null)
          result = caseIElementExtensible(message);
        if (result == null)
          result = caseIAttributeExtensible(message);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.PART:
      {
        Part part = (Part)theEObject;
        Object result = casePart(part);
        if (result == null)
          result = caseExtensibleElement(part);
        if (result == null)
          result = caseIPart(part);
        if (result == null)
          result = caseWSDLElement(part);
        if (result == null)
          result = caseIElementExtensible(part);
        if (result == null)
          result = caseIAttributeExtensible(part);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.BINDING:
      {
        Binding binding = (Binding)theEObject;
        Object result = caseBinding(binding);
        if (result == null)
          result = caseExtensibleElement(binding);
        if (result == null)
          result = caseIBinding(binding);
        if (result == null)
          result = caseWSDLElement(binding);
        if (result == null)
          result = caseIElementExtensible(binding);
        if (result == null)
          result = caseIAttributeExtensible(binding);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.BINDING_OPERATION:
      {
        BindingOperation bindingOperation = (BindingOperation)theEObject;
        Object result = caseBindingOperation(bindingOperation);
        if (result == null)
          result = caseExtensibleElement(bindingOperation);
        if (result == null)
          result = caseIBindingOperation(bindingOperation);
        if (result == null)
          result = caseWSDLElement(bindingOperation);
        if (result == null)
          result = caseIElementExtensible(bindingOperation);
        if (result == null)
          result = caseIAttributeExtensible(bindingOperation);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.SERVICE:
      {
        Service service = (Service)theEObject;
        Object result = caseService(service);
        if (result == null)
          result = caseExtensibleElement(service);
        if (result == null)
          result = caseIService(service);
        if (result == null)
          result = caseWSDLElement(service);
        if (result == null)
          result = caseIElementExtensible(service);
        if (result == null)
          result = caseIAttributeExtensible(service);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.PORT:
      {
        Port port = (Port)theEObject;
        Object result = casePort(port);
        if (result == null)
          result = caseExtensibleElement(port);
        if (result == null)
          result = caseIPort(port);
        if (result == null)
          result = caseWSDLElement(port);
        if (result == null)
          result = caseIElementExtensible(port);
        if (result == null)
          result = caseIAttributeExtensible(port);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.EXTENSIBILITY_ELEMENT:
      {
        ExtensibilityElement extensibilityElement = (ExtensibilityElement)theEObject;
        Object result = caseExtensibilityElement(extensibilityElement);
        if (result == null)
          result = caseWSDLElement(extensibilityElement);
        if (result == null)
          result = caseIExtensibilityElement(extensibilityElement);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.DEFINITION:
      {
        Definition definition = (Definition)theEObject;
        Object result = caseDefinition(definition);
        if (result == null)
          result = caseExtensibleElement(definition);
        if (result == null)
          result = caseIDefinition(definition);
        if (result == null)
          result = caseWSDLElement(definition);
        if (result == null)
          result = caseIElementExtensible(definition);
        if (result == null)
          result = caseIAttributeExtensible(definition);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.IMPORT:
      {
        Import import_ = (Import)theEObject;
        Object result = caseImport(import_);
        if (result == null)
          result = caseExtensibleElement(import_);
        if (result == null)
          result = caseIImport(import_);
        if (result == null)
          result = caseWSDLElement(import_);
        if (result == null)
          result = caseIElementExtensible(import_);
        if (result == null)
          result = caseIAttributeExtensible(import_);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.EXTENSIBLE_ELEMENT:
      {
        ExtensibleElement extensibleElement = (ExtensibleElement)theEObject;
        Object result = caseExtensibleElement(extensibleElement);
        if (result == null)
          result = caseWSDLElement(extensibleElement);
        if (result == null)
          result = caseIElementExtensible(extensibleElement);
        if (result == null)
          result = caseIAttributeExtensible(extensibleElement);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.INPUT:
      {
        Input input = (Input)theEObject;
        Object result = caseInput(input);
        if (result == null)
          result = caseMessageReference(input);
        if (result == null)
          result = caseIInput(input);
        if (result == null)
          result = caseExtensibleElement(input);
        if (result == null)
          result = caseIAttributeExtensible(input);
        if (result == null)
          result = caseWSDLElement(input);
        if (result == null)
          result = caseIElementExtensible(input);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.OUTPUT:
      {
        Output output = (Output)theEObject;
        Object result = caseOutput(output);
        if (result == null)
          result = caseMessageReference(output);
        if (result == null)
          result = caseIOutput(output);
        if (result == null)
          result = caseExtensibleElement(output);
        if (result == null)
          result = caseIAttributeExtensible(output);
        if (result == null)
          result = caseWSDLElement(output);
        if (result == null)
          result = caseIElementExtensible(output);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.FAULT:
      {
        Fault fault = (Fault)theEObject;
        Object result = caseFault(fault);
        if (result == null)
          result = caseMessageReference(fault);
        if (result == null)
          result = caseIFault(fault);
        if (result == null)
          result = caseExtensibleElement(fault);
        if (result == null)
          result = caseIAttributeExtensible(fault);
        if (result == null)
          result = caseWSDLElement(fault);
        if (result == null)
          result = caseIElementExtensible(fault);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.BINDING_INPUT:
      {
        BindingInput bindingInput = (BindingInput)theEObject;
        Object result = caseBindingInput(bindingInput);
        if (result == null)
          result = caseExtensibleElement(bindingInput);
        if (result == null)
          result = caseIBindingInput(bindingInput);
        if (result == null)
          result = caseWSDLElement(bindingInput);
        if (result == null)
          result = caseIElementExtensible(bindingInput);
        if (result == null)
          result = caseIAttributeExtensible(bindingInput);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.BINDING_OUTPUT:
      {
        BindingOutput bindingOutput = (BindingOutput)theEObject;
        Object result = caseBindingOutput(bindingOutput);
        if (result == null)
          result = caseExtensibleElement(bindingOutput);
        if (result == null)
          result = caseIBindingOutput(bindingOutput);
        if (result == null)
          result = caseWSDLElement(bindingOutput);
        if (result == null)
          result = caseIElementExtensible(bindingOutput);
        if (result == null)
          result = caseIAttributeExtensible(bindingOutput);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.BINDING_FAULT:
      {
        BindingFault bindingFault = (BindingFault)theEObject;
        Object result = caseBindingFault(bindingFault);
        if (result == null)
          result = caseExtensibleElement(bindingFault);
        if (result == null)
          result = caseIBindingFault(bindingFault);
        if (result == null)
          result = caseWSDLElement(bindingFault);
        if (result == null)
          result = caseIElementExtensible(bindingFault);
        if (result == null)
          result = caseIAttributeExtensible(bindingFault);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.NAMESPACE:
      {
        Namespace namespace = (Namespace)theEObject;
        Object result = caseNamespace(namespace);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.TYPES:
      {
        Types types = (Types)theEObject;
        Object result = caseTypes(types);
        if (result == null)
          result = caseExtensibleElement(types);
        if (result == null)
          result = caseITypes(types);
        if (result == null)
          result = caseWSDLElement(types);
        if (result == null)
          result = caseIElementExtensible(types);
        if (result == null)
          result = caseIAttributeExtensible(types);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT:
      {
        UnknownExtensibilityElement unknownExtensibilityElement = (UnknownExtensibilityElement)theEObject;
        Object result = caseUnknownExtensibilityElement(unknownExtensibilityElement);
        if (result == null)
          result = caseExtensibilityElement(unknownExtensibilityElement);
        if (result == null)
          result = caseWSDLElement(unknownExtensibilityElement);
        if (result == null)
          result = caseIExtensibilityElement(unknownExtensibilityElement);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT:
      {
        XSDSchemaExtensibilityElement xsdSchemaExtensibilityElement = (XSDSchemaExtensibilityElement)theEObject;
        Object result = caseXSDSchemaExtensibilityElement(xsdSchemaExtensibilityElement);
        if (result == null)
          result = caseExtensibilityElement(xsdSchemaExtensibilityElement);
        if (result == null)
          result = caseISchema(xsdSchemaExtensibilityElement);
        if (result == null)
          result = caseWSDLElement(xsdSchemaExtensibilityElement);
        if (result == null)
          result = caseIExtensibilityElement(xsdSchemaExtensibilityElement);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case WSDLPackage.MESSAGE_REFERENCE:
      {
        MessageReference messageReference = (MessageReference)theEObject;
        Object result = caseMessageReference(messageReference);
        if (result == null)
          result = caseExtensibleElement(messageReference);
        if (result == null)
          result = caseWSDLElement(messageReference);
        if (result == null)
          result = caseIElementExtensible(messageReference);
        if (result == null)
          result = caseIAttributeExtensible(messageReference);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      default:
      return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseWSDLElement(WSDLElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Port Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Port Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object casePortType(PortType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseOperation(Operation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Message</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Message</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseMessage(Message object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Part</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Part</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object casePart(Part object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Binding</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Binding</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseBinding(Binding object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Binding Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Binding Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseBindingOperation(BindingOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Service</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Service</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseService(Service object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Port</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Port</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object casePort(Port object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Extensibility Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Extensibility Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseExtensibilityElement(ExtensibilityElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Definition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Definition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseDefinition(Definition object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Import</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Import</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseImport(Import object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Extensible Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Extensible Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseExtensibleElement(ExtensibleElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Input</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Input</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseInput(Input object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Output</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Output</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseOutput(Output object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Fault</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Fault</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseFault(Fault object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Binding Input</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Binding Input</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseBindingInput(BindingInput object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Binding Output</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Binding Output</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseBindingOutput(BindingOutput object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Binding Fault</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Binding Fault</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseBindingFault(BindingFault object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Namespace</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Namespace</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseNamespace(Namespace object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IPort Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IPort Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIPortType(javax.wsdl.PortType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IOperation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IOperation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIOperation(javax.wsdl.Operation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IInput</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IInput</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIInput(javax.wsdl.Input object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IOutput</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IOutput</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIOutput(javax.wsdl.Output object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IFault</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IFault</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIFault(javax.wsdl.Fault object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IMessage</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IMessage</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIMessage(javax.wsdl.Message object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IPart</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IPart</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIPart(javax.wsdl.Part object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IService</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IService</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIService(javax.wsdl.Service object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IPort</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IPort</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIPort(javax.wsdl.Port object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IBinding</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IBinding</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIBinding(javax.wsdl.Binding object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IBinding Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IBinding Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIBindingOperation(javax.wsdl.BindingOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IBinding Input</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IBinding Input</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIBindingInput(javax.wsdl.BindingInput object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IBinding Output</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IBinding Output</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIBindingOutput(javax.wsdl.BindingOutput object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IBinding Fault</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IBinding Fault</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIBindingFault(javax.wsdl.BindingFault object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IExtensibility Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IExtensibility Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIExtensibilityElement(javax.wsdl.extensions.ExtensibilityElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IDefinition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IDefinition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIDefinition(javax.wsdl.Definition object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IImport</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IImport</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIImport(javax.wsdl.Import object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IList</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IList</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIList(List object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IMap</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IMap</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIMap(Map object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IURL</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IURL</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIURL(URL object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IExtension Registry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IExtension Registry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIExtensionRegistry(ExtensionRegistry object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Types</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Types</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseTypes(Types object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IIterator</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IIterator</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIIterator(Iterator object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ITypes</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ITypes</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseITypes(javax.wsdl.Types object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Unknown Extensibility Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Unknown Extensibility Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseUnknownExtensibilityElement(UnknownExtensibilityElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>XSD Schema Extensibility Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>XSD Schema Extensibility Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseXSDSchemaExtensibilityElement(XSDSchemaExtensibilityElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Message Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Message Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseMessageReference(MessageReference object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IElement Extensible</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IElement Extensible</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIElementExtensible(ElementExtensible object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IAttribute Extensible</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IAttribute Extensible</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIAttributeExtensible(AttributeExtensible object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIObject(Object object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ISchema</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ISchema</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseISchema(Schema object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  public Object defaultCase(EObject object)
  {
    return null;
  }

} //WSDLSwitch
