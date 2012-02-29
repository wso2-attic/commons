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
package org.wso2.stratos.automation.test.bps.uploadScenarios;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.*;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.bpel.stub.mgt.types.PaginatedInstanceList;
import org.wso2.carbon.system.test.core.RequestSender;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class BpelInMemoryDeploymentClient extends TestTemplate {

    String sessionCookie = null;
    private static final Log log = LogFactory.getLog(BpelInMemoryDeploymentClient.class);
    String backEndUrl = null;
    String serviceUrl = null;
    AdminServiceBpelUploader bpelUploader;
    AdminServiceBpelPackageManager bpelManager;
    AdminServiceBpelProcessManager bpelProcrss;
    AdminServiceBpelInstanceManager bpelInstance;
    AdminServiceAuthentication adminServiceAuthentication;

    @Override
    public void init() {
        FrameworkSettings.getFrameworkProperties();
        backEndUrl = FrameworkSettings.BPS_BACKEND_URL;
        adminServiceAuthentication = new AdminServiceAuthentication(backEndUrl);
        System.out.println(FrameworkSettings.BPS_BACKEND_URL);
        testClassName = BpelInMemoryDeploymentClient.class.getName();
        if (FrameworkSettings.getStratosTestStatus()) {
            TenantDetails bpsTenant = TenantListCsvReader.getTenantDetails(3);
            serviceUrl = "http://" + FrameworkSettings.BPS_SERVER_HOST_NAME + "/services/t/" + bpsTenant.getTenantName().split("@")[1];
            sessionCookie = adminServiceAuthentication.login(bpsTenant.getTenantName(), bpsTenant.getTenantPassword(), FrameworkSettings.BPS_SERVER_HOST_NAME);

        } else {
            sessionCookie = adminServiceAuthentication.login("admin", "admin", FrameworkSettings.BPS_SERVER_HOST_NAME);
            serviceUrl = "http://" + FrameworkSettings.BPS_SERVER_HOST_NAME + ":" + FrameworkSettings.BPS_SERVER_HTTP_PORT + "/services";
        }
        bpelUploader = new AdminServiceBpelUploader(backEndUrl);
        bpelManager = new AdminServiceBpelPackageManager(backEndUrl, sessionCookie);
        bpelProcrss = new AdminServiceBpelProcessManager(backEndUrl, sessionCookie);
        bpelInstance = new AdminServiceBpelInstanceManager(backEndUrl, sessionCookie);
        bpelUploader.deployBPEL("CustomerInfo", sessionCookie);
    }

    @Override
    public void runSuccessCase() {
        bpelProcrss.getStatus(bpelProcrss.getProcessId("CustomerInfo"));
        RequestSender requestSender = new RequestSender();
        requestSender.waitForProcessDeployment(serviceUrl + "/CustomerInfoService");

        requestSender.assertRequest(serviceUrl + "/CustomerInfoService", "getCustomerSSN", "<p:CustomerInfo xmlns:p=\"http://wso2.org/bps/samples/loan_process/schema\">\n" +
                "      <!--Exactly 1 occurrence-->\n" +
                "      <Name xmlns=\"http://wso2.org/bps/samples/loan_process/schema\">Dharshana</Name>\n" +
                "      <!--Exactly 1 occurrence-->\n" +
                "      <Email xmlns=\"http://wso2.org/bps/samples/loan_process/schema\">dharshanaw@wso2.com</Email>\n" +
                "      <!--Exactly 1 occurrence-->\n" +
                "      <tns:CustomerID xmlns:tns=\"http://wso2.org/bps/samples/loan_process/schema\">?</tns:CustomerID>\n" +
                "      <!--Exactly 1 occurrence-->\n" +
                "      <CreditRating xmlns=\"http://wso2.org/bps/samples/loan_process/schema\">?</CreditRating>\n" +
                "   </p:CustomerInfo>\n", 1, "43235678SSN", true);

        PaginatedInstanceList instanceList = bpelInstance.filterPageInstances(bpelProcrss.getProcessId("CustomerInfo"));
        assertTrue("Service is not running inmemory", instanceList == null);
    }

    @Override
    public void cleanup() {
        bpelManager.undeployBPEL("CustomerInfo");
        adminServiceAuthentication.logOut();
    }
}

