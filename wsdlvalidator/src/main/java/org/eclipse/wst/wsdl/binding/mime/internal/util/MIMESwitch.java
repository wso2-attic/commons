/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.mime.internal.util;


import java.util.List;

import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.binding.mime.*;


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
 * @see org.eclipse.wst.wsdl.binding.mime.MIMEPackage
 * @generated
 */
public class MIMESwitch
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static MIMEPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIMESwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = MIMEPackage.eINSTANCE;
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
      case MIMEPackage.MIME_CONTENT:
      {
        MIMEContent mimeContent = (MIMEContent)theEObject;
        Object result = caseMIMEContent(mimeContent);
        if (result == null)
          result = caseExtensibilityElement(mimeContent);
        if (result == null)
          result = caseIMIMEContent(mimeContent);
        if (result == null)
          result = caseWSDLElement(mimeContent);
        if (result == null)
          result = caseIExtensibilityElement(mimeContent);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case MIMEPackage.MIME_PART:
      {
        MIMEPart mimePart = (MIMEPart)theEObject;
        Object result = caseMIMEPart(mimePart);
        if (result == null)
          result = caseExtensibilityElement(mimePart);
        if (result == null)
          result = caseIMIMEPart(mimePart);
        if (result == null)
          result = caseWSDLElement(mimePart);
        if (result == null)
          result = caseIExtensibilityElement(mimePart);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case MIMEPackage.MIME_MULTIPART_RELATED:
      {
        MIMEMultipartRelated mimeMultipartRelated = (MIMEMultipartRelated)theEObject;
        Object result = caseMIMEMultipartRelated(mimeMultipartRelated);
        if (result == null)
          result = caseExtensibilityElement(mimeMultipartRelated);
        if (result == null)
          result = caseIMIMEMultipartRelated(mimeMultipartRelated);
        if (result == null)
          result = caseWSDLElement(mimeMultipartRelated);
        if (result == null)
          result = caseIExtensibilityElement(mimeMultipartRelated);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      case MIMEPackage.MIME_MIME_XML:
      {
        MIMEMimeXml mimeMimeXml = (MIMEMimeXml)theEObject;
        Object result = caseMIMEMimeXml(mimeMimeXml);
        if (result == null)
          result = caseExtensibilityElement(mimeMimeXml);
        if (result == null)
          result = caseIMIMEMimeXml(mimeMimeXml);
        if (result == null)
          result = caseWSDLElement(mimeMimeXml);
        if (result == null)
          result = caseIExtensibilityElement(mimeMimeXml);
        if (result == null)
          result = defaultCase(theEObject);
        return result;
      }
      default:
      return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Content</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Content</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseMIMEContent(MIMEContent object)
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
  public Object caseMIMEPart(MIMEPart object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Multipart Related</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Multipart Related</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseMIMEMultipartRelated(MIMEMultipartRelated object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Mime Xml</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Mime Xml</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseMIMEMimeXml(MIMEMimeXml object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IMIME Part</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IMIME Part</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIMIMEPart(javax.wsdl.extensions.mime.MIMEPart object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IMIME Multipart Related</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IMIME Multipart Related</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIMIMEMultipartRelated(javax.wsdl.extensions.mime.MIMEMultipartRelated object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IMIME Content</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IMIME Content</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIMIMEContent(javax.wsdl.extensions.mime.MIMEContent object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>IMIME Mime Xml</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>IMIME Mime Xml</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseIMIMEMimeXml(javax.wsdl.extensions.mime.MIMEMimeXml object)
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

} //MIMESwitch
