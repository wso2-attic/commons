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
package org.eclipse.wst.wsi.internal.core.log;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;
import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;

/**
* Message entry interface.
*
* @version 1.0.1
* @author Peter Brittenham (peterbr@us.ibm.com)
*/
public interface MessageEntry extends DocumentElement
{
 /**
  * Element name.
  */
 public static final String ELEM_NAME = WSIConstants.ELEM_MESSAGE_ENTRY;

 /**
  * QName.
  */
 public static final QName QNAME =
   new QName(WSIConstants.NS_URI_WSI_LOG, ELEM_NAME);

 /**
  * Type of log entry.
  */
 public static final String TYPE_REQUEST = "request";
 public static final String TYPE_RESPONSE = "response";

 /**
  * Gets the id.
  * @return the id.
  * @see #setId
  */
 public String getId();

 /**
  * Sets the id.
  * @param id the identifier for the log entry.
  * @see #getId
  */
 public void setId(String id);

 /**
  * Gets the conversation id.
  * @return the conversation id.
  * @see #setConversationId
  */
 public String getConversationId();

 /**
  * Sets the conversation id.
  * @param conversationId the conversation id.
  * @see #getConversationId
  */
 public void setConversationId(String conversationId);

 /**
  * Gets the timestamp.
  * @return the timestamp.
  * @see #setTimestamp
  */
 public String getTimestamp();

 /**
  * Sets the timestamp.
  * @param timestamp the timestamp.
  * @see #getTimestamp
  */
 public void setTimestamp(String timestamp);

 /**
  * Gets the type.
  * @return the type.
  * @see #setType
  */
 public String getType();

 /**
  * Sets the type.
  * @param type the type.
  * @see #getType
  */
 public void setType(String type);

 /**
  * Gets the sender host and port.
  * @return  the sender host and port.
  * @see #setSenderHostAndPort
  */
 public String getSenderHostAndPort();

 /**
  * Sets the sender host and port.
  * @param senderHostAndPort the sender host and port.
  * @see #getSenderHostAndPort
  */
 public void setSenderHostAndPort(String senderHostAndPort);

 /**
  * Gets the receiver host and port.
  * @return the receiver host and port.
  * @see #setReceiverHostAndPort
  */
 public String getReceiverHostAndPort();

 /**
  * Sets the receiver host and port.
  * @param receiverHostAndPort the receiver host and port.
  * @see #getReceiverHostAndPort
  */
 public void setReceiverHostAndPort(String receiverHostAndPort);

 /**
  * Gets the message.
  * @return the message.
  * @see #setMessage
  */
 public String getMessage();

 /**
  * Sets the message.
  * @param message the message.
  * @see #getMessage
  */
 public void setMessage(String message);

 /**
  * Sets the encoding.
  * @param encoding the encoding.
  * @see #getMessage
  */
 public void setEncoding(String encoding);

 /**
  * Gets the BOM.
  * @return the BOM.
  * @see #setBOM
  */
 public int getBOM();

 /**
  * Sets the BOM.
  * @param bom the BOM.
  * @see #getBOM
  */
 public void setBOM(int bom);

 /**
  * Gets the HTTP headers.
  * @return the HTTP headers.
  * @see #setHTTPHeaders
  */
 public String getHTTPHeaders();

 /**
  * Sets the HTTP headers.
  * @param httpHeaders the HTTP headers.
  * @see #getHTTPHeaders
  */
 public void setHTTPHeaders(String httpHeaders);

 /**
  * Gets element location.
  * @return element location.
  * @see #setElementLocation
  */
 public ElementLocation getElementLocation();

 /**
  * Sets element location.
  * @param elementLocation element location.
  * @see #getElementLocation
  */
 public void setElementLocation(ElementLocation elementLocation);
 
 /**
  * If content is MIME type returns true, otherwise false  
  * @return if content is MIME type returns true, otherwise false
  */
 public boolean isMimeContent();
 
 /**
  * Set flag to determine if content is MIME type. 
  * @param isMimeContent a booolean value
  */
 public void setMimeContent(boolean isMimeContent);

 /**
  * Returns array of the MIME parts of the message.
  * @return array of the MIME parts of the message.
  * @see #isMIMEContent() 
  */
 public MimeParts getMimeParts();
 
 /**
  * Sets the MIME parts of the message.
  * @param mimeParts he MIME parts of the message.
  * @see #getMimeParts
  */
 public void setMimeParts(MimeParts mimeParts);

}
