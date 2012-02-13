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
package org.eclipse.wst.wsi.internal.core.profile.validator.impl.message;

import java.util.Iterator;
import java.util.List;

import javax.wsdl.BindingOperation;
import javax.wsdl.Message;
import javax.wsdl.Part;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.mime.MIMEContent;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.mime.MIMEPart;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSITag;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.MIMEConstants;
import org.eclipse.wst.wsi.internal.core.util.MIMEUtils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;

/**
 * AP1942
 *
 * <context>For a candidate part of a multipart/related message that is bound
 * to a mime:content that refers to global element declaration (via the element
 * attribute of the wsdl:part element)</context>
 * <assertionDescription>The bound message part is serialized within the MIME
 * part as a serialization of an XML infoset whose root element is described by
 * the referenced element.</assertionDescription>
 */
public class AP1942 extends AssertionProcess implements WSITag
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1942(BaseMessageValidator impl)
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
      if (!entryContext.getMessageEntry().isMimeContent())
      {
      	throw new AssertionNotApplicableException();
      }
      else
      {
      	MimeParts mimeParts = entryContext.getMessageEntry().getMimeParts();
      
        // If the message does not contain non-root MIME parts
        // the assertion is not applicable
        if (mimeParts.count()< 2)
          throw new AssertionNotApplicableException();
      
        // Getting an operation matched for a message
        BindingOperation bindingOperation = validator.getOperationMatch(
          entryContext.getEntry().getEntryType(),
          entryContext.getMessageEntryDocument());
        // If no one operation matches, the assertion is not applicable
        if (bindingOperation == null)
        throw new AssertionNotApplicableException();
        // Getting the corresponding extensibility elements and message
        List extElems;
        Message message;
        if (MessageEntry.TYPE_REQUEST
          .equals(entryContext.getEntry().getEntryType()))
        {
          extElems = bindingOperation.getBindingInput() == null ? null
            : bindingOperation.getBindingInput().getExtensibilityElements();
          message = bindingOperation.getOperation().getInput() == null ? null
            : bindingOperation.getOperation().getInput().getMessage();
        }
        else
        {
          extElems = bindingOperation.getBindingOutput() == null ? null
            : bindingOperation.getBindingOutput().getExtensibilityElements();
          message = bindingOperation.getOperation().getOutput() == null ? null
            : bindingOperation.getOperation().getOutput().getMessage();
        }
        // A variable that indicates a binding contains at least one
        // mime:content element that refers to global element declaration 
        boolean mimeContentFound = false;
        // Going through the message MIME parts
        Iterator iMimeParts = mimeParts.getParts().iterator();
        int i = 0;
        while (iMimeParts.hasNext())
        {
      	  i = i + 1;
          // Getting a MIME part
          MimePart mimePart = (MimePart)iMimeParts.next();
          // Getting a part name form the Content-ID header
          String partName = MIMEUtils.getMimeHeaderAttribute(
            mimePart.getHeaders(), MIMEConstants.HEADER_CONTENT_ID);
          try
          {
            int idx = partName.indexOf("=");
            if((idx != -1) && partName.startsWith("<"))
              partName = encodePartName(partName.substring(1, idx));
          }
          catch (Exception e)
          {
            // Could not extract a part name from the header,
            // proceed with the next MIME part
            continue;
          }

          // If the part is bound by a mime:content element
          if (boundToMimeContent(extElems, partName) && message != null)
          {
            // Getting the corresponding part
            Part part = (Part) message.getParts().get(partName);
            QName refName;
            // If the part refers to global element declaration
            if (part != null && (refName = part.getElementName()) != null)
            {
              mimeContentFound = true;
              // Trying to parse part content
              Document doc = null;
              try
              {
                doc = XMLUtils.parseXML(mimePart.getContent());
              }
              catch (Exception e) {}
              // If the message is not an XML infoset, the assertion failed
              if (doc == null)
              {
                throw new AssertionFailException("The bound message part of the "
                  + "MIME part number " + (i + 1) + " is invalid XML infoset.");
              }
              // Creating a QName object of the root element
              QName rootName = new QName(
                doc.getDocumentElement().getNamespaceURI(),
                doc.getDocumentElement().getLocalName());
              // If the names of the root element and the referenced element
              // are not equal, the assertion failed
              if (!refName.equals(rootName))
              {
                throw new AssertionFailException("The root element name is "
                  + rootName + ", the name of the referenced element is "
                  + refName + ".");
              }
            }
          }
        }
        // If no mime:contentS found, the assertion is not applicable
        if (!mimeContentFound)
          throw new AssertionNotApplicableException();
      }
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
   * Validates whether a part is bound by a mime:content element.
   * @param extElems a list of extensibility elements.
   * @param part a part name.
   * @return true if a part is bound by mime:content, false otherwise.
   */
  private boolean boundToMimeContent(List extElems, String part)
  {
    if (extElems != null && extElems.size() > 0)
    {
      ExtensibilityElement extElem = (ExtensibilityElement) extElems.get(0);
      // Expected the first ext elem is mime:multipartRelated
      if (extElem.getElementType().equals(WSDL_MIME_MULTIPART))
      {
        // Getting the mime:part elements of the mime:multipartRelated
        List mimeParts = ((MIMEMultipartRelated) extElem).getMIMEParts();
        for (int i = 0; i < mimeParts.size(); i++)
        {
          // Getting the mime:part element needed
          List elems = ((MIMEPart) mimeParts.get(i)).getExtensibilityElements();
          if (elems.size() > 0)
          {
            // Getting the first element
            ExtensibilityElement elem = (ExtensibilityElement) elems.get(0);
            // If the element is mime:content and it binds a part
            if (elem.getElementType().equals(WSDL_MIME_CONTENT)
              && part.equals(((MIMEContent)elem).getPart()))
            {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Encode string from %HH to UTF
   * @param str string
   * @return encoded string from %HH format
   */
  private String encodePartName(String str) {
    StringBuffer res = new StringBuffer("");
    for (int i = 0; i < str.length(); i++)
    {
      if((str.charAt(i) == '%') && (str.length() > i+2))
      {
        try
        {
          int value = Integer.parseInt(
              String.valueOf(str.charAt(i+1)) +
              String.valueOf(str.charAt(i+2)), 16);
          res.append((char) value);
          i +=2;
        }
        catch(NumberFormatException e){}
      } 
      else 
      {
        res.append(str.charAt(i));
      }
    }
    return res.toString();
  }
}