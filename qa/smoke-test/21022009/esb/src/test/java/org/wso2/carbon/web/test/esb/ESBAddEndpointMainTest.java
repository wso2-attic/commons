package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
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

public class ESBAddEndpointMainTest  extends CommonSetup{

    public ESBAddEndpointMainTest(String text) {
        super(text);
    }

    public void testAddEndpoint() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
                
        seleniumTestBase.loginToUI("admin","admin");

        ESBCommon esbCommon = new ESBCommon(selenium);

        //Adding the security policy and rm policy as local entries
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.testAddLocalEntry("Add Source URL Entry","sec_policy","file:repository/samples/resources/policy/policy_3.xml");
        esbManageLocalEntriesTest.testAddLocalEntry("Add Source URL Entry","rm_policy","file:repository/samples/resources/policy/sample_rm_policy_1.xml");

        esbCommon.testAddEndpoint();

        boolean addEprName = selenium.isTextPresent("1epr");
        if (addEprName){
            //Do nothing
        } else{
            //Adding an Address endpoint
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.testAddAnonAddressEndpoint();
            esbAddAddressEndpointTest.testAddAddressEprMandatoryInfo("1epr","http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.testAddAddressEprFormatOptimizeInfo("SOAP 1.1","SwA");
            esbAddAddressEndpointTest.testAddAddressEprSuspendInfo("101503","1000","10000","2");
            esbAddAddressEndpointTest.testAddAddressEprRetryInfo("101504","1000","2000","Discard message","10");
            esbAddAddressEndpointTest.testAddAddressEprQosInfo("wsAddEnable","sepLister","wsSec","sec_policy","wsRm","rm_policy");
            esbAddAddressEndpointTest.testSaveAddressEndpoint();
        }

        esbCommon.testAddEndpoint();
        boolean wsdlEprName = selenium.isTextPresent("1wsdl");
        if (wsdlEprName){
            //Do nothing
        } else{
            //Adding WSDL endpoints
            ESBAddWSDLEndpointTest esbAddWSDLEndpointTest = new ESBAddWSDLEndpointTest(selenium);
            esbAddWSDLEndpointTest.testAddAnonWsdlEndpoint();
            esbAddWSDLEndpointTest.testAddWsdlEprMandatoryInfo("1wsdl","//input[@name='uri' and @value='URI']","http://localhost:9000/services/SimpleStockQuoteService?wsdl",null,"SimpleStockQuoteService","SimpleStockQuoteServiceSOAP11port_http1");
            esbAddWSDLEndpointTest.testAddWsdlEprSuspendInfo("101503","1000","10000","2");
            esbAddWSDLEndpointTest.testAddWsdlEprRetryInfo("101504","1000","2000","Discard message","10");
            esbAddWSDLEndpointTest.testAddWsdlEprQosInfo("wsAddEnable","sepLister","wsSec","sec_policy","wsRm","rm_policy");
            esbAddWSDLEndpointTest.testSaveWsdlEndpoint();
        }
        
//        //Adding failover endpoints
//
//        addEndpoint.testAddAnonFailoverEndpoint("failover1");
//
//        //Adding and address endpoint to the failover endpoint as a child
//        //String epr, String format, String optimize, String errCode, String suspendDur, String maxDur, String factor, String timeOutErr,
//        //String retry, String retryDelay, String action, String actDur, String wsAddEnable, String sepLister, String wsSec, String secResource, String wsRm, String rmResource
//        addEndpoint.testAddChildEndpointAddress("http://localhost:9000/services/SimpleStockQuoteService",);
            seleniumTestBase.logOutUI();
    }

}
