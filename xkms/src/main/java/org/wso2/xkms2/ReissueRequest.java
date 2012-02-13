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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.IdResolver;
import org.w3c.dom.Element;
import org.wso2.xkms2.util.XKMSUtil;

public class ReissueRequest extends KRSSRequest {

    private ReissueKeyBinding reissueKeyBinding;

    private XMLSignature proofOfPossession;

    private Key popKey;

    public XMLSignature getProofOfPocession() {
        return proofOfPossession;
    }

    public void setProofOfPocession(XMLSignature proofOfPocession) {
        this.proofOfPossession = proofOfPocession;
    }

    public void setProofOfPossessionKey(Key popKey) {
        this.popKey = popKey;
    }

    public Key getProofOfPossessionKey() {
        return popKey;
    }

    public ReissueKeyBinding getReissueKeyBinding() {
        return reissueKeyBinding;
    }

    public void setReissueKeyBinding(ReissueKeyBinding reissueKeyBinding) {
        this.reissueKeyBinding = reissueKeyBinding;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement reissueRequestElem = factory
                .createOMElement(XKMS2Constants.Q_ELEM_REISSUE_REQUEST);
        super.serialize(factory, reissueRequestElem);

        if (reissueKeyBinding == null) {
            throw new XKMSException("ReissueKeyBinding not found");
        }
        //
        reissueRequestElem.addChild(reissueKeyBinding.serialize(factory));

        OMElement reissueKeyBindingElement = reissueRequestElem
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_REISSUE_KEY_BINDING);
        Authentication authentication = getAuthentication();

        if (authentication.getKeyBindingAuthentication() == null) {
            Key key = authentication.getKeyBindingAuthenticationKey();

            if (key != null) {
                XMLSignature keyBindingAuthSignature = XKMSUtil.sign(key,
                        (Element) reissueKeyBindingElement);
                authentication
                        .setKeyBindingAuthentication(keyBindingAuthSignature);
            }
        }

        reissueRequestElem.addChild(authentication.serialize(factory));
        
        if (proofOfPossession == null) {
            if (popKey != null) {
                proofOfPossession = XKMSUtil.sign(popKey,
                        (Element) reissueKeyBindingElement);
            }
        }

        if (proofOfPossession != null) {
            OMElement proofOfPossessionElement = factory.createOMElement(
                    XKMS2Constants.Q_ELEM_PROOF_OF_POSSESSION, reissueRequestElem);
            
            XKMSUtil.appendChild(proofOfPossession.getElement(),
                    proofOfPossessionElement);
        }

        return reissueRequestElem;
    }
}
