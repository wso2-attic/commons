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
package org.wso2.xkms2;

import java.security.Key;
import java.security.cert.X509Certificate;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.w3c.dom.Element;
import org.wso2.xkms2.util.XKMSUtil;

public class RegisterRequest extends KRSSRequest {

    private PrototypeKeyBinding prototypeKeyBinding;

    private XMLSignature proofOfPossesion;

    private Key popKey;

    public void setProofOfPossesion(XMLSignature proofOfPossession) {
        this.proofOfPossesion = proofOfPossession;
    }

    public void setProofOfPocessionKey(Key popKey) {
        this.popKey = popKey;
    }

    public Key getProofOfPocessionKey() {
        return popKey;
    }

    public XMLSignature getProofOfPossession() {
        return proofOfPossesion;
    }

    public void setPrototypeKeyBinding(PrototypeKeyBinding prototypeKeyBinding) {
        this.prototypeKeyBinding = prototypeKeyBinding;
    }

    public PrototypeKeyBinding getPrototypeKeyBinding() {
        return prototypeKeyBinding;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {

        OMElement regRequestElement = factory
                .createOMElement(XKMS2Constants.Q_ELEM_REGISTER_REQUEST);
        regRequestElement.declareNamespace(
                "http://www.w3.org/2000/09/xmldsig#", "ds");

        super.serialize(factory, regRequestElement);

        if (prototypeKeyBinding == null) {
            throw new XKMSException("PrototypeKeyBinding element is null");
        }
        regRequestElement.addChild(prototypeKeyBinding.serialize(factory));

        OMElement prototypeKeyBindingElement = regRequestElement
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_PROTOTYPE_KEY_BINDING);

        Authentication authentication = getAuthentication();

        if (authentication.getKeyBindingAuthentication() == null) {
            Key key = authentication.getKeyBindingAuthenticationKey();

            if (key != null) {
                XMLSignature keyBindingAuthSignature = XKMSUtil.sign(key,
                        (Element) prototypeKeyBindingElement);
                authentication
                        .setKeyBindingAuthentication(keyBindingAuthSignature);
            }
        }

        regRequestElement.addChild(authentication.serialize(factory));

        /*
         * If the proofOfPossession is not set, we see whether the key is set to
         * generate the 'Proof Of Possession'. If so we generate it on the fly.
         */
        if (proofOfPossesion == null) {

            if (popKey != null) {
                proofOfPossesion = XKMSUtil.sign(popKey,
                        (Element) prototypeKeyBindingElement);
            }
        }

        if (proofOfPossesion != null) {
            OMElement proofOfPossessionElement = factory.createOMElement(
                    XKMS2Constants.Q_ELEM_PROOF_OF_POSSESSION,
                    regRequestElement);
            XKMSUtil.appendChild(proofOfPossesion.getElement(),
                    proofOfPossessionElement);
        }

        Key signKey = getSignKey();
        X509Certificate signCert = getSignCert();
        /*
         * If the signKey is set we use that to sign the entier message.
         */
        if (signKey != null && signCert != null) {

            try {
                XKMSUtil.sign(getSignKey(), getSignCert(),
                        (Element) regRequestElement);

            } catch (XKMSException e) {
                throw new XKMSException(
                        "An exception was thrown when signing the message", e);
            }

        }

        return regRequestElement;
    }
}
