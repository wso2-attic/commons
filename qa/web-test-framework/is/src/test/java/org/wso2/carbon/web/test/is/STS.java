package org.wso2.carbon.web.test.is;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

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

public class STS extends TestCase {

    Selenium selenium;
    String url = "https://localhost:9445/is/services";
    String epa = "http://localhost:8280/services/echo";


    public STS(Selenium _selenium) {
        selenium = _selenium;
    }


    public void tesSecTokenServiceUI() throws Exception {
        selenium.open(ISCommon.loadProperties().getProperty("context.root")+"/carbon/admin/index.jsp?loginStatus=true");
        selenium.click("link=Security Token Service");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("STS Configuration"));
        assertTrue(selenium.isElementPresent("link=Apply Security Policy"));
        assertTrue(selenium.isElementPresent("link=exact:" + url + "/wso2carbon-sts"));

    }


    public void testAddTrustedService(String epa, String alias) throws Exception {
        selenium.open(ISCommon.loadProperties().getProperty("context.root")+"/carbon/admin/index.jsp?loginStatus=true");
        selenium.click("link=Security Token Service");
        selenium.waitForPageToLoad("30000");

        selenium.type("endpointaddrs", epa);
        selenium.select("alias", "label=" + alias);
        selenium.click("//input[@value='Apply']");
        selenium.waitForPageToLoad("30000");
        //If trusted service is successfully created it should appear in the table.
        assertEquals(epa, selenium.getText("//table[@id='trustedServices']/tbody/tr/td[1]"));
    }

    public void testDeleteTrustedService() throws Exception {
        selenium.open(ISCommon.loadProperties().getProperty("context.root")+"/carbon/admin/index.jsp?loginStatus=true");
        selenium.click("link=Security Token Service");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=Delete");
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
    }

    public void testApplySecPolicy(String userGroups) throws Exception {
        selenium.open(ISCommon.loadProperties().getProperty("context.root")+"/carbon/admin/index.jsp?loginStatus=true");
        selenium.click("link=Security Token Service");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=Apply Security Policy");
        selenium.waitForPageToLoad("30000");
        selenium.select("securityConfigAction", "label=Yes");
        selenium.click("scenarioId");
        selenium.click("//input[@value='Next >']");
        selenium.waitForPageToLoad("30000");
        if (userGroups.equals("ldapuserole"))
            selenium.click("userGroups");
        else
            selenium.click("//input[@name='userGroups' and @value='" + userGroups + "']");

        selenium.click("//input[@value='Finish']");
        selenium.waitForPageToLoad("30000");
        assertEquals("Security applied successfully.", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }

    public void testDeleteSecPolicy() throws Exception {
        selenium.open(ISCommon.loadProperties().getProperty("context.root")+"/carbon/admin/index.jsp?loginStatus=true");
        selenium.click("link=Security Token Service");
        selenium.waitForPageToLoad("30000");

        selenium.click("link=Apply Security Policy");
        selenium.waitForPageToLoad("30000");
        selenium.select("securityConfigAction", "label=No");
        assertEquals("This will disable security from the service. Click OK to confirm", selenium.getText("messagebox-confirm"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        assertEquals("Security disabled successfully.", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }

}