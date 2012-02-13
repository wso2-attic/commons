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
 * AP2911
 *
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>The mime:multipartRelated elements in wsdl:input and wsdl:ouput elements
 * of operations in a wsdl:binding, contain exactly one mime:part child element that contains
 * a soapbind:body child element.</assertionDescription>
 */
public class AP2911 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP2911(WSDLValidatorImpl impl)
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
    try
    {
      // A variable that indicates a binding contains at least one
      // mime:multipartRelated element
      boolean multipartsFound = false;

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

        // Collecting all the mime:multipartRelated elements from wsdl:input and wsdl:output
        List inputMultiparts = getMimeMultipartElements(
          bindingInput == null ? null : bindingInput.getExtensibilityElements());
        List outputMultiparts = getMimeMultipartElements(
          bindingOutput == null ? null : bindingOutput.getExtensibilityElements());

        // If the wsdl:input contains mime:multipartRelated elements
        if (!inputMultiparts.isEmpty())
        {
          multipartsFound = true;

          // If there is not exactly one mime:part element containing
          // a soapbind:body child, the assertion failed
          if (!containsOneSoapBody(inputMultiparts))
          {
            throw new AssertionFailException("The invalid "
                + "mime:multipartRelated element is in the wsdl:input of the \""
                + bindingOperation.getName() + "\" binding operation.");
          }
        }

        // If the wsdl:output contains mime:multipartRelated elements
        if (!outputMultiparts.isEmpty())
        {
          multipartsFound = true;

          // If there is not exactly one mime:part element containing
          // a soapbind:body child, the assertion failed
          if (!containsOneSoapBody(outputMultiparts))
          {
            throw new AssertionFailException("The invalid "
                + "mime:multipartRelated element is in the wsdl:output of the \""
                + bindingOperation.getName() + "\" binding operation.");
          }
        }

      }
      // If the binding contains no one mime:multipartRelated element,
      // the assertion is not applicable
      if (!multipartsFound)
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
   * Looks for mime:part elements containing a soap:body child
   * for each of mime:multipartRelated element.
   * @param multiparts a list of mime:multipartRelated elements.
   * @return true if there is exactly one mime:part containing a soap:body
   * child, false otherwise.
   */
  private boolean containsOneSoapBody(List multiparts)
  {
    // A variable indicates that a soap:body element is found
    boolean soapBodyFound = false;

    // Going through a list of mime:multipartRelated elements
    for (int i = 0; i < multiparts.size(); i++)
    {
      // Getting a list of mime:part elements
      List mimeParts =
        ((MIMEMultipartRelated) multiparts.get(i)).getMIMEParts();

      // Going through all the mime:part elements
      for (int j = 0; j < mimeParts.size(); j++)
      {
        // Getting a list of extensibility elements of a mime:part
        List extElems =
          ((MIMEPart) mimeParts.get(j)).getExtensibilityElements();
        // Going through the extensibility elements
        for (int k = 0; k < extElems.size(); k++)
        {
          // If an extensibility element is a soap:body
          if (((ExtensibilityElement)extElems.get(k))
            .getElementType().equals(WSDL_SOAP_BODY))
          {
            // If a soap:body element was already found,
            // return true
            if (soapBodyFound)
            {
              return false;
            }
            // else set the variable to the true value
            else
            {
              soapBodyFound = true;
            }
          }
        }
      }
    }

    return soapBodyFound;
  }

  /**
   * Collects all mime:multipartRelated elements.
   * @param extElems a list of extensibility elements that can contain mime:multipartRelated elements.
   * @return the list of mime:multipartRelated elements found.
   */
  private List getMimeMultipartElements(List extElems)
  {
    List mimeMultipartElements = new ArrayList();

    if (extElems != null)
    {
      // Going through all the extensibility elements
      for (int i = 0; i < extElems.size(); i++)
      {
        ExtensibilityElement extElem = (ExtensibilityElement) extElems.get(i);
        // If the element is mime:multipartRelated
        if (extElem.getElementType().equals(WSDL_MIME_MULTIPART))
        {
          // Adding the element to the list being returned
          mimeMultipartElements.add(extElem);
          // Getting the mime:part elements of the mime:multipartRelated
          List mimeParts = ((MIMEMultipartRelated) extElem).getMIMEParts();
          // Going through all the mime:part elements
          for (int j = 0; j < mimeParts.size(); j++)
          {
            // Collecting all the mime:multipartRelated elements of this mime:part
            List elems = getMimeMultipartElements(
              ((MIMEPart) mimeParts.get(j)).getExtensibilityElements());
            // Adding the elements to the list being returned
            mimeMultipartElements.addAll(elems);
          }
        }
      }
    }

    return mimeMultipartElements;
  }
}