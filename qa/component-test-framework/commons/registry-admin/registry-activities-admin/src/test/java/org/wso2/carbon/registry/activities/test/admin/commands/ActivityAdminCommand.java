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
package org.wso2.carbon.registry.activities.test.admin.commands;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.registry.activities.ui.ActivityAdminServiceStub;
import org.wso2.carbon.registry.activities.ui.beans.xsd.ActivityBean;

/**
 * giving the access to operations in ActivityAdminService
 */
public class ActivityAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(ActivityAdminCommand.class);
    ActivityAdminServiceStub activityAdminServiceStub;

    public ActivityAdminCommand(ActivityAdminServiceStub activityAdminServiceStub) {
        this.activityAdminServiceStub = activityAdminServiceStub;
        log.debug("activityAdminServiceStub added");
    }

    public ActivityBean getActivitiesSuccessCase(String userName, String resourcePath,
                                                 String fromDate, String toDate, String filter,
                                                 String page, String sesssionId) {
        ActivityBean activityBean = new ActivityBean();
        try {
            activityBean = activityAdminServiceStub.getActivities(userName, resourcePath, fromDate, toDate, filter, page, sesssionId);
        }
        catch (Exception e) {
            log.error("Unable to get registry activities : " + e.getMessage());
        }
        return activityBean;
    }

    public void getActivitiesFailureCase(String userName, String resourcePath,
                                         String fromDate, String toDate, String filter,
                                         String page, String sesssionId) {
        try {
            activityAdminServiceStub.getActivities(userName, resourcePath, fromDate, toDate, filter, page, sesssionId);
            log.error("Getting activity without session cookie");
            Assert.fail("Getting activity without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }
}
