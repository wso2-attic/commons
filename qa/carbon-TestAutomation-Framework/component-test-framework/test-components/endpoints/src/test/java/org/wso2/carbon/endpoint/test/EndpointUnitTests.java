/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

package org.wso2.carbon.endpoint.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.common.test.utils.TestTemplate;
import org.wso2.carbon.endpoint.test.commands.*;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminStub;


public class EndpointUnitTests extends TestTemplate {
    private static final Log log = LogFactory.getLog(ESBEndpointAddAndRemoveTest.class);
    EndpointAdminStub endpointAdminStub;

    @Override
    public void init() {
        log.info("Running EndpointUnitTests ");
    }

    @Override
    public void runSuccessCase() {
        log.debug("Running EndpointUnitTests SuccessCase ");
        endpointAdminStub = new InitializeEndpointAdminCommand().executeAdminStub(sessionCookie);
        addWrongXMLTest();
        removeWrongNameTest();
        log.info("EndpointUnitTests SuccessCase Passed");
    }

    @Override
    public void runFailureCase() {
        log.debug("Running EndpointUnitTests FailureCase ");
        runSuccessCase();
        log.info("EndpointUnitTests FailureCase Passed");
    }

    @Override
    public void cleanup() {

    }

    //  A test with a wrong xml

    private void addWrongXMLTest() {
        String aWrongEndpointXML = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"Ep50\">\n" +
                "    </address uri=\"http://localhost:9000/services/SimpleStockQuoteService\" format=\"soap11\"/>\n" +
                "</address>\n" +
                "</endpoint>";
        new EndpointAdminCommand(endpointAdminStub).addEndpointExecuteFailureCase(aWrongEndpointXML);
    }


    //  A test to delete a non existing Endpoint

    public void removeWrongNameTest() {
        String endpointName = "aWrongName";
        new EndpointAdminCommand(endpointAdminStub).deleteEndpointExecuteFailureCase(endpointName);
    }


}
