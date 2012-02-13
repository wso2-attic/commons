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

import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.profile.EntryTypeList;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;

/**
 * This class ...
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 */
public class TestAssertionImpl implements TestAssertion
{
  /**
   * Test assertion id.
   */
  protected String id = null;

  /**
   * Test assertion type.
   */
  protected String type = null;

  /**
   * Test assertion primary entry type name.
   */
  protected String entryTypeName = null;

  /**
   * Test assertion enabled indicator.
   */
  protected boolean enabled = true;

  /**
   * Context.
   */
  protected String context = null;

  /**
   * Assertion description.
   */
  protected String assertionDescription = null;

  /**
   * Failure message.
   */
  protected String failureMessage = null;

  /**
   * Failure detail description.
   */
  protected String failureDetailDescription = null;

  /**
   * Detail description.
   */
  protected String detailDescription = null;

  /**
   * Addtional entry type list.
   */
  protected EntryTypeList entryTypeList = null;

  /**
   * Prereq ID list.
   */
  protected Vector prereqIdList = new Vector();

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getId()
   */
  public String getId()
  {
    return this.id;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setId(String)
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getType()
   */
  public String getType()
  {
    return this.type;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setType(String)
   */
  public void setType(String type)
  {
    this.type = type;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getEntryTypeName()
   */
  public String getEntryTypeName()
  {
    return this.entryTypeName;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setEntryTypeName(String)
   */
  public void setEntryTypeName(String entryTypeName)
  {
    this.entryTypeName = entryTypeName;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#isEnabled()
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setEnabled(boolean)
   */
  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getContext()
   */
  public String getContext()
  {
    return context;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setContext(String)
   */
  public void setContext(String context)
  {
    this.context = context;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getAssertionDescription()
   */
  public String getAssertionDescription()
  {
    return this.assertionDescription;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setAssertionDescription(String)
   */
  public void setAssertionDescription(String assertionDescription)
  {
    this.assertionDescription = assertionDescription;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getFailureMessage()
   */
  public String getFailureMessage()
  {
    return this.failureMessage;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setFailureMessage(String)
   */
  public void setFailureMessage(String failureMessage)
  {
    this.failureMessage = failureMessage;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getFailureDetailDescription()
   */
  public String getFailureDetailDescription()
  {
    return this.failureDetailDescription;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setFailureDetailDescription(String)
   */
  public void setFailureDetailDescription(String failureDetailDescription)
  {
    this.failureDetailDescription = failureDetailDescription;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getDetailDescription()
   */
  public String getDetailDescription()
  {
    return this.detailDescription;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setDetailDescription(String)
   */
  public void setDetailDescription(String detailDescription)
  {
    this.detailDescription = detailDescription;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getAdditionalEntryTypeList()
   */
  public EntryTypeList getAdditionalEntryTypeList()
  {
    return this.entryTypeList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#setAdditionalEntryTypeList(org.wsi.test.profile.EntryTypeList)
   */
  public void setAdditionalEntryTypeList(EntryTypeList entryTypeList)
  {
    this.entryTypeList = entryTypeList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#getPrereqIdList()
   */
  public Vector getPrereqIdList()
  {
    return prereqIdList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.TestAssertion#addPrereqId(String)
   */
  public void addPrereqId(String prereqId)
  {
    this.prereqIdList.add(prereqId);
  }
}
