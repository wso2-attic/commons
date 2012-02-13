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

public class ESBAddFailoverEndpointTest extends TestCase {
    Selenium selenium;

    public ESBAddFailoverEndpointTest(Selenium _browser){
        selenium = _browser;
    }

    /*
	 * This method will be used to add anonymous failover endpoints
	 */
    public void addAnonFailoverEndpoint(String eprName) throws Exception{
		selenium.click("link=Add Failover Group");
        Thread.sleep(2000);

		selenium.type("failover.name", eprName);
    }

//    /*
//    This method will add address endpoints to the Failover group
//     */
//    public void testAddFailoverAddChildEpr(String uri, String suspendDuration, String wsAdd) throws Exception{
//        selenium.click("link=Add Endpoint");
//        Thread.sleep(2000);
//        selenium.click("link=Address");
//        Thread.sleep(2000);
//        selenium.type("form_address.0.1_address", "http://localhost:9001/services/LBService1");
//        selenium.type("form_address.0.1_suspend", "10000");
//        selenium.click("form_address.0.1_wsAddressing");
//        selenium.click("//input[@id='']");
//        selenium.click("//button[@type='button']");
//
//    }

}
