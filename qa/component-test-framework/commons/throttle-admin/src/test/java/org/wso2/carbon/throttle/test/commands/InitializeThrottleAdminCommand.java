/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
 
  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/


package org.wso2.carbon.throttle.test.commands;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.throttle.ui.ThrottleAdminServiceStub;

import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.SERVICE_URL;
import static org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings.CONTEXT_ROOT;

public class InitializeThrottleAdminCommand {

    private static final Log log = LogFactory.getLog(InitializeThrottleAdminCommand.class);

    public ThrottleAdminServiceStub throttleAdminServiceStub (String sessionCookie) {
        String serviceURL;
        log.debug("Initialize throttleAdmin service called with sessionCookie :" + sessionCookie);
        FrameworkSettings.getProperty();
        serviceURL = SERVICE_URL +"ThrottleAdminService";
        ThrottleAdminServiceStub throttleAdminServiceStub = null;
        try {
            throttleAdminServiceStub = new ThrottleAdminServiceStub(serviceURL);

            ServiceClient client = throttleAdminServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
            throttleAdminServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(600000);
        } catch (AxisFault axisFault) {
            Assert.fail("Unexpected exception thrown");
            axisFault.printStackTrace();
        }
        log.info("endpointAdminStub created");
        return throttleAdminServiceStub;

    }
}
