/*
 * Copyright 2006,2007 WSO2, Inc. http://www.wso2.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.monitor.jmx;

import org.wso2.monitor.MonitorBase;
import org.wso2.monitor.MonitorException;

import javax.management.remote.JMXServiceURL;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnector;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import javax.management.MBeanServerConnection;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * Abstract base class which handles the JMX connection to a given bean.
 */
public abstract class JMXClient extends MonitorBase {
    protected MBeanServerConnection serverConnection;
    protected JMXConnector jmxConnector;
    protected ObjectName beanObjectName;
    protected String connectorAddress;
    protected String beanName;
    protected boolean hasConnected;

    public JMXClient(String connectorAddress, String beanName) {
        this.connectorAddress = connectorAddress;
        this.beanName = beanName;
    }

    /**
     * Get the URL of the server being monitored.
     *
     * @return String URL.
     */
    public String getConnectorAddress() {
        return connectorAddress;
    }

    /**
     * Set the URL of the server being monitored.
     *
     * @param connectorAddress URL of the JMX connector.
     */
    public void setConnectorAddress(String connectorAddress) {
        this.connectorAddress = connectorAddress;
    }

    /**
     * Get the name of the MBean being used for monitoring.
     *
     * @return String MBean name.
     */
    public String getMBeanName() {
        return beanName;
    }

    /**
     * Set the name of the MBean being used for monitoring.
     *
     * @param mBeanName MBean object to use for monitoring.
     */
    public void setMBeanName(String mBeanName) {
        this.beanName = mBeanName;
    }

    /**
     * Connects to MBean server and instantiates name of MBean to be used for monitoring.
     */
    public void connectToServer() throws MonitorException {
        try {
            JMXServiceURL url = new JMXServiceURL(connectorAddress);
            jmxConnector = JMXConnectorFactory.connect(url);
            serverConnection = jmxConnector.getMBeanServerConnection();
            beanObjectName = new ObjectName(beanName);
            hasConnected = true;
        } catch (MalformedURLException e) {
            log.error("JMX Agent URL incorrect", e);
            throw new MonitorException("JMX Agent URL incorrect", e);
        } catch (IOException e) {
            log.error("IO Error " + e.getLocalizedMessage());
            throw new MonitorException("IO Error " + e.getLocalizedMessage());
        } catch (MalformedObjectNameException e) {
            log.error("MBean name incorrect", e);
            throw new MonitorException("MBean name incorrect", e);
        }
    }

    /**
     * Disconnects from MBean server.
     */
    public void disconnectFromServer() throws MonitorException {
        try {
            jmxConnector.close();
            serverConnection = null;
            hasConnected = false;
        } catch (MalformedURLException e) {
            log.error("JMX Agent URL incorrect", e);
            throw new MonitorException("JMX Agent URL incorrect", e);
        } catch (IOException e) {
            log.error("IO Error", e);
            throw new MonitorException("IO Error " + e.getLocalizedMessage());
        }
    }

   /**
     * Check if client has connected to server sucessfully and the target MBean is registered.
     *
     * @return true if connected to server.
     */
    public boolean isConnected() {
        boolean connected = false;
        if (serverConnection != null) {
            try {
                connected = serverConnection.isRegistered(beanObjectName);
            } catch (IOException ioe) {
                log.error("IO Error", ioe);
            }
        }
        return connected;
    }

    /**
     * Indicates that a connection to the current server was extablished in this session and has not
     * been stopped by the user. Used to determine that an error has occurred, if the connection is
     * lost.
     *
     * @return true if a connection has been established in the current session.
     */
    public boolean getHasConnected() {
        return hasConnected;
    }
}
