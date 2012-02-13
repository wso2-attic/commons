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
package org.eclipse.wst.wsdl.binding.mime.internal.impl;


import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mime Xml</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMimeXmlImpl#getEPart <em>EPart</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MIMEMimeXmlImpl extends ExtensibilityElementImpl implements MIMEMimeXml
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached value of the '{@link #getEPart() <em>EPart</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEPart()
   * @generated
   * @ordered
   */
  protected Part ePart;

  private String part;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MIMEMimeXmlImpl()
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
    return MIMEPackage.Literals.MIME_MIME_XML;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Part getEPart()
  {
    if (ePart != null && ePart.eIsProxy())
    {
      InternalEObject oldEPart = (InternalEObject)ePart;
      ePart = (Part)eResolveProxy(oldEPart);
      if (ePart != oldEPart)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MIMEPackage.MIME_MIME_XML__EPART, oldEPart, ePart));
      }
    }
    return ePart;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Part basicGetEPart()
  {
    return ePart;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEPart(Part newEPart)
  {
    Part oldEPart = ePart;
    ePart = newEPart;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MIMEPackage.MIME_MIME_XML__EPART, oldEPart, ePart));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setPart(String part)
  {
    this.part = part;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String getPart()
  {
    return part;
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
      case MIMEPackage.MIME_MIME_XML__EPART:
      if (resolve)
        return getEPart();
      return basicGetEPart();
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
      case MIMEPackage.MIME_MIME_XML__EPART:
      setEPart((Part)newValue);
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
      case MIMEPackage.MIME_MIME_XML__EPART:
      setEPart((Part)null);
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
      case MIMEPackage.MIME_MIME_XML__EPART:
      return ePart != null;
    }
    return super.eIsSet(featureID);
  }

  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
    setPart(MIMEConstants.getAttribute(changedElement, MIMEConstants.PART_ATTRIBUTE));
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
      if (eAttribute == null || eAttribute == MIMEPackage.Literals.MIME_CONTENT__EPART)
        niceSetAttribute(theElement, MIMEConstants.PART_ATTRIBUTE, getPart());
    }
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(MIMEConstants.MIME_NAMESPACE_URI, MIMEConstants.MIME_XML_ELEMENT_TAG);
    return elementType;
  }
} //MIMEMimeXmlImpl
