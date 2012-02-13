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
import org.apache.axis2.context.MessageContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminException;
import org.wso2.carbon.endpoint.ui.types.EndpointAdminStub;
import org.wso2.carbon.sequences.ui.types.SequenceEditorException;
import org.wso2.carbon.test.framework.util.server.SampleAxis2Server;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.getProperty;
import static org.wso2.carbon.test.framework.util.client.MTOMSwAClient.sendUsingMTOM;
import static org.wso2.carbon.test.framework.util.client.MTOMSwAClient.sendUsingSwA;

/**
 * Created by IntelliJ IDEA.
 * User: suho
 * Date: Jun 17, 2010
 * Time: 11:03:49 AM
 * Testing Sample 51
 */
public class Sample51Test extends SequenceTestCase {

    private String epServiceURL = "https://localhost:9443/services/EndpointAdmin";
    EndpointAdminStub endpointAdminStub;

    @Before
    public void startAxis2Server() throws Exception {

        SampleAxis2Server.build("MTOMSwASampleService");
        SampleAxis2Server.Start();
        endpointInit();
        init();
    }


    private void endpointInit() throws AxisFault {

        Assert.assertTrue(sessionCookie.contains("JSESSIONID="));
        endpointAdminStub = new EndpointAdminStub(epServiceURL);
        ServiceClient client = endpointAdminStub._getServiceClient();
        Options option = client.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, sessionCookie);
    }

    private void init() throws SequenceEditorException, RemoteException, XMLStreamException, EndpointAdminException {
        cleanEndpoints();
        String Ep51_1 = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"Ep51_1\">" +
                "<address uri=\"http://localhost:9000/services/MTOMSwASampleService\" optimize=\"mtom\"/>\n" +
                "</endpoint>";
        String Ep51_2 = "<endpoint xmlns=\"http://ws.apache.org/ns/synapse\" name=\"Ep51_2\">" +
                " <address uri=\"http://localhost:9000/services/MTOMSwASampleService\" optimize=\"swa\"/>\n" +
                "</endpoint>";

        endpointAdminStub.addEndpoint(Ep51_1);
        endpointAdminStub.addEndpoint(Ep51_2);
        String seq50 = "<sequence xmlns=\"http://ws.apache.org/ns/synapse\" name=\"main\">" +
                "<in>\n" +
                "        <filter source=\"get-property('Action')\" regex=\"urn:uploadFileUsingMTOM\">\n" +
                "            <property name=\"example\" value=\"mtom\"/>\n" +
                "            <send>\n" +
                "  <endpoint key=\"Ep51_1\" />" +
                "            </send>\n" +
                "        </filter>\n" +
                "        <filter source=\"get-property('Action')\" regex=\"urn:uploadFileUsingSwA\">\n" +
                "            <property name=\"example\" value=\"swa\"/>\n" +
                "            <send>\n" +
                "  <endpoint key=\"Ep51_2\" />" +
                "            </send>\n" +
                "        </filter>\n" +
                "    </in>\n" +
                "    <out>\n" +
                "        <filter source=\"get-property('example')\" regex=\"mtom\">\n" +
                "            <property name=\"enableMTOM\" value=\"true\" scope=\"axis2\"/>\n" +
                "        </filter>\n" +
                "        <filter source=\"get-property('example')\" regex=\"swa\">\n" +
                "            <property name=\"enableSwA\" value=\"true\" scope=\"axis2\"/>\n" +
                "        </filter>\n" +
                "        <send/>\n" +
                "    </out>" +
                "</sequence>";
        sequenceAdminServiceStub.deleteSequence("main");
        OMElement documentElement = AXIOMUtil.stringToOM(seq50);
        sequenceAdminServiceStub.addSequence(documentElement);

    }

    @Test
    //Testing MTOM
    public void test51MTOM() throws Exception {

        // -Dopt_mode=mtom
        System.setProperty("repository", AXIS2C_HOME + File.separator + "client_repo");

        String targetEPR = getProperty("opt_url", "http://localhost:8280/services/MTOMSwASampleService");
        String fileName = getProperty("opt_file", ESB_HOME + "/repository/samples/resources/mtom/asf-logo.gif");
        String mode = getProperty("opt_mode", "mtom");

        if ("mtom".equals(mode)) {

            Assert.assertTrue(getStringResultOfTest(sendUsingMTOM(fileName, targetEPR)).contains("<m0:uploadFileUsingMTOMResponse xmlns:m0=\"http://services.samples\"><m0:response><m0:image>R0lGODlhgwFkAPcAAPfrfmZmZpmZmczM"));
        }


    }

    @Test
    //Testing SWA
    public void test51SWA() throws Exception {

        // -Dopt_mode=swa
        System.setProperty("repository", AXIS2C_HOME + File.separator + "client_repo");


        String targetEPR = getProperty("opt_url", "http://localhost:8280/services/MTOMSwASampleService");
        String fileName = getProperty("opt_file", ESB_HOME + "/repository/samples/resources/mtom/asf-logo.gif");
        String mode = getProperty("opt_mode", "swa");
        if ("swa".equals(mode)) {
            MessageContext mc = sendUsingSwA(fileName, targetEPR);
            System.out.println(mc);
            Assert.assertEquals(mc.getProperty("IsAddressingProcessed"), true);
            Assert.assertEquals(mc.getProperty("transport.http.statusCode"), 200);
        }

    }

    @After
    public void stopAxis2Server() throws Exception {
        cleanEndpoints();
        SampleAxis2Server.Stop();

    }

    private void cleanEndpoints() throws EndpointAdminException, RemoteException {
        if (endpointAdminStub.getEndPointsNames() != null) {
            List epList = Arrays.asList(endpointAdminStub.getEndPointsNames());
            if (epList.contains("Ep51_1")) {
                endpointAdminStub.deleteEndpoint("Ep51_1");
            }
            if (epList.contains("Ep51_2")) {
                endpointAdminStub.deleteEndpoint("Ep51_2");
            }
        }
    }

}
