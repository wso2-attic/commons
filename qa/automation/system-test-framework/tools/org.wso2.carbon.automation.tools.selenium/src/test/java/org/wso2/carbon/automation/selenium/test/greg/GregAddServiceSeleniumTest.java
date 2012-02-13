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
package org.wso2.carbon.automation.selenium.test.greg;

import com.thoughtworks.selenium.Selenium;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.BrowserManager;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GRegBackEndURLEvaluator;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GregUserLogin;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GregUserLogout;

public class GregAddServiceSeleniumTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(GregAddServiceSeleniumTest.class);
    private static Selenium selenium;
    private static TenantDetails tenantDetails;
    private static WebDriver driver;
    String username;
    String password;

    @Override
    public void init() {
        testClassName = GregAddServiceSeleniumTest.class.getName();
        String userId = new GregUserIDEvaluator().getTenantID();
        String baseUrl = new GRegBackEndURLEvaluator().getBackEndURL();
        log.info("baseURL is " + baseUrl);
        driver = BrowserManager.getWebDriver();
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
        driver.get(baseUrl);
        tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(userId));
        username = tenantDetails.getTenantName();
        password = tenantDetails.getTenantPassword();
    }

    @Override
    public void runSuccessCase() {
        try {
            new GregUserLogin().userLogin(driver, username, password);
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue(selenium.isTextPresent("WSO2 Governance Registry Home"));
            //goto Add Service link
            selenium.click("//li[6]/ul/li[4]/ul/li/a");
            selenium.waitForPageToLoad("30000");
            // verify Add Service page attributes
            Assert.assertTrue(selenium.isTextPresent("Add Service"));
            Assert.assertTrue(selenium.isTextPresent("Overview"));
            Assert.assertTrue(selenium.isTextPresent("Service Lifecycle"));
            Assert.assertTrue(selenium.isTextPresent("Interface"));
            // Enter Service details
            selenium.type("id=id_Overview_Name", "testservice1");
            selenium.type("id=id_Overview_Namespace", "testservice123");
            selenium.select("name=Service_Lifecycle_Lifecycle-Name", "label=ServiceLifeCycle");
            selenium.click("link=Add Contact");
            selenium.select("name=Contacts_Contact1", "label=Business Owner");
            selenium.type("id=id_Contacts_Contact1", "Aaaa");
            selenium.type("id=id_Interface_WSDL-URL", "aaa");
            // click on save
            selenium.click("css=input.button.registryWriteOperation");
            Thread.sleep(5000L);
            // verify service added properly
            Assert.assertEquals("/_system/governance/trunk/services/testservice123/testservice1", selenium.getValue("id=uLocationBar"));
            Assert.assertTrue(selenium.isTextPresent("Metadata"));
            Assert.assertTrue(selenium.isTextPresent("Properties"));
            Assert.assertTrue(selenium.isTextPresent("Content"));
            // click on service dash board
            selenium.click("//div[@id='menu']/ul/li[6]/ul/li[2]/ul/li/a");
            Thread.sleep(2000L);
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue(selenium.isTextPresent("Service List"));
            Assert.assertTrue(selenium.isTextPresent("testservice1"));
            Assert.assertTrue(selenium.isTextPresent("testservice123"));
            // delete service
            selenium.click("//td[4]/a");
            Thread.sleep(2000L);
            Assert.assertTrue(selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue(selenium.isTextPresent("Are you sure you want to delete'/_system/governance/trunk/services/testservice123/testservice1' permanently?"));
            selenium.click("css=button[type=\"button\"]");
            selenium.waitForPageToLoad("30000");
            new GregUserLogout().userLogout(driver);
            log.info("GregAddServiceSeleniumTest - Passed");
        } catch (Exception e) {
            driver.quit();
        }

    }

    @Override
    public void cleanup() {
        driver.quit();
    }
}
