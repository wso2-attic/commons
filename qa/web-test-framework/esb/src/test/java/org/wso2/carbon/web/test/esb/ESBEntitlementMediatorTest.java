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

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;
import org.wso2.carbon.web.test.common.SeleniumTestBase;


public class ESBEntitlementMediatorTest extends TestCase {
    Selenium selenium;

    public ESBEntitlementMediatorTest (Selenium _browser){
		selenium = _browser;
    }

    public void addMediator(String serviceUrl,String serviceUserName,String servicePWD) throws Exception{
        
        if (serviceUrl!=null) {
            selenium.type("remoteServiceUrl", serviceUrl);
        }

        if (serviceUserName !=null){
		    selenium.type("remoteServiceUserName",serviceUserName );
        }

        if (servicePWD !=null){
    		selenium.type("remoteServicePassword",servicePWD );
        }


        //verify the  switch to source view
        selenium.click("//a[@onclick='showSource()']");
        Thread.sleep(2000);
        assertTrue(selenium.isTextPresent("<syn:entitlementService xmlns:syn=\"http://ws.apache.org/ns/synapse\" remoteServiceUrl="+"\""+serviceUrl+"\""+" remoteServiceUserName="+"\""+serviceUserName+"\""+" remoteServicePassword="+"\""+servicePWD+"\""+" />"));
        selenium.click("link=switch to design view");
        Thread.sleep(2000);
        assertEquals(serviceUrl, selenium.getValue("remoteServiceUrl"));
        assertEquals(serviceUserName, selenium.getValue("remoteServiceUserName"));
        assertEquals(servicePWD, selenium.getValue("remoteServicePassword"));

    }
}
