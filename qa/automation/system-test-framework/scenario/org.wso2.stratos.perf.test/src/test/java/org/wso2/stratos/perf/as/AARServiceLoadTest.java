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
package org.wso2.stratos.perf.as;

import junit.framework.AssertionFailedError;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClientUtils;
import org.wso2.carbon.system.test.core.utils.webAppUtils.TestExceptionHandler;

/*
Deploy an axis2 service and do load test against that service (c = 100 and n = 100)
 */
public class AARServiceLoadTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(AARServiceLoadTest.class);

    private static String AXIS2SERVICE_EPR;

    @Override
    public void init() {

        String tenantId = "15";
        testClassName = AARServiceLoadTest.class.getName();
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" + tenantDetails.getTenantDomain() + "/Axis2Service/";
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(FrameworkSettings.APP_BACKEND_URL);
        String sessionCookies = loginClient.login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(), FrameworkSettings.APP_BACKEND_URL);


        try {
            log.debug("Delete the services if already exists");
            deleteAllNotAdminServices(sessionCookies, FrameworkSettings.APP_BACKEND_URL);
            log.debug("Wait for service undeployment");
            AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR);
        } catch (AssertionFailedError e) {
            log.info("Service was already deleted");
        }
    }

    @Override
    public void runSuccessCase() {
        log.info("Running AAR load test with c=100 n=100. Test will take few minutes to complete please be patience");
        TestExceptionHandler exHandler = new TestExceptionHandler();
        AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR); // wait for service deployment
        loadTestAxis2Service();
        if (exHandler.throwable != null) {
            exHandler.throwable.printStackTrace();
            fail(exHandler.throwable.getMessage());
        }
    }

    @Override
    public void cleanup() {
        AxisServiceClientUtils.waitForServiceUnDeployment(AXIS2SERVICE_EPR);
    }

    private static boolean axis2ServiceTest() {
        boolean axis2ServiceStatus = false;
        OMElement result;
        try {
            OMElement payload = createPayLoad();
            ServiceClient serviceclient = new ServiceClient();
            Options opts = new Options();

            opts.setTo(new EndpointReference(AXIS2SERVICE_EPR));
            opts.setAction("http://service.carbon.wso2.org/echoString");
            opts.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
            serviceclient.setOptions(opts);
            log.debug("Axis2Service EPR " + AXIS2SERVICE_EPR);


            log.info("Wait for Demo proxy deployment");

            result = serviceclient.sendReceive(payload);

            if ((result.toString().indexOf("Hello World")) > 0) {
                axis2ServiceStatus = true;
            }
            assertTrue("Axis2Service invocation failed", axis2ServiceStatus);

        } catch (AxisFault axisFault) {
            log.error("Axis2Service invocation failed :" + axisFault.getMessage());
            fail("Axis2Service invocation failed :" + axisFault.getMessage());
        }

        return axis2ServiceStatus;
    }

    private static OMElement createPayLoad() {
        log.debug("Creating payload");
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://service.carbon.wso2.org", "ns1");
        OMElement method = fac.createOMElement("echoString", omNs);
        OMElement value = fac.createOMElement("s", omNs);
        value.addChild(fac.createOMText(value, "Hello World"));
        method.addChild(value);
        return method;
    }

    private static void loadTestAxis2Service() {
        final int concurrencyNumber = 100;
        final int numberOfIterations = 100;
        Thread[] ClientThread = new Thread[concurrencyNumber];
        for (int i = 0; i < concurrencyNumber; i++) {
            log.info("Thread " + "started");
            ClientThread[i] = new Thread() {
                public void run() {
                    for (int i = 0; i < numberOfIterations; i++) {
                        assertTrue("Load test on axis2service failed", axis2ServiceTest());
                    }
                }
            };
            ClientThread[i].start();
        }

        for (int i = 0; i < concurrencyNumber; i++) {
            try {
                ClientThread[i].join();
            } catch (InterruptedException e) {
                fail("Thread join operation interrupted");
                log.error("Thread join operation interrupted " + e.getMessage());
            }
        }
    }

    public static String[] deleteAllNotAdminServices(String sessionCookie, String serviceURL) {
        String[] appList = null;
        AdminServiceService adminServiceService;
        adminServiceService = new AdminServiceService(serviceURL);
        adminServiceService.deleteAllNonAdminServiceGroups(sessionCookie);
        return appList;
    }
}



