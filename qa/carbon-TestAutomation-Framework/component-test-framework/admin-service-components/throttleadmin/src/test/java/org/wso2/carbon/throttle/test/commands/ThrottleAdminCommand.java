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

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.wso2.carbon.throttle.ui.ThrottleAdminServiceStub;
import org.wso2.carbon.throttle.ui.types.ThrottlePolicy;


public class ThrottleAdminCommand extends TestCase {

    private static final Log log = LogFactory.getLog(ThrottleAdminCommand.class);
    ThrottleAdminServiceStub throttleAdminServiceStub;

    public ThrottleAdminCommand(ThrottleAdminServiceStub throttleAdminServiceStub) {
        this.throttleAdminServiceStub = throttleAdminServiceStub;
        log.debug("ThrottleAdminStub added");
    }

    public void enableThrottlingSuccessCase(String serviceName, ThrottlePolicy throttlePolicy)
            throws Exception {
        try {
            throttleAdminServiceStub.enableThrottling(serviceName, throttlePolicy);
            log.debug("throttling enabled");
        }
        catch (Exception e) {
            log.error("Add throttle failed : " + e.getMessage());
            Assert.fail("Add throttle failed");
            e.printStackTrace();
        }
    }

    public void enableThrottlingFailureCase(String serviceName, ThrottlePolicy throttlePolicy)
            throws Exception {
        try {
            throttleAdminServiceStub.enableThrottling(serviceName, throttlePolicy);
            log.error("Throttle enabled in failure case");
            Assert.fail("Throttle enabled in failure case");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
    }

    public void disableThrottlingSuccessCase(String serviceName) throws Exception {
        try {
            throttleAdminServiceStub.disableThrottling(serviceName);
            log.debug("throttling disabled");
        }
        catch (Exception e) {
            log.error("Disable throttle failed : " + e.getMessage());
            Assert.fail("Disable throttle failed");
            e.printStackTrace();
        }
    }

    public void disableThrottlingFailureCase(String serviceName) throws Exception {
        try {
            throttleAdminServiceStub.disableThrottling(serviceName);
            log.error("Throttle disabled in failure case");
            Assert.fail("Throttle disabled in failure case");
        }
        catch (Exception e) {
            log.info("Failure case exception : " + e.toString());

        }
    }

    public void engageOperationalThrottlingSuccessCase(ThrottlePolicy throttlePolicy,
                                                       String serviceName, String operationName)
            throws Exception {
        try {
            throttleAdminServiceStub.engageThrottlingForOperation(throttlePolicy, serviceName, operationName);
            log.debug("operational throttling engaged");
        }
        catch (Exception e) {
            log.error("operational throttling engage failed : " + e.getMessage());
            Assert.fail("operational throttling engage failed");
            e.printStackTrace();
        }
    }

    public void engageOperationalThrottlingFailureCase(ThrottlePolicy throttlePolicy,
                                                       String serviceName, String operationName)
            throws Exception {
        try {
            throttleAdminServiceStub.engageThrottlingForOperation(throttlePolicy, serviceName, operationName);
            log.error("operational throttle engaged without session ");
            Assert.fail("operational throttle engage without session ");
        }
        catch (Exception e) {
            log.info("operational throttle failure case exception : " + e.toString());

        }
    }

    public void disengageOperationalThrottlingSuccessCase(String serviceName, String operationName)
            throws Exception {
        try {
            throttleAdminServiceStub.disengageThrottlingForOperation(serviceName, operationName);
            log.debug("operational throttling disengaged");
        }
        catch (Exception e) {
            log.error("disengage operational throttling failed : " + e.getMessage());
            Assert.fail("disengage operational throttling failed");
            e.printStackTrace();
        }
    }

    public void disengageOperationalThrottlingFailureCase(String serviceName, String operationName)
            throws Exception {
        try {
            throttleAdminServiceStub.disengageThrottlingForOperation(serviceName, operationName);
            log.error("disengage operational throttle without session ");
            Assert.fail("disengage operational throttle session ");
        }
        catch (Exception e) {
            log.info("Failure case exception in disengage oerational throttling : " + e.toString());

        }
    }

    public ThrottlePolicy getGlobalPolicyConfigSuccessCase() throws Exception {
        ThrottlePolicy throttlePolicy = new ThrottlePolicy();
        try {
            throttlePolicy = throttleAdminServiceStub.getGlobalPolicyConfigs();
            log.debug("operational throttling disengaged");
        }
        catch (Exception e) {
            log.error("disengage operational throttling failed : " + e.getMessage());
            Assert.fail("disengage operational throttling failed");
            e.printStackTrace();
        }
        return throttlePolicy;
    }

    public ThrottlePolicy getGlobalPolicyConfigFailureCase() throws Exception {
        ThrottlePolicy throttlePolicy = new ThrottlePolicy();
        try {
            throttlePolicy = throttleAdminServiceStub.getGlobalPolicyConfigs();
            log.error("disengage operational throttle without session ");
            Assert.fail("disengage operational throttle session ");
        }
        catch (Exception e) {
            log.info("Failure case exception in disengage operational throttling : " + e.toString());

        }
        return throttlePolicy;
    }
}
