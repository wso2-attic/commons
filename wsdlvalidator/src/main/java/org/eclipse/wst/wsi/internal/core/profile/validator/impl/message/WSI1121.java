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
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * BPWSI4.
 */
public class WSI1121 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public WSI1121(BaseMessageValidator impl)
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
      // GT: -- start: base on wsi1011 - should refactor this

      Binding[] bindings = validator.analyzerContext.getCandidateInfo().getBindings();

      // get soap message child name
      // Get the DOM contents of the message soap:body (if non-empty)
      Document messageDoc = null;

      QName messagePartElementQName = null;
      if (!entryContext.getMessageEntry().getMessage().equals(""))
      {
        messageDoc =
          XMLUtils.parseXML(entryContext.getMessageEntry().getMessage());
        // get soap operation name from the soap:body

        // NOTE: getSoapBodyChild() may return null, if the soap:body did not contain any child elements
        Element soapMessageElement = this.validator.getSoapBodyChild(messageDoc);

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
        // empty messages and messages containing soap:Faults do not qualify for this TA
        throw new AssertionNotApplicableException();
      }

      BindingOperation[] docBindingOperations =
        this.validator.getMatchingBindingOps(
          WSIConstants.ATTRVAL_SOAP_BIND_STYLE_DOC,
          bindings);
      // list of operations that match the <wsdl:input> or <wsdl:output> (depending on entryType)
      // for part element="..."
      // (ISSUE: what about part type="..."?)
      BindingOperation[] potentialDocLitOps =
        this.validator.getDocLitOperations(
          entryContext.getEntry().getEntryType(),
          messagePartElementQName,
          docBindingOperations);
      // GT: --- end: base on wsi1011

      //               
      if (potentialDocLitOps.length == 0)
      {
        // not doc-lit so quit
        throw new AssertionNotApplicableException();
      }

      // We know there is a doc-lit match, so now try to validate the soap message against the
      // referenced schema

      // GT - how to reference the schema document?? need to use the <wsdl:part>, or do it purely
      // from the soap message elements?
      // ie body, envelope or wsdl??

      // <experiment>

      boolean isSchemaValid;
      
      try {
      		isSchemaValid = this.validator.messageIsDocLitSchemaValid(entryContext);         
      	} catch (Exception e) {
       		throw new AssertionFailException();
      	}
      // </experiment>
      

      //isSchemaValid = true; // for now, getting this far does it

      if (isSchemaValid)
      {
        throw new AssertionPassException();
      }
      else
      {
        throw new AssertionFailException();
      }

    }
    catch (AssertionPassException e)
    {
      result = AssertionResult.RESULT_PASSED;
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