package org.wso2.carbon.web.test.bps;/*
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


public class BPSPackageManagementTest extends CommonSetup {

    String processName = null;
    String packageName = null;

    public BPSPackageManagementTest(String text) {
        super(text);
    }


    public void testLogintoUI() throws Exception {
        BPSLogin.loginToConsole("admin", "admin");

    }


    public void testBPELPackagesHomepage() {

        browser.open("/carbon/bpel/bpel_packages.jsp?region=region2&item=deploy_menu");
        browser.click("link=Deployed Packages");
        browser.waitForPageToLoad("30000");
        assertEquals("Deployed Packages", browser.getText("//div[@id='middle']/h2"));
        assertTrue(browser.isElementPresent("//table[@id='packageListTable']/thead/th[1]"));
        assertEquals("Processes", browser.getText("//table[@id='packageListTable']/thead/th[2]"));
        assertEquals("Package", browser.getText("//table[@id='packageListTable']/thead/th[1]"));

    }

    public void testBPELPackages() {
        int rowcount = 1;
        browser.open("/carbon/bpel/bpel_packages.jsp?region=region2&item=deploy_menu");
        browser.click("link=Deployed Packages");
        browser.waitForPageToLoad("30000");

        while ((browser.isElementPresent("//table[@id='packageListTable']/tbody/tr[" + rowcount + "]/td[1]")) == true) {
            //pkgName = browser.getText("//table[@id='packageListTable']/tbody/tr["+rowcount+"]/td[1]");
            processName = browser.getText("//table[@id='packageListTable']/tbody/tr[" + rowcount + "]/td[2]");
            System.out.println(processName);
            processPackageList(processName);
            rowcount++;
        }

    }


    public void processPackageList(String processName) {

        browser.click("link=" + processName);
        browser.waitForPageToLoad("30000");
        assertTrue(browser.isTextPresent(processName));
        assertEquals(processName, browser.getText("//div[@id='middle']/h2"));
        browser.click("link=Deployed Packages");
        browser.waitForPageToLoad("30000");

    }

    public void testdeletePackages() {
        int rowcount = 1;
        browser.open("/carbon/bpel/bpel_packages.jsp?region=region2&item=deploy_menu");
        browser.click("link=Deployed Packages");
        browser.waitForPageToLoad("30000");

        while ((browser.isElementPresent("//table[@id='packageListTable']/tbody/tr[" + rowcount + "]/td[1]")) == true) {
            //pkgName = browser.getText("//table[@id='packageListTable']/tbody/tr["+rowcount+"]/td[1]");
            packageName = browser.getText("//table[@id='packageListTable']/tbody/tr[" + rowcount + "]/td[1]");
            browser.click("//a[@onclick='undeployPackage" + packageName + "()']");
            assertTrue(browser.isTextPresent("Do you want to undeploy package " + packageName + "?"));
            browser.click("//button[@type='button']");
            browser.waitForPageToLoad("30000");
            assertTrue(browser.isTextPresent("Package " + packageName + " undeployed successfully."));
            browser.click("//button[@type='button']");
            rowcount++;
        }

        browser.click("link=Sign-out");
        browser.waitForPageToLoad("300000");
    }


}
