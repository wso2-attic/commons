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


public class GSAnnonUser extends CommonSetup {

    public GSAnnonUser(String text) {
        super(text);
    }


    public void testAnnonUserDefaultUI() throws Exception {
        selenium.open("/carbon/dashboard/index.jsp");
        assertTrue(selenium.isTextPresent("* Create your account now"));
        assertEquals("Sign-in", selenium.getText("link=Sign-in"));
        assertEquals("Home", selenium.getText("tab0"));
        assertEquals("Add Gadgets", selenium.getText("link=Add Gadgets"));
        assertEquals("Add New Tab", selenium.getText("link=Add New Tab"));
        assertEquals("Remove Active Tab", selenium.getText("link=Remove Active Tab"));


        assertEquals("JIRA Issues", selenium.getText("remote_iframe_0_title"));
        assertEquals("????? On-Screen Keyboard", selenium.getText("remote_iframe_2_title"));
        assertEquals("SOA Platform Blog", selenium.getText("remote_iframe_3_title"));
        assertEquals("TwitterGadget", selenium.getTable("gadgets-gadget-title-bar-1.0.0"));
        selenium.click("remote_iframe_1_title");
        selenium.click("link=Add Gadgets");
        assertEquals("JIRA Gadget\n Sinhala Keyboard Gadget\n SOA Platform Blog Feed Gadget\n Twitter Gadget", selenium.getText("//div[@id='newGadgetsPane']/table/tbody/tr[1]/td[1]"));
        assertEquals("", selenium.getText("//input[@name='checkgroup' and @value='http://10.100.1.120:9763/registry/resource/system/gadgets/twitter.xml']"));
        assertEquals("", selenium.getText("//input[@name='checkgroup' and @value='http://10.100.1.120:9763/registry/resource/system/gadgets/soa.xml']"));
        assertEquals("", selenium.getText("//input[@name='checkgroup' and @value='http://10.100.1.120:9763/registry/resource/system/gadgets/sinhala.xml']"));
    }


    public static void testAnnonUserSetup() throws Exception {
        String[] defaultGadgets = {"JIRA Gadget", "Sinhala Keyboard Gadget", "SOA Platform Blog Feed Gadget", "Twitter Gadget"};
        int i = 3, k = 1;
        for (k = 0; k <= i; k++) {
            boolean value = GSCommon.annonUserSetup(defaultGadgets[k]);

            while (value) {
                if (defaultGadgets.equals("Sinhala Keyboard Gadget")) {
                    assertEquals("????? On-Screen Keyboard", selenium.getText("remote_iframe_0_title"));
                    assertEquals("", selenium.getText("//input[@name='checkgroup' and @value='http://10.100.1.120:9763/registry/resource/system/gadgets/jira-gadget.xml']"));
                } else if (defaultGadgets.equals("JIRA Gadget")) {
                    assertEquals("JIRA Issues", selenium.getText("remote_iframe_1_title"));
                    assertEquals("", selenium.getText("//input[@name='checkgroup' and @value='http://10.100.1.120:9763/registry/resource/system/gadgets/sinhala.xml']"));
                } else if (defaultGadgets.equals("SOA Platform Blog Feed Gadget")) {
                    assertEquals("SOA Platform Blog", selenium.getText("remote_iframe_2_title"));
                    assertEquals("", selenium.getText("//input[@name='checkgroup' and @value='http://10.100.1.120:9763/registry/resource/system/gadgets/soa.xml']"));
                } else if (defaultGadgets.equals("Twitter Gadget")) {
                    assertEquals("TwitterGadget", selenium.getText("remote_iframe_3_title"));
                    assertEquals("", selenium.getText("//input[@name='checkgroup' and @value='http://10.100.1.120:9763/registry/resource/system/gadgets/twitter.xml']"));
                } else {
                    System.out.println("No gadgets are enabled for annon user");
                }
            }
        }
    }
}
