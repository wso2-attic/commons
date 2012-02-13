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
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GregUserLogin;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.GregUserLogout;
import org.wso2.carbon.system.test.core.utils.seleniumUtils.SeleniumScreenCapture;


public class GregApplyTagtoRootSeleniumTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(GregApplyTagtoRootSeleniumTest.class);
    private static Selenium selenium;
    private static WebDriver driver;
    String username;
    String password;

    @Override
    public void init() {
        testClassName = GregApplyTagtoRootSeleniumTest.class.getName();
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
        try {
            new GregUserLogin().userLogin(driver, username, password);
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue(selenium.isTextPresent("WSO2 Governance Registry Home"));
            //Click on Browse link
            driver.findElement(By.linkText("Browse")).click();
            Thread.sleep(8000L);
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Browse"));
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("exact:Location:"));
            //click on Apply Tag Frame
            driver.findElement(By.id("tagsIconMinimized")).click();
            Thread.sleep(2000L);
            //click on Add New Tag
            driver.findElement(By.linkText("Add New Tag")).click();
            Thread.sleep(2000L);
            driver.findElement(By.id("tfTag")).sendKeys("apply root tag");
            driver.findElement(By.xpath("//div[2]/input[3]")).click();
            Thread.sleep(6000L);
            selenium.mouseOver("//div[12]/div[3]/a");
            Thread.sleep(2000L);
            //Delete Tag
            driver.findElement(By.xpath("//div[12]/div[3]/a[2]/img")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Delete Tag Pop-up fail :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Delete Tag Pop-up fail :", selenium.isTextPresent("exact:Are you sure you want to delete this tag?"));
            //click on "yes" button
            selenium.click("//button");
            selenium.waitForPageToLoad("30000");
            //Sign out
            new GregUserLogout().userLogout(driver);

        } catch (AssertionFailedError e) {
            log.info("GregApplyTagtoRootSeleniumTest - Assertion Failure ::" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregApplyTagtoRootSeleniumTest");
            driver.quit();
            Assert.fail("GregApplyTagtoRootSeleniumTest - Assertion Failure :" + e.getMessage());
        } catch (WebDriverException e) {
            log.info("GregApplyTagtoRootSeleniumTest - WebDriver Exception :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregApplyTagtoRootSeleniumTest");
            driver.quit();
            Assert.fail("GregApplyTagtoRootSeleniumTest - WebDriver Exception :" + e.getMessage());
        } catch (Exception e) {
            log.info("GregApplyTagtoRootSeleniumTest- Fail :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregApplyTagtoRootSeleniumTest");
            driver.quit();
            Assert.fail("GregApplyTagtoRootSeleniumTest - Exception :" + e.getMessage());
        }

    }

    @Override
    public void cleanup() {
        driver.quit();
        log.info("GregApplyTagtoRootSeleniumTest - Passed");
    }
}
