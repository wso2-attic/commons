/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/
package org.wso2.carbon.usermanagement.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.common.test.utils.FrameworkSettings;
import static org.wso2.carbon.common.test.utils.FrameworkSettings.BACKENDSERVER_RUNNING;

import java.util.Properties;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: asela
 * Date: Aug 3, 2010
 * Time: 10:59:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestRunner {
        public static Test suite() throws Exception {
        FrameworkSettings.getProperty();
        TestSuite testSuite = new TestSuite();

        String testName = "";
        Properties sysProps = System.getProperties();

        for (Enumeration e = sysProps.propertyNames(); e.hasMoreElements();) {

            String key = (String) e.nextElement();

            if (key.equals("test.suite")) {                
                testName = System.getProperty("test.suite");
            }
        }

        if (testName.equalsIgnoreCase("UserTest")) {
            testSuite.addTestSuite(UserTestInit.class);
            testSuite.addTestSuite(UserRoleUnitTest.class);
        } else if (testName.equalsIgnoreCase("RoleTest")) {
            testSuite.addTestSuite(UserTestInit.class);
            testSuite.addTestSuite(UserRoleUnitTest.class);

        } else {
            System.out.println("Running all User Management tests.");
            testSuite.addTestSuite(UserTestInit.class);
            testSuite.addTestSuite(UserUnitTest.class);
            testSuite.addTestSuite(UserRoleUnitTest.class);
        }
        return testSuite;
    }
}
