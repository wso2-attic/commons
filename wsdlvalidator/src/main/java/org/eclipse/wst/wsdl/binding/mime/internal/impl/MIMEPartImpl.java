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


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.MIMEPart;
import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.eclipse.wst.wsdl.internal.impl.WSDLFactoryImpl;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Part</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPartImpl#getEExtensibilityElements <em>EExtensibility Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MIMEPartImpl extends ExtensibilityElementImpl implements MIMEPart
{

  /**
   * The cached value of the '{@link #getEExtensibilityElements() <em>EExtensibility Elements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEExtensibilityElements()
   * @generated
   * @ordered
   */
  protected EList eExtensibilityElements;

  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MIMEPartImpl()
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
    return MIMEPackage.Literals.MIME_PART;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEExtensibilityElements()
  {
    if (eExtensibilityElements == null)
    {
      eExtensibilityElements = new EObjectContainmentEList(ExtensibilityElement.class, this, MIMEPackage.MIME_PART__EEXTENSIBILITY_ELEMENTS);
    }
    return eExtensibilityElements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addExtensibilityElement(ExtensibilityElement extensibilityElement)
  {
    getExtensibilityElements().add(extensibilityElement);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getExtensibilityElements()
  {
    return getEExtensibilityElements();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case MIMEPackage.MIME_PART__EEXTENSIBILITY_ELEMENTS:
      return ((InternalEList)getEExtensibilityElements()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case MIMEPackage.MIME_PART__EEXTENSIBILITY_ELEMENTS:
      return getEExtensibilityElements();
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
      case MIMEPackage.MIME_PART__EEXTENSIBILITY_ELEMENTS:
      getEExtensibilityElements().clear();
      getEExtensibilityElements().addAll((Collection)newValue);
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
      case MIMEPackage.MIME_PART__EEXTENSIBILITY_ELEMENTS:
      getEExtensibilityElements().clear();
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
      case MIMEPackage.MIME_PART__EEXTENSIBILITY_ELEMENTS:
      return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  //
  // Reconciliation: DOM -> MODEL
  //
  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    if (!WSDLConstants.isMatchingNamespace(child.getNamespaceURI(), WSDLConstants.WSDL_NAMESPACE_URI))
    {
      org.eclipse.wst.wsdl.ExtensibilityElement extensibilityElement = useExtensionFactories()
        ? ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createExtensibilityElement(getNamespace(child), getLocalName(child))
        : ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createUnknownExtensibilityElement();

      extensibilityElement.setEnclosingDefinition(getEnclosingDefinition());
      extensibilityElement.setElement(child);
      addExtensibilityElement(extensibilityElement);
    }
  }

  private boolean useExtensionFactories()
  {
    // Use extension factories by default.
    return getEnclosingDefinition() == null ? true : ((DefinitionImpl)getEnclosingDefinition()).getUseExtensionFactories();
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(MIMEConstants.MIME_NAMESPACE_URI, MIMEConstants.PART_ELEMENT_TAG);
    return elementType;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl#createElement()
   */
  public Element createElement()
  {
    Element newElement = super.createElement();

    Iterator iterator = getExtensibilityElements().iterator();
    while (iterator.hasNext())
    {
      ExtensibilityElement extensibilityElement = (ExtensibilityElement)iterator.next();
      Element child = ((ExtensibilityElementImpl)extensibilityElement).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  }
} //MIMEPartImpl
