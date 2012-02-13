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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.wsdl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP2901
 *
 * <context>For a candidate wsdl:binding element</context>
 * <assertionDescription>A description uses either the WSDL MIME Binding as described in WSDL 1.1 Section 5
 * or WSDL SOAP binding as described in WSDL 1.1 Section 3
 * on each of the wsdl:input or wsdl:output elements of a wsdl:binding. </assertionDescription>
 */
public class AP2901 extends AssertionProcess implements WSITag
{
  /**
   * WSDLValidator
   */
  private final WSDLValidatorImpl validator;

  private Collection mimeElements;

  /**
   * @param WSDLValidatorImpl
   */
  public AP2901(WSDLValidatorImpl impl)
  {
    super(impl);
    this.validator = impl;

    mimeElements = new HashSet();
    mimeElements.add(WSDL_MIME_CONTENT);
    mimeElements.add(WSDL_MIME_XML);
    mimeElements.add(WSDL_MIME_MULTIPART);
  }

  /* Validates the test assertion.
  * @see org.wsi.test.profile.validator.impl.BaseValidatorImpl.AssertionProcess#validate(org.wsi.test.profile.TestAssertion, org.wsi.test.profile.validator.EntryContext)
  */
  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      // Getting a wsdl:binding
      Binding binding = (Binding) entryContext.getEntry().getEntryDetail();

      // Getting its wsdl:operation elements
      List ops = binding.getBindingOperations();

      // Going through the operation elements
      for (int i = 0; i < ops.size(); i++)
      {
        BindingOperation bindingOperation = (BindingOperation) ops.get(i);

        // Getting wsdl:input and wsdl:output elements of an operation
        BindingInput bindingInput = bindingOperation.getBindingInput();
        BindingOutput bindingOutput = bindingOperation.getBindingOutput();

        QName inapplicableElement = null;
        // Getting an inapplicable extensibility element of wsdl:input
        // If such element exists, the assertion failed
        if (bindingInput != null)
        {
          // Getting an inapplicable extensibility element of wsdl:input
          // If such element exists, the assertion failed
          inapplicableElement = getInapplicableElement(bindingInput.getExtensibilityElements());

          if (inapplicableElement != null)
            throw new AssertionFailException(inapplicableElement.toString());
        }
        
        // Getting an inapplicable extensibility element of wsdl:output
        // If such element exists, the assertion failed
        if (bindingOutput != null)
        {
          inapplicableElement =
            getInapplicableElement(bindingOutput.getExtensibilityElements());
          if (inapplicableElement != null)
            throw new AssertionFailException(inapplicableElement.toString());
        }
      }
    }
    catch (AssertionFailException afe)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = validator.createFailureDetail(
        afe.getMessage(), entryContext);
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * Looks for any extensibility element of wsdl:input or wsdl:output
   * that does not conform to the SOAP binding or the MIME binding.
   * @param extElems a list of extensibility elements
   * @return the QName of an inapplicable element
   */
  private QName getInapplicableElement(List extElems)
  {
    for (int i = 0; i < extElems.size(); i++)
    {
      QName elementName =
        ((ExtensibilityElement) extElems.get(i)).getElementType();

      if (!mimeElements.contains(elementName) &&
          !elementName.equals(WSDL_SOAP_BODY) &&
          !elementName.equals(WSDL_SOAP_HEADER))
      {
        return elementName;
      }
    }

    return null;
  }
}