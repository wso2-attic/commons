package org.wso2.carbon.web.test.common;

import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import junit.framework.TestCase;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

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


public class ProfileManagement extends TestCase {

    Selenium selenium;

    public ProfileManagement(Selenium _selenium) {
        selenium = _selenium;
    }

    // Loading the property file.
    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
        return properties;
    }

    /* Tests Profile Management UI */
    public void testProfileManagementUI(String profileConfig) throws Exception {
        selenium.click("link=Profile Management");
        selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Profile Configurations"));
		assertTrue(selenium.isElementPresent("link=Add New Profile Configuration"));
		assertTrue(selenium.isTextPresent("Available Profile Configurations"));
		assertEquals("default", selenium.getTable("//div[@id='workArea']/table.1.0"));
		assertEquals("Delete", selenium.getTable("//div[@id='workArea']/table.1.1"));

        //Click on the Claim URL for http://schemas.xmlsoap.org/ws/2005/05/identity
        assertTrue(selenium.isElementPresent("link=Add New Profile Configuration"));
        selenium.click("link="+ profileConfig );
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Profile Configurations for "+ profileConfig));
		selenium.click("//input[@value='Cancel']");
		selenium.waitForPageToLoad("30000");
    }

    /* Delete the default profile configuration */
     public void testDeleteDefaultProfile(String profileConfig) throws Exception{
        selenium.click("link=Profile Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Delete");
		assertTrue(selenium.isTextPresent("You cannot remove the '"+profileConfig+"' profile"));
		selenium.click("//button[@type='button']");
     }

    /* Update the default profile configuration */
    public void testUpdateDefaultProfile_Configuration(String profileConfig,String ClaimUri,String Behavior) throws Exception{
        selenium.click("link=Profile Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link="+profileConfig);
		selenium.waitForPageToLoad("30000");
		selenium.select(ClaimUri, "label="+Behavior);
		selenium.click("//input[@value='Update']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Profile configuration updated successfully"));
		selenium.click("//button[@type='button']");
    }


    /*Add a new profile configuration */
    public void testAddNewProfile_Configuration(String profileConfName) throws Exception{
        selenium.click("link=Profile Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Add New Profile Configuration");
		selenium.waitForPageToLoad("30000");
		selenium.type("profile", profileConfName);
		selenium.click("//input[@value='Add']");
		selenium.waitForPageToLoad("30000");

        //Add a new profile configuration using a same name as an existing profile configuration.
        if(selenium.isTextPresent("Profile configuration added successfully")) {
           selenium.click("//button[@type='button']");
           assertTrue(selenium.isTextPresent(profileConfName));
        }

        //Add a new profile confguration using the name 'default'.
        if(selenium.isTextPresent("Error while adding profile configuration - make sure profile configuration name is unique")){
            selenium.click("//button[@type='button']");
        }
    }


    /* Delete new profile configuration */
    public void testDeleteNewProfile_Configuration(String profileConfName) throws Exception{
        selenium.click("link=Profile Management");
        selenium.waitForPageToLoad("30000");

        if(selenium.getTable("//div[@id='workArea']/table.1.0").equals(profileConfName))
             selenium.click("link=Delete");

        else
             selenium.click("//a[@onclick=\"remove('Internal','http://wso2.org/claims','"+profileConfName+"');return false;\"]");

        assertTrue(selenium.isTextPresent("exact:You are about to remove "+profileConfName+". Do you want to proceed?"));
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Profile configuration deleted successfully"));
		selenium.click("//button[@type='button']");
    }


    /* Set Mandatory fields' Behavior */
    public void testSetMandatoryFields_Behavior(String profileConf,String behavior) throws Exception{
        ProfileManagement instProfileManagement = new ProfileManagement(selenium);

        instProfileManagement.testUpdateDefaultProfile_Configuration(profileConf,"http://wso2.org/claims/givenname",behavior);
        instProfileManagement.testUpdateDefaultProfile_Configuration(profileConf,"http://wso2.org/claims/lastname",behavior);
        instProfileManagement.testUpdateDefaultProfile_Configuration(profileConf,"http://wso2.org/claims/emailaddress",behavior);
    }

    /*Set all fields' Behavior */
    public void testSetAllFields_Behavior(String profileConf,String behaviorOfMandatory,String behaviorOfOptional) throws Exception{
       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       String arr[]={"givenname","lastname","emailaddress","nickname","dob","gender","country","streetaddress","telephone","mobile","locality","postalcode","region","role","title","url","im","organization","otherphone","fullname","stateorprovince"};
       int i=0;
       while(i<21){
           if(("http://wso2.org/claims/"+arr[i]).equals("http://wso2.org/claims/givenname")||("http://wso2.org/claims/"+arr[i]).equals("http://wso2.org/claims/lastname")||("http://wso2.org/claims/"+arr[i]).equals("http://wso2.org/claims/emailaddress"))
                instProfileManagement.testUpdateDefaultProfile_Configuration(profileConf,"http://wso2.org/claims/"+arr[i],behaviorOfMandatory);
           else
                instProfileManagement.testUpdateDefaultProfile_Configuration(profileConf,"http://wso2.org/claims/"+arr[i],behaviorOfOptional);

           i=i+1;
       }
    }

    /* CSHelp */
    public void testCSHelp() throws Exception{
        String expectedForCSHelp="https://"+ loadProperties().getProperty("host.name")+":"+loadProperties().getProperty("https.fe.port")+loadProperties().getProperty("context.root")+"/carbon/profilemgt/docs/userguide.html";
        selenium.click("link=Profile Management");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("Profile Configuration Management"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }

}