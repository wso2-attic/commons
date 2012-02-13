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

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * BP1202. 
 * Each child element (if any) of the soap:Body element is namespace qualified (not the grandchildren).
 */
public class BP1202 extends AssertionProcess implements WSITag
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1202(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // Parse log message 
    Document doc = entryContext.getMessageEntryDocument();

    if (validator.isOneWayResponse(entryContext))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }

    // Check if there is a soap body element
    else if (!validator.containsSoapBodyWithChild(doc))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // Get the root element
      Element root = doc.getDocumentElement();

      // Get a node list which should contain the soap:Body element
      NodeList rootNodeList = root.getElementsByTagNameNS(
        ELEM_SOAP_BODY.getNamespaceURI(), ELEM_SOAP_BODY.getLocalPart());

      // If there is at least one soap:Body element,
      // then make sure that each child is namespace qualified
      if (rootNodeList.getLength() > 0)
      {
        // Get the soap:Body element
        Element body = (Element) rootNodeList.item(0);

        // Get the list of soap:Body child elements 
        NodeList children = body.getChildNodes();

        // Make sure that each child element is namespace qualified
        for (int i = 0;
          i < children.getLength() && result == AssertionResult.RESULT_PASSED;
          ++i)
        {
          Node n = children.item(i);
          if (n instanceof Element)
          {
            String ns = n.getNamespaceURI();
            if (ns == null || ns.length() == 0)
            {
              result = AssertionResult.RESULT_FAILED;
              failureDetail = validator.createFailureDetail(
                entryContext.getMessageEntry().getMessage(), entryContext);
            }
          }
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}