package org.wso2.carbon.web.test.esb;

import org.wso2.carbon.web.test.common.*;
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

public class ESBLogMediatorTest extends TestCase {
    Selenium selenium;

    public ESBLogMediatorTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a Log mediator
	 */
    public void testAddLogMediator(String level, String logLevel) throws Exception {
		selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(3000);
		selenium.select("mediator.log.log_level", "label="+logLevel);
    }

    /*
    This method will add properties to the Log mediator
     */
    public void testAddLogPropeties(String propertyName0, String propertyTypeSel0, String propertyValue0,
                                   String propertyName1, String propertyTypeSel1, String propertyValue1, String propertyTypeSel2,
                                   String propertyName2, String propertyValue2) throws Exception{
		selenium.click("link=Add Property");
        Thread.sleep(3000);
        selenium.type("propertyName0", propertyName0);
		selenium.select("propertyTypeSelection0", "label="+propertyTypeSel0);
        Thread.sleep(1000);
        selenium.type("propertyValue0", propertyValue0);
		selenium.click("link=Add Property");
        Thread.sleep(1000);
        selenium.type("propertyName1", propertyName1);
		selenium.select("propertyTypeSelection1", "label="+propertyTypeSel1);
        Thread.sleep(1000);
        selenium.type("propertyValue1", propertyValue1);
    }

    /*
    This method will update the Log mediator
     */
    public void testUpdateLogMediator() throws Exception{
        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testMediatorUpdate();
    }
}