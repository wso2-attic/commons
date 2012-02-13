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

public class ESBFilterMediatorsTest extends CommonSetup{
    Properties properties = new Properties();

    public ESBFilterMediatorsTest(String text) {
        super(text);
    }

   /*
    Method to create endpoint
     */
    public void addEndpoint() throws Exception{
       //Adding an endpoint which will be refered through the proxy wizard
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.viewEndpoints();
        boolean endpoint = selenium.isTextPresent("router_epr");
        System.out.println(endpoint);

        if (endpoint) {
            //Do nothing
        } else {
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("router_epr","http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }
   }

    /*
    Adding local entries to be used for the Send mediator
     */
    public void addLocalEntries() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        //Adding the security policy and rm policy as local entries
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("validate_schema","file:"+esbCommon.getCarbonHome()+"/repository/samples/resources/validate/validate.xsd");
    }

    /*
    Adding an Out mediator
     */
    public void addOutMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Filter","Out");
    }

    /*
    Adding an In mediator
     */
    public void addInMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Filter","In");
    }

    /*
    Adding an Validate mediator
     */
    public void addValidateMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");
        ESBValidateMediatorTest esbValidateMediatorTest = new ESBValidateMediatorTest(selenium);
        esbValidateMediatorTest.addValidateMediatorSchemaKey("2","validate_schema");
        esbValidateMediatorTest.addSource("//m0:add");
        esbValidateMediatorTest.addValidateNamespace("m0","https://services/samples");
        esbValidateMediatorTest.setNsLevel();
        esbCommon.mediatorUpdate();
        esbCommon.addMediators("Add Child","2","Core","Log");

        esbCommon.addRootLevelChildren("Add Child","Filter","Validate");
        esbValidateMediatorTest.addValidateMediatorSchemaKey("3","validate_schema");
        esbValidateMediatorTest.addSource("//m0:div");
        esbValidateMediatorTest.addValidateNamespace("m0","https://services/samples");
        esbCommon.mediatorUpdate();
        esbCommon.addMediators("Add Child","3","Core","Log");
    }

    /*
    Adding an Filter mediator
     */
    public void addFilterMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBLogMediatorTest esbLogMediatorTest = new ESBLogMediatorTest(selenium);
        ESBSendMediatorTest esbSendMediatorTest = new ESBSendMediatorTest(selenium);
        ESBFilterMediatorTest esbFilterMediatorTest = new ESBFilterMediatorTest(selenium);

        //Adding Source and Regular Expression Filter mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Filter");
        esbFilterMediatorTest.addSourceRegex("4","get-property('To')",".*/StockQuote.*");
        esbFilterMediatorTest.addFilterNamespace("m0","http://services/samples0");
        esbFilterMediatorTest.addFilterNamespace("m1","http://services/samples1");
        esbFilterMediatorTest.setNsLevel();
        esbCommon.mediatorUpdate();

        esbFilterMediatorTest.addThenChildMediators("4.0","Core", "Log");
        esbLogMediatorTest.addLogMediator("4.0.0","Full");
        esbLogMediatorTest.addLogPropety("text","Value","FilterMediatorLog");
        esbLogMediatorTest.setPropNo();
        esbCommon.mediatorUpdate();
        esbFilterMediatorTest.addThenChildMediators("4.0","Core", "Log");
        esbFilterMediatorTest.addElseChildMediators("4.1","Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("4.1.0");
        esbCommon.mediatorUpdate();

        //Adding Xpath Filter mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Filter");
        esbFilterMediatorTest.addXpath("5","//m0:getQuote/m0:symbol");
        esbFilterMediatorTest.addFilterNamespace("m0","http://services/samples0");
        esbFilterMediatorTest.addFilterNamespace("m1","http://services/samples1");
        esbCommon.mediatorUpdate();

        esbFilterMediatorTest.addThenChildMediators("5.0","Core", "Log");
        esbLogMediatorTest.addLogMediator("5.0.0","Full");
        esbLogMediatorTest.addLogPropety("text","Value","FilterMediatorLog");
        esbCommon.mediatorUpdate();
        esbFilterMediatorTest.addThenChildMediators("5.0","Core", "Log");
        esbFilterMediatorTest.addElseChildMediators("5.1","Core", "Send");
        esbSendMediatorTest.addNormalSendMediator("5.1.0");
        esbCommon.mediatorUpdate();
    }    

    /*
    Adding an Switch mediator
     */
    public void addSwitchMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBSwitchMediatorTest esbSwitchMediatorTest = new ESBSwitchMediatorTest(selenium);
        ESBPropertyMediatorTest esbPropertyMediatorTest = new ESBPropertyMediatorTest(selenium);
        
        //Adding a Switch mediator
        esbCommon.addRootLevelChildren("Add Child","Filter","Switch");
        esbSwitchMediatorTest.addSwitchMediator("6","//m0:getQuote/m0:request/m0:symbol");
        esbSwitchMediatorTest.addSwitchNamespace("m0","http://services.samples/xsd");
        esbCommon.mediatorUpdate();

        //Adding the first level Case mediator
        esbSwitchMediatorTest.addCase("6","IBM","6.0");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("6.0", "Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("6.0.0","symbol","set");
        esbPropertyMediatorTest.addPropertyMediator("value","Great stock - IBM","Synapse");
        esbCommon.mediatorUpdate();

        //Adding the second level Case mediator
        esbSwitchMediatorTest.addCase("6","MSFT","6.1");
        esbCommon.mediatorUpdate();
        esbSwitchMediatorTest.addCaseChildMediator("6.1", "Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("6.1.0","symbol","set");
        esbPropertyMediatorTest.addPropertyMediator("value","Are you sure? - MSFT","Synapse");
        esbCommon.mediatorUpdate();

        //Adding the Default Case
        esbSwitchMediatorTest.addDefaultCase("6");
        esbSwitchMediatorTest.addDefaultChildMediator("6.2","Core","Property");
        esbPropertyMediatorTest.addBasicPropInfo("6.2.0","symbol","set");
        esbPropertyMediatorTest.addPropertyMediator("expression","fn:concat('Normal Stock - ', //m0:getQuote/m0:request/m0:symbol)","Synapse");
        esbCommon.mediatorUpdate();
    }

    /*
    Adding an Router mediator
     */
    public void addRouterMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        ESBRouterMediatorTest esbRouterMediatorTest = new ESBRouterMediatorTest(selenium);
        ESBRouterMediatorUITest esbRouterMediatorUITest = new ESBRouterMediatorUITest(selenium);

        //Adding a Router mediator with None Endpoint and Sequence
        esbCommon.addRootLevelChildren("Add Child","Filter","Router");
        esbRouterMediatorTest.addRouterMediator("7","Yes");
        esbRouterMediatorTest.clickAddRoute();
        esbCommon.mediatorUpdate();

        esbRouterMediatorUITest.verifyRouterMediator("7");
        esbRouterMediatorTest.addRoute("7.0","Yes","//m0:add/m1:x");
        esbRouterMediatorTest.addRouterNamespace("m0", "http://services/samples0");
        esbRouterMediatorTest.addRouterNamespace("m1", "http://services/samples1");
        esbRouterMediatorTest.setNsLevel();
        esbRouterMediatorTest.addRoutePattern("//m0:add");
        esbCommon.mediatorUpdate();

        esbRouterMediatorUITest.testVerifyRouterConfiguration();
        esbRouterMediatorTest.addTarget("7.0.0");
        esbRouterMediatorTest.addTargetSoapAction("urn:add");
        esbRouterMediatorTest.addTargetToAddress("http://localhost:9000/services/SimpleStockQuoteService");
        esbRouterMediatorTest.addTargetNoneSequence();
        esbRouterMediatorTest.addTargetNoneEndpoint();
        esbCommon.mediatorUpdate();
		assertTrue(selenium.isTextPresent("Please select a valid sequence or a valid endpoint"));
		selenium.click("//button[@type='button']");
        esbRouterMediatorTest.addTargetAnonEndpoint("http://localhost:9000/services/SimpleStockQuoteService");
        esbCommon.mediatorUpdate();
        esbRouterMediatorUITest.testVerifyTargetConfiguration();

        //Adding a Router mediator with Anon Endpoint and Anon Sequence
        esbCommon.addRootLevelChildren("Add Child","Filter","Router");
        esbRouterMediatorTest.addRouterMediator("8","No");
        esbRouterMediatorTest.clickAddRoute();
        esbCommon.mediatorUpdate();

        esbRouterMediatorTest.addRoute("8.0","No","//m0:add/m1:x");
        esbRouterMediatorTest.addRouterNamespace("m0", "http://services/samples0");
        esbRouterMediatorTest.addRouterNamespace("m1", "http://services/samples1");
        esbRouterMediatorTest.setNsLevel();
        esbRouterMediatorTest.addRoutePattern("//m0:add");
        esbCommon.mediatorUpdate();

        esbRouterMediatorTest.addTarget("8.0.0");
        esbRouterMediatorTest.addTargetSoapAction("urn:add");
        esbRouterMediatorTest.addTargetToAddress("http://localhost:9000/services/SimpleStockQuoteService");
        esbRouterMediatorTest.addTargetAnonSequence("8.0.0");
        esbCommon.addMediators("Add Child","8.0.0","Core","Log");
        esbRouterMediatorTest.addTarget("8.0.0");
        esbRouterMediatorTest.addTargetNoneEndpoint();
        esbCommon.mediatorUpdate();

        //Adding a Router mediator with Reg Endpoint and Reg Sequence
        esbCommon.addRootLevelChildren("Add Child","Filter","Router");
        esbRouterMediatorTest.addRouterMediator("9","Yes");
        esbRouterMediatorTest.clickAddRoute();
        esbCommon.mediatorUpdate();

        esbRouterMediatorUITest.verifyRouterMediator("9");
        esbRouterMediatorTest.addRoute("9.0","No","//m0:add/m1:x");
        esbRouterMediatorTest.addRouterNamespace("m0", "http://services/samples0");
        esbRouterMediatorTest.addRouterNamespace("m1", "http://services/samples1");
        esbRouterMediatorTest.addRoutePattern("//m0:add");
        esbCommon.mediatorUpdate();

        esbRouterMediatorUITest.testVerifyRouterConfiguration();
        esbRouterMediatorTest.addTarget("9.0.0");
        esbRouterMediatorTest.addTargetSoapAction("urn:add");
        esbRouterMediatorTest.addTargetToAddress("http://localhost:9000/services/SimpleStockQuoteService");
        esbRouterMediatorTest.addTargetRegSequence("fault");
        esbRouterMediatorTest.addTargetRegEndpoint("router_epr");
        esbCommon.mediatorUpdate();
    }

    /*
    This method will add all the mediators which are under the 'Filter' category to a sequence and update the Synapse confoguration
     */
    public void testAddCoreMediators() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }

        seleniumTestBase.loginToUI("admin","admin");
        addLocalEntries();
        addEndpoint();
        esbCommon.addSequence("sequence_filter");
        addInMediator();
        addOutMediator();
        addValidateMediator();
        addFilterMediator();
        addSwitchMediator();
        addRouterMediator();
        esbCommon.sequenceSave();
        seleniumTestBase.logOutUI();
    }

}
