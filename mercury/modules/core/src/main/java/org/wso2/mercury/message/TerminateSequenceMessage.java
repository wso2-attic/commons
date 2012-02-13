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


public class TerminateSequenceMessage extends RMMessage {

    private String identifier;

    private SequenceAcknowledgment sequenceAcknowledgment;

    public TerminateSequenceMessage() {
        super();
    }

    public TerminateSequenceMessage(String rmNamespace, String soapNamesapce) {
        super(rmNamespace, soapNamesapce);
    }

    public TerminateSequenceMessage(String identifier) {
        this.identifier = identifier;
    }

    public SOAPEnvelope toSOAPEnvelope() throws RMMessageBuildingException {
        SOAPFactory soapFactory = getSoapFactory();
        SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();
        OMElement terminateSequenceElement = soapFactory.createOMElement(MercuryConstants.TERMINATE_SEQUENCE,
                this.rmNamespace,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        OMElement identifierElement = soapFactory.createOMElement(MercuryConstants.IDENTIFIER,
                this.rmNamespace,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        identifierElement.setText(identifier);
        terminateSequenceElement.addChild(identifierElement);
        soapEnvelope.getBody().addChild(terminateSequenceElement);
        // this is an terminate message for annonymous inout operation
        if (sequenceAcknowledgment != null){
            soapEnvelope.getHeader().addChild(sequenceAcknowledgment.toSOAPHeaderBlock());
        }
        return soapEnvelope;
    }

    public static TerminateSequenceMessage fromSOAPEnvelpe(SOAPEnvelope soapEnvelope)
            throws RMMessageBuildingException {
        String soapNamespace = soapEnvelope.getNamespace().getNamespaceURI();
        OMElement terminateSequenceElement = soapEnvelope.getBody().getFirstElement();
        String rmNamespace = terminateSequenceElement.getNamespace().getNamespaceURI();
        TerminateSequenceMessage terminateSequenceMessage =
                new TerminateSequenceMessage(rmNamespace,soapNamespace);
        OMElement identifierElement = terminateSequenceElement.getFirstElement();
        terminateSequenceMessage.setIdentifier(identifierElement.getText());

        // parsing the sequence acknowledgment if exists.
        Iterator iter = soapEnvelope.getHeader().getChildrenWithLocalName(MercuryConstants.SEQUENCE_ACKNOWLEDGMENT);
        if (iter.hasNext()) {
            SOAPHeaderBlock sequenceAcknowledgmentHeaderBlock = (SOAPHeaderBlock) iter.next();
            SequenceAcknowledgment sequenceAcknowledgment =
                    SequenceAcknowledgment.fromSOAPHeaderBlock(sequenceAcknowledgmentHeaderBlock);
            terminateSequenceMessage.setSequenceAcknowledgment(sequenceAcknowledgment);
        }
        return terminateSequenceMessage;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public SequenceAcknowledgment getSequenceAcknowledgment() {
        return sequenceAcknowledgment;
    }

    public void setSequenceAcknowledgment(SequenceAcknowledgment sequenceAcknowledgment) {
        this.sequenceAcknowledgment = sequenceAcknowledgment;
    }


}
