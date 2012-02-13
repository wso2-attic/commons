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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPConstants;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;
import org.eclipse.wst.wsi.internal.core.util.Utils;

/**
 * AP1935
 *
 * <context>For a candidate part of a multipart/related message</context>
 * <assertionDescription>The encoding of the body of a part in a
 * multipart/related message conforms to the encoding indicated by the
 * Content-Transfer-Encoding field-value,
 * as specified by RFC2045.</assertionDescription>
 */
public class AP1935 extends AssertionProcess
{
  private final BaseMessageValidator validator;

  /**
   * @param WSDLValidatorImpl
   */
  public AP1935(BaseMessageValidator impl)
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
    if(!entryContext.getMessageEntry().isMimeContent())
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    } 
    else 
    {
      // get MIME parts
      MimeParts parts = entryContext.getMessageEntry().getMimeParts();
      if(parts.count() == 0)
      {
        result = AssertionResult.RESULT_NOT_APPLICABLE;
      }
      else
      {
        // check each part for the encoding match
      	Iterator iparts = parts.getParts().iterator();
      	int i = 0;
      	MimePart root = parts.getRootPart();
        while (iparts.hasNext())
        {
          i = 1;
          try
          {
        	MimePart part = (MimePart)iparts.next();
        	
            // get encoding from header
            String encoding =  HTTPUtils.getHttpHeaderAttribute(part.getHeaders(), 
            	HTTPConstants.HEADER_CONTENT_TRANSFER_ENCODING);
          
           	if ((part == root) || 
           		((encoding != null) && encoding.equalsIgnoreCase("base64")))
              checkPart(part, encoding, false);
          	else 
          	  checkPart(part, encoding, true);
          } catch (AssertionFailException e)
          {
            result = AssertionResult.RESULT_FAILED;
            failureDetail = validator.createFailureDetail(
                "part "+(i+1)+" Error: " + e.getMessage(), entryContext);
          }
        }
      }
    }
    // Return assertion result
    return validator.createAssertionResult(
      testAssertion, result, failureDetail);
  }
  /**
   * Check message entry to encoding conformity 
   * @param entry message entry
   * @throws AssertionFailException if message does not encoding conformity  
   * @throws WSIException 
   */
  private void checkPart(MimePart part, String  encoding, boolean encoded) 
    throws AssertionFailException, WSIException
  {
    String content = null;
    if (encoded)
       content = new String(Utils.decodeBase64(part.getContent()));
     else
    	content = part.getContent();
    
    if(encoding == null) 
    {
      result = AssertionResult.RESULT_NOT_APPLICABLE;
    // check 7bit
    } else if(encoding.equalsIgnoreCase("7bit")) {
      checkOn7bit(content);
    // check 8bit
    } else if(encoding.equalsIgnoreCase("8bit")) {
      checkOn8bit(content);
    // check quoted-printable
    } else if(encoding.equalsIgnoreCase("quoted-printable")) {
      checkOnQuotedPrintable(content);
    // check base64
    } else if(encoding.equalsIgnoreCase("base64")) {
      checkOnBase64(content);
    }
    // we dont check binary encoding, since message can contains any chars 
  }

  /**
   * Validate a 7bit encoded message (RFC2045)
   * @param message message to check
   * @throws AssertionFailException if message does not conform
   */
  private void checkOn7bit(String message) 
    throws AssertionFailException
  {
    String[] strs = split(message);
    for (int i = 0; i < strs.length; i++)
    {
      String str = strs[i];
      
      // check string length
      if(str.length() > 998) 
      {
        throw new AssertionFailException("The length (" + str.length() + 
            ") of the line (" + (i+1) + ") greater than 998");
      }
      // No octets with decimal values greater than 127
      // are allowed and neither are NULs (octets with decimal value 0).  CR
      //(decimal value 13) and LF (decimal value 10) octets only occur as
      // part of CRLF line separation sequences.
      char[] chars = str.toCharArray();
      for (int j = 0; j < chars.length; j++)
      {
        if((chars[j] > 127) || (chars[j] == 0) || 
            (chars[j] == 10) || (chars[j] == 13))
        {
          throw new AssertionFailException("The char (" + chars[j] + 
              ")[code=" + (byte) chars[j] + " position=" + j + 
              "] does not allows in 7bit encoding content");
        }
      }
    }
  }

  /**
   * Validate an 8bit encoded message (RFC2045)
   * @param message message to check
   * @throws AssertionFailException if message does not conform
   */
  private void checkOn8bit(String message) 
    throws AssertionFailException
  {
    String[] strs = split(message);
    for (int i = 0; i < strs.length; i++)
    {
      String str = strs[i];
      
      // check string length
      if(str.length() > 998) 
      {
        throw new AssertionFailException("The length (" + str.length() + 
            ") of the line (" + (i+1) + ") greater than 998");
      }
      // octets with decimal values greater than 127
      // may be used.  As with "7bit data" CR and LF octets only occur as part
      // of CRLF line separation sequences and no NULs are allowed. 
      char[] chars = str.toCharArray();
      for (int j = 0; j < chars.length; j++)
      {
        if((chars[j] == 0) || (chars[j] == 10) || (chars[j] == 13))
        {
          throw new AssertionFailException("The char (" + chars[j] + 
              ")[code=" + (byte) chars[j] + " position=" + j + 
              "] does not allows in 8bit encoding content");
        }
      }
    }
  }

  /**
   * Validate a quoted-printable encoded message (RFC2045)
   * @param message message to check
   * @throws AssertionFailException if message does not conform
   */
  private void checkOnQuotedPrintable(String message)
    throws AssertionFailException
  {
    String[] strs = split(message);
    for (int i = 0; i < strs.length; i++)
    {
      // check length
      // RFC2045
      // (5)   (Soft Line Breaks) The Quoted-Printable encoding
      //REQUIRES that encoded lines be no more than 76
      //characters long.  If longer lines are to be encoded
      //with the Quoted-Printable encoding, "soft" line breaks
      //must be used.  An equal sign as the last character on a
      //encoded line indicates such a non-significant ("soft")
      //line break in the encoded text.
      if(((strs[i].indexOf("\t") != -1) || (strs[i].indexOf(" ") != -1)) &&
          (strs[i].length() > 76))
      {
        throw new AssertionFailException("The length (" + strs[i].length() + 
            ") of the line (" + (i+1) + 
            ") greater than 76, \"soft\" line breaks must be used");
      }

      char[] chars = strs[i].toCharArray();
      for (int j = 0; j < chars.length; j++)
      {
        //(1)   (General 8bit representation) Any octet, except a CR or
        //LF that is part of a CRLF line break of the canonical
        //(standard) form of the data being encoded, may be
        //represented by an "=" followed by a two digit
        //hexadecimal representation of the octet's value.  The
        //digits of the hexadecimal alphabet, for this purpose,
        //are "0123456789ABCDEF".  Uppercase letters must be
        //used; lowercase letters are not allowed.  Thus, for
        //example, the decimal value 12 (US-ASCII form feed) can
        //be represented by "=0C", and the decimal value 61 (US-
        //ASCII EQUAL SIGN) can be represented by "=3D".  This
        //rule must be followed except when the following rules
        //allow an alternative encoding.
        // (2)   (Literal representation) Octets with decimal values of
        //33 through 60 inclusive, and 62 through 126, inclusive,
        //MAY be represented as the US-ASCII characters which
        //correspond to those octets (EXCLAMATION POINT through
        //LESS THAN, and GREATER THAN through TILDE,
        //respectively).
        if((chars[j] == 61) && (chars.length > j+2)) 
        {
          if(!isHex(chars[j+1]) || !isHex(chars[j+2]))
          {
            throw new AssertionFailException("the quoted char (" + 
                 chars[j] + chars[j+1] + chars[j+2] + ") is incorrect");
          } else {
            j += 2;
          }
        } 
        // check for space and tab
        else if((chars[j] != 9) && (chars[j] != 32))
        {
          // check invalid symbol
          if((chars[j] == 0) || (chars[j] == 10) || (chars[j] == 13) ||
             (chars[j] < 33) || (chars[j] > 126) || (chars[j] == 61))
          {
            throw new AssertionFailException("The char (" + chars[j] + 
                ")[code=" + (byte) chars[j] + " position=" + j + 
                "] must be quoted");
          }
        }
      }
    }
  }

  /**
   * Validate a base64 encoded message (RFC3548)
   * @param message message to check
   * @throws AssertionFailException if message does not conform
   */
  private void checkOnBase64(String message)
  throws AssertionFailException
  {
    String[] strs = split(message);
    for (int i = 0; i < strs.length; i++)
    {
      String str = strs[i];
      
      // check string length
      if(str.length() > 76) 
      {
        throw new AssertionFailException("The length (" + str.length() + 
            ") of the line (" + (i+1) + ") greater than 998");
      }
      // check for "ABCDEFGHIJKLMNOPQRSTUVWXYZabcefghijklmnopqrstuvwxyz0123456789/+"
      char[] chars = str.toCharArray();
      for (int j = 0; j < chars.length; j++)
      {
        char c = chars[i];
        if((c < 47) || (c > 122) || ((c > 57) && (c < 65)) || 
           ((c > 90) && (c < 97)))
        {
          throw new AssertionFailException("The char (" + chars[j] + 
              ")[code=" + (byte) chars[j] + " position=" + j + 
              "] does not allows in base64 encoding content");
        }
      }
    }
  }
  
  /**
   * split string to array of strings and use as delimeter CRLF
   * @param str original string
   * @return array of strings
   */
  private String[] split(String str)
  {
    ArrayList list = new ArrayList();
    for(int idx = str.indexOf("\r\n"); idx != -1; idx = str.indexOf("\r\n"))
    {
      list.add(str.substring(0, idx));
      str = str.substring(idx+2);
    }
    list.add(str);
    return (String[]) list.toArray(new String[list.size()]);
  }
  
  /**
   * Returns true if byte is "0123456789ABCDEF" range, false othewise
   * @param c char
   * @return true if byte is "0123456789ABCDEF" range, false othewise
   */
  private boolean isHex(char c) {
    return (((c >= 48) && (c <= 57)) || ((c >= 65) && (c <= 70))); 
  }
}