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


import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLModelLocator;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.ImportImpl#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.ImportImpl#getLocationURI <em>Location URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.ImportImpl#getEDefinition <em>EDefinition</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.ImportImpl#getESchema <em>ESchema</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ImportImpl extends ExtensibleElementImpl implements Import
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  protected boolean resolved;

  /**
   * The default value of the '{@link #getNamespaceURI() <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamespaceURI()
   * @generated
   * @ordered
   */
  protected static final String NAMESPACE_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getNamespaceURI() <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamespaceURI()
   * @generated
   * @ordered
   */
  protected String namespaceURI = NAMESPACE_URI_EDEFAULT;

  /**
   * The default value of the '{@link #getLocationURI() <em>Location URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocationURI()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLocationURI() <em>Location URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocationURI()
   * @generated
   * @ordered
   */
  protected String locationURI = LOCATION_URI_EDEFAULT;

  /**
   * The cached value of the '{@link #getEDefinition() <em>EDefinition</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEDefinition()
   * @generated
   * @ordered
   */
  protected Definition eDefinition;

  /**
   * The cached value of the '{@link #getESchema() <em>ESchema</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getESchema()
   * @generated
   * @ordered
   */
  protected XSDSchema eSchema;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ImportImpl()
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
    return WSDLPackage.Literals.IMPORT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getNamespaceURI()
  {
    return namespaceURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNamespaceURI(String newNamespaceURI)
  {
    String oldNamespaceURI = namespaceURI;
    namespaceURI = newNamespaceURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.IMPORT__NAMESPACE_URI, oldNamespaceURI, namespaceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocationURI()
  {
    return locationURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocationURI(String newLocationURI)
  {
    String oldLocationURI = locationURI;
    locationURI = newLocationURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.IMPORT__LOCATION_URI, oldLocationURI, locationURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition getEDefinition()
  {
    if (eDefinition != null && eDefinition.eIsProxy())
    {
      InternalEObject oldEDefinition = (InternalEObject)eDefinition;
      eDefinition = (Definition)eResolveProxy(oldEDefinition);
      if (eDefinition != oldEDefinition)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.IMPORT__EDEFINITION, oldEDefinition, eDefinition));
      }
    }
    return eDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition basicGetEDefinition()
  {
    return eDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEDefinition(Definition newEDefinition)
  {
    Definition oldEDefinition = eDefinition;
    eDefinition = newEDefinition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.IMPORT__EDEFINITION, oldEDefinition, eDefinition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XSDSchema getESchema()
  {
    if (eSchema != null && eSchema.eIsProxy())
    {
      InternalEObject oldESchema = (InternalEObject)eSchema;
      eSchema = (XSDSchema)eResolveProxy(oldESchema);
      if (eSchema != oldESchema)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.IMPORT__ESCHEMA, oldESchema, eSchema));
      }
    }
    return eSchema;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XSDSchema basicGetESchema()
  {
    return eSchema;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setESchema(XSDSchema newESchema)
  {
    XSDSchema oldESchema = eSchema;
    eSchema = newESchema;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.IMPORT__ESCHEMA, oldESchema, eSchema));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public XSDSchema getSchema()
  {
    return getESchema();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setSchema(XSDSchema schema)
  {
    setESchema(schema);
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
      case WSDLPackage.IMPORT__NAMESPACE_URI:
      return getNamespaceURI();
      case WSDLPackage.IMPORT__LOCATION_URI:
      return getLocationURI();
      case WSDLPackage.IMPORT__EDEFINITION:
      if (resolve)
        return getEDefinition();
      return basicGetEDefinition();
      case WSDLPackage.IMPORT__ESCHEMA:
      if (resolve)
        return getESchema();
      return basicGetESchema();
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
      case WSDLPackage.IMPORT__NAMESPACE_URI:
      setNamespaceURI((String)newValue);
      return;
      case WSDLPackage.IMPORT__LOCATION_URI:
      setLocationURI((String)newValue);
      return;
      case WSDLPackage.IMPORT__EDEFINITION:
      setEDefinition((Definition)newValue);
      return;
      case WSDLPackage.IMPORT__ESCHEMA:
      setESchema((XSDSchema)newValue);
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
      case WSDLPackage.IMPORT__NAMESPACE_URI:
      setNamespaceURI(NAMESPACE_URI_EDEFAULT);
      return;
      case WSDLPackage.IMPORT__LOCATION_URI:
      setLocationURI(LOCATION_URI_EDEFAULT);
      return;
      case WSDLPackage.IMPORT__EDEFINITION:
      setEDefinition((Definition)null);
      return;
      case WSDLPackage.IMPORT__ESCHEMA:
      setESchema((XSDSchema)null);
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
      case WSDLPackage.IMPORT__NAMESPACE_URI:
      return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case WSDLPackage.IMPORT__LOCATION_URI:
      return LOCATION_URI_EDEFAULT == null ? locationURI != null : !LOCATION_URI_EDEFAULT.equals(locationURI);
      case WSDLPackage.IMPORT__EDEFINITION:
      return eDefinition != null;
      case WSDLPackage.IMPORT__ESCHEMA:
      return eSchema != null;
    }
    return super.eIsSet(featureID);
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
    result.append(" (namespaceURI: "); //$NON-NLS-1$
    result.append(namespaceURI);
    result.append(", locationURI: "); //$NON-NLS-1$
    result.append(locationURI);
    result.append(')');
    return result.toString();
  }

  /**
   * @see Import#getDefinition()
   */
  public javax.wsdl.Definition getDefinition()
  {
    return getEDefinition();
  }

  /**
   * @see Import#setDefinition(Definition)
   */
  public void setDefinition(javax.wsdl.Definition definition)
  {
    setEDefinition((org.eclipse.wst.wsdl.Definition)definition);
  }

  //
  // Reconcile methods
  //
  public void reconcileAttributes(Element changedElement)
  {
    setNamespaceURI(WSDLConstants.getAttribute(changedElement, WSDLConstants.NAMESPACE_ATTRIBUTE));
    setLocationURI(WSDLConstants.getAttribute(changedElement, WSDLConstants.LOCATION_ATTRIBUTE));
  }

  //
  // For reconciliation: Model -> DOM
  //
  protected void changeAttribute(EAttribute eAttribute)
  {
    // We need to set this boolean to false because the Import may point to a different location.
    // So we need we should view this import as unresolved.
    resolved = false;

    if (isReconciling)
      return;

    super.changeAttribute(eAttribute);
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eAttribute == null || eAttribute == WSDLPackage.Literals.IMPORT__NAMESPACE_URI)
        niceSetAttribute(theElement, WSDLConstants.NAMESPACE_ATTRIBUTE, getNamespaceURI());

      if (eAttribute == null || eAttribute == WSDLPackage.Literals.IMPORT__LOCATION_URI)
        niceSetAttribute(theElement, WSDLConstants.LOCATION_ATTRIBUTE, getLocationURI());
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.IMPORT);
    setElement(newElement);
    return newElement;
  }

  public void importDefinitionOrSchema()
  {
    resolve(getNamespaceURI(), getLocationURI());
  }

  protected void resolve(String namespace, String location)
  {
    if (!resolved)
    {
      // KB: bugzilla 118293
      // If a user changes <wsdl:import> from a WSDL file to an XML Schema file
      // or the other way around, we should set the variable for the first
      // <wsdl:import> source to null. Here I am simply setting both variables to null,
      // which will have the same effect.
      eDefinition = null;
      eSchema = null;

      Definition definition = getEnclosingDefinition();
      if (definition != null && definition.getDocumentBaseURI() != null)
      {
        Resource resource = definition.eResource();
        if (resource != null)
        {
          ResourceSet resourceSet = resource.getResourceSet();
          if (resourceSet != null)
          {
            if (namespace == null)
            {
              namespace = definition.getTargetNamespace();
            }

            String resolvedLocation = resolveLocation(definition, namespace, location);

            if (resolvedLocation == null)
            {
              return;
            }
            
            URI uri = URI.createURI(resolvedLocation);
            Resource resolvedResource = resourceSet.getResource(uri, false);
            if (resolvedResource == null)
            {
              try
              {
                InputStream inputStream = resourceSet.getURIConverter().createInputStream(uri);
                resolvedResource = getResource(uri, resourceSet);
                resolvedResource.load(inputStream, resourceSet.getLoadOptions());
              }
              catch (IOException exception)
              {
                // It is generally not an error to fail to resolve.
                // If a resource is actually created, 
                // which happens only when we can create an input stream,
                // then it's an error if it's not a good wsdl or schema
              }
            }

            if (resolvedResource != null)
            {
              if (resolvedResource instanceof WSDLResourceImpl)
              {
                eDefinition = ((WSDLResourceImpl)resolvedResource).getDefinition();
              }
              else if (resolvedResource instanceof XSDResourceImpl)
              {
                eSchema = ((XSDResourceImpl)resolvedResource).getSchema();
              }
              else
              {
                eDefinition = WSDLFactory.eINSTANCE.createDefinition();
              }
            }
            resolved = true;
          }
        }
      }
    }
  }

  protected String resolveLocation(Definition definition, String namespace, String schemaLocation)
  {
    String result = null;
    WSDLModelLocator locator = (WSDLModelLocator)EcoreUtil.getRegisteredAdapter(definition.eResource(), WSDLModelLocator.class);
    if (locator != null)
    {
      result = locator.resolveURI(definition.getDocumentBaseURI(), namespace, schemaLocation);
    }
    else
    {
      // TODO... there's some default resolving we'll need to do here
      // see XSDSchemaDirective
      URI baseLocationURI = createURI(definition.getDocumentBaseURI());
      URI locationURI = URI.createURI(schemaLocation);
      return locationURI.resolve(baseLocationURI).toString();
    }
    return result;
  }

  //TODO... push down to EMF
  private static URI createURI(String uriString)
  {
    if (hasProtocol(uriString))
      return URI.createURI(uriString);
    else
      return URI.createFileURI(uriString);
  }

  //TODO... push down to EMF  
  private static boolean hasProtocol(String uri)
  {
    boolean result = false;
    if (uri != null)
    {
      int index = uri.indexOf(":");
      if (index != -1 && index > 2) // assume protocol with be length 3 so that the'C' in 'C:/' is not interpreted as a protocol
      {
        result = true;
      }
    }
    return result;
  }
  
  private static String getRootElementName(String uri)
  {
    RootElementNameContentHandler handler = new RootElementNameContentHandler();
    try
    {
      XMLReader reader = XMLReaderFactory.createXMLReader();
      reader.setContentHandler(handler);
      reader.parse(uri);
    }
    catch (Exception e)
    {
      // Explicitly ignore any exceptions.
    }

    return handler.rootElementName;
  }

  private static class RootElementNameContentHandler extends DefaultHandler
  {
    public String rootElementName;

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      rootElementName = localName;

      // Throw an exception to make the SAX Parser to stop parsing
      throw new SAXException("SAXParser intentionally stopped"); //$NON-NLS-1$
    }
  }
  
  private static final URI XSD_URI = URI.createURI("*.xsd"); //$NON-NLS-1$
  private static final URI WSDL_URI = URI.createURI("*.wsdl"); //$NON-NLS-1$
  
  private static Resource getResource(URI uri, ResourceSet resourceSet)
  {
    // Allow the resource set the first chance to create the resource based on
    // the registered resource factories.
    
    Resource resource = resourceSet.createResource(uri);
    
    boolean isWsdlOrXsdResource = resource instanceof WSDLResourceImpl || resource instanceof XSDResourceImpl;
    
    if (!isWsdlOrXsdResource)
    {
      // The URI was not mapped to a WSDL or XSD resource.
      // Try to peek inside the resource to determine if its an XSD or WSDL document. 
      
      String rootElementName = getRootElementName(uri.toString());
      
      if (rootElementName != null)
      {
        URI resourceURI = null;

        if (XSDConstants.SCHEMA_ELEMENT_TAG.equals(rootElementName))
        {
          resourceURI = XSD_URI;
        }
        else if (WSDLConstants.DEFINITION_ELEMENT_TAG.equals(rootElementName))
        {
          resourceURI = WSDL_URI;
        }
        
        if (resourceURI != null)
        {
          resource = resourceSet.createResource(resourceURI);
          resource.setURI(uri);
        }
      }
    }
    return resource;
  }
} //ImportImpl
