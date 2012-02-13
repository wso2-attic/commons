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

import java.util.TreeMap;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.WSIDocument;

/**
 * This interface maintains profile assertions.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public interface ProfileAssertions extends WSIDocument
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_PROFILE_ASSERTIONS;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_ASSERTIONS, ELEM_NAME);

  /**
   * Get list of profiles.
   * @return list of profiles.
   * @see #setProfileList
   */
  public Vector getProfileList();

  /**
   * Set list of profiles.
   * @param profileList list of profiles.
   * @see #getProfileList
   */
  public void setProfileList(Vector profileList);

  /**
   * Add profile.
   * @param profile profile.
   */
  public void addProfile(Profile profile);

  /**
   * Get list of artifacts.
   * @return list of artifacts.
   * @see #setArtifactList
   */
  public TreeMap getArtifactList();

  /**
   * Get artifact.
   * @param type an artifact type.
   * @return a profile artifact based on the given type.
   */
  public ProfileArtifact getArtifact(String type);

  /**
   * Set list of artifacts.
   * @param artifactList a list pf profile artifiacts.
   * @see #getArtifactList
   */
  public void setArtifactList(TreeMap artifactList);

  /**
   * Add artifact.
   * @param artifact profile artifact.
   */
  public void addArtifact(ProfileArtifact artifact);

  /**
   * Get test assertion.
   * @param id test assertion id.
   * @return test assertion.
   */
  public TestAssertion getTestAssertion(String id);

  /**
   * Create artifact.
   * @return newly created profile artifact.
   */
  public ProfileArtifact createArtifact();

  /**
   * Returns the name of the profile test assertion document.
   * @return the name of the document. 
   */
  public String getTADName();

  /**
   * Set the name of the profile test assertion document.
   * @param documentName the name of the document.
   */
  public void setTADName(String documentName);

  /**
   * Returns the version of the profile test assertion document.
   * @return the version of the document.
   */
  public String getTADVersion();

  /**
   * Set the version of the profile test assertion document.
   * @param documentVersion the version of the document.
   */
  public void setTADVersion(String documentVersion);
}
