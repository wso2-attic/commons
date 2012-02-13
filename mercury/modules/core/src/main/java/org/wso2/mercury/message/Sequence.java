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

import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

import java.util.Iterator;

/**
 * used to create the sequence headers for RMApplication messages
 * and last message.
 */
public class Sequence extends RMMessageHeader {

    private long messageNumber;
    private boolean isLastMessage;
    private String sequenceID;

    public SOAPHeaderBlock toSOAPHeaderBlock() throws RMMessageBuildingException {

        SOAPFactory soapFactory = getSoapFactory();
        OMNamespace rmNamespace = soapFactory.createOMNamespace(
                MercuryConstants.RM_1_0_NAMESPACE, MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        SOAPHeaderBlock sequenceHeaderBolck =
                soapFactory.createSOAPHeaderBlock(MercuryConstants.SEQUENCE, rmNamespace);
        OMElement identifierElement = soapFactory.createOMElement(
                MercuryConstants.IDENTIFIER,
                this.rmNamespace,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        identifierElement.setText(this.sequenceID);
        sequenceHeaderBolck.addChild(identifierElement);
        OMElement messageNumberElement = soapFactory.createOMElement(
                MercuryConstants.MESSAGE_NUMBER,
                this.rmNamespace,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        messageNumberElement.setText(String.valueOf(this.messageNumber));
        sequenceHeaderBolck.addChild(messageNumberElement);
        if (this.isLastMessage) {
            OMElement lastMessageElement = soapFactory.createOMElement(
                    MercuryConstants.LAST_MESSAGE,
                    this.rmNamespace,
                    MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
            sequenceHeaderBolck.addChild(lastMessageElement);
        }
        return sequenceHeaderBolck;
    }

    public static Sequence fromSOAPHeaderBlock(SOAPHeaderBlock soapHeaderBlock) {
        soapHeaderBlock.setProcessed();
        String soapNamespace = soapHeaderBlock.getQName().getNamespaceURI();
        Sequence sequence = new Sequence();
        sequence.setSoapNamesapce(soapNamespace);

        String rmNamesapce = soapHeaderBlock.getNamespace().getNamespaceURI();
        sequence.setRmNamespace(rmNamesapce);
        soapHeaderBlock.setProcessed();
        if (soapHeaderBlock.getNamespace().getNamespaceURI().equals(MercuryConstants.RM_1_0_NAMESPACE)) {
            // we have found the sequence header block.
            Iterator sequenceElementIter = soapHeaderBlock.getChildElements();
            OMElement childElement;
            for (; sequenceElementIter.hasNext();) {
                childElement = (OMElement) sequenceElementIter.next();
                if (childElement.getLocalName().equals(MercuryConstants.IDENTIFIER)) {
                    sequence.setSequenceID(childElement.getText());
                } else if (childElement.getLocalName().equals(MercuryConstants.MESSAGE_NUMBER)) {
                    sequence.setMessageNumber(Long.parseLong(childElement.getText()));
                } else if (childElement.getLocalName().equals(MercuryConstants.LAST_MESSAGE)) {
                    sequence.setLastMessage(true);
                }
            }
        }
        return sequence;
    }

    public long getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(long messageNumber) {
        this.messageNumber = messageNumber;
    }

    public boolean isLastMessage() {
        return isLastMessage;
    }

    public void setLastMessage(boolean lastMessage) {
        isLastMessage = lastMessage;
    }

    public String getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(String sequenceID) {
        this.sequenceID = sequenceID;
    }
}
