package org.wso2.carbon.web.test.is;

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


import junit.framework.TestCase;

import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

public class ISCommon extends CommonSetup {

    public ISCommon(String text) {
        super(text);
    }

    // Loading the property file.
    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
        return properties;
    }

    //Common login
    public static void testCommonLogin(String serverName, String userName, String passwd) throws Exception {
        selenium.open("https://" + loadProperties().getProperty("host.name") + ":" + loadProperties().getProperty("https.port." + serverName) + loadProperties().getProperty("context.root."+ serverName) + "/carbon/admin/login.jsp");
        Thread.sleep(10000);
        selenium.type("txtUserName", userName);
        selenium.type("txtPassword", passwd);
        selenium.click("//input[@value='Sign-in']");
        selenium.waitForPageToLoad("30000");
    }


    public static void forChangedContext() throws Exception {
        Properties properties = new Properties();

        FileInputStream freader = new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties");
        properties.load(freader);
        String context_root = properties.getProperty("context.root");

        if (context_root.equals(null)) {
            selenium.open("/carbon/admin/login.jsp");
//            Thread.sleep(8000);
        } else {
            selenium.open(context_root + "/carbon/admin/login.jsp");
//            Thread.sleep(8000);
        }
        for (int second = 0; ; second++) {
            if (second >= 15) fail("timeout");
            try {
                if (selenium.isElementPresent("txtUserName")) break;
            } catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }
}