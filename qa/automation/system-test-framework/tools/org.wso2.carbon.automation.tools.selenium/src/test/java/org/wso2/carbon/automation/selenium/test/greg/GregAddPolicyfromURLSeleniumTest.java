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
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GRegBackEndURLEvaluator;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GregResourceURLUploader;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GregUserLogin;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.SeleniumScreenCapture;


public class GregAddPolicyfromURLSeleniumTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(GregAddPolicyfromURLSeleniumTest.class);
    private static Selenium selenium;
    private static WebDriver driver;
    String username;
    String password;

    @Override
    public void init() {
        testClassName = GregAddPolicyfromURLSeleniumTest.class.getName();
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
        String policyURL = "https://wso2.org/repos/wso2/trunk/commons/qa/qa-artifacts/greg/policies/";
        String policyName = "policy.xml";
        try {
            new GregUserLogin().userLogin(driver, username, password);
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("G-Reg Home Page fail :", selenium.isTextPresent("WSO2 Governance Registry Home"));
            // goto add policy link
            driver.findElement(By.linkText("WS Policy")).click();
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("Add Policy Page Fail :", selenium.isTextPresent("Add Policy"));
            Assert.assertTrue("Add Policy Page Fail :", selenium.isTextPresent("Add New Policy"));
            // Enter policy info
            new GregResourceURLUploader().uploadResource(driver, policyURL, policyName);
            Thread.sleep(6000L);
            Assert.assertTrue("Policy Dash Board fail :", selenium.isTextPresent("Service Policy List"));
            Assert.assertTrue("Uploaded policy does not appear on dashboard :", selenium.isTextPresent("policy.xml"));
            Assert.assertTrue("Policy Dash Board fail :", selenium.isTextPresent("Policy Name"));
            //Delete policy
            driver.findElement(By.linkText("Delete")).click();
            Assert.assertTrue("Policy Delete Dialog Popup does not appear", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Delete Policy popup message failed :", selenium.isTextPresent("exact:Are you sure you want to delete'/_system/governance/trunk/policies/policy.xml' permanently?"));
            selenium.click("css=button[type=\"button\"]");
            selenium.waitForPageToLoad("30000");
        } catch (AssertionFailedError e) {
            log.info("GregAddPolicyfromURLSeleniumTest - Assertion Failure ::" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddPolicyfromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddPolicyfromURLSeleniumTest - Assertion Failure :" + e.getMessage());
        } catch (WebDriverException e) {
            log.info("GregAddPolicyfromURLSeleniumTest - WebDriver Exception :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddPolicyfromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddPolicyfromURLSeleniumTest - WebDriver Exception :" + e.getMessage());
        } catch (Exception e) {
            log.info("GregAddPolicyfromURLSeleniumTest - Fail :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddPolicyfromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddPolicyfromURLSeleniumTest - Exception :" + e.getMessage());
        }

    }

    @Override
    public void cleanup() {
        driver.quit();
        log.info("GregAddPolicyfromURLSeleniumTest - Passed");
    }
}
