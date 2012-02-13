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
import org.wso2.stratos.automation.test.dss.utils.TestTemplateRSS;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class TestMySqlFileServiceClient extends TestTemplateRSS {
    private static final Log log = LogFactory.getLog(TestMySqlFileServiceClient.class);
    private static final String txtFileName = "TestFile.txt";
    private static final String txtFileType = "txt";


    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "MySqlFileServiceTest.dbs";
        serviceName = "MySqlFileServiceTest";
        serviceGroup = "MySqlFileService";

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

        OMElement response = checkFileExists();
        if (Integer.parseInt(response.getFirstElement().getFirstElement().getText()) == 1) {
            deleteFile();
        }

        createNewFile();
        response = checkFileExists();
        Assert.assertEquals("Expected Not same .File Not Exists", "1", response.getFirstElement().getFirstElement().getText());

        getFileType();
        getFileNames();
        addRecord();
        getRecord();
        getFileSize();
        deleteFile();
        response = checkFileExists();
        Assert.assertEquals("Expected Not same .File not deleted", "0", response.getFirstElement().getFirstElement().getText());

    }

    private void deleteFile() {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/file_service", "ns1");
        OMElement payload = fac.createOMElement("_getdeletefile", omNs);

        OMElement fileName = fac.createOMElement("fileName", omNs);
        fileName.setText(txtFileName);
        payload.addChild(fileName);

        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "_getdeletefile");
    }

    private void getFileSize() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/file_service", "ns1");
        OMElement payload = fac.createOMElement("_getgetfilesize", omNs);

        OMElement fileName = fac.createOMElement("fileName", omNs);
        fileName.setText(txtFileName);
        payload.addChild(fileName);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "_getgetfilesize");
        System.out.println(result);
        Assert.assertNotNull("Response message null ", result);
        Assert.assertTrue("Expected not same", Integer.parseInt(result.getFirstElement().getFirstElement().getText()) > 1);


    }

    private OMElement getRecord() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/file_service", "ns1");
        OMElement payload = fac.createOMElement("_getgetfilerecords", omNs);

        OMElement fileName = fac.createOMElement("fileName", omNs);
        fileName.setText(txtFileName);
        payload.addChild(fileName);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "_getgetfilerecords");
        System.out.println(result);
        Assert.assertNotNull("Response message null ", result);
        return result;
    }

    private void addRecord() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/file_service", "ns1");
        OMElement payload = fac.createOMElement("_postappenddatatofile", omNs);
        String recordsExpected = "";

        OMElement fileName = fac.createOMElement("fileName", omNs);
        fileName.setText(txtFileName);
        payload.addChild(fileName);

        OMElement fileRecord = fac.createOMElement("data", omNs);
        AxisServiceClient axisClient = new AxisServiceClient();
        for (int i = 0; i < 5; i++) {
            fileRecord.setText("TestFileRecord" + i);
            payload.addChild(fileRecord);
            recordsExpected = recordsExpected + fileRecord.getText() + ";";
            axisClient.sendRobust(payload, serviceEndPoint, "_postappenddatatofile");

        }

        OMElement response = getRecord();
        Iterator file = response.getChildrenWithLocalName("File");
        String recordData = "";
        while(file.hasNext()){
            OMElement data = (OMElement)file.next();
            recordData = recordData + data.getFirstElement().getText() + ";";

        }
        Assert.assertNotSame("No Record added to file. add records to file", "", recordsExpected);
        Assert.assertEquals("Record Data Mismatchecd", recordsExpected, recordData);
    }

    private void getFileType() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/file_service", "ns1");
        OMElement payload = fac.createOMElement("_getgetfiletype", omNs);

        OMElement fileName = fac.createOMElement("fileName", omNs);
        fileName.setText(txtFileName);
        payload.addChild(fileName);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "_getgetgetfiletype");
        Assert.assertNotNull("Response message null ", result);
        Assert.assertEquals("Expected not same", txtFileType, result.getFirstElement().getFirstElement().getText());


    }

    private void getFileNames() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/file_service", "ns1");
        OMElement payload = fac.createOMElement("_getgetfilenames", omNs);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "_getgetfilenames");
        Assert.assertNotNull("Response message null ", result);
        log.debug(result);
        Assert.assertTrue("File name not found ", result.toString().indexOf("<fileName>" + txtFileName + "</fileName>") > 0);
    }

    private OMElement checkFileExists() {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/file_service", "ns1");
        OMElement payload = fac.createOMElement("_getcheckfileexists", omNs);

        OMElement fileName = fac.createOMElement("fileName", omNs);
        fileName.setText(txtFileName);
        payload.addChild(fileName);

        OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "_getcheckfileexists");
        Assert.assertNotNull("Response message null ", result);
        Assert.assertTrue("Expected not same", result.toString().indexOf("Files") == 1);
        return result;

    }

    private void createNewFile() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/file_service", "ns1");
        OMElement payload = fac.createOMElement("_getcreatenewfile", omNs);

        OMElement fileName = fac.createOMElement("fileName", omNs);
        fileName.setText(txtFileName);
        payload.addChild(fileName);

        OMElement fileType = fac.createOMElement("fileType", omNs);
        fileType.setText(txtFileType);
        payload.addChild(fileType);

        new AxisServiceClient().sendRobust(payload, serviceEndPoint, "_getcreatenewfile");


    }
}
