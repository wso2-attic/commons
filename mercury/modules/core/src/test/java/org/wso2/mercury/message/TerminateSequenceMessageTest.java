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
import org.apache.axiom.soap.SOAPEnvelope;
import org.wso2.mercury.exception.RMMessageBuildingException;

/**
 * used to serialize and deserialize the terminate sequence
 * message.
 */
public class TerminateSequenceMessageTest extends TestCase {

    public void testTerminateSequenceMessage(){
        TerminateSequenceMessage terminateSequenceMessage = new TerminateSequenceMessage("testID");
        try {
            SOAPEnvelope soapEnvelope = terminateSequenceMessage.toSOAPEnvelope();
            System.out.println("SOAP Envelope ==> " + soapEnvelope.toString());
            TerminateSequenceMessage result = TerminateSequenceMessage.fromSOAPEnvelpe(soapEnvelope);
            assertEquals(result.getIdentifier(),"testID");
        } catch (RMMessageBuildingException e) {
            fail();
        }

    }

    public void testTerminateSequenceMessageWithAcknowledgment() {
        TerminateSequenceMessage terminateSequenceMessage = new TerminateSequenceMessage("testID");

        SequenceAcknowledgment sequenceAcknowledgment = new SequenceAcknowledgment("testID");
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(7, 5));
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(4, 1));

        terminateSequenceMessage.setSequenceAcknowledgment(sequenceAcknowledgment);
        try {
            SOAPEnvelope soapEnvelope = terminateSequenceMessage.toSOAPEnvelope();
            System.out.println("SOAP Envelope ==> " + soapEnvelope.toString());
            TerminateSequenceMessage result = TerminateSequenceMessage.fromSOAPEnvelpe(soapEnvelope);
            assertEquals(result.getIdentifier(), "testID");
        } catch (RMMessageBuildingException e) {
            fail();
        }

    }
}
