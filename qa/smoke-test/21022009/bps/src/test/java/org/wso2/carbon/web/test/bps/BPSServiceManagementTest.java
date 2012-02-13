package org.wso2.carbon.web.test.bps;

import org.wso2.carbon.web.test.common.ServiceManagement;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

public class BPSServiceManagementTest extends CommonSetup {

    public BPSServiceManagementTest(String text) {
        super(text);
    }

    public void testServiceManagement() throws Exception {
        BPSLogin.loginToConsole("admin", "admin");
        ServiceManagement sm = new ServiceManagement(browser);
        sm.accessServiceDashboard("HelloWorldNew");
        sm.checkThrottling("http://10.100.1.60:9763/services/HelloWorldNew/", "HelloWorldRequest", "http://helloWorld/process", "http://helloWorld", "input");
        sm.enableRM("LoanService");
        //sm.rmInvocations("LoanService", "urn:echo", "");
        sm.disableRM("LoanService");
        sm.openServiceDashboard("LoanService", "bpel");
        sm.openOperationDashboard("LoanService", "request");
        sm.accessServiceDashboard("LoanService");
        sm.checkTransport("LoanService");
        sm.engageServiceModule("LoanService", "wso2mex-1.00");
        sm.disengageServiceModule("LoanService", "wso2mex-1.00");
        //Assigning security scenarios
        sm.assignConfidentialitySecurityScenario("LoanService");
        sm.assignUTSecurityScenario("LoanService");
        sm.assignIntegritySecurityScenario("LoanService");
        sm.assignNonRepudiationSecurityScenario("LoanService");
        sm.assignSecConEncrOnlyAnonymousSecurityScenario("LoanService");
        sm.assignSecConSignOnlySecurityScenario("LoanService");
        sm.assignSecConEncrOnlySecurityScenario("LoanService");
        sm.assignSecConSignOnlyAnonymousSecurityScenario("LoanService");
        sm.assignSecConSgnEncrSecurityScenario("LoanService");
        sm.assignSecConSgnEncrUsernameSecurityScenario("LoanService");
        sm.assignEncrOnlyUsernameSecurityScenario("LoanService");
        sm.assignSigEncrSecurityScenario("LoanService");
        sm.assignSgnEncrUsernameSecurityScenario("LoanService");
        sm.assignSgnEncrAnonymousScenario("LoanService");
        sm.assignSecConEncrUsernameSecurityScenario("LoanService");
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");

    }


}
