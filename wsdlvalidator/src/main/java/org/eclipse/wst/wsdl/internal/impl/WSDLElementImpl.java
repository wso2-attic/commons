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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Namespace;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.ibm.icu.util.StringTokenizer;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl#getDocumentationElement <em>Documentation Element</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl#getElement <em>Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class WSDLElementImpl extends EObjectImpl implements WSDLElement
{
  /**
   * The default value of the '{@link #getDocumentationElement() <em>Documentation Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDocumentationElement()
   * @generated
   * @ordered
   */
  protected static final Element DOCUMENTATION_ELEMENT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDocumentationElement() <em>Documentation Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDocumentationElement()
   * @generated
   * @ordered
   */
  protected Element documentationElement = DOCUMENTATION_ELEMENT_EDEFAULT;

  /**
   * The default value of the '{@link #getElement() <em>Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElement()
   * @generated
   * @ordered
   */
  protected static final Element ELEMENT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getElement() <em>Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElement()
   * @generated
   * @ordered
   */
  protected Element element = ELEMENT_EDEFAULT;

  protected boolean isReconciling = false;

  protected boolean updatingDOM = false;

  protected EList wsdlContents;

  private Definition enclosingDefinition;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected WSDLElementImpl()
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
    return WSDLPackage.Literals.WSDL_ELEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Element getDocumentationElement()
  {
    return documentationElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDocumentationElement(Element newDocumentationElement)
  {
    Element oldDocumentationElement = documentationElement;
    documentationElement = newDocumentationElement;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(
        this,
        Notification.SET,
        WSDLPackage.WSDL_ELEMENT__DOCUMENTATION_ELEMENT,
        oldDocumentationElement,
        documentationElement));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Element getElement()
  {
    return element;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setElementGen(Element newElement)
  {
    Element oldElement = element;
    element = newElement;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.WSDL_ELEMENT__ELEMENT, oldElement, element));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setEnclosingDefinition(Definition definition)
  {
    enclosingDefinition = definition;
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
      case WSDLPackage.WSDL_ELEMENT__DOCUMENTATION_ELEMENT:
      return getDocumentationElement();
      case WSDLPackage.WSDL_ELEMENT__ELEMENT:
      return getElement();
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
      case WSDLPackage.WSDL_ELEMENT__DOCUMENTATION_ELEMENT:
      setDocumentationElement((Element)newValue);
      return;
      case WSDLPackage.WSDL_ELEMENT__ELEMENT:
      setElement((Element)newValue);
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
      case WSDLPackage.WSDL_ELEMENT__DOCUMENTATION_ELEMENT:
      setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
      return;
      case WSDLPackage.WSDL_ELEMENT__ELEMENT:
      setElement(ELEMENT_EDEFAULT);
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
      case WSDLPackage.WSDL_ELEMENT__DOCUMENTATION_ELEMENT:
      return DOCUMENTATION_ELEMENT_EDEFAULT == null
        ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.WSDL_ELEMENT__ELEMENT:
      return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
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
    result.append(" (documentationElement: "); //$NON-NLS-1$
    result.append(documentationElement);
    result.append(", element: "); //$NON-NLS-1$
    result.append(element);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcilation methods: DOM -> Model
  //

  public static class WSDLContentsEList extends EContentsEList
  {
    WSDLContentsEList(EObject eObject, EStructuralFeature[] eStructuralFeatures)
    {
      super(eObject, eStructuralFeatures);
    }

    protected ListIterator newListIterator()
    {
      return new FeatureIteratorImpl(eObject, eStructuralFeatures);
    }

    protected boolean isIncluded(EStructuralFeature eStructuralFeature)
    {
      EReference eReference = (EReference)eStructuralFeature;
      return !eReference.isTransient();
    }

    public List basicList()
    {
      return new WSDLContentsEList(eObject, eStructuralFeatures);
    }

    public Iterator basicIterator()
    {
      return new FeatureIteratorImpl(eObject, eStructuralFeatures);
    }

    public static class FeatureIteratorImpl extends EContentsEList.FeatureIteratorImpl
    {
      public FeatureIteratorImpl(EObject eObject)
      {
        super(eObject, (EStructuralFeature[])((BasicEList)eObject.eClass().getEAllReferences()).data());
      }

      public FeatureIteratorImpl(EObject eObject, EStructuralFeature[] eStructuralFeatures)
      {
        super(eObject, eStructuralFeatures);
      }

      protected boolean isIncluded(EStructuralFeature eStructuralFeature)
      {
        EReference eReference = (EReference)eStructuralFeature;
        return !eReference.isTransient();
      }
    }
  }

  public EList getWSDLContents()
  {
    if (wsdlContents == null)
    {
      wsdlContents = new WSDLContentsEList(this, (EStructuralFeature[])((BasicEList)eClass().getEAllContainments()).data());
    }

    return wsdlContents;
  }

  public void setElement(Element element)
  {
    if (element == null)
    {
      isReconciling = true;
      setElementGen(null);
      for (Iterator contents = getWSDLContents().iterator(); contents.hasNext();)
      {
        Object obj = contents.next();
        if (obj instanceof WSDLElementImpl)
        {
          WSDLElementImpl content = (WSDLElementImpl)obj;
          content.setElement(null);
        }
      }
      isReconciling = false;
    }
    else
    {
      setElementGen(element);
    }
  }

  public boolean eNotificationRequired()
  {
    return true;
  }

  public void eNotify(Notification msg)
  {
    int eventType = msg.getEventType();
    Object feature = msg.getFeature();
    Object oldValue = msg.getOldValue();
    Object newValue = msg.getNewValue();

    if (eClass().getEAllReferences().contains(feature))
    {
      EReference eReference = (EReference)feature;
      if (eReference.isContainment() && !eReference.isTransient())
      {
        switch (eventType)
        {
          case Notification.ADD:
          {
            adoptContent(eReference, newValue);
            break;
          }
          case Notification.ADD_MANY:
          {
            for (Iterator newValues = ((Collection)newValue).iterator(); newValues.hasNext();)
            {
              WSDLElement wsdlElement = (WSDLElement)newValues.next();
              adoptContent(eReference, wsdlElement);
            }
            break;
          }
          case Notification.REMOVE:
          {
            if (oldValue != null)
            {
              orphanContent(eReference, /*(WSDLElement)*/oldValue);
            }
            break;
          }
          case Notification.REMOVE_MANY:
          {
            for (Iterator oldValues = ((Collection)oldValue).iterator(); oldValues.hasNext();)
            {
              EObject object = (EObject)oldValues.next();
              orphanContent(eReference, object);
            }
            break;
          }
          case Notification.MOVE:
          {
            moveContent(eReference, (WSDLElement)newValue);
            break;
          }
          case Notification.SET:
          case Notification.UNSET:
          {
            if (oldValue != null)
            {
              orphanContent(eReference, oldValue);
            }
            if (newValue != null)
            {
              adoptContent(eReference, newValue);
            }
            break;
          }
        }
      }
      else
      {
        switch (eventType)
        {
          case Notification.ADD:
          case Notification.ADD_MANY:
          case Notification.REMOVE:
          case Notification.REMOVE_MANY:
          case Notification.MOVE:
          case Notification.SET:
          case Notification.UNSET:
          {
            changeReference(eReference);
            break;
          }
        }
      }
    }
    else if (eClass().getEAllAttributes().contains(feature))
    {
      EAttribute eAttribute = (EAttribute)feature;
      switch (eventType)
      {
        case Notification.ADD:
        case Notification.ADD_MANY:
        case Notification.REMOVE:
        case Notification.REMOVE_MANY:
        case Notification.MOVE:
        case Notification.SET:
        case Notification.UNSET:
        {
          changeAttribute(eAttribute);
          break;
        }
      }
    }
    super.eNotify(msg);
  }

  protected void orphanContent(EReference eReference, Object obj)
  {
    if (!isReconciling)
    {
      Element parent = getElement();
      if (parent != null && obj != null && obj instanceof WSDLElement)
      {
        WSDLElement wsdlElement = (WSDLElement)obj;
        Element contentElement = wsdlElement.getElement();
        if (contentElement != null)
          niceRemoveChild(contentElement.getParentNode(), contentElement);
      }
      else
        ;//System.out.println("WSDLElement.orphanContent(), Object is: " + obj);
    }
  }

  protected void niceRemoveChild(Node parent, Node child)
  {
    if (isReconciling)
    {
      // System.out.println("**** cyclic dom writeback avoided " + eClass().getName());
      return;
    }
    forceNiceRemoveChild(parent, child);
  }

  public void forceNiceRemoveChild(Node parent, Node child)
  {
    updatingDOM = true;

    boolean done = false;

    // System.out.println("?*");
    Node previous = child.getPreviousSibling();
    if (previous != null && previous.getNodeType() == Node.TEXT_NODE)
    {
      Text text = (Text)previous;
      String data = text.getData();
      int index = data.lastIndexOf('\n');
      if (index != -1)
      {
        if (index - 1 > 0 && data.charAt(index - 1) == '\r')
        {
          //System.out.println("1*");
          text.deleteData(index - 1, data.length() - index + 1);
        }
        else
        {
          //System.out.println("2*");
          text.deleteData(index, data.length() - index);
        }
        done = true;
      }
      else
      {
        //System.out.println("No return before!");
      }
    }
    else
    {
      // System.out.println("No text node before!");
    }

    if (!done)
    {
      for (Node next = child.getNextSibling(); next != null; next = next.getNextSibling())
      {
        if (next.getNodeType() == Node.TEXT_NODE)
        {
          Text text = (Text)next;
          String data = text.getData();
          /*
           System.out.print("xx " + data.length() + ":: ");
           for (int i = 0; i < data.length(); ++i)
           {
           System.out.print(" " + Integer.toHexString((int)data.charAt(i)));
           }
           System.out.println();
           */

          int index = data.indexOf('\n');
          if (index != -1)
          {
            if (index + 1 < data.length() && data.charAt(index + 1) == '\r')
            {
              // System.out.println("3*");
              text.deleteData(0, index + 2);
            }
            else
            {
              //System.out.println("4*");
              text.deleteData(0, index + 1);
            }
            break;
          }
          else
          {
            // System.out.println("No return after!");
          }
        }
        else if (next.getNodeType() == Node.ELEMENT_NODE)
        {
          break;
        }
      }
    }

    // System.out.println("Removing from--------\n     " + parent);
    // System.out.println("Removing--------\n     " + child);
    if (parent != null)
      parent.removeChild(child);

    updatingDOM = false;
  }

  protected void moveContent(EReference eReference, WSDLElement wsdlElement)
  {
    if (isReconciling)
    {
      // System.out.println("**** cyclic dom writeback avoided " + eClass().getName());
      return;
    }
    //System.out.println("moving " + xsdConcreteComponent);
    Node parent = getAdoptionParentNode(eReference);
    if (parent != null)
    {
      Element child = wsdlElement.getElement();
      if (child != null)
      {
        List contents = getWSDLContents();
        int index = contents.indexOf(wsdlElement);
        niceRemoveChild(parent, child);
        niceInsertBefore(parent, child, ++index == contents.size() ? null : ((WSDLElement)contents.get(index)).getElement());
      }
    }
    //System.out.println("moved " + xsdConcreteComponent);

  }

  protected void adoptContent(EReference eReference, Object object)
  {
    if (object instanceof WSDLElement)
    {
      WSDLElement wsdlElement = (WSDLElement)object;
      if (isReconciling)
      {
        if (wsdlElement.getElement() != null)
        {
          ((WSDLElementImpl)wsdlElement).elementChanged(wsdlElement.getElement());
        }
      }
      else
      {
        Element childElement = wsdlElement.getElement();
        if (getElement() != null && (childElement == null || childElement.getParentNode() == null))
        {
          if (childElement != null && childElement.getOwnerDocument() != getElement().getOwnerDocument())
          {
            wsdlElement.setElement(null);
            childElement = null;
          }

          handleElementForAdopt(eReference, wsdlElement);
          ((WSDLElementImpl)wsdlElement).updateElement();
        }
      }

      Definition definition = getEnclosingDefinition();
      if (definition != null)
      {
        ((WSDLElementImpl)wsdlElement).adoptBy(definition);
      }
    }
    else if (object instanceof Namespace)
    {
      // Add a namespace attribute to the Definitions element.
      Namespace ns = (Namespace)object;
      Node adoptionParent = getAdoptionParentNode(eReference); // Definitions node
      // KB: Assumption - The prefix is unique if we are here (by the Definitions model). 
      if (adoptionParent != null)
      {
        ((Element)adoptionParent).setAttribute("xmlns:" + ns.getPrefix(), ns.getURI());
      }
    }
  }

  protected Node getAdoptionParentNode(EReference eReference)
  {
    return getElement();
  }

  protected void handleElementForAdopt(EReference eReference, WSDLElement wsdlElement)
  {
    // Establish DOM parent-child relationship

    Node adoptionParent = getAdoptionParentNode(eReference);
    Element childElement = wsdlElement.getElement();
    Element referencedElement = null;

    for (Iterator contents = getWSDLContents().iterator(); contents.hasNext();)
    {
      if (contents.next() == wsdlElement)
      {
        if (contents.hasNext())
        {
          Object next = contents.next();
          if (next instanceof WSDLElement)
          {
            referencedElement = ((WSDLElement)next).getElement();
            while (referencedElement != null)
            {
              Node parent = referencedElement.getParentNode();
              if (parent == null)
              {
                referencedElement = null;
                break;
              }
              else if (parent == adoptionParent)
              {
                break;
              }
              else if (parent.getNodeType() == Node.ELEMENT_NODE)
              {
                referencedElement = (Element)parent;
              }
              else
              {
                break;
              }
            }
          }
        }
        break;
      }
    }

    if (childElement == null)
    {
      ((WSDLElementImpl)wsdlElement).isReconciling = true;
      childElement = ((WSDLElementImpl)wsdlElement).createElement();
      ((WSDLElementImpl)wsdlElement).isReconciling = false;
      if (childElement == null)
      {
        //System.out.println("not created! " + wsdlElement);
        return;
      }
    }

    boolean isAttached = false;
    for (Node rootDocument = childElement; rootDocument != null; rootDocument = rootDocument.getParentNode())
    {
      if (WSDLConstants.nodeType(rootDocument) == WSDLConstants.DEFINITION)
      {
        isAttached = true;
        break;
      }
    }

    if (!isAttached)
    {
      // If we're dealing with a documentation element we need to put it first in the list

      if (referencedElement == null && WSDLConstants.nodeType(childElement) == WSDLConstants.DOCUMENTATION && !eReference.isMany())
      {
        // Here we find the first element node in the list, the documentation element needs to go before this element

        for (Node child = adoptionParent.getFirstChild(); child != null; child = child.getNextSibling())
        {
          if (child.getNodeType() == Node.ELEMENT_NODE)
          {
            referencedElement = (Element)child;
            break;
          }
        }
      }
      //referencedElement = computeTopLevelRefChild(adoptionParent,childElement);     
      niceInsertBefore(adoptionParent, childElement, referencedElement);
    }
  }

  public void niceInsertBefore(Node parent, Node newChild, Node referenceChild)
  {
    if (isReconciling)
    {
      // System.out.println("**** cyclic dom writeback avoided " + eClass().getName());
      return;
    }

    forceNiceInsertBefore(parent, newChild, referenceChild);
  }

  public void forceNiceInsertBefore(Node parent, Node newChild, Node referenceChild)
  {
    updatingDOM = true;

    LOOP: for (Node child = referenceChild == null ? parent.getLastChild() : referenceChild.getPreviousSibling(); child != null; child = child.getPreviousSibling())
    {
      switch (child.getNodeType())
      {
        case Node.TEXT_NODE:
        {
          Text text = (Text)child;
          String data = text.getData();

          /*
           System.out.print("xx " + data.length() + ":: ");
           for (int i = 0; i < data.length(); ++i)
           {
           System.out.print(" " + Integer.toHexString((int)data.charAt(i)));
           }
           System.out.println();
           */

          int index = data.lastIndexOf('\n');
          if (index != -1)
          {
            // System.out.println("In here");

            StringBuffer indent = new StringBuffer();
            for (Node ancestor = parent.getParentNode(); ancestor != null && ancestor.getNodeType() != Node.DOCUMENT_NODE; ancestor = ancestor.getParentNode())
            {
              indent.append("    ");
            }

            if (index + 1 < data.length() && data.charAt(index + 1) == '\r')
            {
              ++index;
            }
            text.replaceData(index + 1, data.length() - index - 1, indent + "    ");

            // setCorrectIndentation(indent, newText);
            if (referenceChild != null)
            {
              indent.append("    ");
            }
            Text newText = parent.getOwnerDocument().createTextNode("\n" + indent);
            // System.out.println("Inserted..." + newText);
            parent.insertBefore(newText, referenceChild);
            referenceChild = newText;
            break LOOP;
          }

          break;
        }
        case Node.ELEMENT_NODE:
        {
          break LOOP;
        }
      }
    }

    // System.out.println("Inserted..." + newChild);
    parent.insertBefore(newChild, referenceChild);

    updatingDOM = false;
  }

  protected Element computeTopLevelRefChild(Node parentNode, Node nodeToAdd)
  {
    Element result = null;
    int a = getPrecedence(nodeToAdd);

    for (Node node = parentNode.getFirstChild(); node != null; node = node.getNextSibling())
    {
      if (node.getNodeType() == Node.ELEMENT_NODE)
      {
        int b = getPrecedence(node);
        if (b >= a)
        {
          result = (Element)node;
          break;
        }
      }
    }
    return result;
  }

  private HashMap precedenceMap = createPrecedenceMap();

  private int getPrecedence(Node node)
  {
    int result = 2;
    String localName = node.getLocalName();
    if (localName != null)
    {
      Integer integer = (Integer)precedenceMap.get(localName);
      if (integer != null)
      {
        result = integer.intValue();
      }
    }
    return result;
  }

  private HashMap createPrecedenceMap()
  {
    HashMap hashMap = new HashMap();
    hashMap.put(WSDLConstants.DOCUMENTATION_ELEMENT_TAG, new Integer(1));
    hashMap.put(WSDLConstants.IMPORT_ELEMENT_TAG, new Integer(3));
    hashMap.put(WSDLConstants.TYPES_ELEMENT_TAG, new Integer(4));
    hashMap.put(WSDLConstants.MESSAGE_ELEMENT_TAG, new Integer(5));
    hashMap.put(WSDLConstants.PORT_TYPE_ELEMENT_TAG, new Integer(6));
    hashMap.put(WSDLConstants.BINDING_ELEMENT_TAG, new Integer(7));
    hashMap.put(WSDLConstants.SERVICE_ELEMENT_TAG, new Integer(8));
    return hashMap;
  }

  protected void adoptBy(Definition definition)
  {
    for (Iterator components = getWSDLContents().iterator(); components.hasNext();)
    {
      Object child = components.next();
      if (child instanceof WSDLElementImpl)
      {
        ((WSDLElementImpl)child).adoptBy(definition);
      }
    }
  }

  protected boolean isUpdatingDOM()
  {
    return updatingDOM;
  }

  public void elementChanged(Element changedElement)
  {
    if (!isUpdatingDOM())
    {
      if (!isReconciling)
      {
        //System.out.println("**** changeFor " + eClass().getName());
        isReconciling = true;
        reconcile(changedElement);

        WSDLElement theContainer = getContainer();
        if (theContainer != null && theContainer.getElement() == changedElement)
        {
          ((WSDLElementImpl)theContainer).elementChanged(changedElement);
        }

        isReconciling = false;
        traverseToRootForPatching();
      }
      else
      {
        ; //System.out.println("**** cyclic internal reconcile avoided " + eClass().getName());
      }
    }
    else
    {
      ; //System.out.println("**** cyclic DOM reconcile avoided " + eClass().getName());
    }
  }

  protected void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
  }

  /**
   * @deprecated
   * TODO... remove this method!
   */
  public Collection getModelObjects(Object component)
  {
    return getWSDLContents();
  }

  /*
   protected Element getElementForModelObject(Object modelObject)
   {
   Element result = null;
   if (modelObject instanceof Element)
   {
   result = (Element) modelObject;
   }
   else if (modelObject instanceof WSDLElement)
   {
   result = ((WSDLElement) modelObject).getElement();
   }
   return result;
   }
   */

  private Collection getContentNodes(Element changedElement)
  {
    Collection result = new ArrayList();
    for (Node child = changedElement.getFirstChild(); child != null; child = child.getNextSibling())
    {
      if (child.getNodeType() == Node.ELEMENT_NODE)
      {
        result.add(child);
      }
    }
    return result;
  }

  // Some subclasses use this method
  protected QName createQName(Definition definition, String prefixedName)
  {
    // Delegate to the new form to preserve backward compatibility in case someone
    // else calls this method.

    return createQName(definition, prefixedName, null);
  }

  /**
   * Creates a QName from a prefixed name. Takes into account locally defined
   * namespace prefixes.
   * 
   * @param definition
   *          the enclosing definition. Must not be null.
   * @param prefixedName
   *          the prefixed name to convert to QName
   * @param element
   *          the enclosing element. May be null in which case the prefix is
   *          only looked up among the ones defined at the definition level.
   * @return the QName equivalent for the given prefixed name, or null if a
   *         namespace prefix cannot be found for the given namespace URI or if
   *         the prefixed name is null.
   */
  protected QName createQName(Definition definition, String prefixedName, Element element)
  {
    return createQName(definition, prefixedName, element, false);
  }

  /**
   * Creates a QName from a prefixed name. Takes into account locally defined
   * namespace prefixes. Selectively allows null namespace URIs.
   * 
   * @param definition
   *          the enclosing definition. Must not be null.
   * @param prefixedName
   *          the prefixed name to convert to QName
   * @param element
   *          the enclosing element. May be null in which case the prefix is
   *          only looked up among the ones defined at the definition level.
   * @param allowNullNamespaceURI if true and the prefixed name does not have 
   *          a prefix a QName is constructed and returned using 
   *          {@link XMLConstants#NULL_NS_URI} for the namespace URI          
   * @return the QName equivalent for the given prefixed name, or null if a
   *         namespace prefix cannot be found for the given namespace URI or if
   *         the prefixed name is null. 
   */
  protected QName createQName(Definition definition, String prefixedName, Element element, boolean allowNullNamespaceURI)
  {
    QName qname = null;
    if (prefixedName != null)
    {
      int index = prefixedName.indexOf(":"); ////$NON-NLS-1$
      String prefix = (index == -1) ? "" : prefixedName.substring(0, index); //$NON-NLS-1$
      String namespace = definition.getNamespace(prefix);

      if (namespace == null && element != null)
      {
        // Try to find a locally defined namespace prefix.

        namespace = getNamespaceURIFromPrefix(element, prefix);
      }

      if (namespace != null || (allowNullNamespaceURI && prefix.length() == 0))
      {
        String localPart = prefixedName.substring(index + 1);
        qname = new QName(namespace, localPart);
      }
    }
    return qname;
  }

  /**
   * Given a prefix and a node, finds the namespace URI pointed to by the
   * prefix. Walks the element containment hierarchy until it finds one or it
   * reaches the document root.
   * 
   * @param node
   *          the starting node
   * @param prefix
   *          the prefix to find an xmlns:prefix=uri for
   * 
   * @return the namespace URI or null if not found
   */
  private static String getNamespaceURIFromPrefix(Node node, String prefix)
  {
    if (node == null || prefix == null)
    {
      return null;
    }

    Node currentNode = node;

    while (currentNode != null && currentNode.getNodeType() == Node.ELEMENT_NODE)
    {
      String namespaceURI = getAttributeNS((Element)currentNode, XSDConstants.XMLNS_URI_2000, prefix);

      if (namespaceURI != null)
      {
        return namespaceURI;
      }
      else
      {
        currentNode = currentNode.getParentNode();
      }
    }

    return null;
  }

  /**
   * Retrieves an attribute's value.
   * @param element the containing element.
   * @param namespaceURI the namespace URI.
   * @param localPart the local name.
   * @return the attribute's value if present, or null if not. 
   */
  private static String getAttributeNS(Element element, String namespaceURI, String localPart)
  {
    String attributeValue = null;
    Attr attribute = element.getAttributeNodeNS(namespaceURI, localPart);

    if (attribute != null)
    {
      attributeValue = attribute.getValue();
    }

    return attributeValue;
  }

  //
  // For reconciliation: Model -> DOM
  //

  public void updateElement(boolean deep)
  {
    if (deep || getElement() == null)
    {
      updateElement();
    }
    else
    {
      changeAttribute(null);
      changeReference(null);
    }
  }

  public void updateElement()
  {
    if (getElement() == null)
    {
      isReconciling = true;
      createElement();
      isReconciling = false;
    }

    changeAttribute(null);
    changeReference(null);

    Object obj = null;
    for (Iterator containments = eClass().getEAllContainments().iterator(); containments.hasNext();)
    {
      EReference eReference = (EReference)containments.next();
      if (eReference == WSDLPackage.Literals.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA)
      {
        // TBD - This is an instance of XSDSchemaExtensibilityElement and
        // eRefernce is XSDSchema. What do we want to do for update?
      }
      else if (!eReference.isTransient())
      {
        if (eReference.isMany())
        {
          for (Iterator objects = ((Collection)eGet(eReference)).iterator(); objects.hasNext();)
          {
            obj = objects.next();
            if (!(obj instanceof WSDLElement) || obj == null)
            {
              // Skip it. Probably this is Namespace.
            }
            else
            {
              WSDLElement wsdlElement = (WSDLElement)obj;
              //handleElementForAdopt(eReference, wsdlElement);
              ((WSDLElementImpl)wsdlElement).updateElement();
            }
          }
        }
        else
        {
          WSDLElement wsdlElement = (WSDLElement)eGet(eReference);
          if (wsdlElement != null)
          {
            //handleElementForAdopt(eReference,  wsdlElement);
            ((WSDLElementImpl)wsdlElement).updateElement();
          }
        }
      } // end else if
    } // end for
  }

  protected void changeAttribute(EAttribute eAttribute)
  {
    if (eAttribute == WSDLPackage.Literals.WSDL_ELEMENT__ELEMENT)
    {
      Element theElement = getElement();
      if (theElement != null && !isReconciling)
      {
        for (Node rootDocument = theElement.getParentNode(); rootDocument != null; rootDocument = rootDocument.getParentNode())
        {
          if (rootDocument.getNodeType() == Node.DOCUMENT_NODE)
          {
            isReconciling = true;
            if (getEnclosingDefinition() == null)
            {
              reconcileAttributes(getElement());
              isReconciling = false;
            }
            else
            {
              reconcile(getElement());
              isReconciling = false;
              traverseToRootForPatching();
            }
            break;
          }
        }
      }
    }

  }

  protected void traverseToRootForPatching()
  {
    if (!isReconciling)
    {
      WSDLElementImpl container = (WSDLElementImpl)getContainer();
      if (container != null)
      {
        container.traverseToRootForPatching();
      }
    }
  }

  protected void reconcile(Element changedElement)
  {
    reconcileAttributes(changedElement);
    reconcileContents(changedElement);
  }

  protected void reconcileAttributes(Element changedElement)
  {
  }

  protected void reconcileContents(Element changedElement)
  {
    List remainingModelObjects = new ArrayList(getWSDLContents());

    Collection contentNodes = getContentNodes(changedElement);

    Element theDocumentationElement = null;

    // for each applicable child node of changedElement
    LOOP: for (Iterator i = contentNodes.iterator(); i.hasNext();)
    {
      Element child = (Element)i.next();
      // Set Documentation element if exists
      if (WSDLConstants.DOCUMENTATION_ELEMENT_TAG.equals(child.getLocalName())
        && WSDLConstants.isMatchingNamespace(child.getNamespaceURI(), WSDLConstants.WSDL_NAMESPACE_URI))
      {
        // assume the first 'documentation' element is 'the' documentation element
        // 'there can be only one!'
        if (theDocumentationElement == null)
        {
          theDocumentationElement = child;
        }
      }
      // go thru the model objects to collect matching object for reuse
      for (Iterator contents = remainingModelObjects.iterator(); contents.hasNext();)
      {
        Object modelObject = (Object)contents.next();

        if (modelObject instanceof WSDLElement)
        {
          if (((WSDLElement)modelObject).getElement() == child)
          {
            contents.remove(); // removes the 'child' Node from the remainingModelObjects list
            continue LOOP;
          }
        }
      }

      // if the documentation element has changed... update it
      //
      if (theDocumentationElement != getDocumentationElement())
      {
        setDocumentationElement(theDocumentationElement);
      }

      // we haven't found a matching model object for the Node, se we may need to
      // create a new model object
      handleUnreconciledElement(child, remainingModelObjects);
    }

    // now we can remove the remaining model objects
    handleReconciliation(remainingModelObjects);

  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Definition getEnclosingDefinition()
  {
    if (enclosingDefinition != null)
      return enclosingDefinition;

    // TBD - Revisit
    //EObject c = eContaier(); c != null; c = c.eContainter())
    //{

    for (WSDLElement container = this; container != null; container = ((WSDLElementImpl)container).getContainer())
    {
      if (container instanceof Definition)
      {
        enclosingDefinition = (Definition)container;
        return enclosingDefinition;
      }
    }

    return null;
  }

  /*
   public Definition getRootEnclosingDefinition()
   {
   if (enclosingDefinition == null)
   {
   Resource resource = eResource();
   List contents = resource.getContents();
   if (contents.size() == 1 
   && contents.get(0) instanceof Definition)
   enclosingDefinition = (Definition)contents.get(0);
   } 
   return enclosingDefinition;
   }
   */

  public WSDLElement getContainer()
  {
    return eContainer() instanceof WSDLElement ? (WSDLElement)eContainer() : null;
  }

  protected void niceSetAttribute(Element element, String attribute, String value)
  {
    if (isReconciling)
    {
      //System.out.println("**** cyclic dom attribute writeback avoided " + eClass().getName());
      return;
    }

    if (value == null)
    {
      if (element.hasAttributeNS(null, attribute))
      {
        updatingDOM = true;
        element.removeAttribute(attribute);
        updatingDOM = false;
      }
    }
    else if (!element.hasAttributeNS(null, attribute) || !element.getAttributeNS(null, attribute).equals(value))
    {
      updatingDOM = true;
      element.setAttributeNS(null, attribute, value);
      updatingDOM = false;
    }
  }

  protected String niceCreateNamespaceAttribute(String namespace)
  {
    // Create and return a unique prefix for "namespace."
    return null;
  }

  protected void niceSetAttributeURIValue(Element element, String attribute, String uriList)
  {
    if (isReconciling)
    {
      // System.out.println("**** cyclic dom attribute writeback avoided " + eClass().getName());
      return;
    }

    if (uriList == null)
    {
      updatingDOM = true;
      element.removeAttribute(attribute);
      updatingDOM = false;
    }
    else
    {
      StringBuffer value = new StringBuffer();
      for (StringTokenizer stringTokenizer = new StringTokenizer(uriList, " "); stringTokenizer.hasMoreElements();)
      {
        String uri = stringTokenizer.nextToken();
        String namespace = null;
        String localName = uri;
        int index = uri.lastIndexOf("#");
        if (index != -1)
        {
          if (index > 0)
          {
            namespace = uri.substring(0, index);
          }
          localName = uri.substring(index + 1);
        }
        String qualifier = XSDConstants.lookupQualifier(element, namespace);
        if (qualifier == null)
        {
          qualifier = niceCreateNamespaceAttribute(namespace);
        }

        String qName = qualifier == null || qualifier.length() == 0 ? localName : qualifier + ":" + localName;

        if (value.length() != 0)
        {
          value.append(' ');
        }
        value.append(qName);
      }

      if (!element.hasAttributeNS(null, attribute) || !element.getAttributeNS(null, attribute).equals(value.toString()))
      {
        updatingDOM = true;
        element.setAttributeNS(null, attribute, value.toString());
        updatingDOM = false;
      }
    }
  }

  protected void changeReference(EReference eReference)
  {
  }

  protected Element createElement() // Should I make this abstract?
  {
    return null;
  }

  protected final Element createElement(int nodeType)
  {
    Definition definition = null;
    if (this instanceof Definition)
      definition = (Definition)this;
    else
      definition = getEnclosingDefinition();

    if (definition == null)
      return null;

    Document document = definition.getDocument();
    if (document == null)
      document = ((DefinitionImpl)definition).updateDocument();

    // Retrieve the namespace prefix for the WSDL namespace
    String wsdlNamespace = WSDLConstants.WSDL_NAMESPACE_URI;
    String qualifier = definition.getPrefix(wsdlNamespace);

    Element newElement = document.createElementNS(wsdlNamespace, (qualifier == null ? "" : qualifier + ":")
      + WSDLConstants.getElementTag(nodeType));

    if (document.getDocumentElement() == null)
      document.appendChild(newElement);

    if (getDocumentationElement() != null)
      newElement.appendChild(document.importNode(getDocumentationElement(), true));

    return newElement;
  }

  public void reconcileReferences(boolean deep)
  {
    if (deep)
    {
      for (Iterator contents = getWSDLContents().iterator(); contents.hasNext();)
      {
        Object content = contents.next();
        if (content instanceof WSDLElementImpl)
        {
          WSDLElementImpl element = (WSDLElementImpl)content;
          element.isReconciling = true;
          element.reconcileReferences(true);
          element.isReconciling = false;
        }
      }
    }
  }

  public WSDLElement getBestWSDLElement(List elementPath)
  {
    WSDLElement result = this;
    for (Iterator components = getWSDLContents().iterator(); components.hasNext();)
    {
      Object object = components.next();
      if (object instanceof WSDLElementImpl)
      {
        WSDLElementImpl childWSDLElement = (WSDLElementImpl)object;

        if (elementPath.contains(childWSDLElement.getElement()))
        {
          result = childWSDLElement;
          WSDLElement betterWSDLElement = childWSDLElement.getBestWSDLElement(elementPath);
          if (betterWSDLElement != null)
          {
            result = betterWSDLElement;
          }

          if (!considerAllContainsForBestWSDLElement())
          {
            break;
          }
        }
      }
    }

    return result;
  }

  protected boolean considerAllContainsForBestWSDLElement()
  {
    return false;
  }

  public void elementAttributesChanged(Element changedElement)
  {
    if (!isUpdatingDOM())
    {
      if (!isReconciling)
      {
        // System.out.println("**** changeFor " + eClass().getName());

        isReconciling = true;
        reconcileAttributes(changedElement);

        WSDLElementImpl theContainer = (WSDLElementImpl)getContainer();
        if (theContainer != null && theContainer.getElement() == changedElement)
        {
          theContainer.elementAttributesChanged(changedElement);
        }

        isReconciling = false;
        traverseToRootForPatching();
      }
      else
      {
        // System.out.println("**** cyclic internal reconcile avoided " + eClass().getName());
      }
    }
    else
    {
      // System.out.println("**** cyclic DOM reconcile avoided " + eClass().getName());
    }
  }

  public void elementContentsChanged(Element changedElement)
  {
    if (!isUpdatingDOM())
    {
      if (!isReconciling)
      {
        // System.out.println("**** changeFor " + eClass().getName());

        isReconciling = true;
        reconcileContents(changedElement);

        WSDLElementImpl theContainer = (WSDLElementImpl)getContainer();
        if (theContainer != null && theContainer.getElement() == changedElement)
        {
          theContainer.elementContentsChanged(changedElement);
        }

        isReconciling = false;
        traverseToRootForPatching();
      }
      else
      {
        // System.out.println("**** cyclic internal reconcile avoided " + eClass().getName());
      }
    }
    else
    {
      // System.out.println("**** cyclic DOM reconcile avoided " + eClass().getName());
    }
  }

  protected void updatePrefix(Node node, String namespace, String oldPrefix, String newPrefix)
  {
    if ((namespace == null ? node.getNamespaceURI() == null : namespace.equals(node.getNamespaceURI()))
      && (oldPrefix == null ? node.getPrefix() == null : oldPrefix.equals(node.getPrefix())))
    {
      node.setPrefix(newPrefix);
    }

    for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
    {
      updatePrefix(child, namespace, oldPrefix, newPrefix);
    }
  }

  protected String getNamespace(Element element)
  {
    String name = element.getTagName();
    int index = name.indexOf(":"); //$NON-NLS-1$
    String nsPrefix = null;
    if (index != -1)
    {
      nsPrefix = name.substring(0, index);
    }
    else
    {
      nsPrefix = "xmlns"; //$NON-NLS-1$
    }

    String namespaceURI = null;

    // First try to locate the namespace URI in the definition's prefix to namespace map.
    // This will provide backward compatibility for existing clients.

    Definition enclosingDefinition = getEnclosingDefinition();
    if (enclosingDefinition != null)
    {
      namespaceURI = enclosingDefinition.getNamespace(nsPrefix);
    }

    // We did not find it at the top level, try to find a locally defined namespace prefix.

    if (namespaceURI == null)
    {
      namespaceURI = getNamespaceURIFromPrefix(element, nsPrefix);
    }

    return namespaceURI;
  }

  protected String getLocalName(Element element)
  {
    String name = element.getTagName();
    int index = name.indexOf(":");
    if (index == -1)
      return name;
    else
      return name.substring(index + 1);
  }

} //WSDLElementImpl
