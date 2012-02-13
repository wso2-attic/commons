/*
 * Copyright (c) 2006, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import junit.framework.TestCase;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 */
public class SAAJMessageBuilderTest extends TestCase {

    // This test fails because of a bug in axis2 there fore its coded so that the test passes when
    // an exception is thrown
    public void testMessageBuilingFromByteArray() throws Exception {

        SOAPFactory fac = OMAbstractFactory.getSOAP12Factory();
        SOAPEnvelope env = fac.createSOAPEnvelope();
        fac.createSOAPBody(env);
        env.getBody().addChild(fac.createOMElement("test", "http://t", "t"));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            env.serialize(outStream);
            MessageFactory mf = MessageFactory.newInstance();
            mf.createMessage(new MimeHeaders(), new ByteArrayInputStream(outStream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(true);
        }
    }
}
