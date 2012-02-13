/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.eventing.eventsource.test.commands;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.authenticator.proxy.test.utils.FrameworkSettings;
import org.wso2.carbon.eventing.eventsource.ui.EventSourceAdminServiceStub;

public class InitializeEventSourceAdminCommand {
    private static final Log log = LogFactory.getLog(InitializeEventSourceAdminCommand.class);

    public EventSourceAdminServiceStub executeAdminStub(String sessionCookie) {
        if (log.isTraceEnabled()) {
            log.trace("Event Sources AdminService Tests Initiated");
        }
        if (log.isDebugEnabled()) {
            log.debug("sessionCookie:" + sessionCookie);
        }
        FrameworkSettings.getProperty();
        String serviceURL = FrameworkSettings.SERVICE_URL + "EventSourceAdminService";
        EventSourceAdminServiceStub eventSourceAdminServiceStub = null;
        try {
            eventSourceAdminServiceStub = new EventSourceAdminServiceStub(serviceURL);
            ServiceClient client = eventSourceAdminServiceStub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
        }
        catch (AxisFault e) {
            log.error("Error occured: " + e.toString());
            Assert.fail("Error occured: " + e);
        }
        return eventSourceAdminServiceStub;

    }
}
