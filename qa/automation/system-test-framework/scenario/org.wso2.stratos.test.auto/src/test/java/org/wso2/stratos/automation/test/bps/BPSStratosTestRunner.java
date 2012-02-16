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

package org.wso2.stratos.automation.test.bps;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.wso2.stratos.automation.test.bps.bpelActivities.bpelActCombineUrl;
import org.wso2.stratos.automation.test.bps.bpelActivities.bpelActIgnoreMissingFromData;
import org.wso2.stratos.automation.test.bps.manageScenarios.BpelInstanceManagementClient;
import org.wso2.stratos.automation.test.bps.manageScenarios.BpelProcessManagementClient;
import org.wso2.stratos.automation.test.bps.manageStructuredActivities.*;
import org.wso2.stratos.automation.test.bps.uploadScenarios.BpelDeployClient;
import org.wso2.stratos.automation.test.bps.uploadScenarios.BpelRedeployClient;
import org.wso2.stratos.automation.test.bps.uploadScenarios.BpelRetireDeploymentClient;


public class BPSStratosTestRunner extends TestSuite {

    public static Test suite() {
        TestSuite testSuite = new TestSuite();

        //bpel activities
        testSuite.addTestSuite(bpelActCombineUrl.class);
        testSuite.addTestSuite(bpelActIgnoreMissingFromData.class);
        testSuite.addTestSuite(bpelStructAct_FlowClient.class);
        testSuite.addTestSuite(bpelStructAct_forEachClient.class);
        testSuite.addTestSuite(bpelStructAct_IfClient.class);
        testSuite.addTestSuite(bpelStructAct_RepeatUntillClient.class);
        testSuite.addTestSuite(bpelStructAct_WhileClient.class);


        testSuite.addTestSuite(BpelInstanceManagementClient.class);
        testSuite.addTestSuite(BpelProcessManagementClient.class);
        testSuite.addTestSuite(BpelDeployClient.class);

        testSuite.addTestSuite(BpelRedeployClient.class);
        testSuite.addTestSuite(BpelRetireDeploymentClient.class);

        //BPEL manage structured activities


        return testSuite;
    }
}

