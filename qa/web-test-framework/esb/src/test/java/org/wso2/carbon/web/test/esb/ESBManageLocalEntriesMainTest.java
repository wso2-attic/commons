package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

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

public class ESBManageLocalEntriesMainTest  extends CommonSetup{
    Properties properties = new Properties();

    public ESBManageLocalEntriesMainTest(String text) {
        super(text);
    }

   /*
    Method to log into the system
     */
    public void testLogin() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean login = selenium.isTextPresent("Sign-out");

        if (login) {
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin", "admin");
    }

    /*
    Verifying adding of source URL entries
     */
    public void testAddSourceUrlEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        //Available options are Add Source URL Entry, Add In-lined XML Entry, Add In-lined Text Entry
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("source_url","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/script/stockquoteTransform.js");
    }

    /*
    Verifying adding of Inline XML entries
     */
    public void testAddInlineXmlEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Available options are Add Source URL Entry, Add In-lined XML Entry, Add In-lined Text Entry
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addInlineXmlEntry("inline_xml","<m0:getQuote xmlns:m0=\"http://services.samples/xsd\"><m0:request><m0:symbol>IBM</m0:symbol></m0:request></m0:getQuote>");
    }


    /*
    Verifying adding of Inline Text entries
     */
    public void testAddInlineTextEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Available options are Add Source URL Entry, Add In-lined XML Entry, Add In-lined Text Entry
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addInlineTextEntry("inline_text","0.1");
    }

     /*
    Check for the error message of existing entry addition
      */
    public void testAddExistingLocalEntryNameAndValue() throws Exception {
         ESBCommon esbCommon = new ESBCommon(selenium);
         ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
         esbManageLocalEntriesTest.addExistingEntry("source_url", "source_url", "file:" + esbCommon.getCarbonHome() + "/repository/samples/resources/script/stockquoteTransform.js");
         esbManageLocalEntriesTest.addExistingEntry("inline_xml", "inline_xml", "<m0:getQuote xmlns:m0=\"http://services.samples/xsd\"><m0:request><m0:symbol>IBM</m0:symbol></m0:request></m0:getQuote>");
         esbManageLocalEntriesTest.addExistingEntry("inline_text", "inline_text", "0.1");
      }

     public void testAddExistingLocalEntryNameAndDifferentValue() throws Exception {
         ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
         esbManageLocalEntriesTest.addExistingEntry("source_url", "source_url","/repository/samples/resources");
         esbManageLocalEntriesTest.addExistingEntry("inline_xml", "inline_xml", "<e/>");
         esbManageLocalEntriesTest.addExistingEntry("inline_text", "inline_text", "abc");
      }

    /*
    Verifying whether the Added local entries are available
     */
    public void testVerifyAddedLocalEntry() throws Exception{
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.verifyAddedLocalEntry("source_url","Source URL");
        esbManageLocalEntriesTest.verifyAddedLocalEntry("inline_xml","Inline XML");
        esbManageLocalEntriesTest.verifyAddedLocalEntry("inline_text","Inline Text");
    }

    /*
    Verifying whether the added values are acturate
     */
    public void testVerifyLocalEntryVal() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.verifySourceInlineTextValues("source_url","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/script/stockquoteTransform.js");
        esbManageLocalEntriesTest.verifySourceInlineTextValues("inline_text","0.1");
        esbManageLocalEntriesTest.verifyInlineXmlValues("inline_xml","<m0:getQuote xmlns:m0=\"http://services.samples/xsd\"><m0:request><m0:symbol>IBM</m0:symbol></m0:request></m0:getQuote>");
    }

    /*
    Editing Source URL Entries
     */
    public void testEditSourceUrlEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.editSourceurlInlinetextEntry("source_url","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/script/stockquoteTransform.rb");
        esbManageLocalEntriesTest.editSourceurlInlinetextEntry("inline_text","0.1234");
        esbManageLocalEntriesTest.editInlineXmlEntry("inline_xml","<m0:getQuote xmlns:m0=\"http://services.samples/xsd\"><m0:request></m0:request></m0:getQuote>");
    }

    /*
    Verifying whether the added values are acturate
     */
    public void testVerifyLocalEntryValAfterEdit() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.verifySourceInlineTextValues("source_url","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/script/stockquoteTransform.rb");
        esbManageLocalEntriesTest.verifySourceInlineTextValues("inline_text","0.1234");
        esbManageLocalEntriesTest.verifyInlineXmlValues("inline_xml","<m0:getQuote xmlns:m0=\"http://services.samples/xsd\"><m0:request /></m0:getQuote>");
    }

    //    /*
//    Check for the error message of existing entry addition after editting the entry
//     */
//      public void testAddExistingLocalEntryNameAfterEditting() throws Exception {
//          testAddSourceUrlEntry();
//          testAddInlineXmlEntry();
//          testAddInlineTextEntry();
//      }

    /*
    Chech for the error message when the value is not specified
     */
    public void testAddEntryWithoutValue() throws Exception {
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addEntryWithoutValue("source_url", "source_url_no_value");
        esbManageLocalEntriesTest.addEntryWithoutValue("inline_text", "inline_text_no_value");
        esbManageLocalEntriesTest.addEntryWithoutValue("inline_xml", "inline_xml_no_value");
    }

    /*
    Deleting the Local Entries
     */
    public void testDeleteLocalEntry() throws Exception{
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.deleteLocalEntry("source_url");
        esbManageLocalEntriesTest.deleteLocalEntry("inline_text");
        esbManageLocalEntriesTest.deleteLocalEntry("inline_xml");        
    }

    /*
    Verify whether the entry has been deleted
     */
    public void testVerifyDeleted() throws Exception{
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.verifyLocalEntryDeletion("source_url");
        esbManageLocalEntriesTest.verifyLocalEntryDeletion("inline_text");
        esbManageLocalEntriesTest.verifyLocalEntryDeletion("inline_xml");        
    }

    /*
    Method to logout from the Management Console
     */
    public void testLogout() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        seleniumTestBase.logOutUI();
    }
}

