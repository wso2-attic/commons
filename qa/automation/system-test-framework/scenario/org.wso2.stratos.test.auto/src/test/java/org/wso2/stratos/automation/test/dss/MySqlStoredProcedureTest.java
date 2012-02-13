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

public class MySqlStoredProcedureTest extends TestTemplateRSS {
    private static final Log log = LogFactory.getLog(MySqlStoredProcedureTest.class);

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "StoredProcedureServiceTest.dbs";
        serviceName = "StoredProcedureServiceTest";
        serviceGroup = "StoredProcedure";
    }

    @Override
    public void executeSql() {
        final String sqlFileLocation = RESOURCE_LOCATION + File.separator + "sql" + File.separator + "MySql" + File.separator;

        ArrayList<String> sqlFiles = new ArrayList<String>();
        sqlFiles.add(sqlFileLocation + "StoredProcedures.sql");
        sqlFiles.add(sqlFileLocation + "Employees.sql");

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

        log.info("Testing Stored procedures, IN params");
        for (int i = 1; i < 6; i++) {
            addEmployee(i + "");
        }
        log.info("Stored procedures, IN and OUT params");
        for (int i = 1; i < 6; i++) {
            getEmployeeById(i + "");
        }
        log.info("Stored procedures, OUT params");
        getEmployees();
    }

    private void addEmployee(String employeeNumber) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/StoredProcedure", "ns1");
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
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/StoredProcedure", "ns1");
        OMElement payload = fac.createOMElement("getEmployeeByEmpNo", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText(employeeNumber);
        payload.addChild(empNo);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "getEmployeeByEmpNo");
        Assert.assertTrue("Result Set Not found in response message", result.toString().contains("<Employee>"));
        Assert.assertTrue("Expected Result Mismatched", (result.toString().indexOf("<EmployeeNumber>" + employeeNumber + "</EmployeeNumber>") > 1));

        return result;
    }

    private OMElement getEmployees() {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/StoredProcedure", "ns1");
        OMElement payload = fac.createOMElement("getEmployees", omNs);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "getEmployees");
        Assert.assertTrue("Expected Result Mismatched", (result.toString().indexOf("<FirstName>AAA</FirstName>") > 1));
        return result;
    }

}
