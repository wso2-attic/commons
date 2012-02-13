package org.wso2.carbon.web.test.bps;

import java.awt.event.KeyEvent;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
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

public class BPSFileUploadTest extends CommonSetup {


    public BPSFileUploadTest(String text) {
        super(text);
    }


    public void testBPELUpload() throws Exception {
        Properties properties = new Properties();
        SeleniumTestBase seleniumTestBase = new SeleniumTestBase(browser);
        properties.load(new FileInputStream("src" + File.separator + "test" + File.separator + "resources" + File.separator + "bps.properties"));
        String packages[] = new String[2];
        packages[0] = properties.getProperty("bpel.package.path1");
        packages[1] = properties.getProperty("bpel.package.path2");
        BPSLogin.loginToConsole("admin", "admin");

        for (int i = 0; i < packages.length; i++) {
            browser.open("/carbon/bpel/summary_page.jsp?region=region2&item=summary_menu");
            browser.waitForPageToLoad("30000");
            browser.click("link=Add BPEL");
            browser.waitForPageToLoad("30000");
            seleniumTestBase.SetFileBrowse("bpelFileName", packages[i]);
            browser.click("upload");
            Thread.sleep(5000);
            //browser.waitForPageToLoad("30000");
            //verifyTrue(browser.isTextPresent("BPEL Package Uploaded Successfully!"));
            //browser.waitForPageToLoad("30000");
            browser.click("//button[@type='button']");
            browser.click("link=Deployed Packages");
            browser.waitForPageToLoad("30000");
            Thread.sleep(10000);
            browser.refresh();
            Thread.sleep(10000);
        }
        assertTrue(browser.isTextPresent("HelloWorldNew"));
        assertTrue(browser.isTextPresent("counter"));
        browser.click("link=Sign-out");
        browser.waitForPageToLoad("30000");


    }
}
