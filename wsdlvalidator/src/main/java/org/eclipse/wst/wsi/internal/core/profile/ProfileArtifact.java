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
package org.eclipse.wst.wsi.internal.core.profile;

import java.util.LinkedList;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;
import org.eclipse.wst.wsi.internal.core.util.ArtifactType;

/**
 * An artifact contains a set of related test assertions.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface ProfileArtifact extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_ARTIFACT;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_ASSERTIONS, ELEM_NAME);

  /**
   * Get type.
   * @return artifact type.
   * @see #setType
   */
  public ArtifactType getType();

  /**
   * Set type.
   * @param type an artifact type.
   * @see #getType
   */
  public void setType(ArtifactType type);

  /**
   * Get specification list.
   * @return specification list.
   * @see #setSpecificationList
   */
  public Vector getSpecificationList();

  /**
   * Set specification list.
   * @param specificationList specification list.
   * @see #getSpecificationList
   */
  public void setSpecificationList(Vector specificationList);

  /**
   * Get test assertion list.
   * @return test assertion list.
   */
  public LinkedList getTestAssertionList();

  /**
   * Get test assertion.
   * @param id test assertion id.
   * @return test assertion.
   */
  public TestAssertion getTestAssertion(String id);

  /**
   * Add test assertion.
   * @param testAssertion test assertion.
   */
  public void addTestAssertion(TestAssertion testAssertion);
}
