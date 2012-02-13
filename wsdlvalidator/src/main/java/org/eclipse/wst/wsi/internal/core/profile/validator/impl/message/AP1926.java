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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.BindingOperation;
import javax.wsdl.extensions.mime.MIMEContent;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.mime.MIMEPart;

import org.eclipse.wst.wsi.internal.core.WSIException;
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

/**
 * AP1926
 *
 * <context>For a candidate message</context>
 * <assertionDescription>A message includes all of the MIME parts described by
 * its WSDL MIME binding.</assertionDescription>
 */
public class AP1926 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1926(BaseMessageValidator impl)
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
      if(!entryContext.getMessageEntry().isMimeContent())
      {
        throw new AssertionNotApplicableException();
      }
      
      // get message mime parts
      MimeParts mimeParts = entryContext.getMessageEntry().getMimeParts();
      
      BindingOperation bindingOperation = validator.getOperationMatch(
          entryContext.getEntry().getEntryType(),
          entryContext.getMessageEntryDocument());

      // If there is no matched operation, the assertion is not applicable
      if (bindingOperation == null)
        throw new AssertionNotApplicableException();

      // Finding operation extensibility elems
      // in the binding depending on message type
      List extElems = null;
      if (entryContext.getMessageEntry().getType().equals(
        MessageEntry.TYPE_REQUEST)
        && bindingOperation.getBindingInput() != null)
      {
        extElems = bindingOperation
          .getBindingInput().getExtensibilityElements();
      }
      else if (entryContext.getMessageEntry().getType().equals(
        MessageEntry.TYPE_RESPONSE)
        && bindingOperation.getBindingOutput() != null)
      {
        extElems = bindingOperation
          .getBindingOutput().getExtensibilityElements();
      }
      // check list for the first element
      if((extElems == null) || (extElems.size() == 0) || 
         !(extElems.get(0) instanceof MIMEMultipartRelated))
        throw new AssertionNotApplicableException();
      
      // get list mime parts from definition
      MIMEMultipartRelated mime = (MIMEMultipartRelated) extElems.get(0);
      List parts = mime.getMIMEParts();

      if(mimeParts.getRootPart() == null || mimeParts.count()==0)
        throw new AssertionNotApplicableException();
      // skip root part
      for (int i = 1; i < parts.size(); i++)
      {
        String partName = getMIMEContentPart((MIMEPart) parts.get(i));
        // find the corresponding MIME part
        if (findMIMEPart(mimeParts.getParts(), partName) == null) 
        {            
          throw new AssertionFailException("The corresponding binding " +
              "operation \"" + bindingOperation.getName() + 
              "\" does not contain part \"" + partName + "\"");
        }
      }
    }
    catch (AssertionNotApplicableException e)
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    }
    catch (AssertionFailException e)
    {
      result = AssertionResult.RESULT_FAILED;
      failureDetail = 
        validator.createFailureDetail(e.getMessage(), entryContext);
    }
    
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }

  /**
   * find corresponding MIME part
   * @param parts array of soap MIME parts
   * @param partName part name to find
   * @return corresponding MIME part "Content-ID" value, 
   * if the part doesnt found, retuns null
   * @throws WSIException
   */
  private String findMIMEPart(Collection parts, String partName)
    throws WSIException
  {
    if(partName == null)
       return null;
    Iterator iparts = parts.iterator();
    while (iparts.hasNext())
    {
      MimePart part = (MimePart)iparts.next();
      String m_partName = MIMEUtils.getMimeHeaderAttribute(
          part.getHeaders(), MIMEConstants.HEADER_CONTENT_ID);

      // check value format <partname=UID@host>
      if ((m_partName != null) && (m_partName.startsWith("<")) && 
          (m_partName.indexOf("=") != -1) && 
          (encodePartName(m_partName.substring(1, m_partName.indexOf("="))).equals(partName)))
      {
        return m_partName;
      }
    }
    return null;
  }

  /**
   * Returns the part value from MIMEContent element
   * @param part MIMEPart element
   * @return the part value from MIMEContent element
   */
  private String getMIMEContentPart(MIMEPart part) {
    List list = part.getExtensibilityElements();
    if(list.size() == 0)
      return null;
    return ((MIMEContent) list.get(0)).getPart();
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