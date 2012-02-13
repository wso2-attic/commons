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

public class ESBAddEndpointTest extends TestCase {
    Selenium selenium;

    public ESBAddEndpointTest(Selenium _browser){
        selenium = _browser;
    }

     
    /*
	 * This method will be used to add child endpoints to failover endpoints which are inside failover endpoints
	 */
    public void addChildEndpointFailover (String childEndpointType, String failoverOrLaobalance) throws Exception{
		selenium.click("link=Failover Group");
        Thread.sleep(2000);

        if (failoverOrLaobalance != null){
            if (failoverOrLaobalance.equals("failover")){
                selenium.click("//div[@id='failover.0.1']/div/div[1]/a");
                Thread.sleep(1000);
            } else if (failoverOrLaobalance.equals("load")){
                selenium.click("//div[@id='load.0.1']/div/div[1]/a");
                Thread.sleep(1000);
            }
        }

        selenium.click("link="+childEndpointType);
        Thread.sleep(1000);
    }

}