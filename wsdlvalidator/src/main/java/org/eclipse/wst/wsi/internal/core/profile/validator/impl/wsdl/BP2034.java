/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import java.util.List;

import javax.wsdl.Definition;
import javax.wsdl.Types;
import javax.wsdl.extensions.schema.Schema;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * BP2034
 *
 * <context>For a candidate description within a WSDL document</context>
 * <assertionDescription>The candidate description does not contain the namespace declaration xmlns:xml="http://www.w3.org/XML/1998/namespace".</assertionDescription> 
 */
public class BP2034 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2034(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    // Getting wsdl:definition element
    Definition def = (Definition) entryContext.getEntry().getEntryDetail();
    Types types;
    // Getting the namespace declaration xmlns:xml
    String xmlNs = (String) def.getNamespaces().get("xml");
    // If it exists and equals to "http://www.w3.org/XML/1998/namespace"
    if (xmlNs != null && xmlNs.equals(WSIConstants.NS_URI_XML))
    {
      // setting assertion result to warning
      result = AssertionResult.RESULT_WARNING;
    }
    // there is no namespace declaration xmlns:xml in the wsdl:definition
    // element, checking whether the definition has wsdl:type element
    else if ((types = def.getTypes()) != null)
    {
      // Getting the list of wsdl:schemaS
      List extElems = types.getExtensibilityElements();
      for (int i = 0; i < extElems.size(); i++)
      {
        // Getting wsdl:schema element
        Schema schema =
          (Schema) extElems.get(i);
        // Getting a value of the attribute xmlns:xml
        String attrValue = schema.getElement().getAttributeNS(
          WSIConstants.NS_URI_XMLNS, "xml");
        // If it equals to "http://www.w3.org/XML/1998/namespace",
        if (WSIConstants.NS_URI_XML.equals(attrValue))
        {
          // setting assertion result to warning
          result = AssertionResult.RESULT_WARNING;
          failureDetail = validator.createFailureDetail("The namespace "
            + "declaration nested in wsdl:schema element.", entryContext);
          break;
        }
      }
    }

    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
}