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
package org.eclipse.wst.wsi.internal.core.monitor.config;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsi.internal.core.WSIConstants;
import org.eclipse.wst.wsi.internal.core.document.DocumentElement;

/**
 * The interface for monitor config Redirect element.
 * 
 * @version 1.0.1
 * @author Peter Brittenham (peterbr@us.ibm.com)
 */
public interface Redirect extends DocumentElement
{
  /**
   * Element name.
   */
  public static final String ELEM_NAME = WSIConstants.ELEM_REDIRECT;

  /**
   * QName.
   */
  public static final QName QNAME =
    new QName(WSIConstants.NS_URI_WSI_MONITOR_CONFIG, ELEM_NAME);

  /**
   * Get comment.
   * @return comment.
   * @see #setComment
   */
  public Comment getComment();

  /**
   * Set comment.
   * @param comment a Commentobject.
   * @see #getComment
   */
  public void setComment(Comment comment);

  /**
   * Get listen port.
   * @return listen port.
   * @see #setListenPort
   */
  public int getListenPort();

  /**
   * Set listen port.
   * @param listenPort listen port.
   * @see #getListenPort
   */
  public void setListenPort(int listenPort);

  /**
   * Get host.
   * @return host.
   * @see #setHost
   */
  public String getHost();

  /**
   * Set host.
   * @param host  host.
   * @see #getHost
   */
  public void setHost(String host);

  /**
   * Get send to port.
   * @return send to port.
   */
  public int getToPort();

  /**
   * Get send to host.
   * @return send to host.
   */
  public String getToHost();

  /**
   * Get send to protocol.
   * @return send to protocol.
   */
  public String getToProtocol();

  /**
   * Get maximum connections.
   * @return maximum connections.
   * @see #setMaxConnections
   */
  public int getMaxConnections();

  /**
   * Set maximum connections.
   * @param maxConnections maximum connections.
   * @see #getMaxConnections
   */
  public void setMaxConnections(int maxConnections);

  /**
   * Get read timeout seconds.
   * @return read timeout seconds.
   * @see #setReadTimeoutSeconds
   */
  public int getReadTimeoutSeconds();

  /**
   * Set read timeout seconds.
   * @param readTimeoutSeconds read timeout seconds.
   * @see #getReadTimeoutSeconds
   */
  public void setReadTimeoutSeconds(int readTimeoutSeconds);
}
