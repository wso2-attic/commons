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

/**
 * Management interface for the WSO2 server products.
 */
public interface ServerMonitorMBean {

    /**
     * Gets any queued messages from the server.
     * @return message string.
     */
    public String getMessages();

    /**
     * Send a given message to any registered notification listeners.
     *
     * @param title String containing title of message.
     * @param message String containing message to be notified.
     * @param severity Used to specify message severity.
     */
    public void notifyMonitor(String title, String message, int severity);

    /**
     * Indicates if the monitored server is running.
     * @return true if the server is running.
     */
    public boolean isServerRunning();

    /**
     * Used to set the server status indication.
     * @param serverRunning true to indicate that the server is running,
     */
    public void setServerRunning(boolean serverRunning);
}
