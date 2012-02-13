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


public class ISMyProfile extends CommonSetup {


    public ISMyProfile(String text) {
        super(text);
    }


    public static boolean[] myProfileUI() throws Exception {
        selenium.click("link=My Profiles");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        assertEquals("Update Profile", selenium.getText("//div[@id='middle']/h2"));

        boolean profileName = selenium.isTextPresent("Profile Name*");
        boolean profileConf = selenium.isTextPresent("Profile Configuration");
        boolean firstName = selenium.isTextPresent("First Name *");
        boolean lastName = selenium.isTextPresent("Last Name *");
        boolean org = selenium.isTextPresent("Organization");
        boolean add = selenium.isTextPresent("Address");
        boolean country = selenium.isTextPresent("Country");
        boolean email = selenium.isTextPresent("Email *");
        boolean tel = selenium.isTextPresent("Telephone");
        boolean mobile = selenium.isTextPresent("Mobile");
        boolean im = selenium.isTextPresent("IM");
        boolean url = selenium.isTextPresent("URL");

        boolean result[] = {profileName, profileConf, firstName, lastName, org, add, country, email, tel, mobile, im, url};

        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("My Profiles"));
        return result;
    }


    public static void updateDefaultProfile(String fn, String ln, String org, String addr, String country, String email, String tel, String mob, String im, String url) throws Exception {
        selenium.click("link=My Profiles");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        selenium.type("http://wso2.org/claims/givenname", fn);
        selenium.type("http://wso2.org/claims/lastname", ln);
        selenium.type("http://wso2.org/claims/organization", org);
        selenium.type("http://wso2.org/claims/streetaddress", addr);
        selenium.type("http://wso2.org/claims/country", country);
        selenium.type("http://wso2.org/claims/emailaddress", email);
        selenium.type("http://wso2.org/claims/telephone", tel);
        selenium.type("http://wso2.org/claims/mobile", mob);
        selenium.type("http://wso2.org/claims/im", im);
        selenium.type("http://wso2.org/claims/url", url);
        selenium.click("updateprofile");
        selenium.waitForPageToLoad("30000");
        assertEquals("User profile updated successfully", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }

    public static void testUpdatedDefaultProfile(String fn, String ln, String org, String addr, String country, String email, String tel, String mob, String im, String url) throws Exception {
        selenium.click("link=My Profiles");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=default");
        selenium.waitForPageToLoad("30000");
        assertEquals(fn, selenium.getValue("http://wso2.org/claims/givenname"));
        assertEquals(ln, selenium.getValue("http://wso2.org/claims/lastname"));
        assertEquals(org, selenium.getValue("http://wso2.org/claims/organization"));
        assertEquals(addr, selenium.getValue("http://wso2.org/claims/streetaddress"));
        assertEquals(country, selenium.getValue("http://wso2.org/claims/country"));
        assertEquals(email, selenium.getValue("http://wso2.org/claims/emailaddress"));
        assertEquals(tel, selenium.getValue("http://wso2.org/claims/telephone"));
        assertEquals(mob, selenium.getValue("http://wso2.org/claims/mobile"));
        assertEquals(im, selenium.getValue("http://wso2.org/claims/im"));
        assertEquals(url, selenium.getValue("http://wso2.org/claims/url"));
        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
    }


    public static void deleteDefault() throws Exception {
        selenium.click("link=My Profiles");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"remove('admin','default');return false;\"]");
        assertEquals("You cannot remove the 'default' profile", selenium.getText("messagebox-warning"));
        selenium.click("//button[@type='button']");
    }


    public static void addNewProfile(String profConf, String profile, String fn, String ln, String org, String addr, String country, String email, String tel, String mob, String im, String url) throws Exception {
        selenium.click("link=My Profiles");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Add New Profile");
        selenium.waitForPageToLoad("30000");
        selenium.type("profile", profile);
        selenium.select("profileConfiguration", "label=" + profConf);
        selenium.type("http://wso2.org/claims/givenname", fn);
        selenium.type("http://wso2.org/claims/lastname", ln);
        selenium.type("http://wso2.org/claims/organization", org);
        selenium.type("http://wso2.org/claims/streetaddress", addr);
        selenium.type("http://wso2.org/claims/country", country);
        selenium.type("http://wso2.org/claims/emailaddress", email);
        selenium.type("http://wso2.org/claims/telephone", tel);
        selenium.type("http://wso2.org/claims/mobile", mob);
        selenium.type("http://wso2.org/claims/im", im);
        selenium.type("http://wso2.org/claims/url", url);
        selenium.click("addprofile");
        selenium.waitForPageToLoad("30000");
        assertEquals("User profile added successfully", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }

    public static void testNewProfile(String profile, String fn, String ln, String org, String addr, String country, String email, String tel, String mob, String im, String url) {
        selenium.click("link=My Profiles");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=" + profile);
        selenium.waitForPageToLoad("30000");
        assertEquals(fn, selenium.getValue("http://wso2.org/claims/givenname"));
        assertEquals(ln, selenium.getValue("http://wso2.org/claims/lastname"));
        assertEquals(org, selenium.getValue("http://wso2.org/claims/organization"));
        assertEquals(addr, selenium.getValue("http://wso2.org/claims/streetaddress"));
        assertEquals(country, selenium.getValue("http://wso2.org/claims/country"));
        assertEquals(email, selenium.getValue("http://wso2.org/claims/emailaddress"));
        assertEquals(tel, selenium.getValue("http://wso2.org/claims/telephone"));
        assertEquals(mob, selenium.getValue("http://wso2.org/claims/mobile"));
        assertEquals(im, selenium.getValue("http://wso2.org/claims/im"));
        assertEquals(url, selenium.getValue("http://wso2.org/claims/url"));
        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
    }


    public static void deleteNewProfile(String profilename) throws Exception {
        selenium.click("link=My Profiles");
        selenium.waitForPageToLoad("30000");
        selenium.click("//a[@onclick=\"remove('admin','" + profilename + "');return false;\"]");
        Thread.sleep(1000);
        assertTrue(selenium.isElementPresent("messagebox-confirm"));
        assertTrue(selenium.isTextPresent("You are about to remove " + profilename + ". Do you want to proceed?"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
        assertEquals("User profile deleted successfully", selenium.getText("messagebox-info"));
        selenium.click("//button[@type='button']");
    }

    /* CSHelp */
    public static void testCSHelp() throws Exception{
        String expectedForCSHelp="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/carbon/userprofile/docs/userguide.html";
        selenium.click("link=My Profiles");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("User Profile Management"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

}