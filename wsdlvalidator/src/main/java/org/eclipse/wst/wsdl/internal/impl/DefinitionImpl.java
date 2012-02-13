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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Namespace;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.impl.XSDImportImpl;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getTargetNamespace <em>Target Namespace</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getEImports <em>EImports</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getETypes <em>ETypes</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getEMessages <em>EMessages</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getEPortTypes <em>EPort Types</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getEBindings <em>EBindings</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getEServices <em>EServices</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.DefinitionImpl#getENamespaces <em>ENamespaces</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DefinitionImpl extends ExtensibleElementImpl implements Definition
{
  private static final String XMLNS = "xmlns"; //$NON-NLS-1$

  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetNamespace()
   * @generated
   * @ordered
   */
  protected static final String TARGET_NAMESPACE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetNamespace()
   * @generated
   * @ordered
   */
  protected String targetNamespace = TARGET_NAMESPACE_EDEFAULT;

  /**
   * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected String location = LOCATION_EDEFAULT;

  /**
   * The default value of the '{@link #getQName() <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQName()
   * @generated
   * @ordered
   */
  protected static final QName QNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getQName() <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQName()
   * @generated
   * @ordered
   */
  protected QName qName = QNAME_EDEFAULT;

  /**
   * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEncoding()
   * @generated
   * @ordered
   */
  protected static final String ENCODING_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEncoding()
   * @generated
   * @ordered
   */
  protected String encoding = ENCODING_EDEFAULT;

  /**
   * The cached value of the '{@link #getEImports() <em>EImports</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEImports()
   * @generated
   * @ordered
   */
  protected EList eImports;

  /**
   * The cached value of the '{@link #getETypes() <em>ETypes</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getETypes()
   * @generated
   * @ordered
   */
  protected Types eTypes;

  /**
   * The cached value of the '{@link #getEMessages() <em>EMessages</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEMessages()
   * @generated
   * @ordered
   */
  protected EList eMessages;

  /**
   * The cached value of the '{@link #getEPortTypes() <em>EPort Types</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEPortTypes()
   * @generated
   * @ordered
   */
  protected EList ePortTypes;

  /**
   * The cached value of the '{@link #getEBindings() <em>EBindings</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEBindings()
   * @generated
   * @ordered
   */
  protected EList eBindings;

  /**
   * The cached value of the '{@link #getEServices() <em>EServices</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEServices()
   * @generated
   * @ordered
   */
  protected EList eServices;

  /**
   * The cached value of the '{@link #getENamespaces() <em>ENamespaces</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getENamespaces()
   * @generated
   * @ordered
   */
  protected EList eNamespaces;

  private ExtensionRegistry extensionRegistry;

  private Document document;

  private HashMap namespaces = new HashMap();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DefinitionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EClass eStaticClass()
  {
    return WSDLPackage.Literals.DEFINITION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTargetNamespace()
  {
    return targetNamespace;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTargetNamespace(String newTargetNamespace)
  {
    String oldTargetNamespace = targetNamespace;
    targetNamespace = newTargetNamespace;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.DEFINITION__TARGET_NAMESPACE, oldTargetNamespace, targetNamespace));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocation()
  {
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocation(String newLocation)
  {
    String oldLocation = location;
    location = newLocation;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.DEFINITION__LOCATION, oldLocation, location));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QName getQName()
  {
    return qName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setQName(QName newQName)
  {
    QName oldQName = qName;
    qName = newQName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.DEFINITION__QNAME, oldQName, qName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getEncoding()
  {
    return encoding;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEncoding(String newEncoding)
  {
    String oldEncoding = encoding;
    encoding = newEncoding;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.DEFINITION__ENCODING, oldEncoding, encoding));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEMessages()
  {
    if (eMessages == null)
    {
      eMessages = new EObjectContainmentEList(Message.class, this, WSDLPackage.DEFINITION__EMESSAGES);
    }
    return eMessages;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEPortTypes()
  {
    if (ePortTypes == null)
    {
      ePortTypes = new EObjectContainmentEList(PortType.class, this, WSDLPackage.DEFINITION__EPORT_TYPES);
    }
    return ePortTypes;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEBindings()
  {
    if (eBindings == null)
    {
      eBindings = new EObjectContainmentEList(Binding.class, this, WSDLPackage.DEFINITION__EBINDINGS);
    }
    return eBindings;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEServices()
  {
    if (eServices == null)
    {
      eServices = new EObjectContainmentEList(Service.class, this, WSDLPackage.DEFINITION__ESERVICES);
    }
    return eServices;
  }

  /**
   * <!-- begin-user-doc -->
   * @deprecated
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getENamespaces()
  {
    if (eNamespaces == null)
    {
      eNamespaces = new EObjectContainmentEList(Namespace.class, this, WSDLPackage.DEFINITION__ENAMESPACES);
    }
    return eNamespaces;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Types getETypes()
  {
    return eTypes;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetETypes(Types newETypes, NotificationChain msgs)
  {
    Types oldETypes = eTypes;
    eTypes = newETypes;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WSDLPackage.DEFINITION__ETYPES, oldETypes, newETypes);
      if (msgs == null)
        msgs = notification;
      else
        msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setETypes(Types newETypes)
  {
    if (newETypes != eTypes)
    {
      NotificationChain msgs = null;
      if (eTypes != null)
        msgs = ((InternalEObject)eTypes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WSDLPackage.DEFINITION__ETYPES, null, msgs);
      if (newETypes != null)
        msgs = ((InternalEObject)newETypes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WSDLPackage.DEFINITION__ETYPES, null, msgs);
      msgs = basicSetETypes(newETypes, msgs);
      if (msgs != null)
        msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.DEFINITION__ETYPES, newETypes, newETypes));
  }

  public void eNotify(Notification msg)
  {
    super.eNotify(msg);

    // cs.. if we've added a Types element, and this definition is already attached to a resource
    // we need to set the schemaLocations for any inline schemaLocations.
    // If not yet attached to a resource, the schemaLocation's will be set via WSDLResourceImpl.attached(EObject o)
    //
    if (msg.getFeature() == WSDLPackage.Literals.DEFINITION__ETYPES && msg.getEventType() == Notification.SET)
    {
      if (eResource() instanceof WSDLResourceImpl && getEnclosingDefinition() != null)
      {
        setInlineSchemaLocations(eResource());
      }
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEImports()
  {
    if (eImports == null)
    {
      eImports = new EObjectContainmentEList(Import.class, this, WSDLPackage.DEFINITION__EIMPORTS);
    }
    return eImports;
  }

  /**
   * <!-- begin-user-doc -->
   * Add a binding to this WSDL description.
   * @param binding the binding to be added
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addBinding(javax.wsdl.Binding binding)
  {
    getEBindings().add((Binding)binding);
  }

  /**
   * <!-- begin-user-doc -->
   * Add an import to this WSDL description.
   * @param importDef the import to be added
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addImport(javax.wsdl.Import importDef)
  {
    getEImports().add((Import)importDef);
  }

  /**
   * <!-- begin-user-doc -->
   * Add a message to this WSDL description.
   * @param message the message to be added
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addMessage(javax.wsdl.Message message)
  {
    getEMessages().add((Message)message);
  }

  /**
   * <!-- begin-user-doc -->
   * This is a way to add a namespace association to a definition.
   * It is similar to adding a namespace prefix declaration to the
   * top of a &lt;wsdl:definition&gt; element. This has nothing to do
   * with the &lt;wsdl:import&gt; element; there are separate methods for
   * dealing with information described by &lt;wsdl:import&gt; elements.
   * There is a default namespace association (which can be
   * overridden) between the null prefix and
   * http://schemas.xmlsoap.org/wsdl/.
   * @param prefix the prefix to use for this namespace (when
   * rendering this information as XML). Use null or an empty string
   * to describe the default namespace (i.e. xmlns="...").
   * @param namespaceURI the namespace URI to associate the prefix
   * with. If you use null, the namespace association will be removed.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addNamespace(String prefix, String namespaceURI)
  {
    if (prefix == null)
      prefix = "";

    if (namespaceURI != null)
    {
      // First, check if there is this namespace already.
      String existingPrefix = getPrefix(namespaceURI);
      if (existingPrefix != null)
        return;

      // Make sure the prefix is not already used for a different namespace
      Map namespaces = getNamespaces();
      String existingNamespace = (String)namespaces.get(prefix);
      if (existingNamespace == null)
      {
        namespaces.put(prefix, namespaceURI);

        // Support for Namespace
        Namespace ens = WSDLFactory.eINSTANCE.createNamespace();
        ens.setPrefix(prefix);
        ens.setURI(namespaceURI);
        getENamespaces().add(ens);

        return;
      }

      // The prefix is taken already. Make a unique prefix
      if (prefix.endsWith("1"))
        prefix = prefix.substring(0, prefix.length() - 1);

      for (int i = 2;; i++)
      {
        String newPrefix = prefix + i;
        if (!namespaces.containsKey(newPrefix))
        {
          namespaces.put(newPrefix, namespaceURI);

          // Support for Namespace
          Namespace ens = WSDLFactory.eINSTANCE.createNamespace();
          ens.setPrefix(prefix);
          ens.setURI(namespaceURI);
          getENamespaces().add(ens);

          return;
        }
      }
    } // end if (namespaceURI != null)
    else
    {
      getNamespaces().remove(prefix);

      // Support for Namespace
      getENamespaces().remove(prefix);
    }
  }

  /**
   * <!-- begin-user-doc -->
   * Add a portType to this WSDL description.
   * @param portType the portType to be added
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addPortType(javax.wsdl.PortType portType)
  {
    getEPortTypes().add((PortType)portType);
  }

  /**
   * <!-- begin-user-doc -->
   * Add a service to this WSDL description.
   * @param service the service to be added
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addService(javax.wsdl.Service service)
  {
    getEServices().add((Service)service);
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new binding fault.
   * @return the newly created binding fault
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public BindingFault createBindingFault()
  {
    javax.wsdl.BindingFault bindingFault = WSDLFactoryImpl.eINSTANCE.createBindingFault();
    return bindingFault;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new binding input.
   * @return the newly created binding input
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public BindingInput createBindingInput()
  {
    javax.wsdl.BindingInput bindingInput = WSDLFactoryImpl.eINSTANCE.createBindingInput();
    return bindingInput;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new binding output.
   * @return the newly created binding output
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public BindingOutput createBindingOutput()
  {
    javax.wsdl.BindingOutput bindingOutput = WSDLFactoryImpl.eINSTANCE.createBindingOutput();
    return bindingOutput;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new binding operation.
   * @return the newly created binding operation
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public BindingOperation createBindingOperation()
  {
    javax.wsdl.BindingOperation bindingOperation = WSDLFactoryImpl.eINSTANCE.createBindingOperation();
    return bindingOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new binding.
   * @return the newly created binding
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Binding createBinding()
  {
    javax.wsdl.Binding binding = WSDLFactoryImpl.eINSTANCE.createBinding();
    binding.setUndefined(true);
    return binding;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new fault.
   * @return the newly created fault
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Fault createFault()
  {
    javax.wsdl.Fault fault = WSDLFactoryImpl.eINSTANCE.createFault();
    return fault;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new import.
   * @return the newly created import
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Import createImport()
  {
    javax.wsdl.Import importDef = WSDLFactoryImpl.eINSTANCE.createImport();
    return importDef;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new input.
   * @return the newly created input
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Input createInput()
  {
    javax.wsdl.Input input = WSDLFactoryImpl.eINSTANCE.createInput();
    return input;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new message.
   * @return the newly created message
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Message createMessage()
  {
    javax.wsdl.Message message = WSDLFactoryImpl.eINSTANCE.createMessage();
    message.setUndefined(true);
    return message;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new operation.
   * @return the newly created operation
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Operation createOperation()
  {
    javax.wsdl.Operation operation = WSDLFactoryImpl.eINSTANCE.createOperation();
    operation.setUndefined(true);
    return operation;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new output.
   * @return the newly created output
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Output createOutput()
  {
    javax.wsdl.Output output = WSDLFactoryImpl.eINSTANCE.createOutput();
    return output;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new part.
   * @return the newly created part
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Part createPart()
  {
    javax.wsdl.Part part = WSDLFactoryImpl.eINSTANCE.createPart();
    return part;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new port.
   * @return the newly created port
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Port createPort()
  {
    javax.wsdl.Port port = WSDLFactoryImpl.eINSTANCE.createPort();
    return port;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new port type.
   * @return the newly created port type
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.PortType createPortType()
  {
    javax.wsdl.PortType portType = WSDLFactoryImpl.eINSTANCE.createPortType();
    portType.setUndefined(true);
    return portType;
  }

  /**
   * <!-- begin-user-doc -->
   * Create a new service.
   * @return the newly created service
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Service createService()
  {
    javax.wsdl.Service service = WSDLFactoryImpl.eINSTANCE.createService();
    return service;
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified binding. Also checks imported documents.
   * @param name the name of the desired binding.
   * @return the corresponding binding, or null if there wasn't
   * any matching binding
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Binding getBinding(QName name)
  {
    return (javax.wsdl.Binding)resolveWSDLElement(WSDLConstants.BINDING, name, this);
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the bindings defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getBindings()
  {
    return createMap(WSDLConstants.BINDING, getEBindings());
  }

  /**
   * <!-- begin-user-doc -->
   * Get a map of lists containing all the imports defined here.
   * The map's keys are the namespaceURIs, and the map's values
   * are lists. There is one list for each namespaceURI for which
   * imports have been defined.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getImports()
  {
    HashMap map = new HashMap();
    for (Iterator i = getEImports().iterator(); i.hasNext();)
    {
      Import theImport = (Import)i.next();
      String key = theImport.getNamespaceURI();
      if (key == null)
      {
        key = "";
      }

      List list = null;
      if (map.containsKey(key))
      {
        list = (List)map.get(key);
        list.add(theImport);
      }
      else
      {
        list = new ArrayList();
        list.add(theImport);
      }
      map.put(key, list);
    }
    return map;
  }

  /**
   * <!-- begin-user-doc -->
   * Get the list of imports for the specified namespaceURI.
   * @param namespaceURI the namespaceURI associated with the
   * desired imports.
   * @return a list of the corresponding imports
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getImports(String namespaceURI)
  {
    List list = new ArrayList();
    for (Iterator i = getEImports().iterator(); i.hasNext();)
    {
      Import theImport = (Import)i.next();
      if (WSDLConstants.isMatchingNamespace(namespaceURI, theImport.getNamespaceURI()))
      {
        list.add(theImport);
      }
    }
    return list;
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified message. Also checks imported documents.
   * @param name the name of the desired message.
   * @return the corresponding message, or null if there wasn't
   * any matching message
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Message getMessage(QName name)
  {
    return (javax.wsdl.Message)resolveWSDLElement(WSDLConstants.MESSAGE, name, this);
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the messages defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getMessages()
  {
    return createMap(WSDLConstants.MESSAGE, getEMessages());
  }

  /**
   * <!-- begin-user-doc -->
   * Get the namespace URI associated with this prefix. Or null if
   * there is no namespace URI associated with this prefix. This is
   * unrelated to the &lt;wsdl:import&gt; element.
   * @see #addNamespace(String, String)
   * @see #getPrefix(String)
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String getNamespace(String prefix)
  {
    return (String)getNamespaces().get(prefix);
  }

  /**
   * <!-- begin-user-doc -->
   * Get all namespace associations in this definition. The keys are
   * the prefixes, and the namespace URIs are the values. This is
   * unrelated to the &lt;wsdl:import&gt; element.
   * @see #addNamespace(String, String)
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getNamespaces()
  {
    return namespaces;
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified portType. Also checks imported documents.
   * @param name the name of the desired portType.
   * @return the corresponding portType, or null if there wasn't
   * any matching portType
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.PortType getPortType(QName name)
  {
    return (javax.wsdl.PortType)resolveWSDLElement(WSDLConstants.PORT_TYPE, name, this);
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the portTypes defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getPortTypes()
  {
    return createMap(WSDLConstants.PORT_TYPE, getEPortTypes());
  }

  /**
   * <!-- begin-user-doc -->
   * Get a prefix associated with this namespace URI. Or null if
   * there are no prefixes associated with this namespace URI. This is
   * unrelated to the &lt;wsdl:import&gt; element.
   * @see #addNamespace(String, String)
   * @see #getNamespace(String)
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String getPrefix(String namespaceURI)
  {
    if (namespaceURI == null)
      return null;

    Iterator entryIterator = getNamespaces().entrySet().iterator();
    while (entryIterator.hasNext())
    {
      Map.Entry entry = (Map.Entry)entryIterator.next();
      String prefix = (String)entry.getKey();
      String assocNamespaceURI = (String)entry.getValue();

      if (namespaceURI.equals(assocNamespaceURI) && prefix != "") // default namespace
        return prefix;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified service. Also checks imported documents.
   * @param name the name of the desired service.
   * @return the corresponding service, or null if there wasn't
   * any matching service
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Service getService(QName name)
  {
    return (javax.wsdl.Service)resolveWSDLElement(WSDLConstants.SERVICE, name, this);
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the services defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getServices()
  {
    return createMap(WSDLConstants.SERVICE, getEServices());
  }

  /**
   * <!-- begin-user-doc -->
   * Get a reference to the ExtensionRegistry for this Definition.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public ExtensionRegistry getExtensionRegistry()
  {
    return extensionRegistry;
  }

  /**
   * <!-- begin-user-doc -->
   * Set the ExtensionRegistry for this Definition.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setExtensionRegistry(ExtensionRegistry extensionRegistry)
  {
    this.extensionRegistry = extensionRegistry;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String getDocumentBaseURI()
  {
    return getLocation();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setDocumentBaseURI(String documentBase)
  {
    setLocation(documentBase);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Types createTypes()
  {
    javax.wsdl.Types types = WSDLFactoryImpl.eINSTANCE.createTypes();
    return types;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Service removeService(QName name)
  {
    WSDLElement wsdlElement = resolveWSDLElement(WSDLConstants.SERVICE, name);
    boolean removed = false;
    if (wsdlElement instanceof Service)
      removed = getEServices().remove(wsdlElement);
    if (!removed)
      wsdlElement = null;
    return (Service)wsdlElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Binding removeBinding(QName name)
  {
    WSDLElement wsdlElement = resolveWSDLElement(WSDLConstants.BINDING, name);
    boolean removed = false;
    if (wsdlElement instanceof Binding)
      removed = getEBindings().remove(wsdlElement);
    if (!removed)
      wsdlElement = null;
    return (Binding)wsdlElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.PortType removePortType(QName name)
  {
    WSDLElement wsdlElement = resolveWSDLElement(WSDLConstants.PORT_TYPE, name);
    boolean removed = false;
    if (wsdlElement instanceof PortType)
      removed = getEPortTypes().remove(wsdlElement);
    if (!removed)
      wsdlElement = null;
    return (PortType)wsdlElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Message removeMessage(QName name)
  {
    WSDLElement wsdlElement = resolveWSDLElement(WSDLConstants.MESSAGE, name);
    boolean removed = false;
    if (wsdlElement instanceof Message)
      removed = getEMessages().remove(wsdlElement);
    if (!removed)
      wsdlElement = null;
    return (Message)wsdlElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Types getTypes()
  {
    return getETypes();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setTypes(javax.wsdl.Types types)
  {
    setETypes((Types)types);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case WSDLPackage.DEFINITION__EIMPORTS:
      return ((InternalEList)getEImports()).basicRemove(otherEnd, msgs);
      case WSDLPackage.DEFINITION__ETYPES:
      return basicSetETypes(null, msgs);
      case WSDLPackage.DEFINITION__EMESSAGES:
      return ((InternalEList)getEMessages()).basicRemove(otherEnd, msgs);
      case WSDLPackage.DEFINITION__EPORT_TYPES:
      return ((InternalEList)getEPortTypes()).basicRemove(otherEnd, msgs);
      case WSDLPackage.DEFINITION__EBINDINGS:
      return ((InternalEList)getEBindings()).basicRemove(otherEnd, msgs);
      case WSDLPackage.DEFINITION__ESERVICES:
      return ((InternalEList)getEServices()).basicRemove(otherEnd, msgs);
      case WSDLPackage.DEFINITION__ENAMESPACES:
      return ((InternalEList)getENamespaces()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case WSDLPackage.DEFINITION__TARGET_NAMESPACE:
      return getTargetNamespace();
      case WSDLPackage.DEFINITION__LOCATION:
      return getLocation();
      case WSDLPackage.DEFINITION__QNAME:
      return getQName();
      case WSDLPackage.DEFINITION__ENCODING:
      return getEncoding();
      case WSDLPackage.DEFINITION__EIMPORTS:
      return getEImports();
      case WSDLPackage.DEFINITION__ETYPES:
      return getETypes();
      case WSDLPackage.DEFINITION__EMESSAGES:
      return getEMessages();
      case WSDLPackage.DEFINITION__EPORT_TYPES:
      return getEPortTypes();
      case WSDLPackage.DEFINITION__EBINDINGS:
      return getEBindings();
      case WSDLPackage.DEFINITION__ESERVICES:
      return getEServices();
      case WSDLPackage.DEFINITION__ENAMESPACES:
      return getENamespaces();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case WSDLPackage.DEFINITION__TARGET_NAMESPACE:
      setTargetNamespace((String)newValue);
      return;
      case WSDLPackage.DEFINITION__LOCATION:
      setLocation((String)newValue);
      return;
      case WSDLPackage.DEFINITION__QNAME:
      setQName((QName)newValue);
      return;
      case WSDLPackage.DEFINITION__ENCODING:
      setEncoding((String)newValue);
      return;
      case WSDLPackage.DEFINITION__EIMPORTS:
      getEImports().clear();
      getEImports().addAll((Collection)newValue);
      return;
      case WSDLPackage.DEFINITION__ETYPES:
      setETypes((Types)newValue);
      return;
      case WSDLPackage.DEFINITION__EMESSAGES:
      getEMessages().clear();
      getEMessages().addAll((Collection)newValue);
      return;
      case WSDLPackage.DEFINITION__EPORT_TYPES:
      getEPortTypes().clear();
      getEPortTypes().addAll((Collection)newValue);
      return;
      case WSDLPackage.DEFINITION__EBINDINGS:
      getEBindings().clear();
      getEBindings().addAll((Collection)newValue);
      return;
      case WSDLPackage.DEFINITION__ESERVICES:
      getEServices().clear();
      getEServices().addAll((Collection)newValue);
      return;
      case WSDLPackage.DEFINITION__ENAMESPACES:
      getENamespaces().clear();
      getENamespaces().addAll((Collection)newValue);
      return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case WSDLPackage.DEFINITION__TARGET_NAMESPACE:
      setTargetNamespace(TARGET_NAMESPACE_EDEFAULT);
      return;
      case WSDLPackage.DEFINITION__LOCATION:
      setLocation(LOCATION_EDEFAULT);
      return;
      case WSDLPackage.DEFINITION__QNAME:
      setQName(QNAME_EDEFAULT);
      return;
      case WSDLPackage.DEFINITION__ENCODING:
      setEncoding(ENCODING_EDEFAULT);
      return;
      case WSDLPackage.DEFINITION__EIMPORTS:
      getEImports().clear();
      return;
      case WSDLPackage.DEFINITION__ETYPES:
      setETypes((Types)null);
      return;
      case WSDLPackage.DEFINITION__EMESSAGES:
      getEMessages().clear();
      return;
      case WSDLPackage.DEFINITION__EPORT_TYPES:
      getEPortTypes().clear();
      return;
      case WSDLPackage.DEFINITION__EBINDINGS:
      getEBindings().clear();
      return;
      case WSDLPackage.DEFINITION__ESERVICES:
      getEServices().clear();
      return;
      case WSDLPackage.DEFINITION__ENAMESPACES:
      getENamespaces().clear();
      return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case WSDLPackage.DEFINITION__TARGET_NAMESPACE:
      return TARGET_NAMESPACE_EDEFAULT == null ? targetNamespace != null : !TARGET_NAMESPACE_EDEFAULT.equals(targetNamespace);
      case WSDLPackage.DEFINITION__LOCATION:
      return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
      case WSDLPackage.DEFINITION__QNAME:
      return QNAME_EDEFAULT == null ? qName != null : !QNAME_EDEFAULT.equals(qName);
      case WSDLPackage.DEFINITION__ENCODING:
      return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
      case WSDLPackage.DEFINITION__EIMPORTS:
      return eImports != null && !eImports.isEmpty();
      case WSDLPackage.DEFINITION__ETYPES:
      return eTypes != null;
      case WSDLPackage.DEFINITION__EMESSAGES:
      return eMessages != null && !eMessages.isEmpty();
      case WSDLPackage.DEFINITION__EPORT_TYPES:
      return ePortTypes != null && !ePortTypes.isEmpty();
      case WSDLPackage.DEFINITION__EBINDINGS:
      return eBindings != null && !eBindings.isEmpty();
      case WSDLPackage.DEFINITION__ESERVICES:
      return eServices != null && !eServices.isEmpty();
      case WSDLPackage.DEFINITION__ENAMESPACES:
      return eNamespaces != null && !eNamespaces.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Document getDocument()
  {
    return document;
  }

  /**
   * <!-- begin-user-doc -->
   * Sets the owner document.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setDocument(Document document)
  {
    this.document = document;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String toString()
  {
    if (eIsProxy())
      return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (targetNamespace: "); //$NON-NLS-1$
    result.append(targetNamespace);
    result.append(", location: "); //$NON-NLS-1$
    result.append(location);
    result.append(", qName: "); //$NON-NLS-1$
    result.append(qName);
    result.append(", encoding: "); //$NON-NLS-1$
    result.append(encoding);
    result.append(')');
    return result.toString();
  }

  //
  // Hand-coded methods
  //

  protected static ResourceSet globalResourceSet;

  public static synchronized ResourceSet getGlobalResourceSet()
  {
    if (globalResourceSet == null)
    {
      globalResourceSet = createResourceSet();
      //String baseURL = WSDLPlugin.INSTANCE.getBaseURL().toString();
      //System.out.println("Base URL is: " + baseURL);
    }

    return globalResourceSet;
  }

  public static ResourceSet createResourceSet()
  {
    ResourceSet result = new ResourceSetImpl();
    result.getResourceFactoryRegistry().getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl()); //$NON-NLS-1$
    return result;
  }

  public static Definition createDefinition(Node node)
  {
    return createDefinition(node, null);
  }

  public static Definition createDefinition(Node node, String location)
  {
    return createDefinition(node, location, true);
  }

  public static Definition createDefinition(Node node, String location, boolean useExtensionFactories)
  {
    Definition definition = WSDLFactory.eINSTANCE.createDefinition();
    ((DefinitionImpl)definition).setUseExtensionFactories(useExtensionFactories);
    definition.setElement((Element)node);
    definition.setDocumentBaseURI(location);
    return definition;
  }

  private WSDLElement resolveWSDLElement(int type, List list, QName qname)
  {
    WSDLElement result = null;
    if (qname != null)
    {
      for (Iterator i = list.iterator(); i.hasNext();)
      {
        WSDLElement wsdlElement = (WSDLElement)i.next();
        QName theQName = getQNameForWSDLElement(type, wsdlElement);
        if (qname.equals(theQName))
        {
          result = wsdlElement;
          break;
        }
      }
    }
    return result;
  }

  private HashMap createMap(int type, List list)
  {
    HashMap map = new HashMap();
    for (Iterator i = list.iterator(); i.hasNext();)
    {
      WSDLElement wsdlElement = (WSDLElement)i.next();
      QName theQName = getQNameForWSDLElement(type, wsdlElement);
      if (theQName != null)
      {
        map.put(theQName, wsdlElement);
      }
    }
    return map;
  }

  private QName getQNameForWSDLElement(int type, WSDLElement wsdlElement)
  {
    QName result = null;
    switch (type)
    {
      case WSDLConstants.MESSAGE:
      result = ((Message)wsdlElement).getQName();
        break;
      case WSDLConstants.PORT_TYPE:
      result = ((PortType)wsdlElement).getQName();
        break;
      case WSDLConstants.BINDING:
      result = ((Binding)wsdlElement).getQName();
        break;
      case WSDLConstants.SERVICE:
      result = ((Service)wsdlElement).getQName();
        break;
    }
    return result;
  }

  /**
   * Resolves the element described by type and qname in the context of the
   * given definition. The definition's imported definitions are checked as well.
   * 
   * @param type the element type.
   * @param qname the element's QName. Must not be null.
   * @param definition the context definition. Must not be null.
   * @return the resolved WSDLElement or null if one cannot be found.
   */
  private static WSDLElement resolveWSDLElement(int type, QName qname, DefinitionImpl definition)
  {
    if (qname == null || qname.getNamespaceURI() == null)
    {
      return null;
    }

    List definitions = new ArrayList();
    definitions.add(definition);
    WSDLElement result = resolveWSDLElement(type, qname, definitions, new ArrayList());

    return result;
  }

  /**
   * Resolves the element described by type and qname in the context of the
   * given definitions. The definitions imported definitions are checked as well.
   * 
   * IMPORTANT! This method is recursive. It checks the definitions and all their
   * imported definitions. It also is coded such that it avoids a cyclic import
   * should one be encountered.
   * 
   * @param type the element type.
   * @param qname the element's qName.
   * @param definitions the context definitions
   * @param visitedDefinitions a list with the definitions already visited.
   * @return the resolved WSDLElement or null if one cannot be found.
   */
  private static WSDLElement resolveWSDLElement(int type, QName qname, List definitions, List visitedDefinitions)
  {
    WSDLElement result = null;

    if (definitions.isEmpty())
    {
      return result;
    }

    // In order to preserve the old behaviour which used to check the definition
    // and all first level imports, check the entire list of definitions passed
    // in first.

    Iterator definitionsIterator = definitions.iterator();

    while (definitionsIterator.hasNext())
    {
      DefinitionImpl definition = (DefinitionImpl)definitionsIterator.next();

      result = definition.resolveWSDLElement(type, qname);

      if (result != null)
      {
        return result;
      }
    }

    // Re-iterate over the definitions and recurse into their imports.

    definitionsIterator = definitions.iterator();

    while (definitionsIterator.hasNext())
    {
      DefinitionImpl definition = (DefinitionImpl)definitionsIterator.next();

      if (visitedDefinitions.contains(definition))
      {
        // This might happen if we have a case of cyclic imports. Since we
        // already looked in it, we can and should skip it.

        continue;
      }

      visitedDefinitions.add(definition);

      List importedDefinitions = definition.getImportedDefinitions(qname.getNamespaceURI());

      result = resolveWSDLElement(type, qname, importedDefinitions, visitedDefinitions);

      if (result != null)
      {
        break;
      }
    }

    return result;
  }

  private WSDLElement resolveWSDLElement(int type, QName qname)
  {
    WSDLElement result = null;
    if (qname.getNamespaceURI() != null)
    {
      switch (type)
      {
        case WSDLConstants.MESSAGE:
        result = resolveWSDLElement(type, getEMessages(), qname);
          break;
        case WSDLConstants.PORT_TYPE:
        result = resolveWSDLElement(type, getEPortTypes(), qname);
          break;
        case WSDLConstants.BINDING:
        result = resolveWSDLElement(type, getEBindings(), qname);
          break;
        case WSDLConstants.SERVICE:
        result = resolveWSDLElement(type, getEServices(), qname);
          break;
      }
    }
    return result;
  }

  //
  // Reconciliation methods
  //

  public void setElement(Element element)
  {
    Element oldElement = getElement();
    if (oldElement instanceof EventTarget)
    {
      EventTarget oldEventTarget = ((EventTarget)oldElement);
      oldEventTarget.removeEventListener("DOMNodeInserted", getEventListener(), true); //$NON-NLS-1$
      oldEventTarget.removeEventListener("DOMNodeRemoved", getEventListener(), true); //$NON-NLS-1$
      oldEventTarget.removeEventListener("DOMAttrModified", getEventListener(), true); //$NON-NLS-1$
    }
    super.setElement(element);
    if (element instanceof EventTarget)
    {
      EventTarget eventTarget = ((EventTarget)element);
      eventTarget.addEventListener("DOMNodeInserted", getEventListener(), true); //$NON-NLS-1$
      eventTarget.addEventListener("DOMNodeRemoved", getEventListener(), true); //$NON-NLS-1$
      eventTarget.addEventListener("DOMAttrModified", getEventListener(), true); //$NON-NLS-1$
    }
    if (element != null)
    {
      document = element.getOwnerDocument();
    }
  }

  protected void reconcileAttributes(Element changedElement)
  {
    super.reconcileAttributes(changedElement);

    if (changedElement == getElement())
    {
      if (changedElement.hasAttribute(WSDLConstants.TARGETNAMESPACE_ATTRIBUTE))
        setTargetNamespace(changedElement.getAttribute(WSDLConstants.TARGETNAMESPACE_ATTRIBUTE));
      if (changedElement.hasAttribute(WSDLConstants.NAME_ATTRIBUTE))
        setQName(new QName(WSDLConstants.WSDL_NAMESPACE_URI, changedElement.getAttribute(WSDLConstants.NAME_ATTRIBUTE)));
      getENamespaces().clear();
      getNamespaces().clear();
      //getNamespaces().put("", null);

      NamedNodeMap map = changedElement.getAttributes();
      int mapLength = map.getLength();
      for (int i = 0; i < mapLength; i++)
      {
        Attr attr = (Attr)map.item(i);
        String nsPrefix = null;
        if (XMLNS.equals(attr.getPrefix()))
        {
          nsPrefix = attr.getLocalName();
        }
        else if (XMLNS.equals(attr.getNodeName()))
        {
          nsPrefix = "";
        }

        if (nsPrefix != null)
        {
          getNamespaces().put(nsPrefix, attr.getValue());
        }
      }
    }
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    switch (WSDLUtil.getInstance().getWSDLType(child))
    {
      case WSDLConstants.BINDING:
      {
        Binding binding = WSDLFactory.eINSTANCE.createBinding();
        binding.setEnclosingDefinition(this);
        binding.setElement(child);
        addBinding(binding);
        break;
      }
      case WSDLConstants.DOCUMENTATION:
      {
        // CS: we need to figure out how to unset this element when its removed
        //definition.setDocumentationElement(child);  
        break;
      }
      case WSDLConstants.IMPORT:
      {
        Import i = WSDLFactory.eINSTANCE.createImport();
        i.setEnclosingDefinition(this);
        i.setElement(child);
        addImport(i);
        break;
      }
      case WSDLConstants.MESSAGE:
      {
        Message message = WSDLFactory.eINSTANCE.createMessage();
        message.setEnclosingDefinition(this);
        message.setElement(child);
        addMessage(message);
        break;
      }
      case WSDLConstants.PORT_TYPE:
      {
        PortType portType = WSDLFactory.eINSTANCE.createPortType();
        portType.setEnclosingDefinition(this);
        portType.setElement(child);
        addPortType(portType);
        break;
      }
      case WSDLConstants.SERVICE:
      {
        Service service = WSDLFactoryImpl.eINSTANCE.createService();
        service.setEnclosingDefinition(this);
        service.setElement(child);
        addService(service);
        break;
      }
      case WSDLConstants.TYPES:
      {
        if (getETypes() == null)
        {
          Types types = WSDLFactoryImpl.eINSTANCE.createTypes();
          types.setEnclosingDefinition(this);
          types.setElement(child);
          setETypes(types);
        }
        break;
      }
      default:
      {
        ExtensibilityElement extensibilityElement = getUseExtensionFactories()
          ? ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createExtensibilityElement(getNamespace(child), getLocalName(child))
          : ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createUnknownExtensibilityElement();

        extensibilityElement.setEnclosingDefinition(this);
        extensibilityElement.setElement(child);
        getEExtensibilityElements().add(extensibilityElement);
        break;
      }
    }
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
    for (Iterator i = remainingModelObjects.iterator(); i.hasNext();)
    {
      remove(this, i.next());
    }
    reconcileReferences(true);
  }

  protected void remove(Object component, Object modelObject)
  {
    if (modelObject instanceof Types)
    {
      Definition definition = (Definition)component;
      if (definition.getETypes() == modelObject)
      {
        definition.setETypes(null);
      }
    }
    else
    {
      List list = getList(component, modelObject);
      if (list != null)
      {
        list.remove(modelObject);
      }
    }
  }

  private List getList(Object component, Object modelObject)
  {
    List result = null;
    Definition definition = (Definition)component;

    // todo... use WSDLSwitch
    //
    if (modelObject instanceof Binding)
    {
      result = definition.getEBindings();
    }
    else if (modelObject instanceof Import)
    {
      result = definition.getEImports();
    }
    else if (modelObject instanceof Message)
    {
      result = definition.getEMessages();
    }
    else if (modelObject instanceof PortType)
    {
      result = definition.getEPortTypes();
    }
    else if (modelObject instanceof Service)
    {
      result = definition.getEServices();
    }
    else if (modelObject instanceof ExtensibilityElement)
    {
      result = definition.getEExtensibilityElements();
    }
    return result;
  }

  public Collection getModelObjects(Object component)
  {
    List list = new ArrayList();
    Definition definition = (Definition)component;
    list.addAll(definition.getEImports());
    list.add(definition.getETypes());
    list.addAll(definition.getEMessages());
    list.addAll(definition.getEPortTypes());
    list.addAll(definition.getEBindings());
    list.addAll(definition.getEServices());
    list.addAll(definition.getEExtensibilityElements());
    return list;
  }

  //
  // For reconciliation: Model -> DOM
  //

  public Document updateDocument()
  {
    document = createDocument();
    return document;
  }

  private Document createDocument()
  {
    try
    {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(true);
      documentBuilderFactory.setValidating(false);
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      return documentBuilder.newDocument();
    }
    catch (ParserConfigurationException exception)
    {
      WSDLPlugin.INSTANCE.log(exception);
      return null;
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.DEFINITION);
    setElement(newElement);
    addChildElements(newElement, getEExtensibilityElements());

    addChildElements(newElement, getEImports());

    Types types = getETypes();
    if (types != null)
    {
      Element child = ((TypesImpl)types).createElement();
      newElement.appendChild(child);
    }

    addChildElements(newElement, getEMessages());
    addChildElements(newElement, getEPortTypes());
    addChildElements(newElement, getEBindings());
    addChildElements(newElement, getEServices());

    return newElement;
  }

  protected void addChildElements(Element parent, List c)
  {
    for (Iterator iterator = c.iterator(); iterator.hasNext();)
    {
      WSDLElementImpl wsdlElement = (WSDLElementImpl)iterator.next();
      Element child = wsdlElement.createElement();
      parent.appendChild(child);
    }
  }

  protected void changeReference(EReference eReference)
  {
    // Add namespace prefix info
    // TBD - Need to revisit.
    Element theElement = getElement();
    if (eReference == null && theElement != null)
    // We are updating the Definition element.
    {
      Iterator iterator = getNamespaces().entrySet().iterator();
      String prefix = null;
      String uri = null;
      while (iterator.hasNext())
      {
        Map.Entry entry = (Map.Entry)iterator.next();
        prefix = (String)entry.getKey();
        uri = (String)entry.getValue();
        if (prefix != "")
          theElement.setAttributeNS(XSDConstants.XMLNS_URI_2000, "xmlns:" + prefix, uri); //$NON-NLS-1$
        else if (uri != null)
          // Handle default namespace, e.g. xmlns="http://schemas.xmlsoap.org/wsdl/"
          theElement.setAttributeNS(XSDConstants.XMLNS_URI_2000, XMLNS, uri);
      }
    }
  }

  protected void changeAttribute(EAttribute eAttribute)
  {
    if (isReconciling)
      return;

    super.changeAttribute(eAttribute);
    Element theElement = getElement();
    if (theElement != null)
    {
      //if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getDefinition_Encoding())
      //  niceSetAttribute(theElement, WSDLConstants.ENCODING_ATTRIBUTE, getEncoding());
      if (eAttribute == null || eAttribute == WSDLPackage.Literals.DEFINITION__QNAME)
        if (getQName() != null)
          niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getQName().getLocalPart());
      if (eAttribute == null || eAttribute == WSDLPackage.Literals.DEFINITION__TARGET_NAMESPACE)
      {
        if (getTargetNamespace() != null)
        {
          niceSetAttribute(theElement, WSDLConstants.TARGETNAMESPACE_ATTRIBUTE, getTargetNamespace());
          if (eAttribute != null && !isReconciling)
          {
            patchTargetNamespace();
          }
        }
      }
    }
  }

  private List getImportedDefinitions(String namespace)
  {
    List list = new ArrayList();

    for (Iterator i = getImports(namespace).iterator(); i.hasNext();)
    {
      Import theImport = (Import)i.next();
      ((ImportImpl)theImport).importDefinitionOrSchema();
      Definition importedDefinition = theImport.getEDefinition();
      if (importedDefinition != null && WSDLConstants.isMatchingNamespace(namespace, importedDefinition.getTargetNamespace()))
      {
        list.add(importedDefinition);
      }
    }

    return list;
  }

  public XSDElementDeclaration resolveElementDeclarationURI(String uri)
  {
    XSDElementDeclaration result = null;
    int index = uri.lastIndexOf("#");
    if (index != -1)
    {
      result = resolveElementDeclaration(uri.substring(0, index), uri.substring(index + 1));
    }
    return result;
  }

  private boolean isComponentDefined(XSDConcreteComponent component)
  {
    // note the getContainer() test to eliminate 'synthetic' types
    // that are created by the XMLSchema model when resolution fails   
    return component != null && component.getContainer() != null;
  }

  public XSDElementDeclaration resolveElementDeclaration(String namespace, String localName)
  {
    XSDElementDeclaration result = null;
    for (Iterator i = resolveSchema(namespace).iterator(); i.hasNext();)
    {
      XSDSchema schema = (XSDSchema)i.next();
      result = schema.resolveElementDeclaration(namespace, localName);
      if (isComponentDefined(result))
        return result;
    }

    // Could not resolve. Try against all <import>ed and inlined schemas.
    for (Iterator i = getImportedOrInlinedSchemas().iterator(); i.hasNext();)
    {
      XSDSchema schema = (XSDSchema)i.next();
      result = schema.resolveElementDeclaration(namespace, localName);
      if (isComponentDefined(result))
        return result;
    }

    return result;
  }

  public XSDTypeDefinition resolveTypeDefinitionURI(String uri)
  {
    XSDTypeDefinition result = null;
    int index = uri.lastIndexOf("#");
    if (index != -1)
    {
      result = resolveTypeDefinition(uri.substring(0, index), uri.substring(index + 1));
    }
    return result;
  }

  public XSDTypeDefinition resolveTypeDefinition(String namespace, String localName)
  {
    XSDTypeDefinition result = null;
    for (Iterator i = resolveSchema(namespace).iterator(); i.hasNext();)
    {
      XSDSchema schema = (XSDSchema)i.next();
      result = schema.resolveTypeDefinition(namespace, localName);
      if (isComponentDefined(result))
        return result;
    }

    // Could not resolve. Try against all <import>ed and inlined schemas.
    for (Iterator i = getImportedOrInlinedSchemas().iterator(); i.hasNext();)
    {
      XSDSchema schema = (XSDSchema)i.next();
      result = schema.resolveTypeDefinition(namespace, localName);
      if (isComponentDefined(result))
        return result;
    }

    return result; // Failed to resolve.
  }

  /**
   * This returns set of schemas with the given namespace as it's target namespace.
   */
  public Collection resolveSchema(String namespace)
  {
    if ("".equals(namespace))
    {
      namespace = null;
    }

    if (XSDConstants.isSchemaForSchemaNamespace(namespace))
    {
      return Collections.singleton(XSDSchemaImpl.getSchemaForSchema(namespace));
    }
    else if (XSDConstants.isSchemaInstanceNamespace(namespace))
    {
      return Collections.singleton(XSDSchemaImpl.getSchemaInstance(namespace));
    }
    else
    {
      return getImportedOrInlinedSchemas(namespace);
    }
  }

  protected List getImportedOrInlinedSchemas(String namespace)
  {
    if (namespace == null)
    {
      namespace = "";
    }

    List list = new ArrayList();
    collectImportedSchemas(namespace, list);
    collectInlinedSchemas(namespace, list);
    collectInlinedSchemasOfImportedDefinitions(namespace, list);
    return list;
  }

  private List getImportedOrInlinedSchemas()
  {
    List list = new ArrayList();
    for (Iterator i = getEImports().iterator(); i.hasNext();)
    {
      Import theImport = (Import)i.next();
      ((ImportImpl)theImport).importDefinitionOrSchema();
      XSDSchema schema = theImport.getESchema();
      if (schema != null)
        list.add(schema);
    }

    if (getETypes() != null)
    {
      list.addAll(getETypes().getSchemas());
    }
    return list;
  }

  protected EventListener eventListener;

  protected Node deletionNode;

  public Node getDeletionNode()
  {
    return deletionNode;
  }

  protected EventListener getEventListener()
  {
    if (eventListener == null)
    {
      eventListener = new EventListener()
        {
          public void handleEvent(Event event)
          {
            if (event instanceof MutationEvent)
            {
              MutationEvent mutationEvent = (MutationEvent)event;
              if (mutationEvent.getTarget() instanceof Node)
              {
                Node node = (Node)mutationEvent.getTarget();
                while (node.getNodeType() != Node.ELEMENT_NODE)
                {
                  node = node.getParentNode();
                }
                if (mutationEvent.getAttrChange() == 0)
                {
                  WSDLElementImpl listener = (WSDLElementImpl)getCorrespondingComponent(node.getParentNode());
                  if (listener != null)
                  {
                    if (event.getType().equals("DOMNodeRemoved")) //$NON-NLS-1$
                    {
                      deletionNode = (Node)event.getTarget();
                    }
                    listener.elementContentsChanged((Element)node.getParentNode());
                    deletionNode = null;
                  }
                }
                else
                {
                  WSDLElementImpl listener = (WSDLElementImpl)getCorrespondingComponent(node);
                  if (listener != null)
                  {
                    listener.elementAttributesChanged((Element)node);
                  }
                }
              }
            }
          }
        };
    }
    return eventListener;
  }

  public WSDLElement getCorrespondingComponent(Node node)
  {
    // We consider all parents so that they can handle other contained nodes that otherwise don't correspond to a component.
    //
    List parents = new ArrayList();

    if (node.getNodeType() == Node.ATTRIBUTE_NODE)
    {
      node = ((Attr)node).getOwnerElement();
    }
    else
    {
      // Skip ahead to an element.
      //
      for (Node scanNode = node; scanNode != null; scanNode = scanNode.getNextSibling())
      {
        if (scanNode.getNodeType() == Node.ELEMENT_NODE)
        {
          node = scanNode;
          break;
        }
      }

      // Skip back to an element.
      //
      for (Node scanNode = node; scanNode != null; scanNode = scanNode.getPreviousSibling())
      {
        if (scanNode.getNodeType() == Node.ELEMENT_NODE)
        {
          node = scanNode;
          break;
        }
      }
    }

    // Navigate out through the elements.
    //
    for (Node parent = node; parent != null; parent = parent.getParentNode())
    {
      if (parent.getNodeType() == Node.ELEMENT_NODE)
      {
        parents.add(parent);
      }
    }

    WSDLElement bestWSDLElement = getBestWSDLElement(parents);
    return bestWSDLElement;
  }

  //
  //
  //
  private boolean useExtensionFactories = true;

  public void setUseExtensionFactories(boolean value)
  {
    useExtensionFactories = value;
  }

  public boolean getUseExtensionFactories()
  {
    return useExtensionFactories;
  }

  // See Bug 5366
  public void removeAll()
  {
    try
    {
      isReconciling = true;
      document = null;
      element = null;
      getEServices().clear();
      getEBindings().clear();
      getEPortTypes().clear();
      getEMessages().clear();
      setETypes(null);
      getEImports().clear();
      getEExtensibilityElements().clear();
    }
    catch (Exception e)
    {

    }
    finally
    {
      isReconciling = false;
    }
  }

  public void setInlineSchemaLocations(Resource resource)
  {
    // Initialize the inline schemas location 
    Types types = this.getETypes();
    if (types != null)
    {
      Iterator iterator = types.getEExtensibilityElements().iterator();
      while (iterator.hasNext())
      {
        ExtensibilityElement extensibilityElement = (ExtensibilityElement)iterator.next();
        if (!(extensibilityElement instanceof XSDSchemaExtensibilityElement))
        {
          continue;
        }
        XSDSchemaExtensibilityElement el = (XSDSchemaExtensibilityElement)extensibilityElement;
        XSDSchema schema = el.getSchema();
        if (schema != null)
        {
          // We need this try-catch block in case we encounter an exception while attempting
          // to resolve the schema.  In the case of the WSDL Editor, we get a
          // 'cannot create part exception'......See eclipse bugzilla bug 89855
          try
          {
            schema.setSchemaLocation(resource.getURI().toString());
          }
          catch (Exception e)
          {

          }
        }
      }
    }
  }

  /** 
   *  This method will collect the schemas matching the namespace
   *  that are inlined directly within this definition.
   *  
   *  @param namespace the namespace to match.
   *  @param list all inline schemas that match the namespace will be added to this list.
   */
  protected void collectInlinedSchemas(String namespace, List list)
  {
    Types types = getETypes();
    if (types != null)
    {
      List schemas = types.getSchemas();
      Iterator schemasIterator = schemas.iterator();
      while (schemasIterator.hasNext())
      {
        XSDSchema schema = (XSDSchema)schemasIterator.next();
        String targetNamespace = schema.getTargetNamespace();

        if (WSDLConstants.isMatchingNamespace(namespace, targetNamespace))
        {
          list.add(schema);
        }

        EList schemaContents = schema.getContents();
        Iterator schemaContentsIterator = schemaContents.iterator();

        while (schemaContentsIterator.hasNext())
        {
          Object component = schemaContentsIterator.next();
          if (component instanceof XSDImport)
          {
            XSDImport theImport = (XSDImport)component;
            String importNamespace = theImport.getNamespace();
            if (WSDLConstants.isMatchingNamespace(namespace, importNamespace))
            {
              ((XSDImportImpl)theImport).importSchema();
              XSDSchema importedSchema = theImport.getResolvedSchema();
              if (importedSchema != null)
              {
                list.add(importedSchema);
              }
            }
          }
        }
      }
    }
  }

  /** 
   *  This method will collect any schema matching the given namespace that are
   *  imported directly via a wsdl import (e.g. &lt;wsdl:import schemaLocation="Foo.xsd"...)
   *  
   *  @param namespace the namespace to match
   *  @param list all imported schemas that match the namespace will be added to this list.
   */
  protected void collectImportedSchemas(String namespace, List list)
  {
    EList imports = getEImports();
    Iterator importsIterator = imports.iterator();
    while (importsIterator.hasNext())
    {
      Import theImport = (Import)importsIterator.next();
      String importNamespaceURI = theImport.getNamespaceURI();

      if (WSDLConstants.isMatchingNamespace(importNamespaceURI, namespace))
      {
        ((ImportImpl)theImport).importDefinitionOrSchema();
        XSDSchema schema = theImport.getESchema();

        if (schema != null && WSDLConstants.isMatchingNamespace(schema.getTargetNamespace(), namespace))
        {
          list.add(schema);
        }
      }
    }
  }

  /** 
   *  This method will visit any imported wsdl's and collect any contained inlined schemas that match
   *  the given namespace. 
   *  
   *  @param namespace the namespace to match
   *  @param list all inlined schemas of imported definitions that match the namespace will be added to this list.
   */
  protected void collectInlinedSchemasOfImportedDefinitions(String namespace, List list)
  {
    EList imports = getEImports();
    Iterator importsIterator = imports.iterator();
    while (importsIterator.hasNext())
    {
      Import theImport = (Import)importsIterator.next();
      DefinitionImpl importedDefinition = (DefinitionImpl)theImport.getEDefinition();
      if (importedDefinition != null)
      {
        importedDefinition.collectInlinedSchemas(namespace, list);
      }
    }
  }

  /**
   * Propagates the change in the target namespace to all the top level named components.
   */
  private void patchTargetNamespace()
  {
    EList messages = getEMessages();
    patchTargetNamespace(messages, WSDLConstants.MESSAGE);

    EList portTypes = getEPortTypes();
    patchTargetNamespace(portTypes, WSDLConstants.PORT_TYPE);

    EList bindings = getEBindings();
    patchTargetNamespace(bindings, WSDLConstants.BINDING);

    EList services = getEServices();
    patchTargetNamespace(services, WSDLConstants.SERVICE);
  }

  /**
   * Iterates over the WSDL components in the list and updates their QName. Uses
   * the component type to determine what type of object is in the list.
   * 
   * @param componentsList a list with WSDLElements: messages, port types, bindings or port types.
   * @param componentType the associated component type as defined in {@link WSDLConstants}
   */
  private void patchTargetNamespace(EList componentsList, int componentType)
  {
    Iterator componentsIterator = componentsList.iterator();
    while (componentsIterator.hasNext())
    {
      WSDLElement wsdlElement = (WSDLElement)componentsIterator.next();
      Definition enclosingDefinition = wsdlElement.getEnclosingDefinition();
      if (enclosingDefinition != null)
      {
        String targetNamespace = enclosingDefinition.getTargetNamespace();

        QName componentQName = getQNameForWSDLElement(componentType, wsdlElement);

        if (componentQName != null)
        {
          String localPart = componentQName.getLocalPart();
          QName newComponentQName = new QName(targetNamespace, localPart);
          setQNameForWSDLElement(componentType, wsdlElement, newComponentQName);
        }
      }
    }
  }

  /**
   * Updates the QName for the given component and component type.
   * @param type the component type. See {@link WSDLConstants}.
   * @param wsdlElement the target WSDL element.
   * @param qName the new component {@link QName}.
   */
  private void setQNameForWSDLElement(int type, WSDLElement wsdlElement, QName qName)
  {
    switch (type)
    {
      case WSDLConstants.MESSAGE:
      ((Message)wsdlElement).setQName(qName);
        break;
      case WSDLConstants.PORT_TYPE:
      ((PortType)wsdlElement).setQName(qName);
        break;
      case WSDLConstants.BINDING:
      ((Binding)wsdlElement).setQName(qName);
        break;
      case WSDLConstants.SERVICE:
      ((Service)wsdlElement).setQName(qName);
        break;
    }
  }
} //DefinitionImpl
