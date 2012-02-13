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


import javax.wsdl.extensions.AttributeExtensible;
import javax.wsdl.extensions.ElementExtensible;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extensible Element</b></em>'.
 * @since 1.0
 * @ignore
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *  WSDL allows elements representing a specific technology (referred to here as extensibility elements) under various elements defined by WSDL. This class represents a WSDL point of extensibility.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.ExtensibleElement#getEExtensibilityElements <em>EExtensibility Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.WSDLPackage#getExtensibleElement()
 * @model abstract="true" superTypes="org.eclipse.wst.wsdl.WSDLElement org.eclipse.wst.wsdl.IElementExtensible org.eclipse.wst.wsdl.IAttributeExtensible"
 * @generated
 */
public interface ExtensibleElement extends WSDLElement, ElementExtensible, AttributeExtensible
{
  /**
   * Returns the value of the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.ExtensibilityElement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EExtensibility Elements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EExtensibility Elements</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getExtensibleElement_EExtensibilityElements()
   * @model type="org.eclipse.wst.wsdl.ExtensibilityElement" containment="true"
   * @generated
   */
  EList getEExtensibilityElements();

} // ExtensibleElement
