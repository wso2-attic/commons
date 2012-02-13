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
import org.apache.axiom.om.OMNamespace;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

import java.util.*;

/**
 * used to create and parse sequence Acknowledgment messages
 */
public class SequenceAcknowledgmentMessage extends RMMessage {

    private SequenceAcknowledgment sequenceAcknowledgment;

    public SequenceAcknowledgmentMessage() {
        super();
    }

    public SequenceAcknowledgmentMessage(SequenceAcknowledgment sequenceAcknowledgment) {
        this.sequenceAcknowledgment = sequenceAcknowledgment;
    }

    public SOAPEnvelope toSOAPEnvelope() throws RMMessageBuildingException {
        SOAPFactory soapFactory = getSoapFactory();
        SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();
        soapEnvelope.getHeader().addChild(sequenceAcknowledgment.toSOAPHeaderBlock());
        return soapEnvelope;
    }


    public static SequenceAcknowledgmentMessage fromSOAPEnvelope(SOAPEnvelope soapEnvelope)
            throws RMMessageBuildingException {
        String soapNamespace = soapEnvelope.getNamespace().getNamespaceURI();
        Iterator iter = soapEnvelope.getHeader().getChildrenWithLocalName(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT);
        SequenceAcknowledgmentMessage sequenceAcknowledgmentMessage = new SequenceAcknowledgmentMessage();
        sequenceAcknowledgmentMessage.setSoapNamesapce(soapNamespace);
        //TODO: validate this properly.
        for (; iter.hasNext();) {
            SOAPHeaderBlock sequenceAcknowledgmentHeaderBlock = (SOAPHeaderBlock) iter.next();
            if (sequenceAcknowledgmentHeaderBlock.getLocalName().equals(
                    MercuryConstants.SEQUENCE_ACKNOWLEDGMENT)) {
                String rmNamesapce = sequenceAcknowledgmentHeaderBlock.getNamespace().getNamespaceURI();
                sequenceAcknowledgmentMessage.setRmNamespace(rmNamesapce);
                SequenceAcknowledgment sequenceAcknowledgment =
                        SequenceAcknowledgment.fromSOAPHeaderBlock(sequenceAcknowledgmentHeaderBlock);
                sequenceAcknowledgmentMessage.setSequenceAcknowledgment(sequenceAcknowledgment);
                break;
            }
        }

        return sequenceAcknowledgmentMessage;
    }

    public SequenceAcknowledgment getSequenceAcknowledgment() {
        return sequenceAcknowledgment;
    }

    public void setSequenceAcknowledgment(SequenceAcknowledgment sequenceAcknowledgment) {
        this.sequenceAcknowledgment = sequenceAcknowledgment;
    }

}
