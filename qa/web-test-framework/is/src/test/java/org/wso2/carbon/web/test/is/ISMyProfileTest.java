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


public class ISMyProfileTest extends CommonSetup {
    //selenium selenium;

    public ISMyProfileTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testSignin() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }

    //Test MyProfile UI.
    public void testmyProfilesUI() throws Exception {
        ISMyProfile.testCSHelp();
        boolean test[]=ISMyProfile.myProfileUI();
        if(test[0] && test[1] && test[2] && test[3] && test[4] && test[5] && test[6] && test[7] && test[8] && test[9] && test[10] && test[11])
            System.out.println("All default claims are available.........................");
        else
            System.out.println("Some default claims are not available....................");
    }

    //Basi MyProfie tests.
    public void testmyProfilesBasics() throws Exception {
        ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
        ISMyProfile.testUpdatedDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
        ISMyProfile.deleteDefault();
        ISMyProfile.addNewProfile("default","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
        ISMyProfile.testNewProfile("home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
        ISMyProfile.deleteNewProfile("home");
    }

    //Log out from the admin console.
    public void testSignout() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.logOutUI();
    }

}