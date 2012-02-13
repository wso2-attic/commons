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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.stratos.automation.test.dss.utils.FileManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateRSS;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class InputParametersValidationTest extends TestTemplateRSS {
    private static final Log log = LogFactory.getLog(InputParametersValidationTest.class);

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "InputParamsValidationTest.dbs";
        serviceName = "InputParamsValidationTest";
        serviceGroup = "InputValidation";
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
        validateLastName("BB");
        validateLastName("BBBBBBBBBBBB CCCCCCCC");
        validateEmail("aaabbb.com");
        validateEmail("aaa@bbb");

        addEmployee("1");
        boolean validatePrimaryKey = false;
        try {
            addEmployee("1");
        } catch (junit.framework.AssertionFailedError e) {
            validatePrimaryKey = true;
            Assert.assertTrue("DATABASE_ERROR Not Found in error message", (e.getMessage().indexOf("DATABASE_ERROR") > 1));
            Assert.assertTrue("Message Duplicate entry Not Found in error message", (e.getMessage().indexOf("Duplicate entry") > 1));

        }
        Assert.assertTrue("primary key validation failed in Employees table", validatePrimaryKey);
    }

    private void validateLastName(String lastNameValue) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/StoredProcedure", "ns1");
        OMElement payload = fac.createOMElement("addEmployee", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText("127");
        payload.addChild(empNo);

        OMElement lastName = fac.createOMElement("lastName", omNs);
        lastName.setText(lastNameValue);
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

        boolean validationStatus = false;
        try {
            new AxisServiceClient().sendRobust(payload, serviceEndPoint, "addEmployee");
        } catch (junit.framework.AssertionFailedError e) {
            validationStatus = true;
            Assert.assertTrue("VALIDATION_ERROR Not Found in error message", (e.getMessage().indexOf("VALIDATION_ERROR") > 1));
            Assert.assertTrue("Field Name: lastName Not Found in error message", (e.getMessage().indexOf("Field Name: lastName") > 1));
            Assert.assertTrue("Validation Message: The value length must be between 3 and 20 Not Found in error message", (e.getMessage().indexOf("The value length must be between 3 and 20") > 1));
            log.info("Last Name length validated");
        }
        Assert.assertTrue("Last Name length Not validated", validationStatus);

    }


    private void validateEmail(String emailAddress) {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/StoredProcedure", "ns1");
        OMElement payload = fac.createOMElement("addEmployee", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText("124");
        payload.addChild(empNo);

        OMElement lastName = fac.createOMElement("lastName", omNs);
        lastName.setText("BBB");
        payload.addChild(lastName);

        OMElement fName = fac.createOMElement("firstName", omNs);
        fName.setText("AAA");
        payload.addChild(fName);

        OMElement email = fac.createOMElement("email", omNs);
        email.setText(emailAddress);
        payload.addChild(email);

        OMElement salary = fac.createOMElement("salary", omNs);
        salary.setText("50000");
        payload.addChild(salary);

        boolean validationStatus = false;
        try {
            new AxisServiceClient().sendRobust(payload, serviceEndPoint, "addEmployee");

        } catch (junit.framework.AssertionFailedError e) {
            validationStatus = true;
            Assert.assertTrue("VALIDATION_ERROR Not Found in error message", (e.getMessage().indexOf("VALIDATION_ERROR") > 1));
            Assert.assertTrue("Field Name: email Not Found in error message", (e.getMessage().indexOf("Field Name: email") > 1));
            Assert.assertTrue("Validation Message: Pattern Not Found in error message", (e.getMessage().indexOf("Pattern") > 1));

            log.info("email length validated");
        }
        Assert.assertTrue("email address Not validated", validationStatus);
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
}
