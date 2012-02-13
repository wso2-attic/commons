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

package org.wso2.carbon.automation.ravana.test.esb;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.automation.ravana.test.ravanaUtils.RavanaTestTemplate;

import java.io.File;                     
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RavanaESBTest extends RavanaTestTemplate {
    private static final Log log = LogFactory.getLog(RavanaESBTest.class);
    private static ResultSet resultSet;


    @Override
    public void executeSql() {
        testClassName = RavanaESBTest.class.getName();
        String queryString = "select TPS, TPS_MIN from TEST, SCENARIO,  SVN, SVN_URL where PRODUCT='wso2esb-3.0.1' " +
                "and SCENARIO.VERSION='3.0.1' and SVN.VERSION=(select max(VERSION) from SVN) and CONCURRENCY=20 " +
                "and MESSAGE_SIZE=297 and TEST.SCENARIO_ID=SCENARIO.ID and TEST.SVN_ID=SVN.ID;";
        try {
            resultSet = mysqlDBMgt.executeQuery(queryString);
        } catch (SQLException e) {
            log.error("SQL error " + e.getMessage());
            Assert.fail("SQL error " + e.getMessage());
        }
    }

    @Override
    public void runSuccessCase() {
        log.info("Running RavanaESBTest..");
        try {
            if (resultSet.next()) {
                int tps = resultSet.getInt("TPS");
                int tpsThreshold = resultSet.getInt("TPS_MIN");
                log.info("Actual TPS:" + tps);
                log.info("Expected TPS minimum value :" + tpsThreshold);
                Assert.assertTrue("Actual - " + tps + " TPS is less than expected - " + tpsThreshold + " TPS minimum value", tps > tpsThreshold);
            }else{
                Assert.assertNotNull("Result set is empty", resultSet);
            }
        } catch (SQLException e) {
            log.error("SQL error " + e.getMessage());
            Assert.fail("SQL error " + e.getMessage());
        }
    }

    @Override
    public void createProcessBuilder() {
        String scenarioName = "esb_direct_string";
        String ravanaFrameworkPath = FrameworkSettings.RAVANA_FRAMEWORK_PATH;
        log.info("Running " + scenarioName + "scenario ..");
        ProcessBuilder pb = new ProcessBuilder("./test.sh", "wso2/" + scenarioName);
        log.info("Scenario execution finish ..");
        pb.directory(new File(ravanaFrameworkPath));
        Process proc;
        try {
            proc = pb.start();
            int exitVal = proc.waitFor();
            log.info("Proc wait for status " + exitVal);
        } catch (IOException e) {
            log.error("File IO error " + e.getMessage());
            Assert.fail("File IO error " + e.getMessage());
        } catch (InterruptedException e) {
            log.error("Process interruption exception " + e.getMessage());
            Assert.fail("Process interruption exception " + e.getMessage());
        }
        log.info("Ravana Test run finished");
    }
}
