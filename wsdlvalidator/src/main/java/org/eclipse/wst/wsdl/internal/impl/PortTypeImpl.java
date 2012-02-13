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
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl#isUndefined <em>Undefined</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl#getEOperations <em>EOperations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PortTypeImpl extends ExtensibleElementImpl implements PortType
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
   * The cached value of the '{@link #getEOperations() <em>EOperations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEOperations()
   * @generated
   * @ordered
   */
  protected EList eOperations;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PortTypeImpl()
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
    return WSDLPackage.Literals.PORT_TYPE;
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.PORT_TYPE__QNAME, oldQName, qName));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.PORT_TYPE__UNDEFINED, oldUndefined, undefined));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEOperations()
  {
    if (eOperations == null)
    {
      eOperations = new EObjectContainmentEList(Operation.class, this, WSDLPackage.PORT_TYPE__EOPERATIONS);
    }
    return eOperations;
  }

  /**
   * <!-- begin-user-doc -->
   * Add an operation to this port type.
   * @param operation the operation to be added
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addOperation(javax.wsdl.Operation operation)
  {
    if (!operation.isUndefined())
      getEOperations().add(operation);
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified operation. Note that operation names can
   * be overloaded within a PortType. In case of overloading, the
   * names of the input and output messages can be used to further
   * refine the search.
   * @param name the name of the desired operation.
   * @param inputName the name of the input message; if this is null
   *        it will be ignored.
   * @param outputName the name of the output message; if this is null
   *        it will be ignored.
   * @return the corresponding operation, or null if there wasn't
   *         any matching operation
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Operation getOperation(String name, String inputName, String outputName)
  {
    Iterator opIterator = getOperations().iterator();
    while (opIterator.hasNext())
    {
      javax.wsdl.Operation op = (javax.wsdl.Operation)opIterator.next();
      if (op == null)
        continue;

      String opName = op.getName();
      if (name != null && opName != null)
      {
        if (!name.equals(opName))
          op = null;
      }
      else if (name != null || opName != null)
        op = null;

      if (op != null && inputName != null)
      {
        javax.wsdl.Input input = op.getInput();
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
        javax.wsdl.Output output = op.getOutput();
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
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the operations defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getOperations()
  {
    if (!isUndefined())
      return getEOperations();
    else
      return new ArrayList();
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
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      return ((InternalEList)getEOperations()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.PORT_TYPE__QNAME:
      return getQName();
      case WSDLPackage.PORT_TYPE__UNDEFINED:
      return isUndefined() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      return getEOperations();
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
      case WSDLPackage.PORT_TYPE__QNAME:
      setQName((QName)newValue);
      return;
      case WSDLPackage.PORT_TYPE__UNDEFINED:
      setUndefined(((Boolean)newValue).booleanValue());
      return;
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      getEOperations().clear();
      getEOperations().addAll((Collection)newValue);
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
      case WSDLPackage.PORT_TYPE__QNAME:
      setQName(QNAME_EDEFAULT);
      return;
      case WSDLPackage.PORT_TYPE__UNDEFINED:
      setUndefined(UNDEFINED_EDEFAULT);
      return;
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      getEOperations().clear();
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
      case WSDLPackage.PORT_TYPE__QNAME:
      return QNAME_EDEFAULT == null ? qName != null : !QNAME_EDEFAULT.equals(qName);
      case WSDLPackage.PORT_TYPE__UNDEFINED:
      return undefined != UNDEFINED_EDEFAULT;
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      return eOperations != null && !eOperations.isEmpty();
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
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    switch (WSDLUtil.getInstance().getWSDLType(child))
    {
      case WSDLConstants.OPERATION:
      {
        Operation operation = WSDLFactory.eINSTANCE.createOperation();
        operation.setEnclosingDefinition(getEnclosingDefinition());
        operation.setElement(child);
        getEOperations().add(operation);
        break;
      }
      default:
      {
        super.handleUnreconciledElement(child, remainingModelObjects);
        break;
      }
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
    PortType portType = (PortType)component;
    if (modelObject instanceof Operation)
    {
      result = portType.getEOperations();
    }
    else if (modelObject instanceof ExtensibilityElement)
    {
      result = getExtensibilityElements();
    }
    
    return result;
  }

  public Collection getModelObjects(Object component)
  {
    PortType portType = (PortType)component;

    List list = portType.getEOperations();
    return list;
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
      if (eAttribute == null || eAttribute == WSDLPackage.Literals.PORT_TYPE__QNAME)
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getQName().getLocalPart());
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.PORT_TYPE);
    setElement(newElement);

    Iterator iterator = getEOperations().iterator();
    while (iterator.hasNext())
    {
      Operation operation = (Operation)iterator.next();
      Element child = ((OperationImpl)operation).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  }
} //PortTypeImpl
