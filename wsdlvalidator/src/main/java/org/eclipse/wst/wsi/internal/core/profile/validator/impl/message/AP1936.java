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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.message;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP1936
 * <context>For a candidate multipart/related message</context>
 * <assertionDescription>In a message, all MIME encapsulation boundary strings 
 * are preceded with the ascii characters CR (13) and LF (10) 
 * in that sequence.</assertionDescription>
 */
public class AP1936 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public AP1936(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // if boundary equals null, then result is not applicable
    if (!entryContext.getMessageEntry().isMimeContent())
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }    
    // otherwise it is true -- checked in monitor
    // MimeParts mimeParts = entryContext.getMessageEntry().getMimeParts();
    // Iterator i = mimeParts.getParts().iterator();
    // boolean invalidBoundaryStringFound = false;
    //while (i.hasNext() && !invalidBoundaryStringFound)
    //{
    //	MimePart part = (MimePart)i.next();
    //	String[] boundaries = part.getBoundaryStrings();
    //	for (int j = 0; j<boundaries.length; j++)
    //	{
    //		if (!boundaries[j].startsWith("&#xd;\n"))
    //		{
    //			invalidBoundaryStringFound = true;
    //			result = AssertionResult.RESULT_FAILED;
    //			failureDetail = validator.createFailureDetail(boundaries[j], entryContext);
    //			break;
    //		}
    //	}
    //}
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}
