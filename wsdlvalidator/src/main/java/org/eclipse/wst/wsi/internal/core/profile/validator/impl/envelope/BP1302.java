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
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
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
   * BP1302.
   * The soap:faultcode value in the soap:Fault element of the response 
   * message is not custom, and is one of: VersionMismatch, MustUnderstand, Client, Server.
   */
public class BP1302 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1302(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  //SOAP fault code values should not be custom: only standard code qualifiers 
  //are used: VersionMismatch, MustUnderstand, Client, Server
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    //String requestMessage = null;
    String responseMessage = null;

    if (this.validator.isOneWayResponse(entryContext))
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      //Get the response message check the faultcode.
      MessageEntry logEntryResponse = entryContext.getResponse();
      if (logEntryResponse != null)
      {
        responseMessage = logEntryResponse.getMessage();

        Document targetDoc = XMLUtils.parseXML(responseMessage);
        NodeList faultCodes = targetDoc.getElementsByTagName("faultcode");
        //                    targetDoc.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "faultcode");

        if (faultCodes.getLength() <= 0)
          result = AssertionResult.RESULT_NOT_APPLICABLE;
        //No faultcode	

        for (int i = 0; i < faultCodes.getLength(); i++)
        {
          Node faultCodeElem = faultCodes.item(i);
          //If the faultcode was MustUnderstand, check the value of 
          //the mustUnderstand attribute in the request.
          if (faultCodeElem == null)
            result = AssertionResult.RESULT_NOT_APPLICABLE;
          else if (faultCodeElem.getNodeType() == Node.ELEMENT_NODE)
          {
            if (isValidFaultCode((Element) faultCodeElem))
            {
              result = AssertionResult.RESULT_PASSED;
            }
            else
            {
              result = AssertionResult.RESULT_WARNING;
              failureDetail =
                this.validator.createFailureDetail(responseMessage, entryContext);
              //failureDetail =
              //    "The faultcode value was : "
              //        + faultCodeElem.getFirstChild().getNodeValue().trim();
            } //End if (faultCode.equals...)
          }
          else
          {
            result = AssertionResult.RESULT_NOT_APPLICABLE;
          }
        } //End for	
      } //End if logEntryResponse!= null
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  /**
   * Check faultCode.
   * @param faultCodeElem a faultcode.
   * @return true if faultcode is valid.
   */
  public boolean isValidFaultCode(Element faultCodeElem)
  {
    //A faultcode may or maynot have a prefix, if it does its namespace should be 
    //http://schemas.xmlsoap.org/soap/envelope which is the same as the faultcode element.
    //If a prefix is present and it corresponds to a different namespace or is null 
    //then this returns false.  The local part of the faultcode is then check to see
    //if it is one of the permitted values.
    if (faultCodeElem == null)
      return true;

    String faultCode = faultCodeElem.getFirstChild().getNodeValue().trim();
    if (faultCode == null)
      return false;

    // FIX: This is not correct, since this element can not be namespace qualified
    String faultCodeElemPrefix = faultCodeElem.getParentNode().getPrefix();

    // If this is not a QName then return false
    int index;
    String faultCodePrefix = null;
    String faultCodeName = faultCode;
    if ((index = faultCode.indexOf(':')) != -1)
    {
      faultCodePrefix = faultCode.substring(0, index);
      faultCodeName = faultCode.substring(index + 1, faultCode.length());
    }

    if ((faultCodeElemPrefix != null && faultCodePrefix != null))
    {
      if (faultCodePrefix.equals(faultCodeElemPrefix))
      {
        if ((faultCodeName.equals("MustUnderstand")
          || faultCodeName.equals("VersionMismatch")
          || faultCodeName.equals("Client")
          || faultCodeName.equals("Server")))
          /* REMOVE:
          || faultCodeName.startsWith("MustUnderstand.")
          || faultCodeName.startsWith("VersionMismatch.")
          || faultCodeName.startsWith("Client.")
          || faultCodeName.startsWith("Server.")))
          */
          return true;
        else
          return false;
      }
      else
      {
        return true;
      }
    }
    else
    {
      if (faultCodeName.equals("MustUnderstand")
        || faultCodeName.equals("VersionMismatch")
        || faultCodeName.equals("Client")
        || faultCodeName.equals("Server")
        || faultCodeName.startsWith("MustUnderstand.")
        || faultCodeName.startsWith("VersionMismatch.")
        || faultCodeName.startsWith("Client.")
        || faultCodeName.startsWith("Server."))
        return true;
      else
        return false;
    }
  }
}