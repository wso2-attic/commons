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
import org.wso2.carbon.admin.service.AdminServiceSecurity;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.system.test.core.utils.axis2Client.SecureAxisServiceClient;
import org.wso2.stratos.automation.test.dss.utils.AdminServiceClientDSS;
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

public class SecureDataServiceTestClient extends TestTemplateRSS {

    private static final Log log = LogFactory.getLog(TestMySqlFileServiceClient.class);

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "SecureDataService.dbs";
        serviceName = "SecureDataService";
        serviceGroup = "secureService";
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
        OMElement response;
        //todo this sleep should be removed after fixing CARBON-11900
         try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Assert.fail("InterruptedException :" + e.getMessage());
            }
        //secure service with username token
        secureService(1);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Assert.fail("InterruptedException :" + e.getMessage());

        }

        SecureAxisServiceClient secureAxisServiceClient = new SecureAxisServiceClient();

        AdminServiceClientDSS adminServiceClientDSS = new AdminServiceClientDSS(DSS_BACKEND_URL);
        ServiceMetaData serviceMetaData = adminServiceClientDSS.getServiceData(sessionCookie, serviceName);
        String[] endpoints = serviceMetaData.getEprs();
        Assert.assertNotNull("Service Endpoint object null", endpoints);
        Assert.assertTrue("No service endpoint found", (endpoints.length > 0));
        for(String epr : endpoints){
            if(epr.startsWith("https://")){
                serviceEndPoint = epr;
                break;
            }
        }
        log.info("Service End point :" + serviceEndPoint);

        for (int i = 0; i < 5; i++) {
            secureAxisServiceClient = new SecureAxisServiceClient();
            response = secureAxisServiceClient.sendReceive(USER_NAME, PASSWORD, serviceEndPoint, "showAllOffices", getPayload(), 1);
            Assert.assertTrue("Expected Result not Found", (response.toString().indexOf("<Office>") > 1));
            Assert.assertTrue("Expected Result not Found", (response.toString().indexOf("</Office>") > 1));
        }

        response = null;
        //todo should be enabled for kerberos i <17
        for (int i = 2; i < 16; i++) {
            secureService(i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Assert.fail("InterruptedException :" + e.getMessage());
            }
            serviceMetaData = adminServiceClientDSS.getServiceData(sessionCookie, serviceName);
            endpoints = serviceMetaData.getEprs();
            Assert.assertNotNull("Service Endpoint object null", endpoints);
            Assert.assertTrue("No service endpoint found", (endpoints.length > 0));
            for (String epr : endpoints) {
                if (epr.startsWith("http://")) {
                    serviceEndPoint = epr;
                    break;
                }
            }
            log.info("Service End point :" + serviceEndPoint);
            for (int j = 0; j < 5; j++) {
                response = secureAxisServiceClient.sendReceive(USER_NAME, PASSWORD, serviceEndPoint, "showAllOffices", getPayload(), i);
                Assert.assertTrue("Expected Result not Found", (response.toString().indexOf("<Office>") > 1));
                Assert.assertTrue("Expected Result not Found", (response.toString().indexOf("</Office>") > 1));
            }
        }
    }

    private void secureService(int policyId) {
        AdminServiceSecurity adminServiceSecurity = new AdminServiceSecurity(DSS_BACKEND_URL);
        authenticate();
        if (FrameworkSettings.STRATOS_TEST) {
            adminServiceSecurity.applySecurity(sessionCookie, serviceName, policyId + "", new String[]{"admin"}, new String[]{"wso2carbon.jks"}, TENANT_DOMAIN.replace('.', '-') + ".jks");
        } else {
            adminServiceSecurity.applySecurity(sessionCookie, serviceName, policyId + "", new String[]{"admin"}, new String[]{"wso2carbon.jks"}, "wso2carbon.jks");
        }
        log.info("Security Scenario " + policyId + " Applied");
    }

    private OMElement getPayload() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/secure_dataservice", "ns1");
        OMElement payload = fac.createOMElement("showAllOffices", omNs);
        return payload;
    }
}
