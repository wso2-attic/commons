/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.endpoint.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.ConfigHelper;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.endpoint.test.commands.*;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminStub;

import java.util.Arrays;
import java.util.List;

public class ESBEndpointAddAndRemoveTest extends TestTemplate {

    private static final Log log = LogFactory.getLog(ESBEndpointAddAndRemoveTest.class);
    //  private int EpNumber;
    private String epName;
    //  private String addressUrl;
    private String endpointXML;


    @Override
    public void init() {
        log.info("Running ESBEndpointAddAndRemoveTest ");
        endpointXML = ConfigHelper.getXMLConfig("/config/TestSuites/Endpoints/ESBEndpointAddAndRemoveTest/endpoint.xml");
        epName = ConfigHelper.readXML("name", endpointXML);
        //  addressUrl = ConfigHelper.readXML("address uri", endpointXML);
        log.debug("Test Initialised");

    }

    @Override
    public void runSuccessCase() {
        log.debug("Running ESBEndpointAddAndRemoveTest SuccessCase ");
        try {

            EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
            List endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());
            if (endpointList.contains(epName)) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase(epName);
            }
            Thread.sleep(4000);
            int endpointCount = new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase();
            new EndpointAdminCommand(endpointAdminStub).addEndpointExecuteSuccessCase(endpointXML);
            Thread.sleep(4000);
            assertEquals(endpointCount + 1, new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase());

            endpointList = Arrays.asList(new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteSuccessCase());
            if (endpointList.contains(epName)) {
                new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteSuccessCase(epName);
            }
            Thread.sleep(4000);
            assertEquals(endpointCount, new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteSuccessCase());
            log.info("ESBEndpointAddAndRemoveTest SuccessCase Passed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runFailureCase() {
        log.debug("Running ESBEndpointAddAndRemoveTest FailureCase ");
        EndpointAdminStub endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
        new EndpointAdminCommand(endpointAdminStub).getEndpointsNamesExecuteFailureCase();
        new EndpointAdminCommand(endpointAdminStub).getEndpointCountExecuteFailureCase();
        new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteFailureCase(epName);
        new EndpointAdminCommand(endpointAdminStub).addEndpointExecuteFailureCase(endpointXML);
        log.info("ESBEndpointAddAndRemoveTest FailureCase Passed");
    }

    @Override
    public void cleanup() {
        loadDefaultConfig();
     }
}
