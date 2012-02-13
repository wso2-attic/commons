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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * BP4101
 * <context>For a candidate envelope containing a soap:actor attribute with a value other than the special uri "http://schemas.xmlsoap.org/soap/actor/next".</context>
 * <assertionDescription>A header element in the envelope contains a soap:actor attribute with a value other than the special uri "http://schemas.xmlsoap.org/soap/actor/next".</assertionDescription>
 */
public class BP4101 extends AssertionProcess {

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP4101(BaseMessageValidator impl)
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

      // Getting header elements from envelope
      Element root = doc.getDocumentElement();
      NodeList headerList = root.getElementsByTagNameNS(
        WSIConstants.NS_URI_SOAP, XMLUtils.SOAP_ELEM_HEADER);

      // If there is no header, the assertion is not applicable
      if (headerList == null || headerList.getLength() == 0)
      {
        throw new AssertionNotApplicableException();
      }

      // Getting the header element
      Node header = headerList.item(0);

      // Getting the immediate child elements of the header
      NodeList elems = header.getChildNodes();

      // If there is no child elements of the header
      // the assertion is not applicable
      if (elems == null || elems.getLength() == 0)
      {
        throw new AssertionNotApplicableException();
      }

      // Walking through child elements
      for (int i = 0; i < elems.getLength(); i++)
      {
        // Getting node attributes
        NamedNodeMap attrs = elems.item(i).getAttributes();

        // If the node does not have attributes then continue
        if (attrs == null)
        {
          continue;
        }

        // Getting actor attribute
        Node actor = attrs.getNamedItem(root.getPrefix()
          + ":" + XMLUtils.SOAP_ATTR_ACTOR);

        // If the actor attribute is presented and does not equal
        // to "http://schemas.xmlsoap.org/soap/actor/next",
        // then the assertion passed
        if (actor != null
          && !actor.getNodeValue().equals(WSIConstants.NS_URI_SOAP_NEXT_ACTOR))
        {
          throw new AssertionPassException();
        }
      }

      // No one actor attribute has a value other than
      // "http://schemas.xmlsoap.org/soap/actor/next",
      // the assertion is not applicable 
      result  = AssertionResult.RESULT_NOT_APPLICABLE;

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