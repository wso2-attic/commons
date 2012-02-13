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

package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.apache.axis2.context.ServiceContext;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;

import java.io.File;
import java.rmi.RemoteException;


public class AdminServiceAuthentication {
    private static final Log log = LogFactory.getLog(AdminServiceAuthentication.class);

    private final String serviceName = "AuthenticationAdmin";
    private AuthenticationAdminStub authenticationAdminStub;
    private String endPoint;

    public AdminServiceAuthentication(String backendUrl) {
        //setting client side keyStores
        System.setProperty("javax.net.ssl.trustStore", FrameworkSettings.getKeyStoreLocation());
        System.setProperty("javax.net.ssl.trustStorePassword", FrameworkSettings.getKeyStorePassword());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        this.endPoint = backendUrl + "/" + serviceName;
        try {
            authenticationAdminStub = new AuthenticationAdminStub(endPoint);
        } catch (AxisFault axisFault) {
            log.error("Initializing authentication stub failed : " + axisFault.getMessage());
            Assert.fail("Initializing authentication stub failed : " + axisFault.getMessage());
        }
    }

    public String login(String userName, String password, String hostName) {

        Boolean loginStatus = null;
        try {
            loginStatus = authenticationAdminStub.login(userName, password, hostName);
        } catch (RemoteException e) {
            log.error("Login failed due to RemoteException" + e.getMessage());
            Assert.fail("Login failed due to RemoteException" + e.getMessage());
        } catch (LoginAuthenticationExceptionException e) {
            log.error("Login failed due to LoginAuthenticationExceptionException" + e.getMessage());
            Assert.fail("Login failed due to LoginAuthenticationExceptionException" + e.getMessage());
        }
        Assert.assertTrue("Login unsuccessful", loginStatus);
        log.info("Login Successful");
        ServiceContext serviceContext = authenticationAdminStub.
                _getServiceClient().getLastOperationContext().getServiceContext();
        String sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        return sessionCookie;
    }

    public void logOut() throws LogoutAuthenticationExceptionException, RemoteException {
        authenticationAdminStub.logout();
        log.info("log out");
    }
}
