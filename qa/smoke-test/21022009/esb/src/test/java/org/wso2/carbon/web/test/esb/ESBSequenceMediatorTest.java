package org.wso2.carbon.web.test.esb;

import com.thoughtworks.selenium.*;
import junit.framework.TestCase;

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

public class ESBSequenceMediatorTest extends TestCase{
    Selenium selenium;

    public ESBSequenceMediatorTest(Selenium _browser){
        selenium=_browser;
    }

    public void testAddSequenceMediator(String refSequence) throws Exception {
		selenium.click("link=Sequence");
        Thread.sleep(3000);

        //Selecting resources from the Registry Browser
        selenium.click("//a[@onclick=\"showInLinedRegistryBrowser('seq_ref')\"]");
        Thread.sleep(2000);

        ESBCommon esbCommon = new ESBCommon(selenium);
        esbCommon.testSelectResource("Sequence",refSequence);

        //Updating the sequence mediator
        esbCommon.testMediatorUpdate();

        //Switching to the source view of the sequence
        selenium.click("link=switch to source view");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" name=\"sequence_mediator_sequence\"> <syn:sequence key=\"fault\" /> </syn:sequence>"));
		selenium.click("link=switch to design view");
		selenium.waitForPageToLoad("30000");
		selenium.click("link=Sequence");
        selenium.waitForPageToLoad("30000");
		selenium.click("//a[@onclick='showSource()']");
        selenium.waitForPageToLoad("30000");

        //Verifying the source view of the Mediator
        assertTrue(selenium.isTextPresent("<syn:sequence xmlns:syn=\"http://ws.apache.org/ns/synapse\" key=\"fault\" />"));
		selenium.click("link=switch to design view");
    }

}
