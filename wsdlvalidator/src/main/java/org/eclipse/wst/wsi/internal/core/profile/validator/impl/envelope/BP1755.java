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

import javax.wsdl.BindingOperation;
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
 * BP1755
 *
 * <context>For a candidate non-fault envelope containing a soap:body with at least one element, and that is referred by a binding style RPC-literal</context>
 * <assertionDescription>Each part accessor element in the envelope has a local name of the same value as the name attribute of the corresponding wsdl:part element.</assertionDescription>
 */
public class BP1755 extends AssertionProcess {

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1755(BaseMessageValidator impl)
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

      // Finding operation message parts and extensibility elems
      // in the binding depending on message type
      List operationMessageParts = null;
      List extElems = null;
      if (entryContext.getMessageEntry().getType().equals(
        MessageEntry.TYPE_REQUEST))
      {
        operationMessageParts = bindingOperation.getOperation()
          .getInput().getMessage().getOrderedParts(null);
        if (bindingOperation.getBindingInput() != null)
          extElems =
            bindingOperation.getBindingInput().getExtensibilityElements();
      }
      else
      {
        operationMessageParts = bindingOperation.getOperation()
          .getOutput().getMessage().getOrderedParts(null);
        if (bindingOperation.getBindingOutput() != null)
          extElems =
            bindingOperation.getBindingOutput().getExtensibilityElements();
      }

      // If the message is not literal, the assertion is not applicable
      if (!validator.isLiteral(extElems))
        throw new AssertionNotApplicableException();


      /* Basic Profile Version 1.1 (http://www.ws-i.org/Profiles/Basic/2003-12/BasicProfile-1.1.htm)
       * says that the order of the elements in the soap:body of an ENVELOPE MUST be
       * the same as that of the wsdl:parts in the wsdl:message that describes it.
       * However, we should keep in mind there is the "parts" attribute of soapbind:body
       * that indicates which parts appear within the SOAP Body. 
       */

      // Getting the ordered list of wsdl:part names
      List orderedPartNames =
        validator.orderPartNames(operationMessageParts, extElems);
      Iterator i = orderedPartNames.iterator();
      // Getting the first accessor
      Element accessor = XMLUtils.getFirstChild(soapOperation);
      while (accessor != null)
      {
        // If there is no the corresponding wsdl:part element
        // for an accessor, the assertion failed
        if (!i.hasNext())
          throw new AssertionFailException(
            "The part accessor element '" + accessor.getLocalName()
            + "' does not have the corresponding wsdl:part element.");

        // If local name of accessor does not equal to the name of the
        // corresponding wsdl:part element, the assertion failed
        String partName = (String) i.next();
        if (!accessor.getLocalName().equals(partName))
          throw new AssertionFailException(
            "The accessor local name is " + accessor.getLocalName()
            + ", the corresponding part element name is " + partName);

        // Getting the next accessor
        accessor = XMLUtils.getNextSibling(accessor);
      }
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