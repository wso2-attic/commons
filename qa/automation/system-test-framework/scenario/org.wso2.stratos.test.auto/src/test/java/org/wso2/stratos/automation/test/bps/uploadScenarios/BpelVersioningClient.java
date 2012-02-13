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
import org.wso2.carbon.bpel.stub.mgt.types.LimitedProcessInfoType;
import org.wso2.carbon.bpel.stub.mgt.types.PaginatedProcessInfoList;
import org.wso2.carbon.bpel.stub.mgt.types.ProcessStatus;
import org.wso2.carbon.system.test.core.RequestSender;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import java.util.LinkedList;
import java.util.List;

public class BpelVersioningClient extends TestTemplate {
    String sessionCookie = null;
    private static final Log log = LogFactory.getLog(BpelVersioningClient.class);
    String backEndUrl = null;
    String serviceUrl = null;
    AdminServiceBpelUploader bpelUploader;
    AdminServiceBpelPackageManager bpelManager;
    AdminServiceBpelProcessManager bpelProcrss;
    AdminServiceBpelInstanceManager bpelInstance;
    AdminServiceAuthentication adminServiceAuthentication;
    RequestSender requestSender;

    @Override
    public void init() {
        FrameworkSettings.getFrameworkProperties();
        backEndUrl = FrameworkSettings.BPS_BACKEND_URL;
        adminServiceAuthentication = new AdminServiceAuthentication(backEndUrl);
        System.out.println(FrameworkSettings.BPS_BACKEND_URL);
        testClassName = BpelVersioningClient.class.getName();
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
        requestSender = new RequestSender();
        bpelUploader.deployBPEL("HelloWorld2", "HelloWorld2", sessionCookie);
    }

    @Override
    public void runSuccessCase() {
        try {
            Thread.sleep(5000);
            PaginatedProcessInfoList processBefore = bpelProcrss.getProcessInfo("HelloWorld2");
            List<String> activeStatus = new LinkedList<String>();
            boolean activeProcessFound = false;
            for (LimitedProcessInfoType processInfo : processBefore.getProcessInfo()) {
                if (processInfo.getStatus() == ProcessStatus.ACTIVE) {
                    activeStatus.add(processInfo.getPid());
                }
            }
            String payLoad = " <p:hello xmlns:p=\"http://ode/bpel/unit-test.wsdl\">\n" +
                    "      <!--Exactly 1 occurrence--><TestPart>test</TestPart>\n" +
                    "   </p:hello>";

            String operation = "hello";
            String serviceName = "/HelloService";
            String expectedBefore = "World";
            String expectedAfter = "World-Version";
            requestSender.assertRequest(serviceUrl + serviceName, operation, payLoad,
                    1, expectedBefore, true);

            bpelUploader.deployBPEL("HelloWorld2", "VersioningSamples", "HelloWorld2", sessionCookie);

            PaginatedProcessInfoList processAfter = null;
            for (int a = 0; a <= 10; a++) {
                Thread.sleep(5000);
                processAfter = bpelProcrss.getProcessInfo("HelloWorld2");
                if (bpelProcrss.getStatus(activeStatus.get(0)).equals(ProcessStatus.RETIRED.toString()))
                    break;
            }

            for (String process : activeStatus) {
                assertTrue("Versioning failed : Previous Version " + process + "is still active", bpelProcrss.getStatus(process).equals(ProcessStatus.RETIRED.toString()));
            }
            for (LimitedProcessInfoType processInfo : processAfter.getProcessInfo()) {
                if (processInfo.getStatus() == ProcessStatus.ACTIVE) {
                    activeProcessFound = true;
                    for (String process : activeStatus) {
                        assertFalse("Versioning failed : Previous Version " + processInfo.getVersion() + "is still active", process.equals(processInfo.getPid()));
                    }
                }
            }
            assertTrue("No Active Processes available", activeProcessFound);
            requestSender.assertRequest(serviceUrl + serviceName, operation, payLoad,
                    1, expectedAfter, true);
        } catch (InterruptedException e) {
            fail("Process Interrupted");
        }

    }

    @Override
    public void cleanup() {
        bpelManager.undeployBPEL("HelloWorld2");
        adminServiceAuthentication.logOut();
    }


}
