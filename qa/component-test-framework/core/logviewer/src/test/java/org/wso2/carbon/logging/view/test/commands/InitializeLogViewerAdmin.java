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
package org.wso2.carbon.logging.view.test.commands;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.authenticator.proxy.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.proxy.AuthenticationExceptionException;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.logging.view.LogViewerStub;
import org.wso2.carbon.utils.NetworkUtils;

import java.net.SocketException;
import java.rmi.RemoteException;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HOST_NAME;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.HTTPS_PORT;

public class InitializeLogViewerAdmin {
    private static final Log log = LogFactory.getLog(InitializeLogViewerAdmin.class);
    private AuthenticationAdminStub authenticationAdminStub;

    public LogViewerStub executeAdminStub() {
        String superTenantSessionCookie = login();
        if (log.isTraceEnabled()) {
            log.trace("InitializeLogViewerAdmin class called.");
        }
        if (log.isDebugEnabled()) {
            log.debug("sessionCookie:" + superTenantSessionCookie);
        }
        FrameworkSettings.getProperty();
        String serviceURL = "https://" + HOST_NAME + ":" + HTTPS_PORT + "/services/LogViewer";
        LogViewerStub logViewerStub = null;
        try {
            logViewerStub = new LogViewerStub(serviceURL);
            ServiceClient client = logViewerStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, superTenantSessionCookie);
        }
        catch (AxisFault axisFault) {
            log.error("AxisFault in InitializeLogViewerAdmin class : " + axisFault.toString());
            Assert.fail("Unexpected exception thrown");
            axisFault.printStackTrace();
        }
        log.trace("InitializeLogViewerAdmin : InitializeLogViewerAdmin created with session " + superTenantSessionCookie);
        return logViewerStub;

    }

    public String login() {
        String superTenantSessionCookie = null;
        try {
            log.debug("Server login class login method called");
            FrameworkSettings.getProperty();
            String authenticationServiceURL = FrameworkSettings.SERVICE_URL + "AuthenticationAdmin";
            log.debug("AuthenticationAdminService URL = " + authenticationServiceURL);
            authenticationAdminStub = new AuthenticationAdminStub(authenticationServiceURL);
            ServiceClient client = authenticationAdminStub._getServiceClient();
            Options options = client.getOptions();
            options.setManageSession(true);

            String userName = FrameworkSettings.SUPERTENANT_USERNAME;
            String password = FrameworkSettings.SUPERTENANT_PASSWORD;

            String hostName = NetworkUtils.getLocalHostname();
            authenticationAdminStub.login(userName, password, hostName);
            log.debug("getting sessionCookie");
            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            superTenantSessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
            log.debug("sessionCookie : " + superTenantSessionCookie);
            log.info("Successfully logged : " + superTenantSessionCookie);
            log.debug("exit from login method");

        }
        catch (SocketException e) {
            log.error("Error occurred: " + e);

        }
        catch (RemoteException e) {
            log.error("Error occurred: " + e);

        }
        catch (AuthenticationExceptionException e) {
            log.error("Error occurred: " + e);

        }
        return superTenantSessionCookie;
    }
}
