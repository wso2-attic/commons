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

public class ESBManageSynapseConfigurationUITest extends TestCase {
    Selenium selenium;

    public ESBManageSynapseConfigurationUITest(Selenium _browser){
        selenium = _browser;
    }

    /*
    This test will verify the properties of the Manage Synapse Configuration page
     */
    public void testVerifyManageSynapseConfig() throws Exception{
		selenium.click("link=Synapse");
		Thread.sleep(2000);
		assertTrue(selenium.isTextPresent("Manage Synapse Configuration"));
		assertTrue(selenium.isTextPresent("'Update' the configuration if any changes have been made manually. Then 'Save' to commit the current operational configuration to disk."));
		assertTrue(selenium.isTextPresent("Current Configuration"));
		assertEquals("", selenium.getTable("//form[@id='configform']/table.2.0"));
		selenium.click("updateConfig");
        Thread.sleep(5000);
		assertTrue(selenium.isTextPresent("Configuration updated successfully.."));
		assertEquals("OK", selenium.getText("//button[@type='button']"));
        selenium.click("//button[@type='button']");
        selenium.click("saveConfig");
        Thread.sleep(5000);
		assertTrue(selenium.isTextPresent("Configuration saved successfully.."));
		assertEquals("OK", selenium.getText("//button[@type='button']"));
        selenium.click("//button[@type='button']");
    }

}
