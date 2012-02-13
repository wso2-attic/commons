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


public class ISSignupTest extends CommonSetup {

    public ISSignupTest(String text) {
        super(text);
    }

    //Check Sign up User Interface.
    public void testSignupUI() throws Exception {
        boolean test[]=ISSignup.signupUI();
        if(test[0] && test[1] &&test[2] &&test[3] &&test[4] &&test[5] &&test[6] &&test[7] &&test[8] &&test[9])
            System.out.println("All default claims available................Test is passed..........");
        else
            System.out.println("Some default claims are not available.......Test is failed..........");
    }

    //Check basic Sign up details.
    public void testSignupBasics() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);

        ISSignup.signUpNewUser("tester","tester", "tester", "Tester123", "ABC Org", "12, flower strt", "SL", "tester@y.com", "0112632436", "0777795242", "tester@tt.com", "http://www.test.com");
        ISSignup.signInSignupUser("tester", "tester");
        ISSignup.myProfileSignupUser();
        ISSignup.mfAuthSignupUser();
        ISSignup.relyingPartiesSignupUser();
        ISSignup.inforOpenIDSignupUser();
        instseleniumTestBase.logOutUI();
        instseleniumTestBase.loginToUI("admin","admin");
        ISSignup.DeleteUserFromUM("tester");
        instseleniumTestBase.logOutUI();
    }

    //Test Mandatory Fields.
    public void testMandatoryFields() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        ISSignup.testMandatoryFieldsOf_SignUp("tester","1234","234567","tester","Tester","Tester123","tester","tester@t.com");
        ISSignup.signInSignupUser("tester", "tester");
        instseleniumTestBase.logOutUI();
    }

    //Check sign up user is in User Management.
    public void testUserInUM() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin","admin");
        ISSignup.testSignUpUser_InUM("tester");
        ISSignup.ChangePasswdOfUser("tester123","tester");
        instseleniumTestBase.logOutUI();
        ISSignup.signInSignupUser("tester", "tester123");
        instseleniumTestBase.logOutUI();
    }

    //Test with same sign up user name.
    public void testWithSameSignUpUser() throws Exception{
        ISSignup.SignUpUsing_SameUserName("tester","tester", "tester", "Tester123", "ABC Org", "12, flower strt", "SL", "tester@y.com", "0112632436", "0777795242", "tester@tt.com", "http://www.test.com");
    }

    //Delete user from User Management.
    public void testDeleteSignUpUser_FromUM() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin","admin");
        ISSignup.DeleteUserFromUM("tester");
        instseleniumTestBase.logOutUI();
        ISSignup.testInCorrectUser_Passwd("tester","tester111");
    }

}