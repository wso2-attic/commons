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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;




import java.awt.event.KeyEvent;
import java.util.Properties;
import java.io.FileInputStream;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.ModuleManagement;
import org.wso2.carbon.web.test.common.Tryit;
import java.util.*;
import java.text.*;


public class MSCommon extends CommonSetup {

    public MSCommon(String text) {
        super(text);
    }


    // Loading the property file.
    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
        return properties;
    }

   /*
    This is a common part for below two methods.(For testDelete method and testCreateNewService method )
    */
   public static void testSearchAndDelete(String serviceName) throws Exception {
        int i = 1,k=1;
        boolean next=selenium.isTextPresent("next >");
        boolean search=true;

        while(next){
               selenium.click("link=next >");
               Thread.sleep(6000);
               next = selenium.isTextPresent("next >");
               i=i+1;
        }
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        for(k=1;k<=i;k++){
           int j=1,x=1;
           while(j<k){
              selenium.click("link=next >");
               j=j+1;
           }
           selenium.type("serviceGroupSearchString", serviceName);
		   selenium.click("//a[@onclick='javascript:searchServices(); return false;']");
		   selenium.waitForPageToLoad("30000");
           search = selenium.isTextPresent("No Deployed Services Found");
           if(search){
             selenium.click("link=List");
             selenium.waitForPageToLoad("30000");
           }
           else{
             selenium.click("serviceGroups");
		     selenium.click("delete1");
		     selenium.click("//button[@type='button']");
		     selenium.waitForPageToLoad("30000");
		     selenium.click("//button[@type='button']");
             k=i+1;
           }
        }
   }

    //Login to admin console and check the environment
    public static void testDelete(String serviceName) throws Exception {

        //Delete any existing services with the name "automation"
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        selenium.refresh();
        Thread.sleep(10000);
        selenium.refresh();
        Thread.sleep(10000);
        testSearchAndDelete(serviceName);
    }

    /*
    Create a new .js service
     */
    public static void testCreateNewService(String serviceName) throws Exception {
        String expectedText = "<h4>Service cannot be found. Cannot display <em>API Documentation</em>.</h4>";
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
      //delete required service name,if it is available in the list.After Create that particular service again.
        testSearchAndDelete(serviceName);
        selenium.click("link=Create");
        selenium.waitForPageToLoad("30000");
        selenium.type("serviceName", serviceName);
        selenium.click("next");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Editing the mashup " + serviceName));
        //Test Custom UI Code
        selenium.clickAt("tab2","");
        selenium.click("//input[@value='Generate Template']");
        selenium.click("//input[@value='Apply changes' and @type='button' and @onclick=\"saveSource(ui_code_text.getCode(), 'customUICodeForm', 'html', 'apply');\"]");
		selenium.click("//button[@type='button']");
        //Test Gadget UI Code
        selenium.clickAt("tab3","");
        selenium.click("//input[@value='Generate Template' and @type='button' and @onclick=\"generateNewUi('gadget');\"]");
		selenium.click("//input[@value='Apply changes' and @type='button' and @onclick=\"saveSource(gadget_code_text.getCode(), 'gadgetUICodeForm', 'gadget', 'apply');\"]");
		selenium.click("//button[@type='button']");
        selenium.clickAt("tab1","");
        selenium.click("//input[@value='Save changes']");
        selenium.waitForPageToLoad("30000");
        selenium.refresh();
        Thread.sleep(10000);
        selenium.refresh();
        Thread.sleep(10000);
        selenium.click("link="+serviceName);
		selenium.waitForPageToLoad("30000");
		selenium.click("link="+serviceName);
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Edit Mashup");
		selenium.waitForPageToLoad("30000");
        //Assert text editor contents
        selenium.clickAt("tab2","");
        assertTrue(selenium.getValue("ui_code_text_cp").indexOf(expectedText)==0);
        selenium.clickAt("tab3","");
        assertTrue(selenium.getValue("gadget_code_text_cp").indexOf(expectedText)==0);
    }

    /*
        Search faulty service.
    */
    public static void testFaultyService() throws Exception{
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Faulty Service Group(s)."));
		selenium.click("//div[@id='workArea']/form[1]/table/tbody/tr[1]/td/nobr/u/a/font");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("Edit Faulty Service"));
		assertTrue(selenium.isElementPresent("//div[@id='workArea']/form/table/tbody/tr[1]/td[2]"));
    }

    /*
        Delete that faulty service.
     */
    public static void testDeleteFaultyService() throws Exception{
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        selenium.click("//div[@id='workArea']/form[1]/table/tbody/tr[1]/td/nobr/u/a/font");
        selenium.click("link=Select all in this page");
		selenium.click("delete1");
		assertTrue(selenium.isTextPresent("exact:Do you want to delete the selected faulty service groups?"));
		selenium.click("//button[@type='button']");
		selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("No Faulty service groups found"));
		selenium.click("link=List");
		selenium.waitForPageToLoad("30000");
    }

    //Upload a new .js service.
    public static void testServiceUpload(String ServiceName) throws Exception{
        SeleniumTestBase InstSeleniumTestBase = new SeleniumTestBase(selenium);

        File resourcePath = new File("." + File.separator + "lib" + File.separator + ServiceName + ".js.zip");

        selenium.click("link=Upload");
		selenium.waitForPageToLoad("30000");
        InstSeleniumTestBase.SetFileBrowse("jsFilename", resourcePath.getCanonicalPath());
		selenium.click("upload");
		selenium.waitForPageToLoad("30000");
		selenium.click("//button[@type='button']");
        selenium.refresh();
        Thread.sleep(10000);
        selenium.refresh();
        Thread.sleep(10000);
    }

    /*
        To access the ?tryit of the services
     */
    public static void testAccessTryit(String jsservice) throws Exception {
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        int i = 1,k=1;
        boolean next=selenium.isTextPresent("next >");
        boolean search=true;

        while(next){

               selenium.click("link=next >");
               Thread.sleep(6000);
               next = selenium.isTextPresent("next >");
               i=i+1;
        }
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        for(k=1;k<=i;k++){
           int j=1,x=1;
           while(j<k){
              selenium.click("link=next >");
               j=j+1;
           }
           selenium.type("serviceGroupSearchString", jsservice);
		   selenium.click("//a[@onclick='javascript:searchServices(); return false;']");
		   selenium.waitForPageToLoad("30000");
           search = selenium.isTextPresent("No Deployed Services Found");
           if(search){
             selenium.click("link=List");
             selenium.waitForPageToLoad("30000");
           }
           else{
             selenium.click("link=Try this service");
             String tryitwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
             selenium.selectWindow(tryitwinid);
             Thread.sleep(10000);
             k=i+1;
           }
        }
    }

    //To access the ?tryit of the new service that previously uploaded.And check wsdl address lacations.
    public static void testNewService_Upload(String newService) throws Exception{
          testServiceUpload(newService);
          testAccessTryit(newService);
          selenium.click("button_toString");
          assertTrue(selenium.isTextPresent("Hi, my name is newService_upload"));
          selenium.close();
          selenium.selectWindow("");
          MSSamplesServices.testWSDL_Link(newService,loadProperties().getProperty("host.name"),loadProperties().getProperty("http.port"),"wsdl","wsdl2");
    }

    //Delete the faulty service that previously uploaded.
    public static void testFaultyService_Upload(String faultyService) throws Exception{
          testServiceUpload(faultyService);
          selenium.click("//div[@id='workArea']/form[1]/table/tbody/tr[1]/td/nobr/u/a/font");
          selenium.waitForPageToLoad("30000");
          assertTrue(selenium.isTextPresent("Edit Faulty Service"));
          assertTrue(selenium.isTextPresent(faultyService));
    }

    public static void testCustom_UI_Gadget_UICode_ForService(String ServiceName) throws Exception{
          String unexpectedText = "<h4>Service cannot be found. Cannot display <em>API Documentation</em>.</h4>";
          selenium.click("link=List");
          selenium.waitForPageToLoad("30000");
          selenium.click("link="+ServiceName);
		  selenium.waitForPageToLoad("30000");
		  selenium.click("link="+ServiceName);
		  selenium.waitForPageToLoad("30000");
		  selenium.click("link=Edit Mashup");
		  selenium.waitForPageToLoad("30000");

          selenium.clickAt("tab2","");
          selenium.click("//input[@value='Generate Template']");
          Thread.sleep(1000);
          selenium.click("//input[@value='Save changes' and @type='button' and @onclick=\"saveSource(ui_code_text.getCode(), 'customUICodeForm', 'html', 'save');\"]");
		  selenium.waitForPageToLoad("30000");
          selenium.click("link="+ServiceName);
		  selenium.waitForPageToLoad("30000");
		  selenium.click("link="+ServiceName);
		  selenium.waitForPageToLoad("30000");
		  selenium.click("link=Edit Mashup");
		  selenium.waitForPageToLoad("30000");
          selenium.clickAt("tab2","");
          assertTrue(selenium.getValue("ui_code_text_cp").indexOf(unexpectedText)==-1);
          
          selenium.clickAt("tab3","");
		  selenium.click("//input[@value='Generate Template' and @type='button' and @onclick=\"generateNewUi('gadget');\"]");
          Thread.sleep(1000);
          selenium.click("//input[@value='Save changes' and @type='button' and @onclick=\"saveSource(gadget_code_text.getCode(), 'gadgetUICodeForm', 'gadget', 'save');\"]");
		  selenium.waitForPageToLoad("30000");
          selenium.click("link="+ServiceName);
		  selenium.waitForPageToLoad("30000");
		  selenium.click("link="+ServiceName);
		  selenium.waitForPageToLoad("30000");
		  selenium.click("link=Edit Mashup");
		  selenium.waitForPageToLoad("30000");
          selenium.clickAt("tab3","");
          assertTrue(selenium.getValue("gadget_code_text_cp").indexOf(unexpectedText)==-1);
     }

    /*
        Specific Configuration
     */
       public static void testSpecificConfigurationLink(String serviceName) throws Exception {

            ServiceManagement serviceManagement = new ServiceManagement(selenium);
            serviceManagement.accessServiceDashboard(serviceName);

            assertEquals("API Documentation", selenium.getText("link=API Documentation"));
		    assertEquals("Source Code", selenium.getText("link=Source Code"));
		    assertEquals("Source code template [HTML interface]", selenium.getText("link=Source code template [HTML interface]"));
		    assertEquals("Source code template [Gadget interface]", selenium.getText("link=Source code template [Gadget interface]"));
		    assertEquals("XML Schema", selenium.getText("link=XML Schema"));
		    assertEquals("Javascript (DOM) stub", selenium.getText("link=Javascript (DOM) stub"));
		    assertEquals("Javascript (E4X) stub", selenium.getText("link=Javascript (E4X) stub"));
		    assertEquals("Javascript (E4X) stub [localhost endpoints]", selenium.getText("link=Javascript (E4X) stub [localhost endpoints]"));

       }


    /*
        API Documentation
     */
      public static void testAPIDocumentation(String serviceName,String IP,String httpPort,String httpsPort) throws Exception {

            ServiceManagement serviceManagement = new ServiceManagement(selenium);
            serviceManagement.accessServiceDashboard(serviceName);

            String expected1 = "https://"+IP+":"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?doc";
            String expected2 = "https://localhost:"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?doc";

            selenium.click("link=API Documentation");
            Thread.sleep(5000);
            String signinwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
            selenium.selectWindow(signinwinid);
            String actual1 = selenium.getLocation();

            System.out.println(actual1);
            System.out.println(expected1);

            if ((actual1.equals(expected1))||(actual1.equals(expected2))) {
                System.out.println("Test1 passed");
            } else {
                System.out.println("Test1 failed");
            }

            selenium.click("insecure-tryit");
		    selenium.waitForPageToLoad("30000");
            String actual1_1 = selenium.getLocation();
            System.out.println(actual1_1);
            if ((actual1_1.equals("http://"+IP+":"+httpPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?tryit"))||(actual1_1.equals("http://localhost:"+httpPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?tryit"))) {
                System.out.println("Test1_1 passed");
            } else {
                System.out.println("Test1_1 failed");
            }
            selenium.goBack();

		    selenium.click("secure-tryit");
		    selenium.waitForPageToLoad("30000");
            String actual1_2 = selenium.getLocation();
            System.out.println(actual1_2);
            if ((actual1_2.equals("https://"+IP+":"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?tryit"))||(actual1_2.equals("https://localhost:"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?tryit"))) {
                System.out.println("Test1_2 passed");
            } else {
                System.out.println("Test1_2 failed");
            }
            selenium.goBack();

		    selenium.click("link=Source Code");
		    selenium.waitForPageToLoad("30000");
            String actual1_3 = selenium.getLocation();
            System.out.println(actual1_3);
            if ((actual1_3.equals("https://"+IP+":"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?source&content-type=text/plain"))||(actual1_3.equals("https://localhost:"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?source&content-type=text/plain"))) {
                System.out.println("Test1_3 passed");
            } else {
                System.out.println("Test1_3 failed");
            }
            selenium.goBack();

		    selenium.click("link=Javascript (DOM) stub");
		    selenium.waitForPageToLoad("30000");
            String actual1_4 = selenium.getLocation();
            System.out.println(actual1_4);
            if ((actual1_4.equals("https://"+IP+":"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?stub&content-type=text/plain"))||(actual1_4.equals("https://localhost:"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?stub&content-type=text/plain"))) {
            System.out.println("Test1_4 passed");
            } else {
            System.out.println("Test1_4 failed");
            }
            selenium.goBack();

		    selenium.click("link=Javascript (E4X) stub");
		    selenium.waitForPageToLoad("30000");
            String actual1_5 = selenium.getLocation();
            System.out.println(actual1_5);
            if ((actual1_5.equals("https://"+IP+":"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?stub&lang=e4x&content-type=text/plain"))||(actual1_5.equals("https://localhost:"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?stub&lang=e4x&content-type=text/plain"))) {
            System.out.println("Test1_5 passed");
            } else {
            System.out.println("Test1_5 failed");
            }
            selenium.goBack();

		    selenium.click("link=WSDL 2.0");
		    selenium.waitForPageToLoad("30000");
            String actual1_6 = selenium.getLocation();
            System.out.println(actual1_6);
            if ((actual1_6.equals("https://"+IP+":"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?wsdl2"))||(actual1_6.equals("https://localhost:"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?wsdl2"))) {
            System.out.println("Test1_6 passed");
            } else {
            System.out.println("Test1_6 failed");
            }
            selenium.goBack();

		    selenium.click("link=WSDL 1.1");
		    selenium.waitForPageToLoad("30000");
            String actual1_7 = selenium.getLocation();
            System.out.println(actual1_7);
            if ((actual1_7.equals("https://"+IP+":"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?wsdl"))||(actual1_7.equals("https://localhost:"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?wsdl"))) {
            System.out.println("Test1_7 passed");
            } else {
            System.out.println("Test1_7 failed");
            }
            selenium.goBack();
           
		    selenium.click("link=XML Schema");
		    selenium.waitForPageToLoad("30000");
            String actual1_8 = selenium.getLocation();
            System.out.println(actual1_8);
            if ((actual1_8.equals("https://"+IP+":"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?xsd"))||(actual1_8.equals("https://localhost:"+httpsPort+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+"?xsd"))) {
            System.out.println("Test1_8 passed");
            } else {
            System.out.println("Test1_8 failed");
            }
            selenium.goBack();

            selenium.close();
            selenium.selectWindow("");
       }

        /*
         Configurations
     */
       public static void testConfiguration(String serviceName,String linkName,String IP,String port,String finalpart) throws Exception {
            ServiceManagement serviceManagement = new ServiceManagement(selenium);
            serviceManagement.accessServiceDashboard(serviceName);

            String expected ="https://"+IP+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+serviceName+finalpart;

            selenium.click("link="+linkName);
            Thread.sleep(5000);
            String signinwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
            selenium.selectWindow(signinwinid);
            String actual = selenium.getLocation();

            System.out.println(actual);
            System.out.println(expected);

            if (expected.equals(actual)) {
                System.out.println("Test passed");
            } else {
                System.out.println("Test failed");
            }

            selenium.close();
            selenium.selectWindow("");
        }

    /*
       Sign-in to hotmail account(Sign -in and goto inbox) 
     */
    public static void testLogginToAccounts(String userName,String password) throws Exception{
        String signinwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.openWindow("http://login.live.com/login.srf?wa=wsignin1.0&rpsnv=11&ct=1251269841&rver=5.5.4177.0&wp=MBI&wreply=http:%2F%2Fmail.live.com%2Fdefault.aspx&lc=1033&id=64855&mkt=en-US",signinwinid);
		selenium.selectWindow(signinwinid);
        selenium.waitForPageToLoad("30000");
        if(selenium.isTextPresent("Click a Windows Live ID to sign in")) {
           selenium.click("i1668");
        }
        selenium.type("i0116", userName);
		selenium.type("i0118", password);
        
		selenium.click("idSIButton9");
        selenium.click("idSIButton9");
		selenium.waitForPageToLoad("60000");
		Thread.sleep(40000);
		selenium.click("//li[@id='00000000-0000-0000-0000-000000000001']/a/span/span");  //
		selenium.waitForPageToLoad("30000");
		
    }

    //For Module Management
    public static void checkAccessDenyAtGlobalThottling_OfModule(String inputString) throws Exception{
         ModuleManagement moduleManagement=new ModuleManagement(selenium);
         selenium.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
		 selenium.waitForPageToLoad("30000");
         assertTrue(selenium.isTextPresent("Global Throttling Configuration "));

         if ((selenium.getSelectedValue("enableThrottle")).equals("Yes")) {
            selenium.select("enableThrottle", "label=No");
         }
         selenium.select("enableThrottle", "label=Yes");
         //browser.click("//table[@id='dataTable']/tbody/tr[2]/td[7]/a");
         selenium.select("data15", "label=Deny");
		 selenium.click("//input[@value='Finish']");
		 selenium.waitForPageToLoad("30000");
         assertTrue(selenium.isTextPresent("Successfully applied throttling configuration"));
		 selenium.click("//button[@type='button']");
         Thread.sleep(1000);
         MSDataBinding.testallCommons_Service(inputString);

         moduleManagement.accessModuleIndexPage();
         selenium.click("//a[@onclick=\"submitHiddenForm('../throttling/index.jsp');return false;\"]");
		 selenium.waitForPageToLoad("30000");
		 selenium.select("enableThrottle", "label=No");
         assertTrue(selenium.isTextPresent("This will disengage throttling. Click Yes to confirm"));
		 selenium.click("//button[@type='button']");
         selenium.waitForPageToLoad("30000");
		 selenium.click("//button[@type='button']");
    }

    public static void DateToGMT(String stringtoAssert) throws Exception {
        final int msInMin = 60000;
        final int minInHr = 60;
        Date date = new Date();
        int Hours, Minutes;
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG );
        TimeZone zone = dateFormat.getTimeZone();
        Minutes =zone.getOffset( date.getTime() ) / msInMin;
        System.out.println("min: "+Minutes);
        Hours = Minutes / minInHr;
        System.out.println("Hours: "+Hours);
        Minutes=360-Minutes;
        String tmp;
        if(Minutes<10&&Hours<10)
        	tmp="0"+Hours+"0"+Minutes;
        else if(Minutes<10&&Hours>10)
        	tmp=Hours+"0"+Minutes;
        else if(Minutes>10&&Hours<10)
        	tmp="0"+Hours+Minutes;
        else
                tmp=Hours+Minutes+"";

        String dt=stringtoAssert;
        String sdt1=dt.substring(0,10);
        String sdt2=dt.substring(11,19);
        String sdt3=dt.substring(0,4);
        String contdt=sdt1+" "+sdt2;
        java.text.SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        format.setCalendar(cal);
        java.util.Date date1 = format.parse(contdt);
        System.out.println(date1);
        String s=date1+"";
        System.out.println("Today is " + s);
        int i=0;
        int count=0;
        String last="";
        while(count<=2){
                last=last+s.charAt(i);
        	if(s.charAt(i)==' ')
        		count=count+1;
        i=i+1;
        }
        last=last+""+sdt3+" "+s.substring(i,i+8)+" GMT+"+tmp;
        System.out.println(last);
    }

    //Copy files from one folder to another folder.
    public static synchronized void copy(File src, File dest) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dest);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
        }
        in.close();
        out.close();
   }

    public static void testContextRoot() throws Exception{
        String context_root=loadProperties().getProperty("context.root");
        if(!context_root.equals(""))
             Thread.sleep(3000);
    }

     /*
       Sign-out from hotmail account
     */
    public static void testlogOutFromAccounts() throws Exception {
        selenium.click("c_signout");
		selenium.waitForPageToLoad("60000");
    }





}


