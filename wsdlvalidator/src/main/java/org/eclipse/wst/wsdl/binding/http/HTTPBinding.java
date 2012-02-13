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
package org.eclipse.wst.wsdl.binding.http;


import org.eclipse.wst.wsdl.ExtensibilityElement;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.http.HTTPBinding#getVerb <em>Verb</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.binding.http.HTTPPackage#getHTTPBinding()
 * @model superTypes="org.eclipse.wst.wsdl.ExtensibilityElement org.eclipse.wst.wsdl.binding.http.IHTTPBinding"
 * @generated
 */
public interface HTTPBinding extends ExtensibilityElement, javax.wsdl.extensions.http.HTTPBinding
{
  /**
   * Returns the value of the '<em><b>Verb</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Verb</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Verb</em>' attribute.
   * @see #setVerb(String)
   * @see org.eclipse.wst.wsdl.binding.http.HTTPPackage#getHTTPBinding_Verb()
   * @model
   * @generated
   */
  String getVerb();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.http.HTTPBinding#getVerb <em>Verb</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Verb</em>' attribute.
   * @see #getVerb()
   * @generated
   */
  void setVerb(String value);

} // HTTPBinding
