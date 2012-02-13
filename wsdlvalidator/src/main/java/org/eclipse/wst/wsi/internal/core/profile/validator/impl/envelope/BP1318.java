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

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.OperationSignature;
import org.eclipse.wst.wsi.internal.core.util.TypesRegistry;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLUtils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
	 * BP1318
	 * 
	 * The grandchildren elements of soap:Body do not have a soap:encodingStyle attribute.
	 */
public class BP1318 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1318(BaseMessageValidator impl)
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
      if (this.validator.isOneWayResponse(entryContext))
        throw new AssertionNotApplicableException();

      // Parse message
      Document responseDoc = entryContext.getMessageEntryDocument();
      Document requestDoc = entryContext.getRequestDocument();

      // messages are empty or invalid, DOM objects are null
      if (responseDoc == null || requestDoc == null) {
        throw new AssertionNotApplicableException();
      }

      //if (isFault(responseDoc))
      //  throw new AssertionNotApplicableException();

      // Get SOAPAction
      String headers = entryContext.getRequest().getHTTPHeaders();
      String action = null;
      if (headers != null)
        action = (String) HTTPUtils.getHttpHeaderTokens(headers, ":").get("SOAPAction".toUpperCase());

      // Get the binding that is being processed                
      Binding binding = validator.analyzerContext.getCandidateInfo().getBindings()[0];

      //Create the types registry
      TypesRegistry registry =
        new TypesRegistry(
          this.validator.getWSDLDocument().getDefinitions(),
          validator);

      // Find an operation match        
      OperationSignature.OperationMatch match =
        OperationSignature.matchOperation(
          requestDoc,
          action,
          binding,
          registry);
      if (match == null)
        throw new AssertionNotApplicableException();

      // Get the binding operation based on the match
      BindingOperation bindingOperation = match.getOperation();

      // If this is not rpc-literal, then return notApplicable result
      if (!WSDLUtils
        .isRpcLiteral(match.getOperationStyle(), bindingOperation))
      {
        throw new AssertionNotApplicableException();
      }
      // look for <soap:Body> elements:
      NodeList soapBodyList =
        responseDoc.getElementsByTagNameNS(
          WSIConstants.NS_URI_SOAP,
          XMLUtils.SOAP_ELEM_BODY);
      if ((soapBodyList == null) || (soapBodyList.getLength() == 0))
        // Response does not contain any soap Body elements
        throw new AssertionNotApplicableException();

      // check that no <soap:Body> child or grandchild elements contains a soap:encodingStyle attribute
      // For each <soap:Body>
      boolean grandChildFound = false;
      for (int n = 0; n < soapBodyList.getLength(); n++)
      {
        Element nextBodyElem = (Element) soapBodyList.item(n);
        // REMOVE: This will get all nodes (child, grandchildren, etc.)
        //NodeList childList = nextBodyElem.getElementsByTagName("*");
        NodeList childList = nextBodyElem.getChildNodes();
        if (childList != null)
        {
          // check all child elements
          for (int m = 0; m < childList.getLength(); m++)
          {
            if (childList.item(m).getNodeType() == Node.ELEMENT_NODE)
            {
              Element nextChildElem = (Element) childList.item(m);
              // check children of this child
              // REMOVE: This will get all nodes (child, grandchildren, etc.)
              NodeList grandChildList = nextChildElem.getChildNodes();
              if (grandChildList != null)
              {
                for (int p = 0; p < grandChildList.getLength(); p++)
                {
                  if (grandChildList.item(p).getNodeType()
                    == Node.ELEMENT_NODE)
                  {
                  	grandChildFound = true;
                    Element nextGrandChildElem =
                      (Element) grandChildList.item(p);
                    if (nextGrandChildElem
                      .getAttributeNodeNS(
                        WSIConstants.NS_URI_SOAP,
                        "encodingStyle")
                      != null)
                    {
                      // Assertion failed (ADD highlight the child here ?)
                      throw new AssertionFailException(
                        entryContext.getMessageEntry().getMessage());
                    }
                  }
                }
              }
            }
          }
        }
      }
      if (!grandChildFound)
      {
      	throw new AssertionNotApplicableException();
      }
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