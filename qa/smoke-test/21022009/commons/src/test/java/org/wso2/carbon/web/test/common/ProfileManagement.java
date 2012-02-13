package org.wso2.carbon.web.test.common;

import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: Yumani Ranaweera
 * Date: May 19, 2009
 * Time: 11:06:37 AM
 * To change this template use File | Settings | File Templates.
 */


public class ProfileManagement extends TestCase {

    Selenium browser;

    public ProfileManagement(Selenium _browser) {
        browser = _browser;
    }


    /* Tests Profile Management UI */
    public void testProfileManagementUI(String claimURL,String profileConfigs) throws Exception {

       SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        browser.open("/carbon/profilemgt/index.jsp?region=region1&item=profilemgt_menu");
        browser.waitForPageToLoad("50000");
        browser.click("link=Profile Management");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Profile Configurations"));
        assertTrue(browser.isTextPresent("Profile Configurations for Internal User Store"));
        assertTrue(browser.isTextPresent("Profile Configurations for External User Store"));

        //Click on the Claim URL for http://schemas.xmlsoap.org/ws/2005/05/identity
        browser.click("link=exact:"+ claimURL);
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Profile Configurations for " + claimURL));
        assertTrue(browser.isElementPresent("link=Add New Profile Configuration"));
        assertTrue(browser.isElementPresent("link=" + profileConfigs));
        browser.click("link=" + profileConfigs);
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Profile Configuration for " + profileConfigs));
        browser.click("//input[@value='Cancel']");
        browser.waitForPageToLoad("30000");
    }

 // functional tests are to be added

}
