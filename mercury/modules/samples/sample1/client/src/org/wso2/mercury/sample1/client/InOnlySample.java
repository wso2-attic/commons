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
package org.wso2.mercury.sample1.client;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.AxisFault;
import org.wso2.mercury.sample1.stub.SampleServiceStub;
import org.wso2.mercury.client.MercuryClient;
import org.wso2.mercury.util.MercuryClientConstants;


public class InOnlySample {

    public static final String AXIS2_CLIENT_CONFIG_FILE = "conf/axis2-client.xml";
    public static final String AXIS2_REPOSITORY_LOCATION = "repository";

    public void testPlain(boolean isDuplex){
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            SampleServiceStub sampleServiceStub =
                    new SampleServiceStub(configurationContext,
                            "http://localhost:8088/axis2/services/SampleService",
                            isDuplex);
            sampleServiceStub._getServiceClient().engageModule("addressing");
            sampleServiceStub.receiveInt(1);
        } catch (AxisFault e) {
            e.printStackTrace();
        } catch (java.rmi.RemoteException e) {
            e.printStackTrace();
        }
    }

    public void testRM(boolean isDuplex){
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            SampleServiceStub sampleServiceStub =
                    new SampleServiceStub(configurationContext,
                            "http://localhost:8088/axis2/services/SampleService",
                            isDuplex);
            sampleServiceStub._getServiceClient().engageModule("addressing");
            sampleServiceStub._getServiceClient().engageModule("Mercury");
            sampleServiceStub._getServiceClient().getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
            sampleServiceStub.receiveInt(1);
            MercuryClient mercuryClient = new MercuryClient(sampleServiceStub._getServiceClient());
            mercuryClient.terminateSequence("key1");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (AxisFault e) {
            e.printStackTrace();
        } catch (java.rmi.RemoteException e) {
            e.printStackTrace();
        }
    }

    public void testRMSequence(boolean isDuplex){
        try {

            ConfigurationContext configurationContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(
                            AXIS2_REPOSITORY_LOCATION, AXIS2_CLIENT_CONFIG_FILE);
            SampleServiceStub sampleServiceStub =
                    new SampleServiceStub(configurationContext,
                            "http://localhost:8088/axis2/services/SampleService",
                            isDuplex);
            sampleServiceStub._getServiceClient().engageModule("addressing");
            sampleServiceStub._getServiceClient().engageModule("Mercury");
            for (int i = 0; i < 10; i++) {
                sampleServiceStub._getServiceClient().getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key1");
                sampleServiceStub.receiveInt(i);
                sampleServiceStub._getServiceClient().getOptions().setProperty(MercuryClientConstants.INTERNAL_KEY, "key2");
                sampleServiceStub.receiveInt(i);
            }

            MercuryClient mercuryClient = new MercuryClient(sampleServiceStub._getServiceClient());
            mercuryClient.terminateSequence("key1");
            mercuryClient.terminateSequence("key2");

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (AxisFault e) {
            e.printStackTrace();
        } catch (java.rmi.RemoteException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        // annonymous mode
//        new InOnlySample().testPlain(false);
//        new InOnlySample().testRM(false);
//        new InOnlySample().testRMSequence(false);

        // duplex mode
//        new InOnlySample().testPlain(true);
//        new InOnlySample().testRM(true);
        new InOnlySample().testRMSequence(true);
    }
}
