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

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.wst.wsi.internal.core.monitor.config.Redirect;

/**
 * A server socket listener.
 *
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class ServerSocketListener
  extends Thread
  implements ConnectionListener // SS
{
  protected Monitor monitor = null;
  protected Redirect redirect = null;

  protected ServerSocket serverSocket = null;
  protected boolean stopServerSocket = false;

  protected Vector connectionList = new Vector();

  /**
   * Create server socket listener.
   * @param monitor a Monitor object.
   * @param redirect a Redirect object.
   */
  public ServerSocketListener(Monitor monitor, Redirect redirect)
  {
    this.monitor = monitor;
    this.redirect = redirect;

    // Start listening
    start();
  }

  /**
   * Run the thread and listen for a connection on the socket.
   */
  public void run()
  {
    try
    {
      // Create server socket
      serverSocket =
        new ServerSocket(
          redirect.getListenPort(),
          redirect.getMaxConnections());
      // SS

      // Set timeout so that server socket will continue to accept connections
      serverSocket.setSoTimeout(0);

      // Listen for connections until interrupted
      while (!stopServerSocket)
      {
        // Accept connections
        Socket socket = serverSocket.accept();

        // If the server socket was NOT stopped, process connection
        if (!stopServerSocket)
        {
          // Create new connection
          SocketConnection socketConnection =
            new SocketConnection(this.monitor, this.redirect, socket);

          //register as a listener
          socketConnection.addConnectionListener(this); // SS

          // Add to connection list
          connectionList.add(socketConnection);
        }
      }
    }

    catch (Exception e)
    {
      // If the exception was NOT caused by closing the socket, then stop monitor
      if (!e.getMessage().equals("socket closed"))
      {
        monitor.exitMonitor(e);
      }
    }
  }

  /**
   * Stop listening for a connection on the socket.
   */
  public void shutdown()
  {
    try
    {
      // Shutdown all active connections
      Iterator iterator = connectionList.iterator();
      while (iterator.hasNext())
      {
        ((SocketConnection) iterator.next()).shutdown();
      }

      // Tell the server socket listening thread to stop
      this.stopServerSocket = true;

      // Set the server socket timeout to 1 ms
      serverSocket.setSoTimeout(1);

      // Close the server socket
      if (serverSocket != null)
        serverSocket.close();
    }

    catch (Exception e)
    {
    }
  }

  // ==== SS start ====
  /* (non-Javadoc)
   * @see org.wsi.test.monitor.ConnectionListener#connectionClosed()
   */
  public void connectionClosed(SocketConnection connection)
  {
    connectionList.remove(connection);
  }
  // ==== SS end ====
}
