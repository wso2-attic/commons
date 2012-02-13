package org.wso2.carbon.web.test.wsas;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import org.wso2.carbon.web.test.common.Tryit;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.ds.common.*;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.net.ssl.*;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.security.Security;

import sun.net.www.http.HttpClient;
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

public class ToolsTest extends TestCase {
    Selenium browser;
    Properties property;
    String username;
    String password;
    String hostname;
    String httpport;
    String contextroot;

    public void setUp() throws Exception {
        property = BrowserInitializer.loadProperties();
        browser = BrowserInitializer.getBrowser();
        username = property.getProperty("admin.username");
        password = property.getProperty("admin.password");
        hostname = property.getProperty("host.name");
        httpport = property.getProperty("http.be.port");
        contextroot = property.getProperty("context.root");
    }

    public ToolsTest(String s){
        super(s);
    }


    public void testPojo_upload() throws Exception {
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        InstSeleniumTestBase.loginToUI(username, password);


        File pojoPath = new File("." + File.separator + "lib" + File.separator + "PojoService.class");
        String ServiceName = "PojoService";

        //browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        browser.click("link=POJO Service");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("jarZipFilename", pojoPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Files have been uploaded successfully. Please refresh this page in a while to see the status of the created POJO service"));

        browser.click("//button[@type='button']");
        Thread.sleep(12000);
        browser.click("link=List");
        browser.waitForPageToLoad("120000");
        assertTrue(browser.isTextPresent(ServiceName));;
    }

    public void testAbout()throws Exception
    {
        // browser.open("/carbon/service-mgt/index.jsp?region=region1&item=services_list_menu");
        Thread.sleep(10000);
        browser.click("link=About");
        String tryitwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        browser.selectWindow(tryitwinid);
        assertTrue(browser.isTextPresent("Version 2.0"));
        browser.close();
        browser.selectWindow("");
    }

    public void testLogout1()throws Exception
    {
        SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(browser);
        inSeleniumTestBase.logOutUI();
    }

    public void testInvokeExternalTryit()throws Exception
    {

        Tryit instTryit = new Tryit(browser);
        instTryit.invokeTryit("external","PojoService", "echoString","s","Hello",4, "Hello");

    }

    public void testInvokeExternalTryitfromURL()throws Exception
    {

        Tryit instTryit = new Tryit(browser);
        String URL="http://" + hostname + ":" + httpport + contextroot + "/services/PojoService?wsdl";
        instTryit.invokeTryItByURL("PojoService", "echoString","s","Hello",4, "Hello",URL);

    }


    public void testLoginToolsTest()throws Exception
    {
        SeleniumTestBase instSeleniumTestBase = new SeleniumTestBase(browser);
        instSeleniumTestBase.loginToUI(username, password);
    }
    //
    public void testInvokeInternalTryitfromURL()throws Exception
    {   //http://localhost:9763/services/PojoService?wsdl

        Tryit instTryit = new Tryit(browser);
        String URL="http://" + hostname + ":" + httpport + contextroot + "/services/PojoService?wsdl";
        instTryit.invokeTryItByURL("PojoService", "echoString","s","Hello",4, "Hello",URL);

    }

    public void testWSDLConverter() throws Exception{
        ServiceManagement instServiceManagement = new ServiceManagement(browser);
        String  assertString="description";

        instServiceManagement.Login();

        browser.click("link=WSDL Converter");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("WSDL Converter"));
        assertEquals("Enter WSDL Document URL", browser.getText("//div[@id='workArea']/table/thead/tr/th"));
        browser.type("wsdlURL", "http://www50.brinkster.com/vbfacileinpt/np.asmx?wsdl");
        browser.click("convertWSDLUsingURL");
        browser.waitForPopUp("", "30000");

        //get the new window id
        String tryitwinid = browser.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        Thread.sleep(1000);
        browser.selectWindow(tryitwinid);
        System.out.println(browser.getLocation());
        String strURL = browser.getLocation().toString();
        //close the window
        browser.close();
        browser.selectWindow("");

        //getting the text in from https
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs,String authType) {
            }
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs,String authType) {
            }
        }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


        URL url=new URL(strURL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        StringBuffer sb=new StringBuffer();
        String data="";
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        InputStream is=connection.getInputStream();

        int ch;
        while((ch=is.read()) != -1){
            sb.append((char)ch);
        }

        data=sb.toString();
        System.out.println(data);
        if(data.contains(assertString)){
            System.out.println("Element found");
        }else{
            System.out.println("Element not found");
            is.close();

            assertTrue(browser.isTextPresent("Element found"));
        }
        is.close();



    }

    public void testServiceValidatorWithAAR() throws Exception{
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        File aarPath = new File("." + File.separator + "lib" + File.separator + "Axis2Service.aar");

        instServiceManagement.Login();

        browser.click("link=Service Validator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Service Archive Validator"));
        InstSeleniumTestBase.SetFileBrowse("serviceArchiveFile",aarPath.getCanonicalPath());
        browser.click("upload");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Service Archive Validation Report"));
        assertTrue(browser.isTextPresent("There are no errors in your services.xml"));
        browser.click("//input[@value=' Finish ']");
        browser.waitForPageToLoad("30000");

    }

    public void testServiceValidatorWithXML() throws Exception{
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        File xmlPath = new File("." + File.separator + "lib" + File.separator + "services.xml");

        instServiceManagement.Login();

        browser.click("link=Service Validator");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Service Archive Validator"));
        InstSeleniumTestBase.SetFileBrowse("serviceXMLFile",xmlPath.getCanonicalPath());
        browser.click("//input[@value='Validate services.xml']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Service Archive Validation Report"));

        if(browser.isTextPresent("There are no errors in your services.xml") || browser.isElementPresent("messagebox-error")){
              if(browser.isTextPresent("The XML descriptor file is empty or not well-formed"))
                  browser.click("//button[@type='button']");
        }else{
              assertTrue("Service validation fails",false);
        }

        browser.click("//input[@value=' Finish ']");
        browser.waitForPageToLoad("30000");

    }

    public void testModuleValidatorWithMar() throws Exception{
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        File marPath = new File("." + File.separator + "lib" + File.separator + "counter-module-SNAPSHOT.mar");

        instServiceManagement.Login();

        browser.click("link=Module Validator");
        browser.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("moduleArchiveFile",marPath.getCanonicalPath());
        assertTrue(browser.isTextPresent("Module Archive Validator"));
        browser.click("//input[@value='Validate MAR']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module Archive Validation Report"));
        assertTrue(browser.isTextPresent("There are no errors in your module.xml"));
        browser.click("//input[@value=' Finish ']");
        browser.waitForPageToLoad("30000");
    }

    public void testModuleValidatorWithXML() throws Exception{
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(browser);
        ServiceManagement instServiceManagement = new ServiceManagement(browser);

        File xmlPath = new File("." + File.separator + "lib" + File.separator + "module.xml");

        instServiceManagement.Login();

        browser.click("link=Module Validator");
        browser.waitForPageToLoad("30000");
        browser.type("moduleXMLFile", "/home/jayani/Desktop/2.0.2/wsas/lib/module.xml");
        InstSeleniumTestBase.SetFileBrowse("moduleXMLFile",xmlPath.getCanonicalPath());
        assertTrue(browser.isTextPresent("Module Archive Validator"));
        browser.click("//input[@value='Validate module.xml']");
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent("Module Archive Validation Report"));

        if(browser.isTextPresent("There are no errors in your module.xml") || browser.isElementPresent("messagebox-error")){
              if(browser.isTextPresent("The XML descriptor file is empty or not well-formed"))
                  browser.click("//button[@type='button']");
        }else{
              assertTrue("Service validation fails",false);
        }
       
        browser.click("//input[@value=' Finish ']");
        browser.waitForPageToLoad("30000");
    }


    public void testLogoutToolsTest()throws Exception
    {
        SeleniumTestBase inSeleniumTestBase = new SeleniumTestBase(browser);
        inSeleniumTestBase.logOutUI();
    }

}
