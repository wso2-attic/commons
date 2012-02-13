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
    This method will add a normal Send mediator to the sequence tree without defining endpoints
     */
    public void addNormalSendMediator(String level) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        if(selenium.getText("treePane").equals("Root Add Child")){
            esbCommon.addRootLevelChildren("Add Child","Core","Send");
        }
            selenium.click("//a[@id='mediator-"+level+"']");
            Thread.sleep(3000);
            //Select radio button None
            selenium.click("epOpNone");
    }

    /*
    parameters:
    level-mediator level of the send mediator
    epType-type of the end point to be added
            address,wsdl,failover,loadbalance
     */
    public void clickAddAnonSendMediator(String level,String epType) throws Exception{
        //Select radio button Anonymous
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("epAnonAdd");
        selenium.waitForPageToLoad("30000");

        if(epType.equalsIgnoreCase("address"))
            selenium.click("link=Add Address Endpoint");
        else if(epType.equalsIgnoreCase("wsdl"))
            selenium.click("link=Add WSDL Endpoint");
        else if(epType.equalsIgnoreCase("failover"))
            selenium.click("link=Add Failover Group");
        else if(epType.equalsIgnoreCase("loadbalance"))
            selenium.click("link=Add Load-balance Group");
		selenium.waitForPageToLoad("30000");
    }

    /*
     This method will add the anonymous send mediator to the Tree  with Address endpoints
     */
    public void addAnonSendMediator(String level) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if(selenium.getText("treePane").equals("Root Add Child")){
            esbCommon.addRootLevelChildren("Add Child","Core","Send");
        }
        
        //Select radio button Anonymous
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("epAnonAdd");
        selenium.waitForPageToLoad("30000");

        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        esbAddAddressEndpointTest.addAnonAddressEndpoint();
    }

    /*
     This method will add the anonymous send mediator to the Tree with WSDL endpoints
     */
    public void addWsdlEprAnonSendMediator(String level) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        if(selenium.getText("treePane").equals("Root Add Child")){
            esbCommon.addRootLevelChildren("Add Child","Core","Send");
        }
        
        //Select radio button Anonymous
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("epAnonAdd");
        selenium.waitForPageToLoad("30000");

        ESBAddWsdlEndpointTest esbAddWsdlEndpointTest = new ESBAddWsdlEndpointTest(selenium);
        esbAddWsdlEndpointTest.addAnonWsdlEndpoint();
    }

    /*
        This method will add the  'Pick From Registry' send mediator to the Tree
     */
    public void addRegistrySendMediator(String level,String resourceType,String resourceName) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        if(selenium.getText("treePane").equals("Root Add Child")){
            esbCommon.addRootLevelChildren("Add Child","Core","Send");
        }

         //Select radio button None
         selenium.click("//a[@id='mediator-"+level+"']");
         Thread.sleep(2000);
         selenium.click("epOpReg");

         if(resourceType!=null && resourceName!=null){
            selenium.click("regEpLink");
            esbCommon.selectResource(resourceType,resourceName);
         }
    }


    /*
    This method will add mandatory information for the endpoint
     */
    public void addMandInfoSendMediator(String endpointURI) throws Exception{
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        esbAddAddressEndpointTest.addAddressEprMandatoryInfo(null,endpointURI);
    }

    /*
    This method will add Format and Optimize information for the endpoint
     */
    public void addFormatOptimizeInfoSendMediator(String format, String optimize) throws Exception{
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        esbAddAddressEndpointTest.addAddressEprFormatOptimizeInfo(format,optimize);
    }

    /*
    This method will add Suspend information for the endpoint
     */
    public void addSuspendInfoSendMediator(String errCode, String durSec, String maxDur, String factor) throws Exception{
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        esbAddAddressEndpointTest.addAddressEprSuspendInfo(errCode,durSec,maxDur,factor);
    }

    /*
    This method will add retry timeout information for the endpoint
     */
    public void retryTimeoutInfoSendMediator(String timeoutErr, String retry, String retryDelay) throws Exception{
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        esbAddAddressEndpointTest.addAddressEprRetryInfo(timeoutErr,retry,retryDelay);
    }    

    /*
    This method will add timeout information for the endpoint
     */
    public void timeoutInfoSendMediator(String action, String actDur) throws Exception{
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);
        esbAddAddressEndpointTest.addAddressEprTimeoutInfo(action,actDur);
    }

    /*
    This method will add QoS information to the endpoint
     */
    public void eprQosInfoSendMediator(String wsAddEnable, String sepLister, String wsSec, String secResource, String wsRm, String rmResource) throws Exception{
        ESBAddAddressEndpointTest esbAddAddressEndpointTest = new ESBAddAddressEndpointTest(selenium);        
        esbAddAddressEndpointTest.addAddressEprQosInfo(wsAddEnable,sepLister,wsSec,secResource,wsRm,rmResource);
    }

    /*
    This method will add inline WSDL endpoints
     */
    public void addInlineWsdlMandInfoSendMediator(String eprName, String xml, String serviceName, String port) throws Exception{
        ESBAddWsdlEndpointTest esbAddWsdlEndpointTest = new ESBAddWsdlEndpointTest(selenium);
        esbAddWsdlEndpointTest.addInlineWsdlEprMandInfo(eprName,xml,serviceName,port);
    }

    /*
    This method will add uri WSDL endpoints
     */
    public void addUriMandInfoSendMediator(String eprName, String uriWsdl, String serviceName, String port) throws Exception{
        ESBAddWsdlEndpointTest esbAddWsdlEndpointTest = new ESBAddWsdlEndpointTest(selenium);
        esbAddWsdlEndpointTest.addUriWsdlEprMandInfo(eprName,uriWsdl,serviceName,port);

    }

    /*
    This method will add a Send mediator (which refers the endpoint as a registry key )to the sequence tree without defining endpoints
     */
    public void addRegSendMediator(String level, String resourceName) throws Exception {
        ESBCommon esbCommon = new ESBCommon(selenium);
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(3000);

        //Select radio button None
        selenium.click("epOpReg");
        assertEquals("Registry Browser", selenium.getText("regEpLink"));
        selenium.click("regEpLink");

        esbCommon.selectResource("Enpoint",resourceName);
//        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
    }

/*
    Add send mediator with resource pick from path in registry
     */
    public void addRegistryResFromPickedPath(String level,String path) throws Exception {
        //Select radio button Pick from Registry
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
        selenium.click("epOpReg");

        if(path!=null){
           selenium.click("regEpLink");
           selenium.type("pickedPath", path);
           selenium.click("//input[@value='OK']");
        }
    }
    
    /*
    This method will save the added endpoint
     */
    public void saveEndpoint() throws Exception {
		selenium.click("save");
        selenium.waitForPageToLoad("30000");        
    }
}
