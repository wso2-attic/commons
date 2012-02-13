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

package org.wso2.carbon.web.test.GaaS;

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
    String content = " <aspect name=\"ProjectChecklist\" class=\"org.wso2.carbon.governance.samples.lcm.DistributedLCM\">\n" +
            "            <!-- Checklist can either be provided as a resource(type=resource) or as xml content(type=literal, default) as provided below. -->\n" +
            "            <!-- <configuration type=\"resource\">/workspace/configuration</configuration> -->\n" +
            "            <!-- OR -->\n" +
            "            <configuration type=\"literal\">\n" +
            "                <lifecycle>\n" +
            "                    <state name=\"Design\" location=\"/environment/design\" roles=\"arch\">\n" +
            "                        <checkitem>Requirements Gathered</checkitem>\n" +
            "                        <checkitem>Architecture Finalized</checkitem>\n" +
            "                        <checkitem>High Level Design Completed</checkitem>\n" +
            "                    </state>\n" +
            "                    <state name=\"Development\" location=\"/environment/development\" roles=\"dev\">\n" +
            "                        <permissions>\n" +
            "                            <permission action=\"promote\" roles=\"tmc\"/>\n" +
            "                        </permissions>\n" +
            "                        <checkitem>Code Completed</checkitem>\n" +
            "                        <checkitem>WSDL, Schema Created</checkitem>\n" +
            "                        <checkitem>QoS Created</checkitem>\n" +
            "                    </state>\n" +
            "                    <state name=\"Testing\" location=\"/environment/qa\" roles=\"qa\">\n" +
            "                        <checkitem>Effective Inspection Completed</checkitem>\n" +
            "                        <checkitem>Test Cases Passed</checkitem>\n" +
            "                        <checkitem>Smoke Test Passed</checkitem>\n" +
            "                    </state>\n" +
            "                    <state name=\"Production\" location=\"/environment/production\" roles=\"prod\">\n" +
            "                        <checkitem>Project Item Deprecated</checkitem>\n" +
            "                    </state>\n" +
            "                    <state name=\"Deprecated\" location=\"/environment/deprecated\" roles=\"arch, prod\"/>\n" +
            "                </lifecycle>\n" +
            "            </configuration>\n" +
            "        </aspect>";
    String lcname = "ProjectChecklist";
    String lcNewName = "ProjectChecklist 2";
    String NewContentLC = " <aspect name=\"ProjectChecklist 2\" class=\"org.wso2.carbon.governance.samples.lcm.DistributedLCM\">\n" +
            "            <!-- Checklist can either be provided as a resource(type=resource) or as xml content(type=literal, default) as provided below. -->\n" +
            "            <!-- <configuration type=\"resource\">/workspace/configuration</configuration> -->\n" +
            "            <!-- OR -->\n" +
            "            <configuration type=\"literal\">\n" +
            "                <lifecycle>\n" +
            "                    <state name=\"Design\" location=\"/Test environment/Test design\" roles=\"arch\">\n" +
            "                        <checkitem>Requirements Gathered</checkitem>\n" +
            "                        <checkitem>Architecture Finalized</checkitem>\n" +
            "                        <checkitem>High Level Design Completed</checkitem>\n" +
            "                    </state>\n" +
            "                    <state name=\"Development\" location=\"/Test environment/Test development\" roles=\"dev\">\n" +
            "                        <permissions>\n" +
            "                            <permission action=\"promote\" roles=\"tmc\"/>\n" +
            "                        </permissions>\n" +
            "                        <checkitem>Code Completed</checkitem>\n" +
            "                        <checkitem>WSDL, Schema Created</checkitem>\n" +
            "                        <checkitem>QoS Created</checkitem>\n" +
            "                    </state>\n" +
            "                    <state name=\"Testing\" location=\"/Test environment/Test qa\" roles=\"qa\">\n" +
            "                        <checkitem>Effective Inspection Completed</checkitem>\n" +
            "                        <checkitem>Test Cases Passed</checkitem>\n" +
            "                        <checkitem>Smoke Test Passed</checkitem>\n" +
            "                    </state>\n" +
            "                    <state name=\"Production\" location=\"/Test environment/Test production\" roles=\"prod\">\n" +
            "                        <checkitem>Project Item Deprecated</checkitem>\n" +
            "                    </state>\n" +
            "                    <state name=\"Deprecated\" location=\"/Test environment/Test deprecated\" roles=\"arch, prod\"/>\n" +
            "                </lifecycle>\n" +
            "            </configuration>\n" +
            "        </aspect>";

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
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.addNewLC(lcname, content);
        registryCommon.addTextResourceToRoot(resName);
        registryCommon.addCollectionToRoot(colname, "", "");
        registryCommon.moveCollection(colname,"/","/environment/design");
        registryCommon.moveResource(registryCommon.getId(resName),"/","/environment/design");
        UmCommon.logOutUI();
    }

//    public void testResPromoteInitilize() throws Exception {
//        String resName = "test text res";
//        registryCommon.signOut();
//        UmCommon.loginToUI(adminUserName, adminPassword);
//        registryCommon.gotopath("/", resName);
//        registryCommon.addLifeCycle(lcname);
//        registryCommon.tickCheckList();
    //        registryCommon.tickCheckList();
    //        registryCommon.tickCheckList();
    //        assertTrue(registryCommon.gotopath("/environment/design",resName));
    //        UmCommon.logOutUI();
    //    }
    //
    public void testResPromoteDesign() throws Exception {
        String resName = "test text res";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotopath("/environment/design", resName);
        registryCommon.addLifeCycle(lcname);
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/environment/design",resName));
        assertTrue(registryCommon.gotopath("/environment/development",resName));
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/environment/development",resName));
        assertTrue(registryCommon.gotopath("/environment/qa", resName));
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/environment/qa", resName));
        assertTrue(registryCommon.gotopath("/environment/production", resName));
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/environment/production", resName));
        assertTrue(registryCommon.gotopath("/environment/deprecated", resName));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/environment/deprecated", resName));
        assertTrue(registryCommon.gotopath("/environment/production", resName));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/environment/production", resName));
        assertTrue( registryCommon.gotopath("/environment/qa", resName));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/environment/qa", resName));
        assertTrue(registryCommon.gotopath("/environment/development", resName));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/environment/development", resName));
        assertTrue(registryCommon.gotopath("/environment/design", resName));

//        assertTrue(registryCommon.gotopath("/environment/development",resName));
        UmCommon.logOutUI();
    }



//    public void testColPromoteInitilize() throws Exception {
//        String colname = "test coll LC";
//        registryCommon.signOut();
//        UmCommon.loginToUI(adminUserName, adminPassword);
//        registryCommon.gotopath("/", colname);
//
//        registryCommon.tickCheckList();
//        registryCommon.tickCheckList();
//        registryCommon.tickCheckList();
//        assertTrue(registryCommon.gotopath("/environment/design", colname));
//        UmCommon.logOutUI();
//    }

    public void testColPromoteDesign() throws Exception {
        String colname = "test coll LC";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotopath("/environment/design", colname);
        registryCommon.addLifeCycle(lcname);
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/environment/design", colname));
        registryCommon.gotopath("/environment/development", colname);
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/environment/development", colname));
        assertTrue(registryCommon.gotopath("/environment/qa", colname));
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/environment/qa", colname));
        assertTrue(registryCommon.gotopath("/environment/production", colname));
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/environment/production", colname));
        assertTrue(registryCommon.gotopath("/environment/deprecated", colname));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/environment/deprecated", colname));
        assertTrue(registryCommon.gotopath("/environment/production", colname));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/environment/production", colname));
        assertTrue(registryCommon.gotopath("/environment/qa", colname));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/environment/qa", colname));
        assertTrue(registryCommon.gotopath("/environment/development", colname));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/environment/development", colname));
        assertTrue(registryCommon.gotopath("/environment/design", colname));

//        assertTrue(registryCommon.gotopath("/environment/development",colname));
        UmCommon.logOutUI();
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void testNewInitilize() throws Exception {
        String resName = "test text res";
        String colname = "test coll LC";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.addNewLC(lcNewName, NewContentLC);
        registryCommon.addTextResourceToRoot(resName);
        registryCommon.addCollectionToRoot(colname, "", "");
        registryCommon.moveCollection(colname,"/","/Test environment/Test design");
        registryCommon.moveResource(registryCommon.getId(resName),"/","/Test environment/Test design");
        assertTrue(registryCommon.gotopath("/Test environment/Test design", resName));
        assertTrue(registryCommon.addLifeCycle(lcNewName));
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/Test environment/Test design", resName));
        assertTrue(registryCommon.gotopath("/Test environment/Test development", resName));
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/Test environment/Test development", resName));
        assertTrue(registryCommon.gotopath("/Test environment/Test qa", resName));
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue( registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/Test environment/Test qa", resName));
        assertTrue(registryCommon.gotopath("/Test environment/Test production", resName));
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/Test environment/Test production", resName));
        assertTrue(registryCommon.gotopath("/Test environment/Test deprecated", resName));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/Test environment/Test deprecated", resName));
        assertTrue(registryCommon.gotopath("/Test environment/Test production", resName));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/Test environment/Test production", resName));
        assertTrue(registryCommon.gotopath("/Test environment/Test qa", resName));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/Test environment/Test qa", resName));
        assertTrue(registryCommon.gotopath("/Test environment/Test development", resName));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/Test environment/Test development", resName));
        assertTrue(registryCommon.gotopath("/Test environment/Test design", resName));


        UmCommon.logOutUI();
    }

//    public void testNewResPromoteInitilize() throws Exception {
//        String resName = "test text res";
//        registryCommon.signOut();
//        UmCommon.loginToUI(adminUserName, adminPassword);
//        registryCommon.gotopath("/", resName);
//
//        registryCommon.tickCheckList();
//        registryCommon.tickCheckList();
//        registryCommon.tickCheckList();
//        assertTrue(registryCommon.gotopath("/Test environment/Test design", resName));
//        UmCommon.logOutUI();
//    }


    public void testNewColPromoteInitilize() throws Exception {
        String colname = "test coll LC";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotopath("/", colname);
        registryCommon.addLifeCycle(lcNewName);
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.gotopath("/Test environment/Test design", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test design", colname));
        assertTrue(registryCommon.addLifeCycle(lcNewName));
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/Test environment/Test design", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test development", colname));
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/Test environment/Test development", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test qa", colname));
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/Test environment/Test qa", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test production", colname));
        assertTrue(registryCommon.tickCheckList());
        assertFalse(registryCommon.gotopath("/Test environment/Test production", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test deprecated", colname));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/Test environment/Test deprecated", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test production", colname));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/Test environment/Test production", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test qa", colname));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/Test environment/Test qa", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test development", colname));
        assertTrue(registryCommon.demoteLC());
        assertFalse(registryCommon.gotopath("/Test environment/Test development", colname));
        assertTrue(registryCommon.gotopath("/Test environment/Test design", colname));

        UmCommon.logOutUI();
    }


}