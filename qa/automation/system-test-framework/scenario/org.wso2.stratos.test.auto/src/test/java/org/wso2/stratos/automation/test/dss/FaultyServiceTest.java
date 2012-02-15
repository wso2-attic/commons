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
package org.wso2.stratos.automation.test.dss;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.service.mgt.stub.types.carbon.FaultyService;
import org.wso2.stratos.automation.test.dss.utils.AdminServiceClientDSS;
import org.wso2.stratos.automation.test.dss.utils.MySqlDatabaseManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateFaultyService;

import javax.activation.DataHandler;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

public class FaultyServiceTest extends TestTemplateFaultyService {

    private static final Log log = LogFactory.getLog(FaultyServiceTest.class);

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "FaultyDataService.dbs";
        serviceName = "FaultyDataService";
        serviceGroup = "FaultyService";
    }

    @Override
    public void executeSql() {
        final String sqlFileLocation = RESOURCE_LOCATION + File.separator + "sql" + File.separator + "MySql" + File.separator;
        final String insertOfficeSqlFile = sqlFileLocation + "Offices.sql";
        log.debug("Sql File :" + insertOfficeSqlFile);
        try {
            MySqlDatabaseManager dbm = new MySqlDatabaseManager(jdbcUrl, databaseUser, databasePassword);

            File createTable = new File(insertOfficeSqlFile);
            dbm.executeUpdate(createTable);

            dbm.disconnect();
        } catch (IOException e) {
            log.error("IOException When reading SQL files: " + e.getMessage());
            Assert.fail("IOException When reading SQL files: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found. Check MySql-jdbc Driver in classpath: " + e.getMessage());
            Assert.fail("Class Not Found. Check MySql-jdbc Driver in classpath: " + e.getMessage());
        } catch (SQLException e) {
            log.error("SQLException When executing SQL: " + e.getMessage());
            Assert.fail("SQLException When executing SQL: " + e.getMessage());
        }
    }

    @Override
    public void createArtifact() {
        try {
            serviceFile = new DataHandler(new URL("file://" + serviceFileLocation + File.separator + serviceFileName));
        } catch (MalformedURLException e) {
            log.error("Resource file Not Found " + e.getMessage());
            Assert.fail("Resource file Not Found " + e.getMessage());
        }
    }


    @Override
    public void runSuccessCase() {
        AdminServiceClientDSS adminServiceClientDSS = new AdminServiceClientDSS(DSS_BACKEND_URL);
        FaultyService faultyService;
        sessionCookie = adminServiceClientDSS.authenticate(USER_NAME, PASSWORD);
        Assert.assertTrue("Service not in faulty service list", adminServiceClientDSS.isServiceFaulty(sessionCookie, serviceName));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("InterruptedException " + e.getMessage());
            Assert.fail("InterruptedException " + e.getMessage());
        }
        faultyService = adminServiceClientDSS.getFaultyServiceData(sessionCookie, serviceName);
        adminServiceClientDSS.deleteFaultyService(sessionCookie, faultyService.getArtifact());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("InterruptedException " + e.getMessage());
            Assert.fail("InterruptedException " + e.getMessage());
        }
        Assert.assertFalse("Service Still in service list", adminServiceClientDSS.isServiceFaulty(sessionCookie, serviceName));
        adminServiceClientDSS.logOut();
    }


}
