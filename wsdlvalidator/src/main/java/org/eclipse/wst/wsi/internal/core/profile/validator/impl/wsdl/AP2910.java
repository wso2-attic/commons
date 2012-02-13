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
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Output;
import javax.wsdl.Part;
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
 * AP2910
 *
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>A mime:content in a DESCRIPTION references
 * a wsdl:part that is defined using either the type attribute
 * or the element attribute.</assertionDescription>
 */
public class AP2910 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP2910(WSDLValidatorImpl impl)
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
      // mime:content element
      boolean mimeContentsFound = false;

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

        // Collecting all the mime:content elements from wsdl:input and wsdl:output
        List inputMimeContents = getMimeContentElements(
          bindingInput == null ? null : bindingInput.getExtensibilityElements());
        List outputMimeContents = getMimeContentElements(
          bindingOutput == null ? null : bindingOutput.getExtensibilityElements());

        // If the wsdl:input contains mime:content elements
        if (!inputMimeContents.isEmpty())
        {
          mimeContentsFound = true;
          Input portTypeInput = bindingOperation.getOperation().getInput();
          // If there is no the corresponding wsdl:input in wsdl:portType
          // or the wsdl:input does not specify a message attribute,
          // the assertion failed
          if (portTypeInput == null || portTypeInput.getMessage() == null)
            throw new AssertionFailException(
              "The corresponging operation in the wsdl:portType for the \""
              + bindingOperation.getName() + "\" binding operation is invalid.");
          // Getting a mime:content referencing an invalid wsdl:part 
          String part = getInvalidMimeContentPart(
            inputMimeContents, portTypeInput.getMessage());
          // If such part is found, the assertion failed
          if (part != null)
            throw new AssertionFailException("part=\"" + part +
              "\", the input of the binding operation \""
              + bindingOperation.getName() + "\"");
        }

        // If the wsdl:output contains mime:content elements
        if (!outputMimeContents.isEmpty())
        {
          mimeContentsFound = true;
          Output portTypeOutput = bindingOperation.getOperation().getOutput();
          // If there is no the corresponding wsdl:output in wsdl:portType
          // or the wsdl:output does not specify a message attribute,
          // the assertion failed
          if (portTypeOutput == null || portTypeOutput.getMessage() == null)
            throw new AssertionFailException(
              "The corresponging operation in the wsdl:portType for the \""
              + bindingOperation.getName() + "\" binding operation is invalid.");
          // Getting a mime:content referencing an invalid wsdl:part
          String part = getInvalidMimeContentPart(
              outputMimeContents, portTypeOutput.getMessage());
          // If such part is found, the assertion failed
          if (part != null)
            throw new AssertionFailException("part=\"" + part +
                "\", the output of the binding operation \""
                + bindingOperation.getName() + "\"");
        }
      }

      // If the binding does not contain mime:content elements,
      // the assertion is not applicable
      if (!mimeContentsFound)
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
   * Validates every mime:content element. If it references a wsdl:part that
   * is defined using neither the type attribute nor the element attribute
   * or using them together, it is invalid.
   * @param mimeContents a list of mime:content elements.
   * @param message the corresponding wsdl:message element for mime:contentS.
   * @return a value of the part attribute of an invalid mime:content
   * or null if all the mime:content elements are valid.
   */
  private String getInvalidMimeContentPart(List mimeContents, Message message) {
    // Going throug all the mime:content elements
    for (int i = 0; i < mimeContents.size(); i++)
    {
      // Getting a value of the part attribute from a mime:element
      String partName = ((MIMEContent) mimeContents.get(i)).getPart();
      // Getting the corresponging wsdl:part
      Part part = message.getPart(partName);
      // if it is defined and use neither the type attribute
      // nor the element attribute or use them together, return part value
      if (part != null
        && ((part.getTypeName() == null && part.getElementName() == null)
        || (part.getTypeName() != null && part.getElementName() != null)))
      {
        return partName;
      }
    }
    // Return null if no one part is found
    return null;
  }

  /**
   * Collects all mime:content elements.
   * @param extElems a list of extensibility elements that can contain mime:contentS.
   * @return the list of mime:content elements found.
   */
  private List getMimeContentElements(List extElems)
  {
    List mimeContentElements = new ArrayList();

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
          List mimeParts = ((MIMEMultipartRelated) extElem).getMIMEParts();
          // Going through all the mime:part elements
          for (int j = 0; j < mimeParts.size(); j++)
          {
            // Collecting all the mime:content elements of this mime:part
            List elems = getMimeContentElements(
              ((MIMEPart) mimeParts.get(j)).getExtensibilityElements());
            // Adding the elements to the list being returned
            mimeContentElements.addAll(elems);
          }
        }
        // Else if the element is mime:content
        else if (extElem.getElementType().equals(WSDL_MIME_CONTENT))
        {
          // Adding the element to the list being returned
          mimeContentElements.add(extElem);
        }
      }
    }

    return mimeContentElements;
  }
}