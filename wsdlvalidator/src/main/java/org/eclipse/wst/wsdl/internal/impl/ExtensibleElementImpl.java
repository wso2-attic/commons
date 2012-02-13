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
package org.eclipse.wst.wsdl.internal.impl;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extensible Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.ExtensibleElementImpl#getEExtensibilityElements <em>EExtensibility Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ExtensibleElementImpl extends WSDLElementImpl implements ExtensibleElement
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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ExtensibleElementImpl()
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
    return WSDLPackage.Literals.EXTENSIBLE_ELEMENT;
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
      eExtensibilityElements = new EObjectContainmentEList(
        ExtensibilityElement.class,
        this,
        WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS);
    }
    return eExtensibilityElements;
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
   * @generated NOT
   */
  public void addExtensibilityElement(javax.wsdl.extensions.ExtensibilityElement extElement)
  {
    getExtensibilityElements().add(extElement);
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
      return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  //
  //
  //
  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    if (!WSDLConstants.isMatchingNamespace(child.getNamespaceURI(), WSDLConstants.WSDL_NAMESPACE_URI))
    {
      ExtensibilityElement extensibilityElement = useExtensionFactories()
        ? ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createExtensibilityElement(getNamespace(child), getLocalName(child))
        : ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createUnknownExtensibilityElement();

      extensibilityElement.setEnclosingDefinition(getEnclosingDefinition());
      extensibilityElement.setElement(child);
      getEExtensibilityElements().add(extensibilityElement);
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Object getExtensionAttribute(QName name)
  {
    // TODO: implement this method
    // Ensure that you remove @generated or mark it @generated NOT
    throw new UnsupportedOperationException();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setExtensionAttribute(QName name, Object value)
  {
    // TODO: implement this method
    // Ensure that you remove @generated or mark it @generated NOT
    throw new UnsupportedOperationException();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getExtensionAttributes()
  {
    // TODO: implement this method
    // Ensure that you remove @generated or mark it @generated NOT
    throw new UnsupportedOperationException();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getNativeAttributeNames()
  {
    // TODO: implement this method
    // Ensure that you remove @generated or mark it @generated NOT
    throw new UnsupportedOperationException();
  }

  private boolean useExtensionFactories()
  {
    // Use extension factories by default.
    return getEnclosingDefinition() == null ? true : ((DefinitionImpl)getEnclosingDefinition()).getUseExtensionFactories();
  }

  /**
   * Override this method if the remainingModelObjects contain elements that are not only ExtensibilityElement
   */
  protected void handleReconciliation(Collection remainingModelObjects)
  {
    super.handleReconciliation(remainingModelObjects);
    Iterator iterator = remainingModelObjects.iterator();
    List extensibilityElements = getExtensibilityElements();
    while (iterator.hasNext())
    {
      Object modelObject = iterator.next();
      if (modelObject instanceof ExtensibilityElement) 
      {
        extensibilityElements.remove(modelObject);
      }
    }
  }

} //ExtensibleElementImpl
