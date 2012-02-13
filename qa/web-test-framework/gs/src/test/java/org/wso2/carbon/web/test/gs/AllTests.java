package org.wso2.carbon.web.test.gs;

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

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;
import junit.extensions.TestSetup;

import java.io.IOException;

import org.wso2.carbon.web.test.gs.UserManagementTest;
import org.wso2.carbon.web.test.gs.LoggingTest;
import org.wso2.carbon.web.test.gs.GSAnnonUserTest;
import org.wso2.carbon.web.test.gs.BrowserInitializer;


public class AllTests extends TestSuite {
    public AllTests(String Name) {
        super(Name);
    }

    public static void main(String[] args) {
        try {
            TestRunner.run(suite());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(GSAnnonUserTest.class);

        TestSetup wrapper = new TestSetup(suite) {
            protected void setUp() throws IOException {
                oneTimeSetUp();
            }

            private void oneTimeSetUp() throws IOException {
                BrowserInitializer.initbrowser();
            }

            private void oneTimeTearDown() {
                BrowserInitializer.stopbrowser();
            }

            protected void tearDown() throws Exception {
                oneTimeTearDown();
            }
        };

        return wrapper;
    }
}