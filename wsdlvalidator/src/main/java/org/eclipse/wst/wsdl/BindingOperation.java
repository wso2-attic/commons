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
package org.eclipse.wst.wsdl;


import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binding Operation</b></em>'.
 * @since 1.0
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL operation element within a binding. An operation element within a binding specifies binding information for the operation with the same name within the binding's portType. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.BindingOperation#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.BindingOperation#getEOperation <em>EOperation</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.BindingOperation#getEBindingInput <em>EBinding Input</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.BindingOperation#getEBindingOutput <em>EBinding Output</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.BindingOperation#getEBindingFaults <em>EBinding Faults</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.WSDLPackage#getBindingOperation()
 * @model superTypes="org.eclipse.wst.wsdl.ExtensibleElement org.eclipse.wst.wsdl.IBindingOperation"
 * @generated
 */
public interface BindingOperation extends ExtensibleElement, javax.wsdl.BindingOperation
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getBindingOperation_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.BindingOperation#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>EOperation</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EOperation</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EOperation</em>' reference.
   * @see #setEOperation(Operation)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getBindingOperation_EOperation()
   * @model required="true"
   * @generated
   */
  Operation getEOperation();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.BindingOperation#getEOperation <em>EOperation</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>EOperation</em>' reference.
   * @see #getEOperation()
   * @generated
   */
  void setEOperation(Operation value);

  /**
   * Returns the value of the '<em><b>EBinding Input</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EBinding Input</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EBinding Input</em>' containment reference.
   * @see #setEBindingInput(BindingInput)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getBindingOperation_EBindingInput()
   * @model containment="true"
   * @generated
   */
  BindingInput getEBindingInput();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.BindingOperation#getEBindingInput <em>EBinding Input</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>EBinding Input</em>' containment reference.
   * @see #getEBindingInput()
   * @generated
   */
  void setEBindingInput(BindingInput value);

  /**
   * Returns the value of the '<em><b>EBinding Output</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EBinding Output</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EBinding Output</em>' containment reference.
   * @see #setEBindingOutput(BindingOutput)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getBindingOperation_EBindingOutput()
   * @model containment="true"
   * @generated
   */
  BindingOutput getEBindingOutput();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.BindingOperation#getEBindingOutput <em>EBinding Output</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>EBinding Output</em>' containment reference.
   * @see #getEBindingOutput()
   * @generated
   */
  void setEBindingOutput(BindingOutput value);

  /**
   * Returns the value of the '<em><b>EBinding Faults</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.BindingFault}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EBinding Faults</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EBinding Faults</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getBindingOperation_EBindingFaults()
   * @model type="org.eclipse.wst.wsdl.BindingFault" containment="true"
   * @generated
   */
  EList getEBindingFaults();

} // BindingOperation
