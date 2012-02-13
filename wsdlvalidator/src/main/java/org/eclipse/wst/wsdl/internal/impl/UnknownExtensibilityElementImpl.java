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

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unknown Extensibility Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.UnknownExtensibilityElementImpl#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnknownExtensibilityElementImpl extends ExtensibilityElementImpl implements UnknownExtensibilityElement
{
  /**
   * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChildren()
   * @generated
   * @ordered
   */
  protected EList children;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected UnknownExtensibilityElementImpl()
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
    return WSDLPackage.Literals.UNKNOWN_EXTENSIBILITY_ELEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getChildren()
  {
    if (children == null)
    {
      children = new EObjectContainmentEList(UnknownExtensibilityElement.class, this, WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN);
    }
    return children;
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
      case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
      return ((InternalEList)getChildren()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
      return getChildren();
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
      case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
      getChildren().clear();
      getChildren().addAll((Collection)newValue);
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
      case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
      getChildren().clear();
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
      case WSDLPackage.UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN:
      return children != null && !children.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  // 
  // Reconcile methods
  //

  public void setElement(Element element)
  {
    if (element == null && !isReconciling)
    {
      //System.out.println("ExtensibilityElement.setElement(): Preserving old element");
    }
    else
    {
      setElementGen(element);
    }
  }

  public void reconcileAttributes(Element changedElement)
  {
    //System.out.println("UnknownExtensibilityElementImpl.reconcileAttributes()");
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    //System.out.println("UnknownExtensibilityElementImpl.handleUnreconciledElement()");
    UnknownExtensibilityElement extensibilityElement = WSDLFactory.eINSTANCE.createUnknownExtensibilityElement();
    extensibilityElement.setEnclosingDefinition(getEnclosingDefinition());
    extensibilityElement.setElement(child);

    // TODO..  we need to figure out where the child should go in the in current list
    // so that it doesn't always end up going to the end of the list 
    // (since a new element might be added at the start)
    getChildren().add(extensibilityElement);
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
    for (Iterator i = remainingModelObjects.iterator(); i.hasNext();)
    {
      remove(this, i.next());
    }
  }

  protected void remove(Object component, Object modelObject)
  {
    UnknownExtensibilityElement uee = (UnknownExtensibilityElement)component;
    if (modelObject instanceof UnknownExtensibilityElement)
    {
      uee.getChildren().remove(modelObject);
    }
  }

  public Element createElement()
  {
    Definition definition = getEnclosingDefinition();
    if (definition == null)
      return null;

    Document document = definition.getDocument();
    if (document == null)
      document = ((DefinitionImpl)definition).updateDocument();

    Element newElement = null;
    if (element != null) // This is an old element.
    {
      newElement = (Element)document.importNode(element, true);
      element = newElement;
    }
    else
    {
      String namespace = getElementType().getNamespaceURI();
      String qualifier = definition.getPrefix(namespace);
      newElement = document.createElementNS(namespace, (qualifier == null ? "" : qualifier + ":") + getElementType().getLocalPart());
      element = newElement;
    }

    return newElement;
  }

  private java.util.Map properties = new java.util.HashMap();

  public void setAttribute(String key, String value)
  {
    properties.put(key, value);
  }

  protected void changeAttribute(EAttribute eAttribute)
  {
    if (isReconciling)
      return;

    super.changeAttribute(eAttribute);
    Element theElement = getElement();
    if (theElement != null)
    {
      Iterator iterator = properties.entrySet().iterator();
      java.util.Map.Entry entry = null;
      while (iterator.hasNext())
      {
        entry = (java.util.Map.Entry)iterator.next();
        String attribute = (String)entry.getKey();
        String value = (String)entry.getValue();
        niceSetAttribute(theElement, attribute, value);
      }
    }
  }
} //UnknownExtensibilityElementImpl
