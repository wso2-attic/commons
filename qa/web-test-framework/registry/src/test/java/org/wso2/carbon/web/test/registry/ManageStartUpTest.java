/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.registry;

import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.StartUpTest;

public class ManageStartUpTest extends TestCase {

    Selenium browser;
    StartUpTest startUpBrowser;

    public ManageStartUpTest(String s) {
        super(s);
    }

    public void setUp() throws Exception {
        browser = BrowserInitializer.getBrowser();
        startUpBrowser = new StartUpTest(browser);
    }

    public void testErrorLogTesting() throws Exception {
        startUpBrowser.ErorrsInStartUp("wso2carbon.log");
    }
}
