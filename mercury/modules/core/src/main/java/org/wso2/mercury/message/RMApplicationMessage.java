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
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.AddressingConstants;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.exception.RMMessageBuildingException;

import java.util.Iterator;


public class RMApplicationMessage extends RMMessage {


    private SOAPEnvelope originalMessage;
    private boolean isSequenceHeaderAdded;

    private SequenceAcknowledgment sequenceAcknowledgment;
    private Sequence sequence;

    public RMApplicationMessage(SOAPEnvelope originalMessage) {
        super();
        this.soapNamesapce = originalMessage.getNamespace().getNamespaceURI();
        this.originalMessage = originalMessage;
        this.isSequenceHeaderAdded = false;
    }

    public RMApplicationMessage(String rmNamespace,
                                String soapNamesapce,
                                SOAPEnvelope originalMessage) {
        super(rmNamespace, soapNamesapce);
        this.originalMessage = originalMessage;
        this.isSequenceHeaderAdded = false;
    }

    public SOAPEnvelope toSOAPEnvelope() throws RMMessageBuildingException {

        if (!this.isSequenceHeaderAdded) {
            if (this.originalMessage.getHeader() == null){
                SOAPFactory soapFactory = getSoapFactory();
                soapFactory.createSOAPHeader(this.originalMessage);
            }
            this.originalMessage.getHeader().addChild(this.sequence.toSOAPHeaderBlock());
            this.isSequenceHeaderAdded = true;
        }

        // here we have to append the sequence acknowledgment header
        if (sequenceAcknowledgment != null) {
            // get the existing header block if exists and remove it.
            Iterator iter = this.originalMessage.getHeader().getChildrenWithLocalName(
                    MercuryConstants.SEQUENCE_ACKNOWLEDGMENT);
            for (; iter.hasNext();) {
                SOAPHeaderBlock sequenceAcknowledgmentHeaderBlock = (SOAPHeaderBlock) iter.next();
                if (sequenceAcknowledgmentHeaderBlock.getLocalName().equals(
                        MercuryConstants.SEQUENCE_ACKNOWLEDGMENT)) {
                    sequenceAcknowledgmentHeaderBlock.detach();
                    break;
                }
            }
            this.originalMessage.getHeader().addChild(sequenceAcknowledgment.toSOAPHeaderBlock());
        }

        // remove the existing relates to  headers otherwise addressing handler add an duplicate
        // relates to item.
        SOAPHeaderBlock soapHeaderBlock;
        for (Iterator iter = this.originalMessage.getHeader().getChildElements(); iter.hasNext();){
           soapHeaderBlock = (SOAPHeaderBlock) iter.next();
           if (soapHeaderBlock.getLocalName().equals(AddressingConstants.WSA_RELATES_TO)){
               soapHeaderBlock.detach();
           }
        }

        return this.originalMessage;
    }

    public static RMApplicationMessage fromSOAPEnvelope(SOAPEnvelope soapEnvelope) throws RMMessageBuildingException {
        String soapNamespace = soapEnvelope.getNamespace().getNamespaceURI();
        RMApplicationMessage rmApplicationMessage = null;
        if (soapEnvelope.getHeader() != null) {
            Iterator childElements = soapEnvelope.getHeader().getChildElements();
            SOAPHeaderBlock headerBlock;
            for (; childElements.hasNext();) {
                headerBlock = (SOAPHeaderBlock) childElements.next();
                if (headerBlock.getLocalName().equals(MercuryConstants.SEQUENCE)) {
                    String rmNamespace = headerBlock.getNamespace().getNamespaceURI();
                    rmApplicationMessage = new RMApplicationMessage(rmNamespace, soapNamespace, soapEnvelope);
                    rmApplicationMessage.setSequence(Sequence.fromSOAPHeaderBlock(headerBlock));
                }
            }

            Iterator iter = soapEnvelope.getHeader().getChildrenWithLocalName(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT);
            for (; iter.hasNext();) {
                SOAPHeaderBlock sequenceAcknowledgmentHeaderBlock = (SOAPHeaderBlock) iter.next();
                if (sequenceAcknowledgmentHeaderBlock.getLocalName().equals(
                        MercuryConstants.SEQUENCE_ACKNOWLEDGMENT)) {
                    SequenceAcknowledgment sequenceAcknowledgment =
                            SequenceAcknowledgment.fromSOAPHeaderBlock(sequenceAcknowledgmentHeaderBlock);
                    rmApplicationMessage.setSequenceAcknowledgment(sequenceAcknowledgment);
                    break;
                }
            }
        }
        return rmApplicationMessage;
    }

    public SOAPEnvelope getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(SOAPEnvelope originalMessage) {
        this.originalMessage = originalMessage;
    }

    public boolean isSequenceHeaderAdded() {
        return isSequenceHeaderAdded;
    }

    public void setSequenceHeaderAdded(boolean sequenceHeaderAdded) {
        isSequenceHeaderAdded = sequenceHeaderAdded;
    }

    public SequenceAcknowledgment getSequenceAcknowledgment() {
        return sequenceAcknowledgment;
    }

    public void setSequenceAcknowledgment(SequenceAcknowledgment sequenceAcknowledgment) {
        this.sequenceAcknowledgment = sequenceAcknowledgment;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }
}
