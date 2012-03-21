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
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.commons.datasource.DataSourceInformation;
import org.wso2.carbon.admin.service.*;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.securevault.secret.SecretInformation;
import org.wso2.stratos.automation.test.dss.utils.FileManager;
import org.wso2.stratos.automation.test.dss.utils.MySqlDatabaseManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateRSS;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;

public class CarbonDataSourceTestClient extends TestTemplateRSS {

    private static final Log log = LogFactory.getLog(CarbonDataSourceTestClient.class);

    private String carbonDataSourceName = null;

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "MySqlRSSDataServiceTest.dbs";
        serviceName = "CarbonDSDataServiceTest";
        serviceGroup = "CarbonDS";
    }


    @Override
    public void createArtifact() {
        createDataSource();
        Assert.assertNotNull("Carbon data source name null. create carbon data source", carbonDataSourceName);
        try {

            OMElement dbsFile = AXIOMUtil.stringToOM(FileManager.readFile(serviceFileLocation + File.separator + serviceFileName).trim());
            dbsFile.getAttribute(new QName("name")).setAttributeValue(serviceName);
            dbsFile.getAttribute(new QName("serviceGroup")).setAttributeValue(serviceGroup);
            OMElement dbsConfig = dbsFile.getFirstChildWithName(new QName("config"));

            Iterator configElement1 = dbsConfig.getChildElements();
            while (configElement1.hasNext()) {
                OMElement property = (OMElement) configElement1.next();
                property.discard();
            }
            OMElement om = OMAbstractFactory.getOMFactory().createOMElement("property", null);
            om.addAttribute(OMAbstractFactory.getOMFactory().createOMAttribute("name", null, "carbon_datasource_name"));
            om.setText(carbonDataSourceName);
            dbsConfig.addChild(om);

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
        for (int i = 0; i < 5; i++) {
            getCustomerInBoston();
        }

        for (int i = 0; i < 5; i++) {
            addEmployee(String.valueOf(i));
        }
        for (int i = 0; i < 5; i++) {
            getEmployeeById(String.valueOf(i));
        }
        for (int i = 0; i < 5; i++) {
            IncreaseEmployeeSalary(String.valueOf(i));
        }
        for (int i = 0; i < 5; i++) {
            deleteEmployeeById(String.valueOf(i));
        }
    }


    @Override
    public void executeSql() {
        final String sqlFileLocation = RESOURCE_LOCATION + File.separator + "sql" + File.separator + "MySql" + File.separator;
        final String insertCustomerSql = sqlFileLocation + "Customers.sql";

        try {
            MySqlDatabaseManager dbm = new MySqlDatabaseManager(jdbcUrl, databaseUser, databasePassword);

            File insertCustomer = new File(insertCustomerSql);
            dbm.executeUpdate(insertCustomer);

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

    private void getCustomerInBoston() {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/rdbms_sample", "ns1");
        OMElement payload = fac.createOMElement("customersInBoston", omNs);
        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "customersInBoston");
        Assert.assertTrue("Expected Result Mismatched", (result.toString().indexOf("<city>Boston</city>") > 1));

    }

    private void addEmployee(String employeeNumber) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/rdbms_sample", "ns1");
        OMElement payload = fac.createOMElement("addEmployee", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText(employeeNumber);
        payload.addChild(empNo);

        OMElement lastName = fac.createOMElement("lastName", omNs);
        lastName.setText("BBB");
        payload.addChild(lastName);

        OMElement fName = fac.createOMElement("firstName", omNs);
        fName.setText("AAA");
        payload.addChild(fName);

        OMElement email = fac.createOMElement("email", omNs);
        email.setText("aaa@ccc.com");
        payload.addChild(email);

        OMElement salary = fac.createOMElement("salary", omNs);
        salary.setText("50000");
        payload.addChild(salary);

        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "addEmployee");

    }

    private OMElement getEmployeeById(String employeeNumber) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/rdbms_sample", "ns1");
        OMElement payload = fac.createOMElement("employeesByNumber", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText(employeeNumber);
        payload.addChild(empNo);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "employeesByNumber");
        Assert.assertTrue("Expected Result Mismatched", (result.toString().indexOf("<first-name>AAA</first-name>") > 1));
        return result;
    }

    private void deleteEmployeeById(String employeeNumber) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/rdbms_sample", "ns1");
        OMElement payload = fac.createOMElement("deleteEmployeeById", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText(employeeNumber);
        payload.addChild(empNo);

        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "deleteEmployeeById");


    }

    private void IncreaseEmployeeSalary(String employeeNumber) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/rdbms_sample", "ns1");
        OMElement payload = fac.createOMElement("incrementEmployeeSalary", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText(employeeNumber);
        payload.addChild(empNo);

        OMElement salary = fac.createOMElement("increment", omNs);
        salary.setText("10000");
        payload.addChild(salary);

        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "incrementEmployeeSalary");

        OMElement result = getEmployeeById(employeeNumber);
        Assert.assertTrue("Expected Result Mismatched", (result.toString().indexOf("<salary>60000.0</salary>") > 0));

    }
    private void createDataSource() {

        DataServiceAdminService dataServiceAdminService = new DataServiceAdminService(DSS_BACKEND_URL);
        DataSourceAdminService dataSourceAdminService = new DataSourceAdminService(DSS_BACKEND_URL);
        String[] list = dataServiceAdminService.getCarbonDataSources(sessionCookie);
        String createDataSourceResponse;
        DataSourceInformation dataSourceInfo;

        if(FrameworkSettings.STRATOS_TEST) {
            if (list != null) {
                for (String ds : list) {
                    if (ds.startsWith(databaseName + "_")) {
                        dataSourceAdminService.removeCarbonDataSources(sessionCookie, ds);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            log.error("InterruptedException : " + e.getMessage());
                            Assert.fail("InterruptedException : " + e.getMessage());
                        }
                    }
                }
            }
            RSSAdminConsoleService rSSAdminConsoleService = new RSSAdminConsoleService(DSS_BACKEND_URL);
            createDataSourceResponse = rSSAdminConsoleService.createCarbonDSFromDatabaseUserEntry(sessionCookie, dbInstanceId, databaseUserId);
            Assert.assertTrue("Database name not found in create data source response message", (createDataSourceResponse.indexOf(databaseName + "_") == 0));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("InterruptedException : " + e.getMessage());
                Assert.fail("InterruptedException : " + e.getMessage());
            }

            dataSourceInfo = dataSourceAdminService.getCarbonDataSources(sessionCookie, createDataSourceResponse);
            dataSourceInfo.getSecretInformation().setAliasSecret(databasePassword);
            dataSourceAdminService.editCarbonDataSources(sessionCookie, createDataSourceResponse, dataSourceInfo);

        } else {
            String dataSourceName = databaseName + "DataSource";
            if (list != null) {
                for (String ds : list) {
                    if (dataSourceName.equalsIgnoreCase(ds)) {
                        dataSourceAdminService.removeCarbonDataSources(sessionCookie, ds);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            log.error("InterruptedException : " + e.getMessage());
                            Assert.fail("InterruptedException : " + e.getMessage());
                        }
                    }
                }
            }

            dataSourceInfo = getDataSourceInformation(dataSourceName);

            dataSourceAdminService.addDataSourceInformation(sessionCookie, dataSourceName, dataSourceInfo);
            createDataSourceResponse = dataSourceName;
        }

        list = dataServiceAdminService.getCarbonDataSources(sessionCookie);
        Assert.assertNotNull("Data Source list null", list);
        for (String ds : list) {
            if (ds.equals(createDataSourceResponse)) {
                carbonDataSourceName = ds;
                break;
            }
        }

        Assert.assertNotNull("DataSource Not found in DataSource List", carbonDataSourceName);

    }

    private DataSourceInformation getDataSourceInformation(String dataSourceName) {
        DataSourceInformation dataSourceInfo = new DataSourceInformation();
        SecretInformation secretInformation = new SecretInformation();
        dataSourceInfo.setSecretInformation(secretInformation);
        
        dataSourceInfo.setAlias(dataSourceName);
        dataSourceInfo.setDriver("com.mysql.jdbc.Driver");
        dataSourceInfo.setUrl(jdbcUrl);
        dataSourceInfo.getSecretInformation().setUser(databaseUser);
        dataSourceInfo.getSecretInformation().setAliasSecret(databasePassword);
        dataSourceInfo.setType("BasicDataSource");
        dataSourceInfo.setDatasourceName(dataSourceName);
        dataSourceInfo.setDefaultAutoCommit(true);
        dataSourceInfo.setMaxActive(8);
        dataSourceInfo.setMaxIdle(8);
        dataSourceInfo.setMaxOpenPreparedStatements(-1);
        dataSourceInfo.setMaxWait(-1);
        dataSourceInfo.setMinIdle(0);
        dataSourceInfo.setInitialSize(0);
        dataSourceInfo.setPoolPreparedStatements(false);
        dataSourceInfo.setTestOnBorrow(true);
        dataSourceInfo.setTestWhileIdle(false);
        

//        dataSourceInfo.setNumTestsPerEvictionRun(3);
//        dataSourceInfo.setTimeBetweenEvictionRunsMillis(-1);
//        dataSourceInfo.setRemoveAbandonedTimeout(0);
//        dataSourceInfo.setDefaultTransactionIsolation(-1);
//        dataSourceInfo.setAccessToUnderlyingConnectionAllowed(false);
//        dataSourceInfo.setTestOnReturn(false);
//        dataSourceInfo.setRemoveAbandoned(false);
//        dataSourceInfo.setDefaultReadOnly(false);
//        dataSourceInfo.setLogAbandoned(true);
//        dataSourceInfo.addParameter("registry", "memory");
//        dataSourceInfo.setMinEvictableIdleTimeMillis(1800000);

        return dataSourceInfo;
    }

}
