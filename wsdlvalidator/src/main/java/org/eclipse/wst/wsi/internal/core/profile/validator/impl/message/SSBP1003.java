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

import java.util.Map;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionFailException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionNotApplicableException;
import org.eclipse.wst.wsi.internal.core.analyzer.AssertionPassException;
import org.eclipse.wst.wsi.internal.core.profile.TestAssertion;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.AssertionProcess;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.eclipse.wst.wsi.internal.core.report.AssertionResult;
import org.eclipse.wst.wsi.internal.core.util.HTTPUtils;


/**
 * SSBP1003.
 *
 * <context>For a candidate non-multipart/related message in the log file, which has a non-empty entity-body</context>
 * <assertionDescription>
 *  The logged SOAP envelope is a UTF-8 transcript of an envelope originally encoded as UTF-8 or UTF-16. 
 *  The HTTP Content-Type header's charset value is either UTF-8 or UTF-16. Looking at the messageContent 
 *  element of the logged message, either 
 *    (1) it has a BOM attribute which maps the charset value in the Content-Type header, or 
 *    (2) it has it has an XML declaration which matches the charset value in the Content-Type header, or 
 *    (3) there is no BOM attribute and no XML declaration, and the charset value is UTF-8.
 * </assertionDescription>
 *
 * @author lauzond
 */ 
public class SSBP1003 extends AssertionProcess
{
  protected final BaseMessageValidator validator;

  /**
   * @param BaseMessageValidator
   */
  public SSBP1003(BaseMessageValidator impl)
  {
    super(impl);
    this.validator = impl;
  }

  public AssertionResult validate(
    TestAssertion testAssertion,
    EntryContext entryContext)
    throws WSIException
  {
    try
    {
      // If there is a response message for one-way operation, the assertion is not applicable
      if (validator.isOneWayResponse(entryContext))
      {
        throw new AssertionNotApplicableException();
      }

      String httpHeader = entryContext.getMessageEntry().getHTTPHeaders();

      Map httpHeaderMap = HTTPUtils.getHttpHeaderTokens(httpHeader, ":");
      String contentType = (String) httpHeaderMap.get("Content-Type".toUpperCase());

      if (contentType == null)
      {
        throw new AssertionFailException(
          "The Content-Type header is not present.");
      }

      contentType = contentType.trim();
      String charset = contentType.substring(
        contentType.indexOf(";") + 1, contentType.length());
      charset = charset.trim();
      if (!charset.startsWith("charset"))
      {
        throw new AssertionFailException(
          "Missing or bad \"charset\" attribute in the Content-Type header: "
          + charset);
      }

      String charsetValue = charset.substring(
        charset.indexOf("=") + 1, charset.length());
      if (charsetValue.startsWith("\""))
      {
        charsetValue = charsetValue.substring(1, charsetValue.length() - 1);
      }

      if (!charsetValue.equalsIgnoreCase("utf-8")
          && !charsetValue.equalsIgnoreCase("utf-16"))
      {
        throw new AssertionFailException("The value of the \"charset\" "
          + "attribute of the Content-Type header is " + contentType);
      }
      // Parse log message
      //gets first string
      int idx = entryContext.getMessageEntry().getMessage().indexOf("<?xml");
      if (idx == -1)
      {
        throw new AssertionPassException();
      }

      int idx2 = entryContext.getMessageEntry().getMessage().indexOf("?>");
      if (idx2 == -1)
      {
        throw new AssertionPassException();
      }

      String xmlDeclaration = entryContext.getMessageEntry().getMessage()
        .substring(idx, idx2 + "?>".length());

      idx = xmlDeclaration.indexOf("encoding");
      if (idx == -1)
      {
        if (charsetValue.equalsIgnoreCase("utf-8"))
        {
            throw new AssertionPassException();
        }
        else
        {
          throw new AssertionFailException("There is no XML declaration and the"
            + " charset value in the Content-Type header is not UTF-8."
            + "\nCharset value in the Content-Type header: " + charsetValue);
        }
      }

      int idxQ = xmlDeclaration.indexOf('\'', idx);
      int idxQQ = xmlDeclaration.indexOf('\"', idx);
      int idxQuote = -1;
      char qouteCh = '\0';
      if (idxQ == -1)
      {
        idxQuote = idxQQ;
        qouteCh = '\"';
      }
      else
      {
        if (idxQQ == -1)
        {
          idxQuote = idxQ;
          qouteCh = '\'';
        }
        else
        {
          if (idxQQ < idxQ)
          {
            idxQuote = idxQQ;
            qouteCh = '\"';
          }
          else
          {
            idxQuote = idxQ;
            qouteCh = '\'';
          }
        }
      }

      if (idxQuote == -1 || qouteCh == '\0')
      {
        throw new AssertionPassException();
      }

      int idxLQoute = xmlDeclaration.indexOf(qouteCh, idxQuote + 1);

      if (idxLQoute == -1)
      {
        throw new AssertionPassException();
      }

      String xmlEncoding =
        xmlDeclaration.substring(idxQuote + 1, idxLQoute);
      if (charsetValue.equalsIgnoreCase(xmlEncoding))
      {
        // If there is a BOM, then check that it is the same as the xmlEncoding
        int bom = 0;
        if ((bom = entryContext.getMessageEntry().getBOM()) != 0)
        {
          if ((bom == WSIConstants.BOM_UTF8
            && !xmlEncoding.equalsIgnoreCase("utf-8"))
            || ((bom == WSIConstants.BOM_UTF16
              && !xmlEncoding.equalsIgnoreCase("utf-16")))
            || ((bom == WSIConstants.BOM_UTF16_BIG_ENDIAN
              && !xmlEncoding.equalsIgnoreCase("utf-16"))))
          {
            throw new AssertionFailException(
              "The BOM and XML declaration do not match.");
          }
        }

        throw new AssertionPassException();
      }
      else
      {
        throw new AssertionFailException("There is an XML declaration, but its "
          + "encoding value does not match the charset value.\n"
          + "Charset value in the Content-Type header: " + charsetValue
          + "\nEncoding in the XML declaration: " + xmlEncoding);
      }
    }
    catch (AssertionPassException e)
    {
      result = AssertionResult.RESULT_PASSED;
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
}