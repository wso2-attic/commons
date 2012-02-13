package org.wso2.carbon.web.test.esb;

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

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;

public class ESBAddEndpointsTest extends TestCase {
    Selenium selenium;

    public ESBAddEndpointsTest(Selenium _browser){
        selenium = _browser;
    }

    /*
	 * This method will be used to add anonymous endpoints
	 */
    public void testAddAnonEndpoint(String endpointType, String epr, String format, String optimize, String errCode, String durSec, String maxDur, String factor, String timoutErr, String retry, String action, String actDur) throws Exception {
        Thread.sleep(1000);
		selenium.click("link="+endpointType);
		selenium.waitForPageToLoad("30000");
		selenium.type("address", epr);
//        selenium.select("format", "label="+format);
//        selenium.select("optimize", "label="+optimize);
//        selenium.type("errCodes", errCode);
//        selenium.type("durSec", durSec);
//        selenium.type("maxDur", maxDur);
//        selenium.type("factor", factor);
//        selenium.type("timoutErr", timoutErr);
//        selenium.type("retry", retry);
//        selenium.type("retryDelay", retry);
//        selenium.select("Action", "label="+action);
//        selenium.type("actDur", actDur);
        selenium.click("save");
		selenium.waitForPageToLoad("30000");

    }

    /*
	 * This method will be used to edit anonymous endpoints
	 */
    public void testEditAnonEndpoint(String epr, String format, String optimize, String errCode, String durSec, String maxDur, String factor, String timoutErr, String retry, String action, String actDur) throws Exception {
		selenium.click("epAnonEdit");
		selenium.waitForPageToLoad("30000");
		selenium.type("address", epr);
		selenium.select("format", "label="+format);
		selenium.select("optimize", "label="+optimize);
		selenium.type("errCodes", errCode);
		selenium.type("durSec", durSec);
		selenium.type("maxDur", maxDur);
		selenium.type("factor", factor);
		selenium.type("timoutErr", timoutErr);
		selenium.type("retry", retry);
		selenium.type("retryDelay", retry);
		selenium.select("Action", "label="+action);
		selenium.type("actDur", actDur);

		selenium.click("save");
		selenium.waitForPageToLoad("30000");
		assertEquals("Edit", selenium.getText("epAnonEdit"));
		assertEquals("Clear", selenium.getText("epAnonClear"));

    }
}
