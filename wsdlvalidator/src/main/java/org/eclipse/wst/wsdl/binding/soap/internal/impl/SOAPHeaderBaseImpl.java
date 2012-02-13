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
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;

import com.ibm.icu.util.StringTokenizer;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Header Base</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getEEncodingStyles <em>EEncoding Styles</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getMessage <em>Message</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getPart <em>Part</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getEMessage <em>EMessage</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getEPart <em>EPart</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPHeaderBaseImpl extends ExtensibilityElementImpl implements SOAPHeaderBase
{
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
   * The default value of the '{@link #getMessage() <em>Message</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessage()
   * @generated
   * @ordered
   */
  protected static final QName MESSAGE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMessage() <em>Message</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessage()
   * @generated
   * @ordered
   */
  protected QName message = MESSAGE_EDEFAULT;

  /**
   * The default value of the '{@link #getPart() <em>Part</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPart()
   * @generated
   * @ordered
   */
  protected static final String PART_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPart() <em>Part</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPart()
   * @generated
   * @ordered
   */
  protected String part = PART_EDEFAULT;

  /**
   * The cached value of the '{@link #getEMessage() <em>EMessage</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEMessage()
   * @generated
   * @ordered
   */
  protected Message eMessage;

  /**
   * The cached value of the '{@link #getEPart() <em>EPart</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEPart()
   * @generated
   * @ordered
   */
  protected Part ePart;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SOAPHeaderBaseImpl()
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
    return SOAPPackage.Literals.SOAP_HEADER_BASE;
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
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__USE, oldUse, use));
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
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI, oldNamespaceURI, namespaceURI));
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
      eEncodingStyles = new EDataTypeUniqueEList(String.class, this, SOAPPackage.SOAP_HEADER_BASE__EENCODING_STYLES);
    }
    return eEncodingStyles;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message getEMessage()
  {
    if (eMessage != null && eMessage.eIsProxy())
    {
      InternalEObject oldEMessage = (InternalEObject)eMessage;
      eMessage = (Message)eResolveProxy(oldEMessage);
      if (eMessage != oldEMessage)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SOAPPackage.SOAP_HEADER_BASE__EMESSAGE, oldEMessage, eMessage));
      }
    }
    return eMessage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message basicGetEMessage()
  {
    return eMessage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEMessage(Message newEMessage)
  {
    Message oldEMessage = eMessage;
    eMessage = newEMessage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__EMESSAGE, oldEMessage, eMessage));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Part getEPart()
  {
    if (ePart != null && ePart.eIsProxy())
    {
      InternalEObject oldEPart = (InternalEObject)ePart;
      ePart = (Part)eResolveProxy(oldEPart);
      if (ePart != oldEPart)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SOAPPackage.SOAP_HEADER_BASE__EPART, oldEPart, ePart));
      }
    }
    return ePart;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Part basicGetEPart()
  {
    return ePart;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEPart(Part newEPart)
  {
    Part oldEPart = ePart;
    ePart = newEPart;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__EPART, oldEPart, ePart));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QName getMessage()
  {
    return message;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMessage(QName newMessage)
  {
    QName oldMessage = message;
    message = newMessage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__MESSAGE, oldMessage, message));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPart()
  {
    return part;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPart(String newPart)
  {
    String oldPart = part;
    part = newPart;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__PART, oldPart, part));
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
  public void setEncodingStyles(List encodingStyles)
  {
    eEncodingStyles = (EList)encodingStyles;
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
      case SOAPPackage.SOAP_HEADER_BASE__USE:
      return getUse();
      case SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI:
      return getNamespaceURI();
      case SOAPPackage.SOAP_HEADER_BASE__EENCODING_STYLES:
      return getEEncodingStyles();
      case SOAPPackage.SOAP_HEADER_BASE__MESSAGE:
      return getMessage();
      case SOAPPackage.SOAP_HEADER_BASE__PART:
      return getPart();
      case SOAPPackage.SOAP_HEADER_BASE__EMESSAGE:
      if (resolve)
        return getEMessage();
      return basicGetEMessage();
      case SOAPPackage.SOAP_HEADER_BASE__EPART:
      if (resolve)
        return getEPart();
      return basicGetEPart();
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
      case SOAPPackage.SOAP_HEADER_BASE__USE:
      setUse((String)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI:
      setNamespaceURI((String)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__EENCODING_STYLES:
      getEEncodingStyles().clear();
      getEEncodingStyles().addAll((Collection)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__MESSAGE:
      setMessage((QName)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__PART:
      setPart((String)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__EMESSAGE:
      setEMessage((Message)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__EPART:
      setEPart((Part)newValue);
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
      case SOAPPackage.SOAP_HEADER_BASE__USE:
      setUse(USE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI:
      setNamespaceURI(NAMESPACE_URI_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__EENCODING_STYLES:
      getEEncodingStyles().clear();
      return;
      case SOAPPackage.SOAP_HEADER_BASE__MESSAGE:
      setMessage(MESSAGE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__PART:
      setPart(PART_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__EMESSAGE:
      setEMessage((Message)null);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__EPART:
      setEPart((Part)null);
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
      case SOAPPackage.SOAP_HEADER_BASE__USE:
      return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
      case SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI:
      return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case SOAPPackage.SOAP_HEADER_BASE__EENCODING_STYLES:
      return eEncodingStyles != null && !eEncodingStyles.isEmpty();
      case SOAPPackage.SOAP_HEADER_BASE__MESSAGE:
      return MESSAGE_EDEFAULT == null ? message != null : !MESSAGE_EDEFAULT.equals(message);
      case SOAPPackage.SOAP_HEADER_BASE__PART:
      return PART_EDEFAULT == null ? part != null : !PART_EDEFAULT.equals(part);
      case SOAPPackage.SOAP_HEADER_BASE__EMESSAGE:
      return eMessage != null;
      case SOAPPackage.SOAP_HEADER_BASE__EPART:
      return ePart != null;
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
    result.append(", message: "); //$NON-NLS-1$
    result.append(message);
    result.append(", part: "); //$NON-NLS-1$
    result.append(part);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
    setUse(SOAPConstants.getAttribute(changedElement, SOAPConstants.USE_ATTRIBUTE));
    setNamespaceURI(SOAPConstants.getAttribute(changedElement, SOAPConstants.NAMESPACE_ATTRIBUTE));
    if (changedElement.hasAttribute(SOAPConstants.MESSAGE_ATTRIBUTE))
    {
      Definition definition = (Definition)getEnclosingDefinition();
      QName messageQName = createQName(definition, changedElement.getAttribute(SOAPConstants.MESSAGE_ATTRIBUTE), changedElement);
      setMessage(messageQName);
    }
    else
    {
      setMessage(null);
    }
    setPart(SOAPConstants.getAttribute(changedElement, SOAPConstants.PART_ATTRIBUTE));
    if (changedElement.hasAttribute(SOAPConstants.ENCODING_STYLE_ATTRIBUTE))
    {
      String encodingStyles = changedElement.getAttribute(SOAPConstants.ENCODING_STYLE_ATTRIBUTE);
      StringTokenizer tokenizer = new StringTokenizer(encodingStyles);
      while (tokenizer.hasMoreTokens())
        getEEncodingStyles().add(tokenizer.nextToken());
    }
    else
    {
      getEEncodingStyles().clear();
    }

    reconcileReferences(false);
  }

  public void reconcileReferences(boolean deep)
  {
    if (element != null)
    {
      Definition definition = (Definition)getEnclosingDefinition();
      Message message = (this.message != null) ? (Message)definition.getMessage(this.message) : null;
      if (message != null && message != getEMessage())
      {
        setEMessage(message);

        Part newPart = (Part)message.getPart(part);
        if (newPart != null && newPart != getEPart())
        {
          setEPart(newPart);
        }
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
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_HEADER_BASE__USE)
        niceSetAttribute(theElement, SOAPConstants.USE_ATTRIBUTE, getUse());
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_HEADER_BASE__NAMESPACE_URI)
        niceSetAttribute(theElement, SOAPConstants.NAMESPACE_ATTRIBUTE, getNamespaceURI());
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_HEADER_BASE__PART)
        niceSetAttribute(theElement, SOAPConstants.PART_ATTRIBUTE, getPart());
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_HEADER_BASE__MESSAGE)
      {
        String uriList = message != null ? message.getNamespaceURI() + "#" + message.getLocalPart() : null;
        niceSetAttributeURIValue(theElement, SOAPConstants.MESSAGE_ATTRIBUTE, uriList);
      }
      if (eAttribute == null || eAttribute == SOAPPackage.Literals.SOAP_HEADER_BASE__EENCODING_STYLES)
      {
        List encodingStyleList = getEEncodingStyles();
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
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eReference == null || eReference == SOAPPackage.Literals.SOAP_HEADER_BASE__EMESSAGE)
      {
        Message message = getEMessage();
        if (message != null)
        {
          QName qName = message.getQName();
          niceSetAttributeURIValue(theElement, SOAPConstants.MESSAGE_ATTRIBUTE, qName.getNamespaceURI() + "#" + qName.getLocalPart());
        }
      }

      if (eReference == null || eReference == SOAPPackage.Literals.SOAP_HEADER_BASE__EPART)
      {
        Part part = getEPart();
        if (part != null)
        {
          String partName = part.getName();
          niceSetAttribute(theElement, SOAPConstants.PART_ATTRIBUTE, partName);
        }
      }
    }
  }
} //SOAPHeaderBaseImpl
