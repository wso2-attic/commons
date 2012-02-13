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
 * used to serialize and deserilize Sequence Acknowledgment message
 */
public class SequenceAcknowledgmentMessageTest extends TestCase {

    public void testSequenceAcknowledgmentMessage(){

        SequenceAcknowledgment sequenceAcknowledgment = new SequenceAcknowledgment("testID");
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(7,5));
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(4,1));

        SequenceAcknowledgmentMessage sequenceAcknowledgmentMessage =
                new SequenceAcknowledgmentMessage(sequenceAcknowledgment);

        try {
            SOAPEnvelope soapEnvelope = sequenceAcknowledgmentMessage.toSOAPEnvelope();
            System.out.println("SOAP Envelope ==> " + soapEnvelope.toString());
            SequenceAcknowledgmentMessage result = SequenceAcknowledgmentMessage.fromSOAPEnvelope(soapEnvelope);
            assertEquals(result.getSequenceAcknowledgment().getIdentifier(),"testID");
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }
}
