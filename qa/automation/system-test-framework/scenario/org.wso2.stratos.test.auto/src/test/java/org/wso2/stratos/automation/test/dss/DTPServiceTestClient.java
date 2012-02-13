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
import junit.framework.AssertionFailedError;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.RSSAdminConsoleService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.adminconsole.ui.stub.types.DatabaseUserEntry;
import org.wso2.carbon.adminconsole.ui.stub.types.RSSInstanceEntry;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.stratos.automation.test.dss.utils.FileManager;
import org.wso2.stratos.automation.test.dss.utils.MySqlDatabaseManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateRSS;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class DTPServiceTestClient extends TestTemplateRSS {
    private static final Log log = LogFactory.getLog(DTPServiceTestClient.class);
    private String jdbcUrlNew;
    private String databaseNameNew = "testDatabase2";
    private String databaseUserNew = "tstAto4";
    private String databasePasswordNew = "test123";
    private String userPrivilegeGroupNew = "automation1";

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "DTPServiceTest.dbs";
        serviceName = "DTPServiceTest";
        serviceGroup = "DTP";
    }

    @Override
    public void executeSql() {
        final String sqlFileLocation = RESOURCE_LOCATION + File.separator + "sql" + File.separator + "MySql" + File.separator;
        ArrayList<String> sqlFiles = new ArrayList<String>();
        sqlFiles.add(sqlFileLocation + "CreateTablesAccount.sql");

        try {
            MySqlDatabaseManager dbm = new MySqlDatabaseManager(jdbcUrl, databaseUser, databasePassword);

            for (int i = 0; i < sqlFiles.size(); i++) {
                File query = new File(sqlFiles.get(i));
                dbm.executeUpdate(query);

            }

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
        if (FrameworkSettings.STRATOS_TEST) {
            int dbInsId;
            int userId;
            int privGrpId;
            int rssLocalInstanceId;


            RSSAdminConsoleService rSSAdminConsoleService;
            rSSAdminConsoleService = new RSSAdminConsoleService(DSS_BACKEND_URL);

            try {
                dbInsId = rSSAdminConsoleService.getDatabaseInstance(sessionCookie, databaseNameNew + "_" + TENANT_DOMAIN.replace(".", "_")).getDbInstanceId();
                try {
                    userId = rSSAdminConsoleService.getDatabaseUser(sessionCookie, rSSAdminConsoleService.getFullyQualifiedUsername(databaseUserNew, TENANT_DOMAIN), dbInsId).getUserId();
                    rSSAdminConsoleService.deleteUser(sessionCookie, userId, dbInsId);
                } catch (AssertionFailedError e) {
                    log.info("User Not Found");
                }
                rSSAdminConsoleService.dropDatabase(sessionCookie, dbInsId);
            } catch (AssertionFailedError e) {
                log.info("Database Not Found");
            }
            try {
                privGrpId = rSSAdminConsoleService.getPrivilegeGroupId(sessionCookie, userPrivilegeGroupNew);
                rSSAdminConsoleService.deletePrivilegeGroup(sessionCookie, privGrpId);
            } catch (AssertionFailedError e) {
                log.info("privilege Group Not Found");
            }


            RSSInstanceEntry rssInstance;

            rssInstance = rSSAdminConsoleService.getRoundRobinAssignedRSSInstance(sessionCookie);
            rssLocalInstanceId = rssInstance.getRssInstanceId();
            log.info(rssLocalInstanceId);

            //creating database
            rSSAdminConsoleService.createDatabase(sessionCookie, databaseNameNew, rssLocalInstanceId);

            //set database full name
            databaseNameNew = databaseNameNew + "_" + TENANT_DOMAIN.replace(".", "_");
            log.info(databaseName);

            jdbcUrlNew = rssInstance.getServerUrl() + "/" + databaseNameNew;
            log.info(jdbcUrlNew);

            dbInsId = rSSAdminConsoleService.getDatabaseInstance(sessionCookie, databaseNameNew).getDbInstanceId();
            log.info(dbInsId);


            rSSAdminConsoleService.createPrivilegeGroup(sessionCookie, userPrivilegeGroupNew);
            privGrpId = rSSAdminConsoleService.getPrivilegeGroupId(sessionCookie, userPrivilegeGroupNew);
            Assert.assertNotSame("Privilege Group Not Found", -1, privGrpId);

            // DatabaseUserEntry dbUser;
            rSSAdminConsoleService.createUser(sessionCookie, databaseUserNew, databasePasswordNew, rssLocalInstanceId, dbInsId, privGrpId);
            databaseUserNew = rSSAdminConsoleService.getFullyQualifiedUsername(databaseUserNew, TENANT_DOMAIN);
            log.info(databaseUserNew);
        } else {
            jdbcUrlNew = FrameworkSettings.JDBC_URL;
            databaseUserNew = FrameworkSettings.DB_USER;
            databasePasswordNew = FrameworkSettings.DB_PASSWORD;

            try {
                MySqlDatabaseManager dbm = new MySqlDatabaseManager(jdbcUrlNew, databaseUserNew, databasePasswordNew);
                dbm.executeUpdate("DROP DATABASE IF EXISTS " + databaseNameNew);
                dbm.executeUpdate("CREATE DATABASE " + databaseNameNew);
                jdbcUrlNew = jdbcUrlNew + "/" + databaseNameNew;

                dbm.disconnect();
            } catch (ClassNotFoundException e) {
                log.error("Class Not Found. Check MySql-jdbc Driver in classpath: " + e.getMessage());
                Assert.fail("Class Not Found. Check MySql-jdbc Driver in classpath: " + e.getMessage());
            } catch (SQLException e) {
                log.error("SQLException When executing SQL: " + e.getMessage());
                Assert.fail("SQLException When executing SQL: " + e.getMessage());
            }


        }

        final String createTableSqlFile = sqlFileLocation + "CreateTablesAccount.sql";

        try {
            MySqlDatabaseManager dbm = new MySqlDatabaseManager(jdbcUrlNew, databaseUserNew, databasePasswordNew);

            File craeteTable = new File(createTableSqlFile);
            dbm.executeUpdate(craeteTable);

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
        Assert.assertNotNull("Initialize jdbcUrl", jdbcUrl);
        Assert.assertNotNull("Initialize jdbcUrl", jdbcUrlNew);

        try {
            OMElement dbsFile = AXIOMUtil.stringToOM(FileManager.readFile(serviceFileLocation + File.separator + serviceFileName).trim());
            OMElement dbsConfig;

            Iterator config = dbsFile.getChildrenWithName(new QName("config"));
            while (config.hasNext()) {
                String jdbc;
                String user;
                String passwd;
                dbsConfig = (OMElement) config.next();
                if (dbsConfig.getAttributeValue(new QName("id")).equals("MySqlDataSource1")) {
                    jdbc = jdbcUrl;
                    user = databaseUser;
                    passwd = databasePassword;
                } else {
                    jdbc = jdbcUrlNew;
                    user = databaseUserNew;
                    passwd = databasePasswordNew;
                }
                Iterator configElement = dbsConfig.getChildElements();
                while (configElement.hasNext()) {
                    OMElement properties = (OMElement) configElement.next();
                    String datasource = properties.getAttributeValue(new QName("name"));
                    if (datasource.equals("org.wso2.ws.dataservice.xa_datasource_properties")) {

                        Iterator dbPropertyElement = properties.getChildElements();
                        while (dbPropertyElement.hasNext()) {
                            OMElement property = (OMElement) dbPropertyElement.next();
                            String value = property.getAttributeValue(new QName("name"));

                            if ("URL".equals(value)) {
                                property.setText(jdbc);
                            } else if ("user".equals(value)) {
                                property.setText(user);

                            } else if ("password".equals(value)) {
                                property.setText(passwd);
                            }
                        }
                    }
                }

            }
            log.debug(dbsFile);
            ByteArrayDataSource dbs = new ByteArrayDataSource(dbsFile.toString().getBytes());
            serviceFile = new DataHandler(dbs);

        } catch (XMLStreamException e) {
            log.error("XMLStreamException when Reading Service File" + e.getMessage());
            Assert.fail("XMLStreamException when Reading Service File" + e.getMessage());
        } catch (IOException e) {
            log.error("IOException when Reading Service File" + e.getMessage());
            Assert.fail("IOException  when Reading Service File" + e.getMessage());
        }
    }

    @Override
    public void runSuccessCase() {

        addBankAccount1test();
        addBankAccount2test();
        distributedTransactionSuccess();
        distributedTransactionFail();

    }

    private OMElement addAccountToBank1(double accountBalance) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/dtp_sample", "ns1");
        OMElement payload = fac.createOMElement("addAccountToBank1", omNs);

        OMElement balance = fac.createOMElement("balance", omNs);
        balance.setText(accountBalance + "");
        payload.addChild(balance);
        return payload;

    }

    private OMElement addAccountToBank2(double accountBalance) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/dtp_sample", "ns1");
        OMElement payload = fac.createOMElement("addAccountToBank2", omNs);

        OMElement balance = fac.createOMElement("balance", omNs);
        balance.setText(accountBalance + "");
        payload.addChild(balance);
        return payload;

    }

    private OMElement getAccountBalanceFromBank1(int accId) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/dtp_sample", "ns1");
        OMElement payload = fac.createOMElement("getAccountBalanceFromBank1", omNs);

        OMElement accountId = fac.createOMElement("accountId", omNs);
        accountId.setText(accId + "");
        payload.addChild(accountId);

        return payload;

    }

    private OMElement getAccountBalanceFromBank2(int accId) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/dtp_sample", "ns1");
        OMElement payload = fac.createOMElement("getAccountBalanceFromBank2", omNs);

        OMElement accountId = fac.createOMElement("accountId", omNs);
        accountId.setText(accId + "");
        payload.addChild(accountId);

        return payload;

    }

    private OMElement addToAccountBalanceInBank1(int accId, double value) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/dtp_sample", "ns1");
        OMElement payload = fac.createOMElement("addToAccountBalanceInBank1", omNs);

        OMElement accountId = fac.createOMElement("accountId", omNs);
        accountId.setText(accId + "");
        payload.addChild(accountId);

        OMElement valueEl = fac.createOMElement("value", omNs);
        valueEl.setText(value + "");
        payload.addChild(valueEl);

        return payload;

    }

    private OMElement addToAccountBalanceInBank2(int accId, double value) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/dtp_sample", "ns1");
        OMElement payload = fac.createOMElement("addToAccountBalanceInBank2", omNs);

        OMElement accountId = fac.createOMElement("accountId", omNs);
        accountId.setText(accId + "");
        payload.addChild(accountId);

        OMElement valueEl = fac.createOMElement("value", omNs);
        valueEl.setText(value + "");
        payload.addChild(valueEl);

        return payload;

    }

    private OMElement begin_boxcar() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/dtp_sample", "ns1");
        return fac.createOMElement("begin_boxcar", omNs);

    }

    private OMElement end_boxcar() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/dtp_sample", "ns1");
        return fac.createOMElement("end_boxcar", omNs);

    }

    private int getAccountIdFromResponse(OMElement response) {
        Assert.assertNotNull("Response Message is null", response);
        log.debug(response);
        try {
            return Integer.parseInt(((OMElement) ((OMElement) response.getChildrenWithLocalName("Entry").next())
                    .getChildrenWithLocalName("ID").next()).getText());
        } catch (Exception e) {
            fail("Id Not Found in response : " + e.getMessage());
        }
        return -1;
    }

    private double getBalanceFromResponse(OMElement response) {
        Assert.assertNotNull("Response Message is null", response);
        log.debug(response);
        try {
            return Double.parseDouble(((OMElement) response.getChildrenWithLocalName("Value").next()).getText());
        } catch (Exception e) {
            fail("Balance Not Found in response : " + e.getMessage());
        }
        return -1;
    }

    private void addBankAccount1test() {
        ServiceClient sender;
        Options options;
        OMElement response;

        try {
            sender = new ServiceClient();
            options = new Options();
            options.setTo(new EndpointReference(serviceEndPoint));
            options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            options.setManageSession(true);
            sender.setOptions(options);

            options.setAction("urn:" + "addAccountToBank1");
            sender.setOptions(options);
            response = sender.sendReceive(addAccountToBank1(100));
            log.debug(response);
            int id1 = getAccountIdFromResponse(response);

            options.setAction("urn:" + "getAccountBalanceFromBank1");
            Assert.assertEquals("invalid balance ", 100.0, getBalanceFromResponse(sender.sendReceive(getAccountBalanceFromBank1(id1))));

        } catch (AxisFault axisFault) {
            Assert.fail("AxisFault while getting response :" + axisFault.getMessage());
        }


    }

    private void addBankAccount2test() {
        ServiceClient sender;
        Options options;
        OMElement response;

        try {
            sender = new ServiceClient();
            options = new Options();
            options.setTo(new EndpointReference(serviceEndPoint));
            options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            options.setManageSession(true);
            sender.setOptions(options);


            options.setAction("urn:" + "addAccountToBank2");
            sender.setOptions(options);
            response = sender.sendReceive(addAccountToBank2(200));
            log.debug(response);

            int id1 = getAccountIdFromResponse(response);

            options.setAction("urn:" + "getAccountBalanceFromBank2");
            Assert.assertEquals("invalid balance ", 200.0, getBalanceFromResponse(sender.sendReceive(getAccountBalanceFromBank2(id1))));

        } catch (AxisFault axisFault) {
            Assert.fail("AxisFault while getting response :" + axisFault.getMessage());
        }


    }

    private void distributedTransactionSuccess() {
        ServiceClient sender;
        Options options;

        try {
            sender = new ServiceClient();
            options = new Options();
            options.setTo(new EndpointReference(serviceEndPoint));
            options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            options.setManageSession(true);
            sender.setOptions(options);

            options.setAction("urn:" + "addAccountToBank1");
            sender.setOptions(options);
            int id1 = getAccountIdFromResponse(sender.sendReceive(addAccountToBank1(100)));

            options.setAction("urn:" + "addAccountToBank2");
            sender.setOptions(options);
            int id2 = getAccountIdFromResponse(sender.sendReceive(addAccountToBank2(200)));

            options.setAction("urn:" + "begin_boxcar");
            sender.setOptions(options);
            sender.sendRobust(begin_boxcar());

            options.setAction("urn:" + "addToAccountBalanceInBank1");
            sender.setOptions(options);
            sender.sendRobust(addToAccountBalanceInBank1(id1, 50));

            options.setAction("urn:" + "addToAccountBalanceInBank2");
            sender.setOptions(options);
            sender.sendRobust(addToAccountBalanceInBank2(id2, -25));

            options.setAction("urn:" + "end_boxcar");
            sender.sendRobust(end_boxcar());

            options.setAction("urn:" + "getAccountBalanceFromBank1");
            sender.setOptions(options);
            Assert.assertEquals("Expected not same", 150.0, getBalanceFromResponse(sender.sendReceive(getAccountBalanceFromBank1(id1))));

            options.setAction("urn:" + "getAccountBalanceFromBank2");
            sender.setOptions(options);
            Assert.assertEquals("Expected not same", 175.0, getBalanceFromResponse(sender.sendReceive(getAccountBalanceFromBank2(id2))));


        } catch (AxisFault axisFault) {
            Assert.fail("AxisFault while getting response :" + axisFault.getMessage());
        }


    }

    private void distributedTransactionFail() {
        ServiceClient sender;
        Options options;

        try {
            sender = new ServiceClient();
            options = new Options();
            options.setTo(new EndpointReference(serviceEndPoint));
            options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            options.setManageSession(true);
            sender.setOptions(options);

            options.setAction("urn:" + "addAccountToBank1");
            sender.setOptions(options);
            int id1 = getAccountIdFromResponse(sender.sendReceive(addAccountToBank1(11500)));

            options.setAction("urn:" + "addAccountToBank2");
            sender.setOptions(options);
            int id2 = getAccountIdFromResponse(sender.sendReceive(addAccountToBank2(2000)));
            int id3 = getAccountIdFromResponse(sender.sendReceive(addAccountToBank2(1500)));
            int id4 = getAccountIdFromResponse(sender.sendReceive(addAccountToBank2(3000)));

            options.setAction("urn:" + "begin_boxcar");
            sender.setOptions(options);
            sender.sendRobust(begin_boxcar());

            options.setAction("urn:" + "addToAccountBalanceInBank2");
            sender.setOptions(options);
            sender.sendRobust(addToAccountBalanceInBank2(id2, 1500));
            sender.sendRobust(addToAccountBalanceInBank2(id3, 350));
            sender.sendRobust(addToAccountBalanceInBank2(id4, 700));

            /* this should fail, validation error, value < -2000 */
            options.setAction("urn:" + "addToAccountBalanceInBank1");
            sender.setOptions(options);
            sender.sendRobust(addToAccountBalanceInBank1(id1, -2700));
            try {
                options.setAction("urn:" + "end_boxcar");
                sender.sendRobust(end_boxcar());
                fail("Service validation failed. end_boxcar Not Working");
            } catch (AxisFault e) {
                log.info("Operation failed");

            }

            options.setAction("urn:" + "getAccountBalanceFromBank1");
            sender.setOptions(options);
            Assert.assertEquals("Expected not same", 11500.0, getBalanceFromResponse(sender.sendReceive(getAccountBalanceFromBank1(id1))));

            options.setAction("urn:" + "getAccountBalanceFromBank2");
            sender.setOptions(options);
            Assert.assertEquals("Expected not same", 2000.0, getBalanceFromResponse(sender.sendReceive(getAccountBalanceFromBank2(id2))));
            Assert.assertEquals("Expected not same", 1500.0, getBalanceFromResponse(sender.sendReceive(getAccountBalanceFromBank2(id3))));
            Assert.assertEquals("Expected not same", 3000.0, getBalanceFromResponse(sender.sendReceive(getAccountBalanceFromBank2(id4))));


        } catch (AxisFault axisFault) {
            Assert.fail("AxisFault while getting response :" + axisFault.getMessage());
        }

    }
}
