/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.startos.system.test.stratosUtils;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;

import java.io.File;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class ServiceLoginClient {

    private static final Log log = LogFactory.getLog(ServiceLoginClient.class);

    public static String loginChecker(String hostName) {

        System.setProperty("javax.net.ssl.trustStore", FrameworkSettings.getKeyStoreLocation());
        System.setProperty("javax.net.ssl.trustStorePassword", FrameworkSettings.getKeyStorePassword());
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        String authenticationServiceURL = "https://" + hostName + "/services/AuthenticationAdmin";

        AuthenticationAdminStub authenticationAdminStub;
        String sessionCookie = null;

        try {

            authenticationAdminStub = new AuthenticationAdminStub(authenticationServiceURL);
            ServiceClient client = authenticationAdminStub._getServiceClient();
            Options options = client.getOptions();
            options.setManageSession(true);

            assertTrue("Failed to login to : + hostName", authenticationAdminStub.login("admin123@manualQA0001.org", "admin123", hostName));
            log.info("Successfully login to " + hostName + "service");
            ServiceContext serviceContext = authenticationAdminStub.
                    _getServiceClient().getLastOperationContext().getServiceContext();
            sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);

        } catch (AxisFault e) {
            log.error("Fail to login to service:" + hostName + "" + e.getMessage());
            fail("Fail to login to service:" + hostName + "" + e.getMessage());

        } catch (Exception e) {
            log.error("Fail to login to service:" + hostName + "" + e.getMessage());
            fail("Fail to login to service:" + hostName + "" + e.getMessage());
        }
        return sessionCookie;
    }
}
