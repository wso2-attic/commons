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

package org.wso2.carbon.interop.mtom.soap11.utf8;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tempuri.mtom.soap11.utf8.MtomServiceStub;
import org.apache.axis2.Constants;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.xmlsoap.ping.MtomTestStruct;
import org.xmlsoap.ping.ByteArray;

import javax.activation.DataHandler;
import java.rmi.RemoteException;
import java.io.IOException;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfbase64Binary;
import junit.framework.TestCase;

/**
* Author: amila
* Date: Jul 4, 2007
*/
public class MtomServiceSoap11Utf8StubTest extends TestCase {

    private static final Log log = LogFactory.getLog(MtomServiceSoap11Utf8StubTest.class);

    MtomServiceStub stub = null;

    protected void setUp() throws Exception {
        stub = new MtomServiceStub();
        stub._getServiceClient().getOptions().setProperty(Constants.Configuration.ENABLE_MTOM,
                Constants.VALUE_TRUE);
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
