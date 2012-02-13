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

public class ESBAddAddressEndpointTest  extends TestCase {
    Selenium selenium;

    public ESBAddAddressEndpointTest(Selenium _browser){
        selenium = _browser;
    }

    /*
	 * This method will be used to add anonymous address endpoints
	 */
    public void addAnonAddressEndpoint() throws Exception {
        selenium.click("link=Address Endpoint");
        selenium.waitForPageToLoad("30000");
    }

    /*
    This method will be used to add endpoint mandatory information
     */
    public void addAddressEprMandatoryInfo(String eprName, String epr) throws Exception{
        if (eprName !=null){
            selenium.type("address", eprName);
        }
        selenium.type("address", epr);
        selenium.click("testAddress");

        if(selenium.isTextPresent("Successfully connected to http://localhost:9000/services/SimpleStockQuoteService")) {
            System.out.println("Successfully connected to end point");
            selenium.click("//button[@type='button']");
        }  else 
            System.out.println("Invalid Endpoint!!!");

    }

    /*
    This method will be used to add format and optimize information
     */
    public void addAddressEprFormatOptimizeInfo(String format, String optimize) throws Exception{
        if (format !=null){
            selenium.select("format", "label="+format);
        }

        if (optimize !=null){
            selenium.select("optimize", "label="+optimize);
        }
    }


    /*
    This method will be used to add endpoint suspend information
     */
    public void addAddressEprSuspendInfo(String errCode, String durSec, String maxDur, String factor) throws Exception{
        if (errCode !=null){
            selenium.type("suspendErrorCode", errCode);
        }

        if (durSec !=null){
            selenium.type("suspendDuration", durSec);
        }

        if (maxDur !=null){
          selenium.type("suspendMaxDuration", maxDur);
        }

        if (factor !=null){
          selenium.type("factor", factor);
        }
    }

    /*
    This method will be used to add endpoint retry information
     */
    public void addAddressEprRetryInfo(String retryErroCode,String retryTimeOut, String retryDelay) throws Exception{
        if (retryErroCode !=null){
           selenium.type("retryErroCode", retryErroCode);
        }

        if (retryTimeOut !=null){
          selenium.type("retryTimeOut", retryTimeOut);
        }

        if (retryDelay !=null){
          selenium.type("retryDelay", retryDelay);
        }
    }

    /*
    This method will be used to add endpoint retry information
     */
    public void addAddressEprTimeoutInfo(String actionSelect, String actionDuration) throws Exception{
        if (actionSelect !=null){
          selenium.select("actionSelect", "label="+actionSelect);
        }

        if (actionDuration !=null){
          selenium.type("actionDuration", actionDuration);
        }
    }

    /*
    This method will be used to add endpoint QoS information
     */
    public void addAddressEprQosInfo(String wsAddressing, String sepListener, String wsSecurity, String secResource, String wsRM, String rmResource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (wsAddressing !=null){
            selenium.click("wsAddressing");
            Thread.sleep(2000);
        }

        if (sepListener != null){
            selenium.click("sepListener");
            Thread.sleep(2000);
        }

        if (wsSecurity != null){
            selenium.click("wsSecurity");
            selenium.click("link=Registry Browser");

            esbCommon.selectResource("Entry",secResource);
            Thread.sleep(2000);
        }

        if (wsRM != null){
            selenium.click("wsRM");
            Thread.sleep(2000);
        }

        if (rmResource != null){
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('wsrmPolicyKeyID')\"]");
            esbCommon.selectResource("Entry",rmResource);
            Thread.sleep(2000);
        }
    }

    /*
    This method is used to save the address endpoint
     */
    public void saveAddressEndpoint() throws Exception{
        selenium.click("save");
        selenium.waitForPageToLoad("30000");
    }

 /*
    This method is used to saveAs the address endpoint
     */
    public void saveAsAddressEndpoint(String registryPath) throws Exception{
        selenium.click("//input[@name='save' and @value='Save As']");
		selenium.type("synRegKey", registryPath);
		selenium.click("saveSynRegButton");
		selenium.waitForPageToLoad("30000");
        Thread.sleep(5000);
    }    
}
