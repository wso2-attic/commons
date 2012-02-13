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
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.om.OMAbstractFactory;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

public class RMApplicationMessageTest extends TestCase {

    public void testRMApplicationMessage(){
        SOAPFactory soapFactory = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();
        RMApplicationMessage rmApplicationMessage = new RMApplicationMessage(soapEnvelope);
        Sequence sequence = new Sequence();
        sequence.setSequenceID("testID");
        sequence.setLastMessage(true);
        sequence.setMessageNumber(2);
        rmApplicationMessage.setSequence(sequence);
        try {
            soapEnvelope = rmApplicationMessage.toSOAPEnvelope();
            System.out.println("SOAP Envolope ==> " + soapEnvelope.toString());
            RMApplicationMessage result = RMApplicationMessage.fromSOAPEnvelope(soapEnvelope);
            assertEquals(result.getSequence().getSequenceID(),"testID");
            assertEquals(result.getSequence().isLastMessage(),true);
            assertEquals(result.getSequence().getMessageNumber(),2);
            assertEquals(result.getRmNamespace(), MercuryConstants.RM_1_0_NAMESPACE);
            assertEquals(result.getSoapNamesapce(), SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }

    public void testRMApplicationMessageWithAcknowledgment(){
        SOAPFactory soapFactory = OMAbstractFactory.getSOAP11Factory();
        SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();
        RMApplicationMessage rmApplicationMessage = new RMApplicationMessage(soapEnvelope);
        Sequence sequence = new Sequence();
        sequence.setSequenceID("testID");
        sequence.setLastMessage(true);
        sequence.setMessageNumber(2);
        rmApplicationMessage.setSequence(sequence);

        SequenceAcknowledgment sequenceAcknowledgment = new SequenceAcknowledgment("testID");
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(7, 5));
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(4, 1));
        rmApplicationMessage.setSequenceAcknowledgment(sequenceAcknowledgment);

        try {
            soapEnvelope = rmApplicationMessage.toSOAPEnvelope();
            System.out.println("SOAP Envolope ==> " + soapEnvelope.toString());
            RMApplicationMessage result = RMApplicationMessage.fromSOAPEnvelope(soapEnvelope);
            assertEquals(result.getSequence().getSequenceID(),"testID");
            assertEquals(result.getSequence().isLastMessage(),true);
            assertEquals(result.getSequence().getMessageNumber(),2);
            assertEquals(result.getRmNamespace(), MercuryConstants.RM_1_0_NAMESPACE);
            assertEquals(result.getSoapNamesapce(), SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }
}
