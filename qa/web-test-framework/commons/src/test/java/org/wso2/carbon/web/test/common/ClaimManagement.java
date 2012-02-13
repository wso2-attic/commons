package org.wso2.carbon.web.test.common;

import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;

import java.awt.event.KeyEvent;
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


public class ClaimManagement extends TestCase {
    Selenium selenium;


    public ClaimManagement(Selenium _selenium) {
        selenium = _selenium;
    }

    // Loading the property file.
    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
        return properties;
    }


    /* Claim Management UI  for internal user stores*/
    public void testInternalUSClaimsUI(String claimUrlIntUserStore) throws Exception {
        selenium.click("link=Claim Management");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Claim Management"));
        assertTrue(selenium.isElementPresent("link=Add New Claim Dialect"));

        //Claim dialects for External user store
        assertTrue(selenium.isTextPresent("Available Claim Dialects for Internal User Store"));
        selenium.click("link=exact:" + claimUrlIntUserStore);
        selenium.waitForPageToLoad("30000");

        //In the claim view page
        assertTrue(selenium.isTextPresent("Claim Management")); // ---- need to be updated to "Claim View once CARBON-3816 is fixed.
        assertTrue(selenium.isElementPresent("link=Add New Claim Mapping"));
    }


      /* Claim Management UI  for external user stores*/
    public boolean testExternalUSClaimsUI(String claimUrlExUserStore) throws Exception {
       //Test claim dialects for external user stores only if an external user store is available.
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("User Management"));

        boolean x = selenium.isElementPresent("link=Add External User Store");
        if (!x) {
            selenium.click("link=Claim Management");
            selenium.waitForPageToLoad("30000");

            //Claim dialects for External user store
            assertTrue(selenium.isTextPresent("Available Claim Dialects for External User Store"));
            selenium.click("//div[@id='workArea']/table[2]/tbody/tr[1]/td[1]/a"); // -- need to replace with a para for claim url. this is the click for http://schemas.xmlsoap.org/ws/2005/05/identity
            selenium.waitForPageToLoad("30000");

            //In the claim view page
            assertTrue(selenium.isTextPresent("Available Claims for "+claimUrlExUserStore));    // ---- need to be updated to "Claim View" once CARBON-3816 is fixed.
            assertTrue(selenium.isElementPresent("link=Add New Claim Mapping"));
        }
        return x;
    }
//.................................................................................................................................................................................


   /* Login to available Claim Dialects of Internal and External User Store. */
    public void testInternal_ExternalUSUrl(String claimUrlUserStore,String In_ExUS) throws Exception{
        selenium.click("link=Claim Management");
        selenium.waitForPageToLoad("30000");
        int i=1;
        int j=1;
        String temp="";
        if(In_ExUS.equals("Internal"))
           i=1;
        if(In_ExUS.equals("External"))
           i=2;

           while((j<=4)){
                temp=selenium.getText("//div[@id='workArea']/table["+i+"]/tbody/tr["+ j +"]/td[1]/a");
                if(temp.equals(claimUrlUserStore)){
                  selenium.click("//div[@id='workArea']/table["+i+"]/tbody/tr["+ j +"]/td[1]/a");
                  selenium.waitForPageToLoad("30000");
                  assertTrue(selenium.isTextPresent("Available Claims for "+claimUrlUserStore));
                  j=5;
                }
                j=j+1;
           }
    }

//....................................................................................................................................................................

    /* Add New Claim. */
    public void testAddNewClaim(String claimUrlUserStore,String In_Ex,String DisplayName,String Description,String ClaimUri,String MappedAttribute,String regex,String displayOrder,String supported,String required) throws Exception{
        testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);

        selenium.click("link=Add New Claim Mapping");
		selenium.waitForPageToLoad("30000");
        //Check Mandatory Fields are available
		assertTrue(selenium.isTextPresent("New Claim Details"));
		assertTrue(selenium.isTextPresent("exact:Display Name*"));
		assertTrue(selenium.isTextPresent("exact:Description*"));
		assertTrue(selenium.isTextPresent("exact:Claim Uri*"));
		assertTrue(selenium.isTextPresent("exact:Mapped Attribute*"));

        selenium.click("//input[@value='Add']");
		assertTrue(selenium.isTextPresent("Attribute is required"));
		selenium.click("//button[@type='button']");
		selenium.type("displayName", DisplayName);
		selenium.click("//input[@value='Add']");
		assertTrue(selenium.isTextPresent("Attribute is required"));
		selenium.click("//button[@type='button']");
		selenium.type("description", Description);
		selenium.click("//input[@value='Add']");
		assertTrue(selenium.isTextPresent("Attribute is required"));
		selenium.click("//button[@type='button']");
		selenium.type("claimUri", ClaimUri);
		selenium.click("//input[@value='Add']");
		assertTrue(selenium.isTextPresent("Attribute is required"));
		selenium.click("//button[@type='button']");
		selenium.type("attribute", MappedAttribute);

        selenium.type("regex", regex);
		selenium.type("displayOrder", displayOrder);

        if(supported.equals("supported"))
		    selenium.click("supported");
        if(required.equals("required"))
		    selenium.click("required");

		selenium.click("//input[@value='Add']");
		selenium.waitForPageToLoad("30000");

        selenium.focus("link="+DisplayName);
        Thread.sleep(1000);
        selenium.click("link="+DisplayName);
        selenium.waitForPageToLoad("30000");
        assertEquals(DisplayName, selenium.getValue("displayName"));
		assertEquals(Description, selenium.getValue("description"));
		assertTrue(selenium.isTextPresent("exact:"+ClaimUri));
		assertEquals(MappedAttribute, selenium.getValue("attribute"));
    }


    /*Added new claims without considering assertions and checking mandatory fields.*/
    public void testAddClaims(String claimUrlUserStore,String In_Ex,String DisplayName,String Description,String ClaimUri,String MappedAttribute,String regex,String displayOrder,String supported,String required) throws Exception{
        testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);

        selenium.click("link=Add New Claim Mapping");
		selenium.waitForPageToLoad("30000");

        selenium.type("displayName", DisplayName);
        selenium.type("description", Description);
        selenium.type("claimUri", ClaimUri);
        selenium.type("attribute", MappedAttribute);
        selenium.type("regex", regex);
		selenium.type("displayOrder", displayOrder);

        if(supported.equals("supported"))
		    selenium.click("supported");
        if(required.equals("required"))
		    selenium.click("required");

		selenium.click("//input[@value='Add']");
		selenium.waitForPageToLoad("30000");
    }

    /* Update existing Claim. */
    public void testUpdateClaim(String claimUrlUserStore,String In_Ex,String ClaimName,String UpdateName,String MappedAttributes) throws Exception{
        testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);

        selenium.focus("link="+ClaimName);
        Thread.sleep(1000);
        selenium.click("link="+ClaimName);
        selenium.waitForPageToLoad("30000");
        selenium.type("displayName", UpdateName);
		selenium.type("description", UpdateName);
		selenium.type("attribute", MappedAttributes);
		selenium.click("//input[@value='Update']");
		selenium.waitForPageToLoad("30000");

        selenium.focus("link="+UpdateName);
        Thread.sleep(1000);
        selenium.click("link="+UpdateName);
        selenium.waitForPageToLoad("30000");
        assertEquals(UpdateName, selenium.getValue("displayName"));
		assertEquals(UpdateName, selenium.getValue("description"));
		assertEquals(MappedAttributes, selenium.getValue("attribute"));
    }

    /* Cancel the updated changes. */
    public void testCancelUpdateClaim(String claimUrlUserStore,String In_Ex,String ClaimName,String UpdateName,String MappedAttributes) throws Exception{
        testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);

        selenium.focus("link="+ClaimName);
        Thread.sleep(1000);
        selenium.click("link="+ClaimName);
        selenium.waitForPageToLoad("30000");
        String displayName1=selenium.getValue("displayName");
        String description1=selenium.getValue("description");
        String attribute1=selenium.getValue("attribute");
        selenium.type("displayName", UpdateName);
		selenium.type("description", UpdateName);
		selenium.type("attribute", MappedAttributes);
		selenium.click("//input[@value='Cancel']");
		selenium.waitForPageToLoad("30000");

        selenium.focus("link="+ClaimName);
        Thread.sleep(1000);
        selenium.click("link="+ClaimName);
        selenium.waitForPageToLoad("30000");
        assertEquals(displayName1, selenium.getValue("displayName"));
		assertEquals(description1, selenium.getValue("description"));
		assertEquals(attribute1, selenium.getValue("attribute"));

    }

    /* Add a claim with a claim uri that already exist. */
    public void testAlreadyExistClaimUri(String claimUrlUserStore,String In_Ex,String DisplayName,String Description,String AlreadyExistClaimUri,String MappedAttribute) throws Exception{
        testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);

        selenium.click("link=Add New Claim Mapping");
		selenium.waitForPageToLoad("30000");
        selenium.type("displayName", DisplayName);
		selenium.type("description", Description);
		selenium.type("claimUri", AlreadyExistClaimUri);
		selenium.type("attribute", MappedAttribute);
		selenium.click("//input[@value='Add']");
		selenium.waitForPageToLoad("30000");

        boolean present=selenium.isElementPresent("link="+DisplayName);
        assertTrue(!present);
    }

//..........................................................................................................................................................................................

    /* Delete a claim. */
    public void testDeleteClaim(String claimUrlUserStore,String In_Ex,String ClaimName) throws Exception{
        testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);

        selenium.focus("link="+ClaimName);
        Thread.sleep(1000);
        selenium.click("link="+ClaimName);
        selenium.waitForPageToLoad("30000");
        String temp = selenium.getText("//div[@id='workArea']/form/table/tbody/tr[1]/td/table/tbody/tr[3]/td[2]");
        selenium.click("link=Remove Claim Mapping");
		assertTrue(selenium.isTextPresent("exact:You are about to remove "+temp+". Do you want to proceed?"));
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");

        boolean present=selenium.isElementPresent("link="+ClaimName);
        assertTrue(!present);
    }

     /* Delete all the claims within a claim dialects. */
    public void testDeleteAllClaim(String claimUrlUserStore,String In_Ex) throws Exception{
       boolean test=true;
         while(test){
            testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);
            String temp = selenium.getText("//div[@id='workArea']/table/thead[1]/tr/th");
            testDeleteClaim(claimUrlUserStore,In_Ex,temp);
            selenium.click("link=Claim Management");
            selenium.waitForPageToLoad("30000");
            test=selenium.isTextPresent(claimUrlUserStore);
         }
    }

     /* Store Informations of a particular claim. */
     public String[] testStoreInformation_OfClaim(String claimUrlUserStore,String In_Ex,String ClaimName) throws Exception{
        testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);

        selenium.focus("link="+ClaimName);
        Thread.sleep(1000);
        selenium.click("link="+ClaimName);
        selenium.waitForPageToLoad("30000");

        String displayName=selenium.getValue("displayName");
		String description=selenium.getValue("description");
		String claimUri=selenium.getText("//div[@id='workArea']/form/table/tbody/tr[1]/td/table/tbody/tr[3]/td[2]");
		String attribute=selenium.getValue("attribute");
		String regex=selenium.getValue("regex");
		String displayOrder=selenium.getValue("displayOrder");
		String supported=selenium.getValue("supported");
		String required=selenium.getValue("required");

        String arr[]={displayName,description,claimUri,attribute,regex,displayOrder,supported,required};

        return arr;
    }

    /* Delete required claims. */
    public String[] testDeleteRequiredCliam(String claimUrlUserStore,String In_Ex) throws Exception{
        testInternal_ExternalUSUrl(claimUrlUserStore,In_Ex);

        String required ="off";
        int i=1;
        while(required.equals("off")){
             selenium.focus("link="+selenium.getText("//div[@id='workArea']/table/thead["+i+"]/tr/th"));
             Thread.sleep(1000);
             selenium.click("link="+selenium.getText("//div[@id='workArea']/table/thead["+i+"]/tr/th"));
             selenium.waitForPageToLoad("30000");

             required = selenium.getValue("required");
             selenium.click("//input[@value='Cancel']");
		     selenium.waitForPageToLoad("30000");
             i=i+1;
        }
        int j=i-1;
        String RequiredClaim = selenium.getText("//div[@id='workArea']/table/thead["+j+"]/tr/th");
        String info[]=testStoreInformation_OfClaim(claimUrlUserStore,In_Ex,RequiredClaim);
        Thread.sleep(3000);
        testDeleteClaim(claimUrlUserStore,In_Ex,RequiredClaim);

        String arr[]={RequiredClaim,info[0],info[1],info[2],info[3],info[4],info[5],info[6],info[7]};
        return arr;
    }

      /* Store supported and required attributes values. */
      public String[] testSupported_RequiredAttributes(String supported,String required) throws Exception{
        if(supported.equals("on"))
        supported="supported";
        else
        supported="";

        if(required.equals("on"))
        required="required";
        else
        required="";

        String arr[]={supported,required};
        return arr;
    }

//....................................................................................................................................................................................................


    /* CSHelp */
    public void testCSHelp() throws Exception{
        String expectedForCSHelp="https://"+ loadProperties().getProperty("host.name")+":"+loadProperties().getProperty("https.fe.port")+loadProperties().getProperty("context.root")+"/carbon/claim-mgt/docs/userguide.html";
        selenium.click("link=Claim Management");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
        assertTrue(selenium.isTextPresent("Claim Management"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
    }
}