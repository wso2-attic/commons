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

public class ESBAddWSDLEndpointTest   extends TestCase {
    Selenium selenium;

    public ESBAddWSDLEndpointTest(Selenium _browser){
        selenium = _browser;
    }
    /*
	 * This method will be used to add anonymous WSDL endpoints
	 */
    public void testAddAnonWsdlEndpoint() throws Exception {
		selenium.click("link=Add WSDL Endpoint");
        Thread.sleep(2000);
    }

    /*
    This method will be used to add endpoint mandatory information
     */
    public void testAddWsdlEprMandatoryInfo(String eprName, String specifyAs, String uri, String xml, String service, String port) throws Exception{
        if (eprName !=null){
            selenium.type("addname", eprName);
        }

        if (specifyAs.equals("uri")){
            selenium.click("//input[@name='uri' and @value='URI']");
            selenium.type("inlineprop_value", xml);
        } else if (specifyAs.equals("//input[@name='uri' and @value='URI']")){
            selenium.click("//input[@name='uri' and @value='URI']");
            selenium.type("wsdlUri", uri);
        }
        selenium.type("service", service);
        selenium.type("port", port);
    }

    /*
    This method will be used to add endpoint suspend information
     */
    public void testAddWsdlEprSuspendInfo(String errCode, String durSec, String maxDur, String factor) throws Exception{
        selenium.type("errCodes", errCode);
        selenium.type("DurationSec", durSec);
        selenium.type("maxDur", maxDur);
        selenium.type("factor", factor);
    }

    /*
    This method will be used to add endpoint retry information
     */
    public void testAddWsdlEprRetryInfo(String timoutErr,String retry, String retryDelay, String action, String actDur) throws Exception{
        selenium.type("timoutErr", timoutErr);
        selenium.type("retry", retry);
        selenium.type("retryDelay", retryDelay);
        selenium.select("Action", "label="+action);
        selenium.type("actDur", actDur);
    }


    /*
    This method will be used to add endpoint QoS information
     */
    public void testAddWsdlEprQosInfo(String wsAddEnable, String sepLister, String wsSec, String secResource, String wsRm, String rmResource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (wsAddEnable !=null){
            selenium.click("wsAddEnable");
        }

        if (sepLister != null){
            selenium.click("sepLis");
        }

        if (wsSec != null){
            selenium.click("wsSec");
            selenium.click("link=Registry Keys");

            esbCommon.testSelectResource("Entry",secResource);
        }

        if (wsRm != null){
            selenium.click("wsRm");
        }

        if (rmResource != null){
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('rmpolKey');\"]");
            esbCommon.testSelectResource("Entry",rmResource);
        }
    }

    /*
    This method is used to save the address endpoint
     */
    public void testSaveWsdlEndpoint() throws Exception{
        selenium.click("save");
        selenium.waitForPageToLoad("30000");
    }
}
