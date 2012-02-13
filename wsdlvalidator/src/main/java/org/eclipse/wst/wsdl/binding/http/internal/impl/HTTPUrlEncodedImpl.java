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
package org.eclipse.wst.wsdl.binding.http.internal.impl;


import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.wst.wsdl.binding.http.HTTPPackage;
import org.eclipse.wst.wsdl.binding.http.HTTPUrlEncoded;
import org.eclipse.wst.wsdl.binding.http.internal.util.HTTPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Url Encoded</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class HTTPUrlEncodedImpl extends ExtensibilityElementImpl implements HTTPUrlEncoded
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
  protected HTTPUrlEncodedImpl()
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
    return HTTPPackage.Literals.HTTP_URL_ENCODED;
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(HTTPConstants.HTTP_NAMESPACE_URI, HTTPConstants.URL_ENCODED_ELEMENT_TAG);
    return elementType;
  }

} //HTTPUrlEncodedImpl
