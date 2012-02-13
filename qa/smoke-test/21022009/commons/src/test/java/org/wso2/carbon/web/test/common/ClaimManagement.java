package org.wso2.carbon.web.test.common;

import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: Yumani Ranaweera
 * Date: May 19, 2009
 * Time: 12:24:06 PM
 * To change this template use File | Settings | File Templates.
 */


public class ClaimManagement extends TestCase {
    Selenium browser;


    public ClaimManagement(Selenium _browser) {
        browser = _browser;
    }


    /* Claim Management UI  for internal user stores*/
    public void testInternalUSClaimsUI(String claimUrlIntUserStore) throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        browser.open("/carbon/claim-mgt/index.jsp?region=region1&item=claim_mgt_menu");
        browser.waitForPageToLoad("30000");
        browser.click("link=Claim Management");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Claim Management"));
        assertTrue(browser.isElementPresent("link=Add New Claim Dialect"));

        //Claim dialects for External user store
        assertTrue(browser.isTextPresent("Available Claim Dialects for Internal User Store"));
        browser.click("link=exact:" + claimUrlIntUserStore);
        browser.waitForPageToLoad("30000");

        //In the claim view page
        assertTrue(browser.isTextPresent("Claim Management")); // ---- need to be updated to "Claim View once CARBON-3816 is fixed.
        assertTrue(browser.isElementPresent("link=Add New Claim Mapping"));
    }


    /* Claim Management UI  for external user stores*/
    public void testExternalUSClaimsUI() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);

        //Test claim dialects for external user stores only if an external user store is available.
        browser.click("link=User Management");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("User Management"));

        boolean x = browser.isElementPresent("link=Add External User Store");
        if (!x) {
            browser.click("link=Claim Management");
            browser.waitForPageToLoad("30000");

            //Claim dialects for External user store
            assertTrue(browser.isTextPresent("Available Claim Dialects for External User Store"));
            browser.click("//div[@id='workArea']/table[2]/tbody/tr[1]/td[1]/a"); // -- need to replace with a para for claim url
            browser.waitForPageToLoad("30000");

            //In the claim view page
            assertTrue(browser.isTextPresent("Claim Management"));    // ---- need to be updated to "Claim View" once CARBON-3816 is fixed.
            assertTrue(browser.isElementPresent("link=Add New Claim Mapping"));
        }
    }

}

