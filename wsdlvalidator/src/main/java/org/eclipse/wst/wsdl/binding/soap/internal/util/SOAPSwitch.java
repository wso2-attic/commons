/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.util;


import java.util.List;

import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.binding.soap.*;


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
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage
 * @generated
 */
public class SOAPSwitch
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static SOAPPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = SOAPPackage.eINSTANCE;
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
      case SOAPPackage.SOAP_BINDING:
      {
        SOAPBinding soapBinding = (SOAPBinding)theEObject;
        Object result = caseSOAPBinding(soapBinding);
        if (result == null)
          result = caseExtensibilityElement(soapBinding);
        if (result == null)
          result = caseISOAPBinding(soapBinding);
        if (result == null)
          result = caseWSDLElement(soapBinding);
        if (result == null)
          result = caseIExtensibilityElement(soapBinding);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case SOAPPackage.SOAP_BODY:
      {
        SOAPBody soapBody = (SOAPBody)theEObject;
        Object result = caseSOAPBody(soapBody);
        if (result == null)
          result = caseExtensibilityElement(soapBody);
        if (result == null)
          result = caseISOAPBody(soapBody);
        if (result == null)
          result = caseWSDLElement(soapBody);
        if (result == null)
          result = caseIExtensibilityElement(soapBody);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case SOAPPackage.SOAP_HEADER_BASE:
      {
        SOAPHeaderBase soapHeaderBase = (SOAPHeaderBase)theEObject;
        Object result = caseSOAPHeaderBase(soapHeaderBase);
        if (result == null)
          result = caseExtensibilityElement(soapHeaderBase);
        if (result == null)
          result = caseWSDLElement(soapHeaderBase);
        if (result == null)
          result = caseIExtensibilityElement(soapHeaderBase);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case SOAPPackage.SOAP_FAULT:
      {
        SOAPFault soapFault = (SOAPFault)theEObject;
        Object result = caseSOAPFault(soapFault);
        if (result == null)
          result = caseExtensibilityElement(soapFault);
        if (result == null)
          result = caseISOAPFault(soapFault);
        if (result == null)
          result = caseWSDLElement(soapFault);
        if (result == null)
          result = caseIExtensibilityElement(soapFault);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case SOAPPackage.SOAP_OPERATION:
      {
        SOAPOperation soapOperation = (SOAPOperation)theEObject;
        Object result = caseSOAPOperation(soapOperation);
        if (result == null)
          result = caseExtensibilityElement(soapOperation);
        if (result == null)
          result = caseISOAPOperation(soapOperation);
        if (result == null)
          result = caseWSDLElement(soapOperation);
        if (result == null)
          result = caseIExtensibilityElement(soapOperation);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case SOAPPackage.SOAP_ADDRESS:
      {
        SOAPAddress soapAddress = (SOAPAddress)theEObject;
        Object result = caseSOAPAddress(soapAddress);
        if (result == null)
          result = caseExtensibilityElement(soapAddress);
        if (result == null)
          result = caseISOAPAddress(soapAddress);
        if (result == null)
          result = caseWSDLElement(soapAddress);
        if (result == null)
          result = caseIExtensibilityElement(soapAddress);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case SOAPPackage.SOAP_HEADER_FAULT:
      {
        SOAPHeaderFault soapHeaderFault = (SOAPHeaderFault)theEObject;
        Object result = caseSOAPHeaderFault(soapHeaderFault);
        if (result == null)
          result = caseSOAPHeaderBase(soapHeaderFault);
        if (result == null)
          result = caseISOAPHeaderFault(soapHeaderFault);
        if (result == null)
          result = caseExtensibilityElement(soapHeaderFault);
        if (result == null)
          result = caseWSDLElement(soapHeaderFault);
        if (result == null)
          result = caseIExtensibilityElement(soapHeaderFault);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case SOAPPackage.SOAP_HEADER:
      {
        SOAPHeader soapHeader = (SOAPHeader)theEObject;
        Object result = caseSOAPHeader(soapHeader);
        if (result == null)
          result = caseSOAPHeaderBase(soapHeader);
        if (result == null)
          result = caseISOAPHeader(soapHeader);
        if (result == null)
          result = caseExtensibilityElement(soapHeader);
        if (result == null)
          result = caseWSDLElement(soapHeader);
        if (result == null)
          result = caseIExtensibilityElement(soapHeader);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      default:
      return defaultCase(theEObject);
    }
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
  public Object caseSOAPBinding(SOAPBinding object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Body</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Body</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseSOAPBody(SOAPBody object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Header Base</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Header Base</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseSOAPHeaderBase(SOAPHeaderBase object)
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
  public Object caseSOAPFault(SOAPFault object)
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
  public Object caseSOAPOperation(SOAPOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Address</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Address</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseSOAPAddress(SOAPAddress object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Header Fault</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Header Fault</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseSOAPHeaderFault(SOAPHeaderFault object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Header</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Header</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseSOAPHeader(SOAPHeader object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ISOAP Binding</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ISOAP Binding</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseISOAPBinding(javax.wsdl.extensions.soap.SOAPBinding object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ISOAP Address</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ISOAP Address</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseISOAPAddress(javax.wsdl.extensions.soap.SOAPAddress object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ISOAP Body</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ISOAP Body</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseISOAPBody(javax.wsdl.extensions.soap.SOAPBody object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ISOAP Fault</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ISOAP Fault</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseISOAPFault(javax.wsdl.extensions.soap.SOAPFault object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ISOAP Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ISOAP Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseISOAPOperation(javax.wsdl.extensions.soap.SOAPOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ISOAP Header Fault</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ISOAP Header Fault</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseISOAPHeaderFault(javax.wsdl.extensions.soap.SOAPHeaderFault object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ISOAP Header</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ISOAP Header</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseISOAPHeader(javax.wsdl.extensions.soap.SOAPHeader object)
  {
    return null;
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
  public Object caseIExtensibilityElement(ExtensibilityElement object)
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
  public Object caseExtensibilityElement(org.eclipse.wst.wsdl.ExtensibilityElement object)
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

} //SOAPSwitch
