/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.automation.selenium.test.greg;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;


public class GregSeleniumTestRunner extends TestSuite {
    public static Test suite() {
        TestSuite testSuite = new TestSuite();

        if (!(FrameworkSettings.getStratosTestStatus())) {
            testSuite.addTestSuite(GregLoginSeleniumTest.class);
            testSuite.addTestSuite(GregAddWsdlfromURLSeleniumTest.class);
            testSuite.addTestSuite(GregAddServiceSeleniumTest.class);
            testSuite.addTestSuite(GregAddSchemafromURLSeleniumTest.class);
            testSuite.addTestSuite(GregAddPolicyfromURLSeleniumTest.class);
            testSuite.addTestSuite(GregCreateCollectionSeleniumTest.class);
            testSuite.addTestSuite(GregAddResourcetoCollectionSeleniumTest.class);
            testSuite.addTestSuite(GregAddCommenttoCollectionSeleniumTest.class);
            testSuite.addTestSuite(GregApplyTagtoRootSeleniumTest.class);
            testSuite.addTestSuite(GregApplyTagtoCollectionSeleniumTest.class);
            testSuite.addTestSuite(GregApplyTagtoResourceSeleniumTest.class);


        }
        return testSuite;
    }
}
