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

// AuthenticationAdmin service test case class

package org.wso2.carbon.authenticator.proxy.test;

import junit.framework.TestCase;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.wso2.carbon.authenticator.proxy.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.utils.NetworkUtils;

import java.io.File;


public class LoginTest extends TestCase {


    protected static String sessionCookie;
    AuthenticationAdminStub authenticationAdminStub;

    @Test
    public void testNormal() throws Exception {
        FrameworkSettings.getProperty();
        String authenticationServiceURL = FrameworkSettings.SERVICE_URL + "AuthenticationAdmin";
        String clientTrustStorePath = null;
        if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
            clientTrustStorePath = FrameworkSettings.CARBON_HOME + File.separator + "wso2carbon.jks";
        }
        else {
            clientTrustStorePath = FrameworkSettings.CARBON_HOME + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks";
        }
        System.setProperty("javax.net.ssl.trustStore", clientTrustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        authenticationAdminStub = new AuthenticationAdminStub(authenticationServiceURL);
        String username = FrameworkSettings.USER_NAME;
        String password = FrameworkSettings.PASSWORD;
        boolean loginResponse;

        loginResponse = authenticationAdminStub.login(username, password, NetworkUtils.getLocalHostname());
        ServiceContext serviceContext = authenticationAdminStub.
                _getServiceClient().getLastOperationContext().getServiceContext();
        sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        System.out.println("Logged in: " + loginResponse);

        if (!loginResponse) {
            Assert.fail("Failed to login");
        }
        logout();
    }

    @Test
    public void testUserName() throws Exception {
        FrameworkSettings.getProperty();
        String authenticationServiceURL = FrameworkSettings.SERVICE_URL + "AuthenticationAdmin";
        String clientTrustStorePath = null;
        if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
            clientTrustStorePath = FrameworkSettings.CARBON_HOME + File.separator + "wso2carbon.jks";
        }
        else {
            clientTrustStorePath = FrameworkSettings.CARBON_HOME + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks";
        }
        System.setProperty("javax.net.ssl.trustStore", clientTrustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        authenticationAdminStub = new AuthenticationAdminStub(authenticationServiceURL);

        String username = FrameworkSettings.USER_NAME + "1";
        String password = FrameworkSettings.PASSWORD;
        boolean loginResponse;

        loginResponse = authenticationAdminStub.login(username, password, NetworkUtils.getLocalHostname());
        ServiceContext serviceContext = authenticationAdminStub.
                _getServiceClient().getLastOperationContext().getServiceContext();
        sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        System.out.println("Logged in: " + loginResponse);

        if (loginResponse) {
            Assert.fail("User name does not checking");
        }
        logout();
    }

    @Test
    public void testPassword() throws Exception {
        FrameworkSettings.getProperty();
        String authenticationServiceURL = FrameworkSettings.SERVICE_URL + "AuthenticationAdmin";
        String clientTrustStorePath = null;
        if (FrameworkSettings.STRATOS.equalsIgnoreCase("true")) {
            clientTrustStorePath = FrameworkSettings.CARBON_HOME + File.separator + "wso2carbon.jks";
        }
        else {
            clientTrustStorePath = FrameworkSettings.CARBON_HOME + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks";
        }
        System.setProperty("javax.net.ssl.trustStore", clientTrustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        authenticationAdminStub = new AuthenticationAdminStub(authenticationServiceURL);

        String username = FrameworkSettings.USER_NAME;
        String password = FrameworkSettings.PASSWORD + "123";
        boolean loginResponse;

        loginResponse = authenticationAdminStub.login(username, password, NetworkUtils.getLocalHostname());
        ServiceContext serviceContext = authenticationAdminStub.
                _getServiceClient().getLastOperationContext().getServiceContext();
        sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);
        System.out.println("Logged in: " + loginResponse);

        if (loginResponse) {
            Assert.fail("Password does not checking");
        }
        logout();
    }

    @After
    public void logout() throws Exception {

        try {
            authenticationAdminStub.logout();
            // session.removeAttribute(ServerConstants.ADMIN_SERVICE_AUTH_TOKEN);
        }
        catch (java.lang.Exception e) {
            String msg = "Error occurred while logging out";
            Assert.fail("Error occurred while logging out");
            //log.error(msg, e);
            throw new AuthenticationException(msg, e);
        }
    }
}
