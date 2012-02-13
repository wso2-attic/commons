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


import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.binding.http.*;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.binding.http.HTTPPackage
 * @generated
 */
public class HTTPAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static HTTPPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = HTTPPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected HTTPSwitch modelSwitch = new HTTPSwitch()
    {
      public Object caseHTTPBinding(HTTPBinding object)
      {
        return createHTTPBindingAdapter();
      }

      public Object caseHTTPOperation(HTTPOperation object)
      {
        return createHTTPOperationAdapter();
      }

      public Object caseHTTPUrlReplacement(HTTPUrlReplacement object)
      {
        return createHTTPUrlReplacementAdapter();
      }

      public Object caseHTTPUrlEncoded(HTTPUrlEncoded object)
      {
        return createHTTPUrlEncodedAdapter();
      }

      public Object caseHTTPAddress(HTTPAddress object)
      {
        return createHTTPAddressAdapter();
      }

      public Object caseIHTTPAddress(javax.wsdl.extensions.http.HTTPAddress object)
      {
        return createIHTTPAddressAdapter();
      }

      public Object caseIHTTPBinding(javax.wsdl.extensions.http.HTTPBinding object)
      {
        return createIHTTPBindingAdapter();
      }

      public Object caseIHTTPOperation(javax.wsdl.extensions.http.HTTPOperation object)
      {
        return createIHTTPOperationAdapter();
      }

      public Object caseIHTTPUrlEncoded(javax.wsdl.extensions.http.HTTPUrlEncoded object)
      {
        return createIHTTPUrlEncodedAdapter();
      }

      public Object caseIHTTPUrlReplacement(javax.wsdl.extensions.http.HTTPUrlReplacement object)
      {
        return createIHTTPUrlReplacementAdapter();
      }

      public Object caseWSDLElement(WSDLElement object)
      {
        return createWSDLElementAdapter();
      }

      public Object caseIExtensibilityElement(ExtensibilityElement object)
      {
        return createIExtensibilityElementAdapter();
      }

      public Object caseExtensibilityElement(org.eclipse.wst.wsdl.ExtensibilityElement object)
      {
        return createExtensibilityElementAdapter();
      }

      public Object defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  public Adapter createAdapter(Notifier target)
  {
    return (Adapter)modelSwitch.doSwitch((EObject)target);
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.http.HTTPBinding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPBinding
   * @generated
   */
  public Adapter createHTTPBindingAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.http.HTTPOperation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPOperation
   * @generated
   */
  public Adapter createHTTPOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.http.HTTPUrlReplacement <em>Url Replacement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPUrlReplacement
   * @generated
   */
  public Adapter createHTTPUrlReplacementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.http.HTTPUrlEncoded <em>Url Encoded</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPUrlEncoded
   * @generated
   */
  public Adapter createHTTPUrlEncodedAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.http.HTTPAddress <em>Address</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPAddress
   * @generated
   */
  public Adapter createHTTPAddressAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.http.HTTPAddress <em>IHTTP Address</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.http.HTTPAddress
   * @generated
   */
  public Adapter createIHTTPAddressAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.http.HTTPBinding <em>IHTTP Binding</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.http.HTTPBinding
   * @generated
   */
  public Adapter createIHTTPBindingAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.http.HTTPOperation <em>IHTTP Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.http.HTTPOperation
   * @generated
   */
  public Adapter createIHTTPOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.http.HTTPUrlEncoded <em>IHTTP Url Encoded</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.http.HTTPUrlEncoded
   * @generated
   */
  public Adapter createIHTTPUrlEncodedAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.http.HTTPUrlReplacement <em>IHTTP Url Replacement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.http.HTTPUrlReplacement
   * @generated
   */
  public Adapter createIHTTPUrlReplacementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.WSDLElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.WSDLElement
   * @generated
   */
  public Adapter createWSDLElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.ExtensibilityElement <em>IExtensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.ExtensibilityElement
   * @generated
   */
  public Adapter createIExtensibilityElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.ExtensibilityElement <em>Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.ExtensibilityElement
   * @generated
   */
  public Adapter createExtensibilityElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //HTTPAdapterFactory
