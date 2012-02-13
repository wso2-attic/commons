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
package org.eclipse.wst.wsi.internal.core.util;

import java.util.ResourceBundle;

/**
 * This class creates and maintains a message list.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class MessageList
{
  /**
   * Message resource bundle.
   */
  private ResourceBundle messageResourceBundle = null;

  /**
   * Constructor for MessageList.
   * @param resourceBundleName  a resource bundle name.
   */
  public MessageList(String resourceBundleName)
  {
    this.messageResourceBundle = ResourceBundle.getBundle(resourceBundleName);
  }

  /**
   * Get a message from the resource bundle.
   * @param key                 a key.
   * @param defaultMessage      a default message.
   * @return message from the resource bundle.
   */
  public String getMessage(String key, String defaultMessage)
  {
    return getMessage(key, null, defaultMessage);
  }

  /**
   * Get a message from the resource bundle.
   * @param key                 a key.
   * @param messageData         message data.
   * @param defaultMessage      a default message.
   * @return message from the resource bundle.
   */
  public String getMessage(
    String key,
    String messageData,
    String defaultMessage)
  {
    String message = defaultMessage;

    try
    {
      // Get message
      message = messageResourceBundle.getString(key);
    }

    catch (Exception e)
    {
      // Ignore and just use default message
    }

    if (messageData != null)
      message += " " + messageData;

    // Return message
    return message;
  }

  /**
   * Print a message from the resource bundle.
   * @param key                 a key.
   * @param defaultMessage      a default message.
   */
  public void printMessage(String key, String defaultMessage)
  {
    printMessage(key, null, defaultMessage);
  }

  /**
   * Print a message from the resource bundle.
   * @param key                 a key.
   * @param messageData         message data.
   * @param defaultMessage      a default message.
   */
  public void printMessage(
    String key,
    String messageData,
    String defaultMessage)
  {
    // Print message
    System.out.println(getMessage(key, messageData, defaultMessage));
  }

  /**
   * Static get message.
   * @param resourceBundleName  resource bundle name.
   * @param key                 a key.
   * @param messageData         message data.
   * @param defaultMessage      a default message.
   * @return message.
   */
  public static String getMessage(
    String resourceBundleName,
    String key,
    String messageData,
    String defaultMessage)
  {
    String message = defaultMessage;
    ResourceBundle messageResourceBundle = null;

    try
    {
      // Get resource bundle
      messageResourceBundle = ResourceBundle.getBundle(resourceBundleName);

      // Get message
      message = messageResourceBundle.getString(key);
    }

    catch (Exception e)
    {
      // Ignore and just use default message
    }

    if (messageData != null)
      message += " " + messageData;

    if (!message.endsWith("."))
      message += ".";

    // Return message
    return message;
  }

  /**
   * Print message.
   * @param resourceBundleName  resource bundle name.
   * @param key                 a key.
   * @param messageData         message data.
   * @param defaultMessage      a default message.
   */
  public static void printMessage(
    String resourceBundleName,
    String key,
    String messageData,
    String defaultMessage)
  {
    // Print message
    System.out.println(
      getMessage(resourceBundleName, key, messageData, defaultMessage));
  }

}
