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

package org.wso2.carbon.registry.ws.client.test.general;

import junit.framework.TestCase;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.authenticator.proxy.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.core.common.AuthenticationException;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;

import java.io.File;

public class TestSetup extends TestCase {

    public TestSetup(String text) {
        super(text);
    }

    String backendURL = "http://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTP_PORT + "/services/";
    private String cookie = null;
    ConfigurationContext configContext = null;

    private final String CARBON_HOME = FrameworkSettings.CARBON_HOME;
    private String axis2Repo = CARBON_HOME + "/repository/deployment/client";
    private String axis2Conf = CARBON_HOME + "/repository/conf/axis2_client.xml";
    String username = "admin";
    String password = "admin";
    String serverURL = "https://" + FrameworkSettings.HOST_NAME + ":" + FrameworkSettings.HTTPS_PORT + "/services/";
    String policyPath = "META-INF/policy.xml";
    static WSRegistryServiceClient registry = null;

    protected void setUp() throws Exception {
        super.setUp();

        System.setProperty("javax.net.ssl.trustStore", CARBON_HOME + "/resources/security/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        try {
            configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(axis2Repo, axis2Conf);
            authenticate(configContext, serverURL);
            registry = new WSRegistryServiceClient(serverURL, "admin", "admin", backendURL, configContext);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to authenticate the client. Caused by: " + e.getMessage());
        }

    }

    public boolean authenticate(ConfigurationContext ctx, String serverURL) throws AxisFault, AuthenticationException {
        String serviceEPR = serverURL + "AuthenticationAdmin";
        AuthenticationAdminStub stub = new AuthenticationAdminStub(ctx, serviceEPR);
        ServiceClient client = stub._getServiceClient();
        Options options = client.getOptions();
        options.setManageSession(true);
        try {
            boolean result = stub.login(username, password, serviceEPR);
            if (result) {
                cookie = (String) stub._getServiceClient().getServiceContext().
                        getProperty(HTTPConstants.COOKIE_STRING);
            }
            return result;
        } catch (Exception e) {
            String msg = "Error occurred while logging in";
            throw new AuthenticationException(msg, e);
        }
    }
}
    
