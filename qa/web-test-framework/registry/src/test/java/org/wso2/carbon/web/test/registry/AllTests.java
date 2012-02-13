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

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

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

        String testName = System.getProperty("test.suite");
        boolean all = testName == null || testName.equalsIgnoreCase("all") || testName.equals("${test.suite}");

//        suite.addTestSuite(ManageRegistryBrowserTest.class);

        /// / suite.addTest(new CleanUpTest("testCleanRegistry"));
        suite.addTest(new ManageRegistryBrowserTest("testAnonymousAccessHttps"));
        suite.addTest(new ManageRegistryBrowserTest("testAnoumousAccesshttp"));
//        suite.addTest(new MetaDataTest("testDeleteDependency"));
//        suite.addTest(new MetaDataTest("testAddNewDepenToWsdl"));
//        suite.addTest(new MetaDataTest("testAddAssociation"));
//        suite.addTest(new MetaDataTest("testWsdlValidation"));
//        suite.addTest(new MetaDataTest("testCorrectnessInvalidWsdl"));
//
        

//        if (!all) {
//            System.out.println("Running Test: " + testName);
//        } else {
//            System.out.println("Running all tests.");
//        }
//
//        if (all || testName.equalsIgnoreCase("ManageStartUpTest")) {
//            suite.addTestSuite(ManageStartUpTest.class);
//        }
//
//        if (all || testName.equalsIgnoreCase("RegistryTags")) {
//            suite.addTestSuite(RegistryTags.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("SearchTest")) {
//            suite.addTestSuite(SearchTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("RegistryComment")) {
//            suite.addTestSuite(RegistryComment.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("RegistryRating")) {
//            suite.addTestSuite(RegistryRating.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("RegistryProperties")) {
//            suite.addTestSuite(RegistryProperties.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("ManageRegistryBrowserTest")) {
//            suite.addTestSuite(ManageRegistryBrowserTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("AssociationTest")) {
//            suite.addTestSuite(AssociationTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("DependencyTest")) {
//            suite.addTestSuite(DependencyTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("MyProfileTest")) {
//            suite.addTestSuite(MyProfileTest.class);
//            suite.addTest(new CleanUpTest("testFinalizeUsers"));
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("MetaDataTest")) {
//            suite.addTestSuite(MetaDataTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("NotificationTest")) {
//            suite.addTestSuite(NotificationTest.class);
//            suite.addTest(new CleanUpTest("testfinalizeNotificationTest"));
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("LifeCycleTest")) {
//            suite.addTestSuite(LifeCycleTest.class);
//            suite.addTest(new CleanUpTest("testFinalizeLifeCycle"));
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//            suite.addTest(new CleanUpTest("testCleanUpLCs"));
//        }
//        if (all || testName.equalsIgnoreCase("DistributedLCMTest")) {
//            suite.addTestSuite(DistributedLCMTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("ActivityTest")) {
//            suite.addTestSuite(ActivityTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("HelpPagesTest")) {
//            suite.addTestSuite(HelpPagesTest.class);
//        }
//        if (all || testName.equalsIgnoreCase("FeedsTest")) {
//            suite.addTestSuite(FeedsTest.class);
//        }
//        if (all || testName.equalsIgnoreCase("RegistryPaginationTest")) {
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//            suite.addTestSuite(RegistryPaginationTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }
//        if (all || testName.equalsIgnoreCase("CheckPointRestoreTest")) {
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//            suite.addTestSuite(CheckPointRestoreTest.class);
//            suite.addTest(new CleanUpTest("testCleanRegistry"));
//        }


        return new TestSetup(suite) {


            protected void setUp() throws Exception {
                oneTimeSetUp();
            }

            private void oneTimeSetUp() throws Exception {
                BrowserInitializer.initProperty();
                BrowserInitializer.initBrowser();
            }

            private void oneTimeTearDown() {
                BrowserInitializer.stopBrowser();
            }

            protected void tearDown() {
                oneTimeTearDown();
            }
        };
    }
}
