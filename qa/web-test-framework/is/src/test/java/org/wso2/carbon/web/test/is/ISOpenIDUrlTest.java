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


public class ISOpenIDUrlTest extends CommonSetup {

    public ISOpenIDUrlTest(String text) {
        super(text);
    }


    //For sign up user
    public void testOpenIDUrlForSignupUser() throws Exception{

        ISSignup.signUpNewUser("tester1","tester1", "tester1", "tester1", "ABC Org", "12, flower strt", "SL", "tester1@y.com", "0112632436", "0777795242", "tester1@tt.com", "http://www.test.com");
        ISOpenIDUrl.testCreateUserProfile("default","tester1","tester1","home", "tester2", "tester2", "ABC", "85, borupana road, ratmalana", "SL", "tester2@yahoo.com", "43434343", "77799999", "tester2@t.com", "https://tester2.com");
        ISOpenIDUrl.testOpenIDUrlOfUsers("tester1","tester1");
        ISOpenIDUrl.testSignInWithOpenID("tester1","tester1","abcdef","tester1@y.com","tester1","SL","default","tester1");
        ISOpenIDUrl.testSignInWithOpenID("tester1","tester1","abcdef","tester2@yahoo.com","tester2","SL","home","tester1");
    }

    //For admin user
    public void testOpenIDUrlForAdminUser() throws Exception{
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);

        ISOpenIDUrl.testOpenIDUrlOfUsers("admin","admin");
        ISOpenIDUrl.testSignInWithOpenID1("admin","admin","abcdef","admin");
        mySeleniumTestBase.loginToUI("admin","admin");
        ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
        mySeleniumTestBase.logOutUI();

        ISOpenIDUrl.testCreateUserProfile("default","admin","admin","home", "tester2", "tester2", "ABC", "85, borupana road, ratmalana", "SL", "tester2@yahoo.com", "43434343", "77799999", "tester2@t.com", "https://tester2.com");
        ISOpenIDUrl.testOpenIDUrlOfUsers("admin","admin");
        ISOpenIDUrl.testSignInWithOpenID("admin","admin","abcdef","admin@yahoo.com","admin","SL","default","admin");
        ISOpenIDUrl.testSignInWithOpenID("admin","admin","abcdef","tester2@yahoo.com","tester2","SL","home","admin");
    }

    //For User Management User
    public void testOpenIDUrlForUMUser() throws Exception{
        SeleniumTestBase mySeleniumTestBase = new SeleniumTestBase(selenium);

        ISOpenIDUrl.testCreateUMUser("user1","userone","wso2","Login to admin console","user1", "user1", "ABC", "85, testers zone", "SL", "user1@yahoo.com", "123456", "777987676", "user1@t.com", "https://user1.com");
        ISOpenIDUrl.testOpenIDUrlOfUsers("user1","userone");
        ISOpenIDUrl.testSignInWithOpenID1("user1","userone","abcdef","user1");
        mySeleniumTestBase.loginToUI("user1","userone");
        ISMyProfile.updateDefaultProfile("user1", "user1", "ABC", "85, testers zone", "SL", "user1@yahoo.com", "123456", "777987676", "user1@t.com", "https://user1.com");
        mySeleniumTestBase.logOutUI();

        ISOpenIDUrl.testCreateUserProfile("default","user1","userone","home", "tester2", "tester2", "ABC", "85, borupana road, ratmalana", "SL", "tester2@yahoo.com", "43434343", "77799999", "tester2@t.com", "https://tester2.com");
        ISOpenIDUrl.testOpenIDUrlOfUsers("user1","userone");
        ISOpenIDUrl.testSignInWithOpenID("user1","userone","abcdef","user1@yahoo.com","user1","SL","default","user1");
        ISOpenIDUrl.testSignInWithOpenID("user1","userone","abcdef","tester2@yahoo.com","tester2","SL","home","user1");
    }

     //Delete users
    public void testDaleteUsers() throws Exception{
        ISOpenIDUrl.testDeleteUsersAndProfiles("tester1","identity","user1","wso2","home");
    }
}
