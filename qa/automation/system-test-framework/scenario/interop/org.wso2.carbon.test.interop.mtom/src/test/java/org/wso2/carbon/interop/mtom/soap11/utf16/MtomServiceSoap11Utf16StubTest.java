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
package org.wso2.carbon.interop.mtom.soap11.utf16;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfbase64Binary;
import junit.framework.TestCase;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tempuri.mtom.soap11.utf16.MtomServiceStub;
import org.xmlsoap.ping.ByteArray;
import org.xmlsoap.ping.MtomTestStruct;

import javax.activation.DataHandler;
import java.io.IOException;
import java.rmi.RemoteException;


public class MtomServiceSoap11Utf16StubTest extends TestCase {

    private static final Log log = LogFactory.getLog(MtomServiceSoap11Utf16StubTest.class);

    MtomServiceStub stub = null;

    protected void setUp() throws Exception {
        stub = new MtomServiceStub();
        stub._getServiceClient().getOptions().setProperty(
                Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
//        stub._getServiceClient().getOptions().setProperty(Constants.Configuration.CHARACTER_SET_ENCODING,"utf-16");
        stub._getServiceClient().getOptions().setProperty(
                Constants.Configuration.CHARACTER_SET_ENCODING, "UTF-16LE");

//        stub._getServiceClient().getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,
//                "http://localhost:8085/MTOM_Service_Indigo/Soap11MtomUtf16.svc/MtomTest");
//        stub._getServiceClient().getOptions().setProperty(Constants.Configuration.TRANSPORT_URL,
//                "http://localhost:8085/MTOM_Service_Indigo/Soap11MtomUtf16.svc/MtomTest");
    }

    public void testEchoBinaryAsString() {

        String testString = "new test string";
        DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(testString.getBytes()));
        try {
            String returnStirng = stub.echoBinaryAsString(dataHandler);
            assertEquals(returnStirng, "new test string");
        } catch (RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }
    }

    public void testEchoStringAsBinary() {
        String testString = "new test string";
        try {
            DataHandler dataHandler = stub.echoStringAsBinary(testString);
            String result = getStringFromDataHandler(dataHandler);
            assertEquals(result,"new test string");
        } catch (RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
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
            String[] result = stub.echoBinaryArrayAsStringArray(arrayOfbase64Binary).getString();
            assertEquals(result.length,2);
            assertEquals(result[0],"new test string");
            assertEquals(result[1],"new test string");
        } catch (RemoteException e) {
            log.error(e.getMessage(), e);
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
            String result = stub.echoBinaryFieldAsString(mtomTestStruct);
            assertEquals(result,"new test string");
        } catch (RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }
    }

    public void testEchoBinaryHeaderAsString() {

        String testString = "new test string";
        DataHandler dataHandler = new DataHandler(new ByteArrayDataSource(testString.getBytes()));
        ByteArray byteArray = new ByteArray();
        byteArray.setByteArray(dataHandler);
        try {
            String result = stub.echoBinaryHeaderAsString(byteArray);
            assertEquals(result,"new test string");
        } catch (RemoteException e) {
            log.error(e.getMessage(), e);
            fail();
        }

    }

    public String getStringFromDataHandler(DataHandler dataHandler)
            throws IOException {
        byte[] bytes = new byte[128];
        int length = dataHandler.getInputStream().read(bytes);
        return new String(bytes, 0, length);
    }

}
