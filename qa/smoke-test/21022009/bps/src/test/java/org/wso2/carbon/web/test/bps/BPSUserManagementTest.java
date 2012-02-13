package org.wso2.carbon.web.test.bps;

import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class BPSUserManagementTest extends CommonSetup{
    public BPSUserManagementTest(String text) {
        super(text);
    }

    public void testUserManagement() throws Exception {
        BPSLogin.loginToConsole("admin", "admin");

        SeleniumTestBase stb = new SeleniumTestBase(browser);
        stb.addNewUser("charitha", "charitha");

        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");
        


     }
}
