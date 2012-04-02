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


public class GregAddCommenttoCollectionSeleniumTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(GregAddCommenttoCollectionSeleniumTest.class);
    private static Selenium selenium;
    private static WebDriver driver;
    String username;
    String password;

    @Override
    public void init() {
        testClassName = GregAddCommenttoCollectionSeleniumTest.class.getName();
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
        addCommentToCollection();
        addCommentToResource();
        addCommentToRoot();

    }

    @Override
    public void cleanup() {
        driver.quit();
        log.info("GregAddCommenttoCollectionSeleniumTest - Passed");
    }

    private void addCommentToCollection() {
        String collectionPath = "/selenium_root/comment_test/a1/b1";
        try {
            new GregUserLogin().userLogin(driver, username, password);
            selenium.waitForPageToLoad("30000");
            Assert.assertTrue(selenium.isTextPresent("WSO2 Governance Registry Home"));
            //Click on Browse link
            driver.findElement(By.linkText("Browse")).click();
            Thread.sleep(5000L);
            //Go to Detail view Tab
            driver.findElement(By.id("stdView")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Browse"));
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Metadata"));
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Entries"));
            //Click on Add Collection link
            driver.findElement(By.linkText("Add Collection")).click();
            Thread.sleep(3000L);
            driver.findElement(By.id("collectionName")).sendKeys(collectionPath);
            driver.findElement(By.id("colDesc")).sendKeys("Selenium Test");
            //Click on Add button
            driver.findElement(By.xpath("//div[7]/form/table/tbody/tr[5]/td/input")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Add new Collection pop -up failed :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Add new Collection pop -up failed :", selenium.isTextPresent("Successfully added new collection."));
            //click on OK button
            selenium.click("//button");
            Thread.sleep(2000L);
//            Assert.assertEquals("Collection Creation Fail :", "/selenium_root/a1/b1/c1", selenium.getValue("//input"));
            Assert.assertEquals("Collection Creation Fail :", collectionPath, selenium.getValue("//input"));
            //add comment to Collection
            driver.findElement(By.id("commentsIconMinimized")).click();
            Thread.sleep(2000L);
            driver.findElement(By.linkText("Add Comment")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Add comment window pop -up failed :", selenium.isTextPresent("Add New Comment"));
            Assert.assertEquals("Add comment window  pop -up failed :", "Add", selenium.getValue("//div[12]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input"));
            Assert.assertEquals("Add comment window  pop -up failed :", "Cancel", selenium.getValue("//div[12]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input[2]"));
            driver.findElement(By.id("comment")).sendKeys("Comment1");
            driver.findElement(By.xpath("//div[12]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input")).click();
            Thread.sleep(5000L);
            Assert.assertEquals("Added comment failed :", "Comment1 \n posted on 0m ago by admin", selenium.getText("//div[4]/div/div[2]/table/tbody/tr/td/div"));
//            Assert.assertTrue( selenium.isTextPresent("Comment1 \\n posted on 0m ago by admin"));
            //Delete Comment
            driver.findElement(By.id("closeC0")).click();
            Thread.sleep(5000L);
            Assert.assertTrue("Comment Delete pop-up  failed :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Comment Delete pop-up  failed :", selenium.isTextPresent("exact:Are you sure you want to delete this comment?"));
            selenium.click("//button");
            //Sign out
            new GregUserLogout().userLogout(driver);
        } catch (AssertionFailedError e) {
            log.info("GregAddCommenttoCollectionSeleniumTest - Assertion Failure ::" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - Assertion Failure :" + e.getMessage());
        } catch (WebDriverException e) {
            log.info("GregAddCommenttoCollectionSeleniumTest - WebDriver Exception :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - WebDriver Exception :" + e.getMessage());
        } catch (Exception e) {
            log.info("GregAddCommenttoCollectionSeleniumTest- Fail :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - Exception :" + e.getMessage());
        }
    }

    private void userLogin() {
        new GregUserLogin().userLogin(driver, username, password);
        selenium.waitForPageToLoad("30000");
        Assert.assertTrue(selenium.isTextPresent("WSO2 Governance Registry Home"));
    }

    private void gotoDetailViewTab() {
        try {
            //Click on Browse link
            driver.findElement(By.linkText("Browse")).click();
            Thread.sleep(5000L);
            //Go to Detail view Tab
            driver.findElement(By.id("stdView")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Browse"));
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Metadata"));
            Assert.assertTrue("Browse Detail View Page fail :", selenium.isTextPresent("Entries"));
        } catch (InterruptedException e) {
            log.info("CommentSeleniumTest - gotoDetailViewTab InterruptedException thrown:" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "CommentSeleniumTest - gotoDetailViewTab");
            driver.quit();
            Assert.fail("CommentSeleniumTest - gotoDetailViewTab InterruptedException thrown:" + e.getMessage());
        }

    }

    private void addComment(String comment, String xpathValue) {
        try {
            System.out.println("commend ekata awa........");
            driver.findElement(By.id("commentsIconMinimized")).click();
            Thread.sleep(3000L);
            driver.findElement(By.linkText("Add Comment")).click();
            Thread.sleep(5000L);
            Assert.assertTrue("Add comment window pop -up failed :", selenium.isTextPresent("Add New Comment"));
            Assert.assertEquals("Add comment window  pop -up failed :", "Add", selenium.getValue("//div[" + xpathValue + "]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input"));
            Assert.assertEquals("Add comment window  pop -up failed :", "Cancel", selenium.getValue("//div[" + xpathValue + "]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input[2]"));
            System.out.println("AWa da..............................");
            Thread.sleep(3000L);
            driver.findElement(By.id("comment")).sendKeys(comment);
            driver.findElement(By.xpath("//div[" + xpathValue + "]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input")).click();
            Thread.sleep(5000L);
            Assert.assertEquals("Added comment failed :", "Comment1 \n posted on 0m ago by admin", selenium.getText("//div[4]/div/div[2]/table/tbody/tr/td/div"));

        } catch (InterruptedException e) {
            log.info("CommentSeleniumTest - addComment InterruptedException thrown:" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "CommentSeleniumTest - addComment");
            driver.quit();
            Assert.fail("CommentSeleniumTest - addComment InterruptedException thrown:" + e.getMessage());
        }
    }

    private void addCommentToResource() {
        String resourcePath = "/test";
        try {
            userLogin();
            gotoDetailViewTab();
            //click on Add Resource button
            driver.findElement(By.linkText("Add Resource")).click();
            Thread.sleep(2000L);
            Assert.assertTrue("Add new resource page failed :", selenium.isTextPresent("Add Resource"));
            //select create text content
            selenium.select("id=addMethodSelector", "label=Create Text content");
            selenium.click("css=option[value=\"text\"]");
            Thread.sleep(3000L);
            // Enter name
            driver.findElement(By.id("trFileName")).sendKeys("test");
            driver.findElement(By.id("trMediaType")).sendKeys("txt");
            driver.findElement(By.id("trDescription")).sendKeys("selenium test resource");
            driver.findElement(By.id("trPlainContent")).sendKeys("selenium test123");
            // Click on Add button
            driver.findElement(By.xpath("//tr[4]/td/form/table/tbody/tr[6]/td/input")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Add Resource pop-up fail :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Add Resource pop-up fail :", selenium.isTextPresent("Successfully added Text content."));
            //Click on OK button
            driver.findElement(By.xpath("//button")).click();
            Thread.sleep(2000L);
            driver.findElement(By.id("uLocationBar")).clear();
            driver.findElement(By.id("uLocationBar")).sendKeys(resourcePath);
            driver.findElement(By.xpath("//input[2]")).click();
            Thread.sleep(3000L);
           //Add Comment
            driver.findElement(By.id("commentsIconMinimized")).click();
            Thread.sleep(2000L);
            driver.findElement(By.linkText("Add Comment")).click();
            Thread.sleep(5000L);
            Assert.assertTrue("Add comment window pop -up failed :", selenium.isTextPresent("Add New Comment"));
            Assert.assertEquals("Add comment window  pop -up failed :", "Add", selenium.getValue("//div[15]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input"));
            Assert.assertEquals("Add comment window  pop -up failed :", "Cancel", selenium.getValue("//div[15]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input[2]"));
            driver.findElement(By.id("comment")).sendKeys("Comment1");
            Thread.sleep(3000L);
            driver.findElement(By.xpath("//div[15]/div/div[3]/div[3]/form/table/tbody/tr[2]/td/input")).click();
            Thread.sleep(5000L);
            Assert.assertEquals("Added comment failed :", "Comment1 \n posted on 0m ago by admin", selenium.getText("//div[4]/div/div[2]/table/tbody/tr/td/div"));
            //Delete Comment
            driver.findElement(By.id("closeC0")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Comment Delete pop-up  failed :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Comment Delete pop-up  failed :", selenium.isTextPresent("exact:Are you sure you want to delete this comment?"));
            selenium.click("//button");
            //Sign out
            new GregUserLogout().userLogout(driver);
        } catch (AssertionFailedError e) {
            log.info("GregAddCommenttoCollectionSeleniumTest - Assertion Failure ::" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - Assertion Failure :" + e.getMessage());
        } catch (WebDriverException e) {
            log.info("GregAddCommenttoCollectionSeleniumTest - WebDriver Exception :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - WebDriver Exception :" + e.getMessage());
        } catch (Exception e) {
            log.info("GregAddCommenttoCollectionSeleniumTest- Fail :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - Exception :" + e.getMessage());
        }
    }

    private void addCommentToRoot(){
        try {
            userLogin();
            gotoDetailViewTab();
            //Add Comment
            driver.findElement(By.id("commentsIconMinimized")).click();
            Thread.sleep(2000L);
            driver.findElement(By.linkText("Add Comment")).click();
            Thread.sleep(5000L);
            Assert.assertTrue("Add comment window pop -up failed :", selenium.isTextPresent("Add New Comment"));
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
            Assert.assertEquals("Add comment window  pop -up failed :", "Add", selenium.getValue("//div[3]/div[3]/form/table/tbody/tr[2]/td/input"));
            Assert.assertEquals("Add comment window  pop -up failed :", "Cancel", selenium.getValue("//div[3]/div[3]/form/table/tbody/tr[2]/td/input[2]"));
            driver.findElement(By.id("comment")).sendKeys("rootComment");
            Thread.sleep(3000L);
            driver.findElement(By.xpath("//div[3]/div[3]/form/table/tbody/tr[2]/td/input")).click();
            Thread.sleep(5000L);
            Assert.assertEquals("Added comment failed :", "rootComment \n posted on 0m ago by admin", selenium.getText("//div[4]/div/div[2]/table/tbody/tr/td/div"));
            //Delete Comment
            driver.findElement(By.id("closeC0")).click();
            Thread.sleep(3000L);
            Assert.assertTrue("Comment Delete pop-up  failed :", selenium.isTextPresent("WSO2 Carbon"));
            Assert.assertTrue("Comment Delete pop-up  failed :", selenium.isTextPresent("exact:Are you sure you want to delete this comment?"));
            selenium.click("//button");
            //Sign out
            new GregUserLogout().userLogout(driver);
        } catch (AssertionFailedError e) {
            log.info("GregAddCommenttoCollectionSeleniumTest - Assertion Failure ::" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - Assertion Failure :" + e.getMessage());
        } catch (WebDriverException e) {
            log.info("GregAddCommenttoCollectionSeleniumTest - WebDriver Exception :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - WebDriver Exception :" + e.getMessage());
        } catch (Exception e) {
            log.info("GregAddCommenttoCollectionSeleniumTest- Fail :" + e.getMessage());
            new SeleniumScreenCapture().getScreenshot(driver, "greg", "GregAddCommenttoCollectionSeleniumTest");
            driver.quit();
            Assert.fail("GregAddCommenttoCollectionSeleniumTest - Exception :" + e.getMessage());
        }

    }
}
