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
import org.apache.xml.security.signature.XMLSignature;
import org.wso2.xkms2.util.XKMSUtil;

public class Authentication {

    private XMLSignature keyBindingAuthenticationSig;

    private byte[] nonBoundAuthenticationValue;

    private String nonBoundAuthenticationProtocol;

    private String keyBindingAuthenticationKeyName;

    private Key keyBindingAuthenticationKey;

    public void setKeyBindingAuthentication(
            XMLSignature keyBindingAuthenticationSig) {
        this.keyBindingAuthenticationSig = keyBindingAuthenticationSig;
    }

    public XMLSignature getKeyBindingAuthentication() {
        return keyBindingAuthenticationSig;
    }

    public void setNonBoundAuthenticationValue(
            byte[] nonBoundAuthenticationValue) {
        this.nonBoundAuthenticationValue = nonBoundAuthenticationValue;
    }

    public byte[] getNonBoundAuthenticationValue() {
        return nonBoundAuthenticationValue;
    }

    public void setNonBoundAuthenticationProtocol(
            String nonBoundAuthenticationProtocol) {
        this.nonBoundAuthenticationProtocol = nonBoundAuthenticationProtocol;
    }

    public String getNonBoundAuthenticationProtocol() {
        return nonBoundAuthenticationProtocol;
    }

    public Key getKeyBindingAuthenticationKey() {
        return keyBindingAuthenticationKey;
    }

    public void setKeyBindingAuthenticationKey(Key keyBindingAuthenticationKey) {
        this.keyBindingAuthenticationKey = keyBindingAuthenticationKey;
    }

    public String getKeyBindingAuthenticationKeyName() {
        return keyBindingAuthenticationKeyName;
    }

    public void setKeyBindingAuthenticationKeyName(
            String keyBindingAuthenticationKeyName) {
        this.keyBindingAuthenticationKeyName = keyBindingAuthenticationKeyName;
    }

    public OMElement serialize(OMFactory factory) {
        OMElement authElement = factory
                .createOMElement(XKMS2Constants.Q_ELEM_AUTHENTICATION);

        if (keyBindingAuthenticationSig != null) {
            OMElement keyBindingAuthenticationElem = factory.createOMElement(
                    XKMS2Constants.Q_ELEM_KEY_BINDING_AUTH, authElement);
            XKMSUtil.appendChild(keyBindingAuthenticationSig.getElement(),
                    keyBindingAuthenticationElem);
            authElement.addChild(keyBindingAuthenticationElem);
        }

        return authElement;
    }
}
