package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.*;

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

public class ESBServiceManagementMainTest  extends CommonSetup{


    public ESBServiceManagementMainTest(String text) {
        super(text);
    }


    public void testServiceManagement() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
                
        seleniumTestBase.loginToUI("admin","admin");
        
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
//        serviceManagement.serviceActivation("proxy1","Deactivate");
//        serviceManagement.serviceActivation("proxy1","Activate");
        serviceManagement.accessServiceDashboard("newproxy");
//        serviceManagement.openServiceDashboard("proxy1","proxy");
//        serviceManagement.openOperationDashboard("proxy1","echoString");
//        serviceManagement.checkThrottling("http://localhost:8280/services/proxy1","proxy1","echoString","http://echo.services.core.carbon.wso2.org","echoString");
//        serviceManagement.checkCaching("http://localhost:8280/services/proxy1","echoString","echoString","http://echo.services.core.carbon.wso2.org");

        serviceManagement.assignConfidentialitySecurityScenario("newproxy", "http://echo.services.core.carbon.wso2.org", "urn:echoString", "echoString", "in");
//        serviceManagement.assignUTSecurityScenario
        serviceManagement.assignIntegritySecurityScenario("newproxy", "http://echo.services.core.carbon.wso2.org", "urn:echoString", "echoString", "in");
        serviceManagement.assignNonRepudiationSecurityScenario("newproxy", "http://echo.services.core.carbon.wso2.org", "urn:echoString", "echoString", "in");
//        serviceManagement.assignSecConEncrOnlyAnonymousSecurityScenario
//        serviceManagement.assignSecConSignOnlySecurityScenario
//        serviceManagement.assignSecConEncrOnlySecurityScenario
//        serviceManagement.assignSecConSignOnlyAnonymousSecurityScenario
//        serviceManagement.assignSecConSgnEncrSecurityScenario
//        serviceManagement.assignSecConSgnEncrUsernameSecurityScenario
//        serviceManagement.assignEncrOnlyUsernameSecurityScenario
//        serviceManagement.assignSigEncrSecurityScenario
//        serviceManagement.assignSgnEncrUsernameSecurityScenario
//        serviceManagement.assignSgnEncrAnonymousScenario
//        serviceManagement.assignSecConEncrUsernameSecurityScenario
//        serviceManagement.accessSecurityScenarioPage
//        serviceManagement.disableSecurity
//        serviceManagement.addServiceGroupParameter
//        serviceManagement.deleteServiceGroupParameter
//        serviceManagement.engageServiceModule
//        serviceManagement.engageServiceGroupModule
//        serviceManagement.disengageServiceModule
//        serviceManagement.disengageServiceGroupModule
//        serviceManagement.checkTransport
//        serviceManagement.engageOperationModule
//        serviceManagement.disengageOperationModule
//        serviceManagement.checkParamFlows
//        serviceManagement.enableRM
//        serviceManagement.disableRM
//        serviceManagement.rmInvocations
//        serviceManagement.accessServiceGroupPage
//        serviceManagement.changeServiceGroupMTOM
//        serviceManagement.changeServiceMTOM
//        serviceManagement.changeOperationalMTOM
    }
}
