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

import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Message;
import javax.wsdl.Part;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionPassException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.OperationSignature;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * BP1008.
 * Message has part accessor elements for parameters and return value, in no namespaces, 
 * but the children of these are namespace qualified with the same targetNamespace with which 
 * their types are defined.
 */
public class BP1008 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1008(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
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

      if (this.validator.isOneWayResponse(entryContext))
        throw new AssertionFailException(
          AssertionResult.RESULT_NOT_APPLICABLE);

      // Parse message
      Document doc = entryContext.getMessageEntryDocument();

      // If there is no message, then throw fail exception
      if (doc == null)
        throw new AssertionFailException();

      if (this.validator.isFault(doc))
      {
        throw new AssertionFailException(
          AssertionResult.RESULT_NOT_APPLICABLE);
      }

      // Check if there is a soap body element
      if (!this.validator.containsSoapBodyWithChild(doc))
      {
        throw new AssertionNotApplicableException();
      }

      // Parse request message
      Document docRequest = entryContext.getRequestDocument();

      // get SOAPAction
      String action =
        validator.getSoapAction(entryContext.getRequest().getHTTPHeaders());

      Binding binding = validator.analyzerContext.getCandidateInfo().getBindings()[0];
      //Definition definition = entryContext.getAnalyzerContext().getCandidateInfo().getDefinitions()[0];
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
        throw new AssertionNotApplicableException();

      BindingOperation bindingOperation = match.getOperation();

      // Is rpc style?
      if (!WSIConstants
        .ATTRVAL_SOAP_BIND_STYLE_RPC
        .equals(match.getOperationStyle()))
        throw new AssertionFailException(
          AssertionResult.RESULT_NOT_APPLICABLE);

      // Gets body
      NodeList soapBodyList =
        doc.getElementsByTagNameNS(
          WSIConstants.NS_URI_SOAP,
          XMLUtils.SOAP_ELEM_BODY);
      if (soapBodyList.getLength() == 0 || soapBodyList.getLength() > 1)
      {
        // There is not a body or more than one bodies in the envlope.
        throw new AssertionFailException();
      }
      Element soapBodyElem = (Element) soapBodyList.item(0);

      result = null;

      // Find operation in the body
      Element soapOperation = XMLUtils.getFirstChild(soapBodyElem);
      if (soapOperation == null)
      {
        throw new AssertionFailException();
      }

      // Find operation message and ext. elements in the binding
      Message operationMessage = null;
      List extElements = null;
      if (MessageEntry
        .TYPE_REQUEST
        .equals(entryContext.getMessageEntry().getType()))
      {
        operationMessage =
          bindingOperation.getOperation().getInput().getMessage();
        if (bindingOperation.getBindingInput() != null)
          extElements =
            bindingOperation.getBindingInput().getExtensibilityElements();
      }
      else
      {
        if (MessageEntry
          .TYPE_RESPONSE
          .equals(entryContext.getMessageEntry().getType()))
        {
          operationMessage =
            bindingOperation.getOperation().getOutput().getMessage();
          if (bindingOperation.getBindingOutput() != null)
            extElements =
              bindingOperation.getBindingOutput().getExtensibilityElements();
        }
      }

      // Is message RPC-literal?
      if (!validator.isLiteral(extElements))
        throw new AssertionFailException(
          AssertionResult.RESULT_NOT_APPLICABLE);

      // gets first child of message
      Element soapMessagePart = XMLUtils.getFirstChild(soapOperation);
      if (soapMessagePart == null)
      {
        throw new AssertionPassException();
      }

      while (soapMessagePart != null)
      {
        // check whether part accessor or return are in no namespace		    		

        if (soapMessagePart.getNamespaceURI() != null
          || "".equals(soapMessagePart.getNamespaceURI()))
        {
          throw new AssertionFailException();
        }

        //    check whether children are namespace qualified 
        //  with the same targetNamespace with which their types are defined
        String typesTargetNS =
          getTypeNS(operationMessage, soapMessagePart.getLocalName());

        if (typesTargetNS == null)
          throw new AssertionFailException();

        if (!checkChildrenNamespace(soapMessagePart, typesTargetNS))
        {
          throw new AssertionFailException();
        }

        soapMessagePart = XMLUtils.getNextSibling(soapMessagePart);
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
   * Gets part type NS.
   * @param message
   * @param partName
   * @return String
   */
  private String getTypeNS(Message message, String partName)
  {
    Part part = message.getPart(partName);
    if (part == null)
      return null;
    else
      return part.getTypeName().getNamespaceURI();
  }

  /**
   * Checks children elements' namespace.
   * @param node
   * @param typesTargetNS
   * @return boolean
   */

  private boolean checkChildrenNamespace(Element node, String typesTargetNS)
  {
    NodeList parts = node.getElementsByTagName("*");
    for (int i = 0; i < parts.getLength(); i++)
    {
      Element part = (Element) parts.item(i);
      if (!typesTargetNS.equals(part.getNamespaceURI()))
      {
        return false;
      }
      if (!checkChildrenNamespace(part, typesTargetNS))
      {
        return false;
      }
    }
    return true;
  }

}