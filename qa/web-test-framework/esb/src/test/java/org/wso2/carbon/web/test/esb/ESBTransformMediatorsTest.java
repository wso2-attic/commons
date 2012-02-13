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

public class ESBTransformMediatorsTest extends CommonSetup{
    Properties  properties = new Properties();

    public ESBTransformMediatorsTest(String text) {
        super(text);
    }


    /*
    Adds the local entry required to test the XSLT mediator
     */
    public void addLocalEntry() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);        
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("stockquoterequest","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/xquery/xquery_commisson.xq");
        esbManageLocalEntriesTest.addSourceUrlEntry("commission","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/misc/commission.xml");
    }
    
    public void addHeaderMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding the first header mediator
        esbCommon.addRootLevelChildren("Add Child","Transform","Header");

        ESBHeaderMediatorTest esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        esbHeaderMediatorTest.addHeaderMediator("0","RESPONSE");
        esbHeaderMediatorTest.setHeaderAction("value","true");
        esbCommon.mediatorUpdate();

        //Adding the second header mediator
        esbCommon.addRootLevelChildren("Add Child","Transform","Header");

        esbHeaderMediatorTest = new ESBHeaderMediatorTest(selenium);
        esbHeaderMediatorTest.addHeaderMediator("1","RESPONSE");
        esbHeaderMediatorTest.setHeaderAction("expression","//m0:add/m1:x");
        esbHeaderMediatorTest.addHeaderExpNamespace("m0","http://samples/services");
        esbHeaderMediatorTest.addHeaderExpNamespace("m1","http://samples/services");
        esbCommon.mediatorUpdate();
    }

    public void addXsltMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding the first XSLT mediator
        esbCommon.addRootLevelChildren("Add Child","Transform","XSLT");
        ESBXSLTMediatorTest esbxsltMediatorTest = new ESBXSLTMediatorTest(selenium);
        esbxsltMediatorTest.addXSLTMediator("2","stockquoterequest");
        esbxsltMediatorTest.addXsltSource("//m0:add/m1:x");
        esbxsltMediatorTest.addSourceNamespace("m0","http://samples/services");
        esbxsltMediatorTest.addSourceNamespace("m1","http://samples/services");
        esbxsltMediatorTest.addValProperties("test","value");
        esbxsltMediatorTest.addExpressionProp("expression","//m0:add/m1:x");
        esbxsltMediatorTest.addPropNamespace("m0","http://samples/services");
        esbCommon.mediatorUpdate();
    }

    /*
    Add Fault mediators
     */
    public void addFaultMediator() throws Exception{
        ESBFaultMediatorTest esbFaultMediatorTest = new ESBFaultMediatorTest(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Transform","Fault");        
        //SOAP11 - Value type Fault String
        esbFaultMediatorTest.setSoap11Fault("3","Server");
        esbFaultMediatorTest.setFaultCodeString("SOAP11 Value String");
        esbFaultMediatorTest.setSoap11FaultGeneralInfo("soap11actor","soap11detail");
        esbCommon.mediatorUpdate();

        //SOAP11 - Expression type Fault String
        esbCommon.addRootLevelChildren("Add Child","Transform","Fault");
        esbFaultMediatorTest.setSoap11Fault("4","Server");
        esbFaultMediatorTest.setFaultCodeExpression("//m0:getQuote/m1:add");
        esbFaultMediatorTest.addNamespace("m0","http://samples/services");
        esbFaultMediatorTest.addNamespace("m1","http://samples/services");
        esbFaultMediatorTest.setSoap11FaultGeneralInfo("soap11actor","soap11detail");
        esbCommon.mediatorUpdate();

        //SOAP12 - Value type Fault String
        esbCommon.addRootLevelChildren("Add Child","Transform","Fault");
        esbFaultMediatorTest.setSoap12Fault("5","Receiver");
        esbFaultMediatorTest.setFaultCodeString("SOAP12 Value String");
        esbFaultMediatorTest.setSoap12FaultGeneralInfo("soap12actor","soap12node","soap12detail");
        esbCommon.mediatorUpdate();

        //SOAP12 - Expression type Fault String
        esbCommon.addRootLevelChildren("Add Child","Transform","Fault");        
        esbFaultMediatorTest.setSoap12Fault("6","Receiver");
        esbFaultMediatorTest.setFaultCodeExpression("//m0:getQuote/m1:add");
        esbFaultMediatorTest.setNsProp();
        esbFaultMediatorTest.addNamespace("m0","http://samples/services");
        esbFaultMediatorTest.addNamespace("m1","http://samples/services");
        esbFaultMediatorTest.setSoap12FaultGeneralInfo("soap12actor","soap12node","soap12detail");
        esbCommon.mediatorUpdate();
    }

    /*
    Adding Xquery mediators
     */
    public void addXqueryMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBXqueryMediatorTest esbXqueryMediatorTest = new ESBXqueryMediatorTest(selenium);
        esbCommon.addRootLevelChildren("Add Child","Transform","XQuery");        
        esbXqueryMediatorTest.addXqueryKey("7","stockquoterequest");
        esbXqueryMediatorTest.addTarget("//m0:add/m1:x");
        esbXqueryMediatorTest.addTargetNamespace("m0","http://services/samples");
        esbXqueryMediatorTest.addTargetNamespace("m1","http://services/samples");        

        esbXqueryMediatorTest.addVariables("INT","int","10");
        esbXqueryMediatorTest.addVariables("INTEGER","integer","20");
        esbXqueryMediatorTest.addVariables("BOOLEAN","boolean","true");
        esbXqueryMediatorTest.addVariables("BYTE","byte","1024");
        esbXqueryMediatorTest.addVariables("DOUBLE","double","50.00");
        esbXqueryMediatorTest.addVariables("SHORT","short","-32700");
        esbXqueryMediatorTest.addVariables("LONG","long","-9223372036854700000");
        esbXqueryMediatorTest.addVariables("FLOAT","float","100.23");
        esbXqueryMediatorTest.addVariables("STRING","string","This is a string");
        esbXqueryMediatorTest.addExpressionVariable("DOCUMENT","payload",null,"stockquoterequest");
        esbXqueryMediatorTest.addExpressionVariable("DOCUMENT_ELEMENT","commission",null,"commission");
        esbXqueryMediatorTest.addExpressionVariable("ELEMENT","payload",null,"stockquoterequest");
        esbCommon.mediatorUpdate();
    }

    /*
    This method will add all the mediators which are under the 'Transform' category to a sequence and update the Synapse confoguration
     */
    public void testAddTransformMediators() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
        addLocalEntry();
        esbCommon.addSequence("sequence_transform");
        addHeaderMediator();
        addXsltMediator();
        addFaultMediator();
        addXqueryMediator();

        esbCommon.sequenceSave();
        seleniumTestBase.logOutUI();
    }
}
