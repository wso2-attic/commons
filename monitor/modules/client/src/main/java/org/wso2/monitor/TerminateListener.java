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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starts a listener which accepts a termination string from the monitor.
 */
public class TerminateListener extends MonitorBase implements Runnable {

    /**
     * Implementation of the runnable interface method, instantiates a server socket and listens for
     * a termination string.
     *
     * Currently no security check is performed and the termination request  is always assumed to be
     * authentic.
     *
     * Failure to start the listener is assumed to be due to an attempt to start a duplicate
     * instance of the monitor. 
     */
    public void run() {
        ServerSocket listenerSocket = null;
        Socket terminateListener;
        String terminateInput;

        // Try to start server socket. Failure to do so implies a duplicate instance of the monitor.
        int monitorPort = Integer.parseInt(monitorProperties.getProperty("monitor.listenport"));
        try {
            listenerSocket = new ServerSocket(monitorPort);
            log.debug("Started termination listener on port " + monitorPort);
        } catch (IOException e) {
            log.error("Unable to listen on port " + monitorPort + ", shutting down duplicate" +
                    " monitor instance. Please change port if in use by another appliation!");
            System.exit(-1);
        }

        // Accept a connection and if terminate is signaled, comply.
        try {
            terminateListener = listenerSocket.accept();
            log.debug("Accepted termination listener connection");
            BufferedReader listenerStream = new BufferedReader(new InputStreamReader(
                    terminateListener.getInputStream()));
            terminateInput = listenerStream.readLine();
            if (terminateInput != null &&
                    terminateInput.equals(MonitorConstants.TRANSMIT_TERMINATE)) {
                log.info("Terminate signal received. Exiting!");
                System.exit(0);
            }
        } catch (IOException e) {
            log.error("Unable to accept connection on port " + monitorPort + ". Only menu based " +
                    "monitor shutdown is possible.");
        }
    }
}
