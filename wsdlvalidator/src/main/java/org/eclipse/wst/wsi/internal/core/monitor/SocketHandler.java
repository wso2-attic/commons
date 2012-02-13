/*******************************************************************************
 * Copyright (c) 2002-2003 IBM Corporation, Beacon Information Technology Inc. and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM      - Initial API and implementation
 *   BeaconIT - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.util.Utils;

/**
 * Socket Handler.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class SocketHandler extends Thread
{
  protected SocketConnection socketConnection;
  protected SocketHandler pairedSocketHandler;
  protected String connectionType;
  protected int conversationID;
  protected String targetHost;
  protected int readTimeoutSeconds;

  protected Socket inSocket;
  protected Socket outSocket;
  protected InputStream inputStream = null;
  protected OutputStream outputStream = null;

  protected boolean verbose = false;

  protected boolean readTimedOut = false;

  // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT
  private String mimeCharset = null;
  private String xmlEncoding = null;

  protected static final String CRLF = "\r\n";

  protected static final String HTTP_100_CONTINUE =
    "100 Continue".toUpperCase();
  protected static final String CHUNKED =
    "Transfer-Encoding: chunked".toUpperCase();
  protected static final String CHUNKED_WITH_QUOTES =
    "Transfer-Encoding: \"chunked\"".toUpperCase();
  protected static final String CONTENT_LENGTH =
    "Content-Length:".toUpperCase();

  /**
   * Create socket handler.
   * @param socketConnection   socket connection.
   * @param connectionType     connection type.
   * @param conversationID     conversation id.
   * @param targetHost         target host.
   * @param inSocket           in socket.
   * @param outSocket          out socket.
   * @param readTimeoutSeconds read timeout seconds.
   */
  public SocketHandler(
    SocketConnection socketConnection,
    String connectionType,
    int conversationID,
    String targetHost,
    Socket inSocket,
    Socket outSocket,
    int readTimeoutSeconds)
  {
    this.socketConnection = socketConnection;
    this.connectionType = connectionType;
    this.conversationID = conversationID;
    this.targetHost = targetHost;
    this.inSocket = inSocket;
    this.outSocket = outSocket;
    this.readTimeoutSeconds = readTimeoutSeconds;

    // ADD:
    verbose =
      socketConnection.getMonitor().getMonitorConfig().getVerboseOption();

    start();
  }

  /**
   * Set paired socket handler.
   * @param pairedSocketHandler paired socket handler.
   */
  public void setPairedSocketHandler(SocketHandler pairedSocketHandler)
  {
    this.pairedSocketHandler = pairedSocketHandler;
  }

  /**
   * Send the data from the input socket to the output socket.
   */
  public void run()
  {
    int readLen;
    String readMsg;
    MessageContext messageContext = null;

    // Create read buffer      
    byte[] readBuffer = new byte[4096];

    try
    {
      // Get the input and output streams
      this.inputStream = this.inSocket.getInputStream();
      this.outputStream = this.outSocket.getOutputStream();

      // Process while the connection is active 
      // (NOTE: there might be more than 1 message per connection)
      boolean connectionActive = true;
      while (connectionActive)
      {
        // Reset all data
        readLen = 0;
        messageContext = new MessageContext();

        // Read until message is complete
        boolean messageComplete = false;
        while (!messageComplete)
        {
          try
          {
            // DEBUG:
            debug("run", "Read data from the input stream.");

            // Read data from the input stream
            readLen = inputStream.read(readBuffer, 0, readBuffer.length);

            // Reset read timeout flag
            readTimedOut = false;

            // DEBUG:
            debug("run", "readLen: " + readLen);

            if (readLen == -1)
            {
              connectionActive = false;
              messageComplete = true;
            }

            // If data was read, then check for 100 continue
            else if (readLen > 0)
            {
              // If this is the first data that was read, then get the timestamp
              if (messageContext.timestamp == null)
                messageContext.timestamp = Utils.getTimestamp();

              if (connectionType.equals(MessageEntry.TYPE_REQUEST))
              {
                byte[] toHost =
                  new String(
                    socketConnection.redirect.getToHost()
                      + ":"
                      + socketConnection.redirect.getToPort())
                    .getBytes();

                String message = new String(readBuffer, 0, readLen);

                int index = message.indexOf(CRLF + "Host: ");
                if (index > -1)
                {
                  index += 8;
                  int secondPart = message.indexOf(CRLF, index);

                  // Write the data to the output stream and then go format it
                  write(this.outputStream, readBuffer, 0, index);
                  write(this.outputStream, toHost, 0, toHost.length);
                  write(
                    this.outputStream,
                    readBuffer,
                    secondPart,
                    readLen - secondPart);
                }
                else
                {
                  // Write the data to the output stream and then go format it
                  write(this.outputStream, readBuffer, 0, readLen);
                }
              }
              else
              {
                // Write the data to the output stream and then go format it
                write(this.outputStream, readBuffer, 0, readLen);
              }

              // DEBUG:
              if (verbose)
              {
                String bufferString = new String(readBuffer, 0, readLen);
                debug("run", "buffer as string: [" + bufferString + "]");
                if (bufferString.length() <= 50)
                  debug(
                    "run",
                    "buffer as hexstring: ["
                      + Utils.toHexString(bufferString)
                      + "]");
                else
                  debug(
                    "run",
                    "buffer as hexstring: ["
                      + Utils.toHexString(bufferString.substring(0, 50))
                      + " ...]");
              }

              // See if this part of the buffer contains the BOM
              if (messageContext.bom == 0)
              {
                messageContext.bom = getBOM(readBuffer);
              }

              // DEBUG
              debug("run", "bom: " + messageContext.bom);

              String encoding;

              try
              {
                encoding = getEncoding();
                readMsg =
                  new String(
                    readBuffer,
                    0,
                    readLen,
                    Utils.getJavaEncoding(encoding));
                setEncoding(readMsg);
                if (!encoding.equals(getEncoding()))
                {
                  encoding = getEncoding();
                  readMsg =
                    new String(
                      readBuffer,
                      0,
                      readLen,
                      Utils.getJavaEncoding(encoding));
                }
              }

              catch (UnsupportedEncodingException uee)
              {
                debug("run", "EXCEPTION (3): " + uee.toString());
                throw new RuntimeException(uee.toString());
              }

              // Set encoding in the message context
              messageContext.encoding = encoding;

              // DEBUG
              debug("run", "encoding: " + messageContext.encoding);

              // Process message
              messageContext = processMessage(readLen, readMsg, messageContext);
            }

            // If message is complete, then log it and reset buffer
            if ((isMessageComplete(messageContext))
              || ((readLen == -1) && (messageContext.messageBuffer.length() > 0)))
            {
              // Log message
              logMessage(messageContext);

              // Set message complete
              messageComplete = true;
            }
          }

          catch (InterruptedIOException ie)
          {
            // Set read timeout flag
            readTimedOut = true;

            debug("run", "InterruptedIOException: " + ie.toString());

            // If the read is not done, then shutdown 
            if (pairedSocketHandler != null
              && pairedSocketHandler.isReadWaiting()
              && pairedSocketHandler.isReadTimedOut())
            {
              // DEBUG:
              debug("run", "read timed out on both sockets");

              // If there is data in the message buffer and it is complete, then log it
              if ((isMessageComplete(messageContext))
                || (messageContext.messageBuffer.length() > 0))
              {
                // Log message
                logMessage(messageContext);
              }

              // Set message complete
              connectionActive = false;
              messageComplete = true;
            }
          }

          catch (Exception e2)
          {
            // DEBUG:
            debug(
              "run",
              "EXCEPTION (2): "
                + e2.toString()
                + "\n"
                + Utils.getExceptionDetails(e2));
            //e2.printStackTrace();

            // If there is data in the message buffer and it is complete, then log it
            if ((isMessageComplete(messageContext))
              || (messageContext.messageBuffer.length() > 0))
            {
              // Log message
              logMessage(messageContext);
            }

            // Set message complete
            connectionActive = false;
            messageComplete = true;
          }
        }
      }
    }

    catch (Exception e)
    {
      // DEBUG:
      debug(
        "run",
        "EXCEPTION (1): "
          + e.getMessage()
          + "\n"
          + Utils.getExceptionDetails(e));
      //e.printStackTrace();
    }

    catch (Error err)
    {
      // DEBUG:
      debug("run", "ERROR: " + err.getMessage());
      //err.printStackTrace();
    }

    finally
    {
      shutdown();
      socketConnection.wakeUp();
    }
  }

  /**
   * Process the message.
   */
  private MessageContext processMessage(
    int readLen,
    String readMsg,
    MessageContext inMessageContext)
    throws WSIException
  {
    boolean continueRead = false;

    // Initialize message context                                       
    MessageContext messageContext = inMessageContext;

    // Get message buffer and chunked data from message context
    StringBuffer messageBuffer = messageContext.messageBuffer;
    ChunkedData chunkedData = messageContext.chunkedData;

    // If all we received was the header with 100 continue, then ignore it
    if ((readMsg.toUpperCase().indexOf(HTTP_100_CONTINUE) != -1)
      && (readLen >= 25))
    {
      // DEBUG:
      debug("processMessage", "Ignore HTTP 100 Continue.");

      // Find the end of the HTTP 100 message
      int index = Utils.getFirstCRLFCRLF(readMsg);

      // If there is only the HTTP 100 message, then just ignore it
      if (index == readMsg.length())
        continueRead = true;

      // Otherwise remove the HTTP 100 message and continue
      else
        readMsg = readMsg.substring(index);
    }

    // ADD: What if a bypassed message contains another message after it?
    if (!continueRead && bypassMessage(readMsg))
    {
      // DEBUG:
      debug(
        "processMessage",
        "Do not log message as defined in the monitor spec, but it will be sent.");

      continueRead = true;
    }

    if (!continueRead)
    {
      int index = 0;

      // If there is chunked data, then get the length
      if ((readMsg.toUpperCase().indexOf(CHUNKED) != -1)
        || (readMsg.toUpperCase().indexOf(CHUNKED_WITH_QUOTES) != -1))
      {
        // DEBUG:
        debug("processMessage", "Processing chunked data...");

        // Get the location of the first CFLF
        if ((index = readMsg.indexOf(CRLF + CRLF)) == -1)
        {
          throw new WSIException("Could not locate end of HTTP header.");
        }

        // Include the CRLF+CRLF in the index
        index += 4;

        // DEBUG:
        debug(
          "processMessage",
          "Add header before decoding chunked data: ["
            + readMsg.substring(0, index)
            + "]");

        // Add HTTP header to buffer
        messageBuffer.append(readMsg.substring(0, index));

        // If there is no more data (i.e. header only), then just indicate that there is more chunked data
        if (readMsg.length() == index)
        {
          chunkedData = new ChunkedData(this, true);

          // DEBUG:
          debug(
            "processMessage",
            "There is chunk data, but none in this part of the message.");
        }

        // Determine if the remainder of the data is complete (i.e. ends with [0][CRLF][Optional Footer][CRLF])
        else
        {
          // Create chunked data object
          chunkedData = new ChunkedData(this, readMsg.substring(index));

          if (!chunkedData.isMoreChunkedData())
          {
            chunkedData.decodeAndAddDataToBuffer(messageBuffer);
          }
        }
      }

      else if (chunkedData != null && chunkedData.isMoreChunkedData())
      {
        // DEBUG:
        debug("processMessage", "Processing MORE chunked data...");

        // Add data
        chunkedData.addData(readMsg);

        // Decode data
        if (!chunkedData.isMoreChunkedData())
        {
          chunkedData.decodeAndAddDataToBuffer(messageBuffer);
        }
      }

      // Else just append the data to the buffer
      else
      {
        // DEBUG:
        debug(
          "processMessage",
          "Add data to message entry buffer: [" + readMsg + "]");

        messageBuffer.append(readMsg);
      }
    }

    // Set updated message buffer and chunked data in message context    
    messageContext.messageBuffer = messageBuffer;
    messageContext.chunkedData = chunkedData;

    // Return message context 
    return messageContext;
  }

  /**
   * Shutdown input socket and close input stream.
   * @param inSocket in socket.
   * @param inputStream input stream.
   */
  protected void stopInput(Socket inSocket, InputStream inputStream)
  {
    try
    {
      // If there is a input socket, then shutdown the input
      if (inSocket != null)
      {
        inSocket.shutdownInput();
      }

      // If there is an input stream then close it
      if (inputStream != null)
      {
        inputStream.close();
      }
    }
    catch (Exception e)
    {
      // Ignore since we are stopping everything
    }

    inputStream = null;
  }

  /**
   * Shutdown output socket and close output stream.
   * @param outSocket out socket.
   * @param outputStream output stream.
   */
  protected void stopOutput(Socket outSocket, OutputStream outputStream)
  {
    try
    {
      // If there is an output stream, then flush it
      if (outputStream != null)
      {
        outputStream.flush();
      }

      // If there is a input socket, then shutdown the input
      if (outSocket != null)
      {
        outSocket.shutdownOutput();
      }

      // If there is an output stream then close it
      if (outputStream != null)
      {
        outputStream.close();
      }
    }

    catch (Exception e)
    {
      // Ignore since we are stopping everything
    }

    outputStream = null;
  }

  /**
   * Shutdown handler.
   */
  public void shutdown()
  {
    // Stop both the input and output
    stopInput(this.inSocket, this.inputStream);
    stopOutput(this.outSocket, this.outputStream);
  }

  /**
   * Display debug messages.
   */
  void debug(String method, String message)
  {
    debug("SocketHandler", method, message);
  }

  /**
   * Display debug messages.
   */
  void debug(String className, String method, String message)
  {
    if (verbose)
      print(className, method, message);
  }

  /**
   * Display messages.
   */
  void print(String className, String method, String message)
  {
    System.out.println(
      "["
        + Thread.currentThread().getName()
        + "] ["
        + className
        + "."
        + method
        + "] ["
        + this.connectionType
        + "] "
        + message);
  }

  /** 
   * Write data.
   */
  private void write(
    OutputStream outputStream,
    byte[] buffer,
    int start,
    int length)
    throws IOException
  {
    if (outputStream == null)
    {
      // DEBUG:
      debug("write", "Could not write buffer because output stream is null.");
    }
    else
    {
      // DEBUG:
      debug("write", "buffer: [" + new String(buffer, start, length) + "]");

      outputStream.write(buffer, start, length);
    }
  }

  /** 
   * Check if message is complete.
   * 
   * @param messageContext
   */
  private boolean isMessageComplete(MessageContext messageContext)
    throws WSIException
  {
    int index, index2, contentLen;
    boolean messageComplete = false;

    boolean moreChunkedData = messageContext.chunkedData.isMoreChunkedData();

    String message = messageContext.messageBuffer.toString();

    // Find the first CRLF + CRLF which marks the end of the HTTP header
    String httpHeader;
    index = Utils.getFirstCRLFCRLF(message);
    if (index == -1)
      httpHeader = message;
    else
      httpHeader = message.substring(0, index);

    // If chunked data, then complete only if there is no more data
    if (((httpHeader.toUpperCase().indexOf(CHUNKED) != -1)
      || (httpHeader.toUpperCase().indexOf(CHUNKED_WITH_QUOTES) != -1))
      && (!moreChunkedData))
    {
      debug(
        "isMessageComplete",
        "HTTP header indicates chunked data and there is no more chunked data");
      messageComplete = true;
    }

    // Check for content length
    else if ((index = httpHeader.toUpperCase().indexOf(CONTENT_LENGTH)) == -1)
    {
      debug("isMessageComplete", "HTTP header does not contain content length");

      // Should not have complete POST header without content length
      if (httpHeader.startsWith("POST"))
      {
        if (httpHeader.endsWith(CRLF + CRLF))
        {
          throw new WSIException("Could not locate content-length in HTTP POST header.");
        }

        messageComplete = false;
      }

      // This could be a GET, so see if the the complete header has been received
      else if (
        httpHeader.startsWith("GET")
          && (message.length() == httpHeader.length()
            && message.endsWith(CRLF + CRLF)))
      {
        messageComplete = true;
      }

      else
      {
        messageComplete = false;
      }
    }

    // If there is content length, then see if the entire message has been received
    else if ((index = httpHeader.toUpperCase().indexOf(CONTENT_LENGTH)) != -1)
    {
      // Find end of content length value
      index2 = httpHeader.indexOf(CRLF, index);

      debug("isMessageComplete", "CRLF: " + Utils.toHexString(CRLF));
      debug(
        "isMessageComplete",
        "httpHeader/index: " + Utils.toHexString(httpHeader.substring(index)));

      // Get content length
      contentLen =
        Integer
          .decode(
            httpHeader.substring(index + CONTENT_LENGTH.length() + 1, index2))
          .intValue();

      // DEBUG:
      debug("isMessageComplete", "contentLen: " + contentLen);

      // Find the first CRLF + CRLF which marks the end of the HTTP header
      index = Utils.getFirstCRLFCRLF(message);

      // DEBUG:
      debug(
        "isMessageComplete",
        "actual received message length: " + (message.length() - (index)));

      // If content length is equal to actual message content length, then message is complete
      if (contentLen == message.length() - index)
      {
        messageComplete = true;

        // DEBUG:
        debug(
          "isMessageComplete",
          "contentLen = actual received message length.");
      }
    }

    // Message is not complete
    else
    {
      messageComplete = false;
    }

    // DEBUG:
    debug("isMessageComplete", "messageComplete: " + messageComplete);

    return messageComplete;
  }

  /** 
   * Log message.
   * 
   * @param messageBuffer
   */
  private void logMessage(MessageContext messageContext) throws WSIException
  {
    // Determine sender and receiver host/port
    String senderHostAndPort, receiverHostAndPort;

    // Request
    if (connectionType.equals(MessageEntry.TYPE_REQUEST))
    {
      senderHostAndPort =
        inSocket.getInetAddress().getHostAddress() + ":" + inSocket.getPort();
      receiverHostAndPort = targetHost + ":" + outSocket.getPort();
    }

    // Response      
    else
    {
      senderHostAndPort = targetHost + ":" + inSocket.getPort();
      receiverHostAndPort =
        outSocket.getInetAddress().getHostAddress() + ":" + outSocket.getPort();
    }

    // Create message entry
    this.socketConnection.logMessage(
      conversationID,
      this.connectionType,
      messageContext.timestamp,
      senderHostAndPort,
      receiverHostAndPort,
      messageContext.messageBuffer,
      messageContext.bom,
      messageContext.encoding);
  }

  /**
   * Check for HTTP messages that should not be logged.
   */
  private boolean bypassMessage(String message)
  {
    boolean bypass = false;
    if ((message.startsWith("CONNECT"))
      || (message.startsWith("TRACE"))
      || (message.startsWith("DELETE"))
      || (message.startsWith("OPTIONS"))
      || (message.startsWith("HEAD")))
    {
      bypass = true;
    }

    return bypass;
  }

  // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT
  /**
   * Set Encoding Parameters
   * @param messageFragment String of a HTTP message fragment.
   * @author K.Nakagome@BeaconIT Japan SIG 
   */
  private void setEncoding(String messageFragment)
  {
    if (mimeCharset == null || mimeCharset.length() == 0)
    {
      mimeCharset = Utils.getHTTPCharset(messageFragment);
    }
    if (xmlEncoding == null || xmlEncoding.length() == 0)
    {
      xmlEncoding = Utils.getXMLEncoding(messageFragment);
    }
    return;
  }

  // I18N: 2003.02.26 modified by K.Nakagome@BeaconIT
  /**
   * Get Encoding Parameter
   * @return Character encoding of HTTP message.
   * @author K.Nakagome@BeaconIT Japan SIG 
   */
  private String getEncoding()
  {
    String encoding = WSIConstants.DEFAULT_XML_ENCODING;
    if (mimeCharset != null && mimeCharset.length() > 0)
    {
      encoding = mimeCharset;
    }
    if (xmlEncoding != null && xmlEncoding.length() > 0)
    {
      encoding = xmlEncoding;
    }
    return encoding;
  }

  /**
   * Get the Byte Order Mark from the message (if there is one).
   */
  private int getBOM(byte[] message)
  {
    int bom = 0;

    byte FF = (byte) 0xFF;
    byte FE = (byte) 0xFE;
    byte EF = (byte) 0xEF;
    byte BB = (byte) 0xBB;
    byte BF = (byte) 0xBF;

    // Search through the byte array for CRLF+CRLF.  This will mark the end of the header.
    int i = Utils.getFirstCRLFCRLF(message, 0);
    if (i != -1)
    {
      // DEBUG:
      debug(
        "getBOM",
        "message[i]: "
          + message[i]
          + ", message[i+1]: "
          + message[i+1]);

      // Check for UTF-8 BOM
      if (((i + 2) < message.length)
        && message[i] == EF
        && message[i+1] == BB
        && message[i+2] == BF)
      {
        bom = 0xEFBBBF;
      }
      // Check for UTF-16 big-endian BOM
      else if (
        ((i+1) < message.length)
          && message[i] == FE
          && message[i + 1] == FF)
      {
        bom = 0xFEFF;
      }
      // Check for UTF-16 little-endian BOM
      else if (
        ((i+1) < message.length)
          && message[i] == FF
          && message[i+1] == FE)
      {
        bom = 0xFFFE;
      }
      // ADD: Do we need to check for other BOMs
    }
    return bom;
  }

  /**
   * Determine if the read is still waiting for data.
   */
  boolean isReadWaiting()
  {
    boolean readWaiting = false;

    try
    {
      // DEBUG:
      debug(
        "isReadWaiting",
        "inSocket.getInputStream().available(): "
          + inSocket.getInputStream().available());

      if (inSocket.getInputStream().available() == 0)
      {
        readWaiting = true;
      }
    }

    catch (IOException ioe)
    {
    }

    return readWaiting;
  }

  /**
   * Get read timed out flag.
   */
  boolean isReadTimedOut()
  {
    return this.readTimedOut;
  }

  /**
   * Message context contains information about the message that is being processed.
   */
  class MessageContext
  {
    StringBuffer messageBuffer;
    ChunkedData chunkedData;
    String timestamp;
    int bom;
    String encoding;

    /**
     * Create new message context.
     */
    MessageContext()
    {
      messageBuffer = new StringBuffer();
      chunkedData = new ChunkedData();
      timestamp = null;
      bom = 0;
      encoding = WSIConstants.DEFAULT_XML_ENCODING;
    }
  }
}
