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
package org.eclipse.wst.wsdl.binding.http;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
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
 * @see org.eclipse.wst.wsdl.binding.http.HTTPFactory
 * @model kind="package"
 * @generated
 */
public interface HTTPPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "http"; //$NON-NLS-1$

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/wsdl/2003/HTTP"; //$NON-NLS-1$

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "http"; //$NON-NLS-1$

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  HTTPPackage eINSTANCE = org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPBindingImpl <em>Binding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPBindingImpl
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPBinding()
   * @generated
   */
  int HTTP_BINDING = 0;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_BINDING__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_BINDING__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_BINDING__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_BINDING__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Verb</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_BINDING__VERB = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Binding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_BINDING_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPOperationImpl <em>Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPOperationImpl
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPOperation()
   * @generated
   */
  int HTTP_OPERATION = 1;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_OPERATION__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_OPERATION__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_OPERATION__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_OPERATION__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Location URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_OPERATION__LOCATION_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_OPERATION_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPUrlReplacementImpl <em>Url Replacement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPUrlReplacementImpl
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPUrlReplacement()
   * @generated
   */
  int HTTP_URL_REPLACEMENT = 2;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_REPLACEMENT__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_REPLACEMENT__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_REPLACEMENT__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_REPLACEMENT__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The number of structural features of the '<em>Url Replacement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_REPLACEMENT_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPUrlEncodedImpl <em>Url Encoded</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPUrlEncodedImpl
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPUrlEncoded()
   * @generated
   */
  int HTTP_URL_ENCODED = 3;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_ENCODED__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_ENCODED__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_ENCODED__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_ENCODED__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The number of structural features of the '<em>Url Encoded</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_URL_ENCODED_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPAddressImpl <em>Address</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPAddressImpl
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPAddress()
   * @generated
   */
  int HTTP_ADDRESS = 4;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_ADDRESS__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_ADDRESS__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_ADDRESS__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_ADDRESS__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Location URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_ADDRESS__LOCATION_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Address</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int HTTP_ADDRESS_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.http.HTTPAddress <em>IHTTP Address</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.http.HTTPAddress
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPAddress()
   * @generated
   */
  int IHTTP_ADDRESS = 5;

  /**
   * The number of structural features of the '<em>IHTTP Address</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IHTTP_ADDRESS_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.http.HTTPBinding <em>IHTTP Binding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.http.HTTPBinding
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPBinding()
   * @generated
   */
  int IHTTP_BINDING = 6;

  /**
   * The number of structural features of the '<em>IHTTP Binding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IHTTP_BINDING_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.http.HTTPOperation <em>IHTTP Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.http.HTTPOperation
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPOperation()
   * @generated
   */
  int IHTTP_OPERATION = 7;

  /**
   * The number of structural features of the '<em>IHTTP Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IHTTP_OPERATION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.http.HTTPUrlEncoded <em>IHTTP Url Encoded</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.http.HTTPUrlEncoded
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPUrlEncoded()
   * @generated
   */
  int IHTTP_URL_ENCODED = 8;

  /**
   * The number of structural features of the '<em>IHTTP Url Encoded</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IHTTP_URL_ENCODED_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.http.HTTPUrlReplacement <em>IHTTP Url Replacement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.http.HTTPUrlReplacement
   * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPUrlReplacement()
   * @generated
   */
  int IHTTP_URL_REPLACEMENT = 9;

  /**
   * The number of structural features of the '<em>IHTTP Url Replacement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IHTTP_URL_REPLACEMENT_FEATURE_COUNT = 0;

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.http.HTTPBinding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding</em>'.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPBinding
   * @generated
   */
  EClass getHTTPBinding();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.http.HTTPBinding#getVerb <em>Verb</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Verb</em>'.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPBinding#getVerb()
   * @see #getHTTPBinding()
   * @generated
   */
  EAttribute getHTTPBinding_Verb();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.http.HTTPOperation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operation</em>'.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPOperation
   * @generated
   */
  EClass getHTTPOperation();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.http.HTTPOperation#getLocationURI <em>Location URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPOperation#getLocationURI()
   * @see #getHTTPOperation()
   * @generated
   */
  EAttribute getHTTPOperation_LocationURI();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.http.HTTPUrlReplacement <em>Url Replacement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Url Replacement</em>'.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPUrlReplacement
   * @generated
   */
  EClass getHTTPUrlReplacement();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.http.HTTPUrlEncoded <em>Url Encoded</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Url Encoded</em>'.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPUrlEncoded
   * @generated
   */
  EClass getHTTPUrlEncoded();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.http.HTTPAddress <em>Address</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Address</em>'.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPAddress
   * @generated
   */
  EClass getHTTPAddress();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.http.HTTPAddress#getLocationURI <em>Location URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.http.HTTPAddress#getLocationURI()
   * @see #getHTTPAddress()
   * @generated
   */
  EAttribute getHTTPAddress_LocationURI();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.http.HTTPAddress <em>IHTTP Address</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IHTTP Address</em>'.
   * @see javax.wsdl.extensions.http.HTTPAddress
   * @model instanceClass="javax.wsdl.extensions.http.HTTPAddress"
   * @generated
   */
  EClass getIHTTPAddress();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.http.HTTPBinding <em>IHTTP Binding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IHTTP Binding</em>'.
   * @see javax.wsdl.extensions.http.HTTPBinding
   * @model instanceClass="javax.wsdl.extensions.http.HTTPBinding"
   * @generated
   */
  EClass getIHTTPBinding();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.http.HTTPOperation <em>IHTTP Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IHTTP Operation</em>'.
   * @see javax.wsdl.extensions.http.HTTPOperation
   * @model instanceClass="javax.wsdl.extensions.http.HTTPOperation"
   * @generated
   */
  EClass getIHTTPOperation();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.http.HTTPUrlEncoded <em>IHTTP Url Encoded</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IHTTP Url Encoded</em>'.
   * @see javax.wsdl.extensions.http.HTTPUrlEncoded
   * @model instanceClass="javax.wsdl.extensions.http.HTTPUrlEncoded"
   * @generated
   */
  EClass getIHTTPUrlEncoded();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.http.HTTPUrlReplacement <em>IHTTP Url Replacement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IHTTP Url Replacement</em>'.
   * @see javax.wsdl.extensions.http.HTTPUrlReplacement
   * @model instanceClass="javax.wsdl.extensions.http.HTTPUrlReplacement"
   * @generated
   */
  EClass getIHTTPUrlReplacement();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  HTTPFactory getHTTPFactory();

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
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPBindingImpl <em>Binding</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPBindingImpl
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPBinding()
     * @generated
     */
    EClass HTTP_BINDING = eINSTANCE.getHTTPBinding();

    /**
     * The meta object literal for the '<em><b>Verb</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute HTTP_BINDING__VERB = eINSTANCE.getHTTPBinding_Verb();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPOperationImpl <em>Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPOperationImpl
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPOperation()
     * @generated
     */
    EClass HTTP_OPERATION = eINSTANCE.getHTTPOperation();

    /**
     * The meta object literal for the '<em><b>Location URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute HTTP_OPERATION__LOCATION_URI = eINSTANCE.getHTTPOperation_LocationURI();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPUrlReplacementImpl <em>Url Replacement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPUrlReplacementImpl
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPUrlReplacement()
     * @generated
     */
    EClass HTTP_URL_REPLACEMENT = eINSTANCE.getHTTPUrlReplacement();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPUrlEncodedImpl <em>Url Encoded</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPUrlEncodedImpl
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPUrlEncoded()
     * @generated
     */
    EClass HTTP_URL_ENCODED = eINSTANCE.getHTTPUrlEncoded();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPAddressImpl <em>Address</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPAddressImpl
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getHTTPAddress()
     * @generated
     */
    EClass HTTP_ADDRESS = eINSTANCE.getHTTPAddress();

    /**
     * The meta object literal for the '<em><b>Location URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute HTTP_ADDRESS__LOCATION_URI = eINSTANCE.getHTTPAddress_LocationURI();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.http.HTTPAddress <em>IHTTP Address</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.http.HTTPAddress
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPAddress()
     * @generated
     */
    EClass IHTTP_ADDRESS = eINSTANCE.getIHTTPAddress();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.http.HTTPBinding <em>IHTTP Binding</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.http.HTTPBinding
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPBinding()
     * @generated
     */
    EClass IHTTP_BINDING = eINSTANCE.getIHTTPBinding();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.http.HTTPOperation <em>IHTTP Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.http.HTTPOperation
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPOperation()
     * @generated
     */
    EClass IHTTP_OPERATION = eINSTANCE.getIHTTPOperation();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.http.HTTPUrlEncoded <em>IHTTP Url Encoded</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.http.HTTPUrlEncoded
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPUrlEncoded()
     * @generated
     */
    EClass IHTTP_URL_ENCODED = eINSTANCE.getIHTTPUrlEncoded();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.http.HTTPUrlReplacement <em>IHTTP Url Replacement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.http.HTTPUrlReplacement
     * @see org.eclipse.wst.wsdl.binding.http.internal.impl.HTTPPackageImpl#getIHTTPUrlReplacement()
     * @generated
     */
    EClass IHTTP_URL_REPLACEMENT = eINSTANCE.getIHTTPUrlReplacement();

  }

} //HTTPPackage
