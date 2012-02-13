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

import java.net.MalformedURLException;
import java.net.URL;
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
 * AP1933
 *
 * <context>For a candidate message</context>
 * <assertionDescription>If a description binds a wsdl:message part to a
 * mime:content element, then the corresponding MIME part in a message has a
 * content-id header with a globally unique value of the form
 * &lt;partname=ID@hostname&gt;, where 'partname' is the value of the name attribute of
 * the wsdl:part element referenced by the mime:content.</assertionDescription>
 */
public class AP1933 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1933(BaseMessageValidator impl)
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
    throws WSIException, AssertionFailException
  {
    if(partName == null)
       return null;
    
    Iterator iparts = parts.iterator();
    int i = 0;
    while (iparts.hasNext())
    {
      i = i + 1;
      MimePart part = (MimePart)iparts.next();
      String m_partName = MIMEUtils.getMimeHeaderAttribute(
          part.getHeaders(), MIMEConstants.HEADER_CONTENT_ID);

      // check value format <partname=UID@hostname>
      if ((m_partName != null) && m_partName.startsWith("<") && m_partName.endsWith(">")) 
      {
        int idx, atIdx;
        String enPartName = null;
        String uid = null;
        String host = null;
        
        // get part name from content-id
        if ((idx = m_partName.indexOf("=")) != -1) 
        {
          enPartName = encodePartName(m_partName.substring(1, idx)); 
        }
        // get uid and hostname from content-id
        if (((atIdx = m_partName.indexOf("@")) != -1) && (atIdx > idx)) 
        {
          uid = m_partName.substring(idx+1, atIdx);
          host = m_partName.substring(atIdx+1, m_partName.length()-1);
        }
        // if part names match, validate format
        if((enPartName != null) && enPartName.equals(partName))
        {
          if((uid == null) || (uid.length() == 0))
            throw new AssertionFailException(
                "The \"Content-ID\" attribute of the part \""+(i+1)+
                "\"contains value \""+ m_partName+
                "\" which has a incorrect UID format");
          if(host == null) 
            throw new AssertionFailException(
                "The \"Content-ID\" attribute of the part \""+(i+1)+
                "\"contains value \""+ m_partName+
                "\" which has a incorrect host format");
          // check for malformed URL
          try
          {
            new URL("HTTP", host, "");
          }
          catch (MalformedURLException e)
          {
            throw new AssertionFailException(
                "The \"Content-ID\" attribute of the part \""+(i+1)+
                "\"contains value \""+ m_partName+
                "\" which has a incorrect host format");
          }
          return m_partName;
        } 
      }
    }
    return null;
  }
  
  /**
   * Returns the part value from MIMEContent element
   * @param part MIMEPart element
   * @return the part value from MIMEContent element
   */
  private String getMIMEContentPart(MIMEPart part) 
  {
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