/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.web.test.registry;

import junit.framework.TestCase;
import com.thoughtworks.selenium.Selenium;

import java.util.Properties;

import org.wso2.carbon.web.test.common.RegistryCommon;
import org.wso2.carbon.web.test.common.SeleniumTestBase;


public class GaaSTesting extends TestCase {

    Selenium selenium;
    Properties property;
    RegistryCommon registryCommon;
    SeleniumTestBase UmCommon;
    String adminUserName;
    String adminPassword;
    String Curspeed, NewSpeed;

    public void setUp() throws Exception {

        property = BrowserInitializer.getProperties();
        selenium = BrowserInitializer.getBrowser();
        registryCommon = new RegistryCommon(selenium);
        UmCommon = new SeleniumTestBase(selenium);
        adminUserName = property.getProperty("admin.username");
        adminPassword = property.getProperty("admin.password");
    }

    public void testResourceTagCount() throws Exception {


        assertTrue(selenium.isTextPresent("Browse"));
        selenium.click("link=Add Resource");
        assertTrue(selenium.isTextPresent("Add Resource"));
        Thread.sleep(1000);

        for (int i = 0; i <= 100; i++) {

            selenium.select("addMethodSelector", "label=Create Text content");
            selenium.click("//option[@value='text']");
            selenium.type("trFileName", "resourceName" + 1);
            selenium.type("trDescription", "resourceNameDesc");
            selenium.click("//input[@value='Add' and @type='button' and @onclick='whileUpload();submitTextContentForm();']");
            Thread.sleep(2000);


            registryCommon.resourcetextContentMsg();
        }

    }
}

