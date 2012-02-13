/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

// famework setting propertiy loader

package org.wso2.carbon.authenticator.proxy.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FrameworkSettings {
    public static String CARBON_HOME;
    public static String HOST_NAME;
    public static String HTTP_PORT;
    public static String HTTPS_PORT;
    public static String DERBY_PORT;
    public static String CONTEXT_ROOT;
    public static String USER_NAME;
    public static String PASSWORD;
    public static String TRUSTSTORE_PATH;
    public static String TRUSTSTORE_PASSWORD;
    public static boolean BACKENDSERVER_RUNNING;
    public static String TEST_FRAMEWORK_HOME;
    public static String NIO_TRANSPORT_HTTPS;
    public static String TENANT_NAME;
    public static String SERVICE_URL;
    public static String STRATOS;
    public static String BACKENDSERVER_HOST_NAME;
    public static String BACKENDSERVER_HTTPS_PORT;
    public static String BACKENDSERVER_HTTP_PORT;
    public static String P2_REPO;
    public static String SUPERTENANT_USERNAME;
    public static String SUPERTENANT_PASSWORD;
    public static String ESB_TEST_SERVER;


    public static void getProperty() {
        try {
            File filePath = new File("./");
            String relativePath = filePath.getCanonicalPath();
            File findFile = new File(relativePath + File.separator + "config" + File.separator + "framework.properties");
            if (!findFile.isFile()) {
                filePath = new File("./../../");
                relativePath = filePath.getCanonicalPath();
               // relativePath = relativePath + File.separator + "config" + File.separator + "framework.properties";
            }
            Properties prop = new Properties();
            FileInputStream fReader = new FileInputStream(relativePath + File.separator + "config" + File.separator + "framework.properties");
            prop.load(fReader);
            fReader.close();
            CARBON_HOME = (prop.getProperty("carbon.home"));
            HOST_NAME = (prop.getProperty("host.name", "localhost"));
            HTTPS_PORT = (prop.getProperty("https.port", "9443"));
            HTTP_PORT = (prop.getProperty("http.port", "8280"));
            ESB_TEST_SERVER = (prop.getProperty("esb.test.server", "9002"));
            NIO_TRANSPORT_HTTPS = (prop.getProperty("nio.transport.port", "8243"));
            DERBY_PORT = prop.getProperty("derby.port", "1527");
            P2_REPO = prop.getProperty("p2.repo", "http://builder.wso2.org/~carbon/releases/carbon/3.0.1/RC3/p2-repo/");

            CONTEXT_ROOT = (prop.getProperty("context.root"));
            STRATOS = (prop.getProperty("stratos", "false"));
            USER_NAME = (prop.getProperty("server.username", "admin"));
            PASSWORD = (prop.getProperty("server.password", "admin"));
            SUPERTENANT_USERNAME = (prop.getProperty("supertenant.username", "admin"));
            SUPERTENANT_PASSWORD = (prop.getProperty("supertenant.password", "admin"));

            BACKENDSERVER_RUNNING = Boolean.parseBoolean(prop.getProperty("backendserver.running", "true"));
            BACKENDSERVER_HTTP_PORT = (prop.getProperty("backendserver_http.port", "9000"));
            BACKENDSERVER_HTTPS_PORT = (prop.getProperty("backendserver_https.port", "9443"));
            BACKENDSERVER_HOST_NAME = (prop.getProperty("backendserver_host.name", "localhost"));

            TRUSTSTORE_PATH = (prop.getProperty("truststore.path", FrameworkSettings.CARBON_HOME + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks"));
            TRUSTSTORE_PASSWORD = (prop.getProperty("truststore.password", "wso2carbon"));
            TEST_FRAMEWORK_HOME = relativePath;
            SERVICE_URL = "https://" + HOST_NAME + ":" + HTTPS_PORT + "/services/";
            if (STRATOS.equalsIgnoreCase("true")) {
                TENANT_NAME = (prop.getProperty("tenant.name"));
                CARBON_HOME = relativePath + File.separator + "lib" + File.separator + "stratos-artifacts";
                SERVICE_URL = "https://" + HOST_NAME + "/services/";
                TRUSTSTORE_PATH = (prop.getProperty("truststore.path", FrameworkSettings.CARBON_HOME + File.separator + "wso2carbon.jks"));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFrameworkPath() throws Exception {
        String relativePath = null;
        try {
            File filePath = new File("./");
            relativePath = filePath.getCanonicalPath();
            File findFile = new File(relativePath + File.separator + "config" + File.separator + "framework.properties");
            if (!findFile.isFile()) {
                filePath = new File("./../");
                relativePath = filePath.getCanonicalPath();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return relativePath;
    }


}
