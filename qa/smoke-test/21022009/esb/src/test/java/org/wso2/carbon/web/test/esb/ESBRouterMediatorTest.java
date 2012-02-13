package org.wso2.carbon.web.test.esb;

import junit.framework.TestCase;
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

public class ESBRouterMediatorTest extends TestCase {
    Selenium selenium;

    public ESBRouterMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Router mediator
	 */
    public void testAddRouterMediator(String level, String routeContinue) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.select("mediator.router.continue", "label="+routeContinue);
		selenium.click("link=Add Route");
		selenium.waitForPageToLoad("30000");
    }

    /*
     * This method will add a Route
     */
    public void testAddRoute(String expression, String namespacePrefix, String namespaceURI, String routePattern) throws Exception{
		selenium.click("link=Route");
        Thread.sleep(2000);
		selenium.type("mediator.route.expression", expression);
		selenium.click("mediator.route.expression.xpath_nmsp_button");        

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testAddNamespace(namespacePrefix, namespaceURI);

		selenium.type("mediator.route.pattern", routePattern);
        esbCommon.testMediatorUpdate();
    }

    /*
     * This method will add a Router Target
     */
    public void testAddTarget(String soapAction, String toAddress, String radioSelectionSeq, String radioSelectionEpr,
                              String resourceNameSeq, String resourceNameEpr, String nodeType, String mediatorCategory, String mediatorName ) throws Exception{
		selenium.click("link=Target");
        Thread.sleep(5000);

        if (soapAction != null){
            selenium.type("mediator.target.soapaction", soapAction);
        }
        if (toAddress !=null){
            selenium.type("mediator.target.toaddress", toAddress);
        }

        ESBCommon esbCommon = new ESBCommon(selenium);
        if (radioSelectionSeq.equals("mediator.target.seq.radio.reg") ){
            selenium.click("mediator.target.seq.radio.reg");
            selenium.click("mediator.target.seq.reg.link");
            esbCommon.testSelectResource("Sequence", resourceNameSeq);
        } else if (radioSelectionSeq.equals("mediator.target.seq.radio.anon")){
            selenium.click("mediator.target.seq.radio.anon");
            selenium.click("//div[@id='mediator-0.0.0']/div/div[1]/a");
            esbCommon.testAddRootLevelChildren(nodeType, mediatorCategory, mediatorName);
        } else if (radioSelectionSeq.equals("mediator.target.seq.radio.none")){
            selenium.click("mediator.target.seq.radio.none");
        }
        
        if (radioSelectionEpr.equals("epOpReg") ){
            selenium.click("epOpReg");
            selenium.click("regEpLink");
            esbCommon.testSelectResource("Endpoint", resourceNameEpr);
        } else if (radioSelectionEpr.equals("epOpAnon")){
            selenium.click("epOpAnon");
            selenium.click("epAnonAdd");
            Thread.sleep(5000);

            ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
            esbAddAddressEndpointTest.testAddAnonAddressEndpoint();
            esbAddAddressEndpointTest.testAddAddressEprMandatoryInfo(null,"http://localhost:9000/services/SimpleStockQuoteService");
            esbAddAddressEndpointTest.testSaveAddressEndpoint();

        } else if (radioSelectionEpr.equals("epOpNone")){
            selenium.click("epOpNone");
        }
		selenium.click("link=Target");
        Thread.sleep(2000);	
        esbCommon.testMediatorUpdate();
    }
}
