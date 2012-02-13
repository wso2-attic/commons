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

package org.wso2.startos.system.test.stratosUtils.gadgetUtils;

import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.dashboard.mgt.gadgetrepo.stub.GadgetRepoServiceStub;
import org.wso2.carbon.dashboard.stub.DashboardServiceStub;
import org.wso2.carbon.dashboard.stub.DashboardUtilServiceStub;

import java.io.File;

public class GadgetTestUtils {

    private static final Log log = LogFactory.getLog(GadgetTestUtils.class);

    public static String getGadgetResourcePath(String resourcePath) {
        return resourcePath + File.separator + "HelloWorldGadget.xml";
    }

    public static GadgetRepoServiceStub getGadgetRepoServiceStub(String sessionCookie) {
        String serviceURL = null;
        serviceURL = FrameworkSettings.GS_BACKEND_URL + "/GadgetRepoService";
        GadgetRepoServiceStub gadgetRepoServiceStub = null;
        try {
            // String sessionCookie = serverLogin.login("gadget.stratoslive.wso2.com");
            gadgetRepoServiceStub = new GadgetRepoServiceStub("https://gadget.stratoslive.wso2.com/services/GadgetRepoService");
            ServiceClient client = gadgetRepoServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
            gadgetRepoServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(600000);
        } catch (AxisFault axisFault) {
            Assert.fail("Unexpected exception thrown");
            axisFault.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("gadgetRepoServiceStub created");
        return gadgetRepoServiceStub;
    }

    public static DashboardServiceStub getDashboardServiceStub(String sessionCookie) {
        String serviceURL = null;
        serviceURL = "https://gadget.stratoslive.wso2.com/services/" + "DashboardService";
        DashboardServiceStub dashboardServiceStub = null;
        try {
            dashboardServiceStub = new DashboardServiceStub(serviceURL);
            ServiceClient client = dashboardServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
            dashboardServiceStub._getServiceClient().getOptions().setTimeOutInMilliSeconds(600000);
        } catch (AxisFault axisFault) {
            Assert.fail("Unexpected exception thrown in DashboardServiceStub creation");
            axisFault.printStackTrace();
        }
        log.info("DashboardServiceStub created");
        return dashboardServiceStub;
    }

    // This method return stub without setting session Cookie
    public static DashboardUtilServiceStub getDashboardUtilServiceStub() {
        String serviceURL = null;
        serviceURL = "https://gadget.stratoslive.wso2.com/services/" + "DashboardUtilService";
        DashboardUtilServiceStub dashboardUtilServiceStub = null;
        try {
            dashboardUtilServiceStub = new DashboardUtilServiceStub(serviceURL);
            ServiceClient client = dashboardUtilServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
        } catch (AxisFault axisFault) {
            Assert.fail("Unexpected exception thrown in DashboardUtilServiceStub creation");
            axisFault.printStackTrace();
        }
        log.info("DashboardUtilServiceStub created");

        return dashboardUtilServiceStub;
    }
}