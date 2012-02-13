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
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


/**
 * BP1203. 
 * The namespace of the detail element of a qualified attribute in the soap:Fault is a 
 * foreign namespace, different from "http://schemas.xmlsoap.org/soap/envelope/".
 */
public class BP1203 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1203(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    boolean qualifiedAttributes = false;

    Document doc;
    // Check if this is one way response or message is mepty or invalid
    if (this.validator.isOneWayResponse(entryContext)
        || (doc = entryContext.getMessageEntryDocument()) == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // ADD: Need to determine if this check is required, since it should already be done
      // Applicable to response messages only
      if (entryContext
        .getMessageEntry()
        .getType()
        .equalsIgnoreCase(MessageEntry.TYPE_RESPONSE))
      {
        // look for <soap:Fault> element:
        NodeList faultList =
          doc.getElementsByTagNameNS(
            WSIConstants.NS_URI_SOAP,
            XMLUtils.SOAP_ELEM_FAULT);
        if ((faultList == null) || (faultList.getLength() == 0))
        {
          // Response does not contain a soap:Fault
          result = AssertionResult.RESULT_NOT_APPLICABLE;
        }

        // we have a soap:Fault. 
        else
        {
          try
          {
            // look at each soap:Fault in turn
            for (int i = 0; i < faultList.getLength(); i++)
            {
              Element soapFault = (Element) faultList.item(i);
              // find the detail element(s) if any
              NodeList detailList =
                soapFault.getElementsByTagName(XMLUtils.SOAP_ELEM_FAULT_DETAIL);
              if (detailList != null)
              {
                // for each detail element...     				
                for (int j = 0; j < detailList.getLength(); j++)
                {
                  NamedNodeMap detailAttribs =
                    detailList.item(j).getAttributes();
                  // check for any qualified attributes
                  if (detailAttribs != null)
                  {
                    for (int k = 0; k < detailAttribs.getLength(); k++)
                    {
                      Attr nextAttr = (Attr) (detailAttribs.item(k));
                      // for each qualified attribute, check that 
                      // qname != http://schemas.xmlsoap.org/soap/envelope
                      if (nextAttr.getNamespaceURI() != null)
                      {
                        qualifiedAttributes = true;
                        if (nextAttr
                          .getNamespaceURI()
                          .equals(WSIConstants.NS_URI_SOAP))
                        {
                          // found unexpected qname
                          throw new AssertionFailException(
                            entryContext.getMessageEntry().getMessage());
                        }
                      }
                    }
                  }
                }
              }
            }

            if (!qualifiedAttributes)
              result = AssertionResult.RESULT_NOT_APPLICABLE;
          }
          catch (AssertionFailException e)
          {
            result = AssertionResult.RESULT_FAILED;
            failureDetail = this.validator.createFailureDetail(e.getMessage(), entryContext);
          }
        }
      }
      else
      {
        // target message is not a Response
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}