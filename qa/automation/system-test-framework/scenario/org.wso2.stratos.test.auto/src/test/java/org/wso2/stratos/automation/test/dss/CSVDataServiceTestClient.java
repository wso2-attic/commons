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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceResourceAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.service.mgt.stub.types.carbon.ServiceMetaData;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.stratos.automation.test.dss.utils.AdminServiceClientDSS;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class CSVDataServiceTestClient extends TestTemplate {
    static {
        FrameworkSettings.getFrameworkProperties();
    }

    private static final Log log = LogFactory.getLog(CSVDataServiceTestClient.class);

    private static final String DSS_IP = FrameworkSettings.DSS_SERVER_HOST_NAME;
    private static final String DSS_BACKEND_URL = FrameworkSettings.DSS_BACKEND_URL;
    private static final String GR_BACKEND_URL = FrameworkSettings.GREG_BACKEND_URL;

    private static final String RESOURCE_LOCATION = ProductConstant.getResourceLocations(ProductConstant.DSS_SERVER_NAME);

    private static TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("3"));
    private static final String USER_NAME = tenantDetails.getTenantName();
    private static final String PASSWORD = tenantDetails.getTenantPassword();

    private static final String SERVICE_FILE_LOCATION = RESOURCE_LOCATION + File.separator + "dbs" + File.separator + "csv";
    private static final String SERVICE_FILE_NAME = "CSVDataService.dbs";
    private static final String SERVICE_NAME = "CSVDataService";
    private static final String SERVICE_GROUP = "CSV";

    private static final String RESOURCE_FILE_LOCATION = RESOURCE_LOCATION + File.separator + "resources";
    private static final String RESOURCE_FILE_NAME = "Products.csv";

    private String serviceEndPoint;

    @Override
    public void init() {
        testClassName = CSVDataServiceTestClient.class.getName();

        String sessionCookie;
        URL urlResourceFile = null;
        URL urlServiceFile = null;
        try {
            urlResourceFile = new URL("file://" + RESOURCE_FILE_LOCATION + File.separator + RESOURCE_FILE_NAME);
            urlServiceFile = new URL("file://" + SERVICE_FILE_LOCATION + File.separator + SERVICE_FILE_NAME);
        } catch (MalformedURLException e) {
            fail("Resource file Not Found " + e.getMessage());
        }

        DataHandler dhResource = new DataHandler(urlResourceFile);
        DataHandler dhService = new DataHandler(urlServiceFile);


        //login to governance
        AdminServiceAuthentication adminServiceAuthentication = new AdminServiceAuthentication(GR_BACKEND_URL);
        sessionCookie = adminServiceAuthentication.login(USER_NAME, PASSWORD, "localhost");
        AdminServiceResourceAdmin adminServiceResourceAdmin = new AdminServiceResourceAdmin(GR_BACKEND_URL);

        adminServiceResourceAdmin.addResource(sessionCookie, "/_system/governance/automation/resources/" + RESOURCE_FILE_NAME, "application/vnd.ms-excel", "", dhResource);
        adminServiceAuthentication.logOut();

        //login to data service
        AdminServiceClientDSS adminServiceClientDSS = new AdminServiceClientDSS(DSS_BACKEND_URL);
        sessionCookie = adminServiceClientDSS.authenticate(USER_NAME, PASSWORD);

        if (adminServiceClientDSS.isServiceExist(sessionCookie, SERVICE_NAME)) {
            adminServiceClientDSS.deleteService(sessionCookie, new String[]{SERVICE_GROUP});
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                Assert.fail("InterruptedException :" + e.getMessage());
            }
        }

        adminServiceClientDSS.uploadArtifact(sessionCookie, SERVICE_FILE_NAME, dhService);

        log.info("waiting " + FrameworkSettings.SERVICE_DEPLOYMENT_DELAY + " millis for service deployment");

        adminServiceClientDSS.isServiceDeployed(sessionCookie, SERVICE_NAME, FrameworkSettings.SERVICE_DEPLOYMENT_DELAY);

        ServiceMetaData serviceMetaData = adminServiceClientDSS.getServiceData(sessionCookie, SERVICE_NAME);
        Assert.assertEquals("Service Name Mismatched", SERVICE_NAME, serviceMetaData.getName());
        Assert.assertEquals("Service Group Mismatched", SERVICE_GROUP, serviceMetaData.getServiceGroupName());
        log.info("Service Deployed");

        String[] endpoints = serviceMetaData.getEprs();
        Assert.assertNotNull("Service Endpoint object null", endpoints);
        Assert.assertTrue("No service endpoint found", (endpoints.length > 0));
        for (String epr : endpoints) {
            if (epr.startsWith("http://")) {
                serviceEndPoint = epr;
                break;
            }
        }
        log.info("Service End point :" + serviceEndPoint);
        Assert.assertNotNull("service endpoint null", serviceEndPoint);
        Assert.assertTrue("Service endpoint not contain service name", serviceEndPoint.contains(SERVICE_NAME));
        adminServiceClientDSS.logOut();


    }

    @Override
    public void runSuccessCase() {

        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://ws.wso2.org/dataservice/samples/csv_sample_service", "ns1");
        OMElement payload = fac.createOMElement("getProducts", omNs);

        for (int i = 0; i < 5; i++) {
            OMElement result = new AxisServiceClient().sendReceive(payload, serviceEndPoint, "getProducts");
            Assert.assertTrue((result.toString().indexOf("Products") == 1));

        }
    }


    @Override
    public void cleanup() {
        AdminServiceAuthentication adminServiceAuthentication = new AdminServiceAuthentication(GR_BACKEND_URL);
        String sessionCookie = adminServiceAuthentication.login(USER_NAME, PASSWORD, "localhost");
        AdminServiceResourceAdmin adminServiceResourceAdmin = new AdminServiceResourceAdmin(GR_BACKEND_URL);

        adminServiceResourceAdmin.deleteResource(sessionCookie, "/_system/governance/automation/resources/" + RESOURCE_FILE_NAME);
        adminServiceAuthentication.logOut();


        AdminServiceClientDSS adminServiceClientDSS = new AdminServiceClientDSS(DSS_BACKEND_URL);
        sessionCookie = adminServiceClientDSS.authenticate(USER_NAME, PASSWORD);
        adminServiceClientDSS.deleteService(sessionCookie, new String[]{SERVICE_GROUP});
        adminServiceClientDSS.logOut();
    }

}