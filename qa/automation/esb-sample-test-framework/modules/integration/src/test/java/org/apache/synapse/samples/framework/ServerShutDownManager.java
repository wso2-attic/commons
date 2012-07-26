/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.apache.synapse.samples.framework;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.server.admin.stub.ServerAdminStub;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 *
 */
public class ServerShutDownManager {
    private static final Log log = LogFactory.getLog(ServerShutDownManager.class);

    private final String serviceName = "AuthenticationAdmin";
    private AuthenticationAdminStub authenticationAdminStub;
    private String endPoint;
    private static final long TIMEOUT = 60 * 1000;
    private String carbonHome;
    private String hostName;
    private String userName;
    private String password;
    private String contextRoot;


    public ServerShutDownManager() throws IOException {
        PropertyLoader.getProperties();
        carbonHome = PropertyLoader.CARBON_HOME;
        hostName = PropertyLoader.HOST_NAME;
        userName = PropertyLoader.USER_NAME;
        password = PropertyLoader.PASSWORD;
        contextRoot = PropertyLoader.CONTEXT_ROOT;
    }

    public String login() {
        String authenticationServiceURL;
        log.debug("Server login class login method called");
        System.setProperty("javax.net.ssl.trustStore", carbonHome + File.separator + "repository" +
                File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        if (contextRoot != null) {
            authenticationServiceURL = getBackEndUrl() + "/" + contextRoot + "/" + serviceName;
        } else {
            authenticationServiceURL = getBackEndUrl() + serviceName;
        }
        log.info("AuthenticationAdminService URL = " + authenticationServiceURL);
        try {
            authenticationAdminStub = new AuthenticationAdminStub(authenticationServiceURL);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
        ServiceClient client = authenticationAdminStub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);

        log.debug("UserName : " + userName + " Password : " + password + " HostName : " + hostName);
        try {
            authenticationAdminStub.login(userName, password, hostName);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
        }
        log.debug("getting sessionCookie");
        ServiceContext serviceContext = authenticationAdminStub.
                _getServiceClient().getLastOperationContext().getServiceContext();
        String sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        log.info("sessionCookie : " + sessionCookie);
        log.info("Successfully logged : " + sessionCookie);
        log.debug("exit from login method");
        return sessionCookie;
    }

    private String getBackEndUrl() {
        String backendUrl;
        if (PropertyLoader.CONTEXT_ROOT != null && PropertyLoader.HTTPS_PORT != null) {
            backendUrl = "https://" + PropertyLoader.HOST_NAME + ":" + PropertyLoader.HTTPS_PORT + "/" + PropertyLoader.CONTEXT_ROOT + "/" + "services/";
        } else if (PropertyLoader.CONTEXT_ROOT == null && PropertyLoader.HTTPS_PORT != null) {
            backendUrl = "https://" + PropertyLoader.HOST_NAME + ":" + PropertyLoader.HTTPS_PORT + "/" + "services/";
        } else if (PropertyLoader.CONTEXT_ROOT == null) {
            backendUrl = "https://" + PropertyLoader.HOST_NAME + "/" + "services/";
        } else {
            backendUrl = "https://" + PropertyLoader.HOST_NAME + "/" + PropertyLoader.CONTEXT_ROOT + "/" + "services/";
        }
        return backendUrl;
    }


    public void shutdownServer() throws RemoteException {
        String serviceURL = getBackEndUrl() + "ServerAdmin";
        ServerAdminStub serverAdminStub = null;
        try {
            serverAdminStub = new ServerAdminStub(serviceURL);

            ServiceClient client = serverAdminStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(HTTPConstants.COOKIE_STRING, login());
            serverAdminStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(600000);
        } catch (AxisFault axisFault) {
            log.error(axisFault.toString());
            Assert.fail("Unexpected exception thrown");
        }
        serverAdminStub.shutdownGracefully();
        waitForServerShutDown(Integer.parseInt(PropertyLoader.HTTPS_PORT), PropertyLoader.HOST_NAME);
    }

    private void waitForServerShutDown(int port, String hostName) {
        long startTime = System.currentTimeMillis();
        boolean isPortOpen = true;
        while ((System.currentTimeMillis() - startTime) < TIMEOUT && isPortOpen) {
            Socket socket = null;
            try {
                InetAddress address = InetAddress.getByName(hostName);
                socket = new Socket(address, port);
                isPortOpen = socket.isConnected();
                if (isPortOpen) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                } else {
                    return;
                }
            } catch (IOException e) {
                log.info("Cannot create the socket");
                return;
            } finally {
                try {
                    if ((socket != null) && (!socket.isConnected())) {
                        socket.close();
                    }
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
        throw new RuntimeException("Port " + port + " is still open");
    }


}
