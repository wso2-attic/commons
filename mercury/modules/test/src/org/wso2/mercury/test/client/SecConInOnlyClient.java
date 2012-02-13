/*
 * Copyright 2004,2005 The Apache Software Foundation.
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
package org.wso2.mercury.test.client;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.description.PolicyInclude;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.wso2.mercury.util.MercuryClientConstants;
import org.wso2.mercury.client.MercuryClient;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;


public class SecConInOnlyClient {

    public static final String AXIS2_CLIENT_CONFIG_FILE = "conf/axis2-client.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository";

    private void testSecConService() {
        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestSecConInService"));
            serviceClient.getOptions().setAction("urn:TestInOperation");

            serviceClient.engageModule("Mercury");

            serviceClient.engageModule("rampart");
            StAXOMBuilder builder = new StAXOMBuilder("conf/secconpolicy.xml");
            Policy policy = PolicyEngine.getPolicy(builder.getDocumentElement());
            serviceClient.getAxisService().getPolicySubject().attachPolicy(policy);

            MercuryClient mercuryClient = new MercuryClient(serviceClient);
            mercuryClient.setRMSSequenceRetransmitTime(20000);

            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.getOptions().setProperty(MercuryClientConstants.USE_SECURE_RM, Constants.VALUE_TRUE);
            serviceClient.fireAndForget(getTestOMElement(1));

            try {
                Thread.sleep(200000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private OMElement getTestOMElement(long number) {
        OMFactory omFactory = OMAbstractFactory.getOMFactory();
        OMNamespace omNamespace = omFactory.createOMNamespace("http://wso2.org/temp1", "ns1");
        OMElement omElement = omFactory.createOMElement("TestElement", omNamespace);
        omElement.setText("org element " + number);
        return omElement;
    }

    public static void main(String[] args) {
        new SecConInOnlyClient().testSecConService();
    }
}
