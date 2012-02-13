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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.OperationType;
import javax.wsdl.Part;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionPassException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
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
 * BP1009.
 * Message includes all soapbind:headers specified in the wsdl:input 
 * (if request) or wsdl:output (if response) of the operation referred 
 * to by its wsdl:binding, and may also include headers that were not specified.
 */
public class BP1009 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1009(BaseMessageValidator impl)
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
    try
    {
      if (this.validator.isOneWayResponse(entryContext))
      {
        throw new AssertionNotApplicableException();
      }

      // Parse message
      Document doc = entryContext.getMessageEntryDocument();

      // Parse request message
      Document docRequest = entryContext.getRequestDocument();

      if (doc == null || docRequest == null)
        throw new AssertionNotApplicableException();

      // Check if there is a soap body element
      if (!this.validator.containsSoapBodyWithChild(doc))
      {
        throw new AssertionNotApplicableException();
      }

      if (this.validator.isFault(doc))
      {
        throw new AssertionFailException(
          AssertionResult.RESULT_NOT_APPLICABLE);
      }

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
        throw new AssertionNotApplicableException();
      }

      BindingOperation bindingOperation = match.getOperation();

      // If this is a one-way operation and we are processing a response, then set result to notApplicable
      if (bindingOperation
        .getOperation()
        .getStyle()
        .equals(OperationType.ONE_WAY)
        && (entryContext
          .getMessageEntry()
          .getType()
          .equals(MessageEntry.TYPE_RESPONSE)))
      {
        throw new AssertionNotApplicableException();
      }

      // find body
      NodeList soapBodyList =
        doc.getElementsByTagNameNS(
          WSIConstants.NS_URI_SOAP,
          XMLUtils.SOAP_ELEM_BODY);
      if (soapBodyList.getLength() == 0 || soapBodyList.getLength() > 1)
      {
        // There is not a body or more than one bodies in the envlope.
        throw new AssertionPassException();
      }
      // find headers
      NodeList soapHeaders =
        doc.getElementsByTagNameNS(
          WSITag.ELEM_SOAP_HEADER.getNamespaceURI(),
          WSITag.ELEM_SOAP_HEADER.getLocalPart());

      //find all operation
      //Element soapOperation = XMLUtils.getFirstChild(soapBodyElem);

      // gets soapbind:headers
      List bindingHeaders = null;
      if (entryContext
        .getMessageEntry()
        .getType()
        .equals(MessageEntry.TYPE_REQUEST))
      {
        if (bindingOperation.getBindingInput() != null)
          bindingHeaders =
            bindingOperation.getBindingInput().getExtensibilityElements();
        else
          throw new AssertionFailException();

      }
      else
      {
        if (entryContext
          .getMessageEntry()
          .getType()
          .equals(MessageEntry.TYPE_RESPONSE))
        {
          if (bindingOperation.getBindingOutput() != null)
            bindingHeaders =
              bindingOperation.getBindingOutput().getExtensibilityElements();
          else
            throw new AssertionFailException();
        }
      }
      if (bindingHeaders == null || bindingHeaders.size() == 0)
      {
        throw new AssertionPassException();
      }

      Iterator iterator = bindingHeaders.iterator();
      while (iterator.hasNext())
      {
        Object bindingHeader = iterator.next();
        if (bindingHeader instanceof SOAPHeader)
        {
          // find soapbind:header in SOAP message
          if (!isBindingHeaderInSOAPMessage(entryContext,
            soapHeaders,
            (SOAPHeader) bindingHeader))
          {
            throw new AssertionFailException();
          }
        }
      }

      result = AssertionResult.RESULT_PASSED;

    }
    catch (AssertionFailException e)
    {
      if (e.getMessage() != null && e.getMessage().length() > 0)
        result = e.getMessage();
      else
        result = AssertionResult.RESULT_FAILED;
    }
    catch (AssertionPassException e)
    {
      result = AssertionResult.RESULT_PASSED;
    }
    catch (Exception e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    if (result == AssertionResult.RESULT_FAILED)
    {
      failureDetail = this.validator.createFailureDetail(null, entryContext);
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /**
  * Checks whether specified SOAPHeader is in the SOAPMessage.
  * @param soapHeaderList
  * @param SOAPHeader
  * @return boolean
  */
  private boolean isBindingHeaderInSOAPMessage(
    EntryContext entryContext,
    NodeList soapHeaders,
    SOAPHeader bindingHeader)
  {

    Message msg = getMessageByQName(entryContext, bindingHeader.getMessage());
    if (msg == null)
      return false;

    Part part = msg.getPart(bindingHeader.getPart());
    if (part == null)
      return false;

    QName partElementName = part.getElementName();
    QName partTypeName = part.getTypeName();

    if (partTypeName == null && partElementName == null)
      return false;

    if (soapHeaders == null || soapHeaders.getLength() == 0)
    {
      return false;
    }
    Vector headersList =
      XMLUtils.getChildElements((Element) soapHeaders.item(0));

    for (int indexHeader = 0;
      indexHeader < headersList.size();
      indexHeader++)
    {
      Element soapHeaderPart = (Element) headersList.get(indexHeader);
      if (soapHeaderPart != null)
      {
        QName soapHeaderPartQName =
          new QName(
            soapHeaderPart.getNamespaceURI(),
            soapHeaderPart.getLocalName());
        if ((partTypeName != null
          && partTypeName.equals(soapHeaderPartQName))
          || (partElementName != null
            && partElementName.equals(soapHeaderPartQName)))
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Gets message by QName.
   * @param entryContext
   * @param messageName
   * @return Message
   */
  private Message getMessageByQName(
    EntryContext entryContext,
    QName messageName)
  {
    Definition[] defs = validator.analyzerContext.getCandidateInfo().getDefinitions();
    for (int i = 0; i < defs.length; i++)
    {
      Message msg = defs[i].getMessage(messageName);
      if (msg != null)
        return msg;
    }
    return null;
  }

}