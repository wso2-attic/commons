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

import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.ErrorList;


/**
 * BP2122. 
 *    <context>For a candidate wsdl:types element</context>
 *    <assertionDescription>The data type definition if any within the wsdl:types element is an XML schema definition defined in the XML Schema 1.0 Recommendation with the namespace URI "http://www.w3.org/2001/XMLSchema".</assertionDescription>
 */
public class BP2122 extends AssertionProcess
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public BP2122(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;
  }

  /**
   * @see org.eclipse.wst.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(TestAssertion, EntryContext)
   */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    result = AssertionResult.RESULT_PASSED;
    ErrorList errorList = new ErrorList();

    // Get the types from the entry context
    Types types = (Types) entryContext.getEntry().getEntryDetail();

    if (types != null)
    {
      ExtensibilityElement extElement;
      List extElements = types.getExtensibilityElements();

      // Process each ext. element
      for (int i = 0; i < extElements.size(); i++)
      {
        // If it is an unknown ext. element and it is a schema, then check it
        if ((extElement = (ExtensibilityElement) extElements.get(i))
          instanceof Schema)
        {
          if (!extElement.getElementType().equals(WSITag.ELEM_XSD_SCHEMA))
          {
            // If it is not a schema definition, then it is an error
            errorList.add(
              extElement.getElementType().toString()
                + " can not be a child of the wsdl:types element.");
          }
        }
      }
    }

    // If XMLSchemaValidator could not find any errors, check errors from the inline schema validator
    if (errorList.isEmpty())
      errorList.add(validator.wsdlDocument.getSchemasValidationErrors());

    if (!errorList.isEmpty())
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = this.validator.createFailureDetail(errorList.toString(), entryContext);
    }

    return validator.createAssertionResult(testAssertion, result, failureDetail);
  }
}