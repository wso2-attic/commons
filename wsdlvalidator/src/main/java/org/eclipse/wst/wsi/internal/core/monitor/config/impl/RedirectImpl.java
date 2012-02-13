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
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.monitor.config.Comment;
import org.eclipse.wst.wsi.internal.core.monitor.config.Redirect;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;

/**
 *  The implementation for monitor config Redirect element.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public class RedirectImpl implements Redirect
{
  /**
   * Comment.
   */
  protected Comment comment;

  /**
   * Listen port.
   */
  protected int listenPort;

  /**
   * Host.
   */
  protected String host;

  /**
   * To protocol.
   */
  protected String toProtocol;

  /**
   * To port.
   */
  protected int toPort;

  /**
   * To host.
   */
  protected String toHost;

  /**
   * Maximum connections.
   */
  protected int maxConnections;

  /**
   * Read timeout seconds.
   */
  protected int readTimeoutSeconds;

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#getComment()
   */
  public Comment getComment()
  {
    return this.comment;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#setComment(comment)
   */
  public void setComment(Comment comment)
  {
    this.comment = comment;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#getListenPort()
   */
  public int getListenPort()
  {
    return this.listenPort;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#setListenPort(int)
   */
  public void setListenPort(int listenPort)
  {
    this.listenPort = listenPort;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#getHost()
   */
  public String getHost()
  {
    return this.host;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#setHost(String)
   */
  public void setHost(String host)
  {
    this.host = host;

    // Parse location to get protocol, host and port
    URL url;
    try
    {
      url = new URL(host);
      this.toProtocol = url.getProtocol();
      this.toHost = url.getHost();
      this.toPort = url.getPort();
      if (this.toPort == -1)
      {
        if (this.toProtocol.equalsIgnoreCase("http"))
          this.toPort = 80;
        else
          this.toPort = 80;
      }
    }

    catch (MalformedURLException mue)
    {
      throw new IllegalArgumentException(
        "The "
          + WSIConstants.ELEM_SCHEME_AND_HOSTPORT
          + " option contains an invalid value: "
          + host);
    }

    if ((url.getPath() != null && !url.getPath().equals(""))
      || (url.getQuery() != null && !url.getQuery().equals("")))
      throw new IllegalArgumentException(
        "The "
          + WSIConstants.ELEM_SCHEME_AND_HOSTPORT
          + " option must contain only a scheme, host and port: "
          + host);
  }

  /**
   * Get to port.
   * @see org.eclipse.wst.wsi.monitor.config.Redirect#getToPort()
   */
  public int getToPort()
  {
    return this.toPort;
  }

  /**
   * Get to host.
   * @see org.eclipse.wst.wsi.monitor.config.Redirect#getToHost()
   */
  public String getToHost()
  {
    return this.toHost;
  }

  /**
   * Get to protocol.
   * @see org.eclipse.wst.wsi.monitor.config.Redirect#getToProtocol()
   */
  public String getToProtocol()
  {
    return this.toProtocol;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#getMaxConnections()
   */
  public int getMaxConnections()
  {
    return this.maxConnections;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#setMaxConnections(int)
   */
  public void setMaxConnections(int maxConnections)
  {
    this.maxConnections = maxConnections;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#getReadTimeoutSeconds()
   */
  public int getReadTimeoutSeconds()
  {
    return this.readTimeoutSeconds;
  }

  /* (non-Javadoc)
   * @see org.wsi.test.monitor.config.Redirect#setReadTimeoutSeconds(int)
   */
  public void setReadTimeoutSeconds(int readTimeoutSeconds)
  {
    this.readTimeoutSeconds = readTimeoutSeconds;
  }

  /**
   * Get string representation of this object.
   */
  public String toString()
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    pw.println("    comment ................... " + this.comment);
    pw.println("    listenPort ................ " + this.listenPort);
    pw.println("    host ...................... " + cutHost(this.host));
    pw.println("    maxConnections ............ " + this.maxConnections);
    pw.println("    readTimeoutSeconds ........ " + this.readTimeoutSeconds);

    return sw.toString();
  }

  /**
   * Formats host name according to the specification (path is ommited).
   * @param host
   * @return
   */
  private String cutHost(String host)
  {
    try
    {
      URL url = new URL(host);
      String port = url.getPort() > -1 ? String.valueOf(url.getPort()) : "80";
      // REMOVED: This code only works on 1.4
      // String.valueOf(url.getDefaultPort());
      return url.getProtocol() + "://" + url.getHost() + ":" + port;
    }
    catch (Exception e)
    {
      return host;
    }
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

    // Start element
    pw.println("        <" + nsName + WSIConstants.ELEM_REDIRECT + ">");

    // Comment
    if (this.comment != null)
    {
      pw.println(this.comment.toXMLString(nsName));
    }

    // listenPort
    pw.print("          <" + nsName + WSIConstants.ELEM_LISTEN_PORT + ">");
    pw.print(getListenPort());
    pw.println("</" + nsName + WSIConstants.ELEM_LISTEN_PORT + ">");

    // schemeAndHostPort
    pw.print(
      "          <" + nsName + WSIConstants.ELEM_SCHEME_AND_HOSTPORT + ">");
    pw.print(XMLUtils.xmlEscapedString(getHost()));
    pw.println("</" + nsName + WSIConstants.ELEM_SCHEME_AND_HOSTPORT + ">");

    // maxConnections
    pw.print("          <" + nsName + WSIConstants.ELEM_MAX_CONNECTIONS + ">");
    pw.print(getMaxConnections());
    pw.println("</" + nsName + WSIConstants.ELEM_MAX_CONNECTIONS + ">");

    // readTimeoutSeconds
    pw.print(
      "          <" + nsName + WSIConstants.ELEM_READ_TIMEOUT_SECONDS + ">");
    pw.print(getReadTimeoutSeconds());
    pw.println("</" + nsName + WSIConstants.ELEM_READ_TIMEOUT_SECONDS + ">");

    // End Element
    pw.println("        </" + nsName + WSIConstants.ELEM_REDIRECT + ">");

    return sw.toString();
  }

}
