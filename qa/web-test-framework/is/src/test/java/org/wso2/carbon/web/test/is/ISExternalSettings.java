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
import org.wso2.carbon.web.test.common.ServiceManagement;

public class ISExternalSettings extends CommonSetup {

    public ISExternalSettings(String text) {
        super(text);
    }

    //Create proxy service
    public static void proxy_FineGrainedAuth(String serviceName,String remoteServiceUserName,String remoteServicePassword) throws Exception{
        Thread.sleep(3000);
        selenium.click("link=Proxy Service");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Step 1 of 3"));
        selenium.type("psName", serviceName);
		selenium.click("nextBtn");

        assertTrue(selenium.isTextPresent("Step 2 of 3"));
		selenium.click("inSeqOpAnon");
		selenium.click("inAnonAddEdit");
		selenium.waitForPageToLoad("30000");

        Thread.sleep(3000);
		selenium.click("link=Add Child");
		selenium.click("link=Entitlement");
        Thread.sleep(3000);
		selenium.type("remoteServiceUrl", "https://localhost:9443/services/");
		selenium.type("remoteServiceUserName", remoteServiceUserName);
		selenium.type("remoteServicePassword", remoteServicePassword);
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");

        Thread.sleep(3000);
        selenium.click("inAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add Child");
		selenium.click("link=Header");
        Thread.sleep(3000);
        selenium.type("mediator.header.name", "Security");
		selenium.click("mediator.header.name.namespace_button");
        Thread.sleep(3000);
		selenium.type("prefix0", "wsse");
		selenium.type("uri0", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		selenium.click("saveNSButton");
		selenium.click("remove");
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");

        Thread.sleep(3000);
        selenium.click("inAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add Child");
		selenium.click("link=Core");
		selenium.click("link=Send");
        Thread.sleep(3000);
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");

        selenium.click("nextBtn");
        assertTrue(selenium.isTextPresent("Step 3 of 3"));
		selenium.click("outSeqOpAnon");
		selenium.click("outAnonAddEdit");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Add Child");
		selenium.click("link=Core");
		selenium.click("link=Send");
		selenium.click("//input[@value='Update']");
		selenium.click("saveButton");
		selenium.waitForPageToLoad("30000");
		selenium.click("saveBtn");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent(serviceName));
    }

    //Apply Security
    public static void sec_FineGrainedAuthProxy(String serviceName,String scenario) throws Exception{
        ServiceManagement serviceManagement = new ServiceManagement(selenium);
        serviceManagement.enableSecurityScenario(serviceName,scenario);
    }

    //Apply policy
    public static void policy_FineGrainedAuthProxy() throws Exception{
        selenium.click("link=Policies");
		selenium.waitForPageToLoad("30000");
		selenium.click("//table[@id='binding-hierarchy-table']/tbody/tr[1]/td[2]/input");
		selenium.waitForPageToLoad("30000");
        String temp=selenium.getValue("raw-policy");
        selenium.click("link=List");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@value='Edit Policy']");
		selenium.waitForPageToLoad("30000");
		selenium.type("raw-policy", temp);
		selenium.click("save-policy");
		selenium.waitForPageToLoad("30000");
    }


    public static void delete_FineGrainedAuthProxy(String serviceName) throws Exception{
        selenium.click("link=List");
		selenium.waitForPageToLoad("30000");
		selenium.click("//input[@name='serviceGroups' and @value='"+serviceName+"']");
		selenium.click("delete1");
		assertTrue(selenium.isTextPresent("exact:Do you want to delete the selected service groups?"));
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
		selenium.click("//button[@type='button']");
    }

}