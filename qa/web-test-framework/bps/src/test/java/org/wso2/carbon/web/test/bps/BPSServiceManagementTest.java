package org.wso2.carbon.web.test.bps;

import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.SecurityClient;

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
//<<<<<<< .mine
        sm.accessServiceDashboard("HelloWorldNew");
        sm.checkThrottling("http://localhost:9763/services/HelloWorldNew/", "HelloWorldRequest", "http://helloWorld/process", "http://helloWorld", "input");
        sm.enableRM("LoanService");
        //sm.rmInvocations("LoanService", "urn:echo", "");
        sm.disableRM("LoanService");
        sm.openServiceDashboard("LoanService", "bpel");
        sm.openOperationDashboard("LoanService", "request");
        sm.accessServiceDashboard("LoanService");
        sm.checkTransport("LoanService");
       // sm.engageServiceModule("LoanService", "wso2mex-1.00");
       // sm.disengageServiceModule("LoanService", "wso2mex-1.00");
//=======
        //sm.accessServiceDashboard("Axis2Service");
//        sm.checkThrottling("http://10.100.1.60:9763/services/HelloWorldNew/", "HelloWorldRequest", "http://helloWorld/process", "http://helloWorld", "input");
        /* sm.enableRM("Axis2Service");
         sm.rmInvocations("Axis2Service", "urn:echoString","http://service.carbon.wso2.org", "echoString", "s");
        //sm.rmInvocations("Axis1Service", "http://ws.apache.org/axis/Axis1Service/echoStringRequest","http://service.carbon.wso2.org", "echoString", "s");
        //sm.rmInvocations("JaxWSTestService.JaxWSServicePort", "","http://service.carbon.wso2.org", "echoString", "s");
         sm.disableRM("Axis2Service");
     // sm.openServiceDashboard("Axis2Service", "axis2");
     //   sm.openOperationDashboard("Axis2Service", "echoString");
    //    sm.accessServiceDashboard("Axis2Service");
    //    sm.checkTransport("Axis2Service");
//       sm.engageServiceModule("LoanService", "wso2mex-1.00");
//        sm.disengageServiceModule("LoanService", "wso2mex-1.00");
//>>>>>>> .r42235
        //Assigning security scenarios
//<<<<<<< .mine
//        sm.assignConfidentialitySecurityScenario("LoanService");
//        sm.assignUTSecurityScenario("LoanService");
//        sm.assignIntegritySecurityScenario("LoanService");
//        sm.assignNonRepudiationSecurityScenario("LoanService");
//        sm.assignSecConEncrOnlyAnonymousSecurityScenario("LoanService");
//        sm.assignSecConSignOnlySecurityScenario("LoanService");
//        sm.assignSecConEncrOnlySecurityScenario("LoanService");
//        sm.assignSecConSignOnlyAnonymousSecurityScenario("LoanService");
//        sm.assignSecConSgnEncrSecurityScenario("LoanService");
//        sm.assignSecConSgnEncrUsernameSecurityScenario("LoanService");
//        sm.assignEncrOnlyUsernameSecurityScenario("LoanService");
//        sm.assignSigEncrSecurityScenario("LoanService");
//        sm.assignSgnEncrUsernameSecurityScenario("LoanService");
//        sm.assignSgnEncrAnonymousScenario("LoanService");
//        sm.assignSecConEncrUsernameSecurityScenario("LoanService");
//=======

        //If you get CommandExecutionFailures, run security sceanrios one by one

        sm.assignConfidentialitySecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
       //sm.assignConfidentialitySecurityScenario("Axis1Service", "http://service.carbon.wso2.org", "http://ws.apache.org/axis/Axis1Service/echoStringRequest", "echoString", "s");
        //  sm.verifyBindingPolicy("Axis2Service", "EncrOnlyAnonymous", "scenario5");

       sm.assignUTSecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
       // sm.assignUTSecurityScenario("Axis1Service", "http://service.carbon.wso2.org", "http://ws.apache.org/axis/Axis1Service/echoStringRequest", "echoString", "s");

        sm.assignIntegritySecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
       sm.assignNonRepudiationSecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
       //sm.assignNonRepudiationSecurityScenario("JaxWSTestService.JaxWSServicePort", "http://service.carbon.wso2.org", "", "echoString");

        sm.assignSecConEncrOnlyAnonymousSecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");*/
        //sm.assignSecConSignOnlySecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
        //sm.assignSecConEncrOnlySecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
        //sm.assignSecConSignOnlyAnonymousSecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
       // sm.assignSecConSgnEncrSecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
        //sm.assignSecConSgnEncrUsernameSecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
        //sm.assignEncrOnlyUsernameSecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
        //sm.assignSigEncrSecurityScenario("Axis2Service", "http://service.carbon.wso2.org", "urn:echoString", "echoString", "s");
//        sm.assignSgnEncrUsernameSecurityScenario("LoanService");
        //sm.assignSgnEncrAnonymousScenario("Axis2Service", "http://service.carbon.wso2.org");    
//        sm.assignSecConEncrUsernameSecurityScenario("LoanService");
        Thread.sleep(1000);
//>>>>>>> .r42235
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");

    }


}
