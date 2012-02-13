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

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.ToolEnvironment;
import org.eclipse.wst.wsi.internal.core.ToolInfo;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.log.LogReader;
import org.eclipse.wst.wsi.internal.core.log.MessageEntry;
import org.eclipse.wst.wsi.internal.core.log.MessageEntryHandler;
import org.eclipse.wst.wsi.internal.core.log.MimePart;
import org.eclipse.wst.wsi.internal.core.log.MimeParts;
import org.eclipse.wst.wsi.internal.core.monitor.config.Comment;
import org.eclipse.wst.wsi.internal.core.monitor.config.impl.CommentImpl;
import org.eclipse.wst.wsi.internal.core.profile.validator.EntryContext;
import org.eclipse.wst.wsi.internal.core.profile.validator.MessageValidator;
import org.eclipse.wst.wsi.internal.core.report.ArtifactReference;
import org.eclipse.wst.wsi.internal.core.report.Entry;
import org.eclipse.wst.wsi.internal.core.report.impl.ArtifactReferenceImpl;
import org.eclipse.wst.wsi.internal.core.report.impl.EntryImpl;
import org.eclipse.wst.wsi.internal.core.util.EntryType;
import org.eclipse.wst.wsi.internal.core.util.Utils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.eclipse.wst.wsi.internal.core.xml.dom.ElementLocation;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Defines the implementation used to read the Log file.
 * 
 * @version 1.0.1
 * @author Neil Delima (nddelima@ca.ibm.com)
 * @author Peter Brittenham (peterbr@us.ibm.com)
 * @author Graham Turrell (gturrell@uk.ibm.com)
 */
public class LogReaderImpl implements LogReader
{
  /**
   * Document location.
   */
  private String documentURI;

  /** Used to ensure that the implementer is the first element after the log element **/
  private int firstLogElement = 0;
  private int elementCount = 0;
  private boolean bMonitorFirst = false;

  private WSIException wsiException = null;

  /**
   * @see org.eclipse.wst.wsi.internal.core.log.LogReader#readLog(String, LogEntryHandler)
   */
  public void readLog(String logURI, MessageEntryHandler logReaderCallback)
    throws WSIException
  {
    // Read the log file entries
    readLog(new InputSource(logURI), logReaderCallback);
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.log.LogReader#readLog(Reader, LogEntryHandler)
   */
  public void readLog(Reader reader, MessageEntryHandler logReaderCallback)
    throws WSIException
  {
    // Read the log file entries
    readLog(new InputSource(reader), logReaderCallback);
  }

  /**
   * Read and parse the log file.
   * @param inputSource          an InputSource object
   * @param messageEntryHandler  a MessageEntryHandler object
   * @throws WSIException if a problem occurs while reading the log file.
   */
  protected void readLog(
    InputSource inputSource,
    MessageEntryHandler messageEntryHandler)
    throws WSIException
  {
    try
    {
      // Get the XML reader
      XMLReader xmlReader = XMLUtils.getXMLReader();

      /** TO DO:  The LogFile must be valid wrt its schema.  The schemaLocation probably
       * needs to be set in some properties file.
       * These two are to turn on validation 
      reader.setFeature(FEATURE_VALIDATION,true);
      */

      // Set the content handler
      xmlReader.setContentHandler(new LogFileHandler(messageEntryHandler));

      // Start parsing the file
      xmlReader.parse(inputSource);

      // ADD: Need to find a way to determine when this exception should be thrown, 
      // since by the time we get here all of the test assertions have been processed.
      // Also, only the last exception is propogated back.  Should all of the exceptions
      // be captured and propogated back?
      if (wsiException != null)
      {
        // TEMP: For now, don't throw the exception
        //throw wsiException;
      }

      //Requirement, the monitor MUST be the first child of the log.
      //Perhaps we need to preprocess the log if this is the first condition to be met.
      if (!bMonitorFirst)
        throw new WSIException(
          "The Element \""
            + WSIConstants.ELEM_MONITOR
            + "\" is not the first child of the Element \"log\"");
    }
    catch (Exception e)
    {
      throw new WSIException(
        "An error occurred while processing the message log file.",
        e);
    }
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.DocumentReader#getLocation()
   */
  public String getLocation()
  {
    return this.documentURI;
  }

  /**
   * @see org.eclipse.wst.wsi.internal.core.document.DocumentReader#setLocation(String)
   */
  public void setLocation(String documentURI)
  {
    this.documentURI = documentURI;
  }

  /**
  * Process and handles elements and attribute in the log file and invokes
  * message validation assertions serially as the file is being processed,
  * if a messagevalidator exists.
  */
  private class LogFileHandler extends DefaultHandler
  {

    /** private members that represent a MessageEntry.*/
    private String id;
    private String conversationID;
    private String type;
    private String timestamp;
    private StringBuffer senderHostAndPort = null;
    private StringBuffer receiverHostAndPort  = null;
    private StringBuffer messageContent  = null;
    private StringBuffer httpHeaders  = null;
    private StringBuffer boundary  = null;
    private StringBuffer mimeHeaders = null;
    private StringBuffer mimeContent = null;
    private MimeParts mimeParts = null;
    private List boundaries = null;

    private int bom = 0;

    private String logTimestamp = null;
    private StringBuffer monitorComment;
    private boolean ignoreComment = false;

    /** Other private members */
    private String currentElement;

    private ElementLocation entryElementLocation = null;

    private ToolInfo toolInfo = null;

    private Vector messageEntryList = new Vector();

    private MessageEntryHandler logEntryHandler = null;

    private Locator locator = null;

    /**
     * Log file handler. 
     */
    public LogFileHandler(MessageEntryHandler logEntryHandler)
    {
      this.logEntryHandler = logEntryHandler;

      toolInfo = new ToolInfo();
      toolInfo.setToolEnvironment(new ToolEnvironment());
    }

    /** 
     * Start document. 
     */
    public void startDocument()
    {
    }

    /** 
     * Set document locato.
     */
    public void setDocumentLocator(Locator locator)
    {
      this.locator = locator;

      // DEBUG:
      //System.out.println("line: " + locator.getLineNumber() + ", column: " + locator.getColumnNumber());
    }

    /** 
     * Start element. 
     */
    public void startElement(
      String namespaceURI,
      String localName,
      String qName,
      Attributes attributes)
    {
      // DEBUG:
      //System.out.println("localName:" + localName + ", line: " + locator.getLineNumber() + ", column: " + locator.getColumnNumber());

      //Requirement, the implementer MUST be the first child of the log       
      if (elementCount == 2)
        if (firstLogElement == 1)
          bMonitorFirst = true;

      elementCount++;

      // <log>
      if (localName == WSIConstants.ELEM_LOG)
      {
        // Save the timestamp value
        this.logTimestamp = attributes.getValue(WSIConstants.ATTR_TIMESTAMP);
      }

      // <wsi-config:configuration>
      else if (
        (localName == WSIConstants.ELEM_CONFIG)
          && (namespaceURI == WSIConstants.NS_URI_WSI_MONITOR_CONFIG))
      {
        // Save comment element if it exists
        ignoreComment = false;
      }

      // <wsi-config:comment>
      else if (
        (localName == WSIConstants.ELEM_COMMENT)
          && (namespaceURI == WSIConstants.NS_URI_WSI_MONITOR_CONFIG))
      {
        // If this is the comment element within the configuration element, then get its content
        if (!ignoreComment)
        {
          currentElement = WSIConstants.ELEM_COMMENT;
          monitorComment = new StringBuffer();
        }
      }

      // <wsi-config:redirect>
      else if (
        (localName == WSIConstants.ELEM_REDIRECT)
          && (namespaceURI == WSIConstants.NS_URI_WSI_MONITOR_CONFIG))
      {
        // After hitting the first redirect statement, ignore all comment elements
        ignoreComment = true;
        monitorComment = null;
      }

      // <monitor>
      else if (localName == WSIConstants.ELEM_MONITOR)
      {
        firstLogElement++;
        toolInfo.setName(WSIConstants.ELEM_MONITOR);

        // Get the monitor version and release date
        toolInfo.setVersion(attributes.getValue(WSIConstants.ATTR_VERSION));
        toolInfo.setReleaseDate(
          attributes.getValue(WSIConstants.ATTR_RELEASE_DATE));
      }

      // <implementer>
      else if (localName == WSIConstants.ELEM_IMPLEMENTER)
      {
        currentElement = WSIConstants.ELEM_IMPLEMENTER;
        toolInfo.setImplementer(attributes.getValue(WSIConstants.ATTR_NAME));
        toolInfo.setLocation(attributes.getValue(WSIConstants.ATTR_LOCATION));
      }

      // <runtime>
      else if (localName == WSIConstants.ELEM_RUNTIME)
      {
        currentElement = WSIConstants.ELEM_RUNTIME;
        toolInfo.getToolEnvironment().setRuntimeName(
          attributes.getValue(WSIConstants.ATTR_NAME));
        toolInfo.getToolEnvironment().setRuntimeVersion(
          attributes.getValue(WSIConstants.ATTR_VERSION));
      }

      // <operatingSystem>
      else if (localName == WSIConstants.ELEM_OPERATING_SYSTEM)
      {
        currentElement = WSIConstants.ELEM_OPERATING_SYSTEM;
        toolInfo.getToolEnvironment().setOSName(
          attributes.getValue(WSIConstants.ATTR_NAME));
        toolInfo.getToolEnvironment().setOSVersion(
          attributes.getValue(WSIConstants.ATTR_VERSION));
      }

      // <xmlParser>
      else if (localName == WSIConstants.ELEM_XML_PARSER)
      {
        currentElement = WSIConstants.ELEM_XML_PARSER;
        toolInfo.getToolEnvironment().setXMLParserName(
          attributes.getValue(WSIConstants.ATTR_NAME));
        toolInfo.getToolEnvironment().setXMLParserVersion(
          attributes.getValue(WSIConstants.ATTR_VERSION));
      }

      // <messageEntry>
      else if (localName == WSIConstants.ELEM_MESSAGE_ENTRY)
      {
        currentElement = WSIConstants.ELEM_MESSAGE_ENTRY;
        entryElementLocation = new ElementLocation(this.locator);
        timestamp = attributes.getValue(WSIConstants.ATTR_TIMESTAMP);
        conversationID = attributes.getValue(WSIConstants.ATTR_CONVERSATION_ID);
        id = attributes.getValue(WSIConstants.ATTR_ID_UC);
        type = attributes.getValue(WSIConstants.ATTR_TYPE);
      }

      // <httpHeaders>
      else if (localName == WSIConstants.ELEM_HTTP_HEADERS)
      {
        //Note: Character data could be split up and returned 
        //in more more than one characters call. 
        httpHeaders = new StringBuffer();
        currentElement = WSIConstants.ELEM_HTTP_HEADERS;
      }

      // <messageContent>
      else if (localName == WSIConstants.ELEM_MESSAGE_CONTENT)
      {
        messageContent = new StringBuffer();
        mimeParts = null;
        currentElement = WSIConstants.ELEM_MESSAGE_CONTENT;
        String bomString = attributes.getValue(WSIConstants.ATTR_BOM);
        if (bomString != null)
          bom = Integer.valueOf(bomString).intValue();
        else
          bom = 0;
      }

      // <messageContentWithAttachments>
      else if (localName == WSIConstants.ELEM_MESSAGE_CONTENT_WITH_ATTACHMENTS)
      {
        messageContent = null;
        mimeParts = new MimePartsImpl();

        String bomString = attributes.getValue(WSIConstants.ATTR_BOM);
        if (bomString != null)
          bom = Integer.valueOf(bomString).intValue();
        else
          bom = 0;
      }

      // <mimePart>
      else if (localName == WSIConstants.ELEM_MIME_PART)
      {
        if (boundaries == null)
          boundaries = new ArrayList(2);
        else
          boundaries.clear();
      }

      // <boundaryString>
      else if (localName == WSIConstants.ELEM_BOUNDARY_STRING)
      {
        boundary = new StringBuffer();
        currentElement = WSIConstants.ELEM_BOUNDARY_STRING;
      }

      // <mimeHeaders>
      else if (localName == WSIConstants.ELEM_MIME_HEADERS)
      {
        mimeHeaders = new StringBuffer();
        currentElement = WSIConstants.ELEM_MIME_HEADERS;
      }

      // <mimeContent>
      else if (localName == WSIConstants.ELEM_MIME_CONTENT)
      {
        mimeContent = new StringBuffer();
        currentElement = WSIConstants.ELEM_MIME_CONTENT;
      }

      // <senderHostAndPort>
      else if (localName == WSIConstants.ELEM_SENDER_HOST_AND_PORT)
      {
        senderHostAndPort = new StringBuffer();
        currentElement = WSIConstants.ELEM_SENDER_HOST_AND_PORT;
      }

      // <receiverHostAndPort>
      else if (localName == WSIConstants.ELEM_RECEIVER_HOST_AND_PORT)
      {
        receiverHostAndPort = new StringBuffer();
        currentElement = WSIConstants.ELEM_RECEIVER_HOST_AND_PORT;
      }
    }

    /** 
    * Characters callback. 
    */
    public void characters(char ch[], int start, int length)
    {
      // Ignoring CRLF that appears in log elements
      int altStart = start;
      int altLength = length;
      if (start > 0 && ch[start-1] == 13 && ch[start] == 10)
      {
        altStart++;
        altLength--;
      }

      // <wsi-monConfig:comment>
      if (monitorComment != null && currentElement == WSIConstants.ELEM_COMMENT)
      {
        monitorComment.append(ch, start, length);
      }

      // <messageContent>
      else if (messageContent != null
        && currentElement == WSIConstants.ELEM_MESSAGE_CONTENT)
      {
        messageContent.append(ch, start, length);
      }

      // <httpHeaders>
      else if (httpHeaders != null
        && currentElement == WSIConstants.ELEM_HTTP_HEADERS)
      {
        httpHeaders.append(ch, start, length);
      }

      // <boundaryString>
      else if (boundary != null
        && currentElement == WSIConstants.ELEM_BOUNDARY_STRING)
      {
        boundary.append(ch, altStart, altLength);
      }

      // <mimeHeaders>
      else if (mimeHeaders != null
        && currentElement == WSIConstants.ELEM_MIME_HEADERS)
      {
        mimeHeaders.append(ch, altStart, altLength);
      }

      // <mimeContent>
      else if (mimeContent != null
        && currentElement == WSIConstants.ELEM_MIME_CONTENT)
      {
        mimeContent.append(ch, altStart, altLength);
      }

      // <senderHostAndPort>
      else if (senderHostAndPort != null
        && currentElement == WSIConstants.ELEM_SENDER_HOST_AND_PORT)
      {
        senderHostAndPort.append(ch, start, length);
      }

      // <receiverHostAndPort>
      else if (receiverHostAndPort != null
        && currentElement == WSIConstants.ELEM_RECEIVER_HOST_AND_PORT)
      {
        receiverHostAndPort.append(ch, start, length);
      }
    }

    /** 
     * End Element. 
     */
    public void endElement(String namespaceURI, String localName, String qName)
    {
      // <wsi-config:comment>
      if (
        (localName == WSIConstants.ELEM_COMMENT)
          && (namespaceURI == WSIConstants.NS_URI_WSI_MONITOR_CONFIG))
      {
        // If we saved the comment, then process it and the log timestamp
        if (this.monitorComment != null)
        {
          ArtifactReference artifactReference = new ArtifactReferenceImpl();
          artifactReference.setTimestamp(this.logTimestamp);
          Comment comment = new CommentImpl();
          comment.setText(monitorComment.toString());
          artifactReference.setDocumentElement(
            comment,
            WSIConstants.NS_NAME_WSI_MONITOR_CONFIG);
          processArtifactReference(artifactReference);
        }
      }

      // <boundaryString>
      else if (localName == WSIConstants.ELEM_BOUNDARY_STRING)
      {
        // Adding boundary string to a list
        boundaries.add(boundary.toString());
      }

      // <mimePart>
      else if (localName == WSIConstants.ELEM_MIME_PART)
      {
        // Creating a MessageMIMEPart instance
        MimePart part = new MimePartImpl();
        part.setHeaders(mimeHeaders.toString());
        part.setContent(mimeContent.toString());
        part.setBoundaryStrings((String[])boundaries.toArray(new String[0]));

        mimeParts.addPart(part);
      }

      // <messageEntry>
      else if (localName == WSIConstants.ELEM_MESSAGE_ENTRY)
      {
        // Assumption: Related messages are sequentially available in the message log.
        if (MessageEntry.TYPE_REQUEST.equalsIgnoreCase(type))
        {
          // Create log entry
          MessageEntry messageEntryRequest =
            createMessageEntry(
                id,
                conversationID,
                type,
                timestamp,
                senderHostAndPort.toString(),
                receiverHostAndPort.toString(),
                messageContent == null ? "" : messageContent.toString(),
                httpHeaders.toString(),
                mimeParts,
                entryElementLocation);

          // Add log entry to the list
          messageEntryList.add(messageEntryRequest);
          // Request is now processed only when the response is received.
        }

        else if (MessageEntry.TYPE_RESPONSE.equalsIgnoreCase(type))
        {
          // Create log entry
          MessageEntry messageEntryResponse =
            createMessageEntry(
                id,
                conversationID,
                type,
                timestamp,
                senderHostAndPort.toString(),
                receiverHostAndPort.toString(),
                messageContent == null ? "" : messageContent.toString(),
                httpHeaders.toString(),
                mimeParts,
                entryElementLocation);

          if (logEntryHandler != null)
          {
            // look up the request message
            // ISSUE : need to throw & catch a nullpointerexception in here...
            MessageEntry messageEntryRequest = findRelatedRequest(messageEntryResponse);

            if (messageEntryRequest != null)
            {
              if (!isMessageEncrypted(messageEntryRequest.getMessage()) &&
                  !isMessageEncrypted(messageEntryResponse.getMessage()))
              {
	        if (!omitRequest(messageEntryRequest))
	        {
	          // Create entry 
	          // ADD: Need to create entry from report
	          //Entry entry = this.reporter.getReport().createEntry();
	          Entry entry = new EntryImpl();
	          entry.setEntryType(
	                EntryType.getEntryType(MessageValidator.TYPE_MESSAGE_REQUEST));
	          entry.setReferenceID(messageEntryRequest.getId());
	          entry.setEntryDetail(messageEntryRequest);
	
	          // Create the context for the request-response pair
	          EntryContext requestTargetContext =
	               new EntryContext(
	                    entry,
	                    messageEntryRequest,
	                    messageEntryResponse);
	          if (requestTargetContext != null)
	            processLogEntry(requestTargetContext);
	
	          // Create entry 
	          // ADD: Need to create entry from report
	          //Entry entry = this.reporter.getReport().createEntry();
	          entry = new EntryImpl();
	          entry.setEntryType(
	            EntryType.getEntryType(MessageValidator.TYPE_MESSAGE_RESPONSE));
	          entry.setReferenceID(messageEntryResponse.getId());
	          entry.setEntryDetail(messageEntryResponse);
	
	          EntryContext responseTargetContext =
	                new EntryContext(
	                  entry,
	                  messageEntryRequest,
	                  messageEntryResponse);
	          if (responseTargetContext != null)
	            processLogEntry(responseTargetContext);
	        }
	      }
            }
          }
        }
      }

      currentElement = null;
    }

    /** 
    * End Document. 
    */
    public void endDocument()
    {
      //If we want to serially process the log file.  Invoke validateMessages with
      //msgValidator!=null.  Here we have the option of clearing the logEntry vector
      //once message validation is done.
      //if (msgValidator!=null)
      //logEntryList.clear();
        
        /* Process all remaining requests in the messageEntryList */
        for (int i = 0; i < messageEntryList.size(); i++) {
            MessageEntry logEntry = (MessageEntry) messageEntryList.get(i);
            if (!omitRequest(logEntry))
            {
              Entry entry = new EntryImpl();
              entry.setEntryType(EntryType.getEntryType(
                    MessageValidator.TYPE_MESSAGE_REQUEST));
              entry.setReferenceID(logEntry.getId());
              entry.setEntryDetail(logEntry);
              EntryContext requestTargetContext =
                new EntryContext(entry, logEntry, null);
              if (requestTargetContext != null)
                processLogEntry(requestTargetContext);
            }
        }
    }

    /**
     * Process the message in the log entry.
     */
    public void processLogEntry(EntryContext entryContext)
    {
      try
      {
        //Before validating any messages check if implementer is the first element.
        logEntryHandler.processLogEntry(entryContext);
      }
      catch (Exception e)
      {
        wsiException =
          new WSIException(
            "An error occurred while validating"
              + " the message logEntry: "
              + entryContext.getMessageEntry().getId(),
            e);
      }
    }

    /**
     * Process the artifact reference.
     */
    private void processArtifactReference(ArtifactReference artifactReference)
    {
      try
      {
        //Before validating any messages check if implementer is the first element.
        logEntryHandler.processArtifactReference(artifactReference);
      }

      catch (Exception e)
      {
        wsiException =
          new WSIException(
            "An error occurred while processing"
              + " the artifact reference: "
              + artifactReference.toString(),
            e);
      }
    }

    /**
       * Find the related Request LogEntry.  The current rule for related log entires is
       * that a request will always precede its response.
       * ISSUE: Sequence in log file is timestamp driven (monitor spec). 
       * Assertion is that for a given conversationID, a second request is not sent 
       * until its response is received (chronologically). 
       * This method will have to be modified if the rule changes.
       */
    // could be made more efficient by keeping a much smaller list of unresponded requests...
    public MessageEntry findRelatedRequest(MessageEntry logEntryResponse)
    {
      if (logEntryResponse != null)
      {
        for (int entry = messageEntryList.size() - 1; entry >= 0; entry--)
        {
          // Get the log entry of the matching request
          MessageEntry logEntry = (MessageEntry) messageEntryList.get(entry);
          // Ignore own entry
          if (!logEntryResponse.equals(logEntry))
          {
            if (logEntryResponse
              .getConversationId()
              .equals(logEntry.getConversationId()))
            {
              // found the most recently read message with the same conversationID.
              // From above, this should be the corresponding request. Check as far as possible.
              if (logEntry.getType().equals(MessageEntry.TYPE_REQUEST))
              {
                messageEntryList.remove(entry);
                return logEntry;
              }
              else
              {
                return null; // expected a request. need to throw an exception!
              }
            }
          }
        }
      }
      return null;
      // conversationID match not found anywhere. need to throw an exception!
    }

    /**
     * Create log entry.
     */
    private MessageEntry createMessageEntry(
      String id,
      String conversationId,
      String type,
      String timestamp,
      String senderIPPort,
      String receiverIPPort,
      String messageContent,
      String httpHeaders,
      MimeParts mimeParts,
      ElementLocation elementLocation)
    {
      // Create log entry
      MessageEntry messageEntry = new MessageEntryImpl();
      messageEntry.setId(id);
      messageEntry.setConversationId(conversationID);
      messageEntry.setType(type);
      messageEntry.setTimestamp(timestamp);
      messageEntry.setSenderHostAndPort(senderIPPort);
      messageEntry.setReceiverHostAndPort(receiverIPPort);
      messageEntry.setHTTPHeaders(httpHeaders);
      messageEntry.setBOM(bom);
      messageEntry.setElementLocation(elementLocation);

      String encoding = null;
      if (mimeParts == null)
      {
        // Get the encoding for this message content
        encoding = Utils.getXMLEncoding(messageContent);
        messageEntry.setMessage(messageContent);
       	messageEntry.setMimeContent(false);
      }
      else
      {
       	messageEntry.setMimeParts(mimeParts);
       	MimePart root = Utils.findRootPart(httpHeaders, mimeParts.getParts());
       	if (root != null)
       	{
          // Get the encoding for this message content
          encoding = Utils.getXMLEncoding(root.getContent());
          mimeParts.setRootPart(root);
       	}
   		// else empty body
       	messageEntry.setMimeContent(true);
      }
      if ((encoding == null) || (encoding.equals("")))
         encoding = WSIConstants.DEFAULT_XML_ENCODING;
      messageEntry.setEncoding(encoding);

      return messageEntry;
    }

  } //End ContentHandler
  
  /**
   * Check for HTTP messages that should not be logged.
   * @param rr: a request-response pair.
   * @return true if the request-response pair should be omitted from the log.
   */
  private boolean omitRequest(MessageEntry messageEntryRequest)
  {
    boolean omit = false;
    if (messageEntryRequest == null)
       omit = true;
    else
    {
      String requestHeaders = messageEntryRequest.getHTTPHeaders();
      if ((requestHeaders != null) &&
          ((requestHeaders.startsWith("CONNECT")) ||
           (requestHeaders.startsWith("TRACE")) || 
           (requestHeaders.startsWith("DELETE")) || 
           (requestHeaders.startsWith("OPTIONS")) || 
           (requestHeaders.startsWith("HEAD")) ||
           ((requestHeaders.startsWith("GET")) &&
        	(!isMessageWithBrackets(messageEntryRequest.getMessage())))))
      { 
        omit = true;
      }
    }
    return omit;
  }

  /**
   * Returns true if the content of the message has at least
   * one left and one right bracket.
   * @param message: a message content.
   * @return true if the content of the message has at least
   *         one left and one right bracket.
   */
  public boolean isMessageWithBrackets(String message)
  {
    return ((message != null) && 
            (message.indexOf("<")!= -1) && 
            (message.indexOf(">") != -1));
  }
  
  private boolean isMessageEncrypted(String message)
  {
    return ((message != null) && 
	    (message.indexOf("<EncryptedKey ") != -1));
  }
}