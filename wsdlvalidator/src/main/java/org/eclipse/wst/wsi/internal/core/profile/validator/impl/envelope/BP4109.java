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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * BP4109
 *
 * <context>For a candidate envelope containing a soap:Body element with attributes</context>
 * <assertionDescription>An envelope contains a a soap:Body element with attributes.</assertionDescription>
 */
public class BP4109 extends AssertionProcess {

  private final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public BP4109(BaseMessageValidator impl)
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

      // Getting the soap:Body elements from envelope
      NodeList bodyList = doc.getDocumentElement().getElementsByTagNameNS(
        WSIConstants.NS_URI_SOAP, XMLUtils.SOAP_ELEM_BODY);

      // If there is no Body element, the assertion is not applicable
      if (bodyList == null || bodyList.getLength() == 0)
      {
        throw new AssertionNotApplicableException();
      }

      // Getting the first soap:Body element
      Element body = (Element) bodyList.item(0);

      // If the Body element has attributes (excluding namespace declarations),
      // the assertion passed
      NamedNodeMap attrs = body.getAttributes();
      if (attrs != null)
      {
        for (int i = 0; i < attrs.getLength(); i++)
        {
          String attrName = ((Attr) attrs.item(i)).getName();
          if (!attrName.equals("xmlns") && !attrName.startsWith("xmlns:"))
          {
            throw new AssertionPassException();
          }
        }
      }

      throw new AssertionNotApplicableException();
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