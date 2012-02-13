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
    public void testAddAnonAddressEndpoint() throws Exception {
        selenium.click("link=Add Address Endpoint");
        selenium.waitForPageToLoad("30000");
    }

    /*
    This method will be used to add endpoint mandatory information
     */
    public void testAddAddressEprMandatoryInfo(String eprName, String epr) throws Exception{
        if (eprName !=null){
            selenium.type("addname", eprName);
        }
        selenium.type("address", epr);
    }

    /*
    This method will be used to add format and optimize information
     */
    public void testAddAddressEprFormatOptimizeInfo(String format, String optimize) throws Exception{
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
    public void testAddAddressEprSuspendInfo(String errCode, String durSec, String maxDur, String factor) throws Exception{
        if (errCode !=null){
            selenium.type("errCodes", errCode);
        }

        if (durSec !=null){
            selenium.type("durSec", durSec);
        }

        if (maxDur !=null){
          selenium.type("maxDur", maxDur);
        }

        if (factor !=null){
          selenium.type("factor", factor);
        }
    }

    /*
    This method will be used to add endpoint retry information
     */
    public void testAddAddressEprRetryInfo(String timeoutErr,String retry, String retryDelay, String action, String actDur) throws Exception{
        if (timeoutErr !=null){
           selenium.type("timoutErr", timeoutErr);
        }

        if (retry !=null){
          selenium.type("retry", retry);
        }

        if (retryDelay !=null){
          selenium.type("retryDelay", retryDelay);
        }

        if (action !=null){
          selenium.select("Action", "label="+action);
        }

        if (actDur !=null){
          selenium.type("actDur", actDur);
        }
    }


    /*
    This method will be used to add endpoint QoS information
     */
    public void testAddAddressEprQosInfo(String wsAddEnable, String sepLister, String wsSec, String secResource, String wsRm, String rmResource) throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);

        if (wsAddEnable !=null){
            selenium.click("wsAddEnable");
            Thread.sleep(2000);
        }

        if (sepLister != null){
            selenium.click("sepLis");
            Thread.sleep(2000);
        }

        if (wsSec != null){
            selenium.click("wsSec");
            selenium.click("link=Registry Keys");

            esbCommon.testSelectResource("Entry",secResource);
            Thread.sleep(2000);
        }

        if (wsRm != null){
            selenium.click("wsRm");
            Thread.sleep(2000);
        }

        if (rmResource != null){
            selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('rmpolKey')\"]");
            esbCommon.testSelectResource("Entry",rmResource);
            Thread.sleep(2000);
        }

    }

    /*
    This method is used to save the address endpoint
     */
    public void testSaveAddressEndpoint() throws Exception{
        selenium.click("save");
        selenium.waitForPageToLoad("30000");
    }
}
