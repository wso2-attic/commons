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
package org.eclipse.wst.wsi.internal.core.monitor.config.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSIFileNotFoundException;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.common.impl.AddStyleSheetImpl;
import org.eclipse.wst.wsi.internal.core.monitor.config.Comment;
import org.eclipse.wst.wsi.internal.core.monitor.config.ManInTheMiddle;
import org.eclipse.wst.wsi.internal.core.monitor.config.MonitorConfig;
import org.eclipse.wst.wsi.internal.core.monitor.config.MonitorConfigReader;
import org.eclipse.wst.wsi.internal.core.monitor.config.Redirect;
import org.eclipse.wst.wsi.internal.core.util.MessageList;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Defines the implementation used to read the monitor config documents.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class MonitorConfigReaderImpl implements MonitorConfigReader
{
  /**
   * Message list reference.
   */
  protected MessageList messageList = null;

  /**
   * Document location.
   */
  protected String documentURI;

  /**
   * Initialize monitor config.
   */
  public void init(MessageList messageList)
  {
    this.messageList = messageList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfigReader#readMonitorConfig(String)
   */
  public MonitorConfig readMonitorConfig(String monitorConfigURI)
    throws WSIException
  {
    FileReader fileReader = null;

    try
    {
      fileReader = new FileReader(monitorConfigURI);
    }

    catch (FileNotFoundException fnfe)
    {
      throw new WSIFileNotFoundException(
        getMessage("config02", monitorConfigURI, "Could not find file:"),
        fnfe);
    }

    catch (Exception e)
    {
      throw new WSIException(
        getMessage("config03", monitorConfigURI, "Could not read file:"),
        e);
    }

    return readMonitorConfig(fileReader);
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfigReader#readMonitorConfig(Reader)
   */
  public MonitorConfig readMonitorConfig(Reader reader) throws WSIException
  {
    MonitorConfig monitorConfig = new MonitorConfigImpl();

    // Parse XML
    Document doc = XMLUtils.parseXML(reader);

    // Parse elements in the config document
    parseConfigElement(monitorConfig, doc.getDocumentElement());

    return monitorConfig;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentReader#getLocation()
   */
  public String getLocation()
  {
    return this.documentURI;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.DocumentReader#setLocation(String)
   */
  public void setLocation(String documentURI)
  {
    this.documentURI = documentURI;
  }

  /**
   * Parse config element.
   */
  private void parseConfigElement(MonitorConfig monitorConfig, Element element)
    throws WSIException
  {
    // ADD: Verify that this is the config element

    // Get first child element
    Element nextElement = XMLUtils.getFirstChild(element);

    boolean isLogDurationDefine = false;
    boolean isCleanupTimeoutSecondsDefine = false;

    // Process each child element
    while (nextElement != null)
    {
      // <logFile>
      if (isElement(nextElement, WSIConstants.ELEM_LOG_FILE))
      {
        // Parse the log element
        parseLogFileElement(monitorConfig, nextElement);
      }

      // <logDuration>
      else if (isElement(nextElement, WSIConstants.ELEM_LOG_DURATION))
      {
        monitorConfig.setLogDuration(getIntValue(nextElement));
        if (monitorConfig.getLogDuration() < 0)
          throw new IllegalArgumentException(
            getMessage(
              "config09",
              WSIConstants.ELEM_LOG_DURATION,
              "Parameter value can't be negative. Element name:"));
        isLogDurationDefine = true;
      }

      // <cleanupTimeoutSeconds>
      else if (
        isElement(nextElement, WSIConstants.ELEM_CLEANUP_TIMEOUT_SECONDS))
      {
        monitorConfig.setTimeout(getIntValue(nextElement));
        if (monitorConfig.getTimeout() < 0)
          throw new IllegalArgumentException(
            getMessage(
              "config09",
              WSIConstants.ELEM_CLEANUP_TIMEOUT_SECONDS,
              "Parameter value can't be negative. Element name:"));
        isCleanupTimeoutSecondsDefine = true;
      }

      // <manInTheMiddle>
      else if (isElement(nextElement, WSIConstants.ELEM_MAN_IN_THE_MIDDLE))
      {
        ManInTheMiddle manInTheMiddle = new ManInTheMiddleImpl();
        monitorConfig.setManInTheMiddle(manInTheMiddle);
        parseManInTheMiddleElement(manInTheMiddle, nextElement);
      }

      // <comment>
      else if (isElement(nextElement, WSIConstants.ELEM_COMMENT))
      {
        Comment comment = new CommentImpl();
        comment.setText(XMLUtils.getText(nextElement));
        monitorConfig.setComment(comment);
      }

      else
      {
        // ADD: handle invalid elements
      }

      // Get next element
      nextElement = XMLUtils.getNextSibling(nextElement);
    }

    if (!isLogDurationDefine)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config10",
          WSIConstants.ELEM_LOG_DURATION,
          "Element is missing. Element name:"));
    }
    if (!isCleanupTimeoutSecondsDefine)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config10",
          WSIConstants.ELEM_CLEANUP_TIMEOUT_SECONDS,
          "Element is missing. Element name:"));
    }
  }

  /**
   * Parse logFile element.
   */
  private void parseLogFileElement(
    MonitorConfig monitorConfig,
    Element element)
    throws WSIException
  {
    // Get the location attribute
    monitorConfig.setLogLocation(
      XMLUtils.getAttributeValue(element, WSIConstants.ATTR_LOCATION));

    // Get the replace attribute
    monitorConfig.setReplaceLog(
      XMLUtils.getBooleanValue(element, WSIConstants.ATTR_REPLACE, false));

    // Get first child element
    Element nextElement = XMLUtils.getFirstChild(element);

    // Process each child element
    while (nextElement != null)
    {
      // <addStyleSheet>
      if (isElement(nextElement, WSIConstants.ELEM_ADD_STYLE_SHEET))
      {
        AddStyleSheet addStyleSheet = new AddStyleSheetImpl();

        // Parse the element  
        TestUtils.parseAddStyleSheet(
          nextElement,
          addStyleSheet,
          WSIConstants.DEFAULT_LOG_XSL);

        // Set add style sheet
        monitorConfig.setAddStyleSheet(addStyleSheet);
      }

      else
      {
        // ADD: handle invalid elements
      }

      // Get next element
      nextElement = XMLUtils.getNextSibling(nextElement);
    }
  }

  /**
   * Parse manIntheMiddle element.
   */
  private void parseManInTheMiddleElement(
    ManInTheMiddle manInTheMiddle,
    Element element)
    throws WSIException
  {
    // Get first child element
    Element nextElement = XMLUtils.getFirstChild(element);

    // Process each child element
    while (nextElement != null)
    {
      // <redirect>
      if (isElement(nextElement, WSIConstants.ELEM_REDIRECT))
      {
        Redirect redirect = new RedirectImpl();
        manInTheMiddle.addRedirect(redirect);
        parseRedirectElement(redirect, nextElement);
      }

      // <comment>
      else if (isElement(nextElement, WSIConstants.ELEM_COMMENT))
      {
        Comment comment = new CommentImpl();
        comment.setText(XMLUtils.getText(nextElement));
        manInTheMiddle.setComment(comment);
      }

      else
      {
        // ADD: handle invalid elements
      }

      // Get next element
      nextElement = XMLUtils.getNextSibling(nextElement);
    }
  }

  /**
   * Parse redirect element.
   */
  private void parseRedirectElement(Redirect redirect, Element element)
    throws WSIException
  {
    // Get first child element
    Element nextElement = XMLUtils.getFirstChild(element);

    boolean isListenPortDefine = false;
    boolean isSchemeAndHostPortDefine = false;
    boolean isMaxConnectionsDefine = false;
    boolean isReadTimeoutSecondsDefine = false;

    // Process each child element
    while (nextElement != null)
    {
      // <listenPort>
      if (isElement(nextElement, WSIConstants.ELEM_LISTEN_PORT))
      {
        redirect.setListenPort(getIntValue(nextElement));
        isListenPortDefine = true;
      }

      // <schemeAndHostPort>
      else if (isElement(nextElement, WSIConstants.ELEM_SCHEME_AND_HOSTPORT))
      {
        redirect.setHost(XMLUtils.getText(nextElement));
        isSchemeAndHostPortDefine = true;
      }

      // <maxConnections>
      else if (isElement(nextElement, WSIConstants.ELEM_MAX_CONNECTIONS))
      {
        redirect.setMaxConnections(getIntValue(nextElement));
        isMaxConnectionsDefine = true;
      }

      // <readTimeoutSeconds>
      else if (isElement(nextElement, WSIConstants.ELEM_READ_TIMEOUT_SECONDS))
      {
        redirect.setReadTimeoutSeconds(getIntValue(nextElement));
        if (redirect.getReadTimeoutSeconds() < 0)
        {
          throw new IllegalArgumentException(
            getMessage(
              "config09",
              WSIConstants.ELEM_READ_TIMEOUT_SECONDS,
              "Parameter value can't be negative. Element name:"));
        }
        isReadTimeoutSecondsDefine = true;
      }

      // <comment>
      else if (isElement(nextElement, WSIConstants.ELEM_COMMENT))
      {
        Comment comment = new CommentImpl();
        comment.setText(XMLUtils.getText(nextElement));
        redirect.setComment(comment);
      }

      else
      {
        // ADD: handle invalid elements
      }

      // Get next element
      nextElement = XMLUtils.getNextSibling(nextElement);
    }

    if (!isListenPortDefine)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config10",
          WSIConstants.ELEM_LISTEN_PORT,
          "Element is missing. Element name:"));
    }
    if (!isSchemeAndHostPortDefine)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config10",
          WSIConstants.ELEM_SCHEME_AND_HOSTPORT,
          "Element is missing. Element name:"));

    }
    if (!isMaxConnectionsDefine)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config10",
          WSIConstants.ELEM_MAX_CONNECTIONS,
          "Element is missing. Element name:"));
    }
    if (!isReadTimeoutSecondsDefine)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config10",
          WSIConstants.ELEM_READ_TIMEOUT_SECONDS,
          "Element is missing. Element name:"));
    }
  }

  /**
   * Determine if this element matches specified local name in the analyzer config namespace.
   */
  private boolean isElement(Element element, String localName)
  {
    return isElement(
      element,
      WSIConstants.NS_URI_WSI_MONITOR_CONFIG,
      localName);
  }

  /**
   * Determine if this element matches specified local name in the analyzer config namespace.
   */
  private boolean isElement(
    Element element,
    String namespace,
    String localName)
  {
    return XMLUtils.isElement(element, namespace, localName);
  }

  /**
   * Get element text as an int.
   */
  private int getIntValue(Element element) throws WSIException
  {
    int returnValue;
    String intValue = null;

    // Get value as a string
    if ((intValue = XMLUtils.getText(element)) == null)
    {
      throw new WSIException(
        getMessage(
          "config04",
          element.getLocalName() + "Element must contain an integer value:"));
    }

    else
    {
      returnValue = Integer.valueOf(intValue).intValue();
    }

    // Return int
    return returnValue;
  }

  /**
   * Get message from resource bundle.
   */
  private String getMessage(String messageID, String defaultMessage)
  {
    return getMessage(messageID, null, defaultMessage);
  }

  /**
   * Get message from resource bundle.
   */
  private String getMessage(
    String messageID,
    String messageData,
    String defaultMessage)
  {
    String message = defaultMessage;
    if (messageList != null)
      message = messageList.getMessage(messageID, messageData, defaultMessage);
    else
      message += " " + messageData + ".";

    return message;
  }
}
