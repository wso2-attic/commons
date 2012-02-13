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


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;


/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * The WSDL model contains classes for the Web Services Description Language (WSDL).
 * 
 * WSDL describes network services as sets of endpoints operating on messages. The operations and messages are described abstractly, and then bound to a concrete network protocol and message format to define an endpoint.
 * 
 * WSDL describes the formats of the messages exchanged by the services, and supports the XML Schemas specification as its canonical type system. This package uses an XML Schema Infoset model package (see the XSD package) to describe the abstract message formats.
 * 
 * The model contains the following diagrams, named after the corresponding chapters in the WSDL 1.1 specification (http://www.w3.org/TR/2001/NOTE-wsdl-20010315)
 * - 2.1 Definition, shows the WSDL definition element and the WSDL document structure
 * - 2.1.1 Naming and Linking, shows the namespace and import mechanism
 * - 2.1.3 Extensibility, shows the WSDL extensibility mechanism
 * - 2.2 Types, shows the use of XML Schema types in WSDL
 * - 2.3 Messages, 2.4 PortTypes, 2.5 Bindings and 2.7 Services, show the major WSDL elements and their relations.
 * 
 * The WSDL classes extend the javax.wsdl interfaces defined by JSR 110. Classes with interface and datatype stereotypes are used to represent these non-MOF interfaces.
 * <!-- end-model-doc -->
 * @see org.eclipse.wst.wsdl.WSDLFactory
 * @model kind="package"
 * @generated
 */
public interface WSDLPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "wsdl"; //$NON-NLS-1$

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/wsdl/2003/WSDL"; //$NON-NLS-1$

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "wsdl"; //$NON-NLS-1$

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  WSDLPackage eINSTANCE = org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl <em>Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getWSDLElement()
   * @generated
   */
  int WSDL_ELEMENT = 0;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WSDL_ELEMENT__DOCUMENTATION_ELEMENT = 0;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WSDL_ELEMENT__ELEMENT = 1;

  /**
   * The number of structural features of the '<em>Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WSDL_ELEMENT_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.ExtensibleElementImpl <em>Extensible Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.ExtensibleElementImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getExtensibleElement()
   * @generated
   */
  int EXTENSIBLE_ELEMENT = 12;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBLE_ELEMENT__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Extensible Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBLE_ELEMENT_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl <em>Port Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.PortTypeImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getPortType()
   * @generated
   */
  int PORT_TYPE = 1;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_TYPE__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_TYPE__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_TYPE__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_TYPE__QNAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_TYPE__UNDEFINED = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EOperations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_TYPE__EOPERATIONS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Port Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_TYPE_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.OperationImpl <em>Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.OperationImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getOperation()
   * @generated
   */
  int OPERATION = 2;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Style</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__STYLE = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__UNDEFINED = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>EInput</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__EINPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EOutput</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__EOUTPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>EFaults</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__EFAULTS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>EParameter Ordering</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__EPARAMETER_ORDERING = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.MessageImpl <em>Message</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.MessageImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getMessage()
   * @generated
   */
  int MESSAGE = 3;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE__QNAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE__UNDEFINED = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EParts</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE__EPARTS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Message</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.PartImpl <em>Part</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.PartImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getPart()
   * @generated
   */
  int PART = 4;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Element Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__ELEMENT_NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__TYPE_NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Type Definition</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__TYPE_DEFINITION = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Element Declaration</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__ELEMENT_DECLARATION = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__EMESSAGE = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the '<em>Part</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingImpl <em>Binding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.BindingImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBinding()
   * @generated
   */
  int BINDING = 5;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__QNAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__UNDEFINED = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EPort Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__EPORT_TYPE = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>EBinding Operations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__EBINDING_OPERATIONS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Binding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl <em>Binding Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBindingOperation()
   * @generated
   */
  int BINDING_OPERATION = 6;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EOperation</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__EOPERATION = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EBinding Input</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__EBINDING_INPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>EBinding Output</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__EBINDING_OUTPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EBinding Faults</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__EBINDING_FAULTS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Binding Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.ServiceImpl <em>Service</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.ServiceImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getService()
   * @generated
   */
  int SERVICE = 7;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__QNAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__UNDEFINED = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EPorts</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__EPORTS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Service</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.PortImpl <em>Port</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.PortImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getPort()
   * @generated
   */
  int PORT = 8;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EBinding</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT__EBINDING = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Port</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl <em>Extensibility Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getExtensibilityElement()
   * @generated
   */
  int EXTENSIBILITY_ELEMENT = 9;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBILITY_ELEMENT__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBILITY_ELEMENT__REQUIRED = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBILITY_ELEMENT__ELEMENT_TYPE = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Extensibility Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBILITY_ELEMENT_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl <em>Definition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.DefinitionImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getDefinition()
   * @generated
   */
  int DEFINITION = 10;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__TARGET_NAMESPACE = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__LOCATION = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__QNAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Encoding</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__ENCODING = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EImports</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__EIMPORTS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>ETypes</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__ETYPES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>EMessages</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__EMESSAGES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>EPort Types</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__EPORT_TYPES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>EBindings</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__EBINDINGS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>EServices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__ESERVICES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 9;

  /**
   * The feature id for the '<em><b>ENamespaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__ENAMESPACES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 10;

  /**
   * The number of structural features of the '<em>Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 11;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.ImportImpl <em>Import</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.ImportImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getImport()
   * @generated
   */
  int IMPORT = 11;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__NAMESPACE_URI = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Location URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__LOCATION_URI = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EDefinition</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__EDEFINITION = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>ESchema</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__ESCHEMA = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Import</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.MessageReferenceImpl <em>Message Reference</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.MessageReferenceImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getMessageReference()
   * @generated
   */
  int MESSAGE_REFERENCE = 46;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE__EMESSAGE = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Message Reference</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.InputImpl <em>Input</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.InputImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getInput()
   * @generated
   */
  int INPUT = 13;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT__DOCUMENTATION_ELEMENT = MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT__ELEMENT = MESSAGE_REFERENCE__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT__EEXTENSIBILITY_ELEMENTS = MESSAGE_REFERENCE__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT__NAME = MESSAGE_REFERENCE__NAME;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT__EMESSAGE = MESSAGE_REFERENCE__EMESSAGE;

  /**
   * The number of structural features of the '<em>Input</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT_FEATURE_COUNT = MESSAGE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.OutputImpl <em>Output</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.OutputImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getOutput()
   * @generated
   */
  int OUTPUT = 14;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT__DOCUMENTATION_ELEMENT = MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT__ELEMENT = MESSAGE_REFERENCE__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT__EEXTENSIBILITY_ELEMENTS = MESSAGE_REFERENCE__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT__NAME = MESSAGE_REFERENCE__NAME;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT__EMESSAGE = MESSAGE_REFERENCE__EMESSAGE;

  /**
   * The number of structural features of the '<em>Output</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT_FEATURE_COUNT = MESSAGE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.FaultImpl <em>Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.FaultImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getFault()
   * @generated
   */
  int FAULT = 15;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FAULT__DOCUMENTATION_ELEMENT = MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FAULT__ELEMENT = MESSAGE_REFERENCE__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FAULT__EEXTENSIBILITY_ELEMENTS = MESSAGE_REFERENCE__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FAULT__NAME = MESSAGE_REFERENCE__NAME;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FAULT__EMESSAGE = MESSAGE_REFERENCE__EMESSAGE;

  /**
   * The number of structural features of the '<em>Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FAULT_FEATURE_COUNT = MESSAGE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingInputImpl <em>Binding Input</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.BindingInputImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBindingInput()
   * @generated
   */
  int BINDING_INPUT = 16;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_INPUT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_INPUT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_INPUT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_INPUT__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EInput</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_INPUT__EINPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Binding Input</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_INPUT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingOutputImpl <em>Binding Output</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.BindingOutputImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBindingOutput()
   * @generated
   */
  int BINDING_OUTPUT = 17;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OUTPUT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OUTPUT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OUTPUT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OUTPUT__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EOutput</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OUTPUT__EOUTPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Binding Output</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OUTPUT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingFaultImpl <em>Binding Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.BindingFaultImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBindingFault()
   * @generated
   */
  int BINDING_FAULT = 18;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FAULT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FAULT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FAULT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FAULT__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EFault</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FAULT__EFAULT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Binding Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FAULT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.NamespaceImpl <em>Namespace</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.NamespaceImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getNamespace()
   * @generated
   */
  int NAMESPACE = 19;

  /**
   * The feature id for the '<em><b>URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMESPACE__URI = 0;

  /**
   * The feature id for the '<em><b>Prefix</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMESPACE__PREFIX = 1;

  /**
   * The number of structural features of the '<em>Namespace</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NAMESPACE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.AttributeExtensible <em>IAttribute Extensible</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.AttributeExtensible
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIAttributeExtensible()
   * @generated
   */
  int IATTRIBUTE_EXTENSIBLE = 48;

  /**
   * The number of structural features of the '<em>IAttribute Extensible</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IATTRIBUTE_EXTENSIBLE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.PortType <em>IPort Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.PortType
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIPortType()
   * @generated
   */
  int IPORT_TYPE = 20;

  /**
   * The number of structural features of the '<em>IPort Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IPORT_TYPE_FEATURE_COUNT = IATTRIBUTE_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.ElementExtensible <em>IElement Extensible</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.ElementExtensible
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIElementExtensible()
   * @generated
   */
  int IELEMENT_EXTENSIBLE = 47;

  /**
   * The number of structural features of the '<em>IElement Extensible</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IELEMENT_EXTENSIBLE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Operation <em>IOperation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Operation
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIOperation()
   * @generated
   */
  int IOPERATION = 21;

  /**
   * The number of structural features of the '<em>IOperation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IOPERATION_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Input <em>IInput</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Input
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIInput()
   * @generated
   */
  int IINPUT = 22;

  /**
   * The number of structural features of the '<em>IInput</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IINPUT_FEATURE_COUNT = IATTRIBUTE_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Output <em>IOutput</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Output
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIOutput()
   * @generated
   */
  int IOUTPUT = 23;

  /**
   * The number of structural features of the '<em>IOutput</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IOUTPUT_FEATURE_COUNT = IATTRIBUTE_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Fault <em>IFault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Fault
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIFault()
   * @generated
   */
  int IFAULT = 24;

  /**
   * The number of structural features of the '<em>IFault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IFAULT_FEATURE_COUNT = IATTRIBUTE_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Message <em>IMessage</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Message
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIMessage()
   * @generated
   */
  int IMESSAGE = 25;

  /**
   * The number of structural features of the '<em>IMessage</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMESSAGE_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Part <em>IPart</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Part
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIPart()
   * @generated
   */
  int IPART = 26;

  /**
   * The number of structural features of the '<em>IPart</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IPART_FEATURE_COUNT = IATTRIBUTE_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Service <em>IService</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Service
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIService()
   * @generated
   */
  int ISERVICE = 27;

  /**
   * The number of structural features of the '<em>IService</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISERVICE_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Port <em>IPort</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Port
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIPort()
   * @generated
   */
  int IPORT = 28;

  /**
   * The number of structural features of the '<em>IPort</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IPORT_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Binding <em>IBinding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Binding
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBinding()
   * @generated
   */
  int IBINDING = 29;

  /**
   * The number of structural features of the '<em>IBinding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IBINDING_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.BindingOperation <em>IBinding Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.BindingOperation
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBindingOperation()
   * @generated
   */
  int IBINDING_OPERATION = 30;

  /**
   * The number of structural features of the '<em>IBinding Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IBINDING_OPERATION_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.BindingInput <em>IBinding Input</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.BindingInput
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBindingInput()
   * @generated
   */
  int IBINDING_INPUT = 31;

  /**
   * The number of structural features of the '<em>IBinding Input</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IBINDING_INPUT_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.BindingOutput <em>IBinding Output</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.BindingOutput
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBindingOutput()
   * @generated
   */
  int IBINDING_OUTPUT = 32;

  /**
   * The number of structural features of the '<em>IBinding Output</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IBINDING_OUTPUT_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.BindingFault <em>IBinding Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.BindingFault
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBindingFault()
   * @generated
   */
  int IBINDING_FAULT = 33;

  /**
   * The number of structural features of the '<em>IBinding Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IBINDING_FAULT_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.ExtensibilityElement <em>IExtensibility Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.ExtensibilityElement
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIExtensibilityElement()
   * @generated
   */
  int IEXTENSIBILITY_ELEMENT = 34;

  /**
   * The number of structural features of the '<em>IExtensibility Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IEXTENSIBILITY_ELEMENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Definition <em>IDefinition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Definition
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIDefinition()
   * @generated
   */
  int IDEFINITION = 35;

  /**
   * The number of structural features of the '<em>IDefinition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IDEFINITION_FEATURE_COUNT = IELEMENT_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Import <em>IImport</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Import
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIImport()
   * @generated
   */
  int IIMPORT = 36;

  /**
   * The number of structural features of the '<em>IImport</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IIMPORT_FEATURE_COUNT = IATTRIBUTE_EXTENSIBLE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link java.util.List <em>IList</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.util.List
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIList()
   * @generated
   */
  int ILIST = 37;

  /**
   * The number of structural features of the '<em>IList</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ILIST_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link java.util.Map <em>IMap</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.util.Map
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIMap()
   * @generated
   */
  int IMAP = 38;

  /**
   * The number of structural features of the '<em>IMap</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAP_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link java.net.URL <em>IURL</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.net.URL
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIURL()
   * @generated
   */
  int IURL = 39;

  /**
   * The number of structural features of the '<em>IURL</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IURL_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.ExtensionRegistry <em>IExtension Registry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.ExtensionRegistry
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIExtensionRegistry()
   * @generated
   */
  int IEXTENSION_REGISTRY = 40;

  /**
   * The number of structural features of the '<em>IExtension Registry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IEXTENSION_REGISTRY_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.TypesImpl <em>Types</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.TypesImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getTypes()
   * @generated
   */
  int TYPES = 41;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPES__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPES__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPES__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The number of structural features of the '<em>Types</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPES_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link java.util.Iterator <em>IIterator</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.util.Iterator
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIIterator()
   * @generated
   */
  int IITERATOR = 42;

  /**
   * The number of structural features of the '<em>IIterator</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IITERATOR_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Types <em>ITypes</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.Types
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getITypes()
   * @generated
   */
  int ITYPES = 43;

  /**
   * The number of structural features of the '<em>ITypes</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ITYPES_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.UnknownExtensibilityElementImpl <em>Unknown Extensibility Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.UnknownExtensibilityElementImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getUnknownExtensibilityElement()
   * @generated
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT = 44;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT = EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT = EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT__REQUIRED = EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT_TYPE = EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Children</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Unknown Extensibility Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT_FEATURE_COUNT = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl <em>XSD Schema Extensibility Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getXSDSchemaExtensibilityElement()
   * @generated
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT = 45;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT = EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__ELEMENT = EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__REQUIRED = EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__ELEMENT_TYPE = EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Document Base URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__DOCUMENT_BASE_URI = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Schema</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>XSD Schema Extensibility Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT_FEATURE_COUNT = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link java.lang.Object <em>IObject</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.Object
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIObject()
   * @generated
   */
  int IOBJECT = 49;

  /**
   * The number of structural features of the '<em>IObject</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IOBJECT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.schema.Schema <em>ISchema</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.schema.Schema
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getISchema()
   * @generated
   */
  int ISCHEMA = 50;

  /**
   * The number of structural features of the '<em>ISchema</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISCHEMA_FEATURE_COUNT = IEXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '<em>QName</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.xml.namespace.QName
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getQName()
   * @generated
   */
  int QNAME = 51;

  /**
   * The meta object id for the '<em>Operation Type</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.OperationType
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getOperationType()
   * @generated
   */
  int OPERATION_TYPE = 52;

  /**
   * The meta object id for the '<em>DOM Element</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.w3c.dom.Element
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getDOMElement()
   * @generated
   */
  int DOM_ELEMENT = 53;

  /**
   * The meta object id for the '<em>Exception</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.WSDLException
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getWSDLException()
   * @generated
   */
  int WSDL_EXCEPTION = 54;

  /**
   * The meta object id for the '<em>DOM Document</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.w3c.dom.Document
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getDOMDocument()
   * @generated
   */
  int DOM_DOCUMENT = 55;

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.WSDLElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Element</em>'.
   * @see org.eclipse.wst.wsdl.WSDLElement
   * @generated
   */
  EClass getWSDLElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.WSDLElement#getDocumentationElement <em>Documentation Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Documentation Element</em>'.
   * @see org.eclipse.wst.wsdl.WSDLElement#getDocumentationElement()
   * @see #getWSDLElement()
   * @generated
   */
  EAttribute getWSDLElement_DocumentationElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.WSDLElement#getElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Element</em>'.
   * @see org.eclipse.wst.wsdl.WSDLElement#getElement()
   * @see #getWSDLElement()
   * @generated
   */
  EAttribute getWSDLElement_Element();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.PortType <em>Port Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Port Type</em>'.
   * @see org.eclipse.wst.wsdl.PortType
   * @generated
   */
  EClass getPortType();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.PortType#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wst.wsdl.PortType#getQName()
   * @see #getPortType()
   * @generated
   */
  EAttribute getPortType_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.PortType#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wst.wsdl.PortType#isUndefined()
   * @see #getPortType()
   * @generated
   */
  EAttribute getPortType_Undefined();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.PortType#getEOperations <em>EOperations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EOperations</em>'.
   * @see org.eclipse.wst.wsdl.PortType#getEOperations()
   * @see #getPortType()
   * @generated
   */
  EReference getPortType_EOperations();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Operation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operation</em>'.
   * @see org.eclipse.wst.wsdl.Operation
   * @generated
   */
  EClass getOperation();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Operation#getStyle <em>Style</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Style</em>'.
   * @see org.eclipse.wst.wsdl.Operation#getStyle()
   * @see #getOperation()
   * @generated
   */
  EAttribute getOperation_Style();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Operation#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.Operation#getName()
   * @see #getOperation()
   * @generated
   */
  EAttribute getOperation_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Operation#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wst.wsdl.Operation#isUndefined()
   * @see #getOperation()
   * @generated
   */
  EAttribute getOperation_Undefined();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wst.wsdl.Operation#getEInput <em>EInput</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>EInput</em>'.
   * @see org.eclipse.wst.wsdl.Operation#getEInput()
   * @see #getOperation()
   * @generated
   */
  EReference getOperation_EInput();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wst.wsdl.Operation#getEOutput <em>EOutput</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>EOutput</em>'.
   * @see org.eclipse.wst.wsdl.Operation#getEOutput()
   * @see #getOperation()
   * @generated
   */
  EReference getOperation_EOutput();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Operation#getEFaults <em>EFaults</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EFaults</em>'.
   * @see org.eclipse.wst.wsdl.Operation#getEFaults()
   * @see #getOperation()
   * @generated
   */
  EReference getOperation_EFaults();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.wst.wsdl.Operation#getEParameterOrdering <em>EParameter Ordering</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>EParameter Ordering</em>'.
   * @see org.eclipse.wst.wsdl.Operation#getEParameterOrdering()
   * @see #getOperation()
   * @generated
   */
  EReference getOperation_EParameterOrdering();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Message <em>Message</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Message</em>'.
   * @see org.eclipse.wst.wsdl.Message
   * @generated
   */
  EClass getMessage();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Message#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wst.wsdl.Message#getQName()
   * @see #getMessage()
   * @generated
   */
  EAttribute getMessage_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Message#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wst.wsdl.Message#isUndefined()
   * @see #getMessage()
   * @generated
   */
  EAttribute getMessage_Undefined();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Message#getEParts <em>EParts</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EParts</em>'.
   * @see org.eclipse.wst.wsdl.Message#getEParts()
   * @see #getMessage()
   * @generated
   */
  EReference getMessage_EParts();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Part <em>Part</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Part</em>'.
   * @see org.eclipse.wst.wsdl.Part
   * @generated
   */
  EClass getPart();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Part#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.Part#getName()
   * @see #getPart()
   * @generated
   */
  EAttribute getPart_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Part#getElementName <em>Element Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Element Name</em>'.
   * @see org.eclipse.wst.wsdl.Part#getElementName()
   * @see #getPart()
   * @generated
   */
  EAttribute getPart_ElementName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Part#getTypeName <em>Type Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type Name</em>'.
   * @see org.eclipse.wst.wsdl.Part#getTypeName()
   * @see #getPart()
   * @generated
   */
  EAttribute getPart_TypeName();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.Part#getTypeDefinition <em>Type Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type Definition</em>'.
   * @see org.eclipse.wst.wsdl.Part#getTypeDefinition()
   * @see #getPart()
   * @generated
   */
  EReference getPart_TypeDefinition();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.Part#getElementDeclaration <em>Element Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Element Declaration</em>'.
   * @see org.eclipse.wst.wsdl.Part#getElementDeclaration()
   * @see #getPart()
   * @generated
   */
  EReference getPart_ElementDeclaration();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.Part#getEMessage <em>EMessage</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EMessage</em>'.
   * @see org.eclipse.wst.wsdl.Part#getEMessage()
   * @see #getPart()
   * @generated
   */
  EReference getPart_EMessage();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Binding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding</em>'.
   * @see org.eclipse.wst.wsdl.Binding
   * @generated
   */
  EClass getBinding();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Binding#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wst.wsdl.Binding#getQName()
   * @see #getBinding()
   * @generated
   */
  EAttribute getBinding_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Binding#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wst.wsdl.Binding#isUndefined()
   * @see #getBinding()
   * @generated
   */
  EAttribute getBinding_Undefined();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.Binding#getEPortType <em>EPort Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EPort Type</em>'.
   * @see org.eclipse.wst.wsdl.Binding#getEPortType()
   * @see #getBinding()
   * @generated
   */
  EReference getBinding_EPortType();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Binding#getEBindingOperations <em>EBinding Operations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EBinding Operations</em>'.
   * @see org.eclipse.wst.wsdl.Binding#getEBindingOperations()
   * @see #getBinding()
   * @generated
   */
  EReference getBinding_EBindingOperations();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.BindingOperation <em>Binding Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding Operation</em>'.
   * @see org.eclipse.wst.wsdl.BindingOperation
   * @generated
   */
  EClass getBindingOperation();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.BindingOperation#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.BindingOperation#getName()
   * @see #getBindingOperation()
   * @generated
   */
  EAttribute getBindingOperation_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.BindingOperation#getEOperation <em>EOperation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EOperation</em>'.
   * @see org.eclipse.wst.wsdl.BindingOperation#getEOperation()
   * @see #getBindingOperation()
   * @generated
   */
  EReference getBindingOperation_EOperation();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wst.wsdl.BindingOperation#getEBindingInput <em>EBinding Input</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>EBinding Input</em>'.
   * @see org.eclipse.wst.wsdl.BindingOperation#getEBindingInput()
   * @see #getBindingOperation()
   * @generated
   */
  EReference getBindingOperation_EBindingInput();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wst.wsdl.BindingOperation#getEBindingOutput <em>EBinding Output</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>EBinding Output</em>'.
   * @see org.eclipse.wst.wsdl.BindingOperation#getEBindingOutput()
   * @see #getBindingOperation()
   * @generated
   */
  EReference getBindingOperation_EBindingOutput();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.BindingOperation#getEBindingFaults <em>EBinding Faults</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EBinding Faults</em>'.
   * @see org.eclipse.wst.wsdl.BindingOperation#getEBindingFaults()
   * @see #getBindingOperation()
   * @generated
   */
  EReference getBindingOperation_EBindingFaults();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Service <em>Service</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Service</em>'.
   * @see org.eclipse.wst.wsdl.Service
   * @generated
   */
  EClass getService();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Service#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wst.wsdl.Service#getQName()
   * @see #getService()
   * @generated
   */
  EAttribute getService_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Service#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wst.wsdl.Service#isUndefined()
   * @see #getService()
   * @generated
   */
  EAttribute getService_Undefined();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Service#getEPorts <em>EPorts</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EPorts</em>'.
   * @see org.eclipse.wst.wsdl.Service#getEPorts()
   * @see #getService()
   * @generated
   */
  EReference getService_EPorts();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Port <em>Port</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Port</em>'.
   * @see org.eclipse.wst.wsdl.Port
   * @generated
   */
  EClass getPort();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Port#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.Port#getName()
   * @see #getPort()
   * @generated
   */
  EAttribute getPort_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.Port#getEBinding <em>EBinding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EBinding</em>'.
   * @see org.eclipse.wst.wsdl.Port#getEBinding()
   * @see #getPort()
   * @generated
   */
  EReference getPort_EBinding();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.ExtensibilityElement <em>Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Extensibility Element</em>'.
   * @see org.eclipse.wst.wsdl.ExtensibilityElement
   * @generated
   */
  EClass getExtensibilityElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.ExtensibilityElement#isRequired <em>Required</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Required</em>'.
   * @see org.eclipse.wst.wsdl.ExtensibilityElement#isRequired()
   * @see #getExtensibilityElement()
   * @generated
   */
  EAttribute getExtensibilityElement_Required();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.ExtensibilityElement#getElementType <em>Element Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Element Type</em>'.
   * @see org.eclipse.wst.wsdl.ExtensibilityElement#getElementType()
   * @see #getExtensibilityElement()
   * @generated
   */
  EAttribute getExtensibilityElement_ElementType();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Definition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Definition</em>'.
   * @see org.eclipse.wst.wsdl.Definition
   * @generated
   */
  EClass getDefinition();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Definition#getTargetNamespace <em>Target Namespace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Target Namespace</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getTargetNamespace()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_TargetNamespace();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Definition#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getLocation()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_Location();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Definition#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getQName()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Definition#getEncoding <em>Encoding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Encoding</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getEncoding()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_Encoding();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Definition#getEImports <em>EImports</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EImports</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getEImports()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_EImports();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wst.wsdl.Definition#getETypes <em>ETypes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>ETypes</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getETypes()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_ETypes();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Definition#getEMessages <em>EMessages</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EMessages</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getEMessages()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_EMessages();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Definition#getEPortTypes <em>EPort Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EPort Types</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getEPortTypes()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_EPortTypes();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Definition#getEBindings <em>EBindings</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EBindings</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getEBindings()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_EBindings();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Definition#getEServices <em>EServices</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EServices</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getEServices()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_EServices();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.Definition#getENamespaces <em>ENamespaces</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>ENamespaces</em>'.
   * @see org.eclipse.wst.wsdl.Definition#getENamespaces()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_ENamespaces();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Import <em>Import</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Import</em>'.
   * @see org.eclipse.wst.wsdl.Import
   * @generated
   */
  EClass getImport();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Import#getNamespaceURI <em>Namespace URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace URI</em>'.
   * @see org.eclipse.wst.wsdl.Import#getNamespaceURI()
   * @see #getImport()
   * @generated
   */
  EAttribute getImport_NamespaceURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Import#getLocationURI <em>Location URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location URI</em>'.
   * @see org.eclipse.wst.wsdl.Import#getLocationURI()
   * @see #getImport()
   * @generated
   */
  EAttribute getImport_LocationURI();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.Import#getEDefinition <em>EDefinition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EDefinition</em>'.
   * @see org.eclipse.wst.wsdl.Import#getEDefinition()
   * @see #getImport()
   * @generated
   */
  EReference getImport_EDefinition();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.Import#getESchema <em>ESchema</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>ESchema</em>'.
   * @see org.eclipse.wst.wsdl.Import#getESchema()
   * @see #getImport()
   * @generated
   */
  EReference getImport_ESchema();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.ExtensibleElement <em>Extensible Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Extensible Element</em>'.
   * @see org.eclipse.wst.wsdl.ExtensibleElement
   * @generated
   */
  EClass getExtensibleElement();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.ExtensibleElement#getEExtensibilityElements <em>EExtensibility Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EExtensibility Elements</em>'.
   * @see org.eclipse.wst.wsdl.ExtensibleElement#getEExtensibilityElements()
   * @see #getExtensibleElement()
   * @generated
   */
  EReference getExtensibleElement_EExtensibilityElements();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Input <em>Input</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Input</em>'.
   * @see org.eclipse.wst.wsdl.Input
   * @generated
   */
  EClass getInput();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Output <em>Output</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Output</em>'.
   * @see org.eclipse.wst.wsdl.Output
   * @generated
   */
  EClass getOutput();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Fault <em>Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Fault</em>'.
   * @see org.eclipse.wst.wsdl.Fault
   * @generated
   */
  EClass getFault();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.BindingInput <em>Binding Input</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding Input</em>'.
   * @see org.eclipse.wst.wsdl.BindingInput
   * @generated
   */
  EClass getBindingInput();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.BindingInput#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.BindingInput#getName()
   * @see #getBindingInput()
   * @generated
   */
  EAttribute getBindingInput_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.BindingInput#getEInput <em>EInput</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EInput</em>'.
   * @see org.eclipse.wst.wsdl.BindingInput#getEInput()
   * @see #getBindingInput()
   * @generated
   */
  EReference getBindingInput_EInput();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.BindingOutput <em>Binding Output</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding Output</em>'.
   * @see org.eclipse.wst.wsdl.BindingOutput
   * @generated
   */
  EClass getBindingOutput();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.BindingOutput#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.BindingOutput#getName()
   * @see #getBindingOutput()
   * @generated
   */
  EAttribute getBindingOutput_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.BindingOutput#getEOutput <em>EOutput</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EOutput</em>'.
   * @see org.eclipse.wst.wsdl.BindingOutput#getEOutput()
   * @see #getBindingOutput()
   * @generated
   */
  EReference getBindingOutput_EOutput();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.BindingFault <em>Binding Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding Fault</em>'.
   * @see org.eclipse.wst.wsdl.BindingFault
   * @generated
   */
  EClass getBindingFault();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.BindingFault#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.BindingFault#getName()
   * @see #getBindingFault()
   * @generated
   */
  EAttribute getBindingFault_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.BindingFault#getEFault <em>EFault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EFault</em>'.
   * @see org.eclipse.wst.wsdl.BindingFault#getEFault()
   * @see #getBindingFault()
   * @generated
   */
  EReference getBindingFault_EFault();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Namespace <em>Namespace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Namespace</em>'.
   * @see org.eclipse.wst.wsdl.Namespace
   * @generated
   */
  EClass getNamespace();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Namespace#getURI <em>URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>URI</em>'.
   * @see org.eclipse.wst.wsdl.Namespace#getURI()
   * @see #getNamespace()
   * @generated
   */
  EAttribute getNamespace_URI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.Namespace#getPrefix <em>Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Prefix</em>'.
   * @see org.eclipse.wst.wsdl.Namespace#getPrefix()
   * @see #getNamespace()
   * @generated
   */
  EAttribute getNamespace_Prefix();

  /**
   * Returns the meta object for class '{@link javax.wsdl.PortType <em>IPort Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IPort Type</em>'.
   * @see javax.wsdl.PortType
   * @model instanceClass="javax.wsdl.PortType" superTypes="org.eclipse.wst.wsdl.IAttributeExtensible"
   * @generated
   */
  EClass getIPortType();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Operation <em>IOperation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IOperation</em>'.
   * @see javax.wsdl.Operation
   * @model instanceClass="javax.wsdl.Operation" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIOperation();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Input <em>IInput</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IInput</em>'.
   * @see javax.wsdl.Input
   * @model instanceClass="javax.wsdl.Input" superTypes="org.eclipse.wst.wsdl.IAttributeExtensible"
   * @generated
   */
  EClass getIInput();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Output <em>IOutput</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IOutput</em>'.
   * @see javax.wsdl.Output
   * @model instanceClass="javax.wsdl.Output" superTypes="org.eclipse.wst.wsdl.IAttributeExtensible"
   * @generated
   */
  EClass getIOutput();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Fault <em>IFault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IFault</em>'.
   * @see javax.wsdl.Fault
   * @model instanceClass="javax.wsdl.Fault" superTypes="org.eclipse.wst.wsdl.IAttributeExtensible"
   * @generated
   */
  EClass getIFault();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Message <em>IMessage</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IMessage</em>'.
   * @see javax.wsdl.Message
   * @model instanceClass="javax.wsdl.Message" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIMessage();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Part <em>IPart</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IPart</em>'.
   * @see javax.wsdl.Part
   * @model instanceClass="javax.wsdl.Part" superTypes="org.eclipse.wst.wsdl.IAttributeExtensible"
   * @generated
   */
  EClass getIPart();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Service <em>IService</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IService</em>'.
   * @see javax.wsdl.Service
   * @model instanceClass="javax.wsdl.Service" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIService();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Port <em>IPort</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IPort</em>'.
   * @see javax.wsdl.Port
   * @model instanceClass="javax.wsdl.Port" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIPort();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Binding <em>IBinding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding</em>'.
   * @see javax.wsdl.Binding
   * @model instanceClass="javax.wsdl.Binding" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIBinding();

  /**
   * Returns the meta object for class '{@link javax.wsdl.BindingOperation <em>IBinding Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding Operation</em>'.
   * @see javax.wsdl.BindingOperation
   * @model instanceClass="javax.wsdl.BindingOperation" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIBindingOperation();

  /**
   * Returns the meta object for class '{@link javax.wsdl.BindingInput <em>IBinding Input</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding Input</em>'.
   * @see javax.wsdl.BindingInput
   * @model instanceClass="javax.wsdl.BindingInput" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIBindingInput();

  /**
   * Returns the meta object for class '{@link javax.wsdl.BindingOutput <em>IBinding Output</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding Output</em>'.
   * @see javax.wsdl.BindingOutput
   * @model instanceClass="javax.wsdl.BindingOutput" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIBindingOutput();

  /**
   * Returns the meta object for class '{@link javax.wsdl.BindingFault <em>IBinding Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding Fault</em>'.
   * @see javax.wsdl.BindingFault
   * @model instanceClass="javax.wsdl.BindingFault" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIBindingFault();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.ExtensibilityElement <em>IExtensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IExtensibility Element</em>'.
   * @see javax.wsdl.extensions.ExtensibilityElement
   * @model instanceClass="javax.wsdl.extensions.ExtensibilityElement"
   * @generated
   */
  EClass getIExtensibilityElement();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Definition <em>IDefinition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IDefinition</em>'.
   * @see javax.wsdl.Definition
   * @model instanceClass="javax.wsdl.Definition" superTypes="org.eclipse.wst.wsdl.IElementExtensible"
   * @generated
   */
  EClass getIDefinition();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Import <em>IImport</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IImport</em>'.
   * @see javax.wsdl.Import
   * @model instanceClass="javax.wsdl.Import" superTypes="org.eclipse.wst.wsdl.IAttributeExtensible"
   * @generated
   */
  EClass getIImport();

  /**
   * Returns the meta object for class '{@link java.util.List <em>IList</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IList</em>'.
   * @see java.util.List
   * @model instanceClass="java.util.List"
   * @generated
   */
  EClass getIList();

  /**
   * Returns the meta object for class '{@link java.util.Map <em>IMap</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IMap</em>'.
   * @see java.util.Map
   * @model instanceClass="java.util.Map"
   * @generated
   */
  EClass getIMap();

  /**
   * Returns the meta object for class '{@link java.net.URL <em>IURL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IURL</em>'.
   * @see java.net.URL
   * @model instanceClass="java.net.URL"
   * @generated
   */
  EClass getIURL();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.ExtensionRegistry <em>IExtension Registry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IExtension Registry</em>'.
   * @see javax.wsdl.extensions.ExtensionRegistry
   * @model instanceClass="javax.wsdl.extensions.ExtensionRegistry"
   * @generated
   */
  EClass getIExtensionRegistry();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.Types <em>Types</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Types</em>'.
   * @see org.eclipse.wst.wsdl.Types
   * @generated
   */
  EClass getTypes();

  /**
   * Returns the meta object for class '{@link java.util.Iterator <em>IIterator</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IIterator</em>'.
   * @see java.util.Iterator
   * @model instanceClass="java.util.Iterator"
   * @generated
   */
  EClass getIIterator();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Types <em>ITypes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ITypes</em>'.
   * @see javax.wsdl.Types
   * @model instanceClass="javax.wsdl.Types"
   * @generated
   */
  EClass getITypes();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.UnknownExtensibilityElement <em>Unknown Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Unknown Extensibility Element</em>'.
   * @see org.eclipse.wst.wsdl.UnknownExtensibilityElement
   * @generated
   */
  EClass getUnknownExtensibilityElement();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.UnknownExtensibilityElement#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Children</em>'.
   * @see org.eclipse.wst.wsdl.UnknownExtensibilityElement#getChildren()
   * @see #getUnknownExtensibilityElement()
   * @generated
   */
  EReference getUnknownExtensibilityElement_Children();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement <em>XSD Schema Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>XSD Schema Extensibility Element</em>'.
   * @see org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement
   * @generated
   */
  EClass getXSDSchemaExtensibilityElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement#getDocumentBaseURI <em>Document Base URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Document Base URI</em>'.
   * @see org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement#getDocumentBaseURI()
   * @see #getXSDSchemaExtensibilityElement()
   * @generated
   */
  EAttribute getXSDSchemaExtensibilityElement_DocumentBaseURI();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement#getSchema <em>Schema</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Schema</em>'.
   * @see org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement#getSchema()
   * @see #getXSDSchemaExtensibilityElement()
   * @generated
   */
  EReference getXSDSchemaExtensibilityElement_Schema();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.MessageReference <em>Message Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Message Reference</em>'.
   * @see org.eclipse.wst.wsdl.MessageReference
   * @generated
   */
  EClass getMessageReference();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.MessageReference#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.MessageReference#getName()
   * @see #getMessageReference()
   * @generated
   */
  EAttribute getMessageReference_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.MessageReference#getEMessage <em>EMessage</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EMessage</em>'.
   * @see org.eclipse.wst.wsdl.MessageReference#getEMessage()
   * @see #getMessageReference()
   * @generated
   */
  EReference getMessageReference_EMessage();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.ElementExtensible <em>IElement Extensible</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IElement Extensible</em>'.
   * @see javax.wsdl.extensions.ElementExtensible
   * @model instanceClass="javax.wsdl.extensions.ElementExtensible"
   * @generated
   */
  EClass getIElementExtensible();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.AttributeExtensible <em>IAttribute Extensible</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IAttribute Extensible</em>'.
   * @see javax.wsdl.extensions.AttributeExtensible
   * @model instanceClass="javax.wsdl.extensions.AttributeExtensible"
   * @generated
   */
  EClass getIAttributeExtensible();

  /**
   * Returns the meta object for class '{@link java.lang.Object <em>IObject</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IObject</em>'.
   * @see java.lang.Object
   * @model instanceClass="java.lang.Object"
   * @generated
   */
  EClass getIObject();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.schema.Schema <em>ISchema</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ISchema</em>'.
   * @see javax.wsdl.extensions.schema.Schema
   * @model instanceClass="javax.wsdl.extensions.schema.Schema" superTypes="org.eclipse.wst.wsdl.IExtensibilityElement"
   * @generated
   */
  EClass getISchema();

  /**
   * Returns the meta object for data type '{@link javax.xml.namespace.QName <em>QName</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>QName</em>'.
   * @see javax.xml.namespace.QName
   * @model instanceClass="javax.xml.namespace.QName"
   * @generated
   */
  EDataType getQName();

  /**
   * Returns the meta object for data type '{@link javax.wsdl.OperationType <em>Operation Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Operation Type</em>'.
   * @see javax.wsdl.OperationType
   * @model instanceClass="javax.wsdl.OperationType"
   * @generated
   */
  EDataType getOperationType();

  /**
   * Returns the meta object for data type '{@link org.w3c.dom.Element <em>DOM Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>DOM Element</em>'.
   * @see org.w3c.dom.Element
   * @model instanceClass="org.w3c.dom.Element"
   * @generated
   */
  EDataType getDOMElement();

  /**
   * Returns the meta object for data type '{@link javax.wsdl.WSDLException <em>Exception</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Exception</em>'.
   * @see javax.wsdl.WSDLException
   * @model instanceClass="javax.wsdl.WSDLException"
   * @generated
   */
  EDataType getWSDLException();

  /**
   * Returns the meta object for data type '{@link org.w3c.dom.Document <em>DOM Document</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>DOM Document</em>'.
   * @see org.w3c.dom.Document
   * @model instanceClass="org.w3c.dom.Document"
   * @generated
   */
  EDataType getDOMDocument();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  WSDLFactory getWSDLFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl <em>Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getWSDLElement()
     * @generated
     */
    EClass WSDL_ELEMENT = eINSTANCE.getWSDLElement();

    /**
     * The meta object literal for the '<em><b>Documentation Element</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute WSDL_ELEMENT__DOCUMENTATION_ELEMENT = eINSTANCE.getWSDLElement_DocumentationElement();

    /**
     * The meta object literal for the '<em><b>Element</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute WSDL_ELEMENT__ELEMENT = eINSTANCE.getWSDLElement_Element();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl <em>Port Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.PortTypeImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getPortType()
     * @generated
     */
    EClass PORT_TYPE = eINSTANCE.getPortType();

    /**
     * The meta object literal for the '<em><b>QName</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PORT_TYPE__QNAME = eINSTANCE.getPortType_QName();

    /**
     * The meta object literal for the '<em><b>Undefined</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PORT_TYPE__UNDEFINED = eINSTANCE.getPortType_Undefined();

    /**
     * The meta object literal for the '<em><b>EOperations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PORT_TYPE__EOPERATIONS = eINSTANCE.getPortType_EOperations();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.OperationImpl <em>Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.OperationImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getOperation()
     * @generated
     */
    EClass OPERATION = eINSTANCE.getOperation();

    /**
     * The meta object literal for the '<em><b>Style</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OPERATION__STYLE = eINSTANCE.getOperation_Style();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OPERATION__NAME = eINSTANCE.getOperation_Name();

    /**
     * The meta object literal for the '<em><b>Undefined</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute OPERATION__UNDEFINED = eINSTANCE.getOperation_Undefined();

    /**
     * The meta object literal for the '<em><b>EInput</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__EINPUT = eINSTANCE.getOperation_EInput();

    /**
     * The meta object literal for the '<em><b>EOutput</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__EOUTPUT = eINSTANCE.getOperation_EOutput();

    /**
     * The meta object literal for the '<em><b>EFaults</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__EFAULTS = eINSTANCE.getOperation_EFaults();

    /**
     * The meta object literal for the '<em><b>EParameter Ordering</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__EPARAMETER_ORDERING = eINSTANCE.getOperation_EParameterOrdering();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.MessageImpl <em>Message</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.MessageImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getMessage()
     * @generated
     */
    EClass MESSAGE = eINSTANCE.getMessage();

    /**
     * The meta object literal for the '<em><b>QName</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MESSAGE__QNAME = eINSTANCE.getMessage_QName();

    /**
     * The meta object literal for the '<em><b>Undefined</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MESSAGE__UNDEFINED = eINSTANCE.getMessage_Undefined();

    /**
     * The meta object literal for the '<em><b>EParts</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MESSAGE__EPARTS = eINSTANCE.getMessage_EParts();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.PartImpl <em>Part</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.PartImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getPart()
     * @generated
     */
    EClass PART = eINSTANCE.getPart();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PART__NAME = eINSTANCE.getPart_Name();

    /**
     * The meta object literal for the '<em><b>Element Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PART__ELEMENT_NAME = eINSTANCE.getPart_ElementName();

    /**
     * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PART__TYPE_NAME = eINSTANCE.getPart_TypeName();

    /**
     * The meta object literal for the '<em><b>Type Definition</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PART__TYPE_DEFINITION = eINSTANCE.getPart_TypeDefinition();

    /**
     * The meta object literal for the '<em><b>Element Declaration</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PART__ELEMENT_DECLARATION = eINSTANCE.getPart_ElementDeclaration();

    /**
     * The meta object literal for the '<em><b>EMessage</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PART__EMESSAGE = eINSTANCE.getPart_EMessage();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingImpl <em>Binding</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.BindingImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBinding()
     * @generated
     */
    EClass BINDING = eINSTANCE.getBinding();

    /**
     * The meta object literal for the '<em><b>QName</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINDING__QNAME = eINSTANCE.getBinding_QName();

    /**
     * The meta object literal for the '<em><b>Undefined</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINDING__UNDEFINED = eINSTANCE.getBinding_Undefined();

    /**
     * The meta object literal for the '<em><b>EPort Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING__EPORT_TYPE = eINSTANCE.getBinding_EPortType();

    /**
     * The meta object literal for the '<em><b>EBinding Operations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING__EBINDING_OPERATIONS = eINSTANCE.getBinding_EBindingOperations();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl <em>Binding Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBindingOperation()
     * @generated
     */
    EClass BINDING_OPERATION = eINSTANCE.getBindingOperation();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINDING_OPERATION__NAME = eINSTANCE.getBindingOperation_Name();

    /**
     * The meta object literal for the '<em><b>EOperation</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING_OPERATION__EOPERATION = eINSTANCE.getBindingOperation_EOperation();

    /**
     * The meta object literal for the '<em><b>EBinding Input</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING_OPERATION__EBINDING_INPUT = eINSTANCE.getBindingOperation_EBindingInput();

    /**
     * The meta object literal for the '<em><b>EBinding Output</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING_OPERATION__EBINDING_OUTPUT = eINSTANCE.getBindingOperation_EBindingOutput();

    /**
     * The meta object literal for the '<em><b>EBinding Faults</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING_OPERATION__EBINDING_FAULTS = eINSTANCE.getBindingOperation_EBindingFaults();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.ServiceImpl <em>Service</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.ServiceImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getService()
     * @generated
     */
    EClass SERVICE = eINSTANCE.getService();

    /**
     * The meta object literal for the '<em><b>QName</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SERVICE__QNAME = eINSTANCE.getService_QName();

    /**
     * The meta object literal for the '<em><b>Undefined</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SERVICE__UNDEFINED = eINSTANCE.getService_Undefined();

    /**
     * The meta object literal for the '<em><b>EPorts</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SERVICE__EPORTS = eINSTANCE.getService_EPorts();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.PortImpl <em>Port</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.PortImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getPort()
     * @generated
     */
    EClass PORT = eINSTANCE.getPort();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PORT__NAME = eINSTANCE.getPort_Name();

    /**
     * The meta object literal for the '<em><b>EBinding</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PORT__EBINDING = eINSTANCE.getPort_EBinding();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl <em>Extensibility Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getExtensibilityElement()
     * @generated
     */
    EClass EXTENSIBILITY_ELEMENT = eINSTANCE.getExtensibilityElement();

    /**
     * The meta object literal for the '<em><b>Required</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EXTENSIBILITY_ELEMENT__REQUIRED = eINSTANCE.getExtensibilityElement_Required();

    /**
     * The meta object literal for the '<em><b>Element Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute EXTENSIBILITY_ELEMENT__ELEMENT_TYPE = eINSTANCE.getExtensibilityElement_ElementType();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl <em>Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.DefinitionImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getDefinition()
     * @generated
     */
    EClass DEFINITION = eINSTANCE.getDefinition();

    /**
     * The meta object literal for the '<em><b>Target Namespace</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__TARGET_NAMESPACE = eINSTANCE.getDefinition_TargetNamespace();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__LOCATION = eINSTANCE.getDefinition_Location();

    /**
     * The meta object literal for the '<em><b>QName</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__QNAME = eINSTANCE.getDefinition_QName();

    /**
     * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__ENCODING = eINSTANCE.getDefinition_Encoding();

    /**
     * The meta object literal for the '<em><b>EImports</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__EIMPORTS = eINSTANCE.getDefinition_EImports();

    /**
     * The meta object literal for the '<em><b>ETypes</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__ETYPES = eINSTANCE.getDefinition_ETypes();

    /**
     * The meta object literal for the '<em><b>EMessages</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__EMESSAGES = eINSTANCE.getDefinition_EMessages();

    /**
     * The meta object literal for the '<em><b>EPort Types</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__EPORT_TYPES = eINSTANCE.getDefinition_EPortTypes();

    /**
     * The meta object literal for the '<em><b>EBindings</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__EBINDINGS = eINSTANCE.getDefinition_EBindings();

    /**
     * The meta object literal for the '<em><b>EServices</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__ESERVICES = eINSTANCE.getDefinition_EServices();

    /**
     * The meta object literal for the '<em><b>ENamespaces</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFINITION__ENAMESPACES = eINSTANCE.getDefinition_ENamespaces();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.ImportImpl <em>Import</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.ImportImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getImport()
     * @generated
     */
    EClass IMPORT = eINSTANCE.getImport();

    /**
     * The meta object literal for the '<em><b>Namespace URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMPORT__NAMESPACE_URI = eINSTANCE.getImport_NamespaceURI();

    /**
     * The meta object literal for the '<em><b>Location URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMPORT__LOCATION_URI = eINSTANCE.getImport_LocationURI();

    /**
     * The meta object literal for the '<em><b>EDefinition</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IMPORT__EDEFINITION = eINSTANCE.getImport_EDefinition();

    /**
     * The meta object literal for the '<em><b>ESchema</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference IMPORT__ESCHEMA = eINSTANCE.getImport_ESchema();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.ExtensibleElementImpl <em>Extensible Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.ExtensibleElementImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getExtensibleElement()
     * @generated
     */
    EClass EXTENSIBLE_ELEMENT = eINSTANCE.getExtensibleElement();

    /**
     * The meta object literal for the '<em><b>EExtensibility Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS = eINSTANCE.getExtensibleElement_EExtensibilityElements();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.InputImpl <em>Input</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.InputImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getInput()
     * @generated
     */
    EClass INPUT = eINSTANCE.getInput();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.OutputImpl <em>Output</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.OutputImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getOutput()
     * @generated
     */
    EClass OUTPUT = eINSTANCE.getOutput();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.FaultImpl <em>Fault</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.FaultImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getFault()
     * @generated
     */
    EClass FAULT = eINSTANCE.getFault();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingInputImpl <em>Binding Input</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.BindingInputImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBindingInput()
     * @generated
     */
    EClass BINDING_INPUT = eINSTANCE.getBindingInput();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINDING_INPUT__NAME = eINSTANCE.getBindingInput_Name();

    /**
     * The meta object literal for the '<em><b>EInput</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING_INPUT__EINPUT = eINSTANCE.getBindingInput_EInput();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingOutputImpl <em>Binding Output</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.BindingOutputImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBindingOutput()
     * @generated
     */
    EClass BINDING_OUTPUT = eINSTANCE.getBindingOutput();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINDING_OUTPUT__NAME = eINSTANCE.getBindingOutput_Name();

    /**
     * The meta object literal for the '<em><b>EOutput</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING_OUTPUT__EOUTPUT = eINSTANCE.getBindingOutput_EOutput();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.BindingFaultImpl <em>Binding Fault</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.BindingFaultImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getBindingFault()
     * @generated
     */
    EClass BINDING_FAULT = eINSTANCE.getBindingFault();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINDING_FAULT__NAME = eINSTANCE.getBindingFault_Name();

    /**
     * The meta object literal for the '<em><b>EFault</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference BINDING_FAULT__EFAULT = eINSTANCE.getBindingFault_EFault();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.NamespaceImpl <em>Namespace</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.NamespaceImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getNamespace()
     * @generated
     */
    EClass NAMESPACE = eINSTANCE.getNamespace();

    /**
     * The meta object literal for the '<em><b>URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NAMESPACE__URI = eINSTANCE.getNamespace_URI();

    /**
     * The meta object literal for the '<em><b>Prefix</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NAMESPACE__PREFIX = eINSTANCE.getNamespace_Prefix();

    /**
     * The meta object literal for the '{@link javax.wsdl.PortType <em>IPort Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.PortType
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIPortType()
     * @generated
     */
    EClass IPORT_TYPE = eINSTANCE.getIPortType();

    /**
     * The meta object literal for the '{@link javax.wsdl.Operation <em>IOperation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Operation
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIOperation()
     * @generated
     */
    EClass IOPERATION = eINSTANCE.getIOperation();

    /**
     * The meta object literal for the '{@link javax.wsdl.Input <em>IInput</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Input
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIInput()
     * @generated
     */
    EClass IINPUT = eINSTANCE.getIInput();

    /**
     * The meta object literal for the '{@link javax.wsdl.Output <em>IOutput</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Output
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIOutput()
     * @generated
     */
    EClass IOUTPUT = eINSTANCE.getIOutput();

    /**
     * The meta object literal for the '{@link javax.wsdl.Fault <em>IFault</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Fault
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIFault()
     * @generated
     */
    EClass IFAULT = eINSTANCE.getIFault();

    /**
     * The meta object literal for the '{@link javax.wsdl.Message <em>IMessage</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Message
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIMessage()
     * @generated
     */
    EClass IMESSAGE = eINSTANCE.getIMessage();

    /**
     * The meta object literal for the '{@link javax.wsdl.Part <em>IPart</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Part
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIPart()
     * @generated
     */
    EClass IPART = eINSTANCE.getIPart();

    /**
     * The meta object literal for the '{@link javax.wsdl.Service <em>IService</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Service
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIService()
     * @generated
     */
    EClass ISERVICE = eINSTANCE.getIService();

    /**
     * The meta object literal for the '{@link javax.wsdl.Port <em>IPort</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Port
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIPort()
     * @generated
     */
    EClass IPORT = eINSTANCE.getIPort();

    /**
     * The meta object literal for the '{@link javax.wsdl.Binding <em>IBinding</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Binding
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBinding()
     * @generated
     */
    EClass IBINDING = eINSTANCE.getIBinding();

    /**
     * The meta object literal for the '{@link javax.wsdl.BindingOperation <em>IBinding Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.BindingOperation
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBindingOperation()
     * @generated
     */
    EClass IBINDING_OPERATION = eINSTANCE.getIBindingOperation();

    /**
     * The meta object literal for the '{@link javax.wsdl.BindingInput <em>IBinding Input</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.BindingInput
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBindingInput()
     * @generated
     */
    EClass IBINDING_INPUT = eINSTANCE.getIBindingInput();

    /**
     * The meta object literal for the '{@link javax.wsdl.BindingOutput <em>IBinding Output</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.BindingOutput
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBindingOutput()
     * @generated
     */
    EClass IBINDING_OUTPUT = eINSTANCE.getIBindingOutput();

    /**
     * The meta object literal for the '{@link javax.wsdl.BindingFault <em>IBinding Fault</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.BindingFault
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIBindingFault()
     * @generated
     */
    EClass IBINDING_FAULT = eINSTANCE.getIBindingFault();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.ExtensibilityElement <em>IExtensibility Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.ExtensibilityElement
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIExtensibilityElement()
     * @generated
     */
    EClass IEXTENSIBILITY_ELEMENT = eINSTANCE.getIExtensibilityElement();

    /**
     * The meta object literal for the '{@link javax.wsdl.Definition <em>IDefinition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Definition
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIDefinition()
     * @generated
     */
    EClass IDEFINITION = eINSTANCE.getIDefinition();

    /**
     * The meta object literal for the '{@link javax.wsdl.Import <em>IImport</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Import
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIImport()
     * @generated
     */
    EClass IIMPORT = eINSTANCE.getIImport();

    /**
     * The meta object literal for the '{@link java.util.List <em>IList</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIList()
     * @generated
     */
    EClass ILIST = eINSTANCE.getIList();

    /**
     * The meta object literal for the '{@link java.util.Map <em>IMap</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Map
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIMap()
     * @generated
     */
    EClass IMAP = eINSTANCE.getIMap();

    /**
     * The meta object literal for the '{@link java.net.URL <em>IURL</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.net.URL
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIURL()
     * @generated
     */
    EClass IURL = eINSTANCE.getIURL();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.ExtensionRegistry <em>IExtension Registry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.ExtensionRegistry
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIExtensionRegistry()
     * @generated
     */
    EClass IEXTENSION_REGISTRY = eINSTANCE.getIExtensionRegistry();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.TypesImpl <em>Types</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.TypesImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getTypes()
     * @generated
     */
    EClass TYPES = eINSTANCE.getTypes();

    /**
     * The meta object literal for the '{@link java.util.Iterator <em>IIterator</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Iterator
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIIterator()
     * @generated
     */
    EClass IITERATOR = eINSTANCE.getIIterator();

    /**
     * The meta object literal for the '{@link javax.wsdl.Types <em>ITypes</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.Types
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getITypes()
     * @generated
     */
    EClass ITYPES = eINSTANCE.getITypes();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.UnknownExtensibilityElementImpl <em>Unknown Extensibility Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.UnknownExtensibilityElementImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getUnknownExtensibilityElement()
     * @generated
     */
    EClass UNKNOWN_EXTENSIBILITY_ELEMENT = eINSTANCE.getUnknownExtensibilityElement();

    /**
     * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN = eINSTANCE.getUnknownExtensibilityElement_Children();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl <em>XSD Schema Extensibility Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getXSDSchemaExtensibilityElement()
     * @generated
     */
    EClass XSD_SCHEMA_EXTENSIBILITY_ELEMENT = eINSTANCE.getXSDSchemaExtensibilityElement();

    /**
     * The meta object literal for the '<em><b>Document Base URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute XSD_SCHEMA_EXTENSIBILITY_ELEMENT__DOCUMENT_BASE_URI = eINSTANCE.getXSDSchemaExtensibilityElement_DocumentBaseURI();

    /**
     * The meta object literal for the '<em><b>Schema</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA = eINSTANCE.getXSDSchemaExtensibilityElement_Schema();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.internal.impl.MessageReferenceImpl <em>Message Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.internal.impl.MessageReferenceImpl
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getMessageReference()
     * @generated
     */
    EClass MESSAGE_REFERENCE = eINSTANCE.getMessageReference();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MESSAGE_REFERENCE__NAME = eINSTANCE.getMessageReference_Name();

    /**
     * The meta object literal for the '<em><b>EMessage</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MESSAGE_REFERENCE__EMESSAGE = eINSTANCE.getMessageReference_EMessage();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.ElementExtensible <em>IElement Extensible</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.ElementExtensible
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIElementExtensible()
     * @generated
     */
    EClass IELEMENT_EXTENSIBLE = eINSTANCE.getIElementExtensible();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.AttributeExtensible <em>IAttribute Extensible</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.AttributeExtensible
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIAttributeExtensible()
     * @generated
     */
    EClass IATTRIBUTE_EXTENSIBLE = eINSTANCE.getIAttributeExtensible();

    /**
     * The meta object literal for the '{@link java.lang.Object <em>IObject</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Object
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getIObject()
     * @generated
     */
    EClass IOBJECT = eINSTANCE.getIObject();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.schema.Schema <em>ISchema</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.schema.Schema
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getISchema()
     * @generated
     */
    EClass ISCHEMA = eINSTANCE.getISchema();

    /**
     * The meta object literal for the '<em>QName</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.xml.namespace.QName
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getQName()
     * @generated
     */
    EDataType QNAME = eINSTANCE.getQName();

    /**
     * The meta object literal for the '<em>Operation Type</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.OperationType
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getOperationType()
     * @generated
     */
    EDataType OPERATION_TYPE = eINSTANCE.getOperationType();

    /**
     * The meta object literal for the '<em>DOM Element</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3c.dom.Element
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getDOMElement()
     * @generated
     */
    EDataType DOM_ELEMENT = eINSTANCE.getDOMElement();

    /**
     * The meta object literal for the '<em>Exception</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.WSDLException
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getWSDLException()
     * @generated
     */
    EDataType WSDL_EXCEPTION = eINSTANCE.getWSDLException();

    /**
     * The meta object literal for the '<em>DOM Document</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3c.dom.Document
     * @see org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl#getDOMDocument()
     * @generated
     */
    EDataType DOM_DOCUMENT = eINSTANCE.getDOMDocument();

  }

} //WSDLPackage
