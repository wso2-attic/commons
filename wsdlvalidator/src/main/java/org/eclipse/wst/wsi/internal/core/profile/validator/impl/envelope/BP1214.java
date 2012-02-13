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

import java.util.List;

import javax.wsdl.BindingOperation;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * BP1214
 *
 * <context>For a candidate non-fault envelope containing a soap:Body element, and is referred by a rpc-literal binding</context>
 * <assertionDescription>The envelope does not contain any part accessor elements if the value of the parts attribute of the soapbind:body is an empty string in the corresponding rpc-literal description.</assertionDescription>
 */
public class BP1214 extends AssertionProcess {

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1214(BaseMessageValidator impl)
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
      if (validator.isOneWayResponse(entryContext))
        throw new AssertionNotApplicableException();

      // Getting a message document
      Document doc = entryContext.getMessageEntryDocument();

      Element soapOperation = null;
      // If there is a Fault entry or no body entries,
      // the assertion is not applicable
      if (validator.isFault(doc)
        || (soapOperation = validator.getSoapBodyChild(doc)) == null)
        throw new AssertionNotApplicableException();

      // Creating a qualified name of potential SOAP operation
      QName operationQName = new QName(
        soapOperation.getNamespaceURI(), soapOperation.getLocalName());

      // Retrieving all the RPC binding operations from wsdl:binding
      BindingOperation[] rpcBindingOperations =
        validator.getMatchingBindingOps(
          WSIConstants.ATTRVAL_SOAP_BIND_STYLE_RPC,
          validator.analyzerContext.getCandidateInfo().getBindings());

      // Retrieving binding operation by given operation name
      BindingOperation bindingOperation = validator.getOperationMatch(
        entryContext.getEntry().getEntryType(),
        operationQName,
        rpcBindingOperations);

      // If there is no matched operation, the assertion is not applicable
      if (bindingOperation == null)
        throw new AssertionNotApplicableException();

      // Finding operation extensibility elems
      // in the binding depending on message type
      List extElems = null;
      if (entryContext.getMessageEntry().getType().equals(
        MessageEntry.TYPE_REQUEST)
        && bindingOperation.getBindingInput() != null)
      {
        extElems =
          bindingOperation.getBindingInput().getExtensibilityElements();
      }
      else if (entryContext.getMessageEntry().getType().equals(
        MessageEntry.TYPE_RESPONSE)
        && bindingOperation.getBindingOutput() != null)
      {
        extElems =
          bindingOperation.getBindingOutput().getExtensibilityElements();
      }

      // If the message is not literal, the assertion is not applicable
      if (!validator.isLiteral(extElems))
        throw new AssertionNotApplicableException();

      // Getting the parts attribute from soapbind:body
      List parts = null;
      SOAPBody soapBody = validator.getSOAPBody(extElems);
      if (soapBody != null)
        parts = soapBody.getParts();

      // if the parts attribute is an empty string and there is
      // at least one accessor, the assertion failed
      if (parts != null && parts.isEmpty()
        && !XMLUtils.getChildElements(soapOperation).isEmpty())
        throw new AssertionFailException("wsdl:operation name is "
          + bindingOperation.getName());

    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}