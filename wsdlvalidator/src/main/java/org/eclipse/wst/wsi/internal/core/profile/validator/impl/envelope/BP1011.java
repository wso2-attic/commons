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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.envelope;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionPassException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * BP1011.
 */
public class BP1011 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1011(BaseMessageValidator impl)
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
      // TODO need to handle soap with attachments
      if (entryContext.getMessageEntry().isMimeContent())
      	 throw new AssertionNotApplicableException();
         
      Binding[] bindings = validator.analyzerContext.getCandidateInfo().getBindings();

      // get soap message child name
      // Get the DOM contents of the message soap:body (if non-empty)
      Document messageDoc = null;

      QName messagePartElementQName = null;

      if (!entryContext.getMessageEntry().getMessage().equals(""))
      {
        messageDoc = entryContext.getMessageEntryDocument();
        //messageDoc = XMLUtils.parseXML(entryContext.getMessageEntry().getMessage(), 
        //	WSIProperties.DEF_SOAP_SCHEMA);
        // get soap operation name from the soap:body

        // NOTE: getSoapBodyChild() may return null, if the soap:body did not contain any child elements
        Element soapMessageElement = validator.getSoapBodyChild(messageDoc);

        // for doc-lit... find the wsdl:part from one of the specified operations that has 
        // the soap element used in the message.      
        // we are looking for a part with an element attrib val matching the soap message element
        if (soapMessageElement != null)
        {
          messagePartElementQName =
            new QName(
              soapMessageElement.getNamespaceURI(),
              soapMessageElement.getLocalName());
        }
      }

      if ((messageDoc == null) || this.validator.isFault(messageDoc))
      {
        // empty messages and fault messages do not qualify for this TA
        throw new AssertionNotApplicableException();
      }

      // look for match(s) in doc operations
      // if found, we're done, else
      // look for match(s) in rpc operations

      /* Doc-lit 
       * get list of operations (from candidates) for which the first soap:body child QName is the
       * <part element="..."> value of that operation's <wsdl:input> or <wsdl:output> element (depending
       * on whether message is a request or a response).
       */

      BindingOperation[] docBindingOperations =
        validator.getMatchingBindingOps(
          WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC,
          bindings);
      // list of operations that match the <wsdl:input> or <wsdl:output> (depending on entryType)
      // for part element="..."
      // (ISSUE: what about part type="..."?)
      BindingOperation[] potentialDocLitOps =
        validator.getDocLitOperations(
          entryContext.getEntry().getEntryType(),
          messagePartElementQName,
          docBindingOperations);
      if (potentialDocLitOps.length != 0)
      {
        // found at least one doc lit match

        // *** Now we know it's document-literal, schema-validate the SOAP payload accordingly            		
        try
        {
          this.validator.messageIsDocLitSchemaValid(entryContext);
        }
        catch (Exception e)
        {
          throw new AssertionFailException(e.getMessage());
        }
        throw new AssertionPassException();
      }

      /* soap message is not doc-lit so try :
       * Rpc-lit
       */
      BindingOperation[] rpcBindingOperations =
        this.validator.getMatchingBindingOps(
          WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC,
          bindings);

      // Determine if there is an operation match
      BindingOperation rcpOpMatch =
        this.validator.getOperationMatch(
          entryContext.getEntry().getEntryType(),
          messageDoc,
          rpcBindingOperations);
      if (rcpOpMatch != null)
      {
        // Determine if the parts match
        rcpOpMatch =
          this.validator.getOperationPartsMatch(
            entryContext.getEntry().getEntryType(),
            messageDoc,
            rpcBindingOperations);
      }

      if (rcpOpMatch == null)
      {
        //not matched with rpc either. Prepare assertion failure.
        StringBuffer rpcOperationList = new StringBuffer();
        for (int i = 0; i < rpcBindingOperations.length; i++)
        {
          rpcOperationList.append(rpcBindingOperations[i].toString() + "\n");
        }

        StringBuffer docOperationList = new StringBuffer();
        for (int i = 0; i < docBindingOperations.length; i++)
        {
          docOperationList.append(docBindingOperations[i].toString() + "\n");
        }

        throw new AssertionFailException(
          "--MESSAGE:\n"
            + entryContext.getMessageEntry().getMessage()
            + "\n--CANDIDATE RPC STYLE OPERATIONS ("
            + (rpcBindingOperations.length != 0
              ? (rpcBindingOperations.length + "):\n" + rpcOperationList)
              : "NONE)")
            + "\n--CANDIDATE DOCUMENT STYLE OPERATIONS ("
            + (docBindingOperations.length != 0
              ? (docBindingOperations.length + "):\n" + docOperationList)
              : "NONE)"));

      }
    }
    catch (AssertionPassException e)
    {
    }
    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(e.getMessage(), entryContext);
    }
    catch (AssertionNotApplicableException e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Return assertion result

    return validator.createAssertionResult(testAssertion, result, failureDetail);

  }
}