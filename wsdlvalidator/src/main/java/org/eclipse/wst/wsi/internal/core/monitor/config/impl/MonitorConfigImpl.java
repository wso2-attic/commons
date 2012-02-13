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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.common.AddStyleSheet;
import org.eclipse.wst.wsi.internal.core.common.impl.AddStyleSheetImpl;
import org.eclipse.wst.wsi.internal.core.monitor.Monitor;
import org.eclipse.wst.wsi.internal.core.monitor.config.Comment;
import org.eclipse.wst.wsi.internal.core.monitor.config.ManInTheMiddle;
import org.eclipse.wst.wsi.internal.core.monitor.config.MonitorConfig;
import org.eclipse.wst.wsi.internal.core.monitor.config.MonitorConfigReader;
import org.eclipse.wst.wsi.internal.core.monitor.config.Redirect;
import org.eclipse.wst.wsi.internal.core.util.MessageList;

/**
 * This is the implementation for the monitor configuration file.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class MonitorConfigImpl implements MonitorConfig
{
  /**
   * Message list reference.
   */
  protected MessageList messageList = null;

  /**
   * Monitor configuration file URI.
   */
  protected String documentURI = null;

  /**
   * Comment.
   */
  protected Comment comment = null;

  /**
   * Log location.
   */
  protected String logURI;

  /**
   * Replace log indicator.
   */
  protected boolean replaceLog = false;

  /**
   * Style sheet declaration.
   */
  protected AddStyleSheet addStyleSheet;

  /**
   * Log duration.
   */
  protected int logDuration = 600;

  /**
   * Timeout.
   */
  protected int timeout = 3;

  /**
   * Man-in-the-middle settings.
   */
  protected ManInTheMiddle manInTheMiddle = null;

  /**
   * Verbose option.
   */
  protected boolean verbose = false;

  /**
   * Initialize monitor config.
   */
  public void init(MessageList messageList)
  {
    this.messageList = messageList;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#getComment()
   */
  public Comment getComment()
  {
    return this.comment;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#setComment(Comment)
   */
  public void setComment(Comment comment)
  {
    this.comment = comment;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#getLogLocation()
   */
  public String getLogLocation()
  {
    return this.logURI;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#setLogLocation(String)
   */
  public void setLogLocation(String logURI)
  {
    this.logURI = logURI;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#getReplaceLog()
   */
  public boolean getReplaceLog()
  {
    return this.replaceLog;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#setReplaceLog(boolean)
   */
  public void setReplaceLog(boolean replaceLog)
  {
    this.replaceLog = replaceLog;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#getAddStyleSheet()
   */
  public AddStyleSheet getAddStyleSheet()
  {
    return this.addStyleSheet;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#setAddStyleSheet(org.wsi.test.common.AddStyleSheet)
   */
  public void setAddStyleSheet(AddStyleSheet addStyleSheet)
  {
    this.addStyleSheet = addStyleSheet;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#getLogDuration()
   */
  public int getLogDuration()
  {
    return logDuration;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#setLogDuration(int)
   */
  public void setLogDuration(int logDuration)
  {
    this.logDuration = logDuration;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#getTimeout()
   */
  public int getTimeout()
  {
    return this.timeout;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#setTimeout(int)
   */
  public void setTimeout(int timeout)
  {
    this.timeout = timeout;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#getManInTheMiddle()
   */
  public ManInTheMiddle getManInTheMiddle()
  {
    return manInTheMiddle;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#setManInTheMiddle(ManInTheMiddle)
   */
  public void setManInTheMiddle(ManInTheMiddle manInTheMiddle)
  {
    this.manInTheMiddle = manInTheMiddle;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.WSIDocument#getLocation()
   */
  public String getLocation()
  {
    return this.documentURI;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.document.WSIDocument#setLocation(String)
   */
  public void setLocation(String documentURI)
  {
    this.documentURI = documentURI;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#getVerboseOption()
   */
  public boolean getVerboseOption()
  {
    return this.verbose;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.MonitorConfig#setVerboseOption(boolean)
   */
  public void setVerboseOption(boolean verbose)
  {
    this.verbose = verbose;
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

    // Config options
    pw.println("    <" + nsName + ELEM_NAME + ">");

    // Comment
    if (this.comment != null)
    {
      pw.print(comment.toXMLString(nsName));
    }

    // Log file
    pw.print("      <" + nsName + WSIConstants.ELEM_LOG_FILE + " ");
    pw.print(WSIConstants.ATTR_REPLACE + "=\"" + this.replaceLog + "\" ");
    pw.println(WSIConstants.ATTR_LOCATION + "=\"" + this.logURI + "\">");
    pw.print(getAddStyleSheet().toXMLString(namespaceName));
    pw.println("    </" + nsName + WSIConstants.ELEM_LOG_FILE + ">");

    // Log duration
    pw.print("      <" + nsName + WSIConstants.ELEM_LOG_DURATION + ">");
    pw.print(this.logDuration);
    pw.println("</" + nsName + WSIConstants.ELEM_LOG_DURATION + ">");

    // Timeout
    pw.print(
      "      <" + nsName + WSIConstants.ELEM_CLEANUP_TIMEOUT_SECONDS + ">");
    pw.print(this.timeout);
    pw.println("</" + nsName + WSIConstants.ELEM_CLEANUP_TIMEOUT_SECONDS + ">");

    // Man-in-the-middle
    if (this.manInTheMiddle != null)
    {
      pw.print(manInTheMiddle.toXMLString(nsName));
    }

    // End element
    pw.println("    </" + nsName + ELEM_NAME + ">");

    return sw.toString();
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println("  comment ..................... " + this.comment);
    pw.println("  logURI ...................... " + this.logURI);
    pw.println("  replaceLog .................. " + this.replaceLog);
    pw.println("  logDuration ................. " + this.logDuration);
    pw.println("  timeout ..................... " + this.timeout);

    // addStyleSheet
    if (this.addStyleSheet != null)
      pw.println(
        "  addStyleSheet ............... " + this.addStyleSheet.toString());

    // Man-in-the-middle
    if (this.manInTheMiddle != null)
    {
      pw.print(this.manInTheMiddle.toString());
    }

    return sw.toString();
  }

  /**
   * Set config values from another monitor config object.
   */
  private void setConfig(MonitorConfig monitorConfig)
  {
    // ADD: Should this be a clone operation?
    this.comment = monitorConfig.getComment();
    this.documentURI = monitorConfig.getLocation();
    this.logURI = monitorConfig.getLogLocation();
    this.replaceLog = monitorConfig.getReplaceLog();
    this.logDuration = monitorConfig.getLogDuration();
    this.timeout = monitorConfig.getTimeout();
    this.manInTheMiddle = monitorConfig.getManInTheMiddle();
    this.addStyleSheet = monitorConfig.getAddStyleSheet();
    this.verbose = monitorConfig.getVerboseOption();
  }

  /**
   * Verify config options.
   */
  private void verifyConfig() throws WSIException
  {
    // If no redirect statements, then exit
    if ((this.manInTheMiddle == null)
      || (this.manInTheMiddle.getRedirectList() == null)
      || (this.manInTheMiddle.getRedirectList().size() == 0))
    {
      throw new IllegalArgumentException(
        getMessage(
          "config05",
          "Monitor config file did not contain any redirect statements."));
    }

    if (this.logURI == null)
    {
      throw new IllegalArgumentException(
        getMessage(
          "config06",
          "Monitor config file did not contain the log file URI."));
    }
  }

  /**
   * Parse the command line arguments.
   */
  public void parseArgs(String[] args) throws WSIException
  {
    // Get new config reader
    MonitorConfigReader monitorConfigReader = new MonitorConfigReaderImpl();

    // Monitor config object which will be merged after all of the input parms are processed
    MonitorConfig monitorConfigFromArgs = null;

    // If no input arguments, then throw exception
    if (args.length == 0)
    {
      // ADD: 
      throw new IllegalArgumentException(
        getMessage("usage01", Monitor.USAGE_MESSAGE));
    }

    // Parse the command line arguments to locate the config file option (if it was specified)
    for (int argCount = 0; argCount < args.length; argCount++)
    {
      // -configFile
      if ((args[argCount].equalsIgnoreCase("-config"))
        || (args[argCount].equals("-c")))
      {
        argCount++;
        monitorConfigFromArgs =
          monitorConfigReader.readMonitorConfig(args[argCount]);
      }
    }

    // If config file was not specified, then create analyzer config object
    if (monitorConfigFromArgs == null)
    {
      monitorConfigFromArgs = new MonitorConfigImpl();
      monitorConfigFromArgs.init(this.messageList);
    }

    // Parse all of the command line arguments
    for (int argCount = 0; argCount < args.length; argCount++)
    {
      // -configFile
      if ((args[argCount].equalsIgnoreCase("-config"))
        || (args[argCount].equals("-c")))
      {
        // Skip this option since it was already processed
        argCount++;
      }

      // -comment
      else if (
        (args[argCount].equalsIgnoreCase("-comment"))
          || (args[argCount].equals("-C")))
      {
        argCount++;
        Comment comment = new CommentImpl();
        comment.setText(args[argCount]);
        monitorConfigFromArgs.setComment(comment);
      }

      // -logFile
      else if (
        (args[argCount].equalsIgnoreCase("-logFile"))
          || (args[argCount].equals("-l")))
      {
        argCount++;
        monitorConfigFromArgs.setLogLocation(args[argCount]);
      }

      // -replace
      else if (
        (args[argCount].equalsIgnoreCase("-replace"))
          || (args[argCount].equals("-r")))
      {
        argCount++;
        monitorConfigFromArgs.setReplaceLog(
          Boolean.valueOf(args[argCount]).booleanValue());
      }

      // -logDuration
      else if (
        (args[argCount].equalsIgnoreCase("-logDuration"))
          || (args[argCount].equals("-d")))
      {
        argCount++;
        monitorConfigFromArgs.setLogDuration(
          Integer.valueOf(args[argCount]).intValue());
      }

      // -timeout
      else if (
        (args[argCount].equalsIgnoreCase("-timeout"))
          || (args[argCount].equals("-t")))
      {
        argCount++;
        monitorConfigFromArgs.setTimeout(
          Integer.valueOf(args[argCount]).intValue());
      }

      // -manInTheMiddle
      else if (
        (args[argCount].equalsIgnoreCase("-manInTheMiddle"))
          || (args[argCount].equals("-m")))
      {
        ManInTheMiddle manInTheMiddle = null;

        // Increment arg count
        argCount++;

        // Get the man-in-the-middle object
        if ((manInTheMiddle = monitorConfigFromArgs.getManInTheMiddle())
          == null)
          manInTheMiddle = new ManInTheMiddleImpl();

        // ADD: Check for correct number of remaining args

        // Create redirect
        Redirect redirect = new RedirectImpl();

        // Get the redirect values 
        redirect.setListenPort(Integer.valueOf(args[argCount++]).intValue());
        redirect.setHost(args[argCount++]);
        redirect.setMaxConnections(
          Integer.valueOf(args[argCount++]).intValue());
        redirect.setReadTimeoutSeconds(
          Integer.valueOf(args[argCount++]).intValue());

        // Add redirect
        manInTheMiddle.addRedirect(redirect);

        // Set the man-in-the-middle
        monitorConfigFromArgs.setManInTheMiddle(manInTheMiddle);
      }

      // -verbose
      else if (
        (args[argCount].equalsIgnoreCase("-verbose"))
          || (args[argCount].equals("-v")))
      {
        argCount++;
        //monitorConfigFromArgs.setVerboseOption(Boolean.valueOf(args[argCount]).booleanValue());
        monitorConfigFromArgs.setVerboseOption(true);
      }

      // -GUI
      else if (args[argCount].equalsIgnoreCase("-GUI"))
      {
        // Ignore -GUI option
      }

      else
      {
        throw new IllegalArgumentException(
          getMessage(
            "config01",
            args[argCount],
            "The specified option is not supported."));
      }
    }

    // If addStyleSheet was not specified, then create a comment version of it
    if (monitorConfigFromArgs.getAddStyleSheet() == null)
    {
      AddStyleSheet addStyleSheet = new AddStyleSheetImpl();
      addStyleSheet.setHref(WSIConstants.DEFAULT_LOG_XSL);
      addStyleSheet.setType(WSIConstants.DEFAULT_XSL_TYPE);
      addStyleSheet.setComment(true);
      monitorConfigFromArgs.setAddStyleSheet(addStyleSheet);
    }

    // Merge config info into this object
    setConfig(monitorConfigFromArgs);

    // Verify config options
    verifyConfig();
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
