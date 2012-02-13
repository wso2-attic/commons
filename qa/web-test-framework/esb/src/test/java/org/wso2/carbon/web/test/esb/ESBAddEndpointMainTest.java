package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class ESBAddEndpointMainTest  extends CommonSetup{
    Properties properties = new Properties();

    public ESBAddEndpointMainTest(String text) {
        super(text);
    }


    public void testAddEndpoint() throws Exception{
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        ESBCommon esbCommon = new ESBCommon(selenium);

        boolean login = selenium.isTextPresent("Sign-out");

        if (login){
            seleniumTestBase.logOutUI();
        }
                
        seleniumTestBase.loginToUI("admin","admin");
        //Adding the security policy and rm policy as local entries
        ESBManageLocalEntriesTest esbManageLocalEntriesTest = new ESBManageLocalEntriesTest(selenium);
        esbManageLocalEntriesTest.addSourceUrlEntry("sec_policy","file:"+ esbCommon.getCarbonHome()+"/repository/samples/resources/policy/policy_3.xml");
        esbManageLocalEntriesTest.addSourceUrlEntry("rm_policy","file:"+ esbCommon.getCarbonHome()+"/repository/samples/resources/policy/sample_rm_policy_1.xml");

        esbCommon.viewEndpoints();

        boolean addEprName = selenium.isTextPresent("epr1");
        if (addEprName){
            //Do nothing
        } else{
            //Adding an Address endpoint
            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.addAnonAddressEndpoint();
            esbAddAddressEndpointTest.addAddressEprMandatoryInfo("epr1","http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.addAddressEprFormatOptimizeInfo("SOAP 1.1","SwA");
            esbAddAddressEndpointTest.addAddressEprSuspendInfo("101503","1000","10000","2");
            esbAddAddressEndpointTest.addAddressEprRetryInfo("101504","1000","2000");
            esbAddAddressEndpointTest.addAddressEprTimeoutInfo("Discard message","10");
            esbAddAddressEndpointTest.addAddressEprQosInfo("wsAddEnable","sepLister","wsSec","sec_policy","wsRm","rm_policy");
            esbAddAddressEndpointTest.saveAddressEndpoint();
        }

        esbCommon.viewEndpoints();
        boolean wsdlEprName = selenium.isTextPresent("wsdl1");
        if (wsdlEprName){
            //Do nothing
        } else{
            //Adding WSDL endpoints
            ESBAddWsdlEndpointTest esbAddWsdlEndpointTest = new ESBAddWsdlEndpointTest(selenium);
            esbAddWsdlEndpointTest.addAnonWsdlEndpoint();
            esbAddWsdlEndpointTest.addUriWsdlEprMandInfo("wsdl1","http://localhost:9000/services/SimpleStockQuoteService?wsdl","SimpleStockQuoteService","SimpleStockQuoteServiceSOAP11port_http1");
            esbAddWsdlEndpointTest.addWsdlEprSuspendInfo("101503","1000","10000","2");
            esbAddWsdlEndpointTest.addWsdlEprRetryInfo("101504","1000","2000");
            esbAddWsdlEndpointTest.addWsdlEprTimeoutInfo("Discard message","10");
            esbAddWsdlEndpointTest.addWsdlEprQosInfo("wsAddEnable","sepLister","wsSec","sec_policy","wsRm","rm_policy");
            esbAddWsdlEndpointTest.saveWsdlEndpoint();
        }
        
//        //Adding failover endpoints
//
//        addEndpoint.addAnonFailoverEndpoint("failover1");
//
//        //Adding and address endpoint to the failover endpoint as a child
//        //String epr, String format, String optimize, String errCode, String suspendDur, String maxDur, String factor, String timeOutErr,
//        //String retry, String retryDelay, String action, String actDur, String wsAddEnable, String sepLister, String wsSec, String secResource, String wsRm, String rmResource
//        addEndpoint.testAddChildEndpointAddress("http://localhost:9000/services/SimpleStockQuoteService",);
            seleniumTestBase.logOutUI();
    }
}
