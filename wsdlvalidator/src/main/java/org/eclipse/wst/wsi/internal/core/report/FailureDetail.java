/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.report;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;
import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;

/**
 * Failure detail.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public interface FailureDetail extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_FAILURE_DETAIL;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_REPORT, ELEM_NAME);

  /**
   * Get failure detail message.
   * @return failure detail message.
   * @see #setFailureMessage
   */
  public String getFailureMessage();

  /**
   * Set failure detail message.
   * @param failureMessage failure detail message.
   * @see #getFailureMessage
   */
  public void setFailureMessage(String failureMessage);

  /**
   * Get reference type.
   * @return reference type.
   * @see #setReferenceType
   */
  public String getReferenceType();

  /**
   * Set reference type.
   * @param referenceType reference type.
   * @see #getReferenceType
   */
  public void setReferenceType(String referenceType);

  /**
   * Get reference ID.
   * @return reference ID.
   * @see #setReferenceIDn
   */
  public String getReferenceID();

  /**
   * Set reference ID.
   * @param referenceID reference ID.
   * @see #getReferenceID
   */
  public void setReferenceIDn(String referenceID);

  /**
   * Get element location.
   * @return element location.
   * @see #setElementLocation
   */
  public ElementLocation getElementLocation();

  /**
   * Set element location.
   * @param elementLocation element location.
   * @see #getElementLocation
   */
  public void setElementLocation(ElementLocation elementLocation);

}
