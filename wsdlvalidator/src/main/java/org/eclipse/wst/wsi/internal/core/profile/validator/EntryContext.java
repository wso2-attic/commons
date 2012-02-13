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
package org.eclipse.wst.wsi.internal.core.profile.validator;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.analyzer.AnalyzerContext;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.util.EntryType;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLDocument;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;

/**
 * This class contains the target information needed by a test assertion.
 * 
 * @version 1.0.1
 * @author Peter Brittenham  (peterbr@us.ibm.com)
 * @author Graham Turrell (gturrell@uk.ibm.com)
 */
public class EntryContext
{
  protected Entry entry = null;

  protected MessageEntry request = null;
  protected MessageEntry response = null;
  protected MessageEntry messageEntry = null;

  protected Document requestDocument = null;
  protected Document responseDocument = null;
  protected Document messageEntryDocument = null;

  protected WSDLDocument wsdlDocument = null;

  protected AnalyzerContext analyzerContext = null;

  /**
   * Create entry context.
   * @param entry    a message entry.
   * @param request  the request.
   * @param response the response.
   */
  public EntryContext(Entry entry, MessageEntry request, MessageEntry response)
  {
    this(entry, request, response, null);
  }

  /**
   * Create entry context.
   * @param entry           a message entry.
   * @param request         the request.
   * @param response        the response.
   * @param analyzerContext the analyzerContext.
   */
  public EntryContext(
    Entry entry,
    MessageEntry request,
    MessageEntry response,
    AnalyzerContext analyzerContext)
  {
    this.entry = entry;
    this.request = request;
    this.response = response;
    this.analyzerContext = analyzerContext;

    try
    {
      // Parse all documents
      if(request.isMimeContent()) 
      {
      	MimePart part = request.getMimeParts().getRootPart();
        if ((part != null) && (part.getContent().length() > 0))
          requestDocument = XMLUtils.parseXML(part.getContent());
      }
      else 
      {
        if (request.getMessage().length() > 0)
          requestDocument = XMLUtils.parseXML(request.getMessage());
      }

      if (response != null) {
          if(response.isMimeContent()) 
          {
          	MimePart part = response.getMimeParts().getRootPart();
          	if ((part != null) && (part.getContent().length() > 0))
              responseDocument = XMLUtils.parseXML(part.getContent());
          }
          else 
          {
            if (response.getMessage().length() > 0)
              responseDocument = XMLUtils.parseXML(response.getMessage());
          }
      }
    }

    catch (WSIException we)
    {
    }

    // need some exception handling in here and more careful checking
    if (entry.getEntryType().equals(EntryType.getEntryType(MessageValidator.TYPE_MESSAGE_REQUEST)))
    {
      this.messageEntry = request;
      this.messageEntryDocument = requestDocument;
    }
    else
    {
      this.messageEntry = response;
      this.messageEntryDocument = responseDocument;
    }
  }

  /**
   * Create entry context.
   * @param entry a message entry.
   * @param wsdlDocument a WSDL document.
   */
  public EntryContext(Entry entry, WSDLDocument wsdlDocument)
  {
    this.entry = entry;
    this.wsdlDocument = wsdlDocument;
  }

  /**
   * Create entry context.
   * @param entry a message entry.
   * @param analyzerContext the analyzerContext.
   */
  public EntryContext(Entry entry, AnalyzerContext analyzerContext)
  {
    this.entry = entry;
    this.analyzerContext = analyzerContext;
  }

  /**
   * Returns the entry. 
   * @return the entry. 
   */
  public Entry getEntry()
  {
    return this.entry;
  }

  /**
   * Returns the request.
   * @return the request.
   */
  public MessageEntry getRequest()
  {
    return request;
  }

  /**
   * Returns the response.
   * @return the response.
   */
  public MessageEntry getResponse()
  {
    return response;
  }

  /**
   * Returns the log entry which is not null.
   * @return the log entry.
   */
  public MessageEntry getMessageEntry()
  {
    return messageEntry;
  }

  /**
   * Returns the request.
   * @return MessageEntry
   */
  public Document getRequestDocument()
  {
    return requestDocument;
  }

  /**
   * Returns the response.
   * @return the response.
   */
  public Document getResponseDocument()
  {
    return responseDocument;
  }

  /**
   * Returns the log entry which is not null.
   * @return the log entry.
   */
  public Document getMessageEntryDocument()
  {
    return messageEntryDocument;
  }

  /**
   * Returns the WSDL document.
   * @return the WSDL document.
   */
  public WSDLDocument getWSDLDocument()
  {
    return wsdlDocument;
  }

  /**
   * Returns analyzerContext.
   * @return analyzerContext
   */
  public AnalyzerContext getAnalyzerContext()
  {
    return this.analyzerContext;
  }
}
