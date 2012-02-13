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

public class NestedQueryTestClient extends TestTemplateRSS {
    private static final Log log = LogFactory.getLog(NestedQueryTestClient.class);

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "NestedQueryTest.dbs";
        serviceName = "NestedQueryTest";
        serviceGroup = "NestedQuery";
    }

    @Override
    public void executeSql() {
        final String sqlFileLocation = RESOURCE_LOCATION + File.separator + "sql" + File.separator + "MySql" + File.separator;
        ArrayList<String> sqlFiles = new ArrayList<String>();
        sqlFiles.add(sqlFileLocation + "Customers.sql");
        sqlFiles.add(sqlFileLocation + "Employees.sql");
        sqlFiles.add(sqlFileLocation + "Offices.sql");
        sqlFiles.add(sqlFileLocation + "Orders.sql");

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

    }

    @Override
    public void createArtifact() {
        Assert.assertNotNull("Initialize jdbcUrl", jdbcUrl);
        try {
            OMElement dbsFile = AXIOMUtil.stringToOM(FileManager.readFile(serviceFileLocation + File.separator + serviceFileName).trim());
            OMElement dbsConfig = dbsFile.getFirstChildWithName(new QName("config"));
            Iterator configElement1 = dbsConfig.getChildElements();
            while (configElement1.hasNext()) {
                OMElement property = (OMElement) configElement1.next();
                String value = property.getAttributeValue(new QName("name"));
                if ("org.wso2.ws.dataservice.protocol".equals(value)) {
                    property.setText(jdbcUrl);

                } else if ("org.wso2.ws.dataservice.user".equals(value)) {
                    property.setText(databaseUser);

                } else if ("org.wso2.ws.dataservice.password".equals(value)) {
                    property.setText(databasePassword);
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
        getOffices();
        getEmployeesInOffice();
        getCustomerOrders();
        getCustomerName();

    }

    private void getCustomerName() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples", "ns1");
        OMElement payload = fac.createOMElement("customerName", omNs);

        OMElement customerNumber = fac.createOMElement("customerNumber", omNs);
        customerNumber.setText("103");
        payload.addChild(customerNumber);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "customerName");
        Assert.assertNotNull("Response message null ", result);
        log.debug(result);
        Assert.assertTrue("Expected not same", (result.toString().indexOf("<Name>Atelier graphique</Name>") > 1));

    }

    private void getCustomerOrders() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("customerOrders", omNs);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "customerOrders");
        Assert.assertNotNull("Response message null ", result);
        log.debug(result);
        Assert.assertTrue("Expected not same", (result.toString().indexOf("<Order><Order-number>") > 1));
    }

    private void getEmployeesInOffice() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples", "ns1");
        OMElement payload = fac.createOMElement("employeesInOffice", omNs);

        OMElement officeCode = fac.createOMElement("officeCode", omNs);
        officeCode.setText("1");
        payload.addChild(officeCode);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "employeesInOffice");
        Assert.assertNotNull("Response message null ", result);
        log.debug(result);
        Assert.assertNotNull("First Chilled null ", result.getFirstElement());
        log.debug(result.getFirstElement());
        Assert.assertTrue("Expected not same", (result.getFirstElement().toString().indexOf("<employeeNumber>1002</employeeNumber>") > 1));
    }

    private void getOffices() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("listOffices", omNs);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "listOffices");
        Assert.assertNotNull("Response message null ", result);
        log.debug(result);
        Assert.assertTrue("Expected not same", (result.toString().indexOf("<Office><officeCode>1</officeCode>") > 1));
    }
}
