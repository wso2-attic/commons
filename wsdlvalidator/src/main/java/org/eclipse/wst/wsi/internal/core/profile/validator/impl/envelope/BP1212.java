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
 * BP1212
 *
 * <context>For a candidate non-fault envelope containing a soap:body with at least one element</context>
 * <assertionDescription>The envelope contains exactly one part accessor element for each of the wsdl:part elements bound to the envelope's corresponding soapbind:body element.</assertionDescription>
 */
public class BP1212 extends AssertionProcess {

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1212(BaseMessageValidator impl)
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

      // Getting all the accessors of the operation element
      List accessors = XMLUtils.getChildElements(soapOperation);
      // Getting the ordered list of wsdl:part names
      List orderedPartNames =
        validator.orderPartNames(operationMessageParts, extElems);
      // Going through all the wsdl:part names
      Iterator i = orderedPartNames.iterator();
      while (i.hasNext())
      {
        String partName = (String) i.next();
        // If there is not exactly one accessor for the part specified,
        // the assertion failed
        if (getPartsCount(accessors, partName) != 1)
          throw new AssertionFailException(
            "The name of wsdl:part is " + partName);
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

  /**
   * Counts the amount of accessors of a specific name
   * @param accessors a list of accessors
   * @param name the name of accessor elements to be counted
   * @return
   */
  private int getPartsCount(List accessors, String name)
  {
    int count = 0;

    for (int i = 0; i < accessors.size(); i++)
    {
      Element accessor = (Element) accessors.get(i);
      if (accessor.getLocalName().equals(name))
        count++;
    }

    return count;
  }
}