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
package org.eclipse.wst.wsdl.binding.mime.internal.util;


import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.binding.mime.*;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.binding.mime.MIMEPackage
 * @generated
 */
public class MIMEAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static MIMEPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIMEAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = MIMEPackage.eINSTANCE;
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
  protected MIMESwitch modelSwitch = new MIMESwitch()
    {
      public Object caseMIMEContent(MIMEContent object)
      {
        return createMIMEContentAdapter();
      }

      public Object caseMIMEPart(MIMEPart object)
      {
        return createMIMEPartAdapter();
      }

      public Object caseMIMEMultipartRelated(MIMEMultipartRelated object)
      {
        return createMIMEMultipartRelatedAdapter();
      }

      public Object caseMIMEMimeXml(MIMEMimeXml object)
      {
        return createMIMEMimeXmlAdapter();
      }

      public Object caseIMIMEPart(javax.wsdl.extensions.mime.MIMEPart object)
      {
        return createIMIMEPartAdapter();
      }

      public Object caseIMIMEMultipartRelated(javax.wsdl.extensions.mime.MIMEMultipartRelated object)
      {
        return createIMIMEMultipartRelatedAdapter();
      }

      public Object caseIMIMEContent(javax.wsdl.extensions.mime.MIMEContent object)
      {
        return createIMIMEContentAdapter();
      }

      public Object caseIMIMEMimeXml(javax.wsdl.extensions.mime.MIMEMimeXml object)
      {
        return createIMIMEMimeXmlAdapter();
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
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.mime.MIMEContent <em>Content</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEContent
   * @generated
   */
  public Adapter createMIMEContentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.mime.MIMEPart <em>Part</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEPart
   * @generated
   */
  public Adapter createMIMEPartAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated <em>Multipart Related</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated
   * @generated
   */
  public Adapter createMIMEMultipartRelatedAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml <em>Mime Xml</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml
   * @generated
   */
  public Adapter createMIMEMimeXmlAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.mime.MIMEPart <em>IMIME Part</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.mime.MIMEPart
   * @generated
   */
  public Adapter createIMIMEPartAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.mime.MIMEMultipartRelated <em>IMIME Multipart Related</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.mime.MIMEMultipartRelated
   * @generated
   */
  public Adapter createIMIMEMultipartRelatedAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.mime.MIMEContent <em>IMIME Content</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.mime.MIMEContent
   * @generated
   */
  public Adapter createIMIMEContentAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.mime.MIMEMimeXml <em>IMIME Mime Xml</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.mime.MIMEMimeXml
   * @generated
   */
  public Adapter createIMIMEMimeXmlAdapter()
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

} //MIMEAdapterFactory
