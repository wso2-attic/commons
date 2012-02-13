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


import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service</b></em>'.
 * @since 1.0
 * @ignore
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL service element. A service groups a set of related ports together.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.Service#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Service#isUndefined <em>Undefined</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Service#getEPorts <em>EPorts</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.WSDLPackage#getService()
 * @model superTypes="org.eclipse.wst.wsdl.ExtensibleElement org.eclipse.wst.wsdl.IService"
 * @generated
 */
public interface Service extends ExtensibleElement, javax.wsdl.Service
{
  /**
   * Returns the value of the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>QName</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>QName</em>' attribute.
   * @see #setQName(QName)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getService_QName()
   * @model dataType="org.eclipse.wst.wsdl.QName"
   * @generated
   */
  QName getQName();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.Service#getQName <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>QName</em>' attribute.
   * @see #getQName()
   * @generated
   */
  void setQName(QName value);

  /**
   * Returns the value of the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Undefined</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Undefined</em>' attribute.
   * @see #setUndefined(boolean)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getService_Undefined()
   * @model
   * @generated
   */
  boolean isUndefined();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.Service#isUndefined <em>Undefined</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Undefined</em>' attribute.
   * @see #isUndefined()
   * @generated
   */
  void setUndefined(boolean value);

  /**
   * Returns the value of the '<em><b>EPorts</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.Port}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EPorts</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EPorts</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getService_EPorts()
   * @model type="org.eclipse.wst.wsdl.Port" containment="true"
   * @generated
   */
  EList getEPorts();

} // Service
