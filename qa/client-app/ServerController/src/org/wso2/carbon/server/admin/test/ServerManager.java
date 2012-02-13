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
package org.wso2.carbon.server.admin.test;

import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.server.admin.ui.ServerAdminStub;
import org.wso2.carbon.authenticator.proxy.AuthenticationAdminStub;
import org.wso2.carbon.utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ServerManager {


    public static String SERVER_IP;
    public static String HTTPS_PORT;
    public static String CARBON_HOME;
    public static String USER_NAME;
    public static String PASSWORD;
    public static String sessionCookie;


    public static void main(String[] args) throws Exception {
        try {
            ServerManager serverManager = new ServerManager();
            serverManager.getProperty();
            String sessionCookie = serverManager.getSession();
            ServerAdminStub serverAdminStub = serverManager.InitializeServiceAdmin(sessionCookie);

            System.out.println("\n");
            System.out.println("###################################################################\n");
            System.out.println("                  WSO2 Server Management Client\n");
            System.out.println("###################################################################\n");
            System.out.println("1. Forceful Restart\n");
            System.out.println("2. Graceful Restart\n");
            System.out.println("3. Forceful Shutdown\n");
            System.out.println("4. Graceful Shutdown\n");
            System.out.println("5. Start Maintenance Mode\n");
            System.out.println("6. Stop Maintenance Mode\n");
            System.out.println("7. Exit\n");
            System.out.println("###################################################################\n");
            System.out.println("Please select option : ");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int isSelect = Integer.parseInt(br.readLine());


            switch (isSelect) {
                case 1:
                    System.out.println("Server forcefully restarting...\n");
                    serverManager.restartServer(serverAdminStub);
                    break;
                case 2:
                    System.out.println("Server gracefully restarting...\n");
                    serverManager.gracefulRestartServer(serverAdminStub);
                    break;
                case 3:
                    System.out.println("Server forcefully shutting down...\n");
                    serverManager.shutdownServer(serverAdminStub);
                    break;
                case 4:
                    System.out.println("Server gracefully shutting down...\n");
                    serverManager.gracefulShutdownServer(serverAdminStub);
                    break;
                case 5:
                    System.out.println("Starting maintenance mode...\n");
                    serverManager.startMaintenanceMode(serverAdminStub);
                    break;
                case 6:
                    System.out.println("Stopping maintenance mode...\n");
                    serverManager.endMaintenanceMode(serverAdminStub);
                    break;
                case 7:
                    System.out.println("Exit..\n");
                    Thread.currentThread().stop();
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
        catch (Exception e) {
            System.out.println("Invalid option");
        }
    }

    public void getProperty() throws Exception {
        try {
            File filePath = new File("./");
            String relativePath = filePath.getCanonicalPath();
            File findFile = new File(relativePath + File.separator + "conf" + File.separator + "client.properties");
            if (!findFile.isFile()) {
                filePath = new File("./../");
                relativePath = filePath.getCanonicalPath();
            }
            Properties prop = new Properties();
            FileInputStream fReader = new FileInputStream(relativePath + File.separator + "conf" + File.separator + "client.properties");
            prop.load(fReader);
            fReader.close();
            SERVER_IP = (prop.getProperty("server_ip", "localhost"));
            HTTPS_PORT = (prop.getProperty("https_port", "9443"));
            CARBON_HOME = (prop.getProperty("carbon_home"));
            USER_NAME = (prop.getProperty("admin_username", "admin"));
            PASSWORD = (prop.getProperty("admin_password", "admin"));
        }
        catch (Exception e) {
            System.out.println("please check client.properties file");
            e.printStackTrace();
            Thread.currentThread().stop();
        }
    }

    public String getSession() throws Exception {

        try {
            System.setProperty("javax.net.ssl.trustStore", CARBON_HOME + File.separator + "resources" +
                                                           File.separator + "security" + File.separator + "wso2carbon.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");

            AuthenticationAdminStub authenticationAdminStub = new AuthenticationAdminStub("https://" + SERVER_IP + ":"
                                                                                          + HTTPS_PORT + "/services/AuthenticationAdmin");
            String username = USER_NAME;
            String password = PASSWORD;


            authenticationAdminStub.login(username, password, NetworkUtils.getLocalHostname());
            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        }
        catch (Exception e) {
            System.out.println("Unable to connect server : " + e.getMessage());
            e.printStackTrace();
            Thread.currentThread().stop();
        }
        return sessionCookie;

    }

    public ServerAdminStub InitializeServiceAdmin(String sessionCookie) throws Exception {
        String serviceURL = "https://" + SERVER_IP + ":" + HTTPS_PORT + "/services/ServerAdmin";
        ServerAdminStub serverAdminStub = null;
        try {
            serverAdminStub = new ServerAdminStub(serviceURL);
            ServiceClient client = serverAdminStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
        }
        catch (Exception e) {
            System.out.println("Failed to initialize ServiceAdmin : " + e.getMessage());
            e.printStackTrace();
            Thread.currentThread().stop();
        }
        return serverAdminStub;
    }

    public void restartServer(ServerAdminStub serverAdminStub) {
        try {
            serverAdminStub.restart();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gracefulRestartServer(ServerAdminStub serverAdminStub) {
        try {
            serverAdminStub.restartGracefully();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdownServer(ServerAdminStub serverAdminStub) {
        try {
            serverAdminStub.shutdown();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gracefulShutdownServer(ServerAdminStub serverAdminStub) {
        try {
            serverAdminStub.shutdownGracefully();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMaintenanceMode(ServerAdminStub serverAdminStub) {
        try {
            serverAdminStub.startMaintenance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endMaintenanceMode(ServerAdminStub serverAdminStub) {
        try {
            serverAdminStub.endMaintenance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
