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
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.AdminServiceAuthentication;
import org.wso2.carbon.admin.service.AdminServiceService;
import org.wso2.carbon.admin.service.AdminServiceStaticstics;
import org.wso2.carbon.admin.service.utils.FrameworkSettings;
import org.wso2.carbon.statistics.stub.types.carbon.SystemStatistics;
import org.wso2.carbon.system.test.core.TestTemplate;
import org.wso2.carbon.system.test.core.utils.TenantDetails;
import org.wso2.carbon.system.test.core.utils.TenantListCsvReader;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClient;
import org.wso2.carbon.system.test.core.utils.axis2Client.AxisServiceClientUtils;

import javax.xml.stream.XMLStreamException;
import java.rmi.RemoteException;

/**
 * Deploy a service from one tenant and invoke it multiple times.
 * Then check the system statistics for request, response and faults.
 */
public class StratosASSystemStatTest extends TestTemplate {
    private static final Log log = LogFactory.getLog(StratosASSystemStatTest.class);
    private static String AXIS2SERVICE_EPR;
    private static AdminServiceStaticstics adminServiceStatistics;

    @Override
    public void init() {
        String tenantId = "15";
        TenantDetails tenantDetails = TenantListCsvReader.getTenantDetails(TenantListCsvReader.getTenantId(tenantId));
        String sessionCookie = login(tenantDetails.getTenantName(), tenantDetails.getTenantPassword(),
                FrameworkSettings.APP_BACKEND_URL);
        adminServiceStatistics = new AdminServiceStaticstics(FrameworkSettings.APP_BACKEND_URL, sessionCookie);
        testClassName = StratosASSystemStatTest.class.getName() +1;
        AXIS2SERVICE_EPR = "http://" + FrameworkSettings.APP_SERVER_HOST_NAME + "/services/t/" +
                tenantDetails.getTenantDomain() + "/Axis2Service";
        try {
            log.debug("Delete already existing services");
//            deleteAllNotAdminServices(sessionCookie, FrameworkSettings.APP_BACKEND_URL);
            log.debug("Wait for service undeployment");
            AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR);
        } catch (AssertionFailedError e) {
            log.info("Service was already deleted");
        }
    }

    @Override
    public void runSuccessCase() {
        log.info("Running StratosASServiceStatTest...");
        String operation = "echoInt";
        String expectedValue = "123";
        int numberOfRequests = 100;

        log.info("Wait for service deployment");
        AxisServiceClientUtils.waitForServiceDeployment(AXIS2SERVICE_EPR); // wait for service deployment
        waitForOperation();

        //get stats of other tenant prior to invoke service.
        try {
            SystemStatistics systemStatisticsBeforeExecution = adminServiceStatistics.getSystemStatistics();

            //invoke service for 100 times
            for (int i = 0; i < numberOfRequests; i++) {
                OMElement result = new AxisServiceClient().sendReceive(createPayLoad(operation, expectedValue),
                        AXIS2SERVICE_EPR, operation);
                log.debug("Response for request " + i + " " + result);
                Assert.assertTrue((result.toString().indexOf(expectedValue) >= 1));
            }
            //get system stats again after 100 service runs   x
            SystemStatistics systemStatisticsAfterExecution = adminServiceStatistics.getSystemStatistics();


            log.debug("Request count is incorrect : " + systemStatisticsAfterExecution.getRequestCount());
            log.debug("Request count before execution: " + systemStatisticsBeforeExecution.getRequestCount());
            assertEquals("Request count is incorrect  ",
                    (getStatDifference(systemStatisticsAfterExecution.getRequestCount(),
                            systemStatisticsBeforeExecution.getRequestCount())), numberOfRequests);
            log.info("Request count verification passed");

            log.debug("Response count after execution: " + systemStatisticsAfterExecution.getResponseCount());
            log.debug("Response count before execution: " + systemStatisticsBeforeExecution.getResponseCount());
            assertEquals("Response count is incorrect ",
                    (getStatDifference(systemStatisticsAfterExecution.getResponseCount(),
                            systemStatisticsBeforeExecution.getResponseCount())), numberOfRequests);
            log.info("Response count verification passed");

            log.debug("Fault count after execution" + systemStatisticsAfterExecution.getFaultCount());
            log.debug("Fault count after execution " + systemStatisticsBeforeExecution.getFaultCount());
            assertEquals("Fault count incorrect ",
                    (getStatDifference(systemStatisticsAfterExecution.getFaultCount(),
                            systemStatisticsBeforeExecution.getFaultCount())), 0);
            log.info("Fault count verification passed");

            //introducing faults and verity fault count
            String invalidIntNumber = "test";
            systemStatisticsBeforeExecution = adminServiceStatistics.getSystemStatistics();
            
            EndpointReference epr = new EndpointReference(AXIS2SERVICE_EPR);
            for (int i = 0; i < numberOfRequests; i++) {
                try {
                    OMElement result = AxisServiceClientUtils.sendRequest(createPayLoad(operation, invalidIntNumber).toString(), epr);
                    log.debug("Response for request " + i + " " + result);
                    Assert.assertTrue((result.toString().indexOf("Fault") >= 1));
                } catch (AssertionFailedError e) {
                    log.debug("Faulty service invocation" + e.getMessage());
                } catch (XMLStreamException e) {
                    log.debug("XML Stream parser error" + e.getMessage());
                } catch (AxisFault axisFault) {
                    log.debug(axisFault.getMessage());
                }
            }
            systemStatisticsAfterExecution = adminServiceStatistics.getSystemStatistics();

            log.debug("Fault count after execution " + systemStatisticsBeforeExecution.getFaultCount());
            assertEquals("Fault count incorrect ",
                    (getStatDifference(systemStatisticsAfterExecution.getFaultCount(),
                            systemStatisticsBeforeExecution.getFaultCount())), numberOfRequests);
            log.info("Fault count verification passed");
        } catch (RemoteException e) {
            log.error("Fail to get system stats " + e.getMessage());
            fail("Fail to get system stats " + e.getMessage());
        }
    }

    @Override
    public void cleanup() {
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

    private int getStatDifference(int after, int before) {
        return after - before;
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

    private void waitForOperation() {
        try {
            Thread.sleep(FrameworkSettings.SERVICE_DEPLOYMENT_DELAY);
        } catch (InterruptedException ignored) {
        }
    }
}
