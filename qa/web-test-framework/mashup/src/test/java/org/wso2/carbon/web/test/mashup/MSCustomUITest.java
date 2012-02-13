package org.wso2.carbon.web.test.mashup;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


public class MSCustomUITest extends CommonSetup {

    public MSCustomUITest(String text) {
        super(text);
    }

    /*
          Sign-in to Mashup Server admin console
     */
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }


    /*
          Testing Custom UI functionality of digit2image service.
     */
    public void testcustomUI_Functionality_Of_digit2image() throws Exception{
        MSCustomUI.accessCustomUIOfServices("admin/digit2image");
        Thread.sleep(10000);
        MSCustomUI.customUI_Functionality_Of_digit2image();

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing Custom UI functionality of formulaFlicks service.
     */
    public void testcustomUI_Functionality_Of_formulaFlicks() throws Exception{
        MSCustomUI.accessCustomUIOfServices("admin/formulaFlicks");
        Thread.sleep(10000);
        MSCustomUI.customUI_Functionality_Of_formulaFlicks();

        selenium.close();
        selenium.selectWindow("");

    }


    /*
          Testing Custom UI functionality of sudoku service.
     */
    public void testcustomUI_Functionality_Of_sudoku() throws Exception{
        MSCustomUI.accessCustomUIOfServices("admin/sudoku");
        Thread.sleep(10000);
        MSCustomUI.customUI_Functionality_Of_sudoku();

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing Custom UI functionality of tomatoTube service.
     */
    public void testcustomUI_Functionality_Of_tomatoTube() throws Exception{
        MSCustomUI.accessCustomUIOfServices("admin/tomatoTube");
        Thread.sleep(10000);
        MSCustomUI.customUI_Functionality_Of_tomatoTube();

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing Custom UI functionality of TwitterMap service.   ------- DONT UNCOMMENT
     */
//    public void testcustomUI_Functionality_Of_TwitterMap() throws Exception{
//        MSCustomUI.accessCustomUIOfServices("admin/TwitterMap");
//        Thread.sleep(10000);
//        MSCustomUI.customUI_Functionality_Of_TwitterMap();
//
//        selenium.close();
//        selenium.selectWindow("");
//    }


    /*
          Testing Custom UI functionality of upgradeChecker service.
     */
    public void testcustomUI_Functionality_Of_upgradeChecker() throws Exception{
        MSCustomUI.accessCustomUIOfServices("admin/upgradeChecker");
        Thread.sleep(10000);
        MSCustomUI.customUI_Functionality_Of_upgradeChecker();

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing Custom UI functionality of version service.
     */
    public void testcustomUI_Functionality_Of_version() throws Exception{
        MSCustomUI.accessCustomUIOfServices("admin/version");
        Thread.sleep(10000);
        MSCustomUI.customUI_Functionality_Of_version();

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing Custom UI functionality of yahooGeoCode service.          ------  DONT UNCOMMENT
     */
//    public void testcustomUI_Functionality_Of_yahooGeoCode() throws Exception{
//        MSCustomUI.accessCustomUIOfServices("admin/yahooGeoCode");
//        Thread.sleep(10000);
//        MSCustomUI.customUI_Functionality_Of_yahooGeoCode();
//
//        selenium.close();
//        selenium.selectWindow("");
//    }


    /*
          Sign out from Mashup server
     */
    public void testSignout() throws Exception {
        SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
        instseleniumTestBase.logOutUI();
    }
}

