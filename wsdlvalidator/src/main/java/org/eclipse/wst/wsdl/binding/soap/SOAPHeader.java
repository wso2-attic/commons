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
package org.eclipse.wst.wsdl.binding.soap;


import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Header</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeader#getHeaderFaults <em>Header Faults</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeader()
 * @model superTypes="org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase org.eclipse.wst.wsdl.binding.soap.ISOAPHeader"
 * @generated
 */
public interface SOAPHeader extends SOAPHeaderBase, javax.wsdl.extensions.soap.SOAPHeader
{
  /**
   * Returns the value of the '<em><b>Header Faults</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Header Faults</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Header Faults</em>' containment reference list.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeader_HeaderFaults()
   * @model type="org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault" containment="true"
   * @generated
   */
  EList getHeaderFaults();

} // SOAPHeader
