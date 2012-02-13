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

public class CardIssuerTest extends CommonSetup {
    //Selenium selenium;

    public CardIssuerTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
    }


    /* Access card issuer and check the UI */
    public void testCardIssuerUI() throws Exception {
        selenium.click("link=Card Issuer");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Card Issuer Configuration"));
        assertTrue(selenium.isElementPresent("link=Configure"));
        assertEquals("Configuration Parameters", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[2]/td"));
        assertEquals("Card Name", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[3]/td[1]"));
        assertEquals("WSO2 Managed Card", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[3]/td[2]"));
        assertEquals("Valid Period", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[4]/td[1]"));
        assertEquals("365", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[4]/td[2]"));
        assertEquals("Supporting Token Types", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[5]/td[1]"));
        assertEquals("SAML10,SAML11,SAML20,OpenID", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[5]/td[2]"));
        assertEquals("Symmetric binding used", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[6]/td[1]"));
        assertEquals("false", selenium.getText("//div[@id='workArea']/table[2]/tbody/tr[6]/td[2]"));
    }


    /* Test the UI of CArd Issure configuration edit page */
    public void testCIConfifigurationUI() throws Exception {
        selenium.click("link=Card Issuer");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Configure");
        selenium.waitForPageToLoad("30000");
        assertEquals("Configuration Parameters", selenium.getText("//form[@id='configuration']/table/tbody/tr[2]/td"));
        assertTrue(selenium.isElementPresent("cardName"));
        assertEquals("Card Name", selenium.getText("//form[@id='configuration']/table/tbody/tr[3]/td[1]"));
        assertTrue(selenium.isElementPresent("validPeriod"));
        assertEquals("Valid Period", selenium.getText("//form[@id='configuration']/table/tbody/tr[4]/td[1]"));
        assertEquals("Supporting Token Types", selenium.getText("//form[@id='configuration']/table/tbody/tr[5]/td[1]"));
        assertEquals("SAML10", selenium.getText("//form[@id='configuration']/table/tbody/tr[5]/td[2]/div[1]"));
        assertEquals("SAML11", selenium.getText("//form[@id='configuration']/table/tbody/tr[5]/td[2]/div[2]"));
        assertEquals("SAML20", selenium.getText("//form[@id='configuration']/table/tbody/tr[5]/td[2]/div[3]"));
        assertEquals("OpenID", selenium.getText("//form[@id='configuration']/table/tbody/tr[5]/td[2]/div[4]"));
        assertEquals("Symmetric binding used", selenium.getText("//form[@id='configuration']/table/tbody/tr[6]/td[1]"));
    }


    /*  *//* Configure card issure and check the updates *//*
    public void testConfigureCI() throws Exception {
        selenium.click("link=Card Issuer");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Configure");
        selenium.waitForPageToLoad("30000");
        selenium.type("cardName", "this is a test");
        selenium.type("validPeriod", "250");
        selenium.click("SAML10");
        selenium.click("SAML20");
        selenium.click("isSymmetricBinding");
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("this is a test"));
        assertTrue(selenium.isTextPresent("250"));
        assertTrue(selenium.isTextPresent("SAML11,OpenID,"));
        assertTrue(selenium.isTextPresent("false"));
    }


    *//* Resetting the environment *//*
    public void testResetingEnv() throws Exception {
        selenium.click("link=Card Issuer");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Configure");
        selenium.waitForPageToLoad("30000");
        selenium.type("cardName", "WSO2 Managed Card");
        selenium.type("validPeriod", "365");
        selenium.click("SAML10");
        selenium.click("SAML20");
        selenium.click("isSymmetricBinding");
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("30000");
    }
*/

    public void testLogout() throws Exception {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(selenium);
        instSeleniumTestBase.logOutUI();
    }
}
