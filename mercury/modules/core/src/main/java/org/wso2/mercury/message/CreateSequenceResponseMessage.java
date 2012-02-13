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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.mercury.exception.RMMessageBuildingException;
import org.wso2.mercury.util.MercuryConstants;

import java.util.Iterator;

/**
 * this class used to serialize and deserilize the
 * create sequence response
 */
public class CreateSequenceResponseMessage extends RMMessage {

    private String identifier;
    private Accept accept;

    private static Log log = LogFactory.getLog(CreateSequenceResponseMessage.class);

    public CreateSequenceResponseMessage(String rmNamespace, String soapNamesapce) {
        super(rmNamespace, soapNamesapce);
    }

    public CreateSequenceResponseMessage() {
        super();
    }


    public SOAPEnvelope toSOAPEnvelope() throws RMMessageBuildingException {
        SOAPFactory soapFactory = getSoapFactory();
        SOAPEnvelope soapEnvelope = soapFactory.getDefaultEnvelope();
        OMElement createSequenceResponseElement = soapFactory.createOMElement(
                MercuryConstants.CREATE_SEQUENCE_RESPONSE,
                this.rmNamespace,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        OMElement identifierElement = soapFactory.createOMElement(MercuryConstants.IDENTIFIER,
                this.rmNamespace,
                MercuryConstants.RM_1_0_NAMESPACE_PREFIX);
        identifierElement.setText(this.identifier);
        createSequenceResponseElement.addChild(identifierElement);
        if (accept != null){
            createSequenceResponseElement.addChild(accept.toOM());
        }
        soapEnvelope.getBody().addChild(createSequenceResponseElement);
        return soapEnvelope;
    }

    public static CreateSequenceResponseMessage fromSOAPEnvolope(SOAPEnvelope soapEnvelope)
            throws RMMessageBuildingException {

        String soapNamespace = soapEnvelope.getNamespace().getNamespaceURI();
        OMElement createSequenceResponseOMElement = soapEnvelope.getBody().getFirstElement();
        String rmNamespace = createSequenceResponseOMElement.getNamespace().getNamespaceURI();
        CreateSequenceResponseMessage createSequenceResponseMessage =
                new CreateSequenceResponseMessage(rmNamespace, soapNamespace);
        Iterator responseChildren = createSequenceResponseOMElement.getChildElements();
        OMElement omElement = null;
        for (;responseChildren.hasNext();){
            omElement = (OMElement) responseChildren.next();
            if (omElement.getLocalName().equals(MercuryConstants.IDENTIFIER)){
                createSequenceResponseMessage.setIdentifier(omElement.getText());
            } else if (omElement.getLocalName().equals(MercuryConstants.ACCEPT)) {
                createSequenceResponseMessage.setAccept(Accept.fromOM(omElement));
            }
        }
        if (!createSequenceResponseMessage.isValid()){
            log.error("Invalid Create sequence response received");
            throw new RMMessageBuildingException("Invalid Create sequence response received");
        }
        return createSequenceResponseMessage;
    }

    public boolean isValid(){
        boolean isValid = false;
        if (this.identifier != null){
            isValid = true;
        }
        return isValid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Accept getAccept() {
        return accept;
    }

    public void setAccept(Accept accept) {
        this.accept = accept;
    }
}
