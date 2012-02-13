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
package org.wso2.startos.system.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;

public class StratosRegressionTestRunner extends TestSuite {

    public static Test suite() {
        TestSuite testSuite = new TestSuite();
        FrameworkSettings.getFrameworkProperties();
        String expectedServerHostName = "appserver.stratoslive.wso2.com";
        String actualServerHostName = FrameworkSettings.APP_SERVER_HOST_NAME;

        //execute tests only on StraotsLive - We have manually deployed artifacts on each service. These tests can be used to test proper data migration etc..
        if (FrameworkSettings.getStratosTestStatus() && actualServerHostName.equals(expectedServerHostName)) {
            testSuite.addTestSuite(StratosAppServiceTest.class);
            testSuite.addTestSuite(StratosBAMServiceTest.class);
            testSuite.addTestSuite(StratosBPSServiceTest.class);
            testSuite.addTestSuite(StratosBRSServiceTest.class);
            testSuite.addTestSuite(StratosCEPServiceTest.class);
            testSuite.addTestSuite(StratosDSSServiceTest.class);
            testSuite.addTestSuite(StratosESBServiceTest.class);
            testSuite.addTestSuite(StratosGREGServiceTest.class);
            testSuite.addTestSuite(StratosGSServiceTest.class);
            testSuite.addTestSuite(StratosISServiceTest.class);
            testSuite.addTestSuite(StratosManagerServiceTest.class);
            testSuite.addTestSuite(StratosMBServiceTest.class);
            testSuite.addTestSuite(StratosMSServiceTest.class);
        }

        return testSuite;
    }
}
