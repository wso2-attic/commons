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
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Types</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class TypesImpl extends ExtensibleElementImpl implements Types
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TypesImpl()
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
    return WSDLPackage.Literals.TYPES;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getSchemas()
  {
    List arrayList = new ArrayList();
    for (Iterator i = getEExtensibilityElements().iterator(); i.hasNext();)
    {
      ExtensibilityElement extensibilityElement = (ExtensibilityElement)i.next();
      if (!(extensibilityElement instanceof XSDSchemaExtensibilityElement))
      {
        continue;
      }
      XSDSchemaExtensibilityElement xsdee = (XSDSchemaExtensibilityElement)extensibilityElement;
      if (xsdee.getSchema() != null)
      {
        arrayList.add(xsdee.getSchema());
      }
    }
    return arrayList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getSchemas(String namespaceURI)
  {
    List schemas = new ArrayList();
    for (Iterator i = getSchemas().iterator(); i.hasNext();)
    {
      XSDSchema schema = (XSDSchema)i.next();
      if (namespaceURI == null && schema.getTargetNamespace() == null)
      {
        schemas.add(schema);
      }
      else if (namespaceURI != null && namespaceURI.equals(schema.getTargetNamespace()))
      {
        schemas.add(schema);
      }
    }
    return schemas;
  }

  public void eNotify(Notification msg)
  {
    super.eNotify(msg);

    // cs.. if we've added an XSDSchemaExtensibilityElementImpl and the Types object is already attached 
    // to a resource we need to set the schemaLocation for the inline schema.
    // If not yet attached to a resource, the schemaLocation's will be set via WSDLResourceImpl.attached(EObject o)
    //     
    if (msg.getFeature() == WSDLPackage.Literals.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS && msg.getEventType() == Notification.ADD)
    {
      if (msg.getNewValue() instanceof XSDSchemaExtensibilityElementImpl)
      {
        XSDSchemaExtensibilityElementImpl ee = (XSDSchemaExtensibilityElementImpl)msg.getNewValue();
        if (ee.getSchema() != null && ee.getSchema().eResource() != null)
        {
          ee.getSchema().setSchemaLocation(ee.getSchema().eResource().getURI().toString());
        }
      }
    }
  }

  //
  // Reconcile methods
  //

  public void reconcileAttributes(Element changedElement)
  {
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    if (XSDConstants.SCHEMA_ELEMENT_TAG.equals(child.getLocalName()))
    {
      try
      {
        XSDSchemaExtensibilityElement xsdee = WSDLFactory.eINSTANCE.createXSDSchemaExtensibilityElement();
        xsdee.setEnclosingDefinition(getEnclosingDefinition());
        xsdee.setElement(child); // cs : this has the side effect of creating the inline schema               
        addExtensibilityElement(xsdee);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      super.handleUnreconciledElement(child, remainingModelObjects);
    }
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
    super.handleReconciliation(remainingModelObjects);
    ((DefinitionImpl)getEnclosingDefinition()).reconcileReferences(true);
  }

  public Collection getModelObjects(Object component)
  {
    Types types = (Types)component;
    List list = new ArrayList();
    list.addAll(types.getEExtensibilityElements());
    return list;
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.TYPES);
    setElement(newElement);

    // Add children
    Definition definition = getEnclosingDefinition();
    if (definition == null)
      return newElement;

    Document document = definition.getDocument();
    if (document == null)
      document = ((DefinitionImpl)definition).updateDocument();

    Iterator iter = getEExtensibilityElements().iterator();
    Element el = null;
    while (iter.hasNext())
    {
      ExtensibilityElement extensibility = (ExtensibilityElement)iter.next();
      el = extensibility.getElement();
      if (el != null)
      {
        try
        {
          Element reParented = (Element)document.importNode(el, true);
          extensibility.setElement(reParented); // replace with the new one
          newElement.appendChild(reParented);
        }
        catch (DOMException e)
        {
          e.printStackTrace();
          return newElement;
        }
      }
      else
      {
        Element child = ((ExtensibilityElementImpl)extensibility).createElement();
        newElement.appendChild(child);
      }
    }

    return newElement;
  }
} //TypesImpl