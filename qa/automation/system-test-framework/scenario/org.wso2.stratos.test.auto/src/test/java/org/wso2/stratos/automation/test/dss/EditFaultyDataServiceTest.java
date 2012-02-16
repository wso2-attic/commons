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
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.DataServiceAdminService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.stratos.automation.test.dss.utils.AdminServiceClientDSS;
import org.wso2.stratos.automation.test.dss.utils.MySqlDatabaseManager;
import org.wso2.stratos.automation.test.dss.utils.TestTemplateFaultyService;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;

public class EditFaultyDataServiceTest extends TestTemplateFaultyService {

    private static final Log log = LogFactory.getLog(FaultyServiceTest.class);

    @Override
    public void setServiceMetaData() {
        serviceFileLocation = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "rdbms" + File.separator + "MySql";
        serviceFileName = "FaultyDataService.dbs";
        serviceName = "FaultyDataService";
        serviceGroup = "FaultyService";
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
        try {
            serviceFile = new DataHandler(new URL("file://" + serviceFileLocation + File.separator + serviceFileName));
        } catch (MalformedURLException e) {
            log.error("Resource file Not Found " + e.getMessage());
            Assert.fail("Resource file Not Found " + e.getMessage());
        }
    }

    @Override
    public void runSuccessCase() {
        AdminServiceClientDSS adminServiceClientDSS = new AdminServiceClientDSS(DSS_BACKEND_URL);
        DataServiceAdminService dataServiceAdminService = new DataServiceAdminService(DSS_BACKEND_URL);
        String serviceContent;
        String newServiceContent = null;
        String serviceEndpoint = null;
        Assert.assertTrue("Service not in faulty service list", adminServiceClientDSS.isServiceFaulty(sessionCookie, serviceName));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("InterruptedException " + e.getMessage());
            Assert.fail("InterruptedException " + e.getMessage());
        }
        serviceContent = dataServiceAdminService.getDataServiceContent(sessionCookie, serviceName);

        try {
            OMElement dbsFile = AXIOMUtil.stringToOM(serviceContent);
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
            newServiceContent = dbsFile.toString();
        } catch (XMLStreamException e) {
            log.error("XMLStreamException while handling data service content " + e.getMessage());
            Assert.fail("XMLStreamException while handling data service content " + e.getMessage());
        }
        Assert.assertNotNull("Could not edited service content", newServiceContent);
        dataServiceAdminService.editDataService(sessionCookie, serviceName, "", newServiceContent);
        adminServiceClientDSS.isServiceDeployed(sessionCookie, serviceName, FrameworkSettings.SERVICE_DEPLOYMENT_DELAY);

        //todo this sleep should be removed after fixing CARBON-11900 gira
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            Assert.fail("Thread InterruptedException");
        }

        ServiceMetaData serviceMetaData = adminServiceClientDSS.getServiceData(sessionCookie, serviceName);
        String[] endpoints = serviceMetaData.getEprs();
        Assert.assertNotNull("Service Endpoint object null", endpoints);
        Assert.assertTrue("No service endpoint found", (endpoints.length > 0));
        for (String epr : endpoints) {
            if (epr.startsWith("http://")) {
                serviceEndpoint = epr;
                break;
            }
        }
        log.info("Service End point :" + serviceEndpoint);
        Assert.assertNotNull("Service Endpoint null", serviceEndpoint);
        invokeService(serviceEndpoint);
    }

    private void invokeService(String serviceEndpoint) {
        OMElement response;
        for (int i = 0; i < 5; i++) {
            AxisServiceClient axisServiceClient = new AxisServiceClient();
            response = axisServiceClient.sendReceive(getPayload(), serviceEndpoint, "showAllOffices");
            Assert.assertTrue("Expected Result not Found", (response.toString().indexOf("<Office>") > 1));
            Assert.assertTrue("Expected Result not Found", (response.toString().indexOf("</Office>") > 1));
        }
    }

    private OMElement getPayload() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/faulty_dataservice", "ns1");
        OMElement payload = fac.createOMElement("showAllOffices", omNs);
        return payload;
    }

    @Override
    public void artifactCleanup() {
        try {
            Thread.sleep(15 * 1000);
        } catch (InterruptedException e) {
            log.error(e);
        }
        AdminServiceAuthentication adminServiceAuthentication = new AdminServiceAuthentication(DSS_BACKEND_URL);
        AdminServiceService adminServiceService = new AdminServiceService(DSS_BACKEND_URL);
        sessionCookie = adminServiceAuthentication.login(USER_NAME, PASSWORD, "localhost");
        adminServiceService.deleteService(sessionCookie, new String[]{serviceGroup});
        log.info("Service undeployed");
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            log.error(e);
        }
        Assert.assertFalse("Service Still in service list", adminServiceService.isServiceExists(sessionCookie, serviceName));

    }
}
