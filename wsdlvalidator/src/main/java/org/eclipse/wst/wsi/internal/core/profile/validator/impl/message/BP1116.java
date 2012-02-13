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

import javax.wsdl.extensions.soap.SOAPOperation;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcessVisitor;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.OperationSignature;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversal;
import org.eclipse.wst.wsi.internal.core.wsdl.traversal.WSDLTraversalContext;
import org.w3c.dom.Document;


/**
 * BP1116.
 * SOAPAction header should contain a quoted string that has same 
 * value as the value of the soapbind:operation/@soapAction attribute, 
 * and an empty string ("") if there is no such attribute.
 */
public class BP1116 extends AssertionProcessVisitor
{

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1116(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  private String action = null;
  private String headers = null;

  /* (non-Javadoc)
   * @see org.wsi.wsdl.traversal.WSDLVisitor#visit(javax.wsdl.extensions.soap.SOAPOperation, java.lang.Object, org.wsi.wsdl.traversal.WSDLTraversalContext)
   */
  public void visit(
    SOAPOperation soapOper,
    Object parent,
    WSDLTraversalContext ctx)
  {
    String soapAction = soapOper.getSoapActionURI();
    if (soapAction == null)
    {
      if (!"".equals(action))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetailMessage =
          "\nHTTP headers:\n" + headers + "\nsoapAction:\n" + soapAction;
      }
    }
    else
    {
      if (!soapAction.equals(action))
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetailMessage =
          "\nHTTP headers:\n" + headers + "\nsoapAction:\n" + soapAction;
      }
    }
  }

  /* (non-Javadoc)
   * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      // Parse request message
      Document doc = entryContext.getMessageEntryDocument();

      // get SOAPAction
      headers = entryContext.getRequest().getHTTPHeaders();
      if (headers != null)
        action = (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("SOAPAction".toUpperCase());

      if (action == null) 
        throw new AssertionNotApplicableException();

      if (action.length() > 1
        && action.charAt(0) == '"'
        && action.charAt(action.length() - 1) == '"')
      {
        action = action.substring(1, action.length() - 1);
      }

      OperationSignature.OperationMatch match =
        OperationSignature.matchOperation(
          doc,
          null,
          validator.analyzerContext.getCandidateInfo().getBindings()[0],
          new TypesRegistry(
              validator.analyzerContext
              .getCandidateInfo()
              .getWsdlDocument()
              .getDefinitions(),
              validator),
          false);

      if (match != null)
      {
        WSDLTraversal traversal = new WSDLTraversal();
        //VisitorAdaptor.adapt(this);
        traversal.setVisitor(this);
        traversal.visitSOAPOperation(true);
        traversal.traverse(match.getOperation());
      }
      else
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
    }
    catch (Exception e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    if (result == AssertionResult.RESULT_FAILED
      && failureDetailMessage != null)
    {
      failureDetail = this.validator.createFailureDetail(failureDetailMessage, entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}