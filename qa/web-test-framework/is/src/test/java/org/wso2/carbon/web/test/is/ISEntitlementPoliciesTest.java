package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.is.ISXACML;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
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


public class ISEntitlementPoliciesTest extends CommonSetup {
    //selenium selenium;

    public ISEntitlementPoliciesTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    //Policy UI Test
    public void testEntitlementPolicyUI() throws Exception{
         ISXACML.entitlementPolicyUI();
         ISXACML.testCSHelp();
    }

    //Test basic policy tests.
    public void testEntitlementPolicyBasics() throws Exception {
        ISXACML.addNewPolicy();
        ISXACML.deletePolicy("urn:sample:xacml:2.0:samplepolicy");
        ISXACML.cancelAddNewPolicy();
        ISXACML.importPolicy();
        ISXACML.policyEvaluation();
        ISXACML.cancelPolicyImport();
        ISXACML.deletePolicy("urn:sample:xacml:2.0:samplepolicy");
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }

}