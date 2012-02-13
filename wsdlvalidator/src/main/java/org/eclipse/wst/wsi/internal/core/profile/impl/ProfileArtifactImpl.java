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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.profile.ProfileArtifact;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.util.ArtifactType;

/**
 * An artifact contains a set of related test assertions.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class ProfileArtifactImpl implements ProfileArtifact
{
  /**
   * Artifact type.
   */
  protected ArtifactType type;

  /**
   * Test assertion map.
   */
  protected TreeMap testAssertionMap = new TreeMap();

  /**
   * Test assertion list.
   */
  protected LinkedList testAssertionList = new LinkedList();

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ReportArtifact#getType()
   */
  public ArtifactType getType()
  {
    return this.type;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ReportArtifact#setType(ArtifactType)
   */
  public void setType(ArtifactType type)
  {
    this.type = type;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ReportArtifact#getSpecificationList()
   */
  public Vector getSpecificationList()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ReportArtifact#setSpecificationList(Vector)
   */
  public void setSpecificationList(Vector specificationList)
  {
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ReportArtifact#getTestAssertionList()
   */
  public LinkedList getTestAssertionList()
  {
    return this.testAssertionList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ReportArtifact#getTestAssertion(String)
   */
  public TestAssertion getTestAssertion(String id)
  {
    return (TestAssertion) this.testAssertionMap.get(id);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.ReportArtifact#addTestAssertion(TestAssertion)
   */
  public void addTestAssertion(TestAssertion testAssertion)
  {
    // ADD: Need to merge the list and map
    this.testAssertionMap.put(testAssertion.getId(), testAssertion);
    this.testAssertionList.add(testAssertion);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // ReportArtifact
    pw.println("      <" + nsName + ELEM_NAME + ">");

    pw.println("      </" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }

}
