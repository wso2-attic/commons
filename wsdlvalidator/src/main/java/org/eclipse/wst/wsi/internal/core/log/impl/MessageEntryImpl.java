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
package org.eclipse.wst.wsi.internal.core.log.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;

/**
 * Message entry implementation.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class MessageEntryImpl implements MessageEntry
{
  /**
   * Log entry id.
   */
  protected String id;

  protected String conversationId;
  protected String type;
  protected String timestamp;
  protected String senderHostAndPort;
  protected String receiverHostAndPort;
  protected String message = "";
  protected String httpHeaders = "";
  protected int bom;
  protected String encoding = "";
  protected boolean isMimeContent = false;
  protected MimeParts mimeParts = new MimePartsImpl();

  protected ElementLocation elementLocation = null;

  /**
   * Empty constructor.
   */
  public MessageEntryImpl()
  {
  }
  
  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#getId()
   */
  public String getId()
  {
    return this.id;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setId(String)
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#getConversationId()
   */
  public String getConversationId()
  {
    return this.conversationId;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setConversationId(String)
   */
  public void setConversationId(String conversationId)
  {
    this.conversationId = conversationId;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#getTimestamp()
   */
  public String getTimestamp()
  {
    return this.timestamp;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setTimestamp(String)
   */
  public void setTimestamp(String timestamp)
  {
    this.timestamp = timestamp;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#getType()
   */
  public String getType()
  {
    return this.type;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setType(String)
   */
  public void setType(String type)
  {
    this.type = type;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#getSenderHostAndPort()
   */
  public String getSenderHostAndPort()
  {
    return this.senderHostAndPort;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setSenderHostAndPort(String)
   */
  public void setSenderHostAndPort(String senderHostAndPort)
  {
    this.senderHostAndPort = senderHostAndPort;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#getReceiverHostAndPort()
   */
  public String getReceiverHostAndPort()
  {
    return this.receiverHostAndPort;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setReceiverHostAndPort(String)
   */
  public void setReceiverHostAndPort(String receiverHostAndPort)
  {
    this.receiverHostAndPort = receiverHostAndPort;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#getMessage()
   */
  public String getMessage()
  {
  	if (!this.isMimeContent)
       return this.message;
  	else
  	{
  	   if (this.mimeParts.getRootPart() == null) return "";
  	   else return this.mimeParts.getRootPart().getContent();
  	}
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setMessage(String)
   */
  public void setMessage(String message)
  {
    this.message = message;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setEncoding(String)
   */
  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MessageEntry#getBOM()
   */
  public int getBOM()
  {
    return bom;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MessageEntry#setBOM(int)
   */
  public void setBOM(int bom)
  {
    this.bom = bom;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#getHTTPHeaders()
   */
  public String getHTTPHeaders()
  {
    return this.httpHeaders;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.LogEntry#setHTTPHeaders(String)
   */
  public void setHTTPHeaders(String httpHeaders)
  {
    this.httpHeaders = httpHeaders;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MessageEntry#getElementLocation()
   */
  public ElementLocation getElementLocation()
  {
    return this.elementLocation;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MessageEntry#setElementLocation(org.wsi.xml.dom.ElementLocation)
   */
  public void setElementLocation(ElementLocation elementLocation)
  {
    this.elementLocation = elementLocation;
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    return toXMLString(WSIConstants.NS_NAME_WSI_LOG);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentElement#toXMLString(String)
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    String nsName = namespaceName;
    if ((!nsName.equals("") && (!nsName.endsWith(":"))))
      nsName += ":";

    // ADD: Need to add way to indicate XML schema type for logEntry
    // Add log entry
    pw.print("<" + nsName + ELEM_NAME + " ");
    pw.print(
      "xsi:type=\"httpMessageEntry\" ");
    pw.print(WSIConstants.ATTR_ID_UC + "=\"" + id + "\" ");
    pw.print(
      WSIConstants.ATTR_CONVERSATION_ID + "=\"" + conversationId + "\" ");
    pw.print(WSIConstants.ATTR_TYPE + "=\"" + type + "\" ");
    pw.println(WSIConstants.ATTR_TIMESTAMP + "=\"" + timestamp + "\">");

    if (isMimeContent())
    {
        // Add message content with attachments element
        pw.print("<" + nsName + WSIConstants.ELEM_MESSAGE_CONTENT_WITH_ATTACHMENTS);
        if (bom != 0)
          pw.print(" " + WSIConstants.ATTR_BOM + "=\"" + bom + "\"");
        pw.print(">");

        Collection partList = mimeParts.getParts();
        if (!partList.isEmpty())
        {
         	Iterator iMimeParts = partList.iterator();
        	while (iMimeParts.hasNext())
            {
        	  MimePart mimePart = (MimePart)iMimeParts.next();
         	  pw.print(mimePart.toXMLString(namespaceName));
            }
        }

        // Add end message element
        pw.println("</" + nsName + WSIConstants.ELEM_MESSAGE_CONTENT_WITH_ATTACHMENTS + ">");
    }
    else
    {
      // Add message content element
      pw.print("<" + nsName + WSIConstants.ELEM_MESSAGE_CONTENT);
      if (bom != 0)
        pw.print(" " + WSIConstants.ATTR_BOM + "=\"" + bom + "\"");
      pw.print(">");

      // Add encoded message
      pw.print(XMLUtils.xmlEscapedString(message));
      
      // Add end message element
      pw.println("</" + nsName + WSIConstants.ELEM_MESSAGE_CONTENT + ">");
    }

    // Add senderIPPort element
    pw.println(
      "<"
        + nsName
        + WSIConstants.ELEM_SENDER_HOST_AND_PORT
        + ">"
        + senderHostAndPort
        + "</"
        + nsName
        + WSIConstants.ELEM_SENDER_HOST_AND_PORT
        + ">");

    // Add receiverIP element
    pw.println(
      "<"
        + nsName
        + WSIConstants.ELEM_RECEIVER_HOST_AND_PORT
        + ">"
        + receiverHostAndPort
        + "</"
        + nsName
        + WSIConstants.ELEM_RECEIVER_HOST_AND_PORT
        + ">");

    // Add HTTPHeaders element
    pw.print("<" + nsName + WSIConstants.ELEM_HTTP_HEADERS + ">");

    // ADD: Does this string need to be XML encoded?
    // Add HTTP headers
    pw.print(XMLUtils.xmlEscapedString(httpHeaders));

    // End HTTPHeaders element
    pw.println("</" + nsName + WSIConstants.ELEM_HTTP_HEADERS + ">");

    // End log entry
    pw.println("</" + nsName + ELEM_NAME + ">");

    // Return string
    return sw.toString();
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MessageEntry#isMimeContent()
   */
  public boolean isMimeContent()
  {
    return isMimeContent;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MessageEntry#setMimeContent()
   */
  public void setMimeContent(boolean isMimeContent)
  {
    this.isMimeContent = isMimeContent;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MessageEntry#getMIMEParts()
   */
  public MimeParts getMimeParts()
  {
    return mimeParts;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.log.MessageEntry#setMimeParts()
   */
  public void setMimeParts(MimeParts mimeParts)
  {
    this.mimeParts = mimeParts;
  }

}
