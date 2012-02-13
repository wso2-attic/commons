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
package org.eclipse.wst.wsdl.internal.util;


import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.extensions.AttributeExtensible;
import javax.wsdl.extensions.ElementExtensible;
import javax.wsdl.extensions.ExtensionRegistry;

import javax.wsdl.extensions.schema.Schema;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.*;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.WSDLPackage
 * @generated
 */
public class WSDLAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static WSDLPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WSDLAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = WSDLPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected WSDLSwitch modelSwitch = new WSDLSwitch()
    {
      public Object caseWSDLElement(WSDLElement object)
      {
        return createWSDLElementAdapter();
      }

      public Object casePortType(PortType object)
      {
        return createPortTypeAdapter();
      }

      public Object caseOperation(Operation object)
      {
        return createOperationAdapter();
      }

      public Object caseMessage(Message object)
      {
        return createMessageAdapter();
      }

      public Object casePart(Part object)
      {
        return createPartAdapter();
      }

      public Object caseBinding(Binding object)
      {
        return createBindingAdapter();
      }

      public Object caseBindingOperation(BindingOperation object)
      {
        return createBindingOperationAdapter();
      }

      public Object caseService(Service object)
      {
        return createServiceAdapter();
      }

      public Object casePort(Port object)
      {
        return createPortAdapter();
      }

      public Object caseExtensibilityElement(ExtensibilityElement object)
      {
        return createExtensibilityElementAdapter();
      }

      public Object caseDefinition(Definition object)
      {
        return createDefinitionAdapter();
      }

      public Object caseImport(Import object)
      {
        return createImportAdapter();
      }

      public Object caseExtensibleElement(ExtensibleElement object)
      {
        return createExtensibleElementAdapter();
      }

      public Object caseInput(Input object)
      {
        return createInputAdapter();
      }

      public Object caseOutput(Output object)
      {
        return createOutputAdapter();
      }

      public Object caseFault(Fault object)
      {
        return createFaultAdapter();
      }

      public Object caseBindingInput(BindingInput object)
      {
        return createBindingInputAdapter();
      }

      public Object caseBindingOutput(BindingOutput object)
      {
        return createBindingOutputAdapter();
      }

      public Object caseBindingFault(BindingFault object)
      {
        return createBindingFaultAdapter();
      }

      public Object caseNamespace(Namespace object)
      {
        return createNamespaceAdapter();
      }

      public Object caseIPortType(javax.wsdl.PortType object)
      {
        return createIPortTypeAdapter();
      }

      public Object caseIOperation(javax.wsdl.Operation object)
      {
        return createIOperationAdapter();
      }

      public Object caseIInput(javax.wsdl.Input object)
      {
        return createIInputAdapter();
      }

      public Object caseIOutput(javax.wsdl.Output object)
      {
        return createIOutputAdapter();
      }

      public Object caseIFault(javax.wsdl.Fault object)
      {
        return createIFaultAdapter();
      }

      public Object caseIMessage(javax.wsdl.Message object)
      {
        return createIMessageAdapter();
      }

      public Object caseIPart(javax.wsdl.Part object)
      {
        return createIPartAdapter();
      }

      public Object caseIService(javax.wsdl.Service object)
      {
        return createIServiceAdapter();
      }

      public Object caseIPort(javax.wsdl.Port object)
      {
        return createIPortAdapter();
      }

      public Object caseIBinding(javax.wsdl.Binding object)
      {
        return createIBindingAdapter();
      }

      public Object caseIBindingOperation(javax.wsdl.BindingOperation object)
      {
        return createIBindingOperationAdapter();
      }

      public Object caseIBindingInput(javax.wsdl.BindingInput object)
      {
        return createIBindingInputAdapter();
      }

      public Object caseIBindingOutput(javax.wsdl.BindingOutput object)
      {
        return createIBindingOutputAdapter();
      }

      public Object caseIBindingFault(javax.wsdl.BindingFault object)
      {
        return createIBindingFaultAdapter();
      }

      public Object caseIExtensibilityElement(javax.wsdl.extensions.ExtensibilityElement object)
      {
        return createIExtensibilityElementAdapter();
      }

      public Object caseIDefinition(javax.wsdl.Definition object)
      {
        return createIDefinitionAdapter();
      }

      public Object caseIImport(javax.wsdl.Import object)
      {
        return createIImportAdapter();
      }

      public Object caseIList(List object)
      {
        return createIListAdapter();
      }

      public Object caseIMap(Map object)
      {
        return createIMapAdapter();
      }

      public Object caseIURL(URL object)
      {
        return createIURLAdapter();
      }

      public Object caseIExtensionRegistry(ExtensionRegistry object)
      {
        return createIExtensionRegistryAdapter();
      }

      public Object caseTypes(Types object)
      {
        return createTypesAdapter();
      }

      public Object caseIIterator(Iterator object)
      {
        return createIIteratorAdapter();
      }

      public Object caseITypes(javax.wsdl.Types object)
      {
        return createITypesAdapter();
      }

      public Object caseUnknownExtensibilityElement(UnknownExtensibilityElement object)
      {
        return createUnknownExtensibilityElementAdapter();
      }

      public Object caseXSDSchemaExtensibilityElement(XSDSchemaExtensibilityElement object)
      {
        return createXSDSchemaExtensibilityElementAdapter();
      }

      public Object caseMessageReference(MessageReference object)
      {
        return createMessageReferenceAdapter();
      }

      public Object caseIElementExtensible(ElementExtensible object)
      {
        return createIElementExtensibleAdapter();
      }

      public Object caseIAttributeExtensible(AttributeExtensible object)
      {
        return createIAttributeExtensibleAdapter();
      }

      public Object caseIObject(Object object)
      {
        return createIObjectAdapter();
      }

      public Object caseISchema(Schema object)
      {
        return createISchemaAdapter();
      }

      public Object defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  public Adapter createAdapter(Notifier target)
  {
    return (Adapter)modelSwitch.doSwitch((EObject)target);
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.WSDLElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.WSDLElement
   * @generated
   */
  public Adapter createWSDLElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.PortType <em>Port Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.PortType
   * @generated
   */
  public Adapter createPortTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Operation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Operation
   * @generated
   */
  public Adapter createOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Message <em>Message</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Message
   * @generated
   */
  public Adapter createMessageAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Part <em>Part</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Part
   * @generated
   */
  public Adapter createPartAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Binding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Binding
   * @generated
   */
  public Adapter createBindingAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.BindingOperation <em>Binding Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.BindingOperation
   * @generated
   */
  public Adapter createBindingOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Service <em>Service</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Service
   * @generated
   */
  public Adapter createServiceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Port <em>Port</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Port
   * @generated
   */
  public Adapter createPortAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.ExtensibilityElement <em>Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.ExtensibilityElement
   * @generated
   */
  public Adapter createExtensibilityElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Definition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Definition
   * @generated
   */
  public Adapter createDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Import <em>Import</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Import
   * @generated
   */
  public Adapter createImportAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.ExtensibleElement <em>Extensible Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.ExtensibleElement
   * @generated
   */
  public Adapter createExtensibleElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Input <em>Input</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Input
   * @generated
   */
  public Adapter createInputAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Output <em>Output</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Output
   * @generated
   */
  public Adapter createOutputAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Fault <em>Fault</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Fault
   * @generated
   */
  public Adapter createFaultAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.BindingInput <em>Binding Input</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.BindingInput
   * @generated
   */
  public Adapter createBindingInputAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.BindingOutput <em>Binding Output</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.BindingOutput
   * @generated
   */
  public Adapter createBindingOutputAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.BindingFault <em>Binding Fault</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.BindingFault
   * @generated
   */
  public Adapter createBindingFaultAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Namespace <em>Namespace</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Namespace
   * @generated
   */
  public Adapter createNamespaceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.PortType <em>IPort Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.PortType
   * @generated
   */
  public Adapter createIPortTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Operation <em>IOperation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Operation
   * @generated
   */
  public Adapter createIOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Input <em>IInput</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Input
   * @generated
   */
  public Adapter createIInputAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Output <em>IOutput</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Output
   * @generated
   */
  public Adapter createIOutputAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Fault <em>IFault</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Fault
   * @generated
   */
  public Adapter createIFaultAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Message <em>IMessage</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Message
   * @generated
   */
  public Adapter createIMessageAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Part <em>IPart</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Part
   * @generated
   */
  public Adapter createIPartAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Service <em>IService</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Service
   * @generated
   */
  public Adapter createIServiceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Port <em>IPort</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Port
   * @generated
   */
  public Adapter createIPortAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Binding <em>IBinding</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Binding
   * @generated
   */
  public Adapter createIBindingAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.BindingOperation <em>IBinding Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.BindingOperation
   * @generated
   */
  public Adapter createIBindingOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.BindingInput <em>IBinding Input</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.BindingInput
   * @generated
   */
  public Adapter createIBindingInputAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.BindingOutput <em>IBinding Output</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.BindingOutput
   * @generated
   */
  public Adapter createIBindingOutputAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.BindingFault <em>IBinding Fault</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.BindingFault
   * @generated
   */
  public Adapter createIBindingFaultAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.ExtensibilityElement <em>IExtensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.ExtensibilityElement
   * @generated
   */
  public Adapter createIExtensibilityElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Definition <em>IDefinition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Definition
   * @generated
   */
  public Adapter createIDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Import <em>IImport</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Import
   * @generated
   */
  public Adapter createIImportAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.util.List <em>IList</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.util.List
   * @generated
   */
  public Adapter createIListAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.util.Map <em>IMap</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.util.Map
   * @generated
   */
  public Adapter createIMapAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.net.URL <em>IURL</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.net.URL
   * @generated
   */
  public Adapter createIURLAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.ExtensionRegistry <em>IExtension Registry</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.ExtensionRegistry
   * @generated
   */
  public Adapter createIExtensionRegistryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.Types <em>Types</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.Types
   * @generated
   */
  public Adapter createTypesAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.util.Iterator <em>IIterator</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.util.Iterator
   * @generated
   */
  public Adapter createIIteratorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.Types <em>ITypes</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.Types
   * @generated
   */
  public Adapter createITypesAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.UnknownExtensibilityElement <em>Unknown Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.UnknownExtensibilityElement
   * @generated
   */
  public Adapter createUnknownExtensibilityElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement <em>XSD Schema Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement
   * @generated
   */
  public Adapter createXSDSchemaExtensibilityElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.wst.wsdl.MessageReference <em>Message Reference</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.wst.wsdl.MessageReference
   * @generated
   */
  public Adapter createMessageReferenceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.ElementExtensible <em>IElement Extensible</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.ElementExtensible
   * @generated
   */
  public Adapter createIElementExtensibleAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.AttributeExtensible <em>IAttribute Extensible</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.AttributeExtensible
   * @generated
   */
  public Adapter createIAttributeExtensibleAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.lang.Object <em>IObject</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.lang.Object
   * @generated
   */
  public Adapter createIObjectAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link javax.wsdl.extensions.schema.Schema <em>ISchema</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see javax.wsdl.extensions.schema.Schema
   * @generated
   */
  public Adapter createISchemaAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //WSDLAdapterFactory
