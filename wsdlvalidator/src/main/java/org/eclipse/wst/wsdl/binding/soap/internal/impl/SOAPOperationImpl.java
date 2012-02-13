/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.impl;


import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl#getSoapActionURI <em>Soap Action URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl#getStyle <em>Style</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPOperationImpl extends ExtensibilityElementImpl implements SOAPOperation
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getSoapActionURI() <em>Soap Action URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSoapActionURI()
   * @generated
   * @ordered
   */
  protected static final String SOAP_ACTION_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSoapActionURI() <em>Soap Action URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSoapActionURI()
   * @generated
   * @ordered
   */
  protected String soapActionURI = SOAP_ACTION_URI_EDEFAULT;

  /**
   * The default value of the '{@link #getStyle() <em>Style</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStyle()
   * @generated
   * @ordered
   */
  protected static final String STYLE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getStyle() <em>Style</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStyle()
   * @generated
   * @ordered
   */
  protected String style = STYLE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SOAPOperationImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EClass eStaticClass()
  {
    return SOAPPackage.Literals.SOAP_OPERATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSoapActionURI()
  {
    return soapActionURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSoapActionURI(String newSoapActionURI)
  {
    String oldSoapActionURI = soapActionURI;
    soapActionURI = newSoapActionURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI, oldSoapActionURI, soapActionURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getStyle()
  {
    return style;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setStyle(String newStyle)
  {
    String oldStyle = style;
    style = newStyle;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_OPERATION__STYLE, oldStyle, style));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI:
      return getSoapActionURI();
      case SOAPPackage.SOAP_OPERATION__STYLE:
      return getStyle();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI:
      setSoapActionURI((String)newValue);
      return;
      case SOAPPackage.SOAP_OPERATION__STYLE:
      setStyle((String)newValue);
      return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI:
      setSoapActionURI(SOAP_ACTION_URI_EDEFAULT);
      return;
      case SOAPPackage.SOAP_OPERATION__STYLE:
      setStyle(STYLE_EDEFAULT);
      return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI:
      return SOAP_ACTION_URI_EDEFAULT == null ? soapActionURI != null : !SOAP_ACTION_URI_EDEFAULT.equals(soapActionURI);
      case SOAPPackage.SOAP_OPERATION__STYLE:
      return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String toString()
  {
    if (eIsProxy())
      return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (soapActionURI: "); //$NON-NLS-1$
    result.append(soapActionURI);
    result.append(", style: "); //$NON-NLS-1$
    result.append(style);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
    setSoapActionURI(SOAPConstants.getAttribute(changedElement, SOAPConstants.SOAP_ACTION_ATTRIBUTE));
    setStyle(SOAPConstants.getAttribute(changedElement, SOAPConstants.STYLE_ATTRIBUTE));
    reconcileReferences(false);
  }

  //
  // For reconciliation: Model -> DOM
  //

  protected void changeAttribute(EAttribute eAttribute)
  {
    if (isReconciling)
      return;

    super.changeAttribute(eAttribute);
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_OPERATION__SOAP_ACTION_URI)
        niceSetAttribute(theElement, SOAPConstants.SOAP_ACTION_ATTRIBUTE, getSoapActionURI());
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_OPERATION__STYLE)
        niceSetAttribute(theElement, SOAPConstants.STYLE_ATTRIBUTE, getStyle());
    }
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.OPERATION_ELEMENT_TAG);
    return elementType;
  }

} //SOAPOperationImpl
