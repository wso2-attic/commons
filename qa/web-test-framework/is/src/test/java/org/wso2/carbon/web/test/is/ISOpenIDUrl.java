package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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


public class ISOpenIDUrl extends CommonSetup {

    public ISOpenIDUrl(String text) {
        super(text);
    }


    //Create a user profile for different types of users.
     public static void testCreateUserProfile(String profConf,String UserName,String Passwd,String profile, String fn, String ln, String org, String addr, String country, String email, String tel, String mob, String im, String url) throws Exception{
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI(UserName,Passwd);
        ISMyProfile.addNewProfile(profConf,profile,fn,ln,org,addr,country,email,tel,mob,im,url);
        mySeleniumTestBase.logOutUI();
     }

    //Create User Management user.
      public static void testCreateUMUser(String NewUsernm,String NewPasswd,String RoleName,String Role,String fn, String ln, String org, String addr, String country, String email, String tel, String mob, String im, String url) throws Exception{
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI("admin", "admin");
        mySeleniumTestBase.addNewUser(NewUsernm, NewPasswd);
        mySeleniumTestBase.addNewRole(RoleName, NewUsernm, Role);
        mySeleniumTestBase.logOutUI();
     }

    //Test menu.
    public static void testMenu(String UserType) throws Exception{

        boolean test1=selenium.isTextPresent("Policies");

		boolean test2=selenium.isTextPresent("User Management");
		boolean test3=selenium.isTextPresent("Claim Management");
		boolean test4=selenium.isTextPresent("Profile Management");
		boolean test5=selenium.isTextPresent("Key Stores");
		boolean test6=selenium.isTextPresent("XKMS");
		boolean test7=selenium.isTextPresent("Logging");

		boolean test8=selenium.isTextPresent("Card Issuer");
		boolean test9=selenium.isTextPresent("Security Token Service");
		boolean test10=selenium.isTextPresent("Shutdown/Restart");

		boolean test11=selenium.isTextPresent("Browse");
		boolean test12=selenium.isTextPresent("Search");

		boolean test13=selenium.isTextPresent("System Statistics");
		boolean test14=selenium.isTextPresent("System Logs");

		boolean test15=selenium.isTextPresent("My Profiles");
		boolean test16=selenium.isTextPresent("InfoCard/OpenID");
		boolean test17=selenium.isTextPresent("Relying Parties");
		boolean test18=selenium.isTextPresent("Multifactor Authentication");

        if(UserType.equals("admin"))
        {
            if(test1 && test2 && test3 && test4 && test5 && test6 && test7 && test8 && test9 && test10 && test11 && test12 && test13 && test14 && test15 && test16 && test16 && test17 && test18)
            {
                System.out.println("OpenId Url for "+UserType+" user :");
                System.out.println("Entitlement,Configure,Manage,Registry,Monitor and My Identity menues are present......... So Test is passed.");
                System.out.println("------------------------------------------------------------");
            }
            else{
                System.out.println("OpenId Url for "+UserType+" user :");
                System.out.println("Test is failed.........Need to check.........");
                System.out.println("------------------------------------------------------------");
            }
        }
        else{
            if(!test1 && !test2 && !test3 && !test4 && !test5 && !test6 && !test7 && !test8 && !test9 && !test10 && !test11 && !test12 && !test13 && !test14 && test15 && test16 && test16 && test17 && test18)
            {
              System.out.println("OpenId Url for "+UserType+" user :");
              System.out.println("My Identity menue is present.Other menues are not present......... So Test is passed.");
              System.out.println("------------------------------------------------------------");
            }
            else{
                System.out.println("OpenId Url for "+UserType+" user :");
                System.out.println("Test is failed.........Need to check.........");
                System.out.println("------------------------------------------------------------");
            }
        }

    }

    //Check OpenID url of each users.
    public static void testOpenIDUrlOfUsers(String UserName,String Passwd) throws Exception{
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
        mySeleniumTestBase.loginToUI(UserName,Passwd);

        selenium.click("link=InfoCard/OpenID");
		selenium.waitForPageToLoad("30000");

      //  String url="https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+UserName;
        String url="https://"+ISCommon.loadProperties().getProperty("host.name")+ISCommon.loadProperties().getProperty("openid.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+UserName;
		assertTrue(selenium.isTextPresent("exact:"+url));
        selenium.click("link=Delete");
		assertTrue(selenium.isTextPresent("You cannot remove the OpenID provided by WSO2 Identity Server"));
		selenium.click("//button[@type='button']");
	    mySeleniumTestBase.logOutUI();
    }


    //Sign in with OpenID url,.........empty data with default profile.
     public static void testSignInWithOpenID1(String UserName,String Passwd,String WrongPasswd,String UserType) throws Exception{
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);

        selenium.click("link=InfoCard/OpenID Sign-in");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Sign-in with OpenID"));
		//selenium.type("openIdUrl", "https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+UserName);
		selenium.type("openIdUrl", "https://"+ISCommon.loadProperties().getProperty("host.name")+ISCommon.loadProperties().getProperty("openid.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+UserName);
        selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("https://"+ISCommon.loadProperties().getProperty("host.name")+ISCommon.loadProperties().getProperty("openid.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+UserName));
        selenium.type("password", WrongPasswd);
		selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Invalid credentials"));
		selenium.click("//button[@type='button']");
		selenium.type("password", Passwd);
		selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("OpenID User Profile"));
        selenium.select("selectedProfile", "label=default");

		selenium.click("approve");
		selenium.waitForPageToLoad("30000");
        Thread.sleep(10000);

        testMenu(UserType);

        mySeleniumTestBase.logOutUI();
    }


    //Sign in with OpenIDurl,...........
    public static void testSignInWithOpenID(String UserName,String Passwd,String WrongPasswd,String Email,String FirstName,String Country,String profile,String UserType) throws Exception{
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);

        selenium.click("link=InfoCard/OpenID Sign-in");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Sign-in with OpenID"));
	//	selenium.type("openIdUrl", "https://"+ISCommon.loadProperties().getProperty("host.name")+":"+ISCommon.loadProperties().getProperty("https.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+UserName);
        selenium.type("openIdUrl", "https://"+ISCommon.loadProperties().getProperty("host.name")+ISCommon.loadProperties().getProperty("openid.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+UserName);
		selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("https://"+ISCommon.loadProperties().getProperty("host.name")+ISCommon.loadProperties().getProperty("openid.port")+ISCommon.loadProperties().getProperty("context.root")+"/openid/"+UserName));
        selenium.type("password", WrongPasswd);
		selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Invalid credentials"));
		selenium.click("//button[@type='button']");
		selenium.type("password", Passwd);
		selenium.click("//input[@value='Login']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("OpenID User Profile"));
        if(profile.equals("default"))
            selenium.select("selectedProfile", "label="+profile);
        else {
            selenium.select("selectedProfile", "label="+profile);
		    selenium.waitForPageToLoad("30000");
        }
		assertTrue(selenium.isTextPresent(Email));
		assertTrue(selenium.isTextPresent(FirstName));
		assertTrue(selenium.isTextPresent(Country));

		selenium.click("approve");
		selenium.waitForPageToLoad("30000");
        Thread.sleep(10000);
        testMenu(UserType);

        mySeleniumTestBase.logOutUI();
    }

    //Dlelete users and profiles
    public static void testDeleteUsersAndProfiles(String SignUpUsernm,String SignUpRole,String NewUsernm,String NewRole,String profile) throws Exception{
       SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);
       mySeleniumTestBase.loginToUI("admin", "admin");

       mySeleniumTestBase.deleteUser(SignUpUsernm);
       mySeleniumTestBase.deleteUser(NewUsernm);
       mySeleniumTestBase.deleteRole(SignUpRole);
       mySeleniumTestBase.deleteRole(NewRole);

       ISMyProfile.deleteNewProfile(profile);

       mySeleniumTestBase.logOutUI();
    }
}
