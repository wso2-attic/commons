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
package org.wso2.carbon.sequences;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminStub;
import org.wso2.carbon.test.framework.util.client.StockQuoteClient;
import org.wso2.carbon.test.framework.util.server.SampleAxis2Server;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 17, 2010
 * Time: 11:03:49 AM
 * Testing Test Sample 50
 */
public class Sample50Test extends SequenceTestCase {

    private String epServiceURL = "https://localhost:9443/services/EndpointAdmin";
    EndpointAdminStub endpointAdminStub;

    @Before
    //Stating Sample Axis2Server
    public void startAxis2Server() throws Exception {
        SampleAxis2Server.build("SimpleStockQuoteService");
        SampleAxis2Server.Start();
    }

    @Before
    //Initialising EndpointAdminStub
    public void endpointInit() throws AxisFault {

        Assert.assertTrue(sessionCookie.contains("JSESSIONID="));
        endpointAdminStub = new EndpointAdminStub(epServiceURL);
        ServiceClient client = endpointAdminStub._getServiceClient();
        Options option = client.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
    }

    @Test
    public void test50() throws Exception {
        if (endpointAdminStub.getEndPointsNames() != null) {
            List epList = Arrays.asList(endpointAdminStub.getEndPointsNames());
            if (epList.contains("Ep50")) {
                endpointAdminStub.deleteEndpoint("Ep50");
            }
        }
        String Ep50 = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"Ep50\">" +
                " <address uri=\"http://localhost:9000/services/SimpleStockQuoteService\" format=\"soap11\"/>" +
                "</endpoint>";

        endpointAdminStub.addEndpoint(Ep50);
        String seq50 = "<sequence xmlns=\"http://ws.apache.org/ns/synapse\" name=\"main\">" +
                " <filter source=\"get-property('To')\" regex=\".*/StockQuote.*\">\n" +
                "        <send>\n" +
                "  <endpoint key=\"Ep50\" />" +
                "        </send>\n" +
                "        <drop/>\n" +
                "    </filter>\n" +
                "    <send/>" +
                "</sequence>";
        sequenceAdminServiceStub.deleteSequence("main");
        OMElement documentElement = AXIOMUtil.stringToOM(seq50);
        sequenceAdminServiceStub.addSequence(documentElement);

        // -Dtrpurl=http://localhost:8280/services/StockQuote -Drest=true
        System.setProperty("repository", AXIS2C_HOME + File.separator + "client_repo");
        System.setProperty("rest", "true");
        System.setProperty("trpurl", "http://localhost:8280/services/StockQuote");

        String resultString = getStringResultOfTest(StockQuoteClient.executeTestClient());
        Assert.assertTrue(resultString.contains(":last>"));
        Assert.assertTrue(resultString.contains("name>IBM Company<"));
        if (endpointAdminStub.getEndPointsNames() != null) {
            List epList = Arrays.asList(endpointAdminStub.getEndPointsNames());
            if (epList.contains("Ep50")) {
                endpointAdminStub.deleteEndpoint("Ep50");
            }
        }
    }

    @After
    //Stopping Sample Axis2Server
    public void stopAxis2Server() throws Exception {

        SampleAxis2Server.Stop();
    }

}
