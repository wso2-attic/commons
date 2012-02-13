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


import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;

import com.ibm.icu.util.StringTokenizer;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Body</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl#getEEncodingStyles <em>EEncoding Styles</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl#getEParts <em>EParts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPBodyImpl extends ExtensibilityElementImpl implements SOAPBody
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getUse() <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUse()
   * @generated
   * @ordered
   */
  protected static final String USE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUse() <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUse()
   * @generated
   * @ordered
   */
  protected String use = USE_EDEFAULT;

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
   * The cached value of the '{@link #getEEncodingStyles() <em>EEncoding Styles</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEEncodingStyles()
   * @generated
   * @ordered
   */
  protected EList eEncodingStyles;

  /**
   * The cached value of the '{@link #getEParts() <em>EParts</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEParts()
   * @generated
   * @ordered
   */
  protected EList eParts;

  /**
   * The cached value of the '{@link #getParts() <em>Parts</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParts()
   * @generated NOT
   * @ordered
   */
  protected List parts;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SOAPBodyImpl()
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
    return SOAPPackage.Literals.SOAP_BODY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUse()
  {
    return use;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUse(String newUse)
  {
    String oldUse = use;
    use = newUse;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_BODY__USE, oldUse, use));
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
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_BODY__NAMESPACE_URI, oldNamespaceURI, namespaceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEEncodingStyles()
  {
    if (eEncodingStyles == null)
    {
      eEncodingStyles = new EDataTypeUniqueEList(String.class, this, SOAPPackage.SOAP_BODY__EENCODING_STYLES);
    }
    return eEncodingStyles;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEParts()
  {
    if (eParts == null)
    {
      eParts = new EObjectResolvingEList(Part.class, this, SOAPPackage.SOAP_BODY__EPARTS);
    }
    return eParts;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setEncodingStyles(List list)
  {
    eEncodingStyles = (EList)list;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getEncodingStyles()
  {
    return getEEncodingStyles();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setParts(List list)
  {
    parts = list;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getParts()
  {
    if (parts == null)
    {
      parts = new EObjectResolvingEList(Part.class, this, SOAPPackage.SOAP_BODY__EPARTS);
      return getImplicitParts();
    }

    if (parts.size() == 0)
    {
      return getImplicitParts();
    }

    return parts;
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
      case SOAPPackage.SOAP_BODY__USE:
      return getUse();
      case SOAPPackage.SOAP_BODY__NAMESPACE_URI:
      return getNamespaceURI();
      case SOAPPackage.SOAP_BODY__EENCODING_STYLES:
      return getEEncodingStyles();
      case SOAPPackage.SOAP_BODY__EPARTS:
      return getEParts();
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
      case SOAPPackage.SOAP_BODY__USE:
      setUse((String)newValue);
      return;
      case SOAPPackage.SOAP_BODY__NAMESPACE_URI:
      setNamespaceURI((String)newValue);
      return;
      case SOAPPackage.SOAP_BODY__EENCODING_STYLES:
      getEEncodingStyles().clear();
      getEEncodingStyles().addAll((Collection)newValue);
      return;
      case SOAPPackage.SOAP_BODY__EPARTS:
      getEParts().clear();
      getEParts().addAll((Collection)newValue);
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
      case SOAPPackage.SOAP_BODY__USE:
      setUse(USE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_BODY__NAMESPACE_URI:
      setNamespaceURI(NAMESPACE_URI_EDEFAULT);
      return;
      case SOAPPackage.SOAP_BODY__EENCODING_STYLES:
      getEEncodingStyles().clear();
      return;
      case SOAPPackage.SOAP_BODY__EPARTS:
      getEParts().clear();
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
      case SOAPPackage.SOAP_BODY__USE:
      return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
      case SOAPPackage.SOAP_BODY__NAMESPACE_URI:
      return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case SOAPPackage.SOAP_BODY__EENCODING_STYLES:
      return eEncodingStyles != null && !eEncodingStyles.isEmpty();
      case SOAPPackage.SOAP_BODY__EPARTS:
      return eParts != null && !eParts.isEmpty();
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
    result.append(" (use: "); //$NON-NLS-1$
    result.append(use);
    result.append(", namespaceURI: "); //$NON-NLS-1$
    result.append(namespaceURI);
    result.append(", eEncodingStyles: "); //$NON-NLS-1$
    result.append(eEncodingStyles);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcile methods
  //

  public void reconcileAttributes(Element changedElement)
  {
    setUse(SOAPConstants.getAttribute(changedElement, SOAPConstants.USE_ATTRIBUTE));
    setNamespaceURI(SOAPConstants.getAttribute(changedElement, SOAPConstants.NAMESPACE_ATTRIBUTE));
    if (changedElement.hasAttribute(SOAPConstants.ENCODING_STYLE_ATTRIBUTE))
    {
      String encodingStyles = changedElement.getAttribute(SOAPConstants.ENCODING_STYLE_ATTRIBUTE);
      StringTokenizer tokenizer = new StringTokenizer(encodingStyles);
      while (tokenizer.hasMoreTokens())
        getEncodingStyles().add(tokenizer.nextToken());
    }
    else
    {
      getEncodingStyles().clear();
    }

    reconcileReferences(false);
  }

  public void reconcileReferences(boolean deep)
  {
    if (element != null && element.hasAttribute(SOAPConstants.PARTS_ATTRIBUTE))
    // Synchronize 'parts' variable from element's attribute.
    {
      Message message = getMessage();
      if (message == null)
        return;

      String partNames = element.getAttribute(SOAPConstants.PARTS_ATTRIBUTE);
      StringTokenizer parser = new StringTokenizer(partNames, " ");
      String partName = null;
      Part newPart = null;
      getParts().clear();
      while (parser.hasMoreTokens())
      {
        partName = parser.nextToken();
        newPart = (message != null) ? (Part)message.getPart(partName) : null;
        if (newPart != null)
          // Do not use getParts() here since it will return a list of
          // implicitly collected parts.
          parts.add(newPart);
      }
    }
    super.reconcileReferences(deep);
  }

  //
  // For reconciliation: Model -> DOM
  //

  protected void changeAttribute(EAttribute eAttribute)
  {
    if (isReconciling)
      return;

    super.changeAttribute(eAttribute);
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_BODY__USE)
        niceSetAttribute(theElement, SOAPConstants.USE_ATTRIBUTE, getUse());
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_BODY__NAMESPACE_URI)
        niceSetAttribute(theElement, SOAPConstants.NAMESPACE_ATTRIBUTE, getNamespaceURI());
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_BODY__EENCODING_STYLES)
      {
        List encodingStyleList = getEncodingStyles();
        String encodingStyles = "";
        Iterator iterator = encodingStyleList.iterator();
        while (iterator.hasNext())
        {
          if (encodingStyles.equals("")) // first iteration
            encodingStyles += (String)iterator.next();
          else
            encodingStyles += " " + (String)iterator.next();
        }
        if (!encodingStyles.equals(""))
          niceSetAttribute(theElement, SOAPConstants.ENCODING_STYLE_ATTRIBUTE, encodingStyles);
      }
    }
  }

  protected void changeReference(EReference eReference)
  {
    if (isReconciling)
      return;

    super.changeReference(eReference);
    //
    // Update the element's "parts" attribute value.
    //
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eReference == null || eReference == SOAPPackage.Literals.SOAP_BODY__EPARTS)
      {
        // Used to be Bugzilla 108176, now it is Bugzilla 137990

        String partsAttributeValue = null;

        // Do not use getParts() as it will return the list of implicit message
        // parts.

        if (parts != null && !parts.isEmpty())
        {
          StringBuffer partNamesBuffer = new StringBuffer();
          Iterator iterator = parts.iterator();

          while (iterator.hasNext())
          {
            Part part = (Part)iterator.next();
            String partName = part.getName();

            partNamesBuffer.append(partName);

            if (iterator.hasNext())
            {
              partNamesBuffer.append(" "); //$NON-NLS-1$
            }
          }

          partsAttributeValue = partNamesBuffer.toString();
        }

        niceSetAttribute(theElement, SOAPConstants.PARTS_ATTRIBUTE, partsAttributeValue);
      }
    }
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.BODY_ELEMENT_TAG);
    return elementType;
  }

  private List getImplicitParts()
  {
    List implicitParts = new java.util.Vector();
    Message message = getMessage();
    if (message != null && message.getEParts().size() > 0)
    {
      implicitParts.addAll(message.getEParts());
    }
    return implicitParts;
  }

  private Message getMessage()
  {
    Message message = null;

    if (eContainer() instanceof BindingInput)
    {
      if (((BindingInput)eContainer()).getEInput() != null)
        message = ((BindingInput)eContainer()).getEInput().getEMessage();
    }
    if (eContainer() instanceof BindingOutput)
    {
      if (((BindingOutput)eContainer()).getEOutput() != null)
        message = ((BindingOutput)eContainer()).getEOutput().getEMessage();
    }
    if (eContainer() instanceof BindingFault)
    {
      if (((BindingFault)eContainer()).getEFault() != null)
        message = ((BindingFault)eContainer()).getEFault().getEMessage();
    }
    return message;
  }

} //SOAPBodyImpl
