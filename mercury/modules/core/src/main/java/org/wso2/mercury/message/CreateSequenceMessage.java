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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;
import org.wso2.mercury.security.SecurityToken;
import org.wso2.mercury.security.RMSecurityManager;

import java.util.Iterator;


public class CreateSequenceMessage extends RMMessage {

    // addressing namesapce used
    private String addressingNamespace;
    // acks to address used in the envelpe
    private String acksToAddress;
    // used to given an offer to response chanel
    private String offerIdentifier;

    private OMElement securityTokenReference;

    public CreateSequenceMessage() {
        super();
    }

    public CreateSequenceMessage(String rmNamespace, String soapNamesapce) {
        super(rmNamespace, soapNamesapce);
    }

    public SOAPEnvelope toSOAPEnvelope() throws RMMessageBuildingException {
        SOAPFactory soapFactory = getSoapFactory();
        SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();

        OMElement createSequence = soapFactory.createOMElement(MercuryConstants.CREATE_SEQUENCE,
                MercuryConstants.RM_1_0_NAMESPACE,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);

        AcksTo acksTo = new AcksTo(this.rmNamespace);
        acksTo.setEndpointAddress(acksToAddress);
        acksTo.setAddressingNamespace(this.addressingNamespace);
        createSequence.addChild(acksTo.toOM());

        if (offerIdentifier != null){
            Offer offer = new Offer(this.offerIdentifier);
            offer.setRmNamespace(this.rmNamespace);
            createSequence.addChild(offer.toOM());
        }

        if (securityTokenReference != null){
            createSequence.addChild(securityTokenReference);
        }
        soapEnvelope.getBody().addChild(createSequence);
        return soapEnvelope;
    }

    public static CreateSequenceMessage fromSOAPEnvelope(SOAPEnvelope soapEnvelope) throws RMMessageBuildingException {
        String soapNamespace = soapEnvelope.getNamespace().getNamespaceURI();
        OMElement createSequenceOMElement = soapEnvelope.getBody().getFirstElement();
        String rmNamespace = createSequenceOMElement.getNamespace().getNamespaceURI();

        CreateSequenceMessage createSequenceMessage = new CreateSequenceMessage(rmNamespace,soapNamespace);
        //TODO: do the validations
        OMElement omElement = null;
        for (Iterator iter = createSequenceOMElement.getChildElements();iter.hasNext();){
            omElement = (OMElement) iter.next();
            if (omElement.getLocalName().equals(MercuryConstants.ACKS_TO)) {
                AcksTo acksTo = AcksTo.fromOM(omElement);
                createSequenceMessage.setAddressingNamespace(acksTo.getAddressingNamespace());
                createSequenceMessage.setAcksToAddress(acksTo.getEndpointAddress());
            } else if (omElement.getLocalName().equals(MercuryConstants.OFFER)) {
                Offer offer = Offer.fromOM(omElement);
                createSequenceMessage.setOfferIdentifier(offer.getIdentifer());
            } else if (omElement.getLocalName().equals(MercuryConstants.SECURITY_TOKEN_REFERENCE)){
                createSequenceMessage.setSecurityTokenReference(omElement);
            }
        }

        return createSequenceMessage;
    }

    public String getAddressingNamespace() {
        return addressingNamespace;
    }

    public void setAddressingNamespace(String addressingNamespace) {
        this.addressingNamespace = addressingNamespace;
    }

    public String getAcksToAddress() {
        return acksToAddress;
    }

    public void setAcksToAddress(String acksToAddress) {
        this.acksToAddress = acksToAddress;
    }

    public String getOfferIdentifier() {
        return offerIdentifier;
    }

    public void setOfferIdentifier(String offerIdentifier) {
        this.offerIdentifier = offerIdentifier;
    }

    public OMElement getSecurityTokenReference() {
        return securityTokenReference;
    }

    public void setSecurityTokenReference(OMElement securityTokenReference) {
        this.securityTokenReference = securityTokenReference;
    }
}
