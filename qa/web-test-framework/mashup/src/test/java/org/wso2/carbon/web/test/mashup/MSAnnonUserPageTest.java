package org.wso2.carbon.web.test.mashup;

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


public class MSAnnonUserPageTest extends CommonSetup{

    public MSAnnonUserPageTest(String text) {
           super(text);
    }

    //Test service list of annon user.
    public void testAnnonUserList() throws Exception{
       MSAnnonUserPage.annonUser_Services("admin/allCommons");

        //Test wsdls.
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/digit2image",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/exchangeRate",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/feedCache",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
     //  MSAnnonUserPage.AnnonUser_WSDL_Links("system/formulaFlicks",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/RESTSample",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/storexml",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/sudoku",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/tomatoTube",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/TwitterMap",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/upgradeChecker",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/version",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
       MSAnnonUserPage.AnnonUser_WSDL_Links("system/yahooGeoCode",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.be.port"),"wsdl","wsdl2");
    }


    //Test scraping assistant functionality of the annon user.
    public void testAnnonUserScrapingAssistant() throws Exception{
       ScrapingAssistant.testScrapingAssistantURL();
       ScrapingAssistant.testScraperConfiguration();
    }


    //Test Java scriptstub generator functionality of the annon user.
    public void testAnnonUserJavaScriptStubGenerator() throws Exception{
       JavaScriptStubGenerator.testJavaScriptStubGenerator();
    }


    //Go to Home page.
    public void testGoToHome() throws Exception{
        selenium.click("link=Sign-in");
		selenium.waitForPageToLoad("30000");
    }
}
