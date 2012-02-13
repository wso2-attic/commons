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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingImpl#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingImpl#isUndefined <em>Undefined</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingImpl#getEPortType <em>EPort Type</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingImpl#getEBindingOperations <em>EBinding Operations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BindingImpl extends ExtensibleElementImpl implements Binding
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getQName() <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQName()
   * @generated
   * @ordered
   */
  protected static final QName QNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getQName() <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQName()
   * @generated
   * @ordered
   */
  protected QName qName = QNAME_EDEFAULT;

  /**
   * The default value of the '{@link #isUndefined() <em>Undefined</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUndefined()
   * @generated
   * @ordered
   */
  protected static final boolean UNDEFINED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isUndefined() <em>Undefined</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUndefined()
   * @generated
   * @ordered
   */
  protected boolean undefined = UNDEFINED_EDEFAULT;

  /**
   * The cached value of the '{@link #getEPortType() <em>EPort Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEPortType()
   * @generated
   * @ordered
   */
  protected PortType ePortType;

  /**
   * The cached value of the '{@link #getEBindingOperations() <em>EBinding Operations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEBindingOperations()
   * @generated
   * @ordered
   */
  protected EList eBindingOperations;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BindingImpl()
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
    return WSDLPackage.Literals.BINDING;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QName getQName()
  {
    return qName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setQName(QName newQName)
  {
    QName oldQName = qName;
    qName = newQName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING__QNAME, oldQName, qName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isUndefined()
  {
    return undefined;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUndefined(boolean newUndefined)
  {
    boolean oldUndefined = undefined;
    undefined = newUndefined;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING__UNDEFINED, oldUndefined, undefined));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PortType getEPortType()
  {
    if (ePortType != null && ePortType.eIsProxy())
    {
      InternalEObject oldEPortType = (InternalEObject)ePortType;
      ePortType = (PortType)eResolveProxy(oldEPortType);
      if (ePortType != oldEPortType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.BINDING__EPORT_TYPE, oldEPortType, ePortType));
      }
    }
    return ePortType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PortType basicGetEPortType()
  {
    return ePortType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEPortType(PortType newEPortType)
  {
    PortType oldEPortType = ePortType;
    ePortType = newEPortType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING__EPORT_TYPE, oldEPortType, ePortType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEBindingOperations()
  {
    if (eBindingOperations == null)
    {
      eBindingOperations = new EObjectContainmentEList(BindingOperation.class, this, WSDLPackage.BINDING__EBINDING_OPERATIONS);
    }
    return eBindingOperations;
  }

  /**
   * <!-- begin-user-doc -->
   * Add an operation binding to binding.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addBindingOperation(javax.wsdl.BindingOperation bindingOperation)
  {
    getBindingOperations().add(bindingOperation);
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified operation binding. Note that operation names can
   * be overloaded within a PortType. In case of overloading, the
   * names of the input and output messages can be used to further
   * refine the search.
   * @param name the name of the desired operation binding.
   * @param inputName the name of the input message; if this is null
   * it will be ignored.
   * @param outputName the name of the output message; if this is null
   * it will be ignored.
   * @return the corresponding operation binding, or null if there wasn't
   * any matching operation binding
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.BindingOperation getBindingOperation(String name, String inputName, String outputName)
  {
    Iterator opBindingIterator = getBindingOperations().iterator();
    while (opBindingIterator.hasNext())
    {
      javax.wsdl.BindingOperation op = (javax.wsdl.BindingOperation)opBindingIterator.next();
      if (op == null)
        continue;

      String opName = op.getName();
      if (name != null && opName != null)
      {
        if (!name.equals(opName))
          op = null;
      }
      else if (name != null || opName != null)
      {
        op = null;
      }

      if (op != null && inputName != null)
      {
        javax.wsdl.BindingInput input = op.getBindingInput();
        if (input != null)
        {
          String opInputName = input.getName();
          if (opInputName == null || !opInputName.equals(inputName))
            op = null;
        }
        else
          op = null;
      }

      if (op != null && outputName != null)
      {
        javax.wsdl.BindingOutput output = op.getBindingOutput();
        if (output != null)
        {
          String opOutputName = output.getName();
          if (opOutputName == null || !opOutputName.equals(outputName))
            op = null;
        }
        else
          op = null;
      }

      if (op != null)
        return op;
    } // end while
    return null; // binding operation not found
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the operation bindings defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getBindingOperations()
  {
    return getEBindingOperations();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.PortType getPortType()
  {
    return getEPortType();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setPortType(javax.wsdl.PortType portType)
  {
    setEPortType((PortType)portType);
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
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
      return ((InternalEList)getEBindingOperations()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.BINDING__QNAME:
      return getQName();
      case WSDLPackage.BINDING__UNDEFINED:
      return isUndefined() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.BINDING__EPORT_TYPE:
      if (resolve)
        return getEPortType();
      return basicGetEPortType();
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
      return getEBindingOperations();
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
      case WSDLPackage.BINDING__QNAME:
      setQName((QName)newValue);
      return;
      case WSDLPackage.BINDING__UNDEFINED:
      setUndefined(((Boolean)newValue).booleanValue());
      return;
      case WSDLPackage.BINDING__EPORT_TYPE:
      setEPortType((PortType)newValue);
      return;
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
      getEBindingOperations().clear();
      getEBindingOperations().addAll((Collection)newValue);
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
      case WSDLPackage.BINDING__QNAME:
      setQName(QNAME_EDEFAULT);
      return;
      case WSDLPackage.BINDING__UNDEFINED:
      setUndefined(UNDEFINED_EDEFAULT);
      return;
      case WSDLPackage.BINDING__EPORT_TYPE:
      setEPortType((PortType)null);
      return;
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
      getEBindingOperations().clear();
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
      case WSDLPackage.BINDING__QNAME:
      return QNAME_EDEFAULT == null ? qName != null : !QNAME_EDEFAULT.equals(qName);
      case WSDLPackage.BINDING__UNDEFINED:
      return undefined != UNDEFINED_EDEFAULT;
      case WSDLPackage.BINDING__EPORT_TYPE:
      return ePortType != null;
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
      return eBindingOperations != null && !eBindingOperations.isEmpty();
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
    result.append(" (qName: "); //$NON-NLS-1$
    result.append(qName);
    result.append(", undefined: "); //$NON-NLS-1$
    result.append(undefined);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcile methods
  //

  public void reconcileAttributes(Element changedElement)
  {
    Definition definition = getEnclosingDefinition();
    String name = changedElement.getAttribute(WSDLConstants.NAME_ATTRIBUTE);
    QName qname = new QName(definition.getTargetNamespace(), name == null ? "" : name); //$NON-NLS-1$
    setQName(qname);
    reconcileReferences(false);
  }

  public Collection getModelObjects(Object component)
  {
    Binding binding = (Binding)component;

    List list = new ArrayList();
    list.addAll(binding.getEBindingOperations());
    list.addAll(binding.getEExtensibilityElements());
    return list;
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    switch (WSDLUtil.getInstance().getWSDLType(child))
    {
      case WSDLConstants.OPERATION:
      BindingOperation operation = WSDLFactory.eINSTANCE.createBindingOperation();
      operation.setEnclosingDefinition(getEnclosingDefinition());
      operation.setElement(child);
      addBindingOperation(operation);
        break;
      default:
      super.handleUnreconciledElement(child, remainingModelObjects);
        break;
    }
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
    List list = getList(component, modelObject);
    if (list != null)
    {
      list.remove(modelObject);
    }
  }

  private List getList(Object component, Object modelObject)
  {
    List result = null;
    Binding binding = (Binding)component;
    if (modelObject instanceof BindingOperation)
    {
      result = binding.getEBindingOperations();
    }
    else if (modelObject instanceof ExtensibilityElement)
    {
      result = binding.getEExtensibilityElements();
    }
    return result;
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
      if (eAttribute == null || eAttribute == WSDLPackage.Literals.BINDING__QNAME)
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getQName().getLocalPart());
    }
  }

  protected void changeReference(EReference eReference)
  {
    if (isReconciling)
      return;

    super.changeReference(eReference);
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eReference == null || eReference == WSDLPackage.Literals.BINDING__EPORT_TYPE)
      {
        PortType portType = getEPortType();
        if (portType != null)
        {
          QName qName = portType.getQName();
          niceSetAttributeURIValue(theElement, WSDLConstants.TYPE_ATTRIBUTE, qName.getNamespaceURI() + "#" + qName.getLocalPart());
        }
      }
    }
  }

  protected Element createElement()
  {
    Element newElement = createElement(WSDLConstants.BINDING);
    setElement(newElement);

    Iterator iterator = getExtensibilityElements().iterator();
    while (iterator.hasNext())
    {
      ExtensibilityElement extensibilityElement = (ExtensibilityElement)iterator.next();
      Element child = ((ExtensibilityElementImpl)extensibilityElement).createElement();
      newElement.appendChild(child);
    }

    iterator = getEBindingOperations().iterator();
    while (iterator.hasNext())
    {
      BindingOperation operation = (BindingOperation)iterator.next();
      Element child = ((BindingOperationImpl)operation).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  }

  public void reconcileReferences(boolean deep)
  {
    if (element != null)
    {
      Definition definition = (Definition)getEnclosingDefinition();
      QName portTypeQName = createQName(definition, element.getAttribute(WSDLConstants.TYPE_ATTRIBUTE), element);
      PortType newPortType = portTypeQName != null ? (PortType)definition.getPortType(portTypeQName) : null;
      if (newPortType != getEPortType())
      {
        setEPortType(newPortType);
      }
    }
    super.reconcileReferences(deep);
  }
} //BindingImpl
