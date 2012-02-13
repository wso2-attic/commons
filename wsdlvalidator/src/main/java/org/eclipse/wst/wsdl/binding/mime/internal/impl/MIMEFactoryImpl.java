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
package org.eclipse.wst.wsdl.binding.mime.internal.impl;


import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.wst.wsdl.binding.mime.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MIMEFactoryImpl extends EFactoryImpl implements MIMEFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MIMEFactory init()
  {
    try
    {
      MIMEFactory theMIMEFactory = (MIMEFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.eclipse.org/wsdl/2003/MIME"); //$NON-NLS-1$ 
      if (theMIMEFactory != null)
      {
        return theMIMEFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new MIMEFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIMEFactoryImpl()
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
      case MIMEPackage.MIME_CONTENT:
      return createMIMEContent();
      case MIMEPackage.MIME_PART:
      return createMIMEPart();
      case MIMEPackage.MIME_MULTIPART_RELATED:
      return createMIMEMultipartRelated();
      case MIMEPackage.MIME_MIME_XML:
      return createMIMEMimeXml();
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
      case MIMEPackage.LIST:
      return createListFromString(eDataType, initialValue);
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
      case MIMEPackage.LIST:
      return convertListToString(eDataType, instanceValue);
      default:
      throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIMEContent createMIMEContent()
  {
    MIMEContentImpl mimeContent = new MIMEContentImpl();
    return mimeContent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIMEPart createMIMEPart()
  {
    MIMEPartImpl mimePart = new MIMEPartImpl();
    return mimePart;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIMEMultipartRelated createMIMEMultipartRelated()
  {
    MIMEMultipartRelatedImpl mimeMultipartRelated = new MIMEMultipartRelatedImpl();
    return mimeMultipartRelated;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIMEMimeXml createMIMEMimeXml()
  {
    MIMEMimeXmlImpl mimeMimeXml = new MIMEMimeXmlImpl();
    return mimeMimeXml;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List createListFromString(EDataType eDataType, String initialValue)
  {
    return (List)super.createFromString(eDataType, initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertListToString(EDataType eDataType, Object instanceValue)
  {
    return super.convertToString(eDataType, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIMEPackage getMIMEPackage()
  {
    return (MIMEPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  public static MIMEPackage getPackage()
  {
    return MIMEPackage.eINSTANCE;
  }
} //MIMEFactoryImpl
