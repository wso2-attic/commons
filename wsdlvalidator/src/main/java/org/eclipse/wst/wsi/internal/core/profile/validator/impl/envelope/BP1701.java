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
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.wsdl.util.xml.DOM2Writer;


/**
 * BP1701.
 */
public class BP1701 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1701(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    if (this.validator.isOneWayResponse(entryContext))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      try
      {
        // TEMP: Use local copy of schema for now
        // Document doc = XMLUtils.parseXML(entryContext.getLogEntry().getMessage(), WSIConstants.NS_URI_SOAP);
        // Document doc = XMLUtils.parseXML(entryContext.getLogEntry().getMessage(), "schemas/soapEnvelope.xsd");

        // Get a non-validated but (well-formed) DOM tree of the message content
        Document doc =
          XMLUtils.parseXML(entryContext.getMessageEntry().getMessage());

        // Remove any xsi:types attributes from the message (SOAP Header, Body and Envelope itself)

        NodeList elementList = doc.getElementsByTagName("*");
        if (elementList != null)
        {
          for (int i = 0; i < elementList.getLength(); i++)
          {
            Element element = (Element) elementList.item(i);
            element.removeAttributeNS(WSIConstants.NS_URI_XSI, "type");
          }
        }

        // Write out the (potentially) modified tree to String
        String filteredMessage = DOM2Writer.nodeToString(doc);

        // Parse the result with validation "on"
        XMLUtils.parseXML(
          filteredMessage,
          TestUtils.getSOAPSchemaLocation());
      }
      catch (WSIException e)
      {
        if (e.getTargetException() instanceof SAXException)
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail =
            this.validator.createFailureDetail(
              Utils.getExceptionDetails(e.getTargetException()),
              entryContext);
        }
      }
      catch (Exception e)
      {
        result = AssertionResult.RESULT_FAILED;
        failureDetail =
          this.validator.createFailureDetail(Utils.getExceptionDetails(e), entryContext);
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}