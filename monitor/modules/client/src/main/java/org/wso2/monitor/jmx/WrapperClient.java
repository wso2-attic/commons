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

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

/**
 * Communicatees with JMX MBean interface exposed by the wrapper services to be monitored.
 */
public class WrapperClient extends JMXClient {

    /**
     * Calls the superclass constructor.
     *
     * @param connectorAddress The URL to connect to the server for monitoring.
     * @param beanName Name of the MBean that will be used for monitoring.
     */
    public WrapperClient(String connectorAddress, String beanName) {
        super(connectorAddress, beanName);
    }

    /**
     * Gets the process ID of the running server.
     *
     * @return true if a shutdown hook has been triggered.
     */
    public int getProcessId() {
        Integer pid = 0;

        try {
            // If a connection has been established, get PID.
            if (isConnected()) {
                log.debug("Gets the wrapper's process ID.");
                pid = (Integer) serverConnection.invoke(beanObjectName, "getWrapperPID", null,
                                                        null);
            }
        } catch (InstanceNotFoundException e) {
            log.error("Named bean not found", e);
        } catch (MBeanException e) {
            log.error("MBean error", e);
        } catch (ReflectionException e) {
            log.error("Error invoking method", e);
        } catch (IOException e) {
            log.error("IO Error invoking method", e);
        }
        return pid;
    }

    /**
     * Checks ir a ShutdownHook for the server being monitored has already been triggered.
     *
     * @return true if a shutdown hook has been triggered.
     */
    public boolean isShutdownTriggered() {
        Boolean shutdownTriggered = false;

        try {
            // If a connection has been established, get PID.
            if (isConnected()) {
                log.debug("Checking if shutdown hook has been triggered.");
                shutdownTriggered = (Boolean) serverConnection.invoke(beanObjectName,
                                              "getHasShutdownHookBeenTriggered", null, null);
            }
        } catch (InstanceNotFoundException e) {
            log.error("Named bean not found", e);
        } catch (MBeanException e) {
            log.error("MBean error", e);
        } catch (ReflectionException e) {
            log.error("Error invoking method", e);
        } catch (IOException e) {
            log.error("IO Error invoking method", e);
        }
        return shutdownTriggered;
    }

    /**
     * Invokes the restart of the server being monitored, if connection has been established and
     * a shutdown hook has not been triggered.
     */
    public void restartServer() {
        try {
            // initiate restart.
            if (isConnected() && !isShutdownTriggered()) {
                log.debug("Invoking restart operation.");
                serverConnection.invoke(beanObjectName, "restart", null, null);
                hasConnected = false;
                serverConnection = null;
            } else {
                log.info("Not connected or shutdown hook already triggered.");
            }
        } catch (InstanceNotFoundException e) {
            log.error("Named bean not found", e);
        } catch (MBeanException e) {
            log.error("MBean error", e);
        } catch (ReflectionException e) {
            log.error("Error invoking method", e);
        } catch (IOException e) {
            log.error("IO Error invoking method", e);
        }
    }

    /**
     * Invokes the shutdown of the server being monitored.
     */
    public void stopServer() {
        try {
            // If a shutdown hook has not been triggered, initiate shutdown.
            if (isConnected() && !isShutdownTriggered()) {
                log.debug("Invoking stop operation.");
                Object[] params = { new Integer(0) };
                String[] signatures = { "int" };
                serverConnection.invoke(beanObjectName, "stop", params, signatures);
                hasConnected = false;
                serverConnection = null;
            } else {
                log.info("Shutdown hook already triggered.");
            }
        } catch (InstanceNotFoundException e) {
            log.error("Named bean not found", e);
        } catch (MBeanException e) {
            log.error("MBean error", e);
        } catch (ReflectionException e) {
            log.error("Error invoking method", e);
        } catch (IOException e) {
            log.error("IO Error invoking method", e);
        }
    }
}
