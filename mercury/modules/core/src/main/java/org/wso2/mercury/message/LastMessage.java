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

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.om.OMElement;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

import java.util.Iterator;

/**
 * used to send the last mesage to client
 */
public class LastMessage extends RMMessage {

    private Sequence sequence;
    private SequenceAcknowledgment sequenceAcknowledgment;

    public LastMessage(Sequence sequence) {
        this.sequence = sequence;
    }

    public LastMessage(String rmNamespace, String soapNamesapce) {
        super(rmNamespace, soapNamesapce);
    }

    public SOAPEnvelope toSOAPEnvelope() throws RMMessageBuildingException {

        SOAPFactory soapFactory = getSoapFactory();
        SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();
        if (this.sequenceAcknowledgment != null){
            soapEnvelope.getHeader().addChild(this.sequenceAcknowledgment.toSOAPHeaderBlock());
        }
        soapEnvelope.getHeader().addChild(this.sequence.toSOAPHeaderBlock());
        return soapEnvelope;
    }

     public static LastMessage fromSOAPEnvelope(SOAPEnvelope soapEnvelope) throws RMMessageBuildingException {
        String soapNamespace = soapEnvelope.getNamespace().getNamespaceURI();
        Iterator childElements = soapEnvelope.getHeader().getChildElements();
        SOAPHeaderBlock headerBlock;
        LastMessage lastMessage = null;
        for (; childElements.hasNext();) {
            headerBlock = (SOAPHeaderBlock) childElements.next();
            if (headerBlock.getLocalName().equals(MercuryConstants.SEQUENCE)) {
                String rmNamespace = headerBlock.getNamespace().getNamespaceURI();
                lastMessage = new LastMessage(rmNamespace, soapNamespace);
                lastMessage.setSequence(Sequence.fromSOAPHeaderBlock(headerBlock));
            }
        }

        Iterator iter = soapEnvelope.getHeader().getChildrenWithLocalName(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT);
        for (; iter.hasNext();) {
            SOAPHeaderBlock sequenceAcknowledgmentHeaderBlock = (SOAPHeaderBlock) iter.next();
            if (sequenceAcknowledgmentHeaderBlock.getLocalName().equals(
                    MercuryConstants.SEQUENCE_ACKNOWLEDGMENT)) {
                SequenceAcknowledgment sequenceAcknowledgment =
                        SequenceAcknowledgment.fromSOAPHeaderBlock(sequenceAcknowledgmentHeaderBlock);
                lastMessage.setSequenceAcknowledgment(sequenceAcknowledgment);
                break;
            }
        }

        return lastMessage;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public SequenceAcknowledgment getSequenceAcknowledgment() {
        return sequenceAcknowledgment;
    }

    public void setSequenceAcknowledgment(SequenceAcknowledgment sequenceAcknowledgment) {
        this.sequenceAcknowledgment = sequenceAcknowledgment;
    }
}
