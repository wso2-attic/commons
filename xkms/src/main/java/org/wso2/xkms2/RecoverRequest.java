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

public class RecoverRequest extends KRSSRequest {

    private RecoverKeyBinding recoverKeyBinding = null;

    public RecoverKeyBinding getRecoverKeyBinding() {
        return recoverKeyBinding;
    }

    public void setRecoverKeyBinding(RecoverKeyBinding recoverKeyBinding) {
        this.recoverKeyBinding = recoverKeyBinding;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement recoverRequestElem = factory
                .createOMElement(XKMS2Constants.Q_ELEM_RECOVER_REQUEST);
        super.serialize(factory, recoverRequestElem);

        if (recoverKeyBinding == null) {
            throw new XKMSException("RecoverKeyBinding is not found");
        }
        recoverRequestElem.addChild(recoverKeyBinding.serialize(factory));

        OMElement prototypeKeyBindingElement = recoverRequestElem
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_RECOVER_KEY_BINDING);
        String prototypeKeyBindingRefId = "#" + recoverKeyBinding.getId();

        Authentication authentication = getAuthentication();

        if (authentication.getKeyBindingAuthentication() == null) {
            Key key = authentication.getKeyBindingAuthenticationKey();

            if (key != null) {
                XMLSignature keyBindingAuthSignature;
                try {

                    IdResolver.registerElementById(
                            (Element) prototypeKeyBindingElement,
                            recoverKeyBinding.getId());

                    keyBindingAuthSignature = new XMLSignature(
                            ((Element) prototypeKeyBindingElement)
                                    .getOwnerDocument(), "",
                            XMLSignature.ALGO_ID_MAC_HMAC_SHA1,
                            Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
                    Transforms transforms = new Transforms(
                            ((Element) prototypeKeyBindingElement)
                                    .getOwnerDocument());
                    transforms
                            .addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
                    keyBindingAuthSignature.addDocument(
                            prototypeKeyBindingRefId, transforms,
                            MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1);
                    keyBindingAuthSignature.sign(key);
                    authentication
                            .setKeyBindingAuthentication(keyBindingAuthSignature);

                } catch (XMLSecurityException xse) {
                    throw new XKMSException(xse);
                }
            }
        }
        recoverRequestElem.addChild(authentication.serialize(factory));
        return recoverRequestElem;
    }
}
