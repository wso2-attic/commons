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

import java.io.StringReader;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.log.LogWriter;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;

/**
 * This class is the queue that is used to write request and response messages
 * to the log file in the correct order.
 *
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class MessageEntryQueue
{
  /**
   * Request type message entry queue.
   */
  protected Vector requestList = new Vector();

  /**
   * Response type message entry queue.
   */
  protected Vector responseList = new Vector();

  /**
   * Monitor.
   */
  protected Monitor monitor = null;

  /**
   * Log file.
   */
  protected Log log = null;

  /**
   * Log writer.
   */
  protected LogWriter logWriter = null;

  /**
   * List lock.
   */
  protected String listLock = "listLock";

  /**
   * Message ID which is incremented when a new log entry is added to the log file.
   */
  protected int messageId = 1;

  /**
   * Create message entry queue.
   * @param monitor a monitor.
   * @param log the log.
   * @param logWriter a log writer.
   */
  public MessageEntryQueue(Monitor monitor, Log log, LogWriter logWriter)
  {
    // Save input references
    this.monitor = monitor;
    this.log = log;
    this.logWriter = logWriter;
  }

  /**
   * Add log entry to queue.
   * @param messageEntry a message entry.
   * @throws WSIException is there is a problem adding the log 
   *         entry to the queue.
   */
  public synchronized void addMessageEntry(MessageEntry messageEntry)
    throws WSIException
  {
    synchronized (listLock)
    {
      // If this is a request type of message entry, then add to request list
      if (messageEntry.getType().equals(MessageEntry.TYPE_REQUEST))
      {
        requestList.add(messageEntry);
      }

      // Otherwise this is a response type of message entry
      else
      {
        responseList.add(messageEntry);
      }
    }

    // Tell the log entry writer thread to run
    writeFromQueue();
  }

  /**
   * Determine if any log entries need to be written.
   * @throws WSIException is there is a problem adding log entries.
  
   */
  public synchronized void writeFromQueue() throws WSIException
  {
    MessageEntry requestMessageEntry = null;
    MessageEntry responseMessageEntry = null;
    boolean responseFound = false;

    // Create array that will contain list of index to remove from request list
    int[] requestIndexList = new int[requestList.size()];
    int reqIndexCount = 0;

    synchronized (listLock)
    {
      // If there are entries in the request queue,
      // then try to match them with entries in the response queue
      for (int reqIndex = 0; reqIndex < requestList.size(); reqIndex++)
      {
        // Get request log entry
        requestMessageEntry = (MessageEntry) requestList.elementAt(reqIndex);

        // Check each response log entry to see if the conversationId matches
        responseFound = false;
        for (int respIndex = 0;
          respIndex < responseList.size() && !responseFound;
          respIndex++)
        {
          // Get response log entry
          responseMessageEntry =
            (MessageEntry) responseList.elementAt(respIndex);

          // If the request and response have the same conversationId,
          // then write them out to the log file
          if (requestMessageEntry
            .getConversationId()
            .equals(responseMessageEntry.getConversationId()))
          {
            responseFound = true;

            // Set the id for the log entries
            requestMessageEntry.setId(getNextMessageId());
            responseMessageEntry.setId(getNextMessageId());

            // Write out request and then response
            //==== SS start : what this line is used for??? - causes memory leak, as no removeLogEntry is called ====
            //            log.addLogEntry(requestMessageEntry);
            //==== SS end ===
            StringReader requestReader =
              new StringReader(requestMessageEntry.toXMLString(""));
            logWriter.write(requestReader);

            // Display message
            printMessage(requestMessageEntry);

            // Write out response
            //==== SS start : what this line is used for??? - causes memory leak, as no removeLogEntry is called ====
            //            log.addLogEntry(responseMessageEntry);
            //==== SS end ===
            StringReader responseReader =
              new StringReader(responseMessageEntry.toXMLString(""));
            logWriter.write(responseReader);

            // Display message
            printMessage(responseMessageEntry);

            // Add request to list so that it can be removed later
            requestIndexList[reqIndexCount++] = reqIndex;

            // Remove log entry from response list
            responseList.remove(respIndex);
          }
        }
      }

      // Remove requests that were written out
      for (int count = 0; count < reqIndexCount; count++)
        requestList.remove(requestIndexList[count]);
    }
  }

  /**
   * Get the next message identifier.
   * @return the next message identifier.
   */
  protected synchronized String getNextMessageId()
  {
    return String.valueOf(messageId++);
  }

  /**
   * Display message.
   * @param messageEntry a message entry.
   */
  protected void printMessage(MessageEntry messageEntry)
  {
    // Display message
    monitor.printMessage(
      "log01",
      messageEntry.getId()
        + ", "
        + messageEntry.getType()
        + ", "
        + messageEntry.getSenderHostAndPort(),
      "Log message entry -  ID: "
        + messageEntry.getId()
        + ", Type: "
        + messageEntry.getType()
        + ", Sender: "
        + messageEntry.getSenderHostAndPort());
  }
}
