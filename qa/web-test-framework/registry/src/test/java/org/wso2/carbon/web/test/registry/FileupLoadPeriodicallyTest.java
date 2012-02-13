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

import java.util.Properties;

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;

public class FileupLoadPeriodicallyTest extends CommonSetup {
    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    String Curspeed;

    public FileupLoadPeriodicallyTest(String txt) {
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

    public void addResources() throws Exception {
        String path = new java.io.File(".").getCanonicalPath();
        String filePath = "/lib/resources";
        String fileName="";
        String[] files = {"Screenshot-1.png","Screenshot-2.png","Screenshot-3.png","Screenshot-4.png","Screenshot-5.png",
                "Screenshot-6.png","Screenshot-7.png","Screenshot-8.png","governance-registry-datasheet.pdf","governance-registry-datasheet 3rd copy.pdf",
                "governance-registry-datasheet 4th copy.pdf","governance-registry-datasheet another copy.pdf","governance-registry-datasheet copy.pdf",
                "wso2_esb_product_data_sheet.pdf","wso2_esb_product_data_sheet 3rd copy.pdf","wso2_esb_product_data_sheet 4th copy.pdf",
                "wso2_esb_product_data_sheet another copy.pdf","wso2_esb_product_data_sheet copy.pdf"};

        String collectionName=registryCommon.getTimeStamp();
        registryCommon.signOut();
        UmCommon.loginToUI(adminUserName, adminPassword);
        registryCommon.gotoBrowsePage();
        registryCommon.addCollectionToRoot(collectionName,"","");
        registryCommon.gotopath("/",collectionName);
        for (int j = 0; j < files.length; j++) {
            fileName = files[j];

            registryCommon.addResFromLocalFS(path+filePath+"/"+fileName,fileName);
        }


        UmCommon.logOutUI();

    }

    public void testuploadResources() throws Exception {
        int iterations=7;
        for (int j = 0; j < iterations; j++) {
            addResources();
        }
    }

}