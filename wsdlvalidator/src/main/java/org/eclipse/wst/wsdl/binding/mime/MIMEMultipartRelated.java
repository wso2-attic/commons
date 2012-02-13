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


import java.util.List;

import javax.wsdl.extensions.mime.MIMEPart;

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.wsdl.ExtensibilityElement;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multipart Related</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated#getEMIMEPart <em>EMIME Part</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.binding.mime.MIMEPackage#getMIMEMultipartRelated()
 * @model superTypes="org.eclipse.wst.wsdl.ExtensibilityElement org.eclipse.wst.wsdl.binding.mime.IMIMEMultipartRelated"
 * @generated
 */
public interface MIMEMultipartRelated extends ExtensibilityElement, javax.wsdl.extensions.mime.MIMEMultipartRelated
{
  /**
   * Returns the value of the '<em><b>EMIME Part</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.binding.mime.MIMEPart}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EMIME Part</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EMIME Part</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEPackage#getMIMEMultipartRelated_EMIMEPart()
   * @model type="org.eclipse.wst.wsdl.binding.mime.MIMEPart" containment="true"
   * @generated
   */
  EList getEMIMEPart();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model mimePartType="org.eclipse.wst.wsdl.binding.mime.IMIMEPart"
   * @generated
   */
  void addMIMEPart(MIMEPart mimePart);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation" dataType="org.eclipse.wst.wsdl.binding.mime.List" many="false"
   * @generated
   */
  List getMIMEParts();

} // MIMEMultipartRelated
