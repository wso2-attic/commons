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
package org.eclipse.wst.wsdl.internal.impl;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extensibility Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl#isRequired <em>Required</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl#getElementType <em>Element Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExtensibilityElementImpl extends WSDLElementImpl implements ExtensibilityElement
{
  /**
   * The default value of the '{@link #isRequired() <em>Required</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRequired()
   * @generated
   * @ordered
   */
  protected static final boolean REQUIRED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isRequired() <em>Required</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRequired()
   * @generated
   * @ordered
   */
  protected boolean required = REQUIRED_EDEFAULT;

  /**
   * The default value of the '{@link #getElementType() <em>Element Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElementType()
   * @generated
   * @ordered
   */
  protected static final QName ELEMENT_TYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getElementType() <em>Element Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElementType()
   * @generated
   * @ordered
   */
  protected QName elementType = ELEMENT_TYPE_EDEFAULT;

  private HashMap beanPropertyDescriptors;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ExtensibilityElementImpl()
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
    return WSDLPackage.Literals.EXTENSIBILITY_ELEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isRequired()
  {
    return required;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRequired(boolean newRequired)
  {
    boolean oldRequired = required;
    required = newRequired;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED, oldRequired, required));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(eClass().getEPackage().getNsURI(), eClass().getName());

    return elementType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setElementType(QName newElementType)
  {
    QName oldElementType = elementType;
    elementType = newElementType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE, oldElementType, elementType));
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
      case WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED:
      return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE:
      return getElementType();
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
      case WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED:
      setRequired(((Boolean)newValue).booleanValue());
      return;
      case WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE:
      setElementType((QName)newValue);
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
      case WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED:
      setRequired(REQUIRED_EDEFAULT);
      return;
      case WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE:
      setElementType(ELEMENT_TYPE_EDEFAULT);
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
      case WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED:
      return required != REQUIRED_EDEFAULT;
      case WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE:
      return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
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
    result.append(" (required: "); //$NON-NLS-1$
    result.append(required);
    result.append(", elementType: "); //$NON-NLS-1$
    result.append(elementType);
    result.append(')');
    return result.toString();
  }

  /**
   * @see javax.wsdl.extensions.ExtensibilityElement#getRequired()
   */
  public Boolean getRequired()
  {
    return new Boolean(isRequired());
  }

  /**
   * @see javax.wsdl.extensions.ExtensibilityElement#setRequired(Boolean)
   */
  public void setRequired(Boolean required)
  {
    setRequired(required.booleanValue());
  }

  /**
   * Returns the property descriptors for a given class.
   */
  public java.util.Map getPropertyDescriptors(java.lang.Class clazz)
  {
    // Lookup properties cache
    Map properties = null;
    if (beanPropertyDescriptors == null)
      beanPropertyDescriptors = new HashMap();
    else
      properties = (Map)beanPropertyDescriptors.get(clazz);

    if (properties == null)
    {
      // Introspect the bean
      ArrayList binfos = new ArrayList();
      try
      {
        BeanInfo binfo = Introspector.getBeanInfo(clazz);
        if (binfo != null)
        {
          binfos.add(binfo);
          BeanInfo[] abinfo = binfo.getAdditionalBeanInfo();
          if (abinfo != null)
          {
            for (int b = 0; b < abinfo.length; b++)
              binfos.add(abinfo[b]);
          }
        }
      }
      catch (IntrospectionException e)
      {
        // TBD - handle exception
      }

      properties = new HashMap();
      for (Iterator b = binfos.iterator(); b.hasNext();)
      {
        BeanInfo binfo = (BeanInfo)b.next();
        PropertyDescriptor[] pdesc = binfo.getPropertyDescriptors();
        if (pdesc == null)
          continue;
        for (int p = 0; p < pdesc.length; p++)
          properties.put(pdesc[p].getName(), pdesc[p]);
      }
      beanPropertyDescriptors.put(clazz, properties);
    }
    return properties;
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
    super.reconcileAttributes(changedElement);
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    super.handleUnreconciledElement(child, remainingModelObjects);
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
    super.handleReconciliation(remainingModelObjects);
  }

  //
  // For reconciliation: EMF -> DOM
  //

  protected void changeAttribute(EAttribute eAttribute)
  {
    if (isReconciling)
      return;

    super.changeAttribute(eAttribute);
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
} //ExtensibilityElementImpl
