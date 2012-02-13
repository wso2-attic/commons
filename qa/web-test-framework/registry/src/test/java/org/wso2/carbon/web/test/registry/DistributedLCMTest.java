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
import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

import java.util.Properties;


public class DistributedLCMTest extends CommonSetup {
    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    String Curspeed;
    String content = "<aspect name=\"ProjectChecklist\" class=\"org.wso2.carbon.governance.samples.lsm.DistributedLSM\">\n" +
            "\t\t<!-- Checklist can either be provided as a resource(type=resource) or as xml content(type=literal, default) as provided below. -->\n" +
            "\t\t<!-- <configuration type=\"resource\">/workspace/checklist</configuration> -->\n" +
            "\t\t<!-- OR -->\n" +
            "\t\t<configuration type=\"literal\">\n" +
            "\t\t\t<lifecycle>\n" +
            "    \t\t\t<state name=\"Initialize\" location=\"/environment/init\">\n" +
            "\t\t        \t<checkitem>Requirements Gathered</checkitem>\n" +
            "        \t\t\t<checkitem>Architecture Finalized</checkitem>\n" +
            "\t\t\t        <checkitem>High Level Design Completed</checkitem>\n" +
            "\t\t\t    </state>\n" +
            "\t\t\t    <state name=\"Designed\" location=\"/environment/design\">\n" +
            "\t\t\t        <checkitem>Code Completed</checkitem>\n" +
            "\t\t\t        <checkitem>WSDL, Schema Created</checkitem>\n" +
            "\t\t\t\t    <checkitem>QoS Created</checkitem>\n" +
            "\t\t\t        </state>\t\n" +
            "\t\t\t    <state name=\"Created\" location=\"/environment/development\">\n" +
            "\t\t\t        <checkitem>Effective Inspection Completed</checkitem>\n" +
            "\t\t\t        <checkitem>Test Cases Passed</checkitem>\n" +
            "\t\t\t        <checkitem>Smoke Test Passed</checkitem>\n" +
            "\t\t\t    </state>\n" +
            "\t\t\t    <state name=\"Tested\" location=\"/environment/qa\">\n" +
            "\t\t\t\t\t<checkitem>Service Configuration</checkitem>\n" +
            "\t\t\t          </state>\n" +
            "\t\t\t   <state name=\"Deployed\" location=\"/environment/prod\">\n" +
            "\t\t\t\t\t<checkitem>Service Configuration</checkitem>\n" +
            "\t\t\t\t</state>\n" +
            "\t\t\t\t<state name=\"Deprecated\" location=\"/environment/Deprecated\"> \n" +
            "\t\t\t\t</state>\n" +
            "\t\t\t</lifecycle>\n" +
            "\t\t</configuration>\n" +
            "\t</aspect>";
    String lcname = "ProjectChecklist";
    String lcNewName = "ProjectChecklist 2";
    String NewContentLC = "<aspect name=\"ProjectChecklist 2\" class=\"org.wso2.carbon.governance.samples.lsm.DistributedLSM\">\n" +
            "\t\t<!-- Checklist can either be provided as a resource(type=resource) or as xml content(type=literal, default) as provided below. -->\n" +
            "\t\t<!-- <configuration type=\"resource\">/workspace/checklist</configuration> -->\n" +
            "\t\t<!-- OR -->\n" +
            "\t\t<configuration type=\"literal\">\n" +
            "\t\t\t<lifecycle>\n" +
            "    \t\t\t<state name=\"Test Initialize\" location=\"/Test environment/Test init\">\n" +
            "\t\t        \t<checkitem>Requirements Gathered</checkitem>\n" +
            "        \t\t\t<checkitem>Architecture Finalized</checkitem>\n" +
            "\t\t\t        <checkitem>High Level Design Completed</checkitem>\n" +
            "\t\t\t    </state>\n" +
            "\t\t\t    <state name=\"Test Designed\" location=\"/Test environment/Test design\">\n" +
            "\t\t\t        <checkitem>Code Completed</checkitem>\n" +
            "\t\t\t        <checkitem>WSDL, Schema Created</checkitem>\n" +
            "\t\t\t\t    <checkitem>QoS Created</checkitem>\n" +
            "\t\t\t        </state>\t\n" +
            "\t\t\t    <state name=\"Created\" location=\"/Test environment/Test development\">\n" +
            "\t\t\t        <checkitem>Effective Inspection Completed</checkitem>\n" +
            "\t\t\t        <checkitem>Test Cases Passed</checkitem>\n" +
            "\t\t\t        <checkitem>Smoke Test Passed</checkitem>\n" +
            "\t\t\t    </state>\n" +
            "\t\t\t    <state name=\"Tested\" location=\"/Test environment/Test qa\">\n" +
            "\t\t\t\t\t<checkitem>Service Configuration</checkitem>\n" +
            "\t\t\t          </state>\n" +
            "\t\t\t   <state name=\"Deployed\" location=\"/Test environment/Test prod\">\n" +
            "\t\t\t\t\t<checkitem>Service Configuration</checkitem>\n" +
            "\t\t\t\t</state>\n" +
            "\t\t\t\t<state name=\"Deprecated\" location=\"/Test environment/Test Deprecated\"> \n" +
            "\t\t\t\t</state>\n" +
            "\t\t\t</lifecycle>\n" +
            "\t\t</configuration>\n" +
            "\t</aspect>";

    public DistributedLCMTest(String txt) {
        super(txt);

    }

    public void setUp() throws Exception {
        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public void testInitilize() throws Exception {
        String resName = "test text res";
        String colname = "test coll LC";
        String resNameForEdittedLC = "test text res New LC";
        String colnameForEdittedLC = "test coll New LC";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.cleanRegistry();
        registryCommon.addNewLC(lcname, content);
        registryCommon.addTextResourceToRoot(resName);
        registryCommon.addCollectionToRoot(colname, "", "");
        registryCommon.addNewLC(lcNewName, NewContentLC);
        registryCommon.addTextResourceToRoot(resNameForEdittedLC);
        registryCommon.addCollectionToRoot(colnameForEdittedLC, "", "");
        UmCommon.logOutUI();
    }

    public void testResource() throws Exception {
        String resName = "test text res";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotopath("/", resName);
        registryCommon.addLifeCycle(lcname);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertTrue(registryCommon.gotopath("/environment/design", resName));
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/environment/design", resName));
        registryCommon.gotopath("/environment/development", resName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/environment/development", resName));
        registryCommon.gotopath("/environment/qa", resName);
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/environment/qa", resName));
        registryCommon.gotopath("/environment/prod", resName);
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/environment/prod", resName));
        registryCommon.gotopath("/environment/Deprecated", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/Deprecated", resName));
        registryCommon.gotopath("/environment/prod", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/prod", resName));
        registryCommon.gotopath("/environment/qa", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/qa", resName));
        registryCommon.gotopath("/environment/development", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/development", resName));
        registryCommon.gotopath("/environment/design", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/design", resName));

        UmCommon.logOutUI();
    }

    public void testCollection() throws Exception {
        String colname = "test coll LC";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotopath("/environment/design", colname);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/environment/design", colname));
//        assertTrue(registryCommon.gotopath("/environment/development",colname));
        registryCommon.gotopath("/environment/development", colname);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/environment/development", colname));
        registryCommon.gotopath("/environment/qa", colname);
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/environment/qa", colname));
        registryCommon.gotopath("/environment/prod", colname);
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/environment/prod", colname));
        registryCommon.gotopath("/environment/Deprecated", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/Deprecated", colname));
        registryCommon.gotopath("/environment/prod", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/prod", colname));
        registryCommon.gotopath("/environment/qa", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/qa", colname));
        registryCommon.gotopath("/environment/development", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/development", colname));
        registryCommon.gotopath("/environment/design", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/environment/design", colname));
        UmCommon.logOutUI();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void testResourceWithNewDLC() throws Exception {
        String resName = "test text res New LC";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotopath("/", resName);
        registryCommon.addLifeCycle(lcNewName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertTrue(registryCommon.gotopath("/Test environment/Test design", resName));
        registryCommon.gotopath("/Test environment/Test design", resName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/Test environment/Test design", resName));
        registryCommon.gotopath("/Test environment/Test development", resName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/Test environment/Test development", resName));
        registryCommon.gotopath("/Test environment/Test qa", resName);
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/Test environment/Test qa", resName));
        registryCommon.gotopath("/Test environment/Test prod", resName);
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/Test environment/Test prod", resName));
        registryCommon.gotopath("/Test environment/Test Deprecated", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test Deprecated", resName));
        registryCommon.gotopath("/Test environment/Test prod", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test prod", resName));
        registryCommon.gotopath("/Test environment/Test qa", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test qa", resName));
        registryCommon.gotopath("/Test environment/Test development", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test development", resName));
        registryCommon.gotopath("/Test environment/Test design", resName);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test design", resName));
        UmCommon.logOutUI();
    }
     

    public void testNewColllectionWithNewDLC() throws Exception {
        String colname = "test coll New LC";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotopath("/", colname);
        registryCommon.addLifeCycle(lcNewName);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertTrue(registryCommon.gotopath("/Test environment/Test design", colname));
         registryCommon.gotopath("/Test environment/Test design", colname);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/Test environment/Test design", colname));
        registryCommon.gotopath("/Test environment/Test development", colname);
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/Test environment/Test development", colname));
        registryCommon.gotopath("/Test environment/Test qa", colname);
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/Test environment/Test qa", colname));
        registryCommon.gotopath("/Test environment/Test prod", colname);
        registryCommon.tickCheckList();
        assertFalse(registryCommon.gotopath("/Test environment/Test prod", colname));
        registryCommon.gotopath("/Test environment/Test Deprecated", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test Deprecated", colname));
        registryCommon.gotopath("/Test environment/Test prod", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test prod", colname));
        registryCommon.gotopath("/Test environment/Test qa", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test qa", colname));
         registryCommon.gotopath("/Test environment/Test development", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test development", colname));
        registryCommon.gotopath("/Test environment/Test design", colname);
        registryCommon.demoteLC();
        assertFalse(registryCommon.gotopath("/Test environment/Test design", colname));
        UmCommon.logOutUI();
    }

}