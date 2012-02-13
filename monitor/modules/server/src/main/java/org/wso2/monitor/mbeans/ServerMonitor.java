/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
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
package org.wso2.monitor.mbeans;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;
import javax.management.JMException;
import javax.management.NotificationBroadcasterSupport;
import javax.management.Notification;
import javax.management.AttributeChangeNotification;

/**
 * Implementation of the server monitor MBean interface.
 */
public class ServerMonitor extends NotificationBroadcasterSupport implements ServerMonitorMBean {
    protected Logger log = Logger.getLogger(getClass());
    private long notificationSeqNum = 1;
    private boolean serverRunning;

    /**
     * Obtains an instance of the MBean Server and registers self as an MBean.
     */
    public ServerMonitor() {
        MBeanServer beanServer = getBeanServer();
        ObjectName beanName;

        try {
            beanName = new ObjectName("org.wso2.monitor.mbeans:type=ServerMonitor");
            beanServer.registerMBean(this, beanName);
        } catch (MalformedObjectNameException e) {
            log.error("Object name incorrect when registering MBean", e);
        } catch (JMException e) {
            log.error("Error registering MBean", e);
        }
    }

    /**
     * Gets any queued messages from the server.
     *
     * @return An XML string containing any queued messages.
     */
    public String getMessages() {
        String message;

        // Todo: collect and send queued/logged messages.
        message = "The monitored WSO2 Mashup Server instance is active.";
        return message;
    }

    /**
     * Send a given message to any registered notification listeners.
     *
     * Should be used by MBean methods to display messages on the server monitor.
     *
     * @param title String containing title of message.
     * @param message String containing message to be notified.
     * @param severity Used to specify message severity.
     *
     */
    public void notifyMonitor(String title, String message, int severity) {
        log.debug("notifyMonitor method invoked");
        Notification notification = new AttributeChangeNotification(this, notificationSeqNum++,
                                                System.currentTimeMillis(),
                                                message, title, "int", 0, severity);
        sendNotification(notification);
    }

    /**
     * Attempts to locate an MBean Server, failing which an instance is created.
     *
     * @return an instance of a MBean Server.
     */
    private MBeanServer getBeanServer() {
        MBeanServer beanServer;
        ArrayList serverList = MBeanServerFactory.findMBeanServer(null);

        if (serverList.size() > 0) {
            beanServer = (MBeanServer) serverList.get(0);
        } else {
            beanServer = MBeanServerFactory.createMBeanServer();
        }

        return beanServer;
    }

    /**
     * Indicates if the monitored server is running.
     * @return true if the server is running.
     */
    public boolean isServerRunning() {
        return serverRunning;
    }

    /**
     * Used to set the server status indication.
     * @param serverRunning true to indicate that the server is running,
     */
    public void setServerRunning(boolean serverRunning) {
        this.serverRunning = serverRunning;
    }
}
