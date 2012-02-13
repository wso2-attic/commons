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
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP5101
 * <context>For a non-multipart/related candidate message in the log file.</context>
 * <assertionDescription>A message must have a "Content-Type" HTTP header field.  The "Content-Type" HTTP header field must have a field-value whose media type is "text/xml".</assertionDescription>
 * 
 * @author lauzond
 */
public class AP5101 extends SSBP5101 {

	/**
	 * @param impl
	 */
	public AP5101(BaseMessageValidator impl) 
	{
		super(impl);
	}

	public AssertionResult validate(
	  TestAssertion testAssertion,
	  EntryContext entryContext)
	  throws WSIException
	{
	  // If there is a SOAP Message with Attachments, the assertion is not applicable
	  if (entryContext.getMessageEntry().isMimeContent())
	  {
	  	return validator.createAssertionResult(testAssertion, AssertionResult.RESULT_NOT_APPLICABLE, failureDetail);
	  }
      else
      {
        return super.validate(testAssertion, entryContext);
	  }
	}
}
