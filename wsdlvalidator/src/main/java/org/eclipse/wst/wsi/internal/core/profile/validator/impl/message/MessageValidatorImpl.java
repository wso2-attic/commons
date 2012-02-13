/*******************************************************************************
 * Copyright (c) 2002-2003 IBM Corporation, Parasoft and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *   IBM      - Initial API and implementation
 *   Parasoft - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.message;

import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;

/**
 * @version 1.0
 */
public class MessageValidatorImpl
  extends BaseMessageValidator
  implements MessageValidator
{
  /**
   * Get the artifact type that this validator applies to.
   * @return the artifact type (a String)
   */
  public String getArtifactType() {
      return TYPE_MESSAGE;
  }

  /**
   * Get the collection of entry types that this validator applies to.
   * @return an array of entry types (Strings)
   */
  public String[] getEntryTypes() {
      return new String[] {
              TYPE_MESSAGE_REQUEST,
              TYPE_MESSAGE_RESPONSE,
              TYPE_MESSAGE_ANY,
              TYPE_MIME_PART,
              TYPE_MIME_ROOT_PART
      };
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl#isPrimaryEntryTypeMatch(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  protected boolean isPrimaryEntryTypeMatch(
    TestAssertion testAssertion,
    EntryContext entryContext)
  {
    boolean match = false;

    // Verify that the entry and test assertion have the same primary context
    if ((testAssertion.getEntryTypeName().equals(TYPE_MESSAGE_ANY))
      || (testAssertion.getEntryTypeName().equals(TYPE_MESSAGE_REQUEST)
        && (entryContext
          .getEntry()
          .getEntryType()
          .getTypeName()
          .equals(TYPE_MESSAGE_REQUEST)))
      || (testAssertion.getEntryTypeName().equals(TYPE_MESSAGE_RESPONSE)
        && (entryContext
          .getEntry()
          .getEntryType()
          .getTypeName()
          .equals(TYPE_MESSAGE_RESPONSE)))
      || testAssertion.getEntryTypeName().equals(TYPE_MIME_ROOT_PART)
      || testAssertion.getEntryTypeName().equals(TYPE_MIME_PART))
    {
      match = true;
    }

    return match;
  }
}