/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.ws.dataservice.test.util;

import java.io.File;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.SimpleHTTPServer;

public class UtilServer {
    private static int count = 0;
    private static SimpleHTTPServer receiver;
    public static final int TESTING_PORT = 5555;


    public synchronized void start(String repository, String axis2xml) throws Exception {
        if (count == 0) {
            ConfigurationContext er = getNewConfigurationContext(repository, axis2xml);
            receiver = new SimpleHTTPServer(er, TESTING_PORT);
            try {
                receiver.start();
            } catch (Exception e) {
                throw new AxisFault("Error while starting SimpleHTTPServer on port " + TESTING_PORT, e);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                throw new AxisFault("Thread interuptted", e1);
            }
        }
        count++;
    }


    private ConfigurationContext getNewConfigurationContext(
            String repository, String axis2xml) throws Exception {
        File file = new File(repository);
        if (!file.exists()) {
            throw new Exception("repository directory "
                                + file.getAbsolutePath() + " does not exists");
        }
        return ConfigurationContextFactory
                .createConfigurationContextFromFileSystem(file.getAbsolutePath(),
                                                          axis2xml);
    }

    public synchronized void stop() throws AxisFault {
        if (count == 1) {
            receiver.stop();
            while (receiver.isRunning()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    //nothing to do here
                }
            }
            count = 0;
            System.out.print("Server stopped .....");
        } else {
            count--;
        }
        receiver.getConfigurationContext().terminate();
    }
}
