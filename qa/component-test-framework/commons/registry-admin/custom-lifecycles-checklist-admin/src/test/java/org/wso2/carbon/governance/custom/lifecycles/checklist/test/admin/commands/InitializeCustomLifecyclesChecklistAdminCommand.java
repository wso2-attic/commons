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
package org.wso2.carbon.governance.custom.lifecycles.checklist.test.admin.commands;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.governance.custom.lifecycles.checklist.ui.CustomLifecyclesChecklistAdminServiceStub;

/**
 * Custom Life-cycles Checklist Admin Service initialization class: it returns lifeCycleManagementServiceStub
 */
public class InitializeCustomLifecyclesChecklistAdminCommand {

    private static final Log log = LogFactory.getLog(InitializeCustomLifecyclesChecklistAdminCommand.class);

    public CustomLifecyclesChecklistAdminServiceStub executeAdminStub(String sessionCookie) {
        log.debug("sessionCookie:" + sessionCookie);
        String serviceURL = null;
        FrameworkSettings.getProperty();
        serviceURL = FrameworkSettings.SERVICE_URL + "CustomLifecyclesChecklistAdminService";
        CustomLifecyclesChecklistAdminServiceStub customLifecyclesChecklistAdminServiceStub = null;
        try {
            customLifecyclesChecklistAdminServiceStub = new CustomLifecyclesChecklistAdminServiceStub(serviceURL);

            ServiceClient client = customLifecyclesChecklistAdminServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
            customLifecyclesChecklistAdminServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(600000);
        }
        catch (AxisFault axisFault) {
            Assert.fail("Unexpected exception thrown");
            axisFault.printStackTrace();
        }
        log.info("lifecyclesAdminServiceStub created");
        return customLifecyclesChecklistAdminServiceStub;

    }
}
