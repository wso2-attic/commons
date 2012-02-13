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

public class MSSamplesServicesTest extends CommonSetup {

    public MSSamplesServicesTest(String text) {
        super(text);
    }


    /*
        *  Sign-in to Mashup Server admin console
     */
    public void testSignIn() throws Exception {
        SeleniumTestBase myseleniumTestBase = new SeleniumTestBase(selenium);
        myseleniumTestBase.loginToUI("admin", "admin");
    }


    /*
            wsdl
     */
    public void testWSDL() throws Exception {      //use the properties file

       MSSamplesServices.testWSDL_Link("digit2image",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("exchangeRate",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("feedCache",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("formulaFlicks",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("RESTSample",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("storexml",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("sudoku",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("tomatoTube",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("TwitterMap",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("upgradeChecker",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("version",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
       MSSamplesServices.testWSDL_Link("yahooGeoCode",MSCommon.loadProperties().getProperty("host.ip"),MSCommon.loadProperties().getProperty("http.port"),"wsdl","wsdl2");
    }


     /*
           Testing digit2image service.
     */

    public void testdigit2image() throws Exception {

            MSCommon.testAccessTryit("digit2image");
            Thread.sleep(10000);
            MSSamplesServices.testdigit2image_Service();

            selenium.close();
            selenium.selectWindow("");
    }

     /*
           Testing exchangeRate service.
     */

    public void testexchangeRate() throws Exception {

            MSCommon.testAccessTryit("exchangeRate");
            Thread.sleep(10000);
            MSSamplesServices.testexchangeRate_Service();

            selenium.close();
            selenium.selectWindow("");
    }

     /*
           Testing feedCache service.
     */

   public void testfeedCache() throws Exception {

            MSCommon.testAccessTryit("feedCache");
            Thread.sleep(10000);
            MSSamplesServices.testfeedCache_Service();

            selenium.close();
            selenium.selectWindow("");
   }

    /*
           Testing formulaFlicks service.
     */

   public void testformulaFlicks() throws Exception {

            MSCommon.testAccessTryit("formulaFlicks");
            Thread.sleep(10000);
            MSSamplesServices.testformulaFlicks_Service();

            selenium.close();
            selenium.selectWindow("");
   }

     /*
           Testing RESTSample service.
     */

   public void testRESTSample() throws Exception {

            MSCommon.testAccessTryit("RESTSample");
            Thread.sleep(10000);
            MSSamplesServices.testRESTSample_Service();

            selenium.close();
            selenium.selectWindow("");
   }

     /*
           Testing storexml service.
     */

   public void teststorexml() throws Exception {

            MSCommon.testAccessTryit("storexml");
            Thread.sleep(10000);
            MSSamplesServices.teststorexml_Service();

            selenium.close();
            selenium.selectWindow("");
   }

    /*
           Testing sudoku service.
     */
   public void testsudoku() throws Exception {

            MSCommon.testAccessTryit("sudoku");
            Thread.sleep(10000);
            MSSamplesServices.testsudoku_Service();

            selenium.close();
            selenium.selectWindow("");
   }

    /*
           Testing tomatoTube service.
     */
   public void testtomatoTube() throws Exception {

            MSCommon.testAccessTryit("tomatoTube");
            Thread.sleep(10000);
            MSSamplesServices.testtomatoTube_Service();

            selenium.close();
            selenium.selectWindow("");
   }

    /*
           Testing upgradeChecker service.
     */
   public void testupgradeChecker() throws Exception {

            MSCommon.testAccessTryit("upgradeChecker");
            Thread.sleep(10000);
            MSSamplesServices.testupgradeChecker_Service();

            selenium.close();
            selenium.selectWindow("");
   }

     /*
           Testing version service.
     */
   public void testversion() throws Exception {

            MSCommon.testAccessTryit("version");
            Thread.sleep(10000);
            MSSamplesServices.testversion_Service();

            selenium.close();
            selenium.selectWindow("");
   }

   /*
        Sign-out from Mashup server
     */
   public void testSignout() throws Exception {
            SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
            instseleniumTestBase.logOutUI();
    }
}

