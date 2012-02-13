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

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionPassException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.OperationSignature;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * BPWSI4.
 * The response (SOAP envelope) should be carried by an HTTP 
 * response, over same HTTP connection as the Request. 
 */
public class WSI1311 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public WSI1311(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(TestAssertion, EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    result = AssertionResult.RESULT_PASSED;
    boolean isOneWayResponse = this.validator.isOneWayResponse(entryContext);

    Document docResponse = null;
    if (!isOneWayResponse)
      docResponse = entryContext.getResponseDocument();

    //if fault
    if (!isOneWayResponse && this.validator.isFault(docResponse))
    {
      result = AssertionResult.RESULT_PASSED;
    }
    else
    { // not fault
      // 1. get an operation from request
      // Parse request message
      Document docRequest = entryContext.getRequestDocument();

      // get SOAPAction
      String headers = entryContext.getRequest().getHTTPHeaders();
      String action = null;
      if (headers != null)
        action = (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("SOAPAction".toUpperCase());
      Binding binding = validator.analyzerContext.getCandidateInfo().getBindings()[0];
      TypesRegistry registry =
        new TypesRegistry(
          this.validator.getWSDLDocument().getDefinitions(),
          validator);
      OperationSignature.OperationMatch match =
        OperationSignature.matchOperation(
          docRequest,
          action,
          binding,
          registry);

      if (match == null)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        try
        {

          BindingOperation bindingOperation = match.getOperation();
          Operation operation = null;

          //    Check whether the transport attribute has the value 
          //  "http://schemas.xmlsoap.org/soap/http". 
          List extElem = binding.getExtensibilityElements();
          for (Iterator index = extElem.iterator(); index.hasNext();)
          {
            Object o = (Object) index.next();
            if (o instanceof SOAPBinding)
            {
              SOAPBinding soapBinding = (SOAPBinding) o;
              if (!WSIConstants
                .NS_URI_SOAP_HTTP
                .equals(soapBinding.getTransportURI()))
              {
                throw new AssertionFailException();
              }

            }
          }

          //find operation in port type
          operation = bindingOperation.getOperation();
          if (operation == null)
          {
            throw new AssertionFailException();
          }

          if (isOneWayResponse)
          {
            if (operation.getOutput() == null)
              throw new AssertionPassException();
            else
              throw new AssertionFailException();
          }

          //    check whether the response message from the log 
          //  contains the output message
          NodeList soapBodyList =
            docResponse.getElementsByTagNameNS(
              WSIConstants.NS_URI_SOAP,
              XMLUtils.SOAP_ELEM_BODY);
          if ((soapBodyList == null) || (soapBodyList.getLength() == 0))
          {
            // Response does not contain any soap Body elements
            throw new AssertionFailException();
          }
          for (int i = 0; i < soapBodyList.getLength(); i++)
          {
            Element nextBodyElem = (Element) soapBodyList.item(i);
            Element soapMessageElement = XMLUtils.getFirstChild(nextBodyElem);
            while (soapMessageElement != null)
            {
              // check whether the operation output has message from SOAP response
              Message message = operation.getOutput().getMessage();
              QName soapMessageQName =
                new QName(
                  soapMessageElement.getNamespaceURI(),
                  soapMessageElement.getLocalName());

              if (message != null
                && soapMessageQName.equals(message.getQName()))
              {
                throw new AssertionPassException();
              }

              soapMessageElement =
                XMLUtils.getNextSibling(soapMessageElement);
            }
          }

        }
        catch (AssertionPassException e)
        {
          result = AssertionResult.RESULT_PASSED;
        }
        catch (AssertionFailException e)
        {
          result = AssertionResult.RESULT_FAILED;
        }

      }
    }

    if (result == AssertionResult.RESULT_FAILED)
    {
      failureDetail = this.validator.createFailureDetail(null, entryContext);
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}