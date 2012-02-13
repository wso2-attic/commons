package org.wso2.carbon.web.test.bps;

import org.wso2.carbon.web.test.common.KeyStoreManagement;

import java.util.Properties;
import java.io.FileInputStream;/*
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

public class BPSKeystoreManagement extends CommonSetup  {
    public BPSKeystoreManagement(String text) {
        super(text);
    }


    public void testKeyStoreManagement() throws Exception {

        Properties properties = new Properties();
        properties.load(new FileInputStream("..\\commons\\src\\test\\resources\\framework.properties"));
        String keystorePath = properties.getProperty("keystorePath");

        BPSLogin.loginToConsole("admin", "admin");
        KeyStoreManagement keyStoreManagement = new KeyStoreManagement(browser);
        keyStoreManagement.AddKeystore(keystorePath, "qaserver");
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");
    }
}
