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
package org.wso2.stratos.automation.test.bps.manageScenarios;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.*;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.bpel.stub.mgt.types.LimitedInstanceInfoType;
import org.wso2.carbon.bpel.stub.mgt.types.PaginatedInstanceList;
import org.wso2.carbon.system.test.core.RequestSender;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;

import javax.xml.stream.XMLStreamException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class BpelInstanceManagementClient extends TestTemplate {

    String sessionCookie = null;
    private static final Log log = LogFactory.getLog(BpelProcessManagementClient.class);
    String backEndUrl = null;
    String serviceUrl = null;
    AdminServiceBpelUploader bpelUploader;
    AdminServiceBpelPackageManager bpelManager;
    AdminServiceBpelProcessManager bpelProcrss;
    AdminServiceBpelInstanceManager bpelInstance;
    AdminServiceAuthentication adminServiceAuthentication;
    RequestSender requestSender;

    @Override
    public void init() throws MalformedURLException, InterruptedException, RemoteException {
        FrameworkSettings.getFrameworkProperties();
        backEndUrl = FrameworkSettings.BPS_BACKEND_URL;
        adminServiceAuthentication = new AdminServiceAuthentication(backEndUrl);
        System.out.println(FrameworkSettings.BPS_BACKEND_URL);
        testClassName = BpelProcessManagementClient.class.getName();
        if (FrameworkSettings.getStratosTestStatus()) {
            TenantDetails bpsTenant = TenantListCsvReader.getTenantDetails(2);
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
          //  bpelUploader.deployBPEL("TestPickOneWay",  sessionCookie);
    }

    @Override
    public void runSuccessCase() {
        try {

            assertTrue("Service is still available", requestSender.isServiceAvailable(serviceUrl + "/PickService"));
            EndpointReference epr = new EndpointReference(serviceUrl + "/PickService" + "/" + "dealDeck");
            try {
                requestSender.sendRequest("<pic:dealDeck xmlns:pic=\"http://www.stark.com/PickService\">" +
                        "   <pic:Deck>testPick</pic:Deck>" +
                        "</pic:dealDeck>", epr);
            } catch (XMLStreamException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            Thread.sleep(5000);
            PaginatedInstanceList instanceList = bpelInstance.filterPageInstances(bpelProcrss.getProcessId("PickProcess"));
            LimitedInstanceInfoType instanceInfo = instanceList.getInstance()[0];
            if (instanceList.getInstance().length != 0) {
                bpelInstance.performAction(instanceInfo.getIid(), AdminServiceBpelInstanceManager.InstanceOperation.SUSPEND);
                Thread.sleep(5000);
                assertTrue("The Service Is not Suspended", bpelInstance.getInstanceInfo(instanceInfo.getIid()).getStatus().getValue().equals("SUSPENDED"));
                bpelInstance.performAction(instanceInfo.getIid(), AdminServiceBpelInstanceManager.InstanceOperation.RESUME);
                Thread.sleep(5000);
                assertTrue("The Service Is not Suspended", bpelInstance.getInstanceInfo(instanceInfo.getIid()).getStatus().getValue().equals("ACTIVE"));
                bpelInstance.performAction(instanceInfo.getIid(), AdminServiceBpelInstanceManager.InstanceOperation.TERMINATE);
                Thread.sleep(5000);
                assertTrue("The Service Is not Suspended", bpelInstance.getInstanceInfo(instanceInfo.getIid()).getStatus().getValue().equals("TERMINATED"));
                bpelInstance.deleteInstance(instanceInfo.getIid());
            }
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("Instance management failed" + e.getMessage());
            fail(e.getMessage());
        }
    }

    @Override
    public void cleanup() {
        bpelManager.undeployBPEL("TestPickOneWay");
        adminServiceAuthentication.logOut();
    }
}
