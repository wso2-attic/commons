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

public class ESBCacheMediatorUITest extends TestCase {
    Selenium selenium;

    public ESBCacheMediatorUITest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will verify the Cache mediator properties
	 */
    public void testVerifyCacheMediator(String level, String onCacheHit) throws Exception{
        selenium.click("//a[@id='mediator-"+level+"']");
        Thread.sleep(2000);
		assertTrue(selenium.isTextPresent("Cache Mediator"));
		assertTrue(selenium.isTextPresent("Cache Id"));
		assertTrue(selenium.isTextPresent("Cache Scope"));
        assertEquals("Per-Host Per-Mediator", selenium.getText("cacheScope"));
		assertTrue(selenium.isTextPresent("Cache Type"));
		assertEquals("Finder Collector", selenium.getText("cacheType"));
		assertTrue(selenium.isTextPresent("Hash Generator"));
		assertEquals("org.wso2.caching.digest.DOMHASHGenerator", selenium.getValue("hashGen"));
		assertTrue(selenium.isTextPresent("Cache Timeout (seconds)"));
		assertTrue(selenium.isTextPresent("Maximum Message Size"));
		assertTrue(selenium.isTextPresent("Cache Implementation Details"));
		assertTrue(selenium.isTextPresent("Implementation Type"));
		assertEquals("In-Memory", selenium.getText("impType"));
		assertTrue(selenium.isTextPresent("Maximum Size"));
		assertTrue(selenium.isTextPresent("On Cache Hit"));

        if (onCacheHit.equals("sequenceOptionAnon")){
            selenium.click("sequenceOptionAnon");            
            assertEquals("on", selenium.getValue("sequenceOptionAnon"));
            assertEquals("off", selenium.getValue("sequenceOptionReference"));
            assertTrue(selenium.isTextPresent("Annonymous"));
        } else {
            selenium.click("sequenceOptionReference");
            assertEquals("on", selenium.getValue("sequenceOptionReference"));
            assertEquals("off", selenium.getValue("sequenceOptionAnon"));
            selenium.click("sequenceOptionReference");
            assertTrue(selenium.isTextPresent("Sequence Reference"));
            assertEquals("Registry Browser", selenium.getText("//a[@onclick=\"showInLinedRegistryBrowser('mediator.sequence')\"]"));
        }

		assertEquals("Update", selenium.getValue("//input[@value='Update']"));
		assertEquals("Help", selenium.getText("//div[@id='mediatorDesign']/div/div/a"));

//        ESBCommon esbCommon = new ESBCommon(selenium);
//        esbCommon.testMediatorHelp("Cache");
    }
}
