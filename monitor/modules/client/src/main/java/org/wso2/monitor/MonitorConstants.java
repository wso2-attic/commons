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
package org.wso2.monitor;

/**
 * Contains constants used by the monitoring application.
 */
public class MonitorConstants {
    // Bean name running on WSO2 monitored servers.
    public static final String SERVER_MBEAN_NAME = "org.wso2.monitor.mbeans:type=ServerMonitor";

    // Status image file names.
    public static final String SERVER_UP_IMAGE = "icons/serverup.GIF";
    public static final String SERVER_ERROR_IMAGE = "icons/error.GIF";
    public static final String SERVER_INTERMEDIATE_IMAGE = "icons/intermediate.GIF";
    public static final String SERVER_UNCONNECTED_IMAGE = "icons/default.GIF";
    public static final String SERVER_DOWN_IMAGE = "icons/serverdown.GIF";

    // Menu icon file names.
    public static final String CONNECT_IMAGE = "icons/connect.GIF";
    public static final String START_IMAGE = "icons/start.GIF";
    public static final String RESTART_IMAGE = "icons/restart.GIF";
    public static final String STOP_IMAGE = "icons/stop.GIF";

    // Monitor control constants.
    public static final String COMMAND_START = "-start";
    public static final String COMMAND_STOP = "-stop";
    public static final String TRANSMIT_TERMINATE = "monitor_terminate";
    public static final String LOCALHOST = "127.0.0.1";
}
