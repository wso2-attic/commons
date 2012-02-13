package org.wso2.carbon.web.test.esb;

import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;

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

public class ESBSendMediatorTest extends TestCase {
    Selenium selenium;

    public ESBSendMediatorTest(Selenium _browser){
        selenium=_browser;
    }

    /*
     * This method will be called when a person wants to add a Send mediator without specifying an endpoint
     */
    public void testAddSendMediator(String level, String radioSelection, String resourceName, String endpointURI, String format, String optimize,
                                    String errCode, String durSec, String maxDur, String factor,String timeoutErr, String retry,
                                    String retryDelay, String action, String actDur, String wsAddEnable, String sepLister, String wsSec,
                                    String secResource, String wsRm, String rmResource) throws Exception {
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(3000);

        //Select radio button None
        selenium.click(radioSelection);
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (radioSelection.equals("epOpNone")){
        selenium.click("//a[@id='mediator-"+level+"']");
            Thread.sleep(2000);

            //Updating the mediator
            esbCommon.testMediatorUpdate();

        } else if (radioSelection.equals("epOpAnon")){
            assertEquals("Add", selenium.getText("epAnonAdd"));
		    selenium.click("epAnonAdd");
		    selenium.waitForPageToLoad("30000");

            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.testAddAnonAddressEndpoint();
            esbAddAddressEndpointTest.testAddAddressEprMandatoryInfo(null,endpointURI);
            esbAddAddressEndpointTest.testAddAddressEprFormatOptimizeInfo(format,optimize);
            esbAddAddressEndpointTest.testAddAddressEprSuspendInfo(errCode,durSec,maxDur,factor);
            esbAddAddressEndpointTest.testAddAddressEprRetryInfo(timeoutErr,retry,retryDelay,action,actDur);
            esbAddAddressEndpointTest.testAddAddressEprQosInfo(wsAddEnable,sepLister,wsSec,secResource,wsRm,rmResource);
            esbAddAddressEndpointTest.testSaveAddressEndpoint();

            selenium.click("//a[@id='mediator-"+level+"']");
            Thread.sleep(2000);

            //Updating the mediator
            esbCommon.testMediatorUpdate();

        } else if (radioSelection.equals("epOpReg")){
            assertEquals("Registry Browser", selenium.getText("regEpLink"));
            selenium.click("regEpLink");

            esbCommon.testSelectResource("Endpoint",resourceName);
            selenium.click("//a[@id='mediator-"+level+"']");
            Thread.sleep(2000);

            //Updating the mediator
            esbCommon.testMediatorUpdate();
        }
    }
}
