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
import org.wso2.carbon.admin.service.DataServiceAdminService;
import org.wso2.carbon.dataservices.ui.stub.admin.core.xsd.DSTaskInfo;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.stratos.automation.test.dss.utils.FileManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateRSS;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ScheduleTaskAddTest extends TestTemplateRSS {
    private static final Log log = LogFactory.getLog(ScheduleTaskAddTest.class);
    private final String scheduleTaskName = "testScheduleTask";

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "ScheduleTaskTest.dbs";
        serviceName = "ScheduleTaskTest";
        serviceGroup = "ScheduleTask";
    }

    @Override
    public void executeSql() {

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
        authenticate();
        DataServiceAdminService dataServiceAdminService = new DataServiceAdminService(DSS_BACKEND_URL);
        DSTaskInfo dsTaskInfo = new DSTaskInfo();

        String[] taskNames = dataServiceAdminService.getAllTaskNames(sessionCookie);
        final String employeeId = "1";
        final int taskInterval = 10000;
        double empSalary;
        if (taskNames != null) {
            for (String task : taskNames) {
                if (scheduleTaskName.equals(task)) {
                    dataServiceAdminService.deleteTask(sessionCookie, scheduleTaskName);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        log.error("InterruptedException :", e);
                        Assert.fail("InterruptedException :" + e.getMessage());
                    }
                    break;
                }
            }
        }

        deleteEmployees();
        addEmployee(employeeId);
        getEmployeeById(employeeId);
        IncreaseEmployeeSalary(employeeId);

        empSalary = getEmployeeSalary(getEmployeeById(employeeId));
        log.info("Initial salary :" + empSalary);

        dsTaskInfo.setName(scheduleTaskName);
        dsTaskInfo.setServiceName(serviceName);
        dsTaskInfo.setOperationName("incrementEmployeeSalary");
        dsTaskInfo.setTaskInterval(taskInterval);
        dsTaskInfo.setTaskCount(9);
        dataServiceAdminService.scheduleTask(sessionCookie, dsTaskInfo);

        //if task count is 9
        for (int i = 0; i < 10; i++) {
            double currentSalary = getEmployeeSalary(getEmployeeById(employeeId));
            log.info("current salary after task: " + currentSalary);
            Assert.assertEquals("Task not properly Executed", (empSalary = empSalary + 10000), currentSalary);
            try {
                Thread.sleep(taskInterval);
            } catch (InterruptedException e) {
                log.error("InterruptedException :", e);
                Assert.fail("InterruptedException :" + e.getMessage());
            }
        }

        //for testing taskCount
        for (int i = 0; i < 5; i++) {
            double currentSalary = getEmployeeSalary(getEmployeeById(employeeId));
            log.info("current salary after exceeding task count " + currentSalary);
            Assert.assertEquals("Task Repeat Counter not properly Executed", empSalary, currentSalary);
            try {
                Thread.sleep(taskInterval);
            } catch (InterruptedException e) {
                log.error("InterruptedException :", e);
                Assert.fail("InterruptedException :" + e.getMessage());
            }
        }

        dataServiceAdminService.deleteTask(sessionCookie, scheduleTaskName);

        deleteEmployees();

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
        Assert.assertNotNull("Employee record null", result);
        Assert.assertTrue("Expected Result Mismatched", (result.toString().indexOf("<first-name>AAA</first-name>") > 1));
        return result;
    }

    private void deleteEmployees() {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/rdbms_sample", "ns1");
        OMElement payload = fac.createOMElement("deleteEmployees", omNs);

        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "deleteEmployees");

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

    private double getEmployeeSalary(OMElement employeeRecord) {
        OMElement employee = employeeRecord.getFirstElement();
        OMElement salary = (OMElement) employee.getChildrenWithLocalName("salary").next();
        return Double.parseDouble(salary.getText());
    }
}
