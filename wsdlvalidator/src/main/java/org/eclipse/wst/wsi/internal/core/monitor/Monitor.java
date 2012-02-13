/*******************************************************************************
 * Copyright (c) 2002-2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.ToolInfo;
import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.WSIFileNotFoundException;
import org.eclipse.wst.wsi.internal.core.document.DocumentFactory;
import org.eclipse.wst.wsi.internal.core.log.Log;
import org.eclipse.wst.wsi.internal.core.log.LogWriter;
import org.eclipse.wst.wsi.internal.core.monitor.config.ManInTheMiddle;
import org.eclipse.wst.wsi.internal.core.monitor.config.MonitorConfig;
import org.eclipse.wst.wsi.internal.core.monitor.config.Redirect;
import org.eclipse.wst.wsi.internal.core.util.MessageList;
import org.eclipse.wst.wsi.internal.core.util.TestUtils;

/**
 * Message Monitor.
 *
 * @author Peter Brittenham (peterbr.us.ibm.com)
 */

public class Monitor
{
  /**
   * Message list.
   */
  protected MessageList messageList = null;

  private final static String RESOURCE_BUNDLE_NAME =
    "org.eclipse.wst.wsi.internal.core.monitor.Monitor";

  public final static String USAGE_MESSAGE =
    "Usage: Monitor -config <configFilename>";

  /**
   * Conversation ID.
   */
  private int conversationId = 1;

  private Log log = null;
  private LogWriter logWriter = null;
  private MonitorConfig monitorConfig = null;

  /**
  * Tool information.
  */
  public static final String TOOL_NAME = "Monitor";

  protected ToolInfo toolInfo = null;

  protected Vector listenerList = new Vector();

  protected MessageEntryQueue messageEntryQueue = null;

  /**
   * Message monitor.
   * @param args command line arguments.
   * @throws WSIException if there is a problem creating a Monitor object.
   */
  public Monitor(String[] args) throws WSIException
  {
    // Create message list
    this.messageList = new MessageList(RESOURCE_BUNDLE_NAME);

    // Tool information
    toolInfo = new ToolInfo(TOOL_NAME);

    // Create document factory
    DocumentFactory documentFactory = DocumentFactory.newInstance();

    // Get new config object
    monitorConfig = documentFactory.newMonitorConfig();
    monitorConfig.init(this.messageList);

    // Parse command line arguments
    monitorConfig.parseArgs(args);

    String logLocation = monitorConfig.getLogLocation();
    if (logLocation.indexOf(WSIConstants.PATH_SEPARATOR) > -1)
    {
      throw new WSIException(
        messageList.getMessage(
          "config11",
          monitorConfig.getLogLocation(),
          "The log file location value cannot contain the pass separator character:"));
    }

    File file = null;
    try
    {
      // Get file object for log file
      file = new File(monitorConfig.getLogLocation());
    }

    catch (Exception e)
    {
      throw new WSIException(
        messageList.getMessage("config07", "Could not get log file location."),
        e);
    }

    // If replace flag is false and file exists, then throw exception
    if (file.exists() && !monitorConfig.getReplaceLog())
    {
      throw new IllegalArgumentException(
        messageList.getMessage(
          "config08",
          monitorConfig.getLogLocation(),
          "Log file already exists:"));
    }

    try
    {
      // Create output file
      log = documentFactory.newLog();

      // Set style sheet string
      log.setStyleSheetString(
        monitorConfig.getAddStyleSheet().getStyleSheetString());

      // Get log writer
      logWriter = documentFactory.newLogWriter();
      logWriter.setWriter(monitorConfig.getLogLocation());

      // Write start of log file
      logWriter.write(new StringReader(log.getStartXMLString("")));

      // Write monitor tool information
      logWriter.write(new StringReader(toXMLString("")));

      // Create log entry queue
      messageEntryQueue = new MessageEntryQueue(this, log, logWriter);
    }

    catch (Exception e)
    {
      throw new WSIException(
        messageList.getMessage(
          "error03",
          "Could not create log or log writer."),
        e);
    }

    // Get manInTheMiddle settings
    ManInTheMiddle manInTheMiddle = monitorConfig.getManInTheMiddle();

    // Get list of redirects
    Iterator iterator = manInTheMiddle.getRedirectList().iterator();

    // Process each redirect
    Redirect redirect;
    while (iterator.hasNext())
    {
      // Get next redirect
      redirect = (Redirect) iterator.next();

      // Create server socket socket listener
      listenerList.add(new ServerSocketListener(this, redirect));
    }

    // Add shutdown hook
    Runtime.getRuntime().addShutdownHook(new ShutdownHook());

    // Create and start console
    Console console = new Console();
    console.start();
  }

  /**
   * Get the monitor config file.
   * @return the monitor config file.
   */
  public MonitorConfig getMonitorConfig()
  {
    return monitorConfig;
  }

  /**
   * Get the log object.
   * @return the log object.
   */
  public Log getLog()
  {
    return this.log;
  }

  /**
   * Get the log entry queue object.
   * @return the log entry queue object.
   */
  public MessageEntryQueue getMessageEntryQueue()
  {
    return this.messageEntryQueue;
  }

  /**
   * Terminate the monitor.
   */
  void exitMonitor()
  {
    printMessage("stopping01", "Stopping the monitor...");
    System.exit(0);
  }

  /**
   * Stop the monitor because an exception occurred.
   */
  void exitMonitor(Exception e)
  {
    // Display error message
    printMessage(
      "stopping02",
      "Stopping monitor because an exception occurred.");
    System.err.println("EXCEPTION: " + e.toString());
    if (this.monitorConfig.getVerboseOption())
      e.printStackTrace();

    // Exit monitor
    exitMonitor();
  }

  /**
   * Stop the monitor.
   */
  void stopMonitor()
  {
    try
    {
      // Get list of listeners to stop
      Iterator iterator = listenerList.iterator();

      while (iterator.hasNext())
      {
        ((ServerSocketListener) iterator.next()).shutdown();
      }

      // Wait for the cleanup timeout seconds
      Thread.sleep(monitorConfig.getTimeout() * 1000);

      // Write end of log file
      if (logWriter != null)
      {
        logWriter.write(new StringReader(log.getEndXMLString("")));

        logWriter.close();
      }
    }

    catch (Exception e)
    {
      // ADD: How should this execption be handled?
    }

    printMessage("stopped01", "Monitor stopped.");
  }

  /**
   * Command line interface.
   * @param args command line arguments.
   */
  public static void main(String[] args)
  {
    Monitor monitor = null;

    try
    {
      if (args.length < 2)
      {
        staticPrintMessage("usage01", USAGE_MESSAGE);
        System.exit(1);
      }

      if (!args[0].equalsIgnoreCase("-config"))
      {
        staticPrintMessage("usage01", USAGE_MESSAGE);
        System.exit(1);
      }

      // Run the monitor
      monitor = new Monitor(args);
    }

    catch (Exception e)
    {
      boolean printStackTrace = true;
      String messageID;
      String defaultMessage;
      String messageData;

      if ((e instanceof WSIFileNotFoundException)
        || (e instanceof IllegalArgumentException))
      {
        printStackTrace = false;
        messageID = "error01";
        defaultMessage = "Monitor Error:";
        messageData = e.getMessage();
      }

      else
      {
        printStackTrace = true;
        messageID = "error02";
        defaultMessage = "Monitor Stopped By Exception:";
        messageData = e.toString();
      }

      if (monitor != null)
        monitor.printMessage(messageID, messageData, defaultMessage);
      else
        Monitor.staticPrintMessage(messageID, messageData, defaultMessage);

      if (printStackTrace)
        e.printStackTrace();

      // Exit
      if (monitor != null)
        monitor.exitMonitor();
      else
        System.exit(2);
    }
  }

  /**
   * Print a message from the resource bundle.
   * @param key a key.
   * @param defaultMessage a default message.
   */
  public void printMessage(String key, String defaultMessage)
  {
    printMessage(key, null, defaultMessage);
  }

  /**
   * Print a message from the resource bundle.
   * @param key a key.
   * @param messageData message data.
   * @param defaultMessage a default message.
   */
  public void printMessage(
    String key,
    String messageData,
    String defaultMessage)
  {
    messageList.printMessage(key, messageData, defaultMessage);
  }

  /**
   * Print message.
   * @param key a key.
   * @param defaultMessage a default message.
   */
  public static void staticPrintMessage(String key, String defaultMessage)
  {
    staticPrintMessage(key, null, defaultMessage);
  }

  /**
   * Print message.
   * @param key a key.
   * @param messageData message data.
   * @param defaultMessage a default message.
  
   */
  public static void staticPrintMessage(
    String key,
    String messageData,
    String defaultMessage)
  {
    MessageList.printMessage(
      RESOURCE_BUNDLE_NAME,
      key,
      messageData,
      defaultMessage);
  }

  /**
   * Get the next conversation identifier.
   * @return the next conversation identifier.
   */
  synchronized int getNextConversationId()
  {
    return conversationId++;
  }

  /**
   * Return XML string representation of this object.
   * @param namespaceName namespace prefix.
   * @return XML string representation of this object.
   */
  public String toXMLString(String namespaceName)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    // Start
    pw.print(toolInfo.getStartXMLString(namespaceName));

    // Config
    pw.print(
      monitorConfig.toXMLString(WSIConstants.NS_NAME_WSI_MONITOR_CONFIG));

    // End
    pw.println(toolInfo.getEndXMLString(namespaceName));

    return sw.toString();
  }

  /**
   * Shutdown hook.
   */
  class ShutdownHook extends Thread
  {
    /**
     * Run shutdown procedure.
     */
    public void run()
    {
      stopMonitor();
    }
  }

  /**
   * Run command from console.
   */
  class Console extends Thread
  {
    /**
     * Prompt user and wait for input.
     */
    public void run()
    {
      // Get the exit string
      String exitString = messageList.getMessage("exit01", "exit");

      // Display options and how to stop application
      TestUtils.printToolInfo(toolInfo);
      System.out.print(monitorConfig.toString());
      System.out.println(" ");
      printMessage(
        "start01",
        "The "
          + toolInfo.getName()
          + " tool is ready to intercept and log web service messages.");
      printMessage(
        "start02",
        "Type \"exit\" to stop the " + toolInfo.getName() + ".");
      System.out.println(" ");

      // Get the time to stop accepting connections
      long stopTime =
        System.currentTimeMillis() + (monitorConfig.getLogDuration() * 1000);
      // SS

      try
      {
        // Get stdin as a buffered reader
        BufferedReader reader =
          new BufferedReader(new InputStreamReader(System.in));

        // Process input from console
        boolean exit = false;
        while ((!exit) && (System.currentTimeMillis() < stopTime))
        { // SS
          // Sleep
          Thread.sleep(500);

          // Check for user input
          if (reader.ready())
          {
            if (reader.readLine().equalsIgnoreCase(exitString))
              exit = true;
          }
        }
      }

      catch (Exception e)
      {
        // ADD: How should this be handled?
        System.err.println(e.toString());
      }

      // Exit
      exitMonitor();
    }
  }
}
