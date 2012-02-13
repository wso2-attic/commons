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

package org.wso2.carbon.app.test.registry;

import junit.framework.TestCase;
import org.wso2.carbon.registry.app.RemoteRegistry;

import java.net.URL;
import java.util.Properties;

public class TestSetup extends TestCase {
    static RemoteRegistry registry = null;
    static String REMOTE_REGISTRY_URL;
    static int iterationsNumber;
    static int concurrentUsers;
    static int workerClass;

    public TestSetup(String text) {
        super(text);
    }

    public void setUp() throws Exception {

        try {
            REMOTE_REGISTRY_URL = System.getProperty("url.property");
            //REMOTE_REGISTRY_URL = "https://localhost:9443/registry/";
            iterationsNumber = Integer.parseInt(PropertyReader.loadRegistryProperties().getProperty("iterations"));
            concurrentUsers = Integer.parseInt(PropertyReader.loadRegistryProperties().getProperty("concurrent-users"));
            workerClass = Integer.parseInt(PropertyReader.loadRegistryProperties().getProperty("wokerClass"));

            System.out.println(REMOTE_REGISTRY_URL);

            System.out.println(iterationsNumber);
            System.out.println(concurrentUsers);
            System.out.println(workerClass);

            System.setProperty("javax.net.ssl.trustStore", "../resources/security/wso2carbon.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            registry = new RemoteRegistry(new URL(REMOTE_REGISTRY_URL), "admin", "admin");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    
