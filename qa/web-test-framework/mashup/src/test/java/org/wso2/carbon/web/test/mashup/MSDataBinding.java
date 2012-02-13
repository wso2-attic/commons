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


public class MSDataBinding extends CommonSetup {

    public MSDataBinding(String text) {
        super(text);
    }



      /*
     Accessing tryit to test JS:enumeration type.
    */
    public static void testJSEnumerationType_Service(String input_testechoEnumeration_param) throws Exception {
        selenium.click("link=echoEnumeration");
        selenium.select("input_echoEnumeration_param_0", input_testechoEnumeration_param);
        selenium.click("button_echoEnumeration");
        MSCommon.testContextRoot();
    }

     /*
     Accessing tryit to test JS:number type.
    */
    public static void testJSnumberType_Service(String input_testnumber_param) throws Exception {
        selenium.click("link=echoJSnumber");
        selenium.type("input_echoJSnumber_param_0", input_testnumber_param);
        selenium.click("button_echoJSnumber");
        MSCommon.testContextRoot();
    }

     /*
     Accessing tryit to test JS:Boolean type.
    */
    public static void testJSBooleanType_Service(String input_testBoolean_param) throws Exception {
        selenium.click("link=echoJSBoolean");
        selenium.click("input_echoJSBoolean_param_0");
        selenium.click("button_echoJSBoolean");
        MSCommon.testContextRoot();
    }

     /*
     Accessing tryit to test JS:string type.
    */
    public static void testJSstringType_Service(String input_teststring_param) throws Exception {
        selenium.click("link=echoJSstring");
        selenium.type("input_echoJSstring_param_0",input_teststring_param);
        selenium.click("button_echoJSstring");
        MSCommon.testContextRoot(); 
    }

    /*
     Accessing tryit to test JS:Number type.
    */
    public static void testJSNumberType_Service(String input_testNumber_param) throws Exception {
        selenium.click("link=echoJSNumber");
        selenium.type("input_echoJSNumber_param_0", input_testNumber_param);
        selenium.click("button_echoJSNumber");
        MSCommon.testContextRoot();
    }

     /*
     Accessing tryit to test JS:boolean type.
    */
    public static void testJSbooleanType_Service(String input_testboolean_param) throws Exception {
        selenium.click("link=echoJSboolean");
        selenium.click("input_echoJSboolean_param_0");
        selenium.click("button_echoJSboolean");
        MSCommon.testContextRoot();
    }

    /*
     Accessing tryit to test JS:ArrayWithProperties type.
    */
    public static void testJSArrayWithPropertiesType_Service() throws Exception {
        selenium.click("link=returnJSArrayWithProperties");
		selenium.click("button_returnJSArrayWithProperties");
        MSCommon.testContextRoot();
    }



    /*
     Accessing tryit to test JS:String type.
    */
    public static void testJSStringType_Service(String input_testString_param) throws Exception {
        selenium.click("link=echoJSString");
        selenium.type("input_echoJSString_param_0",input_testString_param);
        selenium.click("button_echoJSString");
        MSCommon.testContextRoot();
    }

    /*
     Accessing tryit to test JS:date type.
    */
    public static void testJSdateType_Service(String input_testdate_param) throws Exception {
        selenium.click("link=echoJSdate");
        selenium.type("input_echoJSdate_param_0",input_testdate_param);
        selenium.click("button_echoJSdate");
        MSCommon.testContextRoot();
    }

    /*
     Accessing tryit to test JS:Object type.
    */
    public static void testJSObjectType_Service(String input_testObject_param_1,String input_testObject_param_2,String input_testObject_param_3) throws Exception {
        selenium.click("link=returnJSObject");
        selenium.type("input_returnJSObject_param1_0",input_testObject_param_1);
        selenium.type("input_returnJSObject_param2_0",input_testObject_param_2);
        selenium.click("input_returnJSObject_param3_0");
        selenium.click("button_returnJSObject");
        MSCommon.testContextRoot();
    }

    /*
     Accessing tryit to test JS:stringArray type.
    */
    public static void testJSstringArrayType_Service(String input_teststringArray_param_1,String input_teststringArray_param_2,String input_teststringArray_param_3) throws Exception {
        selenium.click("link=echoStringArray");
        selenium.type("input_echoStringArray_param_0",input_teststringArray_param_1);
        selenium.click("//input[@value='Add param']");
        selenium.type("input_echoStringArray_param_1", input_teststringArray_param_2);
        selenium.click("//input[@value='Add param']");
		selenium.type("input_echoStringArray_param_2", input_teststringArray_param_3);
		selenium.click("button_echoStringArray");
        MSCommon.testContextRoot();
    }

    /*
     Accessing tryit to test JS:ArrayWithIndices type.
    */
    public static void testJSArrayWithIndicesType_Service() throws Exception {
        selenium.click("link=returnJSArrayWithIndices");
		selenium.click("button_returnJSArrayWithIndices");
        MSCommon.testContextRoot();
    }

    /*
     Accessing tryit to test JS:Date type.
    */
    public static void testJSDateType_Service(String input_testDate_param) throws Exception {
        selenium.click("link=echoJSDate");
        selenium.type("input_echoJSDate_param_0",input_testDate_param);
        selenium.click("button_echoJSDate");
        MSCommon.testContextRoot();
    }

//.................................................................................................

    /*
   Accessing tryit to test xs:Name type.
    */
    public static void testXSStringType_service(String input_echoXSName_param) throws Exception {
        selenium.click("link=echoXSName");
        selenium.type("input_echoXSName_param_0", input_echoXSName_param);
        selenium.click("button_echoXSName");
        MSCommon.testContextRoot();
    }


    /*
   Accessing tryit to test xs:Long type.
    */
    public static void testXSLongType(String input_echoXSlong_param) throws Exception {
        selenium.click("link=echoXSlong");
        selenium.type("input_echoXSlong_param_0", input_echoXSlong_param);
        selenium.click("button_echoXSlong");
        MSCommon.testContextRoot();
    }


    /*
   Accessing tryit to test xs:Time type.
    */
    public static void testXSTimeType_service(String input_echoXStime_param) throws Exception {
        selenium.click("link=echoXStime");
        selenium.type("input_echoXStime_param_0", input_echoXStime_param);
        selenium.click("button_echoXStime");
        MSCommon.testContextRoot();
    }


    /*
   Accessing tryit to test xs:Double type.
    */
    public static void testXSDoubleType_service(String input_echoXSdouble_param) throws Exception {
        selenium.click("link=echoXSdouble");
        selenium.type("input_echoXSdouble_param_0", input_echoXSdouble_param);
        selenium.click("button_echoXSdouble");
        MSCommon.testContextRoot();
    }


    /*
   Accessing tryit to test xs:duration type.
    */
    public static void testXSDurationType_service(String input_echoXSduration_param) throws Exception {
        selenium.click("link=echoXSduration");
        selenium.type("input_echoXSduration_param_0", input_echoXSduration_param);
        selenium.click("button_echoXSduration");
        MSCommon.testContextRoot();
    }


    /*
   Accessing tryit to test xs:Decimal type.
    */
    public static void testXSDecimalType_service(String input_echoXSDecimal_param) throws Exception {
        selenium.click("link=echoXSdecimal");
        selenium.type("input_echoXSdecimal_param_0", input_echoXSDecimal_param);
        selenium.click("button_echoXSdecimal");
        MSCommon.testContextRoot();
    }


    /*
   Accessing tryit to test xs:anyuri type.
    */
    public static void testXSAnyURIType_service(String input_echoXSanyURI_param) throws Exception {
        selenium.click("link=echoXSanyURI");
        selenium.type("input_echoXSanyURI_param_0", input_echoXSanyURI_param);
        selenium.click("button_echoXSanyURI");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:nonPositiveInteger type.
    */
    public static void testXSNonPositiveIntegerType_service(String input_echoXSnonPositiveInteger_param) throws Exception {
        selenium.click("link=echoXSnonPositiveInteger");
        selenium.type("input_echoXSnonPositiveInteger_param_0", input_echoXSnonPositiveInteger_param);
        selenium.click("button_echoXSnonPositiveInteger");
        MSCommon.testContextRoot();
    }

      /*
   Accessing tryit to test xs:negativeInteger type.
    */
    public static void testXSNegativeIntegerType_service(String input_echoXSnegativeInteger_param) throws Exception {
        selenium.click("link=echoXSnegativeInteger");
        selenium.type("input_echoXSnegativeInteger_param_0", input_echoXSnegativeInteger_param);
        selenium.click("button_echoXSnegativeInteger");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:gYearMonth type.
    */
    public static void testXSgYearMonthType_service(String input_echoXSgYearMonth_param) throws Exception {
        selenium.click("link=echoXSgYearMonth");
        selenium.type("input_echoXSgYearMonth_param_0", input_echoXSgYearMonth_param);
        selenium.click("button_echoXSgYearMonth");
        MSCommon.testContextRoot();
    }

      /*
   Accessing tryit to test xs:unsignedByte type.
    */
    public static void testXSUnsignedByteType_service(String input_echoXSUnsignedByte_param) throws Exception {
        selenium.click("link=echoXSunsignedByte");
        selenium.type("input_echoXSunsignedByte_param_0", input_echoXSUnsignedByte_param);
        selenium.click("button_echoXSunsignedByte");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:normalizedString type.
    */
    public static void testXSNormalizedStringType_service(String input_echoXSNormalizedString_param) throws Exception {
        selenium.click("link=echoXSnormalizedString");
        selenium.type("input_echoXSnormalizedString_param_0", input_echoXSNormalizedString_param);
        selenium.click("button_echoXSnormalizedString");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:unsignedShort type.
    */
    public static void testXSUnsignedShortType_service(String input_echoXSUnsignedShort_param) throws Exception {
        selenium.click("link=echoXSunsignedShort");
        selenium.type("input_echoXSunsignedShort_param_0", input_echoXSUnsignedShort_param);
        selenium.click("button_echoXSunsignedShort");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:dateTime type.
    */
    public static void testXSDateTimeType_service(String input_echoXSDateTime_param) throws Exception {
        selenium.click("link=echoXSdateTime");
        selenium.type("input_echoXSdateTime_param_0", input_echoXSDateTime_param);
        selenium.click("button_echoXSdateTime");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:short type.
    */
    public static void testXSShortType_service(String input_echoXSShort_param) throws Exception {
        selenium.click("link=echoXSshort");
        selenium.type("input_echoXSshort_param_0", input_echoXSShort_param);
        selenium.click("button_echoXSshort");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:gMonth type.
    */
    public static void testXSgMonthType_service(String input_echoXSgMonth_param) throws Exception {
        selenium.click("link=echoXSgMonth");
        selenium.type("input_echoXSgMonth_param_0", input_echoXSgMonth_param);
        selenium.click("button_echoXSgMonth");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:QName type.
    */
    public static void testXSQNameType_service(String input_echoXSQName_param_ns,String input_echoXSQName_param) throws Exception {
        selenium.click("link=echoXSQName");
        selenium.type("input_echoXSQName_param_ns_0", input_echoXSQName_param_ns);
		selenium.type("input_echoXSQName_param_0", input_echoXSQName_param);
		selenium.click("button_echoXSQName");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:nonNegativeInteger type.
    */
    public static void testXSNonNegativeIntegerType_service(String input_echoXSNonNegativeInteger_param) throws Exception {
        selenium.click("link=echoXSnonNegativeInteger");
        selenium.type("input_echoXSnonNegativeInteger_param_0", input_echoXSNonNegativeInteger_param);
        selenium.click("button_echoXSnonNegativeInteger");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:float type.
    */
    public static void testXSFloatType_service(String input_echoXSFloat_param) throws Exception {
        selenium.click("link=echoXSfloat");
        selenium.type("input_echoXSfloat_param_0", input_echoXSFloat_param);
        selenium.click("button_echoXSfloat");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:positiveInteger type.
    */
    public static void testXSPositiveIntegerType_service(String input_echoXSPositiveInteger_param) throws Exception {
        selenium.click("link=echoXSpositiveInteger");
        selenium.type("input_echoXSpositiveInteger_param_0", input_echoXSPositiveInteger_param);
        selenium.click("button_echoXSpositiveInteger");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:hexBinary type.
    */
    public static void testXShexBinaryType_service(String input_echoXShexBinary_param) throws Exception {
        selenium.click("link=echoXShexBinary");
        selenium.type("input_echoXShexBinary_param_0", input_echoXShexBinary_param);
        selenium.click("button_echoXShexBinary");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:notation type.
    */
    public static void testXSNOTATIONType_service(String input_echoXSNOTATION_param) throws Exception {
        selenium.click("link=echoXSNOTATION");
        selenium.type("input_echoXSNOTATION_param_0", input_echoXSNOTATION_param);
        selenium.click("button_echoXSNOTATION");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:string type.
    */
    public static void testXSstringType_service(String input_echoXSstring_param) throws Exception {
        selenium.click("link=echoXSstring");
        selenium.type("input_echoXSstring_param_0", input_echoXSstring_param);
        selenium.click("button_echoXSstring");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:date type.
    */
    public static void testXSDateType_service(String input_echoXSDate_param) throws Exception {
        selenium.click("link=echoXSdate");
        selenium.type("input_echoXSdate_param_0", input_echoXSDate_param);
        selenium.click("button_echoXSdate");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:gDay type.
    */
    public static void testXSgDayType_service(String input_echoXSgDay_param) throws Exception {
        selenium.click("link=echoXSgDay");
        selenium.type("input_echoXSgDay_param_0", input_echoXSgDay_param);
        selenium.click("button_echoXSgDay");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:NCName type.
    */
    public static void testXSNCNameType_service(String input_echoXSNCName_param) throws Exception {
        selenium.click("link=echoXSNCName");
        selenium.type("input_echoXSNCName_param_0", input_echoXSNCName_param);
        selenium.click("button_echoXSNCName");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:unsignedLong type.
    */
    public static void testXSUnsignedLongType_service(String input_echoXSUnsignedLong_param) throws Exception {
        selenium.click("link=echoXSunsignedLong");
        selenium.type("input_echoXSunsignedLong_param_0", input_echoXSUnsignedLong_param);
        selenium.click("button_echoXSunsignedLong");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:gMonthDay type.
    */
    public static void testXSgMonthDayType_service(String input_echoXSgMonthDay_param) throws Exception {
        selenium.click("link=echoXSgMonthDay");
        selenium.type("input_echoXSgMonthDay_param_0", input_echoXSgMonthDay_param);
        selenium.click("button_echoXSgMonthDay");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:token type.
    */
    public static void testXSTokenType_service(String input_echoXSToken_param) throws Exception {
        selenium.click("link=echoXStoken");
        selenium.type("input_echoXStoken_param_0", input_echoXSToken_param);
        selenium.click("button_echoXStoken");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:integer type.
    */
    public static void testXSIntegerType_service(String input_echoXSInteger_param) throws Exception {
        selenium.click("link=echoXSinteger");
        selenium.type("input_echoXSinteger_param_0", input_echoXSInteger_param);
        selenium.click("button_echoXSinteger");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:language type.
    */
    public static void testXSLanguageType_service(String input_echoXSLanguage_param) throws Exception {
        selenium.click("link=echoXSlanguage");
        selenium.type("input_echoXSlanguage_param_0", input_echoXSLanguage_param);
        selenium.click("button_echoXSlanguage");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:unsignedInt type.
    */
    public static void testXSUnsignedIntType_service(String input_echoXSUnsignedInt_param) throws Exception {
        selenium.click("link=echoXSunsignedInt");
        selenium.type("input_echoXSunsignedInt_param_0", input_echoXSUnsignedInt_param);
        selenium.click("button_echoXSunsignedInt");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:gYear type.
    */
    public static void testXSgYearType_service(String input_echoXSgYear_param) throws Exception {
        selenium.click("link=echoXSgYear");
        selenium.type("input_echoXSgYear_param_0", input_echoXSgYear_param);
        selenium.click("button_echoXSgYear");
        MSCommon.testContextRoot();
    }

      /*
   Accessing tryit to test xs:base64Binary type.
    */
    public static void testXSbase64BinaryType_service(String input_echoXSbase64Binary_param) throws Exception {
        selenium.click("link=echoXSbase64Binary");
        selenium.type("input_echoXSbase64Binary_param_0", input_echoXSbase64Binary_param);
        selenium.click("button_echoXSbase64Binary");
        MSCommon.testContextRoot();
    }

     /*
   Accessing tryit to test xs:int type.
    */
    public static void testXSIntType_service(String input_echoXSInt_param) throws Exception {
        selenium.click("link=echoXSint");
        selenium.type("input_echoXSint_param_0", input_echoXSInt_param);
        selenium.click("button_echoXSint");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:byte type.
    */
    public static void testXSByteType_service(String input_echoXSByte_param) throws Exception {
        selenium.click("link=echoXSbyte");
        selenium.type("input_echoXSbyte_param_0", input_echoXSByte_param);
        selenium.click("button_echoXSbyte");
        MSCommon.testContextRoot();
    }

    /*
   Accessing tryit to test xs:boolean type.
    */
    public static void testXSBooleanType_service(String input_echoXSBoolean_param) throws Exception {
        selenium.click("link=echoXSboolean");
        selenium.click("input_echoXSboolean_param_0");
        selenium.click("button_echoXSboolean");
        MSCommon.testContextRoot();
    }

//................................................................................................................................

    /*
      Accessing tryit to test xs:echoComplexCompositions type.
       */
        public static void testXSechoComplexCompositionsType_service(String input_echoComplexCompositionsTypecomplex1Type_0,String input_echoComplexCompositionsTypecomplex2Type_0,String input_echoComplexCompositions_simple1_0,String input_echoComplexCompositions_simple1_1,String input_echoComplexCompositions_simple1_2,String input_echoComplexCompositions_simple2_0,String input_echoComplexCompositions_simple3_0,String input_echoComplexCompositions_simple3_1,String input_echoComplexCompositions_simple3_2,String input_echoComplexCompositions_simple4_0) throws Exception {
            selenium.click("link=echoComplexCompositions");
		    selenium.type("input_echoComplexCompositions_echoComplexCompositionsTypecomplex1Type_0", input_echoComplexCompositionsTypecomplex1Type_0);
		    selenium.type("input_echoComplexCompositions_echoComplexCompositionsTypecomplex2Type_0", input_echoComplexCompositionsTypecomplex2Type_0);
		    selenium.type("input_echoComplexCompositions_simple1_0", input_echoComplexCompositions_simple1_0);
		    selenium.click("//input[@value='Add simple1']");
		    selenium.type("input_echoComplexCompositions_simple1_1", input_echoComplexCompositions_simple1_1);
		    selenium.click("//input[@value='Add simple1']");
		    selenium.type("input_echoComplexCompositions_simple1_2", input_echoComplexCompositions_simple1_2);
		    selenium.type("input_echoComplexCompositions_simple2_0", input_echoComplexCompositions_simple2_0);
		    selenium.type("input_echoComplexCompositions_simple3_0", input_echoComplexCompositions_simple3_0);
		    selenium.click("//input[@value='Add simple3']");
		    selenium.type("input_echoComplexCompositions_simple3_1", input_echoComplexCompositions_simple3_1);
		    selenium.click("//input[@value='Add simple3']");
		    selenium.type("input_echoComplexCompositions_simple3_2", input_echoComplexCompositions_simple3_2);
		    selenium.click("input_echoComplexCompositions_simple4_0");
		    selenium.click("button_echoComplexCompositions");
            MSCommon.testContextRoot();
            Thread.sleep(100);
        } 



      /*
      Accessing tryit to test xs:complexEchoOptionalParams type.
       */
       public static void testXScomplexEchoOptionalParamsType_service(String input_complexEchoOptionalParamsTypecomplexType_0,String input_complexEchoOptionalParams_simple_0,String input_complexEchoOptionalParams_simple2_0) throws Exception {
          selenium.click("link=complexEchoOptionalParams");
		  selenium.type("input_complexEchoOptionalParams_complexEchoOptionalParamsTypecomplexType_0", input_complexEchoOptionalParamsTypecomplexType_0);
          selenium.type("input_complexEchoOptionalParams_simple_0", input_complexEchoOptionalParams_simple_0);
		  selenium.type("input_complexEchoOptionalParams_simple2_0", input_complexEchoOptionalParams_simple2_0);
		  selenium.click("button_complexEchoOptionalParams");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       } 


    /*
    Accessing tryit to test xs:returnInfinity type.
     */
     public static void testXSreturnInfinityType_service() throws Exception {
        selenium.click("link=returnInfinity");
		selenium.click("button_returnInfinity");
        MSCommon.testContextRoot();
        Thread.sleep(100);
     }
   
    
      /*
    Accessing tryit to test xs:objectFunction type.
     */
     public static void testXSobjectFunctionType_service() throws Exception {
        selenium.click("link=objectFunction");
		selenium.click("button_objectFunction");
        MSCommon.testContextRoot();
        Thread.sleep(100);
     }

    /*
      Accessing tryit to test xs:noOutputAsComplex type.
       */
       public static void testXSnoOutputAsComplexType_service(String input_echoXSnoOutputAsComplex_param) throws Exception {
          selenium.click("link=noOutputAsComplex");
		  selenium.type("input_noOutputAsComplex_param_0", input_echoXSnoOutputAsComplex_param);
		  selenium.click("button_noOutputAsComplex");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

    /*
      Accessing tryit to test xs:echoXMLNodeList type.
       */
       public static void testXSechoXMLNodeListType_service(String input_echoXSechoXMLNodeList_param) throws Exception {
          selenium.click("link=echoXMLNodeList");
		  selenium.type("input_echoXMLNodeList__0", input_echoXSechoXMLNodeList_param);
		  selenium.click("button_echoXMLNodeList");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

    /*
      Accessing tryit to test xs:noOutputAsSimple type.
       */
       public static void testXSnoOutputAsSimpleType_service(String input_echoXSnoOutputAsSimple_param) throws Exception {
          selenium.click("link=noOutputAsSimple");
		  selenium.type("input_noOutputAsSimple_param_0", input_echoXSnoOutputAsSimple_param);
		  selenium.click("button_noOutputAsSimple");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

     /*
      Accessing tryit to test xs:echoXMLNodeList3 type.
       */
       public static void testXSechoXMLNodeList3Type_service(String input_echoXSechoXMLNodeList3_param) throws Exception {
          selenium.click("link=echoXMLNodeList3");
		  selenium.type("input_echoXMLNodeList3__0", input_echoXSechoXMLNodeList3_param);
		  selenium.click("button_echoXMLNodeList3");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

       /*
      Accessing tryit to test xs:complexEcho type.
       */
       public static void testXScomplexEchoType_service(String input_complexEchoTypecomplexType_0,String input_complexEcho_simple_0,String input_complexEcho_simple2_0) throws Exception {
          selenium.click("link=complexEcho");
		  selenium.type("input_complexEcho_complexEchoTypecomplexType_0", input_complexEchoTypecomplexType_0);
          selenium.type("input_complexEcho_simple_0", input_complexEcho_simple_0);
		  selenium.type("input_complexEcho_simple2_0", input_complexEcho_simple2_0);
		  selenium.click("button_complexEcho");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       } 

    /*
      Accessing tryit to test xs:echoXMLNodeList2 type.
       */
       public static void testXSechoXMLNodeList2Type_service(String input_echoXSechoXMLNodeList2_param) throws Exception {
          selenium.click("link=echoXMLNodeList2");
		  selenium.type("input_echoXMLNodeList2__0", input_echoXSechoXMLNodeList2_param);
		  selenium.click("button_echoXMLNodeList2");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

    /*
      Accessing tryit to test xs:noInputAsSimple type.
       */
      public static void testXSnoInputAsSimpleType_service() throws Exception {
         selenium.click("link=noInputAsSimple");
		 selenium.click("button_noInputAsSimple");
         MSCommon.testContextRoot();
         Thread.sleep(100);
      }

        /*
      Accessing tryit to test xs:echoComplexParam type.
       */
       public static void testXSechoComplexParamType_service(String input_echoComplexParamTypeparamType_0) throws Exception {
          selenium.click("link=echoComplexParam");
		  selenium.type("input_echoComplexParam_echoComplexParamTypeparamType_0", input_echoComplexParamTypeparamType_0);
		  selenium.click("button_echoComplexParam");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

         /*
      Accessing tryit to test xs:complexEchoArrayParams type.
       */
       public static void testXScomplexEchoArrayParamsType_service(String input__complexEchoArrayParamsTypecomplexType_0,String input_complexEchoArrayParams_simple_0,String input_complexEchoArrayParams_simple_1,String input_complexEchoArrayParams_simple_2,String input_complexEchoArrayParams_simple2_0) throws Exception {
          selenium.click("link=complexEchoArrayParams");
          selenium.type("input_complexEchoArrayParams_complexEchoArrayParamsTypecomplexType_0", input__complexEchoArrayParamsTypecomplexType_0);
		  selenium.type("input_complexEchoArrayParams_simple_0", input_complexEchoArrayParams_simple_0);
		  selenium.click("//input[@value='Add simple']");
		  selenium.type("input_complexEchoArrayParams_simple_1", input_complexEchoArrayParams_simple_1);
		  selenium.click("//input[@value='Add simple']");
		  selenium.type("input_complexEchoArrayParams_simple_2", input_complexEchoArrayParams_simple_2);
		  selenium.type("input_complexEchoArrayParams_simple2_0", input_complexEchoArrayParams_simple2_0);
		  selenium.click("button_complexEchoArrayParams");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       } 

        /*
      Accessing tryit to test xs:noInputAsComplex type.
       */
       public static void testXSnoInputAsComplexType_service() throws Exception {
          selenium.click("link=noInputAsComplex");
		  selenium.click("button_noInputAsComplex");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

      /*
      Accessing tryit to test xs:noInputAsNone type.
       */
       public static void testXSnoInputAsNoneType_service() throws Exception {
          selenium.click("link=noInputAsNone");
		  selenium.click("button_noInputAsNone");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

    /*
      Accessing tryit to test xs:echoXMLNode type.
       */
       public static void testXSechoXMLNodeType_service(String input_echoXSechoXMLNode_param) throws Exception {
          selenium.click("link=echoXMLNode");
          selenium.type("input_echoXMLNode__0", input_echoXSechoXMLNode_param);
		  selenium.click("button_echoXMLNode");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

    /*
      Accessing tryit to test xs:noOutputAsNone type.
       */
       public static void testXSnoOutputAsNoneType_service(String input_echoXSnoOutputAsNone_param) throws Exception {
          selenium.click("link=noOutputAsNone");
          selenium.type("input_noOutputAsNone_param_0", input_echoXSnoOutputAsNone_param);
		  selenium.click("button_noOutputAsNone");
          MSCommon.testContextRoot();
          Thread.sleep(100);
       }

       /*
        Accessing tryit to test allCommons service--->This method is used in "ModuleManagementTest.java" class.
        */
       public static void testallCommons_Service(String input_echoString_param) throws Exception{
           MSCommon.testAccessTryit("allCommons");
           selenium.click("link=echoString");
           selenium.type("input_echoString_param1_0", input_echoString_param);
		   selenium.click("button_echoString");
           MSCommon.testContextRoot();
           Thread.sleep(100);
           assertTrue(selenium.isTextPresent("Fault: Access deny for a caller "));
           selenium.close();
           selenium.selectWindow("");
       } 
}
