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

import org.jdesktop.jdic.tray.TrayIcon;
import org.wso2.monitor.MessageDisplay;
import org.wso2.monitor.MonitorException;

import javax.management.AttributeChangeNotification;
import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ReflectionException;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import java.io.IOException;

/**
 * Communicates with ServerMonitor JMX MBean interface exposed by the server to be monitored.
 */
public class ServerMonitorClient extends JMXClient implements NotificationListener {

    // Server is assumed to be error until notification of an error is received.
    boolean serverErrorFree = true;
    boolean listenerRegistered = false;
    MessageDisplay display;

    /**
     * Calls the superclass constructor and sets the notification display component instance.
     *
     * @param display Implementation of MessageDisplay interface, used to show notification
     *                messages.
     * @param connectorAddress The URL to connect to the server for monitoring.
     * @param beanName Name of the MBean that will be used for monitoring.
     */
    public ServerMonitorClient(MessageDisplay display, String connectorAddress, String beanName) {
        super(connectorAddress, beanName);
        this.display = display;
    }

    /**
     * Indicates if the server is currently error free. This is determined based on the messages
     * received from the server. @see handleNotification method.
     *
     * @return true if server is running in an error free state.
     */
    public boolean isServerErrorFree() {
        return serverErrorFree;
    }

    /**
     * Sets the notification listener registration status.
     * @param listenerRegistered boolean indicating status.
     */
    public void setListenerRegistered(boolean listenerRegistered) {
        this.listenerRegistered = listenerRegistered;
    }

    /**
     * Indicates that the notification listener has been successfully registered.
     * @return true if the listener is registered.
     */
    public boolean isListenerRegistered() {
        return listenerRegistered;
    }

    /**
     * Registers self as a listener for MBean notifications.
     */
    public void registerListener() throws MonitorException {
        try {
            // If a connection has been established, add the notification listener.
            if (isConnected() && !isListenerRegistered()) {
                serverConnection.addNotificationListener(beanObjectName, this, null, null);
                listenerRegistered = true;
                log.debug("Registered listener successfully.");
            }
        } catch (InstanceNotFoundException e) {
            log.error("Named bean not found", e);
            throw new MonitorException("Named bean not found", e);
        } catch (IOException e) {
            log.error("IO Error invoking method", e);
            throw new MonitorException("IO Error invoking method", e);
        }
    }

    /**
     * Registers self as a listener for MBean notifications.
     */
    public void removeListener() throws MonitorException {
        try {
            // If a connection has been established, add the notification listener.
            if (isConnected() && isListenerRegistered()) {
                serverConnection.removeNotificationListener(beanObjectName, this);
                listenerRegistered = false;
                log.debug("Removed listener registration.");
            }
        } catch (InstanceNotFoundException e) {
            log.error("Named bean not found", e);
            throw new MonitorException("Named bean not found", e);
        } catch (ListenerNotFoundException e) {
            log.error("Listener not found", e);
            throw new MonitorException("Listener not found", e);
        } catch (IOException e) {
            log.error("IO Error invoking method", e);
            throw new MonitorException("IO Error invoking method", e);
        }
    }

   /**
     * Implementation of the NotificationListener's method which handles notifications. Currently
     * just displays the recieved message.
     *
     * @param notification The notification object.
     * @param handback     Added at the point of registering the listener, to be received by the
     *                     handler.
     */
    public void handleNotification(Notification notification, Object handback) {
        log.debug("Received notification.");

        // If the message is valid, display the contents using the system tray.
        if (notification != null && notification instanceof AttributeChangeNotification) {
            AttributeChangeNotification serverNotification =
                    (AttributeChangeNotification) notification;
            int newMessageStatus = (Integer) serverNotification.getNewValue();
            int oldMessageStatus = (Integer) serverNotification.getOldValue();

            /**
             * If an 'ERROR' level message is received from the server, set status to error and
             * reset to normal once any other type of message is received.
             */
            serverErrorFree = newMessageStatus != TrayIcon.ERROR_MESSAGE_TYPE;

            display.showMessage(serverNotification.getAttributeName(), notification.getMessage(),
                                newMessageStatus);
        }
    }

    /**
     * Gets any pending messages from the server being monitored.
     *
     * @return Messages from the server.
     */
    public String getMessages() throws MonitorException {
        String message = null;

        try {
            // If a connection has been established, get messages.
            if (isConnected()) {
                log.debug("Getting messages.");
                message = (String) serverConnection.invoke(beanObjectName, "getMessages", null,
                                                           null);
            }
        } catch (InstanceNotFoundException e) {
            log.error("Named bean not found", e);
            throw new MonitorException("Named bean not found", e);
        } catch (MBeanException e) {
            log.error("MBean error", e);
            throw new MonitorException("MBean error", e);
        } catch (ReflectionException e) {
            log.error("Error invoking method", e);
            throw new MonitorException("Reflection error invoking method", e);
        } catch (IOException e) {
            log.error("IO Error invoking method", e);
            throw new MonitorException("IO Error invoking method", e);
        }
        return message;
    }

    /**
     * Gets the run status of the monitored server.
     *
     * @return Messages from the server.
     */
    public boolean isServerRunning() throws MonitorException {
        Boolean running = false;

        try {
            // If a connection has been established, get messages.
            if (isConnected()) {
                running = (Boolean) serverConnection.getAttribute(beanObjectName, "ServerRunning");
            }
        } catch (InstanceNotFoundException e) {
            log.error("Named bean not found", e);
            throw new MonitorException("Named bean not found", e);
        } catch (MBeanException e) {
            log.error("MBean error", e);
            throw new MonitorException("MBean error", e);
        } catch (ReflectionException e) {
            log.error("Error invoking method", e);
            throw new MonitorException("Reflection error invoking method", e);
        } catch (IOException e) {
            log.error("IO Error invoking method", e);
            throw new MonitorException("IO Error invoking method", e);
        } catch (AttributeNotFoundException e) {
            log.info("Attribute not found", e);
            throw new MonitorException("Attribute not found", e);
        }
        return running;
    }
}
