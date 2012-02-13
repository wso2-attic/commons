///*
// * Copyright 2004,2005 The Apache Software Foundation.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
package org.wso2.carbon.interop.mtom.soap11.utf8.sign;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfbase64Binary;
import junit.framework.TestCase;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.Constants;
import org.apache.axis2.description.PolicyInclude;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.rampart.policy.model.CryptoConfig;
import org.apache.rampart.policy.model.RampartConfig;
import org.apache.ws.security.components.crypto.Merlin;
//import org.tempuri.mtom.soap11.utf8.sign.MtomServiceStub;

import org.xmlsoap.ping.ByteArray;
import org.xmlsoap.ping.MtomTestStruct;

import javax.activation.DataHandler;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;


public class MtomServiceSoap11Utf8SignStubTest { /*extends TestCase {

    MtomServiceStub stub;

    public static final String AXIS2_REPOSITORY =
            "/home/amila/projects/interop/interop/microsoftInterop/mtom_client_wcf/repository";
    public static final String AXIS2_XML =
            "/home/amila/projects/interop/interop/microsoftInterop/mtom_client_wcf/conf/axis2.xml";

    Policy ramapConfigPolicy = null;

    protected void setUp() throws Exception {

        ConfigurationContext configurationContext =
                ConfigurationContextFactory.createConfigurationContextFromFileSystem(AXIS2_REPOSITORY, AXIS2_XML);
        stub = new MtomServiceStub(configurationContext,
                "http://32j1v71.b20.microsoftlab.net/MTOM_Service_Indigo/Soap11MtomUtf8SignOnly.svc/MtomTest");
        stub._getServiceClient().getOptions().setProperty(AddressingConstants.WS_ADDRESSING_VERSION,
                AddressingConstants.Submission.WSA_NAMESPACE);
        stub._getServiceClient().getOptions().setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        stub._getServiceClient().getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
        //engaing rampart
        stub._getServiceClient().engageModule("rampart");
        stub._getServiceClient().getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,
                "http://localhost:8085/MTOM_Service_Indigo/Soap11MtomUtf8SignOnly.svc/MtomTest");
        // set the rampart config properties correctly
        CryptoConfig signcriptoInfo = new CryptoConfig();
        signcriptoInfo.setProvider(Merlin.class.getName());
        Properties properties = new Properties();
        properties.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        properties.setProperty("org.apache.ws.security.crypto.merlin.file", "security_client_wcf/conf/sec.jks");
        properties.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", "password");
        signcriptoInfo.setProp(properties);

        CryptoConfig encriptcriptoInfo = new CryptoConfig();
        encriptcriptoInfo.setProp(properties);
        encriptcriptoInfo.setProvider(Merlin.class.getName());

        RampartConfig config = new RampartConfig();
        config.setUser("alice");
        config.setEncryptionUser("bob");
        config.setPwCbClass("util.PasswordCallbackHandler");
        config.setSigCryptoConfig(signcriptoInfo);
        config.setEncrCryptoConfig(encriptcriptoInfo);

        ramapConfigPolicy = new Policy();
        ramapConfigPolicy.addAssertion(config);
    }


    public void testEchoBinaryAsString() {

        stub._getServiceClient().getAxisService().getPolicyInclude().addPolicyElement(
                    PolicyInclude.ANON_POLICY, ramapConfigPolicy);
        String testString = "new test string";
        DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(testString.getBytes()));
        try {
            String returnStirng = stub.EchoBinaryAsString(dataHandler);
            assertEquals(returnStirng, "new test string");
        } catch (RemoteException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testEchoStringAsBinary() {
        String testString = "new test string";
        try {
            DataHandler dataHandler = stub.EchoStringAsBinary(testString);
            String result = getStringFromDataHandler(dataHandler);
            assertEquals(result, "new test string");
        } catch (RemoteException e) {
            fail();
        } catch (IOException e) {
            fail();
        }
    }

    public void testEchoBinaryArrayAsStringArray() {

        String testString = "new test string";
        DataHandler dataHandlers[] = new DataHandler[2];
        dataHandlers[0] = new DataHandler(new ByteArrayDataSource(testString.getBytes()));
        dataHandlers[1] = new DataHandler(new ByteArrayDataSource(testString.getBytes()));

        ArrayOfbase64Binary arrayOfbase64Binary = new ArrayOfbase64Binary();
        arrayOfbase64Binary.setBase64Binary(dataHandlers);

        try {
            String[] result = stub.EchoBinaryArrayAsStringArray(arrayOfbase64Binary).getString();
            assertEquals(result.length, 2);
            assertEquals(result[0], "new test string");
            assertEquals(result[1], "new test string");
        } catch (RemoteException e) {
            fail();
        }

    }

    public void testEchoBinaryFieldAsString() {

        String testString = "new test string";
        DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(testString.getBytes()));
        MtomTestStruct mtomTestStruct = new MtomTestStruct();
        mtomTestStruct.setName("test data");
        mtomTestStruct.setArray(dataHandler);
        try {
            String result = stub.EchoBinaryFieldAsString(mtomTestStruct);
            assertEquals(result, "new test string");
        } catch (RemoteException e) {
            fail();
        }
    }

    public void testEchoBinaryHeaderAsString() {

        String testString = "new test string";
        DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(testString.getBytes()));
        ByteArray byteArray = new ByteArray();
        byteArray.setByteArray(dataHandler);
        try {
            String result = stub.EchoBinaryHeaderAsString(byteArray);
            assertEquals(result, "new test string");
        } catch (RemoteException e) {
            fail();
        }

    }

    public String getStringFromDataHandler(DataHandler dataHandler)
            throws IOException {
        byte[] bytes = new byte[128];
        int length = dataHandler.getInputStream().read(bytes);
        return new String(bytes, 0, length);
    }
*/}
