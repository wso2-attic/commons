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
package org.eclipse.wst.wsdl.binding.soap.internal.impl;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fault</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl#getEEncodingStyles <em>EEncoding Styles</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPFaultImpl extends ExtensibilityElementImpl implements SOAPFault
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getUse() <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUse()
   * @generated
   * @ordered
   */
  protected static final String USE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUse() <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUse()
   * @generated
   * @ordered
   */
  protected String use = USE_EDEFAULT;

  /**
   * The default value of the '{@link #getNamespaceURI() <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamespaceURI()
   * @generated
   * @ordered
   */
  protected static final String NAMESPACE_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getNamespaceURI() <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamespaceURI()
   * @generated
   * @ordered
   */
  protected String namespaceURI = NAMESPACE_URI_EDEFAULT;

  /**
   * The cached value of the '{@link #getEEncodingStyles() <em>EEncoding Styles</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEEncodingStyles()
   * @generated
   * @ordered
   */
  protected EList eEncodingStyles;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SOAPFaultImpl()
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
    return SOAPPackage.Literals.SOAP_FAULT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUse()
  {
    return use;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUse(String newUse)
  {
    String oldUse = use;
    use = newUse;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_FAULT__USE, oldUse, use));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getNamespaceURI()
  {
    return namespaceURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNamespaceURI(String newNamespaceURI)
  {
    String oldNamespaceURI = namespaceURI;
    namespaceURI = newNamespaceURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_FAULT__NAMESPACE_URI, oldNamespaceURI, namespaceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEEncodingStyles()
  {
    if (eEncodingStyles == null)
    {
      eEncodingStyles = new EDataTypeUniqueEList(String.class, this, SOAPPackage.SOAP_FAULT__EENCODING_STYLES);
    }
    return eEncodingStyles;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_FAULT__NAME, oldName, name));
  }

  /*
   * (non-Javadoc)
   * @see javax.wsdl.extensions.soap.SOAPFault#getEncodingStyles()
   */
  public List getEncodingStyles()
  {
    return getEEncodingStyles();
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
      case SOAPPackage.SOAP_FAULT__USE:
      return getUse();
      case SOAPPackage.SOAP_FAULT__NAMESPACE_URI:
      return getNamespaceURI();
      case SOAPPackage.SOAP_FAULT__EENCODING_STYLES:
      return getEEncodingStyles();
      case SOAPPackage.SOAP_FAULT__NAME:
      return getName();
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
      case SOAPPackage.SOAP_FAULT__USE:
      setUse((String)newValue);
      return;
      case SOAPPackage.SOAP_FAULT__NAMESPACE_URI:
      setNamespaceURI((String)newValue);
      return;
      case SOAPPackage.SOAP_FAULT__EENCODING_STYLES:
      getEEncodingStyles().clear();
      getEEncodingStyles().addAll((Collection)newValue);
      return;
      case SOAPPackage.SOAP_FAULT__NAME:
      setName((String)newValue);
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
      case SOAPPackage.SOAP_FAULT__USE:
      setUse(USE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_FAULT__NAMESPACE_URI:
      setNamespaceURI(NAMESPACE_URI_EDEFAULT);
      return;
      case SOAPPackage.SOAP_FAULT__EENCODING_STYLES:
      getEEncodingStyles().clear();
      return;
      case SOAPPackage.SOAP_FAULT__NAME:
      setName(NAME_EDEFAULT);
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
      case SOAPPackage.SOAP_FAULT__USE:
      return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
      case SOAPPackage.SOAP_FAULT__NAMESPACE_URI:
      return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case SOAPPackage.SOAP_FAULT__EENCODING_STYLES:
      return eEncodingStyles != null && !eEncodingStyles.isEmpty();
      case SOAPPackage.SOAP_FAULT__NAME:
      return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
    }
    return super.eIsSet(featureID);
  }

  public void setEncodingStyles(List list)
  {
    eEncodingStyles = (EList)list;
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
    result.append(" (use: "); //$NON-NLS-1$
    result.append(use);
    result.append(", namespaceURI: "); //$NON-NLS-1$
    result.append(namespaceURI);
    result.append(", eEncodingStyles: "); //$NON-NLS-1$
    result.append(eEncodingStyles);
    result.append(", name: "); //$NON-NLS-1$
    result.append(name);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
    setName(SOAPConstants.getAttribute(changedElement, SOAPConstants.NAME_ATTRIBUTE));
    setNamespaceURI(SOAPConstants.getAttribute(changedElement, SOAPConstants.NAMESPACE_ATTRIBUTE));
    setUse(SOAPConstants.getAttribute(changedElement, SOAPConstants.USE_ATTRIBUTE));

    // TBD - handle encodingStyles

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
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_FAULT__NAMESPACE_URI)
        niceSetAttribute(theElement, SOAPConstants.NAMESPACE_ATTRIBUTE, getNamespaceURI());
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_FAULT__USE)
        niceSetAttribute(theElement, SOAPConstants.USE_ATTRIBUTE, getUse());
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_FAULT__EENCODING_STYLES)
      {
        List encodingStyleList = getEncodingStyles();
        String encodingStyles = "";
        Iterator iterator = encodingStyleList.iterator();
        while (iterator.hasNext())
        {
          if (encodingStyles.equals("")) // first iteration
            encodingStyles += (String)iterator.next();
          else
            encodingStyles += " " + (String)iterator.next();
        }
        if (!encodingStyles.equals(""))
          niceSetAttribute(theElement, SOAPConstants.ENCODING_STYLE_ATTRIBUTE, encodingStyles);
      } // TBD - Is this the proper way to handle encodingStyles
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_FAULT__NAME)
        niceSetAttribute(theElement, SOAPConstants.NAME_ATTRIBUTE, getName());
    }
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.FAULT_ELEMENT_TAG);
    return elementType;
  }

} //SOAPFaultImpl
