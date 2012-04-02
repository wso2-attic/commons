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


public class GregAddWsdlfromURLSeleniumTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(GregAddWsdlfromURLSeleniumTest.class);
    private static Selenium selenium;
    private static WebDriver driver;
    String username;
    String password;

    @Override
    public void init() {
        testClassName = GregAddWsdlfromURLSeleniumTest.class.getName();
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
        String wsdlURL = "http://people.wso2.com/~evanthika/wsdls/echo.wsdl";
//        String wsdlName = "echo.wsdl";
        try {
            new GregUserLogin().userLogin(driver, username, password);
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("G-Reg Home Page fail :", selenium.isTextPresent("WSO2 Governance Registry Home"));
            // Click on add wsdl link
            driver.findElement(By.linkText("WSDL")).click();
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("Add WSDL Dash Board - Add WSDL Text does not appear::", selenium.isTextPresent("Add WSDL"));
            Assert.assertTrue("Add WSDL Dash Board - Add New WSDL Text does not appear::", selenium.isTextPresent("Add New WSDL"));
            // Add WSDL Dashboard
            new GregResourceURLUploader().uploadResource(driver, wsdlURL, null);
            Thread.sleep(15000L);
            // wsdl dash board
            Assert.assertTrue("WSDL Dashboard Does not appear ::", selenium.isTextPresent("WSDL List"));
            Assert.assertTrue("Uploaded WSDL name does appear on WSDL Dashboard ::", selenium.isTextPresent("echo.wsdl"));
            // click on delete wsdl link
            driver.findElement(By.linkText("Delete")).click();
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("Delete Pop-up fail:", selenium.isTextPresent("WSO2 Carbon"));
            selenium.click("css=button[type=\"button\"]");
            selenium.waitForPageToLoad("30000");
            //goto service dashboard
            driver.findElement(By.xpath("//li[6]/ul/li[2]/ul/li/a")).click();
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("Service List DashBoard fail : ", selenium.isTextPresent("Service List"));
            // delete service
            driver.findElement(By.linkText("Delete")).click();
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue("Service Delete Popup fail :", selenium.isTextPresent("WSO2 Carbon"));
            selenium.click("css=button[type=\"button\"]");
            selenium.waitForPageToLoad("30000");
            //logout
            new GregUserLogout().userLogout(driver);
        } catch (AssertionFailedError e) {
            log.info("GregAddWsdlfromURLSeleniumTest - Assertion Failure ::" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddWsdlfromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddWsdlfromURLSeleniumTest - Assertion Failure :" + e.getMessage());
        } catch (WebDriverException e) {
            log.info("GregAddWsdlfromURLSeleniumTest - WebDriver Exception :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddWsdlfromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddWsdlfromURLSeleniumTest - WebDriver Exception :" + e.getMessage());
        } catch (Exception e) {
            log.info("GregAddWsdlfromURLSeleniumTest - Fail :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddWsdlfromURLSeleniumTest");
            driver.quit();
            Assert.fail("GregAddWsdlfromURLSeleniumTest - Exception :" + e.getMessage());
        }
    }

    @Override
    public void cleanup() {
        driver.quit();
        log.info("GregAddWsdlfromURLSeleniumTest - Passed");
    }
}
