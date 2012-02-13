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
package org.eclipse.wst.wsdl.binding.mime;


import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Part;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mime Xml</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml#getEPart <em>EPart</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.binding.mime.MIMEPackage#getMIMEMimeXml()
 * @model superTypes="org.eclipse.wst.wsdl.ExtensibilityElement org.eclipse.wst.wsdl.binding.mime.IMIMEMimeXml"
 * @generated
 */
public interface MIMEMimeXml extends ExtensibilityElement, javax.wsdl.extensions.mime.MIMEMimeXml
{
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
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEPackage#getMIMEMimeXml_EPart()
   * @model
   * @generated
   */
  Part getEPart();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml#getEPart <em>EPart</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>EPart</em>' reference.
   * @see #getEPart()
   * @generated
   */
  void setEPart(Part value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void setPart(String part);

  /**
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Part</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @model kind="operation"
   * @generated
   */
  String getPart();

} // MIMEMimeXml
