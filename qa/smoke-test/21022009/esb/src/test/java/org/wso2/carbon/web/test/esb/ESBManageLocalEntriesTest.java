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
import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class ESBManageLocalEntriesTest extends TestCase {
    Selenium selenium;

    public ESBManageLocalEntriesTest(Selenium _browser){
		selenium = _browser;
    }

    /*
	 * This method will add a local entry
	 */
    public void testAddLocalEntry(String entryType, String name, String value) throws Exception{
		selenium.click("link=Local Entries");
        selenium.waitForPageToLoad("30000");

        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(selenium);
        boolean localEntry = selenium.isTextPresent(name);

        if (localEntry){
            //Do nothing
        }else {
            if (entryType.equals("Add Source URL Entry")){
                selenium.click("link=Add Source URL Entry");
                selenium.waitForPageToLoad("30000");
                selenium.type("Name", name);
                selenium.type("Value", value);
                selenium.click("//input[@value='Save']");
                selenium.waitForPageToLoad("30000");
            } else if (entryType.equals("Add In-lined XML Entry")){
                selenium.click("link=Add In-lined XML Entry");
                selenium.waitForPageToLoad("30000");
                selenium.type("Name", name);
                selenium.type("entry_value", value);
                selenium.click("//input[@value='Save']");
                selenium.waitForPageToLoad("30000");
            } else if (entryType.equals("Add In-lined Text Entry")){
                selenium.click("link=Add In-lined Text Entry");
                selenium.waitForPageToLoad("30000");
                selenium.type("Name", name);
                selenium.type("Value", value);
                selenium.click("//input[@value='Save']");
                selenium.waitForPageToLoad("30000");
            }            
        }

    }
}
