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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;

/**
 * BP4104
 * <context>For a candidate message in the message log file containing an HTTP Header field that is not from the following list of specified header fields: (http://www.mnot.net/drafts/draft-nottingham-http-header-reg-00.txt)</context>
 * <assertionDescription>The message contains an HTTP Header field that is not from the following list of specified header fields: (http://www.mnot.net/drafts/draft-nottingham-http-header-reg-00.txt)</assertionDescription>
 */
public class BP4104 extends AssertionProcess {

  private final BaseMessageValidator validator;

  private List knownHeaderNames;

  /**
   * @param BaseMessageValidator
   */
  public BP4104(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
    knownHeaderNames = null;
  }

  private List getAllKnownHeaderNames() {
    if (knownHeaderNames == null) {
      knownHeaderNames = HTTPConstants.getAllKnownHeaderNames();
    }
    return knownHeaderNames;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    // getting HTTP Headers
    Map hmap = HTTPUtils.getHttpHeaderTokens(
      entryContext.getMessageEntry().getHTTPHeaders(), ":");

    Iterator i = hmap.keySet().iterator() ;
    while (i.hasNext()) {
      String headerName = (String) i.next();
      // If header name is not known, then the assertion passed
      if (!getAllKnownHeaderNames().contains(headerName.toUpperCase()))
      {
        failureDetail = validator.createFailureDetail(
          testAssertion.getDetailDescription(), entryContext);

        break;
      }
    }

    // If there is no HTTP header that is not from a list,
    // then the assertion is not applicable 
    if (failureDetail == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}