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

import java.util.Vector;

/**
 * A test assertion is one assertion in a profile definition.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface TestAssertion
{
  public static final String TYPE_INFORMATIONAL = "informational";
  public static final String TYPE_RECOMMENDED = "recommended";
  public static final String TYPE_REQUIRED = "required";
  /**
   * Get test assertion id.
   * @return test assertion id.
   * @see #setId
   */
  public String getId();

  /**
   * Set test assertion id.
   * @param id test assertion id.
   * @see #getId
   */
  public void setId(String id);

  /**
   * Get test assertion type.
   * @return test assertion type.
   * @see #setType
   */
  public String getType();

  /**
   * Set test assertion type.
   * @param  type test assertion type.
   * @see #getType
   */
  public void setType(String type);

  /**
   * Get test assertion primary entry type name.
   * @return test assertion primary entry type name.
   * @see #setEntryTypeName
   */
  public String getEntryTypeName();

  /**
   * Set test assertion primary entry type name.
   * @param entryTypeName test assertion primary entry type name.
   * @see #getEntryTypeName
   */
  public void setEntryTypeName(String entryTypeName);

  /**
   * Get enabled indicator.
   * @return enabled indicator.
   * @see #setEnabled
   */
  public boolean isEnabled();

  /**
   * Set  enabled indicator.
   * @param enabled enabled indicator.
   * @see #isEnabled
   */
  public void setEnabled(boolean enabled);

  /**
   * Get context.
   * @return context.
   * @see #setContext
   */
  public String getContext();

  /**
   * Set context.
   * @param context context.
   * @see #getContext
   */
  public void setContext(String context);

  /**
   * Get assertion description.
   * @return assertion description.
   * @see #setAssertionDescription
   */
  public String getAssertionDescription();

  /**
   * Set assertion description.
   * @param assertionDescription assertion description.
   * @see #getAssertionDescription
   */
  public void setAssertionDescription(String assertionDescription);

  /**
   * Get failure message.
   * @return failure message.
   * @see #setFailureMessage
   */
  public String getFailureMessage();

  /**
   * Set failure message.
   * @param failureMessage failure message.
   * @see #getFailureMessage
   */
  public void setFailureMessage(String failureMessage);

  /**
   * Get failure detail description.
   * @return failure detail description.
   * @see #setFailureDetailDescription
   */
  public String getFailureDetailDescription();

  /**
   * Set failure detail description.
   * @param failureDetailDescription failure detail description.
   * @see #getFailureDetailDescription
   */
  public void setFailureDetailDescription(String failureDetailDescription);

  /**
   * Get detail description.
   * @return detail description.
   * @see #setDetailDescription
   */
  public String getDetailDescription();

  /**
   * Set detail description.
   * @param detailDescription detail description.
   * @see #getDetailDescription
   */
  public void setDetailDescription(String detailDescription);

  /**
   * Get additional entry type list.
   * @return additional entry type list.
   * @see #setAdditionalEntryTypeList
   */
  public EntryTypeList getAdditionalEntryTypeList();

  /**
   * Set additional entry type list.
   * @param entryTypeList additional entry type list.
   * @see #getAdditionalEntryTypeList
   */
  public void setAdditionalEntryTypeList(EntryTypeList entryTypeList);

  /**
   * Get list of prereq ids.
   * @return list of prereq ids.
   */
  public Vector getPrereqIdList();

  /**
   * Add prereq test assertion id.
   * @param  prereqId prereq test assertion id.
   */
  public void addPrereqId(String prereqId);
}
