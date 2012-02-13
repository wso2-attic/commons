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
import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;
import java.io.File;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;


public class JavaScriptStubGenerator extends CommonSetup {

    public JavaScriptStubGenerator(String text) {
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
          CSHelp
    */
    public void testCSHelp() throws Exception{
         String expectedForCSHelp="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.be.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/js_stub_generator/docs/userguide.html";
         selenium.click("link=JavaScript Stub Generator");
         selenium.waitForPageToLoad("30000");
         assertTrue(selenium.isTextPresent("Help"));
         selenium.click("link=Help");
         String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
         selenium.selectWindow(helpwinid);
         Thread.sleep(10000);
         assertTrue(selenium.isTextPresent("JavaScript Stub Generator")); 
         String actualForCSHelp = selenium.getLocation();
         if(actualForCSHelp.equals(expectedForCSHelp))
             System.out.println("Actual location & expected location are matched");
         else
             System.out.println("Actual location & expected location are not matched");
         selenium.close();
         selenium.selectWindow("");
    }



    /*
        JavaScript Stub Generator
     */
    public static void testJavaScriptStubGenerator() throws Exception {
        selenium.click("link=JavaScript Stub Generator");
		selenium.waitForPageToLoad("30000");
      //Test the URL
        assertTrue(selenium.isTextPresent("JavaScript Stub Generator"));
		assertTrue(selenium.isTextPresent("Upload WSDL File"));
		assertTrue(selenium.isTextPresent("Read WSDL URL"));

      //Test,without uploading WSDLfile
        selenium.click("type");
		selenium.click("doUpload");
        assertTrue(selenium.isTextPresent("Please browse your file system for a valid wsdl document"));
		selenium.click("//button[@type='button']");
        selenium.click("//input[@name='type' and @value='e4x']");
		selenium.click("doUpload");
        assertTrue(selenium.isTextPresent("Please browse your file system for a valid wsdl document"));
		selenium.click("//button[@type='button']");

      //Test,without reading WSDL URL  
        selenium.click("//form[@id='form2']/table/tbody/tr[1]/td/input");
		selenium.click("//input[@value='Generate from URL']");
        assertTrue(selenium.isTextPresent("Please enter the url of a valid wsdl document"));
		selenium.click("//button[@type='button']");
        selenium.click("//form[@id='form2']/table/tbody/tr[2]/td/input");
		selenium.click("//input[@value='Generate from URL']");
        assertTrue(selenium.isTextPresent("Please enter the url of a valid wsdl document"));
		selenium.click("//button[@type='button']");

      //Upload WSDL file
        String unexpectedvalue="Apache Tomcat/5.5.23 - Error report<!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}HR {color : #525D76;}-->";
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(selenium);
        File resourcePath = new File("." + File.separator + "lib" + File.separator + "CurrencyConvertor.wsdl" );
        InstSeleniumTestBase.SetFileBrowse("wsdl", resourcePath.getCanonicalPath());
        selenium.click("type");
		selenium.click("doUpload");
        Thread.sleep(10000);
        assertTrue(selenium.getValue("js-stub").indexOf(unexpectedvalue)==-1);
        selenium.click("//input[@name='type' and @value='e4x']");
		selenium.click("doUpload");
        Thread.sleep(10000);
        assertTrue(selenium.getValue("js-stub").indexOf(unexpectedvalue)==-1);


      //Read WSDL URL
        String unexpectedvalueForURL="<tns:CarbonException xmlns:tns=\\\"http://jsstubgenerator.carbon.wso2.org\\\"><tns:CarbonException xmlns:ns=\\\"http://jsstubgenerator.carbon.wso2.org\\\" xmlns:ax296=\\\"http://carbon.wso2.org/xsd\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\" xsi:type=\\\"ax296:CarbonException\\\" /></tns:CarbonException>";
        selenium.click("//form[@id='form2']/table/tbody/tr[1]/td/input");
		selenium.type("url", "http://www.webservicex.net/CurrencyConvertor.asmx?WSDL");
		selenium.click("//input[@value='Generate from URL']");
        Thread.sleep(15000);
        assertTrue(selenium.getValue("js-stub").indexOf(unexpectedvalueForURL)==-1);
        selenium.click("//form[@id='form2']/table/tbody/tr[2]/td/input");
		selenium.type("url", "http://www.webservicex.net/CurrencyConvertor.asmx?WSDL");
		selenium.click("//input[@value='Generate from URL']");
        Thread.sleep(15000);
        assertTrue(selenium.getValue("js-stub").indexOf(unexpectedvalueForURL)==-1);
    }

    /*
        *  Sign-out from Mashup Server
     */
    public void testSignout() throws Exception {
            SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
            instseleniumTestBase.logOutUI();
    }


}
