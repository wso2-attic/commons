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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
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
 * An implementation of the model object '<em><b>Binding Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl#getEOperation <em>EOperation</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl#getEBindingInput <em>EBinding Input</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl#getEBindingOutput <em>EBinding Output</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl#getEBindingFaults <em>EBinding Faults</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BindingOperationImpl extends ExtensibleElementImpl implements BindingOperation
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

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
   * The cached value of the '{@link #getEOperation() <em>EOperation</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEOperation()
   * @generated
   * @ordered
   */
  protected Operation eOperation;

  /**
   * The cached value of the '{@link #getEBindingInput() <em>EBinding Input</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEBindingInput()
   * @generated
   * @ordered
   */
  protected BindingInput eBindingInput;

  /**
   * The cached value of the '{@link #getEBindingOutput() <em>EBinding Output</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEBindingOutput()
   * @generated
   * @ordered
   */
  protected BindingOutput eBindingOutput;

  /**
   * The cached value of the '{@link #getEBindingFaults() <em>EBinding Faults</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEBindingFaults()
   * @generated
   * @ordered
   */
  protected EList eBindingFaults;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BindingOperationImpl()
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
    return WSDLPackage.Literals.BINDING_OPERATION;
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING_OPERATION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operation getEOperation()
  {
    if (eOperation != null && eOperation.eIsProxy())
    {
      InternalEObject oldEOperation = (InternalEObject)eOperation;
      eOperation = (Operation)eResolveProxy(oldEOperation);
      if (eOperation != oldEOperation)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.BINDING_OPERATION__EOPERATION, oldEOperation, eOperation));
      }
    }
    return eOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operation basicGetEOperation()
  {
    return eOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEOperation(Operation newEOperation)
  {
    Operation oldEOperation = eOperation;
    eOperation = newEOperation;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING_OPERATION__EOPERATION, oldEOperation, eOperation));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BindingInput getEBindingInput()
  {
    return eBindingInput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEBindingInput(BindingInput newEBindingInput, NotificationChain msgs)
  {
    BindingInput oldEBindingInput = eBindingInput;
    eBindingInput = newEBindingInput;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(
        this,
        Notification.SET,
        WSDLPackage.BINDING_OPERATION__EBINDING_INPUT,
        oldEBindingInput,
        newEBindingInput);
      if (msgs == null)
        msgs = notification;
      else
        msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEBindingInput(BindingInput newEBindingInput)
  {
    if (newEBindingInput != eBindingInput)
    {
      NotificationChain msgs = null;
      if (eBindingInput != null)
        msgs = ((InternalEObject)eBindingInput).eInverseRemove(
          this,
          EOPPOSITE_FEATURE_BASE - WSDLPackage.BINDING_OPERATION__EBINDING_INPUT,
          null,
          msgs);
      if (newEBindingInput != null)
        msgs = ((InternalEObject)newEBindingInput).eInverseAdd(
          this,
          EOPPOSITE_FEATURE_BASE - WSDLPackage.BINDING_OPERATION__EBINDING_INPUT,
          null,
          msgs);
      msgs = basicSetEBindingInput(newEBindingInput, msgs);
      if (msgs != null)
        msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(
        this,
        Notification.SET,
        WSDLPackage.BINDING_OPERATION__EBINDING_INPUT,
        newEBindingInput,
        newEBindingInput));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BindingOutput getEBindingOutput()
  {
    return eBindingOutput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEBindingOutput(BindingOutput newEBindingOutput, NotificationChain msgs)
  {
    BindingOutput oldEBindingOutput = eBindingOutput;
    eBindingOutput = newEBindingOutput;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(
        this,
        Notification.SET,
        WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT,
        oldEBindingOutput,
        newEBindingOutput);
      if (msgs == null)
        msgs = notification;
      else
        msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEBindingOutput(BindingOutput newEBindingOutput)
  {
    if (newEBindingOutput != eBindingOutput)
    {
      NotificationChain msgs = null;
      if (eBindingOutput != null)
        msgs = ((InternalEObject)eBindingOutput).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
          - WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT, null, msgs);
      if (newEBindingOutput != null)
        msgs = ((InternalEObject)newEBindingOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
          - WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT, null, msgs);
      msgs = basicSetEBindingOutput(newEBindingOutput, msgs);
      if (msgs != null)
        msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(
        this,
        Notification.SET,
        WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT,
        newEBindingOutput,
        newEBindingOutput));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEBindingFaults()
  {
    if (eBindingFaults == null)
    {
      eBindingFaults = new EObjectContainmentEList(BindingFault.class, this, WSDLPackage.BINDING_OPERATION__EBINDING_FAULTS);
    }
    return eBindingFaults;
  }

  /**
   * <!-- begin-user-doc -->
   * Add a fault binding.
   * @param fault the new fault binding
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addBindingFault(javax.wsdl.BindingFault bindingFault)
  {
    getEBindingFaults().add((BindingFault)bindingFault);
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified fault binding.
   * @param name the name of the desired fault binding.
   * @return the corresponding fault binding, or null if there wasn't
   * any matching fault binding
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.BindingFault getBindingFault(String name)
  {
    BindingFault result = null;
    for (Iterator i = getEBindingFaults().iterator(); i.hasNext();)
    {
      BindingFault fault = (BindingFault)i.next();
      if (name.equals(fault.getName()))
      {
        result = fault;
        break;
      }
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the fault bindings associated with this operation binding.
   * @return names of fault bindings
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getBindingFaults()
  {
    HashMap hashMap = new HashMap();
    for (Iterator i = getEBindingFaults().iterator(); i.hasNext();)
    {
      BindingFault bindingFault = (BindingFault)i.next();
      hashMap.put(bindingFault.getName(), bindingFault);
    }
    return hashMap;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Operation getOperation()
  {
    return getEOperation();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setOperation(javax.wsdl.Operation operation)
  {
    setEOperation((Operation)operation);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.BindingInput getBindingInput()
  {
    return getEBindingInput();
  }

  /**
   * <!-- begin-user-doc -->
   * Set the input of this operation binding.
   * @param input the desired input
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setBindingInput(javax.wsdl.BindingInput bindingInput)
  {
    setEBindingInput((BindingInput)bindingInput);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.BindingOutput getBindingOutput()
  {
    return getEBindingOutput();
  }

  /**
   * <!-- begin-user-doc -->
   * Set the output of this operation binding.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setBindingOutput(javax.wsdl.BindingOutput bindingOutput)
  {
    setEBindingOutput((BindingOutput)bindingOutput);
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
      case WSDLPackage.BINDING_OPERATION__EBINDING_INPUT:
      return basicSetEBindingInput(null, msgs);
      case WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT:
      return basicSetEBindingOutput(null, msgs);
      case WSDLPackage.BINDING_OPERATION__EBINDING_FAULTS:
      return ((InternalEList)getEBindingFaults()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.BINDING_OPERATION__NAME:
      return getName();
      case WSDLPackage.BINDING_OPERATION__EOPERATION:
      if (resolve)
        return getEOperation();
      return basicGetEOperation();
      case WSDLPackage.BINDING_OPERATION__EBINDING_INPUT:
      return getEBindingInput();
      case WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT:
      return getEBindingOutput();
      case WSDLPackage.BINDING_OPERATION__EBINDING_FAULTS:
      return getEBindingFaults();
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
      case WSDLPackage.BINDING_OPERATION__NAME:
      setName((String)newValue);
      return;
      case WSDLPackage.BINDING_OPERATION__EOPERATION:
      setEOperation((Operation)newValue);
      return;
      case WSDLPackage.BINDING_OPERATION__EBINDING_INPUT:
      setEBindingInput((BindingInput)newValue);
      return;
      case WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT:
      setEBindingOutput((BindingOutput)newValue);
      return;
      case WSDLPackage.BINDING_OPERATION__EBINDING_FAULTS:
      getEBindingFaults().clear();
      getEBindingFaults().addAll((Collection)newValue);
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
      case WSDLPackage.BINDING_OPERATION__NAME:
      setName(NAME_EDEFAULT);
      return;
      case WSDLPackage.BINDING_OPERATION__EOPERATION:
      setEOperation((Operation)null);
      return;
      case WSDLPackage.BINDING_OPERATION__EBINDING_INPUT:
      setEBindingInput((BindingInput)null);
      return;
      case WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT:
      setEBindingOutput((BindingOutput)null);
      return;
      case WSDLPackage.BINDING_OPERATION__EBINDING_FAULTS:
      getEBindingFaults().clear();
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
      case WSDLPackage.BINDING_OPERATION__NAME:
      return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case WSDLPackage.BINDING_OPERATION__EOPERATION:
      return eOperation != null;
      case WSDLPackage.BINDING_OPERATION__EBINDING_INPUT:
      return eBindingInput != null;
      case WSDLPackage.BINDING_OPERATION__EBINDING_OUTPUT:
      return eBindingOutput != null;
      case WSDLPackage.BINDING_OPERATION__EBINDING_FAULTS:
      return eBindingFaults != null && !eBindingFaults.isEmpty();
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
    result.append(" (name: "); //$NON-NLS-1$
    result.append(name);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcile methods
  //

  public void reconcileAttributes(Element changedElement)
  {
    String name = changedElement.getAttribute("name");
    if (name != null)
    {
      setName(name);
    }
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    Definition definition = getEnclosingDefinition();

    switch (WSDLUtil.getInstance().getWSDLType(child))
    {
      case WSDLConstants.INPUT:
      {
        BindingInput input = WSDLFactory.eINSTANCE.createBindingInput();
        input.setEnclosingDefinition(definition);
        input.setElement(child);
        setBindingInput(input);
        break;
      }
      case WSDLConstants.OUTPUT:
      {
        BindingOutput output = WSDLFactory.eINSTANCE.createBindingOutput();
        output.setEnclosingDefinition(definition);
        output.setElement(child);
        setBindingOutput(output);
        break;
      }
      case WSDLConstants.FAULT:
      {
        BindingFault fault = WSDLFactory.eINSTANCE.createBindingFault();
        fault.setEnclosingDefinition(definition);
        fault.setElement(child);
        addBindingFault(fault);
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
    BindingOperation bindingOperation = (BindingOperation)component;
    if (modelObject instanceof BindingInput)
    {
      bindingOperation.setEBindingInput(null);
    }
    else if (modelObject instanceof BindingOutput)
    {
      bindingOperation.setEBindingOutput(null);
    }
    else if (modelObject instanceof BindingFault)
    {
      bindingOperation.getEBindingFaults().remove(modelObject);
    }
    else
    {
      bindingOperation.getEExtensibilityElements().remove(modelObject);
    }
  }

  public Collection getModelObjects(Object component)
  {
    BindingOperation bindingOperation = (BindingOperation)component;

    List list = new ArrayList();

    if (bindingOperation.getEBindingInput() != null)
    {
      list.add(bindingOperation.getEBindingInput());
    }

    if (bindingOperation.getEBindingOutput() != null)
    {
      list.add(bindingOperation.getEBindingOutput());
    }

    list.addAll(bindingOperation.getEBindingFaults());
    list.addAll(bindingOperation.getEExtensibilityElements());

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
      if (eAttribute == null || eAttribute == WSDLPackage.Literals.BINDING_OPERATION__NAME)
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getName());
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.OPERATION);
    setElement(newElement);

    Iterator iterator = getExtensibilityElements().iterator();
    while (iterator.hasNext())
    {
      ExtensibilityElement extensibilityElement = (ExtensibilityElement)iterator.next();
      Element child = ((ExtensibilityElementImpl)extensibilityElement).createElement();
      newElement.appendChild(child);
    }

    BindingInput input = getEBindingInput();
    if (input != null)
    {
      Element child = ((BindingInputImpl)input).createElement();
      newElement.appendChild(child);
    }

    BindingOutput output = getEBindingOutput();
    if (output != null)
    {
      Element child = ((BindingOutputImpl)output).createElement();
      newElement.appendChild(child);
    }

    iterator = getEBindingFaults().iterator();
    while (iterator.hasNext())
    {
      BindingFault fault = (BindingFault)iterator.next();
      Element child = ((BindingFaultImpl)fault).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  }

  // Resolve the reference to Operation
  public void reconcileReferences(boolean deep)
  {
    if (element != null)
    {
      Binding binding = (Binding)getContainer();
      PortType portType = binding.getEPortType();
      if (portType != null)
      {
        BindingInput input = getEBindingInput();
        BindingOutput output = getEBindingOutput();

        String inputName = input != null ? input.getName() : null;
        String outputName = output != null ? output.getName() : null;

        setOperation(portType.getOperation(getName(), inputName, outputName));
      }
    }
    super.reconcileReferences(deep);
  }
} //BindingOperationImpl
