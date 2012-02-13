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
import com.thoughtworks.selenium.*;
import junit.framework.TestCase;

public class ISCardIssuer extends CommonSetup {
    public ISCardIssuer(String text) {
        super(text);
    }


    /* Access card issuer and check the UI */
    public static void cardIssuerUI() throws Exception {
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


    /* Test the UI of Card Issure configuration edit page */
    public static void cardIssureConfigurationUI() throws Exception {
        selenium.click("link=Card Issuer");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Configure");
        selenium.waitForPageToLoad("30000");
        assertEquals("Configuration Parameters", selenium.getText("//form[@id='configuration']/table/tbody/tr[2]/td"));
        assertTrue(selenium.isElementPresent("cardName"));
        assertEquals("Card Name", selenium.getText("//form[@id='configuration']/table/tbody/tr[3]/td/table/tbody/tr[1]/td[1]"));
        assertEquals("Valid Period", selenium.getText("//form[@id='configuration']/table/tbody/tr[3]/td/table/tbody/tr[2]/td[1]"));
        assertEquals("Supporting Token Types", selenium.getText("//form[@id='configuration']/table/tbody/tr[3]/td/table/tbody/tr[3]/td[1]"));
        assertEquals("Symmetric binding used", selenium.getText("//form[@id='configuration']/table/tbody/tr[3]/td/table/tbody/tr[4]/td[1]"));
    }


    /* Change the CardName Positive tests*/
    public static void editCardNamePositive(String cardname) throws Exception {

        selenium.click("link=Card Issuer");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Configure");
        selenium.waitForPageToLoad("30000");
        selenium.type("cardName", cardname);
        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("30000");
    }


    /* Change the CardName Negative tests*/
    public static void editCardNameNegative(String cardname) throws Exception {

        selenium.click("link=Card Issuer");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Configure");
        selenium.waitForPageToLoad("30000");
        selenium.type("cardName", cardname);
        selenium.click("//input[@value='Update']");
    }


    public static void editValidPeriod(String validperiod) throws Exception {

        selenium.click("link=Card Issuer");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Configure");
        selenium.waitForPageToLoad("30000");
        selenium.type("validPeriod", validperiod);
        selenium.click("//input[@value='Update']");
        // TO ADD - Sign-in from the card and see the valid period.
    }


    public static void editTokenType(String SAML10, String SAML11, String SAML20, String OpenID) throws Exception {

        selenium.click("link=Configure");
        selenium.waitForPageToLoad("30000");

        if (!SAML10.equals("")) {
            selenium.click(SAML10);
        }

        if (!SAML11.equals("")) {
            selenium.click(SAML11);
        }

        if (!SAML20.equals("")) {
            selenium.click(SAML20);
        }

        if (!OpenID.equals("")) {
            selenium.click(OpenID);
        }

        selenium.click("//input[@value='Update']");
        selenium.waitForPageToLoad("30000");
    }


    /* CSHelp */
    public static void testCSHelp() throws Exception{
        String expectedForCSHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/identity-sts/docs/userguide.html";
        selenium.click("link=Card Issuer");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("Information Cards Issuer Configuration"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }



}