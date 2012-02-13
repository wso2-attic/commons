/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
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
package org.wso2.mercury.message;

import junit.framework.TestCase;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.util.XMLUtils;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;

/**
 *
 */
public class CreateSequenceResponseMessageTest extends TestCase {

    public void testCreateSequenceResponseMessage1(){
         CreateSequenceResponseMessage createSequenceResponseMessage =
                 new CreateSequenceResponseMessage(MercuryConstants.RM_1_0_NAMESPACE,
                         SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        createSequenceResponseMessage.setIdentifier("testIdentifier");
        try {
            SOAPEnvelope soapEnvelope = createSequenceResponseMessage.toSOAPEnvelope();
            System.out.println("SOAP Envelope ==> " + soapEnvelope.toString());
            CreateSequenceResponseMessage result = CreateSequenceResponseMessage.fromSOAPEnvolope(soapEnvelope);
            assertEquals(result.getRmNamespace(),MercuryConstants.RM_1_0_NAMESPACE);
            assertEquals(result.getSoapNamesapce(),SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            assertEquals(result.getIdentifier(),"testIdentifier");
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }

    public void testCreateSequenceResponseMessage2(){
         CreateSequenceResponseMessage createSequenceResponseMessage =
                 new CreateSequenceResponseMessage(MercuryConstants.RM_1_0_NAMESPACE,
                         SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        createSequenceResponseMessage.setIdentifier("testIdentifier");
        Accept accept = new Accept(MercuryConstants.RM_1_0_NAMESPACE);
        accept.setAcceptERP("http://localhost:8080/test");
        createSequenceResponseMessage.setAccept(accept);

        try {
            SOAPEnvelope soapEnvelope = createSequenceResponseMessage.toSOAPEnvelope();
            System.out.println("SOAP Envelope ==> " + soapEnvelope.toString());
            CreateSequenceResponseMessage result = CreateSequenceResponseMessage.fromSOAPEnvolope(soapEnvelope);
            assertEquals(result.getRmNamespace(),MercuryConstants.RM_1_0_NAMESPACE);
            assertEquals(result.getSoapNamesapce(),SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            assertEquals(result.getIdentifier(),"testIdentifier");
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }
    
}
