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
import org.w3c.dom.Document;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Definition</b></em>'.
 * @since 1.0
 * @ignore
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL definitions element. The WSDL definitions element is the root element of a WSDL document.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getTargetNamespace <em>Target Namespace</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getEImports <em>EImports</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getETypes <em>ETypes</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getEMessages <em>EMessages</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getEPortTypes <em>EPort Types</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getEBindings <em>EBindings</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getEServices <em>EServices</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.Definition#getENamespaces <em>ENamespaces</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition()
 * @model superTypes="org.eclipse.wst.wsdl.ExtensibleElement org.eclipse.wst.wsdl.IDefinition"
 * @generated
 */
public interface Definition extends ExtensibleElement, javax.wsdl.Definition
{
  /**
   * Returns the value of the '<em><b>Target Namespace</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target Namespace</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target Namespace</em>' attribute.
   * @see #setTargetNamespace(String)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_TargetNamespace()
   * @model
   * @generated
   */
  String getTargetNamespace();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.Definition#getTargetNamespace <em>Target Namespace</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target Namespace</em>' attribute.
   * @see #getTargetNamespace()
   * @generated
   */
  void setTargetNamespace(String value);

  /**
   * Returns the value of the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Location</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location</em>' attribute.
   * @see #setLocation(String)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_Location()
   * @model
   * @generated
   */
  String getLocation();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.Definition#getLocation <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location</em>' attribute.
   * @see #getLocation()
   * @generated
   */
  void setLocation(String value);

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
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_QName()
   * @model dataType="org.eclipse.wst.wsdl.QName"
   * @generated
   */
  QName getQName();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.Definition#getQName <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>QName</em>' attribute.
   * @see #getQName()
   * @generated
   */
  void setQName(QName value);

  /**
   * Returns the value of the '<em><b>Encoding</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Encoding</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Encoding</em>' attribute.
   * @see #setEncoding(String)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_Encoding()
   * @model
   * @generated
   */
  String getEncoding();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.Definition#getEncoding <em>Encoding</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Encoding</em>' attribute.
   * @see #getEncoding()
   * @generated
   */
  void setEncoding(String value);

  /**
   * Returns the value of the '<em><b>EMessages</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.Message}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EMessages</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EMessages</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_EMessages()
   * @model type="org.eclipse.wst.wsdl.Message" containment="true"
   * @generated
   */
  EList getEMessages();

  /**
   * Returns the value of the '<em><b>EPort Types</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.PortType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EPort Types</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EPort Types</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_EPortTypes()
   * @model type="org.eclipse.wst.wsdl.PortType" containment="true"
   * @generated
   */
  EList getEPortTypes();

  /**
   * Returns the value of the '<em><b>EBindings</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.Binding}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EBindings</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EBindings</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_EBindings()
   * @model type="org.eclipse.wst.wsdl.Binding" containment="true"
   * @generated
   */
  EList getEBindings();

  /**
   * Returns the value of the '<em><b>EServices</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.Service}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EServices</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EServices</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_EServices()
   * @model type="org.eclipse.wst.wsdl.Service" containment="true"
   * @generated
   */
  EList getEServices();

  /**
   * Returns the value of the '<em><b>ENamespaces</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.Namespace}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>ENamespaces</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>ENamespaces</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_ENamespaces()
   * @model type="org.eclipse.wst.wsdl.Namespace" containment="true"
   * @generated
   */
  EList getENamespaces();

  /**
   * Returns the value of the '<em><b>ETypes</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>ETypes</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>ETypes</em>' containment reference.
   * @see #setETypes(Types)
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_ETypes()
   * @model containment="true"
   * @generated
   */
  Types getETypes();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.Definition#getETypes <em>ETypes</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>ETypes</em>' containment reference.
   * @see #getETypes()
   * @generated
   */
  void setETypes(Types value);

  /**
   * Returns the value of the '<em><b>EImports</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.Import}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>EImports</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>EImports</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.WSDLPackage#getDefinition_EImports()
   * @model type="org.eclipse.wst.wsdl.Import" containment="true"
   * @generated
   */
  EList getEImports();

  /**
   * <!-- begin-user-doc -->
   * Returns a W3C DOM Document built from the input WSDL document.
   * @return W3C DOM Document.
   * @ignore
   * <!-- end-user-doc -->
   * @model kind="operation" dataType="org.eclipse.wst.wsdl.DOMDocument"
   * @generated
   */
  Document getDocument();

  /**
   * <!-- begin-user-doc -->
   * Sets a W3C DOM Document to be used when building up a DOM Element tree for the WSDL model.
   * @param document W3C DOM Document.
   * @ignore
   * <!-- end-user-doc -->
   * @model documentDataType="org.eclipse.wst.wsdl.DOMDocument"
   * @generated
   */
  void setDocument(Document document);

} // Definition
