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
package org.eclipse.wst.wsi.internal.core.profile.impl;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.profile.Profile;
import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;

/**
 * This class ...
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ProfileAssertionsImpl implements ProfileAssertions
{
  /**
   * Location of the profile definition.
   */
  protected String documentURI;

  /**
   * Name of the profile test assertion document.
   */
  protected String documentName;

  /**
   * Version of the profile test assertion document.
   */
  protected String documentVersion;

  // ADD: A profile definition should also have a description.

  /**
   * List of profiles.
   */
  Vector profileList = new Vector();

  /**
   * List of artifacts.
   */
  TreeMap artifactList = new TreeMap();

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#getProfileList()
   */
  public Vector getProfileList()
  {
    return this.profileList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#setProfileList(Vector)
   */
  public void setProfileList(Vector profileList)
  {
    this.profileList = profileList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#addProfile(Profile)
   */
  public void addProfile(Profile profile)
  {
    this.profileList.add(profile);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#getArtifactList()
   */
  public TreeMap getArtifactList()
  {
    return this.artifactList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#getArtifact(java.lang.String)
   */
  public ProfileArtifact getArtifact(String type)
  {
    return (ProfileArtifact) this.artifactList.get(type);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#setArtifactList(TreeMap)
   */
  public void setArtifactList(TreeMap artifactList)
  {
    this.artifactList = artifactList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#addArtifact(ReportArtifact)
   */
  public void addArtifact(ProfileArtifact artifact)
  {
    this.artifactList.put(artifact.getType(), artifact);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#getTestAssertion(String)
   */
  public TestAssertion getTestAssertion(String id)
  {
    TestAssertion testAssertion = null;

    // Go through the list of artifacts and find the test assertion 
    Iterator iterator = artifactList.values().iterator();
    while ((iterator.hasNext()) && (testAssertion == null))
    {
      testAssertion = ((ProfileArtifact) iterator.next()).getTestAssertion(id);
    }

    return testAssertion;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ProfileAssertions#createArtifact()
   */
  public ProfileArtifact createArtifact()
  {
    return new ProfileArtifactImpl();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.WSIDocument#getLocation()
   */
  public String getLocation()
  {
    return this.documentURI;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.WSIDocument#setLocation(String)
   */
  public void setLocation(String documentURI)
  {
    this.documentURI = documentURI;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions#getTADName()
   */
  public String getTADName()
  {
    return this.documentName;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions#setTADName(String)
   */
  public void setTADName(String documentName)
  {
    this.documentName = documentName;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions#getTADVersion()
   */
  public String getTADVersion()
  {
    return this.documentVersion;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.profile.ProfileAssertions#setTADVersion(String)
   */
  public void setTADVersion(String documentVersion)
  {
    this.documentVersion = documentVersion;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    return null;
  }
}
