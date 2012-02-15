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
package org.wso2.stratos.automation.test.dss.utils;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceDataServiceFileUploader;
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.RSSAdminConsoleService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.adminconsole.ui.stub.types.DatabaseUserEntry;
import org.wso2.carbon.adminconsole.ui.stub.types.RSSInstanceEntry;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import javax.activation.DataHandler;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public abstract class TestTemplateFaultyService extends TestTemplate {

    static {
        FrameworkSettings.getFrameworkProperties();

    }

    private static final Log log = LogFactory.getLog(TestTemplateFaultyService.class);

    protected static final String DSS_IP = FrameworkSettings.DSS_SERVER_HOST_NAME;
    protected static final String DSS_BACKEND_URL = FrameworkSettings.DSS_BACKEND_URL;

    protected static final String RESOURCE_LOCATION = ProductConstant.getResourceLocations(ProductConstant.DSS_SERVER_NAME);

    protected static TenantDetails TENANT_DETAILS = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("3"));
    protected static final String USER_NAME = TENANT_DETAILS.getTenantName();
    protected static final String PASSWORD = TENANT_DETAILS.getTenantPassword();
    protected static final String TENANT_DOMAIN = TENANT_DETAILS.getTenantDomain();

    protected String sessionCookie = null;
    protected int rssInstanceId = -1;
    protected int dbInstanceId = -1;
    protected int userPrivilegeGroupId = -1;
    protected String jdbcUrl = null;
    protected int databaseUserId = -1;
    protected String databaseName = "testDatabase1";
    protected String databaseUser = "tstAto3";
    protected String databasePassword = "test123";
    protected String userPrivilegeGroup = "automation";

    protected String serviceFileLocation = null;
    protected String serviceFileName = null;
    protected String serviceName = null;
    protected String serviceGroup = null;
    protected DataHandler serviceFile = null;


    public abstract void setServiceMetaData();

    public abstract void executeSql();

    public abstract void createArtifact();

    @Override
    public void init() {
        authenticate();
        if (FrameworkSettings.STRATOS_TEST) {
            setPriConditions();
            createDataBase();
            createPrivilegeGroup();
            createUser();
        } else {
            jdbcUrl = FrameworkSettings.JDBC_URL;
            databaseUser = FrameworkSettings.DB_USER;
            databasePassword = FrameworkSettings.DB_PASSWORD;
            createDataBase(jdbcUrl, databaseUser, databasePassword);


        }
        createTables();
        executeSql();
    }


    @Override
    public void artifactDeployment() {
        setServiceMetaData();
        checkServiceMetaData();
        deleteServiceIfExist();
        createArtifact();
        Assert.assertNotNull("Please create service file ", serviceFile);
        AdminServiceDataServiceFileUploader adminServiceDataServiceFileUploader = new AdminServiceDataServiceFileUploader(DSS_BACKEND_URL);
        AdminServiceClientDSS adminServiceClientDSS = new AdminServiceClientDSS(DSS_BACKEND_URL);


        adminServiceDataServiceFileUploader.uploadDataServiceFile(sessionCookie, serviceFileName, serviceFile);
        log.info(serviceFileName + " Service File Uploaded");
        log.info("waiting " + FrameworkSettings.SERVICE_DEPLOYMENT_DELAY + " millis for service deployment");

        adminServiceClientDSS.isServiceFaulty(sessionCookie, serviceName, FrameworkSettings.SERVICE_DEPLOYMENT_DELAY);

    }

    @Override
    public void cleanup() {
        if (FrameworkSettings.STRATOS_TEST) {
            RSSAdminConsoleService rSSAdminConsoleService = new RSSAdminConsoleService(DSS_BACKEND_URL);

            rSSAdminConsoleService.deleteUser(sessionCookie, databaseUserId, dbInstanceId);
            rSSAdminConsoleService.deletePrivilegeGroup(sessionCookie, userPrivilegeGroupId);
            rSSAdminConsoleService.dropDatabase(sessionCookie, dbInstanceId);
            log.info("Database Dropped");
        }

    }

    private void deleteServiceIfExist() {
        AdminServiceService adminServiceService;
        adminServiceService = new AdminServiceService(DSS_BACKEND_URL);

        if (adminServiceService.isServiceExists(sessionCookie, serviceName)) {
            log.info("Service already in server");
            adminServiceService.deleteService(sessionCookie, new String[]{serviceGroup});
            log.info("Service Deleted");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                Assert.fail("InterruptedException :" + e.getMessage());
            }
        }else if (adminServiceService.isServiceFaulty(sessionCookie, serviceName)) {
            log.info("Service already in faulty service list");
            adminServiceService.deleteFaultyService(sessionCookie, File.separator + "dataservices" + File.separator + serviceFileName);
            log.info("Service Deleted from faulty service list");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                Assert.fail("InterruptedException :" + e.getMessage());
            }
        }
        Assert.assertFalse("Service Still in service list. service deletion failed", adminServiceService.isServiceExists(sessionCookie, serviceName));
        Assert.assertFalse("Service Still in faulty service list. service deletion failed", adminServiceService.isServiceFaulty(sessionCookie, serviceName));
    }

    private void createDataBase() {
        RSSInstanceEntry rssInstance;
        RSSAdminConsoleService rSSAdminConsoleService = new RSSAdminConsoleService(DSS_BACKEND_URL);

        rssInstance = rSSAdminConsoleService.getRoundRobinAssignedRSSInstance(sessionCookie);
        rssInstanceId = rssInstance.getRssInstanceId();
        log.info("RSS Instance Id :" + rssInstanceId);

        //creating database
        rSSAdminConsoleService.createDatabase(sessionCookie, databaseName, rssInstanceId);
        log.info("Database created");
        //set database full name
        databaseName = databaseName + "_" + TENANT_DOMAIN.replace(".", "_");
        log.info("Database name :" + databaseName);

        jdbcUrl = rssInstance.getServerUrl() + "/" + databaseName;
        log.info("JDBC URL :" + jdbcUrl);

        dbInstanceId = rSSAdminConsoleService.getDatabaseInstance(sessionCookie, databaseName).getDbInstanceId();
        log.debug("Database instance id :" + dbInstanceId);

    }

    public void createDataBase(String jdbc, String user, String password) {
        try {
            MySqlDatabaseManager dbm = new MySqlDatabaseManager(jdbc, user, password);
            dbm.executeUpdate("DROP DATABASE IF EXISTS " + databaseName);
            dbm.executeUpdate("CREATE DATABASE " + databaseName);
            jdbcUrl = jdbc + "/" + databaseName;

            dbm.disconnect();
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found. Check MySql-jdbc Driver in classpath: " + e.getMessage());
            Assert.fail("Class Not Found. Check MySql-jdbc Driver in classpath: " + e.getMessage());
        } catch (SQLException e) {
            log.error("SQLException When executing SQL: " + e.getMessage());
            Assert.fail("SQLException When executing SQL: " + e.getMessage());
        }

    }

    private void createPrivilegeGroup() {
        RSSAdminConsoleService rSSAdminConsoleService = new RSSAdminConsoleService(DSS_BACKEND_URL);
        rSSAdminConsoleService.createPrivilegeGroup(sessionCookie, userPrivilegeGroup);
        userPrivilegeGroupId = rSSAdminConsoleService.getPrivilegeGroupId(sessionCookie, userPrivilegeGroup);
        log.info("privilege Group Created");
        log.debug("Privilege Group Id :" + userPrivilegeGroupId);
        Assert.assertNotSame("Privilege Group Not Found", -1, userPrivilegeGroupId);
    }

    private void createUser() {
        RSSAdminConsoleService rSSAdminConsoleService = new RSSAdminConsoleService(DSS_BACKEND_URL);
        DatabaseUserEntry dbUser;
        rSSAdminConsoleService.createUser(sessionCookie, databaseUser, databasePassword, rssInstanceId, dbInstanceId, userPrivilegeGroupId);
        log.info("Database User Created");
        databaseUser = rSSAdminConsoleService.getFullyQualifiedUsername(databaseUser, TENANT_DOMAIN);
        log.info("Database User Name :" + databaseUser);

        dbUser = rSSAdminConsoleService.getDatabaseUser(sessionCookie, databaseUser, dbInstanceId);
        databaseUserId = dbUser.getUserId();
        log.debug("Database UserId :" + databaseUserId);

    }

    private void authenticate() {
        AdminServiceAuthentication adminServiceAuthentication = new AdminServiceAuthentication(DSS_BACKEND_URL);
        sessionCookie = adminServiceAuthentication.login(USER_NAME, PASSWORD, "localhost");
    }

    private void createTables() {
        final String sqlFileLocation = RESOURCE_LOCATION + File.separator + "sql" + File.separator + "MySql" + File.separator;
        final String createTableSqlFile = sqlFileLocation + "CreateTables.sql";
        log.debug("Sql File :" + createTableSqlFile);
        try {
            MySqlDatabaseManager dbm = new MySqlDatabaseManager(jdbcUrl, databaseUser, databasePassword);

            File createTable = new File(createTableSqlFile);
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

    private void setPriConditions() {
        int dbInsId;
        int userId;
        int privGrpId;

        RSSAdminConsoleService rSSAdminConsoleService;
        rSSAdminConsoleService = new RSSAdminConsoleService(DSS_BACKEND_URL);
        log.info("Setting pre conditions");
        try {
            dbInsId = rSSAdminConsoleService.getDatabaseInstance(sessionCookie, databaseName + "_" + TENANT_DOMAIN.replace(".", "_")).getDbInstanceId();
            log.info("Database name already in server");
            try {
                userId = rSSAdminConsoleService.getDatabaseUser(sessionCookie, rSSAdminConsoleService.getFullyQualifiedUsername(databaseUser, TENANT_DOMAIN), dbInsId).getUserId();
                log.info("User already in Database. deleting user");
                rSSAdminConsoleService.deleteUser(sessionCookie, userId, dbInsId);
                log.info("User Deleted");
            } catch (AssertionFailedError e) {
            }
            log.info("Dropping database");
            rSSAdminConsoleService.dropDatabase(sessionCookie, dbInsId);
            log.info("database Dropped");
        } catch (AssertionFailedError e) {
        }
        try {
            privGrpId = rSSAdminConsoleService.getPrivilegeGroupId(sessionCookie, userPrivilegeGroup);
            log.info("Privilege Group name already in server");
            rSSAdminConsoleService.deletePrivilegeGroup(sessionCookie, privGrpId);
            log.info("Privilege Group Deleted");
        } catch (AssertionFailedError e) {
        }
        log.info("pre conditions created");

    }

    private void checkServiceMetaData() {
        Assert.assertNotNull("Please set serviceName", serviceName);
        Assert.assertNotNull("Please set serviceGroup", serviceGroup);
        Assert.assertNotNull("Please set serviceFileName", serviceFileName);
        Assert.assertNotNull("Please set serviceFileLocation", serviceFileLocation);

    }

}