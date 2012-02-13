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
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;


/**
 * SSBP5100
 * <context>For a candidate message in the log file, with an HTTP entity-body.</context>
 * <assertionDescription>The SOAP envelope is the exclusive payload of the HTTP entity-body.</assertionDescription>
 */
public class SSBP5100 extends AssertionProcess {

  protected final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public SSBP5100(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    try
    {
      // Getting a message document
      Document doc = entryContext.getMessageEntryDocument();
      
      // If the message is empty or invalid, the assertion is not applicable
      if (doc == null)
      { 
      	if (this.validator.isOneWayResponse(entryContext))
      		 throw new AssertionNotApplicableException();
      	else	
             throw new AssertionFailException();
      }
      if (!doc.getDocumentElement().getLocalName().equals(XMLUtils.SOAP_ELEM_ENVELOPE))
        throw new AssertionFailException();
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
          testAssertion.getFailureMessage(),
          entryContext);
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}