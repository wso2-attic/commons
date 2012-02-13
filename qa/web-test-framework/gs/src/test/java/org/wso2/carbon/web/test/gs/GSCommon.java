package org.wso2.carbon.web.test.gs;

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

import com.thoughtworks.selenium.*;

import java.awt.event.KeyEvent;
import java.util.Properties;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;

import junit.framework.TestCase;
import org.wso2.carbon.web.test.common.SeleniumTestBase;
import org.wso2.carbon.web.test.common.ServiceManagement;
import org.wso2.carbon.web.test.common.ModuleManagement;
import org.wso2.carbon.web.test.common.Tryit;

import java.util.*;
import java.text.*;


public class GSCommon extends CommonSetup {

    public GSCommon(String text) {
        super(text);
    }


    // Loading the property file.
    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(".." + File.separator + "commons" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "framework.properties"));
        return properties;
    }


    public static boolean annonUserSetup(String defaultGadgetsList) throws Exception {
        selenium.click("link=Gadget Repository");
        selenium.waitForPageToLoad("30000");

        String defaultGadgets = defaultGadgetsList;
        boolean value = false;
        int i = 3, k = 1;
        for (k = 0; k <= i; k++) {
            String actual = selenium.getText("//table[@id='table-" + k + "']/tbody/tr/td[2]/p/label[1]/strong");
            if (actual.equals(defaultGadgets)) {
                String makeDefault = selenium.getValue("unsignedUserGadget-" + k);
                System.out.println(makeDefault);
                if (makeDefault.equals("on")) {
                    System.out.println("Show Gadget To Anonymous User is  set to ON for " + defaultGadgets);
                    value = true;
                    return value;
                } else {
                    System.out.println("Show Gadget To Anonymous User is  set to OFF for " + defaultGadgets);
                    return value;
                }
            }
        }
        return value;
    }

}


