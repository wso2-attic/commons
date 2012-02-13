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

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;

/**
 * AP2944
 *
 * <context>For a candidate wsdl:binding</context>
 * <assertionDescription>In a DESCRIPTION, if a wsdl:part element refers to a
 * global element declaration (via the element attribute of the wsdl:part element)
 * then the value of the type attribute of a mime:content element that binds that part
 * is a content type suitable for carrying an XML serialization.</assertionDescription>
 */
public class AP2944 extends AssertionProcess implements WSITag
{
  private final WSDLValidatorImpl validator;

  // A variable that indicates a binding contains mime:content elements
  // that bind wsdl:partS defined with the element attribute
  private boolean mimeContentFound;
  /**
   * @param WSDLValidatorImpl
   */
  public AP2944(WSDLValidatorImpl impl)
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
    mimeContentFound = false;

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

        // Collecting all the mime:content elements from wsdl:input and wsdl:output
        List inputMimeContents = getMimeContentElements(
          bindingInput == null ? null : bindingInput.getExtensibilityElements());
        List outputMimeContents = getMimeContentElements(
          bindingOutput == null ? null : bindingOutput.getExtensibilityElements());

        // If the wsdl:input contains mime:content elements
        if (!inputMimeContents.isEmpty())
        {
          Input portTypeInput = bindingOperation.getOperation().getInput();
          // If the corresponding wsdl:input exists in wsdl:portType
          // and includes the message attribute
          if (portTypeInput != null && portTypeInput.getMessage() != null)
          {
            // If there is an invalid mime:content element
            MIMEContent imc = getInvalidMimeContent(
                inputMimeContents, portTypeInput.getMessage());
            if (imc != null)
            {
              throw new AssertionFailException("The mime:content element in "
                + "the wsdl:input of the \"" + bindingOperation.getName()
                + "\" that binds the \"" + imc.getPart()
                + "\" wsdl:part uses the invalid content type \""
                + imc.getType() + "\". ");
            }
          }
        }

        // If the wsdl:output contains mime:content elements
        if (!outputMimeContents.isEmpty())
        {
          Output portTypeOutput = bindingOperation.getOperation().getOutput();
          // If the corresponding wsdl:output exists in wsdl:portType
          // and includes the message attribute
          if (portTypeOutput != null && portTypeOutput.getMessage() != null)
          {
            // If there is an invalid mime:content element
            MIMEContent imc = getInvalidMimeContent(
              outputMimeContents, portTypeOutput.getMessage());
            if (imc != null)
            {
              throw new AssertionFailException("The mime:content element in "
                + "the wsdl:output of the \"" + bindingOperation.getName()
                + "\" that binds the \"" + imc.getPart()
                + "\" wsdl:part uses the invalid content type \""
                + imc.getType() + "\". ");
            }
          }
        }
      }
      // If mime:content elements are not found,
      // the assertion is not applicable
      if (!mimeContentFound)
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
   * Checks whether any mime:content element binds wsdl:part that is defined
   * with the element attribute and uses the content type "text/xml".
   * @param mimeContents a list of mime:content elements of binding operation.
   * @param message the corresponding wsdl:message element.
   * @return a mime:content element that uses a content type other than
   * "text/xml", null if no one such element is found.
   */
  private MIMEContent getInvalidMimeContent(List mimeContents, Message message)
  {
    // Going through a list of mime:content elements
    for (int i = 0; i < mimeContents.size(); i++)
    {
      MIMEContent mimeContent = (MIMEContent) mimeContents.get(i);
      // Getting the corresponding wsdl:part
      Part part = message.getPart(mimeContent.getPart());
      // If the part is defined with the element attribute
      if (part != null && part.getElementName() != null)
      {
        mimeContentFound = true;
        // If the type attribute value is other than "text/xml"
        if (!WSIConstants.CONTENT_TYPE_TEXT_XML.equals(mimeContent.getType()))
        {
          // return the invalid element
          return mimeContent;
        }
      }
    }
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