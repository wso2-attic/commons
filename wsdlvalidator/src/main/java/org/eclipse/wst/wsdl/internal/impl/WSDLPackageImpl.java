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
package org.eclipse.wst.wsdl.internal.impl;


import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.OperationType;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.AttributeExtensible;
import javax.wsdl.extensions.ElementExtensible;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.extensions.schema.Schema;
import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Namespace;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.xsd.XSDPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WSDLPackageImpl extends EPackageImpl implements WSDLPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass wsdlElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass portTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass operationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass messageEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass partEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass bindingEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass bindingOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass serviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass portEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass extensibilityElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass definitionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass importEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass extensibleElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass inputEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass outputEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass faultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass bindingInputEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass bindingOutputEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass bindingFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass namespaceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iPortTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iInputEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iOutputEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iMessageEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iPartEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iServiceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iPortEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iBindingEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iBindingOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iBindingInputEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iBindingOutputEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iBindingFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iExtensibilityElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iDefinitionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iImportEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iListEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iMapEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iurlEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iExtensionRegistryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass typesEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iIteratorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iTypesEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass unknownExtensibilityElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass xsdSchemaExtensibilityElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass messageReferenceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iElementExtensibleEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iAttributeExtensibleEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iObjectEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass iSchemaEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType qNameEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType operationTypeEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType domElementEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType wsdlExceptionEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType domDocumentEDataType = null;

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
   * @see org.eclipse.wst.wsdl.WSDLPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private WSDLPackageImpl()
  {
    super(eNS_URI, WSDLFactory.eINSTANCE);
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
  public static WSDLPackage init()
  {
    if (isInited)
      return (WSDLPackage)EPackage.Registry.INSTANCE.getEPackage(WSDLPackage.eNS_URI);

    // Obtain or create and register package
    WSDLPackageImpl theWSDLPackage = (WSDLPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof WSDLPackageImpl
      ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new WSDLPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    XSDPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theWSDLPackage.createPackageContents();

    // Initialize created meta-data
    theWSDLPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theWSDLPackage.freeze();

    return theWSDLPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWSDLElement()
  {
    return wsdlElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getWSDLElement_DocumentationElement()
  {
    return (EAttribute)wsdlElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getWSDLElement_Element()
  {
    return (EAttribute)wsdlElementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPortType()
  {
    return portTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPortType_QName()
  {
    return (EAttribute)portTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPortType_Undefined()
  {
    return (EAttribute)portTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPortType_EOperations()
  {
    return (EReference)portTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOperation()
  {
    return operationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOperation_Style()
  {
    return (EAttribute)operationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOperation_Name()
  {
    return (EAttribute)operationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getOperation_Undefined()
  {
    return (EAttribute)operationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperation_EInput()
  {
    return (EReference)operationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperation_EOutput()
  {
    return (EReference)operationEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperation_EFaults()
  {
    return (EReference)operationEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getOperation_EParameterOrdering()
  {
    return (EReference)operationEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMessage()
  {
    return messageEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMessage_QName()
  {
    return (EAttribute)messageEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMessage_Undefined()
  {
    return (EAttribute)messageEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMessage_EParts()
  {
    return (EReference)messageEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPart()
  {
    return partEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPart_Name()
  {
    return (EAttribute)partEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPart_ElementName()
  {
    return (EAttribute)partEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPart_TypeName()
  {
    return (EAttribute)partEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPart_TypeDefinition()
  {
    return (EReference)partEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPart_ElementDeclaration()
  {
    return (EReference)partEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPart_EMessage()
  {
    return (EReference)partEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBinding()
  {
    return bindingEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBinding_QName()
  {
    return (EAttribute)bindingEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBinding_Undefined()
  {
    return (EAttribute)bindingEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBinding_EPortType()
  {
    return (EReference)bindingEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBinding_EBindingOperations()
  {
    return (EReference)bindingEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBindingOperation()
  {
    return bindingOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBindingOperation_Name()
  {
    return (EAttribute)bindingOperationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBindingOperation_EOperation()
  {
    return (EReference)bindingOperationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBindingOperation_EBindingInput()
  {
    return (EReference)bindingOperationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBindingOperation_EBindingOutput()
  {
    return (EReference)bindingOperationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBindingOperation_EBindingFaults()
  {
    return (EReference)bindingOperationEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getService()
  {
    return serviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getService_QName()
  {
    return (EAttribute)serviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getService_Undefined()
  {
    return (EAttribute)serviceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getService_EPorts()
  {
    return (EReference)serviceEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPort()
  {
    return portEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPort_Name()
  {
    return (EAttribute)portEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getPort_EBinding()
  {
    return (EReference)portEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExtensibilityElement()
  {
    return extensibilityElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getExtensibilityElement_Required()
  {
    return (EAttribute)extensibilityElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getExtensibilityElement_ElementType()
  {
    return (EAttribute)extensibilityElementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDefinition()
  {
    return definitionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefinition_TargetNamespace()
  {
    return (EAttribute)definitionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefinition_Location()
  {
    return (EAttribute)definitionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefinition_QName()
  {
    return (EAttribute)definitionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefinition_Encoding()
  {
    return (EAttribute)definitionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_EMessages()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_EPortTypes()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_EBindings()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_EServices()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(9);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_ENamespaces()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(10);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_ETypes()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_EImports()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getImport()
  {
    return importEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImport_NamespaceURI()
  {
    return (EAttribute)importEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImport_LocationURI()
  {
    return (EAttribute)importEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getImport_EDefinition()
  {
    return (EReference)importEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getImport_ESchema()
  {
    return (EReference)importEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getExtensibleElement()
  {
    return extensibleElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getExtensibleElement_EExtensibilityElements()
  {
    return (EReference)extensibleElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInput()
  {
    return inputEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getOutput()
  {
    return outputEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFault()
  {
    return faultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBindingInput()
  {
    return bindingInputEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBindingInput_Name()
  {
    return (EAttribute)bindingInputEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBindingInput_EInput()
  {
    return (EReference)bindingInputEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBindingOutput()
  {
    return bindingOutputEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBindingOutput_Name()
  {
    return (EAttribute)bindingOutputEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBindingOutput_EOutput()
  {
    return (EReference)bindingOutputEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBindingFault()
  {
    return bindingFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBindingFault_Name()
  {
    return (EAttribute)bindingFaultEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getBindingFault_EFault()
  {
    return (EReference)bindingFaultEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getNamespace()
  {
    return namespaceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getNamespace_URI()
  {
    return (EAttribute)namespaceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getNamespace_Prefix()
  {
    return (EAttribute)namespaceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIPortType()
  {
    return iPortTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIOperation()
  {
    return iOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIInput()
  {
    return iInputEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIOutput()
  {
    return iOutputEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIFault()
  {
    return iFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIMessage()
  {
    return iMessageEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIPart()
  {
    return iPartEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIService()
  {
    return iServiceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIPort()
  {
    return iPortEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIBinding()
  {
    return iBindingEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIBindingOperation()
  {
    return iBindingOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIBindingInput()
  {
    return iBindingInputEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIBindingOutput()
  {
    return iBindingOutputEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIBindingFault()
  {
    return iBindingFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIExtensibilityElement()
  {
    return iExtensibilityElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIDefinition()
  {
    return iDefinitionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIImport()
  {
    return iImportEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIList()
  {
    return iListEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIMap()
  {
    return iMapEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIURL()
  {
    return iurlEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIExtensionRegistry()
  {
    return iExtensionRegistryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTypes()
  {
    return typesEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIIterator()
  {
    return iIteratorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getITypes()
  {
    return iTypesEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getUnknownExtensibilityElement()
  {
    return unknownExtensibilityElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getUnknownExtensibilityElement_Children()
  {
    return (EReference)unknownExtensibilityElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getXSDSchemaExtensibilityElement()
  {
    return xsdSchemaExtensibilityElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getXSDSchemaExtensibilityElement_DocumentBaseURI()
  {
    return (EAttribute)xsdSchemaExtensibilityElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getXSDSchemaExtensibilityElement_Schema()
  {
    return (EReference)xsdSchemaExtensibilityElementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMessageReference()
  {
    return messageReferenceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMessageReference_Name()
  {
    return (EAttribute)messageReferenceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMessageReference_EMessage()
  {
    return (EReference)messageReferenceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIElementExtensible()
  {
    return iElementExtensibleEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIAttributeExtensible()
  {
    return iAttributeExtensibleEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIObject()
  {
    return iObjectEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getISchema()
  {
    return iSchemaEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getQName()
  {
    return qNameEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getOperationType()
  {
    return operationTypeEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getDOMElement()
  {
    return domElementEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getWSDLException()
  {
    return wsdlExceptionEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getDOMDocument()
  {
    return domDocumentEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WSDLFactory getWSDLFactory()
  {
    return (WSDLFactory)getEFactoryInstance();
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
    wsdlElementEClass = createEClass(WSDL_ELEMENT);
    createEAttribute(wsdlElementEClass, WSDL_ELEMENT__DOCUMENTATION_ELEMENT);
    createEAttribute(wsdlElementEClass, WSDL_ELEMENT__ELEMENT);

    portTypeEClass = createEClass(PORT_TYPE);
    createEAttribute(portTypeEClass, PORT_TYPE__QNAME);
    createEAttribute(portTypeEClass, PORT_TYPE__UNDEFINED);
    createEReference(portTypeEClass, PORT_TYPE__EOPERATIONS);

    operationEClass = createEClass(OPERATION);
    createEAttribute(operationEClass, OPERATION__STYLE);
    createEAttribute(operationEClass, OPERATION__NAME);
    createEAttribute(operationEClass, OPERATION__UNDEFINED);
    createEReference(operationEClass, OPERATION__EINPUT);
    createEReference(operationEClass, OPERATION__EOUTPUT);
    createEReference(operationEClass, OPERATION__EFAULTS);
    createEReference(operationEClass, OPERATION__EPARAMETER_ORDERING);

    messageEClass = createEClass(MESSAGE);
    createEAttribute(messageEClass, MESSAGE__QNAME);
    createEAttribute(messageEClass, MESSAGE__UNDEFINED);
    createEReference(messageEClass, MESSAGE__EPARTS);

    partEClass = createEClass(PART);
    createEAttribute(partEClass, PART__NAME);
    createEAttribute(partEClass, PART__ELEMENT_NAME);
    createEAttribute(partEClass, PART__TYPE_NAME);
    createEReference(partEClass, PART__TYPE_DEFINITION);
    createEReference(partEClass, PART__ELEMENT_DECLARATION);
    createEReference(partEClass, PART__EMESSAGE);

    bindingEClass = createEClass(BINDING);
    createEAttribute(bindingEClass, BINDING__QNAME);
    createEAttribute(bindingEClass, BINDING__UNDEFINED);
    createEReference(bindingEClass, BINDING__EPORT_TYPE);
    createEReference(bindingEClass, BINDING__EBINDING_OPERATIONS);

    bindingOperationEClass = createEClass(BINDING_OPERATION);
    createEAttribute(bindingOperationEClass, BINDING_OPERATION__NAME);
    createEReference(bindingOperationEClass, BINDING_OPERATION__EOPERATION);
    createEReference(bindingOperationEClass, BINDING_OPERATION__EBINDING_INPUT);
    createEReference(bindingOperationEClass, BINDING_OPERATION__EBINDING_OUTPUT);
    createEReference(bindingOperationEClass, BINDING_OPERATION__EBINDING_FAULTS);

    serviceEClass = createEClass(SERVICE);
    createEAttribute(serviceEClass, SERVICE__QNAME);
    createEAttribute(serviceEClass, SERVICE__UNDEFINED);
    createEReference(serviceEClass, SERVICE__EPORTS);

    portEClass = createEClass(PORT);
    createEAttribute(portEClass, PORT__NAME);
    createEReference(portEClass, PORT__EBINDING);

    extensibilityElementEClass = createEClass(EXTENSIBILITY_ELEMENT);
    createEAttribute(extensibilityElementEClass, EXTENSIBILITY_ELEMENT__REQUIRED);
    createEAttribute(extensibilityElementEClass, EXTENSIBILITY_ELEMENT__ELEMENT_TYPE);

    definitionEClass = createEClass(DEFINITION);
    createEAttribute(definitionEClass, DEFINITION__TARGET_NAMESPACE);
    createEAttribute(definitionEClass, DEFINITION__LOCATION);
    createEAttribute(definitionEClass, DEFINITION__QNAME);
    createEAttribute(definitionEClass, DEFINITION__ENCODING);
    createEReference(definitionEClass, DEFINITION__EIMPORTS);
    createEReference(definitionEClass, DEFINITION__ETYPES);
    createEReference(definitionEClass, DEFINITION__EMESSAGES);
    createEReference(definitionEClass, DEFINITION__EPORT_TYPES);
    createEReference(definitionEClass, DEFINITION__EBINDINGS);
    createEReference(definitionEClass, DEFINITION__ESERVICES);
    createEReference(definitionEClass, DEFINITION__ENAMESPACES);

    importEClass = createEClass(IMPORT);
    createEAttribute(importEClass, IMPORT__NAMESPACE_URI);
    createEAttribute(importEClass, IMPORT__LOCATION_URI);
    createEReference(importEClass, IMPORT__EDEFINITION);
    createEReference(importEClass, IMPORT__ESCHEMA);

    extensibleElementEClass = createEClass(EXTENSIBLE_ELEMENT);
    createEReference(extensibleElementEClass, EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS);

    inputEClass = createEClass(INPUT);

    outputEClass = createEClass(OUTPUT);

    faultEClass = createEClass(FAULT);

    bindingInputEClass = createEClass(BINDING_INPUT);
    createEAttribute(bindingInputEClass, BINDING_INPUT__NAME);
    createEReference(bindingInputEClass, BINDING_INPUT__EINPUT);

    bindingOutputEClass = createEClass(BINDING_OUTPUT);
    createEAttribute(bindingOutputEClass, BINDING_OUTPUT__NAME);
    createEReference(bindingOutputEClass, BINDING_OUTPUT__EOUTPUT);

    bindingFaultEClass = createEClass(BINDING_FAULT);
    createEAttribute(bindingFaultEClass, BINDING_FAULT__NAME);
    createEReference(bindingFaultEClass, BINDING_FAULT__EFAULT);

    namespaceEClass = createEClass(NAMESPACE);
    createEAttribute(namespaceEClass, NAMESPACE__URI);
    createEAttribute(namespaceEClass, NAMESPACE__PREFIX);

    iPortTypeEClass = createEClass(IPORT_TYPE);

    iOperationEClass = createEClass(IOPERATION);

    iInputEClass = createEClass(IINPUT);

    iOutputEClass = createEClass(IOUTPUT);

    iFaultEClass = createEClass(IFAULT);

    iMessageEClass = createEClass(IMESSAGE);

    iPartEClass = createEClass(IPART);

    iServiceEClass = createEClass(ISERVICE);

    iPortEClass = createEClass(IPORT);

    iBindingEClass = createEClass(IBINDING);

    iBindingOperationEClass = createEClass(IBINDING_OPERATION);

    iBindingInputEClass = createEClass(IBINDING_INPUT);

    iBindingOutputEClass = createEClass(IBINDING_OUTPUT);

    iBindingFaultEClass = createEClass(IBINDING_FAULT);

    iExtensibilityElementEClass = createEClass(IEXTENSIBILITY_ELEMENT);

    iDefinitionEClass = createEClass(IDEFINITION);

    iImportEClass = createEClass(IIMPORT);

    iListEClass = createEClass(ILIST);

    iMapEClass = createEClass(IMAP);

    iurlEClass = createEClass(IURL);

    iExtensionRegistryEClass = createEClass(IEXTENSION_REGISTRY);

    typesEClass = createEClass(TYPES);

    iIteratorEClass = createEClass(IITERATOR);

    iTypesEClass = createEClass(ITYPES);

    unknownExtensibilityElementEClass = createEClass(UNKNOWN_EXTENSIBILITY_ELEMENT);
    createEReference(unknownExtensibilityElementEClass, UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN);

    xsdSchemaExtensibilityElementEClass = createEClass(XSD_SCHEMA_EXTENSIBILITY_ELEMENT);
    createEAttribute(xsdSchemaExtensibilityElementEClass, XSD_SCHEMA_EXTENSIBILITY_ELEMENT__DOCUMENT_BASE_URI);
    createEReference(xsdSchemaExtensibilityElementEClass, XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA);

    messageReferenceEClass = createEClass(MESSAGE_REFERENCE);
    createEAttribute(messageReferenceEClass, MESSAGE_REFERENCE__NAME);
    createEReference(messageReferenceEClass, MESSAGE_REFERENCE__EMESSAGE);

    iElementExtensibleEClass = createEClass(IELEMENT_EXTENSIBLE);

    iAttributeExtensibleEClass = createEClass(IATTRIBUTE_EXTENSIBLE);

    iObjectEClass = createEClass(IOBJECT);

    iSchemaEClass = createEClass(ISCHEMA);

    // Create data types
    qNameEDataType = createEDataType(QNAME);
    operationTypeEDataType = createEDataType(OPERATION_TYPE);
    domElementEDataType = createEDataType(DOM_ELEMENT);
    wsdlExceptionEDataType = createEDataType(WSDL_EXCEPTION);
    domDocumentEDataType = createEDataType(DOM_DOCUMENT);
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
    XSDPackage theXSDPackage = (XSDPackage)EPackage.Registry.INSTANCE.getEPackage(XSDPackage.eNS_URI);

    // Add supertypes to classes
    portTypeEClass.getESuperTypes().add(this.getExtensibleElement());
    portTypeEClass.getESuperTypes().add(this.getIPortType());
    operationEClass.getESuperTypes().add(this.getExtensibleElement());
    operationEClass.getESuperTypes().add(this.getIOperation());
    messageEClass.getESuperTypes().add(this.getExtensibleElement());
    messageEClass.getESuperTypes().add(this.getIMessage());
    partEClass.getESuperTypes().add(this.getExtensibleElement());
    partEClass.getESuperTypes().add(this.getIPart());
    bindingEClass.getESuperTypes().add(this.getExtensibleElement());
    bindingEClass.getESuperTypes().add(this.getIBinding());
    bindingOperationEClass.getESuperTypes().add(this.getExtensibleElement());
    bindingOperationEClass.getESuperTypes().add(this.getIBindingOperation());
    serviceEClass.getESuperTypes().add(this.getExtensibleElement());
    serviceEClass.getESuperTypes().add(this.getIService());
    portEClass.getESuperTypes().add(this.getExtensibleElement());
    portEClass.getESuperTypes().add(this.getIPort());
    extensibilityElementEClass.getESuperTypes().add(this.getWSDLElement());
    extensibilityElementEClass.getESuperTypes().add(this.getIExtensibilityElement());
    definitionEClass.getESuperTypes().add(this.getExtensibleElement());
    definitionEClass.getESuperTypes().add(this.getIDefinition());
    importEClass.getESuperTypes().add(this.getExtensibleElement());
    importEClass.getESuperTypes().add(this.getIImport());
    extensibleElementEClass.getESuperTypes().add(this.getWSDLElement());
    extensibleElementEClass.getESuperTypes().add(this.getIElementExtensible());
    extensibleElementEClass.getESuperTypes().add(this.getIAttributeExtensible());
    inputEClass.getESuperTypes().add(this.getMessageReference());
    inputEClass.getESuperTypes().add(this.getIInput());
    outputEClass.getESuperTypes().add(this.getMessageReference());
    outputEClass.getESuperTypes().add(this.getIOutput());
    faultEClass.getESuperTypes().add(this.getMessageReference());
    faultEClass.getESuperTypes().add(this.getIFault());
    bindingInputEClass.getESuperTypes().add(this.getExtensibleElement());
    bindingInputEClass.getESuperTypes().add(this.getIBindingInput());
    bindingOutputEClass.getESuperTypes().add(this.getExtensibleElement());
    bindingOutputEClass.getESuperTypes().add(this.getIBindingOutput());
    bindingFaultEClass.getESuperTypes().add(this.getExtensibleElement());
    bindingFaultEClass.getESuperTypes().add(this.getIBindingFault());
    iPortTypeEClass.getESuperTypes().add(this.getIAttributeExtensible());
    iOperationEClass.getESuperTypes().add(this.getIElementExtensible());
    iInputEClass.getESuperTypes().add(this.getIAttributeExtensible());
    iOutputEClass.getESuperTypes().add(this.getIAttributeExtensible());
    iFaultEClass.getESuperTypes().add(this.getIAttributeExtensible());
    iMessageEClass.getESuperTypes().add(this.getIElementExtensible());
    iPartEClass.getESuperTypes().add(this.getIAttributeExtensible());
    iServiceEClass.getESuperTypes().add(this.getIElementExtensible());
    iPortEClass.getESuperTypes().add(this.getIElementExtensible());
    iBindingEClass.getESuperTypes().add(this.getIElementExtensible());
    iBindingOperationEClass.getESuperTypes().add(this.getIElementExtensible());
    iBindingInputEClass.getESuperTypes().add(this.getIElementExtensible());
    iBindingOutputEClass.getESuperTypes().add(this.getIElementExtensible());
    iBindingFaultEClass.getESuperTypes().add(this.getIElementExtensible());
    iDefinitionEClass.getESuperTypes().add(this.getIElementExtensible());
    iImportEClass.getESuperTypes().add(this.getIAttributeExtensible());
    typesEClass.getESuperTypes().add(this.getExtensibleElement());
    typesEClass.getESuperTypes().add(this.getITypes());
    unknownExtensibilityElementEClass.getESuperTypes().add(this.getExtensibilityElement());
    xsdSchemaExtensibilityElementEClass.getESuperTypes().add(this.getExtensibilityElement());
    xsdSchemaExtensibilityElementEClass.getESuperTypes().add(this.getISchema());
    messageReferenceEClass.getESuperTypes().add(this.getExtensibleElement());
    iSchemaEClass.getESuperTypes().add(this.getIExtensibilityElement());

    // Initialize classes and features; add operations and parameters
    initEClass(wsdlElementEClass, WSDLElement.class, "WSDLElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getWSDLElement_DocumentationElement(),
      this.getDOMElement(),
      "documentationElement", null, 0, 1, WSDLElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getWSDLElement_Element(),
      this.getDOMElement(),
      "element", null, 0, 1, WSDLElement.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    addEOperation(wsdlElementEClass, this.getDefinition(), "getEnclosingDefinition", 0, 1); //$NON-NLS-1$

    EOperation op = addEOperation(wsdlElementEClass, null, "setEnclosingDefinition"); //$NON-NLS-1$
    addEParameter(op, this.getDefinition(), "definition", 0, 1); //$NON-NLS-1$

    initEClass(portTypeEClass, PortType.class, "PortType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getPortType_QName(),
      this.getQName(),
      "qName", null, 0, 1, PortType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getPortType_Undefined(),
      ecorePackage.getEBoolean(),
      "undefined", null, 0, 1, PortType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getPortType_EOperations(),
      this.getOperation(),
      null,
      "eOperations", null, 0, -1, PortType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(operationEClass, Operation.class, "Operation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getOperation_Style(),
      this.getOperationType(),
      "style", null, 0, 1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getOperation_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getOperation_Undefined(),
      ecorePackage.getEBoolean(),
      "undefined", null, 0, 1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getOperation_EInput(),
      this.getInput(),
      null,
      "eInput", null, 0, 1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getOperation_EOutput(),
      this.getOutput(),
      null,
      "eOutput", null, 0, 1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getOperation_EFaults(),
      this.getFault(),
      null,
      "eFaults", null, 0, -1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getOperation_EParameterOrdering(),
      this.getPart(),
      null,
      "eParameterOrdering", null, 0, -1, Operation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(messageEClass, Message.class, "Message", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getMessage_QName(),
      this.getQName(),
      "qName", null, 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getMessage_Undefined(),
      ecorePackage.getEBoolean(),
      "undefined", null, 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getMessage_EParts(),
      this.getPart(),
      null,
      "eParts", null, 0, -1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(partEClass, Part.class, "Part", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getPart_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, Part.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getPart_ElementName(),
      this.getQName(),
      "elementName", null, 0, 1, Part.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getPart_TypeName(),
      this.getQName(),
      "typeName", null, 0, 1, Part.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getPart_TypeDefinition(),
      theXSDPackage.getXSDTypeDefinition(),
      null,
      "typeDefinition", null, 0, 1, Part.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getPart_ElementDeclaration(),
      theXSDPackage.getXSDElementDeclaration(),
      null,
      "elementDeclaration", null, 0, 1, Part.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getPart_EMessage(),
      this.getMessage(),
      null,
      "eMessage", null, 0, 1, Part.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(bindingEClass, Binding.class, "Binding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getBinding_QName(),
      this.getQName(),
      "qName", null, 0, 1, Binding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getBinding_Undefined(),
      ecorePackage.getEBoolean(),
      "undefined", null, 0, 1, Binding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBinding_EPortType(),
      this.getPortType(),
      null,
      "ePortType", null, 1, 1, Binding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBinding_EBindingOperations(),
      this.getBindingOperation(),
      null,
      "eBindingOperations", null, 0, -1, Binding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(bindingOperationEClass, BindingOperation.class, "BindingOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getBindingOperation_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, BindingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBindingOperation_EOperation(),
      this.getOperation(),
      null,
      "eOperation", null, 1, 1, BindingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBindingOperation_EBindingInput(),
      this.getBindingInput(),
      null,
      "eBindingInput", null, 0, 1, BindingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBindingOperation_EBindingOutput(),
      this.getBindingOutput(),
      null,
      "eBindingOutput", null, 0, 1, BindingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBindingOperation_EBindingFaults(),
      this.getBindingFault(),
      null,
      "eBindingFaults", null, 0, -1, BindingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(serviceEClass, Service.class, "Service", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getService_QName(),
      this.getQName(),
      "qName", null, 0, 1, Service.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getService_Undefined(),
      ecorePackage.getEBoolean(),
      "undefined", null, 0, 1, Service.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getService_EPorts(),
      this.getPort(),
      null,
      "ePorts", null, 0, -1, Service.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(portEClass, Port.class, "Port", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getPort_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getPort_EBinding(),
      this.getBinding(),
      null,
      "eBinding", null, 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(
      extensibilityElementEClass,
      ExtensibilityElement.class,
      "ExtensibilityElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getExtensibilityElement_Required(),
      ecorePackage.getEBoolean(),
      "required", null, 0, 1, ExtensibilityElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getExtensibilityElement_ElementType(),
      this.getQName(),
      "elementType", null, 0, 1, ExtensibilityElement.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(definitionEClass, Definition.class, "Definition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getDefinition_TargetNamespace(),
      ecorePackage.getEString(),
      "targetNamespace", null, 0, 1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getDefinition_Location(),
      ecorePackage.getEString(),
      "location", null, 0, 1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getDefinition_QName(),
      this.getQName(),
      "qName", null, 0, 1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getDefinition_Encoding(),
      ecorePackage.getEString(),
      "encoding", null, 0, 1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getDefinition_EImports(),
      this.getImport(),
      null,
      "eImports", null, 0, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getDefinition_ETypes(),
      this.getTypes(),
      null,
      "eTypes", null, 0, 1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getDefinition_EMessages(),
      this.getMessage(),
      null,
      "eMessages", null, 0, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getDefinition_EPortTypes(),
      this.getPortType(),
      null,
      "ePortTypes", null, 0, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getDefinition_EBindings(),
      this.getBinding(),
      null,
      "eBindings", null, 0, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getDefinition_EServices(),
      this.getService(),
      null,
      "eServices", null, 0, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getDefinition_ENamespaces(),
      this.getNamespace(),
      null,
      "eNamespaces", null, 0, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    addEOperation(definitionEClass, this.getDOMDocument(), "getDocument", 0, 1); //$NON-NLS-1$

    op = addEOperation(definitionEClass, null, "setDocument"); //$NON-NLS-1$
    addEParameter(op, this.getDOMDocument(), "document", 0, 1); //$NON-NLS-1$

    initEClass(importEClass, Import.class, "Import", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getImport_NamespaceURI(),
      ecorePackage.getEString(),
      "namespaceURI", null, 0, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getImport_LocationURI(),
      ecorePackage.getEString(),
      "locationURI", null, 0, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getImport_EDefinition(),
      this.getDefinition(),
      null,
      "eDefinition", null, 0, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getImport_ESchema(),
      theXSDPackage.getXSDSchema(),
      null,
      "eSchema", null, 0, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    addEOperation(importEClass, theXSDPackage.getXSDSchema(), "getSchema", 0, 1); //$NON-NLS-1$

    op = addEOperation(importEClass, null, "setSchema"); //$NON-NLS-1$
    addEParameter(op, theXSDPackage.getXSDSchema(), "schema", 0, 1); //$NON-NLS-1$

    initEClass(
      extensibleElementEClass,
      ExtensibleElement.class,
      "ExtensibleElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(
      getExtensibleElement_EExtensibilityElements(),
      this.getExtensibilityElement(),
      null,
      "eExtensibilityElements", null, 0, -1, ExtensibleElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(inputEClass, Input.class, "Input", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(outputEClass, Output.class, "Output", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(faultEClass, Fault.class, "Fault", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(bindingInputEClass, BindingInput.class, "BindingInput", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getBindingInput_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, BindingInput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBindingInput_EInput(),
      this.getInput(),
      null,
      "eInput", null, 1, 1, BindingInput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    addEOperation(bindingInputEClass, this.getIInput(), "getInput", 0, 1); //$NON-NLS-1$

    op = addEOperation(bindingInputEClass, null, "setInput"); //$NON-NLS-1$
    addEParameter(op, this.getIInput(), "input", 0, 1); //$NON-NLS-1$

    initEClass(bindingOutputEClass, BindingOutput.class, "BindingOutput", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getBindingOutput_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, BindingOutput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBindingOutput_EOutput(),
      this.getOutput(),
      null,
      "eOutput", null, 1, 1, BindingOutput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    addEOperation(bindingOutputEClass, this.getIOutput(), "getOutput", 0, 1); //$NON-NLS-1$

    op = addEOperation(bindingOutputEClass, null, "setOutput"); //$NON-NLS-1$
    addEParameter(op, this.getIOutput(), "output", 0, 1); //$NON-NLS-1$

    initEClass(bindingFaultEClass, BindingFault.class, "BindingFault", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getBindingFault_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, BindingFault.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getBindingFault_EFault(),
      this.getFault(),
      null,
      "eFault", null, 1, 1, BindingFault.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    addEOperation(bindingFaultEClass, this.getIFault(), "getFault", 0, 1); //$NON-NLS-1$

    op = addEOperation(bindingFaultEClass, null, "setFault"); //$NON-NLS-1$
    addEParameter(op, this.getIFault(), "fault", 0, 1); //$NON-NLS-1$

    initEClass(namespaceEClass, Namespace.class, "Namespace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getNamespace_URI(),
      ecorePackage.getEString(),
      "URI", null, 0, 1, Namespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEAttribute(
      getNamespace_Prefix(),
      ecorePackage.getEString(),
      "prefix", null, 0, 1, Namespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(iPortTypeEClass, javax.wsdl.PortType.class, "IPortType", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    op = addEOperation(iPortTypeEClass, null, "addOperation"); //$NON-NLS-1$
    addEParameter(op, this.getIOperation(), "operation", 0, 1); //$NON-NLS-1$

    op = addEOperation(iPortTypeEClass, this.getIOperation(), "getOperation", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "inputName", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "outputName", 0, 1); //$NON-NLS-1$

    addEOperation(iPortTypeEClass, this.getIList(), "getOperations", 0, 1); //$NON-NLS-1$

    initEClass(iOperationEClass, javax.wsdl.Operation.class, "IOperation", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    op = addEOperation(iOperationEClass, null, "addFault"); //$NON-NLS-1$
    addEParameter(op, this.getIFault(), "fault", 0, 1); //$NON-NLS-1$

    op = addEOperation(iOperationEClass, this.getIFault(), "getFault", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1); //$NON-NLS-1$

    addEOperation(iOperationEClass, this.getIMap(), "getFaults", 0, 1); //$NON-NLS-1$

    addEOperation(iOperationEClass, this.getIList(), "getParameterOrdering", 0, 1); //$NON-NLS-1$

    op = addEOperation(iOperationEClass, null, "setParameterOrdering"); //$NON-NLS-1$
    addEParameter(op, this.getIList(), "parameterOrder", 0, 1); //$NON-NLS-1$

    addEOperation(iOperationEClass, this.getIInput(), "getInput", 0, 1); //$NON-NLS-1$

    op = addEOperation(iOperationEClass, null, "setInput"); //$NON-NLS-1$
    addEParameter(op, this.getIInput(), "input", 0, 1); //$NON-NLS-1$

    addEOperation(iOperationEClass, this.getIOutput(), "getOutput", 0, 1); //$NON-NLS-1$

    op = addEOperation(iOperationEClass, null, "setOutput"); //$NON-NLS-1$
    addEParameter(op, this.getIOutput(), "output", 0, 1); //$NON-NLS-1$

    initEClass(iInputEClass, javax.wsdl.Input.class, "IInput", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    addEOperation(iInputEClass, this.getIMessage(), "getMessage", 0, 1); //$NON-NLS-1$

    op = addEOperation(iInputEClass, null, "setMessage"); //$NON-NLS-1$
    addEParameter(op, this.getIMessage(), "message", 0, 1); //$NON-NLS-1$

    initEClass(iOutputEClass, javax.wsdl.Output.class, "IOutput", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    addEOperation(iOutputEClass, this.getIMessage(), "getMessage", 0, 1); //$NON-NLS-1$

    op = addEOperation(iOutputEClass, null, "setMessage"); //$NON-NLS-1$
    addEParameter(op, this.getIMessage(), "message", 0, 1); //$NON-NLS-1$

    initEClass(iFaultEClass, javax.wsdl.Fault.class, "IFault", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    addEOperation(iFaultEClass, this.getIMessage(), "getMessage", 0, 1); //$NON-NLS-1$

    op = addEOperation(iFaultEClass, null, "setMessage"); //$NON-NLS-1$
    addEParameter(op, this.getIMessage(), "message", 0, 1); //$NON-NLS-1$

    initEClass(iMessageEClass, javax.wsdl.Message.class, "IMessage", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    op = addEOperation(iMessageEClass, null, "addPart"); //$NON-NLS-1$
    addEParameter(op, this.getIPart(), "part", 0, 1); //$NON-NLS-1$

    op = addEOperation(iMessageEClass, this.getIPart(), "getPart", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1); //$NON-NLS-1$

    addEOperation(iMessageEClass, this.getIMap(), "getParts", 0, 1); //$NON-NLS-1$

    op = addEOperation(iMessageEClass, this.getIList(), "getOrderedParts", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getIList(), "partOrder", 0, 1); //$NON-NLS-1$

    initEClass(iPartEClass, javax.wsdl.Part.class, "IPart", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(iServiceEClass, javax.wsdl.Service.class, "IService", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    op = addEOperation(iServiceEClass, null, "addPort"); //$NON-NLS-1$
    addEParameter(op, this.getIPort(), "port", 0, 1); //$NON-NLS-1$

    addEOperation(iServiceEClass, this.getIMap(), "getPorts", 0, 1); //$NON-NLS-1$

    op = addEOperation(iServiceEClass, this.getIPort(), "getPort", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1); //$NON-NLS-1$

    initEClass(iPortEClass, javax.wsdl.Port.class, "IPort", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    addEOperation(iPortEClass, this.getIBinding(), "getBinding", 0, 1); //$NON-NLS-1$

    op = addEOperation(iPortEClass, null, "setBinding"); //$NON-NLS-1$
    addEParameter(op, this.getIBinding(), "binding", 0, 1); //$NON-NLS-1$

    initEClass(iBindingEClass, javax.wsdl.Binding.class, "IBinding", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    op = addEOperation(iBindingEClass, null, "addBindingOperation"); //$NON-NLS-1$
    addEParameter(op, this.getIBindingOperation(), "bindingOperation", 0, 1); //$NON-NLS-1$

    op = addEOperation(iBindingEClass, this.getIBindingOperation(), "getBindingOperation", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "inputName", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "outputName", 0, 1); //$NON-NLS-1$

    addEOperation(iBindingEClass, this.getIList(), "getBindingOperations", 0, 1); //$NON-NLS-1$

    addEOperation(iBindingEClass, this.getIPortType(), "getPortType", 0, 1); //$NON-NLS-1$

    op = addEOperation(iBindingEClass, null, "setPortType"); //$NON-NLS-1$
    addEParameter(op, this.getIPortType(), "portType", 0, 1); //$NON-NLS-1$

    initEClass(
      iBindingOperationEClass,
      javax.wsdl.BindingOperation.class,
      "IBindingOperation", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    op = addEOperation(iBindingOperationEClass, null, "addBindingFault"); //$NON-NLS-1$
    addEParameter(op, this.getIBindingFault(), "bindingFault", 0, 1); //$NON-NLS-1$

    op = addEOperation(iBindingOperationEClass, this.getIBindingFault(), "getBindingFault", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "name", 0, 1); //$NON-NLS-1$

    addEOperation(iBindingOperationEClass, this.getIMap(), "getBindingFaults", 0, 1); //$NON-NLS-1$

    addEOperation(iBindingOperationEClass, this.getIOperation(), "getOperation", 0, 1); //$NON-NLS-1$

    op = addEOperation(iBindingOperationEClass, null, "setOperation"); //$NON-NLS-1$
    addEParameter(op, this.getIOperation(), "operation", 0, 1); //$NON-NLS-1$

    addEOperation(iBindingOperationEClass, this.getIBindingInput(), "getBindingInput", 0, 1); //$NON-NLS-1$

    op = addEOperation(iBindingOperationEClass, null, "setBindingInput"); //$NON-NLS-1$
    addEParameter(op, this.getIBindingInput(), "bindingInput", 0, 1); //$NON-NLS-1$

    addEOperation(iBindingOperationEClass, this.getIBindingOutput(), "getBindingOutput", 0, 1); //$NON-NLS-1$

    op = addEOperation(iBindingOperationEClass, null, "setBindingOutput"); //$NON-NLS-1$
    addEParameter(op, this.getIBindingOutput(), "bindingOutput", 0, 1); //$NON-NLS-1$

    initEClass(iBindingInputEClass, javax.wsdl.BindingInput.class, "IBindingInput", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      iBindingOutputEClass,
      javax.wsdl.BindingOutput.class,
      "IBindingOutput", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(iBindingFaultEClass, javax.wsdl.BindingFault.class, "IBindingFault", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      iExtensibilityElementEClass,
      javax.wsdl.extensions.ExtensibilityElement.class,
      "IExtensibilityElement", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(iDefinitionEClass, javax.wsdl.Definition.class, "IDefinition", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "addBinding"); //$NON-NLS-1$
    addEParameter(op, this.getIBinding(), "binding", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "addImport"); //$NON-NLS-1$
    addEParameter(op, this.getIImport(), "importDef", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "addMessage"); //$NON-NLS-1$
    addEParameter(op, this.getIMessage(), "message", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "addNamespace"); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "prefix", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "namespaceURI", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "addPortType"); //$NON-NLS-1$
    addEParameter(op, this.getIPortType(), "portType", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "addService"); //$NON-NLS-1$
    addEParameter(op, this.getIService(), "service", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIBindingFault(), "createBindingFault", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIBindingInput(), "createBindingInput", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIBindingOutput(), "createBindingOutput", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIBindingOperation(), "createBindingOperation", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIBinding(), "createBinding", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIFault(), "createFault", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIImport(), "createImport", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIInput(), "createInput", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIMessage(), "createMessage", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIOperation(), "createOperation", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIOutput(), "createOutput", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIPart(), "createPart", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIPort(), "createPort", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIPortType(), "createPortType", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIService(), "createService", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIBinding(), "getBinding", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIMap(), "getBindings", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIMap(), "getImports", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIList(), "getImports", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "namespaceURI", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIMessage(), "getMessage", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIMap(), "getMessages", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, ecorePackage.getEString(), "getNamespace", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "prefix", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIMap(), "getNamespaces", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIPortType(), "getPortType", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIMap(), "getPortTypes", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, ecorePackage.getEString(), "getPrefix", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "namespaceURI", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIService(), "getService", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIMap(), "getServices", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getIExtensionRegistry(), "getExtensionRegistry", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "setExtensionRegistry"); //$NON-NLS-1$
    addEParameter(op, this.getIExtensionRegistry(), "extensionRegistry", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, ecorePackage.getEString(), "getDocumentBaseURI", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "setDocumentBaseURI"); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "documentBase", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getITypes(), "createTypes", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIService(), "removeService", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIBinding(), "removeBinding", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIPortType(), "removePortType", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, this.getIMessage(), "removeMessage", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    addEOperation(iDefinitionEClass, this.getITypes(), "getTypes", 0, 1); //$NON-NLS-1$

    op = addEOperation(iDefinitionEClass, null, "setTypes"); //$NON-NLS-1$
    addEParameter(op, this.getITypes(), "types", 0, 1); //$NON-NLS-1$

    initEClass(iImportEClass, javax.wsdl.Import.class, "IImport", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(iListEClass, List.class, "IList", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(iMapEClass, Map.class, "IMap", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(iurlEClass, URL.class, "IURL", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      iExtensionRegistryEClass,
      ExtensionRegistry.class,
      "IExtensionRegistry", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(typesEClass, Types.class, "Types", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    addEOperation(typesEClass, this.getIList(), "getSchemas", 0, 1); //$NON-NLS-1$

    op = addEOperation(typesEClass, this.getIList(), "getSchemas", 0, 1); //$NON-NLS-1$
    addEParameter(op, ecorePackage.getEString(), "namespaceURI", 0, 1); //$NON-NLS-1$

    initEClass(iIteratorEClass, Iterator.class, "IIterator", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(iTypesEClass, javax.wsdl.Types.class, "ITypes", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(
      unknownExtensibilityElementEClass,
      UnknownExtensibilityElement.class,
      "UnknownExtensibilityElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(
      getUnknownExtensibilityElement_Children(),
      this.getUnknownExtensibilityElement(),
      null,
      "children", null, 0, -1, UnknownExtensibilityElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(
      xsdSchemaExtensibilityElementEClass,
      XSDSchemaExtensibilityElement.class,
      "XSDSchemaExtensibilityElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getXSDSchemaExtensibilityElement_DocumentBaseURI(),
      ecorePackage.getEString(),
      "documentBaseURI", null, 0, 1, XSDSchemaExtensibilityElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getXSDSchemaExtensibilityElement_Schema(),
      theXSDPackage.getXSDSchema(),
      null,
      "schema", null, 0, 1, XSDSchemaExtensibilityElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(messageReferenceEClass, MessageReference.class, "MessageReference", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(
      getMessageReference_Name(),
      ecorePackage.getEString(),
      "name", null, 0, 1, MessageReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
    initEReference(
      getMessageReference_EMessage(),
      this.getMessage(),
      null,
      "eMessage", null, 1, 1, MessageReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

    initEClass(
      iElementExtensibleEClass,
      ElementExtensible.class,
      "IElementExtensible", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    addEOperation(iElementExtensibleEClass, this.getIList(), "getExtensibilityElements", 0, 1); //$NON-NLS-1$

    op = addEOperation(iElementExtensibleEClass, null, "addExtensibilityElement"); //$NON-NLS-1$
    addEParameter(op, this.getIExtensibilityElement(), "extElement", 0, 1); //$NON-NLS-1$

    initEClass(
      iAttributeExtensibleEClass,
      AttributeExtensible.class,
      "IAttributeExtensible", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    op = addEOperation(iAttributeExtensibleEClass, this.getIObject(), "getExtensionAttribute", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$

    op = addEOperation(iAttributeExtensibleEClass, null, "setExtensionAttribute"); //$NON-NLS-1$
    addEParameter(op, this.getQName(), "name", 0, 1); //$NON-NLS-1$
    addEParameter(op, this.getIObject(), "value", 0, 1); //$NON-NLS-1$

    addEOperation(iAttributeExtensibleEClass, this.getIMap(), "getExtensionAttributes", 0, 1); //$NON-NLS-1$

    addEOperation(iAttributeExtensibleEClass, this.getIList(), "getNativeAttributeNames", 0, 1); //$NON-NLS-1$

    initEClass(iObjectEClass, Object.class, "IObject", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    initEClass(iSchemaEClass, Schema.class, "ISchema", IS_ABSTRACT, IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    // Initialize data types
    initEDataType(qNameEDataType, QName.class, "QName", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(operationTypeEDataType, OperationType.class, "OperationType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(domElementEDataType, Element.class, "DOMElement", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(wsdlExceptionEDataType, WSDLException.class, "WSDLException", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(domDocumentEDataType, Document.class, "DOMDocument", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    // Create resource
    createResource(eNS_URI);
  }

} //WSDLPackageImpl
