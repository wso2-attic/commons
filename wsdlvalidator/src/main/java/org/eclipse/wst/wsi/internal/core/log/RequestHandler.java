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
package org.eclipse.wst.wsi.internal.core.log;

import java.util.Date;

/**
 * Represents a TCP/IP request made between the client and the server.
 * Each request represents a request-response pair, where the request
 * is from client -> server, and the response is from server -> client.
 * 
 * @author lauzond
 */

public interface RequestHandler 
{
	/**
	 * Returns just the HTTP header of the request portion of this request.
	 * @return the content bytes
	 */
	byte[] getRequestHeader();

	/**
	 * Add the HTTP header of the request portion of this request.
	 * @param requestHeader byte[]
	 */
	void setRequestHeader(byte[] requestHeader);

	/**
	 * Returns just the HTTP header of the response portion of this request.
	 * @return the content bytes
	 */
	byte[] getResponseHeader();

	/**
	 * Add the HTTP header of the response portion of this request.
	 * @param responseHeader byte[]
	 */
	void setResponseHeader(byte[] responseHeader);

	/**
	 * Returns just the HTTP body of the request portion of this request.
	 * @return the content bytes
	 */
	byte[] getRequestContent();

	/**
	 * Add the HTTP body of the request portion of this request.
	 * @param requestContent byte[]
	 */
	void setRequestContent(byte[] requestContent);

	/**
	 * Returns just the HTTP body of the response portion of this request.
	 * @return the content bytes
	 */
	byte[] getResponseContent();

	/**
	 * Add the HTTP body of the response portion of this request.
	 * @param responseContent byte[]
	 */
	void setResponseContent(byte[] responseContent);

	/**
	 * Returns the time this request was made.
	 * @return the timestamp
	 */
	Date getDate();

	/**
	 * Add the time this request was made.
	 * @param date the time
	 */
    void setDate(Date date);

    /**
	 * Returns the local (client) port.
	 * @return the local port number
	 */
	int getLocalPort();

	/**
	 * Add the local (client) port.
	 * @param localPort the local port
	 */
	void setLocalPort(int localPort);

	/**
	 * Returns the remote (server) port.
	 * @return the remote port number
	 */
	int getRemotePort();

	/**
	 * Add the the remote (server) port.
	 * @param remotePort the remote port
	 */
	void setRemotePort(int remotePort);

	/**
	 * Returns the remote (server) host.
     * @return the remote host
	 */
	String getRemoteHost();

	/**
	 * Add the remote (server) host.
	 * @param remoteHost the remote host
	 */
	void setRemoteHost(String remoteHost);

	/**
	 * Returns the server's response time in milliseconds. 
	 * @return the server's response time
	 */
	long getResponseTime();

	/**
	 * Add the the server's response time in milliseconds. 
	 * @param responseTime the response time
	 */
	void setResponseTime(long responseTime);
}
