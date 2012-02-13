package org.wso2.carbon.web.test.mashup;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import com.thoughtworks.selenium.Selenium;

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


public class MSDataBindingTest extends CommonSetup {

    public MSDataBindingTest(String text) {
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
        String expectedForCSHelp="https://"+MSCommon.loadProperties().getProperty("host.name")+":"+MSCommon.loadProperties().getProperty("https.be.port")+MSCommon.loadProperties().getProperty("context.root")+"/carbon/service-mgt/docs/userguide.html";
        selenium.click("link=List");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Help"));
        selenium.click("link=Help");
        String helpwinid = selenium.getEval("{var windowId; for(var x in selenium.browserbot.openedWindows ) {windowId=x;} }");
        selenium.selectWindow(helpwinid);
        Thread.sleep(10000);
		assertTrue(selenium.isTextPresent("Deployed Services"));
        String actualForCSHelp = selenium.getLocation();
        if(actualForCSHelp.equals(expectedForCSHelp))
            System.out.println("Actual location & expected location are matched");
        else
            System.out.println("Actual location & expected location are not matched");
        selenium.close();
        selenium.selectWindow("");
     }


    /*
           Testing JS:enumeration type.
     */

    public void testJSEnumeration() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSEnumerationType_Service("label=a");
        assertTrue(selenium.isTextPresent("a"));

        MSDataBinding.testJSEnumerationType_Service("label=b");
        assertTrue(selenium.isTextPresent("b"));

        MSDataBinding.testJSEnumerationType_Service("label=c");
        assertTrue(selenium.isTextPresent("c"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:number type.
    */
    public void testJSnumber() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSnumberType_Service("12277.98");
        assertTrue(selenium.isTextPresent("12277.98"));

        MSDataBinding.testJSnumberType_Service("898");
        assertTrue(selenium.isTextPresent("898"));

        MSDataBinding.testJSnumberType_Service("12.00");
        assertTrue(selenium.isTextPresent("12"));

        MSDataBinding.testJSnumberType_Service("-12.00");
        assertTrue(selenium.isTextPresent("-12"));

        MSDataBinding.testJSnumberType_Service("-2335.98");
        assertTrue(selenium.isTextPresent("-2335.98"));

        MSDataBinding.testJSnumberType_Service("test");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"test\" to double [detail] <soapenv:Detail />"));

        MSDataBinding.testJSnumberType_Service("");
        assertTrue(selenium.isTextPresent("NaN"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:Boolean type.
    */
    public void testJSBoolean() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSBooleanType_Service("true");
        assertTrue(selenium.isTextPresent("true"));

        MSDataBinding.testJSBooleanType_Service("false");
        assertTrue(selenium.isTextPresent("false"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:string type.
    */
    public void testJSstring() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSstringType_Service("StringType");
        assertTrue(selenium.isTextPresent("StringType"));

        MSDataBinding.testJSstringType_Service("test");
        assertTrue(selenium.isTextPresent("test"));

        MSDataBinding.testJSstringType_Service("This is a Test");
        assertTrue(selenium.isTextPresent("This is a Test"));

        MSDataBinding.testJSstringType_Service("@@@@");
        assertTrue(selenium.isTextPresent("@@@@"));

        MSDataBinding.testJSstringType_Service("1234");
        assertTrue(selenium.isTextPresent("1234"));

        MSDataBinding.testJSstringType_Service("<x>xxx</x>");
        assertTrue(selenium.isTextPresent("<x>xxx</x>"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:Number type.
    */
    public void testJSNumber() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSNumberType_Service("12277.98");
        assertTrue(selenium.isTextPresent("12277.98"));

        MSDataBinding.testJSNumberType_Service("898");
        assertTrue(selenium.isTextPresent("898"));

        MSDataBinding.testJSNumberType_Service("12.00");
        assertTrue(selenium.isTextPresent("12"));

        MSDataBinding.testJSNumberType_Service("-12.00");
        assertTrue(selenium.isTextPresent("-12"));

        MSDataBinding.testJSNumberType_Service("-2335.98");
        assertTrue(selenium.isTextPresent("-2335.98"));

        MSDataBinding.testJSNumberType_Service("test");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"test\" to double [detail] <soapenv:Detail />"));

        MSDataBinding.testJSNumberType_Service("");
        assertTrue(selenium.isTextPresent("NaN"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:boolean type.
    */
    public void testJSboolean() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSbooleanType_Service("true");
        assertTrue(selenium.isTextPresent("true"));

        MSDataBinding.testJSbooleanType_Service("false");
        assertTrue(selenium.isTextPresent("false"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:ArrayWithProperties type.
    */
    public void testJSArrayWithProperties() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSArrayWithPropertiesType_Service();
        assertTrue(selenium.isTextPresent("value1,2.27,<value>2</value>"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:String type.
    */
    public void testJSString() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSStringType_Service("StringType");
        assertTrue(selenium.isTextPresent("StringType"));

        MSDataBinding.testJSStringType_Service("test");
        assertTrue(selenium.isTextPresent("test"));

        MSDataBinding.testJSStringType_Service("This is a Test");
        assertTrue(selenium.isTextPresent("This is a Test"));

        MSDataBinding.testJSStringType_Service("@@@@");
        assertTrue(selenium.isTextPresent("@@@@"));

        MSDataBinding.testJSStringType_Service("1234");
        assertTrue(selenium.isTextPresent("1234"));

        MSDataBinding.testJSStringType_Service("<x>xxx</x>");
        assertTrue(selenium.isTextPresent("<x>xxx</x>"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:date type.
    */
    public void testJSdate() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSdateType_Service("2007-08-27T10:20:30.040Z");
        //assertTrue(selenium.isTextPresent("Mon Aug 27 2007 16:20:30 GMT+0600 (IOT)"));   ***** assert fails depending on the system date format.

        /* NOTE (to be deleted) -Asserting the time is not an option as it varies from machine to machine.
        Let's assert its not returning a fault until we implement a method to get system configs and build a date to compare with the assert. */


        MSDataBinding.testJSdateType_Service("ww");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"ww\" to date time [detail] <soapenv:Detail />"));

        MSDataBinding.testJSdateType_Service("2007-08-27");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2007-08-27\" to date time [detail] <soapenv:Detail />"));

        MSDataBinding.testJSdateType_Service("3");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"3\" to date time [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:Object type.
    */
    public void testJSObject() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSObjectType_Service("test", "123", "true");
        assertTrue(selenium.isTextPresent("{param1 : \"test\", param2 : 123, param3 : true}"));

        MSDataBinding.testJSObjectType_Service("test1", "1234", "false");
        assertTrue(selenium.isTextPresent("{param1 : \"test1\", param2 : 1234, param3 : false}"));

        MSDataBinding.testJSObjectType_Service("test2", "test", "true");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"test\" to double [detail] <soapenv:Detail />"));

        MSDataBinding.testJSObjectType_Service("test", "", "false");
        assertTrue(selenium.isTextPresent("{param1 : \"test\", param2 : NaN, param3 : false}"));

        MSDataBinding.testJSObjectType_Service("", "", "true");
        assertTrue(selenium.isTextPresent("{param1 : \"\", param2 : NaN, param3 : true}"));

        MSDataBinding.testJSObjectType_Service("123", "123", "false");
        assertTrue(selenium.isTextPresent("{param1 : \"123\", param2 : 123, param3 : false}"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:stringArray type.
    */
    public void testJSstringArray() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSstringArrayType_Service("apples", "oranges", "grapes");
        assertTrue(selenium.isTextPresent("apples,oranges,grapes"));
        selenium.click("//input[@value='Remove param']");
        selenium.click("button_echoStringArray");
        assertTrue(selenium.isTextPresent("apples,oranges"));
        selenium.click("//input[@value='Remove param']");
        selenium.click("button_echoStringArray");
        assertTrue(selenium.isTextPresent("apples"));

        MSDataBinding.testJSstringArrayType_Service("1", "2", "3");
        assertTrue(selenium.isTextPresent("1,2,3"));
        selenium.click("//input[@value='Remove param']");
        selenium.click("button_echoStringArray");
        assertTrue(selenium.isTextPresent("1,2"));
        selenium.click("//input[@value='Remove param']");
        selenium.click("button_echoStringArray");
        assertTrue(selenium.isTextPresent("1"));

        MSDataBinding.testJSstringArrayType_Service("ASD!@#", "GT%%", "jjjjj");
        assertTrue(selenium.isTextPresent("ASD!@#,GT%%,jjjjj"));
        selenium.click("//input[@value='Remove param']");
        selenium.click("button_echoStringArray");
        assertTrue(selenium.isTextPresent("ASD!@#,GT%%"));
        selenium.click("//input[@value='Remove param']");
        selenium.click("button_echoStringArray");
        assertTrue(selenium.isTextPresent("ASD!@#"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:ArrayWithIndices type.
    */
    public void testJSArrayWithIndices() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSArrayWithIndicesType_Service();
        assertTrue(selenium.isTextPresent("value1,2.27,<value>2</value>"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
          Testing JS:Date type.
    */
    public void testJSDate() throws Exception {
        MSCommon.testAccessTryit("schemaTest1");
        Thread.sleep(10000);
        MSDataBinding.testJSDateType_Service("2007-08-27T10:20:30.040Z");
     //   assertTrue(selenium.isTextPresent("Mon Aug 27 2007 16:20:30 GMT+0600 (IOT)")); ***** assert fails depending on the system date format. Sarasi fixing this.

        MSDataBinding.testJSDateType_Service("ww");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"ww\" to date time [detail] <soapenv:Detail />"));

        MSDataBinding.testJSDateType_Service("2007-08-27");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2007-08-27\" to date time [detail] <soapenv:Detail />"));

        MSDataBinding.testJSDateType_Service("3");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"3\" to date time [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
           Testing xs:name type.
    */
    public void testXSNameType() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSStringType_service("Snoopy");
        assertTrue(selenium.isTextPresent("Snoopy"));

        MSDataBinding.testXSStringType_service("CMS");
        assertTrue(selenium.isTextPresent("CMS"));

        MSDataBinding.testXSStringType_service("_1950-10-04_10:00");
        assertTrue(selenium.isTextPresent("_1950-10-04_10:00"));

        MSDataBinding.testXSStringType_service("0836217462");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert the return value to name"));

        MSDataBinding.testXSStringType_service("bold,brash");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert the return value to name"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
           Testing xs:long type
    */
    public void testXSLongType() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSLongType("-9223372036854775808");
        assertTrue(selenium.isTextPresent("-9223372036854776000"));

        MSDataBinding.testXSLongType("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSLongType("-0000000000000000000005");
        assertTrue(selenium.isTextPresent("-5"));

        MSDataBinding.testXSLongType("9223372036854775807");
        assertTrue(selenium.isTextPresent("9223372036854776000"));

        MSDataBinding.testXSLongType("1.");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"1.\" to long"));

        MSDataBinding.testXSLongType("9223372036854775808");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"9223372036854775808\" to long"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
            Testing xs:double type.
    */
    public void testXSDoubleType() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSDoubleType_service("123.456");
        assertTrue(selenium.isTextPresent("123.456"));

        MSDataBinding.testXSDoubleType_service("+1234.456");
        assertTrue(selenium.isTextPresent("1234.456"));

        MSDataBinding.testXSDoubleType_service("-1.2344e56");
        assertTrue(selenium.isTextPresent("-1.2344e+56"));

        MSDataBinding.testXSDoubleType_service("-.45E-6");
        assertTrue(selenium.isTextPresent("-4.5e-7"));

        MSDataBinding.testXSDoubleType_service("INF");
        assertTrue(selenium.isTextPresent("Infinity"));

        MSDataBinding.testXSDoubleType_service("-INF");
        assertTrue(selenium.isTextPresent("-Infinity"));

        MSDataBinding.testXSDoubleType_service("NaN");
        assertTrue(selenium.isTextPresent("NaN"));

        MSDataBinding.testXSDoubleType_service("1234.4E 56");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"1234.4E 56\" to double"));

        MSDataBinding.testXSDoubleType_service("1E+2.5");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"1E+2.5\" to double"));

        MSDataBinding.testXSDoubleType_service("NAN");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"NAN\" to double"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
            Testing xs:duration type.
    */
    public void testXSDuration() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSDurationType_service("PT1004199059S");
        assertTrue(selenium.isTextPresent("PT1004199059S"));

        MSDataBinding.testXSDurationType_service("PT130S");
        assertTrue(selenium.isTextPresent("PT130S"));

        MSDataBinding.testXSDurationType_service("PT2M10S");
        assertTrue(selenium.isTextPresent("PT2M10S"));

        MSDataBinding.testXSDurationType_service("P1DT2S");
        assertTrue(selenium.isTextPresent("P1DT2S"));

        MSDataBinding.testXSDurationType_service("-P1Y");
        assertTrue(selenium.isTextPresent("-P1Y"));

        MSDataBinding.testXSDurationType_service("P1Y2M3DT5H20M30.123S");
        assertTrue(selenium.isTextPresent("P1Y2M3DT5H20M30.12S"));

        MSDataBinding.testXSDurationType_service("1Y");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"1Y\" to duration"));

        MSDataBinding.testXSDurationType_service("P1S");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"P1S\" to duration"));

        MSDataBinding.testXSDurationType_service("P-1Y");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"P-1Y\" to duration"));

        MSDataBinding.testXSDurationType_service("P1M2Y");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"P1M2Y\" to duration"));

        MSDataBinding.testXSDurationType_service("P1Y-1M");
        assertTrue(selenium.isTextPresent("Fault: Unable to convert value \"P1Y-1M\" to duration"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
              Testing xs:anyuri type.
      */
      public void testXSAnyURI() throws Exception {
          MSCommon.testAccessTryit("schemaTest2");
          Thread.sleep(10000);
          MSDataBinding.testXSAnyURIType_service("http://www.google.com");
          assertTrue(selenium.isTextPresent("exact:http://www.google.com"));

          MSDataBinding.testXSAnyURIType_service("https://wso2.org/jira/");
          assertTrue(selenium.isTextPresent("exact:https://wso2.org/jira/"));

          selenium.close();
          selenium.selectWindow("");
      }


      /*
              Testing xs:NonPositiveInteger type.
      */
      public void testXSNonPositiveInteger() throws Exception {
          MSCommon.testAccessTryit("schemaTest2");
          Thread.sleep(10000);
          MSDataBinding.testXSNonPositiveIntegerType_service("-123456789012345678901234567890");
          assertTrue(selenium.isTextPresent("-1.2345678901234568e+29"));

          MSDataBinding.testXSNonPositiveIntegerType_service("-1");
          assertTrue(selenium.isTextPresent("-1"));

          MSDataBinding.testXSNonPositiveIntegerType_service("-0000000000000000000005");
          assertTrue(selenium.isTextPresent("-5"));

          MSDataBinding.testXSNonPositiveIntegerType_service("0");
          assertTrue(selenium.isTextPresent("0"));

          MSDataBinding.testXSNonPositiveIntegerType_service("-1.");
          assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-1.\" to non-positive integer [detail] <soapenv:Detail />"));

          selenium.close();
          selenium.selectWindow("");
      }

    /*
               Testing xs:negativeInteger type.
       */
       public void testXSNegativeInteger() throws Exception {
           MSCommon.testAccessTryit("schemaTest2");
           Thread.sleep(10000);
           MSDataBinding.testXSNegativeIntegerType_service("-123456789012345678901234567890");
           assertTrue(selenium.isTextPresent("-1.2345678901234568e+29"));

           MSDataBinding.testXSNegativeIntegerType_service("-1");
           assertTrue(selenium.isTextPresent("-1"));

           MSDataBinding.testXSNegativeIntegerType_service("-0000000000000000000005");
           assertTrue(selenium.isTextPresent("-5"));

           MSDataBinding.testXSNegativeIntegerType_service("0");
           assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"0\" to negative integer [detail] <soapenv:Detail />"));

           MSDataBinding.testXSNegativeIntegerType_service("-1.");
           assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-1.\" to negative integer [detail] <soapenv:Detail />"));

           selenium.close();
           selenium.selectWindow("");
       }

    /*
               Testing xs:normalizedString type.
        */
       public void testXSNString() throws Exception {
           MSCommon.testAccessTryit("schemaTest2");
           Thread.sleep(10000);
           MSDataBinding.testXSNormalizedStringType_service("John Smith");
           assertTrue(selenium.isTextPresent("John Smith"));

           // MSDataBinding.testXSNormalizedStringType_service("  John Smith  ");
           // assertTrue(selenium.isTextPresent("John Smith"));

           selenium.close();
           selenium.selectWindow("");
       }


    /*
            Testing xs:short type.
    */
    public void testXSShort() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSShortType_service("-32768");
        assertTrue(selenium.isTextPresent("-32768"));

        MSDataBinding.testXSShortType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSShortType_service("-0000000000000000000005");
        assertTrue(selenium.isTextPresent("-5"));

        MSDataBinding.testXSShortType_service("32767");
        assertTrue(selenium.isTextPresent("32767"));

        MSDataBinding.testXSShortType_service("32768");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"32768\" to short [detail] <soapenv:Detail />"));

        MSDataBinding.testXSShortType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to short [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
            Testing xs:Float type.
    */
    public void testXSFloat() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSFloatType_service("123.456");
        assertTrue(selenium.isTextPresent("123.456"));

        MSDataBinding.testXSFloatType_service("+1234.456");
        assertTrue(selenium.isTextPresent("1234.456"));

        MSDataBinding.testXSFloatType_service("-1.2344e56");
        assertTrue(selenium.isTextPresent("-Infinity"));

        MSDataBinding.testXSFloatType_service("-.45E-6");
        assertTrue(selenium.isTextPresent("-4.5e-7"));

        MSDataBinding.testXSFloatType_service("INF");
        assertTrue(selenium.isTextPresent("Infinity"));

        MSDataBinding.testXSFloatType_service("-INF");
        assertTrue(selenium.isTextPresent("-Infinity"));

        MSDataBinding.testXSFloatType_service("NaN");
        assertTrue(selenium.isTextPresent("NaN"));

        MSDataBinding.testXSFloatType_service("1234.4E 56");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1234.4E 56\" to float [detail] <soapenv:Detail />"));

        MSDataBinding.testXSFloatType_service("1E+2.5");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1E+2.5\" to float [detail] <soapenv:Detail />"));

        MSDataBinding.testXSFloatType_service("+INF");
        assertTrue(selenium.isTextPresent("Infinity"));

        MSDataBinding.testXSFloatType_service("NAN");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"NAN\" to float [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
      }

    /*
               Testing xs:NOTATION type.
       */
       public void testXSNOTATION() throws Exception {
           MSCommon.testAccessTryit("schemaTest2");
           Thread.sleep(10000);
           MSDataBinding.testXSNOTATIONType_service("test");
           assertTrue(selenium.isTextPresent("org.apache.axis2.databinding.types.Notation@364492"));


           selenium.close();
           selenium.selectWindow("");
       }


       /*
      Testing xs:string type.
       */
       public void testXSstring() throws Exception {
           MSCommon.testAccessTryit("schemaTest2");
           Thread.sleep(10000);
           MSDataBinding.testXSstringType_service("StringType");
           assertTrue(selenium.isTextPresent("StringType"));

           MSDataBinding.testXSstringType_service("test");
           assertTrue(selenium.isTextPresent("test"));

           MSDataBinding.testXSstringType_service("This is a test");
           assertTrue(selenium.isTextPresent("This is a test"));

           MSDataBinding.testXSstringType_service("@@@@");
           assertTrue(selenium.isTextPresent("@@@@"));

           MSDataBinding.testXSstringType_service("<x>xxx</x>");
           assertTrue(selenium.isTextPresent("<x>xxx</x>"));

           selenium.close();
           selenium.selectWindow("");
       }


      /*
              Testing xs:token type.
      */
      public void testXSToken() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSTokenType_service("John Smith");
        assertTrue(selenium.isTextPresent("John Smith"));

        selenium.close();
        selenium.selectWindow("");
     }

    /*
             Testing xs:NCName type.
      */
      public void testXSNCName() throws Exception {
          MSCommon.testAccessTryit("schemaTest2");
          Thread.sleep(10000);
          MSDataBinding.testXSNCNameType_service("Snoopy");
          assertTrue(selenium.isTextPresent("Snoopy"));

          MSDataBinding.testXSNCNameType_service("CMS");
          assertTrue(selenium.isTextPresent("CMS"));

          MSDataBinding.testXSNCNameType_service("_1950-10-04_10-00");
          assertTrue(selenium.isTextPresent("_1950-10-04_10-00"));

          MSDataBinding.testXSNCNameType_service("bold_brash");
          assertTrue(selenium.isTextPresent("bold_brash"));

          MSDataBinding.testXSNCNameType_service("_1950-10-04:10-00");
          assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert the return value to NCName [detail] <soapenv:Detail />"));

          MSDataBinding.testXSNCNameType_service("bold:brash");
          assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert the return value to NCName [detail] <soapenv:Detail />"));

          selenium.close();
          selenium.selectWindow("");

      }

       /*
            Testing xs:integer type.
     */
    public void testXSInteger() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSIntegerType_service("2147483647");
        assertTrue(selenium.isTextPresent("2147483647"));

        MSDataBinding.testXSIntegerType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSIntegerType_service("-0000000000000000000005");
        assertTrue(selenium.isTextPresent("-5"));

        MSDataBinding.testXSIntegerType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to integer [detail] <soapenv:Detail />"));

        MSDataBinding.testXSIntegerType_service("2.6");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2.6\" to integer [detail] <soapenv:Detail />"));

        MSDataBinding.testXSIntegerType_service("A");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"A\" to integer [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
    Testing xs:language type.
               */
    public void testXSLanguage() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSLanguageType_service("en");
        assertTrue(selenium.isTextPresent("en"));

        MSDataBinding.testXSLanguageType_service("en-US");
        assertTrue(selenium.isTextPresent("en-US"));

        MSDataBinding.testXSLanguageType_service("fr");
        assertTrue(selenium.isTextPresent("fr"));

        MSDataBinding.testXSLanguageType_service("fr-FR");
        assertTrue(selenium.isTextPresent("fr-FR"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
                Testing xs:int type.
                   */
    public void testXSInt() throws Exception {
        MSCommon.testAccessTryit("schemaTest2");
        Thread.sleep(10000);
        MSDataBinding.testXSIntType_service("-2147483648");
        assertTrue(selenium.isTextPresent("-2147483648"));

        MSDataBinding.testXSIntType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSIntType_service("-0000000000000000000005");
        assertTrue(selenium.isTextPresent("-5"));

        MSDataBinding.testXSIntType_service("2147483647");
        assertTrue(selenium.isTextPresent("2147483647"));

        MSDataBinding.testXSIntType_service("-2147483649");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-2147483649\" to int [detail] <soapenv:Detail />"));

        MSDataBinding.testXSIntType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to int [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

//.................................................................................................................
    /*
            Testing xs:time type.
     */
    public void testXSTimeType() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSTimeType_service("21:32:52");
//        assertTrue(selenium.isTextPresent("Thu Jan 01 1970 20:32:52 GMT+0500 (IOT)"));

        MSDataBinding.testXSTimeType_service("21:32:52+02:00");
 //       assertTrue(selenium.isTextPresent("Thu Jan 01 1970 00:32:52 GMT+0500 (IOT)"));

        MSDataBinding.testXSTimeType_service("19:32:52Z");
//        assertTrue(selenium.isTextPresent("Thu Jan 01 1970 00:32:52 GMT+0500 (IOT)"));

        MSDataBinding.testXSTimeType_service("19:32:52+00:00");
//        assertTrue(selenium.isTextPresent("Thu Jan 01 1970 00:32:52 GMT+0500 (IOT)"));

        MSDataBinding.testXSTimeType_service("21:32:52.12679");
//        assertTrue(selenium.isTextPresent("Thu Jan 01 1970 20:32:52 GMT+0500 (IOT)"));

        MSDataBinding.testXSTimeType_service("21:32");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"21:32\" to time [detail] <soapenv:Detail />"));

        MSDataBinding.testXSTimeType_service("25:25:10");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"25:25:10\" to time [detail] <soapenv:Detail />"));

        MSDataBinding.testXSTimeType_service("-10:00:00");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-10:00:00\" to time [detail] <soapenv:Detail />"));

        MSDataBinding.testXSTimeType_service("1:20:10");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1:20:10\" to time [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }
    /*
            Testing xs:gYearMonth type.
    */
    public void testXSgYearMonth() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSgYearMonthType_service("2001-10");
        assertTrue(selenium.isTextPresent("2001-10"));

        MSDataBinding.testXSgYearMonthType_service("2001-10+02:00");
        assertTrue(selenium.isTextPresent("2001-10"));

        MSDataBinding.testXSgYearMonthType_service("2001-10Z");
        assertTrue(selenium.isTextPresent("2001-10"));

        MSDataBinding.testXSgYearMonthType_service("2001-10+00:00");
        assertTrue(selenium.isTextPresent("2001-10"));

        MSDataBinding.testXSgYearMonthType_service("-2001-10");
        assertTrue(selenium.isTextPresent("2002-10"));

        MSDataBinding.testXSgYearMonthType_service("-20000-04");
        assertTrue(selenium.isTextPresent("20001-04"));

        MSDataBinding.testXSgYearMonthType_service("2001");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001\" to yearMonth [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgYearMonthType_service("2001-13");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-13\" to yearMonth [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgYearMonthType_service("2001-13-26+02:00");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-13-26+02:00\" to yearMonth [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgYearMonthType_service("01-10");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"01-10\" to yearMonth [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
            Testing xs:decimal type.
     */
    public void testXSDecimal() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSDecimalType_service("123.456");
        assertTrue(selenium.isTextPresent("123.456"));

        MSDataBinding.testXSDecimalType_service("+1234.456");
        assertTrue(selenium.isTextPresent("1234.456"));

        MSDataBinding.testXSDecimalType_service("-1234.456");
        assertTrue(selenium.isTextPresent("-1234.456"));

        MSDataBinding.testXSDecimalType_service("-.456");
        assertTrue(selenium.isTextPresent("-0.456"));

        MSDataBinding.testXSDecimalType_service("-456");
        assertTrue(selenium.isTextPresent("-456"));

        MSDataBinding.testXSDecimalType_service("1234.456");
        assertTrue(selenium.isTextPresent("1234.456"));

        MSDataBinding.testXSDecimalType_service("1234.456E+2");
        assertTrue(selenium.isTextPresent("123445.6"));

        MSDataBinding.testXSDecimalType_service("+1,234.456");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"+1,234.456\" to decimal [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
               Testing xs:DateTime type.
       */
       public void testXSDateTime() throws Exception {
           MSCommon.testAccessTryit("schemaTest4");
           Thread.sleep(10000);
           MSDataBinding.testXSDateTimeType_service("2001-10-26T21:32:52");
    //       assertTrue(selenium.isTextPresent("Fri Oct 26 2001 21:32:52 GMT+0600 (IOT)"));

           MSDataBinding.testXSDateTimeType_service("2001-10-26T21:32:52+02:00");
    //       assertTrue(selenium.isTextPresent("Sat Oct 27 2001 01:32:52 GMT+0600 (IOT)"));

           MSDataBinding.testXSDateTimeType_service("2001-10-26T19:32:52Z");
    //       assertTrue(selenium.isTextPresent("Sat Oct 27 2001 01:32:52 GMT+0600 (IOT)"));

           MSDataBinding.testXSDateTimeType_service("2001-10-26T19:32:52+00:00");
//        assertTrue(selenium.isTextPresent("Sat Oct 27 2001 01:32:52 GMT+0600 (IOT)"));

           MSDataBinding.testXSDateTimeType_service("-2001-10-26T21:32:52");
//        assertTrue(selenium.isTextPresent("Fri Oct 26 2001 21:32:52 GMT+0600 (IOT)"));

           MSDataBinding.testXSDateTimeType_service("2001-10-26T21:32:52.12679");
     //      assertTrue(selenium.isTextPresent("Fri Oct 26 2001 21:32:52 GMT+0600 (IOT)"));

           MSDataBinding.testXSDateTimeType_service("2001-10-26");
           assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-10-26\" to date time [detail] <soapenv:Detail />"));

           MSDataBinding.testXSDateTimeType_service("2001-10-26T21:32");
           assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-10-26T21:32\" to date time [detail] <soapenv:Detail />"));

           MSDataBinding.testXSDateTimeType_service("2001-10-26T25:32:52+02:00");
           assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-10-26T25:32:52+02:00\" to date time [detail] <soapenv:Detail />"));

           MSDataBinding.testXSDateTimeType_service("01-10-26T21:32");
           assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"01-10-26T21:32\" to date time [detail] <soapenv:Detail />"));

           selenium.close();
           selenium.selectWindow("");
       }

    /*
            Testing xs:UnsignedByte type.
    */
    public void testXSUnsignedByte() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSUnsignedByteType_service("255");
        assertTrue(selenium.isTextPresent("255"));

        MSDataBinding.testXSUnsignedByteType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSUnsignedByteType_service("+0000000000000000000005");
        assertTrue(selenium.isTextPresent("5"));

        MSDataBinding.testXSUnsignedByteType_service("1");
        assertTrue(selenium.isTextPresent("1"));

        MSDataBinding.testXSUnsignedByteType_service("-1");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-1\" to unsigned byte [detail] <soapenv:Detail />"));

        MSDataBinding.testXSUnsignedByteType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to unsigned byte [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }



    /*
            Testing xs:UnsignedShort type.
    */
    public void testXSUnsignedShort() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSUnsignedShortType_service("255");
        assertTrue(selenium.isTextPresent("255"));

        MSDataBinding.testXSUnsignedShortType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSUnsignedShortType_service("+0000000000000000000005");
        assertTrue(selenium.isTextPresent("5"));

        MSDataBinding.testXSUnsignedShortType_service("1");
        assertTrue(selenium.isTextPresent("1"));

        MSDataBinding.testXSUnsignedShortType_service("-1");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-1\" to unsigned short [detail] <soapenv:Detail />"));

        MSDataBinding.testXSUnsignedShortType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to unsigned short [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
              Testing xs:gMonth type.
    */
    public void testXSgMonth() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSgMonthType_service("--05");
        MSDataBinding.testXSgMonthType_service("--11Z");
        MSDataBinding.testXSgMonthType_service("--11+02:00");
        MSDataBinding.testXSgMonthType_service("--11-04:00");
        MSDataBinding.testXSgMonthType_service("--02");
        MSDataBinding.testXSgMonthType_service("-01-");
        MSDataBinding.testXSgMonthType_service("--13");
        MSDataBinding.testXSgMonthType_service("--1");
        MSDataBinding.testXSgMonthType_service("01");

        MSDataBinding.testXSgMonthType_service("--05--");
    //    assertTrue(selenium.isTextPresent("--05--"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
            Testing xs:QName type.
    */
    public void testXSQName() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSQNameType_service("x", "y");
        // assertTrue(selenium.isTextPresent("{uri : \"x\", localName : \"s1:y\"}"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
            Testing xs:NonNegativeInteger type.
    */
    public void testXSNonNegativeInteger() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSNonNegativeIntegerType_service("18446744073709551615");
        assertTrue(selenium.isTextPresent("18446744073709552000"));

        MSDataBinding.testXSNonNegativeIntegerType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSNonNegativeIntegerType_service("+0000000000000000000005");
        assertTrue(selenium.isTextPresent("5"));

        MSDataBinding.testXSNonNegativeIntegerType_service("1");
        assertTrue(selenium.isTextPresent("1"));

        MSDataBinding.testXSNonNegativeIntegerType_service("-1");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-1\" to non-negative integer [detail] <soapenv:Detail />"));

        MSDataBinding.testXSNonNegativeIntegerType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to non-negative integer [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
            Testing xs:gYearMonth type.
    */
    public void testXSPositiveInteger() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSPositiveIntegerType_service("123456789012345678901234567890");
        assertTrue(selenium.isTextPresent("1.2345678901234568e+29"));

        MSDataBinding.testXSPositiveIntegerType_service("1");
        assertTrue(selenium.isTextPresent("1"));

        MSDataBinding.testXSPositiveIntegerType_service("0000000000000000000005");
        assertTrue(selenium.isTextPresent("5"));

        MSDataBinding.testXSPositiveIntegerType_service("0");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"0\" to positive integer [detail] <soapenv:Detail />"));

        MSDataBinding.testXSPositiveIntegerType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to positive integer [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
           Testing xs:hexBinary type.
    */
    public void testXShexBinary() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXShexBinaryType_service("3f3c6d78206c657673726f693d6e3122302e20226e656f636964676e223d54552d4622383e3f");
        assertTrue(selenium.isTextPresent("3f3c6d78206c657673726f693d6e3122302e20226e656f636964676e223d54552d4622383e3f"));

        selenium.close();
        selenium.selectWindow("");
    }



    /*
           Testing xs:date type.
    */
    public void testXSDate() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSDateType_service("2001-10-26");
   //     assertTrue(selenium.isTextPresent("Fri Oct 26 2001 12:00:00 GMT+0600 (IOT)"));

        MSDataBinding.testXSDateType_service("2001-10-26+02:00");
   //     assertTrue(selenium.isTextPresent("Fri Oct 26 2001 12:00:00 GMT+0600 (IOT)"));

        MSDataBinding.testXSDateType_service("2001-10-26Z");
  //      assertTrue(selenium.isTextPresent("Fri Oct 26 2001 12:00:00 GMT+0600 (IOT)"));

        MSDataBinding.testXSDateType_service("2001-10-26+00:00");
  //      assertTrue(selenium.isTextPresent("Fri Oct 26 2001 12:00:00 GMT+0600 (IOT)"));

        MSDataBinding.testXSDateType_service("-2001-10-26");
  //      assertTrue(selenium.isTextPresent("Fri Oct 26 2001 12:00:00 GMT+0600 (IOT)"));

        MSDataBinding.testXSDateType_service("-20000-04-01");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-20000-04-01\" to date [detail] <soapenv:Detail />"));

        MSDataBinding.testXSDateType_service("2001-10");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-10\" to date [detail] <soapenv:Detail />"));

        MSDataBinding.testXSDateType_service("2001-10-32");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-10-32\" to date [detail] <soapenv:Detail />"));

        MSDataBinding.testXSDateType_service("2001-13-26+02:00");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-13-26+02:00\" to date [detail] <soapenv:Detail />"));

        MSDataBinding.testXSDateType_service("01-10-26");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"01-10-26\" to date [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
            Testing xs:gDay type.
    */
    public void testXSgDay() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSgDayType_service("---01");
        assertTrue(selenium.isTextPresent("---01"));

        MSDataBinding.testXSgDayType_service("---01Z");
        assertTrue(selenium.isTextPresent("---01"));

        MSDataBinding.testXSgDayType_service("---01+02:00");
        assertTrue(selenium.isTextPresent("---01"));

        MSDataBinding.testXSgDayType_service("---01-04:00");
        assertTrue(selenium.isTextPresent("---01"));

        MSDataBinding.testXSgDayType_service("---15");
        assertTrue(selenium.isTextPresent("---15"));

        MSDataBinding.testXSgDayType_service("---31");
        assertTrue(selenium.isTextPresent("---31"));

        MSDataBinding.testXSgDayType_service("--30-");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"--30-\" to day [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgDayType_service("---35");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"---35\" to day [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgDayType_service("---5");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"---5\" to day [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgDayType_service("15");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"15\" to day [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }



    /*
            Testing xs:unsignedLong type.
    */
    public void testXSUnsignedLong() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSUnsignedLongType_service("18446744073709551615");
        assertTrue(selenium.isTextPresent("18446744073709552000"));

        MSDataBinding.testXSUnsignedLongType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSUnsignedLongType_service("+0000000000000000000005");
        assertTrue(selenium.isTextPresent("5"));

        MSDataBinding.testXSUnsignedLongType_service("1");
        assertTrue(selenium.isTextPresent("1"));

        MSDataBinding.testXSUnsignedLongType_service("-1");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-1\" to unsigned long [detail] <soapenv:Detail />"));

        MSDataBinding.testXSUnsignedLongType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to unsigned long [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
           Testing xs:gMonthDay type.
    */
    public void testXSgMonthDay() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSgMonthDayType_service("--05-01");
        assertTrue(selenium.isTextPresent("--05-01"));

        MSDataBinding.testXSgMonthDayType_service("--11-01Z");
        assertTrue(selenium.isTextPresent("--11-01"));

        MSDataBinding.testXSgMonthDayType_service("--11-01+02:00");
        assertTrue(selenium.isTextPresent("--11-01"));

        MSDataBinding.testXSgMonthDayType_service("--11-01-04:00");
        assertTrue(selenium.isTextPresent("--11-01"));

        MSDataBinding.testXSgMonthDayType_service("--11-15");
        assertTrue(selenium.isTextPresent("--11-15"));

        MSDataBinding.testXSgMonthDayType_service("--02-29");
        assertTrue(selenium.isTextPresent("--02-29"));

        MSDataBinding.testXSgMonthDayType_service("-01-30-");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-01-30-\" to MonthDay [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgMonthDayType_service("--01-35");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"--01-35\" to MonthDay [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgMonthDayType_service("--1-5");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"--1-5\" to MonthDay [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgMonthDayType_service("01-15");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"01-15\" to MonthDay [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }





    /*
 Testing xs:unsignedInt type.
    */
    public void testXSUnsignedInt() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSUnsignedIntType_service("4294967295");
        assertTrue(selenium.isTextPresent("4294967295"));

        MSDataBinding.testXSUnsignedIntType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSUnsignedIntType_service("+0000000000000000000005");
        assertTrue(selenium.isTextPresent("5"));

        MSDataBinding.testXSUnsignedIntType_service("1");
        assertTrue(selenium.isTextPresent("1"));

        MSDataBinding.testXSUnsignedIntType_service("-1");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"-1\" to unsigned int [detail] <soapenv:Detail />"));

        MSDataBinding.testXSUnsignedIntType_service("1.");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1.\" to unsigned int [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
            Testing xs:gYear type.
               */
    public void testXSgYear() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSgYearType_service("2001");
        assertTrue(selenium.isTextPresent("2001"));

        MSDataBinding.testXSgYearType_service("2001+02:00");
        assertTrue(selenium.isTextPresent("2001"));

        MSDataBinding.testXSgYearType_service("2001Z");
        assertTrue(selenium.isTextPresent("2001"));

        MSDataBinding.testXSgYearType_service("2001+00:00");
        assertTrue(selenium.isTextPresent("2001"));

        MSDataBinding.testXSgYearType_service("-2001");
        assertTrue(selenium.isTextPresent("2002"));

        MSDataBinding.testXSgYearType_service("-20000");
        assertTrue(selenium.isTextPresent("20001"));

        MSDataBinding.testXSgYearType_service("01");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"01\" to year [detail] <soapenv:Detail />"));

        MSDataBinding.testXSgYearType_service("2001-12");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"2001-12\" to year [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:base64Binary type.
    */
    public void testXSbase64Binary() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSbase64BinaryType_service("B64S B64S B64S B64S");
        assertTrue(selenium.isTextPresent("B64S B64S B64S B64S"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
 Testing xs:byte type.
    */
    public void testXSByte() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSByteType_service("27");
        assertTrue(selenium.isTextPresent("27"));

        MSDataBinding.testXSByteType_service("-34");
        assertTrue(selenium.isTextPresent("-34"));

        MSDataBinding.testXSByteType_service("0");
        assertTrue(selenium.isTextPresent("0"));

        MSDataBinding.testXSByteType_service("0A");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"0A\" to byte [detail] <soapenv:Detail />"));

        MSDataBinding.testXSByteType_service("1524");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"1524\" to byte [detail] <soapenv:Detail />"));

        MSDataBinding.testXSByteType_service("INF");
        assertTrue(selenium.isTextPresent("exact:Fault: Unable to convert value \"INF\" to byte [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:boolean type.
    */
    public void testXSBoolean() throws Exception {
        MSCommon.testAccessTryit("schemaTest4");
        Thread.sleep(10000);
        MSDataBinding.testXSBooleanType_service("true");
        assertTrue(selenium.isTextPresent("true"));

        MSDataBinding.testXSBooleanType_service("false");
        assertTrue(selenium.isTextPresent("false"));

        selenium.close();
        selenium.selectWindow("");

    }

//.............................................................................................................

    /*  Testing xs:echoComplexCompositions type.
    */

    public void testXSechoComplexCompositions() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSechoComplexCompositionsType_service("<nested1>12</nested1><nested2>true</nested2>", "<nested2>true</nested2><nested3><test>just some text</test></nested3>", "element1", "element2", "element3", "10", "2001-10-26T21:32:52", "2003-10-26T21:20:50", "2000-10-26T21:10:35", "true");

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:complexEchoOptionalParams type.
    */
    public void testXScomplexEchoOptionalParams() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXScomplexEchoOptionalParamsType_service("<nested1>12</nested1><nested2>true</nested2>", "test", "123");
        assertTrue(selenium.isTextPresent("123"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
 Testing xs:returnInfinity type.
    */
    public void testXSreturnInfinity() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSreturnInfinityType_service();
        assertTrue(selenium.isTextPresent("Infinity"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:objectFunction type.
    */
    public void testXSobjectFunction() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSobjectFunctionType_service();
        assertTrue(selenium.isTextPresent("(null)"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
  Testing xs:noOutputAsComplex type.
    */
    public void testXSnoOutputAsComplex() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSnoOutputAsComplexType_service("String");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsComplexType_service("This is a test");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsComplexType_service("123");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsComplexType_service("@@##$");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsComplexType_service("<x>xxx</x>");
        assertTrue(selenium.isTextPresent("(null)"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:echoXMLNodeList type.
    */
    public void testXSechoXMLNodeList() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSechoXMLNodeListType_service("<x><xxx></x>");
        assertTrue(selenium.isTextPresent("successful"));

        MSDataBinding.testXSechoXMLNodeListType_service("<x></x>");
        assertTrue(selenium.isTextPresent("successful"));

        MSDataBinding.testXSechoXMLNodeListType_service("test");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));


        MSDataBinding.testXSechoXMLNodeListType_service("123");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));

        MSDataBinding.testXSechoXMLNodeListType_service("@##$%");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:noOutputAsSimple type.
    */
    public void testXSnoOutputAsSimple() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSnoOutputAsSimpleType_service("test");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsSimpleType_service("This is a test");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsSimpleType_service("123");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsSimpleType_service("@@@@");
        assertTrue(selenium.isTextPresent("(null)"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:echoXMLNodeList3 type.
    */
    public void testXSechoXMLNodeList3() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSechoXMLNodeList3Type_service("<x>xxx</x>");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSechoXMLNodeList3Type_service("test");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));


        MSDataBinding.testXSechoXMLNodeList3Type_service("123");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));

        MSDataBinding.testXSechoXMLNodeList3Type_service("@##$%");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
 Testing xs:complexEcho type.
    */
    public void testXScomplexEcho() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXScomplexEchoType_service("<nested1>20</nested1><nested2>true</nested2>", "test", "123");
        assertTrue(selenium.isTextPresent("123"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:echoXMLNodeList2 type.
    */
    public void testXSechoXMLNodeList2() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSechoXMLNodeList2Type_service("<x><xxx></x>");
        assertTrue(selenium.isTextPresent("successful"));
        assertTrue(selenium.isTextPresent("excess"));

        MSDataBinding.testXSechoXMLNodeList2Type_service("<x><xxx><xxx></x>");
        assertTrue(selenium.isTextPresent("successful"));
        assertTrue(selenium.isTextPresent("excess"));

        MSDataBinding.testXSechoXMLNodeList2Type_service("test");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));


        MSDataBinding.testXSechoXMLNodeList2Type_service("123");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));

        MSDataBinding.testXSechoXMLNodeList2Type_service("@##$%");
        assertTrue(selenium.isTextPresent("exact:Fault: Non-XML payload is not allowed. PayLoad inside the SOAP body needs to be an XML element. [detail] <soapenv:Detail />"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:noInputAsSimple type.
    */
    public void testXSnoInputAsSimple() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSnoInputAsSimpleType_service();
        assertTrue(selenium.isTextPresent("ok"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:echoComplexParam type.
    */
    public void testXSechoComplexParam() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSechoComplexParamType_service("<nested>stringParam</nested>");
        assertTrue(selenium.isTextPresent("exact:ws:echoComplexParamResponse"));
        assertTrue(selenium.isTextPresent("exact:ws:echoComplexParamResponseTypeparamType"));
        assertTrue(selenium.isTextPresent("stringParam"));
        assertTrue(selenium.isTextPresent("exact:ws:echoComplexParamResponseTypeparamType"));
        assertTrue(selenium.isTextPresent("exact:ws:echoComplexParamResponse"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:complexEchoArrayParams type.
    */
    public void testXScomplexEchoArrayParams() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXScomplexEchoArrayParamsType_service("<nested1>12</nested1><nested2>true</nested2>", "element1", "element2", "element3", "123");
        assertTrue(selenium.isTextPresent("123,NaN,NaN,NaN,NaN"));
        selenium.click("//input[@value='Remove simple']");
        selenium.click("button_complexEchoArrayParams");
        assertTrue(selenium.isTextPresent("123,NaN,NaN,NaN"));
        selenium.click("//input[@value='Remove simple']");
        selenium.click("button_complexEchoArrayParams");
        assertTrue(selenium.isTextPresent("123,NaN,NaN"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
 Testing xs:noInputAsComplex type.
    */
    public void testXSnoInputAsComplex() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSnoInputAsComplexType_service();
        assertTrue(selenium.isTextPresent("ok"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:noInputAsNone type.
    */
    public void testXSnoInputAsNone() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSnoInputAsNoneType_service();
        assertTrue(selenium.isTextPresent("ok"));

        selenium.close();
        selenium.selectWindow("");
    }


    /*
 Testing xs:echoXMLNode type.
    */
    public void testXSechoXMLNode() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSechoXMLNodeType_service("<x>xxx</x>");
        assertTrue(selenium.isTextPresent("successful"));

        selenium.close();
        selenium.selectWindow("");
    }

    /*
 Testing xs:noOutputAsNone type.
    */
    public void testXSnoOutputAsNone() throws Exception {
        MSCommon.testAccessTryit("schemaTest3");
        Thread.sleep(10000);
        MSDataBinding.testXSnoOutputAsNoneType_service("test");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsNoneType_service("123");
        assertTrue(selenium.isTextPresent("(null)"));

        MSDataBinding.testXSnoOutputAsNoneType_service("@@@@");
        assertTrue(selenium.isTextPresent("(null)"));

        selenium.close();
        selenium.selectWindow("");
    }

             /*
                Sign-out from Mashup
              */
     public void testSignout() throws Exception {
          SeleniumTestBase instseleniumTestBase = new SeleniumTestBase(selenium);
          instseleniumTestBase.logOutUI();
     }
}



