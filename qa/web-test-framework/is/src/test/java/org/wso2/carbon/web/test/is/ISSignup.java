package org.wso2.carbon.web.test.is;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.io.FileInputStream;
import java.io.File;
import java.util.Properties;

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

public class ISSignup extends CommonSetup {

    public ISSignup(String text) {
        super(text);
    }


    public static boolean[] signupUI() throws Exception {

        ISCommon.forChangedContext();
        selenium.click("link=Sign-up");      //Click on sign-up link in Home page.
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Sign-up"));  //Sign-up page opens up
        assertTrue(selenium.isElementPresent("link=Sign-up"));
        assertTrue(selenium.isTextPresent("Sign-up with User Name / Password"));
        assertTrue(selenium.isElementPresent("//div[@id='loginbox']/a/img"));
        selenium.click("//div[@id='loginbox']/a/img");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Sign-up with User Name/Password"));
        assertTrue(selenium.isTextPresent("User Registration"));
        assertTrue(selenium.isTextPresent("User name*"));
        assertTrue(selenium.isTextPresent("Password*"));
        assertTrue(selenium.isTextPresent("Re-type Password*"));

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

        boolean arr[] = {firstName, lastName, org, add, country, email, tel, mobile, im, url};

        selenium.click("//input[@value='Cancel']");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Home");
        selenium.waitForPageToLoad("30000");

        return arr;
    }

    //Claims that are available in sign-up are set by 'Claim Management". Therefore set of claims that should be available in signup is dynamic.
    public static void signUpNewUser(String un, String pass, String givenname, String ln, String org, String address, String country, String email, String tel, String mob, String im, String url) throws Exception {

        ISCommon.forChangedContext();
        selenium.click("link=Sign-up");
        selenium.waitForPageToLoad("30000");
        selenium.click("//div[@id='loginbox']/a/img");
        selenium.waitForPageToLoad("30000");


        selenium.type("username", un);
        selenium.type("password", pass);
        selenium.type("retypedPassword", pass);

        if (selenium.isTextPresent("First Name *"))
            selenium.type("http://wso2.org/claims/givenname", givenname);

        if (selenium.isTextPresent("Last Name *"))
            selenium.type("http://wso2.org/claims/lastname", ln);

        if (selenium.isTextPresent("Organization"))
            selenium.type("http://wso2.org/claims/organization", org);

        if (selenium.isTextPresent("Address"))
            selenium.type("http://wso2.org/claims/streetaddress", address);

        if (selenium.isTextPresent("Country"))
            selenium.type("http://wso2.org/claims/country", country);

        if (selenium.isTextPresent("Email *"))
            selenium.type("http://wso2.org/claims/emailaddress", email);

        if (selenium.isTextPresent("Telephone"))
            selenium.type("http://wso2.org/claims/telephone", tel);

        if (selenium.isTextPresent("Mobile"))
            selenium.type("http://wso2.org/claims/mobile", mob);

        if (selenium.isTextPresent("IM"))
            selenium.type("http://wso2.org/claims/im", im);

        if (selenium.isTextPresent("URL"))
            selenium.type("http://wso2.org/claims/url", url);


        selenium.click("adduser");
        selenium.waitForPageToLoad("30000");
    }


    //Signing in to Indentity Server from the new user sccount
    public static void signInSignupUser(String un, String pass) throws Exception {

        ISCommon.forChangedContext();
        selenium.type("txtUserName", un);
        selenium.type("txtPassword", pass);
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");

        //The new user should only see 'Idenity' menu
        assertTrue(selenium.isTextPresent("WSO2 Identity Server Home"));
        assertTrue(selenium.isTextPresent("Signed-in as: " + un + "@" + ISCommon.loadProperties().getProperty("host.name") + ":" + ISCommon.loadProperties().getProperty("https.port")));
        assertEquals("Home", selenium.getText("link=Home"));
        assertTrue(selenium.isTextPresent("My Identity"));
        assertEquals("My Profiles", selenium.getText("link=My Profiles"));
        assertEquals("InfoCard/OpenID", selenium.getText("link=InfoCard/OpenID"));
        assertEquals("Relying Parties", selenium.getText("link=Relying Parties"));
        assertEquals("Multifactor Authentication", selenium.getText("link=Multifactor Authentication"));
    }


    public static void myProfileSignupUser() throws Exception {
        selenium.click("link=My Profiles");
        selenium.waitForPageToLoad("50000");
        assertTrue(selenium.isTextPresent("My Profile"));
    }


    public static void mfAuthSignupUser() throws Exception {
        selenium.click("link=Multifactor Authentication");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Multifactor Authentication Configurations"));

    }

    public static void inforOpenIDSignupUser() throws Exception {
        selenium.click("link=InfoCard/OpenID");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("InfoCard/OpenID Dashboard"));

    }

    public static void relyingPartiesSignupUser() throws Exception {
        selenium.click("link=Relying Parties");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("InfoCard/OpenID - Trusted Relying Parties"));
    }

    //Check if mandatory fields are validated.
    public static void testMandatoryFieldsOf_SignUp(String UserName, String ShortLengthPasswd, String MisMatchPasswd, String CorrectPasswd, String FirstN, String LastN, String InvalidEmail, String ValidEmail) throws Exception {
        ISCommon.forChangedContext();
        selenium.click("link=Sign-up");
        selenium.waitForPageToLoad("30000");
        selenium.click("//div[@id='loginbox']/a/img");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Sign-up with User Name/Password"));
        assertTrue(selenium.isTextPresent("User Registration"));

        //User Name is required.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("User name is required"));
        selenium.click("//button[@type='button']");
        selenium.type("username", UserName);
        //Password is required.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("Password is required"));
        selenium.click("//button[@type='button']");
        selenium.type("password", ShortLengthPasswd);
        //Retyped password is required.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("Retyped password is required"));
        selenium.click("//button[@type='button']");
        selenium.type("retypedPassword", MisMatchPasswd);
        //Password should be matched.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("Password mismatch"));
        selenium.click("//button[@type='button']");
        selenium.type("retypedPassword", ShortLengthPasswd);
        //Password should contain at least 6 characters.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("Password should contain at least 6 characters"));
        selenium.click("//button[@type='button']");
        selenium.type("password", CorrectPasswd);
        selenium.type("retypedPassword", CorrectPasswd);
        //First name is required.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("First Name is required"));
        selenium.click("//button[@type='button']");
        selenium.type("http://wso2.org/claims/givenname", FirstN);
        //Last name is required.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("Last Name is required"));
        selenium.click("//button[@type='button']");
        selenium.type("http://wso2.org/claims/lastname", LastN);
        //Email is required.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("Email is required"));
        selenium.click("//button[@type='button']");
        selenium.type("http://wso2.org/claims/emailaddress", InvalidEmail);
        //Email shoul be in valid format.
        selenium.click("adduser");
        assertTrue(selenium.isTextPresent("Email is not valid"));
        selenium.click("//button[@type='button']");
        selenium.type("http://wso2.org/claims/emailaddress", ValidEmail);
        //User registered successfully.
        selenium.click("adduser");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("User registered successfully"));
        selenium.click("//button[@type='button']");
    }

    //Check if sign up user is appearing in User Management.
    public static void testSignUpUser_InUM(String UserName) throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Users");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(UserName));

        assertTrue(selenium.isTextPresent("Change Password"));
        selenium.click("//table[@id='userTable']/tbody/tr[2]/td[2]/a[1]");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Change Password"));
        selenium.goBack();

        assertTrue(selenium.isTextPresent("User Profile"));
        selenium.click("//table[@id='userTable']/tbody/tr[2]/td[2]/a[2]");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("User Profiles : " + UserName));
        selenium.goBack();
        assertTrue(selenium.isTextPresent("Delete"));
    }

    //Change user password of user.
    public static void ChangePasswdOfUser(String NewPasswd, String UserName) throws Exception {
        selenium.click("//table[@id='userTable']/tbody/tr[2]/td[2]/a[1]");
        selenium.waitForPageToLoad("30000");
        selenium.type("newPassword", NewPasswd);
        selenium.type("checkPassword", NewPasswd);
        selenium.click("//input[@value='Change']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Password of user " + UserName + " successfully changed"));
        selenium.click("//button[@type='button']");
    }

    //Sign up with same user name.
    public static void SignUpUsing_SameUserName(String un, String pass, String givenname, String ln, String org, String address, String country, String email, String tel, String mob, String im, String url) throws Exception {
        signUpNewUser(un, pass, givenname, ln, org, address, country, email, tel, mob, im, url);
        assertTrue(selenium.isTextPresent("Error occurred while adding the user - not added"));
        selenium.click("//button[@type='button']");
        selenium.click("link=Sign-in");
        selenium.waitForPageToLoad("30000");
    }

    //Delete user from User Manangement.
    public static void DeleteUserFromUM(String UserName) throws Exception {
        selenium.click("link=User Management");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Users");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(UserName));
        selenium.click("link=Delete");
        assertTrue(selenium.isTextPresent("exact:Do you want to delete the user '" + UserName + "'?"));
        selenium.click("//button[@type='button']");
        selenium.waitForPageToLoad("30000");
    }

    //Sign in with invalid user account.
    public static void testInCorrectUser_Passwd(String un, String pass) throws Exception {
        ISCommon.forChangedContext();
        selenium.type("txtUserName", un);
        selenium.type("txtPassword", pass);
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Login failed! Please recheck the username and password and try again."));
        selenium.click("//button[@type='button']");
    }


}