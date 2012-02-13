package org.wso2.carbon.web.test.gs;

import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.KeyStoreManagement;
import org.wso2.carbon.web.test.common.JDBCUserStore;
import org.wso2.carbon.web.test.common.ADUserStore;
import org.wso2.carbon.web.test.common.LDAPUSerStore;

import java.io.File;

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


public class GSGadgetRepo extends CommonSetup {

    public GSGadgetRepo(String text) {
        super(text);
    }


    public static void testGadgetRepoUI() throws Exception {
        selenium.click("link=Gadget Repository");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Gadget Repository"));
        assertEquals("Add New Gadget", selenium.getText("link=Add New Gadget"));
    }

    public static void testAddGadgettoRepo(String gadgetName, String gadgetUrl, String gadgetDesc, String gadgetScreen) throws Exception {
        selenium.click("link=Gadget Repository");
        selenium.click("link=Add New Gadget");
        selenium.waitForPageToLoad("30000");
        selenium.type("gadgetName", gadgetName);
        selenium.type("gadgetUrl", gadgetUrl);
        selenium.type("gadgetDesc", gadgetDesc);

        File path = new File(".." + File.separator + "mashup" + File.separator + "lib" + File.separator + gadgetScreen);        
        selenium.type("gadgetScreen", path.getCanonicalPath());
        selenium.click("addGadget");
        selenium.click("//button[@type='button']");
    }

    public static void testAddGadgettoRepo_ui() throws Exception {
        selenium.click("link=Gadget Repository");
        selenium.click("link=Add New Gadget");
        assertTrue(selenium.isTextPresent("New Gadget Information"));
        assertTrue(selenium.isTextPresent("Gadget Name *"));
        assertTrue(selenium.isTextPresent("Gadget URL *"));
        assertTrue(selenium.isTextPresent("Gadget Description"));
        assertTrue(selenium.isTextPresent("Gadget Screenshot"));
    }



    public static void testAddGadgettoRepo_reqFields(String gadgetName,String gadgetUrl) throws Exception {
        selenium.click("link=Gadget Repository");
        selenium.click("link=Add New Gadget");
        selenium.type("gadgetUrl", gadgetUrl);
        selenium.click("addGadget");
        assertEquals("Please enter the required information.", selenium.getText("messagebox-error"));
        selenium.click("//button[@type='button']");
        selenium.type("gadgetName", gadgetName);
        selenium.type("gadgetUrl", "");
        selenium.click("addGadget");
        assertEquals("Please enter the required information.", selenium.getText("messagebox-error"));
        selenium.click("//button[@type='button']");
    }



      public static void testGadgettoRepo_addedGadget(String gadgetName, String gadgetUrl, String gadgetDesc) throws Exception {
        selenium.click("link=Gadget Repository");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent(gadgetName));
        assertTrue(selenium.isTextPresent(gadgetUrl));
        assertTrue(selenium.isTextPresent(gadgetDesc));
    }
}
