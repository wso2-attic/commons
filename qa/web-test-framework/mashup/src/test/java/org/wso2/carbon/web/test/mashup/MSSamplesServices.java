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

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;



public class MSSamplesServices extends CommonSetup {

    public MSSamplesServices(String text) {
            super(text);
    }

    /*
          WSDL1.1 link
     */
    public static void testWSDL_Link(String jsservice,String IP,String port,String wsdl,String wsdl2 ) throws Exception{

        String expected1 ="http://"+IP+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+jsservice+"?"+wsdl;
        String expected2 ="http://"+IP+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+jsservice+"?"+wsdl2;

        String expected3 ="http://"+MSCommon.loadProperties().getProperty("host.name")+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+jsservice+"?"+wsdl;
        String expected4 ="http://"+MSCommon.loadProperties().getProperty("host.name")+":"+port+MSCommon.loadProperties().getProperty("context.root")+"/services/"+jsservice+"?"+wsdl2;

        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");

        int i = 1,k=1;
        boolean next=selenium.isTextPresent("next >");
        boolean search=true;

        while(next){
               selenium.click("link=next >");
               Thread.sleep(5000);
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
             selenium.click("link=WSDL1.1");
             String tryitwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
             selenium.selectWindow(tryitwinid);
             String actual1 = selenium.getLocation();

             if ((expected1.equals(actual1))||(expected3.equals(actual1))) {
                System.out.println("Test passed");
             } else {
                System.out.println("Test failed");
             }
                selenium.close();
                selenium.selectWindow("");
               
		        selenium.click("link=WSDL2.0");
                String tryitwinid1 = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
                selenium.selectWindow(tryitwinid1);
                String actual2 = selenium.getLocation();

                if ((expected2.equals(actual2))||(expected4.equals(actual2))) {
                    System.out.println("Test passed");
                } else {
                    System.out.println("Test failed");
                }
                    selenium.close();
                    selenium.selectWindow("");
                    k=i+1;
             }
        }
    }


    /*
        Check "Fault" with string of asserted.
     */
    public static void testCheckString(String check) throws Exception{
        String temp="Fault:";

         if (temp.equals(check)) {
             System.out.println("assert is failed");
         } else{
             System.out.println("assert is passed");
         }
    }
    

     /*
        Accessing tryit to test digit2image service.
    */
    public static void testdigit2image_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");
        selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.click("button_digit2image");
        Thread.sleep(6000);
        String getText1=selenium.getText("console_digit2image");
        testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_digit2image");
        Thread.sleep(6000);
        String getText2=selenium.getText("console_digit2image");
        testCheckString(getText2);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_digit2image");
        Thread.sleep(6000);
		String getText3=selenium.getText("console_digit2image");
        testCheckString(getText3);
    }

    /*
        Accessing tryit to test exchangeRate service.
    */
    public static void testexchangeRate_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");
		selenium.type("input_convert_fromCurrency_0", "USD");
		selenium.type("input_convert_toCurrency_0", "ALL");
		selenium.click("button_convert");
        Thread.sleep(8000);
        String getText1=selenium.getText("console_convert");
		testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_convert");
        Thread.sleep(8000);
        String getText2=selenium.getText("console_convert");
		testCheckString(getText2);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_convert");
        Thread.sleep(8000);
		String getText3=selenium.getText("console_convert");
		testCheckString(getText3);
    }

    /*
        Accessing tryit to test feedCache service.
    */
    public static void testfeedCache_Service() throws Exception{

        selenium.click("//img[@title='Show endpoint options']");
	    selenium.click("link=feedReference");
        selenium.select("endpointSelect", "label=SOAP12Endpoint");
        selenium.type("input_feedReference_feedUrl_0", "http://www.formula1.com/rss/news/headlines.rss");
	    selenium.click("button_feedReference");
        Thread.sleep(15000);
	    String getText1=selenium.getText("console_feedReference");
        testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_feedReference");
        Thread.sleep(15000);
		String getText2=selenium.getText("console_feedReference");
        testCheckString(getText2);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_feedReference");
        Thread.sleep(15000);
	//	assertTrue(selenium.isTextPresent("exact:Fault: Invalid URI: noSlash [detail]"));

        selenium.click("link=cacheSettings");
		selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.type("input_cacheSettings_feedUrl_0", "http://www.formula1.com/rss/news/headlines.rss");
		selenium.type("input_cacheSettings_lifespan_0", "2000");
		selenium.click("input_cacheSettings_prefetch_0");
		selenium.click("button_cacheSettings");
         Thread.sleep(8000);
		String getText3=selenium.getText("console_cacheSettings");
        testCheckString(getText3);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_cacheSettings");
        Thread.sleep(8000);
		String getText4=selenium.getText("console_cacheSettings");
        testCheckString(getText4);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_cacheSettings");
        Thread.sleep(8000);
		String getText5=selenium.getText("console_cacheSettings");
        testCheckString(getText5);

		selenium.click("link=feedContent");
		selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.type("input_feedContent_feedUrl_0", "http://www.formula1.com/rss/news/headlines.rss");
		selenium.click("button_feedContent");
        Thread.sleep(8000);
        String getText6=selenium.getTable("//div[@id='params_feedContent']/table.2.1");
        testCheckString(getText6);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_feedContent");
        Thread.sleep(8000);
        String getText7=selenium.getTable("//div[@id='params_feedContent']/table.2.1");
        testCheckString(getText7);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_feedContent");
        Thread.sleep(8000);
	//	assertTrue(selenium.isTextPresent("Fault: Invalid URI: noSlash [detail]"));

    }

    /*
        Accessing tryit to test formulaFlicks service.
    */
    public static void testformulaFlicks_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");
		selenium.click("link=videoList");
        selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.click("button_videoList");
        Thread.sleep(15000);
		String getText1=selenium.getTable("//div[@id='params_videoList']/table.1.1");
        testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
        selenium.click("button_videoList");
        Thread.sleep(15000);
        String getText2=selenium.getTable("//div[@id='params_videoList']/table.1.1");
		testCheckString(getText2);
        selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_videoList");
        Thread.sleep(15000);
        String getText3=selenium.getTable("//div[@id='params_videoList']/table.1.1");
		testCheckString(getText3);
        
		selenium.click("link=raceNewsRss");
		selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.click("button_raceNewsRss");
        Thread.sleep(10000);
        String getText4=selenium.getText("console_raceNewsRss");
        testCheckString(getText4);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_raceNewsRss");
        Thread.sleep(10000);
        String getText5=selenium.getText("console_raceNewsRss");
        testCheckString(getText5);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_raceNewsRss");
        Thread.sleep(10000);
		String getText6=selenium.getText("console_raceNewsRss");
        testCheckString(getText6);
    }

    /*
        Accessing tryit to test RESTSample service.
    */
    public static void testRESTSample_Service() throws Exception{
        String endpoint[]={"label=SOAP12Endpoint","label=SOAP11Endpoint","label=HTTPEndpoint"};
        int i;
        selenium.click("//img[@title='Show endpoint options']");
        for(i=0;i<3;i++){
            selenium.select("endpointSelect", endpoint[i]);
            selenium.click("link=POSTWeather");
		    selenium.type("input_POSTWeather_city_0", "Matara");
		    selenium.type("input_POSTWeather_weatherDetails_0", "30");
		    selenium.click("button_POSTWeather");
            Thread.sleep(3000);
		    assertTrue(selenium.isTextPresent("Matara"));
        
            selenium.click("link=getWeather");
            selenium.type("input_getWeather_city_0", "Matara");
            selenium.click("button_getWeather");
            Thread.sleep(3000);
		    assertTrue(selenium.isTextPresent("30"));

            selenium.click("link=PUTWeather");
            selenium.type("input_PUTWeather_city_0", "Matara");
		    selenium.type("input_PUTWeather_weatherDetails_0", "40");
		    selenium.click("button_PUTWeather");
            Thread.sleep(3000);
		    assertTrue(selenium.isTextPresent("Matara"));

		    selenium.click("link=DeleteWeather");
		    selenium.type("input_DeleteWeather_city_0", "Matara");
		    selenium.click("button_DeleteWeather");
            Thread.sleep(3000);
		    assertTrue(selenium.isTextPresent("Matara"));
        }
    }

    /*
        Accessing tryit to test storexml service.
    */
    public static void teststorexml_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");
        int i;
        String endpoint[]={"label=SOAP12Endpoint","label=SOAP11Endpoint","label=HTTPEndpoint"};
        for(i=0;i<3;i++){
            selenium.select("endpointSelect", endpoint[i]);
            selenium.click("link=store");
		    selenium.type("input_store_name_0", "test");
		    selenium.type("input_store_value_0", "<x>xxx</x>");
		    selenium.click("button_store");
		    assertTrue(selenium.isTextPresent("<x>xxx</x>"));
		    selenium.click("link=retrieve");
		    selenium.type("input_retrieve_name_0", "test");
		    selenium.click("button_retrieve");
		    assertTrue(selenium.isTextPresent("<x>xxx</x>"));
		    selenium.click("link=remove");
		    selenium.type("input_remove_name_0", "test");
		    selenium.click("button_remove");
		    assertTrue(selenium.isTextPresent("<x>xxx</x>"));
        }
    }

    /*
        Accessing tryit to test sudoku service.
    */
    public static void testsudoku_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");
		selenium.click("link=options");
		selenium.click("button_options");
        Thread.sleep(6000);
		String getText1=selenium.getTable("//div[@id='params_options']/table.1.1");
        testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_options");
        Thread.sleep(6000);
		String getText2=selenium.getTable("//div[@id='params_options']/table.1.1");
        testCheckString(getText2);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_options");
        Thread.sleep(6000);
		String getText3=selenium.getTable("//div[@id='params_options']/table.1.1");
        testCheckString(getText3);

        selenium.click("link=newpuzzle");
		selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.type("input_newpuzzle_options_0", "<options>\n        <symmetrical default=\"true\">false</symmetrical>\n        <difficulty default=\"medium\">expert</difficulty>\n    </options>");
		selenium.click("button_newpuzzle");
        Thread.sleep(15000);
        String getText4=selenium.getTable("//div[@id='params_newpuzzle']/table.2.1");
        testCheckString(getText4);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_newpuzzle");
        Thread.sleep(15000);
        String getText5=selenium.getTable("//div[@id='params_newpuzzle']/table.2.1");
        testCheckString(getText5);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_newpuzzle");
        Thread.sleep(15000);
        String getText6=selenium.getTable("//div[@id='params_newpuzzle']/table.2.1");
        testCheckString(getText6);
    }

    /*
        Accessing tryit to test tomatoTube service.
    */
    public static void testtomatoTube_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");
		selenium.click("link=readTomatoTubeFeed");
		selenium.type("input_readTomatoTubeFeed_mode_0", "dvd");
		selenium.click("button_readTomatoTubeFeed");
        Thread.sleep(15000);
        String getText1=selenium.getTable("//div[@id='params_readTomatoTubeFeed']/table.2.1");
        testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_readTomatoTubeFeed");
        Thread.sleep(15000);
        String getText2=selenium.getTable("//div[@id='params_readTomatoTubeFeed']/table.2.1");
        testCheckString(getText2);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_readTomatoTubeFeed");
        Thread.sleep(15000);
        String getText3=selenium.getTable("//div[@id='params_readTomatoTubeFeed']/table.2.1");
        testCheckString(getText3);
        
		selenium.click("link=findTrailer");
		selenium.type("input_findTrailer_moviename_0", "test");
		selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.click("button_findTrailer");
        String getText4=selenium.getTable("//div[@id='params_findTrailer']/table.2.1");
        testCheckString(getText4);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_findTrailer");
        String getText5=selenium.getTable("//div[@id='params_findTrailer']/table.2.1");
        testCheckString(getText5);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_findTrailer");
        String getText6=selenium.getTable("//div[@id='params_findTrailer']/table.2.1");
        testCheckString(getText6);
    }

    /*
        Accessing tryit to test upgradeChecker service.
    */
    public static void testupgradeChecker_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");
        int i;
        String arr1[]={"testXML","test","getRecommendationXML","getRecommendation"};
        String arr2[]={"//div[@id='params_testXML']/table.2.1","//div[@id='params_test']/table.2.1","//div[@id='params_getRecommendationXML']/table.1.1","//div[@id='params_getRecommendation']/table.1.1"};
        for(i=0;i<4;i++)
        {
		selenium.click("link="+arr1[i]);
        selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.click("button_"+arr1[i]);
        Thread.sleep(10000);
		String getText1=selenium.getTable(arr2[i]);
        testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_"+arr1[i]);
        Thread.sleep(10000);
		String getText2=selenium.getTable(arr2[i]);
        testCheckString(getText2);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_"+arr1[i]);
        Thread.sleep(10000);      
		String getText3=selenium.getTable(arr2[i]);
        testCheckString(getText3);
        }
    }
    
    /*
        Accessing tryit to test version service.
    */
    public static void testversion_Service() throws Exception{
        selenium.click("//img[@title='Show endpoint options']");

		selenium.click("link=isNightly");
		selenium.click("button_isNightly");
		assertTrue(selenium.isTextPresent("false"));
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_isNightly");
		assertTrue(selenium.isTextPresent("false"));
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_isNightly");
		assertTrue(selenium.isTextPresent("false"));

        int i;
        String link[]={"buildDate","getVersion","versionNumber","friendlyBuildDate"};

        for(i=0;i<4;i++){
		selenium.click("link="+link[i]);
		selenium.select("endpointSelect", "label=SOAP12Endpoint");
		selenium.click("button_"+link[i]);
		String getText1=selenium.getText("console_"+link[i]);
        testCheckString(getText1);
		selenium.select("endpointSelect", "label=SOAP11Endpoint");
		selenium.click("button_"+link[i]);
        String getText2=selenium.getText("console_"+link[i]);
        testCheckString(getText2);
		selenium.select("endpointSelect", "label=HTTPEndpoint");
		selenium.click("button_"+link[i]);
        String getText3=selenium.getText("console_"+link[i]);
        testCheckString(getText3);
        }	
    }

}
