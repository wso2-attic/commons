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

import java.util.ArrayList;
import java.util.List;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.mime.MIMEContent;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.mime.MIMEPart;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP2909
 *
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>Multiple mime:content child elements of a mime:part element
 * in a desciption reference the same wsdl:part.</assertionDescription>
 */
public class AP2909 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  // A variable that indicates a binding contains
  // multiple mime:content elements
  private boolean multipleContentFound;

  /**
   * @param WSDLValidatorImpl
   */
  public AP2909(WSDLValidatorImpl impl)
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
    // Resetting the variable
    multipleContentFound = false;

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

        // Collecting all the mime:part elements from wsdl:input and wsdl:output
        List inputMimeParts = getMimeParts(
          bindingInput == null ? null : bindingInput.getExtensibilityElements());
        List outputMimeParts = getMimeParts(
          bindingOutput == null ? null : bindingOutput.getExtensibilityElements());

        // If there is a mime:part containing multiple mime:contentS
        // which reference different wsdl:partS, the assertion failed
        if (containsInvalidMimePart(inputMimeParts))
        {
          throw new AssertionFailException("The invalid mime:part element "
            + "is in the wsdl:input of the \"" + bindingOperation.getName()
            + "\" binding operation.");
        }

        // If there is a mime:part containing multiple mime:contentS
        // which reference different wsdl:partS, the assertion failed
        if (containsInvalidMimePart(outputMimeParts))
        {
          throw new AssertionFailException("The invalid mime:part element "
            + "is in the wsdl:output of the \"" + bindingOperation.getName()
            + "\" binding operation.");
        }

      }
      // If the binding does not contain a mime:part with multiple
      // mime:content elements, the assertion is not applicable
      if (!multipleContentFound)
        throw new AssertionNotApplicableException();
    }
    catch (AssertionNotApplicableException anae)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
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
   * Validates mime:part elements.
   * @param mimeParts a list of mime:part elements.
   * @return true if multiple mime:contentS of a mime:part
   * reference different wsdl:partS, false otherwise.
   */
  private boolean containsInvalidMimePart(List mimeParts)
  {
    // Going through a list of mime:part elements
    for (int i = 0; i < mimeParts.size(); i++)
    {
      // A variable that indicates the mime:part contains
      // at least one mime:content element
      boolean mimeContentFound = false;
      String mimeContentPart = null;

      // Going through mime:part extensibility elements
      List extElems = ((MIMEPart) mimeParts.get(i)).getExtensibilityElements();
      for (int j = 0; j < extElems.size(); j++)
      {
        // If an extensibility element is mime:content
        if (((ExtensibilityElement)extElems.get(j))
          .getElementType().equals(WSDL_MIME_CONTENT))
        {
          MIMEContent mimeContent  = (MIMEContent) extElems.get(j);
          // If a mime:content element was already found in this mime:part
          if (mimeContentFound)
          {
            multipleContentFound = true;
            // If a mime:content references other wsdl:part than the
            // previous mime:content do, return true
            if (mimeContent.getPart() == null
              || !mimeContent.getPart().equals(mimeContentPart))
            {
              return true;
            }
          }
          // This is the first mime:content element of mime:part
          else
          {
            mimeContentFound = true;
            mimeContentPart = mimeContent.getPart();
          }
        }
      }
    }
    // There are no invalid mime:part elements, return false
    return false;
  }

  /**
   * Collects all mime:part elements.
   * @param extElems a list of extensibility elements that can contain mime:part elements.
   * @return the list of mime:part elements found.
   */
  private List getMimeParts(List extElems)
  {
    List mimeParts = new ArrayList();

    if (extElems != null)
    {
      // Going through all the extensibility elements
      for (int i = 0; i < extElems.size(); i++)
      {
        ExtensibilityElement extElem = (ExtensibilityElement) extElems.get(i);
        // If the element is mime:multipartRelated
        if (extElem.getElementType().equals(WSDL_MIME_MULTIPART))
        {
          // Getting the mime:part elements of the mime:multipartRelated
          List mParts = ((MIMEMultipartRelated) extElem).getMIMEParts();
          mimeParts.addAll(mParts);
          // Going through all the mime:part elements
          for (int j = 0; j < mParts.size(); j++)
          {
            List elems = getMimeParts(
                ((MIMEPart) mParts.get(j)).getExtensibilityElements());
              // Adding the elements to the list being returned
              mimeParts.addAll(elems);
          }
        }
      }
    }
    return mimeParts;
  }
}