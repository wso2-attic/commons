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


public class LastMessageTest extends TestCase {

    public void testLastMessage(){
        LastMessage lastMessage = new LastMessage(
                MercuryConstants.RM_1_0_NAMESPACE, SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        Sequence sequence = new Sequence();
        sequence.setLastMessage(true);
        sequence.setMessageNumber(3);
        sequence.setSequenceID("testID");
        lastMessage.setSequence(sequence);

        SequenceAcknowledgment sequenceAcknowledgment = new SequenceAcknowledgment();
        sequenceAcknowledgment.setIdentifier("test2ID");
        sequenceAcknowledgment.addAcknowledgmentRange(new AcknowledgmentRange(3,1));

        lastMessage.setSequenceAcknowledgment(sequenceAcknowledgment);

        try {
            SOAPEnvelope soapEnvelope = lastMessage.toSOAPEnvelope();
            System.out.println("SOAPEnvelop ==> " + soapEnvelope.toString());
            LastMessage result = LastMessage.fromSOAPEnvelope(soapEnvelope);
            assertEquals(result.getSequence().getSequenceID(), "testID");
            if(result.getSequenceAcknowledgment() != null){
            	assertEquals(result.getSequenceAcknowledgment().getIdentifier(),"test2ID");	
            }
            
        } catch (RMMessageBuildingException e) {
            fail();
        }
    }
}
