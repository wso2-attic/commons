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
package org.wso2.carbon.web.test.csg;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;

import java.util.Properties;
import java.util.Enumeration;


public class AllTests extends TestSuite {
    public AllTests(String Name) {
        super(Name);
    }

    public static void main(String srgs[]) {
        try {
            TestRunner.run(suite());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        String testName = "";
        Properties sysprops = System.getProperties();
        boolean all = true;
        for (Enumeration e = sysprops.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();

            if (key.equals("test.suite")) {
                testName = System.getProperty("test.suite");
            }
            all = testName == null || testName.equalsIgnoreCase("all") || testName.equals("${test.suite}");
        }
        // String testName=System.getProperty("test.suite");
        System.out.println("System property " + testName);
        if (!all) {
            System.out.println("Running Test: " + testName);
            suite.addTestSuite(CSGTest.class);
        } else {
            System.out.println("Running all tests.");
        }

        if (testName.equalsIgnoreCase("SetupClientService")) {
            suite.addTestSuite(CSGTest.class);

        } else if (all) {

            suite.addTestSuite(CSGTest.class);

        }


        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() throws Exception {
                oneTimeSetUp();
            }

            private void oneTimeSetUp() throws Exception {
                //Settings.loadProperties();
                //PrepareDatabase.blowAwayDatabase();
                BrowserInitializer.initBrowser();
            }

            private void oneTimeTearDown() {
                BrowserInitializer.stopBrowser();
            }

            protected void tearDown() throws Exception {
                oneTimeTearDown();
            }
        };

        return wrapper;
    }
}
            
