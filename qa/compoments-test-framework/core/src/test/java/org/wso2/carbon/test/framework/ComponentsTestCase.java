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
 *
 */
package org.wso2.carbon.test.framework;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.wso2.carbon.authenticator.proxy.AuthenticationAdminStub;
import org.wso2.carbon.utils.NetworkUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 8, 2010
 * Time: 5:20:17 PM
 * <p/>
 * This provides the most common utils for JUnit tests
 * This only supports carbon v3.x.x
 */
public abstract class ComponentsTestCase extends initTestCase {

    private static final Log log = LogFactory.getLog(ComponentsTestCase.class);
    protected final String authenticationServiceURL = "https://localhost:9443/services/AuthenticationAdmin";

    protected static String sessionCookie;
    AuthenticationAdminStub authenticationAdminStub;


    @Before
    public void login() throws Exception {

        String clientTrustStorePath = ESB_HOME + File.separator + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks";
        System.setProperty("javax.net.ssl.trustStore", clientTrustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        authenticationAdminStub = new AuthenticationAdminStub(authenticationServiceURL);

        String username = "admin";
        String password = "admin";
        //boolean loginResponse = false;

        authenticationAdminStub.login(username, password, NetworkUtils.getLocalHostname());

        ServiceContext serviceContext = authenticationAdminStub.
                _getServiceClient().getLastOperationContext().getServiceContext();
        sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);

    }

    @After
    public void logout() throws Exception {

        try {
            authenticationAdminStub.logout();
            // session.removeAttribute(ServerConstants.ADMIN_SERVICE_AUTH_TOKEN);
        } catch (java.lang.Exception e) {
            String msg = "Error occurred while logging out";
            log.error(msg, e);
            throw new AuthenticationException(msg, e);
        }
    }

    protected String getStringResultOfTest(OMElement elem) throws Exception {
        OutputStream os = new ByteArrayOutputStream();
        elem.serialize(os);
        return os.toString();
    }

}
