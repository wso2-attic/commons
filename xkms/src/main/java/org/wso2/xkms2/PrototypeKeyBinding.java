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

import java.util.Calendar;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.wso2.xkms2.util.XKMSUtil;

public class PrototypeKeyBinding extends KeyBindingAbstractType {

    private byte[] revocationCodeIdentifier;

    private ValidityInterval validityInterval;

    public void setRevocationCodeIdentifier(String passPhrase) {
        throw new UnsupportedOperationException();
    }
    
    public void setRevocationCodeIdentifier(byte[] revocationCodeIdentifier) {
        this.revocationCodeIdentifier = revocationCodeIdentifier;
    }

    public byte[] getRevocationCodeIdentifier() {
        return revocationCodeIdentifier;
    }

    public void setValidityInterval(ValidityInterval validityInterval) {
        this.validityInterval = validityInterval;
    }

    public void setValidityInterval(Calendar notBefore, Calendar notOnOrAfter) {
        this.validityInterval = new ValidityInterval(notBefore, notOnOrAfter);
    }

    public ValidityInterval getValidityInterval() {
        return validityInterval;
    }
    
    public void serialize(OMFactory factory, OMElement container) throws XKMSException {
        container.addChild(this.serialize(factory));
    }
    
    public OMElement serialize(OMFactory factory) throws XKMSException {
        OMElement keybindingElement = factory
                .createOMElement(XKMS2Constants.Q_ELEM_PROTOTYPE_KEY_BINDING);

        super.serialize(factory, keybindingElement);

        if (revocationCodeIdentifier != null) {
            OMElement revocationCodeIdentifierElement = factory
                    .createOMElement(
                            XKMS2Constants.ELEM_REVOCATION_CODE_IDENTIFIER,
                            keybindingElement);
            XKMSUtil.appendAsTest(revocationCodeIdentifier,
                    revocationCodeIdentifierElement);
        }

        if (validityInterval != null) {
            OMElement validityIntervalElement = factory.createOMElement(
                    XKMS2Constants.ELEM_VALIDITY_INTERVAL, keybindingElement);
            
            Calendar notBefore = validityInterval.getNotBefore();
            Calendar notOnOrAfter = validityInterval.getOnOrAfter();

            if (notBefore != null) {
                validityIntervalElement.addAttribute(
                        XKMS2Constants.ATTR_NOT_BEFORE, XKMSUtil
                                .getTimeDate(notBefore.getTime()), null);
            }

            if (notOnOrAfter != null) {
                validityIntervalElement.addAttribute(
                        XKMS2Constants.ATTR_NOT_ON_OR_AFTER, XKMSUtil
                                .getTimeDate(notOnOrAfter.getTime()), null);
            }
        }

        return keybindingElement;
    }
}
