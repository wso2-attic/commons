package org.wso2.carbon.web.test.registry;

import com.thoughtworks.selenium.Selenium;

import java.util.Properties;

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

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

public class RegistryPaginationTest extends CommonSetup {
    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    public RegistryPaginationTest(String txt) {
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
    public void testresource() throws Exception {
        String res1="resourceRegistryPagination1";
        String res2="resourceRegistryPagination2";
        String res3="resourceRegistryPagination3";
        String res4="resourceRegistryPagination4";
        String res5="resourceRegistryPagination5";
        String res6="resourceRegistryPagination6";
        String res7="resourceRegistryPagination7";
        String res8="resourceRegistryPagination8";
        String res9="resourceRegistryPagination9";
        String res10="resourceRegistryPagination10";
        String res11="resourceRegistryPagination11";
        String res12="resourceRegistryPagination12";
        String res13="resourceRegistryPagination13";
        String res14="resourceRegistryPagination14";
        String res15="resourceRegistryPagination15";
        String res16="resourceRegistryPagination16";
        String res17="resourceRegistryPagination17";
        String res18="resourceRegistryPagination18";
        String res19="resourceRegistryPagination19";
        String res20="resourceRegistryPagination20";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        registryCommon.addTextResourceToRoot(res1);
        registryCommon.addTextResourceToRoot(res2);
        registryCommon.addTextResourceToRoot(res3);
        registryCommon.addTextResourceToRoot(res4);
        registryCommon.addTextResourceToRoot(res5);
        registryCommon.addTextResourceToRoot(res6);
        registryCommon.addTextResourceToRoot(res7);
        registryCommon.addTextResourceToRoot(res8);
        registryCommon.addTextResourceToRoot(res9);
        registryCommon.addTextResourceToRoot(res10);
        registryCommon.addTextResourceToRoot(res11);
        registryCommon.addTextResourceToRoot(res12);
        registryCommon.addTextResourceToRoot(res13);
        registryCommon.addTextResourceToRoot(res14);
        registryCommon.addTextResourceToRoot(res15);
        registryCommon.addTextResourceToRoot(res16);
        registryCommon.addTextResourceToRoot(res17);
        registryCommon.addTextResourceToRoot(res18);
        registryCommon.addTextResourceToRoot(res19);
        registryCommon.addTextResourceToRoot(res20);
        assertTrue(registryCommon.exploreThroughPaging("/","system"));
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        Thread.sleep(2000);
        assertTrue(registryCommon.exploreThroughPaging("/",res5));
        Thread.sleep(2000);
        UmCommon.logOutUI();
    }
    public void testcollection() throws Exception {
        String res1="collectionRegistryPagination1";
        String res2="collectionRegistryPagination2";
        String res3="collectionRegistryPagination3";
        String res4="collectionRegistryPagination4";
        String res5="collectionRegistryPagination5";
        String res6="collectionRegistryPagination6";
        String res7="collectionRegistryPagination7";
        String res8="collectionRegistryPagination8";
        String res9="collectionRegistryPagination9";
        String res10="collectionRegistryPagination10";
        String res11="collectionRegistryPagination11";
        String res12="collectionRegistryPagination12";
        String res13="collectionRegistryPagination13";
        String res14="collectionRegistryPagination14";
        String res15="collectionRegistryPagination15";
        String res16="collectionRegistryPagination16";
        String res17="collectionRegistryPagination17";
        String res18="collectionRegistryPagination18";
        String res19="collectionRegistryPagination19";
        String res20="collectionRegistryPagination20";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(res1,"","");
        registryCommon.addCollectionToRoot(res2,"","");
        registryCommon.addCollectionToRoot(res3,"","");
        registryCommon.addCollectionToRoot(res4,"","");
        registryCommon.addCollectionToRoot(res5,"","");
        registryCommon.addCollectionToRoot(res6,"","");
        registryCommon.addCollectionToRoot(res7,"","");
        registryCommon.addCollectionToRoot(res8,"","");
        registryCommon.addCollectionToRoot(res9,"","");
        registryCommon.addCollectionToRoot(res10,"","");
        registryCommon.addCollectionToRoot(res11,"","");
        registryCommon.addCollectionToRoot(res12,"","");
        registryCommon.addCollectionToRoot(res13,"","");
        registryCommon.addCollectionToRoot(res14,"","");
        registryCommon.addCollectionToRoot(res15,"","");
        registryCommon.addCollectionToRoot(res16,"","");
        registryCommon.addCollectionToRoot(res17,"","");
        registryCommon.addCollectionToRoot(res18,"","");
        registryCommon.addCollectionToRoot(res19,"","");
        registryCommon.addCollectionToRoot(res20,"","");
        assertTrue(registryCommon.exploreThroughPaging("/","system"));
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        Thread.sleep(2000);
        assertTrue(registryCommon.exploreThroughPaging("/",res5));
        Thread.sleep(2000);
        UmCommon.logOutUI();
    }
    public void testcollectionWithinCollection() throws Exception {
        String newRoot="newroot";
        String res1="collectionRegistryPagination1";
        String res2="collectionRegistryPagination2";
        String res3="collectionRegistryPagination3";
        String res4="collectionRegistryPagination4";
        String res5="collectionRegistryPagination5";
        String res6="collectionRegistryPagination6";
        String res7="collectionRegistryPagination7";
        String res8="collectionRegistryPagination8";
        String res9="collectionRegistryPagination9";
        String res10="collectionRegistryPagination10";
        String res11="collectionRegistryPagination11";
        String res12="collectionRegistryPagination12";
        String res13="collectionRegistryPagination13";
        String res14="collectionRegistryPagination14";
        String res15="collectionRegistryPagination15";
        String res16="collectionRegistryPagination16";
        String res17="collectionRegistryPagination17";
        String res18="collectionRegistryPagination18";
        String res19="collectionRegistryPagination19";
        String res20="collectionRegistryPagination20";
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(newRoot,"","");
        assertTrue(registryCommon.exploreThroughPaging("/",newRoot));
        registryCommon.addCollectionToRoot(res1,"","");
        registryCommon.addCollectionToRoot(res2,"","");
        registryCommon.addCollectionToRoot(res3,"","");
        registryCommon.addCollectionToRoot(res4,"","");
        registryCommon.addCollectionToRoot(res5,"","");
        registryCommon.addCollectionToRoot(res6,"","");
        registryCommon.addCollectionToRoot(res7,"","");
        registryCommon.addCollectionToRoot(res8,"","");
        registryCommon.addCollectionToRoot(res9,"","");
        registryCommon.addCollectionToRoot(res10,"","");
        registryCommon.addCollectionToRoot(res11,"","");
        registryCommon.addCollectionToRoot(res12,"","");
        registryCommon.addCollectionToRoot(res13,"","");
        registryCommon.addCollectionToRoot(res14,"","");
        registryCommon.addCollectionToRoot(res15,"","");
        registryCommon.addCollectionToRoot(res16,"","");
        registryCommon.addCollectionToRoot(res17,"","");
        registryCommon.addCollectionToRoot(res18,"","");
        registryCommon.addCollectionToRoot(res19,"","");
        registryCommon.addCollectionToRoot(res20,"","");
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        Thread.sleep(2000);
        assertTrue(registryCommon.exploreThroughPaging("/"+newRoot,res1));
        Thread.sleep(2000);
        registryCommon.clickRootIcon();
        Thread.sleep(2000);
        assertTrue(registryCommon.exploreThroughPaging("/"+newRoot,res9));
        Thread.sleep(2000);
        UmCommon.logOutUI();
    }
}
