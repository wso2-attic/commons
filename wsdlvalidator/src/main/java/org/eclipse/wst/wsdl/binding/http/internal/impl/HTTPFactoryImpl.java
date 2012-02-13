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
package org.eclipse.wst.wsdl.binding.http.internal.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.wst.wsdl.binding.http.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class HTTPFactoryImpl extends EFactoryImpl implements HTTPFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static HTTPFactory init()
  {
    try
    {
      HTTPFactory theHTTPFactory = (HTTPFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/wsdl/2003/HTTP"); //$NON-NLS-1$ 
      if (theHTTPFactory != null)
      {
        return theHTTPFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new HTTPFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPFactoryImpl()
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
      case HTTPPackage.HTTP_BINDING:
      return createHTTPBinding();
      case HTTPPackage.HTTP_OPERATION:
      return createHTTPOperation();
      case HTTPPackage.HTTP_URL_REPLACEMENT:
      return createHTTPUrlReplacement();
      case HTTPPackage.HTTP_URL_ENCODED:
      return createHTTPUrlEncoded();
      case HTTPPackage.HTTP_ADDRESS:
      return createHTTPAddress();
      default:
      throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPBinding createHTTPBinding()
  {
    HTTPBindingImpl httpBinding = new HTTPBindingImpl();
    return httpBinding;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPOperation createHTTPOperation()
  {
    HTTPOperationImpl httpOperation = new HTTPOperationImpl();
    return httpOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPUrlReplacement createHTTPUrlReplacement()
  {
    HTTPUrlReplacementImpl httpUrlReplacement = new HTTPUrlReplacementImpl();
    return httpUrlReplacement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPUrlEncoded createHTTPUrlEncoded()
  {
    HTTPUrlEncodedImpl httpUrlEncoded = new HTTPUrlEncodedImpl();
    return httpUrlEncoded;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPAddress createHTTPAddress()
  {
    HTTPAddressImpl httpAddress = new HTTPAddressImpl();
    return httpAddress;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPPackage getHTTPPackage()
  {
    return (HTTPPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  public static HTTPPackage getPackage()
  {
    return HTTPPackage.eINSTANCE;
  }
} //HTTPFactoryImpl
