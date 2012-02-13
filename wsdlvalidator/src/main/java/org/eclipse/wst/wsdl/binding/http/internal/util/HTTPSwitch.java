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
package org.eclipse.wst.wsdl.binding.http.internal.util;


import java.util.List;

import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.binding.http.*;


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
 * @see org.eclipse.wst.wsdl.binding.http.HTTPPackage
 * @generated
 */
public class HTTPSwitch
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static HTTPPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = HTTPPackage.eINSTANCE;
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
      case HTTPPackage.HTTP_BINDING:
      {
        HTTPBinding httpBinding = (HTTPBinding)theEObject;
        Object result = caseHTTPBinding(httpBinding);
        if (result == null)
          result = caseExtensibilityElement(httpBinding);
        if (result == null)
          result = caseIHTTPBinding(httpBinding);
        if (result == null)
          result = caseWSDLElement(httpBinding);
        if (result == null)
          result = caseIExtensibilityElement(httpBinding);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case HTTPPackage.HTTP_OPERATION:
      {
        HTTPOperation httpOperation = (HTTPOperation)theEObject;
        Object result = caseHTTPOperation(httpOperation);
        if (result == null)
          result = caseExtensibilityElement(httpOperation);
        if (result == null)
          result = caseIHTTPOperation(httpOperation);
        if (result == null)
          result = caseWSDLElement(httpOperation);
        if (result == null)
          result = caseIExtensibilityElement(httpOperation);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case HTTPPackage.HTTP_URL_REPLACEMENT:
      {
        HTTPUrlReplacement httpUrlReplacement = (HTTPUrlReplacement)theEObject;
        Object result = caseHTTPUrlReplacement(httpUrlReplacement);
        if (result == null)
          result = caseExtensibilityElement(httpUrlReplacement);
        if (result == null)
          result = caseIHTTPUrlReplacement(httpUrlReplacement);
        if (result == null)
          result = caseWSDLElement(httpUrlReplacement);
        if (result == null)
          result = caseIExtensibilityElement(httpUrlReplacement);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case HTTPPackage.HTTP_URL_ENCODED:
      {
        HTTPUrlEncoded httpUrlEncoded = (HTTPUrlEncoded)theEObject;
        Object result = caseHTTPUrlEncoded(httpUrlEncoded);
        if (result == null)
          result = caseExtensibilityElement(httpUrlEncoded);
        if (result == null)
          result = caseIHTTPUrlEncoded(httpUrlEncoded);
        if (result == null)
          result = caseWSDLElement(httpUrlEncoded);
        if (result == null)
          result = caseIExtensibilityElement(httpUrlEncoded);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case HTTPPackage.HTTP_ADDRESS:
      {
        HTTPAddress httpAddress = (HTTPAddress)theEObject;
        Object result = caseHTTPAddress(httpAddress);
        if (result == null)
          result = caseExtensibilityElement(httpAddress);
        if (result == null)
          result = caseIHTTPAddress(httpAddress);
        if (result == null)
          result = caseWSDLElement(httpAddress);
        if (result == null)
          result = caseIExtensibilityElement(httpAddress);
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
  public Object caseHTTPBinding(HTTPBinding object)
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
  public Object caseHTTPOperation(HTTPOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Url Replacement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Url Replacement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseHTTPUrlReplacement(HTTPUrlReplacement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Url Encoded</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Url Encoded</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseHTTPUrlEncoded(HTTPUrlEncoded object)
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
  public Object caseHTTPAddress(HTTPAddress object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IHTTP Address</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IHTTP Address</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIHTTPAddress(javax.wsdl.extensions.http.HTTPAddress object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IHTTP Binding</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IHTTP Binding</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIHTTPBinding(javax.wsdl.extensions.http.HTTPBinding object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IHTTP Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IHTTP Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIHTTPOperation(javax.wsdl.extensions.http.HTTPOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IHTTP Url Encoded</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IHTTP Url Encoded</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIHTTPUrlEncoded(javax.wsdl.extensions.http.HTTPUrlEncoded object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IHTTP Url Replacement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IHTTP Url Replacement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIHTTPUrlReplacement(javax.wsdl.extensions.http.HTTPUrlReplacement object)
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

} //HTTPSwitch
