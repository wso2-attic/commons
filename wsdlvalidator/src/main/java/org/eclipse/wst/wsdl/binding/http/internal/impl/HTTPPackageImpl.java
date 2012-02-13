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
package org.eclipse.wst.wsdl.binding.http.internal.impl;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.binding.http.HTTPAddress;
import org.eclipse.wst.wsdl.binding.http.HTTPBinding;
import org.eclipse.wst.wsdl.binding.http.HTTPFactory;
import org.eclipse.wst.wsdl.binding.http.HTTPOperation;
import org.eclipse.wst.wsdl.binding.http.HTTPPackage;
import org.eclipse.wst.wsdl.binding.http.HTTPUrlEncoded;
import org.eclipse.wst.wsdl.binding.http.HTTPUrlReplacement;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class HTTPPackageImpl extends EPackageImpl implements HTTPPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass httpBindingEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass httpOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass httpUrlReplacementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass httpUrlEncodedEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass httpAddressEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ihttpAddressEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ihttpBindingEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ihttpOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ihttpUrlEncodedEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass ihttpUrlReplacementEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.eclipse.wst.wsdl.binding.http.HTTPPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private HTTPPackageImpl()
  {
    super(eNS_URI, HTTPFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this
   * model, and for any others upon which it depends.  Simple
   * dependencies are satisfied by calling this method on all
   * dependent packages before doing anything else.  This method drives
   * initialization for interdependent packages directly, in parallel
   * with this package, itself.
   * <p>Of this package and its interdependencies, all packages which
   * have not yet been registered by their URI values are first created
   * and registered.  The packages are then initialized in two steps:
   * meta-model objects for all of the packages are created before any
   * are initialized, since one package's meta-model objects may refer to
   * those of another.
   * <p>Invocation of this method will not affect any packages that have
   * already been initialized.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static HTTPPackage init()
  {
    if (isInited)
      return (HTTPPackage)EPackage.Registry.INSTANCE.getEPackage(HTTPPackage.eNS_URI);

    // Obtain or create and register package
    HTTPPackageImpl theHTTPPackage = (HTTPPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof HTTPPackageImpl
      ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new HTTPPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    WSDLPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theHTTPPackage.createPackageContents();

    // Initialize created meta-data
    theHTTPPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theHTTPPackage.freeze();

    return theHTTPPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getHTTPBinding()
  {
    return httpBindingEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getHTTPBinding_Verb()
  {
    return (EAttribute)httpBindingEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getHTTPOperation()
  {
    return httpOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getHTTPOperation_LocationURI()
  {
    return (EAttribute)httpOperationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getHTTPUrlReplacement()
  {
    return httpUrlReplacementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getHTTPUrlEncoded()
  {
    return httpUrlEncodedEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getHTTPAddress()
  {
    return httpAddressEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getHTTPAddress_LocationURI()
  {
    return (EAttribute)httpAddressEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIHTTPAddress()
  {
    return ihttpAddressEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIHTTPBinding()
  {
    return ihttpBindingEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIHTTPOperation()
  {
    return ihttpOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIHTTPUrlEncoded()
  {
    return ihttpUrlEncodedEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIHTTPUrlReplacement()
  {
    return ihttpUrlReplacementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HTTPFactory getHTTPFactory()
  {
    return (HTTPFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated)
      return;
    isCreated = true;

    // Create classes and their features
    httpBindingEClass = createEClass(HTTP_BINDING);
    createEAttribute(httpBindingEClass, HTTP_BINDING__VERB);

    httpOperationEClass = createEClass(HTTP_OPERATION);
    createEAttribute(httpOperationEClass, HTTP_OPERATION__LOCATION_URI);

    httpUrlReplacementEClass = createEClass(HTTP_URL_REPLACEMENT);

    httpUrlEncodedEClass = createEClass(HTTP_URL_ENCODED);

    httpAddressEClass = createEClass(HTTP_ADDRESS);
    createEAttribute(httpAddressEClass, HTTP_ADDRESS__LOCATION_URI);

    ihttpAddressEClass = createEClass(IHTTP_ADDRESS);

    ihttpBindingEClass = createEClass(IHTTP_BINDING);

    ihttpOperationEClass = createEClass(IHTTP_OPERATION);

    ihttpUrlEncodedEClass = createEClass(IHTTP_URL_ENCODED);

    ihttpUrlReplacementEClass = createEClass(IHTTP_URL_REPLACEMENT);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized)
      return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    WSDLPackage theWSDLPackage = (WSDLPackage)EPackage.Registry.INSTANCE.getEPackage(WSDLPackage.eNS_URI);

    // Add supertypes to classes
    httpBindingEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    httpBindingEClass.getESuperTypes().add(this.getIHTTPBinding());
    httpOperationEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    httpOperationEClass.getESuperTypes().add(this.getIHTTPOperation());
    httpUrlReplacementEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    httpUrlReplacementEClass.getESuperTypes().add(this.getIHTTPUrlReplacement());
    httpUrlEncodedEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    httpUrlEncodedEClass.getESuperTypes().add(this.getIHTTPUrlEncoded());
    httpAddressEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    httpAddressEClass.getESuperTypes().add(this.getIHTTPAddress());

    // Initialize classes and features; add operations and parameters
    initEClass(httpBindingEClass, HTTPBinding.class, "HTTPBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getHTTPBinding_Verb(),
      ecorePackage.getEString(),
      "verb", null, 0, 1, HTTPBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(httpOperationEClass, HTTPOperation.class, "HTTPOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getHTTPOperation_LocationURI(),
      ecorePackage.getEString(),
      "locationURI", null, 0, 1, HTTPOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(
      httpUrlReplacementEClass,
      HTTPUrlReplacement.class,
      "HTTPUrlReplacement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(httpUrlEncodedEClass, HTTPUrlEncoded.class, "HTTPUrlEncoded", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(httpAddressEClass, HTTPAddress.class, "HTTPAddress", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getHTTPAddress_LocationURI(),
      ecorePackage.getEString(),
      "locationURI", null, 0, 1, HTTPAddress.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(
      ihttpAddressEClass,
      javax.wsdl.extensions.http.HTTPAddress.class,
      "IHTTPAddress", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      ihttpBindingEClass,
      javax.wsdl.extensions.http.HTTPBinding.class,
      "IHTTPBinding", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      ihttpOperationEClass,
      javax.wsdl.extensions.http.HTTPOperation.class,
      "IHTTPOperation", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      ihttpUrlEncodedEClass,
      javax.wsdl.extensions.http.HTTPUrlEncoded.class,
      "IHTTPUrlEncoded", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      ihttpUrlReplacementEClass,
      javax.wsdl.extensions.http.HTTPUrlReplacement.class,
      "IHTTPUrlReplacement", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    // Create resource
    createResource(eNS_URI);
  }
} //HTTPPackageImpl
