/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.stratos.automation.test.esb;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceJARServiceUploader;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.admin.service.utils.ProductConstant;
import org.wso2.carbon.jarservices.stub.DuplicateServiceExceptionException;
import org.wso2.carbon.jarservices.stub.DuplicateServiceGroupExceptionException;
import org.wso2.carbon.jarservices.stub.JarUploadExceptionException;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClientUtils;

import javax.activation.DataHandler;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


/*
This is used to test the service chaining functionality of ESB.
In this scenario, ESB will receive a credit request from a proxy service called CreditProxy.
The request has the ID of the requestor of the operation and the credit amount. But, to call the CreditService deployed in the WSO2 Application Server,
the request must also be enriched with the name and address information of the requestor. 
This information can be obtained from the PersonInfoService deployed in the WSO2 Application Server.
So, first, we need to call the PersonInfoService and enrich the request with the name and address before calling the CreditService.
 */


public class ServiceChainingTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(StratosCustomProxyTest.class);

    @Override
    public void init() {
        testClassName = ServiceChainingTest.class.getName();
    }

    public void runSuccessCase() {
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId("0"));
        String payload = createPayLoad().toString();
        List<String> expectedOutput = new ArrayList<String>();
        expectedOutput.add(">true<");

        try {
            String backendURL = FrameworkSettings.APP_BACKEND_URL;
            AdminServiceJARServiceUploader adminServiceJARServiceUploader = new AdminServiceJARServiceUploader(backendURL);
            String sessionCookie = login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(),
                    FrameworkSettings.APP_BACKEND_URL);

            URL url = new URL("file:///" + ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "artifacts" + File.separator + "AS" + File.separator + "jar" + File.separator + "CreditService.jar");
            log.info("URL " + url.toString());
            DataHandler dh = new DataHandler(url);
            adminServiceJARServiceUploader.uploadJARServiceFile(sessionCookie, "serviceGroup", dh, null);
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JarUploadExceptionException e) {
            e.printStackTrace();
        } catch (DuplicateServiceGroupExceptionException e) {
            e.printStackTrace();
        } catch (DuplicateServiceExceptionException e) {
            e.printStackTrace();
        }
        String eprCreditProxy;
        String eprCreditService;
        String eprPersonInfoService;

        if (FrameworkSettings.getStratosTestStatus()) {
            eprCreditProxy = "http://" + FrameworkSettings.ESB_SERVER_HOST_NAME + ":" + FrameworkSettings.ESB_NHTTP_PORT
                    + "/services/t/" + tenantDetails.getTenantDomain() + "/CreditProxy";
            eprCreditService = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain() + "/CreditService";
            eprPersonInfoService = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain() + "/PersonInfoService";

        } else {
            eprCreditProxy = "http://" + FrameworkSettings.ESB_SERVER_HOST_NAME + ":" + FrameworkSettings.ESB_NHTTP_PORT + "/services" + "/CreditProxy";
            eprCreditService = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + ":" + FrameworkSettings.APP_SERVER_HTTP_PORT + "/services" + "/CreditService";
            eprPersonInfoService = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + ":" + FrameworkSettings.APP_SERVER_HTTP_PORT + "/services" + "/PersonInfoService";
        }
        System.out.println(eprCreditProxy);
        System.out.println(eprCreditService);
        System.out.println(eprPersonInfoService);
        String operation = ("credit");
        AxisServiceClientUtils.waitForServiceDeployment(eprCreditService);
        AxisServiceClientUtils.waitForServiceDeployment(eprPersonInfoService);
        AxisServiceClientUtils.waitForServiceDeployment(eprCreditProxy);
        AxisServiceClientUtils.sendRequest(eprCreditProxy, operation, payload, 1, expectedOutput, true);
    }

    @Override
    public void cleanup() {

    }

    private static OMElement createPayLoad() {

        /* PayLoad =  "<sam:credit xmlns:sam="http://samples.esb.wso2.org\">
        <sam:id>99990000</sam:id><sam:amount>1000</sam:amount>
        </sam:credit>*/


        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://samples.esb.wso2.org", "sam");
        OMElement method = fac.createOMElement("credit", omNs);
        OMElement id = fac.createOMElement("id", omNs);
        OMElement amount = fac.createOMElement("amount", omNs);
        id.addChild(fac.createOMText(id, "99990000"));
        amount.addChild(fac.createOMText(amount, "1000"));
        method.addChild(id);
        method.addChild(amount);
        return method;
    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }
}
