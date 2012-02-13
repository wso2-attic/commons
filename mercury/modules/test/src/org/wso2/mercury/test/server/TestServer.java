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
package org.wso2.mercury.test.server;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.SimpleHTTPServer;
import org.apache.axis2.engine.ListenerManager;

public class TestServer {

    public static final String AXIS2_SERVER_CONFIG_FILE = "conf/axis2-server.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository_server";

    public void startServer() {
        try {
            ConfigurationContext confContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_SERVER_CONFIG_FILE);

//            SimpleHTTPServer simpleHttpServer = new SimpleHTTPServer(confContext, 8080);
//            simpleHttpServer.start();

            ListenerManager listenerManager = new ListenerManager();
            listenerManager.startSystem(confContext);
            ListenerManager.defaultConfigurationContext = confContext;

            System.out.println("Server started on port 8080 ");
            try {
                Thread.sleep(2000000);
            } catch (InterruptedException e) {
            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new TestServer().startServer();
    }
}
