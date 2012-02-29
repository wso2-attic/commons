package org.wso2.stratos.automation.test.bps.bpelActivities;

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
import java.util.ArrayList;
import java.util.List;

public class bpelActComposeUrl extends TestTemplate {
    String sessionCookie = null;
    private static final Log log = LogFactory.getLog(bpelActComposeUrl.class);
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
        testClassName = bpelActComposeUrl.class.getName();
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
        bpelUploader.deployBPEL("TestComposeUrl",  sessionCookie);
    }

    @Override
    public void runSuccessCase() {
        int instanceCount = 0;

        String processID = bpelProcrss.getProcessId("TestComposeUrl");
        PaginatedInstanceList instanceList = new PaginatedInstanceList();
        instanceList = bpelInstance.filterPageInstances(processID);
        if (instanceList.getInstance() != null) {
            instanceCount = instanceList.getInstance().length;
        }
        if (!processID.isEmpty()) {
            try {
                this.forEachRequest();
                Thread.sleep(5000);
                if (instanceCount >= bpelInstance.filterPageInstances(processID).getInstance().length) {
                    fail("Instance is not created for the request");
                }
            } catch (InterruptedException e) {
                log.error("Process management failed" + e.getMessage());
                fail(e.getMessage());
            }
            bpelInstance.clearInstancesOfProcess(processID);
        }
    }

    @Override
    public void cleanup() {
        bpelManager.undeployBPEL("TestComposeUrl");
        adminServiceAuthentication.logOut();
    }

    public void forEachRequest() {
         String payload = " <p:composeUrl xmlns:p=\"http://ode/bpel/unit-test.wsdl\">\n" +
                 "      <!--Exactly 1 occurrence-->\n" +
                 "      <template>www.google.com</template>\n" +
                 "      <!--Exactly 1 occurrence-->\n" +
                 "      <name>google</name>\n" +
                 "      <!--Exactly 1 occurrence-->\n" +
                 "      <value>ee</value>\n" +
                 "      <!--Exactly 1 occurrence-->\n" +
                 "      <pairs>\n" +
                 "         <!--Exactly 1 occurrence-->\n" +
                 "         <user>er</user>\n" +
                 "         <!--Exactly 1 occurrence-->\n" +
                 "         <tag>ff</tag>\n" +
                 "      </pairs>\n" +
                 "   </p:composeUrl>";
        String operation = "composeUrl";
        String serviceName = "/TestComposeUrlService";
        List<String> expectedOutput = new ArrayList<String>();
        expectedOutput.add("www.google");

        requestSender.sendRequest(serviceUrl + serviceName, operation, payload,
                1, expectedOutput, true);
    }
}

