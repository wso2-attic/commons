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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extensibility Element</b></em>'.
 * @since 1.0
 * @ignore
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL extensibility element. WSDL allows extensibility elements representing a specific technology under various elements defined by WSDL.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.ExtensibilityElement#isRequired <em>Required</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.ExtensibilityElement#getElementType <em>Element Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.WSDLPackage#getExtensibilityElement()
 * @model superTypes="org.eclipse.wst.wsdl.WSDLElement org.eclipse.wst.wsdl.IExtensibilityElement"
 * @generated
 */
public interface ExtensibilityElement extends WSDLElement, javax.wsdl.extensions.ExtensibilityElement
{
  /**
   * Returns the value of the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Required</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Required</em>' attribute.
   * @see #setRequired(boolean)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getExtensibilityElement_Required()
   * @model
   * @generated
   */
  boolean isRequired();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.ExtensibilityElement#isRequired <em>Required</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Required</em>' attribute.
   * @see #isRequired()
   * @generated
   */
  void setRequired(boolean value);

  /**
   * Returns the value of the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Element Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Element Type</em>' attribute.
   * @see #setElementType(QName)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getExtensibilityElement_ElementType()
   * @model dataType="org.eclipse.wst.wsdl.QName" transient="true"
   * @generated
   */
  QName getElementType();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.ExtensibilityElement#getElementType <em>Element Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element Type</em>' attribute.
   * @see #getElementType()
   * @generated
   */
  void setElementType(QName value);

} // ExtensibilityElement
