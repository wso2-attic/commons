/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap;


import org.eclipse.wst.wsdl.ExtensibilityElement;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fault</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPFault()
 * @model features="eEncodingStyles" 
 *        eEncodingStylesDataType="org.eclipse.wst.wsdl.binding.soap.IString" eEncodingStylesMany="true" eEncodingStylesSuppressedGetVisibility="true" superTypes="org.eclipse.wst.wsdl.ExtensibilityElement org.eclipse.wst.wsdl.binding.soap.ISOAPFault"
 * @generated
 */
public interface SOAPFault extends ExtensibilityElement, javax.wsdl.extensions.soap.SOAPFault
{

  /**
   * Returns the value of the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Use</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Use</em>' attribute.
   * @see #setUse(String)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPFault_Use()
   * @model
   * @generated
   */
  String getUse();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getUse <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Use</em>' attribute.
   * @see #getUse()
   * @generated
   */
  void setUse(String value);

  /**
   * Returns the value of the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Namespace URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Namespace URI</em>' attribute.
   * @see #setNamespaceURI(String)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPFault_NamespaceURI()
   * @model
   * @generated
   */
  String getNamespaceURI();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getNamespaceURI <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Namespace URI</em>' attribute.
   * @see #getNamespaceURI()
   * @generated
   */
  void setNamespaceURI(String value);

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
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPFault_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // SOAPFault
