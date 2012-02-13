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

import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * BP1301.
 */
public class BP1301 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1301(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    //Get the requets message and check the value of mustUnderstand.
    String message = entryContext.getMessageEntry().getMessage();
    Object[] mustUnderstandAttributes = getMustUnderstandAttributes(message);

    if (mustUnderstandAttributes == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      for (int index = 0; index < mustUnderstandAttributes.length; index++)
      {
        if ((mustUnderstandAttributes[index] != null)
          && (mustUnderstandAttributes[index].equals("1")
            || mustUnderstandAttributes[index].equals("0")))
        {
          result = AssertionResult.RESULT_PASSED;
        }
        else
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail =
            this.validator.createFailureDetail(
              "The SOAP mustUnderstand attribute was present "
                + " and its value was  "
                + mustUnderstandAttributes[index],
              entryContext);
        } //End if
      } //End for
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }

  //Retreives the value of the mustUnderstand attribute from ALL SOAP header
  //If the Header is missing or if it does not contain any targets or if
  //the header does not contain a mustUnderstand attribute then set the
  //assertionResult to RESULT_NOT_APPLICABLE.
  private Object[] getMustUnderstandAttributes(String message)
    throws WSIException
  {
    if (message == null || message.trim().equals("")) {
      return null;
    }

    Document doc = XMLUtils.parseXML(message);
    NodeList headers =
      doc.getElementsByTagNameNS(WSIConstants.NS_URI_SOAP, "Header");

    //If a header not present set result to notApplicable
    //else process each headerEntry
    if (headers.getLength() <= 0)
    {
      return null;
    }
    else
    {
      Vector mustUnderstandAttributes = new Vector();
      Element header = (Element) headers.item(0);
      if (header == null)
        return null;

      NodeList headerEntries = header.getChildNodes();
      //For each header entry check retreive the mustUnderstand attribute 
      //if any, else set the test result to notApplicable

      //This is used to indicate if a header contained any child Elements
      boolean headerTargets = false;

      for (int index = 0; index < headerEntries.getLength(); ++index)
      {
        Node headerEntry = headerEntries.item(index);
        if (headerEntry.getNodeType() == Node.ELEMENT_NODE)
        {
          headerTargets = true;
          Attr mustUnderstand =
            ((Element) headerEntry).getAttributeNodeNS(
              WSIConstants.NS_URI_SOAP,
              "mustUnderstand");
          //Retreive the value of the mustUnderstand attribute of the request.
          if (mustUnderstand != null)
          {
            String mustUnderstandValue = mustUnderstand.getNodeValue();
            mustUnderstandAttributes.add(mustUnderstandValue);
          }
          else
          {
            result = AssertionResult.RESULT_NOT_APPLICABLE;
            //The SOAP Header does not contain a mustUnderstand attribute."
          } //	mustUnderstand != null
        }
      } //End for

      if (!headerTargets)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
        //The SOAP Header does not contain any header entries.
      }

      return mustUnderstandAttributes.toArray();
    } //End If headers.getLength() < 0	
  }

  /**
   * Returns true if this object is must understand fault code.
   * @param faultCodeElem understand fault code.
   * @return true if this object is must understand fault code.
   */
  public boolean isMustUnderstandFaultCode(Element faultCodeElem)
  {
    //A faultcode may or maynot have a prefix, if it does its namespace should be 
    //htt://schemas.xmlsoap.org/soap/envelope which is the same as the faultcode element.
    //If a prefix is present and it corresponds to a different namespace or is null 
    //then this returns false.  The local part of the faultcode is then check to see
    //if it is one of the permitted values.
    if (faultCodeElem == null)
      return false;

    String faultCode = faultCodeElem.getFirstChild().getNodeValue().trim();
    if (faultCode == null)
      return false;

    String faultCodeElemPrefix = faultCodeElem.getPrefix();
    String faultCodePrefix = faultCode.substring(0, faultCode.indexOf(':'));
    String faultCodeName =
      faultCode.substring(faultCode.indexOf(':') + 1, faultCode.length());

    if ((faultCodeElemPrefix != null || faultCodePrefix != null))
    {
      if (faultCodePrefix.equals(faultCodeElemPrefix)
        && (faultCodeName.equals("MustUnderstand")
          || faultCodeName.startsWith("MustUnderstand.")))
        return true;
      else
        return false;
    }
    else
    {
      if (faultCode.equals("MustUnderstand")
        || faultCodeName.startsWith("MustUnderstand."))
        return true;
      else
        return false;
    }
  }
}