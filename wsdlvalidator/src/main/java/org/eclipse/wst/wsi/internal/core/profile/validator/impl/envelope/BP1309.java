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
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
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
 * BP1309.
 * The soap:Envelope does not have direct children after the soap:Body element 
 */
public class BP1309 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP1309(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {

    Document doc;
    // Check if this is one way response
    // or message is mepty or invalid
    if (this.validator.isOneWayResponse(entryContext)
      || (doc = entryContext.getMessageEntryDocument()) == null)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    else
    {
      // look for <soap:Envelope> elements:
      // (Note: since this is a Soap message we expect exactly one soap envelope, but we do not assume it).
      NodeList soapEnvList =
        doc.getElementsByTagNameNS(
          WSIConstants.NS_URI_SOAP,
          XMLUtils.SOAP_ELEM_ENVELOPE);
      if ((soapEnvList == null) || (soapEnvList.getLength() == 0))
      {
        // Response does not contain any soap Envelope element(s)
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        // check that if a <soap:Body> exists in a <soap:Envelope>, that its the last in the envelope        		      		
        try
        {
          // For each <soap:Envelope>
          for (int n = 0; n < soapEnvList.getLength(); n++)
          {
            // Get the list of all elements in the Envelope       				
            Element soapEnv = (Element) soapEnvList.item(n);
            NodeList envChildList = soapEnv.getChildNodes();
            Vector envChildElemList = new Vector();
            for (int v = 0; v < envChildList.getLength(); v++)
            {
              Node nextNode = envChildList.item(v);
              if (nextNode.getNodeType() == Node.ELEMENT_NODE)
              {
                envChildElemList.addElement((Element) nextNode);
              }
            }
            // Search the list for <soap:Body>
            for (int m = 0; m < envChildElemList.size(); m++)
            {
              Element envChildElem = (Element) envChildElemList.elementAt(m);
              if (envChildElem
                .getNamespaceURI()
                .equals(WSIConstants.NS_URI_SOAP)
                && envChildElem.getLocalName().equals(XMLUtils.SOAP_ELEM_BODY))
              {
                // found a <soap:Body> so check its the last in the list of child elements of the Envelope
                if (envChildElem != envChildElemList.lastElement())
                {
                  throw new AssertionFailException(
                    entryContext.getMessageEntry().getMessage());
                }
              }
            }
          }
        }
        catch (AssertionFailException e)
        {
          result = AssertionResult.RESULT_FAILED;
          failureDetail = this.validator.createFailureDetail(e.getMessage(), entryContext);
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}