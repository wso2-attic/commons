/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.wst.wsdl.WSDLPackage;


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
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPFactory
 * @model kind="package"
 * @generated
 */
public interface SOAPPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "soap"; //$NON-NLS-1$

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/wsdl/2003/SOAP"; //$NON-NLS-1$

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "soap"; //$NON-NLS-1$

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SOAPPackage eINSTANCE = org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBindingImpl <em>Binding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBindingImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPBinding()
   * @generated
   */
  int SOAP_BINDING = 0;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Transport URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__TRANSPORT_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Style</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__STYLE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Binding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl <em>Body</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPBody()
   * @generated
   */
  int SOAP_BODY = 1;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__USE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__NAMESPACE_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EEncoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__EENCODING_STYLES = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>EParts</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__EPARTS = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Body</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl <em>Header Base</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeaderBase()
   * @generated
   */
  int SOAP_HEADER_BASE = 2;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__USE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__NAMESPACE_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EEncoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__EENCODING_STYLES = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Message</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__MESSAGE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Part</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__PART = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__EMESSAGE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>EPart</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__EPART = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>Header Base</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl <em>Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPFault()
   * @generated
   */
  int SOAP_FAULT = 3;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__USE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__NAMESPACE_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EEncoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__EENCODING_STYLES = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__NAME = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl <em>Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPOperation()
   * @generated
   */
  int SOAP_OPERATION = 4;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Soap Action URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__SOAP_ACTION_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Style</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__STYLE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPAddressImpl <em>Address</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPAddressImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPAddress()
   * @generated
   */
  int SOAP_ADDRESS = 5;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Location URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__LOCATION_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Address</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderFaultImpl <em>Header Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderFaultImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeaderFault()
   * @generated
   */
  int SOAP_HEADER_FAULT = 6;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__DOCUMENTATION_ELEMENT = SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__ELEMENT = SOAP_HEADER_BASE__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__REQUIRED = SOAP_HEADER_BASE__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__ELEMENT_TYPE = SOAP_HEADER_BASE__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__USE = SOAP_HEADER_BASE__USE;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__NAMESPACE_URI = SOAP_HEADER_BASE__NAMESPACE_URI;

  /**
   * The feature id for the '<em><b>EEncoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__EENCODING_STYLES = SOAP_HEADER_BASE__EENCODING_STYLES;

  /**
   * The feature id for the '<em><b>Message</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__MESSAGE = SOAP_HEADER_BASE__MESSAGE;

  /**
   * The feature id for the '<em><b>Part</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__PART = SOAP_HEADER_BASE__PART;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__EMESSAGE = SOAP_HEADER_BASE__EMESSAGE;

  /**
   * The feature id for the '<em><b>EPart</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__EPART = SOAP_HEADER_BASE__EPART;

  /**
   * The number of structural features of the '<em>Header Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT_FEATURE_COUNT = SOAP_HEADER_BASE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderImpl <em>Header</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeader()
   * @generated
   */
  int SOAP_HEADER = 7;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__DOCUMENTATION_ELEMENT = SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__ELEMENT = SOAP_HEADER_BASE__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__REQUIRED = SOAP_HEADER_BASE__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__ELEMENT_TYPE = SOAP_HEADER_BASE__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__USE = SOAP_HEADER_BASE__USE;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__NAMESPACE_URI = SOAP_HEADER_BASE__NAMESPACE_URI;

  /**
   * The feature id for the '<em><b>EEncoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__EENCODING_STYLES = SOAP_HEADER_BASE__EENCODING_STYLES;

  /**
   * The feature id for the '<em><b>Message</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__MESSAGE = SOAP_HEADER_BASE__MESSAGE;

  /**
   * The feature id for the '<em><b>Part</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__PART = SOAP_HEADER_BASE__PART;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__EMESSAGE = SOAP_HEADER_BASE__EMESSAGE;

  /**
   * The feature id for the '<em><b>EPart</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__EPART = SOAP_HEADER_BASE__EPART;

  /**
   * The feature id for the '<em><b>Header Faults</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__HEADER_FAULTS = SOAP_HEADER_BASE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Header</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FEATURE_COUNT = SOAP_HEADER_BASE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.soap.SOAPBinding <em>ISOAP Binding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.soap.SOAPBinding
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPBinding()
   * @generated
   */
  int ISOAP_BINDING = 8;

  /**
   * The number of structural features of the '<em>ISOAP Binding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISOAP_BINDING_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.soap.SOAPAddress <em>ISOAP Address</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.soap.SOAPAddress
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPAddress()
   * @generated
   */
  int ISOAP_ADDRESS = 9;

  /**
   * The number of structural features of the '<em>ISOAP Address</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISOAP_ADDRESS_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.soap.SOAPBody <em>ISOAP Body</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.soap.SOAPBody
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPBody()
   * @generated
   */
  int ISOAP_BODY = 10;

  /**
   * The number of structural features of the '<em>ISOAP Body</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISOAP_BODY_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.soap.SOAPFault <em>ISOAP Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.soap.SOAPFault
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPFault()
   * @generated
   */
  int ISOAP_FAULT = 11;

  /**
   * The number of structural features of the '<em>ISOAP Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISOAP_FAULT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.soap.SOAPOperation <em>ISOAP Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.soap.SOAPOperation
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPOperation()
   * @generated
   */
  int ISOAP_OPERATION = 12;

  /**
   * The number of structural features of the '<em>ISOAP Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISOAP_OPERATION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.soap.SOAPHeaderFault <em>ISOAP Header Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.soap.SOAPHeaderFault
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPHeaderFault()
   * @generated
   */
  int ISOAP_HEADER_FAULT = 13;

  /**
   * The number of structural features of the '<em>ISOAP Header Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISOAP_HEADER_FAULT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.soap.SOAPHeader <em>ISOAP Header</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.soap.SOAPHeader
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPHeader()
   * @generated
   */
  int ISOAP_HEADER = 14;

  /**
   * The number of structural features of the '<em>ISOAP Header</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ISOAP_HEADER_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '<em>IString</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.String
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getIString()
   * @generated
   */
  int ISTRING = 15;

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBinding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBinding
   * @generated
   */
  EClass getSOAPBinding();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBinding#getTransportURI <em>Transport URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Transport URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBinding#getTransportURI()
   * @see #getSOAPBinding()
   * @generated
   */
  EAttribute getSOAPBinding_TransportURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBinding#getStyle <em>Style</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Style</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBinding#getStyle()
   * @see #getSOAPBinding()
   * @generated
   */
  EAttribute getSOAPBinding_Style();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Body</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody
   * @generated
   */
  EClass getSOAPBody();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody#getUse <em>Use</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Use</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody#getUse()
   * @see #getSOAPBody()
   * @generated
   */
  EAttribute getSOAPBody_Use();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody#getNamespaceURI <em>Namespace URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody#getNamespaceURI()
   * @see #getSOAPBody()
   * @generated
   */
  EAttribute getSOAPBody_NamespaceURI();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody#getEEncodingStyles <em>EEncoding Styles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>EEncoding Styles</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody#getEEncodingStyles()
   * @see #getSOAPBody()
   * @generated
   */
  EAttribute getSOAPBody_EEncodingStyles();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody#getEParts <em>EParts</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>EParts</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody#getEParts()
   * @see #getSOAPBody()
   * @generated
   */
  EReference getSOAPBody_EParts();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase <em>Header Base</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Header Base</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase
   * @generated
   */
  EClass getSOAPHeaderBase();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getUse <em>Use</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Use</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getUse()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EAttribute getSOAPHeaderBase_Use();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getNamespaceURI <em>Namespace URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getNamespaceURI()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EAttribute getSOAPHeaderBase_NamespaceURI();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEEncodingStyles <em>EEncoding Styles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>EEncoding Styles</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEEncodingStyles()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EAttribute getSOAPHeaderBase_EEncodingStyles();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getMessage <em>Message</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Message</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getMessage()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EAttribute getSOAPHeaderBase_Message();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getPart <em>Part</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Part</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getPart()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EAttribute getSOAPHeaderBase_Part();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEMessage <em>EMessage</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EMessage</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEMessage()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EReference getSOAPHeaderBase_EMessage();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEPart <em>EPart</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EPart</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEPart()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EReference getSOAPHeaderBase_EPart();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault <em>Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Fault</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault
   * @generated
   */
  EClass getSOAPFault();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getUse <em>Use</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Use</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault#getUse()
   * @see #getSOAPFault()
   * @generated
   */
  EAttribute getSOAPFault_Use();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getNamespaceURI <em>Namespace URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault#getNamespaceURI()
   * @see #getSOAPFault()
   * @generated
   */
  EAttribute getSOAPFault_NamespaceURI();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault <em>EEncoding Styles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>EEncoding Styles</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault
   * @see #getSOAPFault()
   * @generated
   */
  EAttribute getSOAPFault_EEncodingStyles();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault#getName()
   * @see #getSOAPFault()
   * @generated
   */
  EAttribute getSOAPFault_Name();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPOperation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operation</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPOperation
   * @generated
   */
  EClass getSOAPOperation();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPOperation#getSoapActionURI <em>Soap Action URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Soap Action URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPOperation#getSoapActionURI()
   * @see #getSOAPOperation()
   * @generated
   */
  EAttribute getSOAPOperation_SoapActionURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPOperation#getStyle <em>Style</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Style</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPOperation#getStyle()
   * @see #getSOAPOperation()
   * @generated
   */
  EAttribute getSOAPOperation_Style();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPAddress <em>Address</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Address</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPAddress
   * @generated
   */
  EClass getSOAPAddress();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPAddress#getLocationURI <em>Location URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPAddress#getLocationURI()
   * @see #getSOAPAddress()
   * @generated
   */
  EAttribute getSOAPAddress_LocationURI();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault <em>Header Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Header Fault</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault
   * @generated
   */
  EClass getSOAPHeaderFault();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeader <em>Header</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Header</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeader
   * @generated
   */
  EClass getSOAPHeader();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeader#getHeaderFaults <em>Header Faults</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Header Faults</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeader#getHeaderFaults()
   * @see #getSOAPHeader()
   * @generated
   */
  EReference getSOAPHeader_HeaderFaults();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.soap.SOAPBinding <em>ISOAP Binding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ISOAP Binding</em>'.
   * @see javax.wsdl.extensions.soap.SOAPBinding
   * @model instanceClass="javax.wsdl.extensions.soap.SOAPBinding"
   * @generated
   */
  EClass getISOAPBinding();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.soap.SOAPAddress <em>ISOAP Address</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ISOAP Address</em>'.
   * @see javax.wsdl.extensions.soap.SOAPAddress
   * @model instanceClass="javax.wsdl.extensions.soap.SOAPAddress"
   * @generated
   */
  EClass getISOAPAddress();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.soap.SOAPBody <em>ISOAP Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ISOAP Body</em>'.
   * @see javax.wsdl.extensions.soap.SOAPBody
   * @model instanceClass="javax.wsdl.extensions.soap.SOAPBody"
   * @generated
   */
  EClass getISOAPBody();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.soap.SOAPFault <em>ISOAP Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ISOAP Fault</em>'.
   * @see javax.wsdl.extensions.soap.SOAPFault
   * @model instanceClass="javax.wsdl.extensions.soap.SOAPFault"
   * @generated
   */
  EClass getISOAPFault();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.soap.SOAPOperation <em>ISOAP Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ISOAP Operation</em>'.
   * @see javax.wsdl.extensions.soap.SOAPOperation
   * @model instanceClass="javax.wsdl.extensions.soap.SOAPOperation"
   * @generated
   */
  EClass getISOAPOperation();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.soap.SOAPHeaderFault <em>ISOAP Header Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ISOAP Header Fault</em>'.
   * @see javax.wsdl.extensions.soap.SOAPHeaderFault
   * @model instanceClass="javax.wsdl.extensions.soap.SOAPHeaderFault"
   * @generated
   */
  EClass getISOAPHeaderFault();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.soap.SOAPHeader <em>ISOAP Header</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ISOAP Header</em>'.
   * @see javax.wsdl.extensions.soap.SOAPHeader
   * @model instanceClass="javax.wsdl.extensions.soap.SOAPHeader"
   * @generated
   */
  EClass getISOAPHeader();

  /**
   * Returns the meta object for data type '{@link java.lang.String <em>IString</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>IString</em>'.
   * @see java.lang.String
   * @model instanceClass="java.lang.String"
   * @generated
   */
  EDataType getIString();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  SOAPFactory getSOAPFactory();

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
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBindingImpl <em>Binding</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBindingImpl
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPBinding()
     * @generated
     */
    EClass SOAP_BINDING = eINSTANCE.getSOAPBinding();

    /**
     * The meta object literal for the '<em><b>Transport URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_BINDING__TRANSPORT_URI = eINSTANCE.getSOAPBinding_TransportURI();

    /**
     * The meta object literal for the '<em><b>Style</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_BINDING__STYLE = eINSTANCE.getSOAPBinding_Style();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl <em>Body</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPBody()
     * @generated
     */
    EClass SOAP_BODY = eINSTANCE.getSOAPBody();

    /**
     * The meta object literal for the '<em><b>Use</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_BODY__USE = eINSTANCE.getSOAPBody_Use();

    /**
     * The meta object literal for the '<em><b>Namespace URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_BODY__NAMESPACE_URI = eINSTANCE.getSOAPBody_NamespaceURI();

    /**
     * The meta object literal for the '<em><b>EEncoding Styles</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_BODY__EENCODING_STYLES = eINSTANCE.getSOAPBody_EEncodingStyles();

    /**
     * The meta object literal for the '<em><b>EParts</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SOAP_BODY__EPARTS = eINSTANCE.getSOAPBody_EParts();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl <em>Header Base</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeaderBase()
     * @generated
     */
    EClass SOAP_HEADER_BASE = eINSTANCE.getSOAPHeaderBase();

    /**
     * The meta object literal for the '<em><b>Use</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_HEADER_BASE__USE = eINSTANCE.getSOAPHeaderBase_Use();

    /**
     * The meta object literal for the '<em><b>Namespace URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_HEADER_BASE__NAMESPACE_URI = eINSTANCE.getSOAPHeaderBase_NamespaceURI();

    /**
     * The meta object literal for the '<em><b>EEncoding Styles</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_HEADER_BASE__EENCODING_STYLES = eINSTANCE.getSOAPHeaderBase_EEncodingStyles();

    /**
     * The meta object literal for the '<em><b>Message</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_HEADER_BASE__MESSAGE = eINSTANCE.getSOAPHeaderBase_Message();

    /**
     * The meta object literal for the '<em><b>Part</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_HEADER_BASE__PART = eINSTANCE.getSOAPHeaderBase_Part();

    /**
     * The meta object literal for the '<em><b>EMessage</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SOAP_HEADER_BASE__EMESSAGE = eINSTANCE.getSOAPHeaderBase_EMessage();

    /**
     * The meta object literal for the '<em><b>EPart</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SOAP_HEADER_BASE__EPART = eINSTANCE.getSOAPHeaderBase_EPart();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl <em>Fault</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPFault()
     * @generated
     */
    EClass SOAP_FAULT = eINSTANCE.getSOAPFault();

    /**
     * The meta object literal for the '<em><b>Use</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_FAULT__USE = eINSTANCE.getSOAPFault_Use();

    /**
     * The meta object literal for the '<em><b>Namespace URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_FAULT__NAMESPACE_URI = eINSTANCE.getSOAPFault_NamespaceURI();

    /**
     * The meta object literal for the '<em><b>EEncoding Styles</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_FAULT__EENCODING_STYLES = eINSTANCE.getSOAPFault_EEncodingStyles();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_FAULT__NAME = eINSTANCE.getSOAPFault_Name();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl <em>Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPOperation()
     * @generated
     */
    EClass SOAP_OPERATION = eINSTANCE.getSOAPOperation();

    /**
     * The meta object literal for the '<em><b>Soap Action URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_OPERATION__SOAP_ACTION_URI = eINSTANCE.getSOAPOperation_SoapActionURI();

    /**
     * The meta object literal for the '<em><b>Style</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_OPERATION__STYLE = eINSTANCE.getSOAPOperation_Style();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPAddressImpl <em>Address</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPAddressImpl
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPAddress()
     * @generated
     */
    EClass SOAP_ADDRESS = eINSTANCE.getSOAPAddress();

    /**
     * The meta object literal for the '<em><b>Location URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SOAP_ADDRESS__LOCATION_URI = eINSTANCE.getSOAPAddress_LocationURI();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderFaultImpl <em>Header Fault</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderFaultImpl
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeaderFault()
     * @generated
     */
    EClass SOAP_HEADER_FAULT = eINSTANCE.getSOAPHeaderFault();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderImpl <em>Header</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderImpl
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeader()
     * @generated
     */
    EClass SOAP_HEADER = eINSTANCE.getSOAPHeader();

    /**
     * The meta object literal for the '<em><b>Header Faults</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SOAP_HEADER__HEADER_FAULTS = eINSTANCE.getSOAPHeader_HeaderFaults();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.soap.SOAPBinding <em>ISOAP Binding</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.soap.SOAPBinding
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPBinding()
     * @generated
     */
    EClass ISOAP_BINDING = eINSTANCE.getISOAPBinding();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.soap.SOAPAddress <em>ISOAP Address</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.soap.SOAPAddress
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPAddress()
     * @generated
     */
    EClass ISOAP_ADDRESS = eINSTANCE.getISOAPAddress();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.soap.SOAPBody <em>ISOAP Body</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.soap.SOAPBody
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPBody()
     * @generated
     */
    EClass ISOAP_BODY = eINSTANCE.getISOAPBody();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.soap.SOAPFault <em>ISOAP Fault</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.soap.SOAPFault
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPFault()
     * @generated
     */
    EClass ISOAP_FAULT = eINSTANCE.getISOAPFault();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.soap.SOAPOperation <em>ISOAP Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.soap.SOAPOperation
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPOperation()
     * @generated
     */
    EClass ISOAP_OPERATION = eINSTANCE.getISOAPOperation();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.soap.SOAPHeaderFault <em>ISOAP Header Fault</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.soap.SOAPHeaderFault
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPHeaderFault()
     * @generated
     */
    EClass ISOAP_HEADER_FAULT = eINSTANCE.getISOAPHeaderFault();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.soap.SOAPHeader <em>ISOAP Header</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.soap.SOAPHeader
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getISOAPHeader()
     * @generated
     */
    EClass ISOAP_HEADER = eINSTANCE.getISOAPHeader();

    /**
     * The meta object literal for the '<em>IString</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getIString()
     * @generated
     */
    EDataType ISTRING = eINSTANCE.getIString();

  }

} //SOAPPackage
