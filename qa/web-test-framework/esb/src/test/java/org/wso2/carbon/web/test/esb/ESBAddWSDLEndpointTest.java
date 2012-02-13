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

public class ESBAddWSDLEndpointTest extends TestCase {
    Selenium selenium;

    public ESBAddWSDLEndpointTest(Selenium _browser){
        selenium = _browser;
    }

    /*
      * This method will be used to add anonymous WSDL endpoints
      */
    public void addAnonWsdlEndpoint() throws Exception {
		selenium.click("link=Add WSDL Endpoint");
        Thread.sleep(2000);
    }

    /*
    This method will be used to add uri WSDL endpoint mandatory information
     */
    public void addUriWsdlEprMandInfo(String endpointName, String uriWSDLVal, String wsdlendpointService, String wsdlendpointPort) throws Exception{
        if (endpointName !=null){
            selenium.type("endpointName", endpointName);
        }
        Thread.sleep(2000);
        selenium.click("uriWSDL");
		selenium.type("uriWSDLVal", uriWSDLVal);
		selenium.type("wsdlendpointService", uriWSDLVal);
		selenium.type("wsdlendpointPort", wsdlendpointPort);
    }

    /*
    This method will be used to add inline WSDL endpoint mandatory information
     */
    public void addInlineWsdlEprMandInfo(String endpointName, String xml, String wsdlendpointService, String wsdlendpointPort) throws Exception{
        if (endpointName !=null){
            selenium.type("endpointName", endpointName);
        }

        selenium.click("inlineWSDL");
        selenium.type("inlineWSDLVal", xml);
        selenium.type("wsdlendpointService", wsdlendpointService);
        selenium.type("wsdlendpointPort", wsdlendpointPort);
    }

    /*
    This method will be used to add endpoint suspend information
     */
    public void addWsdlEprSuspendInfo(String suspendErrorCode, String suspendDuration, String suspendMaxDuration, String factor) throws Exception{
        selenium.type("suspendErrorCode", suspendErrorCode);
        selenium.type("suspendDuration", suspendDuration);
        selenium.type("suspendMaxDuration", suspendMaxDuration);
        selenium.type("factor", factor);
    }

    /*
    This method will be used to add endpoint retry information
     */
    public void addWsdlEprRetryInfo(String retryErroCode,String retryTimeOut, String retryDelay) throws Exception{
        selenium.type("retryErroCode", retryErroCode);
        selenium.type("retryTimeOut", retryTimeOut);
        selenium.type("retryDelay", retryDelay);
    }

    /*
    This method will be used to add endpoint retry information
     */
    public void addWsdlEprTimeoutInfo(String actionSelect, String actionDuration) throws Exception{
        selenium.select("actionSelect", "label="+actionSelect);
        selenium.type("actionDuration", actionDuration);
    }

    /*
    This method will be used to add endpoint QoS information
     */
    public void addWsdlEprQosInfo(String wsAddressing, String sepListener, String wsSecurity, String secResource, String wsRM, String rmResource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (wsAddressing !=null){
            selenium.click("wsAddressing");
        }

        if (sepListener != null){
            selenium.click("sepListener");
        }

        if (wsSecurity != null){
            selenium.click("wsSecurity");
            selenium.click("link=Registry Browser");

            esbCommon.selectResource("Entry",secResource);
        }

        if (wsRM != null){
            selenium.click("wsRM");
        }

        if (rmResource != null){
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('wsrmPolicyKeyID')\"]");
            esbCommon.selectResource("Entry",rmResource);
        }
    }

    /*
    This method is used to save the address endpoint
     */
    public void saveWsdlEndpoint() throws Exception{
        selenium.click("save");
        selenium.waitForPageToLoad("30000");
    }
}
