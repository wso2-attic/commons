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
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.stratos.automation.test.dss.utils.FileManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateRSS;
import org.apache.commons.logging.Log;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class TestBatchRequestTestClient extends TestTemplateRSS{
    private static final Log log = LogFactory.getLog(TestBatchRequestTestClient.class);
    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "BatchRequestTest.dbs";
        serviceName = "BatchRequestTest";
        serviceGroup = "BatchRequest";
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

        deleteEmployee(5);
        addEmplyee();

        for (int i = 1; i < 6; i++) {
            Assert.assertEquals("Employee Not Found", "1", employeeExists( i + ""));
        }

        deleteEmployee(5);

        for (int i = 1; i < 6; i++) {
            Assert.assertEquals("Employee Found. deletion failed", "0", employeeExists( i + ""));
        }

        //batch request for 25 records
        deleteEmployee( 25);
        
        addEmployeeBatchRequest();
        for (int i = 1; i < 26; i++) {
            Assert.assertEquals("Employee Not Found", "1", employeeExists( i + ""));
        }

        deleteEmployeeBatchRequest();

        for (int i = 1; i < 26; i++) {
            Assert.assertEquals("Employee Found. batch deletion failed", "0", employeeExists( i + ""));
        }


    }

    private void deleteEmployee(int recordCount) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("deleteEmployee", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        payload.addChild(empNo);
        for(int i =1 ;i < recordCount + 1; i++) {
            empNo.setText(i+"");

            new AxisServiceClient().sendRobust(payload, serviceEndPoint, "deleteEmployee");
        }

    }

    private String employeeExists(String empId) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("employeeExists", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText(empId);
        payload.addChild(empNo);

        OMElement response = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "employeeExists");
        Assert.assertNotNull("Response null "+ response);
        return response.getFirstElement().getFirstElement().getText();
    }

    private void addEmplyee() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("addEmployee", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        //empNo.setText("1");
        payload.addChild(empNo);

        OMElement email = fac.createOMElement("email", omNs);
        email.setText("testemail@wso2.com");
        payload.addChild(email);

         for(int i = 1; i<6 ; i++) {
             empNo.setText(i + "");
             new AxisServiceClient().sendRobust(payload, serviceEndPoint, "addEmployee");
         }

    }

    private void addEmployeeBatchRequest() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("addEmployee_batch_req", omNs);

        for(int i = 1; i< 26; i++){
        OMElement batchRequest = fac.createOMElement("addEmployee", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText(i+"");
        batchRequest.addChild(empNo);

        OMElement email = fac.createOMElement("email", omNs);
        email.setText("testemail@wso2.com");
        batchRequest.addChild(email);

        payload.addChild(batchRequest);

        }
        log.debug(payload);
        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "addEmployee_batch_req");


    }

     private void deleteEmployeeBatchRequest() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice", "ns1");
        OMElement payload = fac.createOMElement("deleteEmployee_batch_req", omNs);

        for(int i = 1; i< 26; i++){
        OMElement batchRequest = fac.createOMElement("addEmployee", omNs);

        OMElement empNo = fac.createOMElement("employeeNumber", omNs);
        empNo.setText(i+"");
        batchRequest.addChild(empNo);

        payload.addChild(batchRequest);

        }
        log.debug(payload);
        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "deleteEmployee_batch_req");


    }
}
