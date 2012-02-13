/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl;


import org.eclipse.emf.ecore.EObject;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * @since 1.0
 * @ignore
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL language element.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.WSDLElement#getDocumentationElement <em>Documentation Element</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.WSDLElement#getElement <em>Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.WSDLPackage#getWSDLElement()
 * @model abstract="true"
 * @generated
 */
public interface WSDLElement extends EObject
{
  /**
   * Returns the value of the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Documentation Element</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Documentation Element</em>' attribute.
   * @see #setDocumentationElement(Element)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getWSDLElement_DocumentationElement()
   * @model dataType="org.eclipse.wst.wsdl.DOMElement"
   * @generated
   */
  Element getDocumentationElement();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.WSDLElement#getDocumentationElement <em>Documentation Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Documentation Element</em>' attribute.
   * @see #getDocumentationElement()
   * @generated
   */
  void setDocumentationElement(Element value);

  /**
   * Returns the value of the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * This is the underlying DOM element associated with this component, 
   * i.e., the {@link #updateElement() serialization} of this component.
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Element</em>' attribute.
   * @see #setElement(Element)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getWSDLElement_Element()
   * @model dataType="org.eclipse.wst.wsdl.DOMElement" transient="true"
   * @generated
   */
  Element getElement();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.WSDLElement#getElement <em>Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element</em>' attribute.
   * @see #getElement()
   * @generated
   */
  void setElement(Element value);

  /**
   * <!-- begin-user-doc -->
   * Returns the Definition that contains this WSDL element.
   * @return the Definition that contains this WSDL element.
   * <!-- end-user-doc -->
   * @model kind="operation"
   * @generated
   */
  Definition getEnclosingDefinition();

  /**
   * <!-- begin-user-doc -->
   * Sets the enclosing definition of this WSDL element.
   * @param definition the Definition that contains this WSDL element.
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void setEnclosingDefinition(Definition definition);

  /**
   * Ensures that the underlying DOM {@link #getElement element} both exists and is up-to-date with respect to the model,
   * i.e., it serializes the component and maintains an association with that serialization.
   * @see #updateElement(boolean)
   * @see #getElement
   */
  void updateElement();

  /**
   * Ensures that the underlying DOM {@link #getElement element} both exists and is up-to-date with respect to the model,
   * i.e., it serializes the component and maintains an association with that serialization.
   * For <code>deep == true</code>, 
   * or when <code>{@link #getElement getElement()} == null</code>, 
   * this does the same thing as {@link #updateElement()};
   * otherwise, it updates just the DOM element associated with this component to reflect the current state.
   * @see #updateElement()
   * @see #getElement
   */
  void updateElement(boolean deep);

  /**
   * Returns the value of the '<em><b>Container</b></em>' reference.
   * This represents the concrete container of this component, 
   * i.e., the inverse of the black diamond relations in the UML model.
   * @return the value of the '<em>Container</em>' reference.
   */
  WSDLElement getContainer();

  /**
   * Called to indicate that the given element has changed;
   * the element should typically be the same as the one returned {@link #getElement}.
   * It is expected that clients will not call this themselves 
   * since the DOM event listeners attached to the underlying DOM will invoke these automatically.
   * @param changedElement the DOM element changed for this instance.
   */
  void elementChanged(Element changedElement);

} // WSDLElement
