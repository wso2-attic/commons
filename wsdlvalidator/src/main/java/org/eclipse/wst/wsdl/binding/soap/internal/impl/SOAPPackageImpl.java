/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.impl;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SOAPPackageImpl extends EPackageImpl implements SOAPPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapBindingEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapBodyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapHeaderBaseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapAddressEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapHeaderFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapHeaderEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass isoapBindingEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass isoapAddressEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass isoapBodyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass isoapFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass isoapOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass isoapHeaderFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass isoapHeaderEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType iStringEDataType = null;

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
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private SOAPPackageImpl()
  {
    super(eNS_URI, SOAPFactory.eINSTANCE);
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
  public static SOAPPackage init()
  {
    if (isInited)
      return (SOAPPackage)EPackage.Registry.INSTANCE.getEPackage(SOAPPackage.eNS_URI);

    // Obtain or create and register package
    SOAPPackageImpl theSOAPPackage = (SOAPPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof SOAPPackageImpl
      ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new SOAPPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    WSDLPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theSOAPPackage.createPackageContents();

    // Initialize created meta-data
    theSOAPPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theSOAPPackage.freeze();

    return theSOAPPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPBinding()
  {
    return soapBindingEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBinding_TransportURI()
  {
    return (EAttribute)soapBindingEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBinding_Style()
  {
    return (EAttribute)soapBindingEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPBody()
  {
    return soapBodyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBody_Use()
  {
    return (EAttribute)soapBodyEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBody_NamespaceURI()
  {
    return (EAttribute)soapBodyEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBody_EEncodingStyles()
  {
    return (EAttribute)soapBodyEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOAPBody_EParts()
  {
    return (EReference)soapBodyEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPHeaderBase()
  {
    return soapHeaderBaseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPHeaderBase_Use()
  {
    return (EAttribute)soapHeaderBaseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPHeaderBase_NamespaceURI()
  {
    return (EAttribute)soapHeaderBaseEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPHeaderBase_EEncodingStyles()
  {
    return (EAttribute)soapHeaderBaseEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPHeaderBase_Message()
  {
    return (EAttribute)soapHeaderBaseEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPHeaderBase_Part()
  {
    return (EAttribute)soapHeaderBaseEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOAPHeaderBase_EMessage()
  {
    return (EReference)soapHeaderBaseEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOAPHeaderBase_EPart()
  {
    return (EReference)soapHeaderBaseEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPFault()
  {
    return soapFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPFault_Use()
  {
    return (EAttribute)soapFaultEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPFault_NamespaceURI()
  {
    return (EAttribute)soapFaultEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPFault_EEncodingStyles()
  {
    return (EAttribute)soapFaultEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPFault_Name()
  {
    return (EAttribute)soapFaultEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPOperation()
  {
    return soapOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPOperation_SoapActionURI()
  {
    return (EAttribute)soapOperationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPOperation_Style()
  {
    return (EAttribute)soapOperationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPAddress()
  {
    return soapAddressEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPAddress_LocationURI()
  {
    return (EAttribute)soapAddressEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPHeaderFault()
  {
    return soapHeaderFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPHeader()
  {
    return soapHeaderEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOAPHeader_HeaderFaults()
  {
    return (EReference)soapHeaderEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getISOAPBinding()
  {
    return isoapBindingEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getISOAPAddress()
  {
    return isoapAddressEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getISOAPBody()
  {
    return isoapBodyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getISOAPFault()
  {
    return isoapFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getISOAPOperation()
  {
    return isoapOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getISOAPHeaderFault()
  {
    return isoapHeaderFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getISOAPHeader()
  {
    return isoapHeaderEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getIString()
  {
    return iStringEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPFactory getSOAPFactory()
  {
    return (SOAPFactory)getEFactoryInstance();
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
    soapBindingEClass = createEClass(SOAP_BINDING);
    createEAttribute(soapBindingEClass, SOAP_BINDING__TRANSPORT_URI);
    createEAttribute(soapBindingEClass, SOAP_BINDING__STYLE);

    soapBodyEClass = createEClass(SOAP_BODY);
    createEAttribute(soapBodyEClass, SOAP_BODY__USE);
    createEAttribute(soapBodyEClass, SOAP_BODY__NAMESPACE_URI);
    createEAttribute(soapBodyEClass, SOAP_BODY__EENCODING_STYLES);
    createEReference(soapBodyEClass, SOAP_BODY__EPARTS);

    soapHeaderBaseEClass = createEClass(SOAP_HEADER_BASE);
    createEAttribute(soapHeaderBaseEClass, SOAP_HEADER_BASE__USE);
    createEAttribute(soapHeaderBaseEClass, SOAP_HEADER_BASE__NAMESPACE_URI);
    createEAttribute(soapHeaderBaseEClass, SOAP_HEADER_BASE__EENCODING_STYLES);
    createEAttribute(soapHeaderBaseEClass, SOAP_HEADER_BASE__MESSAGE);
    createEAttribute(soapHeaderBaseEClass, SOAP_HEADER_BASE__PART);
    createEReference(soapHeaderBaseEClass, SOAP_HEADER_BASE__EMESSAGE);
    createEReference(soapHeaderBaseEClass, SOAP_HEADER_BASE__EPART);

    soapFaultEClass = createEClass(SOAP_FAULT);
    createEAttribute(soapFaultEClass, SOAP_FAULT__USE);
    createEAttribute(soapFaultEClass, SOAP_FAULT__NAMESPACE_URI);
    createEAttribute(soapFaultEClass, SOAP_FAULT__EENCODING_STYLES);
    createEAttribute(soapFaultEClass, SOAP_FAULT__NAME);

    soapOperationEClass = createEClass(SOAP_OPERATION);
    createEAttribute(soapOperationEClass, SOAP_OPERATION__SOAP_ACTION_URI);
    createEAttribute(soapOperationEClass, SOAP_OPERATION__STYLE);

    soapAddressEClass = createEClass(SOAP_ADDRESS);
    createEAttribute(soapAddressEClass, SOAP_ADDRESS__LOCATION_URI);

    soapHeaderFaultEClass = createEClass(SOAP_HEADER_FAULT);

    soapHeaderEClass = createEClass(SOAP_HEADER);
    createEReference(soapHeaderEClass, SOAP_HEADER__HEADER_FAULTS);

    isoapBindingEClass = createEClass(ISOAP_BINDING);

    isoapAddressEClass = createEClass(ISOAP_ADDRESS);

    isoapBodyEClass = createEClass(ISOAP_BODY);

    isoapFaultEClass = createEClass(ISOAP_FAULT);

    isoapOperationEClass = createEClass(ISOAP_OPERATION);

    isoapHeaderFaultEClass = createEClass(ISOAP_HEADER_FAULT);

    isoapHeaderEClass = createEClass(ISOAP_HEADER);

    // Create data types
    iStringEDataType = createEDataType(ISTRING);
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
    soapBindingEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapBindingEClass.getESuperTypes().add(this.getISOAPBinding());
    soapBodyEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapBodyEClass.getESuperTypes().add(this.getISOAPBody());
    soapHeaderBaseEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapFaultEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapFaultEClass.getESuperTypes().add(this.getISOAPFault());
    soapOperationEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapOperationEClass.getESuperTypes().add(this.getISOAPOperation());
    soapAddressEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapAddressEClass.getESuperTypes().add(this.getISOAPAddress());
    soapHeaderFaultEClass.getESuperTypes().add(this.getSOAPHeaderBase());
    soapHeaderFaultEClass.getESuperTypes().add(this.getISOAPHeaderFault());
    soapHeaderEClass.getESuperTypes().add(this.getSOAPHeaderBase());
    soapHeaderEClass.getESuperTypes().add(this.getISOAPHeader());

    // Initialize classes and features; add operations and parameters
    initEClass(soapBindingEClass, SOAPBinding.class, "SOAPBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getSOAPBinding_TransportURI(),
      ecorePackage.getEString(),
      "transportURI", null, 0, 1, SOAPBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPBinding_Style(),
      ecorePackage.getEString(),
      "style", null, 0, 1, SOAPBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(soapBodyEClass, SOAPBody.class, "SOAPBody", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getSOAPBody_Use(),
      ecorePackage.getEString(),
      "use", null, 0, 1, SOAPBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPBody_NamespaceURI(),
      ecorePackage.getEString(),
      "namespaceURI", null, 0, 1, SOAPBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPBody_EEncodingStyles(),
      this.getIString(),
      "eEncodingStyles", null, 0, -1, SOAPBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getSOAPBody_EParts(),
      theWSDLPackage.getPart(),
      null,
      "eParts", null, 0, -1, SOAPBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(soapHeaderBaseEClass, SOAPHeaderBase.class, "SOAPHeaderBase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getSOAPHeaderBase_Use(),
      ecorePackage.getEString(),
      "use", null, 0, 1, SOAPHeaderBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPHeaderBase_NamespaceURI(),
      ecorePackage.getEString(),
      "namespaceURI", null, 0, 1, SOAPHeaderBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPHeaderBase_EEncodingStyles(),
      this.getIString(),
      "eEncodingStyles", null, 0, -1, SOAPHeaderBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPHeaderBase_Message(),
      theWSDLPackage.getQName(),
      "message", null, 0, 1, SOAPHeaderBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPHeaderBase_Part(),
      ecorePackage.getEString(),
      "part", null, 0, 1, SOAPHeaderBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getSOAPHeaderBase_EMessage(),
      theWSDLPackage.getMessage(),
      null,
      "eMessage", null, 1, 1, SOAPHeaderBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getSOAPHeaderBase_EPart(),
      theWSDLPackage.getPart(),
      null,
      "ePart", null, 1, 1, SOAPHeaderBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    addEOperation(soapHeaderBaseEClass, theWSDLPackage.getIList(), "getEncodingStyles", 0, 1); //$NON-NLS-1$

    EOperation op = addEOperation(soapHeaderBaseEClass, null, "setEncodingStyles"); //$NON-NLS-1$
    addEParameter(op, theWSDLPackage.getIList(), "encodingStyles", 0, 1); //$NON-NLS-1$

    initEClass(soapFaultEClass, SOAPFault.class, "SOAPFault", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getSOAPFault_Use(),
      ecorePackage.getEString(),
      "use", null, 0, 1, SOAPFault.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPFault_NamespaceURI(),
      ecorePackage.getEString(),
      "namespaceURI", null, 0, 1, SOAPFault.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPFault_EEncodingStyles(),
      this.getIString(),
      "eEncodingStyles", null, 0, -1, SOAPFault.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPFault_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, SOAPFault.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(soapOperationEClass, SOAPOperation.class, "SOAPOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getSOAPOperation_SoapActionURI(),
      ecorePackage.getEString(),
      "soapActionURI", null, 0, 1, SOAPOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getSOAPOperation_Style(),
      ecorePackage.getEString(),
      "style", null, 0, 1, SOAPOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(soapAddressEClass, SOAPAddress.class, "SOAPAddress", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getSOAPAddress_LocationURI(),
      ecorePackage.getEString(),
      "locationURI", null, 0, 1, SOAPAddress.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(soapHeaderFaultEClass, SOAPHeaderFault.class, "SOAPHeaderFault", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(soapHeaderEClass, SOAPHeader.class, "SOAPHeader", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(
      getSOAPHeader_HeaderFaults(),
      this.getSOAPHeaderFault(),
      null,
      "headerFaults", null, 0, -1, SOAPHeader.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(
      isoapBindingEClass,
      javax.wsdl.extensions.soap.SOAPBinding.class,
      "ISOAPBinding", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      isoapAddressEClass,
      javax.wsdl.extensions.soap.SOAPAddress.class,
      "ISOAPAddress", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      isoapBodyEClass,
      javax.wsdl.extensions.soap.SOAPBody.class,
      "ISOAPBody", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    addEOperation(isoapBodyEClass, theWSDLPackage.getIList(), "getEncodingStyles", 0, 1); //$NON-NLS-1$

    op = addEOperation(isoapBodyEClass, null, "setEncodingStyles"); //$NON-NLS-1$
    addEParameter(op, theWSDLPackage.getIList(), "encodingStyles", 0, 1); //$NON-NLS-1$

    addEOperation(isoapBodyEClass, theWSDLPackage.getIList(), "getParts", 0, 1); //$NON-NLS-1$

    op = addEOperation(isoapBodyEClass, null, "setParts"); //$NON-NLS-1$
    addEParameter(op, theWSDLPackage.getIList(), "parts", 0, 1); //$NON-NLS-1$

    initEClass(
      isoapFaultEClass,
      javax.wsdl.extensions.soap.SOAPFault.class,
      "ISOAPFault", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      isoapOperationEClass,
      javax.wsdl.extensions.soap.SOAPOperation.class,
      "ISOAPOperation", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      isoapHeaderFaultEClass,
      javax.wsdl.extensions.soap.SOAPHeaderFault.class,
      "ISOAPHeaderFault", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      isoapHeaderEClass,
      javax.wsdl.extensions.soap.SOAPHeader.class,
      "ISOAPHeader", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    addEOperation(isoapHeaderEClass, theWSDLPackage.getIList(), "getSOAPHeaderFaults", 0, 1); //$NON-NLS-1$

    op = addEOperation(isoapHeaderEClass, null, "addSOAPHeaderFault"); //$NON-NLS-1$
    addEParameter(op, this.getISOAPHeaderFault(), "soapHeaderFault", 0, 1); //$NON-NLS-1$

    // Initialize data types
    initEDataType(iStringEDataType, String.class, "IString", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    // Create resource
    createResource(eNS_URI);
  }
} //SOAPPackageImpl
