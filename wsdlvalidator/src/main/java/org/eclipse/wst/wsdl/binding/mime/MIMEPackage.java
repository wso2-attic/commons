/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.mime;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.wst.wsdl.WSDLPackage;


/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.binding.mime.MIMEFactory
 * @model kind="package"
 * @generated
 */
public interface MIMEPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "mime"; //$NON-NLS-1$

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/wsdl/2003/MIME"; //$NON-NLS-1$

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "mime"; //$NON-NLS-1$

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MIMEPackage eINSTANCE = org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEContentImpl <em>Content</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEContentImpl
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getMIMEContent()
   * @generated
   */
  int MIME_CONTENT = 0;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_CONTENT__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_CONTENT__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_CONTENT__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_CONTENT__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_CONTENT__TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EPart</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_CONTENT__EPART = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Content</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_CONTENT_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPartImpl <em>Part</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPartImpl
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getMIMEPart()
   * @generated
   */
  int MIME_PART = 1;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_PART__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_PART__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_PART__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_PART__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_PART__EEXTENSIBILITY_ELEMENTS = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Part</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_PART_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMultipartRelatedImpl <em>Multipart Related</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMultipartRelatedImpl
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getMIMEMultipartRelated()
   * @generated
   */
  int MIME_MULTIPART_RELATED = 2;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MULTIPART_RELATED__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MULTIPART_RELATED__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MULTIPART_RELATED__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MULTIPART_RELATED__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>EMIME Part</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MULTIPART_RELATED__EMIME_PART = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Multipart Related</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MULTIPART_RELATED_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMimeXmlImpl <em>Mime Xml</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMimeXmlImpl
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getMIMEMimeXml()
   * @generated
   */
  int MIME_MIME_XML = 3;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MIME_XML__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MIME_XML__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MIME_XML__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MIME_XML__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>EPart</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MIME_XML__EPART = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Mime Xml</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MIME_MIME_XML_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.mime.MIMEPart <em>IMIME Part</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.mime.MIMEPart
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getIMIMEPart()
   * @generated
   */
  int IMIME_PART = 4;

  /**
   * The number of structural features of the '<em>IMIME Part</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMIME_PART_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.mime.MIMEMultipartRelated <em>IMIME Multipart Related</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.mime.MIMEMultipartRelated
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getIMIMEMultipartRelated()
   * @generated
   */
  int IMIME_MULTIPART_RELATED = 5;

  /**
   * The number of structural features of the '<em>IMIME Multipart Related</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMIME_MULTIPART_RELATED_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.mime.MIMEContent <em>IMIME Content</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.mime.MIMEContent
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getIMIMEContent()
   * @generated
   */
  int IMIME_CONTENT = 6;

  /**
   * The number of structural features of the '<em>IMIME Content</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMIME_CONTENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.mime.MIMEMimeXml <em>IMIME Mime Xml</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.mime.MIMEMimeXml
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getIMIMEMimeXml()
   * @generated
   */
  int IMIME_MIME_XML = 7;

  /**
   * The number of structural features of the '<em>IMIME Mime Xml</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMIME_MIME_XML_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '<em>List</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.util.List
   * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getList()
   * @generated
   */
  int LIST = 8;

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.mime.MIMEContent <em>Content</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Content</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEContent
   * @generated
   */
  EClass getMIMEContent();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.mime.MIMEContent#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEContent#getType()
   * @see #getMIMEContent()
   * @generated
   */
  EAttribute getMIMEContent_Type();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.binding.mime.MIMEContent#getEPart <em>EPart</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EPart</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEContent#getEPart()
   * @see #getMIMEContent()
   * @generated
   */
  EReference getMIMEContent_EPart();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.mime.MIMEPart <em>Part</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Part</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEPart
   * @generated
   */
  EClass getMIMEPart();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.binding.mime.MIMEPart#getEExtensibilityElements <em>EExtensibility Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EExtensibility Elements</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEPart#getEExtensibilityElements()
   * @see #getMIMEPart()
   * @generated
   */
  EReference getMIMEPart_EExtensibilityElements();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated <em>Multipart Related</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Multipart Related</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated
   * @generated
   */
  EClass getMIMEMultipartRelated();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated#getEMIMEPart <em>EMIME Part</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EMIME Part</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated#getEMIMEPart()
   * @see #getMIMEMultipartRelated()
   * @generated
   */
  EReference getMIMEMultipartRelated_EMIMEPart();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml <em>Mime Xml</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Mime Xml</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml
   * @generated
   */
  EClass getMIMEMimeXml();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml#getEPart <em>EPart</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EPart</em>'.
   * @see org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml#getEPart()
   * @see #getMIMEMimeXml()
   * @generated
   */
  EReference getMIMEMimeXml_EPart();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.mime.MIMEPart <em>IMIME Part</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IMIME Part</em>'.
   * @see javax.wsdl.extensions.mime.MIMEPart
   * @model instanceClass="javax.wsdl.extensions.mime.MIMEPart"
   * @generated
   */
  EClass getIMIMEPart();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.mime.MIMEMultipartRelated <em>IMIME Multipart Related</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IMIME Multipart Related</em>'.
   * @see javax.wsdl.extensions.mime.MIMEMultipartRelated
   * @model instanceClass="javax.wsdl.extensions.mime.MIMEMultipartRelated"
   * @generated
   */
  EClass getIMIMEMultipartRelated();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.mime.MIMEContent <em>IMIME Content</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IMIME Content</em>'.
   * @see javax.wsdl.extensions.mime.MIMEContent
   * @model instanceClass="javax.wsdl.extensions.mime.MIMEContent"
   * @generated
   */
  EClass getIMIMEContent();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.mime.MIMEMimeXml <em>IMIME Mime Xml</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>IMIME Mime Xml</em>'.
   * @see javax.wsdl.extensions.mime.MIMEMimeXml
   * @model instanceClass="javax.wsdl.extensions.mime.MIMEMimeXml"
   * @generated
   */
  EClass getIMIMEMimeXml();

  /**
   * Returns the meta object for data type '{@link java.util.List <em>List</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>List</em>'.
   * @see java.util.List
   * @model instanceClass="java.util.List"
   * @generated
   */
  EDataType getList();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  MIMEFactory getMIMEFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEContentImpl <em>Content</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEContentImpl
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getMIMEContent()
     * @generated
     */
    EClass MIME_CONTENT = eINSTANCE.getMIMEContent();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MIME_CONTENT__TYPE = eINSTANCE.getMIMEContent_Type();

    /**
     * The meta object literal for the '<em><b>EPart</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MIME_CONTENT__EPART = eINSTANCE.getMIMEContent_EPart();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPartImpl <em>Part</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPartImpl
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getMIMEPart()
     * @generated
     */
    EClass MIME_PART = eINSTANCE.getMIMEPart();

    /**
     * The meta object literal for the '<em><b>EExtensibility Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MIME_PART__EEXTENSIBILITY_ELEMENTS = eINSTANCE.getMIMEPart_EExtensibilityElements();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMultipartRelatedImpl <em>Multipart Related</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMultipartRelatedImpl
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getMIMEMultipartRelated()
     * @generated
     */
    EClass MIME_MULTIPART_RELATED = eINSTANCE.getMIMEMultipartRelated();

    /**
     * The meta object literal for the '<em><b>EMIME Part</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MIME_MULTIPART_RELATED__EMIME_PART = eINSTANCE.getMIMEMultipartRelated_EMIMEPart();

    /**
     * The meta object literal for the '{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMimeXmlImpl <em>Mime Xml</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMimeXmlImpl
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getMIMEMimeXml()
     * @generated
     */
    EClass MIME_MIME_XML = eINSTANCE.getMIMEMimeXml();

    /**
     * The meta object literal for the '<em><b>EPart</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MIME_MIME_XML__EPART = eINSTANCE.getMIMEMimeXml_EPart();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.mime.MIMEPart <em>IMIME Part</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.mime.MIMEPart
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getIMIMEPart()
     * @generated
     */
    EClass IMIME_PART = eINSTANCE.getIMIMEPart();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.mime.MIMEMultipartRelated <em>IMIME Multipart Related</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.mime.MIMEMultipartRelated
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getIMIMEMultipartRelated()
     * @generated
     */
    EClass IMIME_MULTIPART_RELATED = eINSTANCE.getIMIMEMultipartRelated();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.mime.MIMEContent <em>IMIME Content</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.mime.MIMEContent
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getIMIMEContent()
     * @generated
     */
    EClass IMIME_CONTENT = eINSTANCE.getIMIMEContent();

    /**
     * The meta object literal for the '{@link javax.wsdl.extensions.mime.MIMEMimeXml <em>IMIME Mime Xml</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see javax.wsdl.extensions.mime.MIMEMimeXml
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getIMIMEMimeXml()
     * @generated
     */
    EClass IMIME_MIME_XML = eINSTANCE.getIMIMEMimeXml();

    /**
     * The meta object literal for the '<em>List</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.List
     * @see org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEPackageImpl#getList()
     * @generated
     */
    EDataType LIST = eINSTANCE.getList();

  }

} //MIMEPackage
