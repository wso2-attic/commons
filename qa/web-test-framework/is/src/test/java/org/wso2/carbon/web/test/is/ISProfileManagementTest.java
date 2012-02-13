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
import org.wso2.carbon.web.test.common.ProfileManagement;

public class ISProfileManagementTest extends CommonSetup {

    public ISProfileManagementTest(String text) {
        super(text);
    }

    //Login to admin console and test Logging.
    public void testRun() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }


    /* Tests Profile Management UI */
    public void testProfileManagementUI() throws Exception {
        ProfileManagement instProfileManagement = new ProfileManagement(selenium);
        instProfileManagement.testProfileManagementUI("default");
        instProfileManagement.testCSHelp();
    }

    /*Test Default profile Configuration */
    public void testDefaultProfile_Configuration() throws Exception{
        ProfileManagement instProfileManagement = new ProfileManagement(selenium);
        instProfileManagement.testDeleteDefaultProfile("default");
        instProfileManagement.testUpdateDefaultProfile_Configuration("default","http://wso2.org/claims/nickname","Hidden");
        instProfileManagement.testUpdateDefaultProfile_Configuration("default","http://wso2.org/claims/nickname","Inherited");
    }

    /*Test New profile configuration */
    public void testNewProfileConfiguration() throws Exception{
        ProfileManagement instProfileManagement = new ProfileManagement(selenium);

        instProfileManagement.testAddNewProfile_Configuration("default");
        instProfileManagement.testAddNewProfile_Configuration("newProfile");
        instProfileManagement.testAddNewProfile_Configuration("newProfile");
        instProfileManagement.testDeleteNewProfile_Configuration("newProfile");
    }

    /* Test Combination 1 : When checked the empty fields of the second profile should have values from the default profile. */
    public void testCombination1() throws Exception{

        ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
        ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
        ISMyProfile.testNewProfile("home","tester","tester","ABC","85, testers zone","SL","tester@yahoo.com","123456","777987676","admin@t.com","https://admin.com");
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }

    /* Test Combination 2 : When the other profile is checked, its optional fields should appear without data. Mandatory fields should have data.*/
    public void testCombination2() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin", "admin");

        ISMyProfile.updateDefaultProfile("admin", "admin", "", "", "", "admin@yahoo.com", "", "", "", "");
        ISMyProfile.deleteNewProfile("home");
        ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
        ISMyProfile.testNewProfile("home","tester","tester","","","","tester@yahoo.com","","","","");

        instseleniumTestBase.logOutUI();
    }

    /* Test Combination 3 : When the other profile is checked, all of its fields should appear with data.*/
    public void testCombination3() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin", "admin");

        ISMyProfile.updateDefaultProfile("admin", "admin", "", "", "", "admin@yahoo.com", "", "", "", "");
        ISMyProfile.deleteNewProfile("home");
        ISMyProfile.addNewProfile("default","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
        ISMyProfile.testNewProfile("home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");

        instseleniumTestBase.logOutUI();
    }

    /* Test Combination 4 : When the other profile is checked, all of its fields should appear without data.
(mandatory fields are insturcted to hide data, there weren't data put for optional fields)*/
    public void testCombination4() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin", "admin");

        ProfileManagement instProfileManagement = new ProfileManagement(selenium);
        instProfileManagement.testSetMandatoryFields_Behavior("default","Hidden");
        ISMyProfile.updateDefaultProfile("admin", "admin", "", "", "", "admin@yahoo.com", "", "", "", "");
        ISMyProfile.deleteNewProfile("home");
        ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
        ISMyProfile.testNewProfile("home","","","","","","","","","","");

        instseleniumTestBase.logOutUI();
    }

    /* Test Combination 5 : When the other profile is checked, only mandatory fields should appear without data.*/
    public void testCombination5() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin", "admin");

        ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
        ISMyProfile.deleteNewProfile("home");
        ISMyProfile.addNewProfile("default","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
        ISMyProfile.testNewProfile("home","","","ABC","85, borupana road, ratmalana","SL","","43434343","77799999","tester@t.com","https://tester.com");

        instseleniumTestBase.logOutUI();
    }

   /* TestCombination 6 : When the other profile is checked, all of its fields should appear without data.*/
    public void testCombination6() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin", "admin");

        ProfileManagement instProfileManagement = new ProfileManagement(selenium);
        instProfileManagement.testSetAllFields_Behavior("default","Hidden","Hidden");
        ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
        ISMyProfile.deleteNewProfile("home");
        ISMyProfile.addNewProfile("default","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
        ISMyProfile.testNewProfile("home","","","","","","","","","","");

        instseleniumTestBase.logOutUI();
    }

    /* Test Combination 7 : When the other profile is checked, all of its fields should appear without data.*/
    public void testCombination7() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ISMyProfile.updateDefaultProfile("admin", "admin", "", "", "", "admin@yahoo.com", "", "", "", "");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home","","","","","","","","","","");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 8 : When the other profile is checked, all of its fields should appear without data.*/
    public void testCombination8() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home","","","","","","","","","","");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 9 : When the other profile is checked, it will have its own data for all the fields.*/
    public void testCombination9() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin", "admin");

        ProfileManagement instProfileManagement = new ProfileManagement(selenium);
        instProfileManagement.testSetAllFields_Behavior("default","Overridden","Inherited");
        ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
        ISMyProfile.deleteNewProfile("home");
        ISMyProfile.addNewProfile("default","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "yumani@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
        ISMyProfile.testNewProfile("home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");

        instseleniumTestBase.logOutUI();
    }

    /* Test Combination 10 : When the other profile is checked, it will have its own data for mandatory fields. Other fields will have data from the default profile.*/
    public void testCombination10() throws Exception{
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.loginToUI("admin", "admin");

        ISMyProfile.updateDefaultProfile("admin", "admin", "DCD", "97,Galle Road", "SL", "admin@yahoo.com", "145678", "0773426177", "admin@t.com", "https://admin.com");
        ISMyProfile.deleteNewProfile("home");
        ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
        ISMyProfile.testNewProfile("home","tester","tester","DCD","97,Galle Road","SL","tester@yahoo.com","145678","0773426177","admin@t.com","https://admin.com");

        instseleniumTestBase.logOutUI();
    }

    /* Test Combination 11 : When the other profile is checked, mandatory fields should have data from own profile. Optionals should be blank.*/
    public void testCombination11() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       Thread.sleep(3000);
       instProfileManagement.testSetAllFields_Behavior("default","Inherited","Overridden");
       Thread.sleep(3000);
       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home","tester","tester","","","","tester@yahoo.com","","","","");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 12 : When the other profile is checked, all fields should have data from own profile.*/
    public void testCombination12() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("default","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
       ISMyProfile.testNewProfile("home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 13 : When the other profile is checked, it will have only the mandatory fields filled with its own data.*/
    public void testCombination13() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       instProfileManagement.testSetMandatoryFields_Behavior("default","Overridden");
       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home","tester","tester","","","","tester@yahoo.com","","","","");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 14 : When the other profile is checked, it will have only the mandatory fields filled with its own data.*/
    public void testCombination14() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ISMyProfile.updateDefaultProfile("admin", "admin", "", "", "", "admin@yahoo.com", "", "", "", "");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("default","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home","tester","tester","","","","tester@yahoo.com","","","","");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 15 : When the other profile is checked, it will have all fields filled with its own data.*/
    public void testCombination15() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("default","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
       ISMyProfile.testNewProfile("home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 16 : When checked the empty fields of the second profile should have values from the default profile.*/
    public void testCombination16() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       instProfileManagement.testSetAllFields_Behavior("default","Inherited","Inherited");
       instProfileManagement.testAddNewProfile_Configuration("test");
       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("test","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home","tester","tester","ABC","85, testers zone","SL","tester@yahoo.com","123456","777987676","admin@t.com","https://admin.com");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 18 : When the other profile is checked, all of its fields should appear with data.*/
    public void testCombination18() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ISMyProfile.updateDefaultProfile("admin", "admin", "", "", "", "admin@yahoo.com", "", "", "", "");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("test","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
       ISMyProfile.testNewProfile("home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 19 : When the other profile is checked, only mandatory fields should appear without data.*/
    public void testCombination19() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       instProfileManagement.testSetMandatoryFields_Behavior("test","Hidden");

       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("test","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
       ISMyProfile.testNewProfile("home", "", "", "ABC", "85, borupana road, ratmalana", "SL", "", "43434343", "77799999", "tester@t.com", "https://tester.com");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 20 : When the other profile is checked, all of its fields should appear without data */
    public void testCombination20() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       instProfileManagement.testSetAllFields_Behavior("test","Hidden","Hidden");

       ISMyProfile.updateDefaultProfile("admin", "admin", "", "", "", "admin@yahoo.com", "", "", "", "");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("test","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home", "", "", "", "", "", "", "", "", "", "");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 21 : When the other profile is checked, mandatory fields should have data from own profile. Optionals should be blank.*/
    public void testCombination21() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       instProfileManagement.testSetAllFields_Behavior("test","Inherited","Overridden");

       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("test","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home","tester","tester","","","","tester@yahoo.com","","","","");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 22 : When the other profile is checked, it will have only the mandatory fields filled with its own data.*/
    public void testCombination22() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       instProfileManagement.testSetMandatoryFields_Behavior("test","Overridden");

       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("test","home","tester","tester","","","","tester@yahoo.com","","","","");
       ISMyProfile.testNewProfile("home","tester","tester","","","","tester@yahoo.com","","","","");

       instseleniumTestBase.logOutUI();
    }

    /* Test Combination 23 : When the other profile is checked, it will have all fields filled with its own data.*/
    public void testCombination23() throws Exception{
       SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
       instseleniumTestBase.loginToUI("admin", "admin");

       ProfileManagement instProfileManagement = new ProfileManagement(selenium);
       ISMyProfile.updateDefaultProfile("admin", "admin", "ABC", "85, testers zone", "SL", "admin@yahoo.com", "123456", "777987676", "admin@t.com", "https://admin.com");
       ISMyProfile.deleteNewProfile("home");
       ISMyProfile.addNewProfile("test","home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");
       ISMyProfile.testNewProfile("home", "tester", "tester", "ABC", "85, borupana road, ratmalana", "SL", "tester@yahoo.com", "43434343", "77799999", "tester@t.com", "https://tester.com");

       ISMyProfile.deleteNewProfile("home");
       instProfileManagement.testDeleteNewProfile_Configuration("test");

       instseleniumTestBase.logOutUI();
    }

}