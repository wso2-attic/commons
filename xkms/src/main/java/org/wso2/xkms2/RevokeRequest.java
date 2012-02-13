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

import javax.activation.DataHandler;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;

public class RevokeRequest extends KRSSRequest {

    private RevokeKeyBinding revokeKeyBinding;

    private byte[] revocationCode;

    public RevokeKeyBinding getRevokeKeyBinding() {
        return revokeKeyBinding;
    }

    public void setRevokeKeyBinding(RevokeKeyBinding revokeKeyBinding) {
        this.revokeKeyBinding = revokeKeyBinding;
    }

    public byte[] getRevocationCode() {
        return revocationCode;
    }

    public void setRevocationCode(byte[] revocationCode) {
        this.revocationCode = revocationCode;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement revokeRequestElem = factory
                .createOMElement(XKMS2Constants.Q_ELEM_REVOKE_REQUEST);
        super.serialize(factory, revokeRequestElem);
        
        revokeRequestElem.addChild(revokeKeyBinding.serialize(factory));

        if (revokeRequestElem
                .getFirstChildWithName(XKMS2Constants.Q_ELEM_AUTHENTICATION) == null) {
            if (revocationCode != null) {
                OMElement revocationCodeElem = factory.createOMElement(
                        XKMS2Constants.Q_ELEM_REVOCATION_CODE,
                        revokeRequestElem);
                DataHandler dataHandler = new DataHandler(
                        new ByteArrayDataSource(revocationCode));
                
                OMText text = factory.createOMText(dataHandler, false);
                revocationCodeElem.addChild(text);
                
            } else {
                throw new XKMSException(
                        "RevocationCode or Authentication is not set");
            }
        }
        return revokeRequestElem;
    }

}
