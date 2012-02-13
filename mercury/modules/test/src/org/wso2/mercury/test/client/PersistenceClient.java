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
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMNamespace;
import org.wso2.mercury.util.MercuryClientConstants;

public class PersistenceClient {

    public static final String AXIS2_CLIENT_CONFIG_FILE = "conf/axis2-client.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository_client";

    public void testRMTestInService() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInService"));
            serviceClient.getOptions().setAction("urn:TestInOperation");
            serviceClient.engageModule("Mercury");
            serviceClient.engageModule("MessageDropModule");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
            for (int i = 1; i < 5; i++) {
                serviceClient.fireAndForget(getTestOMElement(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.fireAndForget(getTestOMElement(11));

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void resumeTestInService() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
//            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
//            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInService"));
//            serviceClient.getOptions().setAction("urn:TestInOperation");
//            serviceClient.engageModule("Mercury");
//            serviceClient.getOptions().setUseSeparateListener(true);
//            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
//            serviceClient.getOptions().setProperty(MercuryClientConstants.RESUME_SEQUENCE, Constants.VALUE_TRUE);
//            serviceClient.fireAndForget(getTestOMElement(301));
//
//            try {
//                System.out.println("Waiting thread to sleep");
//                Thread.sleep(200000);
//            } catch (InterruptedException e) {
//
//            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

     public void testRMTestInoutService() {

        try {
            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            ServiceClient serviceClient = new ServiceClient(configurationContext, null);
            serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInOutService"));
            serviceClient.getOptions().setAction("urn:TestInOutOperation");
            serviceClient.engageModule("Mercury");
//            serviceClient.engageModule("MessageDropModule");
            serviceClient.getOptions().setUseSeparateListener(true);
            serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
            serviceClient.getOptions().setProperty(MercuryClientConstants.SEQUENCE_OFFER, Constants.VALUE_TRUE);
            for (int i = 1; i < 100; i++) {
                serviceClient.sendReceiveNonBlocking(getTestOMElement(i), new PersistanceCallback());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            serviceClient.getOptions().setProperty(MercuryClientConstants.LAST_MESSAGE, Constants.VALUE_TRUE);
            serviceClient.sendReceiveNonBlocking(getTestOMElement(11), new PersistanceCallback());

            try {
                System.out.println("Waiting thread to sleep");
                Thread.sleep(200000);
            } catch (InterruptedException e) {

            }

        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        }
    }

    public void resumeTestInoutService() {

            try {
                ConfigurationContext configurationContext =
                        ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                                AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
                ServiceClient serviceClient = new ServiceClient(configurationContext, null);

//                serviceClient.setTargetEPR(new EndpointReference("http://localhost:8088/axis2/services/TestInOutService"));
//                serviceClient.getOptions().setAction("urn:TestInOutOperation");
//                serviceClient.engageModule("Mercury");
//                serviceClient.getOptions().setUseSeparateListener(true);
//                serviceClient.getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
//                serviceClient.getOptions().setProperty(MercuryClientConstants.RESUME_SEQUENCE, Constants.VALUE_TRUE);
//                serviceClient.sendReceiveNonBlocking(getTestOMElement(301), new PersistanceCallback());

                try {
                    System.out.println("Waiting thread to sleep");
                    Thread.sleep(200000);
                } catch (InterruptedException e) {

                }

            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();
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
//        new PersistenceClient().testRMTestInService();
//        new PersistenceClient().resumeTestInService();
        new PersistenceClient().testRMTestInoutService();
//        new PersistenceClient().resumeTestInoutService();
    }

}
