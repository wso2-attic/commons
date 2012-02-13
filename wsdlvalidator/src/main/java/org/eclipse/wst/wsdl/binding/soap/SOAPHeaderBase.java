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


import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Header Base</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEEncodingStyles <em>EEncoding Styles</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getMessage <em>Message</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getPart <em>Part</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEMessage <em>EMessage</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEPart <em>EPart</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase()
 * @model
 * @generated
 */
public interface SOAPHeaderBase extends ExtensibilityElement
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
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_Use()
   * @model
   * @generated
   */
  String getUse();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getUse <em>Use</em>}' attribute.
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
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_NamespaceURI()
   * @model
   * @generated
   */
  String getNamespaceURI();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getNamespaceURI <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Namespace URI</em>' attribute.
   * @see #getNamespaceURI()
   * @generated
   */
  void setNamespaceURI(String value);

  /**
   * Returns the value of the '<em><b>EEncoding Styles</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EEncoding Styles</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EEncoding Styles</em>' attribute list.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_EEncodingStyles()
   * @model dataType="org.eclipse.wst.wsdl.binding.soap.IString"
   * @generated
   */
  EList getEEncodingStyles();

  /**
   * Returns the value of the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EMessage</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EMessage</em>' reference.
   * @see #setEMessage(Message)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_EMessage()
   * @model required="true"
   * @generated
   */
  Message getEMessage();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEMessage <em>EMessage</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>EMessage</em>' reference.
   * @see #getEMessage()
   * @generated
   */
  void setEMessage(Message value);

  /**
   * Returns the value of the '<em><b>EPart</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EPart</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EPart</em>' reference.
   * @see #setEPart(Part)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_EPart()
   * @model required="true"
   * @generated
   */
  Part getEPart();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEPart <em>EPart</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>EPart</em>' reference.
   * @see #getEPart()
   * @generated
   */
  void setEPart(Part value);

  /**
   * Returns the value of the '<em><b>Message</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message</em>' attribute.
   * @see #setMessage(QName)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_Message()
   * @model dataType="org.eclipse.wst.wsdl.QName"
   * @generated
   */
  QName getMessage();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getMessage <em>Message</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message</em>' attribute.
   * @see #getMessage()
   * @generated
   */
  void setMessage(QName value);

  /**
   * Returns the value of the '<em><b>Part</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Part</em>' attribute.
   * @see #setPart(String)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_Part()
   * @model
   * @generated
   */
  String getPart();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getPart <em>Part</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Part</em>' attribute.
   * @see #getPart()
   * @generated
   */
  void setPart(String value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation" type="org.eclipse.wst.wsdl.IList" many="false"
   * @generated
   */
  List getEncodingStyles();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model encodingStylesType="org.eclipse.wst.wsdl.IList" encodingStylesMany="false"
   * @generated
   */
  void setEncodingStyles(List encodingStyles);

} // SOAPHeaderBase
