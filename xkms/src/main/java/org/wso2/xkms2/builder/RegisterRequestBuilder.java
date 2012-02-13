/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
package org.wso2.xkms2.builder;

import org.apache.axiom.om.OMElement;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.w3c.dom.Element;
import org.wso2.xkms2.PrototypeKeyBinding;
import org.wso2.xkms2.RegisterRequest;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.XKMSElement;
import org.wso2.xkms2.XKMSException;

public class RegisterRequestBuilder extends KRSSRequestBuilder implements
        ElementBuilder {

    public static final RegisterRequestBuilder INSTANCE = new RegisterRequestBuilder();
    
    private RegisterRequest registerRequest;
    
    private RegisterRequestBuilder() {
    }

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        registerRequest = new RegisterRequest();
        super.buildElement(element, registerRequest);

        OMElement prototypeKeyBindingElem = element
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_PROTOTYPE_KEY_BINDING);
        if (prototypeKeyBindingElem != null) {
            processPrototypeKeyBinding(prototypeKeyBindingElem);
        }

        OMElement proofOfPossessionElem = element
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_PROOF_OF_POSSESSION);
        if (proofOfPossessionElem != null) {
            processProofOfPossession(proofOfPossessionElem);
        }

        return registerRequest;
    }

    private void processPrototypeKeyBinding(OMElement prototypeKeyBindingElem)
            throws XKMSException {
        PrototypeKeyBinding prototypeKeyBinding = (PrototypeKeyBinding) PrototypeKeyBindingBuilder.INSTANCE
                .buildElement(prototypeKeyBindingElem);
        registerRequest.setPrototypeKeyBinding(prototypeKeyBinding);

    }

    private void processProofOfPossession(OMElement proofOfPossessionElem)
            throws XKMSException {
        Element sigElement = (Element) proofOfPossessionElem
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_XML_SIG);
        try {
            XMLSignature signature = new XMLSignature(sigElement, "");
            registerRequest.setProofOfPossesion(signature);

        } catch (XMLSignatureException e) {
            throw new XKMSException(e);
        } catch (XMLSecurityException e) {
            throw new XKMSException(e);
        }
    }

}
