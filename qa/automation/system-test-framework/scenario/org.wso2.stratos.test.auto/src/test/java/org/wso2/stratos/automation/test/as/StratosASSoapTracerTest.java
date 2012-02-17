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
package org.wso2.stratos.automation.test.as;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.AdminServiceTracerAdmin;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClientUtils;
import org.wso2.carbon.tracer.stub.types.carbon.MessagePayload;
import org.wso2.carbon.tracer.stub.types.carbon.TracerServiceInfo;

/*
enable soap tracing in one tenant and invoke services in a different tenant.
Then test the message tracing to see whether soap tracing is multi-tenanted correctly
 */
public class StratosASSoapTracerTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(StratosASSoapTracerTest.class);
    private static String AXIS2SERVICE_EPR;
    private static AdminServiceTracerAdmin firstTenantSoupTrackerAdmin;
    private static AdminServiceTracerAdmin secondTenantSoupTrackerAdmin;

    @Override
    public void init() {
        log.info("Running StratosASSoapTracerTest for testing soap tracker multitenancy...");
        String tenantIdOfFirstTenant = "15";
        String tenantIdOfSecondTenant = "12";

        //get tenant1 info
        TenantDetails fistTenantInfo = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId
                (tenantIdOfFirstTenant));

        //get tenant2 info
        TenantDetails secondTenantInfo = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId
                (tenantIdOfSecondTenant));

        String sessionCookieOfFirstTenant = login(fistTenantInfo.getTenantName(), fistTenantInfo.getTenantPassword(),
                                                  FrameworkSettings.APP_BACKEND_URL);

        String sessionCookieOfSecondTenant = login(secondTenantInfo.getTenantName(), secondTenantInfo.getTenantPassword(),
                                                   FrameworkSettings.APP_BACKEND_URL);

        firstTenantSoupTrackerAdmin = new AdminServiceTracerAdmin(FrameworkSettings.APP_BACKEND_URL,
                                                                  sessionCookieOfFirstTenant);
        secondTenantSoupTrackerAdmin = new AdminServiceTracerAdmin(FrameworkSettings.APP_BACKEND_URL,
                                                                   sessionCookieOfSecondTenant);

        testClassName = StratosASSoapTracerTest.class.getName();
        AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" +
                           fistTenantInfo.getTenantDomain() + "/Axis2Service";

        //delete the service if it is already exists
        try {
            log.debug("Delete already existing services");
            deleteAllNotAdminServices(sessionCookieOfFirstTenant, FrameworkSettings.APP_BACKEND_URL);
            log.debug("Wait for service undeployment");
            AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR);
        } catch (AssertionFailedError e) {
            log.info("Service was already deleted");
        }
    }

    @Override
    public void runSuccessCase() {
        String soapTrackerONFlag = "ON";
        String soapTrackerOFFFlag = "OFF";
        String operation = "echoInt";
        String expectedValue = "1234556";
        int noOfMessagesToRetrieve = 200;

        TracerServiceInfo firstTenantSoapTracerServiceInfo;
        TracerServiceInfo secondTenantSoapTracerServiceInfo;

        firstTenantSoapTracerServiceInfo = firstTenantSoupTrackerAdmin.setMonitoring("ON"); //Enable soap tracer of first tenant
        secondTenantSoapTracerServiceInfo = secondTenantSoupTrackerAdmin.setMonitoring("ON"); //Enable soap tracer of second tenant
        waitForOperation();
        assertEquals("Soap tracer ON flag not set", soapTrackerONFlag, firstTenantSoapTracerServiceInfo.getFlag());
        assertEquals("Soap tracer ON flag not set", soapTrackerONFlag, secondTenantSoapTracerServiceInfo.getFlag());

        log.info("Wait for service deployment");
        AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR); // wait for service deployment
        waitForOperation();

        OMElement result = new AxisServiceClient().sendReceive(createPayLoad(operation, expectedValue),
                                                               AXIS2SERVICE_EPR, operation);
        log.debug("Response for request " + result);
        Assert.assertTrue((result.toString().indexOf(expectedValue) >= 1));

        MessagePayload messagePayload;
        //get message by giving operation name as the filter.
        firstTenantSoapTracerServiceInfo = firstTenantSoupTrackerAdmin.getMessages(noOfMessagesToRetrieve, operation);
        secondTenantSoapTracerServiceInfo = secondTenantSoupTrackerAdmin.getMessages(noOfMessagesToRetrieve, operation);

        messagePayload = firstTenantSoapTracerServiceInfo.getLastMessage();
        Assert.assertTrue((messagePayload.getRequest().indexOf(expectedValue) >= 1));
        Assert.assertTrue((messagePayload.getResponse().indexOf(expectedValue) >= 1));
        log.info("Soap traser message assertion passed");
        log.debug("Request Payload" + messagePayload.getRequest());
        log.debug("Response Payload" + messagePayload.getResponse());

        messagePayload = secondTenantSoapTracerServiceInfo.getLastMessage();

        assertNull("Message found in second tenant soap tracer ", secondTenantSoapTracerServiceInfo.getLastMessage());
        log.info("No Messages found in secound tenant soap tracer");

        //if last message exists then check message body for result value.
        if (secondTenantSoapTracerServiceInfo.getLastMessage() != null) {
            log.debug("secondTenantSoapTracerServiceInfo is not null");
            Assert.assertFalse((messagePayload.getRequest().indexOf(expectedValue) >= 1));
            Assert.assertFalse((messagePayload.getResponse().indexOf(expectedValue) >= 1));
            log.info("Messages do not contain expected value, hence test passed");
        }

        firstTenantSoapTracerServiceInfo = firstTenantSoupTrackerAdmin.setMonitoring(soapTrackerOFFFlag);
        assertEquals("Soap tracer OFF flag not set", soapTrackerOFFFlag, firstTenantSoapTracerServiceInfo.getFlag());

        secondTenantSoapTracerServiceInfo = secondTenantSoupTrackerAdmin.setMonitoring(soapTrackerOFFFlag);
        assertEquals("Soap tracer OFF flag not set", soapTrackerOFFFlag, secondTenantSoapTracerServiceInfo.getFlag());
    }

    private void waitForOperation() {
        try {
            Thread.sleep(FrameworkSettings.SERVICE_DEPLOYMENT_DELAY);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void cleanup() {

    }

    protected static String login(String userName, String password, String hostName) {
        AdminServiceAuthentication loginClient = new AdminServiceAuthentication(hostName);
        return loginClient.login(userName, password, hostName);
    }

    public static String[] deleteAllNotAdminServices(String sessionCookie, String serviceURL) {
        String[] appList = null;
        AdminServiceService adminServiceService;
        adminServiceService = new AdminServiceService(serviceURL);
        adminServiceService.deleteAllNonAdminServiceGroups(sessionCookie);
        return appList;
    }

    private static OMElement createPayLoad(String operation, String expectedValue) {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://service.carbon.wso2.org", "ns1");
        OMElement method = fac.createOMElement(operation, omNs);
        OMElement value = fac.createOMElement("x", omNs);
        value.addChild(fac.createOMText(value, expectedValue));
        method.addChild(value);
        log.debug("Created payload is :" + method);
        return method;
    }


}
