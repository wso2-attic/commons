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
    int nsLevel=0;

    public ESBRouterMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Router mediator
	 */
    public void addRouterMediator(String level, String routeContinue) throws Exception{
        if (level.equals("0.0")){
	        selenium.click("link=Router");
            Thread.sleep(2000);
        } else {
            selenium.click("//a[@id='mediator-"+level+"']");
            Thread.sleep(2000);
        }
        selenium.select("mediator.router.continue", "label="+routeContinue);
//		selenium.click("link=Add Route");
//		selenium.waitForPageToLoad("30000");
    }

    public void clickAddRoute() throws Exception{
        selenium.click("link=Add Route");
		selenium.waitForPageToLoad("30000");
    }

    /*
     * This method will add a Route
     */
    public void addRoute(String level, String breakRoute, String expression) throws Exception{
        if (level.equals("0")){
            selenium.click("link=Route");
            Thread.sleep(2000);
        } else {
            selenium.click("//a[@id='mediator-"+level+"']");
            Thread.sleep(2000);
        }
        selenium.select("mediator.route.break", "label="+breakRoute);        
		selenium.type("mediator.route.expression", expression);
    }

    /*
    Adding the routerPattern
     */
    public void addRoutePattern(String routePattern) throws Exception{
		selenium.type("mediator.route.pattern", routePattern);
    }

    /*
    Adding Router Namespaces
     */
    public void addRouterNamespace(String prefix, String uri) throws Exception{
           selenium.click("mediator.route.expression.xpath_nmsp_button");
           Thread.sleep(2000);

           int prefix_no=0;
           String prefix_available="";
           boolean pref=false;
           while(selenium.isElementPresent("prefix"+prefix_no)){
                prefix_available=selenium.getValue("prefix"+prefix_no);
                if(prefix_available.equals(prefix)){
                    pref=true;
                    break;
                }
                else
                   pref=false;

                prefix_no=prefix_no+1;
          }
           if (pref) {
               selenium.click("link=X");
           } else {
               Thread.sleep(1000);
               selenium.click("addNSButton");
               selenium.type("prefix"+nsLevel, prefix);
               selenium.type("uri"+nsLevel, uri);
               nsLevel++;
               selenium.click("saveNSButton");
           }
       }


    public void setNsLevel() throws Exception{
        nsLevel=0;
    }

    /*
     * This method will add a Router Target
     */
    public void addTarget(String level) throws Exception{
        if (level.equals("0.0.0")){
            selenium.click("link=Target");
            Thread.sleep(4000);
        } else {
            selenium.click("//a[@id='mediator-"+level+"']");
            Thread.sleep(4000);
        }
    }

    /*
    Adding None endpoint
     */
    public void addTargetNoneEndpoint() throws Exception{
        selenium.click("epOpNone");
    }

    /*
    Adding Anon endpoint
     */
    public void addTargetAnonEndpoint(String epr) throws Exception{
        selenium.click("epOpAnon");
        selenium.click("epAnonAdd");
        Thread.sleep(5000);

        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        esbAddAddressEndpointTest.addAnonAddressEndpoint();
        esbAddAddressEndpointTest.addAddressEprMandatoryInfo(null,epr);
        esbAddAddressEndpointTest.saveAddressEndpoint();
    }

    /*
    Adding Registry endpoint
     */
    public void addTargetRegEndpoint(String resource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("epOpReg");
        selenium.click("regEpLink");
        esbCommon.selectResource("Enpoint", resource);
    }

    /*
    Adding normal sequence
     */
    public void addTargetNoneSequence() throws Exception{
        selenium.click("mediator.target.seq.radio.none");
    }

    /*
    Adding anonymous sequence
     */
    public void addTargetAnonSequence(String level) throws Exception{
        selenium.click("mediator.target.seq.radio.anon");
        selenium.click("//div[@id='mediator-"+level+"']/div/div[1]/a");
    }

    /*
    Adding anonymous sequence
     */
    public void addTargetRegSequence(String resource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("mediator.target.seq.radio.reg");
        selenium.click("mediator.target.seq.reg.link");
        esbCommon.selectResource("Sequence", resource);
    }

    /*
    Adding target SOAP action
     */
    public void addTargetSoapAction(String soapAction) throws Exception{
        selenium.type("mediator.target.soapaction", soapAction);
    }

    /*
    Adding target To address
     */
    public void addTargetToAddress(String toAddress) throws Exception{
        selenium.type("mediator.target.toaddress", toAddress);
    }

}
