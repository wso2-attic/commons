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
package org.eclipse.wst.wsi.internal.core.monitor;

import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.monitor.config.Redirect;
import org.eclipse.wst.wsi.internal.core.util.Utils;

/**
 * A socket connection.
 *
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class SocketConnection extends Thread
{
  protected Monitor monitor;
  protected Redirect redirect;
  protected Socket inSocket;
  protected Socket outSocket;

  protected String httpProxyHost = null;
  protected int httpProxyPort;

  protected SocketHandler requestSocketHandler = null;
  protected SocketHandler responseSocketHandler = null;

  private Vector listeners = new Vector(); // SS

  /**
   * Create socket connection.
   * @param monitor  a Monitor object.
   * @param redirect a Redirect object.
   * @param inSocket in socket.
   */
  public SocketConnection(Monitor monitor, Redirect redirect, Socket inSocket)
  {
    this.monitor = monitor;
    this.redirect = redirect;
    this.inSocket = inSocket;

    // Start processing the connection
    start();
  }

  /**
   * Process the socket connection.
   */
  public void run()
  {
    // Get the HTTP proxy settings if there are any set (not used currently)
    loadHttpProxySettings();

    try
    {
      // Open the connection to the server
      this.outSocket = new Socket(redirect.getToHost(), redirect.getToPort());

      // Set the read timeout for the input socket
      this.inSocket.setSoTimeout(redirect.getReadTimeoutSeconds() * 1000);

      // Get the next conversation ID
      int conversationId = monitor.getNextConversationId();

      // Create the socket handlers which are used to send the data from
      // the client to the server
      requestSocketHandler =
        new SocketHandler(
          this,
          MessageEntry.TYPE_REQUEST,
          conversationId,
          redirect.getToHost(),
          this.inSocket,
          this.outSocket,
          redirect.getReadTimeoutSeconds());
      responseSocketHandler =
        new SocketHandler(
          this,
          MessageEntry.TYPE_RESPONSE,
          conversationId,
          redirect.getToHost(),
          this.outSocket,
          this.inSocket,
          redirect.getReadTimeoutSeconds());

      // Tell each socketHandler about the other one
      requestSocketHandler.setPairedSocketHandler(responseSocketHandler);
      responseSocketHandler.setPairedSocketHandler(requestSocketHandler);
    }

    catch (Exception e)
    {
      Monitor.staticPrintMessage(
        "error04",
        redirect.getToHost(),
        "Monitor cannot connect to a redirect host:");

      shutdown();

      try
      {
        this.inSocket.close();
      }
      catch (Throwable t)
      {
      }
    }
  }

  /**
   * Log message.
   * @param conversationID      the coversation id.
   * @param connectionType      the connection type.
   * @param timestamp           the timestamp.
   * @param senderHostAndPort   the sender host and port.
   * @param receiverHostAndPort the receiver host and port.
   * @param messageBuffer       the message.
   * @param bom                 the BOM.
   * @param encoding            the encoding.
   * @throws WSIException if  there is a problem logging message.
  */
  public void logMessage(
    int conversationID,
    String connectionType,
    String timestamp,
    String senderHostAndPort,
    String receiverHostAndPort,
    StringBuffer messageBuffer,
    int bom,
    String encoding)
    throws WSIException
  {
    MessageEntry messageEntry = null;

    // Create message entry
    messageEntry =
      createMessageEntry(
        conversationID,
        connectionType,
        timestamp,
        senderHostAndPort,
        receiverHostAndPort,
        messageBuffer.toString(),
        bom,
        encoding);

    // DEBUG:
    //debug("logMessage", " messageEntry: " + messageEntry);

    // Add entry to the queue
    monitor.getMessageEntryQueue().addMessageEntry(messageEntry);
  }

  /**
   * Create a log entry from all of the data that was gathered.
   */
  private MessageEntry createMessageEntry(
    int conversationID,
    String messageType,
    String timestamp,
    String senderHostAndPort,
    String receiverHostAndPort,
    String message,
    int bom,
    String encoding)
    throws WSIException
  {
    // Create log entry object
    MessageEntry messageEntry = monitor.getLog().createLogEntry();

    // Set input information in log entry
    // NOTE:  The ID is set just before the log entry is written to the log file in LogEntryQueue
    //logEntry.setId(id);
    messageEntry.setConversationId(String.valueOf(conversationID));
    messageEntry.setType(messageType);
    messageEntry.setTimestamp(timestamp);
    messageEntry.setSenderHostAndPort(senderHostAndPort);
    messageEntry.setReceiverHostAndPort(receiverHostAndPort);
    messageEntry.setEncoding(encoding);

    // Get HTTP headers from full message
    String httpHeaders = Utils.getHTTPHeaders(message);
    messageEntry.setHTTPHeaders(httpHeaders);
	
   if (Utils.isMultipartRelatedMessage(httpHeaders))
    {
    	MimeParts mimeParts = Utils.parseMultipartRelatedMessage(message, httpHeaders, encoding);
    	if (mimeParts == null)
    	{
    	  // problem creating Mimeparts -- treat it as simple SOAP message
    	  String content = Utils.getContent(message);
    	  messageEntry.setMessage(content);
    	  messageEntry.setMimeContent(false);
    	}
    	else
    	{
          messageEntry.setMimeParts(mimeParts);
          messageEntry.setMimeContent(true);
    	}
    }
    else
    {
      // Get the message content
      String content = Utils.getContent(message);
      messageEntry.setMessage(content);
      messageEntry.setMimeContent(false);
    }

    // Set the BOM, if there is one
    if (bom != 0)
      messageEntry.setBOM(bom);

    return messageEntry;
  }

  /**
   * Load HTTP proxy settings.
   */
  public void loadHttpProxySettings()
  {
    // Get the HTTP proxy host setting
    this.httpProxyHost = System.getProperty("http.proxyHost");
    if (this.httpProxyHost != null && httpProxyHost.equals(""))
      this.httpProxyHost = null;

    // If there was an HTTP proxy host setting, then get the port
    if (this.httpProxyHost != null)
    {
      String portString = System.getProperty("http.proxyPort");

      // Set default to 80
      if (portString == null || portString.equals(""))
        this.httpProxyPort = 80;
      else
        httpProxyPort = Integer.parseInt(portString);
    }
  }

  Monitor getMonitor()
  {
    return this.monitor;
  }

  synchronized void wakeUp()
  {
    fireConnectionClosed(); // SS
    notifyAll();
  }

  /**
   * Shutdown.
   */
  public void shutdown()
  {
    if (this.requestSocketHandler != null)
      this.requestSocketHandler.shutdown();
    if (this.responseSocketHandler != null)
      this.responseSocketHandler.shutdown();
  }

  // ==== SS start ====
  /**
   * Add connection event listener.
   * @param listener event listener.
   * @see #removeConnectionListener
   */
  public void addConnectionListener(ConnectionListener listener)
  {
    listeners.add(listener);
  }
  /**
   * Remove connection event listener.
   * @param listener event listener.
   * @see #addConnectionListener
   */
  public void removeConnectionListener(ConnectionListener listener)
  {
    listeners.remove(listener);
  }
  /**
   * Notify listeners on connection close.
   */
  private void fireConnectionClosed()
  {
    for (Iterator i = listeners.iterator(); i.hasNext();)
    {
      ConnectionListener listener = (ConnectionListener) i.next();
      listener.connectionClosed(this);
    }
  }
  // ==== SS end ====
}
