package org.wso2.carbon.web.test.is;

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

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.apache.axiom.om.OMElement;

public class ISFineGrainedAuthTest extends CommonSetup {

    public ISFineGrainedAuthTest(String text) {
        super(text);
    }

    //Sign in
    public void testRun() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin", "admin");
    }

    //Setup IS server
    public void testSetUpISServer() throws Exception{
        ISXACML.addNewPolicy();
    }

    //Setup esb server
    public void testSetUpEsb_Server() throws Exception{
        ISCommon.testCommonLogin("esb","admin","admin");
        ISExternalSettings.proxy_FineGrainedAuth("test","admin","admin");
        ISExternalSettings.sec_FineGrainedAuthProxy("test","scenario1");
        ISExternalSettings.policy_FineGrainedAuthProxy();
    }



    //Entitlement client
    public void testEntitlementClient() throws Exception{

        OMElement result = new ISEntitlementClient().EntitlementClient();
        System.out.println("-------------------------------------------------");
        System.out.println(result);
        System.out.println("--------------------------------------------------");
    }

    //Clear IS server changes

    public void testClearChanges() throws Exception{
        ISXACML.deletePolicy("urn:sample:xacml:2.0:samplepolicy");
    }

     //Sign out from IS server
    public void testLogOut() throws Exception{
        SeleniumTestBase inseleniumTestBase = new SeleniumTestBase(selenium);
        inseleniumTestBase.logOutUI();
    }
}