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
import org.apache.axiom.om.util.Base64;
import org.wso2.xkms2.RevokeKeyBinding;
import org.wso2.xkms2.RevokeRequest;
import org.wso2.xkms2.XKMS2Constants;
import org.wso2.xkms2.XKMSElement;
import org.wso2.xkms2.XKMSException;

public class RevokeRequestBuilder extends KRSSRequestBuilder {

    public static final RevokeRequestBuilder INSTANCE = new RevokeRequestBuilder();

    private RevokeRequestBuilder() {
    }

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        RevokeRequest revokeRequest = new RevokeRequest();
        super.buildElement(element, revokeRequest);

        OMElement revokeKeyBindingElement = element
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_REVOKE_KEY_BINDING);

        if (revokeKeyBindingElement == null) {
            throw new XKMSException("RevokeKeyBinding element not found");
        }
        revokeRequest
                .setRevokeKeyBinding((RevokeKeyBinding) RevokeKeyBindingBuilder.INSTANCE
                        .buildElement(revokeKeyBindingElement));

        if (revokeRequest.getAuthentication() == null) {
            OMElement revocationCodeElem = element
                    .getFirstChildWithName(XKMS2Constants.Q_ELEM_REVOCATION_CODE);
            if (revocationCodeElem != null) {
                revokeRequest.setRevocationCode(Base64.decode(revocationCodeElem
                        .getText()));
            } else {
                throw new XKMSException(
                        "Neither Authentication nor RevocationCode found");
            }
        }

        return revokeRequest;
    }

}
