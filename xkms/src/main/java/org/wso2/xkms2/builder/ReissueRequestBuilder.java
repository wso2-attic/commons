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
import org.wso2.xkms2.ReissueKeyBinding;
import org.wso2.xkms2.ReissueRequest;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.XKMSElement;
import org.wso2.xkms2.XKMSException;

public class ReissueRequestBuilder extends KRSSRequestBuilder {

    public static final ReissueRequestBuilder INSTANCE = new ReissueRequestBuilder();

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        ReissueRequest reissueRequest = new ReissueRequest();
        super.buildElement(element, reissueRequest);

        OMElement reissueKeyBindingElement = element
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_REISSUE_KEY_BINDING);
        if (reissueKeyBindingElement == null) {
            throw new XKMSException("ReissueKeyBinding element not found");
        }

        reissueRequest
                .setReissueKeyBinding((ReissueKeyBinding) ReissueKeyBindingBuilder.INSTANCE
                        .buildElement(reissueKeyBindingElement));

        OMElement proofOfPossessionElem = element
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_PROOF_OF_POSSESSION);
        if (proofOfPossessionElem != null) {
            OMElement xmlSigElem = proofOfPossessionElem
                    .getFirstChildWithName(XKMS2Constants.Q_ELEM_XML_SIG);
            XMLSignature signature;
            try {
                signature = new XMLSignature((Element) xmlSigElem, "");
                reissueRequest.setProofOfPocession(signature);

            } catch (XMLSignatureException e) {
                throw new XKMSException(e);

            } catch (XMLSecurityException e) {
                throw new XKMSException(e);
            }
        }

        return reissueRequest;
    }

}
