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

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import java.io.IOException;

/**
 * Communicates with ServerMonitor JMX MBean interface exposed by the server to be monitored.
 */
public class ServerAdminClient extends JMXClient {
    /**
     * Calls the superclass constructor.
     *
     * @param connectorAddress The URL to connect to the server for monitoring.
     * @param beanName Name of the MBean that will be used for monitoring.
     */
    public ServerAdminClient(String connectorAddress, String beanName) {
        super(connectorAddress, beanName);
    }

    /**
     * Gets the server status as provided by the ServerAdmin bean and maps it to a state known by
     * the monitor.
     * @return A valid 'server state'. 
     */
    public ServerState getServerStatus() {
        String status;
        ServerState state = ServerState.STOPPED;
        try {
            // If a connection has been established, get the status.
            if (isConnected()) {
                log.debug("Getting server status.");
                status = (String) serverConnection.invoke(beanObjectName, "getServerStatus",
                                                          null, null);
                log.debug("Server status:" + status);
                if ("RUNNING".equals(status)) {
                    state = ServerState.RUNNING;
                } else if ("RESTARTING".equals(status) || "IN_MAINTENANCE".equals(status)) {
                    state = ServerState.INTERMEDIATE;
                }
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
        return state;
    }

   /**
     * Gets server data provided by the server monitor.
     *
     * @return Data from the server.
     */
   public String getMessages() {
       String message = null;

       try {
           // If a connection has been established, get messages.
           if (isConnected()) {
               log.debug("Getting messages.");
               message = (String) serverConnection.invoke(beanObjectName, "getServerDataAsString",
                                                          null, null);
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
       return message;
   }
}
