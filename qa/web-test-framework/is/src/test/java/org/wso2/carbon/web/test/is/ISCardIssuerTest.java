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

import javax.smartcardio.Card;

public class ISCardIssuerTest extends CommonSetup {
    //selenium selenium;

    public ISCardIssuerTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }


    /* Access card issuer and check the UI */
    public void testCardIssureUI() throws Exception {
        ISCardIssuer.cardIssuerUI();
        ISCardIssuer.cardIssureConfigurationUI();
        ISCardIssuer.testCSHelp();
    }

    /* Edit card issuer configuration  - Positive scenarios */
    public void testCardIssureConfigurePositive() throws Exception {

        // tests for card name
        String[] cardName = {"This is a test", "#", "400","WSO2 Managed Card"};
        for (int i = 0; i < cardName.length; i++) {
            ISCardIssuer.editCardNamePositive(cardName[i]);
            assertTrue(selenium.isTextPresent(cardName[i]));
            // TO ADD - Sign-in from the card and see the card name.
        }

        //tests for valid period
        String[] validPeriod = {"200", "1", "400","365"};
        for (int i = 0; i < validPeriod.length; i++) {
            ISCardIssuer.editValidPeriod(validPeriod[i]);
            Thread.sleep(1000);
            assertTrue(selenium.isTextPresent(validPeriod[i]));
        }

        //tests for token type.
        ISCardIssuer.editTokenType("SAML10", "", "", "");
        assertTrue(selenium.isTextPresent("SAML11,SAML20,OpenID,"));

        ISCardIssuer.editTokenType("SAML10", "SAML11", "", "");
        assertTrue(selenium.isTextPresent("SAML20,OpenID,SAML10,"));

        ISCardIssuer.editTokenType("", "SAML11", "SAML20", "");
        assertTrue(selenium.isTextPresent("OpenID,SAML10,SAML11,"));

        ISCardIssuer.editTokenType("", "", "SAML20", "OpenID");
        assertTrue(selenium.isTextPresent("SAML10,SAML11,SAML20,"));

        ISCardIssuer.editTokenType("", "", "", "OpenID");
        assertTrue(selenium.isTextPresent("SAML10,SAML11,SAML20,OpenID"));
    }


    /* Edit card issuer configuration  - Positive scenarios */
    public void testCardIssureConfigureNegative() throws Exception {

        // tests for the card name
        ISCardIssuer.editCardNameNegative("");
        assertEquals("Card name is required", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");
        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");

        //tests for the valid period
        String[] validPeriod = {"0", "", "sss"};
        for (int i = 0; i < validPeriod.length; i++) {
            ISCardIssuer.editValidPeriod(validPeriod[i]);
            assertEquals("Invalid value for valid period", selenium.getText("messagebox-warning"));
            selenium.click("//button[@type='button']");
            selenium.click("//input[@value='Cancel']");
            selenium.waitForPageToLoad("30000");
        }
    }

    //Log out from the admin console.
    public void testLogout() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }
}