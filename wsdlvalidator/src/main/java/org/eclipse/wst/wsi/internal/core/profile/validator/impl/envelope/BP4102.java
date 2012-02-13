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

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * BP4102
 * <context>For a candidate envelope containing a fault with a non-empty detail element.</context>
 * <assertionDescription>A Fault element in an envelope contains a non-empty detail element.</assertionDescription>
 */
public class BP4102 extends AssertionProcess {

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP4102(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try {

      // Parsing the message
      Document doc = entryContext.getMessageEntryDocument();

      // If the message is empty or invalid, the assertion is not applicable
      if (doc == null)
      {
        throw new AssertionNotApplicableException();
      }

      // Getting Fault elements from envelope
      Element root = doc.getDocumentElement();
      NodeList faultList = root.getElementsByTagNameNS(
        WSIConstants.NS_URI_SOAP, XMLUtils.SOAP_ELEM_FAULT);

      // If there is no Fault element, the assertion is not applicable
      if (faultList == null || faultList.getLength() == 0)
      {
        throw new AssertionNotApplicableException();
      }

      // Fault element must not appear more than once
      // within a Body element, so getting the first one
      Element fault = (Element) faultList.item(0);

      // Getting Fualt's detail elements
      NodeList details = fault.getElementsByTagName(
        XMLUtils.SOAP_ELEM_FAULT_DETAIL);
      // If there is a non-empty detail element, then the assertion passed
      if (details != null
        && details.getLength() > 0)
      {
        NodeList list = details.item(0).getChildNodes();
        // search first element node 
        for (int i = 0; i < list.getLength(); i++)
        {
          if((list.item(i).getNodeType() == Node.ELEMENT_NODE) 
          && (list.item(i).getLocalName() != null) ) 
          {
            throw new AssertionPassException();
          }
        }
      }

      // There is no detail element in Fault,
      // the assertion is not applicable
      result = AssertionResult.RESULT_NOT_APPLICABLE;

    }
    catch (AssertionPassException ape)
    {
      failureDetail = validator.createFailureDetail(
        testAssertion.getDetailDescription(), entryContext);
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}