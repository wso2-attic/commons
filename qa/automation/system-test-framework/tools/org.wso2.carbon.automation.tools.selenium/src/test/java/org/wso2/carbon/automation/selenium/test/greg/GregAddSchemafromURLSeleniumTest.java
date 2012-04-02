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
import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebDriverException;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.BrowserManager;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.gregUtils.GregUserIDEvaluator;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.*;


public class GregAddSchemafromURLSeleniumTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(GregAddSchemafromURLSeleniumTest.class);
    private static Selenium selenium;
    private static WebDriver driver;
    String username;
    String password;

    @Override
    public void init() {
        testClassName = GregAddSchemafromURLSeleniumTest.class.getName();
        String userId = new GregUserIDEvaluator().getTenantID();
        String baseUrl = new GRegBackEndURLEvaluator().getBackEndURL();
        log.info("baseURL is " + baseUrl);
        driver = BrowserManager.getWebDriver();
        selenium = new WebDriverBackedSelenium(driver, baseUrl);
        driver.get(baseUrl);
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(userId));
        username = tenantDetails.getTenantName();
        password = tenantDetails.getTenantPassword();
    }

    @Override
    public void runSuccessCase() {
        String schemaURL = "http://people.wso2.com/~evanthika/schemas/org/company/www/person/Person.xsd";
        String schemaName = "Person.xsd";
        try {
            new GregUserLogin().userLogin(driver, username, password);
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("G-Reg Home Page fail :", selenium.isTextPresent("WSO2 Governance Registry Home"));
            //Click on  Add Schema link
            driver.findElement(By.linkText("Schema")).click();
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("Add Schema page Fail :", selenium.isTextPresent("Add Schema"));
            Assert.assertTrue("Add Schema page Fail :", selenium.isTextPresent("Add New Schema"));
            // enter Schema info
            new GregResourceURLUploader().uploadResource(driver, schemaURL, null);
            Thread.sleep(10000L);
            selenium.waitForPageToLoad("30000");
            //  Schema added successfully
            Assert.assertTrue("Schema Dash board Fail :", selenium.isTextPresent("Schema List"));
            Assert.assertTrue("Uploaded Schema name does not display on Shema Dash board :", selenium.isTextPresent("Person.xsd"));
            Assert.assertTrue("Uploaded Schema nameSpace does not display on Shema Dash board  :", selenium.isTextPresent("exact:http://www.company.org"));
            // click on Delete link
            driver.findElement(By.linkText("Delete")).click();
//            selenium.click("link=Delete");
            Thread.sleep(2000L);
            Assert.assertTrue("Schema Delete pop-up fail :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Schema Delete pop-up fail :", selenium.isTextPresent("exact:Are you sure you want to delete'/_system/governance/trunk/schemas/org/company/www/Person.xsd' permanently?"));
            selenium.click("css=button[type=\"button\"]");
            selenium.waitForPageToLoad("30000");
            new GregUserLogout().userLogout(driver);
        } catch (AssertionFailedError e) {
            log.info("GregAddSchemafromURLSeleniumTest - Assertion Failure ::" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddSchemafromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddSchemafromURLSeleniumTest - Assertion Failure :" + e.getMessage());
        } catch (WebDriverException e) {
            log.info("GregAddSchemafromURLSeleniumTest - WebDriver Exception :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddSchemafromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddSchemafromURLSeleniumTest - WebDriver Exception :" + e.getMessage());
        } catch (Exception e) {
            log.info("GregAddSchemafromURLSeleniumTest - Fail :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddSchemafromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddSchemafromURLSeleniumTest - Exception :" + e.getMessage());
        }

    }

    @Override
    public void cleanup() {
        driver.quit();
        log.info("GregAddSchemafromURLSeleniumTest - Passed");
    }
}
